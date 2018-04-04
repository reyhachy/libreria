package com.totalplay.view.CatalogEditText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.totalplay.utilities.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jorgehdezvilla on 13/10/17.
 * Enlace
 */

@SuppressWarnings({"unchecked", "unused"})
public class CatalogEditText<T extends Serializable> extends AppCompatEditText {

    CharSequence mHint;
    OnCatalogSelectedListener<T> onCatalogSelectedListener;
    ListAdapter mSpinnerAdapter;
    public List<T> mItems;
    private T mSelectedObject;

    private String keyInstanceState = "";

    public CatalogEditText(Context context) {
        super(context);
        mHint = getHint();
    }

    public CatalogEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHint = getHint();
    }

    public CatalogEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHint = getHint();
    }

    public void setCatalogs(T[] catalogs) {
        mItems = new ArrayList<>();
        mItems = Arrays.asList(catalogs);
        setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mItems));
    }

    public void setCatalogs(List<T> catalogs) {
        mItems = catalogs;
        setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mItems));
    }

    public void setKeyInstanceState(String keyInstanceState) {
        this.keyInstanceState = keyInstanceState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setAdapter(ListAdapter adapter) {
        mSpinnerAdapter = adapter;
        configureOnClickListener();
    }

    private void configureOnClickListener() {
        setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            @SuppressLint("InflateParams")
            View dialogView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_catalog_edit_text, null);
            TextView titleTextView = (TextView) dialogView.findViewById(R.id.v_catalog_edit_text_title);
            final ListView listView = (ListView) dialogView.findViewById(R.id.v_catalog_edit_text_list);

            titleTextView.setText(mHint);
            listView.setAdapter(mSpinnerAdapter);
            builder.setView(dialogView);
            builder.setPositiveButton(R.string.dialog_cancel, null);

            final AlertDialog dialog = builder.create();

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                T object = (T) listView.getAdapter().getItem(position);
                mSelectedObject = object;
                CatalogEditText.this.setText(object.toString());
                if (onCatalogSelectedListener != null) {
                    onCatalogSelectedListener.onItemCatalogSelectedListener(object, position);
                }
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    public void setOnCatalogSelectedListener(OnCatalogSelectedListener<T> onCatalogSelectedListener) {
        this.onCatalogSelectedListener = onCatalogSelectedListener;
    }

    public void setSelectedObject(T object) {
        if (object != null) {
            mSelectedObject = object;
            setText(object.toString());
        }
    }


    public interface OnCatalogSelectedListener<T> {
        void onItemCatalogSelectedListener(T catalogItem, int selectedIndex);
    }

    public T getSelectedValue() {
        return mSelectedObject;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(keyInstanceState.isEmpty() ? String.valueOf(getId()) : keyInstanceState, getSelectedValue());
    }

    public void onLoadSaveInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            T item = (T) savedInstanceState.getSerializable(keyInstanceState.isEmpty() ? String.valueOf(getId()) : keyInstanceState);
            setSelectedObject(item);
        }
    }
}
