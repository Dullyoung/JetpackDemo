package com.dullyoung.jetpackdemo.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;


import com.blankj.utilcode.util.ActivityUtils;
import com.dullyoung.jetpackdemo.Config;
import com.dullyoung.jetpackdemo.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.request.RequestCall;


import java.io.File;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Description : base engine
 *
 * @author Dullyoung
 * Date : 2021/12/2  11:30
 */
public class BaseEngine {
    private final Context mContext;

    public BaseEngine(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    private static final String TAG = "BaseHttpEngine";
    private static Map<String, String> defaultParams = new HashMap<>();


    public static void setDefaultParams(Map<String, String> defaultParams) {
        BaseEngine.defaultParams = defaultParams;
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("os", "android");
        headers.put("language", "cn");
        return headers;
    }


    private void addDefaultParams(Map<String, String> targetParams) {
        for (String s : defaultParams.keySet()) {
            targetParams.put(s, defaultParams.get(s));
        }
        targetParams.put("timespan", System.currentTimeMillis() + "");
    }

    private void addDefaultParams(JsonObject jsonObject) {
        for (String s : defaultParams.keySet()) {
            jsonObject.addProperty(s, defaultParams.get(s));
        }
        jsonObject.addProperty("timespan", System.currentTimeMillis() + "");
    }

    private <T> T doResponse(RequestCall call, Type type, boolean secure) throws Exception {
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new RuntimeException("Response is null");
        } else {
            String responseString = responseBody.string();
            if (secure) {
                // responseString = EPayUtils.getDecrypt(responseString);
            }
            logForResponse(call.getRequest().url().toString(), responseString, secure);
            if (response.code() != 200) {
                throw new RuntimeException("Code:" + response.code()
                        + "Network Error");
            }
            return getResultInfo(responseString, type);
        }
    }


    private <T> T postJson(String url, Type type, Map<String, String> params, boolean secure) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        addDefaultParams(params);
        JsonObject jsonObject = new JsonObject();
        for (String key : params.keySet()) {
            jsonObject.addProperty(key, params.get(key));
        }
        return postJson(url, type, jsonObject, secure);
    }

    /**
     * @param url        url
     * @param type       response type
     * @param jsonObject param
     * @return Serialized response
     * 加密传输json类型
     */
    private <T> T postJson(String url, Type type, JsonObject jsonObject, boolean secure) throws Exception {
        addDefaultParams(jsonObject);
        String jsonString = jsonObject.toString();
        if (secure) {
//            jsonString = EPayUtils.getEncrypt(jsonString);
        }
        RequestCall call = OkHttpUtils.postString()
                .url(url)
                .headers(getHeaders())
                .content(jsonString)
                .mediaType(JSON)
                .build();
        logForRequest(url, jsonObject, null, secure);
        return doResponse(call, type, secure);
    }

    private <T> T postJson(String url, String json) throws Exception {
        RequestCall call = OkHttpUtils.postString()
                .url(url)
                .headers(getHeaders())
                .content(json)
                .mediaType(JSON)
                .build();
        logForRequest(url, new HashMap<>(), null, false);
        return doResponse(call, String.class, false);
    }


    private String getDefaultStringOnError(Throwable e) {
        String msg = "";
        if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException
                || e.toString().contains(Config.BASE_URL)
                || e.toString().contains("<head><title>404 Not Found</title></head>")
                || e.toString().contains("<head><title>502 Bad Gateway</title></head>")) {
            msg = "Network Error";
        } else {
            msg = e.getMessage();
        }
        JsonObject object = new JsonObject();
        object.addProperty("code", -1);
        object.addProperty("msg", msg);
        return object.toString();
    }

    MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    //< 同步请求post 1
    private <T> T post(String url, Type type, @Nullable Map<String, String> params, boolean secure) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        addDefaultParams(params);
        RequestCall call = OkHttpUtils.post().url(url).headers(getHeaders())
                .params(params).build();
        logForRequest(url, params, null, secure);
        return doResponse(call, type, secure);
    }

    //< 同步请求post 1
    private <T> T postFile(String url, Type type, Map<String, String> params, Map<String, File> fileMap)
            throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(url).headers(getHeaders())
                .params(params);
        for (String key : fileMap.keySet()) {
            File file = fileMap.get(key);
            if (file != null) {
                postFormBuilder.addFile(key, file.getName(), file);
            }
        }
        logForRequest(url, params, fileMap, false);
        RequestCall call = postFormBuilder.build();
        return doResponse(call, type, false);
    }

    private void logForRequest(String url, JsonObject jsonObject, Map<String, File> fileMap, boolean secure) {
        Log.i(TAG, "┌─────────────────────请求数据─────────────────────────────");
        Log.i(TAG, "│ 请求地址:" + url);
        Log.i(TAG, "│ 请求头部:" + new Gson().toJson(getHeaders()));
        Log.i(TAG, "│ 请求参数:" + jsonObject.toString());
        if (fileMap != null) {
            Log.i(TAG, "│ 请求文件:" + fileMap);
        }
        Log.i(TAG, "│ 是否加密:" + secure);
        Log.i(TAG, "└─────────────────────请求数据─────────────────────────────");
    }

    private void logForRequest(String url, Map<String, String> params, Map<String, File> fileMap, boolean secure) {
        Log.i(TAG, "┌─────────────────────请求数据─────────────────────────────");
        Log.i(TAG, "│ 请求地址:" + url);
        Log.i(TAG, "│ 请求头部:" + new Gson().toJson(getHeaders()));
        Log.i(TAG, "│ 请求参数:" + new Gson().toJson(params));
        if (fileMap != null) {
            Log.i(TAG, "│ 请求文件:" + fileMap);
        }
        Log.i(TAG, "│ 是否加密:" + secure);
        Log.i(TAG, "└─────────────────────请求数据─────────────────────────────");
    }

    private void logForResponse(String url, String res, boolean secure) {
        Log.i(TAG, "┌─────────────────────返回数据─────────────────────────────");
        Log.i(TAG, "│ 请求地址:" + url);
        Log.i(TAG, "│ 返回数据:" + res);
        Log.i(TAG, "│ 是否加密:" + secure);
        Log.i(TAG, "└─────────────────────返回数据─────────────────────────────");
    }


    private <T> T getResultInfo(String body, Type type) {
        T resultInfo;
        if (type != null) {
            resultInfo = new Gson().fromJson(body, type);
        } else {
            resultInfo = new Gson().fromJson(body, new TypeToken<T>() {
            }.getType());
        }
        return resultInfo;
    }


    public <T> Observable<T> rxPost(String url, Type type, @Nullable final Map<String, String>
            params, boolean secure) {
        Observable<T> ob = Observable.just("").map((Function<Object, T>) o -> post(url, type, params, secure))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    logForError(url, throwable, secure);
                    return getResultInfo(getDefaultStringOnError(throwable), type);
                });
        Activity a = ActivityUtils.getActivityByContext(getContext());
        if (a instanceof RxAppCompatActivity) {
            RxAppCompatActivity rx = (RxAppCompatActivity) a;
            return ob.doOnDispose(() -> {
                logForResponse(url, rx.getClass().getSimpleName() + "已销毁，请求自动取消", secure);
            })
                    .compose(rx.bindToLifecycle());
        }
        return ob;
    }

    public <T> Observable<T> rxPostJson(String url, Type type, Map<String, String> params, boolean secure) {
        Observable<T> ob = Observable.just("").map((Function<Object, T>) o -> postJson(url, type, params, secure))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    logForError(url, throwable, secure);
                    return getResultInfo(getDefaultStringOnError(throwable), type);
                });
        Activity a = ActivityUtils.getActivityByContext(getContext());
        if (a instanceof RxAppCompatActivity) {
            RxAppCompatActivity rx = (RxAppCompatActivity) a;
            return ob.doOnDispose(() -> {
                logForResponse(url, rx.getClass().getSimpleName() + "已销毁，请求自动取消", secure);
            }).compose(rx.bindToLifecycle());
        }
        return ob;
    }


    public <T> Observable<T> rxPostJson2(String url, Type type, JsonObject params, boolean secure) {
        Observable<T> ob = Observable.just("").map((Function<Object, T>) o -> postJson(url, type, params, secure))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    logForError(url, throwable, secure);
                    return getResultInfo(getDefaultStringOnError(throwable), type);
                });
        Activity a = ActivityUtils.getActivityByContext(getContext());
        if (a instanceof RxAppCompatActivity) {
            RxAppCompatActivity rx = (RxAppCompatActivity) a;
            return ob.doOnDispose(() -> {
                logForResponse(url, rx.getClass().getSimpleName() + "已销毁，请求自动取消", secure);
            }).compose(rx.bindToLifecycle());
        }
        return ob;
    }


    public <T> Observable<T> rxPostFile(String url, Type type, final Map<String, String>
            params, Map<String, File> file) {
        Observable<T> ob = Observable.just("").map((Function<String, T>) s -> postFile(url, type, params, file))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> {
                    logForError(url, throwable, false);
                    return getResultInfo(getDefaultStringOnError(throwable), type);
                });
        Activity a = ActivityUtils.getActivityByContext(getContext());
        if (a instanceof RxAppCompatActivity) {
            RxAppCompatActivity rx = (RxAppCompatActivity) a;
            return ob.doOnDispose(() -> {
                logForResponse(url, rx.getClass().getSimpleName() + "已销毁，请求自动取消", false);
            }).compose(rx.bindToLifecycle());
        }
        return ob;
    }

    private void logForError(String url, Throwable throwable, boolean secure) {
        logForResponse(url, throwable.getMessage(), secure);
    }

}

