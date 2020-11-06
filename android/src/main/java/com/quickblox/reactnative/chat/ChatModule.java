package com.quickblox.reactnative.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
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
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBDialogRequestBuilder;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.reactnative.RNQbReactnativePackage;
import com.quickblox.reactnative.helpers.EventsHelper;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ChatModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "RNQBChatModule";

    private ReactApplicationContext reactContext;

    private Set<QBChatDialog> dialogsCache = new QBDialogsSet(new QBDialogSetAddListenerImpl());
    private SystemMessageListener systemMessageListener;
    private QBMessageStatusListener messageStatusListener;
    private IncomingMessageListener incomingMessageListener;
    private ConnectionListener connectionListener;

    private RNQbReactnativePackage.ModuleEvents moduleEvents;

    public ChatModule(ReactApplicationContext reactContext, RNQbReactnativePackage.ModuleEvents moduleEvents) {
        super(reactContext);
        this.reactContext = reactContext;
        this.moduleEvents = moduleEvents;
    }

    public void onInitCredentials() {
        addConnectionListener();
    }

    private void addIncomingMessageListener() {
        if (incomingMessageListener == null) {
            incomingMessageListener = new IncomingMessageListener();
            QBChatService.getInstance().getIncomingMessagesManager().addDialogMessageListener(incomingMessageListener);
        }
    }

    private void removeIncomingMessageListener() {
        if (incomingMessageListener != null) {
            QBChatService.getInstance().getIncomingMessagesManager().removeDialogMessageListrener(incomingMessageListener);
            incomingMessageListener = null;
        }
    }

    private void addConnectionListener() {
        if (connectionListener == null) {
            connectionListener = new ConnectionListener();
            QBChatService.getInstance().addConnectionListener(connectionListener);
        }
    }

    private void removeConnectionListener() {
        if (connectionListener != null) {
            QBChatService.getInstance().removeConnectionListener(connectionListener);
            connectionListener = null;
        }
    }

    private void addSystemMessageListener() {
        if (systemMessageListener == null) {
            systemMessageListener = new SystemMessageListener();
            QBChatService.getInstance().getSystemMessagesManager().addSystemMessageListener(systemMessageListener);
        }
    }

    private void removeSystemMessageListener() {
        if (systemMessageListener != null) {
            QBChatService.getInstance().getSystemMessagesManager().removeSystemMessageListener(systemMessageListener);
            systemMessageListener = null;
        }
    }

    private synchronized void addMessageStatusListener() {
        if (messageStatusListener == null) {
            messageStatusListener = new MessageStatusListener();
            QBMessageStatusesManager manager = QBChatService.getInstance().getMessageStatusesManager();
            manager.addMessageStatusListener(messageStatusListener);
        }
    }

    private void removeMessageStatusListener() {
        if (messageStatusListener != null) {
            QBChatService.getInstance().getMessageStatusesManager().removeMessageStatusListener(messageStatusListener);
            messageStatusListener = null;
        }
    }

    private QBChatDialog getDialogFromCache(String dialogId) {
        QBChatDialog dialog = null;
        for (QBChatDialog chatDialog : dialogsCache) {
            if (chatDialog.getDialogId().equals(dialogId)) {
                dialog = chatDialog;
                break;
            }
        }
        return dialog;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        //CHAT EVENT
        constants.put(ChatConstants.EVENT_TYPE, ChatConstants.ChatEvents.getChatEvents());

        //DIALOG TYPES
        constants.put(ChatConstants.DIALOG_TYPE, ChatConstants.DialogType.getDialogTypes());

        //DIALOGS SORT
        constants.put(ChatConstants.DIALOGS_SORT, ChatConstants.DialogsSort.getDialogsSorts());

        //DIALOGS_FILTER
        constants.put(ChatConstants.DIALOGS_FILTER, ChatConstants.DialogsFilter.getDialogsFilters());

        //MESSAGES_SORT
        constants.put(ChatConstants.MESSAGES_SORT, ChatConstants.MessagesSort.getMessagesSorts());

        //MESSAGES_FILTER
        constants.put(ChatConstants.MESSAGES_FILTER, ChatConstants.MessagesFilter.getMessagesFilters());

        return constants;
    }

    @ReactMethod
    public void connect(ReadableMap data, final Promise promise) {
        Integer userId = data != null && data.hasKey("userId") ? data.getInt("userId") : null;
        String password = data != null && data.hasKey("password") ? data.getString("password") : null;

        if (userId == null) {
            promise.reject(new Exception("The user id is wrong"));
            return;
        }

        QBUser user = new QBUser();
        if (!TextUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        user.setId(userId);
        QBChatService.getInstance().setUseStreamManagement(true);
        QBChatService.setDefaultPacketReplyTimeout(30000);
        QBChatService.getInstance().login(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                addSystemMessageListener();
                addMessageStatusListener();
                addIncomingMessageListener();
                moduleEvents.onChatConnected();
                promise.resolve(aVoid);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void disconnect(final Promise promise) {
        removeSystemMessageListener();
        removeMessageStatusListener();
        removeConnectionListener();
        removeIncomingMessageListener();
        QBChatService.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                moduleEvents.onChatDisconnected();
                dialogsCache.clear();
                promise.resolve(aVoid);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void isConnected(final Promise promise) {
        promise.resolve(QBChatService.getInstance().isLoggedIn());
    }

    @ReactMethod
    public void pingServer(final Promise promise) {
        QBChatService.getInstance().getPingManager().pingServer(new QBEntityCallback<Void>() {
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
    public void pingUser(ReadableMap data, final Promise promise) {
        Integer userId = data != null && data.hasKey("userId") ? data.getInt("userId") : null;

        if (userId == null || userId == 0) {
            promise.reject(new Exception("The userId is required parameter"));
            return;
        }

        QBChatService.getInstance().getPingManager().pingUser(userId, new QBEntityCallback<Void>() {
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
    public void getDialogs(ReadableMap data, final Promise promise) {
        ReadableMap sortMap = data != null && data.hasKey("sort") ? data.getMap("sort") : null;
        ReadableMap filterMap = data != null && data.hasKey("filter") ? data.getMap("filter") : null;
        final int limit = data != null && data.hasKey("limit") ? data.getInt("limit") : 100;
        final int skip = data != null && data.hasKey("skip") ? data.getInt("skip") : 0;

        QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();

        ChatMapper.addDialogFilterToRequestBuilder(qbRequestGetBuilder, filterMap);
        ChatMapper.addDialogSortToRequestBuilder(qbRequestGetBuilder, sortMap);

        qbRequestGetBuilder.setLimit(limit);
        qbRequestGetBuilder.setSkip(skip);

        QBRestChatService.getChatDialogs(null, qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                WritableMap map = new WritableNativeMap();

                if (dialogsCache.containsAll(qbChatDialogs)) {
                    dialogsCache.removeAll(qbChatDialogs);
                }

                dialogsCache.addAll(qbChatDialogs);
                WritableArray dialogsArray = new WritableNativeArray();
                for (QBChatDialog qbDialog : qbChatDialogs) {
                    WritableMap dialog = ChatMapper.qbChatDialogToMap(qbDialog);
                    dialogsArray.pushMap(dialog);
                }

                map.putArray("dialogs", dialogsArray);
                map.putInt("skip", bundle.containsKey("skip") ? bundle.getInt("skip") : skip);
                map.putInt("limit", bundle.containsKey("limit") ? bundle.getInt("limit") : limit);
                map.putInt("total", bundle.containsKey("total_entries") ? bundle.getInt("total_entries") : -1);

                promise.resolve(map);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void getDialogsCount(ReadableMap data, final Promise promise) {
        ReadableMap filterMap = data != null && data.hasKey("filter") ? data.getMap("filter") : null;
        int limit = data != null && data.hasKey("limit") ? data.getInt("limit") : 100;
        int skip = data != null && data.hasKey("skip") ? data.getInt("skip") : 0;

        QBRequestGetBuilder qbRequestGetBuilder = new QBRequestGetBuilder();

        ChatMapper.addDialogFilterToRequestBuilder(qbRequestGetBuilder, filterMap);

        qbRequestGetBuilder.setLimit(limit);
        qbRequestGetBuilder.setSkip(skip);

        QBRestChatService.getChatDialogsCount(qbRequestGetBuilder, new Bundle()).performAsync(new QBEntityCallback<Integer>() {
            @Override
            public void onSuccess(Integer countOfDialogs, Bundle bundle) {
                promise.resolve(countOfDialogs);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void updateDialog(ReadableMap data, final Promise promise) {
        final String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;
        ReadableArray addUsers = data != null && data.hasKey("addUsers") ? data.getArray("addUsers") : null;
        ReadableArray removeUsers = data != null && data.hasKey("  removeUsers") ? data.getArray("  removeUsers") : null;
        String dialogName = data != null && data.hasKey("name") ? data.getString("name") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog qbChatDialog = getDialogFromCache(dialogId);
        if (qbChatDialog == null) {
            try {
                qbChatDialog = QBRestChatService.getChatDialogById(dialogId).perform();
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
            }
        }

        if (qbChatDialog != null && !TextUtils.isEmpty(dialogName)) {
            qbChatDialog.setName(dialogName);
        }

        if (removeUsers != null && removeUsers.size() > 0) {
            QBDialogRequestBuilder requestBuilder = new QBDialogRequestBuilder();
            for (int index = 0; index < removeUsers.size(); index++) {
                int userId = removeUsers.getInt(index);
                requestBuilder.removeUsers(userId);
            }
            try {
                qbChatDialog = QBRestChatService.updateChatDialog(qbChatDialog, requestBuilder).perform();
                WritableMap dialog = ChatMapper.qbChatDialogToMap(qbChatDialog);
                promise.resolve(dialog);
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
            }
        }

        if (addUsers != null && addUsers.size() > 0) {
            QBDialogRequestBuilder requestBuilder = new QBDialogRequestBuilder();
            for (int index = 0; index < addUsers.size(); index++) {
                int userId = addUsers.getInt(index);
                requestBuilder.addUsers(userId);
            }
            try {
                qbChatDialog = QBRestChatService.updateChatDialog(qbChatDialog, requestBuilder).perform();
                WritableMap dialog = ChatMapper.qbChatDialogToMap(qbChatDialog);
                promise.resolve(dialog);
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
            }
        }

        if (qbChatDialog != null) {
            dialogsCache.remove(new QBChatDialog(dialogId));
            dialogsCache.add(qbChatDialog);
        }
    }

    @ReactMethod
    public void createDialog(ReadableMap data, final Promise promise) {
        ReadableArray occupantsIds = data != null && data.hasKey("occupantsIds") ? data.getArray("occupantsIds") : null;
        String chatName = data != null && data.hasKey("name") ? data.getString("name") : null;
        Integer type = data != null && data.hasKey("type") ? data.getInt("type") : null;

        if (occupantsIds == null || occupantsIds.size() == 0) {
            promise.reject(new Exception("Parameter occupantsIds is required"));
            return;
        }

        List<Integer> occupantsIdsList = new ArrayList<>();
        for (int index = 0; index < occupantsIds.size(); index++) {
            int occupantId = occupantsIds.getInt(index);
            occupantsIdsList.add(occupantId);
        }

        //add current user id
        Integer currentUserId = QBSessionManager.getInstance().getSessionParameters().getUserId();
        if (!occupantsIdsList.isEmpty() && !occupantsIdsList.contains(currentUserId)) {
            occupantsIdsList.add(currentUserId);
        }

        QBDialogType dialogType;

        if (type != null) {
            dialogType = QBDialogType.parseByCode(type);
        } else if (occupantsIdsList.size() > 2) {
            dialogType = (QBDialogType.GROUP);
        } else if (occupantsIdsList.size() == 2) {
            dialogType = (QBDialogType.PRIVATE);
        } else {
            dialogType = (QBDialogType.PUBLIC_GROUP);
        }

        QBChatDialog qbChatDialog = DialogUtils.buildDialog(chatName, dialogType, occupantsIdsList);

        QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

                if (qbChatDialog.getType() == QBDialogType.GROUP || qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP) {

                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(0);

                    qbChatDialog.initForChat(QBChatService.getInstance());

                    try {
                        qbChatDialog.join(history);
                    } catch (XMPPException | SmackException e) {
                        e.printStackTrace();
                        promise.reject(e);
                        return;
                    }
                }

                dialogsCache.add(qbChatDialog);
                WritableMap dialog = ChatMapper.qbChatDialogToMap(qbChatDialog);
                promise.resolve(dialog);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void deleteDialog(ReadableMap data, final Promise promise) {
        final String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBRestChatService.deleteDialog(dialogId, true).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                dialogsCache.remove(new QBChatDialog(dialogId));
                promise.resolve(aVoid);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void leaveDialog(ReadableMap data, final Promise promise) {
        final String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        QBChatDialog dialog;

        if (getDialogFromCache(dialogId) != null) {
            dialog = getDialogFromCache(dialogId);
        } else {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        if (dialog.getType() != QBDialogType.PRIVATE && dialog.isJoined()) {
            try {
                dialog.leave();
                dialogsCache.remove(dialog);
            } catch (Exception e) {
                e.printStackTrace();
                promise.reject(e);
            }
        }

        if (dialog.getType() == QBDialogType.PUBLIC_GROUP) {
            promise.resolve(null);
            return;
        }

        QBRestChatService.deleteDialog(dialogId, false).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                dialogsCache.remove(new QBChatDialog(dialogId));
                promise.resolve(aVoid);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void joinDialog(ReadableMap data, final Promise promise) {
        String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog dialog;

        if (getDialogFromCache(dialogId) != null) {
            dialog = getDialogFromCache(dialogId);
        } else {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
                dialogsCache.add(dialog);
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        if (dialog.getType() != QBDialogType.PRIVATE) {
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);
            dialog.join(history, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {
                    promise.resolve(null);
                }

                @Override
                public void onError(QBResponseException e) {
                    promise.reject(e);
                }
            });
        } else {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getOnlineUsers(ReadableMap data, final Promise promise) {
        String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog dialog;

        if (getDialogFromCache(dialogId) != null) {
            dialog = getDialogFromCache(dialogId);
        } else {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        try {
            List<Integer> users = (List<Integer>) dialog.requestOnlineUsers();
            WritableArray array = Arguments.fromList(users);
            promise.resolve(array);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void sendMessage(ReadableMap data, final Promise promise) {
        final String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;
        final String body = data != null && data.hasKey("body") ? data.getString("body") : null;
        final ReadableArray attachments = data != null && data.hasKey("attachments") ? data.getArray("attachments") : null;
        final ReadableMap properties = data != null && data.hasKey("properties") ? data.getMap("properties") : null;
        final boolean markable = data != null && data.hasKey("markable") && data.getBoolean("markable");
        final long dateSent = data != null && data.hasKey("dateSent") ? data.getInt("dateSent") : System.currentTimeMillis() / 1000;
        final boolean saveToHistory = data != null && data.hasKey("saveToHistory") && data.getBoolean("saveToHistory");

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        final QBChatMessage qbChatMessage = new QBChatMessage();

        List<QBAttachment> qbAttachmentList = new ArrayList<>();

        if (properties != null && properties.toHashMap().size() > 0) {
            for (Map.Entry<String, Object> entry : properties.toHashMap().entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();
                if (propertyValue instanceof Double) {
                    propertyValue = ((Double) propertyValue).intValue();
                }
                qbChatMessage.setProperty(propertyName, propertyValue.toString());
            }
        }

        if (attachments != null && attachments.size() > 0) {
            for (int index = 0; index < attachments.size(); index++) {
                ReadableMap attachmentMap = attachments.getMap(0);

                if (attachmentMap == null) {
                    continue;
                }

                String attachmentType = attachmentMap.hasKey("type") ? attachmentMap.getString("type") : null;
                String attachmentId = attachmentMap.hasKey("id") ? attachmentMap.getString("id") : null;
                String attachmentUrl = attachmentMap.hasKey("url") ? attachmentMap.getString("url") : null;
                String attachmentName = attachmentMap.hasKey("name") ? attachmentMap.getString("name") : null;
                String contentType = attachmentMap.hasKey("contentType") ? attachmentMap.getString("contentType") : null;
                String attachmentData = attachmentMap.hasKey("data") ? attachmentMap.getString("data") : null;
                Integer attachmentSize = attachmentMap.hasKey("size") ? attachmentMap.getInt("size") : null;
                Integer attachmentHeight = attachmentMap.hasKey("height") ? attachmentMap.getInt("height") : null;
                Integer attachmentWidth = attachmentMap.hasKey("width") ? attachmentMap.getInt("width") : null;
                Integer attachmentDuration = attachmentMap.hasKey("duration") ? attachmentMap.getInt("duration") : null;

                if (!TextUtils.isEmpty(attachmentType)) {
                    QBAttachment attachment = new QBAttachment(attachmentType);

                    if (!TextUtils.isEmpty(attachmentId)) {
                        attachment.setId(attachmentId);
                    }
                    if (!TextUtils.isEmpty(attachmentUrl)) {
                        attachment.setUrl(attachmentUrl);
                    }
                    if (!TextUtils.isEmpty(attachmentName)) {
                        attachment.setName(attachmentName);
                    }
                    if (!TextUtils.isEmpty(contentType)) {
                        attachment.setContentType(contentType);
                    }
                    if (!TextUtils.isEmpty(attachmentData)) {
                        attachment.setData(attachmentData);
                    }
                    if (attachmentSize != null && attachmentSize > 0) {
                        attachment.setSize(attachmentSize);
                    }
                    if (attachmentHeight != null && attachmentHeight > 0) {
                        attachment.setHeight(attachmentHeight);
                    }
                    if (attachmentWidth != null && attachmentWidth > 0) {
                        attachment.setWidth(attachmentWidth);
                    }
                    if (attachmentDuration != null && attachmentDuration > 0) {
                        attachment.setDuration(attachmentDuration);
                    }

                    qbAttachmentList.add(attachment);
                }
            }
        }

        qbChatMessage.setBody(body);
        qbChatMessage.setMarkable(markable);
        qbChatMessage.setSaveToHistory(saveToHistory);
        qbChatMessage.setDateSent(dateSent);
        qbChatMessage.setAttachments(qbAttachmentList);

        final QBChatDialog dialog;

        if (!dialogsCache.contains(new QBChatDialog(dialogId))) {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
                dialogsCache.add(dialog);
            } catch (QBResponseException e) {
                promise.reject(e);
                return;
            }
        } else {
            dialog = getDialogFromCache(dialogId);
        }

        if (!dialog.isJoined() && !dialog.getType().equals(QBDialogType.PRIVATE)) {
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(0);

            dialog.initForChat(QBChatService.getInstance());

            try {
                dialog.join(history);
            } catch (XMPPException | SmackException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        if (dialog.getType().equals(QBDialogType.PRIVATE)) {
            qbChatMessage.setRecipientId(dialog.getRecipientId());
        }

        if (!dialog.getType().equals(QBDialogType.PUBLIC_GROUP)) {
            //current user id
            Integer userId = QBSessionManager.getInstance().getSessionParameters().getUserId();

            Collection<Integer> idsList = new ArrayList<>();
            idsList.add(userId);

            qbChatMessage.setDeliveredIds(idsList);
            qbChatMessage.setReadIds(idsList);
        }

        dialog.sendMessage(qbChatMessage, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                promise.resolve(aVoid);

                qbChatMessage.setDialogId(dialogId);

                //timestamp
                long timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                qbChatMessage.setDateSent(timeStamp);

                //current user id
                Integer userId = QBSessionManager.getInstance().getSessionParameters().getUserId();
                qbChatMessage.setSenderId(userId);

                WritableMap message = ChatMapper.qbChatMessageToMap(qbChatMessage);
                String eventName = ChatConstants.ChatEventsMessage.RECEIVED_NEW_MESSAGE.eventValue;
                WritableMap data = EventsHelper.buildResult(eventName, message);
                sendEvent(eventName, data);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    @ReactMethod
    public void sendSystemMessage(ReadableMap data, final Promise promise) {
        Integer recipientId = data != null && data.hasKey("recipientId") ? data.getInt("recipientId") : null;
        ReadableMap properties = data != null && data.hasKey("properties") ? data.getMap("properties") : null;

        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setRecipientId(recipientId);

        if (properties != null) {
            for (Map.Entry<String, Object> entry : properties.toHashMap().entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();
                qbChatMessage.setProperty(propertyName, String.valueOf(propertyValue));
            }
        }

        QBChatService.getInstance().getSystemMessagesManager().sendSystemMessage(qbChatMessage, new QBEntityCallback<Void>() {
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
    public void markMessageRead(ReadableMap data, final Promise promise) {
        ReadableMap messageMap = data != null && data.hasKey("message") ? data.getMap("message") : null;

        if (messageMap == null) {
            promise.reject(new Exception("The parameter message is required"));
            return;
        }

        QBChatMessage chatMessage = ChatMapper.mapToQBChatMessage(messageMap);

        String messageId = chatMessage.getId();
        String dialogId = chatMessage.getDialogId();
        Integer senderId = chatMessage.getSenderId();

        QBChatDialog dialog = getDialogFromCache(dialogId);
        if (dialog == null) {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        chatMessage.setId(messageId);
        chatMessage.setDialogId(dialogId);
        chatMessage.setSenderId(senderId);

        try {
            dialog.readMessage(chatMessage);
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject(e);
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void markMessageDelivered(ReadableMap data, final Promise promise) {

        ReadableMap messageMap = data != null && data.hasKey("message") ? data.getMap("message") : null;

        if (messageMap == null) {
            promise.reject(new Exception("The parameter message is required"));
            return;
        }

        QBChatMessage chatMessage = ChatMapper.mapToQBChatMessage(messageMap);

        String messageId = chatMessage.getId();
        String dialogId = chatMessage.getDialogId();
        Integer senderId = chatMessage.getSenderId();

        QBChatDialog dialog = getDialogFromCache(dialogId);
        if (dialog == null) {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        }

        chatMessage.setId(messageId);
        chatMessage.setDialogId(dialogId);
        chatMessage.setSenderId(senderId);

        try {
            dialog.deliverMessage(chatMessage);
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject(e);
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void sendIsTyping(ReadableMap data, final Promise promise) {
        String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog dialog;

        if (!dialogsCache.contains(new QBChatDialog(dialogId))) {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
                dialogsCache.add(dialog);
            } catch (QBResponseException e) {
                e.printStackTrace();
                promise.reject(e);
                return;
            }
        } else {
            dialog = getDialogFromCache(dialogId);
        }

        dialog.sendIsTypingNotification(new QBEntityCallback<Void>() {
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
    public void sendStoppedTyping(ReadableMap data, final Promise promise) {
        String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog dialog;

        if (!dialogsCache.contains(new QBChatDialog(dialogId))) {
            try {
                dialog = QBRestChatService.getChatDialogById(dialogId).perform();
                dialogsCache.add(dialog);
            } catch (QBResponseException e) {
                promise.reject(e);
                return;
            }
        } else {
            dialog = getDialogFromCache(dialogId);
        }

        dialog.sendStopTypingNotification(new QBEntityCallback<Void>() {
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
    public void getDialogMessages(ReadableMap data, final Promise promise) {
        String dialogId = data != null && data.hasKey("dialogId") ? data.getString("dialogId") : null;
        ReadableMap sortMap = data != null && data.hasKey("sort") ? data.getMap("sort") : null;
        ReadableMap filterMap = data != null && data.hasKey("filter") ? data.getMap("filter") : null;
        final int limit = data != null && data.hasKey("limit") ? data.getInt("limit") : 100;
        final int skip = data != null && data.hasKey("skip") ? data.getInt("skip") : 0;
        boolean markAsRead = data != null && data.hasKey("markAsRead") && data.getBoolean("markAsRead");

        if (TextUtils.isEmpty(dialogId)) {
            promise.reject(new Exception("Parameter dialogId is required"));
            return;
        }

        QBChatDialog dialog = new QBChatDialog(dialogId);
        dialog.initForChat(dialogId, QBDialogType.GROUP, QBChatService.getInstance());

        QBMessageGetBuilder qbRequestGetBuilder = new QBMessageGetBuilder();

        ChatMapper.addMessageFilterToRequestBuilder(qbRequestGetBuilder, filterMap);
        ChatMapper.addMessageSortToRequestBuilder(qbRequestGetBuilder, sortMap);

        qbRequestGetBuilder.setSkip(skip);
        qbRequestGetBuilder.setLimit(limit);
        qbRequestGetBuilder.markAsRead(markAsRead);

        QBRestChatService.getDialogMessages(dialog, qbRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                WritableMap map = new WritableNativeMap();
                WritableArray messages = new WritableNativeArray();
                for (QBChatMessage qbChatMessage : qbChatMessages) {
                    WritableMap message = ChatMapper.qbChatMessageToMap(qbChatMessage);
                    messages.pushMap(message);
                }

                map.putArray("messages", messages);
                map.putInt("skip", bundle.containsKey("skip") ? bundle.getInt("skip") : skip);
                map.putInt("limit", bundle.containsKey("limit") ? bundle.getInt("limit") : limit);

                promise.resolve(map);
            }

            @Override
            public void onError(QBResponseException e) {
                promise.reject(e);
            }
        });
    }

    private void sendEvent(String eventName, @Nullable WritableMap eventData) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, eventData);
    }

    ///////////////////////////////////////////////////////////////////////////
    // INCOMING MESSAGE LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class IncomingMessageListener implements QBChatDialogMessageListener {
        @Override
        public void processMessage(String dialogId, QBChatMessage qbChatMessage, Integer integer) {
            Integer userId = QBSessionManager.getInstance().getSessionParameters().getUserId();

            if (!qbChatMessage.getSenderId().equals(userId)) {
                WritableMap message = ChatMapper.qbChatMessageToMap(qbChatMessage);
                String eventName = ChatConstants.ChatEventsMessage.RECEIVED_NEW_MESSAGE.eventValue;
                WritableMap data = EventsHelper.buildResult(eventName, message);
                sendEvent(eventName, data);
            }
        }

        @Override
        public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONNECTION LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class ConnectionListener implements org.jivesoftware.smack.ConnectionListener {

        @Override
        public void connected(XMPPConnection xmppConnection) {
            String eventName = ChatConstants.ChatEventsConnection.CONNECTED.eventValue;
            WritableMap eventData = EventsHelper.buildResult(eventName, null);
            sendEvent(eventName, eventData);
        }

        @Override
        public void authenticated(XMPPConnection xmppConnection, boolean b) {
            //ignore
        }

        @Override
        public void connectionClosed() {
            String eventName = ChatConstants.ChatEventsConnection.CONNECTION_CLOSED.eventValue;
            WritableMap eventData = EventsHelper.buildResult(eventName, null);
            sendEvent(eventName, eventData);
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            String eventName = ChatConstants.ChatEventsConnection.RECONNECTION_FAILED.eventValue;
            WritableMap eventData = EventsHelper.buildResult(eventName, null);
            sendEvent(eventName, eventData);
        }

        @Override
        public void reconnectionSuccessful() {
            new JoinPublicChatJob(new JoinPublicChatJob.PublicJoinChatJobListener() {
                @Override
                public void onSuccess() {
                    String eventName = ChatConstants.ChatEventsConnection.RECONNECTION_SUCCESSFUL.eventValue;
                    WritableMap eventData = EventsHelper.buildResult(eventName, null);
                    sendEvent(eventName, eventData);
                }

                @Override
                public void onError() {
                    String eventName = ChatConstants.ChatEventsConnection.RECONNECTION_FAILED.eventValue;
                    WritableMap eventData = EventsHelper.buildResult(eventName, null);
                    sendEvent(eventName, eventData);
                }
            }).execute(dialogsCache);
        }

        @Override
        public void reconnectingIn(int i) {
            //ignore
        }

        @Override
        public void reconnectionFailed(Exception e) {
            String eventName = ChatConstants.ChatEventsConnection.RECONNECTION_FAILED.eventValue;
            WritableMap eventData = EventsHelper.buildResult(eventName, null);
            sendEvent(eventName, eventData);
        }
    }

    private static class JoinPublicChatJob extends AsyncTask<Set<QBChatDialog>, Void, Void> {
        private PublicJoinChatJobListener jobListener;

        JoinPublicChatJob(PublicJoinChatJobListener jobListener) {
            this.jobListener = jobListener;
        }

        @Override
        protected Void doInBackground(Set<QBChatDialog>... dialogsCache) {
            boolean successReconnected = true;

            if (dialogsCache.length <= 0) {
                return null;
            }

            Set<QBChatDialog> dialogs = dialogsCache[0];

            for (QBChatDialog dialog : dialogs) {
                if (!dialog.isPrivate()) {
                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(0);

                    dialog.initForChat(QBChatService.getInstance());

                    if (!dialog.isJoined()) {
                        try {
                            dialog.join(history);
                        } catch (XMPPException | SmackException e) {
                            e.printStackTrace();
                            successReconnected = false;
                        }
                    }
                }
            }

            if (successReconnected) {
                jobListener.onSuccess();
            } else {
                jobListener.onError();
            }

            return null;
        }

        private interface PublicJoinChatJobListener {

            void onSuccess();

            void onError();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // SYSTEM MESSAGE LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class SystemMessageListener implements QBSystemMessageListener {
        @Override
        public void processMessage(QBChatMessage qbChatMessage) {
            String eventName = ChatConstants.ChatEventsSystemMessage.RECEIVED_NEW_MESSAGE.eventValue;
            WritableMap message = ChatMapper.qbChatMessageToMap(qbChatMessage);
            WritableMap data = EventsHelper.buildResult(eventName, message);
            sendEvent(eventName, data);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            //ignore
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // MESSAGE STATUS LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class MessageStatusListener implements QBMessageStatusListener {
        private static final String MESSAGE_ID = "messageId";
        private static final String DIALOG_ID = "dialogId";
        private static final String USER_ID = "userId";

        @Override
        public void processMessageDelivered(String messageId, String dialogId, Integer userId) {
            WritableMap payload = new WritableNativeMap();
            payload.putString(MESSAGE_ID, messageId);
            payload.putString(DIALOG_ID, dialogId);
            payload.putInt(USER_ID, userId);

            String eventName = ChatConstants.ChatEventsMessageStatus.MESSAGE_DELIVERED.eventValue;
            WritableMap data = EventsHelper.buildResult(eventName, payload);
            sendEvent(eventName, data);
        }

        @Override
        public void processMessageRead(String messageId, String dialogId, Integer userId) {
            WritableMap payload = new WritableNativeMap();
            payload.putString(MESSAGE_ID, messageId);
            payload.putString(DIALOG_ID, dialogId);
            payload.putInt(USER_ID, userId);

            String eventName = ChatConstants.ChatEventsMessageStatus.MESSAGE_READ.eventValue;
            WritableMap data = EventsHelper.buildResult(eventName, payload);
            sendEvent(eventName, data);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // TYPING LISTENER
    ///////////////////////////////////////////////////////////////////////////
    private class TypingListener implements QBChatDialogTypingListener {
        private static final String DIALOG_ID = "dialogId";
        private static final String USER_ID = "userId";

        private String dialogId;

        TypingListener(String dialogId) {
            this.dialogId = dialogId;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean equals;
            if (obj instanceof TypingListener) {
                equals = this.dialogId.equals(((TypingListener) obj).dialogId);
            } else {
                equals = super.equals(obj);
            }
            return equals;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + dialogId.hashCode();
            return hash;
        }

        @Override
        public void processUserIsTyping(String dialogId, Integer userId) {
            WritableMap payload = new WritableNativeMap();
            payload.putInt(USER_ID, userId);
            payload.putString(DIALOG_ID, dialogId);

            String eventName = ChatConstants.ChatEventsTyping.USER_IS_TYPING.eventValue;
            WritableMap data = EventsHelper.buildResult(eventName, payload);
            sendEvent(eventName, data);
        }

        @Override
        public void processUserStopTyping(String dialogId, Integer userId) {
            WritableMap payload = new WritableNativeMap();
            payload.putInt(USER_ID, userId);
            payload.putString(DIALOG_ID, dialogId);

            String eventName = ChatConstants.ChatEventsTyping.USER_STOPPED_TYPING.eventValue;
            WritableMap data = EventsHelper.buildResult(eventName, payload);
            sendEvent(eventName, data);
        }
    }

    private class QBDialogSetAddListenerImpl implements QBDialogsSet.QBDialogSetAddListener {

        @Override
        public void onAdded(QBChatDialog chatDialog) {
            chatDialog.addIsTypingListener(new TypingListener(chatDialog.getDialogId()));
        }
    }
}