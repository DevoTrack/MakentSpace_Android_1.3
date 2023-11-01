package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    RoomTypeAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.LYS_Property_Type;
import com.makent.trioangle.model.host.Room_Type_model;

import java.util.List;


@SuppressLint("ViewHolder")
public class PropertyTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;

    private static List<Room_Type_model> modelItems;

    static LocalSharedPreferences localSharedPreferences;



    public PropertyTypeAdapter(Context context, List<Room_Type_model> Items) {
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences=new LocalSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_Explore){
            return new MovieHolder(inflater.inflate(R.layout.list_room_type,parent,false));
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
            ((PropertyTypeAdapter.MovieHolder)holder).bindData(modelItems.get(position),position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(modelItems.get(position).type.equals("load")){
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
        TextView room_type_name,room_type_dec;
       ImageView room_type_image;
        RelativeLayout room_type_lay;

        public MovieHolder(View itemView) {
            super(itemView);
            room_type_name=(TextView) itemView.findViewById(R.id.room_type_name);
            room_type_dec=(TextView) itemView.findViewById(R.id.room_type_dec);
            room_type_image=(ImageView) itemView.findViewById(R.id.room_type_image);
            room_type_lay = (RelativeLayout) itemView.findViewById(R.id.room_type_lay);
        }

        void bindData(final Room_Type_model movieModel, int position) {

            System.out.println("inside position " + position);
            if (movieModel.getType().equals("room_type")) {
                room_type_name.setText(movieModel.getName());
                room_type_dec.setText(movieModel.getDesc());

                if (Integer.parseInt(movieModel.getId()) == 1) {
                    room_type_image.setImageResource(R.drawable.icon_entire_home_selected);
                    // room_type_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_entire_home_selected));

                } else if (Integer.parseInt(movieModel.getId()) == 2) {
                    room_type_image.setImageResource(R.drawable.icon_private_room_selected);
                } else if (Integer.parseInt(movieModel.getId()) == 3) {
                    room_type_image.setImageResource(R.drawable.icon_shared_space_selected);
                } else {
                    room_type_image.setImageResource(R.drawable.icon_shared_space_selected);
                }

                room_type_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent lysproperty = new Intent(context, LYS_Property_Type.class);
                        localSharedPreferences.saveSharedPreferences(Constants.ListRoomType, movieModel.getId());
                        localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName, movieModel.getName());
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(context, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                        context.startActivity(lysproperty, bndlanimation);
                    }
                });
            }
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

