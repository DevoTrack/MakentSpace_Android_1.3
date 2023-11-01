package com.makent.trioangle.newhome.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.newhome.models.ActivitiesList;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    Context context;
    private ArrayList<ActivitiesList> activitiesLists = new ArrayList<ActivitiesList>();
    private final ActvitityClickListener onActivityClickListener;


    public ActivityAdapter(ArrayList<ActivitiesList> activitiesLists, Context context, ActvitityClickListener onActivityClickListener) {
        this.activitiesLists = activitiesLists;
        this.context = context;
        this.onActivityClickListener = onActivityClickListener;
    }


    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        ActivitiesList activitiesList = activitiesLists.get(position);
        holder.tvActivityTitle.setText(activitiesList.getName());
        Glide.with(context.getApplicationContext())
                .load(activitiesList.getImage_url())

                .into(new DrawableImageViewTarget(holder.ivActivity) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityClickListener.onActivityClick(activitiesList.getName(),activitiesList.getId());
            }
        });
    }

    public void clearExplore(){
        activitiesLists.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return activitiesLists.size();
    }

    public void updateList(ArrayList<ActivitiesList> activitiesLists) {
        this.activitiesLists = activitiesLists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvActivityTitle;
        private ImageView ivActivity;

        public ViewHolder(View view) {
            super(view);
            tvActivityTitle = (TextView) view.findViewById(R.id.tvActivityTitle);
            ivActivity = (ImageView) view.findViewById(R.id.ivActivity);
        }
    }

    public interface ActvitityClickListener{
        void onActivityClick(String name, String id);
    }

}
