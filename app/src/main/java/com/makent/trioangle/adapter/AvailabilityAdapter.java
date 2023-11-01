package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/guest
 * @category    CalendarListingdetailsAdapter
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.optionaldetails.AddMinMax;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.roomModels.RoomAvailabilityRules;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

@SuppressLint("ViewHolder")
public class AvailabilityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener {

	public @Inject
	ApiService apiService;
	public @Inject
	CommonMethods commonMethods;
	public @Inject
	Gson gson;
	public final int TYPE_Explore = 0;
	public final int TYPE_LOAD = 1;
	public static String from,userid;
	static Context context;
	OnLoadMoreListener loadMoreListener;
	boolean isLoading = false, isMoreDataAvailable = true;
	Dialog_loading mydialog;
	protected static final String TAG = null;
	private ArrayList <RoomAvailabilityRules> modelItems;
	static LocalSharedPreferences localSharedPreferences;
	public int selectedpos;

	public AvailabilityAdapter(Context context, ArrayList<RoomAvailabilityRules> Items, String from) {
		this.context = context;
		this.modelItems = Items;
		this.from = from;
		localSharedPreferences=new LocalSharedPreferences(context.getApplicationContext());
		mydialog = new Dialog_loading(context);
		mydialog.setCancelable(false);
		mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
		ButterKnife.bind((Activity) context);
		AppController.getAppComponent().inject(this);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
			if(viewType==TYPE_Explore){
				if(from.equals("trip")){
				return new MovieHolder(inflater.inflate(R.layout.availability_list,parent,false));
				}else{
					return new MovieHolder (inflater.inflate(R.layout.availability_list_reduced,parent,false));
				}
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
		if(modelItems.get(position).getType().equals("load")){
			return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}
	}

	@Override
	public int getItemCount() {
		return modelItems.size();
	}

	@Override
	public void onSuccess(JsonResponse jsonResp, String data) {
		if (mydialog.isShowing()) {
			mydialog.dismiss();
		}
		if (jsonResp.isSuccess()) {
			try{
				JSONObject response = new JSONObject(jsonResp.getStrResponse());
				JSONArray arrJson = response.getJSONArray("availability_rules");
				System.out.println("responseAvailability="+arrJson);
				localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, arrJson.toString());
				modelItems.remove(selectedpos);
				notifyDataChanged();
			}catch (JSONException j){
				j.printStackTrace();
			}

		} else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {

		}
	}

	@Override
	public void onFailure(JsonResponse jsonResp, String data) {
		if (mydialog.isShowing()) {
			mydialog.dismiss();
		}
	}

    /* VIEW HOLDERS */

	 class MovieHolder extends RecyclerView.ViewHolder{
		RelativeLayout availability_background;
		TextView during,minimum_stay,maximum_stay;
		private ImageView edit_icon,delete_icon;
		public MovieHolder(View itemView) {
			super(itemView);
			during=(TextView) itemView.findViewById(R.id.during);
			minimum_stay=(TextView) itemView.findViewById(R.id.minimum_stay);
			maximum_stay=(TextView) itemView.findViewById(R.id.maximum_stay);
			availability_background=(RelativeLayout) itemView.findViewById(R.id.availability_background);
			delete_icon = (ImageView) itemView.findViewById(R.id.delete_icon);
			edit_icon = (ImageView) itemView.findViewById(R.id.edit_icon);
		}

		void bindData(final RoomAvailabilityRules movieModel, int position){



			during.setText(context.getResources().getString(R.string.during)+" "+movieModel.getDuring());

			if(from.equals("trip")){
				delete_icon.setVisibility(View.GONE);
				edit_icon.setVisibility(View.GONE);
			}else{
				delete_icon.setVisibility(View.VISIBLE);
				edit_icon.setVisibility(View.VISIBLE);
			}

			if(movieModel.getMinimumStay().equals("")){
				minimum_stay.setVisibility(View.GONE);
			}else if(movieModel.getMinimumStay().equals("1")){
				minimum_stay.setVisibility(View.VISIBLE);
				minimum_stay.setText(context.getResources().getString(R.string.guest_stay_minimum)+" "+movieModel.getMinimumStay()+" "+context.getResources().getString(R.string.nightc));
			}else {
				minimum_stay.setVisibility(View.VISIBLE);
				minimum_stay.setText(context.getResources().getString(R.string.guest_stay_minimum)+" "+movieModel.getMinimumStay()+" "+context.getResources().getString(R.string.nights));
			}
			Log.v("getMaximumStay()","0"+movieModel.getMaximumStay());
			if(movieModel.getMaximumStay().equals("")){
				maximum_stay.setVisibility(View.GONE);
			}else if(movieModel.getMaximumStay().equals("1")){
				maximum_stay.setVisibility(View.VISIBLE);
				maximum_stay.setText(context.getResources().getString(R.string.guest_stay_maximum)+" "+movieModel.getMaximumStay()+" "+context.getResources().getString(R.string.nightc));
			}else {
				maximum_stay.setVisibility(View.VISIBLE);
				maximum_stay.setText(context.getResources().getString(R.string.guest_stay_maximum)+" "+movieModel.getMaximumStay()+" "+context.getResources().getString(R.string.nights));
			}

			availability_background.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
			edit_icon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, AddMinMax.class);
					i.putExtra("from","edit");
					i.putExtra("type",movieModel.getType());
					i.putExtra("id",movieModel.getId());
					i.putExtra("minimum_stay",movieModel.getMinimumStay());
					i.putExtra("maximum_stay",movieModel.getMaximumStay());
					i.putExtra("start_date",movieModel.getStartDate());
					i.putExtra("end_date",movieModel.getEndDate());
					i.putExtra("during",movieModel.getDuring());
					i.putExtra("start_date_formatted",movieModel.getStartDateFormatted());
					i.putExtra("end_date_formatted",movieModel.getEndDateFormatted());
					i.putExtra("position",position);
					context.startActivity(i);
				}
			});

			delete_icon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					delete_icon.setEnabled(false);
					//new removeMinMaxForDays().execute(movieModel.getId(),movieModel.getSpaceID(),String.valueOf(position));
					removeMinMaxForDays(movieModel.getId());
					selectedpos=position;

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

	private void removeMinMaxForDays(String ids) {

		System.out.println("Ids check "+ids);

		if (!mydialog.isShowing()) {
			mydialog.show();
		}
		apiService.deleteAvailabilityRule(userid,localSharedPreferences.getSharedPreferences(Constants.mSpaceId),ids).enqueue(new RequestCallback(this));

	}

}