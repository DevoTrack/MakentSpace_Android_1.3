package com.makent.trioangle.adapter;

/**
 * @package com.makent.trioangle
 * @subpackage adapter
 * @category SavedWishListAdapter
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.wishlist.WishListData;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.WishListDetailsActivity;
import com.makent.trioangle.util.ConnectionDetector;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class SavedWishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 1;
    public final int TYPE_LOAD = 2;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    static Context context;
    Header header;


    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    static Activity activity;
    private LayoutInflater inflater;
    private ArrayList<WishListData> modelItems;


    LocalSharedPreferences localSharedPreferences;

    protected boolean isInternetAvailable;


    public SavedWishListAdapter(Header header, Activity activity, Context context, ArrayList<WishListData> Items) {
        this.context = context;
        this.modelItems = Items;
        this.header = header;
        this.activity = activity;
        localSharedPreferences = new LocalSharedPreferences(context);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        System.out.println("Activity B" + activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        System.out.println("View Type" + viewType);
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.wishlist_itemdesignsavedheader, parent, false));
        }
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.wishlist_itemdesignsaved, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        System.out.println("Holder name" + holder);
        System.out.println("Holder position" + position);

        if (holder instanceof HeaderHolder) {
            System.out.println("Header Holder position");
            HeaderHolder VHheader = (HeaderHolder) holder;
            //VHheader.txtTitle.setText("Siva");
        } else if (holder instanceof MovieHolder) {

            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }

            if (getItemViewType(position) == TYPE_Explore) {
                ((MovieHolder) holder).bindData(modelItems.get(position - 1));
            }
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        /*if(modelItems.get(position).getWishlistImage().equals("load")){
            return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}*/
        if (isPositionHeader(position)) return TYPE_HEADER;
        return TYPE_ITEM;
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        return connectionDetector;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return modelItems.size() + 1;
    }

    /* VIEW HOLDERS */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.review_titlename);
        }
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView wishlistImage, wishlist_image1, wishlist_image2, wishlist_image3;
        TextView wishlist_name, wishlist_homecount,wishlist_expcount;
        String listname;
        String expCount="",homeCount="";
        LinearLayout wishlistimages;
        RelativeLayout host_exp_layout;

        public MovieHolder(View itemView) {
            super(itemView);
            wishlistimages = (LinearLayout) itemView.findViewById(R.id.wishlistimages);
            wishlistImage = (ImageView) itemView.findViewById(R.id.wishlist_image);
            wishlist_image1 = (ImageView) itemView.findViewById(R.id.wishlist_image1);
            wishlist_image2 = (ImageView) itemView.findViewById(R.id.wishlist_image2);
            wishlist_image3 = (ImageView) itemView.findViewById(R.id.wishlist_image3);
            wishlist_name = (TextView) itemView.findViewById(R.id.wishlist_name);
            wishlist_homecount = (TextView) itemView.findViewById(R.id.wishlist_homecount);
            wishlist_expcount = (TextView) itemView.findViewById(R.id.wishlist_expcount);
            host_exp_layout =(RelativeLayout)itemView.findViewById(R.id.host_exp_layout);
        }

        void bindData(final WishListData movieModel) {
            System.out.println("InsideAdapt" + movieModel.getListName());
            System.out.println("InsideAdapt" + movieModel.getSpaceThumbImages());
            listname = movieModel.getListName().toString().replace("%20", " ");

            wishlist_name.setText(listname);
            System.out.println("itemsize"+movieModel.getSpaceThumbImages());
            if (movieModel.getSpaceThumbImages() == null) {
                wishlistimages.setVisibility(View.GONE);
                wishlistImage.setVisibility(View.VISIBLE);
                wishlist_homecount.setText(context.getResources().getString(R.string.nothing));
            } else {
                ArrayList images = movieModel.getSpaceThumbImages();
                if (images.size() <= 2) {
                    wishlistImage.setVisibility(View.VISIBLE);
                    wishlistimages.setVisibility(View.GONE);
                    if (images.size()==0){

                        wishlistImage.setImageDrawable(context.getResources().getDrawable(R.drawable.default_photo_bg));

                    }else {
                        Glide.with(context.getApplicationContext()).load(images.get(0))

                                .into(new DrawableImageViewTarget(wishlistImage) {
                                    @Override
                                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        super.onResourceReady(resource, transition);
                                    }
                                });
                    }
                } else {
                    wishlistImage.setVisibility(View.GONE);
                    wishlistimages.setVisibility(View.VISIBLE);
                    Glide.with(context.getApplicationContext()).load(images.get(0))

                            .into(new DrawableImageViewTarget(wishlist_image1) {
                                @Override
                                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                }
                            });
                    Glide.with(context.getApplicationContext()).load(images.get(1))

                            .into(new DrawableImageViewTarget(wishlist_image2) {
                                @Override
                                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                }
                            });
                    Glide.with(context.getApplicationContext()).load(images.get(2))

                            .into(new DrawableImageViewTarget(wishlist_image3) {
                                @Override
                                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                }
                            });
                }

                wishlist_expcount.setVisibility(View.GONE);

                System.out.println("movieModel.getRoomsCount() "+movieModel.getSpaceCount());
                    if(movieModel.getSpaceCount()>1){
                        wishlist_homecount.setVisibility(View.VISIBLE);
                        wishlist_homecount.setText(movieModel.getSpaceCount()+"\t"+context.getString(R.string.cap_spaces));
                    }else if(movieModel.getSpaceCount()>0){
                        wishlist_homecount.setVisibility(View.VISIBLE);
                        wishlist_homecount.setText(movieModel.getSpaceCount()+" "+context.getString(R.string.cap_space));
                    }else{
                        wishlist_homecount.setVisibility(View.GONE);
                    }


            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isInternetAvailable) {
                        if (movieModel.getSpaceThumbImages() != null) {
                            ArrayList images = movieModel.getSpaceThumbImages();
                            System.out.println(" images String.valueOf(images.size() " + String.valueOf(images.size()));
                            localSharedPreferences.saveSharedPreferences(Constants.WishListCount, String.valueOf(images.size()));
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.WishListPrivacy, movieModel.getPrivacy());
                        localSharedPreferences.saveSharedPreferences(Constants.WishListId, movieModel.getListId());
                        localSharedPreferences.saveSharedPreferences(Constants.WishListName, listname);
                        /*Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("tabsaved", 1);
                        activity.overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                        ((Activity) context).finish();*/
                        Intent intent = new Intent(context, WishListDetailsActivity.class);
                        activity.overridePendingTransition(0, 0);
                        context.startActivity(intent);

                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
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
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void clear() {
        modelItems.clear();
        notifyDataSetChanged();
    }

}