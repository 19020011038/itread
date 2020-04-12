package com.example.itread.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.itread.MyCommentsDelActivity;
//import com.example.itread.NewBookActivity;
import com.example.itread.BookActivity;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
//import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyBookCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private Context context;
    private Handler mHandler;
    private Handler mHandler_f;
    private String result;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;

    public MyBookCommentsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return TWO_ITEM;
        } else {
            return ONE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TWO_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mycomment_white, parent, false);
            return new WhiteMyCommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mybook_comment2, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
//            recyclerViewHolder.mybook_comment_bookstatus.setText(list.get(position).get("status").toString());//将子控件中的文本换为map中的文本
//        holder.main_image.setImageURI((Uri) list.get(position).get("images"));
            recyclerViewHolder.mybook_comment_content.setText(list.get(position).get("content").toString());
            recyclerViewHolder.mybook_comment_time.setText(list.get(position).get("time").toString());
            recyclerViewHolder.mybook_comment_bookname.setText(list.get(position).get("book_name").toString());
            recyclerViewHolder.mybook_comment_name.setText(list.get(position).get("comment_title").toString());
//            recyclerViewHolder.mybook_score.setText(list.get(position).get("score").toString());
            final String bookphoto_url = list.get(position).get("book_photo").toString(); //这个非常重要
            final Float score = Float.parseFloat(list.get(position).get("score").toString());
            recyclerViewHolder.mybook_rating.setRating(score);
            final String book_id = list.get(position).get("book_num").toString(); //这个非常重要
            final String comment_id = list.get(position).get("comment_id").toString(); //评论id
            Glide.with(context).load(bookphoto_url).into(recyclerViewHolder.mybook_comment_bookphoto);

            recyclerViewHolder.mybook_comment_bookall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookActivity.class);
                    intent.putExtra("book_id", book_id);
                    context.startActivity(intent);
                }
            });

            recyclerViewHolder.mybook_comment_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder (context);
                    dialog.setTitle("是否删除此条评论？");
                    dialog.setMessage("若选择删除请点击确定");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(Home.this, "相机相机相册相册", Toast.LENGTH_SHORT).show();
                            HttpUtil.WantReadWithOkHttp("http://47.102.46.161/user/delete_comment?comment_id="+comment_id+"&type=long", new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    //在这里对异常情况进行处理
                                    Log.i( "zyr", " name : error");
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //得到服务器返回的具体内容
                                    final String responseData = response.body().string();
//                String header = response.header("set-cookie");
                                    Log.i("zyr","comment_id:"+comment_id);
//
                                    try{
                                        JSONObject object = new JSONObject(responseData);
                                        result = object.getString("result");
                                        Log.i("zyr","short,result:"+result);
                                        if (result.equals("评论删除成功")){
                                            Message message = new Message();
                                            message.what = 1;
                                            //发送信息给handler
                                            mHandler.sendMessage(message);
                                        } else {
                                            Message message = new Message();
                                            message.what = 1;
                                            //发送信息给handler
                                            mHandler_f.sendMessage(message);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i( "zyr", "LLL"+responseData);
                                    }
                                }//标签页
                            });
                            Log.i("zyr","commnt_id"+comment_id);
                            mHandler = new Handler(){
                                //handleMessage为处理消息的方法
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if(true) {
                                        Log.i("zyr","mHandler");
//                                notifyDataSetChanged();
                                        list.remove(position);//集合移除该条
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,list.size()-position);
                                        //                tv.setText(msg.arg1 + "");
                                    }
                                }
                            };
                            mHandler_f = new Handler(){

                                //handleMessage为处理消息的方法
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    if(true) {
                                        Log.i("zyr","mHandler_f");
                                        Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                                        //                tv.setText(msg.arg1 + "");
                                    }
                                }
                            };
                        }
                    });
                    dialog.show();
                }
            });

        } else if (holder instanceof WhiteMyCommentViewHolder) {
            WhiteMyCommentViewHolder whiteStoreViewHolder = (WhiteMyCommentViewHolder) holder;
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView mybook_comment_name;
        private TextView mybook_comment_content;
        private TextView mybook_comment_bookname;
        //        private TextView mybook_comment_bookinfo;
        private ImageView mybook_comment_bookphoto;
        private TextView mybook_comment_bookstatus;
        private TextView mybook_comment_time;
        private TextView mybook_score;
        private ImageButton mybook_comment_del;
        private RelativeLayout mybook_comment_bookall;
        private RatingBar mybook_rating;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            mybook_comment_name = itemView.findViewById(R.id.mybook_comment_name);
            mybook_comment_content = itemView.findViewById(R.id.mybook_comment_content);
            mybook_comment_bookname = itemView.findViewById(R.id.mybook_comment_bookname);
//            mybook_comment_bookinfo = itemView.findViewById(R.id.mybook_comment_bookinfo);
            mybook_comment_bookphoto = itemView.findViewById(R.id.mybook_comment_bookphoto);
            mybook_comment_bookstatus = itemView.findViewById(R.id.mybook_comment_bookstatus);
            mybook_comment_time = itemView.findViewById(R.id.mybook_comment_time);
            mybook_comment_del = itemView.findViewById(R.id.mybook_comment_del);
            mybook_comment_bookall = itemView.findViewById(R.id.mybook_all);
            mybook_rating = itemView.findViewById(R.id.mybook_rating);
//            mybook_score = itemView.findViewById(R.id.mybook_score);
        }
    }

    class WhiteMyCommentViewHolder extends RecyclerView.ViewHolder {
        private TextView white_store;

        WhiteMyCommentViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        } else {
            return 1;
        }
    }
}
