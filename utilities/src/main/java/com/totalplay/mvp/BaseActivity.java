package com.totalplay.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    private BasePresenter[] mPresenters;
    private BasePresenter mPresenter;
    private boolean toolbarEnabled;
    private boolean homeAsUpEnabled;

    protected void setToolbarEnabled(boolean enabled) {
        this.toolbarEnabled = enabled;
    }

    protected void setHomeAsUpEnabled(boolean enabled) {
        this.homeAsUpEnabled = enabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarEnabled = true;
        homeAsUpEnabled = true;
        mPresenters = getPresenters();
        mPresenter = getPresenter();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onCreate();
                }
            }
        }
        if (mPresenter != null) mPresenter.onCreate();
    }

    protected BasePresenter getPresenter() {
        return null;
    }

    protected BasePresenter[] getPresenters() {
        return null;
    }

    public void setSubtitle(String subtitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (toolbarEnabled) {
            View toolbar = findToolbar(findViewById(android.R.id.content));
            if (toolbar != null) {
                mToolbar = (Toolbar) toolbar;
                setupToolbar();
            } else {
                throw new InflateException("You must add a Toolbar on the Activity or " +
                        "setToolbarEnabled(false) before setContentView()");
            }
        }
    }

    private View findToolbar(View view) {
        if (view instanceof Toolbar) return view;
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                View possibleToolbar = findToolbar(child);
                if (possibleToolbar != null && possibleToolbar instanceof Toolbar)
                    return possibleToolbar;
            }
        }
        return null;
    }

    @Override
    protected void onStart() {
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
    protected void onResume() {
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
    protected void onPause() {
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
    protected void onStop() {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenters != null) {
            for (BasePresenter basePresenter : mPresenters) {
                if (basePresenter != null) {
                    basePresenter.onDestroy();
                }
            }
        }
        if (mPresenter != null) mPresenter.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null && homeAsUpEnabled)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
