package com.makent.trioangle.newhome.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.newhome.adapter.ActivityAdapter;
import com.makent.trioangle.newhome.makentspacehome.Adapter.SpaceListAdapter;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeListModel;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeResult;
import com.makent.trioangle.newhome.makentspacehome.Model.HostActivityModel;
import com.makent.trioangle.newhome.models.ActivitiesList;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.newhome.models.ExploreList;
import com.makent.trioangle.newhome.models.Lists;
import com.makent.trioangle.newhome.utils.SpacesItemDecoration;
import com.makent.trioangle.travelling.CalendarActivity;
import com.makent.trioangle.travelling.FilterActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.MapActivity;
import com.makent.trioangle.travelling.Search_Guest_Bed;
import com.makent.trioangle.travelling.Search_anywhere;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makent.trioangle.helper.Constants.LanguageRecreate;
import static com.makent.trioangle.helper.Constants.isFilterApplied;
import static com.makent.trioangle.helper.Constants.type;
import static com.makent.trioangle.util.Enums.REQ_ACT_DATA;
import static com.makent.trioangle.util.Enums.REQ_SPACE_DATA;

public class NewHomeActivity extends AppCompatActivity implements ServiceListener, ActivityAdapter.ActvitityClickListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public @Inject
    CustomDialog customDialog;
    @BindView(R.id.cv_anywhere)
    public CardView cvAnywhere;
    @BindView(R.id.v_remove)
    public View vRemove;
    @BindView(R.id.tvExplore)
    public View tvExplore;
    @BindView(R.id.lltHome)
    public LinearLayout lltHome;
    @BindView(R.id.tv_dates)
    public TextView tvDates;
    @BindView(R.id.tv_guest)
    public TextView tvGuest;
    @BindView(R.id.llt_views)
    public LinearLayout lltViews;
    @BindView(R.id.fab)
    public FloatingActionButton fab;
    @BindView(R.id.iv_search)
    public ImageView ivSearch;
    @BindView(R.id.tv_cat)
    public TextView tvCategory;
    @BindView(R.id.tv_anywhere)
    public TextView tvAnywhere;
    @BindView(R.id.tv_filter)
    public TextView tvFilter;
    @BindView(R.id.explore_exp_category_title)
    public RelativeLayout explore_exp_category_title;
    public EditText edt;
    @BindView(R.id.nsv_explore)
    public NestedScrollView nsvExplore;

    @BindView(R.id.horizontal_scroll)
    public HorizontalScrollView horizontal_scroll;

    public String searchguest = "";
    public String searchlocation = "";
    public String searchlocationAddress = "";
    public String searchlocationcheck = "";
    public String searchcheckin = "";
    public String searchcheckout = "";
    public String searchcheckinout = "";
    public String searchinstantbook = "";
    public String searchamenities = "";
    public String searchroomtypes = "";
    public String searchbeds = "";
    public String searchbedrooms = "";
    public String searchbathrooms = "";
    public String searchminprice = "";
    public String searchmaxprice = "";
    public String searchlatitude = "";
    public String searchlongitude = "";
    public String searchSpace = "";
    public String searchSpecial = "";
    public String searchStyle = "";
    public String searchService = "";
    public String searchSpaceType = "";
    public String searchEventType = "";
    public String searchamenity = "";
    public String event_type_pos = "";


    List<String> searchamenitylist = new ArrayList<String>();
    List<String> searchEventlist = new ArrayList<>();
    List<String> searchSpacetypelist = new ArrayList<>();
    List<String> searchServicelist = new ArrayList<>();
    List<String> searchStylelist = new ArrayList<>();
    List<String> searchSpeciallist = new ArrayList<>();
    List<String> searchSpacelist = new ArrayList<>();

    protected boolean isInternetAvailable;
    boolean doubleBackToExitPressedOnce = false;

    // *** Search Type update ***
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    Snackbar snackbar;

    // added recycler:
    RecyclerView rvActivityList;
    ActivityAdapter activityAdapter;
    ArrayList<ActivitiesList> activitiesLists = new ArrayList<>();

    LinearLayoutManager layoutManager;
    boolean searchdates = false;
    int backPressed = 0;
    HomeActivity homeActivity;

    ArrayList<ExploreList> exploreList = new ArrayList<>();
    ArrayList<Lists> detailsLists = new ArrayList<>();
    ArrayList<Detail> categorydetailsLists = new ArrayList<>();
    ArrayList<ExploreDataModel> exploreDataModel = new ArrayList<>();

    String searchName;
    String searchTypeKey;
    int checkindex = 0;
    int index = 1;
    private long mLastClickTime = 0;
    private String mapFilterType;
    private String mapFilterTypeKey;
    private int filtercount = 0;
    private String getPreferedlanguage;

    private Boolean backArrowBoolean = false;
    // stay, experience , all

    private String listType;
    private int totalpages;

    private String filtercategoryName;
    private boolean onExpCategorySelected;
    private String startTime = "";
    private String endTime = "";

    /*For makent space adapter add:
     * */

    SpaceListAdapter spaceListingAdapter;

    ArrayList<HomeListModel> spaceListModels = new ArrayList<HomeListModel>();
    ArrayList<HomeListModel> spaceList = new ArrayList<>();

    HomeResult spaceResult;

    String homeSpacePageno;
    String activityName = "";
    String activityKey = "";
    private boolean activityCate;
    private RecyclerView rvSpaceList;
    String filter_apply = "";
    String eventname = "";
    private boolean isFilterApply = false;


    @OnClick(R.id.cv_anywhere)
    public void onAnywhere() {
        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        Intent x = new Intent(getApplicationContext(), Search_anywhere.class);
        startActivity(x);
    }

    @OnClick(R.id.explore_remove)
    public void setRemoveFilter() {
        //clearOrUpdateDatas();
        viewAllSpaceData();
    }

    @OnClick(R.id.tv_dates)
    public void setontvDates() {
        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "0");
        Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
        x.putExtra("type", "search");
        startActivity(x);
    }

    @OnClick(R.id.tv_guest)
    public void setontvGuest() {
        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        Intent x = new Intent(getApplicationContext(), Search_Guest_Bed.class);
        // x.putExtra("search", "1");
        startActivity(x);
    }

    @OnClick(R.id.tv_filter)
    public void setontvFilter() {
        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        System.out.println("Filter redirection : " + localSharedPreferences.getSharedPreferences(Constants.SearchLocation));
        Intent x = new Intent(this, FilterActivity.class);
        //  x.putExtra("type", "home");
        startActivity(x);
    }

    @OnClick(R.id.fab)
    public void mapFab() {
        // Preventing multiple clicks, using threshold of 2 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }


        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, "Rooms");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exploreDataModel);
        editor.putString("explore", json);
        editor.apply();

        Intent x = new Intent(this, MapActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("searchlist", spaceListModels);
        x.putExtra("BUNDLE", args);
        startActivity(x);
        // ***Experience Start***

// ***Experience End***
        /*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(categorydetailsLists);
        editor.putString("showAllMap", json);
        editor.apply();

        Intent x = new Intent(this, ShowAllMapActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("showAllMap", categorydetailsLists);
        x.putExtra("BUNDLE", args);
        startActivity(x);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        commonMethods = new CommonMethods();

        System.out.println("OnCreate");
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();

        localSharedPreferences = new LocalSharedPreferences(this);

        Log.e("NewHomeActivity", "NewHomeActivity");

        homeActivity = new HomeActivity();
        rvActivityList = findViewById(R.id.rvActivityList);
        rvSpaceList = findViewById(R.id.rvSpaceList);

        edt = findViewById(R.id.edt);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        getPreferedlanguage = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        isFilterApply = localSharedPreferences.getSharedPreferencesBool(Constants.isFilterApplied);

        if (getPreferedlanguage != null && !getPreferedlanguage.equals("")) {
            setLocale(getPreferedlanguage);
        } else {
            setLocale("en");
        }

        initValues();
        initView();

        horizontal_scroll.setVisibility(View.GONE);

        //spacePageno = "1";
        homeSpacePageno = "1";
        Intent x = getIntent();
        filter_apply = x.getStringExtra("filter");
        System.out.println("Filter Applied" + filter_apply);
        System.out.println("Filter activityKey" + activityKey);
        if (isFilterApply) {
            loadMoreSpace();

            horizontal_scroll.setVisibility(View.VISIBLE);
            rvActivityList.setVisibility(View.GONE);
            rvSpaceList.setVisibility(View.GONE);
            eventname = localSharedPreferences.getSharedPreferences(Constants.EventName);
            localSharedPreferences.saveSharedPreferences(Constants.activityName, eventname);
        } else {

            if (!mydialog.isShowing()) {
                mydialog.show();
            }

            homeSpacePageno = "1";
            spaceListModels.clear();
            loadSpace();

            getActivitySpace();
            rvActivityList.setVisibility(View.VISIBLE);

            horizontal_scroll.setVisibility(View.GONE);
            rvSpaceList.setVisibility(View.VISIBLE);
        }

        System.out.println(searchlocation);
        getSpaceDatas();
        System.out.println(searchlocation);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backArrowBoolean) {
                    //onBackPressed();
                    viewAllSpaceData();
                }
            }
        });
    }

    private void viewAllSpaceData() {
        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
        localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,false);
        isFilterApply = localSharedPreferences.getSharedPreferencesBool(Constants.isFilterApplied);

        //clearHomeFilters();
        clearFilters(0);
        getSpaceDatas();
        setDatasFromLocalSharedPreferences();
        vRemove.setVisibility(View.GONE);
        lltViews.setVisibility(View.VISIBLE);
        nsvExplore.setVisibility(View.VISIBLE);
        lltHome.setVisibility(View.VISIBLE);
        //onBack();
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        getActivitySpace();
        rvActivityList.setVisibility(View.VISIBLE);

        horizontal_scroll.setVisibility(View.GONE);
        rvSpaceList.setVisibility(View.VISIBLE);
    }

    private void clearFilters(int removeAll) {

        backArrowBoolean = false;
        ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
        commonMethods.NotrotateArrow(ivSearch, this);
        listType = null;
        exploreList.clear();
        detailsLists.clear();

        checkindex = 0;
        index = 1;

        nsvExplore.scrollTo(0, 0);

        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
        System.out.println("Search location : three " + localSharedPreferences.getSharedPreferences(Constants.SearchLocation));
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
        localSharedPreferences.saveSharedPreferences(Constants.StartTime, null);
        localSharedPreferences.saveSharedPreferences(Constants.EndTime, null);
        type = null;
        clearHomeFilters();

        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterEventType, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);

        localSharedPreferences.saveSharedPreferences(Constants.GuestLastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestFirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Schedule_id, null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedExpPrice, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExpId, null);
        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestImage, null);

        if (removeAll != 1) {
            localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType, null);
            localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType_key, null);
        }
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType_key, null);
        localSharedPreferences.saveSharedPreferences(Constants.HomePage, "Home");
        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
        localSharedPreferences.saveSharedPreferences(Constants.HomeShowAll, 0);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckAvailableScreen, null);

        initValues();

        homeSpacePageno = "1";
        spaceListModels.clear();
        loadSpace();
        //getHomeExplores();

        getActivitySpace();

        //loadMoreSpace();

        localSharedPreferences.saveSharedPreferences(activityName, null);
    }

    private void clearHomeFilters() {
        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpaceType, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpace, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpecial, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterStyle, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterService, null);
        localSharedPreferences.saveSharedPreferences(Constants.EventName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenity, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterEventType, null);
        filtercount = 0;
    }


    private void addLoaderInSpaceHomeList() {
        spaceListModels.add(new HomeListModel("load"));
        spaceListingAdapter.notifyItemInserted(0);

        rvSpaceList.setNestedScrollingEnabled(false);
        rvSpaceList.setLayoutManager(layoutManager);
        rvSpaceList.setAdapter(spaceListingAdapter);

    }


    private void initValues() {

        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        searchlocationAddress = localSharedPreferences.getSharedPreferences(Constants.searchlocationAddress);
        searchlocationcheck = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        searchcheckinout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut);
        searchinstantbook = localSharedPreferences.getSharedPreferences(Constants.FilterInstantBook);
        searchamenities = localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);
        searchroomtypes = localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes);
        searchbeds = localSharedPreferences.getSharedPreferences(Constants.FilterBeds);
        searchbedrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBedRoom);
        searchbathrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBathRoom);
        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPriceCheck);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPriceCheck);

        searchlatitude = localSharedPreferences.getSharedPreferences(Constants.SearchLocationLatitude);
        searchlongitude = localSharedPreferences.getSharedPreferences(Constants.SearchLocationLongitude);
        listType = localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType_key);

        filtercategoryName = localSharedPreferences.getSharedPreferences(Constants.FilterCategoryName);

        //For makent Space filter count add:
        searchSpace = localSharedPreferences.getSharedPreferences(Constants.FilterSpace);
        searchSpecial = localSharedPreferences.getSharedPreferences(Constants.FilterSpecial);
        searchStyle = localSharedPreferences.getSharedPreferences(Constants.FilterStyle);
        searchService = localSharedPreferences.getSharedPreferences(Constants.FilterService);
        searchSpaceType = localSharedPreferences.getSharedPreferences(Constants.FilterSpaceType);
        searchEventType = localSharedPreferences.getSharedPreferences(Constants.EventName);
        searchamenity = localSharedPreferences.getSharedPreferences(Constants.FilterAmenity);

        event_type_pos = localSharedPreferences.getSharedPreferences(Constants.FilterEventType);
        activityKey = localSharedPreferences.getSharedPreferences(Constants.FilterEventType);

        startTime = localSharedPreferences.getSharedPreferences(Constants.StartTime);
        endTime = localSharedPreferences.getSharedPreferences(Constants.EndTime);

        updateSearchList(searchamenity, searchamenitylist);
        updateSearchList(searchEventType, searchEventlist);
        updateSearchList(searchSpaceType, searchSpacetypelist);
        updateSearchList(searchService, searchServicelist);
        updateSearchList(searchStyle, searchStylelist);
        updateSearchList(searchSpecial, searchSpeciallist);
        updateSearchList(searchSpace, searchSpacelist);

        if(searchSpaceType==null&&searchinstantbook==null&&searchminprice==null&&searchmaxprice==null&&activityKey==null&&searchlocation==null&&searchamenity==null&&searchService==null&&searchSpace==null&&searchStyle==null&&searchSpecial==null&&searchguest==null&&startTime==null&&endTime==null&&searchcheckin==null&& searchcheckout==null){
            isFilterApply = false;
        }else{
            isFilterApply = true;
        }
        String anywhere, nearby;
        anywhere = getResources().getString(R.string.anywhere);
        nearby = getResources().getString(R.string.nearby);
        System.out.println("search location check : " + searchlocationcheck);
        System.out.println("search location : first " + searchlocation);
        System.out.println("search location address : " + localSharedPreferences.getSharedPreferences(Constants.searchlocationAddress));
        if (searchlocationcheck != null) {
            if (searchlocationcheck.equals(anywhere)) {
                searchlocation = null;
            } else if (searchlocationcheck.equals(nearby)) {
                searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocationNearby);
            }
        }
        setDatasFromLocalSharedPreferences();
    }

    private void updateSearchList(String searchString, List<String> searchList) {

        if (searchString != null) {
            String[] split = searchString.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchList.contains(split[i])) {
                    searchList.add(split[i]);
                }
            }
        }

    }

    private void setDatasFromLocalSharedPreferences() {
        if (searchguest != null) {
            tvGuest.setText(searchguest + " " + getResources().getString(R.string.guest_g));
            localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
            tvGuest.setTextColor(getResources().getColor(R.color.white_bg));
            tvGuest.setBackground(getResources().getDrawable(R.drawable.d_blue_border_home));
            //tvGuest.setPaddingRelative(35, 13, 35, 13);
        } else {
            tvGuest.setText(getResources().getString(R.string.guest_g));
            //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged,0);
            tvGuest.setTextColor(getResources().getColor(R.color.text_shadow1));
            tvGuest.setBackground(getResources().getDrawable(R.drawable.d_gray_border_home));
            //tvGuest.setPaddingRelative(35, 13, 35, 13);
        }

        if (searchcheckin != null && searchcheckout != null) {
            localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
            tvDates.setText(searchcheckinout);
            tvDates.setTextColor(getResources().getColor(R.color.white_bg));
            tvDates.setBackground(getResources().getDrawable(R.drawable.d_blue_border_home));
            //tvDates.setPaddingRelative(35, 13, 35, 13);
            searchdates = true;
        } else {
            tvDates.setText(getResources().getString(R.string.dates));
            //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged,0);
            tvDates.setTextColor(getResources().getColor(R.color.text_shadow));
            tvDates.setBackground(getResources().getDrawable(R.drawable.d_gray_border_home));
            //tvDates.setPaddingRelative(35, 13, 35, 13);
        }

        if (searchlocation != null) {
            System.out.println("searchlocationcheck " + searchlocation);
            tvAnywhere.setTextColor(getResources().getColor(R.color.text_shadow));
            String[] address = searchlocation.split(",");
            System.out.println("address " + address[0]);
            System.out.println("searchlocationAddress " + searchlocationAddress);
            if (searchlocationAddress.equalsIgnoreCase(getResources().getString(R.string.nearby))) {
                localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, getResources().getString(R.string.nearby));
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, address[0]);
            }
            localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
            tvAnywhere.setText(address[0]);
        } else {
            tvAnywhere.setText(getResources().getString(R.string.anywhere));
            tvAnywhere.setTextColor(getResources().getColor(R.color.text_light_gray));
            //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged,0);
        }


        System.out.println("searchEventlist " + searchEventlist.size());
        System.out.println("searchEventType " + searchEventType);

        if (searchEventType != null) {
            filtercount = filtercount + 1;
        }
        if (searchSpaceType != null) {
            System.out.println("SpaceTypeList " + searchSpacetypelist.size());
            filtercount = filtercount + searchSpacetypelist.size();
        }

        if (searchSpace != null) {
            filtercount = filtercount + searchSpacelist.size();
        }
        if (searchSpecial != null) {
            filtercount = filtercount + searchSpeciallist.size();
        }
        if (searchService != null) {
            filtercount = filtercount + searchServicelist.size();
        }
        if (searchStyle != null) {
            filtercount = filtercount + searchStylelist.size();
        }

        if (searchamenity != null) {
            filtercount = filtercount + searchamenitylist.size();
        }

        if (searchinstantbook != null) {
            filtercount = filtercount + 1;
        }

        if (searchminprice != null && searchmaxprice != null) {
            filtercount = filtercount + 1;
        }


        if (filtercount == 0) {
            tvFilter.setBackground(getResources().getDrawable(R.drawable.d_gray_border_home));
            tvFilter.setText(getResources().getString(R.string.filter));
            tvFilter.setTextColor(getResources().getColor(R.color.text_shadow));
            //tvFilter.setPaddingRelative(35, 13, 35, 13);
        } else {
            tvFilter.setBackground(getResources().getDrawable(R.drawable.d_blue_border_home));
            tvFilter.setText(getResources().getString(R.string.filters_s) + " • " + filtercount);
            tvFilter.setTextColor(getResources().getColor(R.color.white_bg));
            //tvFilter.setPaddingRelative(35, 13, 35, 13);
        }
    }

    /**
     * init all Recyclerview and its Loadmore
     */
    private void initView() {
        explore_exp_category_title = findViewById(R.id.explore_exp_category_title);

        rvActivityList.addItemDecoration(new SpacesItemDecoration(15, ""));

        spaceListingAdapter = new SpaceListAdapter(this, this, spaceListModels, nsvExplore);
        spaceListingAdapter.setLoadMoreListener(new SpaceListAdapter.OnHomeLoadMoreListener() {
            @Override
            public void onHomeLoadMore() {
                System.out.println("Yes it is working");
                rvSpaceList.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Space totalpages " + totalpages);
                        System.out.println("Space index " + index);

                        if (index <= totalpages) {
                            checkindex = checkindex + 1;
                            System.out.println("\n Curent loaded page pages in checkindex " + checkindex);
                            if (checkindex >= 1) {
                                index = index + 1;
                                SpaceLoadMore(index);
                            }
                        }
                    }
                });
            }
        });

    }

    /**
     * For makent space
     */

    public void getSpaceDatas() {
        if (localSharedPreferences.getSharedPreferencesInt(Constants.searchIconChanged) == 1) {
            if (searchlocationAddress != null) {
                if (localSharedPreferences.getSharedPreferences(Constants.activityName) != null) {
                    tvAnywhere.setText(searchlocationAddress + " • " + localSharedPreferences.getSharedPreferences(Constants.activityName));
                    backArrowBoolean = true;
                    ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.small_arrow_back));
                } else {
                    tvAnywhere.setText(searchlocationAddress);
                    System.out.println("Location Added " + searchlocationAddress);
                    backArrowBoolean = true;
                    ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.small_arrow_back));
                }
            } else {
                System.out.println("Activity Name : " + localSharedPreferences.getSharedPreferences(Constants.activityName));
                if (localSharedPreferences.getSharedPreferences(Constants.activityName) != null) {
                    tvAnywhere.setText(getResources().getString(R.string.anywhere) + " • " + localSharedPreferences.getSharedPreferences(Constants.activityName));
                    backArrowBoolean = true;
                    ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.small_arrow_back));
                } else {
                    tvAnywhere.setText(getResources().getString(R.string.anywhere));
                    backArrowBoolean = false;
                    ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
                }
            }
            // backArrowBoolean = true;
            commonMethods.rotateArrow(ivSearch, this);

        } else {
            tvAnywhere.setText(getResources().getString(R.string.anywhere));
            backArrowBoolean = false;
            ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
        }
        /*else {
            getActivitySpace();

        }*/
    }

    /**
     * Home Api
     */
    public void getHomeExplores() {

        if (localSharedPreferences.getSharedPreferencesInt(Constants.searchIconChanged) == 1) {
            if (searchlocation != null) {
                if (localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType) != null) {
                    if (filtercategoryName != null) {
                        tvAnywhere.setText(searchlocationAddress + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType) + " • " + filtercategoryName);
                    } else {
                        tvAnywhere.setText(searchlocationAddress + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType));
                    }
                } else {
                    tvAnywhere.setText(searchlocationAddress);
                }
            } else {
                if (localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType) != null) {
                    if (filtercategoryName != null) {
                        tvAnywhere.setText(getResources().getString(R.string.anywhere) + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType) + " • " + filtercategoryName);
                    } else {
                        tvAnywhere.setText(getResources().getString(R.string.anywhere) + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType));
                    }
                } else {
                    tvAnywhere.setText(getResources().getString(R.string.anywhere));
                }
            }
            backArrowBoolean = true;
            //ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.small_arrow_back));
            commonMethods.rotateArrow(ivSearch, this);

            String redirectPage = localSharedPreferences.getSharedPreferences(Constants.HomePage);

            if (redirectPage != null) {
                if (redirectPage.equalsIgnoreCase("Home")) {
                    fab.setEnabled(false);

                } else {
                    checkindex = 0;
                    index = 1;
                    //spacePageno = "1";
                    homeSpacePageno = "1";
                    ShowAllExploreData();
                }
            }
        } else {
            fab.setEnabled(false);
        }

    }


    /*
     * Home Api Makent Space
     * */

    public void getActivitySpace() {
        apiService.search_filter(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_ACT_DATA, this));
        ;
    }


    private void loadMoreSpace() {
        fab.setEnabled(false);
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        apiService.exploreHome(localSharedPreferences.getSharedPreferences(Constants.AccessToken), homeSpacePageno, searchSpaceType, searchinstantbook, searchminprice, searchmaxprice, activityKey, searchlocation, searchamenity, searchService, searchSpace, searchStyle, searchSpecial, searchguest, startTime, endTime, searchcheckin, searchcheckout).enqueue(new RequestCallback(REQ_SPACE_DATA, this));
    }

    private void SpaceLoadMore(int index) {
        if (index > 1) {
            spaceListModels.add(new HomeListModel("load"));
            spaceListingAdapter.notifyItemInserted(spaceListModels.size() - 1);
        }
        homeSpacePageno = Integer.toString(index);
        System.out.println("\n Curent loaded page pages in explore page" + index + "   " + homeSpacePageno);
        //loadSpace();
        loadMoreSpace();
    }

    /**
     * To call search api without filters
     */

    private void loadSpace() {
        fab.setEnabled(false);
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.exploreSpace(localSharedPreferences.getSharedPreferences(Constants.AccessToken), homeSpacePageno).enqueue(new RequestCallback(REQ_SPACE_DATA, this));
        ;
    }


    /**
     * Check Internet Connections
     *
     * @return
     */
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("OnResume", "OnResume");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            //new LoadInboxDetails().execute();
        } else {
            snackBar();
            //  commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, edt, edt, getResources(), this);
        }

        /**
         * Need to Reload the HomeActivity when Some datas where Shared
         */
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

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) return;
        }
        /**
         * Note
         * For Load More datas like Home and Experience a method is used to remove the loader function so common method Snackbar is removed
         *
         * But For Show More the resultTemp is diff so CommonMethods.snackbar is added
         */
        switch (jsonResp.getRequestCode()) {

            //newly inserted activity types:
            case REQ_ACT_DATA:
                if (jsonResp.isSuccess()) {
                    onSuccessAllDataActivity(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, rvActivityList, getResources(), this);
                }
                break;

            case REQ_SPACE_DATA:
                if (mydialog.isShowing()) mydialog.dismiss();
                if (jsonResp.isSuccess()) {
                    System.out.println("TAGGED : Dialog dismissed ");
                    onSuccessSpace(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (homeSpacePageno.equals("1")) {
                        if (spaceListModels.size() > 0) {
                            spaceListModels.remove(spaceListModels.size() - 1);
                            spaceListingAdapter.setMoreDataAvailable(false);
                            spaceListingAdapter.notifyDataChanged();
                        } else {
                            removeAllViews();
                        }

                    } else {
                        if (spaceListModels.size() > 0)
                            spaceListModels.remove(spaceListModels.size() - 1);
                        spaceListingAdapter.setMoreDataAvailable(false);
                        spaceListingAdapter.notifyDataChanged();
                        Snackbar snackbar = Snackbar
                                .make(rvSpaceList, "No more data available...", Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                        snackbar.show();

                    }
                    //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, rvHomeList, getResources(), this);
                }
                fab.setEnabled(true);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();

    }

    /*
    Get All data from search_api
    * */
    private void onSuccessAllDataActivity(JsonResponse jsonResponse) {
        activitiesLists.clear();
        HostActivityModel activityModel = gson.fromJson(jsonResponse.getStrResponse(), HostActivityModel.class);
        activitiesLists = activityModel.getActivitiesLists();
        loadActivityData();
    }

    /*
     * Load Activities Data:
     *
     * */

    private void loadActivityData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activityAdapter = new ActivityAdapter(activitiesLists, this, this);
        tvExplore.setVisibility(View.VISIBLE);
        explore_exp_category_title.setVisibility(View.GONE);
        rvActivityList.setHasFixedSize(true);
        rvActivityList.setLayoutManager(layoutManager);
        rvActivityList.setAdapter(activityAdapter);
        //rvHomeList.setVisibility(View.GONE);
    }


    private void onSuccessSpace(JsonResponse jsonResponse) {
        if (spaceListModels.size() == 0) {
            addLoaderInSpaceHomeList();
        }
        spaceListingAdapter.setLoadScroll(rvSpaceList, nsvExplore);
        rvSpaceList.setVisibility(View.VISIBLE);
        rvSpaceList.setNestedScrollingEnabled(false);
        nsvExplore.setVisibility(View.VISIBLE);
        System.out.println("TAGGED : RecyclerView Visibled  ");
        if (homeSpacePageno.equals("1")) {
            nsvExplore.scrollTo(0, 0);
        }

        System.out.println("TAGGED : Scrollview scrolled to top  ");
        //onSuccessGetSpaceExplore(jsonResponse);
        onSuccessGetSpace(jsonResponse);
    }


    private void onSuccessGetSpace(JsonResponse jsonResponse) {
        spaceResult = gson.fromJson(jsonResponse.getStrResponse(), HomeResult.class);
        String totalPage = spaceResult.getTotalPage();
        totalpages = Integer.parseInt(totalPage);
        String minPrice = spaceResult.getMinprice();
        String maxPrice = spaceResult.getMaxprice();

        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPrice, minPrice);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPrice, maxPrice);
        localSharedPreferences.saveSharedPreferences(Constants.TotalPage, totalPage);

        if (spaceListModels.size() > 0) spaceListModels.remove(spaceListModels.size() - 1);
        spaceList = spaceResult.getHomeListModels();
        //System.out.println("Homelistmodel size:  " + homeList.size());
        if (spaceList != null && spaceList.size() > 0) {
            spaceListModels.addAll(spaceList);
            System.out.println("expDatamodelAfter " + spaceListModels.size());
            if (spaceListModels != null && spaceListModels.size() > 0) {
                for (int i = 0; i < spaceListModels.size(); i++) {
                    spaceListModels.get(i).setType("item");
                    System.out.println("currencySymbol" + spaceListModels.get(i).getCurrencySymbol());
                    String currencysymbol = Html.fromHtml(spaceListModels.get(i).getCurrencySymbol()).toString();
                    spaceListModels.get(i).setCurrencySymbol(currencysymbol);
                    // System.out.println("Success incoming check " + homeListModels.get(i).getType() + " " + homeListModels.get(i).getSpaceID());
                }
            }
            spaceListingAdapter.notifyDataChanged();
            System.out.println("TAGGED : Adapter notified ");
            Constants.LoadMore = true;
            fab.show();

        } else {
            removeAllViews();
        }
    }


    /*For Makent Space:
     * */
    @Override
    public void onActivityClick(String name, String id) {
      /*  activityName = activitiesLists.get(position).getName();
        activityKey = activitiesLists.get(position).getId();*/
        activityKey = id;
        activityName = name;

        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
        localSharedPreferences.saveSharedPreferences(Constants.activityName, name);
        localSharedPreferences.saveSharedPreferences(Constants.activityId, id);
        localSharedPreferences.saveSharedPreferences(Constants.EventName, activityName);
        localSharedPreferences.saveSharedPreferences(Constants.FilterEventType, String.valueOf(activityKey));
        searchEventType = activityName;

        activityCate = true;
        if (searchlocation == null) {
            tvAnywhere.setText(getResources().getString(R.string.anywhere) + " • " + name);
        }

        rvActivityList.setVisibility(View.GONE);
        tvExplore.setVisibility(View.GONE);

        nsvExplore.setVisibility(View.VISIBLE);
        horizontal_scroll.setVisibility(View.VISIBLE);
        fab.show();
        homeSpacePageno = "1";
        spaceListModels.clear();
        //initValues();
        //loadMoreSpace();
        updateSearchList(searchEventType, searchEventlist);
        setDatasFromLocalSharedPreferences();
        getSpaceDatas();
        fab.setEnabled(false);
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        System.out.println("Activity Key : " + activityKey);
        apiService.exploreHome(localSharedPreferences.getSharedPreferences(Constants.AccessToken), homeSpacePageno, searchSpaceType, searchinstantbook, searchminprice, searchmaxprice, activityKey, searchlocation, searchamenity, searchService, searchSpace, searchStyle, searchSpecial, searchguest, startTime, endTime, searchcheckin, searchcheckout).enqueue(new RequestCallback(REQ_SPACE_DATA, this));

        //initialize();
    }


    /**
     * Show More Datas in Home
     */
    public void ShowAllHomeDatas() {
        lltHome.setVisibility(View.GONE);
        explore_exp_category_title.setVisibility(View.GONE);
        nsvExplore.scrollTo(0, 0);

        mapFilterTypeKey = localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType);


        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
        backArrowBoolean = true;
        //ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.small_arrow_back));

        commonMethods.rotateArrow(ivSearch, this);

        if (searchlocation != null) {
            tvAnywhere.setText(searchlocationAddress + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType));
        } else {
            tvAnywhere.setText(getResources().getString(R.string.anywhere) + " • " + localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType));
        }
        listType = localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType_key);
        type = listType;

        //tvFilter.setVisibility(View.GONE);
        fab.setEnabled(false);
    }

    /**
     * Show All Home or Exp  datas
     */
    public void ShowAllExploreData() {
        lltHome.setVisibility(View.GONE);
        explore_exp_category_title.setVisibility(View.GONE);

        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
        backArrowBoolean = true;

        commonMethods.rotateArrow(ivSearch, this);

        if (localSharedPreferences.getSharedPreferences(Constants.ExploreHomeType_key) != null) {
            mapFilterType = localSharedPreferences.getSharedPreferences(Constants.ExploreHomeType);
            mapFilterTypeKey = localSharedPreferences.getSharedPreferences(Constants.ExploreHomeType_key);
        } else {
            mapFilterType = localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType);
            mapFilterTypeKey = localSharedPreferences.getSharedPreferences(Constants.ExploreRoom_ExpType_key);
        }

        if (mapFilterTypeKey == null) {
            mapFilterTypeKey = "";
        }
        String locations = "";
        if (searchlocation != null) {
            locations = searchlocationAddress;
        } else {
            locations = getResources().getString(R.string.anywhere);
        }
        if (mapFilterType == null) {
            mapFilterType = "";
            tvAnywhere.setText(locations);
        } else {
            if (filtercategoryName != null) {
                tvAnywhere.setText(locations + " • " + mapFilterType + " • " + filtercategoryName);
            } else {
                tvAnywhere.setText(locations + " • " + mapFilterType);
            }
        }
        type = mapFilterTypeKey;
        if (!mapFilterTypeKey.equalsIgnoreCase("Experiences") && localSharedPreferences.getSharedPreferencesInt(Constants.HomeShowAll) != 1) {
            tvFilter.setVisibility(View.VISIBLE);
        } else {
            // tvCategory.setVisibility(View.VISIBLE);
        }
        fab.setEnabled(false);

    }


    /**
     * Clear all Local variables
     */
    public void clearSavedData(int removeAll) {
        backArrowBoolean = false;
        ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
        commonMethods.NotrotateArrow(ivSearch, this);
        listType = null;
        exploreList.clear();
        detailsLists.clear();

        checkindex = 0;
        index = 1;

        nsvExplore.scrollTo(0, 0);

        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
        System.out.println("Search location : one " + localSharedPreferences.getSharedPreferences(Constants.SearchLocation));
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.StartTime, null);
        localSharedPreferences.saveSharedPreferences(Constants.EndTime, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
        type = null;
        clearHomeFilters();

        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);

        localSharedPreferences.saveSharedPreferences(Constants.GuestLastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestFirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Schedule_id, null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedExpPrice, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExpId, null);
        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestImage, null);


        localSharedPreferences.saveSharedPreferences(Constants.activityName, null);
        localSharedPreferences.saveSharedPreferences(Constants.activityId, null);
        localSharedPreferences.saveSharedPreferences(Constants.EventName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterEventType, null);


        if (removeAll != 1) {
            localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType, null);
            localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType_key, null);
        }
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType_key, null);
        localSharedPreferences.saveSharedPreferences(Constants.HomePage, "Home");
        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
        localSharedPreferences.saveSharedPreferences(Constants.HomeShowAll, 0);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckAvailableScreen, null);

        initValues();

        getHomeExplores();

        getActivitySpace();

        loadMoreSpace();

        localSharedPreferences.saveSharedPreferences(activityName, null);
    }

    private void clearSearchedFilter() {
        listType = null;
        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
        System.out.println("Search location : two " + localSharedPreferences.getSharedPreferences(Constants.SearchLocation));
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");

        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);

        //localSharedPreferences.saveSharedPreferences(Constants.HomePage, "Home");

    }

    /**
     * Set Default Mobile lang Prev Selected
     *
     * @param lang
     */
    public void setLocale(String lang) {

        boolean isrecreate = localSharedPreferences.getSharedPreferencesBool(LanguageRecreate);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        if (isrecreate) {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
            localSharedPreferences.saveSharedPreferences(LanguageRecreate, false);
        }
    }

    public void snackBar() {
        // Create the Snackbar
        snackbar = Snackbar.make(lltHome, "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    initValues();
                    getHomeExplores();

                    //Makent space api:
                    getActivitySpace();
                    // getHomeSpace();
                    // new UpdateDateDetails().execute();
                } else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = snackView.findViewById(R.id.snackbar_text);

        if (!isInternetAvailable) {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }

    /**
     * OnBackPressed
     */
    public void onBackPressed() {

        System.out.println("on Back pressed : " +activityCate);
        System.out.println("dialog is showing : " +mydialog.isShowing());


        rvActivityList.setVisibility(View.VISIBLE);
        tvExplore.setVisibility(View.VISIBLE);

        horizontal_scroll.setVisibility(View.GONE);
        vRemove.setVisibility(View.GONE);
        ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
        localSharedPreferences.saveSharedPreferences(Constants.activityName, null);
        if (backArrowBoolean) {
            /*activityCate = false;
            getSpaceDatas();

            clearOrUpdateDatas();
            getActivitySpace();
            loadSpace();*/
            homeSpacePageno = "1";
            spaceListModels.clear();

            viewAllSpaceData();

        } else {
            onBackClick();
        }


    }


    /*For Makent Space:
     * */
    private void onBackClick() {
        if (localSharedPreferences.getSharedPreferencesInt(Constants.searchIconChanged) == 1) {
            localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
            backArrowBoolean = false;
            ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
            commonMethods.NotrotateArrow(ivSearch, this);
            if (searchlocation != null) {
                tvAnywhere.setText(searchlocationAddress);
            } else {
                tvAnywhere.setText(getResources().getString(R.string.anywhere));
            }
        } else {
            triggerToExit();
        }
    }

    /**
     * Change the Search icon and Clear datas
     */
    private void onBack() {
        if (localSharedPreferences.getSharedPreferencesInt(Constants.searchIconChanged) == 1) {
            localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
            localSharedPreferences.saveSharedPreferences(Constants.HomeShowAll, 0);
            lltHome.setVisibility(View.VISIBLE);
            nsvExplore.scrollTo(0, 0);


            checkindex = 0;
            index = 1;
            vRemove.setVisibility(View.GONE);

            explore_exp_category_title.setVisibility(View.GONE);
            //tvFilter.setVisibility(View.GONE);
            fab.hide();
            backArrowBoolean = false;
            ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_new));
            commonMethods.NotrotateArrow(ivSearch, this);
            if (searchlocation != null) {
                tvAnywhere.setText(searchlocationAddress);
            } else {
                tvAnywhere.setText(getResources().getString(R.string.anywhere));
            }
            clearSavedData(0);
        } else {
            triggerToExit();
        }
    }

    // onBack button pressed
    public void triggerToExit() {
        if (backPressed >= 1) {
            //Exit From APP
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
           /*
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();*/
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
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }


    /**
     * Show Empty view when Error or No data Found
     */
    private void removeAllViews() {
        lltViews.setVisibility(View.GONE);
        nsvExplore.setVisibility(View.GONE);
        explore_exp_category_title.setVisibility(View.GONE);
        fab.hide();


        vRemove.setVisibility(View.VISIBLE);

    }

    private void clearOrUpdateDatas() {


        homeSpacePageno = "1";

        checkindex = 0;
        index = 1;

        filtercategoryName = null;
        filtercount = 0;
        if (localSharedPreferences.getSharedPreferences(Constants.CheckIsFilterApplied) != null &&
                (localSharedPreferences.getSharedPreferences(Constants.CheckIsFilterApplied).equalsIgnoreCase("yes") || localSharedPreferences.getSharedPreferences(Constants.CheckIsFilterApplied).equalsIgnoreCase("showAll"))) {
            vRemove.setVisibility(View.GONE);
            lltViews.setVisibility(View.VISIBLE);
            nsvExplore.setVisibility(View.VISIBLE);
            lltHome.setVisibility(View.VISIBLE);

            nsvExplore.scrollTo(0, 0);
            tvFilter.setVisibility(View.VISIBLE);
            onExpCategorySelected = false;
            clearHomeFilters();
            clearSearchedFilter();
            initValues();
            if (localSharedPreferences.getSharedPreferences(Constants.CheckIsFilterApplied).equalsIgnoreCase("showAll")) {
                ShowAllHomeDatas();
            } else {
                ShowAllExploreData();
            }
            localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);

        } else {
            if (onExpCategorySelected) {
                onExpCategorySelected = false;
                vRemove.setVisibility(View.GONE);
                ShowAllExploreData();
            } else {
                onBack();
            }
        }
    }
}