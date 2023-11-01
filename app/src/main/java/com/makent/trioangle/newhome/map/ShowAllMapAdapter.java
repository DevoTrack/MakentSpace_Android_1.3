package com.makent.trioangle.newhome.map;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.MapFooterFragment;
import com.makent.trioangle.newhome.models.Detail;

import java.util.ArrayList;

public class ShowAllMapAdapter extends FragmentPagerAdapter {
    LayoutInflater layoutInflater;
    ArrayList<Detail> detailArrayList;
    Context mContext;
    int currentposition;
    private static final int TYPE_FOOTER = 1;
    public final int TYPE_Explore = 2;

    public ShowAllMapAdapter(FragmentManager fm, ArrayList<Detail> detailArrayList, Context context, int cp) {
        super(fm);
        layoutInflater = LayoutInflater.from(context);
        this.detailArrayList = detailArrayList;
        this.mContext = context;
        this.currentposition=cp;
        System.out.println(" ******Adapter Room Seleted *******");
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println(" ******Adapter Room Seleted Position *******"+position);
        if (getItemViewType(position) == TYPE_FOOTER) {
            return new MapFooterFragment(this.mContext, currentposition, getCount());
        } else {
            return new ShowAllMapFragment(position, detailArrayList.get(position), this.mContext, currentposition, getCount());
        }
    }

    @Override
    public int getCount() {
        if(mContext.getResources().getString(R.string.layout_direction).equals("1")){
            return this.detailArrayList.size() + 1;
        }else{
            return this.detailArrayList.size() + 1;
        }

    }

    @Override
    public float getPageWidth(int position) {	return .50f; }


    public int getItemViewType(int position) {
        if (isPositionFooter(position)) return TYPE_FOOTER;
        else return TYPE_Explore;
    }

    //If position is last then type is footer
    private boolean isPositionFooter(int position) {
        if(mContext.getResources().getString(R.string.layout_direction).equals("1")) {
            return position == getCount() - 1;
        }
        else
        {
            return position == getCount() - 1;
        }
    }
}
