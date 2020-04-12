package com.example.itread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.itread.Adapter.BooklistAdapter;
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

public class BookListActivity extends AppCompatActivity {

    private List<Map<String, Object>> list = new ArrayList<>();
    private SharedPreferencesUtil check;
    private RecyclerView recyclerView;

    private TextView name;
    private ImageView image;
    private TextView number;
    private TextView content;
    private TextView author;

    private String name1;
    private String image1;
    private String number1;
    private String content1;

    private String title2;
    private String image2;
    private String author2;
    private String content2;

    private String list_id;
    private String book_id;
    private String test;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_booklist);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
//        check.setfragid("2");
//        Intent intent1 = new Intent();
//        intent1.putExtra("frag_id", "2");
//        //设置响应码 和意图
//        setResult(100, intent1);
//        //摧毁一个Activity

        name = findViewById(R.id.title);
        content = findViewById(R.id.list_introduce);
        image = findViewById(R.id.pic);
        recyclerView = findViewById(R.id.recyclerView);
        number = findViewById(R.id.sum);
        imageView = findViewById(R.id.back_from_book);

        Intent intent = getIntent();
        test = intent.getStringExtra("1231232");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_id = intent.getStringExtra("list_id");


    }


    @Override
    protected void onResume() {
        super.onResume();
        //清楚列表内容
        list.clear();

        //联网请求获得图书信息
        BooklistWithOkHttp("http://47.102.46.161/AT_read/book_list1/?list_id="+list_id);
    }

    //获得图书信息的方法
    public void BooklistWithOkHttp(String address){
        HttpUtil.BooklistWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {
                    //解析book
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("book_list");

                    name1 = jsonObject1.getString("name");
                    number1 = jsonObject1.getString("number");
                    image1 = jsonObject1.getString("image");
                    content1 = jsonObject1.getString("content");



                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        book_id = jsonObject3.getString("num");
                        title2 = jsonObject3.getString("title");
                        content2 = jsonObject3.getString("content");
                        image2 = jsonObject3.getString("image");
                        author2 = jsonObject3.getString("author");

                        Map map1 = new HashMap();

                        map1.put("title",title2);
                        map1.put("content",content2);
                        map1.put("image",image2);
                        map1.put("book_id",book_id);
                        map1.put("author",author2);

                        list.add(map1);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            name.setText(name1);
                            content.setText(content1);
                            number.setText("共"+number1+"本书");

                            String picture_1 = image1.replace("\\","");
                            String picture_2 = picture_1.replace("\"","");
                            String picture_3 = picture_2.replace("[","");
                            String picture_4 = picture_3.replace("]","");


                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.logo)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE);
                            Glide.with(BookListActivity.this)
                                    .load(picture_4)
                                    .apply(options)
                                    .into(image);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookListActivity.this));
                            recyclerView.setAdapter(new BooklistAdapter(BookListActivity.this, list));

                            recyclerView.setNestedScrollingEnabled(false);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

}
