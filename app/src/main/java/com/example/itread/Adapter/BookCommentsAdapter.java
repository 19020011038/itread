package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookCommentsDetailActivity;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class BookCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;
    private String book_id;
    public final int None_Comments_View = 2;
    public final int Book_Comments_View = 1;

    public BookCommentsAdapter(Context context, List<Map<String, Object>> list,String book_id) {
        this.context = context;
        this.list = list;
        this.book_id = book_id;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(list.get(position).get("type").toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == None_Comments_View) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_none_comments, parent, false);
            return new NoCommentsViewHolder(view);
        } else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_comments, parent, false);
            return new BookCommentsViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof BookCommentsViewHolder){
            BookCommentsViewHolder viewHolder = (BookCommentsViewHolder)holder;
            Glide.with(BookCommentsAdapter.this.context).load("http://47.102.46.161/media/"+list.get(position).get("image").toString()).into(viewHolder.image);
            viewHolder.name.setText(list.get(position).get("name").toString());
            viewHolder.ratingBar.setRating(Float.parseFloat(list.get(position).get("score").toString()));
            if(list.get(position).get("status").toString().equals("0")){
                viewHolder.status.setText("想读");
            }else if(list.get(position).get("status").toString().equals("1")){
                viewHolder.status.setText("在读");
            }else  if(list.get(position).get("status").toString().equals("2")){
                viewHolder.status.setText("已读");
            }else {
                viewHolder.status.setText(" ");
            }
            viewHolder.time.setText(list.get(position).get("time").toString());
            viewHolder.title.setText(list.get(position).get("title").toString());
            viewHolder.word.setText(list.get(position).get("word").toString());
            //跳转到书评详情
            viewHolder.jump_book_comments_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookCommentsAdapter.this.context, BookCommentsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id",book_id);
                    bundle.putString("position", String.valueOf(list.size()-position-1));
                    Log.d("posi",String.valueOf(list.size()-position-1));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }else{

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BookCommentsViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public RatingBar ratingBar;
        public TextView status;
        public TextView time;
        public TextView title;
        public TextView word;
        public View jump_book_comments_detail;

        BookCommentsViewHolder(@NonNull View itemView) {
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


    class NoCommentsViewHolder extends RecyclerView.ViewHolder {


        NoCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

        }

    }


}
