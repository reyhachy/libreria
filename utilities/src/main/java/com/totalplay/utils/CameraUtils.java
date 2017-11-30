package com.totalplay.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.totalplay.utils.callbacks.OnPhotoImport;
import com.totalplay.utils.callbacks.OnPhotoTaken;
import com.totalplay.utils.callbacks.OnVideoImport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("unused")
public class CameraUtils {

    public static final int REQUEST_TAKE_PHOTO = 0x999;
    public static final int REQUEST_IMPORT_PHOTO = 0x998;
    private static final int REQUEST_IMPORT_VIDEO = 0x997;
    OnPhotoImport onPhotoImport;
    private File photoFile;
    private OnPhotoTaken onPhotoTaken;
    private Context context;
    private OnVideoImport onVideoImport;

    private void takePhoto(Context context, OnPhotoTaken onPhotoTaken, Fragment fragment,
                           AppCompatActivity appCompatActivity) {
        this.context = context;
        this.onPhotoTaken = onPhotoTaken;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                onPhotoTaken.onPhotoError(e);
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                if (fragment != null) {
                    fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                } else if (appCompatActivity != null) {
                    appCompatActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            onPhotoTaken.onPhotoError(new IllegalStateException("Not camera app installed"));
        }
    }

    public void takePhoto(AppCompatActivity context, OnPhotoTaken onPhotoTaken) {
        takePhoto(context, onPhotoTaken, null, context);
    }

    public void takePhoto(Fragment context, OnPhotoTaken onPhotoTaken) {
        takePhoto(context.getContext(), onPhotoTaken, context, null);
    }

    private void importPhoto(Context context, int message, OnPhotoImport callback, boolean allowMultiple,
                             Fragment fragment, AppCompatActivity activity) {
        this.context = context;
        this.onPhotoImport = callback;
        if (callback == null) {
            throw new NullPointerException("OnMultiplePhotoImport must not be null");
        }
        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (fragment != null) {
                    fragment.startActivityForResult(Intent.createChooser(intent, context.getString(message)),
                            REQUEST_IMPORT_PHOTO);
                } else if (activity != null) {
                    activity.startActivityForResult(Intent.createChooser(intent, context.getString(message)),
                            REQUEST_IMPORT_PHOTO);
                }
            } else {
                throw new IllegalArgumentException("Multiple import require minimum API 18");
            }
        } else {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (fragment != null) {
                fragment.startActivityForResult(i, REQUEST_IMPORT_PHOTO);
            } else if (activity != null) {
                activity.startActivityForResult(i, REQUEST_IMPORT_PHOTO);
            }
        }
    }

    public void importPhoto(AppCompatActivity context, int message,
                            OnPhotoImport callback, boolean allowMultiple) {
        importPhoto(context, message, callback, allowMultiple, null, context);
    }

    public void importPhoto(Fragment context, int message,
                            OnPhotoImport callback, boolean allowMultiple) {
        importPhoto(context.getContext(), message, callback, allowMultiple, context, null);
    }

    public void importVideo(Fragment context, int message,
                            OnPhotoImport callback) {
        importVideo(context.getContext(), message, callback, context, null);
    }

    public void importVideo(AppCompatActivity context, int message,
                            OnPhotoImport callback) {
        importVideo(context, message, callback, null, context);
    }

    private void importVideo(Context context, int message,
                             OnPhotoImport callback, Fragment fragment, AppCompatActivity appCompatActivity) {
        this.context = context;
        this.onPhotoImport = callback;
        if (callback == null) {
            throw new NullPointerException("OnPhotoImport must not be null");
        }
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (fragment != null) {
            fragment.startActivityForResult(Intent.createChooser(intent, context.getString(message)),
                    REQUEST_IMPORT_VIDEO);
        } else if (appCompatActivity != null) {
            appCompatActivity.startActivityForResult(Intent.createChooser(intent, context.getString(message)),
                    REQUEST_IMPORT_VIDEO);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.REQUEST_TAKE_PHOTO) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                onPhotoTaken.onPhotoSuccess(fullBitmap(), photoFile);
            } else {
                onPhotoTaken.onPhotoCancelled();
            }
        } else if (requestCode == REQUEST_IMPORT_PHOTO && resultCode == AppCompatActivity.RESULT_OK
                && null != data) {
            Uri selectedFile = data.getData();
            if (selectedFile != null) {
                CameraImportTask task = new CameraImportTask(context, new Uri[]{selectedFile},
                        onPhotoImport);
                task.execute();
            } else {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    Uri[] uris = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uris[i] = clipData.getItemAt(i).getUri();
                    }
                    CameraImportTask task = new CameraImportTask(context, uris, onPhotoImport);
                    task.execute();
                } else {
                    onPhotoImport.onErrorImport(new RuntimeException("Photos not loaded"));
                }
            }
        } else if (requestCode == REQUEST_IMPORT_VIDEO && resultCode == AppCompatActivity.RESULT_OK) {
            Uri selectedFile = data.getData();
            File file = new File(CameraImportTask.getRealPathFromURI(context, selectedFile));
            onVideoImport.onVideoImport(file);
            onVideoImport.onSuccessImport();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private Bitmap fullBitmap() {
        try {

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / 300, photoH / 300);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            return BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
