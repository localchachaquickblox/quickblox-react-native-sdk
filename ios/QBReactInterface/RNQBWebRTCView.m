//
//  RNQBWebRTCView.m
//  RNQbReactnative
//
//  Created by Injoit 8/12/19.
//  Copyright © 2019 Quickblox. All rights reserved.
//

#import "RNQBWebRTCView.h"
#import "QBWebRTCVideoView.h"
#import "QBWebRTCModule.h"
#import "QBWebRTCConstants.h"
#import "QBWebRTCSessionController.h"
#import "QBWebRTCModule+React.h"

typedef NS_ENUM(NSInteger, QBRTCViewScaleType) {
  QBRTCViewScaleTypeFill,
  QBRTCViewScaleTypeFit,
  QBRTCViewScaleTypeAuto
};

@implementation RNQBWebRTCView

RCT_EXPORT_MODULE()

- (UIView *)view {
    QBWebRTCVideoView *view = [[QBWebRTCVideoView alloc] initWithFrame:CGRectMake(2,
                                                                                      2,
                                                                                      2,
                                                                                      2)];
    view.videoGravity = AVLayerVideoGravityResizeAspectFill;
    return view;
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

RCT_EXPORT_VIEW_PROPERTY(mirror, BOOL)


RCT_CUSTOM_VIEW_PROPERTY(scaleType, NSNumber *, QBWebRTCVideoView) {
    if (!json) { return; }
    NSNumber *scaleTypeNumber = [RCTConvert NSNumber:json];
    if (!scaleTypeNumber) { return; }
    if (scaleTypeNumber.integerValue > 2) {
        return;
    }
    QBRTCViewScaleType scaleType = scaleTypeNumber.integerValue;
    switch (scaleType) {
        case QBRTCViewScaleTypeFill: {
            view.videoGravity = AVLayerVideoGravityResizeAspectFill;
            break;
        }
        case QBRTCViewScaleTypeFit: {
            view.videoGravity = AVLayerVideoGravityResizeAspect;
            break;
        }
        case QBRTCViewScaleTypeAuto: {
            view.videoGravity = AVLayerVideoGravityResize;
            break;
        }
    };
}

RCT_EXPORT_METHOD(play:(nonnull NSNumber*) reactTag
                  useriD:(nonnull NSNumber*)userId
                  sessionId:(nonnull NSString *)sessionId) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager,
                                        NSDictionary<NSNumber *,
                                        UIView *> *viewRegistry) {
        QBWebRTCVideoView *view = (QBWebRTCVideoView *)viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[QBWebRTCVideoView class]]) {
            RCTLogError(@"Cannot find RNQBWebRTCView with tag #%@", reactTag);
            return;
        }
        if (!userId) { return; }
        if (!sessionId.length) { return; }
        QBWebRTCModule *module =
        [self.bridge moduleForName:QBWebRTCModule.moduleName];
        QBWebRTCSessionController *sessionController = module.cache[sessionId];
        if (!sessionController.session || !sessionController.currentUserId) {
            RCTLogError(@"Cannot find QBRTCSession with id #%@", sessionId);
            return;
        }
        
        if ([userId isEqual:sessionController.currentUserId] && !view.videoLayer) {
            // Setup local stream
            QBRTCCameraCapture *videoCapture = sessionController.videoCapture;
            videoCapture.previewLayer.videoGravity = view.videoGravity;
            view.videoLayer = videoCapture.previewLayer;
            if (!sessionController.videoCapture.hasStarted && !sessionController.videoCapture.isRunning) {
                [sessionController.videoCapture startSession:nil];
            }
        } else {
            // Setup remote stream
            QBRTCVideoTrack *videoTrack = [sessionController.session remoteVideoTrackWithUserID:userId];
            view.videoTrack = videoTrack;
        }
        view.transform = CGAffineTransformMakeScale(view.mirror ? -1.0 : 1.0, 1.0);
    }];
}

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

@end
