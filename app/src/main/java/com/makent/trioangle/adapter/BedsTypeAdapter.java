package com.makent.trioangle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.model.EventTypeModel;
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class BedsTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;

    static Context context;


    protected static final String TAG = null;
    private static Activity activity;
    private ArrayList<EventTypeModel> bedRoomModel;

    public static LocalSharedPreferences localSharedPreferences;

    ProgressBar explore_progress;



    public BedsTypeAdapter(Activity activity,Context context, ArrayList<EventTypeModel> bedRoomModel) {
        this.context = context;
        this.activity=activity;
        this.bedRoomModel = bedRoomModel;
        localSharedPreferences=new LocalSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BedTypeHolder(inflater.inflate(R.layout.events_type_details,parent,false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((BedTypeHolder)holder).bindData(bedRoomModel.get(position),getItemCount());

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_Explore;
    }


    @Override
    public int getItemCount() {

      /*  if(bedRoomModel.size()>=4)
            return 4;
        else*/
            return bedRoomModel.size();


    }

    /* VIEW HOLDERS */

    static class BedTypeHolder extends RecyclerView.ViewHolder{
         ImageView ivEventType;
         TextView tvHourRate,tvFulldayRate,tvMinBookingHours,tv_eventsTitle;

        public BedTypeHolder(View itemView) {
            super(itemView);
            ivEventType =(ImageView)itemView.findViewById(R.id.events_image);
            tvHourRate =(TextView)itemView.findViewById(R.id.hour_rate);
            tvFulldayRate =(TextView)itemView.findViewById(R.id.fulldayrate);
            tvMinBookingHours =(TextView)itemView.findViewById(R.id.min_booking_hours);
            tv_eventsTitle =(TextView)itemView.findViewById(R.id.tv_eventsTitle);

        }

        void bindData(final EventTypeModel bedTypeModel,int itemCount){

         //   System.out.println("Bed type model : two "+bedTypeModel.getName());
         //   tvBedType.setText(bedTypeModel.getCount()+" "+bedTypeModel.getName());

            tv_eventsTitle.setText(bedTypeModel.getEventTypeTitle());
            if(bedTypeModel.getEvents_Fullday_Rate().isEmpty() || bedTypeModel.getEvents_Fullday_Rate().equals(""))
            {
              tvFulldayRate.setVisibility(View.INVISIBLE);
            }
            tvFulldayRate.setVisibility(View.VISIBLE);
            tvFulldayRate.setText(bedTypeModel.getEvents_Fullday_Rate());


            if(bedTypeModel.getEvents_hours_Rate().isEmpty() || bedTypeModel.getEvents_hours_Rate().equals(""))
            {
                tvHourRate.setVisibility(View.INVISIBLE);
            }
            tvHourRate.setVisibility(View.VISIBLE);
            tvHourRate.setText(bedTypeModel.getEvents_hours_Rate());
            if(bedTypeModel.getMin_Booking_hours().isEmpty() || bedTypeModel.getMin_Booking_hours().equals(""))
            {
                tvMinBookingHours.setVisibility(View.INVISIBLE);
            }
            tvMinBookingHours.setVisibility(View.VISIBLE);
            tvMinBookingHours.setText(bedTypeModel.getMin_Booking_hours());


            Glide.with(context.getApplicationContext())
                    .load(bedTypeModel.getEventTypeImage())

                    .into(new DrawableImageViewTarget(ivEventType) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });


        }
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();

    }






}