package com.makent.trioangle.travelling.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestExploreSearchActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.HomeFragment;
import com.makent.trioangle.R;

import com.makent.trioangle.controller.LocalSharedPreferences;

import com.makent.trioangle.travelling.ViewPagerAdapter;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.model.explore.ExploreResult;
import com.makent.trioangle.travelling.CalendarActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.Search_Guest_Bed;
import com.makent.trioangle.travelling.Search_anywhere;
import com.makent.trioangle.util.CommonMethods;

import java.util.ArrayList;

import javax.inject.Inject;

// ***Experience Start***

// ***Experience End***

public class ExploreSearchActivity extends AppCompatActivity {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RecyclerView recyclerView;

    String TAG = "MainActivity - ";
    Context context;
    String pageno;
    Snackbar snackbar;
    public TabLayout tabLayout;

    TextView wheretxt, whentxt, guesttxt, explorefiltercount;
    private int backPressed = 0;    // used by onBackPressed()

    // String userid="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEwMDA2LCJpc3MiOiJodHRwOlwvXC9kZW1vLnRyaW9hbmdsZS5jb21cL21ha2VudFwvYXBpXC9sb2dpbiIsImlhdCI6MTQ4MzY4ODI3OCwiZXhwIjoxNDg2MjgwMjc4LCJuYmYiOjE0ODM2ODgyNzgsImp0aSI6ImMzYzQ1YTQwYjYyNDJmNDIwMzk4NDQ0OGRmMzc4MWViIn0.rIgDAM1et1xPINQHKWo0gxJ3jiyigrR4cuc4zGdwGqA";


    Button explore_map, explore_collapse, explore_clearall, explore_remove;
    RelativeLayout explore_filter;
    Button where, when, guest;

    LinearLayout exploreserarch, explorenodata;
    String type;
    public FrameLayout frame;
    public Animation slide_down, slide_up;
    public int index = 1;
    public int checkindex = 0;
    public int filtercount = 0;
    boolean searchdates = false;
    public String userid = "";
    public String searchguest = "";
    public String searchlocation = "";
    public String searchlocationcheck = "";
    public String searchcheckin = "";
    public String searchcheckout = "";
    public String searchcheckinout = "";
    public String isCheckAvailability = "";
    public String searchinstantbook = "";
    public String searchamenities = "";
    public String searchroomtypes = "";
    public String searchbeds = "";
    public String searchbedrooms = "";
    public String searchbathrooms = "";
    public String searchminprice = "";
    public String searchmaxprice = "";
    LocalSharedPreferences localSharedPreferences;
    LinearLayout mapfilter;
    int totalpages = 1;
    String url;// Explore search Url
    protected boolean isInternetAvailable;

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    public ViewPager viewPager;
    ExploreResult exploreResult;
    ArrayList<ExploreDataModel> exploreDataModel = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_tab);
        viewPager = (ViewPager) findViewById(R.id.pager);
        commonMethods = new CommonMethods();

        //   ***Exp***

        setupViewPager(viewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent x = getIntent();

        type = x.getStringExtra("type");

        if (type != null) {
            if (type.equals("home")) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);

            }
        }

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);



        localSharedPreferences = new LocalSharedPreferences(this);

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        searchlocationcheck = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        searchcheckinout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut);


        //filter page saved data
        Typeface font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent1));
        Typeface font2 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent2));
        Typeface font3 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent3));
        Typeface font4 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent4));


        searchinstantbook = localSharedPreferences.getSharedPreferences(Constants.FilterInstantBook);
        searchamenities = localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);
        searchroomtypes = localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes);
        searchbeds = localSharedPreferences.getSharedPreferences(Constants.FilterBeds);
        searchbedrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBedRoom);
        searchbathrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBathRoom);
        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPriceCheck);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPriceCheck);


        exploreserarch = (LinearLayout) findViewById(R.id.exploreserarch);
        frame = (FrameLayout) findViewById(R.id.exploreframe);

//Load animation
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar.setVisibility(View.GONE);


        AppBarLayout.OnOffsetChangedListener mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                appBarLayout.setVisibility(View.VISIBLE);


                if (verticalOffset == 0) {
                    System.out.println("Collapse verticalOffset" + verticalOffset);
                    System.out.println("Collapse Tool");

                } else {
                    System.out.println("Expand verticalOffset" + verticalOffset);
                    System.out.println("Expand Tool");
                    if (verticalOffset <= -350) {
                        toolbar.animate().alpha(1).setDuration(300);
                        toolbar.setVisibility(View.VISIBLE);
                        exploreserarch.setVisibility(View.VISIBLE);

                    } else if (verticalOffset >= -200) {
                        exploreserarch.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        frame.setVisibility(View.VISIBLE);

                    }
                }
            }
        };

        appBarLayout.addOnOffsetChangedListener(mListener);


        toolbar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                appBarLayout.setExpanded(true);
                toolbar.setVisibility(View.GONE);
                exploreserarch.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
                frame.startAnimation(slide_down);
                frame.animate().alpha(1).setDuration(1200);
                toolbar.animate().alpha(0).setDuration(1200);

            }
        });

        explore_collapse = (Button) findViewById(R.id.explore_collapse);
        commonMethods.setTvAlign(explore_collapse,this);
        explore_collapse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                frame.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                exploreserarch.setVisibility(View.VISIBLE);
                frame.animate().alpha(0).setDuration(1200);
                toolbar.animate().alpha(1).setDuration(300);

                toolbar
                        .animate()
                        .setDuration(500)
                        .translationY(0);

            }
        });


        whentxt = (TextView) findViewById(R.id.when_txt);
        wheretxt = (TextView) findViewById(R.id.where_txt);
        guesttxt = (TextView) findViewById(R.id.guest_txt);
        when = (Button) findViewById(R.id.when);
        where = (Button) findViewById(R.id.where);
        guest = (Button) findViewById(R.id.guest);
        Drawable icon = new FontIconDrawable(ExploreSearchActivity.this, getResources().getString(R.string.f4globe), font4)
                .sizeDp(21)
                .colorRes(R.color.title_text_color);
        where.setCompoundDrawables(icon, null, null, null);
        where.setCompoundDrawablePadding(20);
        Drawable icon1 = new FontIconDrawable(ExploreSearchActivity.this, getResources().getString(R.string.f3calendar), font3)
                .sizeDp(21)
                .colorRes(R.color.title_text_color);
        when.setCompoundDrawables(icon1, null, null, null);
        when.setCompoundDrawablePadding(20);

        Drawable icon2 = new FontIconDrawable(ExploreSearchActivity.this, getResources().getString(R.string.f1users), font1)
                .sizeDp(21)
                .colorRes(R.color.title_text_color);
        guest.setCompoundDrawables(icon2, null, null, null);
        guest.setCompoundDrawablePadding(20);


        when.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                //  frame.startAnimation(slide_up);
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "0");
                Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                x.putExtra("type", "search");
                startActivity(x);

            }
        });
        guest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                Intent x = new Intent(getApplicationContext(), Search_Guest_Bed.class);
                startActivity(x);

            }
        });
        where.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                Intent x = new Intent(getApplicationContext(), Search_anywhere.class);
                startActivity(x);

            }
        });

        if (searchguest != null) {
            guest.setText(searchguest + " " + getResources().getString(R.string.guests));
            guesttxt.setText(searchguest + " " + getResources().getString(R.string.guests));
        }

        if (searchcheckin != null && searchcheckout != null) {
            when.setText(searchcheckinout);
            whentxt.setText(searchcheckinout);
            searchdates = true;
        }
        if (searchlocationcheck != null) {
            where.setText(searchlocationcheck);
            String[] address = searchlocationcheck.split(",");
            wheretxt.setText(address[0]);
        }

        context = this;

        explore_clearall = (Button) findViewById(R.id.explore_clearall);
        explore_clearall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                clearSavedData();

                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                //x.putExtra("tabsaved", 0);
                startActivity(x);
                finish();


            }
        });

    }

//   ***Exp***


    /**
     * Set up a view pager fragement
     *
     * @param viewPager view to be passed to add fragment
     */

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Homes");
        // ***Experience Start***

        // ***Experience End***

        viewPager.setAdapter(adapter);
    }


    public void onBackPressed() {
        if (backPressed >= 1) {
            super.onBackPressed();

        } else {
            backPressed = backPressed + 1;
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);


        if (localSharedPreferences.getSharedPreferences(Constants.Reload) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, null);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
        }

    }


    public void clearSavedData() {
        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);
        //localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);

        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);


        localSharedPreferences.saveSharedPreferences(Constants.GuestLastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestFirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Schedule_id, null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedExpPrice, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExpId, null);
        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestImage, null);

        System.out.println("Saved Data cleared  : ");
    }

    public void onDestroy() {
        String clearData = localSharedPreferences.getSharedPreferences(Constants.ClearFilter);
        if (clearData == null) { // To prevent clearing data while hit back press from room details
            clearSavedData();
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.ClearFilter, null);
        }


        super.onDestroy();
    }


}
