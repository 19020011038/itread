package com.example.itread.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookActivity;
import com.example.itread.HomeActivity;
import com.example.itread.LoginActivity;
//import com.example.itread.MyCommentsDelActivity;
import com.example.itread.MyShortCommentsActivity;
//import com.example.itread.NewBookActivity;
import com.example.itread.R;
import com.example.itread.SettingActivity;
import com.example.itread.Util.HttpUtil;
import com.longsh.optionframelibrary.OptionBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyShortCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private Context context;
    private String result;
    private Handler mHandler;
    private Handler mHandler_f;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;

    public MyShortCommentsAdapter(Context context, List<Map<String, Object>> list) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_myshort_comment, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
//            recyclerViewHolder.myshort_comment_bookstatus.setText(list.get(position).get("status").toString());//将子控件中的文本换为map中的文本
//        holder.main_image.setImageURI((Uri) list.get(position).get("images"));
            recyclerViewHolder.myshort_comment_content.setText(list.get(position).get("content").toString());
            recyclerViewHolder.myshort_comment_time.setText(list.get(position).get("time").toString());
            recyclerViewHolder.myshort_comment_bookname.setText(list.get(position).get("book_name").toString());
//            recyclerViewHolder.mybook_comment_name.setText(list.get(position).get("还没给的书评名字").toString());
            final String bookphoto_url = list.get(position).get("book_photo").toString(); //这个非常重要
            final String score = list.get(position).get("score").toString(); //这个非常重要
            final String book_id = list.get(position).get("book_num").toString(); //这个非常重要
            final String comment_id = list.get(position).get("comment_id").toString();
            Glide.with(context).load(bookphoto_url).into(recyclerViewHolder.myshort_comment_bookphoto);



            recyclerViewHolder.myshort_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookActivity.class);
                    intent.putExtra("book_id", book_id);
                    context.startActivity(intent);
                }
            });

            recyclerViewHolder.myshort_comment_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, MyCommentsDelActivity.class);
//                    context.startActivity(intent);
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
                            HttpUtil.WantReadWithOkHttp("http://47.102.46.161/user/delete_comment?comment_id="+comment_id+"&type=short", new Callback() {
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
                                        }} catch (JSONException e) {
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
//                                notifyDataSetChanged();
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
        private TextView myshort_comment_name;
        private TextView myshort_comment_content;
        private TextView myshort_comment_bookname;
        //        private TextView mybook_comment_bookinfo;
        private ImageView myshort_comment_bookphoto;
        private TextView myshort_comment_bookstatus;
        private TextView myshort_comment_time;
        private ImageButton myshort_comment_del;
        private RelativeLayout myshort_all;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

//            myshort_comment_name = itemView.findViewById(R.id.myshort_comment_name);
            myshort_comment_content = itemView.findViewById(R.id.myshort_comment_content);
            myshort_comment_bookname = itemView.findViewById(R.id.myshort_comment_bookname);
//            mybook_comment_bookinfo = itemView.findViewById(R.id.mybook_comment_bookinfo);
            myshort_comment_bookphoto = itemView.findViewById(R.id.myshort_comment_bookphoto);
//            myshort_comment_bookstatus = itemView.findViewById(R.id.myshort_comment_bookstatus);
            myshort_comment_time = itemView.findViewById(R.id.myshort_comment_time);
            myshort_comment_del = itemView.findViewById(R.id.myshort_comment_del);
            myshort_all = itemView.findViewById(R.id.myshort_all);
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
