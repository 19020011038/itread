package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout login_register;
    private EditText login_username;
    private EditText login_password;
    private Button login_loginbtn;
    private SharedPreferencesUtil check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        login_register = findViewById(R.id.login_register);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_loginbtn = findViewById(R.id.login_loginbtn);

        login_loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //String loginAddress="http://www.tooltool.club/vip/demo?username=123456&password=123456";
                String loginAddress="http://www.tooltool.club/vip/demo?username=123456&password=123456";
                String loginAccount = login_username.getText().toString();
                String loginPassword = login_password.getText().toString();
                if (TextUtils.isEmpty(loginAccount)){
                    Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(loginPassword)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else {
                    loginWithOkHttp(loginAddress, loginAccount, loginPassword);
                }
            }
        });

        //防空格回车
        setEditTextInputSpace(login_username);
        setEditTextInputSpace(login_password);

        //控制最大长度
        int maxLengthUserName =16;
        int maxLengthPassword = 18;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        login_username.setFilters(fArray);
        InputFilter[] fArray1 =new InputFilter[1];
        fArray1[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        login_password.setFilters(fArray1);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    //实现登录
    public void loginWithOkHttp(String address, final String account, String password){
        HttpUtil.loginWithOkHttp(address,account,password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Toast.makeText(LoginActivity.this, "服务器获取信息失败", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")){
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            check.setLogin(true);  //设置登录状态为已登录
                            check.setAccountId(account);  //添加账户信息
                            Intent intent = new Intent(LoginActivity.this, NewBookActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                        }
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
