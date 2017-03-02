package com.example.hh.androidbaseproject.DataAndHelper;

import android.util.Log;
import android.view.View;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by hh on 2016/11/25.
 */

public class UploadManager {
    public View view;
    public Object otherParam;
    public UploadCallback callback;

    public void uploadFile(String filePath, String useName){
        RequestParams params = new RequestParams(DataCache.getInstance().baseUrl +
                "/api/File");
        params.setMultipart(true);
        params.setMethod(HttpMethod.POST);
        params.addBodyParameter("file", new File(filePath), "image/jpeg", useName);

        Log.e("->", params.toString());
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                callback.onProgress(view, otherParam, total, current, isDownloading);
            }

            @Override
            public void onSuccess(String result) {
                callback.onFinish(view, otherParam, result, null);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onFinish(view, otherParam, null, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface UploadCallback {
        void onProgress(View view, Object otherParam, long total, long current, boolean isDownloading);
        void onFinish(View view, Object otherParam, String result, Throwable ex);
    }
}
