package com.nerv.quickweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by NERV on 2017/12/5.
 * 调用sendOkHttpRequest()发起Http请求
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
