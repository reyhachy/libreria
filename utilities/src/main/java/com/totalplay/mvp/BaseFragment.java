package com.totalplay.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;


public abstract class BaseFragment extends Fragment {

    public Toolbar mToolbar;
    protected BasePresenter mPresenter;
    private BasePresenter[] mPresenters;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();
        mPresenters = getPresenters();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onCreate();
                }
            }
        }
        if (mPresenter != null) mPresenter.onCreate();
    }

    public BasePresenter[] getPresenters() {
        return null;
    }

    public BasePresenter getPresenter() {
        return null;
    }

    public void setSubtitle(String subtitle) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onStart();
                }
            }
        }
        if (mPresenter != null) mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onResume();
                }
            }
        }
        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onPause();
                }
            }
        }
        if (mPresenter != null) mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onStop();
                }
            }
        }
        if (mPresenter != null) mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onDestroy();
                }
            }
        }
        if (mPresenter != null) mPresenter.onDestroy();
        mPresenter = null;
    }


}
