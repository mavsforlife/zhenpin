package com.cang.zhenpin.zhenpincang.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.icu.text.UFormat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.request.target.Target;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by victor on 2017/11/25.
 * Email: wwmdirk@gmail.com
 */

public class DownLoadImageUtil {

    public static final String IMAGE_PATH = "/images/";

    public static File savePicture(Context context, final String fileName, String url) throws Exception {

        checkFilePath(context);
        File f = new File(context.getExternalCacheDir() + IMAGE_PATH + fileName);
        if (f.exists()) {
            return f;
        }
        Bitmap bitmap =
                GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
        if (bitmap != null) {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return f;
        }
        return null;
    }

    private static void checkFilePath(Context context) {
        File f = new File(context.getExternalCacheDir() + IMAGE_PATH);
        if (f.exists()) {
            return;
        } else {
            f.mkdir();
        }
    }

    public static boolean checkFileExist(Context context, String fileName) {
        File f = new File(context.getExternalCacheDir() + IMAGE_PATH + fileName);
        return f.exists();
    }

    public static Bitmap getBitmap(Context context, String url) throws Exception {

        Bitmap bitmap =
                GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
        return bitmap;
    }

    public static File getCacheBitmapFromView(View view, String fileName) throws Exception{
        checkFilePath(view.getContext());
        File f = new File(view.getContext().getExternalCacheDir() + IMAGE_PATH + fileName);
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        if (bitmap != null) {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return f;
        }
        return null;
    }
}
