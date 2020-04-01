package com.example.itread.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.itread.Ui.fragment.collect.ReadedFragment;
import com.example.itread.Ui.fragment.collect.ReadingFragment;
import com.example.itread.Ui.fragment.collect.WantFragment;


public class CollectAdapter extends FragmentStateAdapter {
    private int NUM_PAGES = 3;
    public CollectAdapter(FragmentActivity fa) {
        super(fa);
    }
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new ReadedFragment();

            case 1:
                return new ReadingFragment();

            default:
                return new WantFragment();

        }
    }
    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
