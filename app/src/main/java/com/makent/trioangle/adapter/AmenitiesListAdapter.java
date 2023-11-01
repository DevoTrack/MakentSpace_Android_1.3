package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    AmenitiesListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.model.Makent_model;

import java.util.List;

@SuppressLint("ViewHolder")
public class AmenitiesListAdapter extends BaseAdapter {
	protected static final String TAG = null;
	private Activity activity;
	private LayoutInflater inflater;
	private List<Makent_model> modelItems;

	TextView amenities;
	ImageView amenities_image;

	public AmenitiesListAdapter(Activity activity, List<Makent_model> Items) {
		this.activity = activity;
		this.modelItems = Items;
	}

	@Override
	public int getCount() {
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

			convertView = inflater.inflate(R.layout.amenities_list, parent, false);
			convertView.setClickable(true);
			final Makent_model m = modelItems.get(position);

		amenities_image = convertView.findViewById(R.id.amenities_image);

		//explore_progress = (ProgressBar) convertView.findViewById(R.id.explore_progress);
		amenities = (TextView) convertView.findViewById(R.id.amenities);

		amenities.setText(m.getAmenities());

		System.out.println("m.getAmenities_image() "+m.getAmenities_image());
		Glide.with(activity).load(m.getAmenities_image()).into(amenities_image);

		return convertView;
	}
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		   int targetWidth = 1250;
		   int targetHeight = 1250;
		   Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		                       targetHeight,Bitmap.Config.ARGB_8888);

		   Canvas canvas = new Canvas(targetBitmap);
		   Paint p = new Paint();
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