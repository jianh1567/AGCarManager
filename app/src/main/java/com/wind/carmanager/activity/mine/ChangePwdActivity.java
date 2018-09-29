package com.wind.carmanager.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.carmanager.R;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.model.ForgetPwdBean;
import com.wind.carmanager.model.HttpResponse;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.okhttp.service.ForgetPwdRequest;
import com.wind.carmanager.okhttp.service.GetCodeRequest;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/4
 * 注释：修改密码-获取验证码界面
 */
public class ChangePwdActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.tv_phone_number)
    TextView mTvPhoneNumber;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    @BindView(R.id.btn_change_pwd)
    Button mBtnChangePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        mTvPhoneNumber.setText(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
    }

    @OnClick({R.id.iv_left, R.id.btn_code, R.id.btn_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_code:
                prepGetVerifyCode();
                break;
            case R.id.btn_change_pwd:
                if (TextUtils.isEmpty(mEtCode.getText().toString())) {
                    showToast(getString(R.string.txt_input_code));
                    return;
                }

                requestPassword();

                break;
        }
    }

    //倒计时相关
    private void prepGetVerifyCode() {

        mBtnCode.setEnabled(false);
        CountDownTimer timer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mBtnCode.setText("重新发送" + "(" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                mBtnCode.setEnabled(true);
                mBtnCode.setText("获取验证码");
            }
        };
        timer.start();
        requestVerifyCode();
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 发送验证码
     **/
    private void requestVerifyCode() {
        GetCodeRequest bean = new GetCodeRequest();
        bean.setPhone(mTvPhoneNumber.getText().toString());
        bean.setTarget("PASSWORD");
        OkHttpUtils.postString()
                .url(Api.GET_PHONE_CODE)
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(ChangePwdActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            showToast("发送成功");
                        } else if (mCode == 1001) {
                            showToast("没有有效的JSON数据");
                        } else if (mCode == 1002) {
                            showToast("手机号未注册");
                        }
                    }
                });
    }

    /**
     * 找回密码/验证手机验证码
     **/
    private void requestPassword() {
        ForgetPwdRequest bean = new ForgetPwdRequest();
        bean.setPhone(mTvPhoneNumber.getText().toString().trim());
        bean.setVeri_code(mEtCode.getText().toString());
        OkHttpUtils.postString()
                .url(Api.PASSWORD)
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<ForgetPwdBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(ChangePwdActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(ForgetPwdBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            Intent it = new Intent(ChangePwdActivity.this, ChangePwdSuccessActivity.class);
                            it.putExtra("token", response.getResult().getToken().getToken());
                            startActivity(it);
                        } else if (mCode == 1001) {
                            showToast("没有有效的JSON数据");
                        } else if (mCode == 1002) {
                            showToast("手机号未注册");
                        }
                    }
                });
    }
}