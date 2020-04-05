package com.example.itread.Ui.fragment.tab;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itread.BookListActivity;
import com.example.itread.MainActivity;
import com.example.itread.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooklistFragment1 extends Fragment  {

    private Button bt;
    private TextView tv;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);

        InitSetting();

        InitEvent();

    }

    private void InitSetting() {



    }

    private void InitEvent() {
        Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), BookListActivity.class); //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity

                getActivity().startActivity(intent);

                startActivity(intent);
            }
        });
    }



//////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_booklist1, container, false);
///////


        bt = view.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "123456", Toast.LENGTH_SHORT).show();
            }
        });




        return view;
    }

    public void SetText(String content){
        tv.setText(content);

    }
 //   private void initUI() {

  //     bt = bt.findViewById(R.id.button);
 //       bt.setOnClickListener((View.OnClickListener) this);
 //   }
}
