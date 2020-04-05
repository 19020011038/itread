package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindPasswordActivity extends AppCompatActivity {

    private EditText find_username;
    private EditText find_password;
    private EditText find_email;
    private EditText find_repassword;
    private String header;
    private String result;
    private SharedPreferencesUtil check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_find_password);

        //别忘了这句！！！！
        check = SharedPreferencesUtil.getInstance(getApplicationContext());
        find_username = findViewById(R.id.find_username);
        find_email = findViewById(R.id.find_email);
        find_password = findViewById(R.id.find_password);
        find_repassword = findViewById(R.id.find_repassword);

        setEditTextInputSpace(find_username);
        setEditTextInputSpace(find_email);
        setEditTextInputSpace(find_password);
        setEditTextInputSpace(find_repassword);

        //控制最大长度
        int maxLengthUserName =16;
        int maxLengthPassword = 18;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthUserName);
        find_username.setFilters(fArray);
        InputFilter[] fArray1 =new InputFilter[1];
        fArray1[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        find_password.setFilters(fArray1);
        find_repassword.setFilters(fArray1);
        find_email.setFilters(fArray1);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_get_emailnum:
                String findAddress="http://47.102.46.161/user/forget_password";

                String findAccount = find_username.getText().toString();
                String findEmail = find_email.getText().toString();
                String findPassword = find_password.getText().toString();
                String findRepassword = find_repassword.getText().toString();
                if (TextUtils.isEmpty(findAccount)){
                    Toast.makeText(FindPasswordActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(findEmail)){
                    Toast.makeText(FindPasswordActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(findPassword)){
                    Toast.makeText(FindPasswordActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(findRepassword)){
                    Toast.makeText(FindPasswordActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                }else {
                    if (!findPassword.equals(findRepassword)){
                        Toast.makeText(FindPasswordActivity.this,"新密码与确认密码不相符", Toast.LENGTH_SHORT).show();
                    } else {
                        if (findPassword.length() < 6) {
                            Toast.makeText(FindPasswordActivity.this,"新密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("zyr","Registeraccount:"+findAccount);
                            Log.i("zyr","Registeremail:"+findEmail);
                            Log.i("zyr","Registerpassword:"+findPassword);
                            Log.i("zyr","Registerrepassword:"+findRepassword);
                            findWithOkHttp(findAddress,findAccount,findEmail,findPassword,findRepassword);
                        }
                    }
                }
                break;
            case R.id.find_back:
                finish();
                break;
        }
    }

    //忘记密码
    public void findWithOkHttp(String address,String account,String email, String password, String repassword){
        HttpUtil.findWithOkHttp(address, account, email, password, repassword, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i("zyr","regiser.error");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                header = response.header("set-cookie");
                try{
                    JSONObject object = new JSONObject(responseData);
                    result = object.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "zyr", "find.error2okhttp:"+responseData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("验证码发送成功")) {
                            String JSESSIONID = header.substring(0, 43);
                            Log.i("zyr", "0");
                            Log.i("zyr", "register_jsessionid:" + JSESSIONID);
                            check.setCookie(true);//设置已获得cookie
                            check.saveCookie(JSESSIONID);//保存获得的cookie
                            Toast.makeText(FindPasswordActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FindPasswordActivity.this, GetEmailNumberActivity.class);
                            startActivity(intent);
                        }else if (result.equals("该用户已经被冻结")) {
                            Toast.makeText(FindPasswordActivity.this, "该用户已经被冻结", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("两次密码不一致'")) {
                            Toast.makeText(FindPasswordActivity.this, "两次密码不一致'", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("新密码格式不正确")) {
                            Toast.makeText(FindPasswordActivity.this, "新密码格式不正确", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名与邮箱不匹配")) {
                            Toast.makeText(FindPasswordActivity.this, "用户名与邮箱不匹配", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名不存在")) {
                            Toast.makeText(FindPasswordActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                            Log.i("zyr","register.error:"+result);
                        }else if (result.equals("未提交全部参数")) {
                            Toast.makeText(FindPasswordActivity.this, "未提交全部参数", Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交POST请求")) {
                            Toast.makeText(FindPasswordActivity.this, "提交请求失败", Toast.LENGTH_SHORT).show();
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
