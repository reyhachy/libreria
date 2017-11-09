package com.totalplay.utils;

import android.content.Context;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.List;

/**
 * Created by jorgehdezvilla on 26/06/17.
 * FMM
 */

@SuppressWarnings("unused")
public class StringUtils {

    public static String cleanStringEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Boolean isEmptyString(String string) {
        return string == null || string.trim().equals("");
    }

    public static Boolean isNullOrEmptyString(String string) {
        if (string == null) return null;
        return string.trim().equals("");
    }

    public static String stringEmpty(String string) {
        return string != null && !string.trim().equals("") ? string.trim() : "-";
    }

    public static String stringNotEmpty(String string) {
        return string != null && !string.trim().equals("") ? string.trim() : "";
    }

    public static String getStringResourceByName(Context context, String aString) {
        String packageName = context.getApplicationContext().getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public static boolean validateList(List list) {
        return (list != null && list.size() > 0);
    }

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    public static String printDouble(Double value) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(value);
    }

    public static String formattedAmount(String amount) {
        String[] components = amount.split("\\.");
        if (components.length > 1) {
            if (components[1].length() < 1) {
                amount += "00";
            } else if (components[1].length() < 2) {
                amount += "0";
            }
        } else {
            amount = components[0];
        }
        return amount;
    }

}
