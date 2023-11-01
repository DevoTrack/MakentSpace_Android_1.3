package com.makent.trioangle.adapter;

/**
 * @package com.makent.trioangle
 * @subpackage adapter
 * @category WishListAdapter
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.WishListObjects;
import com.makent.trioangle.newhome.views.NewHomeActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.WishListChooseDialog;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

@SuppressLint("ViewHolder")
public class WishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    protected boolean isInternetAvailable;
    protected static final String TAG = null;
    static Activity activity;
    private LayoutInflater inflater;
    private List<Makent_model> modelItems;

    String  roomid, listid;

    WishListChooseDialog WishListChooseDialog;
    ProgressBar explore_progress;


    LocalSharedPreferences localSharedPreferences;
    private String roomExp;
    private String isFrom;
    private int position;

    public WishListAdapter(WishListChooseDialog WishListChooseDialog, Activity activity, Context context, List<Makent_model> Items,String isFrom,int position) {
        this.context = context;
        this.modelItems = Items;
        this.activity = activity;
        this.isFrom = isFrom;
        this.position = position;
        this.WishListChooseDialog = WishListChooseDialog;
        localSharedPreferences = new LocalSharedPreferences(context);

        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.wishlist_itemdesign, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_Explore) {
            ((MovieHolder) holder).bindData(modelItems.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if (modelItems.get(position).getWishlistName().equals("load")) {
            return TYPE_LOAD;
        } else {
            return TYPE_Explore;
        }
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    /* VIEW HOLDERS */

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView wishlistImage, wishlist_image1, wishlist_image2, wishlist_image3;
        TextView wishlist_name, wishlist_homecount;
        LinearLayout wishlistimages;

        public MovieHolder(View itemView) {
            super(itemView);
            wishlistimages = (LinearLayout) itemView.findViewById(R.id.wishlistimages);
            wishlistImage = (ImageView) itemView.findViewById(R.id.wishlist_image);
            wishlist_image1 = (ImageView) itemView.findViewById(R.id.wishlist_image1);
            wishlist_image2 = (ImageView) itemView.findViewById(R.id.wishlist_image2);
            wishlist_image3 = (ImageView) itemView.findViewById(R.id.wishlist_image3);
            wishlist_name = (TextView) itemView.findViewById(R.id.wishlist_name);
            wishlist_homecount = (TextView) itemView.findViewById(R.id.wishlist_homecount);

        }

        void bindData(final Makent_model movieModel) {

            String listname = movieModel.getWishlistName().toString().replace("%20"," ");
            wishlist_name.setText(listname);
            JSONArray whishlistimages = null;
            try {
                whishlistimages = new JSONArray(movieModel.getWishlistImage());
                if (movieModel.getWishlistImage() == null) {
                    wishlistimages.setVisibility(View.GONE);
                    wishlistImage.setVisibility(View.VISIBLE);
                    wishlist_homecount.setText(context.getResources().getString(R.string.nothing));
                } else {

                    if (whishlistimages.length() <= 2) {
                        wishlistImage.setVisibility(View.VISIBLE);
                        wishlistimages.setVisibility(View.GONE);
                        if (whishlistimages.length()==0){
                            wishlistImage.setImageDrawable(context.getResources().getDrawable(R.drawable.default_photo_bg));
                        }else {
                            Glide.with(context.getApplicationContext()).load(whishlistimages.get(0).toString())

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
                        Glide.with(context.getApplicationContext())
                                .load(whishlistimages.get(0).toString())

                                .into(new DrawableImageViewTarget(wishlist_image1) {
                                    @Override
                                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        super.onResourceReady(resource, transition);
                                    }
                                });
                        Glide.with(context.getApplicationContext())
                                .load(whishlistimages.get(1).toString())

                                .into(new DrawableImageViewTarget(wishlist_image2) {
                                    @Override
                                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        super.onResourceReady(resource, transition);
                                    }
                                });
                        Glide.with(context.getApplicationContext())
                                .load(whishlistimages.get(2).toString())

                                .into(new DrawableImageViewTarget(wishlist_image3) {
                                    @Override
                                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        super.onResourceReady(resource, transition);
                                    }
                                });
                    }
                    if (whishlistimages.length() > 1) {
                        wishlist_homecount.setText(whishlistimages.length() + " " + context.getResources().getString(R.string.homes));
                    } else {
                        wishlist_homecount.setText(whishlistimages.length() + " " + context.getResources().getString(R.string.home));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String wishlisttype = localSharedPreferences.getSharedPreferences(Constants.WishlistType);
                    if(wishlisttype==null||wishlisttype.equals("")){
                        wishlisttype = "home";
                    }
                    localSharedPreferences.saveSharedPreferences(Constants.WishlistType,"");
                    localSharedPreferences.saveSharedPreferences(Constants.Reload,"reload");
                    Intent intent = activity.getIntent();
                    activity.overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //((Activity) context).finish();
                    if (activity instanceof NewHomeActivity) {
                        Intent x = new Intent(activity, HomeActivity.class);
                        activity.overridePendingTransition(0, 0);
                        x.putExtra("tabsaved", 0);
                        x.putExtra("type",wishlisttype);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(x);
                        ((Activity) context).finish();
                    } else {
                        WishListObjects wishListObjects=new WishListObjects();
                        wishListObjects.setWishListFrom(isFrom);
                        wishListObjects.setWishListPosition(position);
                        EventBus.getDefault().post(wishListObjects);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.overridePendingTransition(0, 0);
                        WishListChooseDialog.dismiss();
                        //activity.dismissDialog(0);
                        //context.startActivity(intent);
                        //((Activity) context).finish();
                    }

                    listid = movieModel.getWishlistId();
                    isInternetAvailable = getNetworkState().isConnectingToInternet();
                    if (isInternetAvailable) {
                        addWishList();
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
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


    /**
     * @Reference Get amenities list from server
     */
    public void addWishList(){
        roomid = localSharedPreferences.getSharedPreferences(Constants.WishlistRoomId);
        apiService.addWishList(addWishlistparams()).enqueue(new RequestCallback(this));
    }

    private LinkedHashMap<String ,String > addWishlistparams(){
        LinkedHashMap<String,String> addWishlist =new LinkedHashMap<>();
        addWishlist.put("list_id",listid);
        addWishlist.put("space_id",roomid);
        addWishlist.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return addWishlist;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
            return;
        }

        if (jsonResp.isSuccess()) {
            boolean isWishlistFromMap = localSharedPreferences.getSharedPreferencesBool(Constants.MapFilterWishlist);

            if(isWishlistFromMap){
                EventBus.getDefault().post(localSharedPreferences.getSharedPreferences(Constants.MapFilterWishlistId));
            }


        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
        }
    }



    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        return connectionDetector;
    }
}