package com.example.itread;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itread.Adapter.BookAdapter;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookActivity extends AppCompatActivity {

    //其他的
    private List<Map<String, Object>> list = new ArrayList<>();
    private TextView book_label;
    private ImageView back;
    private RecyclerView recyclerView;
    private String book_id;
    private SharedPreferencesUtil check;
    private String number;      //短评是否为空
    private RefreshLayout refreshLayout;


    //flag
    private boolean flag = false;


    //有关书的
    private String title;
    private String image;
    private String info;
    private String introduce;
    private String score;
    private String done;
    private String progress;
    private String want;
    private String people;

    //有关短评的
    private String s_image;
    private String s_name;
    private String s_content;
    private String s_score;
    private String s_time;

    //图书状态
    private String status = "3";

    //加载数据
    private int array_size = 0;
    private int now_position = 0;
    private BookAdapter bookAdapter;

    //计算分数
    private int all_number = 0;
    private float all_score = (float) 0.0;
    private String flag_short_comments;
    private String flag_book_comments;
    private float final_score = (float)0.0;

    //判断联网
    private boolean isNet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        book_label = (TextView) findViewById(R.id.book_label);
        back = (ImageView) findViewById(R.id.back_from_book);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_book);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        isNet = HttpUtil.isNetworkConnected(BookActivity.this);

        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //接受书的id
        Intent intent = getIntent();
        book_id = intent.getStringExtra("book_id");
        if(isNet){
            //联网请求获得图书信息

            bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);

            //计算书籍评分
            bookScoreWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);


            //判断用户是否登录以显示想读已读在读按钮的状态

            if (check.isLogin()) {

                String a = SharedPreferencesUtil.getCookie();
                Log.d("cookie", a);
                getStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id);

            }
        }else {
            Toast.makeText(BookActivity.this,"网络不给力哦~ 请检查您的网络设置qwq",Toast.LENGTH_LONG).show();
        }
        //刷新的监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isNet = HttpUtil.isNetworkConnected(BookActivity.this);
                if(isNet){
                    list.clear();
                    all_number = 0;
                    all_score = (float)0.0;
                    final_score = (float)0.0;
                    bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);
                    //计算书籍评分
                    bookScoreWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);
                    //判断用户是否登录以显示想读已读在读按钮的状态
                    if (check.isLogin()) {
                        String a = SharedPreferencesUtil.getCookie();
                        Log.d("cookie", a);
                        getStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                    recyclerView.setAdapter(new BookAdapter(BookActivity.this, list, book_id, status, check, number,String.valueOf(all_score),String.valueOf(all_number)));
                    refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                }else {
                    Toast.makeText(BookActivity.this,"刷新失败了qwq，请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //重写onResume方法
    @Override
    protected void onResume() {
        super.onResume();

//        //清楚列表内容
//        list.clear();
//
//        //联网请求获得图书信息
//        bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);
//
//        //判断用户是否登录以显示想读已读在读按钮的状态
////        if(check.isLogin()){
//
//        getStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id);
////        }


    }

    //获得图书信息的方法
    public void bookWithOkHttp(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookActivity.this,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("aaaaa", responseData);
                try {
                    //解析book
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("book");
                    title = jsonObject1.getString("title");
                    info = jsonObject1.getString("info");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("img");   //image
                    image = jsonObject2.getString("img_s");               //image
                    introduce = jsonObject1.getString("introduce");
                    score = jsonObject1.getString("score");     //score
                    done = jsonObject1.getString("done");
                    progress = jsonObject1.getString("progress");
                    want = jsonObject1.getString("want");
                    people = jsonObject1.getString("people");

                    Map map = new HashMap();
                    map.put("title", title);
                    map.put("info", info);
                    map.put("image", image);
                    map.put("introduce", introduce);
                    map.put("score", score);
                    map.put("done", done);
                    map.put("progress", progress);
                    map.put("want", want);
                    map.put("people", people);
                    map.put("type", 1);
                    list.add(map);

                    //添加一行空白间隔
                    Map map1 = new HashMap();
                    map1.put("type", 2);
                    list.add(map1);

                    //添加输入短评框
                    Map map2 = new HashMap();
                    map2.put("type", 3);
                    list.add(map2);

                    //添加一行空白间隔
                    list.add(map1);

                    //判断是否有短评
                    JSONObject jsonObject4 = jsonObject.getJSONObject("number");
                    number = jsonObject4.getString("shortcomments");
                    Log.d("是否有短评", number);
                    if (number.equals("0")) {
                        Map map3 = new HashMap();
                        map3.put("type", 5);
                        list.add(map3);
                    } else {
                        //解析短评
                        JSONArray jsonArray = jsonObject.getJSONArray("shortcomments");
                        array_size = jsonArray.length();
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            s_image = jsonObject3.getString("image");
                            s_name = jsonObject3.getString("name");
                            s_time = jsonObject3.getString("time");
                            s_score = jsonObject3.getString("score");
                            s_content = jsonObject3.getString("content");

                            Map map3 = new HashMap();
                            map3.put("s_image", s_image);
                            map3.put("s_name", s_name);
                            map3.put("s_time", s_time);
                            map3.put("s_score", s_score);
                            map3.put("s_content", s_content);
                            map3.put("type", 4);

                            list.add(map3);

                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                            recyclerView.setAdapter(new BookAdapter(BookActivity.this, list, book_id, status, check, number,String.valueOf(all_score),String.valueOf(all_number)));
                            book_label.setText(title);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获得图书状态的方法
    public void getStatusWithOkHttp(String address) {
        HttpUtil.getStatusWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookActivity.this,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
//                responseData = removeBOM(responseData);
//                if(responseData != null && responseData.startsWith("\ufeff"))
//                    responseData = responseData.substring(1);
                Log.d("获取图书状态：", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    status = jsonObject.getString("result");
                    Log.d("图书状态：", status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                            recyclerView.setAdapter(new BookAdapter(BookActivity.this, list, book_id, status, check, number,String.valueOf(all_score),String.valueOf(all_number)));



                        }
                    });
                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        });
    }

    //计算书籍得分的方法
    public void bookScoreWithOkHttp(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookActivity.this,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("aaaaa", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    //判断是否有短评
                    JSONObject jsonObject4 = jsonObject.getJSONObject("number");
                    flag_short_comments = jsonObject4.getString("shortcomments");
                    flag_book_comments = jsonObject4.getString("bookcomments");
                    if(flag_short_comments.equals("0") && flag_book_comments.equals("0"))
                        final_score = (float)5.0;
                    else if(flag_book_comments.equals("0") && flag_short_comments.equals("1")){
                        String temp;
                        JSONArray jsonArray = jsonObject.getJSONArray("shortcomments");
                        for(int i = 0; i < jsonArray.length() ; i ++){
                            all_number ++;
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            temp = jsonObject3.getString("score");
                            all_score += Float.valueOf(temp);
                        }
                        final_score = all_score / all_number;
                    }else if(flag_book_comments.equals("1") && flag_short_comments.equals("0")){
                        String temp;
                        JSONArray jsonArray = jsonObject.getJSONArray("bookcomments");
                        for(int i = 0; i < jsonArray.length() ; i ++){
                            all_number ++;
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            temp = jsonObject3.getString("score");
                            all_score += Float.valueOf(temp);
                        }
                        final_score = all_score / all_number;
                    }else {
                        String temp;
                        JSONArray jsonArray = jsonObject.getJSONArray("shortcomments");
                        for(int i = 0; i < jsonArray.length() ; i ++){
                            all_number ++;
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            temp = jsonObject3.getString("score");
                            all_score += Float.valueOf(temp);
                        }
                        JSONArray jsonArray1 = jsonObject.getJSONArray("bookcomments");
                        int len2 = jsonArray1.length();
                        for(int i = 0; i < len2 ; i ++){
                            all_number ++;
                            JSONObject jsonObject7 = jsonArray1.getJSONObject(i);
                            temp = jsonObject7.getString("score");
                            all_score += Float.valueOf(temp);
                        }
                        final_score = all_score / all_number;
                    }



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                            recyclerView.setAdapter(new BookAdapter(BookActivity.this, list, book_id, status, check, number,String.valueOf(all_score),String.valueOf(all_number)));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final String removeBOM(String data) {

        if (TextUtils.isEmpty(data)) {

            return data;

        }


        if (data.startsWith("\ufeff")) {

            return data.substring(1);

        } else {

            return data;

        }

    }

}
