package com.example.itread.Ui.fragment.guide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.itread.Adapter.ScreenSlidePagerAdapter2;
import com.example.itread.MyBookCommentsActivity;
import com.example.itread.MyShortCommentsActivity;
import com.example.itread.R;
import com.example.itread.SettingActivity;
import com.example.itread.Util.HttpUtil;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonFragment extends Fragment {

//    @BindView(R.id.viewPager2)
//    ViewPager2 pager;
//
//    @BindView(R.id.tabLayout)
//    TabLayout tabLayout;

    private RelativeLayout home_setting;
    private ImageView home_icon;
    private TextView home_nickname;
    private String home_icon_url;
    private String nameAddress;
    private String nickname;
    private String icon;
    private RelativeLayout home_bookcomment;
    private RelativeLayout home_shortcomment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person, container, false);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
        ScreenSlidePagerAdapter2 sectionsPagerAdapter = new ScreenSlidePagerAdapter2(getActivity(), getChildFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);
        //    FloatingActionButton fab = root.findViewById(R.id.tabLayout);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//            }
//        });

        return root;
    }



    @Override
    public void onResume() {
        super.onResume();

        home_icon = getActivity().findViewById(R.id.home_icon);
        home_nickname = getActivity().findViewById(R.id.home_nickname);
        home_bookcomment = getActivity().findViewById(R.id.home_bookcomment);
        home_shortcomment = getActivity().findViewById(R.id.home_shortcomment);
        home_setting = (RelativeLayout) getActivity().findViewById(R.id.home_setting);
        nameAddress = "http://47.102.46.161/user/index";
        homeNameOkHttp(nameAddress);
        home_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class); //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                getActivity().startActivity(intent);
            }
        });

        home_bookcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyBookCommentsActivity.class); //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                getActivity().startActivity(intent);
            }
        });

        home_shortcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyShortCommentsActivity.class); //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
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