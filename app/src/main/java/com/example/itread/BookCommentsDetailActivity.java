package com.example.itread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.itread.Util.HttpUtil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookCommentsDetailActivity extends AppCompatActivity {
    private String position;
    private String book_id;

    //绑定控件
    private ImageView back;
    private TextView book_comments_detail_title;
    private ImageView book_comments_detail_image;
    private TextView book_comments_detail_name;
    private TextView book_comments_detail_time;
    private RatingBar book_comments_detail_ratingBar;
    private TextView book_comments_detail_score;
    private TextView book_comments_detail_richtext;

    //数据
    private String title;
    private String image;
    private String name;
    private String time;
    private String score;
    private String richtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_comments_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        back = (ImageView)findViewById(R.id.back_from_book_comments_detail);
        book_comments_detail_image = (ImageView)findViewById(R.id.book_comments_detail_image);
        book_comments_detail_name = (TextView)findViewById(R.id.book_comments_detail_name);
        book_comments_detail_ratingBar = (RatingBar)findViewById(R.id.book_comments_detail_rating);
        book_comments_detail_richtext = (TextView)findViewById(R.id.book_comments_detail_richtext);
        book_comments_detail_score = (TextView)findViewById(R.id.book_comments_detail_score);
        book_comments_detail_time = (TextView)findViewById(R.id.book_comments_detail_time);
        book_comments_detail_title = (TextView)findViewById(R.id.book_comments_detail_title);
        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //接受数据
        Bundle bundle = getIntent().getExtras();
        book_id = bundle.getString("book_id");
        position = bundle.getString("position");

        //初始化Richtext
        RichText.initCacheDir(this);

        //请求网络
        bookWithOkHttp("http://47.102.46.161/AT_read/book/?num="+book_id);
    }
    //获得图书信息的方法
    public void bookWithOkHttp(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("rrrrrrrrrrrrrrrrrrrr",responseData);

                try {
                    //解析book
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("bookcomments");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(Integer.parseInt(position));
                    image = jsonObject1.getString("image");
                    name = jsonObject1.getString("name");
                    richtext = jsonObject1.getString("content");
                    title = jsonObject1.getString("title");
                    score = jsonObject1.getString("score");
                    time = jsonObject1.getString("time");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            book_comments_detail_title.setText(title);
                            Glide.with(BookCommentsDetailActivity.this).load(image).into(book_comments_detail_image);
                            book_comments_detail_name.setText(name);
                            book_comments_detail_score.setText(score);
                            book_comments_detail_time.setText(time);
//                            book_comments_detail_ratingBar.setRating(Float.parseFloat(score));
                            RichText.from(richtext).into(book_comments_detail_richtext);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
