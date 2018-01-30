package com.totalplay.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.totalplay.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by reype on 29/01/2018.
 * @deprecated
 */
public class ImageUtils {
    private static final int IMAGE_SCALE_OPTION = 2;
    private static Uri targetResource = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    public static void showAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("Aceptar", null).show();
    }

    public static Bitmap UriToBitMap(Context context, Uri data) {

        Bitmap b = null;
        if (data != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = IMAGE_SCALE_OPTION; // to save memory
                b = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(data), null, options);
            } catch (FileNotFoundException e) {

            }
        }
        return b;
    }

    public static Uri prepareImageFile(Context context, String imageFileName, String imageDescription) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, imageFileName);
        if (imageDescription != null) {
            values.put(MediaStore.Images.ImageColumns.DESCRIPTION, "Android Camera Image");
        }
        return context.getContentResolver().insert(targetResource, values);
    }

    public static String compressBitmapToBase64(Bitmap bitmap, int level) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, level, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        return Base64.encodeToString(FileUtils.bitmapToByteArray(bitmap), Base64.DEFAULT);
    }

    public static Bitmap base64toBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        try {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof VectorDrawable) {
                return getBitmap((VectorDrawable) drawable);
            } else {
                throw new IllegalArgumentException("unsupported drawable type");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();

            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            Bitmap b2 = Bitmap.createBitmap(
                    b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (b != b2) {
                b.recycle();
                b = b2;
            }
        }
        return b;
    }

    public static Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }




}
