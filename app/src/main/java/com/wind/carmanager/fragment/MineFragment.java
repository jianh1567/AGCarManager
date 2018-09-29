package com.wind.carmanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.activity.mine.CarBindingActivity;
import com.wind.carmanager.activity.mine.ChangePwdActivity;
import com.wind.carmanager.activity.mine.SettingActivity;
import com.wind.carmanager.base.BaseFragment;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Created by luow on 2018/6/27
 * 注释：个人中心界面
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_image)
    CircleImageView mIvImage;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.rl_updata_pwd)
    RelativeLayout mRlUpdataPwd;
    @BindView(R.id.rl_car)
    RelativeLayout mRlCar;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvName.setText(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        setTitle(R.string.user_center);
        setOptionsInVisiable();
    }

    @OnClick({R.id.rl_updata_pwd, R.id.rl_car, R.id.rl_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_updata_pwd:
                openActivity(ChangePwdActivity.class);
                break;
            case R.id.rl_car:
                openActivity(CarBindingActivity.class);
                break;
            case R.id.rl_setting:
                openActivity(SettingActivity.class);
                break;
        }
    }
}
