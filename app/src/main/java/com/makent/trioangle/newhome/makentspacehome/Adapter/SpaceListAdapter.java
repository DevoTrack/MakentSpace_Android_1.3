package com.makent.trioangle.newhome.makentspacehome.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeListModel;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.travelling.WishListChooseDialog;
import com.makent.trioangle.travelling.deleteWishList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.makent.trioangle.helper.Constants.isFromHost;

public class SpaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    public static LocalSharedPreferences localSharedPreferences;

    static Context context;
    OnHomeLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    private boolean loading = true;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 1;

    protected static final String TAG = null;
    static Activity activity;
    private ArrayList<HomeListModel> homeListModels;



    public SpaceListAdapter(Activity activity, Context context, ArrayList<HomeListModel> exploreDataModel, NestedScrollView nestedScrollView ) {
        this.context = context;
        this.homeListModels = exploreDataModel;
        this.activity = activity;
        localSharedPreferences = new LocalSharedPreferences(context);

    }
    public void setLoadScroll(RecyclerView recyclerView, NestedScrollView nsvHome){
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        nsvHome.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                View view = (View) nsvHome.getChildAt(nsvHome.getChildCount() - 1);

                int diff = (view.getBottom() - (nsvHome.getHeight() + nsvHome.getScrollY()));

                if(linearLayoutManager.getItemCount() == 0 || linearLayoutManager.getItemCount() >0) {

                    totalItemCount = linearLayoutManager.getItemCount();
                }
                if (diff<0&& Constants.LoadMore){
                    loading=false;
                }

                lastVisibleItem =linearLayoutManager.findLastCompletelyVisibleItemPosition();


                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    Log.e("ONMORE","ONMORE");
                    if (loadMoreListener != null) {
                        loadMoreListener.onHomeLoadMore();
                    }
                    Constants.LoadMore=false;
                    loading = true;
                }
                // }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                System.out.println("linea1 ");
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("linea2 ");
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_Explore){
            return new MovieHolder(inflater.inflate(R.layout.new_explore_exp_list,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==TYPE_Explore){
            ((MovieHolder)holder).bindData(homeListModels.get(position),position);
        }
    }

    public void clearAllDatas(){
        homeListModels.clear();
        notifyDataChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (homeListModels.get(position).getType().equals("load"))
           return TYPE_LOAD;
         else
            return TYPE_Explore;

    }

    @Override
    public int getItemCount() {
        return homeListModels.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder{
        ImageView explore_room_image;
        ImageView explore_wishlists;
        TextView explore_amount, explore_roomdetails, explore_name;
        TextView tv_explore_review_count;
        RatingBar rb_rating;
        RelativeLayout rlt_rating;
        TextView tv_instant;

        public MovieHolder(View itemView) {

            super(itemView);

            explore_room_image = (ImageView) itemView.findViewById(R.id.iv_explore_image);
            explore_wishlists = (ImageView) itemView.findViewById(R.id.iv_explore_wishlist);
            explore_amount = (TextView) itemView.findViewById(R.id.tv_room_price);
            explore_roomdetails = (TextView) itemView.findViewById(R.id.tv_room_detail);
            explore_name = (TextView) itemView.findViewById(R.id.tv_room_name);
            tv_explore_review_count = (TextView) itemView.findViewById(R.id.tv_explore_review_count);
            rlt_rating = itemView.findViewById(R.id.rlt_rating);
            rb_rating = itemView.findViewById(R.id.rb_rating);
            tv_instant = (TextView) itemView.findViewById(R.id.tv_instant);
        }

        void bindData(final HomeListModel homeListModel, int position){
            System.out.println("experience images "+homeListModel.getImage());


            Picasso.with(context.getApplicationContext())
                    .load(homeListModel.getImage())
                    .fit()
                    .centerCrop()
                    .into(explore_room_image);


            String currencySymbol=homeListModel.getCurrencySymbol();

            localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencySymbol);
            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,homeListModel.getCurrencyCode());

            explore_amount.setText(currencySymbol + " " + homeListModel.getHourly() + " "+context.getResources().getString(R.string.per_hour));

            explore_roomdetails.setText(homeListModel.getSpaceTypename() + " • " + homeListModel.getSize() + " " + homeListModel.getSize_type());
            explore_name.setText(homeListModel.getName());


                if (Float.valueOf(homeListModel.getRating()) == 0) {
                    rlt_rating.setVisibility(View.GONE);
                } else {
                    rlt_rating.setVisibility(View.VISIBLE);

                    rb_rating.setRating(Float.valueOf(homeListModel.getRating()));
                }

                if (homeListModel.getReviewscount().equals("1")) {
                    tv_explore_review_count.setText(homeListModel.getReviewscount() + context.getResources().getString(R.string.review_one));
                } else {
                    tv_explore_review_count.setText(homeListModel.getReviewscount() + context.getResources().getString(R.string.review));
                }

            explore_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,homeListModel.getSpaceid());
                    Intent x = new Intent(context, SpaceDetailActivity.class);
                    x.putExtra(isFromHost,false);
                    context.startActivity(x);
                }
            });




                if (homeListModel.getIsWishlist().equals("Yes")) {
                    explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
                } else {
                    explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
                }



                if (homeListModel.getInstantBook().equals("Yes")) {
                    tv_instant.setVisibility(View.VISIBLE);
                } else {
                    tv_instant.setVisibility(View.GONE);
                }


            explore_wishlists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId,homeListModel.getSpaceid());
                        Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1) );
                        Drawable icon1 = new FontIconDrawable(context,context.getResources().getString(R.string.f1like),font1)
                                .sizeDp(20)
                                .colorRes(R.color.title_text_color);


                        localSharedPreferences.saveSharedPreferences(Constants.WishlistType, "exp");

                        if(homeListModel.getIsWishlist().equals("Yes")) {
                            homeListModel.setIsWishlist("No");
                            explore_wishlists.setImageDrawable(icon1);
                            deleteWishList task = new deleteWishList(context);
                            task.execute();
                        }else
                        {
                            localSharedPreferences.saveSharedPreferences(Constants.WishListAddress,homeListModel.getCityName());
                            WishListChooseDialog cdd=new WishListChooseDialog(activity);
                            cdd.show();
                        }
                    } else {
                        Intent home = new Intent(context, MainActivity.class);
                        home.putExtra("isback", 1);
                        context.startActivity(home);
                    }
                }
            });
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder{
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
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }
    public interface OnHomeLoadMoreListener{
        void onHomeLoadMore();
    }
    public void setLoadMoreListener(OnHomeLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void clear() {
        homeListModels.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<HomeListModel> list) {
        homeListModels.addAll(list);
        notifyDataSetChanged();
    }


}
