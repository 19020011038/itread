package com.example.itread.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookIntroductionActivity;
import com.example.itread.HomeActivity;
import com.example.itread.LoginActivity;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private String book_id;
    private String status;
    private float book_score;    //书籍评分
    private float origin_score = (float)0.0;     //短评原始评分
    private float step_score = (float)0.5;       //短评打分增长步伐
    private float user_score;                    //用户的短评评分
    private String user_score_string;            //用户的短评评分的string类型
    private String user_short_comments;
    private SharedPreferencesUtil check;
    private String result;
    public Context context;
    public final int BASIC_VIEW = 1;
    public final int EMPTY_VIEW = 2;
    public final int WRITE_SHORT_COMMENTS_VIEW = 3;
    public final int SHORT_COMMENTS_VIEW = 4;

    private String header;

    public BookAdapter(Context context, List<Map<String, Object>> list, String book_id, String status, SharedPreferencesUtil check) {
        this.context = context;
        this.list = list;
        this.book_id = book_id;
        this.status = status;
        this.check = check;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(list.get(position).get("type").toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == BASIC_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_basic, parent, false);
            return new BasicViewHolder(view);
        } else if (viewType == EMPTY_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        } else if (viewType == WRITE_SHORT_COMMENTS_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_write_short_comments, parent, false);
            return new WriteShortCommentsViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_short_comments, parent, false);
            return new ShortCommentsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BasicViewHolder) {
            //  item_book_basic
            final BasicViewHolder viewHolder = (BasicViewHolder) holder;
            Glide.with(context).load(list.get(position).get("image").toString()).into(viewHolder.book_image);
            viewHolder.book_info.setText(list.get(position).get("info").toString());

            //初始化想读在读已读,并设置监听
            if (check.isLogin()) {
                //想读按钮
                viewHolder.book_want.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();

                        } else if (status.equals("1")) {
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu);
                            viewHolder.book_reading.invalidate();
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();
                        } else if(status.equals("2")){
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu);
                            viewHolder.book_done.invalidate();
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();
                        }else {
                            Toast.makeText(BookAdapter.this.context,"您已将该书添加至想读",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //在读按钮
                viewHolder.book_reading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();

                        } else if (status.equals("2")) {
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu);
                            viewHolder.book_done.invalidate();
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();
                        } else if(status.equals("0")){
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu);
                            viewHolder.book_want.invalidate();
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();
                        }else {
                            Toast.makeText(BookAdapter.this.context,"您已将该书添加至在读",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //已读按钮
                viewHolder.book_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();

                        } else if (status.equals("0")) {
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu);
                            viewHolder.book_want.invalidate();
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();
                        } else if(status.equals("1")){
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/At_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu);
                            viewHolder.book_reading.invalidate();
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();
                        }else {
                            Toast.makeText(BookAdapter.this.context,"您已将该书添加至在读",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (status.equals("0")) {
                    viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                    viewHolder.book_want.invalidate();
                } else if (status.equals("1")) {
                    viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                    viewHolder.book_reading.invalidate();
                } else if (status.equals("2")) {
                    viewHolder.book_done.setImageResource(R.drawable.yidu2);
                    viewHolder.book_done.invalidate();
                }
            } else {
                //未登录时想读已读在读按钮的点击事件
                viewHolder.book_want.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context,"请您登录后再进行次操作！",Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.book_reading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context,"请您登录后再进行次操作！",Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.book_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context,"请您登录后再进行次操作！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //该到对评分的操作了
            book_score = Float.parseFloat(list.get(position).get("score").toString());
            viewHolder.book_rating_1.setRating(book_score);
            viewHolder.book_score.setText(list.get(position).get("score").toString());
            viewHolder.book_people.setText(list.get(position).get("people").toString());

            //简介
            viewHolder.book_intro.setText(list.get(position).get("introduce").toString());
            viewHolder.book_introduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookAdapter.this.context, BookIntroductionActivity.class);
                    intent.putExtra("book_introduction",list.get(position).get("introduce").toString());
                    context.startActivity(intent);
                }
            });

            //跳转到全部书评的按钮
            viewHolder.jump_book_book_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("book_id",book_id);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof EmptyViewHolder) {

            //空白条

        } else if (holder instanceof WriteShortCommentsViewHolder) {
            //  item_book_write_short_comments
            WriteShortCommentsViewHolder viewHolder = (WriteShortCommentsViewHolder) holder;
            //打分
            viewHolder.book_rating_2.setRating(origin_score);
            viewHolder.book_rating_2.setStepSize(step_score);
            viewHolder.book_rating_2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    user_score = rating;
                }
            });
            //写短评
            user_short_comments = viewHolder.book_write_short_comments.getText().toString();
            //发布短评
            viewHolder.book_publish_short_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user_short_comments.equals(null))
                        Toast.makeText(BookAdapter.this.context,"请您输入评论后再发布",Toast.LENGTH_SHORT).show();
                    else
                    {
                        user_score_string = Float.toString(user_score);
                        publishCommentsWithOkHttp("http://47.102.46.161/AT_read/book_review/?num="+book_id+"&type='s'",status," ",user_short_comments,user_score_string,book_id);
                    }
                }
            });
        }else {
            //    item_book_short_comments
            final ShortCommentsViewHolder viewHolder = (ShortCommentsViewHolder) holder;
            Glide.with(context).load(list.get(position).get("s_image").toString()).into(viewHolder.book_short_comments_image);
            viewHolder.book_short_comments_name.setText(list.get(position).get("s_name").toString());
            viewHolder.book_short_comments_time.setText(list.get(position).get("s_time").toString());
            viewHolder.book_rating_3.setRating(Float.parseFloat(list.get(position).get("s_score").toString()));
            viewHolder.book_show_short_rating.setText(list.get(position).get("s_score").toString());
            viewHolder.book_short_comments_content.setText(list.get(position).get("s_content").toString());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeStatusWithOkHttp(String address, final String status) {
        HttpUtil.changeStatusWithOkHttp(address, status, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                header = response.header("set-cookie");
                try {
                    //解析cookie
                    String JSESSIONID=header.substring(0, 43);
                    check.setCookie(true);//设置已获得cookie
                    check.saveCookie(JSESSIONID);//保存获得的cookie
                    String show = null;
                    if(status.equals("0"))
                        show = "想读";
                    else if(status.equals("1"))
                        show = "在读";
                    else if(status.equals("2"))
                        show = "已读";
                    else
                        Toast.makeText(BookAdapter.this.context,"错误",Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(responseData);
                    Toast.makeText(BookAdapter.this.context, "您已将该书添加至"+show, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void publishCommentsWithOkHttp(String address,String status,String title,String content,String score, String book_num) {
        HttpUtil.publishCommentsWithOkHttp(address, status,title,content,score,book_num, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                header = response.header("set-cookie");
                try {
                    //解析cookie
                    String JSESSIONID=header.substring(0, 43);
                    check.setCookie(true);//设置已获得cookie
                    check.saveCookie(JSESSIONID);//保存获得的cookie
                    JSONObject jsonObject = new JSONObject(responseData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class BasicViewHolder extends RecyclerView.ViewHolder {
    public ImageView book_image;
    public TextView book_info;
    public ImageView book_want;
    public TextView book_want_number;
    public ImageView book_reading;
    public TextView book_reading_number;
    public ImageView book_done;
    public TextView book_done_number;
    public RatingBar book_rating_1;
    public TextView book_score;
    public TextView book_people;
    public TextView book_intro;
    public Button jump_book_book_comments;
    public View book_introduction;

    BasicViewHolder(@NonNull View itemView) {
        super(itemView);
        book_image = itemView.findViewById(R.id.book_image);
        book_info = itemView.findViewById(R.id.book_info);
        book_want = itemView.findViewById(R.id.book_want);
        book_want_number = itemView.findViewById(R.id.book_want_number);
        book_reading = itemView.findViewById(R.id.book_reading);
        book_reading_number = itemView.findViewById(R.id.book_reading_number);
        book_done = itemView.findViewById(R.id.book_done);
        book_done_number = itemView.findViewById(R.id.book_done_number);
        book_rating_1 = itemView.findViewById(R.id.book_rating_1);
        book_score = itemView.findViewById(R.id.book_score);
        book_people = itemView.findViewById(R.id.book_people);
        book_intro = itemView.findViewById(R.id.book_intro);
        jump_book_book_comments = itemView.findViewById(R.id.jump_book_book_comments);
        book_introduction = itemView.findViewById(R.id.book_introduction);
    }
}

class EmptyViewHolder extends RecyclerView.ViewHolder {
    EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

class WriteShortCommentsViewHolder extends RecyclerView.ViewHolder {
    public RatingBar book_rating_2;
    public EditText book_write_short_comments;
    public Button book_publish_short_comments;


    WriteShortCommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        book_rating_2 = itemView.findViewById(R.id.book_rating_2);
        book_write_short_comments = itemView.findViewById(R.id.book_write_short_comments);
        book_publish_short_comments = itemView.findViewById(R.id.book_publish_short_comments);

    }
}

class ShortCommentsViewHolder extends RecyclerView.ViewHolder {
    public ImageView book_short_comments_image;
    public TextView book_short_comments_name;
    public RatingBar book_rating_3;
    public TextView book_show_short_rating;
    public TextView book_short_comments_content;
    public TextView book_short_comments_time;

    ShortCommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        book_short_comments_image = itemView.findViewById(R.id.book_short_comments_image);
        book_short_comments_name = itemView.findViewById(R.id.book_short_comments_name);
        book_rating_3 = itemView.findViewById(R.id.book_rating_3);
        book_show_short_rating = itemView.findViewById(R.id.book_show_short_rating);
        book_short_comments_content = itemView.findViewById(R.id.book_short_comments_content);
        book_short_comments_time = itemView.findViewById(R.id.book_short_comments_time);
    }
}


