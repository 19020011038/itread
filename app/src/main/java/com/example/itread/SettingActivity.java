package com.example.itread;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itread.Util.HttpUtil;
import com.example.itread.Util.SharedPreferencesUtil;
import com.longsh.optionframelibrary.OptionBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferencesUtil check;
    private ImageView setting_icon;
    private Button setting_signout;
    private String result;

    private byte[] a = null;

    //拍照功能参数
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    //imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        //别忘了这句！！！！
        check = SharedPreferencesUtil.getInstance(getApplicationContext());

        setting_icon = findViewById(R.id.setting_icon);
        setting_signout = findViewById(R.id.setting_signout);

        setting_signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("zyr","setting_isLogin:"+check.isLogin());
                signoutWithOkHttp("http://47.102.46.161/user/logout");
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_change_nickname:  //修改昵称
                Intent intent2 = new Intent(SettingActivity.this, ChangeNicknameActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_change_password:  //修改密码
                Intent intent1 = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_icon:

                AlertDialog.Builder dialog = new AlertDialog.Builder (SettingActivity.this);
                dialog.setTitle("是否更改头像？");
                dialog.setMessage("若更改头像请点击确定");
                dialog.setCancelable(false);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(Home.this, "相机相机相册相册", Toast.LENGTH_SHORT).show();
                        List<String> stringList = new ArrayList<String>();
                        stringList.add("拍照");
                        stringList.add("从相册选择");
                        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(SettingActivity.this, stringList);
                        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //取消底部弹框
                                //optionBottomDialog.dismiss();
                                switch (position) {
                                    case 0:
                                        //测试使用，验证是否为position= 0
                                        //启动相机程序
                                        //隐式Intent
                                        Intent intent_photo = new Intent( "android.media.action.IMAGE_CAPTURE" );
                                        //putExtra()指定图片的输出地址，填入之前获得的Uri对象
                                        imageUri = SettingActivity.ImageUtils.getImageUri( SettingActivity.this );
                                        intent_photo.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
                                        //startActivity( intent_photo );
                                        startActivityForResult( intent_photo, TAKE_PHOTO );
                                        //底部弹框消失
                                        optionBottomDialog.dismiss();
                                        break;
                                    case 1:
                                        //测试使用，验证是否为position= 1
                                        //Toast.makeText(RegisterIn.this,"position 1", Toast.LENGTH_SHORT ).show();
                                        //打开相册
                                        openAlbum();
                                        //底部弹框消失
                                        optionBottomDialog.dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            //写到这了

                        });

                    }
                });
                dialog.show();
                break;
        }
    }


    //又在这开始写了
    //打开相册
    private void openAlbum() {
        Intent intent_album = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );

        startActivityForResult( intent_album, CHOOSE_PHOTO );
    }

    //剪切图片
    private void startImageCrop(File saveToFile,Uri uri) {
        if(uri == null){
            return ;
        }
        Intent intent = new Intent( "com.android.camera.action.CROP" );
        Log.i( "Test", "startImageCrop: " + "执行到压缩图片了" + "uri is " + uri );
        intent.setDataAndType( uri, "image/*" );//设置Uri及类型
        //uri权限，如果不加的话，   会产生无法加载图片的问题
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra( "crop", "true" );//
        intent.putExtra( "aspectX", 1 );//X方向上的比例
        intent.putExtra( "aspectY", 1 );//Y方向上的比例
        intent.putExtra( "outputX", 250 );//裁剪区的X方向宽
        intent.putExtra( "outputY", 250 );//裁剪区的Y方向宽
        intent.putExtra( "scale", true );//是否保留比例
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.PNG.toString() );
        intent.putExtra( "return-data", false );//是否将数据保留在Bitmap中返回dataParcelable相应的Bitmap数据，防止造成OOM，置位false
        //判断文件是否存在
        //File saveToFile = ImageUtils.getTempFile();
        if (!saveToFile.getParentFile().exists()) {
            saveToFile.getParentFile().mkdirs();
        }
        //将剪切后的图片存储到此文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveToFile));
        Log.i( "Test", "startImageCrop: " + "即将跳到剪切图片" );
        startActivityForResult( intent, CROP_IMAGE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //需要对拍摄的照片进行处理编辑
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
                    Log.i("Test", "onActivityResult TakePhoto : " + imageUri);
                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //takephoto.setImageBitmap( bitmap );
                    //设置照片存储文件及剪切图片
                    File saveFile = SettingActivity.ImageUtils.getTempFile();
                    filePath = SettingActivity.ImageUtils.getTempFile();
                    startImageCrop(saveFile, imageUri);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //选中相册照片显示
                    Log.i("Test", "onActivityResult: 执行到打开相册了");
                    try {
                        imageUri = data.getData(); //获取系统返回的照片的Uri
                        Log.i("Test", "onActivityResult: uriImage is " + imageUri);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //                        photo_taken.setImageBitmap(bitmap);
                        //设置照片存储文件及剪切图片
                        File saveFile = SettingActivity.ImageUtils.setTempFile(SettingActivity.this);
                        filePath = SettingActivity.ImageUtils.getTempFile();
                        startImageCrop(saveFile, imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_IMAGE:
                if (resultCode == RESULT_OK) {
                    Log.i("Test", "onActivityResult: CROP_IMAGE" + "进入了CROP");
                    // 通过图片URI拿到剪切图片
                    //bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
                    //通过FileName拿到图片
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath.toString());


                    //把裁剪后的图片展示出来
                    setting_icon.setImageBitmap(bitmap);
                    Bitmap bitmap1 = compressImage(bitmap);
                    File file = getFile(bitmap1);
                    Log.i("zyr", file.toString());
                    iconWithOkHttp("http://47.102.46.161/user/change_image", file);
                    //a = getBitmapByte(bitmap1);
//                    MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(Home.this);
//                    SQLiteDatabase database = myDataBaseHelper.getReadableDatabase();
//                    ContentValues values = new ContentValues();
//                    values.put("icon", a);
//                    database.update("user", values, "id=?", new String[]{user_id});
//                    database.close();
//                    String b = a.toString();
                    //Log.i( "zyr", "b" );

//                    Intent intent990 = new Intent(Home.this, Main.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent990);
                }
                break;
            default:
                break;
        }
    }

    public static class ImageUtils {
        private static String TAG = "Test";
        public static File tempFile;

        public static Uri getImageUri(Context content) {
            File file = setTempFile( content );
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT >= 24){
                //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = FileProvider.getUriForFile( content, "com.example.itread.fileprovider", file );
                return imageUri;                                                          //a15927.bottombardemo
            }else{
                //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
                Uri imageUri = Uri.fromFile( file );
                return imageUri;
            }
        }

        public static File setTempFile(Context content) {
            //自定义图片名称
            String name = DateFormat.format("yyyyMMdd_hhmmss", SettingActivity.ImageUtils.Calendar.getInstance( Locale.CHINA)) + ".png";
            Log.i( TAG, " name : "+name );
            //定义图片存放的位置
            tempFile = new File(content.getExternalCacheDir(),name);
            Log.i( TAG, " tempFile : "+tempFile );
            return tempFile;
        }

        public static File getTempFile() {
            return tempFile;
        }

        private static class Calendar {
            public static long getInstance(Locale china) {
                return 0;
            }
        }
    }

    //对bitmap进行质量压缩
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //在这里抽取了一个方法   可以封装到自己的工具类中...
    //bitmap转化成file
    public File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //修改头像
    public void iconWithOkHttp(String address, final File icon_file){
        HttpUtil.iconWithOkHttp(address, icon_file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " settingActivity : icon_error");
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
                        if (result.equals("头像修改成功")){
                            Toast.makeText(SettingActivity.this,"头像修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (result.equals("未上传图片")){
                            Toast.makeText(SettingActivity.this,"未上传图片",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("图片格式不正确")){
                            Toast.makeText(SettingActivity.this,"图片格式不正确",Toast.LENGTH_SHORT).show();
                        }else if (result.equals("未提交POST请求")){
                            Toast.makeText(SettingActivity.this,"提交请求失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//标签页
        });
    }

    //退出登录
    public void signoutWithOkHttp(String address){
        HttpUtil.signoutWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
                Log.i( "zyr", " signoutActivity : error");
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
                        if (result.equals("退出登录成功")){
                            Toast.makeText(SettingActivity.this,"退出登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            check.setLogin(false);
                            startActivity(intent);
                        }else if (result.equals("用户未登录")){
                            Toast.makeText(SettingActivity.this,"用户未登录,退出失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }//标签页
        });
    }
}
