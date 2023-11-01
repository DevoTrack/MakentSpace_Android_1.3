package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    PayPalEmailAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.payout.PayoutDetail;
import com.makent.trioangle.profile.PayoutEmailListActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

@SuppressLint("ViewHolder")
public class PayPalEmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener {

	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;

	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;

	protected static final String TAG = null;
	private ArrayList<PayoutDetail> modelItems;
	static  boolean check=false;
	protected boolean isInternetAvailable;
	int selected = -1;

	String payoutid,type,userid;
	LocalSharedPreferences localSharedPreferences;
	Dialog_loading mydialog;
	public @Inject
	ApiService apiService;
	public @Inject
	CommonMethods commonMethods;

	public PayPalEmailAdapter(Activity activity,Context context, ArrayList<PayoutDetail> Items) {
		this.context = context;
		this.modelItems = Items;
		localSharedPreferences=new LocalSharedPreferences(context);
		mydialog = new Dialog_loading(context);
		mydialog.setCancelable(false);
		mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ButterKnife.bind((Activity) context);
		AppController.getAppComponent().inject(this);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.payout_paypal_list,parent,false));
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

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
		if(modelItems.get(position).getPaypalEmail().equals("load")){
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

	class MovieHolder extends RecyclerView.ViewHolder{
		TextView paypal_email,payout_default;
		RelativeLayout paypalemailmore,paypalemailid;
		Button makedefault,delete;
		ImageView image_child1;


		public MovieHolder(View itemView) {
			super(itemView);
			paypal_email=(TextView) itemView.findViewById(R.id.paypal_email);
			payout_default=(TextView) itemView.findViewById(R.id.paypal_ready);
			paypalemailid=(RelativeLayout) itemView.findViewById(R.id.paypalemailid);
			paypalemailmore=(RelativeLayout) itemView.findViewById(R.id.paypalemailmore);
			delete=(Button) itemView.findViewById(R.id.paypal_delete);
			makedefault=(Button) itemView.findViewById(R.id.paypal_default);
			image_child1 =(ImageView)itemView.findViewById(R.id.image_child1);

		}

		void bindData(final PayoutDetail movieModel, int position){

			paypal_email.setText(movieModel.getPaypalEmail().toString());
			final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

			if(movieModel.getIsDefault().equals("No")) {
				payout_default.setVisibility(View.GONE);
			}else
			{
				payout_default.setVisibility(View.VISIBLE);
			}
			commonMethods.rotateArrow(image_child1,context);

			if(selected==position){
				paypalemailmore.setClickable(true);
//				if (movieModel.getIsDefault().equals("No"))
//				{
					if(context.getResources().getString(R.string.layout_direction).equals("1")){
						paypalemailid.animate().translationX(+(screenWidth / 2 + 100)).setDuration(200);
					}else{
						paypalemailid.animate().translationX(-(screenWidth / 2 + 100)).setDuration(200);
					}
//				}

			}
			else
			{
//				if (movieModel.getIsDefault().equals("No"))
//				{
					paypalemailmore.setClickable(false);
					paypalemailid.animate().translationX(0).setDuration(200);
//				}
			}
			paypalemailmore.setClickable(false);

			paypalemailid.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(movieModel.getIsDefault().equals("No")) {

						Log.e("Default Position","Def Position"+position);
						if (!paypalemailmore.isClickable()) {
							paypalemailmore.setClickable(true);
							//paypalemailid.animate().translationX(-(screenWidth / 2 + 100)).setDuration(200);
							Log.e("selected position","sel position"+selected);
							int pos = (Integer)paypalemailid.getTag();
							Log.e("current position","current position"+pos);
							if(selected==position){
								Log.e("Condition","Condition true");
								selected = -1;
							}else{
								selected = pos;
								Log.e("Condition","Condition false");
							}
							notifyDataChanged();
							//notifyItemChanged(position);
						} else {
							paypalemailmore.setClickable(false);
							//paypalemailid.animate().translationX(0).setDuration(200);
							selected = -1;
						}
					}
					else
					{

					}
				}
			});
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					isInternetAvailable = getNetworkState().isConnectingToInternet();
					if (isInternetAvailable) {
						userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
						payoutid = movieModel.getPayoutId().toString();
						type = "delete";
						UpdatePayoutDetails();
						//	payoutemail.payoutOption(view,position,0); // // 0  delete and 1 to set default
						paypalemailmore.setClickable(false);
						paypalemailid.animate().translationX(0).setDuration(200);
						//notifyItemRemoved(getAdapterPosition());
					}else {
						Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
					}
				}
			});
			makedefault.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					isInternetAvailable = getNetworkState().isConnectingToInternet();
					if (isInternetAvailable) {
						userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
						payoutid = movieModel.getPayoutId().toString();
						type = "default";
						UpdatePayoutDetails();
						paypalemailmore.setClickable(false);
						paypalemailid.animate().translationX(0).setDuration(200);
					}else {
						Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
					}
				}
			});

			paypalemailid.setTag(position);
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

	@Override
	public void onSuccess(JsonResponse jsonResp, String data) {
		if (jsonResp.isSuccess()) {
			if (mydialog.isShowing())
				mydialog.dismiss();
			Intent x = new Intent(context, PayoutEmailListActivity.class);
			//activity.setResultTemp(Activity.RESULT_OK,x);
			((Activity)context).finish();
			context.startActivity(x);
		} else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
			if (mydialog.isShowing()) {
				mydialog.dismiss();
			}
		}
	}

	@Override
	public void onFailure(JsonResponse jsonResp, String data) {
		if (mydialog.isShowing())
			mydialog.dismiss();
	}

	public void UpdatePayoutDetails(){
		apiService.payoutChanges(userid,payoutid,type).enqueue(new RequestCallback(this));
	}


	public ConnectionDetector getNetworkState() {
		ConnectionDetector connectionDetector = new ConnectionDetector(context);
		return connectionDetector;
	}

}