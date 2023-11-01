package com.makent.trioangle.adapter.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter/host
 * @category    ShareAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Makent_model;

import java.util.List;

@SuppressLint("ViewHolder")
public class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    Object[] Items;
    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    private Activity activity;
    private LayoutInflater inflater;
    private static List<Makent_model> modelItems;

    public static int lastCheckedPosition = -1;

    TextView explore_amount;
    TextView price;
    TextView address;
    TextView symbol;
    TextView explore_reviewrate;
    ProgressBar explore_progress;
    RatingBar review;
    static LocalSharedPreferences localSharedPreferences;
    static String roomid;
    static String type;


    public ShareAdapter(Context context, List<Makent_model> Items,String type) {
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences=new LocalSharedPreferences(context);
        this.type = type;

        if(type!=null&&type.equals("experience"))
            roomid=localSharedPreferences.getSharedPreferences(Constants.ExpId);
        else
            roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_Explore){
            return new MovieHolder(inflater.inflate(R.layout.shareitem,parent,false));
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
            ((ShareAdapter.MovieHolder)holder).bindData(modelItems.get(position),position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(modelItems.get(position).getShareName().equals("load")){
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
        TextView sharename;
        ImageView shareicon;
        RelativeLayout shareitem;

        public MovieHolder(View itemView) {
            super(itemView);
            sharename=(TextView) itemView.findViewById(R.id.sharename);
            shareicon = (ImageView) itemView.findViewById(R.id.shareicon);
            shareitem=(RelativeLayout) itemView.findViewById(R.id.shareitem);
        }

        void bindData(final Makent_model movieModel,int position){


           // sharename.setText(((ResolveInfo) items[position]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());

           // shareicon.setImageDrawable(((ResolveInfo) items[position]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));

            System.out.println("inside position "+position);
            sharename.setText(movieModel.getShareName());
            shareicon.setImageDrawable(movieModel.getShareIcon());
            /*Glide.with(context)
                    .load(movieModel.getShareIcon())

                    .into(new GlideDrawableImageViewTarget(shareicon) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            //	explore_progress.setVisibility(View.GONE);
                        }
                    });*/


            shareitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String roomimage;
                    if(type!=null&&type.equals("experience"))
                        roomimage=context.getResources().getString(R.string.sharelinkexp)+""+roomid;
                    else
                        roomimage=context.getResources().getString(R.string.sharelink)+""+roomid;


                    ResolveInfo info = (ResolveInfo) movieModel.getShareItem();
                    if(info.activityInfo.packageName.contains("facebook")) {
                    //    new PostToFacebookDialog(context, body).show();
                        //here u can write your own code to handle the particular social media networking apps.
                      //  Toast.makeText(context, "FaceBook", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        intent.putExtra(Intent.EXTRA_TEXT, roomimage);
                        (context).startActivity(intent);
                    } else {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        intent.putExtra(Intent.EXTRA_TEXT, roomimage);
                        (context).startActivity(intent);
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

