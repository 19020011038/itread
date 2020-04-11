package com.example.itread.Ui.fragment.guide;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itread.Adapter.NewBookAdapter;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

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
    private List<Map<String, Object>> list2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private String name;
    private String image;
    private String content;
    private String author;
    private String book_id;
    private SharedPreferencesUtil check;
    private String score;
    private String result;
    private String status;
    private Button button;
    private Handler handler;
    private boolean aBoolean = true;
    private String a = "a";
    private String ab = "1";
    private boolean isNet;
    private NewBookAdapter newBookAdapter;
    int k = 0;
    int g = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        check = SharedPreferencesUtil.getInstance(getActivity());
        View root = inflater.inflate(R.layout.fragment_newbook, container, false);
        
        isNet = HttpUtil.isNetworkConnected(getActivity());

        for(int i= 0;i<20;i++){
            Map map2 = new HashMap();
            map2.put("status",ab);
            list2.add(map2);
        }

        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView5);
        //联网请求获得图书信息
        NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list/");
        Log.d("11111111111111111111",a);
        Log.d("是否登录",String.valueOf(check.isLogin()));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isNet){
            Log.d("qsh","有网络");
        }else{
            Toast.makeText(getActivity(),"网络似乎不太给力=.=", Toast.LENGTH_LONG).show();
        }



        if (check.isLogin())
        {
            Log.d("2222222222222222222",a);
            list2.clear();
            NewBookStatus("http://47.102.46.161/AT_read/20/");
        }
    }

    public void NewBookStatus(String address){
        HttpUtil.NewBookStatus(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                //       Toast.makeText(getActivity(),"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("caonimacaonimacaocaocao",responseData);
                try {

                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("book");

                    for (int i = 0;k == 20 && i < jsonArray.length(); i++,g++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        status = jsonObject1.getString("status");

                        Map map1 = new HashMap();
                        map1.put("status",status);
                        list2.add(map1);

                    }
                    if (!getActivity().equals(null))
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("4444444444",a);
                            if (g==20)
                            {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//垂直排列 , Ctrl+P
                                recyclerView.setAdapter(new NewBookAdapter(getActivity(), list,list2));//绑定适配器
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

                    for (int i = 0; i < jsonArray.length(); i++,k++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        name = jsonObject1.getString("title");
                        image = jsonObject1.getString("image");
                        author = jsonObject1.getString("author");
                        content = jsonObject1.getString("content");
                        book_id = jsonObject1.getString("num");

                        Map map = new HashMap();
                        map.put("name",name);
                        map.put("image",image);
                        map.put("author",author);
                        map.put("content",content);
                        map.put("book_id",book_id);
                        map.put("status",status);

                        list.add(map);


                    }

                    if (!getActivity().equals(null))
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (!getActivity().equals(null))
                            {
                                if (k==20)
                                {
                                if(!check.isLogin()){
                                    Log.d("33333333333333333",a);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//垂直排列 , Ctrl+P
                                    recyclerView.setAdapter(new NewBookAdapter(getActivity(), list,list2));//绑定适配器
                                }else {
                                        list2.clear();
                                        NewBookStatus("http://47.102.46.161/AT_read/20/");
                                    }

                                }
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
