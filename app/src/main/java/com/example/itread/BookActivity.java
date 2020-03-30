package com.example.itread;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;

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
    private List<Map<String, Object>> list = new ArrayList<>();
    TextView book_label;
    ImageView back;
    RecyclerView recyclerView;
    String book_id;
    String url_book;
    String title;
    String image;
    String info;
    String introduce;
    String score;
    String done;
    String progress;
    String want;
    String people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        book_label = (TextView)findViewById(R.id.book_label);
        back = (ImageView)findViewById(R.id.back_from_book);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_book);
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
    }
    //重写onResume方法
    @Override
    protected void onResume(){
        super.onResume();
        //联网请求

    }
    public void bookWithOkHttp(String address){
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Toast.makeText(BookActivity.this,"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("book");
                    title = jsonObject1.getString("title");
                    info = jsonObject1.getString("info");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("image");
                    image = jsonObject2.getString("img_s");
                    introduce = jsonObject1.getString("introduce");
                    score = jsonObject1.getString("score");
                    done = jsonObject1.getString("done");
                    progress = jsonObject1.getString("progress");
                    want = jsonObject1.getString("want");
                    people = jsonObject1.getString("people");
                    Map map = new HashMap();
                    map.put("title",title);
                    map.put("info",info);
                    map.put("image",image);
                    map.put("introduce",introduce);
                    map.put("score",score);
                    map.put("done",done);
                    map.put("progress",progress);
                    map.put("want",want);
                    map.put("people",people);
                    map.put("type",1);
                    list.add(map);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
