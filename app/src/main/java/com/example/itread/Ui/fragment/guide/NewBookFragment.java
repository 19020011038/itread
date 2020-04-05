package com.example.itread.Ui.fragment.guide;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itread.Adapter.NewBookAdapter;
import com.example.itread.Base.BaseFragment;
import com.example.itread.BookListActivity;
import com.example.itread.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.example.itread.Util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewBookFragment extends BaseFragment {

    private List<Map<String, Object>> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private String name;

    private Button button;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_newbook;
    }

    @Override



    public void onResume() {
        super.onResume();

        button = getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookListActivity.class);

                getActivity().startActivity(intent);
//                onDestroyView();
//                onDestroy();


            }
        });






        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        //清除列表内容
        list.clear();

        //联网请求获得图书信息
        NewbookWithOkHttp("http://47.102.46.161/AT_read/book_list1/?list_id=0");



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
                    JSONArray jsonArray = jsonObject.getJSONArray("info");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        name = jsonObject1.getString("title");
                        Map map = new HashMap();

                        map.put("name",name);

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
