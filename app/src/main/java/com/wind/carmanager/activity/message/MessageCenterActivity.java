package com.wind.carmanager.activity.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.wind.carmanager.R;
import com.wind.carmanager.adapter.SevenDayMessageAdapter;
import com.wind.carmanager.adapter.TodayMessageAdapter;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.dialog.IOSAlertDialog;
import com.wind.carmanager.model.GetWarnBean;
import com.wind.carmanager.okhttp.OkHttpUtils;
import com.wind.carmanager.okhttp.api.Api;
import com.wind.carmanager.okhttp.callback.GenericsCallback;
import com.wind.carmanager.okhttp.service.DeleteSingleMsgRequest;
import com.wind.carmanager.utils.BaseInfoSPUtil;
import com.wind.carmanager.utils.DateUtils;
import com.wind.carmanager.utils.JsonGenericsSerializator;
import com.wind.carmanager.utils.NetUtil;
import com.wind.carmanager.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/3
 * 注释：消息中心界面
 */
public class MessageCenterActivity extends BaseActivity implements TodayMessageAdapter.OnAdapterDeleteClickListener, SevenDayMessageAdapter.OnAdapterSevenDeleteClickListener {

    @BindView(R.id.tv_today)
    TextView mTvToday;
    @BindView(R.id.tv_today_no_data)
    TextView mTvTodayNoData;
    @BindView(R.id.tv_seven_no_data)
    TextView mTvSevenNoData;
    @BindView(R.id.tv_seven_day)
    TextView mTvSevenDay;
    @BindView(R.id.view_line)
    ImageView mViewLine;
    @BindView(R.id.recycler_today)
    RecyclerView mRecyclerToday;
    @BindView(R.id.trl_today)
    TwinklingRefreshLayout mTrlToday;
    @BindView(R.id.recycler_seven_today)
    RecyclerView mRecyclerSevenToday;
    @BindView(R.id.trl_seven_today)
    TwinklingRefreshLayout mTrlSevenToday;

    private TodayMessageAdapter mTodayMessageAdapter;
    private SevenDayMessageAdapter mSevenDayMessageAdapter;

    //今天的刷新数据配置
    private int mTadayCurrentPage = -1;
    private final int mTadayPageCount = 10;
    private int mRefreshToadyStatus = -1;//刷新状态，-1,为正常进入界面的加载,0为刷新，1为加载更多

    //最近七天的刷新数据配置
    private int mSevenDayCurrentPage = -1;
    private final int mSevenDayPageCount = 10;
    private int mRefreshSevenDayStatus = -1;//刷新状态，-1,为正常进入界面的加载,0为刷新，1为加载更多

    private int mCurrentPosition = 0;
    //是否清空今天的所有数据
    private boolean isCleanToday = true;
    private List<GetWarnBean.ResultBean.WarnBean> mTodayList;
    private List<GetWarnBean.ResultBean.WarnBean> mSevenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        mViewLine.getLayoutParams().width = ScreenUtils.getScreenWidth(this) / 2;

        //今天的adapter
        mTodayList = new ArrayList<>();
        mTodayMessageAdapter = new TodayMessageAdapter(this, mTodayList);
        mTodayMessageAdapter.setAdapterDeleteOnClickListener(this);
        mRecyclerToday.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerToday.setAdapter(mTodayMessageAdapter);

        //最近七天的adapter
        mSevenList = new ArrayList<>();
        mSevenDayMessageAdapter = new SevenDayMessageAdapter(this, mSevenList);
        mSevenDayMessageAdapter.setAdapterSevenDeleteOnClickListener(this);
        mRecyclerSevenToday.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerSevenToday.setAdapter(mSevenDayMessageAdapter);

        initListener();
        requestToadyWarn();
    }

    @OnClick({R.id.iv_left, R.id.tv_today, R.id.tv_seven_day, R.id.tv_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_today:
                setTabLine(0);
                break;
            case R.id.tv_seven_day:
                setTabLine(1);
                break;
            case R.id.tv_clean:

                if (isCleanToday) {
                    cleanData(1, "是否删除今天的所有数据", -1);

                } else {
                    cleanData(2, "是否删除最近七天的所有数据", -1);
                }
                break;
        }
    }

    private void cleanData(final int type, String content, final int warn_id) {
        IOSAlertDialog iosAlertDialog = new IOSAlertDialog(this);
        iosAlertDialog.builder().setTitle("温馨提示").setMessage(content)
                .setNegativeButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (type == 1) {
                            requestCleanData(1, DateUtils.getCurrentDateNoMinute() + "/00", DateUtils.getTomorrowDate() + "/00");
                        } else if (type == 2) {
                            requestCleanData(2, DateUtils.getYesterdayDate(), DateUtils.getCurrentDateHour());
                        } else {
                            requestCleanData(type, warn_id);
                        }
                    }

                }).setPositiveButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    @Override
    public void onDeleteClick(int position) {
        cleanData(3, "是否删除该条数据", mTodayList.get(position).getId());
    }

    @Override
    public void onSevenDeleteClick(int position) {
        cleanData(4, "是否删除该条数据", mSevenList.get(position).getId());
    }

    //切换tab下划线
    private void setTabLine(int selectId) {
        setTextHint();
        switch (selectId) {
            case 0:
                mTadayCurrentPage = -1;
                requestToadyWarn();
                isCleanToday = true;
                mTvToday.setTextColor(getResources().getColor(R.color.white));
                mTvSevenDay.setTextColor(getResources().getColor(R.color.txt_login_hint));
                break;
            case 1:
                mSevenDayCurrentPage = -1;
                requestSevenWarn();
                isCleanToday = false;
                mTvSevenDay.setTextColor(getResources().getColor(R.color.white));
                mTvToday.setTextColor(getResources().getColor(R.color.txt_login_hint));
                break;
            default:
                break;
        }

        TranslateAnimation ta = new TranslateAnimation(mCurrentPosition * ScreenUtils.getScreenWidth(this) / 2,
                selectId * ScreenUtils.getScreenWidth(this) / 2,
                0, 0);
        ta.setDuration(100);
        ta.setFillAfter(true);
        mViewLine.startAnimation(ta);
        mCurrentPosition = selectId;
    }

    /**
     * 刷新的一些配置
     **/
    private void initListener() {
        //今天的刷新配置
        mRefreshToadyStatus = 0;
        LoadingView loadingView = new LoadingView(this);

        mTrlToday.setBottomView(loadingView);
        mTrlToday.setHeaderView(new ProgressLayout(this));
        mTrlToday.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mRefreshToadyStatus = 0;
                mTadayCurrentPage = -1;
                mTodayList.clear();
                requestToadyWarn();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mRefreshToadyStatus = 1;
                requestToadyWarn();
            }
        });

        //最近七天的刷新配置
        mRefreshSevenDayStatus = 0;
        LoadingView loadingView1 = new LoadingView(this);
        mTrlSevenToday.setBottomView(loadingView1);
        mTrlSevenToday.setHeaderView(new ProgressLayout(this));
        mTrlSevenToday.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mRefreshSevenDayStatus = 0;
                mSevenDayCurrentPage = -1;
                mSevenList.clear();
                requestSevenWarn();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mRefreshSevenDayStatus = 1;
                requestSevenWarn();
            }
        });
    }

    /**
     * Today停止刷新or加载
     */
    private void retractToadyRefresh() {
        if (mRefreshToadyStatus == 0) {
            mTrlToday.finishRefreshing();
        } else if (mRefreshToadyStatus == 1) {
            mTrlToday.finishLoadmore();
        }
    }

    /**
     * 最近七天停止刷新or加载
     */
    private void retractSevenDayRefresh() {
        if (mRefreshSevenDayStatus == 0) {
            mTrlSevenToday.finishRefreshing();
        } else if (mRefreshSevenDayStatus == 1) {
            mTrlSevenToday.finishLoadmore();
        }
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 获取今天指定设备历史推送消息
     **/
    private void requestToadyWarn() {
        OkHttpUtils.get()
                .url(Api.WARN + BaseInfoSPUtil.getInstance().getDeviceId(this))
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .addParams("start_time", DateUtils.getCurrentDateNoMinute() + "/00")
                .addParams("end_time", DateUtils.getTomorrowDate() + "/00")
                .addParams("page_number", ++mTadayCurrentPage + "")
                .addParams("page_size", mTadayPageCount + "")
                .build()
                .execute(new GenericsCallback<GetWarnBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(MessageCenterActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetWarnBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            if (mTadayCurrentPage == 0) {
                                mTodayList.clear();
                                if (response.getResult().getWarn().size() == 0) {
                                    mTvTodayNoData.setVisibility(View.VISIBLE);
                                    mTrlToday.setVisibility(View.GONE);
                                } else {
                                    mTvTodayNoData.setVisibility(View.GONE);
                                    mTrlToday.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (response.getResult().getWarn().size() == 0) {
                                    if (mTadayCurrentPage > 0) {
                                        mTadayCurrentPage--;
                                    }
                                    retractToadyRefresh();
                                    showToast("没有更多信息了");
                                    return;
                                }
                            }

                            mTodayList.addAll(response.getResult().getWarn());
                            mTodayMessageAdapter.notifyDataSetChanged();
                            retractToadyRefresh();

                        } else if (mCode == 1001) {
                            retractToadyRefresh();
                        } else if (mCode == 1002) {
                            retractToadyRefresh();
                        }
                    }
                });
    }

    /**
     * 获取最近七天指定设备历史推送消息
     **/
    private void requestSevenWarn() {
        OkHttpUtils.get()
                .url(Api.WARN + BaseInfoSPUtil.getInstance().getDeviceId(this))
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .addParams("start_time", DateUtils.getYesterdayDate())
                .addParams("end_time", DateUtils.getCurrentDateHour())
                .addParams("page_number", ++mSevenDayCurrentPage + "")
                .addParams("page_size", mSevenDayPageCount + "")
                .build()
                .execute(new GenericsCallback<GetWarnBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(MessageCenterActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetWarnBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            if (mSevenDayCurrentPage == 0) {
                                mSevenList.clear();
                                if (response.getResult().getWarn().size() == 0) {
                                    mTvSevenNoData.setVisibility(View.VISIBLE);
                                    mTrlSevenToday.setVisibility(View.GONE);
                                } else {
                                    mTvSevenNoData.setVisibility(View.GONE);
                                    mTrlSevenToday.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (response.getResult().getWarn().size() == 0) {
                                    if (mSevenDayCurrentPage > 0) {
                                        mSevenDayCurrentPage--;
                                    }
                                    retractSevenDayRefresh();
                                    showToast("没有更多信息了");
                                    return;
                                }
                            }
                            mSevenList.addAll(response.getResult().getWarn());
                            mSevenDayMessageAdapter.notifyDataSetChanged();
                            retractSevenDayRefresh();
                        } else if (mCode == 1001) {
                            retractSevenDayRefresh();
                        } else if (mCode == 1002) {
                            retractSevenDayRefresh();
                        }
                    }
                });
    }

    /**
     * 删除指定设备指定时间段的所有历史推送消息
     **/
    private void requestCleanData(final int type, String start_time, String end_time) {
        DeleteSingleMsgRequest request = new DeleteSingleMsgRequest();
        request.setStart_time(start_time);
        request.setEnd_time(end_time);
        OkHttpUtils.delete()
                .url(Api.WARN + BaseInfoSPUtil.getInstance().getDeviceId(this) +
                        "?"+"start_time="+start_time+"&end_time="+end_time)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .build()
                .execute(new GenericsCallback<GetWarnBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(MessageCenterActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetWarnBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            setTextHint();
                            showToast("删除成功");
                            if (type == 1) {
                                mTadayCurrentPage = -1;
                                requestToadyWarn();
                            } else if (type == 2) {
                                mSevenDayCurrentPage = -1;
                                requestSevenWarn();
                            }
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                        }
                    }
                });
    }

    /**
     * 删除指定设备的指定历史推送消息
     **/
    private void requestCleanData(final int type, int warn_id) {
        OkHttpUtils.delete()
                .url(Api.WARN + BaseInfoSPUtil.getInstance().getDeviceId(this) + "/" + warn_id)
                .addHeader("Authorization", "Bearer " + BaseInfoSPUtil.getInstance().getLoginToken(this))
                .build()
                .execute(new GenericsCallback<GetWarnBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(MessageCenterActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(GetWarnBean response, int id, int code) {

                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            setTextHint();
                            showToast("删除成功");
                            if (type == 3) {
                                mTadayCurrentPage = -1;
                                requestToadyWarn();
                            } else {
                                mSevenDayCurrentPage = -1;
                                requestSevenWarn();
                            }
                        } else if (mCode == 1001) {
                        } else if (mCode == 1002) {
                        }
                    }
                });
    }

    private void setTextHint() {
        mTrlToday.setVisibility(View.GONE);
        mTrlSevenToday.setVisibility(View.GONE);
        mTvTodayNoData.setVisibility(View.GONE);
        mTvSevenNoData.setVisibility(View.GONE);
    }

}