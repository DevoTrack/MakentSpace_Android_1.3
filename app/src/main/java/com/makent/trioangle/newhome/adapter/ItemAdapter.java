package com.makent.trioangle.newhome.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;

// ***Experience Start***
// ***Experience End***

import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.travelling.WishListChooseDialog;
import com.makent.trioangle.travelling.deleteWishList;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Detail> details;
    Context context;
    public static LocalSharedPreferences localSharedPreferences;

    public ItemAdapter(ArrayList<Detail> details, Context context) {
        this.details = details;
        this.context = context;
        localSharedPreferences = new LocalSharedPreferences(context);
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.explore_home_list, viewGroup, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int i) {

        Detail detail = details.get(i);

        String id = String.valueOf(detail.getId());
        String price = String.valueOf(detail.getPrice());
        Log.e("CategoriesName","Categories"+detail.getCategoryName());
        if (detail.getCityName() != null) {
            viewHolder.explore_roomdetails.setText(detail.getCategoryName() + " • " + detail.getCityName());
            Log.e("CityName","CityName"+detail.getCityName());
        } else {
            viewHolder.explore_roomdetails.setText(detail.getCategoryName() + " • " + detail.getCountryName());
            Log.e("CountryName","CountryName"+detail.getCountryName());
        }
        viewHolder.explore_name.setText(detail.getName());
        Glide.with(context.getApplicationContext()).load(detail.getPhotoName())

                .into(new DrawableImageViewTarget(viewHolder.explore_room_image) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                    }
                });

        String currencysymbol = Html.fromHtml(detail.getCurrencySymbol()).toString();
        String per = "";
        if (detail.getType().equalsIgnoreCase("Experiences")) {
            per = context.getResources().getString(R.string.per_person);
        } else {
            per = context.getResources().getString(R.string.pernight);
        }
        viewHolder.explore_amount.setText(currencysymbol + " " + price + " " + per);

        if (detail.getIsWishlist().equals("Yes")) {
            viewHolder.explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
        } else {
            viewHolder.explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
        }


        if (detail.getInstantBook().equalsIgnoreCase("yes")){
            viewHolder.tv_instant.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tv_instant.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currencysymbol = Html.fromHtml(detail.getCurrencySymbol()).toString();
                localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencysymbol);

                String currency = detail.getCurrencyCode() + " (" + currencysymbol + ")";
                localSharedPreferences.saveSharedPreferences(Constants.Currency, currency);
                localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, detail.getCurrencyCode());


                // ***Experience Start***


                    // ***Experience End***
                    Log.e("Not Experience","This is not experience");
                    localSharedPreferences.saveSharedPreferences(Constants.ClearFilter, "Yes");
                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, id);
                    localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, price);
                    Intent x = new Intent(context, SpaceDetailActivity.class);
                    //x.putExtra("finish_it", 1);
                    context.startActivity(x);


                    // ***Experience Start***


                // ***Experience End***
                //Toast.makeText(context,"Details Items clicked",Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.explore_wishlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, id);
                    Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1));
                    Drawable icon1 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like), font1).sizeDp(20).colorRes(R.color.title_text_color);
                    Drawable icon2 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like1), font1).sizeDp(20).colorRes(R.color.red_text);

                    if (detail.getType().equalsIgnoreCase("Experiences")) {
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistType, "exp");
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, "Experiences");
                    } else {
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistType, "home");
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, "Homes");
                    }

                    if (detail.getIsWishlist().equals("Yes")) {
                        detail.setIsWishlist("No");
                        viewHolder.explore_wishlists.setImageDrawable(icon1);

                        deleteWishList task = new deleteWishList(context);
                        task.execute();

//                        WishAddRemoveDialog cdd = new WishAddRemoveDialog((Activity) context);
//                        cdd.show();
//                        cdd.dismiss();


                    } else {//	movieModel.setRoomiswishlist("Yes");
                        System.out.println("detail_title " + detail.getType());
                       // localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, detail.getCountryName());
                        localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, detail.getCityName());
                        WishListChooseDialog cdd = new WishListChooseDialog((Activity) context);
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

    @Override
    public int getItemCount() {
        return (details == null) ? 0 : details.size();
    }

    public void updateList(ArrayList<Detail> details) {
        this.details = details;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView explore_room_image;
        ImageView explore_wishlists;
        TextView explore_amount, explore_roomdetails, explore_name,tv_instant;
        RatingBar explore_room_rate;

        public ViewHolder(View view) {
            super(view);
            explore_room_image = (ImageView) itemView.findViewById(R.id.iv_explore_image);
            explore_wishlists = (ImageView) itemView.findViewById(R.id.iv_explore_wishlist);
            explore_amount = (TextView) itemView.findViewById(R.id.tv_room_price);
            explore_roomdetails = (TextView) itemView.findViewById(R.id.tv_room_detail);
            explore_name = (TextView) itemView.findViewById(R.id.tv_room_name);
            tv_instant = (TextView) itemView.findViewById(R.id.tv_instant);
            explore_room_rate = (RatingBar)itemView.findViewById(R.id.rb_rating);
        }
    }

}
