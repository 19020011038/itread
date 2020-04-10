package com.example.itread;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itread.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetEmailNumberActivity extends AppCompatActivity {

    private EditText getemail;
    private Button email_finish;
    private RelativeLayout email_back;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_email_number);

        getemail = findViewById(R.id.email_get_email);
        email_finish = findViewById(R.id.email_finish);
        email_back = findViewById(R.id.email_back);

        setEditTextInputSpace(getemail);

        //控制最大长度
        int maxLength =10;
        InputFilter[] fArray =new InputFilter[1];
        fArray[0]=new  InputFilter.LengthFilter(maxLength);
        getemail.setFilters(fArray);

        email_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String getemailAddress="http://47.102.46.161/user/check_code";
                String getemail_string = getemail.getText().toString();
                if (TextUtils.isEmpty(getemail_string)){
                    Toast.makeText(GetEmailNumberActivity.this,"验证码为空", Toast.LENGTH_SHORT).show();
                }else {
                    emailWithOkHttp(getemailAddress, getemail_string);
                }
            }
        });

        email_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //输入验证码
    public void emailWithOkHttp(String address, final String email_num){
        HttpUtil.emailWithOkHttp(address,email_num, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " error : emailError");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GetEmailNumberActivity.this, "网络出现了问题...", Toast.LENGTH_SHORT).show();
                    }
                });
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
                    Log.i( "zyr", "email.error"+responseData);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("验证码验证成功")){
                            Toast.makeText(GetEmailNumberActivity.this,"验证码验证成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GetEmailNumberActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else if (result.equals("验证码错误")){
                            Toast.makeText(GetEmailNumberActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            Log.i("zyr","email_num:"+email_num);
                        }else if (result.equals("用户名已经激活")){
                            Toast.makeText(GetEmailNumberActivity.this,"该用户名已经激活",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("用户名不存在")){
                            Toast.makeText(GetEmailNumberActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交全部参数")){
                            Toast.makeText(GetEmailNumberActivity.this,"未提交全部参数",Toast.LENGTH_SHORT).show();
                            Log.i("zyr","error:ChangePasswordActivity 未提交全部参数");
                        }else if (result.equals("未提交POST请求")){
                            Toast.makeText(GetEmailNumberActivity.this,"请求无法提交",Toast.LENGTH_SHORT).show();
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
