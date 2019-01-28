package com.aliyun.pusher.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

/**
 * @author Mulberry
 *         create on 2018/5/11.
 */

public class FileUtils {

    private static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator;
    private static String RESOURCE_DIR = "alivc_resource";
    private static String filename = RESOURCE_DIR + File.separator + "watermark.png";
//    public static final String waterMark = SD_DIR + filename;

    public static void copyAsset(Context context) {
        if(new File(SD_DIR + filename).exists()) {
            return;
        }
        if(SD_DIR != null && new File(SD_DIR).exists()) {

            Boolean isSuccess = true;
            AssetManager assetManager = context.getAssets();

            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                String newFileName = SD_DIR + filename;
                out = new FileOutputStream(newFileName);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
            }
        }
    }

    public static void copyAll(Context cxt) {
        File dir = new File(SD_DIR);
        copySelf(cxt,"alivc_resource");
        dir.mkdirs();
    }

    public static void copySelf(Context cxt, String root) {
        try {
            String[] files = cxt.getAssets().list(root);
            if(files != null && files.length > 0) {
                File subdir = new File(SD_DIR + root);
                if (!subdir.exists()) {
                    subdir.mkdirs();
                }
                for (String fileName : files) {
                    File file = new File(SD_DIR + root + File.separator + fileName);
                    if (file.exists()&&!file.isDirectory()){
                        continue;
                    }
                    copySelf(cxt,root + "/" + fileName);
                }
            }else{
                OutputStream myOutput = new FileOutputStream(SD_DIR+root);
                InputStream myInput = cxt.getAssets().open(root);
                byte[] buffer = new byte[1024 * 8];
                int length = myInput.read(buffer);
                while(length > 0)
                {
                    myOutput.write(buffer, 0, length);
                    length = myInput.read(buffer);
                }

                myOutput.flush();
                myInput.close();
                myOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
