package com.example.itread.Ui.fragment.collect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.itread.Adapter.MyBookCommentsAdapter;
import com.example.itread.Adapter.WantReadAdapter;
import com.example.itread.MyBookCommentsActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadedFragment extends Fragment {

    public ReadedFragment() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    private Map map;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_want, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView = getActivity().findViewById(R.id.wantread_recyclerview);

        WantReadWithOkHttp("http://47.102.46.161/user/index");
    }

    //获得想读
    public void WantReadWithOkHttp(String address){
        HttpUtil.WantReadWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " mywantread : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.i("zyr", "wantread:responseData:"+responseData);
                try{
                    JSONObject object = new JSONObject(responseData);
                    JSONArray jsonArray = object.getJSONArray("want_read");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                        String bookname = jsonObject1.getString("bookname");
                        String bookphoto = jsonObject1.getString("bookphoto");  //头像
                        String author = jsonObject1.getString("author");
                        String publish = jsonObject1.getString("publish");
                        String book_num = jsonObject1.getString("book_num");
                        String publish_time = jsonObject1.getString("publish_time");
                        map = new HashMap();

                        map.put("bookname", bookname);
                        map.put("bookphoto", bookphoto);
                        map.put("author", author);
                        map.put("publish", publish);
                        map.put("publish_time", publish_time);
                        map.put("book_num", book_num);

                        list.add(map);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//纵向
                                recyclerView.setAdapter(new WantReadAdapter(getActivity(), list));
                                recyclerView.setNestedScrollingEnabled(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLLfragment"+responseData);
                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//纵向
//                        recyclerView.setAdapter(new WantReadAdapter(getActivity(), list));
//                        recyclerView.setNestedScrollingEnabled(false);
//                    }
//                });
            }//标签页
        });
    }
}
