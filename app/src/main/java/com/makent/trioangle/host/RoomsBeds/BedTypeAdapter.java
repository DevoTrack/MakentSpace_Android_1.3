package com.makent.trioangle.host.RoomsBeds;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.helper.FontIconDrawable;

import java.util.ArrayList;
import java.util.List;


public class BedTypeAdapter extends RecyclerView.Adapter<BedTypeAdapter.ViewHolder> {
    private List<BedTypesList> bedTypesLists;
    Context context;
    Typeface font1;
    Drawable minusenable,minusdisable,plusenable,plusdisable;
    int count=0;
    private final OnBedClickListener onBedClickListener;

    public BedTypeAdapter(List<BedTypesList> bedTypesLists, Context context, OnBedClickListener onBedClickListener) {
        this.bedTypesLists = new ArrayList<>();
        this.context = context;
        this.onBedClickListener = onBedClickListener;
        init();
    }



    @Override
    public BedTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bedroom_child, viewGroup, false);
        init();
        return new BedTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BedTypeAdapter.ViewHolder viewHolder, int i) {



        BedTypesList bedTypesList = bedTypesLists.get(i);

        enablebuttons(viewHolder,bedTypesList.getCount());

        System.out.println("Get Item count : two "+String.valueOf(getItemCount()));

        viewHolder.bedtext.setText(bedTypesList.getName());
        viewHolder.tvBedCount.setText(String.valueOf(bedTypesList.getCount()));

        viewHolder.iv_bedplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = bedTypesList.getCount();
                ++count;
                /*viewHolder.tvBedCount.setText(String.valueOf(++count));

                enablebuttons(viewHolder,Integer.parseInt(bedTypesList.getCount()));*/
                onBedClickListener.onBedClick(i,count);
            }
        });

        viewHolder.iv_bedminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = bedTypesList.getCount();
                --count;
                /*viewHolder.tvBedCount.setText(String.valueOf(--count));

                enablebuttons(viewHolder,Integer.parseInt(bedTypesList.getCount()));*/

                onBedClickListener.onBedClick(i,count);
            }
        });



    }


    public void updateList(List<BedTypesList> bedTypesLists) {
        this.bedTypesLists = bedTypesLists;
        //System.out.println("Get Item count : One "+String.valueOf(getItemCount()));
        notifyDataSetChanged();
    }

    private void enablebuttons(BedTypeAdapter.ViewHolder viewHolder,int count) {

        if(count==0){
            viewHolder.iv_bedplus.setEnabled(true);
            viewHolder.iv_bedminus.setEnabled(false);
        }else if(count==5){
            viewHolder.iv_bedplus.setEnabled(false);
            viewHolder.iv_bedminus.setEnabled(true);
        }else{
            viewHolder.iv_bedplus.setEnabled(true);
            viewHolder.iv_bedminus.setEnabled(true);
        }


        plusMinus(viewHolder.iv_bedminus,viewHolder.iv_bedplus);

    }

   
    

    public void plusMinus(ImageView minus, ImageView plus)
    {
        if(minus.isEnabled()) {
            minus.setBackground(minusenable);
        }else
        {
            minus.setBackground(minusdisable);
        }

        if(plus.isEnabled()) {
            plus.setBackground(plusenable);
        }else
        {
            plus.setBackground(plusdisable);
        }
    }


    private void init() {


        font1 = Typeface.createFromAsset( context.getAssets(), context.getResources().getString(R.string.fonts_makent4));
        minusenable = new FontIconDrawable(context,context.getResources().getString(R.string.f4checkminus),font1)
                .sizeDp(30).colorRes(R.color.white);
        plusenable = new FontIconDrawable(context,context.getResources().getString(R.string.f4checkplus),font1)
                .sizeDp(30).colorRes(R.color.white);
        minusdisable = new FontIconDrawable(context,context.getResources().getString(R.string.f4checkminus),font1)
                .sizeDp(30)
                .colorRes(R.color.line_gray);
        plusdisable = new FontIconDrawable(context,context.getResources().getString(R.string.f4checkplus),font1)
                .sizeDp(30)
                .colorRes(R.color.line_gray);


    }



    public interface OnBedClickListener {
        void onBedClick(int position, int count);
    }


    @Override
    public int getItemCount() {
        return bedTypesLists.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_bedplus,iv_bedminus;
        public TextView bedtext, tvBedCount;
        public ViewHolder(View view) {
            super(view);
            bedtext = (TextView) view.findViewById(R.id.bedtext);
            tvBedCount = (TextView) view.findViewById(R.id.tv_bed_count);
            iv_bedplus = (ImageView) view.findViewById(R.id.iv_bedplus);
            iv_bedminus = (ImageView) view.findViewById(R.id.iv_bedminus);
        }
    }


}
