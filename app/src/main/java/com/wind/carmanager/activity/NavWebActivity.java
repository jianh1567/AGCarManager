package com.wind.carmanager.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.base.BaseActivity;

public class NavWebActivity extends BaseActivity{
    private static final String TAG = "NavWebActivity";
    private FrameLayout mContainer;
    private WebView mWebView;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        setTitle(R.string.operating_guide_title);
        mContainer = (FrameLayout)findViewById(R.id.webviewContain);

        initWebViewSettings();

        createLoadingDialog(NavWebActivity.this, "加载中...");
        mWebView.loadUrl("file:///android_asset/html/text.html");
    }

    private void initWebViewSettings(){
        mWebView = new WebView(NavWebActivity.this);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mContainer.addView(mWebView);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                Log.i(TAG, "newProgress = " + newProgress);

                if (newProgress == 100) {
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mWebView.removeAllViews();
        mContainer.removeAllViews();
        mWebView = null;
        super.onDestroy();
    }

    public void createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        mLoadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        mLoadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        mLoadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        mLoadingDialog.show();
    }
}
