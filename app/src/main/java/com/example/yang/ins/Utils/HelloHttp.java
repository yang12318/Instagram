package com.example.yang.ins.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by youxihouzainali on 2018/5/14.
 */

public class HelloHttp {
    public static void sendOkHttpRequest(String adress, okhttp3.Callback callback) {
        String url = null;
        //按照开拓的方式处理url
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);

    }
}
