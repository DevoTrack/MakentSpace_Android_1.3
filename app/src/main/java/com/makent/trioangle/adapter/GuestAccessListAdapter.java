package com.makent.trioangle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.model.Space_guestAccess_Model;
import com.makent.trioangle.spacedetail.interfaces.OnItemClickListener;
import com.makent.trioangle.spacedetail.model.SpacesAccessServicesAndOthers;

import java.util.ArrayList;

public class GuestAccessListAdapter extends RecyclerView.Adapter<GuestAccessListAdapter.GuestAccessHolder> {

    private static Context context;
    private static ArrayList<SpacesAccessServicesAndOthers> guestAccess_Model;
    private static OnItemClickListener listener;

    static boolean setMoreData=false;

    public GuestAccessListAdapter(Context context, ArrayList<SpacesAccessServicesAndOthers> guestAccess_Model, OnItemClickListener listener) {
        this.context = context;
        this.guestAccess_Model = guestAccess_Model;
        this.listener = listener;
        this.setMoreData=false;
    }

    @Override
    public  GuestAccessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.listview_item, null, false);
        return new GuestAccessHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GuestAccessHolder holder, int position) {
        SpacesAccessServicesAndOthers space_guestAccess_model=guestAccess_Model.get(position);

        holder.bindData(space_guestAccess_model,position);

       // holder.bind(space_guestAccess_model,position);
       /* if(position>=5)
        {
           holder.tv_guest_access.setText("+More");
            holder.tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            holder.tv_guest_access.setTextColor(context.getResources().getColor(R.color.red_text));
            if(position>5) {
                holder.tv_guest_access.setVisibility(View.GONE);
                holder.tv_guest_access.setText(space_guestAccess_model.getName());
                holder.tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_empty, 0, 0, 0);
            }
            holder.tv_guest_access.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_guest_access.setVisibility(View.VISIBLE);
                }
            });

        }
        else
        {
            holder.tv_guest_access.setText(space_guestAccess_model.getName());
        }
*/
    }

    @Override
    public int getItemCount() {
        return guestAccess_Model.size();
    }

    public void UpdateAdapter() {

      setMoreData=true;
      notifyDataSetChanged();
    }

    public void addItem(SpacesAccessServicesAndOthers item)
    {
        if(setMoreData)
        {
            guestAccess_Model.add(item);
        }
    }

    static class GuestAccessHolder extends RecyclerView.ViewHolder {
        ImageView ivEventType;
        TextView tv_guest_access;

        public GuestAccessHolder(View itemView) {
            super(itemView);
            tv_guest_access = (TextView) itemView.findViewById(R.id.item_text);
        }

        public void bindData(SpacesAccessServicesAndOthers space_guestAccess_model, int position) {
       //  ArrayList<Space_guestAccess_Model> addMoreData=new ArrayList<>();
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
                    tv_guest_access.setText(space_guestAccess_model.getName());
                }
                tv_guest_access.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position==5) {
                            listener.onItemClick("GuestAccess");
                        }
                    }
                });
              //  setMoreData=true;

            }
            else
            {
                tv_guest_access.setVisibility(View.VISIBLE);
                tv_guest_access.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_empty,0,0,0);
                tv_guest_access.setText(space_guestAccess_model.getName());
                tv_guest_access.setTextColor(context.getResources().getColor(R.color.text_shadow));
              //  setMoreData=false;
            }
        }
       /* public void bind(Space_guestAccess_Model space_guestAccess_model, int position) {


        }*/
    }

}

