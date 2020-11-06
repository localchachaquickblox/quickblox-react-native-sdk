
package com.quickblox.reactnative.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.reactnative.helpers.EventsHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FileModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "RNQBFileModule";

    private Set<UploadProgressItem> uploadProgressItemSet = new CopyOnWriteArraySet<>();
    private ReactApplicationContext reactContext;

    public FileModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        //FILE_UPLOAD_PROGRESS
        constants.put(FileConstants.EVENT_TYPE, FileConstants.FileUploadProgress.getAll());

        return constants;
    }

    private UploadProgressItem getUploadProgressItem(String url) {
        UploadProgressItem uploadProgressItem = null;
        if (uploadProgressItemSet.contains(new UploadProgressItem(url))) {
            for (UploadProgressItem item : uploadProgressItemSet) {
                if (item.equals(new UploadProgressItem(url))) {
                    uploadProgressItem = item;
                    break;
                }
            }
        }
        return uploadProgressItem;
    }

    @ReactMethod
    public void subscribeUploadProgress(ReadableMap data, Promise promise) {
        String url = data != null && data.hasKey("url") ? data.getString("url") : null;

        if (TextUtils.isEmpty(url)) {
            promise.reject(new Exception("The url is required parameter"));
            return;
        }

        uploadProgressItemSet.add(new UploadProgressItem(url));

        UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        if (uploadProgressItem != null && !uploadProgressItem.subscribed()) {
            uploadProgressItem.subscribe(true);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void unsubscribeUploadProgress(ReadableMap data, Promise promise) {
        String url = data != null && data.hasKey("url") ? data.getString("url") : null;

        if (TextUtils.isEmpty(url)) {
            promise.reject(new Exception("The url is required parameter"));
            return;
        }

        UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        if (uploadProgressItem != null) {
            uploadProgressItem.subscribe(false);
            uploadProgressItemSet.remove(uploadProgressItem);
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void upload(ReadableMap data, final Promise promise) {
        String url = data != null && data.hasKey("url") ? data.getString("url") : null;
        boolean isPublic = data != null && data.hasKey("public") && data.getBoolean("public");

        if (TextUtils.isEmpty(url)) {
            promise.reject(new Exception("Parameter uriString is required"));
            return;
        }

        new LoadFileFromUrlTask(url, isPublic, promise).execute();
    }

    private class LoadFileFromUrlTask extends AsyncTask<Void, Void, File> {
        private static final String SCHEME_CONTENT = "content";
        private static final String SCHEME_CONTENT_GOOGLE = "content://com.google.android";
        private static final String SCHEME_FILE = "file";

        private static final int BUFFER_SIZE_2_MB = 2048;

        private Promise promise;
        private String uriString;
        private boolean isPublic;

        LoadFileFromUrlTask(String uriString, boolean isPublic, Promise promise) {
            this.promise = promise;
            this.uriString = uriString;
            this.isPublic = isPublic;
        }

        @Override
        protected File doInBackground(Void... voids) {
            String imageFilePath = null;
            Uri uri = Uri.parse(uriString);
            String uriScheme = uri.getScheme();
            File file = null;

            boolean isFromGoogleApp = uri.toString().startsWith(SCHEME_CONTENT_GOOGLE);
            boolean isKitKatAndUpper = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if (SCHEME_CONTENT.equalsIgnoreCase(uriScheme) && !isFromGoogleApp && !isKitKatAndUpper) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = reactContext.getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageFilePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else if (SCHEME_FILE.equalsIgnoreCase(uriScheme)) {
                imageFilePath = uri.getPath();
            } else {
                try {
                    imageFilePath = saveUriToFile(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(imageFilePath)) {
                file = new File(imageFilePath);
            }

            return file;
        }

        private String saveUriToFile(Uri uri) throws Exception {
            ParcelFileDescriptor parcelFileDescriptor = reactContext.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            InputStream inputStream = new FileInputStream(fileDescriptor);
            BufferedInputStream bis = new BufferedInputStream(inputStream);

            File parentDir = getAppExternalDataDirectoryFile();
            String fileExtension = getFileExtension(uri);
            String name = getFileName(uri);
            String fileName = name + "." + fileExtension;
            File resultFile = new File(parentDir, fileName);

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile));

            byte[] buf = new byte[BUFFER_SIZE_2_MB];
            int length;

            try {
                while ((length = bis.read(buf)) > 0) {
                    bos.write(buf, 0, length);
                }
            } catch (Exception e) {
                throw new IOException("Can\'t save Storage API bitmap to a file!", e);
            } finally {
                parcelFileDescriptor.close();
                bis.close();
                bos.close();
            }

            return resultFile.getAbsolutePath();
        }

        private String getFileName(Uri uri) {
            String fileName;
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};
                cursor = reactContext.getContentResolver().query(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                cursor.moveToFirst();
                fileName = cursor.getString(column_index);
            } catch (Exception e) {
                fileName = String.valueOf(System.currentTimeMillis());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            return fileName;
        }

        private String getFileExtension(Uri uri) {
            String fileExtension;
            if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver contentResolver = reactContext.getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
            } else {
                fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            }
            return fileExtension;
        }

        private String getAppExternalDataDirectoryPath() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStorageDirectory())
                    .append(File.separator)
                    .append("Android")
                    .append(File.separator)
                    .append("data")
                    .append(File.separator)
                    .append(reactContext.getPackageName())
                    .append(File.separator);

            return stringBuilder.toString();
        }

        private File getAppExternalDataDirectoryFile() {
            File dataDirectoryFile = new File(getAppExternalDataDirectoryPath());
            dataDirectoryFile.mkdirs();

            return dataDirectoryFile;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                uploadFile(file, isPublic, promise, uriString);
            } else {
                promise.reject(new Exception("Can't load file from uriString: " + uriString));
            }
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap eventData) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, eventData);
    }

    private void uploadFile(final File file, boolean isPublic, final Promise promise, final String url) {
        final UploadProgressItem uploadProgressItem = getUploadProgressItem(url);
        QBContent.uploadFileTask(file, isPublic, "", new QBProgressCallback() {
            @Override
            public void onProgressUpdate(int progressValue) {
                if (uploadProgressItem != null && uploadProgressItem.subscribed()) {
                    WritableMap payload = new WritableNativeMap();
                    payload.putString("url", url);
                    payload.putInt("progress", progressValue);

                    String eventName = FileConstants.FileUploadProgress.FILE_UPLOAD_PROGRESS.eventValue;
                    WritableMap data = EventsHelper.buildResult(eventName, payload);
                    sendEvent(eventName, data);
                }
                if (uploadProgressItem != null && progressValue >= 100) {
                    uploadProgressItemSet.remove(uploadProgressItem);
                }
            }
        }).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                WritableMap file = FileMapper.qbFileToMap(qbFile);
                promise.resolve(file);
            }

            @Override
            public void onError(QBResponseException responseException) {
                promise.reject(responseException);
            }
        });
    }

    @ReactMethod
    public void getInfo(ReadableMap data, final Promise promise) {
        Integer id = data != null && data.hasKey("id") ? data.getInt("id") : null;

        if (id == null || id <= 0) {
            promise.reject(new Exception("The id is required parameter"));
            return;
        }

        QBContent.getFile(id).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle params) {
                WritableMap file = FileMapper.qbFileToMap(qbFile);
                promise.resolve(file);
            }

            @Override
            public void onError(QBResponseException responseException) {
                promise.reject(responseException);
            }
        });
    }

    @ReactMethod
    public void getPublicURL(ReadableMap data, final Promise promise) {
        String uid = data != null && data.hasKey("uid") ? data.getString("uid") : null;

        if (TextUtils.isEmpty(uid)) {
            promise.reject(new Exception("The id is required parameter"));
            return;
        }

        String privateUrl = QBFile.getPublicUrlForUID(uid);
        promise.resolve(privateUrl);
    }

    @ReactMethod
    public void getPrivateURL(ReadableMap data, final Promise promise) {
        String uid = data != null && data.hasKey("uid") ? data.getString("uid") : null;

        if (TextUtils.isEmpty(uid)) {
            promise.reject(new Exception("The id is required parameter"));
            return;
        }

        String privateUrl = QBFile.getPrivateUrlForUID(uid);
        promise.resolve(privateUrl);
    }

    private class UploadProgressItem {
        private String url;
        private boolean subscribe = false;

        UploadProgressItem(String url) {
            this.url = url;
        }

        void subscribe(boolean subscribe) {
            this.subscribe = subscribe;
        }

        boolean subscribed() {
            return subscribe;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            boolean equals;
            if (obj instanceof UploadProgressItem) {
                equals = this.url.equals(((UploadProgressItem) obj).url);
            } else {
                equals = super.equals(obj);
            }
            return equals;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + url.hashCode();
            return hash;
        }
    }
}