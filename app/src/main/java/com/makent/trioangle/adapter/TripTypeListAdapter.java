package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    TripTypeListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.tripsModel.BookingTypeModel;
import com.makent.trioangle.model.tripsModel.TripType;
import com.makent.trioangle.travelling.HomeActivity;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class TripTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	static Context context;
	static LocalSharedPreferences localSharedPreferences;

	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;

	private ArrayList<BookingTypeModel> modelItems;

    ProgressBar explore_progress;


	public TripTypeListAdapter(Context context, ArrayList<BookingTypeModel> Items) {
		this.context = context;
		this.modelItems = Items;
		localSharedPreferences=new LocalSharedPreferences(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return new MovieHolder(inflater.inflate(R.layout.trips_cardlayout,parent,false));
		/*if(viewType==TYPE_Explore){
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}*/
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
			isLoading = true;
			loadMoreListener.onLoadMore();
		}
		System.out.println("Trips : two "+modelItems.get(position).getValue());
		((MovieHolder)holder).bindData(modelItems.get(position));
		/*if(getItemViewType(position)==TYPE_Explore){
		}*/
		//No else part needed as load holder doesn't bind any data
	}

	@Override
	public int getItemViewType(int position) {
		/*if(modelItems.get(position).getTripsType().equals("load")){
			return TYPE_LOAD;
		}else{
		}*/
		return TYPE_Explore;
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		TextView tripstype_txt;
		ImageView tripsimg;
		CardView card_view;

		public MovieHolder(View itemView) {
			super(itemView);
			tripstype_txt=(TextView) itemView.findViewById(R.id.tripstype_txt);
			tripsimg=(ImageView) itemView.findViewById(R.id.tripsimg);
			card_view=(CardView)itemView.findViewById(R.id.card_view);
		}

		void bindData(final BookingTypeModel bookingTypeModel){

			String tripstype[] = {"Pending Bookings", "Current Bookings", "Previous Bookings", "Upcoming Bookings"};

			tripstype_txt.setText(bookingTypeModel.getValue());
			String tripsType = bookingTypeModel.getValue();

			if(bookingTypeModel.getValue().equals(tripstype[0]))
			{

				tripstype_txt.setText(context.getResources().getString(R.string.PendingTrip));
				System.out.println("Trips type"+tripstype_txt.getText().toString()+" Siva"+tripstype[0]);
				tripsimg.setImageDrawable(context.getResources().getDrawable(R.drawable.trip01));

			}
			else if(tripsType.equals(tripstype[1]))
			{

				tripstype_txt.setText(context.getResources().getString(R.string.CurrentTrips));
				System.out.println("Trips type"+tripstype_txt.getText().toString()+" Siva"+tripstype[1]);
				tripsimg.setImageDrawable(context.getResources().getDrawable(R.drawable.trip02));

			}
			else if(tripsType.equals(tripstype[2]))
			{

				tripstype_txt.setText(context.getResources().getString(R.string.PreviousTrips));
				System.out.println("Trips type"+tripstype_txt.getText().toString()+" Siva"+tripstype[2]);
				tripsimg.setImageDrawable(context.getResources().getDrawable(R.drawable.trip04));

			}
			else if(tripsType.equals(tripstype[3]))
			{
				tripstype_txt.setText(context.getResources().getString(R.string.UpcomingTrips));
				System.out.println("Trips type"+tripstype_txt.getText().toString()+" Siva"+tripstype[3]);
				tripsimg.setImageDrawable(context.getResources().getDrawable(R.drawable.trip03));
			}

			card_view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					localSharedPreferences.saveSharedPreferences(Constants.TripType,bookingTypeModel.getValue());
					localSharedPreferences.saveSharedPreferences(Constants.TripTypeCount,bookingTypeModel.getCount());
					Intent x = new Intent(context,HomeActivity.class);
					x.putExtra("tabsaved",3);
					context.startActivity(x);
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

	public void clear() {
		modelItems.clear();
		notifyDataSetChanged();
	}

}