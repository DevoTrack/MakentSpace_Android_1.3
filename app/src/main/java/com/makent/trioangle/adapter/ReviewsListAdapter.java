package com.makent.trioangle.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.Makent_model;

import java.util.List;

@SuppressLint("ViewHolder")
public class ReviewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
public final int TYPE_Explore = 1;
public final int TYPE_LOAD = 2;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	Header header;
	LocalSharedPreferences localSharedPreferences;

static Context context;
		OnLoadMoreListener loadMoreListener;
		boolean isLoading = false, isMoreDataAvailable = true;

protected static final String TAG = null;
private Activity activity;
private LayoutInflater inflater;
private List<Makent_model> modelItems;

public ReviewsListAdapter(Header header,Activity activity,Context context, List<Makent_model> Items) {
	this.header = header;
	this.activity=activity;
		this.context = context;
		this.modelItems = Items;
	localSharedPreferences=new LocalSharedPreferences(context);
		}

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);

	System.out.println("View Type"+viewType);
	if(viewType == TYPE_HEADER)
	{
		return new HeaderHolder(inflater.inflate(R.layout.reviews_headerlist,parent,false));
	}
	if(viewType==TYPE_Explore){
		return new MovieHolder(inflater.inflate(R.layout.reviews_list,parent,false));
	}else{
		return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
	}
	//throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
}

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		/*if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
		isLoading = true;
		loadMoreListener.onLoadMore();
		}*/
	System.out.println("Holder name"+holder);
	System.out.println("Holder position"+position);

	if(holder instanceof HeaderHolder)
	{
		System.out.println("Header Holder position");
		HeaderHolder VHheader = (HeaderHolder)holder;
		((HeaderHolder)holder).bindData(modelItems.get(position));
		//VHheader.txtTitle.setText("Siva");
	}
	else if(holder instanceof MovieHolder)
	{
		System.out.println("BedTypeHolder position"+position);
		//Makent_model currentItem = getItem(position-1);
		if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
			isLoading = true;
			loadMoreListener.onLoadMore();
		}

		if(getItemViewType(position)==TYPE_Explore){
			((MovieHolder)holder).bindData(modelItems.get(position));
		}
		/*VHItem VHitem = (VHItem)holder;
		VHitem.txtName.setText(currentItem.getName());
		VHitem.iv.setBackgroundResource(currentItem.getId());*/
		//((BedTypeHolder)holder).bindData(currentItem);
	}
		/*if(getItemViewType(position)==TYPE_Explore){
		((BedTypeHolder)holder).bindData(modelItems.get(position));
		}*/
		//No else part needed as load holder doesn't bind any data
		}


	private Makent_model getItem(int position)
	{
		return modelItems.get(position);
	}

	/*public int getItemViewType(int position) {
		if(isPositionHeader(position))
			return TYPE_HEADER;
		return TYPE_ITEM;
	}*/
	@Override
	public int getItemViewType(int position) {
		//if (!modelItems.get(position).type.isEmpty()) {
		if (modelItems.get(position).type.equals("load")) {
			return TYPE_LOAD;
		} else if (modelItems.get(position).type.equals("head")) {
			return TYPE_HEADER;
		}else return TYPE_Explore;
		//} else return TYPE_Explore;
	}

	private boolean isPositionHeader(int position)
	{
		return position == 0;
	}
/*@Override
public int getItemViewType(int position) {
		if(modelItems.get(position).getName().equals("load")){
		return TYPE_LOAD;
		}else{
		return TYPE_Explore;
		}
		}*/

@Override
public int getItemCount() {
		return modelItems.size();
		}

    /* VIEW HOLDERS */

	class HeaderHolder extends RecyclerView.ViewHolder{
		TextView review_titlename;
		RatingBar ratingBar_total,ratingBar_accuracy,ratingBar_arrival,ratingBar_clean,ratingBar_communication,ratingBar_location,ratingBar_value;
		public HeaderHolder(View itemView) {
			super(itemView);
			String reviewrates=localSharedPreferences.getSharedPreferences(Constants.RoomReviewRate);
			System.out.println("Adapter Review Rate"+reviewrates);
			String[] reviewrate = reviewrates.split(",");
			this.review_titlename = (TextView)itemView.findViewById(R.id.review_titlename);
			this.ratingBar_total = (RatingBar)itemView.findViewById(R.id.ratingBar_total);
			this.ratingBar_accuracy = (RatingBar)itemView.findViewById(R.id.ratingBar_accuracy);
			this.ratingBar_arrival = (RatingBar)itemView.findViewById(R.id.ratingBar_arrival);
			this.ratingBar_clean = (RatingBar)itemView.findViewById(R.id.ratingBar_clean);
			this.ratingBar_communication = (RatingBar)itemView.findViewById(R.id.ratingBar_communication);
			this.ratingBar_location = (RatingBar)itemView.findViewById(R.id.ratingBar_location);
			this.ratingBar_value = (RatingBar)itemView.findViewById(R.id.ratingBar_value);
			if(reviewrate[1].equals("1")){
				review_titlename.setText(reviewrate[1]+" "+context.getResources().getString(R.string.review_one));
			}else{
				review_titlename.setText(reviewrate[1]+" "+context.getResources().getString(R.string.reviews));
			}

			ratingBar_total.setRating(Float.valueOf(reviewrate[8]));
			ratingBar_accuracy.setRating(Float.valueOf(reviewrate[2]));
			ratingBar_arrival.setRating(Float.valueOf(reviewrate[3]));
			ratingBar_clean.setRating(Float.valueOf(reviewrate[4]));
			ratingBar_communication.setRating(Float.valueOf(reviewrate[5]));
			ratingBar_location.setRating(Float.valueOf(reviewrate[6]));
			ratingBar_value.setRating(Float.valueOf(reviewrate[7]));
		}
		void bindData(Makent_model movieModel) {
			//review_titlename.setText(movieModel.getReviewUserName());
		}
	}
static class MovieHolder extends RecyclerView.ViewHolder{
	TextView review_name,review_date,review_data;
	ImageView review_image;

	public MovieHolder(View itemView) {
		super(itemView);
		review_name=(TextView) itemView.findViewById(R.id.review_name);
		review_date=(TextView) itemView.findViewById(R.id.review_date);
		review_data=(TextView) itemView.findViewById(R.id.review_data);
		review_image=(ImageView) itemView.findViewById(R.id.review_image);
	}

	void bindData(Makent_model movieModel){

		review_name.setText(movieModel.getReviewUserName());
		review_date.setText(movieModel.getReviewDate());
		review_data.setText(movieModel.getReviewMessage());
		Glide.with(context.getApplicationContext()).asBitmap().load(movieModel.getReviewUserImage()).into(new BitmapImageViewTarget(review_image) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				review_image.setImageDrawable(circularBitmapDrawable);
			}
		});

		/*Glide.with(context)
				.load(movieModel.getReviewUserImage())

				.into(new GlideDrawableImageViewTarget(review_image) {
					@Override
					public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
						super.onResourceReady(drawable, anim);
						//	explore_progress.setVisibility(View.GONE);
					}
				});*/
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

	public void setLoadMoreListener(ReviewsListAdapter.OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

}