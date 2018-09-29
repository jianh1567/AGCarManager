package com.wind.carmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.baidu.trace.model.CoordType;
import com.wind.carmanager.MyApplication;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.model.CarStatusInfo;
import com.wind.carmanager.model.DeviceRealtimeBean;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.CommonUtil;
import com.wind.carmanager.utils.Constants;
import com.wind.carmanager.utils.HttpUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.MapUtil;
import com.wind.carmanager.utils.NetUtil;
import com.wind.carmanager.utils.ViewUtil;
import com.wind.carmanager.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 轨迹查询
 */
public class TrackQueryActivity extends BaseActivity implements  View.OnClickListener{
    private static final String TAG = "TrackQueryActivity";
    private MyApplication trackApp = null;
    private ViewUtil viewUtil = null;
    private double mLatitude;
    private double mLongitude;

    /**
     * 地图工具
     */
    private MapUtil mapUtil = null;

    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();

    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 查询轨迹的开始时间
     */
    private long startTime = CommonUtil.getCurrentTime();

    /**
     * 查询轨迹的结束时间
     */
    private long endTime = CommonUtil.getCurrentTime();

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();


    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackquery);
        setTitle(R.string.track_query_title);

        trackApp = (MyApplication) getApplicationContext();
        init();

        startQueryHistoryTrack(getIntent());
    }

    private void startQueryHistoryTrack(Intent intent){
        startTime = intent.getLongExtra("startTime", 0);
        endTime = intent.getLongExtra("endTime", 0);

        trackPoints.clear();
        pageIndex = 1;

        ProcessOption processOption = new ProcessOption();
        processOption.setRadiusThreshold(10);

        processOption.setTransportMode(TransportMode.valueOf(TransportMode.riding.name()));

        processOption.setNeedDenoise(true);
        processOption.setNeedVacuate(true);
        processOption.setNeedMapMatch(true);

        historyTrackRequest.setProcessOption(processOption);
        historyTrackRequest.setSupplementMode(SupplementMode.valueOf(SupplementMode.driving.name()));

        sortType = SortType.valueOf(SortType.asc.name());
        historyTrackRequest.setSortType(sortType);

        historyTrackRequest.setCoordTypeOutput(CoordType.valueOf(CoordType.bd09ll.name()));
        historyTrackRequest.setProcessed(true);

        queryHistoryTrack();
    }

    /**
     * 初始化
     */
    private void init() {
        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.init((TextureMapView) findViewById(R.id.track_query_mapView));
        initListener();

        getDevicePositionInfo();
    }


    private void getDevicePositionInfo(){
        String realTimeUrl = Api.ALL_DEVICE_REALTIME_INFO + "/" + BaseInfoSPUtil.getInstance().getDeviceId(this);
        OkHttpUtils.get()
                .url(realTimeUrl)
                .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(this))
                .build()
                .execute(new GenericsCallback<DeviceRealtimeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Log.i(TAG, "onError e " + e.toString());
                        if (!NetUtil.isNetworkAvailable(TrackQueryActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceRealtimeBean response, int id, int code) {
                        int mCode = response.getCode();
                        if(mCode == 1000){
                            DeviceRealtimeBean.ResultBean.RealTime realTime = response.getResultBean().getRealTime();

                            mLatitude = realTime.getCarLatitude();
                            mLongitude = realTime.getCarLongitude();
                            mapUtil.moveMapToCarPosition(mLatitude, mLongitude);
                        }else if(mCode == 1001){
                            showToast("Device does not exist");
                        }
                    }
                });
    }

    /**
     * 轨迹查询设置回调
     *
     * @param historyTrackRequestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int historyTrackRequestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }

        trackPoints.clear();
        pageIndex = 1;

        if (data.hasExtra("startTime")) {
            startTime = data.getLongExtra("startTime", CommonUtil.getCurrentTime());
        }
        if (data.hasExtra("endTime")) {
            endTime = data.getLongExtra("endTime", CommonUtil.getCurrentTime());
        }

        ProcessOption processOption = new ProcessOption();
        if (data.hasExtra("radius")) {
            processOption.setRadiusThreshold(data.getIntExtra("radius", Constants.DEFAULT_RADIUS_THRESHOLD));
        }
        if (data.hasExtra("transportMode")) {
            processOption.setTransportMode(TransportMode.valueOf(data.getStringExtra("transportMode")));
        }
        if (data.hasExtra("denoise")) {
            processOption.setNeedDenoise(data.getBooleanExtra("denoise", true));
        }
        if (data.hasExtra("vacuate")) {
            processOption.setNeedVacuate(data.getBooleanExtra("vacuate", true));
        }
        if (data.hasExtra("mapmatch")) {
            processOption.setNeedMapMatch(data.getBooleanExtra("mapmatch", true));
        }
        historyTrackRequest.setProcessOption(processOption);

        if (data.hasExtra("supplementMode")) {
            historyTrackRequest.setSupplementMode(SupplementMode.valueOf(data.getStringExtra("supplementMode")));
        }
        if (data.hasExtra("sortType")) {
            sortType = SortType.valueOf(data.getStringExtra("sortType"));
            historyTrackRequest.setSortType(sortType);
        }
        if (data.hasExtra("coordTypeOutput")) {
            historyTrackRequest.setCoordTypeOutput(CoordType.valueOf(data.getStringExtra("coordTypeOutput")));
        }
        if (data.hasExtra("processed")) {
            historyTrackRequest.setProcessed(data.getBooleanExtra("processed", true));
        }

        queryHistoryTrack();
    }

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {
        trackApp.initRequest(historyTrackRequest);
        historyTrackRequest.setEntityName(BaseInfoSPUtil.getInstance().getDeviceName(this));
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constants.PAGE_SIZE);
        trackApp.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    /**
     * 按钮点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 轨迹查询选项
            case R.id.btn_activity_options:
                ViewUtil.startActivityForResult(this, TrackQueryOptionsActivity.class, Constants.REQUEST_CODE);
                break;

            default:
                break;
        }
    }


    private void initListener() {
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    viewUtil.showToast(TrackQueryActivity.this, response.getMessage());
                } else if (0 == total) {
                    viewUtil.showToast(TrackQueryActivity.this, getString(R.string.no_track_data));
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > Constants.PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack();
                } else {
                    mapUtil.drawHistoryTrack(trackPoints, sortType);
                }
            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                super.onLatestPointCallback(response);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != trackPoints) {
            trackPoints.clear();
        }

        trackPoints = null;
        mapUtil.clear();
    }

    public void onBack(View v) {
        super.onBackPressed();
    }
}