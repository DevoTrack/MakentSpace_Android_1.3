package com.makent.trioangle.host.optionaldetails.amenities_nested_recycleview;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails/amenties_nested_recycleview
 * @category    AmenitiesChildAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makent.trioangle.R;

import java.util.ArrayList;

public class AmenitiesChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AmenitiesChild> childData;
    ViewHolder vh;

    public AmenitiesChildAdapter(ArrayList<AmenitiesChild> childData) {
        this.childData = childData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       // @BindView(R.id.tv_child)
      //  @BindView(R.id.amenities_check)
        public TextView tv_child;
        public CheckBox amenities_check;
        public LinearLayout tv_linear;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_child = (TextView) itemView.findViewById(R.id.tv_child);
            amenities_check = (CheckBox) itemView.findViewById(R.id.amenities_check);
            tv_linear=(LinearLayout)itemView.findViewById(R.id.tv_linear);
           // ButterKnife.bind(this, itemView.);
           // ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities_item_child, parent, false);

        AmenitiesChildAdapter.ViewHolder cavh = new AmenitiesChildAdapter.ViewHolder(itemLayoutView);

        return cavh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        vh = (ViewHolder) holder;

        AmenitiesChild c = childData.get(position);
        vh.tv_child.setText(c.getChild_name());

        //in some cases, it will prevent unwanted situations
        vh.amenities_check.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        vh.amenities_check.setChecked(false);

        vh.tv_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.amenities_check.setChecked(!vh.amenities_check.isChecked());
            }
        });
        
        vh.amenities_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                vh.amenities_check.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childData.size();
    }
}
