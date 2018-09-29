package com.wind.carmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.model.GetWarnBean;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者: Created by luow on 2018/7/3.
 * 注释：最近七天消息adapter
 */
public class SevenDayMessageAdapter extends RecyclerView.Adapter<SevenDayMessageAdapter.ViewHolder> {

    private Context mContext;
    private List<GetWarnBean.ResultBean.WarnBean> mData;
    public SevenDayMessageAdapter(Context context, List<GetWarnBean.ResultBean.WarnBean> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_today_message, parent, false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mTvData.setText(mData.get(position).getTime());
        holder.mTvDesc.setText(mData.get(position).getWarn_content());
        holder.mTvTime.setText(mData.get(position).getTime());
        holder.mTvTitle.setText(mData.get(position).getWarn_title());
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSevenDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_data)
        TextView mTvData;
        @BindView(R.id.rl_data)
        RelativeLayout mRlData;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_delete)
        ImageView mIvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnAdapterSevenDeleteClickListener mListener;

    public void setAdapterSevenDeleteOnClickListener(OnAdapterSevenDeleteClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnAdapterSevenDeleteClickListener {
        void onSevenDeleteClick(int position);
    }
}