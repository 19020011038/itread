package com.example.itread.Ui.fragment.guide;



import com.example.itread.Base.BaseFragment;
import com.example.itread.BookListActivity;
import com.example.itread.R;
import com.example.itread.Ui.fragment.tab.BooklistFragment1;
import com.example.itread.Ui.fragment.tab.BooklistFragment2;
import com.example.itread.Ui.fragment.tab.BooklistFragment3;
import com.example.itread.Ui.fragment.tab.BooklistFragment4;
import com.example.itread.Ui.fragment.tab.BooklistFragment5;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.itread.Adapter.ScreenSlidePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.BindView;

public class BookListFragment extends BaseFragment {

    private Button button;
    private ScreenSlidePagerAdapter screenSlidePagerAdapter;

    @BindView(R.id.viewPager2)
    ViewPager2 pager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_booklist;



    }

    @Override
    public void onStart() {
        super.onStart();

         screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getActivity());

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

        ButterKnife.bind(getActivity());

        pager.setAdapter(screenSlidePagerAdapter);
        new TabLayoutMediator(tabLayout, pager, true,new TabLayoutMediator.TabConfigurationStrategy(){

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("书单类别1");
                        break;
                    case 1:
                        tab.setText("书单类别2");
                        break;
                    case 2:
                        tab.setText("书单类别3");
                        break;
                    case 3:
                        tab.setText("书单类别4");
                        break;
                    default:
                        tab.setText("书单类别5");

                }

            }
        }).attach();

    }
}


