//
//  QBWebRTCModule+React.m
//  qb_ios_react_sdk
//
//  Created by Injoit on 20.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCModule+React.h"
#import "QBWebRTCConstants.h"

@implementation RCTConvert (QBRTCConferenceType)
RCT_ENUM_CONVERTER(QBRTCConferenceType, (QBWebRTCConstants.sessionType),
                   QBRTCConferenceTypeVideo, integerValue)
@end

@implementation RCTConvert (QBRTCSessionState)
RCT_ENUM_CONVERTER(QBRTCSessionState, (QBWebRTCConstants.sessionState),
                   QBRTCSessionStateNew, integerValue)
@end

@implementation RCTConvert (QBRTCPeerState)
RCT_ENUM_CONVERTER(QBRTCPeerState, (QBWebRTCConstants.connectionState),
                   QBRTCPeerStateNew, integerValue)
@end

@implementation RCTConvert (QB_AUDIO_OUTPUT)
RCT_ENUM_CONVERTER(QB_AUDIO_OUTPUT, (QBWebRTCConstants.audioOutputs),
                   QB_AUDIO_OUTPUT_EARSPEAKER, integerValue)
@end

@implementation QBWebRTCModule (React)

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport {
  NSMutableDictionary *constantsToExport = self.eventsConstantsToExport.mutableCopy;
  [constantsToExport addEntriesFromDictionary:@{ @"RTC_SESSION_TYPE": QBWebRTCConstants.sessionType,
                                                 @"RTC_SESSION_STATE": QBWebRTCConstants.sessionState,
                                                 @"RTC_PEER_CONNECTION_STATE": QBWebRTCConstants.connectionState,
                                                 @"AUDIO_OUTPUT": QBWebRTCConstants.audioOutputs }];
  return constantsToExport.copy;
}

RCT_EXPORT_MODULE(RNQBWebRTCModule)

RCT_EXTERN_METHOD(init:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(release:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(call:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(accept:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(reject:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(hangUp:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getSession:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(enableVideo:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(enableAudio:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(switchCamera:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(switchAudioOutput:(NSDictionary *)info
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)

@end
