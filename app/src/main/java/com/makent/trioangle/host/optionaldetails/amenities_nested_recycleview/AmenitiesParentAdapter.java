package com.makent.trioangle.host.optionaldetails.amenities_nested_recycleview;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails/amenties_nested_recycleview
 * @category    AmenitiesParentAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.makent.trioangle.R;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AmenitiesParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    ArrayList<Parent> parentData;
//    ArrayList<AmenitiesChild> childData;
    ArrayList<AmenitiesParentChild> parentChildData;

    CheckBox amenities_check;

    public static Context ctx;
    ViewHolder vh;

    public AmenitiesParentAdapter(Context ctx, ArrayList<AmenitiesParentChild> parentChildData) {
        this.ctx = ctx;
//        this.parentData = parentData;
//        this.childData= childData;
        this.parentChildData = parentChildData;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_header)
        TextView tv_header;
        @BindView(R.id.rv_child)
        RecyclerView rv_child;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities_item_parent, parent, false);

        AmenitiesParentAdapter.ViewHolder pavh = new AmenitiesParentAdapter.ViewHolder(itemLayoutView);
        return pavh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        vh = (ViewHolder) holder;


        AmenitiesParentChild p = parentChildData.get(position);
        if(p.getHeader().toString().equals(""))
        {
            vh.tv_header.setVisibility(View.GONE);
        }
        else {
            vh.tv_header.setText(p.getHeader());
        }

        initChildLayoutManager(vh.rv_child, p.getChild());

        /*vh.rv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amenities_check= (CheckBox)vh.rv_child.findViewById(R.id.amenities_check);
                amenities_check.setChecked(true);
                Toast.makeText(ctx,"Item Clicked",Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, String.format("Clicked on position #%s of Section %s",vh.rv_child.getChildAdapterPosition(amenities_check)), Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    private void initChildLayoutManager(final RecyclerView rv_child, ArrayList<AmenitiesChild> childData) {
        LinearLayoutManager manager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_child.setLayoutManager(manager);
        rv_child.setHasFixedSize(true);
        //rv_child.addItemDecoration(new SimpleDividerItemDecoration(ctx));


        rv_child.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            //    Toast.makeText(ctx,"Item Touch",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        AmenitiesChildAdapter childAdapter = new AmenitiesChildAdapter(childData);
        rv_child.setAdapter(childAdapter);

    }



    @Override
    public int getItemCount() {
        return parentChildData.size();
    }
}
