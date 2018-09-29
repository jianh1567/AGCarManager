package com.wind.carmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.model.HistoryTrackInfo;
import com.wind.carmanager.utils.TimeUtil;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by houjian on 2018/7/4.
 */

public class HistoryTrackAdapter extends  RecyclerView.Adapter<HistoryTrackAdapter.ViewHolder>{
    private static final String TAG = "HistoryTrackAdapter";
    private List<HashMap<String, Object>> mHistoryTrackList;
    private OnItemClickListener mOnItemClickListener;

    public HistoryTrackAdapter(List<HashMap<String, Object>> historyTrackInfos){
        mHistoryTrackList = historyTrackInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_track,parent,false);
        SupportMultipleScreensUtil.scale(view);
        HistoryTrackAdapter.ViewHolder viewHolder = new HistoryTrackAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HashMap<String, Object> historyTrackInfo = mHistoryTrackList.get(position);
        Log.i(TAG, "historyTrackInfo startTime = " +  historyTrackInfo.get("startTime"));
        long date = TimeUtil.timeToStamp((String) historyTrackInfo.get("startTime"));
        holder.trackDate.setText(TimeUtil.stampToChineseDate(date));
        holder.startTime.setText((String) historyTrackInfo.get("startTime"));
        holder.endTime.setText((String) historyTrackInfo.get("endTime"));
        holder.startAddress.setText((String) historyTrackInfo.get("startAddress"));
        holder.endAddress.setText((String) historyTrackInfo.get("endAddress"));
        holder.singleDistance.setText(String.valueOf((float) historyTrackInfo.get("singleDistance")));
        holder.singleTime.setText(String.valueOf((int) historyTrackInfo.get("singleTime")));
        holder.averageSpeed.setText((String)historyTrackInfo.get("averageSpeed"));

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mHistoryTrackList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView trackDate;
        TextView startTime;
        TextView endTime;
        TextView startAddress;
        TextView endAddress;
        TextView singleDistance;
        TextView singleTime;
        TextView averageSpeed;

        public ViewHolder(View view){
            super(view);
            trackDate =  view.findViewById(R.id.date);
            startTime =  view.findViewById(R.id.start_time);
            endTime =  view.findViewById(R.id.end_time);
            startAddress = view.findViewById(R.id.start_address);
            endAddress = view.findViewById(R.id.end_address);
            singleDistance = view.findViewById(R.id.single_distance);
            singleTime = view.findViewById(R.id.single_time);
            averageSpeed = view.findViewById(R.id.average_speed);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(HistoryTrackAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
