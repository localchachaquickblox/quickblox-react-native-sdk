//
//  QBChatModule+Messages.m
//  crossplatform-sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Messages.h"
#import "QBChatMessage+QBSerializer.h"
#import "QBResponsePage+QBSerializer.h"
#import "QBChatAttachment+QBSerializer.h"

@implementation QBChatModule (Messages)

- (void)getDialogMessages:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    NSObject *dialogIdObject = info[QBMessageKey.dialogId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:dialogIdObject
              objectKey:QBMessageKey.dialogId]) {
        return;
    }
    NSString *dialogId = (NSString *)dialogIdObject;
    
    NSNumber *limit = info[QBPageKey.limit];
    NSNumber *skip = info[QBPageKey.skip];
    
    QBResponsePage *page =
    [QBResponsePage responsePageWithLimit:limit ? limit.integerValue : 100
                                     skip:skip ? skip.integerValue : 0];
    
    NSMutableDictionary *extendedRequest = @{}.mutableCopy;
    
    NSNumber *mark_as_read = info[@"markAsRead"];
    if (mark_as_read) {
        extendedRequest[@"mark_as_read"] = mark_as_read;
    }
    
    NSDictionary *sortInfo = info[SortKey.sort];
    if (sortInfo) {
        [extendedRequest addEntriesFromDictionary:[sortInfo toSortData]];
    }
    
    NSDictionary *filterInfo = info[FilterKey.filter];
    if (filterInfo) {
        [extendedRequest addEntriesFromDictionary:[filterInfo toFilterData]];
    }
    
    [QBRequest messagesWithDialogID:dialogId
                    extendedRequest:extendedRequest.copy
                            forPage:page
                       successBlock:^(QBResponse *response,
                                      NSArray *messages,
                                      QBResponsePage *responcePage) {
        NSError *error = nil;
        NSArray *results = [messages toQBResultArray:&error];
        if ([error reject:reject]) {
            return;
        }
        NSDictionary *resultPage = [responcePage toQBResultData:&error];
        if ([error reject:reject]) {
            return;
        }
        
        NSMutableDictionary *result = @{ QBChatKey.messages: results }.mutableCopy;
        [result addEntriesFromDictionary:resultPage];
        resolve(result.copy);
    } errorBlock:^(QBResponse *response) {
        [response reject:reject];
    }];
}

- (void)sendMessage:(NSDictionary *)info
           resolver:(QBResolveBlock)resolve
           rejecter:(QBRejectBlock)reject {
    QBChatMessage *message = [QBChatMessage message];
    message.text = info[QBMessageKey.body];
    message.attachments = [QBChatAttachment attachmentsWithInfo:info[QBMessageKey.attachments]];
    NSMutableDictionary *customParameters = @{}.mutableCopy;
    NSNumber *saveToHistory = info[QBMessageKey.saveToHistory];
    if (saveToHistory) {
        [customParameters setObject:saveToHistory
                             forKey:@"save_to_history"];
    }
    NSDictionary *custom_parameters = info[QBMessageKey.properties];
    if (customParameters.count) {
        [customParameters addEntriesFromDictionary:custom_parameters];
    }
    
    message.customParameters = customParameters;
    NSNumber *markable = info[QBMessageKey.markable];
    if (markable) {
        message.markable = markable.boolValue;
    }
    NSNumber *dateSentNumber = info[QBMessageKey.dateSent];
    if (dateSentNumber) {
        message.dateSent = [NSDate dateWithTimeIntervalSince1970:(dateSentNumber.doubleValue / 1000.0)];
    }
    
    __weak __typeof(self)weakSelf = self;
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
                     void(^sendMessage)(void) = ^() {
                         QBChatDialogType type = dialog.type;
                         if (type == QBChatDialogTypePrivate) {
                             message.recipientID = dialog.recipientID;
                         }
                         if (type != QBChatDialogTypePublicGroup) {
                             NSUInteger senderID = weakSelf.currentId;
                             message.deliveredIDs = @[@(senderID)];
                             message.readIDs = @[@(senderID)];
                         }
                         
                         [dialog sendMessage:message completionBlock:^(NSError * _Nullable error) {
                             NSDictionary *messageInfo = nil;
                             if (error == nil) {
                                 messageInfo = [message toQBResultData:&error];
                             }
                             
                             if (![error reject:reject] && resolve) {
                                 resolve(nil);
                                 if (type != QBChatDialogTypePrivate) {
                                     return;
                                 }
                    
                                 [weakSelf postQBEventWithName:QBMessageEvent.receivedNewMessage
                                                          body:messageInfo];
                             }
                         }];
                     };
                     if (dialog.type != QBChatDialogTypePrivate && dialog.isJoined == NO) {
                         [dialog joinWithCompletionBlock:^(NSError * _Nullable error) {
                             if (![error reject:reject] && resolve) {
                                 sendMessage();
                             }
                         }];
                     } else {
                         sendMessage();
                     }
                 } rejecter:reject];
}

- (void)sendSystemMessage:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    NSObject *propertiesObject = info[QBMessageKey.properties];
//    if ([NSError reject:reject
//           checkerClass:NSDictionary.class
//                 object:propertiesObject
//              objectKey:QBMessageKey.properties]) {
//        return;
//    }
    NSDictionary *properties = (NSDictionary *)propertiesObject;
    
    NSObject *recipientIdObject = info[QBMessageKey.recipientId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:recipientIdObject
              objectKey:QBMessageKey.recipientId]) {
        return;
    }
    NSNumber *recipientId = (NSNumber *)recipientIdObject;
    
    QBChatMessage *message = [QBChatMessage message];
    message.customParameters = properties.mutableCopy;
    message.recipientID = recipientId.unsignedIntegerValue;
    [[QBChat instance] sendSystemMessage:message
                              completion:^(NSError * _Nullable error) {
                                  if (![error reject:reject] && resolve) {
                                      resolve(nil);
                                  }
                              }];
}

- (void)markMessageRead:(NSDictionary *)info
               resolver:(QBResolveBlock)resolve
               rejecter:(QBRejectBlock)reject {
    NSDictionary *messageInfo = info[QBChatKey.message];
    NSObject *dialogIdObject = messageInfo[QBMessageKey.dialogId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:dialogIdObject
              objectKey:QBMessageKey.dialogId]) {
        return;
    }
    NSString *dialogId = (NSString *)dialogIdObject;
    
    NSObject *messageIdObject = messageInfo[QBMessageKey.id];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:messageIdObject
              objectKey:QBMessageKey.id]) {
        return;
    }
    NSString *messageId = (NSString *)messageIdObject;
    
    NSObject *senderIdObject = messageInfo[QBMessageKey.senderId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:senderIdObject
              objectKey:QBMessageKey.senderId]) {
        return;
    }
    NSNumber *senderId = (NSNumber *)senderIdObject;
    
    QBChatMessage *message = [QBChatMessage message];
    message.ID = messageId;
    message.dialogID = dialogId;
    message.senderID = senderId.unsignedIntegerValue;
    
    [[QBChat instance] readMessage:message
                        completion:^(NSError *error) {
                            if (![error reject:reject] && resolve) {
                                resolve(nil);
                            }
                        }];
}

- (void)markMessageDelivered:(NSDictionary *)info
                    resolver:(QBResolveBlock)resolve
                    rejecter:(QBRejectBlock)reject {
    NSDictionary *messageInfo = info[QBChatKey.message];
    NSObject *dialogIdObject = messageInfo[QBMessageKey.dialogId];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:dialogIdObject
              objectKey:QBMessageKey.dialogId]) {
        return;
    }
    NSString *dialogId = (NSString *)dialogIdObject;
    
    NSObject *messageIdObject = messageInfo[QBMessageKey.id];
    if ([NSError reject:reject
           checkerClass:NSString.class
                 object:messageIdObject
              objectKey:QBMessageKey.id]) {
        return;
    }
    NSString *messageId = (NSString *)messageIdObject;
    
    NSObject *senderIdObject = messageInfo[QBMessageKey.senderId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:senderIdObject
              objectKey:QBMessageKey.senderId]) {
        return;
    }
    NSNumber *senderId = (NSNumber *)senderIdObject;
    
    QBChatMessage *message = [QBChatMessage message];
    message.ID = messageId;
    message.dialogID = dialogId;
    message.senderID = senderId.unsignedIntegerValue;
    
    [[QBChat instance] markAsDelivered:message
                            completion:^(NSError * _Nullable error) {
                                if (![error reject:reject] && resolve) {
                                    resolve(nil);
                                }
                            }];
}

@end

//MARK: QBChatReceiveMessageProtocol
@implementation QBChatModule(MessageProtocol)

- (void)chatDidReceiveMessage:(QBChatMessage *)message {
    NSError *error = nil;
    NSDictionary *value = [message toQBResultData:&error];
    if (error || !message.ID) {
        return;
    }
    
    [self postQBEventWithName:QBMessageEvent.receivedNewMessage
                         body:value];
}

- (void)chatDidReceiveSystemMessage:(QBChatMessage *)message {
    NSError *error = nil;
    NSDictionary *value = [message toQBResultData:&error];
    if (error) {
        return;
    }
    [self postQBEventWithName:QBMessageEvent.receivedSystemMessage
    body:value];
}

- (void)chatRoomDidReceiveMessage:(QBChatMessage *)message
                     fromDialogID:(NSString *)dialogID {
    if (!dialogID.length) {
        return;
    }
    
    NSError *error = nil;
    NSDictionary *value = [message toQBResultData:&error];
    
    if (error) {
        return;
    }
    
    [self postQBEventWithName:QBMessageEvent.receivedNewMessage
                         body:value];
}

- (void)chatDidDeliverMessageWithID:(NSString *)messageID
                           dialogID:(NSString *)dialogID
                           toUserID:(NSUInteger)userID {
    if (!dialogID.length) {
        return;
    }
    
    NSMutableDictionary *payload = @{}.mutableCopy;
    payload[QBChatKey.messageId] = messageID;
    payload[QBChatKey.dialogId] = dialogID;
    payload[QBChatKey.userId] = @(userID);
    [self postQBEventWithName:QBMessageStatusEvent.delivered body:payload.copy ];
}

- (void)chatDidReadMessageWithID:(NSString *)messageID
                        dialogID:(NSString *)dialogID
                        readerID:(NSUInteger)readerID {
    if (!dialogID.length) {
        return;
    }
    
    NSMutableDictionary *payload = @{}.mutableCopy;
    payload[QBChatKey.messageId] = messageID;
    payload[QBChatKey.dialogId] = dialogID;
    payload[QBChatKey.userId] = @(readerID);
    [self postQBEventWithName:QBMessageStatusEvent.read body:payload.copy ];
}

@end

