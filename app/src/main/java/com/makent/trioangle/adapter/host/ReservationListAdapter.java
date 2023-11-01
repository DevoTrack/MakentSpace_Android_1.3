package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    ReservationListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.PreAcceptActivity;
import com.makent.trioangle.host.ReservationDetails;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.host.ReservationDetailsModel;
import com.makent.trioangle.profile.ProfilePageActivity;

import java.util.List;

@SuppressLint("ViewHolder")
public class ReservationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
public final int TYPE_Explore = 1;
public final int TYPE_LOAD = 2;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	Header header;

static Context context;
	static LocalSharedPreferences localSharedPreferences;
	static String reservationId;
		boolean isLoading = false, isMoreDataAvailable = true;

protected static final String TAG = null;
private Activity activity;
private LayoutInflater inflater;
private List<ReservationDetailsModel> modelItems;


		TextView explore_amount;
		TextView price;
		TextView address;
		TextView symbol;
		TextView explore_reviewrate;


public ReservationListAdapter(Header header, Context context, List<ReservationDetailsModel> Items) {
	this.header = header;
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
		return new HeaderHolder(inflater.inflate(R.layout.tripsdetailsheader,parent,false));
	}
	if(viewType==TYPE_Explore){
		return new ReservationHolder(inflater.inflate(R.layout.tripsdetailslist,parent,false));
	}else{
        return null;
		//return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
	}
	//throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
}

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



	System.out.println("Holder name"+holder);
	System.out.println("Holder position"+position);

	if(holder instanceof HeaderHolder)
	{
		System.out.println("Header Holder position");
		HeaderHolder VHheader = (HeaderHolder)holder;

	}
	else if(holder instanceof ReservationHolder)
	{
		System.out.println("ReservationHolder position"+position);
		ReservationDetailsModel currentItem = getItem(position-1);

		((ReservationHolder)holder).bindData(currentItem);
	}

		}


	private ReservationDetailsModel getItem(int position)
	{
		return modelItems.get(position);
	}

	public int getItemViewType(int position) {
		if(isPositionHeader(position))
			return TYPE_HEADER;
		return TYPE_Explore;
	}

	private boolean isPositionHeader(int position)
	{
		return position == 0;
	}


@Override
public int getItemCount() {
		return modelItems.size()+1;
		}


	public static void updateStatus(String status, TextView tvTextView) {

		if (status != null && status.equals("Cancelled")) {
			tvTextView.setText(context.getResources().getString(R.string.cancelled));
		} else if (status != null && status.equals("Declined")) {
			tvTextView.setText(context.getResources().getString(R.string.declined));
		} else if (status != null && status.equals("Expired")) {
			tvTextView.setText(context.getResources().getString(R.string.expire));
		} else if (status != null && status.equals("Accepted")) {
			tvTextView.setText(context.getResources().getString(R.string.accepted));
		} else if (status != null && status.equals("Pre-Approved")) {
			tvTextView.setText(context.getResources().getString(R.string.preApproved));
		} else if (status != null && status.equals("Pre-Accepted")) {
			tvTextView.setText(context.getResources().getString(R.string.preAccepted));
		} else if (status != null && status.equals("Inquiry")) {
			tvTextView.setText(context.getResources().getString(R.string.inquiry));
		} else if (status != null && status.equals("Pending")) {
			tvTextView.setText(context.getResources().getString(R.string.pending));
		}

	}



	/* VIEW HOLDERS */

	class HeaderHolder extends RecyclerView.ViewHolder{
		TextView tripsdetails_title,tripsdetails_titlemsg;
		public HeaderHolder(View itemView) {
			super(itemView);


			int reservationcount=localSharedPreferences.getSharedPreferencesInt(Constants.ReservationCount);
			this.tripsdetails_title = (TextView)itemView.findViewById(R.id.tripsdetails_title);
			this.tripsdetails_titlemsg = (TextView)itemView.findViewById(R.id.tripsdetails_titlemsg);

			tripsdetails_title.setText(context.getResources().getString(R.string.reservation));
			if(reservationcount>0)
			{
				if(reservationcount==1)
				{
					tripsdetails_titlemsg.setText(context.getResources().getString(R.string.youhave)+" "+String.valueOf(reservationcount)+" "+context.getResources().getString(R.string.reservations));
				}else
				{
					tripsdetails_titlemsg.setText(context.getResources().getString(R.string.youhave)+" "+reservationcount+" "+context.getResources().getString(R.string.reservationss));
				}
			}
		}
	}
static class ReservationHolder extends RecyclerView.ViewHolder{
	TextView td_status,td_username,td_date,td_roomname,td_roomdetails;
	ImageView td_pimage;
	Button td_booknow;
	RelativeLayout inboxlistdetails;

	public ReservationHolder(View itemView) {
		super(itemView);
		td_status=(TextView) itemView.findViewById(R.id.td_status);
		td_username=(TextView) itemView.findViewById(R.id.td_username);
		td_date=(TextView) itemView.findViewById(R.id.td_date);
		td_roomname=(TextView) itemView.findViewById(R.id.td_roomname);
		td_roomdetails=(TextView) itemView.findViewById(R.id.td_roomdetails);
		td_pimage=(ImageView) itemView.findViewById(R.id.td_pimage);
		td_booknow=(Button) itemView.findViewById(R.id.td_booknow);


		inboxlistdetails=(RelativeLayout) itemView.findViewById(R.id.inboxlistdetails);
	}




	void bindData(final ReservationDetailsModel reservationDetailsModel){

		td_username.setText(reservationDetailsModel.getOtherUserName());
		String tripStatus=reservationDetailsModel.getBookingStatus();
		td_status.setText(tripStatus);
		ReservationListAdapter.updateStatus(tripStatus, this.td_status);
		if(tripStatus.equals("Cancelled")||tripStatus.equals("Declined")||tripStatus.equals("Expired"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.text_shadow));
		}else if(tripStatus.equals("Accepted"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.background));
		}else if(tripStatus.equals("Pre-Accepted")||tripStatus.equals("Inquiry"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.yellow));
		}else if(tripStatus.equals("Pending"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.pink));
		}

		td_date.setText(reservationDetailsModel.getReservationDate());
		td_roomname.setText(reservationDetailsModel.getSpaceName());
		td_roomdetails.setText(reservationDetailsModel.getSpaceLocation());
		String host_user_image=reservationDetailsModel.getOtherThumbImage();
		reservationId=reservationDetailsModel.getReservationId();
		Glide.with(context.getApplicationContext()).asBitmap().load(host_user_image).into(new BitmapImageViewTarget(td_pimage) {
			@Override
			protected void setResource(Bitmap resource) {
				RoundedBitmapDrawable circularBitmapDrawable =
						RoundedBitmapDrawableFactory.create(context.getResources(), resource);
				circularBitmapDrawable.setCircular(true);
				td_pimage.setImageDrawable(circularBitmapDrawable);
			}
		});

		td_pimage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View vw) {
				Intent x = new Intent(context,ProfilePageActivity.class);
				System.out.println("Other user id"+reservationDetailsModel.getOtherUserId());
				x.putExtra("otheruserid",reservationDetailsModel.getOtherUserId());
				context.startActivity(x);
			}
		});

		if(tripStatus.equals("Pending"))
		{td_booknow.setText(context.getResources().getString(R.string.preaccept));
			td_booknow.setVisibility(View.VISIBLE);
		}else
		{
			td_booknow.setVisibility(View.GONE);
		}



		inboxlistdetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent x = new Intent(context,ReservationDetails.class);
				x.putExtra("isHost",1);
				x.putExtra("reservationid",reservationDetailsModel.getReservationId());
				x.putExtra("reservationtatus",reservationDetailsModel.getBookingStatus());

				((Activity) context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
				context.startActivity(x);
			}
		});

		td_booknow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent x = new Intent(context, PreAcceptActivity.class);
				x.putExtra("reservationid",reservationDetailsModel.getReservationId());
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
				context.startActivity(x,bndlanimation);
			}
		});
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

	public void clear() {
		modelItems.clear();
		notifyDataSetChanged();
	}


}