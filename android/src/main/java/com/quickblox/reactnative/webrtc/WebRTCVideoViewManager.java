package com.quickblox.reactnative.webrtc;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.RendererCommon;

import java.util.Map;

import javax.annotation.Nonnull;

public class WebRTCVideoViewManager extends SimpleViewManager<QBRTCSurfaceView> {
    private static final String VIEW_NAME = "RNQBWebRTCView";

    private ReactApplicationContext reactContext;
    private static final int PLAY = 1;

    private WebRTCCallService webRTCCallService;

    @Nonnull
    @Override
    public String getName() {
        return VIEW_NAME;
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("play", PLAY);
    }

    public WebRTCVideoViewManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @Nonnull
    @Override
    protected QBRTCSurfaceView createViewInstance(@Nonnull ThemedReactContext reactContext) {
        return new QBRTCSurfaceView(reactContext);
    }

    public void receiveCommand(@Nonnull final QBRTCSurfaceView view, int command, final ReadableArray args) {
        switch (command) {
            case PLAY: {
                if (args != null && args.size() > 0) {
                    Integer userId = args.getInt(0);
                    String sessionId = args.getString(1);
                    play(sessionId, userId, view);
                }
            }
            default:
                break;
        }
    }

    @ReactProp(name = "scalingType", defaultInt = 1)
    public void scaleType(QBRTCSurfaceView view, Integer scaleType) {
        RendererCommon.ScalingType scalingType = WebRTCMapper.scalingTypeFromMap(scaleType);
        view.setScalingType(scalingType);
        view.requestLayout();
    }

    private void play(String sessionId, final Integer userId, final QBRTCSurfaceView view) {
        webRTCCallService.getVideoTrack(sessionId, userId, new WebRTCCallService.ServiceCallback<QBRTCVideoTrack>() {
            @Override
            public void onSuccess(QBRTCVideoTrack videoTrack) {
                if (videoTrack != null) {
                    videoTrack.addRenderer(view);
                    view.requestLayout();
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @ReactProp(name = "mirror", defaultBoolean = false)
    public void setMirror(QBRTCSurfaceView view, boolean mirror) {
        view.setMirror(mirror);
    }

    @ReactProp(name = "pause", defaultBoolean = false)
    public void pause(QBRTCSurfaceView view, boolean pause) {
        view.pauseVideo();
    }

    @ReactProp(name = "release", defaultBoolean = false)
    public void release(QBRTCSurfaceView view, boolean release) {
        view.release();
    }

    public void serviceReleased() {
        webRTCCallService = null;
    }

    public void serviceStarted(WebRTCCallService webRTCCallService) {
        this.webRTCCallService = webRTCCallService;
    }
}