package com.totalplay.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.totalplay.utils.callbacks.OnPhotoImport;

import java.io.File;
import java.io.InputStream;


public class CameraImportTask extends AsyncTask<Void, CameraImportTask.FileBitmap, Boolean> {

    private final Uri[] uris;
    private OnPhotoImport onPhotoImport;
    private final Context context;
    private Exception exception;

    public CameraImportTask(Context context, Uri[] uris, OnPhotoImport onPhotoImport) {
        this.context = context;
        this.uris = uris;
        this.onPhotoImport = onPhotoImport;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onPhotoImport.onStartImport();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        InputStream is;
        try {
            for (Uri uri : uris) {
                is = context.getContentResolver().openInputStream(uri);
                if (is != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    publishProgress(new FileBitmap(bitmap, uri));
                    is.close();
                }
            }
        } catch (Exception e) {
            this.exception = e;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            String[] projection = {MediaStore.Images.Media.DATA};

            CursorLoader cursorLoader = new CursorLoader(context, contentUri, projection, null, null, null);
            Cursor cursor;

            cursor = cursorLoader.loadInBackground();


            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(FileBitmap... values) {
        super.onProgressUpdate(values);
        onPhotoImport.onPhotoImport(values[0].bitmap, new File(getRealPathFromURI(context, values[0].file)));

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean) onPhotoImport.onErrorImport(exception);
        else onPhotoImport.onSuccessImport();
    }

    class FileBitmap {
        Bitmap bitmap;
        Uri file;

        public FileBitmap(Bitmap bitmap, Uri file) {
            this.bitmap = bitmap;
            this.file = file;
        }
    }
}
