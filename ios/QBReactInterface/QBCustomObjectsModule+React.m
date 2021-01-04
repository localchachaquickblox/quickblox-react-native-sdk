//
//  QBCustomObjectsModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 20.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBCustomObjectsModule+React.h"
#import "QBCOCustomObject+QBSerializer.h"

@implementation QBCustomObjectsModule (React)

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
    return @{ @"PERMISSIONS_LEVEL": QBCOCustomObject.permissionsLevel,
              @"OBJECTS_SEARCH_OPERATOR": QBCOCustomObject.searchOperators,
              @"OBJECTS_UPDATE_OPERATOR": QBCOCustomObject.updateOperators,
              @"PULL_FILTER": QBCOCustomObject.pullFilters };
}

RCT_EXPORT_MODULE(RNQBCustomObjectsModule)

RCT_EXTERN_METHOD(create:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getByIds:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(get:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(update:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(remove:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
