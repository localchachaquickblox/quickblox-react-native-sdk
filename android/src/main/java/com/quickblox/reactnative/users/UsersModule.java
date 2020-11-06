
package com.quickblox.reactnative.users;

import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.GenericQueryRule;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class UsersModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "RNQBUsersModule";

    public UsersModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        //USERS SORT
        constants.put(UsersConstants.USERS_SORT, UsersConstants.UsersSort.getUsersSorts());

        //USERS_FILTER
        constants.put(UsersConstants.USERS_FILTER, UsersConstants.UsersFilter.getUsersFilters());

        return constants;
    }

    @ReactMethod
    public void create(ReadableMap data, final Promise promise) {
        String login = data != null && data.hasKey("login") ? data.getString("login") : null;
        String password = data != null && data.hasKey("password") ? data.getString("password") : null;
        String email = data != null && data.hasKey("email") ? data.getString("email") : null;
        Integer blobId = data != null && data.hasKey("blobId") ? data.getInt("blobId") : null;
        Integer externalUserId = data != null && data.hasKey("externalId") ? data.getInt("externalId") : null;
        Integer facebookId = data != null && data.hasKey("facebookId") ? data.getInt("facebookId") : null;
        Integer twitterId = data != null && data.hasKey("twitterId") ? data.getInt("twitterId") : null;
        String fullName = data != null && data.hasKey("fullName") ? data.getString("fullName") : null;
        String phone = data != null && data.hasKey("phone") ? data.getString("phone") : null;
        String webSite = data != null && data.hasKey("website") ? data.getString("website") : null;
        String customData = data != null && data.hasKey("customData") ? data.getString("customData") : null;
        ReadableArray tagsArray = data != null && data.hasKey("tags") ? data.getArray("tags") : null;

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            promise.reject(new Exception("Login and password are required parameters"));
            return;
        }

        QBUser qbUser = new QBUser();
        qbUser.setLogin(login);
        qbUser.setPassword(password);

        if (!TextUtils.isEmpty(email)) {
            qbUser.setEmail(email);
        }
        if (blobId != null && blobId != 0) {
            qbUser.setFileId(blobId);
        }
        if (externalUserId != null && externalUserId != 0) {
            qbUser.setExternalId(String.valueOf(externalUserId));
        }
        if (facebookId != null && facebookId != 0) {
            qbUser.setFacebookId(String.valueOf(facebookId));
        }
        if (twitterId != null && twitterId != 0) {
            qbUser.setTwitterId(String.valueOf(twitterId));
        }
        if (!TextUtils.isEmpty(fullName)) {
            qbUser.setFullName(fullName);
        }
        if (!TextUtils.isEmpty(phone)) {
            qbUser.setPhone(phone);
        }
        if (!TextUtils.isEmpty(webSite)) {
            qbUser.setWebsite(webSite);
        }
        if (!TextUtils.isEmpty(customData)) {
            qbUser.setCustomData(customData);
        }
        if (tagsArray != null && tagsArray.toArrayList().size() > 0) {
            StringifyArrayList<String> tagsList = new StringifyArrayList<>();
            for (int index = 0; index < tagsArray.size(); index++) {
                String tag = tagsArray.getString(index);
                tagsList.add(tag);
            }
            qbUser.setTags(tagsList);
        }

        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                WritableMap user = UsersMapper.qbUserToMap(qbUser);
                promise.resolve(user);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void getUsers(ReadableMap data, final Promise promise) {
        ReadableMap sortMap = data != null && data.hasKey("sort") ? data.getMap("sort") : null;
        ReadableMap filterMap = data != null && data.hasKey("filter") ? data.getMap("filter") : null;
        final int page = data != null && data.hasKey("page") ? data.getInt("page") : 1;
        final int perPage = data != null && data.hasKey("perPage") ? data.getInt("perPage") : 100;

        String filter = UsersMapper.filterMapToFilter(filterMap);
        ArrayList filterValue = UsersMapper.getFilterValueFromMap(filterMap);
        String sort = UsersMapper.sortMapToSort(sortMap);

        ArrayList<GenericQueryRule> queryRules = new ArrayList<>();

        if (!TextUtils.isEmpty(sort)) {
            queryRules.add(new GenericQueryRule("order", sort));
        }

        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setRules(queryRules);
        requestBuilder.setPage(page);
        requestBuilder.setPerPage(perPage);

        if (!TextUtils.isEmpty(filter) && filterValue != null && filterValue.size() > 0) {
            QBUsers.getUsersByFilter(filterValue, filter, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    WritableMap map = new WritableNativeMap();
                    WritableArray usersArray = new WritableNativeArray();
                    for (QBUser qbUser : qbUsers) {
                        WritableMap user = UsersMapper.qbUserToMap(qbUser);
                        usersArray.pushMap(user);
                    }

                    map.putArray("users", usersArray);
                    map.putInt("page", bundle.containsKey("current_page") ? bundle.getInt("current_page") : page);
                    map.putInt("perPage", bundle.containsKey("per_page") ? bundle.getInt("per_page") : perPage);
                    map.putInt("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);

                    promise.resolve(map);
                }

                @Override
                public void onError(QBResponseException e) {
                    promise.reject(e);
                }
            });
        } else {
            QBUsers.getUsers(requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    WritableMap map = new WritableNativeMap();
                    WritableArray usersArray = new WritableNativeArray();
                    for (QBUser qbUser : qbUsers) {
                        WritableMap user = UsersMapper.qbUserToMap(qbUser);
                        usersArray.pushMap(user);
                    }

                    map.putArray("users", usersArray);
                    map.putInt("page", bundle.containsKey("current_page") ? bundle.getInt("current_page") : page);
                    map.putInt("perPage", bundle.containsKey("per_page") ? bundle.getInt("per_page") : perPage);
                    map.putInt("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);

                    promise.resolve(map);
                }

                @Override
                public void onError(QBResponseException e) {
                    promise.reject(e);
                }
            });
        }
    }

    @ReactMethod
    public void update(ReadableMap data, final Promise promise) {
        String login = data != null && data.hasKey("login") ? data.getString("login") : null;
        String newPassword = data != null && data.hasKey("newPassword") ? data.getString("newPassword") : null;
        String password = data != null && data.hasKey("password") ? data.getString("password") : null;
        String email = data != null && data.hasKey("email") ? data.getString("email") : null;
        Integer blobId = data != null && data.hasKey("blobId") ? data.getInt("blobId") : null;
        Integer externalUserId = data != null && data.hasKey("externalId") ? data.getInt("externalId") : null;
        Integer facebookId = data != null && data.hasKey("facebookId") ? data.getInt("facebookId") : null;
        Integer twitterId = data != null && data.hasKey("twitterId") ? data.getInt("twitterId") : null;
        String fullName = data != null && data.hasKey("fullName") ? data.getString("fullName") : null;
        String phone = data != null && data.hasKey("phone") ? data.getString("phone") : null;
        String webSite = data != null && data.hasKey("website") ? data.getString("website") : null;
        String customData = data != null && data.hasKey("customData") ? data.getString("customData") : null;
        ReadableArray tagsArray = data != null && data.hasKey("tags") ? data.getArray("tags") : null;

        if (TextUtils.isEmpty(login)) {
            promise.reject(new Exception("The login is required parameter"));
            return;
        }

        QBUser qbUser = new QBUser();
        qbUser.setLogin(login);

        if (TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(password)) {
            promise.reject(new Exception("If field newPassword did set, the password should set as well."));
        }

        if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(newPassword)) {
            promise.reject(new Exception("If field password did set, the newPassword should set as well."));
        }

        if (!TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(password)) {
            qbUser.setPassword(newPassword);
            qbUser.setOldPassword(password);
        }

        if (!TextUtils.isEmpty(email)) {
            qbUser.setEmail(email);
        }
        if (blobId != null && blobId != 0) {
            qbUser.setFileId(blobId);
        }
        if (externalUserId != null && externalUserId != 0) {
            qbUser.setExternalId(String.valueOf(externalUserId));
        }
        if (facebookId != null && facebookId != 0) {
            qbUser.setFacebookId(String.valueOf(facebookId));
        }
        if (twitterId != null && twitterId != 0) {
            qbUser.setTwitterId(String.valueOf(twitterId));
        }
        if (!TextUtils.isEmpty(fullName)) {
            qbUser.setFullName(fullName);
        }
        if (!TextUtils.isEmpty(phone)) {
            qbUser.setPhone(phone);
        }
        if (!TextUtils.isEmpty(webSite)) {
            qbUser.setWebsite(webSite);
        }
        if (!TextUtils.isEmpty(customData)) {
            qbUser.setCustomData(customData);
        }
        if (tagsArray != null && tagsArray.toArrayList().size() > 0) {
            StringifyArrayList<String> tagsList = new StringifyArrayList<>();
            for (int index = 0; index < tagsArray.size(); index++) {
                String tag = tagsArray.getString(index);
                tagsList.add(tag);
            }
            qbUser.setTags(tagsList);
        }

        int userId = QBSessionManager.getInstance().getSessionParameters().getUserId();
        qbUser.setId(userId);

        QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                WritableMap user = UsersMapper.qbUserToMap(qbUser);
                promise.resolve(user);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }
}