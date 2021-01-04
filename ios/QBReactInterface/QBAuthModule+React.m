//
//  QBAuthModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 17.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBAuthModule+React.h"

@implementation QBAuthModule (React)

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE(RNQBAuthModule)

RCT_EXTERN_METHOD(login:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(logout:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(createSession:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getSession:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
