package com.totalplay.view.CatalogEditText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.totalplay.fieldforcetpenlace.R;
import com.totalplay.fieldforcetpenlace.library.utils.StringUtils;

import java.util.List;

/**
 * Created by jorgehdezvilla on 13/10/17.
 * Enlace
 */

public class CatalogEditText<T extends CatalogObject> extends AppCompatEditText {

    public List<T> mItems;
    CharSequence mHint;
    OnCatalogSelectedListener<T> onCatalogSelectedListener;
    ListAdapter mSpinnerAdapter;
    private T mSelectedObject;

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

    public void setCatalogs(List<T> catalogs) {
        mItems = catalogs;
        setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mItems));
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
            ListView listView = (ListView) dialogView.findViewById(R.id.v_catalog_edit_text_list);
            EditText searchEditText = (EditText) dialogView.findViewById(R.id.v_catalog_edit_text_search);

            titleTextView.setText(mHint);
            listView.setAdapter(mSpinnerAdapter);
            builder.setView(dialogView);
            builder.setPositiveButton(R.string.dialog_cancel, null);

            AlertDialog dialog = builder.create();

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                T object = (T) listView.getAdapter().getItem(position);
                mSelectedObject = object;
                setText(object.toString());
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
            setText(StringUtils.stringNotEmpty(object.toString()));
        }
    }

    public T getSelectedValue() {
        return mSelectedObject;
    }

    public interface OnCatalogSelectedListener<T> {
        void onItemCatalogSelectedListener(T catalogItem, int selectedIndex);
    }

}