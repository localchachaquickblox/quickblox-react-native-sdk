//
//  QBWebRTCVideoView.h
//  crossplatform-sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import <QuickbloxWebRTC/QuickbloxWebRTC.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBWebRTCVideoView : QBRTCRemoteVideoView

@property (nonatomic, assign) BOOL mirror;

@property (weak, nonatomic) AVCaptureVideoPreviewLayer *videoLayer;

@end

NS_ASSUME_NONNULL_END
