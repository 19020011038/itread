package com.example.itread;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
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
import android.util.Log;
import android.view.View;
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

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.itread.tool.ImageScalingUtil.reducingBitmapSampleFromPath;


public class WriteBookCommentsActivity extends AppCompatActivity {
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
        Intent intent = getIntent();
        book_id = intent.getStringExtra("book_id");


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
        write_book_comments_richeditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                html = text;
            }
        });
        //发布
        write_book_comments_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    openAlbum();
                }
            }
        });
        bookWithOkHttp("http://47.102.46.161/AT_read/book/?num=" + book_id);


    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
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
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        //将拍摄的图片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap compress_bitmap = compressImage(bitmap);
//                        file = toFile(compress_bitmap);


                        //
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
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
            bitmap = reducingBitmapSampleFromPath(file.getPath(),300,300);
            file = toFile(bitmap);
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
                            write_book_comments_richeditor.insertImage(url,"image");
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
                }
            }
        };
    }
}
