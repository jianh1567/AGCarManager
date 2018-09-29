package com.wind.carmanager.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.wind.carmanager.R;
import com.wind.carmanager.appupdate.ConfirmDialog;
import com.wind.carmanager.appupdate.Constants;
import com.wind.carmanager.base.BaseFragment;
import com.wind.carmanager.model.HttpResponse;
import com.wind.carmanager.model.VersionNameBean;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.FileCallBack;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.okhttp.service.OperateDeviceRequest;
import com.wind.carmanager.utils.AppInfoUtils;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;
import com.wind.carmanager.widget.SwitchView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/6/27
 * 注释：车辆控制
 */
public class CarControlFragment extends BaseFragment {
    @BindView(R.id.sv_flameout)
    SwitchView mSvFlameout;
    @BindView(R.id.sv_report)
    SwitchView mSvReport;
    @BindView(R.id.rl_check_updata)
    RelativeLayout mRlCheckUpdata;
    private ConfirmDialog mConfirmDialog;
    private boolean isStop = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car_control;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.car_control);
        setOptionsInVisiable();
        //灵敏度设置
        if (BaseInfoSPUtil.getInstance().getSensitivity(getActivity()).equals("HIGH")) {
            mSvReport.setOpened(true);
        } else {
            mSvReport.setOpened(false);
        }

        mSvFlameout.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.setOpened(true);
                //打开电源
                requestOperateDevice("POWER", "ON");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
                //关闭电源
                requestOperateDevice("POWER", "OFF");
            }
        });

        mSvReport.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.setOpened(true);
                //灵敏度高
                requestOperateDevice("SENSITIVITY", "HIGH");
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
                //灵敏度低
                requestOperateDevice("SENSITIVITY", "LOW");

            }
        });
    }

    @OnClick(R.id.rl_check_updata)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_check_updata:
                // showToast("已是最新版本");
                requestViersion();
                break;
        }
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 操作设备
     **/
    private void requestOperateDevice(final String target, final String target_value) {

        OperateDeviceRequest request = new OperateDeviceRequest();
        request.setDevice_id(BaseInfoSPUtil.getInstance().getDeviceId(getActivity()));
        request.setTarget(target);
        request.setTarget_value(target_value);
        OkHttpUtils.put()
                .url(Api.EBIKE)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .requestBody(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            if (target.equals("POWER")) {
                                if (target_value.equals("OFF")) {
                                    showToast("电源已关");
                                } else {
                                    showToast("电源已关");
                                }
                            } else {
                                if (target_value.equals("HIGH")) {
                                    showToast("灵敏度已设置成高");
                                } else {
                                    showToast("灵敏度已设置成低");
                                }
                            }
                        } else if (mCode == 1001) {
                            showToast("没有有效的JSON数据");
                        } else if (mCode == 1002) {
                            showToast("缺少必须的参数");
                        } else if (mCode == 1003) {
                            showToast("设备不存在");
                        }
                    }
                });
    }

    //请求最新版本
    private void requestViersion() {
        OkHttpUtils.get()
                .url(Api.VERSION)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .build()
                .execute(new GenericsCallback<VersionNameBean>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(VersionNameBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            final VersionNameBean.ResultBean.VersionBean mVersionInfo = response.getResult().getVersion();
                            if (!mVersionInfo.getVersion_name().equals(AppInfoUtils.getVersionName(getActivity()))) {
                                //有新版本
                                mConfirmDialog = new ConfirmDialog(getActivity(), "有新版本更新啦", mVersionInfo.getContent(),
                                        "立即更新", "稍后更新", 1);
                                mConfirmDialog.setClickListener(new ConfirmDialog.BtnClickListener() {
                                    @Override
                                    public void doConfirm() {
                                        requestApkUrl(mVersionInfo.getPackage_name(), mVersionInfo.getVersion_name());
                                    }

                                    @Override
                                    public void doCancel() {
                                        mConfirmDialog.cancel();
                                    }

                                    @Override
                                    public void doNoDown() {
                                        mConfirmDialog.cancel();
                                        isStop = true;
                                    }
                                });
                                mConfirmDialog.show();
                            } else {
                                showToast("已是最新版本");
                            }
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                        }
                    }
                });
    }

    //请求下载链接
    private void requestApkUrl(String package_name, String version_name) {
        OkHttpUtils
                .get()
                .url(Api.PACKAGE)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .addParams("package_name", package_name)
                .addParams("version_name", version_name)
                .build()//
                .execute(new FileCallBack(Constants.SDCARD_PATH, Constants.APP_NAME) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        if (!isStop) {
                            int point = (int) (progress * 100);
                            mConfirmDialog.UpdateProgress(getString(R.string.app_download_loading),
                                    point + "%", "取消", "确定", point);
                            if (point == 100) {
                                mConfirmDialog.dismiss();
                                File file = new File(Constants.SDCARD_PATH, Constants.APP_NAME);
                                if (file != null) {
                                    install(file);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {

                    }

                    @Override
                    public void onResponse(File response, int id, int code) {

                    }
                });
    }

    public void install(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            Uri apkUri =
                    FileProvider.getUriForFile(getActivity(), "com.wind.carmanager.provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
}