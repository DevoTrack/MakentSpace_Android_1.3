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
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.util.ArrayList;


@SuppressLint("ViewHolder")
public class BedtypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final String TAG = null;

    static Context context;
    private static Activity activity;
    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    ProgressBar explore_progress;
    private ArrayList<BedRoomBed> bedTypeList = new ArrayList<>();


    public BedtypeAdapter(Activity activity, Context context, ArrayList<BedRoomBed> bedTypeList) {
        this.context = context;
        this.activity = activity;
        this.bedTypeList = bedTypeList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new BedTypeHolder(inflater.inflate(R.layout.bedtype_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((BedTypeHolder) holder).bindData(bedTypeList.get(position));

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_Explore;
    }

    @Override
    public int getItemCount() {
        return bedTypeList.size();
    }

    /* VIEW HOLDERS */

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();

    }

    static class BedTypeHolder extends RecyclerView.ViewHolder {

        ImageView ivBedType;
        TextView tvBedCount;

        private BedsTypeAdapter bedsTypeAdapter;

        public BedTypeHolder(View itemView) {
            super(itemView);
            tvBedCount =(TextView)itemView.findViewById(R.id.tv_bed_count);
            ivBedType =(ImageView)itemView.findViewById(R.id.iv_bed_type);



        }

        void bindData(final BedRoomBed bedRoomBed) {

            tvBedCount.setText(bedRoomBed.getCount()+" "+bedRoomBed.getName());

            Glide.with(context.getApplicationContext())
                    .load(bedRoomBed.getIcon())

                    .into(new DrawableImageViewTarget(ivBedType) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });
        }
    }


}