package com.example.itread.Ui.fragment.collect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itread.Adapter.HaveReadAdapter;
import com.example.itread.Adapter.ReadingAdapter;
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
public class WantFragment extends Fragment {

    public WantFragment() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    private Map map;
    List<Map<String, Object>> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_readed, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView = getActivity().findViewById(R.id.haveread_recyclerview);

        HaveReadWithOkHttp("http://47.102.46.161/user/index");
    }

    //获得在读
    public void HaveReadWithOkHttp(String address){
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
                try{
                    list.clear();
                    JSONObject object = new JSONObject(responseData);
                    JSONArray jsonArray = object.getJSONArray("have_read");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                int news_id = jsonObject1.getInt("news_id");
                        String bookname = jsonObject1.getString("bookname");
                        String bookphoto = jsonObject1.getString("bookphoto");  //头像
                        String info1 = jsonObject1.getString("info");
                        String info = info1.substring(8);
                        String score = jsonObject1.getString("score");
                        String book_num = jsonObject1.getString("num");
                        map = new HashMap();

                        map.put("bookname", bookname);
                        map.put("bookphoto", bookphoto);
                        map.put("info", info);
                        map.put("score", score);
                        map.put("book_num", book_num);

                        list.add(map);




                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//纵向
                            recyclerView.setAdapter(new HaveReadAdapter(getActivity(), list));
                            recyclerView.setNestedScrollingEnabled(false);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
            }//标签页
        });
    }
}
