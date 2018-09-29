package com.wind.carmanager.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.TextureMapView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.wind.carmanager.MyApplication;
import com.wind.carmanager.R;
import com.wind.carmanager.activity.HistoryTrackActivity;
import com.wind.carmanager.activity.MainActivity;
import com.wind.carmanager.activity.NavWebActivity;
import com.wind.carmanager.activity.message.MessageCenterActivity;
import com.wind.carmanager.adapter.CarStatusAdapter;
import com.wind.carmanager.appupdate.ConfirmDialog;
import com.wind.carmanager.appupdate.Constants;
import com.wind.carmanager.base.BaseFragment;
import com.wind.carmanager.model.AllDeviceInfoBean;
import com.wind.carmanager.model.AllDeviceRealtimesBean;
import com.wind.carmanager.model.CarStatusInfo;
import com.wind.carmanager.model.DeviceRealtimeBean;
import com.wind.carmanager.model.GetBikeBean;
import com.wind.carmanager.model.HistoryTrackBean;
import com.wind.carmanager.model.VersionNameBean;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.FileCallBack;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.utils.AppInfoUtils;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.HttpUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.MapUtil;
import com.wind.carmanager.utils.NetUtil;
import com.wind.carmanager.utils.TimeUtil;
import com.wind.carmanager.widget.CirclePercentBar;
import com.wind.carmanager.model.AllDeviceInfoBean.ResultBean.Devices;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/6/27
 * 注释：车况界面
 */
public class VehicleConditionFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "VehicleConditionFm";
    private MapUtil mapUtil;
    private List<CarStatusInfo> mCarStatusList = new ArrayList<>();
    private static final int POSITION_TRACK = 0;
    private static final int POSITION_GUIDE = 3;
    private View mBaseView;
    private int mBattery;
    private int mDistance;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private String mTemp = "0度";
    private String mTrackInfo = "0km/0秒";

    CirclePercentBar batteryPercentBar;
    TextView mDistanceTv;
    TextView mLocation;
    CarStatusAdapter mCarStatusAdapter;
    private TextureMapView mTextureMapView;
    private TwinklingRefreshLayout mRefreshLayout;
    private ScrollView mScrollView;
    private int mRefreshVehicleCondition = -1;
    private MyTimerTask mTimerTask;
    private Timer mTimer;
    private ConfirmDialog mConfirmDialog;
    private boolean isStop = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vehicle_condition;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseView = view;

        mTimer = new Timer();
        init(view);
        initListener();
        initCarStatusList();
        initRecylerView(view);
        setOptionsOnClickListener(this);
        requestViersion();
        requestGetEbike();
    }

    /**
     * 初始化
     */
    private void init(View view) {
        batteryPercentBar = (CirclePercentBar) view.findViewById(R.id.pb_battery);
        batteryPercentBar.setPercentBattery(0.0f,new DecelerateInterpolator());

        mDistanceTv = view.findViewById(R.id.total_mileage);
        mDistanceTv.setText("0" + "km");

        mapUtil = MapUtil.getInstance();
        mapUtil.init((TextureMapView) view.findViewById(R.id.position_mapView));
        mTextureMapView = (TextureMapView) view.findViewById(R.id.position_mapView);

        mLocation = view.findViewById(R.id.current_location);
        mRefreshLayout = view.findViewById(R.id.trl_vehicle_condition);
        mScrollView = view.findViewById(R.id.slv_vehicle_condition);
    }

    private void initListener(){
        mRefreshLayout.setHeaderView(new ProgressLayout(getActivity()));
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setFloatRefresh(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mRefreshVehicleCondition = 0;
                requestGetEbike();
                super.onRefresh(refreshLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retractVehicleConditionRefresh();
                    }
                }, 800);
            }
        });

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(mRefreshLayout != null){
                    mRefreshLayout.setEnableRefresh(mScrollView.getScrollY() == 0);
                }
            }
        });
    }

    private void retractVehicleConditionRefresh(){
        if(mRefreshVehicleCondition == 0){
            mRefreshLayout.finishRefreshing();
        }
    }

    /**
     * 获取所有设备信息
     **/
    private void requestGetEbike() {
        OkHttpUtils.get()
                .url(Api.EBIKE)
                .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .build()
                .execute(new GenericsCallback<GetBikeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetBikeBean response, int id, int code) {
                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            List<GetBikeBean.ResultBean.DevicesBean> devices = response.getResult().getDevices();

                            Log.i(TAG, "devices.get(0).getName() = " + devices.get(0).getName());
                            BaseInfoSPUtil.getInstance().setDeviceName(getActivity(), devices.get(0).getName());
                            BaseInfoSPUtil.getInstance().setDeviceId(getActivity(), devices.get(0).getId());
                            BaseInfoSPUtil.getInstance().setSensitivity(getActivity(), (String) devices.get(0).getSensitivity());
                            getDeviceRealTimeInfo();
                            getHistoryTrackInfo();
                            startTimerTask();

                          /* String deviceName = BaseInfoSPUtil.getInstance().getDeviceName(getActivity());
                            Log.i(TAG, "deviceName = " + deviceName);
                            for(GetBikeBean.ResultBean.DevicesBean device : devices){
                                Log.i(TAG, "device = " + device.getName());
                                if(deviceName.equals(device.getName())){
                                    BaseInfoSPUtil.getInstance().setDeviceId(getActivity(), device.getId());
                                    BaseInfoSPUtil.getInstance().setSensitivity(getActivity(), (String) device.getSensitivity());

                                    getDeviceRealTimeInfo();
                                    getHistoryTrackInfo();
                                    startTimerTask();
                                }
                            }*/
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                            showToast("Unbound devices");
                        }
                    }
                });
    }

   /* private void getAllDeviceInfo(){
        OkHttpUtils.get()
                .url(Api.EBIKE)
                .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .build()
                .execute(new GenericsCallback<AllDeviceInfoBean>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Log.i(TAG, "onError e " + e.toString());
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(AllDeviceInfoBean response, int id, int code) {
                        int mCode = response.getCode();
                        if(mCode == 1000){
                            Devices[] devices = response.getResultBean().getDevices();
                            String deviceName = BaseInfoSPUtil.getInstance().getBoundDeviceName(getActivity());

                            for(Devices device : devices){
                                if(deviceName.equals(device.getName())){
                                    BaseInfoSPUtil.getInstance().setBoundDeviceId(getActivity(), device.getId());
                                    getDeviceRealTimeInfo();
                                    getHistoryTrackInfo();
                                    startTimerTask();
                                }
                            }
                        }else if(mCode == 1002){
                            showToast("Unbound devices");
                        }
                    }
                });
    }*/

    private void startTimerTask(){
        if(mTimer != null){
            if(mTimerTask != null){
                mTimerTask.cancel();
            }

            mTimerTask = new MyTimerTask();
            mTimer.schedule(mTimerTask,10000,30000);
        }
    }

    private void getDeviceRealTimeInfo(){
        String realTimeUrl = Api.ALL_DEVICE_REALTIME_INFO + "/" + BaseInfoSPUtil.getInstance().getDeviceId(getActivity());
        OkHttpUtils.get()
                .url(realTimeUrl)
                .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .build()
                .execute(new GenericsCallback<DeviceRealtimeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Log.i(TAG, "onError e " + e.toString());
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceRealtimeBean response, int id, int code) {
                        int mCode = response.getCode();
                        if(mCode == 1000){
                            DeviceRealtimeBean.ResultBean.RealTime realTime = response.getResultBean().getRealTime();

                            mBattery = realTime.getCarBattery();
                            mDistance = realTime.getCarDistance();
                            mLatitude = realTime.getCarLatitude();
                            mLongitude = realTime.getCarLongitude();
                            mAddress = realTime.getCarAddress();
                            mTemp = realTime.getCarTemperature()
                                    + getString(R.string.degree);

                            batteryPercentBar.setPercentBattery((float)mBattery,new DecelerateInterpolator());
                            mDistanceTv.setText(mDistance/1000 + "km");

                            mapUtil.init(mTextureMapView);
                            mapUtil.moveMapToCarPosition(mLatitude, mLongitude);
                            mLocation.setText(mAddress);

                            CarStatusInfo tempStatus = new CarStatusInfo(R.drawable.control_temp, mTemp, R.string.control_temp_descb);
                            mCarStatusList.set(2, tempStatus);
                            mCarStatusAdapter.notifyItemChanged(2);

                            Log.i(TAG, "address = " + mAddress);
                        }else if(mCode == 1001){
                            showToast("Device does not exist");
                        }
                    }
                });
    }

    private void getHistoryTrackInfo(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page_size", "20");
        hashMap.put("page_number", "0");

        String historyTrackUrl = Api.HISTORY_TRACK + BaseInfoSPUtil.getInstance().getDeviceId(getActivity());
        OkHttpUtils.get()
                .url(historyTrackUrl)
                .params(hashMap)
                .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))
                .build()
                .execute(new GenericsCallback<HistoryTrackBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Log.i(TAG, "onError e " + e.toString());
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HistoryTrackBean response, int id, int code) {
                        int mCode = response.getCode();
                        if(mCode == 1000){
                            HistoryTrackBean.ResultBean.Traces[] traces = response.getResultBean().getTraces();

                            if(traces.length > 0){
                                float distance = traces[0].getSingleDistance();
                                int singleTime = traces[0].getSingleTime();
                                String formatTime = TimeUtil.formatDateTime(singleTime);

                                mTrackInfo = distance + "km/" + formatTime;
                                CarStatusInfo historyTrack = new CarStatusInfo(R.drawable.history_track, mTrackInfo, R.string.history_track_descb);
                                mCarStatusList.set(0, historyTrack);
                                mCarStatusAdapter.notifyItemChanged(0);
                            }
                        }else if(mCode == 1003){
                            showToast("Device does not exist");
                        }
                    }
                });
    }

    private void initCarStatusList(){
        CarStatusInfo historyTrackInfo = new CarStatusInfo(R.drawable.history_track, mTrackInfo,
                                                                R.string.history_track_descb);
        CarStatusInfo fault_alarmInfo = new CarStatusInfo(R.drawable.fault_alarm,  getString(R.string.fault_alarm_title),
                                                                R.string.fault_alarm_descb);
        CarStatusInfo control_temp = new CarStatusInfo(R.drawable.control_temp, mTemp, R.string.control_temp_descb);
        CarStatusInfo operate_guide = new CarStatusInfo(R.drawable.operating_guide, getString(R.string.operating_guide_title),
                                                                R.string.operating_guide_descb);

        mCarStatusList.add(historyTrackInfo);
        mCarStatusList.add(fault_alarmInfo);
        mCarStatusList.add(control_temp);
        mCarStatusList.add(operate_guide);
    }

    private void initRecylerView(View view){
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutmanager = new LinearLayoutManager( getActivity(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerview.setLayoutManager(layoutmanager);
        mCarStatusAdapter = new CarStatusAdapter(mCarStatusList);
        recyclerview.setAdapter(mCarStatusAdapter);
        mCarStatusAdapter.setOnItemClickListener(new CarStatusAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case POSITION_TRACK:
                        openActivity(HistoryTrackActivity.class);
                        break;
                    case POSITION_GUIDE:
                        openActivity(NavWebActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_options:
                openActivity(MessageCenterActivity.class);
                break;
        }
    }


    public void onResume() {
        super.onResume();
        mapUtil.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapUtil.clear();
        mTimer.cancel();
    }

    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            Log.i(TAG, "getRunningActivityName() = " + getRunningActivityName());
            if(getRunningActivityName()
                    .equals("com.wind.carmanager.activity.MainActivity")){
                getDeviceRealTimeInfo();
                getHistoryTrackInfo();
            }
        }
    }

    private String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }


    //--------------------------------------------------版本更新--------------------------------------------
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
