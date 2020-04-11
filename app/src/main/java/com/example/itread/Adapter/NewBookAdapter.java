package com.example.itread.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.itread.BookActivity;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class NewBookAdapter extends RecyclerView.Adapter<NewBookAdapter.ViewHolder>{

    private List<Map<String, Object>> list = new ArrayList<>();
    private List<Map<String, Object>> list2 = new ArrayList<>();
    private Context context;
    private Handler mHandler;
    private Handler mHandler_f;
    private Handler mHandler_e;
    private String result;
    private SharedPreferencesUtil check;
    private String status ;
    private Handler handler;
    private String status2;
    private boolean flag = true;



    public NewBookAdapter(Context context) {
        this.context = context;
        check = SharedPreferencesUtil.getInstance(context.getApplicationContext());
    }

    public List<Map<String, Object>>  getData() {
        return list;
    }

    public void setData(List<Map<String, Object>> list) {
        this.list = list;
    }


    public List<Map<String, Object>>  getData2() {
        return list2;
    }

    public void setData2(List<Map<String, Object>> list2) {
        this.list2 = list2;
    }




    @NonNull
    @Override
    public NewBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newbook, parent, false);

        return new NewBookAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String title = list.get(position).get("name").toString();
        String image = list.get(position).get("image").toString();
        String author = list.get(position).get("author").toString();
        String content = list.get(position).get("content").toString();
        String book_id = list.get(position).get("book_id").toString();
//        String score = list.get(position).get("score").toString();

//        holder.ratingBar.setRating(Float.valueOf(score)/2);
        holder.name.setText(title);
//        holder.score.setText(score);
        holder.content.setText(content);
        holder.author.setText(author);
        holder.textView.setText(position+1+"");

        if (check.isLogin())
        {
            status = list2.get(position).get("status").toString();
            if (status.equals("0"))
                        {
                            holder.like.setImageResource(R.drawable.like2);
                            holder.like.invalidate();
                        }
            else
            {
                holder.like.setImageResource(R.drawable.newbook_want);
                holder.like.invalidate();
            }
        }

//        if (check.isLogin())
//        {
//            if(flag){
//                getStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num="+book_id);
//                flag = false;
//            }

////            handler = new Handler(context.getMainLooper()
////
////            ) {
////                public void handleMessage(Message message4) {
////                    super.handleMessage(message4);
////                    if (true) {
////
////                        if (status.equals("0"))
////                        {
////
////                            holder.like.setImageResource(R.drawable.like2);
////                            holder.like.invalidate();
////                            flag = true;
////
////                        }
////
////                    }
////                }
////            };
//        }

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
        holder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check.isLogin()) {
                    if (!status.equals("0"))
                    {
                        status = "0";
                        changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id,status);

                        holder.like.setImageResource(R.drawable.like2);
                        holder.like.invalidate();
                        Toast.makeText(context, "已成功加入想读", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        status = "1";
                        changeStatusWithOkHttp("http://47.102.46.161/AT_read/status/?num=" + book_id,status);

                        holder.like.setImageResource(R.drawable.newbook_want);
                        holder.like.invalidate();
                        Toast.makeText(context, "已取消想读", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
                }
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
        private RelativeLayout relativeLayout2;
        private TextView textView;
        private ImageView like;
        private RatingBar ratingBar;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
//            score = itemView.findViewById(R.id.score);
            relativeLayout = itemView.findViewById(R.id.layout2);
            relativeLayout2 = itemView.findViewById(R.id.newbook_want);
            textView = itemView.findViewById(R.id.ididid);
            like = itemView.findViewById(R.id.book_want);
//            ratingBar = itemView.findViewById(R.id.book_rating);

        }

    }

    //获得图书状态的方法
    public void getStatusWithOkHttp(String address) {
        HttpUtil.getStatusWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                String responseData = response.body().string();
//                responseData = removeBOM(responseData);
//                if(responseData != null && responseData.startsWith("\ufeff"))
//                    responseData = responseData.substring(1);
                Log.d("获取图书状态：", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    status = jsonObject.getString("result");
                    Log.d("获得图书状态请求的返回结果",status);
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void changeStatusWithOkHttp(String address, final String status) {
        HttpUtil.changeStatusWithOkHttp(address, status, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("改变图书状态的返回结果", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    result = jsonObject.getString("result");
                    Log.d("改变图书状态请求的返回结果",result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}