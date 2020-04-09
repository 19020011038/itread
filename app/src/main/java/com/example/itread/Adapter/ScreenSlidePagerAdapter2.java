package com.example.itread.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.itread.R;
import com.example.itread.Ui.fragment.collect.ReadedFragment;
import com.example.itread.Ui.fragment.collect.WantFragment;

public class ScreenSlidePagerAdapter2 extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_6, R.string.tab_text_7, R.string.tab_text_8};
    private final Context mContext;
//    private BooklistFragment1 booklistFragment1;
//    private BooklistFragment2 booklistFragment2;
//    private BooklistFragment3 booklistFragment3;
//    private BooklistFragment4 booklistFragment4;
//    private BooklistFragment5 booklistFragment5;

    private int NUM_PAGES = 3;
    public ScreenSlidePagerAdapter2(Context context, FragmentManager fa) {
        super(fa);
        mContext = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
//        return PlaceholderFragment.newInstance(position + 1);
//        booklistFragment1 = new BooklistFragment1();
//        booklistFragment2 = new BooklistFragment2();
//        booklistFragment3 = new BooklistFragment3();
//        booklistFragment4 = new BooklistFragment4();
//        booklistFragment5 = new BooklistFragment5();
        switch (position)
        {
            case 0:
                String p = position+"";
                Log.d("qsh1",p);
                return WantFragment.newInstance(position);
            case 1:
                String p1 = position+"";
                Log.d("qsh2",p1);
                return ReadedFragment.newInstance(position);
            default:
                String p4 = position+"";
                Log.d("qsh5",p4);
                return ReadedFragment.newInstance(position);
        }
//        if (position == 0){
//            String p = position+"";
//            Log.d("qsh1",p);
//            return BooklistFragment1.newInstance(position);
//        }
//        else if (position == 1){
//            String p1 = position+"";
//                Log.d("qsh2",p1);
//                return BooklistFragment2.newInstance(position);
//        }
//        else {
//            String p2 = position+"";
//                Log.d("qsh3",p2);
//               return BooklistFragment3.newInstance(position);
//        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
