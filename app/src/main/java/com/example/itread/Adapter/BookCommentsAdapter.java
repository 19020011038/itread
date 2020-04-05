package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookCommentsDetailActivity;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class BookCommentsAdapter extends RecyclerView.Adapter<BookCommentsAdapter.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;
    private String book_id;

    public BookCommentsAdapter(Context context, List<Map<String, Object>> list,String book_id) {
        this.context = context;
        this.list = list;
        this.book_id = book_id;
    }

    @NonNull
    @Override
    public BookCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_comments, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookCommentsAdapter.ViewHolder holder, final int position) {
        Glide.with(BookCommentsAdapter.this.context).load(list.get(position).get("image").toString());
        holder.name.setText(list.get(position).get("name").toString());
//        holder.ratingBar.setRating(Float.parseFloat(list.get(position).get("score").toString()));
        if(list.get(position).get("status").toString().equals("0")){
            holder.status.setText("想读");
        }else if(list.get(position).get("status").toString().equals("1")){
            holder.status.setText("在读");
        }else  if(list.get(position).get("status").toString().equals("2")){
            holder.status.setText("已读");
        }else {
            holder.status.setText(" ");
        }
        holder.time.setText(list.get(position).get("time").toString());
        holder.title.setText(list.get(position).get("title").toString());
        holder.word.setText(list.get(position).get("word").toString());
        //跳转到书评详情
        holder.jump_book_comments_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookCommentsAdapter.this.context, BookCommentsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("book_id",book_id);
                bundle.putString("position", String.valueOf(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public RatingBar ratingBar;
        public TextView status;
        public TextView time;
        public TextView title;
        public TextView word;
        public View jump_book_comments_detail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.book_comments_image);
            name = itemView.findViewById(R.id.book_comments_name);
            ratingBar = itemView.findViewById(R.id.book_comments_rating);
            status = itemView.findViewById(R.id.book_comments_status);
            time = itemView.findViewById(R.id.book_comments_time);
            title = itemView.findViewById(R.id.book_comments_title);
            word = itemView.findViewById(R.id.book_comments_word);
            jump_book_comments_detail = itemView.findViewById(R.id.item_book_comments);
        }
    }
}
