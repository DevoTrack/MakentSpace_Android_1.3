package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestMapFragmentActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeListModel;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;

import java.util.ArrayList;
import java.util.Random;

/* ***********************************************************************
This is Map Page Contain to similar map view
**************************************************************************  */
public class MapFragment extends Fragment {

    int page_position;
    LinearLayout layoutItem;
    ImageView map_wishlists;
    TextView map_amount, map_roomdetails, map_reviewrate, imagetitle,room_title,map_instant;
    RatingBar map_room_rate;
    ImageView map_room_image;
    private HomeListModel dealModel;
    LocalSharedPreferences localSharedPreferences;
    Context context;
    int currentposition;
    RelativeLayout mapItemLayout;
    int totalrooms;
    ArrayList<HomeListModel> deals;
    private Detail details;
    String per;

    public String[] mColors = {
            "00b0ff","089de0","086ae0","e0a208","e03c08","e02208","08e088","9b48a4","a44883","a44868"
    };
    public MapFragment() {

    }

    @SuppressLint("ValidFragment")
    public MapFragment(ArrayList<HomeListModel> deals,int position, HomeListModel placemodel, Context context, int cp, int total) {
        this.page_position = position;
        System.out.println(" ******Fragment Page Position *******" + this.page_position);
        this.dealModel = placemodel;
        this.context = context;
        this.currentposition = cp;
        this.totalrooms = total;
        localSharedPreferences = new LocalSharedPreferences(this.context);
        this.deals = deals;
    }

    public HomeListModel getDealModel() {
        return dealModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_search_viewpage, container, false);
        try {
            imagetitle = (TextView) rootView.findViewById(R.id.imagetitleback);
            map_room_image = (ImageView) rootView.findViewById(R.id.map_room_image);
            map_wishlists = (ImageView) rootView.findViewById(R.id.map_wishlists);
            map_amount = (TextView) rootView.findViewById(R.id.map_amount);
            map_roomdetails = (TextView) rootView.findViewById(R.id.map_roomdetails);
            map_reviewrate = (TextView) rootView.findViewById(R.id.map_reviewrate);
            map_room_rate = (RatingBar) rootView.findViewById(R.id.map_room_rate);
            map_instant=(TextView)rootView.findViewById(R.id.tv_instant) ;

            room_title =(TextView)rootView.findViewById(R.id.room_title);

            mapItemLayout=(RelativeLayout)rootView.findViewById(R.id.mapitemviewlayout);

            if(getResources().getString(R.string.layout_direction).equalsIgnoreCase("1"))
            {
                mapItemLayout.setRotationY(180);
            }
            else
            {
                mapItemLayout.setRotationX(180);
            }

            if(getDealModel().getInstantBook().equalsIgnoreCase("No"))
            {
                map_instant.setVisibility(View.GONE);
            }
            else
            {
                map_instant.setVisibility(View.VISIBLE);
            }

            System.out.println(" Map list ivPhoto" + getDealModel().getName());
            Glide.with(this).load(getDealModel().getImage())
                    .into(new DrawableImageViewTarget(map_room_image) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });

            map_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, getDealModel().getSpaceid());
                    localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, getDealModel().getHourly());
                    Intent x = new Intent(getContext(), SpaceDetailActivity.class);
                    startActivity(x);
                }
            });
            String currencysymbol = Html.fromHtml(getDealModel().getCurrencySymbol()).toString();

            map_amount.measure(0, 0);
           // Log.e("map_roomDetails",getDealModel().getSpaceName());
            int width = map_amount.getMeasuredWidth();
            if (!getDealModel().getName().equals("")) {
                //Log.e("entered","true");
               /* SpannableString s = new SpannableString(getDealModel().getSpaceName());
                s.setSpan(new android.text.style.LeadingMarginSpan.Standard(width + 20, 0), 0, 1, 0);
     */           map_roomdetails.setText(getDealModel().getName());
            }
            if(!getDealModel().getSpaceTypename().equals(""))
            {
                room_title.setText(getDealModel().getSpaceTypename()+ " • " + getDealModel().getSize()+getDealModel().getSize_type());
             //   room_title.setText(getDealModel().getSpaceTypename()+ " • " + getDealModel().getCityName());
            }
            room_title.setTextColor(Color.parseColor ("#"+mColors[new Random().nextInt(10)]));

            /*if (getDealModel().getType().equalsIgnoreCase("Experiences")) {
                per = context.getResources().getString(R.string.per_person);
            } else {
                per = context.getResources().getString(R.string.pernight);
            }*/

            per = context.getResources().getString(R.string.per_hour);
            map_amount.setText(currencysymbol + " " + getDealModel().getHourly()+ " " + per);

            if (getDealModel().getInstantBook().equalsIgnoreCase("yes")) {
                map_instant.setVisibility(View.VISIBLE);
            } else {
                map_instant.setVisibility(View.GONE);
            }

            if (Float.valueOf(getDealModel().getRating()) == 0) {
                map_reviewrate.setVisibility(View.GONE);
                map_room_rate.setVisibility(View.GONE);
            } else {
                map_reviewrate.setVisibility(View.VISIBLE);
                map_room_rate.setVisibility(View.VISIBLE);
            }

            if(getDealModel().getReviewscount().equals("1")){
                map_reviewrate.setText(getDealModel().getReviewscount() + " " + getContext().getResources().getString(R.string.review_one));
            }else{
                map_reviewrate.setText(getDealModel().getReviewscount() + " " + getContext().getResources().getString(R.string.review));
            }

            map_room_rate.setRating(Float.valueOf(getDealModel().getRating()));

            if (getDealModel().getIsWishlist().equalsIgnoreCase("Yes")) {
                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
            } else {
                map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
            }
            map_wishlists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("imageName"+getDealModel().getName()+"\n"+getDealModel().getIsWishlist());
                    if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                        Intent home = new Intent(context, MainActivity.class);
                        home.putExtra("isback", 1);
                        startActivity(home);
                    } else {
                        localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload");
                        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, getDealModel().getSpaceid());

                        if (getDealModel().getIsWishlist().equalsIgnoreCase("Yes")) {
                            getDealModel().setIsWishlist("No");
                            map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
                            deleteWishList task = new deleteWishList(context);
                            task.execute();
                        } else {
                            //map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
                            localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, getDealModel().getCityName());
                            localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist, true);
                            localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId, getDealModel().getSpaceid());
                            WishListChooseDialog cdd = new WishListChooseDialog(getActivity(),"Map",page_position);
                            cdd.show();
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

/*    @Override
    public void onChanged(ImageView imageView, String data) {
        if(data.equals("Yes")) {
            map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_red_fill));
        }
    }

    @Override
    public void notChanged(ImageView jsonResp, String data) {
        if(data.equals("No")) {
            map_wishlists.setImageDrawable(getContext().getResources().getDrawable(R.drawable.n2_heart_light_outline));
        }
    }*/
}
