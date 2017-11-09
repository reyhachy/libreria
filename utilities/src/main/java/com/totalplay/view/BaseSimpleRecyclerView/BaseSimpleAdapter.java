package com.totalplay.view.BaseSimpleRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jorgehdezvilla on 11/03/17.
 * EMP
 */

@SuppressWarnings({"unchecked", "unused"})
public abstract class BaseSimpleAdapter<Item, Holder extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected BaseSimpleRecyclerClickListener<Item> mBaseSimpleRecyclerClickListener;
    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBaseSimpleRecyclerClickListener.onBaseItemClick((Item) v.getTag());
        }
    };

    public View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            mBaseSimpleRecyclerClickListener.onBaseItemLongClick((Item) v.getTag());
            return false;
        }
    };

    protected List<Item> mItems = new ArrayList<>();

    public void update(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public abstract int getItemLayout();
    public abstract BaseViewHolder getViewHolder(View view);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(getItemLayout(), parent, false);
        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Item item = mItems.get(position);
        Holder viewHolder = (Holder) holder;
        viewHolder.populate(viewHolder, position, item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}