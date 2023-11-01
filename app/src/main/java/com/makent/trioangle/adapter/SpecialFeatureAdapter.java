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

public class SpecialFeatureAdapter extends RecyclerView.Adapter<SpecialFeatureAdapter.SpecialViewHolder> {

    private static Context context;
    private static boolean setMoreData = false;
    private static ArrayList<SpacesAccessServicesAndOthers>specialFeature_models;
    private static OnItemClickListener listener;

    public SpecialFeatureAdapter(Context context, ArrayList<SpacesAccessServicesAndOthers> specialFeature_models, OnItemClickListener listener){
        this.context = context;
        this.specialFeature_models = specialFeature_models;
        this.listener = listener;
        this.setMoreData = false;

    }

    @NonNull
    @Override
    public SpecialFeatureAdapter.SpecialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item,null,false);
        return new SpecialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialFeatureAdapter.SpecialViewHolder holder, int position) {
        SpacesAccessServicesAndOthers specialFeature_model = specialFeature_models.get(position);
        holder.bindData(specialFeature_model,position);

    }

    @Override
    public int getItemCount() {
        return specialFeature_models.size();
    }

    public void UpdateAdapter() {
        setMoreData = true;
        notifyDataSetChanged();
    }

    public class SpecialViewHolder extends RecyclerView.ViewHolder {

        TextView tv_guest_access;


        public SpecialViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_guest_access = (TextView) itemView.findViewById(R.id.item_text);
        }

        public void bindData(SpacesAccessServicesAndOthers specialFeature_model, int position) {
            if(!setMoreData)
            {
                if(position==5)
                {
                    // addMoreData.add(space_guestAccess_model);
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
                    tv_guest_access.setText(specialFeature_model.getName());
                }
                tv_guest_access.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==5) {
                            listener.onItemClick("SpecialFeatures");
                        }
                    }
                });
                //  setMoreData=true;

            }
            else
            {
                tv_guest_access.setVisibility(View.VISIBLE);
                tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_empty,0,0,0);
                tv_guest_access.setText(specialFeature_model.getName());
                tv_guest_access.setTextColor(context.getResources().getColor(R.color.text_shadow));
                //  setMoreData=false;
            }


        }
    }

    public interface onFeatureClickListener{
        void onFeatureClick(String itemName);

    }
}
