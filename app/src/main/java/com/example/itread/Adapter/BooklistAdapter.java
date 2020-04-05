package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class BooklistAdapter extends RecyclerView.Adapter<BooklistAdapter.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;



    public BooklistAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public BooklistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_inlist, parent, false);

        return new BooklistAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BooklistAdapter.ViewHolder holder, final int position) {
        String picture = list.get(position).get("s_picture").toString();
        String name = list.get(position).get("s_name").toString();
        String introduce = list.get(position).get("s_introduce").toString();
        String book_id = list.get(position).get("book_id").toString();
//        String score = list.get(position).get("author_score").toString();
////        String author = list.get(position).get("author_name").toString();
////        String publish = list.get(position).get("author_publish").toString();
////        String year = list.get(position).get("author_year").toString();




        holder.mName.setText(name);
//        holder.mScore.setText(score);
//        holder.mAuthor.setText(author + "/" + year + "/" + publish);
        holder.mComment.setText(introduce);

        String picture_1 = picture.replace("\\","");
        String picture_2 = picture_1.replace("\"","");
        String picture_3 = picture_2.replace("[","");
        String picture_4 = picture_3.replace("]","");


        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this.context)
                .load(picture_4)
                .apply(options)
                .into(holder.mImage);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mScore;
        private TextView mAuthor;
        private TextView mComment;
        private ImageView mImage;



        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.name);
            mScore = itemView.findViewById(R.id.score);
            mAuthor = itemView.findViewById(R.id.author);
            mComment = itemView.findViewById(R.id.comment);
            mImage = itemView.findViewById(R.id.image);



        }

    }
}
