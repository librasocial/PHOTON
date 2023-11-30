package com.ssf.homevisit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;

import com.ssf.homevisit.controller.UIController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CameraUtility {
    public static void captureImage(Activity context, int REQUEST_INTENT_RESULT) {
        captureCameraEvent(context, REQUEST_INTENT_RESULT, false);
    }

    private static void captureCameraEvent(Activity context, int REQUEST_INTENT_RESULT, boolean vid) {
        String mediaStore = MediaStore.ACTION_IMAGE_CAPTURE;
        if (vid) mediaStore = MediaStore.ACTION_VIDEO_CAPTURE;
        Intent takePictureIntent = new Intent(mediaStore);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(takePictureIntent, REQUEST_INTENT_RESULT);
        }
}

    public static File createImageFile(Context context) {
        String imageFileName =
                "IMG_" + new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.getDefault()).format(
                        new Date()
                );
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateBucketKey() {
        return UUID.randomUUID().toString();
    }


}
