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
        String str = list.get(position).get("name").toString();
        Log.d("hhh", str);
        holder.textView.setText(str);

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.name);



        }

    }
}