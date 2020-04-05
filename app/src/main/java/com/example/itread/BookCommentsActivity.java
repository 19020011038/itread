package com.example.itread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itread.Adapter.BookCommentsAdapter;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

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

public class BookCommentsActivity extends AppCompatActivity {
    //其他的
    private List<Map<String, Object>> list = new ArrayList<>();
    private ImageView back;
    private RecyclerView recyclerView;
    private String book_id ;
    private String user_status;
    private SharedPreferencesUtil check;
    private ImageView jump_book_comments_write;
    private String number;                           //书评数目


    //长评的内容
    private String image;
    private String name;
    private String status;
    private String content;
    private String word;
    private String title;
    private String score;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_comments);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        //返回按钮
        back = (ImageView)findViewById(R.id.back_from_book_comments);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_book_comments);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());

        //接受书的id
        Intent intent = getIntent();
        book_id = intent.getStringExtra("book_id");



        //跳转到写书评
        jump_book_comments_write = (ImageView)findViewById(R.id.jump_book_comments_write);
        jump_book_comments_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check.isLogin())
                    Toast.makeText(BookCommentsActivity.this,"请您登录后再进行此操作",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent1 = new Intent(BookCommentsActivity.this,WriteBookCommentsActivity.class);
                    intent1.putExtra("book_id",book_id);
                    startActivity(intent1);
                }
            }
        });
    }
    //重写onResume方法
    @Override
    protected void onResume(){
        super.onResume();
        //清空列表
        list.clear();
        //联网请求获得图书信息
        bookWithOkHttp("http://47.102.46.161/AT_read/book/?num="+book_id);
    }

    //获得图书信息的方法
    public void bookWithOkHttp(String address){
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject2 = jsonObject.getJSONObject("number");
                    number = jsonObject2.getString("bookcomments");
                    if(number.equals("0")){
                        Map map = new HashMap();
                        map.put("type",2);
                        list.add(map);
                    }else{
                        //解析长评
                        JSONArray jsonArray = jsonObject.getJSONArray("bookcomments");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            image = jsonObject1.getString("image");
                            name = jsonObject1.getString("name");
                            status = jsonObject1.getString("status");
                            content = jsonObject1.getString("content");
                            word = jsonObject1.getString("word");
                            title = jsonObject1.getString("title");
                            score = jsonObject1.getString("score");
                            time = jsonObject1.getString("time");
                            Map map = new HashMap();
                            map.put("image",image);
                            map.put("name",name);
                            map.put("status",status);
                            map.put("content",content);
                            map.put("word",word);
                            map.put("title",title);
                            map.put("score",score);
                            map.put("time",time);
                            map.put("type",1);
                            list.add(map);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookCommentsActivity.this));//垂直排列 , Ctrl+P
                            recyclerView.setAdapter(new BookCommentsAdapter(BookCommentsActivity.this, list,book_id));//绑定适配器
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
