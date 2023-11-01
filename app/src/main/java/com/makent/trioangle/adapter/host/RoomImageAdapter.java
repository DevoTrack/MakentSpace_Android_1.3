package com.makent.trioangle.adapter.host;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.model.host.RoomImageItem;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewHolder")
public class RoomImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	public static LocalSharedPreferences localSharedPreferences;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	static Activity activity;
	private LayoutInflater inflater;
	private List<RoomImageItem> modelItems;

	public static Boolean showDelete=false;


	static ArrayList<String> selectedStrings = new ArrayList<String>();




	public RoomImageAdapter(Activity activity, Context context, List<RoomImageItem> Items) {
		System.out.println("Adapter Called items"+Items);
		this.context = context;
		this.modelItems = Items;
		this.activity=activity;
		localSharedPreferences=new LocalSharedPreferences(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.gridview_item,parent,false));
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
		//if (!modelItems.get(position).type.isEmpty()) {
			if (modelItems.get(position).getImage().equals("load")) {
				return TYPE_LOAD;
			} else return TYPE_Explore;
		//} else return TYPE_Explore;
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

    /* VIEW HOLDERS */

	static class MovieHolder extends RecyclerView.ViewHolder{
		CheckBox checkBox;
		ImageView image;
		public MovieHolder(View itemView) {
			super(itemView);
			image =(ImageView) itemView.findViewById(R.id.imageView26);
			checkBox=(CheckBox) itemView.findViewById(R.id.checkBox);
		}

		void bindData(final RoomImageItem movieModel,final int position){

			System.out.println("Adapter Called model"+movieModel);
			RoomImageItem model=movieModel;

			if(showDelete){
				checkBox.setVisibility(View.VISIBLE);
			}else {
				checkBox.setVisibility(View.INVISIBLE);

			}

			if((!movieModel.getImage().equals("null"))&&movieModel.getImage()!=null){

				//progress.setVisibility(View.VISIBLE);
				Glide.with(activity)
						.load(movieModel.getImage())

						.into(new DrawableImageViewTarget(image) {
							@Override
							public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
								super.onResourceReady(resource, transition);
							}
						});


			}
			else{
				image.setImageResource(R.drawable.default_photo_bg);
			}

			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if ((isChecked)) {

						selectedStrings.add(movieModel.getId());
						System.out.println("add");
						System.out.println("checkkkkkkkkkkkkkyyyyyyyyyyyyyyyyy" + selectedStrings);

					} else {

						selectedStrings.remove(movieModel.getId());
						System.out.println("checkkkkkkkkkkkkk" + selectedStrings);

					}

				}
			});

		}
	}
	public ArrayList<String> getSelectedString(){
		return selectedStrings;
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
		System.out.println("Adapter Called");
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