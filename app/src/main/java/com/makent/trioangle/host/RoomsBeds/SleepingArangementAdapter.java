package com.makent.trioangle.host.RoomsBeds;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makent.trioangle.R;

import java.util.List;


public class SleepingArangementAdapter extends RecyclerView.Adapter<SleepingArangementAdapter.ViewHolder> {
    private final OnItemClickListener onItemClickListener;
    private final Activity activity;
    Context context;
    private List<AdditionalBedModel> additionalBedModels;
    private int bedRoomCount;

    public SleepingArangementAdapter(List<AdditionalBedModel> additionalBedModels, Context context, OnItemClickListener onItemClickListener, Activity activity) {
        this.additionalBedModels = additionalBedModels;
        this.context = context;
        this.activity=activity;
        this.onItemClickListener = onItemClickListener;


    }


    @Override
    public SleepingArangementAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bedroom_parent, viewGroup, false);
        return new SleepingArangementAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SleepingArangementAdapter.ViewHolder viewHolder, int i) {
        // viewHolder.setIsRecyclable(false);

        AdditionalBedModel additionalBedModel = additionalBedModels.get(i);
        System.out.println("bedmodel"+i);
        bedRoomCount =i+1;


        if (i == getItemCount() - 1)
            viewHolder.tvBedroomCount.setText(context.getResources().getString(R.string.common_space));
        else
            viewHolder.tvBedroomCount.setText(context.getResources().getString(R.string.bedroom_count) + bedRoomCount);


        viewHolder.tvBedsCount.setText(totalCount(additionalBedModel.getBedTypesLists())+" "+context.getResources().getString(R.string.beds_count));

        viewHolder.tvBedTypeCount.setText(totalBeds(additionalBedModel.getBedTypesLists()));

        buttonStatus(additionalBedModel.getBedTypesLists(), viewHolder.editButton);



        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i, viewHolder.itemView);
            }
        });


    }

    private String totalBeds(List<BedTypesList> bedTypesLists) {
        String totalBeds="";

        for(int i=0;i<bedTypesLists.size();i++){

            if(bedTypesLists.get(i).getCount()>0){
                totalBeds = totalBeds+bedTypesLists.get(i).getCount()+" "+bedTypesLists.get(i).getName()+",";
            }

        }

        if(totalBeds.endsWith(","))
             totalBeds = totalBeds.substring(0, totalBeds.length() - 1);

        return totalBeds;
    }

    private String totalCount(List<BedTypesList> bedTypesLists) {
        int totalCount = 0;
        for(int i =0;i<bedTypesLists.size();i++){
            totalCount = totalCount+bedTypesLists.get(i).getCount();
        }
        return String.valueOf(totalCount);
    }

    private void buttonStatus(List<BedTypesList> bedTypesLists, Button editButton) {
        for (int i=0;i<bedTypesLists.size();i++){
            if(bedTypesLists.get(i).getCount()>0){
                editButton.setText(context.getResources().getString(R.string.edit_bed));
                break;
            }{
                editButton.setText(context.getResources().getString(R.string.add_beds));
            }
        }
    }

   

    @Override
    public int getItemCount() {
        return additionalBedModels.size();
    }

    public void updateList(List<AdditionalBedModel> additionalBedModels) {
        this.additionalBedModels = additionalBedModels;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(int position, View view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBedroomCount, tvBedsCount, tvBedTypeCount;
        private Button editButton;

        public ViewHolder(View view) {
            super(view);
            tvBedroomCount = view.findViewById(R.id.tv_bedroom);
            tvBedsCount = view.findViewById(R.id.tv_bed);
            tvBedTypeCount = view.findViewById(R.id.tv_bed_types);

            editButton = view.findViewById(R.id.button_beds);
        }
    }
}
