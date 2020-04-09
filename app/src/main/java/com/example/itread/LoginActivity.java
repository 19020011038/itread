package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText login_username;
    private EditText login_password;
    private Button login_loginbtn;
    private SharedPreferencesUtil check;
    private String result;
    private String code;
    private String header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);


        //别忘了这句！！！！
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_loginbtn = findViewById(R.id.login_loginbtn);

        login_loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String loginAddress="http://47.102.46.161/user/login";
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

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forgetpassword:
                Intent intent1 = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    //实现登录
    public void loginWithOkHttp(String address, final String account, final String password){
        HttpUtil.loginWithOkHttp(address,account,password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " name : error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                header = response.header("set-cookie");
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
                        if (result.equals("登陆成功")){
                            String JSESSIONID=header.substring(0, 43);
                            Log.i("zyr","0");
                            Log.i("zyr","login_jsessionid:"+JSESSIONID);
                            check.setCookie(true);//设置已获得cookie
                            check.saveCookie(JSESSIONID);//保存获得的cookie
                            check.setLogin(true);  //设置登录状态为已登录
                            Log.i("zyr","islogin:"+check.isLogin());
                            check.setAccountId(account);  //添加账户信息
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (result.equals("用户名不存在")){
                            Toast.makeText(LoginActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名或者密码错误")){
                            Toast.makeText(LoginActivity.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("该用户已经被冻结")){
                            Toast.makeText(LoginActivity.this,"该用户尚未完成注册环节或找回密码，处于冻结状态",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交全部参数")){
                            Toast.makeText(LoginActivity.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交POST请求")){
                            Toast.makeText(LoginActivity.this,"提交请求失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//标签页
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        mainActivity.switchFragment(NewBookFragment);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);

    }
}
