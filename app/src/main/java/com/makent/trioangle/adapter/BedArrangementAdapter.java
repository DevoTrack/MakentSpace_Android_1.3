package com.makent.trioangle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.BedtypeListActivity;
import com.makent.trioangle.model.EventTypeModel;
import com.makent.trioangle.model.homeModels.BedRoomBed;
import com.makent.trioangle.spacedetail.model.SpaceActivityArray;
import com.makent.trioangle.travelling.Room_details;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;


@SuppressLint("ViewHolder")
public class BedArrangementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final String TAG = null;
    public static LocalSharedPreferences localSharedPreferences;
    static Context context;
    private static Activity activity;
    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    ProgressBar explore_progress;
    private ArrayList<SpaceActivityArray> bedRoomModel;
    private static ArrayList<String> emptyArrayPositions = new ArrayList<>();

    public BedArrangementAdapter(Activity activity, Context context, ArrayList<SpaceActivityArray> bedRoomModel)
    {
        BedArrangementAdapter.context = context;
        BedArrangementAdapter.activity = activity;
        this.bedRoomModel = bedRoomModel;
        this.emptyArrayPositions = emptyArrayPositions;
        localSharedPreferences = new LocalSharedPreferences(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View rootView = LayoutInflater.from(context).inflate(R.layout.pricing_events_type_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(lp);
        rootView.getLayoutDirection();
        return new BedTypeHolder(rootView);

        /*LayoutInflater inflater = LayoutInflater.from(context);
        return new BedTypeHolder(inflater.inflate(R.layout.sleeping_arrangement_layout, parent, false));*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((BedTypeHolder) holder).bindData(bedRoomModel.get(position));
    }

    @Override
    public int getItemViewType(int position)
    {
        return TYPE_Explore;
    }

    @Override
    public int getItemCount()
    {
        return bedRoomModel.size();
    }

    /* VIEW HOLDERS */

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged()
    {
        notifyDataSetChanged();
    }

    static class BedTypeHolder extends RecyclerView.ViewHolder
    {
        ImageView ivEventType;
        TextView tvHourRate, tvFulldayRate, tvMinBookingHours, tv_eventsTitle;

        public BedTypeHolder(View itemView)
        {
            super(itemView);
            ivEventType = (ImageView) itemView.findViewById(R.id.events_image);
            tvHourRate = (TextView) itemView.findViewById(R.id.hour_rate);
            tvFulldayRate = (TextView) itemView.findViewById(R.id.fulldayrate);
            tvMinBookingHours = (TextView) itemView.findViewById(R.id.min_booking_hours);
            tv_eventsTitle = (TextView) itemView.findViewById(R.id.tv_eventsTitle);
        }

        void bindData(final SpaceActivityArray bedTypeModel)
        {
            //   System.out.println("Bed type model : two "+bedTypeModel.getName());
            //   tvBedType.setText(bedTypeModel.getCount()+" "+bedTypeModel.getName());

            tv_eventsTitle.setText(bedTypeModel.getName());
            if (bedTypeModel.getPriceFullDay().isEmpty() || bedTypeModel.getPriceFullDay().equals("")||bedTypeModel.getPriceFullDay().equals("0"))
            {
                tvFulldayRate.setVisibility(View.GONE);
            }
            else
            {
                tvFulldayRate.setText(context.getResources().getString(R.string.Full_day_rate)+localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol)+bedTypeModel.getPriceFullDay());
                tvFulldayRate.setVisibility(View.VISIBLE);
            }
         //  String fulldayrate= spannabletextview(bedTypeModel.getEvents_Fullday_Rate());
            //tvFulldayRate.setText(context.getResources().getString(R.string.Full_day_rate)+localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol)+bedTypeModel.getEvents_Fullday_Rate());
            if (bedTypeModel.getPriceHourly().isEmpty() || bedTypeModel.getPriceHourly().equals(""))
            {
                tvHourRate.setVisibility(View.GONE);
            }
            else
            {
                tvHourRate.setText(context.getResources().getString(R.string.hourly_rate)+localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol)+ bedTypeModel.getPriceHourly());
                tvHourRate.setVisibility(View.VISIBLE);
            }
           // tvHourRate.setText(context.getResources().getString(R.string.hourly_rate)+localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol)+ bedTypeModel.getEvents_hours_Rate());
            if (bedTypeModel.getPriceMinHours().isEmpty() || bedTypeModel.getPriceMinHours().equals(""))
            {
                tvMinBookingHours.setVisibility(View.GONE);
            }
            else
            {
                tvMinBookingHours.setText(context.getResources().getString(R.string.Minimum_booking_hour)+ bedTypeModel.getPriceMinHours());
                tvMinBookingHours.setVisibility(View.VISIBLE);
            }
            //tvMinBookingHours.setText(context.getResources().getString(R.string.Minimum_booking_hour)+ bedTypeModel.getMin_Booking_hours());
         //   ivEventType.setImageDrawable(bedTypeModel.getEventTypeImage());

            Glide.with(context.getApplicationContext())
                    .load(bedTypeModel.getImageurl())

                    .into(new DrawableImageViewTarget(ivEventType)
                    {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition)
                        {
                            super.onResourceReady(resource, transition);
                        }
                    });
          /*  DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(reservation_dot_loader);
            Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1);
*/
        }
    }

}