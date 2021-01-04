//
//  QBSettingsModule.m
//  crossplatform-sdk
//
//  Created by Injoit on 25.12.2019.
//  Copyright © 2019 Injoit LTD. All rights reserved.
//

#import "QBSettingsModule.h"
#import "QBSettingsConstants.h"

@implementation QBSettingsModule

- (void)init:(NSDictionary *)info
    resolver:(QBResolveBlock)resolve
    rejecter:(QBRejectBlock)reject {
    
    if ([NSError reject:reject
                   info:info
       withRequirements:@[ [Requirement requirementClass:NSString.class
                                                     key:SettingsKey.appId],
                           [Requirement requirementClass:NSString.class
                                                     key:SettingsKey.authKey],
                           [Requirement requirementClass:NSString.class
                                                     key:SettingsKey.authSecret],
                           [Requirement requirementClass:NSString.class
                                                     key:SettingsKey.accountKey] ]
         ]) {
        return;
    } else {
        NSString *appId = info[SettingsKey.appId];
        [QBSettings setApplicationID:(NSUInteger)appId.longLongValue];
        
        NSString *authKey = info[SettingsKey.authKey];
        [QBSettings setAuthKey:authKey];
        
        NSString *authSecret = info[SettingsKey.authSecret];
        [QBSettings setAuthSecret:authSecret];
        
        NSString *accountKey = info[SettingsKey.accountKey];
        [QBSettings setAccountKey:accountKey];
    }
    
    [QBSettings setKeepAliveInterval:15];
    
    NSString *apiEndpoint = info[SettingsKey.apiEndpoint];
    if (apiEndpoint.length) {
        [QBSettings setApiEndpoint:apiEndpoint];
    }
    
    NSString *chatEndpoint = info[SettingsKey.chatEndpoint];
    if (chatEndpoint.length) {
        [QBSettings setChatEndpoint:chatEndpoint];
    }
    
    NSNumber *chatEndpointPort = info[SettingsKey.chatEndpointPort];
    if (chatEndpointPort) {
        [QBSettings setChatEndpointPort:chatEndpointPort.unsignedIntegerValue];
    }
    
    if (resolve) {
        resolve(nil);
    }
}

- (void)get:(QBResolveBlock)resolve
   rejecter:(QBRejectBlock)reject {
    NSMutableDictionary *info = @{}.mutableCopy;
    info[SettingsKey.appId] = @(QBSettings.applicationID).stringValue;
    info[SettingsKey.authKey] = QBSettings.authKey;
    info[SettingsKey.authSecret] = QBSettings.authSecret;
    info[SettingsKey.accountKey] = QBSettings.accountKey;
    info[SettingsKey.apiEndpoint] = QBSettings.apiEndpoint;
    info[SettingsKey.chatEndpoint] = QBSettings.chatEndpoint;
    info[SettingsKey.sdkVersion] = QuickbloxFrameworkVersion;
    //    info[@"chatEndpointPort"] = @(QBSettings.chatEndpointPort);
    
    resolve(info.copy);
}

- (void)initStreamManagement:(NSDictionary *)info
                    resolver:(QBResolveBlock)resolve
                    rejecter:(QBRejectBlock)reject {
    NSNumber *enableNumber = info[SettingsKey.autoReconnect];
    NSNumber *messageTimeout = info[SettingsKey.messageTimeout];
    [QBSettings setKeepAliveInterval:15];
    if (enableNumber) {
        [QBSettings setAutoReconnectEnabled:enableNumber.boolValue];
    }
    if (messageTimeout) {
        QBSettings.streamManagementSendMessageTimeout = messageTimeout.unsignedIntegerValue;
    }
    if (resolve) {
        resolve(nil);
    }
}

- (void)enableAutoReconnect:(NSDictionary *)info
                   resolver:(QBResolveBlock)resolve
                   rejecter:(QBRejectBlock)reject {
    NSNumber *enableNumber = info[SettingsKey.enable];
    BOOL enable = enableNumber ? enableNumber.boolValue : NO;
    [QBSettings setAutoReconnectEnabled:enable];
    [QBSettings setKeepAliveInterval:15];
    if (resolve) {
        resolve(nil);
    }
}

- (void)enableCarbons:(QBResolveBlock)resolve
             rejecter:(QBRejectBlock)reject {
    QBSettings.carbonsEnabled = YES;
    if (resolve) {
        resolve(nil);
    }
}

- (void)disableCarbons:(QBResolveBlock)resolve
              rejecter:(QBRejectBlock)reject {
    QBSettings.carbonsEnabled = NO;
    if (resolve) {
        resolve(nil);
    }
}

@end
