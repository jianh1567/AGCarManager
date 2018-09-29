package com.wind.carmanager.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wind.carmanager.R;
import com.wind.carmanager.activity.MainActivity;
import com.wind.carmanager.activity.load.LoginActivity;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.dialog.IOSAlertDialog;
import com.wind.carmanager.utils.AppManager;
import com.wind.carmanager.utils.BaseInfoSPUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wind.carmanager.utils.BaseInfoSPUtil.KEY_LOGIN_TOKEN;

/**
 * 作者：Created by luow on 2018/7/4
 * 注释：设置界面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.rl_tui_setting)
    RelativeLayout mRlTuiSetting;
    @BindView(R.id.rl_soft_version)
    RelativeLayout mRlSoftVersion;
    @BindView(R.id.rl_logot)
    RelativeLayout mRlLogot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @OnClick({R.id.iv_left, R.id.rl_tui_setting,
            R.id.rl_soft_version, R.id.rl_logot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.rl_tui_setting:
                showToast("推送设置");
                break;
            case R.id.rl_soft_version:
                showToast("软件版本");
                break;
            case R.id.rl_logot:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        //退出登录
        IOSAlertDialog iosAlertDialog = new IOSAlertDialog(this);
        iosAlertDialog.builder().setTitle("温馨提示").setMessage("是否确认退出登录!")
                .setNegativeButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        BaseInfoSPUtil.getInstance().removeSpData(SettingActivity.this, KEY_LOGIN_TOKEN);
                        openActivity(LoginActivity.class);
                        finish();
                        AppManager.getInstance().finishActivity(MainActivity.class);
                    }

                }).setPositiveButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        }).show();
    }
}
