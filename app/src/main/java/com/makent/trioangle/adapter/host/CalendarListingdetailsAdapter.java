package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    CalendarListingdetailsAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.tabs.HostCalenderActivity;
import com.makent.trioangle.model.host.HostCalendarListModel;

import java.util.List;

@SuppressLint("ViewHolder")
public class CalendarListingdetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	private Activity activity;
	private LayoutInflater inflater;
	private List<HostCalendarListModel> modelItems;
	public static int lastCheckedPosition = -1;
	static LocalSharedPreferences localSharedPreferences;




	public CalendarListingdetailsAdapter(Context context, List<HostCalendarListModel> Items) {
		this.context = context;
		this.modelItems = Items;
		localSharedPreferences=new LocalSharedPreferences(context.getApplicationContext());
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.host_list_dialog,parent,false));
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
		if(modelItems.get(position).getRoomThumbImages().equals("load")){
			return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		ImageView calendar_list_image;
		ImageView calendar_list_tickimage;
		RelativeLayout calendar_list;
		TextView calendar_list_title,calendar_list_unlist,calendar_list_hometype;
		public MovieHolder(View itemView) {
			super(itemView);
			calendar_list_image=(ImageView)itemView.findViewById(R.id.calendar_list_image);
			calendar_list_tickimage=(ImageView)itemView.findViewById(R.id.calendar_list_tick);
			calendar_list_title=(TextView) itemView.findViewById(R.id.calendar_list_title);
			calendar_list_unlist=(TextView) itemView.findViewById(R.id.calendar_list_unlist);
			calendar_list_hometype=(TextView) itemView.findViewById(R.id.calendar_list_hometype);
			calendar_list=(RelativeLayout) itemView.findViewById(R.id.calendar_list);
		}

		void bindData(final HostCalendarListModel movieModel, int position){

			calendar_list_title.setText(movieModel.getRoomName());
			calendar_list_hometype.setText(movieModel.getRoomType());

			if(lastCheckedPosition == position){
				calendar_list_tickimage.setVisibility(View.VISIBLE);
			}
			else {
				calendar_list_tickimage.setVisibility(View.GONE);
			}


				if(Integer.parseInt(movieModel.getRemainingSteps())!=0)
			{
				calendar_list_unlist.setText(context.getResources().getString(R.string.unlisted));
			}else
			{
				calendar_list_unlist.setText(context.getResources().getString(R.string.listed));
			}

			Glide.with(context.getApplicationContext())

					.load(movieModel.getRoomThumbImages()[0])

					.into(new DrawableImageViewTarget(calendar_list_image) {
						@Override
						public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
							super.onResourceReady(resource, transition);
						}
					});

			calendar_list.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					/*Intent x = new Intent(context,SpaceDetailActivity.class);
					context.startActivity(x);*/
					localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,movieModel.getRoomId());
					localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice,null);
					lastCheckedPosition = getAdapterPosition();
					calendar_list_tickimage.setVisibility(View.VISIBLE);
					HostCalenderActivity.alertDialogStores.cancel();

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