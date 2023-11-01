package com.makent.trioangle.host.tabs;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostListingActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.ListingdetailsAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.EditListingActivity;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.HostListedModel;
import com.makent.trioangle.model.host.HostListingModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/*************************************************************
Host room listed, unlisted, steps completed, in completed  details
*************************************************************** */
public class HostListingActivity extends AppCompatActivity implements ServiceListener {

    FloatingActionButton fab;
    Animation slide_down,slide_up;
    RecyclerView recyclerView;
    SwipeRefreshLayout listswipeContainer;

    ListingdetailsAdapter adapter;
    Context context;
    int index=1;
    int checkindex=0;

    String pageno;
    LocalSharedPreferences localSharedPreferences;
    int totalpages=1;
    String userid;
    RelativeLayout emptylisting;
    protected boolean isInternetAvailable;

    HostListingModel hostListingModel;

    private List<HostListedModel> hostListedModels;

    Snackbar snackbar;
    private int backPressed = 0;    // used by onBackPressed()

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    private boolean doubleBackToExitPressedOnce=false;
    ImageView inbox_dot_loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_listing);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        inbox_dot_loader = (ImageView)findViewById(R.id.inbox_dot_loader);

        recyclerView = (RecyclerView) findViewById(R.id.listing_room_list);
        emptylisting = (RelativeLayout) findViewById(R.id.emptylisting);
        listswipeContainer = (SwipeRefreshLayout)findViewById(R.id.listswipeContainer);
        //list_dot_loader=(ImageView)findViewById(R.id.list_dot_loader);
        /*GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(list_dot_loader);
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1);*/

        hostListedModels = new ArrayList<>();
        Log.e("HostListActivity","HostListActivity");

        adapter = new ListingdetailsAdapter(this, hostListedModels);
        adapter.setLoadMoreListener(new ListingdetailsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("\n Total number of pages in explore page"+totalpages);
                        System.out.println("\n Curent loaded page pages in explore page"+index);

                        if(index<=totalpages) {
                            checkindex=checkindex+1;
                            System.out.println("\n Curent loaded page pages in checkindex"+checkindex);
                            if(checkindex>1) {
                                index = index + 1;
                                loadMore(index);
                            }
                        }
                    }
                });
            }
        });

        hostListedModels.add(new HostListedModel("load"));
        adapter.notifyItemInserted(0);
        System.out.println("Search list size"+hostListedModels.size());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        pageno="1";

        listswipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("OnSwipeRefresh","OnSwipeRefresh");
               // pageno = pageno +1;
                    pageno = "1";
                    listswipeContainer.setRefreshing(true);
                    //loadswipe();
                    apiService.listingDetail(userid, pageno).enqueue(new RequestCallback(HostListingActivity.this));
                    adapter.notifyDataChanged();

            }
        });

        /*if (isInternetAvailable){
            //new HostListing().execute();
            loadListingDetail();
        }else {
            snackBar();
           // commonMethods.snackBar(getResources().getString(R.string.interneterror), getResources().getString(R.string.retry), true, 2, recyclerView, getResources(), this);
        }*/

        //load(0);

        fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {



                Intent x = new Intent(getApplicationContext(), EditListingActivity.class);
                startActivity(x);
            }
        });

        //Load animation
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_bottom);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_bottom);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

               // System.out.println("DY"+dy);
                if (dy > 0) {
                    // fab.animate().translationY(300).setDuration(300);
                    // fab.startAnimation(slide_down);
                    //  fab.hide();
                }else if(dy<0)
                {
                    // fab.show();
                    //fab.startAnimation(slide_up);
                    // fab.animate().translationY(0).setDuration(300);
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              /*  if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    //fab.show();
                    fab.animate().translationY(0).setDuration(1000);

                }
                if (newState == RecyclerView.FOCUS_UP){
                   // fab.hide();
                    fab.animate().translationY(300).setDuration(1000);
                }*/
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    // Get Rooms details for host
    private void loadListingDetail() {

        apiService.listingDetail(userid,pageno).enqueue(new RequestCallback(this));

    }

    public void loadswipe(){
        if(index<=totalpages) {
            checkindex=checkindex+1;
            System.out.println("\n Curent loaded page pages in checkindex"+checkindex);
            if(checkindex>1) {
                index = index + 1;
                loadMore(index);
            }


        }
    }
    private void refreshList() {
        listswipeContainer.setRefreshing(false);

    }


    private void ListImageNull(){
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount,String.valueOf(0));
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomTitle,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomSummary,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomHouseRules,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomBookType,null);
        localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListLocationLat,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListLocationLong,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomPolicyType,null);
        // localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,null);
        // localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,null);

        localSharedPreferences.saveSharedPreferences(Constants.ListBeds,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListBathRooms,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListGuests,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount,String.valueOf(0));

        localSharedPreferences.saveSharedPreferences(Constants.ListWeeklyPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListMonthlyPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListGuestAfter,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice,null);


        localSharedPreferences.saveSharedPreferences(Constants.IsSpaceList,0);

        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);

        localSharedPreferences.saveSharedPreferences(Constants.City,null);
        localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
        localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
        localSharedPreferences.saveSharedPreferences(Constants.State,null);
        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
        localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
        localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, "");
        localSharedPreferences.saveSharedPreferences(Constants.MinimumStay, "");
        localSharedPreferences.saveSharedPreferences(Constants.MaximumStay, "");
        localSharedPreferences.saveSharedPreferences(Constants.AvailableRulesOption, "");
        localSharedPreferences.saveSharedPreferences(Constants.LastMinCount,"");
        localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount,"");
        localSharedPreferences.saveSharedPreferences(Constants.LengthOfStay,"");


    }


    private void loadMore(int index) {

        if(index>1) {
            hostListedModels.add(new HostListedModel("load"));
            adapter.notifyItemInserted(hostListedModels.size() - 1);
        }
        pageno=Integer.toString(index);
        System.out.println("\n Curent loaded page pages in explore page"+index+"   "+pageno);
        //new HostListing().execute();
        loadListingDetail();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        listswipeContainer.setRefreshing(false);
        if (jsonResp.isSuccess()) {
            //onSuccessListing(jsonResp); // onSuccess call method
        }else{
            if(pageno.equals("1")) {
                if (hostListedModels.size() > 0)
                hostListedModels.remove(hostListedModels.size() - 1);
                adapter.notifyDataChanged();
                emptylisting.setVisibility(View.VISIBLE);
            }else {
                emptylisting.setVisibility(View.GONE);
                adapter.setMoreDataAvailable(false);
                if (hostListedModels.size() > 0)
                    hostListedModels.remove(hostListedModels.size() - 1);
                adapter.notifyDataChanged();
                Snackbar snackbar = Snackbar
                        .make(recyclerView, getResources().getString(R.string.more_data), Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                snackbar.show();
            }
        }
    }


    private void onSuccessListing(JsonResponse jsonResp) {

        hostListingModel = gson.fromJson(jsonResp.getStrResponse(), HostListingModel.class);

            hostListedModels.clear();

           /* if(hostListingModel.getListed().size()>0&& pageno.equals("1")) {

                hostListedModels.add(new HostListedModel("listed"));
            }
            hostListedModels.addAll( hostListingModel.getListed());

            if(hostListingModel.getUnlisted().size()>0&& pageno.equals("1")) {
                hostListedModels.add(new HostListedModel("unlisted"));
            }
            hostListedModels.addAll( hostListingModel.getUnlisted());

            for(int i=0;i<hostListedModels.size();i++){
                if(hostListedModels.get(i).getType()==null)
                    hostListedModels.get(i).setType("item");
            }*/

            adapter.notifyDataChanged();
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        listswipeContainer.setRefreshing(false);
    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

// Show network error and exception
    public void snackBar()
    {
        // Create the Snackbar
        snackbar = Snackbar.make(emptylisting, "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    loadListingDetail();
                    //new HostListing().execute();
                }else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable){
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        if (!snackbar.isShown()) {
            snackbar.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            loadListingDetail();
            //new ExploreSearch().execute();
        } else {
           // commonMethods.snackBar(getResources().getString(R.string.interneterror), getResources().getString(R.string.retry), true, 2, recyclerView, getResources(), this);
             snackBar();
        }
    }
    public void onBackPressed()
    {
        if (backPressed >=1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
            // clean up
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



}
