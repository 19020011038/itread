package com.example.itread.Ui.fragment.guide;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.itread.Adapter.ScreenSlidePagerAdapter;
import com.example.itread.R;
import com.example.itread.Util.HttpUtil;
import com.google.android.material.tabs.TabLayout;

public class BookListFragment extends Fragment  {

    private Button button;
    private ScreenSlidePagerAdapter screenSlidePagerAdapter;
    private boolean isNet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_booklist, container, false);

        ScreenSlidePagerAdapter sectionsPagerAdapter = new ScreenSlidePagerAdapter(getActivity(), getChildFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();

        isNet = HttpUtil.isNetworkConnected(getActivity());
        if (isNet){
            Log.d("qsh","有网络");
        }else{
            Toast.makeText(getActivity(),"网络似乎不太给力=.=", Toast.LENGTH_LONG).show();
        }

    }
}


