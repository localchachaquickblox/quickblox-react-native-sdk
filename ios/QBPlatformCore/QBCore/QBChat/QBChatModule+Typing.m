//
//  QBChatModule+Typing.m
//  crossplatform-sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Typing.h"

#import <objc/runtime.h>

static void *_qbtypingListeners;

@implementation QBChatModule (Typing)

- (NSMutableSet<QBDialogListener *> *)typingListeners {
    NSMutableSet<QBDialogListener *> *result = objc_getAssociatedObject(self,
                                                                        &_qbtypingListeners);
    if (result == nil) {
        result = [NSMutableSet set];
        objc_setAssociatedObject(self,
                                 &_qbtypingListeners,
                                 result,
                                 OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    return result;
}

- (void)sendIsTyping:(NSDictionary *)info
            resolver:(QBResolveBlock)resolve
            rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        [dialog sendUserIsTyping];
        resolve(nil);
    } rejecter:reject];
}

- (void)sendStoppedTyping:(NSDictionary *)info
                 resolver:(QBResolveBlock)resolve
                 rejecter:(QBRejectBlock)reject {
    [self dialogWithInfo:info
                 success:^(QBChatDialog *dialog) {
        [dialog sendUserStoppedTyping];
        resolve(nil);
    } rejecter:reject];
}

- (void)subscribeTyping:(QBChatDialog*)dialog {
    QBDialogListener *listner = [QBDialogListener new];
    listner.delegate = self;
    [listner subscribeWithDialog:dialog];
    [self.typingListeners addObject:listner];
}

- (void)unsubscribeTyping:(QBChatDialog *)dialog {
    QBDialogListener *listner = [QBDialogListener new];
    listner.dialog = dialog;
    [self.typingListeners removeObject:listner];
}

@end

//MARK: DialogListnerProtocol
@implementation QBChatModule(DialogListnerTyping)

- (void)chatDidReciveIsTypingWithUserID:(NSNumber *)userID dialogID:(NSString *)dialogID {
    [self postQBEventWithName:QBUserTypingEvent.isTyping
                         body:@{ QBChatKey.userId: userID,
                                 QBChatKey.dialogId: dialogID }];
}

- (void)chatDidReciveStopTypingWithUserID:(NSNumber *)userID dialogID:(NSString *)dialogID {
    [self postQBEventWithName:QBUserTypingEvent.stopTyping
                         body:@{ QBChatKey.userId: userID,
                                 QBChatKey.dialogId: dialogID }];
}

@end
