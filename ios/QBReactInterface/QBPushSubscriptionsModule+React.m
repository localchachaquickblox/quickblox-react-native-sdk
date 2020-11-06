//
//  QBPushSubscriptionsModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 17.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBPushSubscriptionsModule+React.h"

@implementation QBPushSubscriptionsModule (React)

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
  return @{ @"PUSH_CHANNEL":  @{ @"GCM": QBPushChannelKey.gcm,
                                 @"APNS": QBPushChannelKey.apns,
                                 @"APNS_VOIP": QBPushChannelKey.apnsVoip,
                                 @"EMAIL": QBPushChannelKey.email }};
}

RCT_EXPORT_MODULE(RNQBPushSubscriptionsModule)

RCT_EXTERN_METHOD(create:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(get:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(remove:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)


@end
