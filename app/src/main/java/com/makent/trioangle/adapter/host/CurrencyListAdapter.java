package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    CurrencyListAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.settings.CurrencyListModel;
import com.makent.trioangle.profile.SettingActivity;

import java.util.ArrayList;

import static com.makent.trioangle.createspace.setprice.SetPriceFragment.alertDialogStores2;
import static com.makent.trioangle.host.LYS_OptionalDetails.alertDialogStores;
import static com.makent.trioangle.profile.SettingActivity.alertDialogStores1;

@SuppressLint("ViewHolder")
public class CurrencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    private Activity activity;
    private LayoutInflater inflater;
    private static ArrayList<CurrencyListModel> modelItems;

    static LocalSharedPreferences localSharedPreferences;
    static SettingActivity st=new SettingActivity();

    public static int lastCheckedPosition = -1;

    static  String currency;

    public CurrencyListAdapter(Context context, ArrayList<CurrencyListModel> Items)
    {
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences=new LocalSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_Explore)
        {
            return new MovieHolder(inflater.inflate(R.layout.currency_item_view,parent,false));
        }
        else
        {
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
        {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        if(getItemViewType(position)==TYPE_Explore)
        {
            ((MovieHolder)holder).bindData(modelItems.get(position),position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position)
    {
        if(modelItems.get(position).getCode().equals("load"))
        {
            return TYPE_LOAD;
        }
        else
        {
            return TYPE_Explore;
        }
    }

    @Override
    public int getItemCount()
    {
        return modelItems.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder{
        TextView currencyname,currencysymbol;
        RadioButton radiobutton;
        RelativeLayout selectcurrency;

        public MovieHolder(View itemView) {
            super(itemView);
            currencyname=(TextView) itemView.findViewById(R.id.currencyname_txt);
            currencysymbol=(TextView) itemView.findViewById(R.id.currencysymbol_txt);
            radiobutton = (RadioButton) itemView.findViewById(R.id.radioButton1);
            selectcurrency=(RelativeLayout) itemView.findViewById(R.id.selectcurrency);
        }

        void bindData(CurrencyListModel movieModel,int position){

            final int isSpaceList=localSharedPreferences.getSharedPreferencesInt(Constants.IsSpaceList);
            String currencycode;
            String  default_currency;
            if(isSpaceList==1)
            {   currencycode = localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
                System.out.println("isSpaceList True"+currencycode);
            }else {
                currencycode = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
                default_currency = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
                System.out.println("isSpaceList False"+currencycode);
            }

            String cs= Html.fromHtml(movieModel.getSymbol()).toString();
            currencyname.setText(movieModel.getCode());
            currencysymbol.setText(cs);

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            context.getResources().getColor(R.color.text_shadow)
                            , context.getResources().getColor(R.color.red_text),
                    }
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radiobutton.setButtonTintList(colorStateList);
            }

            System.out.println("inside else condition ");

            if (movieModel.getCode().equals(currencycode)) {
                radiobutton.setChecked(true);
            }
            else {
                radiobutton.setChecked(false);
            }

            if(lastCheckedPosition == position){

                System.out.println("inside if condition ");
            }
            else {

            }

            selectcurrency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isSpaceList==1)
                    {
                        String cs= Html.fromHtml(currencysymbol.getText().toString()).toString();
                        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,currencyname.getText().toString());
                        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,cs);
                        System.out.println("Update isSpaceList True"+cs);
                    }else {
                        currency=currencyname.getText().toString()+" ("+currencysymbol.getText().toString()+")";
                        localSharedPreferences.saveSharedPreferences(Constants.Currency,currency);
                        localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,currencyname.getText().toString());
                        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol,currencysymbol.getText().toString());
                        System.out.println("Update isSpaceList False"+currencysymbol);
                    }

                    lastCheckedPosition = getAdapterPosition();

                    if(alertDialogStores!=null) {
                        alertDialogStores.cancel();
                    }
                    if(alertDialogStores1!=null) {
                        alertDialogStores1.cancel();
                    }
                    if(alertDialogStores2 != null) {
                        alertDialogStores2.cancel();
                    }

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
