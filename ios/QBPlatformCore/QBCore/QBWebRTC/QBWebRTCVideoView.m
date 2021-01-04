//
//  QBWebRTCVideoView.m
//  crossplatform-sdk
//
//  Created by Injoit on 09.01.2020.
//  Copyright © 2020 Injoit LTD. All rights reserved.
//

#import "QBWebRTCVideoView.h"

@interface QBWebRTCVideoView ()

@property (strong, nonatomic) UIView *containerView;

@end

@implementation QBWebRTCVideoView

- (void)setVideoLayer:(AVCaptureVideoPreviewLayer *)videoLayer {
    _videoLayer = videoLayer;
    if (!self.containerView) {
        self.containerView = [[UIView alloc] initWithFrame:self.bounds];
        self.containerView.backgroundColor = [UIColor clearColor];
        [self insertSubview:self.containerView atIndex:0];
    }
    self.containerView.frame = self.bounds;
    [self.containerView.layer insertSublayer:videoLayer atIndex:0];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.containerView.frame = self.bounds;
    self.videoLayer.frame = self.bounds;
    [self updateOrientationIfNeeded];
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
    [super willMoveToSuperview:newSuperview];
    [self updateOrientationIfNeeded];
}

- (void)updateOrientationIfNeeded {
    
    AVCaptureConnection *previewLayerConnection = self.videoLayer.connection;
    UIInterfaceOrientation interfaceOrientation = [[UIApplication sharedApplication] statusBarOrientation];
    AVCaptureVideoOrientation videoOrientation = (AVCaptureVideoOrientation)interfaceOrientation;
    
    BOOL isVideoOrientationSupported = [previewLayerConnection isVideoOrientationSupported];
    if (isVideoOrientationSupported
        && previewLayerConnection.videoOrientation != videoOrientation) {
        [previewLayerConnection setVideoOrientation:videoOrientation];
    }
}

@end
