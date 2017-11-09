package com.totalplay.view.BaseSimpleRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jorgehdezvilla on 10/03/17.
 * EMP
 */

public abstract class BaseViewHolder<Item> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void populate(BaseViewHolder holder, int position, Item item);

}
