package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    CountryListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.travelling.PaymentCountryList;

import java.util.List;

@SuppressLint("ViewHolder")
public class CountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
	private List<Makent_model> modelItems;
	int oldposition=1;
	PaymentCountryList paymentCountryList;

	private boolean[] mIsItemClicked;


	public CountryListAdapter(Header header, Context context, List<Makent_model> Items) {
		this.header = header;
		this.context = context;
		this.modelItems = Items;
		mIsItemClicked = new boolean[241];
		paymentCountryList=new PaymentCountryList();
		localSharedPreferences=new LocalSharedPreferences(context);
		System.out.println("modelItems"+mIsItemClicked.length);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(context);
		System.out.println("View Type"+viewType);
		//mIsItemClicked = new boolean[modelItems.size()];
		//System.out.println("modelItems1"+mIsItemClicked.length);
		if(viewType == TYPE_HEADER)
		{
			return new HeaderHolder(inflater.inflate(R.layout.currency_header, parent, false));
		}
		if(viewType==TYPE_Explore){
			return new MovieHolder(inflater.inflate(R.layout.payment_country_list,parent,false));
		}else{
			return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
		}
		//throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

		/*if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
		isLoading = true;
		loadMoreListener.onLoadMore();
		}*/

		if(holder instanceof HeaderHolder)
		{
			System.out.println("Header Holder position");
			HeaderHolder VHheader = (HeaderHolder)holder;
			//VHheader.txtTitle.setText("Siva");
		}
		else if(holder instanceof MovieHolder)
		{
			System.out.println("BedTypeHolder position"+(position-1));
			final Makent_model currentItem = getItem(position-1);

		final 	MovieHolder mainholder=(MovieHolder)holder;


			mainholder.countryname.setText(currentItem.getCountryName());


			if("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
				mainholder.countryname.setGravity(Gravity.END);
			}


			mainholder.countryname.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					for (int i = 0; i < mIsItemClicked.length; i++) {

						if(i!=position) {
							mIsItemClicked[i] = false;
							//mainholder.countryname.setTextColor(context.getResources().getColor(R.color.material_grey_850));
						}else
						{  // mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
							mIsItemClicked[position]=true;
						//	paymentCountryList.listView.smoothScrollToPosition(position);
						}

					}
					mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
					localSharedPreferences.saveSharedPreferences(Constants.CountryCode,currentItem.getCountryCode().toString());
					localSharedPreferences.saveSharedPreferences(Constants.CountyrNameBooking,currentItem.getCountryName());


					notifyItemChanged(position);
					notifyItemChanged(oldposition);
					oldposition=position;
					//notifyDataChanged();

				}
			});

			String country=localSharedPreferences.getSharedPreferences(Constants.CountyrNameBooking);
			if(currentItem.getCountryName().equals(country))
			{
				mIsItemClicked[position]=true;
				oldposition=position;
				//notifyItemChanged(position);
				//paymentCountryList.listView.smoothScrollToPosition(position);
			}

			//for (int i = 0; i < mIsItemClicked.length; i++) {
				System.out.println("mIsItemClicked value "+mIsItemClicked[position]+" position "+position);
				if (mIsItemClicked[position])
					mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
				else
					mainholder.countryname.setTextColor(context.getResources().getColor(R.color.material_grey_850));
			//}
			//((BedTypeHolder)holder).bindData(currentItem,position-1);
		}

		/*if(getItemViewType(position)==TYPE_Explore){
		((BedTypeHolder)holder).bindData(modelItems.get(position));
		}*/
		//No else part needed as load holder doesn't bind any data
	}


	private Makent_model getItem(int position)
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

		void bindData(Makent_model movieModel,final int position){

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



	/* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
	public void notifyDataChanged(){
		notifyDataSetChanged();
	}


}