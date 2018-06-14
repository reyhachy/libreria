package com.totalplay.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.totalplay.utilities.R;

import java.lang.reflect.Field;

public class ExtendedNumberPicker extends NumberPicker {

    public ExtendedNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        Class<?> numberPickerClass = null;
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field selectionDivider = null;
        try {
            assert numberPickerClass != null;
            selectionDivider = numberPickerClass.getDeclaredField("mSelectionDivider");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            assert selectionDivider != null;
            selectionDivider.setAccessible(true);
            selectionDivider.set(this, null);
        } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextSize(getResources().getDimension(R.dimen.numberpicker_text_size));
            ((EditText) view).setTextColor(ContextCompat.getColor(getContext(), R.color.Color_Purple));

        }
    }
}