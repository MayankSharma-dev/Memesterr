package com.ms.memesterr;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class SaveImage {

    public void dataSave(Bitmap bitmap, Context context){
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For API 29(Q) and above
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "meme" + ".jpg");
            //+System.currentTimeMillis()
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
                // For less than API 28 android 10 (it may require permission)
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File image = new File(imagesDir, "meme" + ".jpg");
                try {
                    fos = new FileOutputStream(image);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

        }

        try {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos).close();

            Toast.makeText(context, "Meme Saved.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void ShareIntent(String post_url , Context context){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, post_url);
        sendIntent.setType("text/plain");

        Intent share = Intent.createChooser(sendIntent, null);
        context.startActivity(share);

        Log.d(">>>>>>>>>>>>Share", "" + post_url);
    }

}
