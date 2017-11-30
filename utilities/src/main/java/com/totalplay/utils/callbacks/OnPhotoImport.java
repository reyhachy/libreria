package com.totalplay.utils.callbacks;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;


public interface OnPhotoImport extends OnImport{
    void onPhotoImport(Bitmap thumbnail, File file);
}
