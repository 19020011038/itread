package com.example.itread;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.itread.Adapter.BookAdapter;
import com.example.itread.Adapter.BooklistAdapter;
import com.example.itread.Base.BaseFragment;
import com.example.itread.Ui.fragment.guide.NewBookFragment;
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

    private ImageView picture;
    private TextView name;
    private TextView num;
    private String book_id;
    private TextView introduce;
    private String list_id;

    private String s_name;
    private String s_num;
    private String s_picture;
    private String s_introduce;

    private String author_score;
    private String author_name;
    private String author_year;
    private String author_publish;

    private JSONObject object;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private BaseFragment NewBookFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_booklist);

        name = findViewById(R.id.title);
        num = findViewById(R.id.sum);
        introduce = findViewById(R.id.list_introduce);
        picture = findViewById(R.id.pic);
        recyclerView = findViewById(R.id.recyclerView);


    }


    @Override
    protected void onResume() {
        super.onResume();
        //清楚列表内容
        list.clear();

        list_id = "0";

        //联网请求获得图书信息
        BooklistWithOkHttp("http://47.102.46.161/AT_read/book_list1/?list_id=0");


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

                    s_name = jsonObject1.getString("name");
                    s_num = jsonObject1.getString("number");
                    s_picture = jsonObject1.getString("image");
                    s_introduce = jsonObject1.getString("content");

                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        book_id = jsonObject3.getString("num");
                        s_name = jsonObject3.getString("title");
                        s_introduce = jsonObject3.getString("content");
                        s_picture = jsonObject3.getString("image");

//                        Log.d("qshqsh", s_name);

//                        object = jsonObject3.getJSONObject("author");
//                        JSONArray jsonArray2 = object.getJSONArray("author");
//
//                        author_score = jsonArray2.getString(0);
//                        author_name = jsonArray2.getString(1);
//                        author_publish = jsonArray2.getString(2);
//                        author_year = jsonArray2.getString(3);

                        Map map1 = new HashMap();
                        map1.put("s_picture",s_picture);
                        map1.put("s_name",s_name);
                        map1.put("s_introduce",s_introduce);
                        map1.put("book_id",book_id);
//                        map1.put("author_score",author_score);
//                        map1.put("author_name",author_name);
//                        map1.put("author_publish",author_publish);
//                        map1.put("author_year",author_year);
                        list.add(map1);
//
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            name.setText(s_name);
                            num.setText(s_num);
                            introduce.setText(s_introduce);

                            String picture_1 = s_picture.replace("\\","");
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
                                    .into(picture);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BookListActivity.this));
                            recyclerView.setAdapter(new BooklistAdapter(BookListActivity.this, list));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }



//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
////        mainActivity.switchFragment(NewBookFragment);
//        Intent intent = new Intent(BookListActivity.this,MainActivity.class);
//        startActivity(intent);
//
//    }


}
