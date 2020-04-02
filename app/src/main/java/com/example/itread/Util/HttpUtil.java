package com.example.itread.Util;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    //登录
    public static void loginWithOkHttp(String address, String account, String password, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("username",account)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    //注册
    public static void registerWithOkHttp(String address, String account, String email, String password, String repassword, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("username", account)
                .add("email", email)
                .add("password", password)
                .add("repassword", repassword)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    //获得书记详情页的书籍信息    GET
    public static void bookWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }


    //获得状态     GET
    public static void getStatusWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //更改图书状态  POST
    public static void changeStatusWithOkHttp(String address,String status, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("status",status)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //发布评论  POST
    public static void publishCommentsWithOkHttp(String address, String status,String title,String content,String score, String book_num, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("status",status)
                .add("title",title)
                .add("content",content)
                .add("score",score)
                .add("book_num",book_num)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
