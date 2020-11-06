package com.quickblox.reactnative.auth;

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.quickblox.auth.session.QBSession;
import com.quickblox.reactnative.helpers.DateHelper;
import com.quickblox.users.model.QBUser;

import java.util.Date;

class AuthMapper {

    private AuthMapper() {
        //empty
    }

    static WritableMap qbUserToMap(QBUser user) {
        WritableMap map = new WritableNativeMap();

        if (user.getFileId() != null && user.getFileId() > 0) {
            map.putInt("blobId", user.getFileId());
        }
        if (!TextUtils.isEmpty(user.getCustomData())) {
            map.putString("customData", user.getCustomData());
        }
        if (!TextUtils.isEmpty(user.getEmail())) {
            map.putString("email", user.getEmail());
        }
        if (!TextUtils.isEmpty(user.getExternalId())) {
            map.putString("externalId", user.getExternalId());
        }
        if (!TextUtils.isEmpty(user.getFacebookId())) {
            map.putString("facebookId", user.getFacebookId());
        }
        if (!TextUtils.isEmpty(user.getFullName())) {
            map.putString("fullName", user.getFullName());
        }
        if (user.getId() != null && user.getId() != 0) {
            map.putInt("id", user.getId());
        }
        if (!TextUtils.isEmpty(user.getLogin())) {
            map.putString("login", user.getLogin());
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            map.putString("phone", user.getPhone());
        }
        if (user.getTags() != null && user.getTags().size() > 0) {
            WritableArray writableArray = Arguments.fromList(user.getTags());
            map.putArray("tags", writableArray);
        }
        if (!TextUtils.isEmpty(user.getTwitterId())) {
            map.putString("twitterId", user.getTwitterId());
        }
        if (!TextUtils.isEmpty(user.getWebsite())) {
            map.putString("website", user.getWebsite());
        }
        if (user.getLastRequestAt() != null) {
            Date date = user.getLastRequestAt();
            String lastRequestAt = DateHelper.convertDateToISO(date);
            map.putString("lastRequestAt", lastRequestAt);
        }

        return map;
    }

    static WritableMap qbSessionToMap(QBSession session) {
        WritableMap map = new WritableNativeMap();

        if (!TextUtils.isEmpty(session.getToken())) {
            map.putString("token", session.getToken());
        }
        if (session.getTokenExpirationDate() != null) {
            Date tokenExpirationDate = session.getTokenExpirationDate();
            String expirationDate = DateHelper.convertDateToISO(tokenExpirationDate);
            map.putString("expirationDate", expirationDate);
        }

        return map;
    }
}