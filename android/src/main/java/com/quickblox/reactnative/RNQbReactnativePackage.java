
package com.quickblox.reactnative;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.quickblox.reactnative.auth.AuthModule;
import com.quickblox.reactnative.chat.ChatModule;
import com.quickblox.reactnative.customobjects.CustomObjectsModule;
import com.quickblox.reactnative.file.FileModule;
import com.quickblox.reactnative.notificationevents.NotificationEventsModule;
import com.quickblox.reactnative.pushsubscriptions.PushSubscriptionsModule;
import com.quickblox.reactnative.settings.SettingsModule;
import com.quickblox.reactnative.users.UsersModule;
import com.quickblox.reactnative.webrtc.WebRTCCallService;
import com.quickblox.reactnative.webrtc.WebRTCModule;
import com.quickblox.reactnative.webrtc.WebRTCVideoViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class RNQbReactnativePackage implements ReactPackage {

    //Modules
    private SettingsModule settingsModule;
    private ChatModule chatModule;
    private UsersModule usersModule;
    private AuthModule authModule;
    private FileModule fileModule;
    private CustomObjectsModule customObjectsModule;
    private PushSubscriptionsModule pushSubscriptionsModule;
    private NotificationEventsModule notificationEventsModule;
    private WebRTCModule webRTCModule;

    //View Managers
    private WebRTCVideoViewManager videoViewManager;

    @Override
    public List<NativeModule> createNativeModules(@Nonnull ReactApplicationContext reactContext) {
        initModules(reactContext);

        List<NativeModule> modules = new ArrayList<>();
        modules.add(settingsModule);
        modules.add(chatModule);
        modules.add(usersModule);
        modules.add(authModule);
        modules.add(fileModule);
        modules.add(customObjectsModule);
        modules.add(pushSubscriptionsModule);
        modules.add(notificationEventsModule);
        modules.add(webRTCModule);

        return modules;
    }

    private void initModules(ReactApplicationContext reactContext) {
        settingsModule = new SettingsModule(reactContext, new ModuleSettingsEvents());
        chatModule = new ChatModule(reactContext, new ModuleSettingsEvents());
        usersModule = new UsersModule(reactContext);
        authModule = new AuthModule(reactContext);
        fileModule = new FileModule(reactContext);
        customObjectsModule = new CustomObjectsModule(reactContext);
        pushSubscriptionsModule = new PushSubscriptionsModule(reactContext);
        notificationEventsModule = new NotificationEventsModule(reactContext);
        webRTCModule = new WebRTCModule(reactContext, new ModuleSettingsEvents());
    }

    // Deprecated from RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(@Nonnull ReactApplicationContext reactContext) {
        initManagers(reactContext);

        List<ViewManager> managers = new ArrayList<>();
        managers.add(videoViewManager);

        return managers;
    }

    private void initManagers(ReactApplicationContext context) {
        videoViewManager = new WebRTCVideoViewManager(context);
    }

    private class ModuleSettingsEvents implements ModuleEvents {

        @Override
        public void onInitCredentials() {
            chatModule.onInitCredentials();
        }

        @Override
        public void onChatConnected() {
        }

        @Override
        public void onChatDisconnected() {
        }

        @Override
        public void onServiceStarted(WebRTCCallService webRTCCallService) {
            videoViewManager.serviceStarted(webRTCCallService);
        }

        @Override
        public void onServiceReleased() {
            videoViewManager.serviceReleased();
        }
    }

    public interface ModuleEvents {
        void onInitCredentials();

        void onChatConnected();

        void onChatDisconnected();

        void onServiceStarted(WebRTCCallService webRTCCallService);

        void onServiceReleased();
    }
}