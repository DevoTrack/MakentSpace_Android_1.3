package com.makent.trioangle.adapter.host;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.LYS_Location;
import com.makent.trioangle.host.LYS_Property_Type;
import com.makent.trioangle.model.host.Property_Type_model;

import java.util.List;

@SuppressLint("ViewHolder")
public class MorePropertyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	DynamicHeight dynamicHeight = new LYS_Property_Type();
	RecyclerView.ViewHolder holder1;
	int position1;
	public static  String str;

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	private List<Property_Type_model> modelItems;
	private RecyclerView rv;
	public static RecyclerView recyclerView;
	static LocalSharedPreferences localSharedPreferences;


	public MorePropertyAdapter(Context context, List<Property_Type_model> Items, RecyclerView rv) {
		this.context = context;
		this.modelItems = Items;
		this.rv=rv;
		localSharedPreferences=new LocalSharedPreferences(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.nested_search_item_child_new,parent,false));
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


		if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
			isLoading = true;
			loadMoreListener.onLoadMore();
		}

		if(getItemViewType(position)==TYPE_Explore){
			((MovieHolder)holder).bindData(modelItems.get(position),position);
			int focusPos=5;
			if (focusPos == position) { // focus last clicked view again
				((MovieHolder) holder).tv_child.requestFocus();
			}

			holder.itemView.post(new Runnable() {
				@Override
				public void run() {

					int cellWidth = holder.itemView.getWidth();// this will give you cell width dynamically
					int cellHeight = holder.itemView.getHeight();// this will give you cell height dynamically

					System.out.print("Text View Height "+cellHeight);
					System.out.print("Text View Weight "+cellWidth);
                    str= String.valueOf(cellHeight+cellHeight);
					rv.setMinimumHeight(Integer.parseInt(str));

					dynamicHeight.HeightChange(position, cellHeight); //call your iterface hear
				}
			});
		}
		//No else part needed as load holder doesn't bind any data
	}

	@Override
	public int getItemViewType(int position) {
		if(modelItems.get(position).type.equals("load")){
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
		TextView tv_child;
		LinearLayout tv_linear;
		ImageView icons;

		public MovieHolder(View itemView) {
			super(itemView);
			tv_child=(TextView)itemView.findViewById(R.id.tv_child);
			tv_linear=(LinearLayout)itemView.findViewById(R.id.tv_linear);
			icons= itemView.findViewById(R.id.icons);
		}

		void bindData(final Property_Type_model movieModel, final int position){


			tv_child.setTextColor(context.getResources().getColor(R.color.text_shadow));
			tv_child.setBackgroundColor(context.getResources().getColor(R.color.title_text_color));
			tv_linear.setBackgroundColor(context.getResources().getColor(R.color.title_text_color));
			tv_child.setText(movieModel.getName());
			Glide.with(context).load(movieModel.getImage()).into(icons);

			System.out.print(movieModel.getName());
			Log.d("getname",movieModel.getName());



			tv_child.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				//	Toast.makeText(context,"Current position"+String.valueOf(position+4),Toast.LENGTH_SHORT).show();
					localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,movieModel.getName());
					localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,movieModel.getId());
//					Intent x = new Intent(context,LYS_Location.class);
//					context.startActivity(x);
					Intent lysproperty=new Intent(context,LYS_Location.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
					context.startActivity(lysproperty,bndlanimation);

				}
			});

			tv_linear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//	Toast.makeText(context,"Current position"+String.valueOf(position+4),Toast.LENGTH_SHORT).show();
					localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,movieModel.getName());
					localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,movieModel.getId());
//					Intent x = new Intent(context,LYS_Location.class);
//					context.startActivity(x);

					Intent lysproperty=new Intent(context,LYS_Location.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
					context.startActivity(lysproperty,bndlanimation);

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
		System.out.println("Item Size"+modelItems.size());
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