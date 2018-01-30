package com.totalplay.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by reype on 29/01/2018.
 * @deprecated
 */
public class FileUtils {
    public static boolean writeToFile(File file, String content) {
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                Log.d("TAG", "Request deleted: " + created);
            }
            PrintWriter pr = new PrintWriter(new FileOutputStream(file));
            pr.println(content);
            pr.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File validFile(File file) {
        try {
            if (file.exists())
                if (!file.delete()) return null;
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) return null;
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Call to save file in Internal Storage
    public static void writeFileInInternalStorage(Context context, String fileName, byte[] byteArr) throws IOException {
        writeFile(context, context.getFilesDir(), fileName, byteArr);
    }

    // Call to read file from Internal Storage
    public static File readFileFromInternalStorage(Context context, String fileName) throws IOException {
        return readFile(context, context.getFilesDir(), fileName);
    }

    // Call to save file on Cache
    public static void writeFileInCacheStorage(Context context, String fileName, byte[] byteArr) throws IOException {
        writeFile(context, context.getCacheDir(), fileName, byteArr);
    }

    // Call to read file from External Storage
    public static File readFileFromCacheStorage(Context context, String fileName) throws IOException {
        return readFile(context, context.getCacheDir(), fileName);
    }

    // Starts writing files in Internal Storage
    public static void writeImageFileInExternalStorage(String fileName, byte byteArr[], String dir) throws ExternalStorageWriteException, IOException {
        if (isExternalStorageWritable()) {

            File myDir = new File(Environment.getExternalStorageDirectory() + "/"+dir);
            if (!myDir.exists()) {
                myDir.mkdir();
            }
            File file = new File(myDir + "/" + fileName + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArr);
            fileOutputStream.close();
        } else {
            throw new ExternalStorageWriteException("External storage is not writable!");
        }
    }


    // Starts writing files in Internal Storage
    public static String writeFileInExternalStorage(String fileName, byte byteArr[]) throws ExternalStorageWriteException, IOException {
        if (isExternalStorageWritable()) {

            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM) + "/Tesseract/tessdata/");
            if (!myDir.exists()) {
                final boolean newMyDir = myDir.mkdirs();
            }
            File file = new File(myDir + "/" + fileName);
            if (!file.exists()) {
                final boolean newFile = file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArr);
            fileOutputStream.close();
            return file.getPath();
        } else {
            throw new ExternalStorageWriteException("External storage is not writable!");
        }


    }

    // Starts reading files from External Storage
    public static byte[] readFileFromExternalStorage(String fileName)
            throws IOException, ExternalStorageReadException {
        byte[] byteArr;
        if (isExternalStorageReadable()) {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
            int size = (int) file.length();
            byteArr = new byte[size];
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.read(byteArr, 0, byteArr.length);
            bufferedInputStream.close();
        } else {
            throw new ExternalStorageReadException();
        }

        return byteArr;
    }

    // reads file with the selected storage method
    private static File readFile(Context context, File dirName, String fileName) throws IOException {
        return new File(dirName, fileName);
    }

    //  Writes file within the selected storage method
    private static void writeFile(Context context, File fileDir, String fileName, byte[] byteArr) throws IOException {
        File file = new File(fileDir, fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(byteArr);
        outputStream.close();
    }

    // Checks if External Storage is editable
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if External Memory is readable
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    // Converts Bitmaps into ByteArrays
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean scaled) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (scaled) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 850, 700, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        }
        return baos.toByteArray();
    }


    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, baos);
        return baos.toByteArray();
    }

    // Converts File into ByteArrays
    public static byte[] fileToByteArray(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum); //no doubt here is 0
                    //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                    System.out.println("read " + readNum + " bytes,");
                }
                return bos.toByteArray();
            } catch (Exception e) {

                return null;
            }
        } catch (Exception e) {

            return null;
        }
    }


    public static String getTimeStamp(String format) {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        return "MI_" + timeStamp + "." + format;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File getCacheFile(Context context, String nameFile) throws IOException {
        File cacheFile = new File(context.getCacheDir(), nameFile);
        try {
            try (InputStream inputStream = context.getAssets().open(nameFile)) {
                try (FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("Error de excepción", e.getMessage());
        }
        return cacheFile;
    }
}
