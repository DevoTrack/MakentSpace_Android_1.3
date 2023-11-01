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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.LYS_Property_Type;
import com.makent.trioangle.model.host.Room_Type_model;
import com.makent.trioangle.travelling.FilterActivity;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewHolder")
public class RoomTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    static List<String> searchroomtype = new ArrayList<String>();
    static String filterroomtypes;
    static Context context;
    OnLoadMoreListener loadMoreListener;
    static OnDataChangeListener mOnDataChangeListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;

    private static List<Room_Type_model> modelItems;

    static LocalSharedPreferences localSharedPreferences;
    static FilterActivity filterActivity;



    public RoomTypeAdapter(Context context, List<Room_Type_model> Items) {
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
            ((RoomTypeAdapter.MovieHolder)holder).bindData(modelItems.get(position),position);
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
        TextView room_type_name,room_type_dec,room_type_shared;
       ImageView room_type_image,room_type_tick;
        RelativeLayout room_type_lay;

        public MovieHolder(View itemView) {
            super(itemView);
            room_type_name=(TextView) itemView.findViewById(R.id.room_type_name);
            room_type_dec=(TextView) itemView.findViewById(R.id.room_type_dec);
            room_type_shared=(TextView) itemView.findViewById(R.id.room_type_shared);
            room_type_image=(ImageView) itemView.findViewById(R.id.room_type_image);
            room_type_tick=(ImageView) itemView.findViewById(R.id.room_type_tick);
            room_type_lay = (RelativeLayout) itemView.findViewById(R.id.room_type_lay);
        }

        void bindData(final Room_Type_model movieModel, int position) {

            System.out.println("inside position " + position);
            if (movieModel.getType().equals("room_type")) {

                room_type_name.setText(movieModel.getName());
                if(movieModel.getDesc().equals(""))
                {
                    room_type_dec.setVisibility(View.GONE);
                }else{
                    room_type_dec.setVisibility(View.VISIBLE);
                }
                room_type_dec.setText(movieModel.getDesc());

                if(movieModel.getIsShared().equals("Yes"))
                    room_type_shared.setVisibility(View.VISIBLE);
                else
                    room_type_shared.setVisibility(View.GONE);

               /* if (Integer.parseInt(movieModel.getId()) == 1) {
                    room_type_image.setImageResource(R.drawable.icon_entire_home_selected);
                    // room_type_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_entire_home_selected));

                } else if (Integer.parseInt(movieModel.getId()) == 2) {
                    room_type_image.setImageResource(R.drawable.icon_private_room_selected);
                } else if (Integer.parseInt(movieModel.getId()) == 3) {
                    room_type_image.setImageResource(R.drawable.icon_shared_space_selected);
                } else {
                    room_type_image.setImageResource(R.drawable.icon_entire_home_selected);
                }*/

                Glide.with(context).load(movieModel.getImage()).into(room_type_image);

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
            }else  if(movieModel.getType().equals("filter")){
                System.out.println("Search list id"+movieModel.getId());
                System.out.println("Search list name"+movieModel.getName());
                System.out.println("Search list desc"+movieModel.getDesc());
                System.out.println("Search list type"+movieModel.type);
                System.out.println("Search list listtype"+movieModel.getType());
                System.out.println("Search list selected"+movieModel.getIsSelected());
                room_type_name.setText(movieModel.getName());
                room_type_dec.setText(movieModel.getDesc());
                room_type_dec.setVisibility(View.GONE);

                if(movieModel.getIsShared().equals("Yes"))
                    room_type_shared.setVisibility(View.VISIBLE);
                else
                    room_type_shared.setVisibility(View.GONE);

                Glide.with(context).load(movieModel.getImage()).into(room_type_image);
                /*if (Integer.parseInt(movieModel.getId()) == 1) {
                    room_type_image.setImageResource(R.drawable.icon_entire_home_selected);
                    // room_type_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_entire_home_selected));

                } else if (Integer.parseInt(movieModel.getId()) == 2) {
                    room_type_image.setImageResource(R.drawable.icon_private_room_selected);
                } else if (Integer.parseInt(movieModel.getId()) == 3) {
                    room_type_image.setImageResource(R.drawable.icon_shared_space_selected);
                } else {
                    room_type_image.setImageResource(R.drawable.icon_entire_home_selected);
                }*/

               if(movieModel.getIsSelected())
                {
                    room_type_tick.setVisibility(View.VISIBLE);
                }else
                {
                    room_type_tick.setVisibility(View.GONE);
                }
                room_type_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        movieModel.setIsSelected(!movieModel.getIsSelected());
                        if(movieModel.getIsSelected())
                        {
                            room_type_tick.setVisibility(View.VISIBLE);
                        }else
                        {
                            room_type_tick.setVisibility(View.GONE);
                        }

                        for(int i=0;i<modelItems.size();i++) {
                            String roomtypeid = modelItems.get(i).getId();
                            String roomtypename = modelItems.get(i).getName();
                            if (modelItems.get(i).getIsSelected()) {
                                System.out.println("searchroomitem Item=" + searchroomtype);
                                if (searchroomtype != null) {
                                    if (!searchroomtype.contains(roomtypeid)) {
                                        searchroomtype.add(roomtypeid);
                                    }
                                } else {
                                    searchroomtype.add(roomtypeid);
                                }

                                System.out.println("Selected searchroomtype Selected Item=" + searchroomtype + " NAme  " + roomtypename);
                            } else {
                                System.out.println("searchroomtype Item=" + searchroomtype);
                                if (searchroomtype != null) {
                                    if (searchroomtype.contains(roomtypeid)) {
                                        searchroomtype.remove(roomtypeid);
                                    }
                                }

                            }
                            filterroomtypes = null;
                            for (String s : searchroomtype) {
                                if (filterroomtypes != null) {
                                    filterroomtypes = filterroomtypes + "," + s;
                                    //filteramenities += s + ",";
                                } else {
                                    filterroomtypes = s;
                                }
                                System.out.println("FilterRoomTypes Item=" + filterroomtypes);
                            }
                            //searchamenities= (Arrays.asList(filteramenities));
                            localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, filterroomtypes);
                            if(mOnDataChangeListener != null){
                                mOnDataChangeListener.onDataChanged(filterroomtypes);
                            }

                        }
                        }
                });
            }
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemloading);
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);
            Glide.with(context).load(R.drawable.dot_loading).into(imageViewTarget);
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


    public interface OnDataChangeListener{
        public void onDataChanged(String size);
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }
}

