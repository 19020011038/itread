package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeNicknameActivity extends AppCompatActivity {

    private EditText nickname;
    private Button nickname_finish;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_nickname);

        nickname = findViewById(R.id.nickname_name);
        nickname_finish = findViewById(R.id.nickname_finish);

        setEditTextInputSpace(nickname);
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    nickname.setText(str1);
                    nickname.setSelection(start);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //控制最大长度
        int maxLengthUserName =16;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        nickname.setFilters(fArray);

        nickname_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nicknameAddress="http://47.102.46.161/user/change_name";
                String new_nickname = nickname.getText().toString();
//                try {
//                    new_nickname = URLEncoder.encode(new_nickname,"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    new_nickname = URLEncoder.encode(new_nickname,"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
////                try {
//                    new_nickname=new String(new_nickname.getBytes("ISO-8859-1"),"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                if (TextUtils.isEmpty(new_nickname)){
                    Toast.makeText(ChangeNicknameActivity.this,"新昵称不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i("zyr","new_nickname:"+new_nickname);
                    nicknameWithOkHttp(nicknameAddress, new_nickname);
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nickname_back:
                finish();
                break;
        }
    }

    //修改昵称
    public void nicknameWithOkHttp(String address, final String new_nickname){
        HttpUtil.nicknameWithOkHttp(address, new_nickname, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " nicknameActivity : error");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeNicknameActivity.this, "网络出现了问题...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
//
                try{
                    JSONObject object = new JSONObject(responseData);
                    result = object.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "LLL"+responseData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("昵称修改成功")){
                            Toast.makeText(ChangeNicknameActivity.this,"昵称修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (result.equals("昵称格式不正确")){
                            Toast.makeText(ChangeNicknameActivity.this,"昵称格式不正确",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交全部参数")){
                            Toast.makeText(ChangeNicknameActivity.this,"未提交全部参数",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交POST请求")){
                            Toast.makeText(ChangeNicknameActivity.this,"提交请求失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//标签页
        });
    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ") || source.toString().contentEquals("\n")) {
                return "";
            } else {
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
