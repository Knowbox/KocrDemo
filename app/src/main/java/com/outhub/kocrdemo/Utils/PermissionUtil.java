package com.outhub.kocrdemo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by SummerRC on 18/1/8.
 * description:
 */

public class PermissionUtil {

    public static void checkWritePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    public static void checkCameraPermission(Activity activity) {
        if (!(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA))) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    public static void checkStrictModeForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }
}
