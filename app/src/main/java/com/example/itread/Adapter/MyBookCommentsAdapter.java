package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.itread.MyCommentsDelActivity;
//import com.example.itread.NewBookActivity;
import com.example.itread.BookActivity;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class MyBookCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private Context context;
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_mybook_comment, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.mybook_comment_bookstatus.setText(list.get(position).get("status").toString());//将子控件中的文本换为map中的文本
//        holder.main_image.setImageURI((Uri) list.get(position).get("images"));
            recyclerViewHolder.mybook_comment_content.setText(list.get(position).get("content").toString());
            recyclerViewHolder.mybook_comment_time.setText(list.get(position).get("time").toString());
            recyclerViewHolder.mybook_comment_bookname.setText(list.get(position).get("book_name").toString());
            recyclerViewHolder.mybook_comment_name.setText(list.get(position).get("还没给的书评名字").toString());
            final String bookphoto_url = list.get(position).get("book_photo").toString(); //这个非常重要
            final String score = list.get(position).get("score").toString(); //这个非常重要
            final String book_id = list.get(position).get("book_num").toString(); //这个非常重要
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
//                    Intent intent = new Intent(context, MyCommentsDelActivity.class);
//                    intent.putExtra("book_id", book_id);
//                    context.startActivity(intent);
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
        private ImageButton mybook_comment_del;
        private RelativeLayout mybook_comment_bookall;

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
            mybook_comment_bookall = itemView.findViewById(R.id.mybook_comment_bookall);
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
