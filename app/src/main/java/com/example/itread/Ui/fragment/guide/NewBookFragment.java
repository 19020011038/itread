package com.example.itread.Ui.fragment.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itread.Adapter.NewBookAdapter;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewBookFragment extends Fragment {

    private List<Map<String, Object>> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private String name;
    private String image;
    private String content;
    private String author;
    private String book_id;
    private String score;
    private String result;

    private Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);


        //清除列表内容
        list.clear();

        //联网请求获得图书信息
        NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list/");
        View root = inflater.inflate(R.layout.fragment_newbook, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override



    public void onResume() {
        super.onResume();

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        //清除列表内容
//        list.clear();
//
//        //联网请求获得图书信息
//        NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list/");



    }

    //获得图书信息的方法
    public void NewbookWithOkHttp(String address){
        HttpUtil.NewbookWithOkHttp(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
         //       Toast.makeText(getActivity(),"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {


                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("book");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        name = jsonObject1.getString("title");
                        image = jsonObject1.getString("image");
                        author = jsonObject1.getString("author");
                        content = jsonObject1.getString("content");
                        book_id = jsonObject1.getString("num");
                        score = jsonObject1.getString("score");


                        Map map = new HashMap();

                        map.put("name",name);
                        map.put("image",image);
                        map.put("author",author);
                        map.put("content",content);
                        map.put("book_id",book_id);
                        map.put("score",score);


                        list.add(map);



                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//垂直排列 , Ctrl+P
                            recyclerView.setAdapter(new NewBookAdapter(getActivity(), list));//绑定适配器
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            private void runOnUiThread(Runnable runnable) {
//            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
