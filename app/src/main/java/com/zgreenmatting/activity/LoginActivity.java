package com.zgreenmatting.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.zgreenmatting.BaseFragmentActivity;
import com.zgreenmatting.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseFragmentActivity {

    @BindView(R.id.rg_login_register)
    RadioGroup rg_login_register;
    @BindView(R.id.rbtn_login)
    RadioButton rbtn_login;
    @BindView(R.id.rbtn_register)
    RadioButton rbtn_register;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Fragment loginFragment;
    Fragment registerFragment;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void preInitData() {

    }

    @Override
    protected int getContentId() {
        return 0;
    }

    @Override
    protected void showFragment(String tag) {

    }
}