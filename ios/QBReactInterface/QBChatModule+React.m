//
//  QBChatModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 20.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBChatModule+React.h"

typedef NS_ENUM(NSInteger, QB_DIALOG_TYPE) {
  QB_PUBLIC_CHAT = 1,
  QB_GROUP_CHAT = 2,
  QB_CHAT = 3,
};

@implementation RCTConvert (QB_DIALOG_TYPE)
RCT_ENUM_CONVERTER(QB_DIALOG_TYPE, (QBChatModule.dialogType),
                   QB_PUBLIC_CHAT, integerValue)
@end

@implementation QBChatModule (React)

+ (NSDictionary *)dialogType {
  static NSDictionary* _dialogType = nil;
  static dispatch_once_t dialogTypeToken;
  dispatch_once(&dialogTypeToken, ^{
    _dialogType = @{ @"PUBLIC_CHAT" : @(QB_PUBLIC_CHAT),
                     @"GROUP_CHAT" : @(QB_GROUP_CHAT),
                     @"CHAT" : @(QB_CHAT) };
  });
  return _dialogType;
}

+ (NSDictionary *)dialogsSort {
  static NSDictionary* _dialogsSort = nil;
  static dispatch_once_t dialogsSortToken;
  dispatch_once(&dialogsSortToken, ^{
    _dialogsSort = @{ FIELD: @{ klastMessageDateSentField: @"last_message_date_sent" } };
  });
  return _dialogsSort;
}

+ (NSDictionary *)messagesSort {
  static NSDictionary* _messagesSort = nil;
  static dispatch_once_t messagesSortToken;
  dispatch_once(&messagesSortToken, ^{
    _messagesSort = @{ FIELD: @{ kDateSentField: @"date_sent" } };
  });
  return _messagesSort;
}

+ (NSDictionary *)dialogsFilter {
  static NSDictionary* _dialogsFilter = nil;
  static dispatch_once_t dialogsFilterToken;
  dispatch_once(&dialogsFilterToken, ^{
    _dialogsFilter = @{ FIELD: @{ kIdField: @"_id",
                                  kTypeField: @"type",
                                  kNameField: @"name",
                                  klastMessageDateSentField: @"last_message_date_sent",
                                  kCreatedAtField: @"created_at",
                                  kUpdatedAtField: @"updated_at" },
                        OPERATOR: @{ kLT: @"lt",
                                     kLTE: @"lte",
                                     kGT: @"gt",
                                     kGTE: @"gte",
                                     kNE: @"ne",
                                     kIN: @"in",
                                     kNIN: @"nin",
                                     kALL: @"all",
                                     kCTN: @"ctn" }
    };
  });
  return _dialogsFilter;
}

+ (NSDictionary *)messagesFilter {
  static NSDictionary* _messagesFilter = nil;
  static dispatch_once_t messagesFilterToken;
  dispatch_once(&messagesFilterToken, ^{
    _messagesFilter = @{ FIELD: @{ kIdField: @"_id",
                                   kBodyField: @"message",
                                   kDateSentField: @"date_sent",
                                   kSenderIdField: @"sender_id",
                                   kRecipientIdField: @"recipient_id",
                                   kAttachmentTypeField: @"attachments.type",
                                   kUpdatedAtField: @"updated_at" },
                         OPERATOR: @{ kLT: @"lt",
                                      kLTE: @"lte",
                                      kGT: @"gt",
                                      kGTE: @"gte",
                                      kNE: @"ne",
                                      kIN: @"in",
                                      kNIN: @"nin",
                                      kOR: @"or",
                                      kCTN: @"ctn" }
    };
  });
  return _messagesFilter;
}

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
  NSMutableDictionary *constantsToExport = self.eventsConstantsToExport.mutableCopy;
  
  [constantsToExport addEntriesFromDictionary: @{ @"DIALOG_TYPE":  QBChatModule.dialogType}];
  
  [constantsToExport addEntriesFromDictionary:@{ @"DIALOGS_SORT": QBChatModule.dialogsSort }];
  [constantsToExport addEntriesFromDictionary:@{ @"MESSAGES_SORT": QBChatModule.messagesSort }];
  
  [constantsToExport addEntriesFromDictionary:@{ @"DIALOGS_FILTER": QBChatModule.dialogsFilter }];
  [constantsToExport addEntriesFromDictionary:@{ @"MESSAGES_FILTER": QBChatModule.messagesFilter }];
  
  return constantsToExport;
}

RCT_EXPORT_MODULE(RNQBChatModule)

//MARK: RNQBChatModuleConnection
RCT_EXTERN_METHOD(connect:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isConnected:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(disconnect:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

//MARK: RNQBChatModuleDialogs
RCT_EXTERN_METHOD(getDialogs:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getDialogsCount:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(createDialog:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(updateDialog:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(deleteDialog:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(joinDialog:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getOnlineUsers:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(leaveDialog:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

//MARK: RNQBChatModuleDialogs
RCT_EXTERN_METHOD(getDialogMessages:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(sendMessage:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(sendSystemMessage:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(markMessageRead:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(markMessageDelivered:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

//MARK: RNQBChatModulePing
RCT_EXTERN_METHOD(pingServer:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(pingUser:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

//MARK: RNQBChatModulePing
RCT_EXTERN_METHOD(sendIsTyping:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(sendStoppedTyping:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
