package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    SimilarSearchRecycleListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.model.roomModels.RoomSimilarModel;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.travelling.WishListChooseDialog;
import com.makent.trioangle.travelling.deleteWishList;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class SimilarSearchRecycleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	private static Activity activity;
	private LayoutInflater inflater;
	private ArrayList<RoomSimilarModel> modelItems;

	public static LocalSharedPreferences localSharedPreferences;
	TextView explore_amount;
	TextView price;
	TextView address;
	TextView symbol;
	TextView explore_reviewrate;
	TextView tv_instant;
    ProgressBar explore_progress;
    RatingBar review;

	public SimilarSearchRecycleListAdapter(Activity activity,Context context, ArrayList<RoomSimilarModel> Items)
	{
		this.context = context;
		this.activity=activity;
		this.modelItems = Items;
		localSharedPreferences=new LocalSharedPreferences(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore)
		{
			return new MovieHolder(inflater.inflate(R.layout.similar_list,parent,false));
		}
		else
		{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
		{
			isLoading = true;
			loadMoreListener.onLoadMore();
		}

		if(getItemViewType(position)==TYPE_Explore)
		{
			((MovieHolder)holder).bindData(modelItems.get(position));
		}
		//No else part needed as load holder doesn't bind any data
	}

	@Override
	public int getItemViewType(int position)
	{
		if(modelItems.get(position).getRoomThumbImage().equals("load"))
		{
			return TYPE_LOAD;
		}
		else
		{
			return TYPE_Explore;
		}
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		ImageView similar_room_image;
		ImageView similar_wishlists;
		TextView similar_amount,similar_roomdetails,similar_reviewrate,tv_instant;
		RatingBar similar_room_rate;
		public MovieHolder(View itemView) {
			super(itemView);
			similar_room_image=(ImageView)itemView.findViewById(R.id.similar_room_image);
			similar_wishlists=(ImageView)itemView.findViewById(R.id.similar_wishlists);
			similar_amount=(TextView) itemView.findViewById(R.id.similar_amount);
			similar_roomdetails=(TextView) itemView.findViewById(R.id.similar_roomdetails);
			similar_reviewrate=(TextView) itemView.findViewById(R.id.similar_reviewrate);
			similar_room_rate=(RatingBar) itemView.findViewById(R.id.similar_room_rate);
			tv_instant =(TextView)itemView.findViewById(R.id.tv_instant);
		}

		void bindData(final RoomSimilarModel movieModel){

			RoomSimilarModel model=movieModel;
			Glide.with(context.getApplicationContext())
					.load(movieModel.getRoomThumbImage())
					.into(new DrawableImageViewTarget(similar_room_image) {
						@Override
						public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
							super.onResourceReady(resource, transition);
						}
					});
			String currencysymbol= Html.fromHtml(localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol)).toString();
			similar_amount.setText(currencysymbol+" "+movieModel.getRoomPrice());
//			similar_amount.measure(0, 0);
//			int width=similar_amount.getMeasuredWidth();
//			if(!movieModel.getSpaceName().equals("")) {
//				SpannableString s = new SpannableString(movieModel.getSpaceName());
//				s.setSpan(new android.text.style.LeadingMarginSpan.Standard(width + 20, 0), 0, 1, 0);
//				similar_roomdetails.setText(s);
//			}
			similar_roomdetails.setText(movieModel.getRoomName());
			if(Float.valueOf(movieModel.getRatingValue())==0)
			{
				similar_reviewrate.setVisibility(View.GONE);
				similar_room_rate.setVisibility(View.GONE);
			}else
			{
				similar_reviewrate.setVisibility(View.VISIBLE);
				similar_room_rate.setVisibility(View.VISIBLE);
			}

			if(movieModel.getReviewsValue().equals("1")){
				similar_reviewrate.setText(movieModel.getReviewsValue()+" "+context.getResources().getString(R.string.review_one));
			}else{
				similar_reviewrate.setText(movieModel.getReviewsValue()+" "+context.getResources().getString(R.string.review));
			}

			similar_room_rate.setRating(Float.valueOf(movieModel.getRatingValue()));

			similar_room_image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
					localSharedPreferences.saveSharedPreferences(Constants.CheckIn,null);
					localSharedPreferences.saveSharedPreferences(Constants.CheckOut,null);
					localSharedPreferences.saveSharedPreferences(Constants.CheckInOut,null);
					localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,movieModel.getRoomId());
					localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice,movieModel.getRoomPrice());
					Intent x = new Intent(context, SpaceDetailActivity.class);
					context.startActivity(x);

				}
			});

			if(movieModel.getInstant_book().equals("Yes")){
				tv_instant.setVisibility(View.VISIBLE);
			}

			if(movieModel.getIsWhishlist().equals("Yes")) {
				similar_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
			}else
			{
				similar_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
			}
			similar_wishlists.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					localSharedPreferences.saveSharedPreferences(Constants.Reload,"reload");
					localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId,movieModel.getRoomId());
					Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1) );
					Drawable icon1 = new FontIconDrawable(context,context.getResources().getString(R.string.f1like),font1)
							.sizeDp(20)
							.colorRes(R.color.title_text_color);
					Drawable icon2 = new FontIconDrawable(context,context.getResources().getString(R.string.f1like1),font1)
							.sizeDp(20)
							.colorRes(R.color.red_text);
					if(movieModel.getIsWhishlist().equals("Yes")) {
						movieModel.setIsWhishlist("No");
						similar_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
						//similar_wishlists.setImageDrawable(icon1);
						deleteWishList task = new deleteWishList(context);
						task.execute();
					}else
					{
						//similar_wishlists.setImageDrawable(icon2);
						localSharedPreferences.saveSharedPreferences(Constants.WishListAddress,movieModel.getCityName());
						WishListChooseDialog cdd=new WishListChooseDialog(activity);
						System.out.println("isFinishing "+activity.isFinishing());
						if(!activity.isFinishing()){
							cdd.show();
							localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist, true);
							localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId, movieModel.getRoomId());
							//similar_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
							//movieModel.setIsWhishlist("Yes");
						}
					}
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