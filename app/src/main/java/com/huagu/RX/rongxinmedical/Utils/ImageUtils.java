package com.huagu.RX.rongxinmedical.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fff on 2016/9/22.
 */
public class ImageUtils {



    /**
     * 保存裁剪之后的图片数据
     */
    public static File getImageToView(Bitmap photo,String filename,int saverate) {
        if (photo != null) {
            String path = Constant.AVATAR_SAVEPATH;
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            try {
                File myCaptureFile = new File(Constant.AVATAR_SAVEPATH+filename);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                photo.compress(Bitmap.CompressFormat.PNG, saverate, bos);
                bos.flush();
                bos.close();
                return myCaptureFile;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 判断图片是不是正常的竖着的，不是的话就旋转下
     * @param path  路径
     * @return
     * @throws IOException
     */
    public static File Bitmap2Vertical(String path) throws IOException {
        int degree = 0;
        // 从指定路径下读取图片，并获取其EXIF信息
        ExifInterface exifInterface = new ExifInterface(path);
        // 获取图片的旋转信息
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            default:
                return new File(path);
        }
//        exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION);
        Bitmap bitmap = BitmapFactory.decodeFile(path,null);
        if (bitmap==null) throw new NullPointerException(".Utils.ImageUtils.getBitmapDegree();判断图片时图片bitmap-> NullPointerException");
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree-90);
        Bitmap returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        File file = getImageToView(returnBm, "rongxin.png", 100);

        // 获取图片的旋转信息
        int aaa = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        return file;
    }




}
