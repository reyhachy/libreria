package com.totalplay.view.BaseSimpleRecyclerView;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jorgehdezvilla on 11/03/17.
 * EMP
 */

@SuppressWarnings({"unchecked", "unused"})
public class BaseSimpleRecyclerView {

    public RecyclerView recyclerView;
    public BaseSimpleAdapter adapter;
    public LinearLayoutManager layout;
    private RefreshBaseRecyclerCallback mRefreshBaseReclycer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewGroup emptyView;
    private View parentView;

    public BaseSimpleRecyclerView(View parentView, int recyclerViewId, int swipeRefreshLayoutId) {
        this.parentView = parentView;
        recyclerView = (RecyclerView) parentView.findViewById(recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration(200);
        recyclerView.addItemDecoration(bottomOffsetDecoration);

        mSwipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(swipeRefreshLayoutId);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshBaseReclycer != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                mRefreshBaseReclycer.onRefreshItems();
            }
        });
    }

    public BaseSimpleRecyclerView(AppCompatActivity appCompatActivity, int recyclerViewId) {
        this.parentView = appCompatActivity.findViewById(android.R.id.content).getRootView();
        recyclerView = (RecyclerView) parentView.findViewById(recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));
    }

    public BaseSimpleRecyclerView(AppCompatActivity appCompatActivity, int recyclerViewId, int swipeRefreshLayoutId) {
        this.parentView = appCompatActivity.findViewById(android.R.id.content).getRootView();
        recyclerView = (RecyclerView) parentView.findViewById(recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(swipeRefreshLayoutId);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshBaseReclycer != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                mRefreshBaseReclycer.onRefreshItems();
            }
        });
    }


    public BaseSimpleRecyclerView(View view, int recyclerViewId) {
        recyclerView = (RecyclerView) view.findViewById(recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public BaseSimpleRecyclerView addBottomOffsetDecoration(int height) {
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration(height);
        recyclerView.addItemDecoration(bottomOffsetDecoration);
        return this;
    }

    public BaseSimpleRecyclerView setRefreshBaseRecycler(RefreshBaseRecyclerCallback refreshBaseRecycler) {
        mRefreshBaseReclycer = refreshBaseRecycler;
        return this;
    }

    public BaseSimpleRecyclerView setAdapter(BaseSimpleAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        return this;
    }

    public BaseSimpleRecyclerView setLayout(LinearLayoutManager layout){
        this.layout = layout;
        recyclerView.setLayoutManager(layout);
        return this;
    }

    public BaseSimpleRecyclerView setGridManager() {
        recyclerView.setLayoutManager(new GridLayoutManager(parentView.getContext(), 3));
        return this;
    }

    public BaseSimpleRecyclerView setEmptyView(int emptyViewId) {
        emptyView = (ViewGroup) parentView.findViewById(emptyViewId);
        return this;
    }

    public void update(List list) {
        adapter.update(list);
        if (emptyView != null) {
            if (list.size() > 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public static class BottomOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mBottomOffset;

        public BottomOffsetDecoration(int bottomOffset) {
            mBottomOffset = bottomOffset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }

        }
    }
}
