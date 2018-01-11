package com.outhub.kocrdemo.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.outhub.kocrdemo.Utils.Cv4jUti;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by SummerRC on 18/1/8.
 * description:
 */

public class OcrAnalyzeData extends DataSource {
    private static final String TAG = "TessTwoManager";

//    private static final String lang = "eng";
    private static final String lang = "chi_sim";
    private static final String TESS_DATA = "tessdata";

    private Context mContext;
    private Bitmap mBitmap;
    private String mDataPath;       //路径下必须包含tessdata目录

    public OcrAnalyzeData(Context context, Bitmap bitmap) {
        mContext = context;
        mBitmap = bitmap;
        mDataPath = context.getCacheDir() + "/";
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        refreshData();
    }

    @Override
    public Object getData() {
        createDirForTessData();
        copyTessDataFiles();

        String content = "";
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        boolean suc = tessBaseAPI.init(mDataPath, lang);
        if (suc) {
            tessBaseAPI.setImage(Cv4jUti.getCv4jBitmap(mBitmap));
            content = tessBaseAPI.getUTF8Text();
            tessBaseAPI.end();
        } else {
            Toast.makeText(mContext, "TessBaseAPI 初始化失败", Toast.LENGTH_SHORT).show();
        }
        return content;
    }

    /**
     * 为训练数据创建目录
     */
    private void createDirForTessData() {
        File dir = new File(mDataPath + TESS_DATA + "/");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + mDataPath + TESS_DATA + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + mDataPath + TESS_DATA);
        }
    }

    /**
     * 将Assert目录下的训练数据拷贝到指定目录
     */
    private void copyTessDataFiles() {
        try {
            String fileList[] = mContext.getAssets().list(TESS_DATA);
            for (String fileName : fileList) {
                String pathToDataFile = mDataPath + TESS_DATA + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {
                    InputStream in = mContext.getAssets().open(TESS_DATA + "/" + fileName);
                    OutputStream out = new FileOutputStream(pathToDataFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }

}
