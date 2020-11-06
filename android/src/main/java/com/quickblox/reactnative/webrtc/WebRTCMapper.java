package com.quickblox.reactnative.webrtc;

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;

import org.webrtc.RendererCommon;

import java.util.List;
import java.util.Map;

public class WebRTCMapper {

    private WebRTCMapper() {
        //empty
    }

    static WritableMap qBRTCSessionToMap(QBRTCSession session) {
        WritableMap map = new WritableNativeMap();

        if (session != null && session.getSessionDescription() != null) {
            QBRTCSessionDescription description = session.getSessionDescription();

            if (!TextUtils.isEmpty(description.getSessionId())) {
                String id = description.getSessionId().toLowerCase();
                map.putString("id", id);
            }
            if (session.getConferenceType() != null) {
                QBRTCTypes.QBConferenceType conferenceType = session.getConferenceType();
                Integer type = WebRTCConstants.RTCSessionType.getValueByType(conferenceType);
                map.putInt("type", type);
            }
            if (session.getState() != null) {
                BaseSession.QBRTCSessionState sessionState = session.getState();
                Integer state = WebRTCConstants.RTCSessionState.getValueByState(sessionState);

                //check on null because the android send the status QB_RTC_SESSION_GOING_TO_CLOSE and IOS
                //doesn't need this status
                if (state != null) {
                    map.putInt("state", state);
                }
            }
            if (description.getCallerID() != null) {
                Integer initiatorId = description.getCallerID();
                map.putInt("initiatorId", initiatorId);
            }
            if (description.getOpponents() != null && description.getOpponents().size() > 0) {
                List<Integer> opponentsIdsList = description.getOpponents();
                WritableArray opponentsIdsArray = Arguments.fromList(opponentsIdsList);
                map.putArray("opponentsIds", opponentsIdsArray);
            }
        }

        return map;
    }

    static WritableMap userInfoToMap(QBRTCSession session) {
        WritableMap map = new WritableNativeMap();

        if (session != null && session.getSessionDescription() != null
                && session.getSessionDescription().getUserInfo() != null
                && session.getSessionDescription().getUserInfo().size() > 0) {
            Map<String, String> userInfo = session.getSessionDescription().getUserInfo();

            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                map.putString(key, value);
            }
        }

        return map;
    }

    static WritableMap userInfoToMap(Map<String, String> userInfo) {
        WritableMap userInfoMap = new WritableNativeMap();

        if (userInfo == null || userInfo.size() <= 0) {
            return userInfoMap;
        }

        for (Map.Entry<String, String> entry : userInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            userInfoMap.putString(key, value);
        }

        return userInfoMap;
    }

    static WritableMap qBRTCVideoTrackToMap(QBRTCVideoTrack videoTrack, Integer userId, String sessionId) {
        WritableMap map = new WritableNativeMap();

        if (userId != null) {
            map.putInt("userId", userId);
        }
        if (videoTrack != null) {
            map.putBoolean("enabled", videoTrack.enabled());
        }
        if (!TextUtils.isEmpty(sessionId)) {
            map.putString("sessionId", sessionId);
        }

        return map;
    }

    static RendererCommon.ScalingType scalingTypeFromMap(Integer scalingTypeValue) {

        RendererCommon.ScalingType scalingType = null;

        if (scalingTypeValue != null) {
            if (scalingTypeValue.equals(WebRTCConstants.RTCViewScaleType.AUTO.eventType)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_BALANCED;
            } else if (scalingTypeValue.equals(WebRTCConstants.RTCViewScaleType.FILL.eventType)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            } else if (scalingTypeValue.equals(WebRTCConstants.RTCViewScaleType.FIT.eventType)) {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
            } else {
                scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
            }
        }

        return scalingType;
    }

    static AppRTCAudioManager.AudioDevice appRTCAudioDeviceFromValue(int value) {
        AppRTCAudioManager.AudioDevice audioDevice = null;
        if (value == WebRTCConstants.AudioOutput.BLUETOOTH.value) {
            audioDevice = AppRTCAudioManager.AudioDevice.BLUETOOTH;
        }
        if (value == WebRTCConstants.AudioOutput.EARSPEAKER.value) {
            audioDevice = AppRTCAudioManager.AudioDevice.EARPIECE;
        }
        if (value == WebRTCConstants.AudioOutput.HEADPHONES.value) {
            audioDevice = AppRTCAudioManager.AudioDevice.WIRED_HEADSET;
        }
        if (value == WebRTCConstants.AudioOutput.LOUDSPEAKER.value) {
            audioDevice = AppRTCAudioManager.AudioDevice.SPEAKER_PHONE;
        }
        return audioDevice;
    }

    static String getDeviceNameFromValue(Integer value) {
        String name = null;
        if (value.equals(WebRTCConstants.AudioOutput.EARSPEAKER.value)) {
            name = WebRTCConstants.AudioOutput.EARSPEAKER.name;
        }
        if (value.equals(WebRTCConstants.AudioOutput.LOUDSPEAKER.value)) {
            name = WebRTCConstants.AudioOutput.LOUDSPEAKER.name;
        }
        if (value.equals(WebRTCConstants.AudioOutput.HEADPHONES.value)) {
            name = WebRTCConstants.AudioOutput.HEADPHONES.name;
        }
        if (value.equals(WebRTCConstants.AudioOutput.BLUETOOTH.value)) {
            name = WebRTCConstants.AudioOutput.BLUETOOTH.name;

        }
        return name;
    }
}