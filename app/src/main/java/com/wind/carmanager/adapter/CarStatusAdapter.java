package com.wind.carmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wind.carmanager.R;
import com.wind.carmanager.model.CarStatusInfo;
import com.wind.carmanager.widget.SupportMultipleScreensUtil;

import java.util.List;

/**
 * Created by houjian on 2018/7/3.
 */

public class CarStatusAdapter extends RecyclerView.Adapter<CarStatusAdapter.ViewHolder>{
    private List<CarStatusInfo> mCarStatusList;
    private OnItemClickListener mOnItemClickListener;

    public CarStatusAdapter(List<CarStatusInfo> carStatusList){
        this.mCarStatusList = carStatusList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_status_info,parent,false);
        SupportMultipleScreensUtil.scale(view);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CarStatusInfo carStatusInfo = mCarStatusList.get(position);
        holder.carStatusImage.setImageResource(carStatusInfo.getImageviewId());
        holder.carStatusTitle.setText(carStatusInfo.getTitle());
        holder.carStatusDescb.setText(carStatusInfo.getDescbId());

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
        return mCarStatusList.size();
    }

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView carStatusImage;
        TextView carStatusTitle;
        TextView carStatusDescb;

        public ViewHolder(View view){
            super(view);
            carStatusImage = (ImageView) view.findViewById(R.id.car_status_image);
            carStatusTitle = (TextView) view.findViewById(R.id.car_status_title);
            carStatusDescb = (TextView) view.findViewById(R.id.car_status_describe);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
