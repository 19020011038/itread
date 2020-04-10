package com.example.itread.Ui.fragment.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itread.Adapter.BooklistFragmentAdapter.BooklistFragmentAdapter1;
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

public class BooklistFragment1 extends Fragment {

    private List<Map<String, Object>> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private String name;
    private String image;
    private String number;
    private String num;

    public static BooklistFragment1 newInstance(int index) {
        BooklistFragment1 fragment = new BooklistFragment1();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booklist1, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        list.clear();

        //联网请求获得图书信息
        NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list2/?list_class=1");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    //获得图书信息的方法
    public void NewbookWithOkHttp(String address){
        HttpUtil.bookWithOkHttp(address, new Callback() {

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

                        name = jsonObject1.getString("name");
                        num = jsonObject1.getString("num");
                        number = jsonObject1.getString("number");
                        image = jsonObject1.getString("image");
                        Map map = new HashMap();

                        map.put("name",name);
                        map.put("image",image);
                        map.put("num",num);
                        map.put("number",number);

                        list.add(map);

                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//垂直排列 , Ctrl+P
                            recyclerView.setAdapter(new BooklistFragmentAdapter1(getActivity(), list));//绑定适配器
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
