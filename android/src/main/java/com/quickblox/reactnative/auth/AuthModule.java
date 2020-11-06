
package com.quickblox.reactnative.auth;

import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AuthModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "RNQBAuthModule";

    public AuthModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void login(ReadableMap data, final Promise promise) {
        String login = data != null && data.hasKey("login") ? data.getString("login") : null;
        String password = data != null && data.hasKey("password") ? data.getString("password") : null;

        QBUsers.signIn(login, password).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                WritableMap map = new WritableNativeMap();

                WritableMap user = AuthMapper.qbUserToMap(qbUser);
                map.putMap("user", user);

                QBSessionManager.getInstance().getActiveSession().setUserId(qbUser.getId());
                QBSession qbSession = QBSessionManager.getInstance().getActiveSession();
                WritableMap session = AuthMapper.qbSessionToMap(qbSession);
                map.putMap("session", session);

                promise.resolve(map);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void logout(final Promise promise) {
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                promise.resolve(aVoid);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void createSession(ReadableMap data, final Promise promise) {
        String token = data != null && data.hasKey("token") ? data.getString("token") : null;
        String tokenExpirationDate = data != null && data.hasKey("tokenExpirationDate") ? data.getString("tokenExpirationDate") : null;

        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenExpirationDate)) {
            promise.reject(new Exception("The parameters are wrong"));
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date expirationDate;

        try {
            expirationDate = simpleDateFormat.parse(tokenExpirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
            promise.reject(new Exception("Parameter tokenExpirationDate is wrong"));
            return;
        }

        QBSessionManager.getInstance().createActiveSession(token, expirationDate);
        QBSession qbSession = QBSessionManager.getInstance().getActiveSession();
        WritableMap session = AuthMapper.qbSessionToMap(qbSession);
        promise.resolve(session);
    }

    @ReactMethod
    public void getSession(final Promise promise) {
        QBSession qbSession = QBSessionManager.getInstance().getActiveSession();
        WritableMap session = AuthMapper.qbSessionToMap(qbSession);
        promise.resolve(session);
    }
}