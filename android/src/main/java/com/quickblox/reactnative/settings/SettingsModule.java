
package com.quickblox.reactnative.settings;

import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.connections.tcp.QBTcpChatConnectionFabric;
import com.quickblox.chat.connections.tcp.QBTcpConfigurationBuilder;
import com.quickblox.core.QBHttpConnectionConfig;
import com.quickblox.core.ServiceZone;
import com.quickblox.reactnative.RNQbReactnativePackage;

import javax.annotation.Nonnull;

public class SettingsModule extends ReactContextBaseJavaModule {
    private static final int HTTP_TIMEOUT_IN_SECONDS = 40 * 1000;

    private static final String MODULE_NAME = "RNQBSettingsModule";
    private ReactContext reactContext;

    private RNQbReactnativePackage.ModuleEvents moduleEvents;

    public SettingsModule(@Nonnull ReactApplicationContext reactContext, RNQbReactnativePackage.ModuleEvents moduleEvents) {
        super(reactContext);
        this.reactContext = reactContext;
        this.moduleEvents = moduleEvents;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void init(ReadableMap data, Promise promise) {
        String applicationID = data != null && data.hasKey("appId") ? data.getString("appId") : null;
        String authKey = data != null && data.hasKey("authKey") ? data.getString("authKey") : null;
        String authSecret = data != null && data.hasKey("authSecret") ? data.getString("authSecret") : null;
        String accountKey = data != null && data.hasKey("accountKey") ? data.getString("accountKey") : null;
        String apiEndpoint = data != null && data.hasKey("apiEndpoint") ? data.getString("apiEndpoint") : null;
        String chatEndpoint = data != null && data.hasKey("chatEndpoint") ? data.getString("chatEndpoint") : null;

        if (TextUtils.isEmpty(applicationID) || TextUtils.isEmpty(authKey)
                || TextUtils.isEmpty(authSecret) || TextUtils.isEmpty(accountKey)) {
            promise.reject(new Exception("The credentials are wrong"));
        } else {
            QBSettings.getInstance().init(reactContext, applicationID, authKey, authSecret);
            QBSettings.getInstance().setAccountKey(accountKey);

            if (!TextUtils.isEmpty(apiEndpoint) && !TextUtils.isEmpty(chatEndpoint)) {
                QBSettings.getInstance().setEndpoints(apiEndpoint, chatEndpoint, ServiceZone.DEVELOPMENT);
                QBSettings.getInstance().setZone(ServiceZone.DEVELOPMENT);
            }

            QBHttpConnectionConfig.setConnectTimeout(HTTP_TIMEOUT_IN_SECONDS);
            QBHttpConnectionConfig.setReadTimeout(HTTP_TIMEOUT_IN_SECONDS);

            moduleEvents.onInitCredentials();

            QBTcpConfigurationBuilder configurationBuilder = new QBTcpConfigurationBuilder()
                    .setAutojoinEnabled(true)
                    .setSocketTimeout(0);

            QBChatService.setConnectionFabric(new QBTcpChatConnectionFabric(configurationBuilder));

            QBChatService.setDebugEnabled(true);
            QBChatService.setDefaultPacketReplyTimeout(30000);

            promise.resolve(null);
        }
    }

    @ReactMethod
    public void get(Promise promise) {
        String applicationID = QBSettings.getInstance().getApplicationId();
        String authKey = QBSettings.getInstance().getAuthorizationKey();
        String authSecret = QBSettings.getInstance().getAuthorizationSecret();
        String accountKey = QBSettings.getInstance().getAccountKey();
        String apiEndpoint = QBSettings.getInstance().getApiEndpoint();
        String chatEndpoint = QBSettings.getInstance().getChatEndpoint();
        String sdkVersion = com.quickblox.BuildConfig.VERSION_NAME;

        WritableMap data = new WritableNativeMap();
        data.putString("appId", applicationID);
        data.putString("authKey", authKey);
        data.putString("authSecret", authSecret);
        data.putString("accountKey", accountKey);
        data.putString("apiEndpoint", apiEndpoint);
        data.putString("chatEndpoint", chatEndpoint);
        data.putString("sdkVersion", sdkVersion);

        promise.resolve(data);
    }

    @ReactMethod
    public void enableCarbons(final Promise promise) {
        try {
            QBChatService.getInstance().enableCarbons();
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void disableCarbons(final Promise promise) {
        try {
            QBChatService.getInstance().disableCarbons();
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void initStreamManagement(ReadableMap data, final Promise promise) {
        boolean autoReconnect = data != null && data.hasKey("autoReconnect") && data.getBoolean("autoReconnect");
        Integer messageTimeout = data != null && data.hasKey("messageTimeout") ? data.getInt("messageTimeout") : null;

        if (messageTimeout != null && messageTimeout > 0) {
            QBChatService qbChatService = QBChatService.getInstance();
            qbChatService.setUseStreamManagement(true);
            qbChatService.setUseStreamManagementResumption(autoReconnect);
            qbChatService.setPreferredResumptionTime(messageTimeout);
            promise.resolve(null);
        } else {
            promise.reject(new Exception("The parameter messageTimeout is required"));
        }
    }

    @ReactMethod
    public void enableAutoReconnect(ReadableMap data, final Promise promise) {
        boolean enabled = data != null && data.hasKey("enable") && data.getBoolean("enable");

        QBChatService.ConfigurationBuilder configurationBuilder = new QBChatService.ConfigurationBuilder();
        configurationBuilder.setReconnectionAllowed(enabled);
        QBChatService.setConfigurationBuilder(configurationBuilder);
        QBChatService.setDefaultPacketReplyTimeout(30000);
        promise.resolve(null);
    }
}