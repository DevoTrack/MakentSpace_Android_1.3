package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/guest
 * @category    CalendarListingdetailsAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.roomModels.RoomLengthOfStay;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class EarlyBirdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener{

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	private ArrayList <RoomLengthOfStay> modelItems;
	static LocalSharedPreferences localSharedPreferences;




	public EarlyBirdAdapter(Context context, ArrayList<RoomLengthOfStay> Items) {
		this.context = context;
		this.modelItems = Items;
		localSharedPreferences=new LocalSharedPreferences(context.getApplicationContext());
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.discount_list,parent,false));
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
			isLoading = true;
			loadMoreListener.onLoadMore();
		}

		if(getItemViewType(position)==TYPE_Explore){
			((MovieHolder)holder).bindData(modelItems.get(position),position);
		}
		//No else part needed as load holder doesn't bind any data
	}

	@Override
	public int getItemViewType(int position) {
		if(modelItems.get(position).getDiscount().equals("load")){
			return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

	@Override
	public void onSuccess(JsonResponse jsonResp, String data) {

	}

	@Override
	public void onFailure(JsonResponse jsonResp, String data) {

	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		RelativeLayout discount_background;
		TextView period,discount;
		public MovieHolder(View itemView) {
			super(itemView);
			period=(TextView) itemView.findViewById(R.id.period);
			discount=(TextView) itemView.findViewById(R.id.discount);
			discount_background=(RelativeLayout) itemView.findViewById(R.id.discount_background);
		}

		void bindData(final RoomLengthOfStay movieModel, int position){



			discount.setText(movieModel.getDiscount()+"%");


				if(movieModel.getPeriod().equals("1"))
					period.setText(movieModel.getPeriod()+" "+context.getResources().getString(R.string.day));
				else
					period.setText(movieModel.getPeriod()+" "+context.getResources().getString(R.string.days));


			discount_background.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
		}
	}

	static class LoadHolder extends RecyclerView.ViewHolder{
		public LoadHolder(View itemView) {
			super(itemView);
		}
	}

	public void setMoreDataAvailable(boolean moreDataAvailable) {
		isMoreDataAvailable = moreDataAvailable;
	}

	/* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
	public void notifyDataChanged(){
		notifyDataSetChanged();
		isLoading = false;
	}


	public interface OnLoadMoreListener{
		void onLoadMore();
	}

	public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

}