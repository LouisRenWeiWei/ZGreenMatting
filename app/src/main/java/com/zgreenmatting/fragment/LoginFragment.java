package com.zgreenmatting.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.listener.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zgreenmatting.R;
import com.zgreenmatting.utils.NetworkUtils;
import com.zgreenmatting.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ASUS on 2017/5/17.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_login)
    TextView tv_login;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_login;
    }

    @OnClick({R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                toLogin();
                break;
        }
    }

    private void toLogin() {
        final String account = et_account.getText().toString().trim();
        final String passwd = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(account)){
            ToastUtils.showCustomerToast(mContext,"请输入账号");
            return;
        }
        if(TextUtils.isEmpty(passwd)){
            ToastUtils.showCustomerToast(mContext,"请输入密码");
            return;
        }
        if(!NetworkUtils.isNetworkAvailable(mContext)){
            ToastUtils.showCustomerToast(mContext,"请检查网络连接");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, "", new Listener<String>() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account",account);
                map.put("passwd", passwd);
                return map;
            }
        };
        Volley.getRequestQueue().add(request);
    }

    @Override
    protected void preInitData() {

    }
}
