package com.yellowseed.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.yellowseed.fragments.HomeBottomFragment.TAKE_PICTURE;


/**
 * this class is used for image operation
 */

@SuppressWarnings("ALL")
@SuppressLint("ALL")
public class TakePictureUtils {

    public static final int TAKE_PICTURE = 101;
    public static final int PICK_GALLERY = 201;
    public static final int CROP_FROM_CAMERA = 301;


    /**
     * this method is used for take picture from camera
     */
    public static void takePicture(Activity context, String fileName) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    /**
     * this method is used for take picture from gallery
     */
    public static void openGallery(Activity context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent, PICK_GALLERY);
    }


    /**
     * this method is used for copy stream
     */

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
