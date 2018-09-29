package com.wind.carmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.wind.carmanager.R;
import com.wind.carmanager.adapter.HistoryTrackAdapter;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.model.HistoryTrackBean;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;
import com.wind.carmanager.utils.TimeUtil;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by houjian on 2018/7/4.
 */

public class HistoryTrackActivity  extends BaseActivity{
    private static final String TAG = "HistoryTrackActivity";
    private List<HashMap<String, Object>> mHistoryTrackList = new ArrayList<>();
    private int mRefreshHistoryStatus = -1;
    private TwinklingRefreshLayout mTrlHistoryTrack;
    private HistoryTrackAdapter mHistoryTrackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_history_track, null);
        SupportMultipleScreensUtil.scale(view);
        setContentView(view);
        setTitle(R.string.history_track_descb);

        initRecylerView(view);
        getHistoryTrack();
        initListener(view);
    }

    private void getHistoryTrack(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("page_size", "20");
        hashMap.put("page_number", "0");

        String historyTrackUrl = Api.HISTORY_TRACK + BaseInfoSPUtil.getInstance().getDeviceId(this);
        OkHttpUtils.get()
                   .url(historyTrackUrl)
                   .params(hashMap)
                   .addHeader("Authorization", "Bearer "+ BaseInfoSPUtil.getInstance().getLoginToken(this))
                   .build()
                   .execute(new GenericsCallback<HistoryTrackBean>(new JsonGenericsSerializator()) {
                       @Override
                       public void onError(Call call, Exception e, int id, int code) {
                           Log.i(TAG, "onError e " + e.toString());
                           if (!NetUtil.isNetworkAvailable(HistoryTrackActivity.this)) {
                               showToast(getResources().getString(R.string.isNetWork));
                           }
                       }

                       @Override
                       public void onResponse(HistoryTrackBean response, int id, int code) {
                           int mCode = response.getCode();
                           if(mCode == 1000){
                               HistoryTrackBean.ResultBean.Traces[] traces = response.getResultBean().getTraces();
                               mHistoryTrackList.clear();
                               for(int i = 0; i < traces.length; i++){
                                   HashMap<String, Object> hashMap = new HashMap<>();
                                   hashMap.put("startTime", traces[i].getStartTime());
                                   hashMap.put("endTime", traces[i].getEndTime());
                                   hashMap.put("startAddress", traces[i].getStartAddress());
                                   hashMap.put("endAddress", traces[i].getEndAddress());
                                   hashMap.put("singleDistance", traces[i].getSingleDistance());
                                   hashMap.put("singleTime", traces[i].getSingleTime());
                                   hashMap.put("averageSpeed", traces[i].getAverageSpeed());
                                   mHistoryTrackList.add(hashMap);
                               }
                               mHistoryTrackAdapter.notifyDataSetChanged();
                           }else if(mCode == 1003){
                               showToast("Device does not exist");
                           }
                       }
                   });
    }

    private void initRecylerView(View view){
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.track_recycler_view);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);

        mHistoryTrackAdapter = new HistoryTrackAdapter(mHistoryTrackList);
        mHistoryTrackAdapter.setOnItemClickListener(new HistoryTrackAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("startTime", TimeUtil.timeToStamp((String) mHistoryTrackList.get(position).get("startTime"))/1000);//startTime 秒为单位
                intent.putExtra("endTime", TimeUtil.timeToStamp((String)mHistoryTrackList.get(position).get("endTime"))/1000);
                openActivity(TrackQueryActivity.class, intent);
            }
        });
        recyclerview.setAdapter(mHistoryTrackAdapter);
    }

    private void initListener(View view){
        mTrlHistoryTrack = (TwinklingRefreshLayout) view.findViewById(R.id.trl_history_track);
        mTrlHistoryTrack.setBottomView(new LoadingView(this));
        mTrlHistoryTrack.setHeaderView(new ProgressLayout(this));
        mTrlHistoryTrack.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mRefreshHistoryStatus = 0;
                getHistoryTrack();
                super.onRefresh(refreshLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retractHistoryTrackRefresh();
                    }
                }, 800);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mRefreshHistoryStatus = 1;
                getHistoryTrack();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retractHistoryTrackRefresh();
                    }
                }, 800);
            }
        });
    }

    private void retractHistoryTrackRefresh(){
        if(mRefreshHistoryStatus == 0){
            mTrlHistoryTrack.finishRefreshing();
        }else if(mRefreshHistoryStatus == 1){
            mTrlHistoryTrack.finishLoadmore();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
