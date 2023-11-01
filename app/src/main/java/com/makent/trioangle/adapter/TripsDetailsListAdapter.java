package com.makent.trioangle.adapter;


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
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.tripsModel.TripDetailData;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.spacebooking.views.RequestSpaceActivity;
import com.makent.trioangle.travelling.TripsSubDetails;

import java.util.ArrayList;

import static com.makent.trioangle.helper.Constants.Bookingtype;
import static com.makent.trioangle.helper.Constants.RESERVATIONID;
import static com.makent.trioangle.helper.Constants.SESSIONID;


@SuppressLint("ViewHolder")
public class TripsDetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
private ArrayList <TripDetailData> modelItems;


		TextView explore_amount;
		TextView price;
		TextView address;
		TextView symbol;
		TextView explore_reviewrate;


public TripsDetailsListAdapter(Header header, Context context, ArrayList<TripDetailData> Items) {
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
		return new MovieHolder(inflater.inflate(R.layout.tripsdetailslist,parent,false));
	}else{
        return null;
		//return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
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
		//VHheader.txtTitle.setText("Siva");
	}
	else if(holder instanceof MovieHolder)
	{
		System.out.println("BedTypeHolder position"+position);
		TripDetailData currentItem = getItem(position-1);
		/*VHItem VHitem = (VHItem)holder;
		VHitem.txtName.setText(currentItem.getName());
		VHitem.iv.setBackgroundResource(currentItem.getId());*/
		((MovieHolder)holder).bindData(currentItem);
	}
		/*if(getItemViewType(position)==TYPE_Explore){
		((BedTypeHolder)holder).bindData(modelItems.get(position));
		}*/
		//No else part needed as load holder doesn't bind any data
		}


	private TripDetailData getItem(int position)
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
		return modelItems.size()+1;
		}

    /* VIEW HOLDERS */

	class HeaderHolder extends RecyclerView.ViewHolder{
		TextView tripsdetails_title,tripsdetails_titlemsg;
		public HeaderHolder(View itemView) {
			super(itemView);
			this.tripsdetails_title = (TextView)itemView.findViewById(R.id.tripsdetails_title);
			this.tripsdetails_titlemsg = (TextView)itemView.findViewById(R.id.tripsdetails_titlemsg);

			String tripsdetails=localSharedPreferences.getSharedPreferences(Constants.TotalTripDetails);
			System.out.println("Total Trips adapter"+tripsdetails);
			String[] tripsdetailin = tripsdetails.split(",");
			if(tripsdetailin[0].equals("0")){
				tripsdetails_title.setText(tripsdetailin[1]);
				tripsdetails_titlemsg.setText(context.getResources().getString(R.string.youhave) + " " + tripsdetailin[0] + " " + tripsdetailin[1]);
			}
			else {
				if (tripsdetails != null) {
					String[] tripsdetail = tripsdetails.split(",");
					tripsdetails_title.setText(tripsdetail[1]);
					tripsdetails_titlemsg.setText(context.getResources().getString(R.string.youhave) + " " + tripsdetail[0] + " " + tripsdetail[1]);
				}
			}
		}
	}
static class MovieHolder extends RecyclerView.ViewHolder{
	TextView td_status,td_username,td_date,td_roomname,td_roomdetails;
	ImageView td_pimage;
	Button td_booknow;
	RelativeLayout inboxlistdetails;

	public MovieHolder(View itemView) {
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

	void bindData(final TripDetailData movieModel){

		td_username.setText(movieModel.getHostUserName());
		String tripStatus=movieModel.getReservationStatus();
		td_status.setText(tripStatus);
		updateStatus(tripStatus,td_status);

		if(tripStatus.equals("Cancelled")||tripStatus.equals("Declined")||tripStatus.equals("Expired"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.text_shadow));
		}else if(tripStatus.equals("Accepted"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.background));
		}else if(tripStatus.equals("Pre-Approved")||tripStatus.equals("Pre-Accepted")||tripStatus.equals("Inquiry"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.yellow));
		}else if(tripStatus.equals("Pending"))
		{
			td_status.setTextColor(context.getResources().getColor(R.color.pink));
		}

		td_date.setText(movieModel.getReservationDate());
		td_roomname.setText(movieModel.getSpaceName());
		td_roomdetails.setText(movieModel.getSpaceLocation());
		String host_user_image=movieModel.getHostThumbImage();
		reservationId=movieModel.getReservationId();
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
				System.out.println("Other user id"+movieModel.getHostUserId());
				x.putExtra("otheruserid",movieModel.getHostUserId());
				context.startActivity(x);
			}
		});

		String isBookNow=movieModel.getBookingStatus();
		if(isBookNow.equals(""))
		{
			td_booknow.setVisibility(View.GONE);
		}else if(isBookNow.equals("Available"))
		{
			td_booknow.setVisibility(View.VISIBLE);
		}else
		{
			td_booknow.setVisibility(View.GONE);
		}



		inboxlistdetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("roomDetail", movieModel);
				System.out.println("tripDetailData"+movieModel.getSpaceId());
				Intent x = new Intent(context,TripsSubDetails.class);
				x.putExtras(bundle);
				x.putExtra("reservationid",movieModel.getReservationId());
				x.putExtra("tripstatus",movieModel.getReservationStatus());
				((Activity) context).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
				context.startActivity(x);
			}
		});

		td_booknow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent x = new Intent(context, RequestSpaceActivity.class);
				x.putExtra(SESSIONID,"");
				x.putExtra(RESERVATIONID,movieModel.getReservationId());
				x.putExtra(Bookingtype,"Yes");

				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				context.startActivity(x, bndlanimation);

				/*String userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
				String url="pay_now?reservation_id="+movieModel.getReservationId()+"&token="+userid;
				String baseurl=context.getResources().getString(R.string.baseurl);
				String weburl=baseurl+url;
				weburl=weburl.replaceAll(" ","%20");
				System.out.println("Payment Page Url TripsDetailsListAdapter "+weburl);
				Intent x = new Intent(context,PaymentWebView.class);
				x.putExtra("weburl",weburl);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
				context.startActivity(x);*/
			}
		});
	}
}
	public static void updateStatus(String status, TextView tvTextView) {

		if(status!=null&&status.equals("Cancelled")){
			tvTextView.setText(context.getResources().getString(R.string.cancelled));
		}else if(status!=null&&status.equals("Declined")){
			tvTextView.setText(context.getResources().getString(R.string.declined));
		}else if(status!=null&&status.equals("Expired")){
			tvTextView.setText(context.getResources().getString(R.string.expire));
		}else if(status!=null&&status.equals("Accepted")){
			tvTextView.setText(context.getResources().getString(R.string.accepted));
		}else if(status!=null&&status.equals("Pre-Approved")){
			tvTextView.setText(context.getResources().getString(R.string.preApproved));
		}else if(status!=null&&status.equals("Pre-Accepted")){
			tvTextView.setText(context.getResources().getString(R.string.preAccepted));
		}else if(status!=null&&status.equals("Inquiry")){
			tvTextView.setText(context.getResources().getString(R.string.inquiry));
		}else if(status!=null&&status.equals("Pending")){
			tvTextView.setText(context.getResources().getString(R.string.pending));
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