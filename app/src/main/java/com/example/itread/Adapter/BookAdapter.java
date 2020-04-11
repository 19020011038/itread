package com.example.itread.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookCommentsActivity;
import com.example.itread.BookIntroductionActivity;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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
    private float origin_score = (float) 5.0;     //短评原始评分
    private float step_score = (float) 0.5;       //短评打分增长步伐
    private float user_score = (float) 5.0;                    //用户的短评评分
    private String user_score_string = "5.0";            //用户的短评评分的string类型
    private String user_short_comments;
    private SharedPreferencesUtil check;
    private String result;
    private boolean flag_publish_finish = false;
    private int want_number;
    private int reading_number;
    private int done_number;

    public Context context;
    public final int BASIC_VIEW = 1;
    public final int EMPTY_VIEW = 2;
    public final int WRITE_SHORT_COMMENTS_VIEW = 3;
    public final int SHORT_COMMENTS_VIEW = 4;
    public final int NONE_COMMENTS_VIEW = 5;
    private String s_image;
    private String s_name;
    private String s_time;
    private String s_score;
    private String s_content;
    private String number;
    private Map map3 = new HashMap();
    private Handler handler;
    private Handler handler1;
    private Handler handler_fail;
    private RecyclerView.ViewHolder holder2;
    private String show;
    private String result_change;
    private String final_score_string;
    private float final_score_float;
    private String text_people;
    private String all_score;
    private String all_number;
    private String result_change_score;
    private Handler handler_change_basic;
    private String new_score;
    private String people;
    private Handler handler_people;
    private Handler net_fail;


    public BookAdapter(Context context, List<Map<String, Object>> list, String book_id, String status, SharedPreferencesUtil check, String number,String all_score,String all_number) {
        this.context = context;
        this.list = list;
        this.book_id = book_id;
        this.status = status;
        this.check = check;
        this.number = number;
        this.all_number = all_number;
        this.all_score = all_score;
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
        } else if (viewType == SHORT_COMMENTS_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_short_comments, parent, false);
            return new ShortCommentsViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_none_comments, parent, false);
            return new NoneCommentsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BasicViewHolder) {
            //  item_book_basic
            final BasicViewHolder viewHolder = (BasicViewHolder) holder;
            Glide.with(context).load(list.get(position).get("image").toString()).into(viewHolder.book_image);
            viewHolder.book_info.setText(list.get(position).get("info").toString());
            //   显示想读在读已读的人数
            want_number = Integer.valueOf(list.get(position).get("want").toString());
            reading_number = Integer.valueOf(list.get(position).get("progress").toString());
            done_number = Integer.valueOf(list.get(position).get("done").toString());
            viewHolder.book_want_number.setText("想读人数：" + list.get(position).get("want").toString());
            viewHolder.book_reading_number.setText("在读人数：" + list.get(position).get("progress").toString());
            viewHolder.book_done_number.setText("已读人数：" + list.get(position).get("done").toString());

            //初始化想读在读已读,并设置监听
            if (check.isLogin()) {
                //   初始化按钮
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
                //想读按钮
                viewHolder.book_want.setOnClickListener(new View.OnClickListener() {
                    private String want_string;
                    private String reading_string;
                    private String done_string;

                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();
                            want_number++;
                            want_string = String.valueOf(want_number);
                            viewHolder.book_want_number.setText("想读人数：" + want_string);

                        } else if (status.equals("1")) {
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu);
                            viewHolder.book_reading.invalidate();
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();
                            reading_number--;
                            reading_string = String.valueOf(reading_number);
                            viewHolder.book_reading_number.setText("在读人数：" + reading_string);
                            want_number++;
                            want_string = String.valueOf(want_number);
                            viewHolder.book_want_number.setText("想读人数：" + want_string);

                        } else if (status.equals("2")) {
                            status = "0";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu);
                            viewHolder.book_done.invalidate();
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu2);
                            viewHolder.book_want.invalidate();
                            want_number++;
                            want_string = String.valueOf(want_number);
                            viewHolder.book_want_number.setText("想读人数：" + want_string);
                            done_number--;
                            done_string = String.valueOf(done_number);
                            viewHolder.book_done_number.setText("已读人数：" + done_string);
                        } else {
                            Toast.makeText(BookAdapter.this.context, "您已将该书添加至想读", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //在读按钮
                viewHolder.book_reading.setOnClickListener(new View.OnClickListener() {
                    private String want_string;
                    private String reading_string;
                    private String done_string;

                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();
                            reading_number++;
                            reading_string = String.valueOf(reading_number);
                            viewHolder.book_reading_number.setText("在读人数：" + reading_string);

                        } else if (status.equals("2")) {
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu);
                            viewHolder.book_done.invalidate();
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();
                            reading_number++;
                            reading_string = String.valueOf(reading_number);
                            viewHolder.book_reading_number.setText("在读人数：" + reading_string);
                            done_number--;
                            done_string = String.valueOf(done_number);
                            viewHolder.book_done_number.setText("已读人数：" + done_string);
                        } else if (status.equals("0")) {
                            status = "1";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu);
                            viewHolder.book_want.invalidate();
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu2);
                            viewHolder.book_reading.invalidate();
                            reading_number++;
                            reading_string = String.valueOf(reading_number);
                            viewHolder.book_reading_number.setText("在读人数：" + reading_string);
                            want_number--;
                            want_string = String.valueOf(want_number);
                            viewHolder.book_want_number.setText("想读人数：" + want_string);
                        } else {
                            Toast.makeText(BookAdapter.this.context, "您已将该书添加至在读", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //已读按钮
                viewHolder.book_done.setOnClickListener(new View.OnClickListener() {
                    private String want_string;
                    private String reading_string;
                    private String done_string;

                    @Override
                    public void onClick(View v) {
                        if (status.equals("3")) {
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();
                            done_number++;
                            done_string = String.valueOf(done_number);
                            viewHolder.book_done_number.setText("已读人数：" + done_string);

                        } else if (status.equals("0")) {
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_want.setImageResource(R.drawable.xiangdu);
                            viewHolder.book_want.invalidate();
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();
                            done_number++;
                            done_string = String.valueOf(done_number);
                            viewHolder.book_done_number.setText("已读人数：" + done_string);
                            want_number--;
                            want_string = String.valueOf(want_number);
                            viewHolder.book_want_number.setText("想读人数：" + want_string);
                        } else if (status.equals("1")) {
                            status = "2";
                            changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id, status);
                            viewHolder.book_reading.setImageResource(R.drawable.zaidu);
                            viewHolder.book_reading.invalidate();
                            viewHolder.book_done.setImageResource(R.drawable.yidu2);
                            viewHolder.book_reading.invalidate();
                            done_number++;
                            done_string = String.valueOf(done_number);
                            viewHolder.book_done_number.setText("已读人数：" + done_string);
                            reading_number--;
                            reading_string = String.valueOf(reading_number);
                            viewHolder.book_reading_number.setText("在读人数：" + reading_string);
                        } else {
                            Toast.makeText(BookAdapter.this.context, "您已将该书添加至在读", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                //未登录时想读已读在读按钮的点击事件
                viewHolder.book_want.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context, "请您登录后再进行此操作！", Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.book_reading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context, "请您登录后再进行此操作！", Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.book_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BookAdapter.this.context, "请您登录后再进行此操作！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //该到对评分的操作了
            if(all_number.equals("0")){
                text_people = "尚未有用户评分";
                final_score_string = "";
                viewHolder.book_people.setText(text_people);
                viewHolder.book_score.setText(final_score_string);
                viewHolder.book_rating_1.setRating((float)5.0);
            }else {
                viewHolder.book_people.setText("评论人数：" + list.get(position).get("people").toString());
                final_score_float = Float.valueOf(all_score) / Float.valueOf(all_number) ;
                final_score_string = String.format("%.1f",final_score_float);
                viewHolder.book_score.setText(final_score_string);
                viewHolder.book_rating_1.setRating(final_score_float);
            }

            //简介
            viewHolder.book_intro.setText(list.get(position).get("introduce").toString());
            viewHolder.book_introduction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookAdapter.this.context, BookIntroductionActivity.class);
                    intent.putExtra("book_introduction", list.get(position).get("introduce").toString());
                    context.startActivity(intent);
                }
            });

            //跳转到全部书评的按钮
            viewHolder.jump_book_book_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookAdapter.this.context, BookCommentsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id",book_id);
                    bundle.putString("all_score",all_score);
                    bundle.putString("all_number",all_number);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            //修改
            handler_change_basic = new Handler(context.getMainLooper()) {
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    if (true) {
                        new_score = String.format("%.1f",Float.valueOf(new_score));
                        Log.d("新的评分",new_score);
                        viewHolder.book_score.setText(new_score);
                        viewHolder.book_rating_1.setRating(Float.valueOf(new_score));
                        bookWithOkHttp2("http://47.102.46.161/AT_read/book/?num=" + book_id);

                        handler_people = new Handler(context.getMainLooper()) {
                            public void handleMessage(Message message1) {
                                super.handleMessage(message1);
                                if (true) {
                                    viewHolder.book_people.setText("评分人数："+people);
                                }
                            }
                        };
                    }
                }
            };
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
            //发布短评
            viewHolder.book_publish_short_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!check.isLogin()) {
                        Toast.makeText(BookAdapter.this.context, "请您登录后再进行此操作", Toast.LENGTH_SHORT).show();
                    } else {
                        //写短评
                        user_short_comments = viewHolder.book_write_short_comments.getText().toString();
                        if ("".equals(user_short_comments))
                            Toast.makeText(BookAdapter.this.context, "请您输入评论后再发布", Toast.LENGTH_SHORT).show();
                        else {
                            user_score_string = Float.toString(user_score);
                            String title = "short_comments_title";
                            Log.d("book_id", book_id);
                            Log.d("title", title);
                            Log.d("content", user_short_comments);
                            Log.d("score", user_score_string);
                            publishCommentsWithOkHttp("http://47.102.46.161/AT_read/book_review/?num=" + book_id + "&type=s", title, user_short_comments, user_score_string, book_id);
                            closeSoftInput(context, v);
                            viewHolder.book_write_short_comments.setText("");
                            viewHolder.book_write_short_comments.clearFocus();
                        }
                    }
                }
            });
        } else if (holder instanceof ShortCommentsViewHolder) {
            //    item_book_short_comments
            ShortCommentsViewHolder viewHolder = (ShortCommentsViewHolder) holder;
            Glide.with(context).load("http://47.102.46.161/media/" + list.get(position).get("s_image").toString()).into(viewHolder.book_short_comments_image);
            viewHolder.book_short_comments_name.setText(list.get(position).get("s_name").toString());
            viewHolder.book_short_comments_time.setText(list.get(position).get("s_time").toString());
            viewHolder.book_rating_3.setRating(Float.parseFloat(list.get(position).get("s_score").toString()));
            viewHolder.book_show_short_rating.setText(list.get(position).get("s_score").toString());
            viewHolder.book_short_comments_content.setText(list.get(position).get("s_content").toString());
        } else {
            //无短评提示图
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
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("要改变为的改变图书状态：", status);
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("改变图书状态的返回结果", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    result = jsonObject.getString("result");
                    if (status.equals("0"))
                        show = "想读";
                    else if (status.equals("1"))
                        show = "在读";
                    else if (status.equals("2"))
                        show = "已读";
                    else {

                    }
                    Log.d("改变后的图书状态：", status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(context,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //修改图书评分的方法
    public void changeScoreWithOkHttp(String address,String score) {
        HttpUtil.changeScoreWithOkHttp(address, score, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //得到服务器返回的具体内容
                final String responseData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    result_change_score = jsonObject.getString("result");
                    Log.d("result_change_score",result_change_score);
                    if(result_change_score.equals("200")){
                        Message message = new Message();
                        message.what = 1;
                        handler_change_basic.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(context,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public void publishCommentsWithOkHttp(String address, String title, String content, String score, String book_num) {
        HttpUtil.publishCommentsWithOkHttp(address, title, content, score, book_num, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {

                    JSONObject jsonObject = new JSONObject(responseData);
                    result = jsonObject.getString("result");
                    if (result.equals("200")) {
                        Message message = new Message();
                        message.what = 1;
                        handler_fail.sendMessage(message);
                        flag_publish_finish = true;
                        if (flag_publish_finish) {
                            bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);
                            flag_publish_finish = false;
                        }
                    } else {
                        Message message = new Message();
                        message.what = 1;
                        handler_fail.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        handler_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    if (result.equals("200")){
                        Toast.makeText(BookAdapter.this.context, "评论成功", Toast.LENGTH_SHORT).show();
                        all_score = String.valueOf((Float.valueOf(user_score_string)+Float.valueOf(all_score)));
                        all_number = String.valueOf((Float.valueOf(all_number)+(float)1.0));
                        new_score = String.valueOf(Float.valueOf(all_score) / Float.valueOf(all_number));
                        Log.d("计算的出的评分",new_score);
                        changeScoreWithOkHttp("http://47.102.46.161/AT_read/score/?num=" + book_id,new_score );
                    }
                    else if (result.equals("402"))
                        Toast.makeText(BookAdapter.this.context, "请求有误", Toast.LENGTH_SHORT).show();
                    else if(result.equals("400"))
                        Toast.makeText(BookAdapter.this.context, "参数有误", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(BookAdapter.this.context, "用户未登录", Toast.LENGTH_SHORT).show();
                }
            }
        };
        net_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(context,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };
    }
    //获得修改后的评论人数
    public void bookWithOkHttp2(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("book");
                    people = jsonObject1.getString("people");
                    Message message = new Message();
                    message.what = 1;
                    handler_people.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(context,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //获得图书信息的方法
    public void bookWithOkHttp(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    //解析短评
                    JSONArray jsonArray = jsonObject.getJSONArray("shortcomments");
                    JSONObject jsonObject3 = jsonArray.getJSONObject(jsonArray.length() - 1);
                    s_image = jsonObject3.getString("image");
                    s_name = jsonObject3.getString("name");
                    s_time = jsonObject3.getString("time");
                    s_score = jsonObject3.getString("score");
                    s_content = jsonObject3.getString("content");
                    map3.put("s_image", s_image);
                    map3.put("s_name", s_name);
                    map3.put("s_time", s_time);
                    map3.put("s_score", s_score);
                    map3.put("s_content", s_content);
                    map3.put("type", 4);
                    list.add(map3);
                    //判断是否有短评
                    Log.d("有无短评", number);
                    //判断是否有短评从而选择是否删除占位图
                    if (number.equals("0")) {
                        Log.d("1号位置", number);
                        Message message1 = new Message();
                        message1.what = 1;
                        handler1.sendMessage(message1);
                    } else {
                        //添加评论
                        Log.d("2号位置", number);
                        Message message2 = new Message();
                        message2.what = 2;
                        handler.sendMessage(message2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (number.equals("0")) {
            handler1 = new Handler(context.getMainLooper()) {
                public void handleMessage(Message message3) {
                    super.handleMessage(message3);
                    if (true) {
                        number = "1";
                        Log.d("3号位置", number);
                        list.remove(4);
                        notifyItemRemoved(4);
//                    //添加评论
//                    Message message3 = new Message();
//                    message3.what = 1;
//                    handler.sendMessage(message3);
                    }
                }
            };
        }
        handler = new Handler(context.getMainLooper()

        ) {
            public void handleMessage(Message message4) {
                super.handleMessage(message4);
                if (true) {
                    Log.d("4号位置", number);
                    addItem(4, map3);
                }
            }
        };
        net_fail = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(context,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //添加数据
    public void addItem(int position, Map data) {
        list.add(position, data);
        list.remove(list.size() - 1);
        notifyItemInserted(position);//通知演示插入动画
        Log.d("调用了我", book_id);
    }


    // 关闭键盘输入法
    public static void closeSoftInput(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
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
        book_write_short_comments = itemView.findViewById(R.id.book_get_short_comments);
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

class NoneCommentsViewHolder extends RecyclerView.ViewHolder {


    NoneCommentsViewHolder(@NonNull View itemView) {
        super(itemView);

    }

}


