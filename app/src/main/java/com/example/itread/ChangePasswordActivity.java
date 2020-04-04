package com.example.itread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText password_old;
    private EditText password_new;
    private EditText password_renew;
    private Button password_finish;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_password);

        password_old = findViewById(R.id.password_old);
        password_new = findViewById(R.id.password_new);
        password_renew = findViewById(R.id.password_renew);
        password_finish = findViewById(R.id.password_finish);

        setEditTextInputSpace(password_old);
        setEditTextInputSpace(password_new);
        setEditTextInputSpace(password_renew);

        //控制最大长度
        int maxLengthPassword = 18;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLengthPassword);
        password_old.setFilters(fArray);
        password_new.setFilters(fArray);
        password_renew.setFilters(fArray);

        password_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String passwordAddress="http://47.102.46.161/user/change_password";
                String old_password = password_old.getText().toString();
                String new_password = password_new.getText().toString();
                String renew_password = password_renew.getText().toString();
                if (TextUtils.isEmpty(old_password)){
                    Toast.makeText(ChangePasswordActivity.this,"请输入原密码", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(new_password)){
                    Toast.makeText(ChangePasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(renew_password)){
                    Toast.makeText(ChangePasswordActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                }else {
                    if (!new_password.equals(renew_password)){
                        Toast.makeText(ChangePasswordActivity.this,"新密码与确认密码不相符", Toast.LENGTH_SHORT).show();
                    } else {
                        if (new_password.length() < 6) {
                            Toast.makeText(ChangePasswordActivity.this,"新密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                        } else {
                            ChangePasswordWithOkHttp(passwordAddress,old_password,new_password,renew_password);
                        }
                    }
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_back:
                finish();
                break;
        }
    }

    //更改密码
    public void ChangePasswordWithOkHttp(String address, final String old_password, final String new_password, final String renew_password){
        HttpUtil.ChangePasswordWithOkHttp(address,old_password,new_password,renew_password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " error : ChangePasswordError");
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
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
                        if (result.equals("密码修改成功")){
                            Toast.makeText(ChangePasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (result.equals("原密码不正确")){
                            Toast.makeText(ChangePasswordActivity.this,"原密码不正确",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("两次密码不一致")){
                            Toast.makeText(ChangePasswordActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("新密码格式不正确")){
                            Toast.makeText(ChangePasswordActivity.this,"新密码格式不正确",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交全部参数")){
                            Toast.makeText(ChangePasswordActivity.this,"未提交全部参数",Toast.LENGTH_SHORT).show();
                            Log.i("zyr","error:ChangePasswordActivity 未提交全部参数");
                        }else if (result.equals("未提交POST请求")){
                            Toast.makeText(ChangePasswordActivity.this,"请求无法提交，密码修改失败",Toast.LENGTH_SHORT).show();
                            Log.i("zyr","error:ChangePasswordActivity post请求提交失败");
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
}
