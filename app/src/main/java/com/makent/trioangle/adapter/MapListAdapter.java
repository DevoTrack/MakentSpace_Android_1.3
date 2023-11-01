package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    MapListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeListModel;
import com.makent.trioangle.travelling.MapFragment;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class MapListAdapter extends FragmentPagerAdapter {
	LayoutInflater layoutInflater;
	ArrayList<HomeListModel> deals;
	Context mContext;
	int currentposition;

	private static final int TYPE_FOOTER = 1;
	public final int TYPE_Explore = 2;

	public MapListAdapter(FragmentManager fm, ArrayList<HomeListModel> deals, Context context, int cp)
	{
		super(fm);
		layoutInflater = LayoutInflater.from(context);
		this.deals = deals;
		this.mContext = context;
		this.currentposition=cp;
		System.out.println(" ******Adapter Room Seleted*******");
	}

	@Override
	public Fragment getItem(int position) {
		System.out.println(" ******Adapter Room Seleted Position *******"+position);
		if (getItemViewType(position) == TYPE_FOOTER) {
			return new MapFooterFragment(this.mContext, currentposition, getCount());
		} else {
			return new MapFragment(deals, position, deals.get(position), this.mContext, currentposition, getCount());
		}
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return this.deals.size() + 1;
	}

	@Override
	public float getPageWidth(int position) {	return .50f; }

	public int getItemViewType(int position) {
		if (isPositionFooter(position)) return TYPE_FOOTER;
		else return TYPE_Explore;
	}

	//If position is last then type is footer
	private boolean isPositionFooter(int position) {
		return position == getCount() - 1;
	}

}