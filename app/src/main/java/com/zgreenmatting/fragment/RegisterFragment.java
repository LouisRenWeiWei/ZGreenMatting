package com.zgreenmatting.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.listener.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zgreenmatting.R;
import com.zgreenmatting.utils.NetworkUtils;
import com.zgreenmatting.utils.PhoneUtil;
import com.zgreenmatting.utils.RequestUtil;
import com.zgreenmatting.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ASUS on 2017/5/17.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_register;
    }

    @OnClick({R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                toRegiste();
                break;
        }
    }

    @Override
    protected void preInitData() {

    }

    private void toRegiste() {
        final String account = et_account.getText().toString().trim();
        final String passwd = et_password.getText().toString().trim();
        final String passwdConfirm = et_confirm_password.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showCustomerToast(mContext, "请输入账号");
            return;
        }
        if (TextUtils.isEmpty(passwd)) {
            ToastUtils.showCustomerToast(mContext, "请输入密码");
            return;
        }
        if (TextUtils.isEmpty(passwdConfirm)) {
            ToastUtils.showCustomerToast(mContext, "请输入确认密码");
            return;
        }
        if (passwdConfirm.equals(passwd)) {
            ToastUtils.showCustomerToast(mContext, "密码输入不一致");
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            ToastUtils.showCustomerToast(mContext, "请检查网络连接");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, RequestUtil.registe, new Listener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("code") == 200) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("account", account);
                map.put("password", passwd);
                map.put("device_id", PhoneUtil.getDevicesID(mContext));
                map.put("model", PhoneUtil.getBrand());
                return map;
            }
        };
        Volley.getRequestQueue().add(request);
    }




}
