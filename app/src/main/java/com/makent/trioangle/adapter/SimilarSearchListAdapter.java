package com.makent.trioangle.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.Room_details;

import java.util.List;

@SuppressLint("ViewHolder")
public class SimilarSearchListAdapter extends BaseAdapter {
	protected static final String TAG = null;
	private Activity activity;
	private LayoutInflater inflater;
	private List<Makent_model> modelItems;

	ImageView explore_room_image;
	ImageView thumbNail1;
	TextView explore_amount;
	TextView price;
	TextView address;
	TextView symbol;
	TextView explore_reviewrate;
    ProgressBar explore_progress;
//	Room_details d = new Room_details();
    RatingBar review;



	public SimilarSearchListAdapter(Activity activity, List<Makent_model> Items) {
		System.out.println("Image from similar list adapter items"+Items);
		this.activity = activity;
		this.modelItems = Items;
	}

	@Override
	public int getCount() {
		System.out.println("Image from similar list adapter size"+modelItems.size());
		return modelItems.size();
	}


	@Override
	public Object getItem(int location) {
		return modelItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		System.out.println("Image from similar list adapter View"+position);


		if (inflater == null) {
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}


		if (convertView == null)

			convertView = inflater.inflate(R.layout.similar_list, parent, false);
			convertView.setClickable(true);
			final Makent_model m = modelItems.get(position);

		explore_room_image = (ImageView) convertView.findViewById(R.id.similar_room_image);

		//explore_progress = (ProgressBar) convertView.findViewById(R.id.explore_progress);
		explore_amount = (TextView) convertView.findViewById(R.id.similar_amount);
		//explore_reviewrate = (TextView) convertView.findViewById(R.id.similar_reviewrate);
		//review = (RatingBar) convertView.findViewById(R.id.ratingBar);


		//review.setRating(4);

		//explore_progress.setVisibility(View.VISIBLE);
System.out.println("Image from similar list adapter"+m.getExplore_room_image());
			Glide.with(activity.getApplicationContext())
					.load(m.getExplore_room_image())

					.into(new DrawableImageViewTarget(explore_room_image) {
						@Override
						public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
							super.onResourceReady(resource, transition);
						}
					});
				 


		explore_room_image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent x = new Intent(activity, SpaceDetailActivity.class);
					activity.startActivity(x);
					//((Activity)context).finish();

				}
			});


    
		return convertView;
		
	}
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		   int targetWidth = 1250;
		   int targetHeight = 1250;
		   Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		                       targetHeight,Bitmap.Config.ARGB_8888);

		   Canvas canvas = new Canvas(targetBitmap);
		   Paint p=new Paint();
		   p.setAntiAlias(true);
		   p.setFilterBitmap(true);
		   p.setDither(true);
		   Path path = new Path();
		   path.addCircle(((float) targetWidth - 1) / 2,
		       ((float) targetHeight - 1) / 2,
		       (Math.min(((float) targetWidth), 
		       ((float) targetHeight)) / 2),
		       Path.Direction.CCW);

		   canvas.clipPath(path);
		   Bitmap sourceBitmap = scaleBitmapImage;
		   canvas.drawBitmap(sourceBitmap, 
		       new Rect(0, 0, sourceBitmap.getWidth(),
		       sourceBitmap.getHeight()), 
		       new Rect(0, 0, targetWidth, targetHeight), null);
		   return targetBitmap;
		}
	public boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		 
		if (ni != null && ni.isConnected())
		  return true;
		else
		  return false;
		}

}