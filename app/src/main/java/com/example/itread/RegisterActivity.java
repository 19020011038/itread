package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_username;
    private EditText register_password;
    private EditText register_email;
    private EditText register_repassword;
    private SharedPreferencesUtil check;
    private RelativeLayout register_back;
    private String header;
    private String result;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_repassword = findViewById(R.id.register_repassword);
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
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
        register_email.setFilters(fArray1);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_get_emailnum:
                String registerAddress="http://47.102.46.161/user/register";
                Log.i("zyr","按了按钮");
                String registerAccount = register_username.getText().toString();
                String registerEmail = register_email.getText().toString();
                String registerPassword = register_password.getText().toString();
                String registerRepassword = register_repassword.getText().toString();
                if (TextUtils.isEmpty(registerAccount)){
                    Toast.makeText(RegisterActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(registerEmail)){
                    Toast.makeText(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(registerPassword)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(registerRepassword)){
                    Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                }else {
                    if (!registerPassword.equals(registerRepassword)){
                        Toast.makeText(RegisterActivity.this,"新密码与确认密码不相符", Toast.LENGTH_SHORT).show();
                    } else {
                        if (registerPassword.length() < 6) {
                            Toast.makeText(RegisterActivity.this,"新密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("zyr","Registeraccount:"+registerAccount);
                            Log.i("zyr","Registeremail:"+registerEmail);
                            Log.i("zyr","Registerpassword:"+registerPassword);
                            Log.i("zyr","Registerrepassword:"+registerRepassword);
                            registerWithOkHttp(registerAddress,registerAccount,registerEmail,registerPassword,registerRepassword);
                            progressDialog = new ProgressDialog(RegisterActivity.this);
                            progressDialog.setTitle("加载中");
                            progressDialog.setMessage("正在加载中......");
                            progressDialog.setCancelable(true);
                            progressDialog.show();

                        }
                    }
                }
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
                Log.i("zyr","regiser.error");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "网络出现了问题...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                header = response.header("set-cookie");
                try{
                    JSONObject object = new JSONObject(responseData);
                    result = object.getString("result");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("验证码发送成功")) {
                            String JSESSIONID = header.substring(0, 43);
                            Log.i("zyr", "0");
                            Log.i("zyr", "register_jsessionid:" + JSESSIONID);
                            check.setCookie(true);//设置已获得cookie
                            check.saveCookie(JSESSIONID);//保存获得的cookie
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, GetEmailNumberActivity.class);
                            startActivity(intent);
                        }else if (result.equals("邮箱格式不正确")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("密码格式不正确")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名格式不正确")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "用户名格式不正确", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名已经存在")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交全部参数")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "信息提交不全", Toast.LENGTH_SHORT).show();
                            Log.i("zyr","register.error:"+result);
                        }else if (result.equals("两次密码不一致")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交POST请求")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "提交请求失败", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("邮箱发送失败")) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "邮箱发送失败", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "register.error2okhttp:"+responseData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    //防止空格回车
    public static void setEditTextInputSpace(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editText.setText(str1);
                    editText.setSelection(start);
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
    }
}
