package com.zgreenmatting;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by czf on 2017/5/13.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        mContext = BaseActivity.this;
        ButterKnife.bind(this);
        preInitView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        preInitData();
    }

    /**
     * onCreate
     */
    protected abstract int getContentLayout();//加载布局

    /**
     * onCreate
     */
    protected abstract void preInitView();//初始化控件

    /**
     * onResume
     */
    protected abstract void preInitData();//初始化数据
}
