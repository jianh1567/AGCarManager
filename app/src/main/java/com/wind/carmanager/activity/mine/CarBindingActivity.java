package com.wind.carmanager.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.carmanager.R;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.dialog.IOSAlertDialog;
import com.wind.carmanager.model.GetBikeBean;
import com.wind.carmanager.model.GetWarnBean;
import com.wind.carmanager.model.HttpResponse;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.okhttp.service.CarBindingRequest;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/4
 * 注释：车架号绑定界面
 */
public class CarBindingActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.et_car_number)
    EditText mEtCarNumber;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.ll_one)
    LinearLayout mLlOne;
    @BindView(R.id.tv_number)
    TextView mTvNumber;
    @BindView(R.id.btn_remove)
    Button mBtnRemove;
    @BindView(R.id.ll_two)
    LinearLayout mLlTwo;
    private int mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_binding);
        requestGetEbike();
    }

    @OnClick({R.id.iv_left, R.id.btn_login, R.id.btn_remove})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_login:
                hideInputMethod();
                if (TextUtils.isEmpty(mEtCarNumber.getText().toString())) {
                    showToast("请输入车架号码");
                    return;
                }
                requestCarBinding();
                break;
            case R.id.btn_remove:
                IOSAlertDialog iosAlertDialog = new IOSAlertDialog(this);
                iosAlertDialog.builder().setTitle("温馨提示").setMessage("您确定要解除当前绑定的车架号吗？")
                        .setNegativeButton("确定", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                requestRemoveBinding();
                            }

                        }).setPositiveButton("取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
        }
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 找回密码/验证手机验证码
     **/
    private void requestCarBinding() {
        CarBindingRequest bean = new CarBindingRequest();
        bean.setDevice_name(mEtCarNumber.getText().toString());
        OkHttpUtils.postString()
                .url(Api.EBIKE)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(CarBindingActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            BaseInfoSPUtil.getInstance().setDeviceName(CarBindingActivity.this, mEtCarNumber.getText().toString());
                            showToast("绑定成功");
                            requestGetEbike();
                        } else if (mCode == 1001) {
                            showToast("没有有效的JSON数据");
                        } else if (mCode == 1002) {
                            showToast("手机号未注册");
                        } else {
                            showToast(response.getMessage());
                        }
                    }
                });
    }

    private void requestRemoveBinding(){
        OkHttpUtils.delete()
                .url(Api.EBIKE + "/"+ mDeviceId)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .build()
                .execute(new GenericsCallback<GetWarnBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(CarBindingActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetWarnBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            showToast("解除成功");
                            requestGetEbike();
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                        }
                    }
                });
    }

    /**
     * 获取所有设备信息
     **/
    private void requestGetEbike() {
        OkHttpUtils.get()
                .url(Api.EBIKE)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .build()
                .execute(new GenericsCallback<GetBikeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                    }

                    @Override
                    public void onResponse(GetBikeBean response, int id, int code) {
                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            List<GetBikeBean.ResultBean.DevicesBean> devices = response.getResult().getDevices();
                            if (devices.size() == 0) {
                                mLlOne.setVisibility(View.VISIBLE);
                                mLlTwo.setVisibility(View.GONE);
                            } else {
                                mLlOne.setVisibility(View.GONE);
                                mLlTwo.setVisibility(View.VISIBLE);
                                mDeviceId = response.getResult().getDevices().get(0).getId();
                                mTvNumber.setText( response.getResult().getDevices().get(0).getName());
                            }
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                            mLlOne.setVisibility(View.VISIBLE);
                            mLlTwo.setVisibility(View.GONE);
                        }
                    }
                });
    }

}