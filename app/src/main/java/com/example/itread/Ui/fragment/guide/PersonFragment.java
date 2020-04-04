package com.example.itread.Ui.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.itread.Adapter.CollectAdapter;
import com.example.itread.Adapter.ScreenSlidePagerAdapter;
import com.example.itread.Base.BaseFragment;
import com.example.itread.BookListActivity;
import com.example.itread.R;
import com.example.itread.SettingActivity;
import com.example.itread.Ui.fragment.collect.ReadedFragment;
import com.example.itread.Ui.fragment.collect.ReadingFragment;
import com.example.itread.Ui.fragment.collect.WantFragment;
import com.example.itread.Util.HttpUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonFragment extends BaseFragment {

    @BindView(R.id.viewPager2)
    ViewPager2 pager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private RelativeLayout home_setting;
    private ImageView home_icon;
    private TextView home_nickname;
    private String home_icon_url;
    private String nameAddress;
    private String nickname;
    private String icon;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_person;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(getActivity());

        pager.setAdapter(new CollectAdapter(getActivity()));
        new TabLayoutMediator(tabLayout, pager, true,new TabLayoutMediator.TabConfigurationStrategy(){

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("想读");
                        break;
                    case 1:
                        tab.setText("在读");
                        break;
                    default:
                        tab.setText("读过");

                }

            }
        }).attach();

    }

    @Override
    public void onResume() {
        super.onResume();

        home_icon = getActivity().findViewById(R.id.home_icon);
        home_nickname = getActivity().findViewById(R.id.home_nickname);
        home_setting = (RelativeLayout) getActivity().findViewById(R.id.home_setting);
        nameAddress = "http://47.102.46.161/user/index";
        homeNameOkHttp(nameAddress);
        home_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class); //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                getActivity().startActivity(intent);
            }
        });
    }

    //获得头像昵称
    public void homeNameOkHttp(String address){
        HttpUtil.homeNameOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " name : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try{
                    JSONObject object = new JSONObject(responseData);
                    JSONObject object1 = object.getJSONObject("user");
                    nickname = object1.getString("nickname");
                    icon = object1.getString("icon");
                    Log.i("zyr", "HomeActivity.icon_url:"+icon);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        home_nickname.setText(nickname);
                        Glide.with(getActivity()).load(icon).into(home_icon);
//                        Toast.makeText(HomeActivity.this,"显示头像",Toast.LENGTH_SHORT).show();
                    }
                });
            }//标签页
        });
    }
}