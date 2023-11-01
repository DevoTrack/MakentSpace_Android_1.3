package com.makent.trioangle.newhome.makentspacehome.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.newhome.makentspacehome.Model.Filter_Model;

import java.util.List;

public class AmenityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_Explore = 1;
    public final int TYPE_LOAD = 2;
    private static final int TYPE_HEADER = 0;

    Header header;
    public static LocalSharedPreferences localSharedPreferences;
    static Context context;

    protected static final String TAG = null;
    private List<Filter_Model> filter_modelList;
    static  int val = 0;

    private boolean[] mIsItemClicked;
    int oldposition=1;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public AmenityAdapter(Header header, Context context, List<Filter_Model> Items, int val) {
        this.header = header;
        this.context = context;
        this.filter_modelList = Items;
        this.val = val;
        localSharedPreferences = new LocalSharedPreferences(context);
        mIsItemClicked = new boolean[31];
        System.out.println("modelItems" + mIsItemClicked.length);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        System.out.println("View Type" + viewType);

        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.currency_header, parent, false));
        }
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.payment_country_list, parent, false));
        }
        else
        {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            System.out.println("Header Holder position");
            HeaderHolder VHheader = (HeaderHolder) holder;
        } else if (holder instanceof MovieHolder) {
            System.out.println("BedTypeHolder position" + (position - 1));
            final Filter_Model currentItem = getItem(position - 1);
            final MovieHolder mainholder = (MovieHolder) holder;
            mainholder.countryname.setText(currentItem.getName());
            if (context.getResources().getString(R.string.layout_direction).equals("1")) {
                mainholder.countryname.setGravity(Gravity.END);
            }

            if(val ==1){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
            if(val == 2){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
            if(val ==3){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
            if(val == 4){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
            if(val == 5){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
            if(val == 6){
                mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
            }
           if(val == 7){
                String eventName = localSharedPreferences.getSharedPreferences(Constants.EventName);
                if(eventName !=null) {
                    if (currentItem.getName().equals(eventName)) {
                        mIsItemClicked[position] = true;
                        oldposition = position;
                    }
                }
                if (mIsItemClicked[position]) {
                    mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
                }
                else{
                    mainholder.countryname.setTextColor(context.getResources().getColor(R.color.material_grey_850));
                }


            }

            //mainholder.countryname.setTextColor(currentItem.getAmenitystate() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));

            mainholder.country_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Position" + position + "Country name" + mainholder.countryname.getText().toString());
                    if(val == 7){
                        for(int i =0;i<mIsItemClicked.length;i++){
                            if(i != position) {
                                mIsItemClicked[i] = false;
                            }else{
                                mIsItemClicked[i] = true;
                            }

                        }
                        if(currentItem.getName() != null) {
                            localSharedPreferences.saveSharedPreferences(Constants.EventName, mainholder.countryname.getText().toString());
                            String val = String.valueOf(mainholder.getAdapterPosition());
                            localSharedPreferences.saveSharedPreferences(Constants.FilterEventType,val);
                            System.out.println("Adapter Position " +mainholder.getAdapterPosition());
                        }
                        mainholder.countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
                        notifyItemChanged(position);
                        notifyItemChanged(oldposition);
                        oldposition=position;
                    }
                    if(val == 1){
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                    }if(val == 2){
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                    }
                   if(val == 3){
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                    }
                    if(val == 4){
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                    }
                   if(val == 5){
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                    }
                   if(val == 6) {
                        currentItem.setStatus(!currentItem.getStatus());
                        mainholder.countryname.setTextColor(currentItem.getStatus() ? context.getResources().getColor(R.color.title_text_color) : context.getResources().getColor(R.color.material_grey_850));
                        //localSharedPreferences.saveSharedPreferences(Constants.SpaceName,currentItem.getName());
                    }

                }
            });

        }
    }

    private Filter_Model getItem(int position) {
        return filter_modelList.get(position);
    }

    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_Explore;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public int getItemCount() {
        return filter_modelList.size();
    }


    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.header);
            switch (val){
                case 1:
                this.txtTitle.setText(context.getResources().getString(R.string.select_amenities));
                break;
                case 2:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_services));
                    break;
                case 3:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_styles));
                    break;
                case 4:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_special));
                    break;
                case 5:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_space));
                    break;
                case 6:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_space_type));
                    break;
                case  7:
                    this.txtTitle.setText(context.getResources().getString(R.string.select_event_type));
                    break;
            }

            this.txtTitle.setTextSize(context.getResources().getDimension(R.dimen.smallb));
            this.txtTitle.setTextColor(context.getResources().getColor(R.color.title_text_color));
        }
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        TextView countryname;
        RelativeLayout country_layout;

        public MovieHolder(View itemView) {
            super(itemView);
            countryname = (TextView) itemView.findViewById(R.id.countryname);
            country_layout = (RelativeLayout) itemView.findViewById(R.id.country_layout);
        }

        void bindData(Filter_Model movieModel, final int position) {
            countryname.setText(movieModel.getName());


            country_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    System.out.println("Position" + position + "Country name" + countryname.getText().toString());

                    countryname.setTextColor(context.getResources().getColor(R.color.title_text_color));
                    //	Toast.makeText(context,"Position"+position+"Country name"+countryname.getText().toString(),Toast.LENGTH_SHORT).show();
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

