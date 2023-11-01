package com.makent.trioangle.createspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.ReadyHostModel.SelectedTimesItem;

import java.util.List;


public class AvailabilityTimeAdapter extends RecyclerView.Adapter<AvailabilityTimeAdapter.MyViewHolder> {

    private final AvailabilityTimeAdapter.OnItemClickListener onItemClickListener;
    public Context context;
    public String time;
    protected boolean isInternetAvailable;
    LocalSharedPreferences localSharedPreferences;
    String timeZone;
    private List<SelectedTimesItem> experienceModelLists;


    public AvailabilityTimeAdapter(List<SelectedTimesItem> experienceModelLists, String timeZone, Context context, AvailabilityTimeAdapter.OnItemClickListener onItemClickListener) {
        this.experienceModelLists = experienceModelLists;
        this.timeZone = timeZone;
        this.context = context;
        localSharedPreferences = new LocalSharedPreferences(context);
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public AvailabilityTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_time, parent, false);

        return new AvailabilityTimeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AvailabilityTimeAdapter.MyViewHolder holder, final int position) {

        final SelectedTimesItem experienceModelList = experienceModelLists.get(position);

        String startTime = experienceModelList.getStartTime();
        String endTime = experienceModelList.getEndTime();


        setTime(holder.tvStartTime, startTime, timeZone);
        setTime(holder.tvEndTime, endTime, timeZone);


        if (experienceModelLists.size() < 2)
            holder.ivRemove.setVisibility(View.GONE);
        else
            holder.ivRemove.setVisibility(View.VISIBLE);

        dropDownVisibility(holder.ivStartTime, experienceModelList.getStartTime());
        dropDownVisibility(holder.ivEndTime, experienceModelList.getEndTime());

        holder.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position, experienceModelList.getStartTime(), "start");
            }
        });

        holder.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position, experienceModelList.getEndTime(), "end");
            }
        });


        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onItemClickListener.onRemoveClicked(position);
            }
        });


    }

    private void setTime(TextView tvTime, String time, String timeZone) {

        if (time.equals("")) {
            tvTime.setText(time);
        } else {
            tvTime.setText(time );
        }

    }

    private void dropDownVisibility(ImageView ivTime, String time) {

        if (time.equals("")) {
            ivTime.setVisibility(View.VISIBLE);
        } else {
            ivTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return experienceModelLists.size();
    }


    public interface OnItemClickListener {
        void onRemoveClicked(int position);

        void onItemClick(int position, String selectedItem, String timeType);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView ivRemove;
        public ImageView ivStartTime;
        public ImageView ivEndTime;
        public TextView tvStartTime;
        public TextView tvEndTime;


        public MyViewHolder(View view) {
            super(view);


            ivRemove = view.findViewById(R.id.iv_remove);
            ivStartTime = view.findViewById(R.id.iv_start_time);
            ivEndTime = view.findViewById(R.id.iv_end_time);
            tvStartTime = view.findViewById(R.id.tv_start_time);
            tvEndTime = view.findViewById(R.id.tv_end_time);


        }
    }

}
