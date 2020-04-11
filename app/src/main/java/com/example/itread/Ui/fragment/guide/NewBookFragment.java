package com.example.itread.Ui.fragment.guide;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private NewBookAdapter mAdapter;
    private int m = 0;
    private int n = 0;
    private int k = 0;
    private boolean flag = false;
    private Handler net_fail;
    private Handler book_finish;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        list.clear();
        for (int i = 0; i < 20; i++) {
            Map map = new HashMap();
            map.put("status", ab);
            list2.add(map);
            k++;
        }
        check = SharedPreferencesUtil.getInstance(getActivity());
        View root = inflater.inflate(R.layout.fragment_newbook, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView5);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        mAdapter = new NewBookAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        //联网请求获得图书信息
        if (k == 20) {
            Log.i("联网获取新书","NewbookWithOkHttp");
//            mAdapter.setData2(list2);
            NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list/");
        }

        Log.i("执行了 on create","123");

//asdasdasdasdasdasdasdsadasdasdasdasd

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(flag){
            Log.i("执行了 on resume","123");
            if(check.isLogin()){
                list2.clear();
                NewBookStatus2("http://47.102.46.161/AT_read/20/");
            }
        }
        flag = true;
    }


    public void NewBookStatus(String address) {
        HttpUtil.NewBookStatus(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                //       Toast.makeText(getActivity(),"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("caonimacaonimacaocaocao", responseData);
                try {
                    n = 0;

                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("book");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        status = jsonObject1.getString("status");

                        Map map1 = new HashMap();
                        map1.put("status", status);
                        list2.add(map1);
                        n++;


                    }
                    if (!getActivity().equals(null))
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (n == 20) {
                                    Log.i("在获取图书状态的结果中绑定适配器",String.valueOf(list));
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.setData(list);
                                    Log.i("zyr","list.size:"+list.size());
                                    mAdapter.setData2(list2);
                                }
                            }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(getActivity().getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(getActivity(), "网络连接失败qwq\n请检查您的网络设置", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public void NewBookStatus2(String address) {
        HttpUtil.NewBookStatus(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                //       Toast.makeText(getActivity(),"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("caonimacaonimacaocaocao", responseData);
                try {
                    n = 0;

                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("book");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        status = jsonObject1.getString("status");

                        Map map1 = new HashMap();
                        map1.put("status", status);
                        list2.add(map1);
                        n++;


                    }
                    if (!getActivity().equals(null))
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (n == 20) {
                                    mAdapter.setData2(list2);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(getActivity().getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(getActivity(), "网络连接失败qwq\n请检查您的网络设置", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //获得图书信息的方法
    public void NewbookWithOkHttp(String address) {
        HttpUtil.NewbookWithOkHttp(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                //       Toast.makeText(getActivity(),"获取图书信息失败，请检查您的网络",Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);
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

                        Map map = new HashMap();
                        map.put("name", name);
                        map.put("image", image);
                        map.put("author", author);
                        map.put("content", content);
                        map.put("book_id", book_id);
                        map.put("status", status);

                        list.add(map);

                        m++;


                    }

                    if (!getActivity().equals(null))
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (!getActivity().equals(null)) {
                                    if (!check.isLogin()) {

                                        if (m == 20) {
                                            Log.i("未登录时绑定适配器","recyclerView.setAdapter(mAdapter);");
                                            recyclerView.setAdapter(mAdapter);
                                            mAdapter.setData(list);
                                            mAdapter.setData2(list2);
                                        }
                                    } else {
                                        if (m == 20) {
                                            list2.clear();
                                            Message message = new Message();
                                            message.what = 1;
                                            book_finish.sendMessage(message);
                                        }
                                    }
                                }
                            }
                        });
                    book_finish = new Handler(getActivity().getMainLooper()) {
                        public void handleMessage(Message message) {
                            super.handleMessage(message);
                            if (true) {
                                Log.i("已经登录，联网获取图书状态"," NewBookStatus");
                                NewBookStatus("http://47.102.46.161/AT_read/20/");
                            }
                        }
                    };

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(getActivity().getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(getActivity(), "网络连接失败qwq\n请检查您的网络设置", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

}
