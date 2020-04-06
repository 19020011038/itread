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
import com.example.itread.BookActivity;
import com.example.itread.R;

import java.util.List;
import java.util.Map;

public class NewBookAdapter extends RecyclerView.Adapter<NewBookAdapter.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;



    public NewBookAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public NewBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newbook, parent, false);

        return new NewBookAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NewBookAdapter.ViewHolder holder, final int position) {
        String title = list.get(position).get("name").toString();
        String image = list.get(position).get("image").toString();
        String author = list.get(position).get("author").toString();
        String content = list.get(position).get("content").toString();
        String book_id = list.get(position).get("book_id").toString();
        String score = list.get(position).get("score").toString();


        holder.name.setText(title);
        holder.score.setText(score);
        holder.content.setText(content);
        holder.author.setText(author);

        String picture_1 = image.replace("\\","");
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
                .into(holder.image);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("book_id",book_id);
                context.startActivity(intent);
            }
        });



    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private TextView author;
        private  TextView content;
        private TextView score;
        private RelativeLayout relativeLayout;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            score = itemView.findViewById(R.id.score);
            relativeLayout = itemView.findViewById(R.id.layout2);



        }

    }
}