package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestRoomdetailsActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.GuestAccessListAdapter;
import com.makent.trioangle.adapter.ServiceOfferAdapter;
import com.makent.trioangle.adapter.SimilarSearchRecycleListAdapter;
import com.makent.trioangle.adapter.BedArrangementAdapter;
import com.makent.trioangle.adapter.SpaceStyleAdapter;
import com.makent.trioangle.adapter.SpecialFeatureAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.helper.GoogleStaticMapsAPIServices;
import com.makent.trioangle.helper.RoomIdPrice;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.EventTypeModel;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.Service_offer_model;
import com.makent.trioangle.model.Space_Model;
import com.makent.trioangle.model.Space_guestAccess_Model;
import com.makent.trioangle.model.SpecialFeature_Model;
import com.makent.trioangle.model.homeModels.BedRoomBed;
import com.makent.trioangle.model.roomModels.RoomAmenitiesModel;
import com.makent.trioangle.model.roomModels.RoomAvailabilityRules;
import com.makent.trioangle.model.roomModels.RoomLengthOfStay;
import com.makent.trioangle.model.roomModels.RoomResult;
import com.makent.trioangle.model.roomModels.RoomSimilarModel;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.spacedetail.model.SpaceActivityArray;
import com.makent.trioangle.spacedetail.model.SpaceResult;
import com.makent.trioangle.storiesprogressview.StoriesProgressView;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is Roomdetails Page Contain Check Availability
**************************************************************************  */

public class Room_details extends AppCompatActivity implements View.OnClickListener, ServiceListener, StoriesProgressView.StoriesListener ,SpecialFeatureAdapter.onFeatureClickListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public ImageView room_details_back, room_details_share, room_details_wishlist;
    public AppBarLayout appBarLayout;
    public String location_address, location_lat, location_lng, roomtypeusername;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    protected boolean isInternetAvailable;
    RelativeLayout roomdetails_footer;
    Handler handler;
    Runnable runnable;
    ViewPager viewPager;
    ImageView ame1, ame2, ame3, ame4;
    String datecheck, searchcheckin, searchcheckout;
    boolean checkdate = true;
    ImageView room_details_image,room_details_imageBac;
    ImageAdapter adapter;
    String[] roomImgItems;
    String[] blocked_dates;

    String nametoshare;
    String imagetoshare;
    RecyclerView recyclerView;
    List<Makent_model> searchlist;
    SimilarSearchRecycleListAdapter listAdapter;
    BedArrangementAdapter bedArrangementAdapter;
    String TAG = "Map - ";
    Context context;
    int start = 1;
    int isHost;

    RecyclerView rv_guest_access;
    RoomIdPrice roomIdPrice;
    Button requestcheck;
    Boolean toolBarShow = false;
    LinearLayout room_details_contacthost, room_details_availability, room_details_triplength, room_details_cancelpolicy, room_details_houserules, room_details_amenities, room_details_additionalprice, room_details_about,space_details_guestAccess_list;
    NestedScrollView roomsdetailsnestedscrool;
    LocalSharedPreferences localSharedPreferences;


    int bottomheight;
    Dialog_loading mydialog;
    String userid, roomid, other_user_id;
    String instant_book, can_book;
    String[] amenities_id;
    String[] amenities_name;
    String[] amenities_icon;
    String[] amenities_image;
    RecyclerView rvEventType,rvseviceType,special_feature,space_style;
    TextView room_details_title, room_details_isshared, room_details_hometype, hostby, room_details_hostedby, room_details_amount, about_readmore;
    TextView no_of_guest, no_of_rooms, no_of_beds, no_of_bath, about_this_home, room_details_amenities_text,room_details_similarlist_txt;
    ImageView room_details_hostprofile, review_user_image, room_details_dotloader, map;
    TextView review_user_name, review_user_date, review_user_msg, review_user_count, cancellation_policy, room_details_ratecount, mapaddress,about_other_service,service_readmore;
    RatingBar review_user_rate, room_details_rate;
    RelativeLayout roomsdetails_review, similarlisting, rooms_details;
    String room_name, room_type, host_user_name, host_user_image, is_whishlist = null;
    String no_of_guests, no_of_bedss, no_of_bedrooms, no_of_bathrooms;
    String room_detail, room_price, amenitiescount;
    String review_user_names, review_user_images, review_dates, review_messages, review_counts, review_rates;
    String house_rules, cancellation_policys;
    String currency_code, currency_symbol, additionalprice;
    String searchstartdate, searchenddate, checkinout;
    Drawable icon1, icon2, icon3, icon4, icon5;
    String minimum_stay, maximum_stay;  // Room minimum and maximum stay
    RoomResult roomResult;
    String weekend;
    ArrayList<Space_guestAccess_Model> guestAccess_model;
    ArrayList<Service_offer_model> service_offer_model;
    ArrayList<SpecialFeature_Model> specialFeature_model;
    ArrayList<Space_Model> space_model;

    ArrayList<RoomAmenitiesModel> roomAmenitiesModel;
    ArrayList<RoomSimilarModel> roomSimilarModel;
    ArrayList<RoomLengthOfStay> lengthOfStays;
    ArrayList<RoomAvailabilityRules> availabilityRules;
    ArrayList<RoomLengthOfStay> earlybirdrules;
    ArrayList<RoomLengthOfStay> lastminrules;
    private String desc_interaction;
    private String desc_notes;
    private String desc_house_rules;
    private String desc_neighborhood_overview;
    private String desc_getting_around;
    private String desc_space;
    private String desc_access;
    private ProgressDialog pDialog;
    private long pressTime = 0L;
    long limit = 500L;
    private int expImage = 0;

    private StoriesProgressView storiesProgressView;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };


    private ArrayList<ArrayList<BedRoomBed>> bedRoomModel = new ArrayList<>();
    private ArrayList<BedRoomBed> bedTypeList = new ArrayList<>();
    private ArrayList<String> emptyArrayPositions = new ArrayList<>();

    private ArrayList<EventTypeModel> event_Type_Model = new ArrayList<EventTypeModel>();
    private TextView eventtype,sqr_ft;
    private GuestAccessListAdapter guestAccessListAdapter;

    private ServiceOfferAdapter serviceOfferAdapter;
    public LinearLayout other_services;
    private SpecialFeatureAdapter specialFeatureAdapter;
    private SpaceStyleAdapter spaceStyleAdapter;

    private SpaceResult spaceResult;
    private String mSpaceSize,mSpacetype;


    private ArrayList<SpaceActivityArray> spaceActivityArrays = new ArrayList<SpaceActivityArray>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        Log.e("Room Details","Room Details");


        roomIdPrice = new RoomIdPrice();
        room_details_isshared = (TextView) findViewById(R.id.room_details_isshared);
        room_details_title = (TextView) findViewById(R.id.room_details_title);
        room_details_hometype = (TextView) findViewById(R.id.room_details_hometype);
        room_details_hostedby = (TextView) findViewById(R.id.room_details_hostedby);
        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        hostby = (TextView) findViewById(R.id.hostby);

        rvEventType = (RecyclerView) findViewById(R.id.rv_event_type);
        rvseviceType =(RecyclerView)findViewById(R.id.service_offer);
        other_services = (LinearLayout)findViewById(R.id.other_services);
        about_other_service =(TextView)findViewById(R.id.about_other_service);
        service_readmore = (TextView)findViewById(R.id.service_readmore);
        special_feature = (RecyclerView)findViewById(R.id.special_feature);
        space_style =(RecyclerView)findViewById(R.id.space_style);



        room_details_hostprofile = (ImageView) findViewById(R.id.room_details_hostprofile);
        no_of_guest = (TextView) findViewById(R.id.no_of_guest);
        eventtype = (TextView) findViewById(R.id.eventType);
        sqr_ft = (TextView) findViewById(R.id.sqr_ft);
  //      no_of_bath = (TextView) findViewById(R.id.no_of_bath);

        about_this_home = (TextView) findViewById(R.id.about_this_home);
        about_readmore = (TextView) findViewById(R.id.about_readmore);
        room_details_amenities_text = (TextView) findViewById(R.id.room_details_amenities_text);

     //   guestAcesslist=(ListView)findViewById(R.id.guestaccess_list) ;
     //   space_details_guestAccess_list=(LinearLayout)findViewById(R.id.room_details_guest_access_list);
      //  guestaccess_read=(TextView)findViewById(R.id.guestaccess_read);



        room_details_hometype = (TextView) findViewById(R.id.room_details_hometype);
        room_details_hostedby = (TextView) findViewById(R.id.room_details_hostedby);

        review_user_image = (ImageView) findViewById(R.id.review_user_image);
        review_user_name = (TextView) findViewById(R.id.review_user_name);
        review_user_date = (TextView) findViewById(R.id.review_user_date);
        review_user_msg = (TextView) findViewById(R.id.review_user_msg);
        review_user_msg.setMovementMethod(new ScrollingMovementMethod());
        review_user_count = (TextView) findViewById(R.id.review_user_count);
        room_details_ratecount = (TextView) findViewById(R.id.room_details_ratecount);
        review_user_rate = (RatingBar) findViewById(R.id.review_user_rate);
        room_details_rate = (RatingBar) findViewById(R.id.room_details_rate);
        roomsdetails_review = (RelativeLayout) findViewById(R.id.roomsdetails_review);
        similarlisting = (RelativeLayout) findViewById(R.id.similarlisting);
        cancellation_policy = (TextView) findViewById(R.id.cancellation_policy);
        room_details_amount = (TextView) findViewById(R.id.room_details_amount);
        room_details_similarlist_txt = (TextView)findViewById(R.id.room_details_similarlist_txt);
        rooms_details = (RelativeLayout) findViewById(R.id.rooms_details);
        room_details_dotloader = (ImageView) findViewById(R.id.room_details_dotloader);
        roomdetails_footer = (RelativeLayout) findViewById(R.id.roomdetails_footer);

        rv_guest_access=(RecyclerView)findViewById(R.id.guest_access);

        ame1 =  findViewById(R.id.ame1);
        ame2 =  findViewById(R.id.ame2);
        ame3 =  findViewById(R.id.ame3);
        ame4 =  findViewById(R.id.ame4);

        map = (ImageView) findViewById(R.id.map);
        mapaddress = (TextView) findViewById(R.id.mapaddress);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(room_details_dotloader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        rooms_details.setVisibility(View.GONE);
        roomdetails_footer.setVisibility(View.GONE);
        localSharedPreferences = new LocalSharedPreferences(this);
        isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        room_details_back = (ImageView) findViewById(R.id.room_details_back);
        commonMethods.rotateArrow(room_details_back,this);

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);


        room_details_share = (ImageView) findViewById(R.id.room_details_share);
        room_details_wishlist = (ImageView) findViewById(R.id.room_details_wishlist);
        roomsdetailsnestedscrool = (NestedScrollView) findViewById(R.id.roomsdetailsnestedscrool);

        Typeface font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent1));
        icon1 = new FontIconDrawable(Room_details.this, getResources().getString(R.string.f1like), font1).sizeDp(25).colorRes(R.color.text_black);
        icon2 = new FontIconDrawable(Room_details.this, getResources().getString(R.string.f1like), font1).sizeDp(25).colorRes(R.color.title_text_color);
        icon3 = new FontIconDrawable(Room_details.this, getResources().getString(R.string.f1share), font1).sizeDp(25).colorRes(R.color.title_text_color);
        icon4 = new FontIconDrawable(Room_details.this, getResources().getString(R.string.f1share), font1).sizeDp(25).colorRes(R.color.text_black);
        icon5 = new FontIconDrawable(Room_details.this, getResources().getString(R.string.f1like1), font1).sizeDp(25).colorRes(R.color.red_text);

        room_details_share.setImageDrawable(icon3);
        room_details_wishlist.setImageDrawable(icon2);

        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //   System.out.println("Offset Value "+verticalOffset);
                if (verticalOffset == -500) {
                    toolBarShow = true;
                } else if (verticalOffset == 0 || verticalOffset > -388) {
                    toolBarShow = false;
                }

                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.title_text_color), PorterDuff.Mode.SRC_ATOP);

                    if ((verticalOffset >= -500 && verticalOffset <= -388) && toolBarShow) {

                        room_details_share.setImageDrawable(icon4);

                        room_details_wishlist.setImageDrawable(icon1);

                        String color = getResources().getString(0 + R.color.text_shadow);
                        room_details_back.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
                            /*room_details_share.setColorFilter(Color.parseColor(color),PorterDuff.Mode.SRC_ATOP);
                            room_details_wishlist.setColorFilter(Color.parseColor(color),PorterDuff.Mode.SRC_ATOP);*/
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.title_text_color));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.title_text_color));
                        toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.d_bottom));
                    } else {
                        room_details_share.setImageDrawable(icon3);
                        room_details_wishlist.setImageDrawable(icon2);
                        room_details_back.setColorFilter(null);
                        //  room_details_share.setColorFilter(null);
                        // room_details_wishlist.setColorFilter(null);
                        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                } else {
                    room_details_share.setImageDrawable(icon3);
                    room_details_wishlist.setImageDrawable(icon2);
                    room_details_back.setColorFilter(null);
                    //room_details_share.setColorFilter(null);
                    // room_details_wishlist.setColorFilter(null);
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.text_black), PorterDuff.Mode.SRC_ATOP);
                }

                if (is_whishlist != null) {
                    if (is_whishlist.equals("Yes")) {
                        room_details_wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
                        //   room_details_wishlist.setImageDrawable(icon5);
                    }
                }


            }


        };

        Log.e("LocationAddress","LocationAddress"+location_address);

        appBarLayout.addOnOffsetChangedListener(mListener);

        requestcheck = (Button) findViewById(R.id.requestcheck);

        if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut) == null) {
            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
        }
        datecheck = localSharedPreferences.getSharedPreferences(Constants.isRequestCheck);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);

        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) == null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {

            if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut) != null || !TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut))) {
                datecheck = "1";
            }

        }

        System.out.println("DateCheck one : "+datecheck);

        System.out.println("Search date " + datecheck + "checkin " + searchcheckin + "checkout " + searchcheckout + "checkdate " + searchstartdate + "check dates " + searchenddate);

        if ((datecheck == null || datecheck.equals("0")) && (searchcheckin == null && searchcheckout == null)) {
            requestcheck.setText(getResources().getString(R.string.checkavailability));
            checkdate = true;
        } else {
            checkdate = false;
            if (instant_book != null && instant_book.equals("Yes")) {
                requestcheck.setText(getResources().getString(R.string.instantbook));
            } else {
                requestcheck.setText(getResources().getString(R.string.requestbook));
            }
        }


        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.similar_roomdetail_list);


        room_details_amenities = (LinearLayout) findViewById(R.id.room_details_amenities);
// On Click function used to click action for check Email id in server send link to Email
        room_details_amenities.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

            }
        });

        room_details_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        hostby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

//                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);// this is used to room details activity to move another profile page activity
//                System.out.println("Other user id" + other_user_id);
//                x.putExtra("otheruserid", other_user_id);
//                startActivity(x);


            }
        });

        room_details_hostedby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + other_user_id);
                x.putExtra("otheruserid", other_user_id);
                startActivity(x);

            }
        });

        room_details_hostprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + other_user_id);
                x.putExtra("otheruserid", other_user_id);
                startActivity(x);
            }
        });

        // On Click function used to click action for check Email id in server send link to Email
        requestcheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (checkdate) {
                    Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                    localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                    x.putExtra("blockdate", blocked_dates);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(x, bndlanimation);
                } else {
                    if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                        localSharedPreferences.saveSharedPreferences(Constants.EMPTYTOKENADDRESS, location_address);
                        localSharedPreferences.saveSharedPreferences(Constants.EMPTYTOKENROOMNAME, room_name);
                        localSharedPreferences.saveSharedPreferences(Constants.EMPTYTOKENBEDROOM, no_of_bedrooms);
                        localSharedPreferences.saveSharedPreferences(Constants.EMPTYTOKENBATHROOM, no_of_bathrooms);
                        localSharedPreferences.saveSharedPreferences(Constants.LastPage, "RequestAccept");

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(blocked_dates);
                        editor.putString("blockdates", json);
                        editor.apply();


                        Intent home = new Intent(context, MainActivity.class);
                        home.putExtra("isback", 1);
                        home.putExtra("blockdate", blocked_dates);
                        context.startActivity(home);
                    } else {
                        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
                        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
                        checkinout = localSharedPreferences.getSharedPreferences(Constants.CheckInOut);
                        if (searchstartdate != null && searchenddate != null) {
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchstartdate);
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchenddate);
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                        } else {
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchcheckin);
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchcheckout);
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                        }
                        Intent x = new Intent(getApplicationContext(), RequestActivity.class);
                        x.putExtra("location", location_address);
                        x.putExtra("roomname", room_name);
                        x.putExtra("bedroom", no_of_bedrooms);
                        x.putExtra("bathroom", no_of_bathrooms);
                        x.putExtra("blockdate", blocked_dates);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                        startActivity(x, bndlanimation);
                    }
                }
            }
        });
        room_details_contacthost = (LinearLayout) findViewById(R.id.room_details_contacthost);
        room_details_availability = (LinearLayout) findViewById(R.id.room_details_availability);
        room_details_triplength = (LinearLayout) findViewById(R.id.room_details_triplength);
        room_details_additionalprice = (LinearLayout) findViewById(R.id.room_details_additionalprice);
        room_details_about = (LinearLayout) findViewById(R.id.room_details_about);
        room_details_cancelpolicy = (LinearLayout) findViewById(R.id.room_details_cancelpolicy);
        room_details_houserules = (LinearLayout) findViewById(R.id.room_details_houserules);

        roomsdetails_review.setOnClickListener(this);
        room_details_contacthost.setOnClickListener(this);
        room_details_availability.setOnClickListener(this);
        room_details_triplength.setOnClickListener(this);
        room_details_cancelpolicy.setOnClickListener(this);
        room_details_houserules.setOnClickListener(this);
        room_details_share.setOnClickListener(this);
        room_details_wishlist.setOnClickListener(this);
        room_details_additionalprice.setOnClickListener(this);
        about_readmore.setOnClickListener(this);
        service_readmore.setOnClickListener(this);


        viewPager = (ViewPager) findViewById(R.id.room_details_viewpager);


        isInternetAvailable = getNetworkState().isConnectingToInternet(); // get the internet avaiable
        if (isInternetAvailable) {
            loadRoomDetail();
        } else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2,  room_details_isshared, getResources(), this);
        }
    }

    private void initBedType() {
        bedTypeList.clear();
        getEventType();
     //   bedRoomModel = roomResult.getBedRoomBeds();
    //    bedTypeList = roomResult.getCommonBeds();
    //    no_of_rooms.setText(roomResult.getBedRoomBeds().size() + " " + getResources().getString(R.string.rooms));

    //    bedRoomModel.add(bedTypeList);

        int beds = 0;
/*
        for(int i=0;i<bedRoomModel.size();i++){
            beds = beds+bedRoomModel.get(i).size();
        }*/


    //    no_of_beds.setText(beds+ " " + getResources().getString(R.string.beds));



       // getEmptyArrayPositions();
      //  removeBedRoomModelItem();



        rvEventType.setNestedScrollingEnabled(false);
        rvEventType.setHasFixedSize(true);
        bedArrangementAdapter = new BedArrangementAdapter(this, this, spaceActivityArrays);
        rvEventType.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvEventType.setAdapter(bedArrangementAdapter);
    }
    private void getEventType() {
       event_Type_Model.add(new EventTypeModel("Festivel",getResources().getDrawable(R.drawable.anim_trips),"800000","50000000","6"));
        event_Type_Model.add(new EventTypeModel("Events",getResources().getDrawable(R.drawable.image),"800","","4"));
        event_Type_Model.add(new EventTypeModel("Production",getResources().getDrawable(R.drawable.icon_apartment_selected),"800","5000","4"));
        event_Type_Model.add(new EventTypeModel("Festivel",getResources().getDrawable(R.drawable.dot_loading),"800","5000","4"));

    }

    private void getEmptyArrayPositions() {

        int pos =1;
        for (int i = 0; i < bedRoomModel.size(); i++){
            if (bedRoomModel.get(i).size()!=0){
                emptyArrayPositions.add(String.valueOf(pos));
            }
            pos++;
        }
    }

    private void removeBedRoomModelItem() {


        for (int i = bedRoomModel.size()-1; i >= 0; i--){
            if (bedRoomModel.get(i).size()==0){
                bedRoomModel.remove(i);
            }
        }
    }

    public void onBackPressed() {
        roomIdPrice.removeRoomId(roomid);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, null);
        if (isHost == 0) localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, null);

        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, null);
        localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 0);
        localSharedPreferences.saveSharedPreferences(Constants.HouseRules, null);

        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        checkinout = localSharedPreferences.getSharedPreferences(Constants.CheckInOut);

        if (searchstartdate != null && searchcheckin != null) {
            if (searchstartdate.equals(searchcheckin) && searchenddate.equals(searchcheckout)) {
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchstartdate);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchenddate);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
                dialog();
                /*close();
                super.onBackPressed();*/

            } else {
                dialog();
            }
        } else {
            if (searchcheckin != null && searchcheckout != null) {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1");
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);
            }
            close();
            super.onBackPressed();
        }
    }

    public void close() {
        if (localSharedPreferences.getSharedPreferences(Constants.Reload) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, null);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
        } else if (getIntent().getIntExtra("isRoomBack", 0) == 1 || getIntent().getIntExtra("finish_it", 0) == 1) {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
        } else if (localSharedPreferences.getSharedPreferences(Constants.REQBACK) != null && localSharedPreferences.getSharedPreferences(Constants.REQBACK).equalsIgnoreCase("goback")) {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
            localSharedPreferences.saveSharedPreferences(Constants.REQBACK, "");
        } else {
            finish();
        }
    }

    public void dialog() {
        final Dialog dialog = new Dialog(Room_details.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        // set the custom dialog components - text, ivPhoto and button
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.logout_msg);
        dialogMessage.setText(getResources().getString(R.string.datedialog1) + "" + checkinout + getResources().getString(R.string.datedialog2));
        Button dialogButton = (Button) dialog.findViewById(R.id.logout_cancel);
        dialogButton.setText(getResources().getString(R.string.no));
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1");
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                //close();
                dialog.dismiss();
            }
        });

        Button dialogButton1 = (Button) dialog.findViewById(R.id.logout_ok);
        dialogButton1.setText(getResources().getString(R.string.yes));
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1");
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchstartdate);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchenddate);
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    private void getRelativeLayoutInfo() {
        int w = roomdetails_footer.getWidth();
        //bottomheight = roomdetails_footer.getHeight();
        Log.v("bottomheight 1 ", w + "-" + bottomheight);

        if (can_book.equals("No")) {
            isHost = 1;
        }

        if (isHost == 0) {

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) roomsdetailsnestedscrool.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, bottomheight);
            roomsdetailsnestedscrool.setLayoutParams(layoutParams);
            roomdetails_footer.setVisibility(View.VISIBLE);
            room_details_contacthost.setVisibility(View.VISIBLE);
            room_details_availability.setVisibility(View.VISIBLE);
            //room_details_triplength.setVisibility(View.VISIBLE);
        } else {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) roomsdetailsnestedscrool.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            roomsdetailsnestedscrool.setLayoutParams(params);
            roomdetails_footer.setVisibility(View.GONE);
            room_details_contacthost.setVisibility(View.GONE);
            room_details_availability.setVisibility(View.GONE);
            //room_details_triplength.setVisibility(View.GONE);

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_details_share: {
                if (imagetoshare != null) {
                    Intent x = new Intent(getApplicationContext(), ShareActivity.class);
                    x.putExtra("imagetoshare", imagetoshare);
                    x.putExtra("nametoshare", nametoshare);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(x, bndlanimation);
                }
            }
            break;
            case R.id.room_details_wishlist: {
                if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload");
                    localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, roomid);
                    if (is_whishlist != null) {
                        if (is_whishlist.equals("Yes")) {
                            is_whishlist = "No";
                            room_details_wishlist.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
                            deleteWishList task = new deleteWishList(context);
                            task.execute();
                        } else {

                            localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, location_address);
                            Log.e("LocationAddress","LocationAddress"+location_address);
                            WishListChooseDialog cdd = new WishListChooseDialog(this);
                            cdd.show();
                        }
                    }
                } else {

                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
                    home.putExtra("isback", 1);
                    startActivity(home);
                    //finish();
                }
            }
            break;
            case R.id.roomsdetails_review: {
                Intent x = new Intent(getApplicationContext(), ReviewActivityHome.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.room_details_contacthost: {

                if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) == null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "Contact_host");
                    Intent x = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(x, bndlanimation);
                    finish();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(blocked_dates);
                    String json1 = gson.toJson(host_user_image);
                    String json2 = gson.toJson(roomtypeusername);
                    String json3 = gson.toJson(host_user_name);
                    String json4 = gson.toJson(no_of_guests);
                    editor.putString("blockdates", json);
                    editor.putString("hostuserimage", json1);
                    editor.putString("roomtypeusername", json2);
                    editor.putString("hostusername", json3);
                    editor.putString("guestcounts", json4);
                    editor.apply();

                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                    Intent x = new Intent(getApplicationContext(), ContactHostActivity.class);
                    x.putExtra("blockdate", blocked_dates);
                    x.putExtra("hostuserimage", host_user_image);
                    x.putExtra("roomtypeusername", roomtypeusername);
                    x.putExtra("hostusername", host_user_name);
                    x.putExtra("guestcounts", no_of_guests);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(x, bndlanimation);
                }
            }
            break;
            case R.id.room_details_additionalprice: {
                Intent x = new Intent(getApplicationContext(), AdditionalPriceActivity.class);
                x.putExtra("additionalprice", additionalprice);
                x.putExtra("length_of_stay_rules", lengthOfStays);
                x.putExtra("early_bird_rules", earlybirdrules);
                x.putExtra("last_min_rules", lastminrules);
                x.putExtra("Weekend_price", weekend);
                x.putExtra("currency_symbol", Html.fromHtml(currency_symbol).toString());
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.about_readmore: {
                int count = about_this_home.getText().length();
                System.out.println("Line Count " + count);
                Intent x = new Intent(getApplicationContext(), RoomAboutList.class);
                x.putExtra("aboutlist", room_detail);
                x.putExtra("desc_interaction", desc_interaction);
                x.putExtra("desc_notes", desc_notes);
                x.putExtra("desc_house_rules", desc_house_rules);
                x.putExtra("desc_neighborhood_overview", desc_neighborhood_overview);
                x.putExtra("desc_getting_around", desc_getting_around);
                x.putExtra("desc_space", desc_space);
                x.putExtra("desc_access", desc_access);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);

            }
            break;
            case R.id.room_details_availability: {
                Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                x.putExtra("blockdate", blocked_dates);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.room_details_triplength: {

                Bundle bundle = new Bundle();
                bundle.putSerializable("availability_rules", availabilityRules);
                Intent x = new Intent(getApplicationContext(), TripLengthActivity.class);
                x.putExtras(bundle);
                x.putExtra("minimum_stay", minimum_stay);
                x.putExtra("maximum_stay", maximum_stay);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.room_details_cancelpolicy: {
                Intent x = new Intent(getApplicationContext(), CancellationPolicyActivity.class);
                x.putExtra("cancellationpolicy", cancellation_policys);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.room_details_houserules: {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestrRoom, 0);
                Intent x = new Intent(getApplicationContext(), HouseRulesActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "Space_detail");
        }

        if (roomIdPrice.getRoomId() != null) roomid = roomIdPrice.getRoomId();
        if (localSharedPreferences.getSharedPreferences(Constants.SelectedRoomPrice) != null)
            room_price = localSharedPreferences.getSharedPreferences(Constants.SelectedRoomPrice);
        else room_price = roomIdPrice.getRoomPrice();

        localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, roomid);
        roomIdPrice.addRoomId(roomid, room_price);
        room_price = roomIdPrice.getRoomPrice();
        roomid = roomIdPrice.getRoomId();

            if (room_price != null && currency_symbol != null) {
                String currencysymbol = Html.fromHtml(currency_symbol).toString();
                room_details_amount.setText(currencysymbol + "" + room_price);
            }




        datecheck = localSharedPreferences.getSharedPreferences(Constants.isRequestCheck);

        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) == null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {

            if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut) != null || !TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut))) {
                datecheck = "1";
            }

        }

        System.out.println("DateCheck two :  " + datecheck);


        if ((datecheck == null || datecheck.equals("0")) && (searchcheckin == null && searchcheckout == null || searchstartdate != "null" && searchenddate != "null")) {

            requestcheck.setText(getResources().getString(R.string.checkavailability));
            checkdate = true;
        } else {
            checkdate = false;
            if (instant_book != null && instant_book.equals("Yes")) {
                requestcheck.setText(getResources().getString(R.string.instantbook));
            } else {
                requestcheck.setText(getResources().getString(R.string.requestbook));
            }

        }

        if (localSharedPreferences.getSharedPreferences(Constants.Reload) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, null);
            Intent x = getIntent();
            finish();
            startActivity(x);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storiesProgressView.destroy();
        hidePDialog();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(null);
        }
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

    }

    public void loadSimilarList() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        searchlist = new ArrayList<>();
        listAdapter = new SimilarSearchRecycleListAdapter(this, this, roomSimilarModel);
        listAdapter.setLoadMoreListener(new SimilarSearchRecycleListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = searchlist.size() - 1;
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        if (isHost == 1)
        {
          similarlisting.setVisibility(View.GONE);
          room_details_similarlist_txt.setVisibility(View.GONE);
           recyclerView.setVisibility(View.GONE);
        }
        else
        {
            similarlisting.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            room_details_similarlist_txt.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(listAdapter);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        Log.e("ON Success","On Success Response"+jsonResp.isSuccess() + "StatusMessage" +jsonResp.getStatusMsg());
        if (jsonResp.isSuccess()) {
            onSuccessRoom(jsonResp); // onSuccess call method
        }
        else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            if (isHost == 0) {
                roomdetails_footer.setVisibility(View.VISIBLE);
            } else {
                roomdetails_footer.setVisibility(View.GONE);
            }

            room_details_dotloader.setVisibility(View.GONE);
            rooms_details.setVisibility(View.VISIBLE);
            //String statusCode = (String) commonMethods.getJsonValue(String.valueOf(jsonResp), Constants.STATUS_CODE, String.class);
            if(jsonResp.getStatusMsg().equals("Room Not available")){
                Toast.makeText(this,jsonResp.getStatusMsg(),Toast.LENGTH_LONG).show();
                finish();
            }else{
                commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, room_details_isshared, getResources(), this);
            }
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, room_details_isshared, getResources(), this);
    }

    public void loadRoomDetail() {
        apiService.spaceDetail(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid,localSharedPreferences.getSharedPreferences(Constants.LanguageCode)).enqueue(new RequestCallback(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String roomId) {

        System.out.println(" Messsage event : "+roomId);

        for(int i=0;i<roomSimilarModel.size();i++){
            if(roomSimilarModel.get(i).getRoomId().equals(roomId)){
                roomSimilarModel.get(i).setIsWhishlist("Yes");
                break;
            }

        }

        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId,"");
        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist,false);


        listAdapter.notifyDataSetChanged();

    };


    public void onSuccessRoom(JsonResponse jsonResp) {
        spaceResult = gson.fromJson(jsonResp.getStrResponse(), SpaceResult.class);

        Log.e("IsHost","IsHost"+isHost);
        other_user_id = spaceResult.getUserId();
        localSharedPreferences.saveSharedPreferences(Constants.HostUser,other_user_id);
        ArrayList<SpaceResult.SpacePhotoList> roomimg = spaceResult.getSpacePhotos();
        roomImgItems = new String[roomimg.size()];
        for (int i = 0; i < roomimg.size(); i++) {
            roomImgItems[i] = roomimg.get(i).getPhotoName();
        }
        if (roomImgItems.length > 0) imagetoshare = roomImgItems[0];
        nametoshare = spaceResult.getSpaceUrl();

        /*ArrayList<String> blockedDates = roomResult.getBlockedDates();
        blocked_dates = new String[blockedDates.size()];
        for (int i = 0; i < blockedDates.size(); i++) {
            blocked_dates[i] = blockedDates.get(i);
        }*/
        room_name = spaceResult.getSpacename();
        room_type = spaceResult.getSpaceTypeName();
        host_user_name = spaceResult.getHostName();
        /*host_user_image = roomResult.getHostUserImage();
        localSharedPreferences.saveSharedPreferences(Constants.HostUserName, host_user_name);*/
        /*review_user_names = roomResult.getReviewUserName();
        review_user_images = roomResult.getReviewUserImage();
        review_dates = roomResult.getReviewDate();
        review_messages = roomResult.getReviewMessage();
        review_counts = roomResult.getReviewCount();
        review_rates = roomResult.getRatingValue();*/

        no_of_guests = spaceResult.getNumberofGuests();
        mSpaceSize=spaceResult.getSpaceSize();
        mSpacetype=spaceResult.getSizeType();

        /*no_of_bedss = roomResult.getNoOfBeds();
        no_of_bedrooms = roomResult.getNoOfBedrooms();
        no_of_bathrooms = roomResult.getNoOfBathrooms();*/

        desc_space = spaceResult.getSpace();
        desc_access = spaceResult.getAccess();
        desc_interaction = spaceResult.getInteraction();
        desc_notes = spaceResult.getNotes();
        //desc_house_rules = roomResult.getHouse_rules();
        //desc_house_rules = roomResult.getHouseRules();
        //desc_neighborhood_overview = roomResult.getNeighborhood_overview();
        //desc_getting_around = roomResult.getGetting_around();

        room_detail = spaceResult.getSummary();
        //weekend = roomResult.getWeekend();

        if (room_detail != null && !room_detail.equals("")) {

            room_details_about.setVisibility(View.VISIBLE);
            about_this_home.setText(room_detail);
          //  int count=  countLines(room_detail);
        //    System.out.println("CountLines"+count);
            if(room_detail.length()<120 && desc_interaction.equals("") && desc_notes.equals("") && desc_house_rules.equals("") && desc_neighborhood_overview.equals("") && desc_getting_around.equals("") && desc_space.equals("") && desc_access.equals("") )
            {
                about_readmore.setVisibility(View.GONE);
            }
        } else {

            room_details_about.setVisibility(View.GONE);
        }

        room_price = roomResult.getRoomPrice();
        instant_book = roomResult.getInstantBook();
        is_whishlist = roomResult.getIsWhishlist();
        System.out.println("is_whishlist" + is_whishlist);
        can_book = roomResult.getCanBook();
        localSharedPreferences.saveSharedPreferences(Constants.isInstantBook, instant_book);
        if (roomResult.getIsShared().equals("Yes"))
            room_details_isshared.setVisibility(View.VISIBLE);
        else room_details_isshared.setVisibility(View.GONE);

        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) == null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {

            if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut) != null || !TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut))) {
                datecheck = "1";
            }

        }

        System.out.println("DateCheck three :  " + datecheck);


        if ((datecheck == null || datecheck.equals("0")) && (searchcheckin == null && searchcheckout == null)) {
            requestcheck.setText(getResources().getString(R.string.checkavailability));
            checkdate = true;
        } else {
            checkdate = false;
            if (instant_book != null && instant_book.equals("Yes")) {
                requestcheck.setText(getResources().getString(R.string.instantbook));
            } else {
                requestcheck.setText(getResources().getString(R.string.requestbook));
            }
        }
        roomAmenitiesModel = roomResult.getAmenities_values();
        amenities_id = new String[roomAmenitiesModel.size()];
        amenities_name = new String[roomAmenitiesModel.size()];
        amenities_icon = new String[roomAmenitiesModel.size()];
        amenities_image = new String[roomAmenitiesModel.size()];

        amenitiescount = Integer.toString(roomAmenitiesModel.size() - 4);

        room_details_amenities_text.setText("+" + amenitiescount);

        for (int i = 0; i < roomAmenitiesModel.size(); i++) {

            amenities_id[i] = roomAmenitiesModel.get(i).getId();
            amenities_name[i] = roomAmenitiesModel.get(i).getName();
            amenities_icon[i] = roomAmenitiesModel.get(i).getIcon();
            amenities_image[i] = roomAmenitiesModel.get(i).getIcon();

            /*for (int j = 0; j < amenities_imagedata.length; j++) {
                System.out.println("amenities_icon[i] " + amenities_icon[i] + " " + amenities_imagedata[j]);

                if (amenities_icon[i].equals(amenities_imagedata[j])) {
                    amenities_image[i] = amenities_imagedata[j].(0);
                    break;
                } else {
                    amenities_image[i] = amenities_imagedata[amenities_imagedata.length - 1].charAt(0);
                }
            }*/
        }
        setAminity();
        loadData();
        guestAccess();
        specialFeature();
        spaceStyle();
        serviceOffered();
        similarListload();
        initBedType();
        additionalPriceCalc();
        rv_guest_access.setNestedScrollingEnabled(false);
        rv_guest_access.setHasFixedSize(true);
        //guestAccessListAdapter =new GuestAccessListAdapter(this,guestAccess_model, this);
        rv_guest_access.setLayoutManager(new GridLayoutManager(context,2));
        rv_guest_access.setAdapter(guestAccessListAdapter);
        location_address = roomResult.getLocaitonName();
        location_lat = roomResult.getLocLatidude();
        location_lng = roomResult.getLocLongidude();
        LatLng latLng = new LatLng(Double.valueOf(location_lat), Double.valueOf(location_lng));


        //serviceOfferAdapter = new ServiceOfferAdapter(this,service_offer_model,this);
        rvseviceType.setLayoutManager(new GridLayoutManager(context,2));
        rvseviceType.setAdapter(serviceOfferAdapter);
        rvseviceType.setNestedScrollingEnabled(false);
        rvseviceType.setHasFixedSize(true);

        //specialFeatureAdapter = new SpecialFeatureAdapter(this,specialFeature_model,this);
        special_feature.setLayoutManager(new GridLayoutManager(context,2));
        special_feature.setAdapter(specialFeatureAdapter);
        special_feature.setNestedScrollingEnabled(false);
        special_feature.setHasFixedSize(true);

        //spaceStyleAdapter = new SpaceStyleAdapter(this,space_model,this);
        space_style.setLayoutManager(new GridLayoutManager(context,2));
        space_style.setAdapter(spaceStyleAdapter);
        space_style.setNestedScrollingEnabled(false);
        space_style.setHasFixedSize(true);



        if (roomImgItems.length > 0) {
            loadImage(); // this function to use load the ivPhoto
        }
        // getRelativeLayoutInfo();
        loadMapImage(location_address, latLng);

        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }

        room_details_dotloader.setVisibility(View.GONE);
        rooms_details.setVisibility(View.VISIBLE);

        if (isHost == 0) {
            roomdetails_footer.setVisibility(View.VISIBLE);
        } else {
            roomdetails_footer.setVisibility(View.GONE);
        }


        // Extra price calculation details

        minimum_stay = roomResult.getMinimumStay();
        maximum_stay = roomResult.getMaximumStay();

        availabilityRules = roomResult.getAvailabilityRules();
        lengthOfStays = roomResult.getLengthOfStayRules();
        earlybirdrules = roomResult.getEarlyBirdRules();
        lastminrules = roomResult.getLastMinRules();

        if (minimum_stay.equals("") && maximum_stay.equals("") && availabilityRules.size() <= 0)
            room_details_triplength.setVisibility(View.GONE);


        ViewTreeObserver vto = roomdetails_footer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    roomdetails_footer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    roomdetails_footer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = roomdetails_footer.getMeasuredWidth();
                bottomheight = roomdetails_footer.getMeasuredHeight();
                System.out.println("bottomheight s " + bottomheight);
                                       /* if (similarlist_values.length() == 0) {
                                            similarlisting.setVisibility(View.GONE);
                                            System.out.println("bottomheight2 "+bottomheight);
                                            rooms_details.setPadding(0,0,0,bottomheight);
                                        }*/
                getRelativeLayoutInfo();
            }
        });

        if(roomImgItems.length>1)
        {
            storiesProgressView.setStoriesCount(roomImgItems.length);
            storiesProgressView.setStoryDuration(6000L);
            storiesProgressView.setStoriesListener(this);
            storiesProgressView.startStories();
        }

    }

    private void guestAccess() {
       guestAccess_model=new ArrayList<>();
        guestAccess_model.add(new Space_guestAccess_Model("Elevator"));
        guestAccess_model.add(new Space_guestAccess_Model("Delivery Access"));
        guestAccess_model.add(new Space_guestAccess_Model("stairs"));
        guestAccess_model.add(new Space_guestAccess_Model("Parking Near By"));
        guestAccess_model.add(new Space_guestAccess_Model("Garage Door"));
        guestAccess_model.add(new Space_guestAccess_Model("Garage Door"));
        guestAccess_model.add(new Space_guestAccess_Model("Garage Door"));

    }


    private void serviceOffered() {
        service_offer_model=new ArrayList<>();
        service_offer_model.add(new Service_offer_model("Food4"));
        service_offer_model.add(new Service_offer_model("Lighting System"));
        service_offer_model.add(new Service_offer_model("Furniture Rental"));
        service_offer_model.add(new Service_offer_model("Event Manager"));
        service_offer_model.add(new Service_offer_model("Cleaning"));
        service_offer_model.add(new Service_offer_model("Trash Removal"));
        service_offer_model.add(new Service_offer_model("Photography"));
    }

    private void specialFeature(){
        specialFeature_model = new ArrayList<>();
        specialFeature_model.add(new SpecialFeature_Model("Natural Light"));
        specialFeature_model.add(new SpecialFeature_Model("Modern Bathroom"));
        specialFeature_model.add(new SpecialFeature_Model("Garden"));
        specialFeature_model.add(new SpecialFeature_Model("Library"));
        specialFeature_model.add(new SpecialFeature_Model("Dining Table"));
        specialFeature_model.add(new SpecialFeature_Model("Screening Room"));
        specialFeature_model.add(new SpecialFeature_Model(" Pool"));
    }

    private void spaceStyle(){
        space_model = new ArrayList<>();
        space_model.add(new Space_Model("Industrial"));
        space_model.add(new Space_Model("Intimate"));
        space_model.add(new Space_Model("Luxurious"));
        space_model.add(new Space_Model("Raw"));
        space_model.add(new Space_Model("Rustic"));
        space_model.add(new Space_Model("Classic"));
        space_model.add(new Space_Model("Cultural"));
    }


    public void setAminity() {
        if (roomAmenitiesModel.size() == 0) {
            room_details_amenities.setVisibility(View.GONE);
        }
        if (roomAmenitiesModel.size() == 1) {
            if (!((Activity) context).isFinishing()) {
                ame1.setVisibility(View.VISIBLE);
                Glide.with(this).load(amenities_image[0]).into(ame1);
            }
            //ame1.setText(String.valueOf(amenities_image[0]));
        } else if (roomAmenitiesModel.size() == 2) {
            if (!((Activity) context).isFinishing()) {
                ame1.setVisibility(View.VISIBLE);
                Glide.with(this).load(amenities_image[0]).into(ame1);
                //ame1.setText(String.valueOf(amenities_image[0]));
                ame2.setVisibility(View.VISIBLE);
                Glide.with(this).load(amenities_image[1]).into(ame2);
            }
            //ame2.setText(String.valueOf(amenities_image[1]));
        } else if (roomAmenitiesModel.size() == 3) {
            if (!((Activity) context).isFinishing()) {
                ame1.setVisibility(View.VISIBLE);
                //ame1.setText(String.valueOf(amenities_image[0]));
                Glide.with(this).load(amenities_image[0]).into(ame1);
                ame2.setVisibility(View.VISIBLE);
                //ame2.setText(String.valueOf(amenities_image[1]));
                Glide.with(this).load(amenities_image[1]).into(ame2);
                ame3.setVisibility(View.VISIBLE);
                //ame3.setText(String.valueOf(amenities_image[2]));
                Glide.with(this).load(amenities_image[2]).into(ame3);
            }
        } else if (roomAmenitiesModel.size() == 4) {
            if (!((Activity) context).isFinishing()) {
                ame1.setVisibility(View.VISIBLE);
                //ame1.setText(String.valueOf(amenities_image[0]));
                Glide.with(this).load(amenities_image[0]).into(ame1);
                ame2.setVisibility(View.VISIBLE);
                //ame2.setText(String.valueOf(amenities_image[1]));
                Glide.with(this).load(amenities_image[1]).into(ame2);
                ame3.setVisibility(View.VISIBLE);
                //ame3.setText(String.valueOf(amenities_image[2]));
                Glide.with(this).load(amenities_image[2]).into(ame3);
                ame4.setVisibility(View.VISIBLE);
                //ame4.setText(String.valueOf(amenities_image[3]));
                Glide.with(this).load(amenities_image[3]).into(ame4);
            }
        } else if (roomAmenitiesModel.size() > 4) {
            if (!((Activity) context).isFinishing()){
                ame1.setVisibility(View.VISIBLE);
            //ame1.setText(String.valueOf(amenities_image[0]));
            Glide.with(this).load(amenities_image[0]).into(ame1);
            ame2.setVisibility(View.VISIBLE);
            //ame2.setText(String.valueOf(amenities_image[1]));
            Glide.with(this).load(amenities_image[1]).into(ame2);
            ame3.setVisibility(View.VISIBLE);
            //ame3.setText(String.valueOf(amenities_image[2]));
            Glide.with(this).load(amenities_image[2]).into(ame3);
            ame4.setVisibility(View.VISIBLE);
            //ame4.setText(String.valueOf(amenities_image[3]));
            Glide.with(this).load(amenities_image[3]).into(ame4);
            ame1.setVisibility(View.VISIBLE);
            room_details_amenities_text.setVisibility(View.VISIBLE);
        }
        }
    }

    public void loadData() {
        room_details_title.setText(room_name);
        room_details_hometype.setText(room_type);
        room_details_hostedby.setText(host_user_name);

        roomtypeusername = room_type + " " + getResources().getString(R.string.hosted_by) + " " + host_user_name;

        Glide.with(getApplicationContext()).asBitmap().load(host_user_image).into(new BitmapImageViewTarget(room_details_hostprofile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                room_details_hostprofile.setImageDrawable(circularBitmapDrawable);
            }
        });

        no_of_guest.setText(no_of_guests + " " + getResources().getString(R.string.guests));
        sqr_ft.setText(mSpaceSize + " " +mSpacetype);
        //no_of_rooms.setText(no_of_bedrooms + " " + getResources().getString(R.string.rooms));
        //no_of_beds.setText(no_of_bedss + " " + getResources().getString(R.string.beds));
       // no_of_bath.setText(no_of_bathrooms + " " + getResources().getString(R.string.bath));
        about_this_home.setText(room_detail);


        // Review User Details
        if (review_counts == "" || review_counts.isEmpty() || review_counts.length() == 0) {
            roomsdetails_review.setVisibility(View.GONE);
            room_details_rate.setVisibility(View.INVISIBLE);
            room_details_ratecount.setVisibility(View.GONE);
        } else {
            Glide.with(getApplicationContext()).asBitmap().load(review_user_images).into(new BitmapImageViewTarget(review_user_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    review_user_image.setImageDrawable(circularBitmapDrawable);
                }
            });
            review_user_name.setText(review_user_names);
            review_user_date.setText(review_dates);
            review_user_msg.setText(review_messages);
            review_user_msg.setText(review_messages);

            if(review_counts.equals("1")){
                review_user_count.setText(getResources().getString(R.string.read) + " " + review_counts + " " + getResources().getString(R.string.review_s_one));
                room_details_ratecount.setText(" " + review_counts + " " + getResources().getString(R.string.review_s_one));
            }else{
                review_user_count.setText(getResources().getString(R.string.read) + " " + review_counts + " " + getResources().getString(R.string.review_s));
                room_details_ratecount.setText(" " + review_counts + " " + getResources().getString(R.string.review_s));
            }

            review_user_rate.setRating(Float.valueOf(review_rates));
            room_details_rate.setRating(Float.valueOf(review_rates));
        }

        house_rules = roomResult.getHouseRules();

        if (house_rules.length() == 0) {
            room_details_houserules.setVisibility(View.GONE);
            localSharedPreferences.saveSharedPreferences(Constants.HouseRules, null);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.HouseRules, house_rules);
        }
        cancellation_policys = roomResult.getCancellationPolicy();
        System.out.println("Cancellation Policy"+cancellation_policys);
        cancellation_policy.setText(cancellation_policys);

    }

    public void similarListload() {

        currency_code = roomResult.getCurrencyCode();
        currency_symbol = roomResult.getCurrencySymbol();
        String currencysymbol = Html.fromHtml(currency_symbol).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, currency_code);
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencysymbol);

        roomSimilarModel = roomResult.getSimilarListDetails();
        if (roomSimilarModel.size() == 0) {
            similarlisting.setVisibility(View.GONE);
        } else {
            loadSimilarList();
            listAdapter.notifyDataChanged();

        }
    }

    public void additionalPriceCalc() {
        String currencysymbol = Html.fromHtml(currency_symbol).toString();

            if (roomIdPrice.getRoomPrice() != null) {

                room_details_amount.setText(currencysymbol + "" + roomIdPrice.getRoomPrice());
            } else {
                roomIdPrice.addRoomId(roomIdPrice.getRoomId(), room_price);  // Add room id and price
                room_details_amount.setText(currencysymbol + "" + room_price);
            }


        String weekly_price;
        String monthly_price;
        String cleaning;
        String additional_guest;
        String security;
        String weekend;
        // weekly_price = response.getString("weekly_price");
        // monthly_price = response.getString("monthly_price");
        cleaning = roomResult.getCleaning();
        additional_guest = roomResult.getAdditionalGuest();
        security = roomResult.getSecurity();
        lengthOfStays = roomResult.getLengthOfStayRules();
        earlybirdrules = roomResult.getEarlyBirdRules();
        lastminrules = roomResult.getLastMinRules();
        weekend = roomResult.getWeekend();


        if (Float.valueOf(additional_guest) == 0 && Float.valueOf(weekend) == 0 &&  Float.valueOf(security) == 0 && Float.valueOf(cleaning) == 0 && lengthOfStays.size() == 0 && earlybirdrules.size() == 0 && lastminrules.size() == 0) {
            room_details_additionalprice.setVisibility(View.GONE);
        } else {
            room_details_additionalprice.setVisibility(View.VISIBLE);
        }
        additional_guest = currencysymbol + "" + additional_guest;
        weekly_price = currencysymbol + "0";
        monthly_price = currencysymbol + "0";
        security = currencysymbol + "" + security;
        cleaning = currencysymbol + "" + cleaning;

        additionalprice = additional_guest + "," + weekly_price + "," + monthly_price + "," + security + "," + cleaning;

    }

    public void loadMapImage(String location_address, LatLng latLng) {
        try {
            GoogleStaticMapsAPIServices staticmaps = new GoogleStaticMapsAPIServices();

            Glide.with(getApplicationContext()).load(staticmaps.getStaticMapURL(latLng, 100,getResources().getString(R.string.google_key)))

                    .into(new DrawableImageViewTarget(map) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });
            mapaddress.setText(location_address);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage() {
        adapter = new ImageAdapter(Room_details.this);
        viewPager.setAdapter(adapter);

        handler = new Handler();
        runnable = new Runnable() {
            int i = 0;

            public void run() {
                viewPager.setCurrentItem(i);
                imagetoshare = roomImgItems[i];
                i++;
                if (i > roomImgItems.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
    @Override
    public void onNext() {
        ++expImage;
        viewPager.setCurrentItem(expImage);
        imagetoshare = roomImgItems[0];
    }

    @Override
    public void onPrev() {
        if (expImage > 0) {
            --expImage;
        }
        viewPager.setCurrentItem(expImage);
    }

    @Override
    public void onComplete() {
        expImage = 0;
        viewPager.setCurrentItem(expImage);
        storiesProgressView.setStoriesCount(roomImgItems.length);
        storiesProgressView.setStoryDuration(6000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();
    }

    /*@Override
    public void onItemClick( String itemName ){
       if(itemName.equalsIgnoreCase("+More"))
       {

         //  guestAccessListAdapter.notifyItemInserted(guestAccess_model.size()-1);
          // ArrayList<Space_guestAccess_Model> itemmodel;
          guestAccessListAdapter.UpdateAdapter();
         // guestAccessListAdapter.addItem(new Space_guestAccess_Model("Less"));


        //   guestAccessListAdapter.update
       }

    }*/

    /*@Override
    public void onClick( String itemName ){
        if(itemName.equalsIgnoreCase("+More"))
        {

            //  guestAccessListAdapter.notifyItemInserted(guestAccess_model.size()-1);
            // ArrayList<Space_guestAccess_Model> itemmodel;
            serviceOfferAdapter.UpdateAdapter();
            // guestAccessListAdapter.addItem(new Space_guestAccess_Model("Less"));


            //   guestAccessListAdapter.update
        }

    }*/

    @Override
    public void onFeatureClick( String itemName ){
        if(itemName.equalsIgnoreCase("+More"))
        {

            //  guestAccessListAdapter.notifyItemInserted(guestAccess_model.size()-1);
            // ArrayList<Space_guestAccess_Model> itemmodel;
            specialFeatureAdapter.UpdateAdapter();
            // guestAccessListAdapter.addItem(new Space_guestAccess_Model("Less"));


            //   guestAccessListAdapter.update
        }

    }

    /*@Override
    public void onSpaceClick(String itemName) {
        if(itemName.equalsIgnoreCase("+More"))
        {

            //  guestAccessListAdapter.notifyItemInserted(guestAccess_model.size()-1);
            // ArrayList<Space_guestAccess_Model> itemmodel;
            spaceStyleAdapter.UpdateAdapter();
            // guestAccessListAdapter.addItem(new Space_guestAccess_Model("Less"));


            //   guestAccessListAdapter.update
        }


    }*/

    public class ImageAdapter extends PagerAdapter {
        Context context;

        String[] mThumbIds = roomImgItems;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            room_details_image = new ImageView(context);
            room_details_image.setScaleType(ImageView.ScaleType.FIT_XY);
//            room_details_imageBac = new ImageView(context);
//            room_details_image.setScaleType(ImageView.ScaleType.FIT_XY);
//            room_details_image.setBackground(getResources().getDrawable(R.drawable.grey_gradient_background));
            Log.e("ImageLength","ImageLenght"+mThumbIds.length);
            Glide.with(getApplicationContext()).load(mThumbIds[position])

                    .into(new DrawableImageViewTarget(room_details_image) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });
            ((ViewPager) container).addView(room_details_image, 0);
            //((ViewPager) container).addView(room_details_imageBac, 0);
            return room_details_image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }


    }


    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
