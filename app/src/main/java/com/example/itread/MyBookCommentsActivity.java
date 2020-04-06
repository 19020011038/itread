package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itread.Adapter.MyBookCommentsAdapter;
import com.example.itread.Util.HttpUtil;

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

public class MyBookCommentsActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private Map map;
    private RelativeLayout mybook_back;

    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_book_comments);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id. mybook_comment_recyclerview);
        mybook_back = findViewById(R.id.mybook_back);

        mybook_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mybookCommentWithOkHttp("http://47.102.46.161/user/comment_request");
    }

    public void mybookCommentWithOkHttp(String address){
        HttpUtil.mybookCommentWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " mybookComment : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try{
                    JSONObject object = new JSONObject(responseData);
                    JSONArray jsonArray = object.getJSONArray("bookcomments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                        String status = jsonObject1.getString("status");
                        String content = jsonObject1.getString("content");  //头像
                        String score = jsonObject1.getString("score");
                        String time = jsonObject1.getString("time");
                        String book_num = jsonObject1.getString("book_num");
                        String book_name = jsonObject1.getString("book_name");
                        String book_photo = jsonObject1.getString("book_photo");
                        map = new HashMap();

                        map.put("status", status);
                        map.put("content", content);
                        map.put("score", score);
                        map.put("time", time);
                        map.put("book_name", book_name);
                        map.put("book_num", book_num);
                        map.put("book_photo", book_photo);

                        list.add(map);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerView.setLayoutManager(new LinearLayoutManager(MyBookCommentsActivity.this));//纵向
                                recyclerView.setAdapter(new MyBookCommentsAdapter(MyBookCommentsActivity.this, list));
                                recyclerView.setNestedScrollingEnabled(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
            }//标签页
        });
    }

}
