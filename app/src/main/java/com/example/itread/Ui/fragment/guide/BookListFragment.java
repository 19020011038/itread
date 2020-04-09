package com.example.itread.Ui.fragment.guide;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.itread.Adapter.ScreenSlidePagerAdapter;
import com.example.itread.R;
import com.google.android.material.tabs.TabLayout;

public class BookListFragment extends Fragment  {

    private Button button;
    private ScreenSlidePagerAdapter screenSlidePagerAdapter;

//    @BindView(R.id.viewPager2)
//    ViewPager2 pager;
//
//    @BindView(R.id.tabLayout)
//    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_booklist, container, false);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
        ScreenSlidePagerAdapter sectionsPagerAdapter = new ScreenSlidePagerAdapter(getActivity(), getChildFragmentManager());
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


}


