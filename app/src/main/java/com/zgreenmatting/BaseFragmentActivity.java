package com.zgreenmatting;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends FragmentActivity {
    protected Context mContext;
    private FragmentManager fragmentManager;
    private Fragment mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        this.getWindow().setFormat(2);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        mContext = BaseFragmentActivity.this;
//        PushAgent.getInstance(mContext).onAppStart();
        fragmentManager = getSupportFragmentManager();
        preInitData();
    }

    protected abstract int getContentLayout();//加载布局

    protected abstract void preInitData();//初始化数据

    protected abstract int getContentId();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected Fragment findFragmentByTag(String tag){
        return fragmentManager.findFragmentByTag(tag);
    }

    protected abstract void showFragment(String tag);//显示fragment

    //fragment切换
    protected void switchContent(Fragment to, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mContent!=null&&to != mContent) {
            if (!to.isAdded()) {
                transaction.hide(mContent).add(getContentId(), to, tag).commit();
            } else {
                transaction.hide(mContent).show(to).commit();
            }
        }else {
            if(mContent==null){
                if (!to.isAdded()) {
                    transaction.add(getContentId(), to, tag).commit();
                }
            }else {//相等的情况
                transaction.show(to).commit();
            }
        }
        mContent = to;
        transaction = null;
    }
}
