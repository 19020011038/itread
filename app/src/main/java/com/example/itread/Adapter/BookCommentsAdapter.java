package com.example.itread.Adapter;

import android.content.Context;
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
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class BookCommentsAdapter extends RecyclerView.Adapter<BookCommentsAdapter.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;

    public BookCommentsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
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
        holder.ratingBar.setRating(Float.parseFloat(list.get(position).get("score").toString()));
        holder.status.setText(list.get(position).get("status").toString());
        holder.time.setText(list.get(position).get("time").toString());
        holder.title.setText(list.get(position).get("title").toString());
        holder.word.setText(list.get(position).get("word").toString());
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.book_comments_image);
            name = itemView.findViewById(R.id.book_comments_name);
            ratingBar = itemView.findViewById(R.id.book_comments_rating);
            status = itemView.findViewById(R.id.book_comments_status);
            time = itemView.findViewById(R.id.book_comments_time);
            title = itemView.findViewById(R.id.book_comments_title);
            word = itemView.findViewById(R.id.book_comments_word);
        }
    }
}
