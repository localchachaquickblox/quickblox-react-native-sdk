//
//  QBChatModule+Ping.m
//  crossplatform-sdk
//
//  Created by Injoit on 26.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBChatModule+Ping.h"

@implementation QBChatModule (Ping)

- (void)pingServer:(QBResolveBlock)resolve
          rejecter:(QBRejectBlock)reject {
    [[QBChat instance] pingServer:^(NSTimeInterval timeInterval,
                                    BOOL success) {
        if (timeInterval == 0 && success == NO) {
            [NSError reject:reject
                    message:NSLocalizedString(@"User is not connected to the chat.", nil)];
            return;
        } else if (success == NO) {
            [NSError reject:reject
                    message:NSLocalizedString(@"The operation timed out.", nil)];
            return;
        }
        
        if (resolve) {
            resolve(nil);
        }
    }];
}

- (void)pingUser:(NSDictionary *)info
        resolver:(QBResolveBlock)resolve
        rejecter:(QBRejectBlock)reject {
    NSObject *userIdObject = info[QBChatKey.userId];
    if ([NSError reject:reject
           checkerClass:NSNumber.class
                 object:userIdObject
              objectKey:QBChatKey.userId]) {
        return;
    }
    NSNumber *userId = (NSNumber *)userIdObject;
    [[QBChat instance]
     pingUserWithID:userId.unsignedIntegerValue
     completion:^(NSTimeInterval timeInterval,
                  BOOL success) {
         if (timeInterval == 0 && success == NO) {
             [NSError reject:reject
                     message:NSLocalizedString(@"User is not connected to the chat.", nil)];
             return;
         } else if (success == NO) {
             [NSError reject:reject
                     message:NSLocalizedString(@"The operation timed out.", nil)];
             return;
         }
         
         if (resolve) {
             resolve(nil);
         }
     }];
}

@end
