package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    PayoutCountryListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.payout.CountryModel;

import java.util.ArrayList;

import static com.makent.trioangle.host.LYS_Step4_AddressDetails.alertDialogStorestwo;
import static com.makent.trioangle.profile.PayoutAddressDetailsActivity.alertDialogStores;

@SuppressLint("ViewHolder")
public class PayoutCountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public final int TYPE_Explore = 1;
	public final int TYPE_LOAD = 2;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	Header header;
	public static LocalSharedPreferences localSharedPreferences;
	static Context context;

	protected static final String TAG = null;
	private Activity activity;
	private LayoutInflater inflater;
	public  android.app.AlertDialog alertDialog;
	private ArrayList<CountryModel> modelItems;
	int oldposition=1;

	private boolean[] mIsItemClicked;



	public PayoutCountryListAdapter(Context context, ArrayList<CountryModel> Items) {
		this.header = header;
		this.context = context;
		this.modelItems = Items;
		mIsItemClicked = new boolean[240];
		localSharedPreferences=new LocalSharedPreferences(context);
		System.out.println("modelItems"+mIsItemClicked.length);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		System.out.println("View Type"+viewType);
		//mIsItemClicked = new boolean[modelItems.size()];
		//System.out.println("modelItems1"+mIsItemClicked.length);
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.payout_country_list,parent,false));
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
		//throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

		if(holder instanceof MovieHolder)
		{
			System.out.println("BedTypeHolder position"+(position));
			final CountryModel currentItem = getItem(position);

		final 	MovieHolder mainholder=(MovieHolder)holder;

			System.out.println("BedTypeHolder getCountryName"+currentItem.getCountryName());
			mainholder.countryname.setText(currentItem.getCountryName());


			mainholder.countryname.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					for (int i = 0; i < mIsItemClicked.length; i++) {

						if(i!=position) {
							mIsItemClicked[i] = false;
						}else
						{
							mIsItemClicked[position]=true;
						}

					}
					//mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
					localSharedPreferences.saveSharedPreferences(Constants.CountryName,currentItem.getCountryName().toString());
					localSharedPreferences.saveSharedPreferences(Constants.PayPalCountryCode,currentItem.getCountryCode().toString());

					if(alertDialogStores!=null) {
						alertDialogStores.cancel();
					}
					if(alertDialogStorestwo!=null) {
						alertDialogStorestwo.cancel();
					}
					/*System.out.println("Position "+position+" Current Value"+currentItem.getCountryName().toString());
					System.out.println("Position "+position+" Current Position"+currentItem.getCountryId());
					notifyItemChanged(position);
					notifyItemChanged(oldposition);
					oldposition=position;*/

				}
			});


				/*System.out.println("mIsItemClicked value "+mIsItemClicked[position]+" position "+position);
				if (mIsItemClicked[position])
					mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
				else
					mainholder.countryname.setTextColor(context.getResources().getColor(R.color.material_grey_850));*/

		}

		}


	private CountryModel getItem(int position)
	{
		return modelItems.get(position);
	}

	public int getItemViewType(int position) {

		return TYPE_Explore;
	}




	@Override
	public int getItemCount() {
		return modelItems.size();
	}



    /* VIEW HOLDERS */

	class HeaderHolder extends RecyclerView.ViewHolder{
		TextView txtTitle;
		public HeaderHolder(View itemView) {
			super(itemView);
			this.txtTitle = (TextView)itemView.findViewById(R.id.header);
			this.txtTitle.setText(context.getResources().getString(R.string.selectcountry));
			this.txtTitle.setTextSize(context.getResources().getDimension(R.dimen.midb));
			this.txtTitle.setTextColor(context.getResources().getColor(R.color.title_text_color));
		}
	}
	static class MovieHolder extends RecyclerView.ViewHolder{
		TextView countryname;
		RelativeLayout country_layout;

		public MovieHolder(View itemView) {
			super(itemView);
			countryname=(TextView) itemView.findViewById(R.id.countryname);
			country_layout=(RelativeLayout) itemView.findViewById(R.id.country_layout);
		}

		void bindData(CountryModel movieModel,final int position){

			countryname.setText(movieModel.getCountryName());


			countryname.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				//	this.notifyDataChanged();
					System.out.println("Position"+position+"Country name"+countryname.getText().toString());

					countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
					//Toast.makeText(context,"Position"+position+"Country name"+countryname.getText().toString(),Toast.LENGTH_SHORT).show();
				}
			});

		}
	}

	static class LoadHolder extends RecyclerView.ViewHolder{
		public LoadHolder(View itemView) {
			super(itemView);
		}
	}

	public void notifyDataChanged(){
		notifyDataSetChanged();
	}


}