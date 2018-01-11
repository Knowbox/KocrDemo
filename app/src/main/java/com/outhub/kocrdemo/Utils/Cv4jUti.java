package com.outhub.kocrdemo.Utils;

import android.graphics.Bitmap;

import com.cv4j.core.binary.Threshold;
import com.cv4j.core.datamodel.ByteProcessor;
import com.cv4j.core.datamodel.CV4JImage;

/**
 * Created by SummerRC on 18/1/11.
 * description: 使用 cv4j 来实现图像的二值化处理，通过二值化将图像上
 *      的像素点的灰度值设置为0或255，凸显文本内容，便于后续步骤识别文本
 */

public class Cv4jUti {

    public static Bitmap getCv4jBitmap(Bitmap bitmap) {
        CV4JImage cv4JImage = new CV4JImage(bitmap);
        Threshold threshold = new Threshold();
        threshold.adaptiveThresh((ByteProcessor) (cv4JImage.convert2Gray().getProcessor()), Threshold.ADAPTIVE_C_MEANS_THRESH, 12, 30, Threshold.METHOD_THRESH_BINARY);
        return cv4JImage.getProcessor().getImage().toBitmap(Bitmap.Config.ARGB_8888);
    }
}
