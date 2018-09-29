package com.wind.carmanager.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.utils.ToastUtils;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import butterknife.ButterKnife;

/**
 * 作者：Created by luow on 2018/5/9
 * 注释：
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private View view;

    /**
     * 最先调用
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 在onCreateView()之前调用，适合初始化数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 布局和控件初始化
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            if (null == view) {
                view = inflater.inflate(getLayoutId(), container, false);
                ButterKnife.bind(this, view);
                SupportMultipleScreensUtil.scale(view);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    protected int getLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 在onCreateView()之后调用
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 显示用户信息
     *
     * @param string 文字文本
     */
    public void showToast(String string) {
        ToastUtils.showToast(string);
    }
	

    /**
     * 打开activity,无参
     */
    public void openActivity(Class<? extends Activity> cls) {
        ((BaseActivity) getActivity()).openActivity(cls);
    }


    /**
     * 打开activity,带Intent参数
     */
    public void openActivity(Class<? extends Activity> cls, Intent intent) {
        ((BaseActivity) getActivity()).openActivity(cls, intent);
    }

    public void setTitle(int resId) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_top);
        TextView textView = (TextView) layout.findViewById(R.id.tv_activity_title);
        textView.setText(resId);
    }

    public void setOptionsInVisiable() {
        getOptionImageView().setVisibility(View.GONE);
    }

    public void setOptionsOnClickListener(View.OnClickListener listener) {
        getOptionImageView().setOnClickListener(listener);
    }

    public void setOptionImageBackground(int resId){
        getOptionImageView().setScaleX(0.6f);
        getOptionImageView().setScaleY(0.6f);
        getOptionImageView().setBackgroundResource(resId);
    }

    private ImageView getOptionImageView(){
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_top);
        ImageView optionsImage = (ImageView) layout.findViewById(R.id.btn_activity_options);
        return  optionsImage;
    }

}
