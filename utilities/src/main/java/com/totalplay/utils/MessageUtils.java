package com.totalplay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.totalplay.utilities.R;

/**
 * Created by totalplay on 11/22/16.
 * Enlace
 */
public class MessageUtils {
    private static ProgressDialog sProgressDialog;
    private static Toast mToast;

    public interface Callback {
        void onStringResult(String string);
    }

    public static void createDialog(Activity context, String title, String message, MessageUtils.Callback callback) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.cuastom_quantity_edittext);

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Aceptar", (dialog, whichButton) -> {
            callback.onStringResult(edt.getText().toString());
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
            dialog.dismiss();
        });
        dialogBuilder.setNegativeButton("Cancelar", (dialog, whichButton) -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
            dialog.dismiss();
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void toast(Context context, int message) {
        if (context == null) return;
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void toast(Context context, String message) {
        if (context == null) return;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void progress(Activity activity, int message) {
        stopProgress();
        if (!(activity).isFinishing()) {
            sProgressDialog = ProgressDialog.show(activity, null, activity.getString(message), true, false);
            sProgressDialog.show();
        }
    }

    public static void progress(Activity activity, String message) {
        stopProgress();
        if (!(activity).isFinishing()) {
            sProgressDialog = ProgressDialog.show(activity, null, message, true, false);
            sProgressDialog.show();
        }
    }

    public static void stopProgress() {
        try {
            if (sProgressDialog != null) {
                sProgressDialog.cancel();
                sProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
