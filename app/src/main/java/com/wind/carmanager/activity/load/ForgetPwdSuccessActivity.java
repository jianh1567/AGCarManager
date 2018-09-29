package com.wind.carmanager.activity.load;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.wind.carmanager.R;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.model.HttpResponse;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.okhttp.service.ResetPwdRequest;
import com.wind.carmanager.utils.AppManager;
import com.wind.carmanager.utils.CheckUtils;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/4
 * 注释：忘记密码界面
 */
public class ForgetPwdSuccessActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.cb_pwd)
    CheckBox mCbPwd;
    @BindView(R.id.et_once_pwd)
    EditText mEtOncePwd;
    @BindView(R.id.cb_one_pwd)
    CheckBox mCbOnePwd;
    @BindView(R.id.btn_success)
    Button mBtnSuccess;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_success);
        mToken = getIntent().getStringExtra("token");
        initListener();
    }

    @OnClick({R.id.iv_left, R.id.btn_success})
    public void onViewClicked(View view) {
        hideInputMethod();
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_success:
                if (TextUtils.isEmpty(mEtPwd.getText().toString())) {
                    showToast(getString(R.string.input_pwd_hint));
                } else if (!CheckUtils.isPassword(mEtPwd.getText().toString())) {
                    showToast(getString(R.string.txt_error_pwd));
                } else if (TextUtils.isEmpty(mEtOncePwd.getText().toString())) {
                    showToast(getString(R.string.input_pwd_hint));
                } else if (!mEtOncePwd.getText().toString().equals(mEtPwd.getText().toString())) {
                    showToast(getString(R.string.txt_pwd_no));
                } else {
                    requestResetPassword();
                }
                break;
        }
    }

    //EditText明文密文切换
    private void initListener() {
        mCbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        mCbOnePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    mEtOncePwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtOncePwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 找回密码/验证手机验证码
     **/
    private void requestResetPassword() {
        ResetPwdRequest bean = new ResetPwdRequest();
        bean.setPassword(mEtPwd.getText().toString().trim());
        OkHttpUtils.put()
                .url(Api.PASSWORD)
                .addHeader("Authorization", "Bearer " + mToken)
                .requestBody(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(ForgetPwdSuccessActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            showToast("密码重置成功");
                            finish();
                            AppManager.getInstance().finishActivity(ForgetPwdActivity.class);
                        } else if (mCode == 1001) {
                            showToast("没有有效的JSON数据");
                        } else if (mCode == 1002) {
                            showToast("手机号未注册");
                        }

                    }
                });
    }

}
