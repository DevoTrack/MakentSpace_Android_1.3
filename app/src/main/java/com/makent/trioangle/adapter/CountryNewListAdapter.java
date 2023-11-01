package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    CountryNewListAdapter
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.profile.SettingActivity;

import java.util.List;

@SuppressLint("ViewHolder")
public class CountryNewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 1;
    public final int TYPE_LOAD = 2;
    private static final int TYPE_HEADER = 0;

    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    private Activity activity;
    private LayoutInflater inflater;
    private static List<Makent_model> modelItems;

    static LocalSharedPreferences localSharedPreferences;
    static SettingActivity st=new SettingActivity();

    public static int lastCheckedPosition = -1;

    Header header;
    TextView explore_amount;
    TextView price;
    TextView address;
    TextView symbol;
    TextView explore_reviewrate;
    ProgressBar explore_progress;
    RatingBar review;

    static  String currency;



    public CountryNewListAdapter(Header header, Context context, List<Makent_model> Items) {
        this.header = header;
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences=new LocalSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == TYPE_HEADER)
        {
            return new HeaderHolder(inflater.inflate(R.layout.currency_header, parent, false));
        }
        if(viewType==TYPE_Explore){
            return new MovieHolder(inflater.inflate(R.layout.payment_country_list,parent,false));
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
        if(modelItems.get(position).getCountryName().equals("load")){
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
        TextView countryname;

        public MovieHolder(View itemView) {
            super(itemView);
            countryname=(TextView) itemView.findViewById(R.id.countryname);
        }

        void bindData(Makent_model movieModel,int position){

            String country=localSharedPreferences.getSharedPreferences(Constants.CountryName);
            if(country!=null&&country.equals(""))
            {
                System.out.println("country Name if"+country);
            }else {
                System.out.println("country Name else"+country);
            }

            countryname.setText(movieModel.getCountryName());
            //currencysymbol.setText(movieModel.getCurrencySymbol());

            if(lastCheckedPosition == position){

                System.out.println("inside if condition ");
                countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
               // radiobutton.setChecked(true);

            }
            else {

                System.out.println("inside else condition ");

                if (movieModel.getCountryName().equals(country)) {
                   // radiobutton.setChecked(true);
                    countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
                }
            }

            countryname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastCheckedPosition = getAdapterPosition();

                   countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
               //    notifyAll();

                }
            });
        }
    }
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


}
