package com.wind.carmanager.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.wind.carmanager.R;
import com.wind.carmanager.utils.AppManager;
import com.wind.carmanager.utils.PermissionListener;
import com.wind.carmanager.utils.ToastUtils;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private static PermissionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.scale(rootView);
        //绑定activity
        ButterKnife.bind(this);
    }

    /**
     * 设置Activity标题
     */
    public void setTitle(int resId) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_top);
        TextView textView = (TextView) layout.findViewById(R.id.tv_activity_title);
        textView.setText(resId);
    }

    /**
     * 设置点击监听器
     *
     * @param listener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_top);
        ImageView optionsButton = (ImageView) layout.findViewById(R.id.btn_activity_options);
        optionsButton.setOnClickListener(listener);
    }

    /**
     * 不显示设置按钮
     */
    public void setOptionsButtonInVisible() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_top);
        LinearLayout optionsButton = (LinearLayout) layout.findViewById(R.id.btn_activity_options);
        optionsButton.setVisibility(View.INVISIBLE);
    }


    /**
     * 打开activity,带Intent参数
     */
    public void openActivity(Class<? extends Activity> cls, Intent intent) {
        intent.setClass(BaseActivity.this, cls);
        startActivity(intent);
    }

    /**
     * 打开activity,无参
     */
    public void openActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(BaseActivity.this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.keep);
    }

    /**
     * 打开activity,带Bundle参数
     */
    public void openActivity(Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 隐藏输入法
     */
    public boolean hideInputMethod() {
        try {
            if (null != this.getCurrentFocus()) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                return im.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 回退事件
     *
     * @param v
     */
    public void onBack(View v) {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().removeActivity(this);
        super.onDestroy();
    }

    public void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    //-------------------------------------6.0以上权限封装----------------------------------------------------------

    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        Activity topActivity = AppManager.getTopActivity();
        if (topActivity == null) {
            return;
        }
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        if (mListener != null)
                            mListener.onGranted();

                    } else {
                        if (mListener != null)
                            mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

}