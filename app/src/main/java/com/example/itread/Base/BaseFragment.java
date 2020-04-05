package com.example.itread.Base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itread.R;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment  {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View rootview = loadRootView(inflater,container,savedInstanceState);
        initView(rootview);


        unbinder = ButterKnife.bind(this,rootview);
      return rootview;
    }

    protected void initView(View rootView){



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    if (unbinder!=null){
        unbinder.unbind();
    }
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        int resId = getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    protected abstract int getRootViewResId();

}
