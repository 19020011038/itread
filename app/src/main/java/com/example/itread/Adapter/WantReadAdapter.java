package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itread.BookActivity;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

//import com.example.itread.MyCommentsDelActivity;
//import com.example.itread.NewBookActivity;

public class WantReadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, Object>> list;
    private Context context;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;

    public WantReadAdapter(Context context, List<Map<String, Object>> list) {
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
            View view = LayoutInflater.from(context).inflate(R.layout.item_wantread_white, parent, false);
            return new WhiteMyCommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_user_want_read, parent, false);
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
//            recyclerViewHolder.mybook_comment_bookstatus.setText(list.get(position).get("status").toString());//将子控件中的文本换为map中的文本
////        holder.main_image.setImageURI((Uri) list.get(position).get("images"));
//            recyclerViewHolder.mybook_comment_content.setText(list.get(position).get("content").toString());
//            recyclerViewHolder.mybook_comment_time.setText(list.get(position).get("time").toString());
            recyclerViewHolder.wantread_book_name.setText(list.get(position).get("bookname").toString());
            recyclerViewHolder.wantread_book_info.setText(list.get(position).get("info").toString());
//            recyclerViewHolder.wantread_score.setText(list.get(position).get("score").toString());
            final String bookphoto_url = list.get(position).get("bookphoto").toString(); //这个非常重要
            final String book_id = list.get(position).get("book_num").toString(); //这个非常重要


            Float score = Float.parseFloat(list.get(position).get("score").toString());
            if (score > 5) {
                score = Float.valueOf("5");
            }
//            final String score22 = score.toString();
            final String score22 = String.format("%.1f",score);

            recyclerViewHolder.wantread_score.setText(score22);

            Log.i("zyr","wantread:"+score22);
            recyclerViewHolder.wantread_ratingbar.setRating(score);
            Glide.with(context).load(bookphoto_url).into(recyclerViewHolder.wantread_book_photo);

            recyclerViewHolder.wantread_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookActivity.class);
                    intent.putExtra("book_id", book_id);
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof WhiteMyCommentViewHolder) {
            WhiteMyCommentViewHolder whiteStoreViewHolder = (WhiteMyCommentViewHolder) holder;
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView wantread_book_name;
        private TextView wantread_book_info;
        //        private TextView mybook_comment_bookname;
        //        private TextView mybook_comment_bookinfo;
        private ImageView wantread_book_photo;
        //        private TextView mybook_comment_bookstatus;
//        private TextView mybook_comment_time;
//        private ImageButton mybook_comment_del;
        private RelativeLayout wantread_all;
        private RatingBar wantread_ratingbar;
        private TextView wantread_score;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            wantread_book_name = itemView.findViewById(R.id.want_read_bookname);
            wantread_book_info = itemView.findViewById(R.id.want_read_bookinfo);
//            mybook_comment_bookname = itemView.findViewById(R.id.mybook_comment_bookname);
//            mybook_comment_bookinfo = itemView.findViewById(R.id.mybook_comment_bookinfo);
            wantread_book_photo = itemView.findViewById(R.id.want_read_photo);
//            mybook_comment_bookstatus = itemView.findViewById(R.id.mybook_comment_bookstatus);
//            mybook_comment_time = itemView.findViewById(R.id.mybook_comment_time);
//            mybook_comment_del = itemView.findViewById(R.id.mybook_comment_del);
            wantread_all = itemView.findViewById(R.id.want_read_all);
            wantread_ratingbar = itemView.findViewById(R.id.want_read_rating);
            wantread_score = itemView.findViewById(R.id.want_read_score);
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
