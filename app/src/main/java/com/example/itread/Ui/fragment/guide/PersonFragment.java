package com.example.itread.Ui.fragment.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.itread.Adapter.CollectAdapter;
import com.example.itread.Adapter.ScreenSlidePagerAdapter;
import com.example.itread.Base.BaseFragment;
import com.example.itread.R;
import com.example.itread.Ui.fragment.collect.ReadedFragment;
import com.example.itread.Ui.fragment.collect.ReadingFragment;
import com.example.itread.Ui.fragment.collect.WantFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonFragment extends BaseFragment {

    @BindView(R.id.viewPager2)
    ViewPager2 pager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


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



    }
}