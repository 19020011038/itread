package com.example.itread.Ui.fragment.guide;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.itread.Base.BaseFragment;
import com.example.itread.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewBookFragment extends BaseFragment {



    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_newbook;
    }
}
