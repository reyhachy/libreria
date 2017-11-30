package com.totalplay.utils.callbacks;

import android.graphics.Bitmap;

import java.io.File;


public interface OnPhotoTaken {

    void onPhotoSuccess(Bitmap fullBitmap, File file);
    void onPhotoError(Exception exception);
    void onPhotoCancelled();
}
