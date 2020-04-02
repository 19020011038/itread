package com.example.itread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BookIntroductionActivity extends AppCompatActivity {
    private ImageView back;
    private TextView textView;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_introdunction);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        back = (ImageView)findViewById(R.id.back_from_book_introduction);
        textView = (TextView)findViewById(R.id.book_introduction_text);

        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //显示文本内容
        Intent intent = getIntent();
        text = intent.getStringExtra("book_introduction");
        textView.setText(text);
    }
}
