//
//  QBUsersModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 17.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBUsersModule+React.h"

@implementation QBUsersModule (React)

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
  NSMutableDictionary *constantsToExport = @{}.mutableCopy;
  [constantsToExport addEntriesFromDictionary:@{ @"USERS_SORT": self.usersSortConstants }];
  [constantsToExport addEntriesFromDictionary:@{ @"USERS_FILTER": self.usersFilterConstants }];
  
  return constantsToExport;
}

//MARK: - Sort
- (NSDictionary<NSString *,NSDictionary *> *)usersSortConstants {
  return @{ TYPE: @{ kStringType: @"string",
                     kNumberType: @"number",
                     kDateType:   @"date" },
            FIELD: @{ kIdField:               @"id",
                      kFullNameField:         @"full_name",
                      kEmailField:            @"email",
                      kLoginField:            @"login",
                      kPhoneField:            @"phone",
                      kWebsiteField:          @"website",
                      kCreatedAtField:        @"created_at",
                      kUpdatedAtField:        @"updated_at",
                      kLastRequestAtField:    @"last_request_at",
                      kExternalUserIdField:   @"external_user_id",
                      kTwitterIdField:        @"twitter_id",
                      kFacebookIdField:       @"facebook_id" }
  };
}

//MARK: - Filters
- (NSDictionary<NSString *,NSDictionary *> *)usersFilterConstants {
  return @{ TYPE: @{ kStringType: @"string",
                     kNumberType: @"number",
                     kDateType:   @"date" },
            FIELD: @{ kIdField:               @"id",
                      kFullNameField:         @"full_name",
                      kEmailField:            @"email",
                      kLoginField:            @"login",
                      kPhoneField:            @"phone",
                      kWebsiteField:          @"website",
                      kCreatedAtField:        @"created_at",
                      kUpdatedAtField:        @"updated_at",
                      kLastRequestAtField:    @"last_request_at",
                      kExternalUserIdField:   @"external_user_id",
                      kTwitterIdField:        @"twitter_id",
                      kFacebookIdField:       @"facebook_id" },
            OPERATOR: @{ kGT:         @"gt",
                         kLT:         @"lt",
                         kGE:         @"ge",
                         kLE:         @"le",
                         kEQ:         @"eq",
                         kNE:         @"ne",
                         kBETWEEN:    @"between",
                         kIN:         @"in" }
  };
}


RCT_EXPORT_MODULE(RNQBUsersModule)

RCT_EXTERN_METHOD(create:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(update:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getUsers:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
