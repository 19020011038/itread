package com.example.itread;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.itread.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WriteBookCommentsActivity extends AppCompatActivity {
    private final static int CROP_IMAGE = 3;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private String url;
    private File file;
    //控件
    private ImageView back;
    private Button write_book_comments_publish;
    private EditText write_book_comments_title;
    private RatingBar write_book_comments_rating;
    private RichEditor write_book_comments_richeditor;
    private ImageView write_book_comments_album;
    private ImageView write_book_comments_image;
    private TextView write_book_comments_book_name;

    //数据
    private String title;
    private String image;
    private String score = "5.0";
    private String html = " ";
    private String book_name;
    private String book_id;
    private String user_status;

    private String result;
    private Handler handler;

    private String all_score;
    private String all_number;
    private String result_change_score;
    private String new_score;
    private boolean isNet;
    private Handler net_fail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_write_book_comments);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        back = (ImageView) findViewById(R.id.back_from_write_book_comments);
        write_book_comments_album = (ImageView) findViewById(R.id.write_book_comments_album);
        write_book_comments_book_name = (TextView) findViewById(R.id.write_book_comments_book_name);
        write_book_comments_image = (ImageView) findViewById(R.id.write_book_comments_image);
        write_book_comments_publish = (Button) findViewById(R.id.write_book_comments_publish);
        write_book_comments_richeditor = (RichEditor) findViewById(R.id.write_book_comments_richeditor);
        write_book_comments_rating = (RatingBar) findViewById(R.id.write_book_comments_rating);
        write_book_comments_title = (EditText) findViewById(R.id.write_book_comments_title);

        //接受数据
        Bundle bundle = getIntent().getExtras();
        book_id = bundle.getString("book_id");
        all_score = bundle.getString("all_score");
        all_number = bundle.getString("all_number");


        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //打分
        write_book_comments_rating.setStepSize((float) 0.5);
        write_book_comments_rating.setRating((float)5.0);
        write_book_comments_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = String.valueOf(rating);
            }
        });

        //写评论
        write_book_comments_richeditor.setPadding(30,0,30,0);
        write_book_comments_richeditor.setPlaceholder("点击此处输入您的评论...");
        write_book_comments_richeditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                html = text;
            }
        });
        isNet = HttpUtil.isNetworkConnected(WriteBookCommentsActivity.this);
        //发布
        write_book_comments_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNet = HttpUtil.isNetworkConnected(WriteBookCommentsActivity.this);
                if(isNet){
                    title = write_book_comments_title.getText().toString();
                    if("".equals(title))
                        Toast.makeText(WriteBookCommentsActivity.this,"请输入书评标题",Toast.LENGTH_SHORT).show();
                    else {
                        if(html.equals(" "))
                            Toast.makeText(WriteBookCommentsActivity.this,"请输入书评内容",Toast.LENGTH_SHORT).show();
                        else {
                            Log.d("book_id",book_id);
                            Log.d("title",title);
                            Log.d("html",html);
                            Log.d("score",score);

                            publishCommentsWithOkHttp("http://47.102.46.161/AT_read/book_review/?num="+book_id+"&type=l",title,html,score,book_id);
                        }
                    }
                }else {
                    Toast.makeText(WriteBookCommentsActivity.this,"网络不给力哦qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        });

        //点击相册突变的监听
        write_book_comments_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(WriteBookCommentsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WriteBookCommentsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    if(write_book_comments_richeditor.isFocused())
                        openAlbum();
                    else {
                        Toast.makeText(WriteBookCommentsActivity.this,"请您先选择评论输入框",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        if(isNet){
            bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);
        }


    }
    //打开相册
    private void openAlbum() {
        Intent intent_album = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );

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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您拒绝了请求", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //需要对拍摄的照片进行处理编辑
                    //拍照成功的话，就调用BitmapFactory的decodeStream()方法把图片解析成Bitmap对象，然后显示
//                    Log.i("Test", "onActivityResult TakePhoto : " + imageUri);
//                    //Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
//                    //takephoto.setImageBitmap( bitmap );
//                    //设置照片存储文件及剪切图片
//                    File saveFile = SettingActivity.ImageUtils.getTempFile();
//                    filePath = SettingActivity.ImageUtils.getTempFile();
//                    startImageCrop(saveFile, imageUri);
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
                        File saveFile = WriteBookCommentsActivity.ImageUtils.setTempFile(WriteBookCommentsActivity.this);
                        file = WriteBookCommentsActivity.ImageUtils.getTempFile();
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
                    Bitmap bitmap = BitmapFactory.decodeFile(file.toString());




                    Bitmap bitmap1 = compressImage(bitmap);

                    Log.i("tttttttttttttttt", file.toString());
                    postFileWithOkHttp("http://47.102.46.161/user/image_upload",file);
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
            String name = DateFormat.format("yyyyMMdd_hhmmss", WriteBookCommentsActivity.ImageUtils.Calendar.getInstance( Locale.CHINA)) + ".png";
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

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            file = toFile(bitmap);
//            Double neicun = FileSizeUtil.getFileOrFilesSize(imagePath,3);
//            if(neicun>3.46){
//                bitmap = reducingBitmapSampleFromPath(file.getPath(),300,300);
//                file = toFile(bitmap);
//            }
            postFileWithOkHttp("http://47.102.46.161/user/image_upload",file);
            Log.d("caocaocaocaocao",file.getName());
            Log.d("pppppppppppp",file.getPath());

        } else {
            Toast.makeText(this, "获取图片失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] getBitmapByte(Bitmap bitmap) {   //将bitmap转化为byte[]类型也就是转化为二进制
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    public File toFile(Bitmap bitmap) {
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

    //转png
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //转png2
    public static void saveBitmapAsPng(Bitmap bmp,File f) {
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//转jpg就把Bitmap.CompressFormat.PNG改成Bitmap.Compressformat.JPEG

    //获得图书信息的方法
    public void bookWithOkHttp(String address) {
        HttpUtil.bookWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("aaaaa", responseData);
                try {
                    //解析book
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("book");
                    book_name = jsonObject1.getString("title");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("img");   //image
                    image = jsonObject2.getString("img_s");               //image
                    Log.d("dddddddddd", book_name);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WriteBookCommentsActivity.this).load(image).into(write_book_comments_image);
                            write_book_comments_book_name.setText(book_name);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //上传文件
    public void postFileWithOkHttp(String address,File file) {
        HttpUtil.postFileWithOkHttp(address,file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

                Message message = new Message();
                message.what = 1;
                net_fail.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                Log.d("qqqqqqqqqq", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    url = jsonObject.getString("result");
                    Log.d("777777777777777777",url);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            write_book_comments_richeditor.insertImage(url,"picvision\"style =\"max-width:100%");
                            write_book_comments_richeditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                                @Override
                                public void onTextChange(String text) {
                                    html = text;
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        net_fail = new Handler(WriteBookCommentsActivity.this.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    Toast.makeText(WriteBookCommentsActivity.this,"网络连接失败qwq\n请检查您的网络设置",Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    //发布评论
    public void publishCommentsWithOkHttp(String address, String title, String content, String score, String book_num) {
        HttpUtil.publishCommentsWithOkHttp(address, title, content, score, book_num, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                try {

                    JSONObject jsonObject = new JSONObject(responseData);
                    result = jsonObject.getString("result");
                    if(result.equals("200"));{
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    Log.d("长评的返回结果:",jsonObject.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        handler = new Handler(WriteBookCommentsActivity.this.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (true) {
                    WriteBookCommentsActivity.this.finish();
                    Toast.makeText(WriteBookCommentsActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                    all_score = String.valueOf((Float.valueOf(score)+Float.valueOf(all_score)));
                    all_number = String.valueOf((Float.valueOf(all_number)+(float)1.0));
                    new_score = String.valueOf(Float.valueOf(all_score) / Float.valueOf(all_number));
                    Log.d("计算的出的评分",new_score);
                    changeScoreWithOkHttp("http://47.102.46.161/AT_read/score/?num=" + book_id,new_score );
                }
            }
        };
    }
    //修改图书评分的方法
    public void changeScoreWithOkHttp(String address,String score) {
        HttpUtil.changeScoreWithOkHttp(address, score, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //得到服务器返回的具体内容
                final String responseData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    result_change_score = jsonObject.getString("result");
                    Log.d("result_change_score",result_change_score);
                    if(result_change_score.equals("200")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void closeKeyBoard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            View v = getCurrentFocus();
            closeSoftInput(this, v);
        }
    }
    // 关闭键盘输入法
    public static void closeSoftInput(Context context, View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
