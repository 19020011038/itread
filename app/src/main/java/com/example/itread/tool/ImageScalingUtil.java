package com.example.itread.tool;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by ZhangXuan
 * 图像缩放工具类
 */
public class ImageScalingUtil {
    /**
     * 通过降低取样点压缩图片，不推荐直接使用<br/>
     * 压缩后图像使用RGB_565模式，即每个像素占位2字节，限定宽高压缩<br/>
     * 由于inSampleSize压缩比这个参数在不同手机表现不同，有的手机可以取任意整数，有的手机只能取2的幂数，则取2的幂数保证所有手机表现一致。<br/>
     * 需注意，由于inSampleSize的特性，若限定宽为1000x1000，实际图片宽为1010x600，则该图片会被压缩为505x300，图片会较小
     *
     * @param imgPath   原图片路径
     * @param reqWidth  最大宽度
     * @param reqHeight 最大高度
     * @return 压缩后的bitmap
     */
    public static Bitmap reducingBitmapSampleFromPath(String imgPath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 读取大小不读取内容
        options.inPreferredConfig = Config.RGB_565;// 设置图片每个像素占2字节，没有透明度
        BitmapFactory.decodeFile(imgPath, options);// options读取图片

        double outWidth = options.outWidth;
        double outHeight = options.outHeight;// 获取到当前图片宽高
        int inSampleSize = 1;

        /*
        先计算原图片宽高比ratio=width/height，再计算限定的范围的宽高比比reqRatio，
        若reqRatio > ratio，则说明限定的范围更加细长，则以高为标准计算inSampleSize
        否则，则说明限定范围更加粗矮，则以宽为计算标准
         */
        double ratio = outWidth / outHeight;
        double reqRatio = reqWidth / reqHeight;
        if (reqRatio > ratio)
            while (outHeight / inSampleSize > reqHeight) inSampleSize *= 2;
        else
            while (outWidth / inSampleSize > reqWidth) inSampleSize *= 2;

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 通过降低取样点压缩图片，不推荐直接使用<br/>
     * 压缩后图像使用RGB_565模式，即每个像素占位2字节，限定大小压缩<br/>
     * 由于inSampleSize压缩比这个参数在不同手机表现不同，有的手机可以取任意整数，有的手机只能取2的幂数，则取2的幂数保证所有手机表现一致。<br/>
     * 需注意，由于inSampleSize的特性，若限定大小为500k，而原图为501k，则压缩后的图片为125.25k，图片会较小
     *
     * @param imgPath 原图片路径
     * @param reqSize 目标文件大小，单位为kb
     * @return 压缩后的bitmap
     */
    public static Bitmap reducingBitmapSampleFromPath(String imgPath, int reqSize) {
        long area = reqSize * 1024 / 2;// 每个像素占2字节，将需求大小转为像素面积

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 读取大小不读取内容
        options.inPreferredConfig = Config.RGB_565;// 设置图片每个像素占2字节，没有透明度
        BitmapFactory.decodeFile(imgPath, options);// options读取图片

        double outWidth = options.outWidth;
        double outHeight = options.outHeight;// 获取到当前图片宽高

        int inSampleSize = 1;
        while ((outHeight / inSampleSize) * (outWidth / inSampleSize) > area)
            inSampleSize *= 2;

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 压缩图片<br/>
     * 通过设定压缩后的宽高的最大像素，将图片等比例缩小<br/>
     * 先通过降低取样点，将图片压缩到比目标宽高稍大一点，然后再通过Matrix将图片精确调整到目标大小<br/>
     * 压缩后图像使用RGB_565模式，即每个像素占位2字节，限定大小压缩<br/>
     * 若被压缩图片本身就小于限定大小，则不改变其大小，只更改图像颜色模式为RGB_565<br/>
     * 由于inSampleSize压缩比这个参数在不同手机表现不同，有的手机可以取任意整数，有的手机只能取2的幂数，则通过混合压缩的方式保证压缩的结果一致<br/>
     *
     * @param imgPath   原图片路径
     * @param reqWidth  最大宽度
     * @param reqHeight 最大高度
     * @return 压缩后的bitmap
     */
    public static Bitmap compressBitmapFromPath(String imgPath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 读取大小不读取内容
        options.inPreferredConfig = Config.RGB_565;// 设置图片每个像素占2字节，没有透明度
        BitmapFactory.decodeFile(imgPath, options);// options读取图片

        double outWidth = options.outWidth;
        double outHeight = options.outHeight;// 获取到当前图片宽高
        int inSampleSize = 1;

        /*
        先计算原图片宽高比ratio=width/height，再计算限定的范围的宽高比比reqRatio，
        若reqRatio > ratio，则说明限定的范围更加细长，则以高为标准计算inSampleSize
        否则，则说明限定范围更加粗矮，则以宽为计算标准
         */
        double ratio = outWidth / outHeight;
        double reqRatio = reqWidth / reqHeight;
        if (reqRatio > ratio)
            while (outHeight / inSampleSize > reqHeight) inSampleSize *= 2;
        else
            while (outWidth / inSampleSize > reqWidth) inSampleSize *= 2;

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        if (1 == inSampleSize) {
            // inSampleSize == 1，就说明原图比要求的尺寸小或者相等，那么不用继续压缩，直接返回。
            return BitmapFactory.decodeFile(imgPath, options);
        }
        /*
        否则的话，先将图片通过减少采样点的方式，以一个比限定范围稍大的尺寸读入内存，
        防止因为图片太大而OOM，以及太大的图片加载时间过长
        然后继续进行压缩的步骤
        */
        options.inSampleSize = inSampleSize / 2;
        Bitmap baseBitmap = BitmapFactory.decodeFile(imgPath, options);

        /*
        使用之前计算过的宽高比，
        若reqRatio > ratio，则说明限定的范围更加细长，则以高为标准计算压缩比
        否则，则说明限定范围更加粗矮，则以宽为计算标准
        */
        float compressRatio = 1;
        if (reqRatio > ratio)
            compressRatio = reqHeight * 1.0f / baseBitmap.getHeight();
        else
            compressRatio = reqWidth * 1.0f / baseBitmap.getWidth();

        Bitmap afterBitmap = Bitmap.createBitmap(
                (int) (baseBitmap.getWidth() * compressRatio),
                (int) (baseBitmap.getHeight() * compressRatio),
                baseBitmap.getConfig());
        Canvas canvas = new Canvas(afterBitmap);
        // 初始化Matrix对象
        Matrix matrix = new Matrix();
        // 根据传入的参数设置缩放比例
        matrix.setScale(compressRatio, compressRatio);
        Paint paint = new Paint();
        // 消除锯齿
        paint.setAntiAlias(true);
        // 根据缩放比例，把图片draw到Canvas上
        canvas.drawBitmap(baseBitmap, matrix, paint);
        return afterBitmap;
    }

    /**
     * 压缩图片<br/>
     * 通过设定压缩后的大小，将图片等比例缩小<br/>
     * 先通过降低取样点，将图片压缩到比目标宽高稍大一点，然后再通过Matrix将图片精确调整到目标大小<br/>
     * 压缩后图像使用RGB_565模式，即每个像素占位2字节<br/>
     * 若被压缩图片本身就小于限定大小，则不改变其大小，只更改图像颜色模式为RGB_565<br/>
     * 由于inSampleSize压缩比这个参数在不同手机表现不同，有的手机可以取任意整数，有的手机只能取2的幂数，则通过混合压缩的方式保证压缩的结果一致<br/>
     *
     * @param imgPath 原图片路径
     * @param reqSize 压缩后文件大小，单位为kb
     * @return 压缩后的bitmap
     */
    public static Bitmap compressBitmapFromPath(String imgPath, int reqSize) {
        long area = reqSize * 1024 / 2;// 每个像素占2字节，将需求大小转为像素面积

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 读取大小不读取内容
        options.inPreferredConfig = Config.RGB_565;// 设置图片每个像素占2字节，没有透明度
        BitmapFactory.decodeFile(imgPath, options);// options读取图片

        double outWidth = options.outWidth;
        double outHeight = options.outHeight;// 获取到当前图片宽高

        int inSampleSize = 1;
        while ((outHeight / inSampleSize) * (outWidth / inSampleSize) > area)
            inSampleSize *= 2;

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        if (1 == inSampleSize) {
            // inSampleSize == 1，就说明原图比要求的尺寸小或者相等，那么不用继续压缩，直接返回。
            return BitmapFactory.decodeFile(imgPath, options);
        }

        /*
        否则的话，先将图片通过减少采样点的方式，以一个比限定范围稍大的尺寸读入内存，
        防止因为图片太大而OOM，以及太大的图片加载时间过长
        然后继续进行压缩的步骤
        */
        options.inSampleSize = inSampleSize / 2;
        Bitmap baseBitmap = BitmapFactory.decodeFile(imgPath, options);

        /*
        目标大小的面积与现在图片大小的面积的比的平方根，就是缩放比
        java Math.sqrt() 函数不能开小数，而且先计算除法，再计算开放，再对结果求反误差很大，所以做两次开方计算
         */
        float compressRatio = 1;
        compressRatio = (float) (Math.sqrt(area) / Math.sqrt(baseBitmap.getWidth() * baseBitmap.getHeight()));

        Bitmap afterBitmap = Bitmap.createBitmap(
                (int) (baseBitmap.getWidth() * compressRatio),
                (int) (baseBitmap.getHeight() * compressRatio),
                baseBitmap.getConfig());
        Canvas canvas = new Canvas(afterBitmap);
        // 初始化Matrix对象
        Matrix matrix = new Matrix();
        // 根据传入的参数设置缩放比例
        matrix.setScale(compressRatio, compressRatio);
        Paint paint = new Paint();
        // 消除锯齿
        paint.setAntiAlias(true);
        // 根据缩放比例，把图片draw到Canvas上
        canvas.drawBitmap(baseBitmap, matrix, paint);
        return afterBitmap;
    }

    /**
     * 将一张图片 以PNG的格式 转换成 base64 编码
     *
     * @param bitmap
     * @return
     */
    public static String savePNGAndToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] pngByte = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(pngByte, Base64.DEFAULT);
    }

    /**
     * 将一张图片 以JPEG的格式 转换成 base64 编码
     *
     * @param bitmap
     * @return
     */
    public static String saveJPEGAndToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] pngByte = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(pngByte, Base64.DEFAULT);
    }
}