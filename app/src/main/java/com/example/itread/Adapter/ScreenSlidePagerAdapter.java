package com.example.itread.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.itread.Ui.fragment.tab.BooklistFragment1;
import com.example.itread.Ui.fragment.tab.BooklistFragment2;
import com.example.itread.Ui.fragment.tab.BooklistFragment3;
import com.example.itread.Ui.fragment.tab.BooklistFragment4;
import com.example.itread.Ui.fragment.tab.BooklistFragment5;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private int NUM_PAGES = 5;
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new BooklistFragment1();
            case 1:
                return new BooklistFragment2();
            case 2:
                return new BooklistFragment3();
            case 3:
                return new BooklistFragment4();
            default:
                return new BooklistFragment5();
        }
    }
    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

