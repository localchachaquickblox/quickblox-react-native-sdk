//
//  QBNotificationEventsModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 17.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBNotificationEventsModule+React.h"
#import "QBMEvent+QBSerializer.h"

@implementation RCTConvert (PUSH_TYPE)
RCT_ENUM_CONVERTER(PUSH_TYPE, (QBMEvent.pushTypes),
                   APNS, integerValue)
@end

@implementation QBNotificationEventsModule (React)

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
  return @{ @"NOTIFICATION_EVENT_TYPE":  QBMEvent.eventTypes,
            @"NOTIFICATION_TYPE":  QBMEvent.notificationTypes,
            @"PUSH_TYPE":  QBMEvent.pushTypes,
            @"NOTIFICATION_EVENT_PERIOD":  QBMEvent.eventPeriods };
}

RCT_EXPORT_MODULE(RNQBNotificationEventsModule)

RCT_EXTERN_METHOD(create:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(get:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getById:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(update:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(remove:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
