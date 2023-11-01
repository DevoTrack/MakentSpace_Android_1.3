package com.makent.trioangle.newhome.map;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowAllMapFragment extends Fragment {

    // private static ImageLoader mImageLoader;
    int page_position;
    LinearLayout layoutItem;
    ImageView map_wishlists;
    TextView map_amount,map_roomdetails,map_reviewrate,imagetitle,room_title;
    RatingBar map_room_rate;
    ImageView map_room_image;
    TextView tv_instant;
    private Detail detail;
    LocalSharedPreferences localSharedPreferences;
    Context context;
    int currentposition;
    RelativeLayout mapItemLayout;
    int totalrooms;
    String per;
    public String[] mColors = {
            "00b0ff","089de0","086ae0","e0a208","e03c08","e02208","08e088","9b48a4","a44883","a44868"

    };
    public ShowAllMapFragment() {

    }

    @SuppressLint("ValidFragment")
    public ShowAllMapFragment(int position, Detail detail, Context context,int cp,int total) {
        this.page_position = position;
        System.out.println(" ******Fragment Page Position *******"+this.page_position);
        this.detail = detail;
        this.context=context;
        this.currentposition=cp;
        this.totalrooms=total;
        localSharedPreferences=new LocalSharedPreferences(this.context);
    }

    public Detail getDetail() {
        return detail;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_search_viewpage, container, false);
        try {
            imagetitle= (TextView) rootView.findViewById(R.id.imagetitleback);
            map_room_image = (ImageView) rootView.findViewById(R.id.map_room_image);
            map_wishlists=(ImageView)rootView.findViewById(R.id.map_wishlists);
            map_amount=(TextView) rootView.findViewById(R.id.map_amount);
            map_roomdetails=(TextView) rootView.findViewById(R.id.map_roomdetails);
            map_reviewrate=(TextView) rootView.findViewById(R.id.map_reviewrate);
            map_room_rate=(RatingBar) rootView.findViewById(R.id.map_room_rate);
            mapItemLayout=(RelativeLayout)rootView.findViewById(R.id.mapitemviewlayout) ;
            room_title =(TextView)rootView.findViewById(R.id.room_title);
            tv_instant = (TextView)rootView.findViewById(R.id.tv_instant);

            if(getResources().getString(R.string.layout_direction).equalsIgnoreCase("1"))
            {
                mapItemLayout.setRotationY(180);
            }
            else
            {
                mapItemLayout.setRotationX(180);
            }
            //  tvTitle.setText(getDealModel().getLine1());
            System.out.println(" Map guestList ivPhoto"+getDetail().getPhotoName());
            Glide.with(this)
                    .load(getDetail().getPhotoName())

                    .into(new DrawableImageViewTarget(map_room_image) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });

            map_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    // ***Experience Start***

                    // ***Experience End***


                        localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, String.valueOf(getDetail().getId()));
                        localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, String.valueOf(getDetail().getId()));
                        Intent x = new Intent(getContext(), SpaceDetailActivity.class);
                        startActivity(x);

                        // ***Experience Start***

                    // ***Experience End***
                }
            });
            System.out.println("Currency symbol "+getDetail().getCurrencySymbol());
            String currencysymbol= Html.fromHtml(getDetail().getCurrencySymbol()).toString();
            if(getDetail().getType().equalsIgnoreCase("Experiences")){
                per = context.getResources().getString(R.string.per_person);
            }
            else{
                per = context.getResources().getString(R.string.pernight);
            }

            map_amount.setText(currencysymbol+getDetail().getPrice() + " " + per);
            map_amount.measure(0, 0);
            int width=map_amount.getMeasuredWidth();
            if(!getDetail().getName().equals("")) {
               /* SpannableString s = new SpannableString(getDealModel().getExperience_name());
                s.setSpan(new android.text.style.LeadingMarginSpan.Standard(width + 20, 0), 0, 1, 0);
*/                map_roomdetails.setText(getDetail().getName());
            }

            if(Float.valueOf(getDetail().getRating())==0)
            {
                map_reviewrate.setVisibility(View.GONE);
                map_room_rate.setVisibility(View.GONE);
            }else
            {
                map_reviewrate.setVisibility(View.VISIBLE);
                map_room_rate.setVisibility(View.VISIBLE);
            }



                if (getDetail().getInstantBook().equalsIgnoreCase("yes")) {
                    tv_instant.setVisibility(View.VISIBLE);
                } else {
                    tv_instant.setVisibility(View.GONE);
                }


            System.out.println("Rating check Exp "+getDetail().getRating()+" "+getDetail().getRating());

            if(getDetail().getReviewsCount() == 1){
                map_reviewrate.setText(getDetail().getReviewsCount()+" "+getContext().getResources().getString(R.string.review_one));
            }else{
                map_reviewrate.setText(getDetail().getReviewsCount()+" "+getContext().getResources().getString(R.string.review));
            }



            map_room_rate.setRating(Float.valueOf(getDetail().getRating()));

            System.out.println("Category Value"+getDetail().getCategoryName());

            if(!getDetail().getCategoryName().equals(""))
                {
                    room_title.setText(getDetail().getCategoryName()+ " • " + getDetail().getCityName());
                }
            room_title.setTextColor(Color.parseColor ("#"+mColors[new Random().nextInt(10)]));

            if(getDetail().getIsWishlist().equals("Yes")) {
                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
            }else
            {
                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
            }
            map_wishlists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                        Intent home = new Intent(context, MainActivity.class);
                        home.putExtra("isback", 1);
                        startActivity(home);
                    }else {
                        if (getDetail().getType().equalsIgnoreCase("Experiences")) {
                            localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, "Experiences");
                            //localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload");
                            localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, String.valueOf(getDetail().getId()));
                            Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1));
                            Drawable icon1 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like), font1).sizeDp(20).colorRes(R.color.title_text_color);
                            Drawable icon2 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like1), font1).sizeDp(20).colorRes(R.color.red_text);
                            if (getDetail().getIsWishlist().equals("Yes")) {
                                getDetail().setIsWishlist("No");
                                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
                                deleteWishList task = new deleteWishList(context);
                                task.execute();
                            } else {
                                //map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
                                localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, getDetail().getCityName());
                                localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist, true);
                                localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId, String.valueOf(getDetail().getId()));
                                WishListChooseDialog cdd = new WishListChooseDialog(getActivity());
                                cdd.show();
                            }
                        }else {
                            localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, "Rooms");
                            localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload");
                            localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, String.valueOf(getDetail().getId()));
                            Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1));
                            Drawable icon1 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like), font1).sizeDp(20).colorRes(R.color.title_text_color);
                            Drawable icon2 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like1), font1).sizeDp(20).colorRes(R.color.red_text);
                            if (getDetail().getIsWishlist().equals("Yes")) {
                                getDetail().setIsWishlist("No");
                                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
                                deleteWishList task = new deleteWishList(context);
                                task.execute();
                            } else {
                                //map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
                                localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, getDetail().getCityName());
                                localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist, true);
                                localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId, String.valueOf(getDetail().getId()));
                                WishListChooseDialog cdd = new WishListChooseDialog(getActivity());
                                cdd.show();
                            }
                        }
                    }

                }
            });
            return rootView;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PlacesMapFragment:" + e.getStackTrace());
        }

    }

}
