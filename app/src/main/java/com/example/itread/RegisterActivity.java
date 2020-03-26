package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private int a;
    private EditText register_username;
    private EditText register_password;
    private EditText register_email;
    private EditText register_repassword;
    private SharedPreferencesUtil check;
    private Button register_get_emailnum;
    private RelativeLayout register_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_repassword = findViewById(R.id.register_repassword);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        register_get_emailnum = findViewById(R.id.register_get_emailnum);
        register_back = findViewById(R.id.register_back);

        setEditTextInputSpace(register_username);
        setEditTextInputSpace(register_email);
        setEditTextInputSpace(register_password);
        setEditTextInputSpace(register_repassword);

        //控制最大长度
        int maxLengthUserName =16;
        int maxLengthPassword = 18;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        register_username.setFilters(fArray);
        InputFilter[] fArray1 =new InputFilter[1];
        fArray1[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        register_password.setFilters(fArray1);
        register_repassword.setFilters(fArray1);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_emailnum:
                String registerAddress="http://henuajy.zicp.vip/LoLBoxServer_war_exploded/RegisterServlet";
                String registerAccount = register_username.getText().toString();
                String registerEmail = register_email.getText().toString();
                String registerPassword = register_password.getText().toString();
                String registerRepassword = register_repassword.getText().toString();
                registerWithOkHttp(registerAddress,registerAccount,registerEmail,registerPassword,registerRepassword);
                break;
            case R.id.register_back:
                finish();
                break;
        }
    }

    //实现注册
    public void registerWithOkHttp(String address,String account,String email, String password, String repassword){
        HttpUtil.registerWithOkHttp(address, account, email, password, repassword, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Toast.makeText(RegisterActivity.this, "服务器连接异常", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, GetEmailNumberActivity.class);
                        intent.putExtra("email_num", responseData);// 传输验证码
                        startActivity(intent);
//                        if (responseData.equals("true")){
//                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }
        });
    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
