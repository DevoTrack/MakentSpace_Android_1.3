package com.makent.trioangle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.spacedetail.interfaces.OnItemClickListener;
import com.makent.trioangle.spacedetail.model.SpacesAccessServicesAndOthers;

import java.util.ArrayList;

public class SpaceStyleAdapter extends RecyclerView.Adapter<SpaceStyleAdapter.SpaceViewHolder> {

    private static Context context;
    private static ArrayList<SpacesAccessServicesAndOthers> space_model;
    private static boolean setMoreData = false;
    private static OnItemClickListener listener;
    public  SpaceStyleAdapter(Context context,ArrayList<SpacesAccessServicesAndOthers> space_model, OnItemClickListener listener){
        this.context = context;
        this.space_model = space_model;
        this.listener = listener;
        this.setMoreData = false;
    }



    @NonNull
    @Override
    public SpaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.listview_item,null,false);
        return new SpaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpaceViewHolder holder, int position) {
        SpacesAccessServicesAndOthers space_models = space_model.get(position);
        holder.bindData(space_models,position);

    }

    @Override
    public int getItemCount() {
        return space_model.size();
    }

    public void UpdateAdapter() {
        setMoreData = true;
        notifyDataSetChanged();
    }

    public class SpaceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_guest_access;

        public SpaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_guest_access = (TextView)itemView.findViewById(R.id.item_text);
        }

        public void bindData(SpacesAccessServicesAndOthers space_models, int position) {
            if(!setMoreData){
                if(position == 5){
                    tv_guest_access.setText("+More");
                    tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    tv_guest_access.setTextColor(context.getResources().getColor(R.color.red_text));
                }else if(position>5)
                {
                    // addMoreData.add(space_guestAccess_model);
                    tv_guest_access.setVisibility(View.GONE);
                }
                else
                {
                    tv_guest_access.setText(space_models.getName());
                }
                tv_guest_access.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==5) {
                            listener.onItemClick("SpaceStyle");
                        }
                    }
                });
                //  setMoreData=true;

            }
            else
            {
                tv_guest_access.setVisibility(View.VISIBLE);
                tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_empty,0,0,0);
                tv_guest_access.setText(space_models.getName());
                tv_guest_access.setTextColor(context.getResources().getColor(R.color.text_shadow));
                //  setMoreData=false;
            }
        }
    }

}
