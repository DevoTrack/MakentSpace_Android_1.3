package com.makent.trioangle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.spacedetail.interfaces.OnItemClickListener;
import com.makent.trioangle.spacedetail.model.SpacesAccessServicesAndOthers;


import java.util.ArrayList;

public class ServiceOfferAdapter extends RecyclerView.Adapter<ServiceOfferAdapter.ServiceHolder> {

    private static Context context;
    private static ArrayList<SpacesAccessServicesAndOthers> service_offer_models;
    private static OnItemClickListener listener;
    static boolean setMoreData=false;


    public ServiceOfferAdapter(Context context, ArrayList<SpacesAccessServicesAndOthers> service_offer_models, OnItemClickListener listener) {
        this.context = context;
        this.service_offer_models = service_offer_models;
        this.listener = listener;
        this.setMoreData = false;

    }

    @NonNull
    @Override
    public ServiceOfferAdapter.ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.listview_item, null, false);
        return new ServiceHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceOfferAdapter.ServiceHolder holder, int position) {

        SpacesAccessServicesAndOthers service_offer_model=service_offer_models.get(position);

        holder.bindData(service_offer_model,position);

    }


    static class ServiceHolder extends RecyclerView.ViewHolder {
        ImageView ivEventType;
        TextView tv_guest_access;

        public ServiceHolder(View itemView) {
            super(itemView);
            tv_guest_access = (TextView) itemView.findViewById(R.id.item_text);
        }

        public void bindData(SpacesAccessServicesAndOthers service_offer_model, int position) {

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
                    tv_guest_access.setText(service_offer_model.getName());
                }
                tv_guest_access.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==5) {
                            listener.onItemClick("ServiceOffered");
                        }
                    }
                });
            }
            else
            {
                tv_guest_access.setVisibility(View.VISIBLE);
                tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_empty,0,0,0);
                tv_guest_access.setText(service_offer_model.getName());
                tv_guest_access.setTextColor(context.getResources().getColor(R.color.text_shadow));
                //  setMoreData=false;
            }
        }

    }

    @Override
    public int getItemCount() {
        return service_offer_models.size();
    }


    public void UpdateAdapter() {

        setMoreData=true;
        notifyDataSetChanged();
    }

    public void addItem(SpacesAccessServicesAndOthers item)
    {
        if(setMoreData)
        {
            service_offer_models.add(item);
        }
    }

}
