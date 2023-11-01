package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    ODAmenitiesAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.host.optionaldetails.OD_AmenitiesNew;
import com.makent.trioangle.model.retrofithost.OdAmenitiesArray;

import java.util.List;

@SuppressLint("ViewHolder")
public class ODAmenitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	public static LocalSharedPreferences localSharedPreferences;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	static Activity activity;
	private LayoutInflater inflater;
	private List<OdAmenitiesArray> modelItems;



	

	public ODAmenitiesAdapter(OD_AmenitiesNew activity, OD_AmenitiesNew context, List<OdAmenitiesArray> amenitiesList) {
		System.out.println("Checkins four amenities ");

		this.context = context;
		this.modelItems = amenitiesList;
		this.activity=activity;
		localSharedPreferences=new LocalSharedPreferences(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		System.out.println("Checkins five amenities ");

		if(viewType==TYPE_Explore){
			System.out.println("Checkins six amenities ");
			return new MovieHolder(inflater.inflate(R.layout.amenities_item_child,parent,false));
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

		System.out.println("Checkins seven amenities ");

		if(getItemViewType(position)==TYPE_Explore){
			System.out.println("Checkins eight amenities ");
			((MovieHolder)holder).bindData(modelItems.get(position),position);
		}
		//No else part needed as load holder doesn't bind any data
	}

	@Override
	public int getItemViewType(int position) {
		//if (!modelItems.get(position).type.isEmpty()) {
			/*if (modelItems.get(position).getAmenities().equals("load")) {
				return TYPE_LOAD;
			} else*/
			return TYPE_Explore;
		//} else return TYPE_Explore;
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		TextView tv_child;
		LinearLayout tv_linear;
		CheckBox amenities_check;
		public MovieHolder(View itemView) {
			super(itemView);
			tv_child=(TextView) itemView.findViewById(R.id.tv_child);
			tv_linear=(LinearLayout) itemView.findViewById(R.id.tv_linear);
			amenities_check=(CheckBox) itemView.findViewById(R.id.amenities_check);
		}

		void bindData(final OdAmenitiesArray movieModel,final int position){

			System.out.println("checking ins amenities adapter ");

			OdAmenitiesArray model=movieModel;

			tv_child.setText(movieModel.getName());
			if(movieModel.getAmenitiesselected()) {
				amenities_check.setChecked(true);
			}else
			{
				amenities_check.setChecked(false);
			}

			tv_linear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					amenities_check.setChecked(!movieModel.getAmenitiesselected());
					movieModel.setAmenitiesselected(!movieModel.getAmenitiesselected());
				}
			});

			amenities_check.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					amenities_check.setChecked(amenities_check.isChecked());
					movieModel.setAmenitiesselected(amenities_check.isChecked());
				}
			});

		}
	}

	static class LoadHolder extends RecyclerView.ViewHolder{
		public LoadHolder(View itemView) {
			super(itemView);

			ImageView imageView = (ImageView) itemView.findViewById(R.id.itemloading);
			DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);
			Glide.with(context).load(R.drawable.dot_loading).into(imageViewTarget);
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