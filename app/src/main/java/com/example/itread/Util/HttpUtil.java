package com.example.itread.Util;

import java.io.File;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {



    //新书速递    GET
    public static void NewbookWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //booklist book_inlist
    public static void BooklistWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client1 = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client1.newCall(request).enqueue(callback);
    }


    //登录
    public static void loginWithOkHttp(String address, String account, String password, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
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
//                .add("repassword", repassword)
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
                .header("Cookie",SharedPreferencesUtil.getCookie())
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
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    //更改图书评分  POST
    public static void changeScoreWithOkHttp(String address,String score, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("score",score)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    //修改密码post请求
    public static void ChangePasswordWithOkHttp(String address, String old_password, String new_password, String renew_password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("old_password", old_password)
                .add("new_password", new_password)
//                .add("re_password", renew_password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
//        client.newCall(request).enqueue(callback);
        Call call = client.newCall(request);
        //5.请求加入调度,重写回调方法
        call.enqueue(callback);
    }

    //home页get头像昵称
    public static void homeNameOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //post传email验证码
    public static void emailWithOkHttp(String address, String email_num, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("code",email_num)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //修改昵称
    public static void nicknameWithOkHttp(String address, String new_nickname, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name",new_nickname)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //退出登录
    public static void signoutWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //忘记密码
    public static void findWithOkHttp(String address, String account, String email, String password, String repassword, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", account)
                .add("email", email)
                .add("new_password", password)
//                .add("re_password", repassword)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //post传照片文件
    public static void iconWithOkHttp(String address, File file, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIATYPE = MediaType.parse("image/jpeg; charset=utf-8");
        RequestBody body = RequestBody.create(MEDIATYPE, file);
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //发布评论  POST
    public static void publishCommentsWithOkHttp(String address,String title,String content,String score, String book_num, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("title",title)
                .add("content",content)
                .add("score",score)
                .add("book_num",book_num)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //上传图片获得url   POST
    public static void postFileWithOkHttp(String address,File file, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //上传头像获得url   POST
    public static void userIconWithOkHttp(String address,File file, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .post(multipartBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    //我的书评get
    public static void mybookCommentWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }

    //个人中心想读已读在读get
    public static void WantReadWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .header("Cookie",SharedPreferencesUtil.getCookie())
                .build();
        client.newCall(request).enqueue(callback);
    }


}
