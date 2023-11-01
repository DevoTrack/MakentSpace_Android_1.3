package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestTripDetailsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.TripsDetailsListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.tripsModel.TripDetailCurrentResult;
import com.makent.trioangle.model.tripsModel.TripDetailData;
import com.makent.trioangle.model.tripsModel.TripDetailPendingResult;
import com.makent.trioangle.model.tripsModel.TripDetailPreviousResult;
import com.makent.trioangle.model.tripsModel.TripDetailUpcomingResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is TripsDetails Page Contain Show the TripsDetails List
**************************************************************************  */
public class TripsDetails extends AppCompatActivity implements ServiceListener
{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    private SwipeRefreshLayout swipeContainer;
    RecyclerView listView;
    List<Makent_model> searchlist;
    TripsDetailsListAdapter adapter;
    Context context;
    ImageView tripsdetails_dot_loader;
    RelativeLayout tripdetailsback;
    protected boolean isInternetAvailable;
    String userid,triptype,triptypes;
    LocalSharedPreferences localSharedPreferences;
    String totaltrips;
    TripDetailPendingResult tripsResult;
    TripDetailPreviousResult tripsPreviousResult;
    TripDetailUpcomingResult tripsUpcomingResult;
    TripDetailCurrentResult tripCurrentResult;
    ArrayList <TripDetailData> Trips=new ArrayList<>();
    public EditText edt;
    public ImageView wishlistdetails_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_details);
        ButterKnife.bind(this);
        commonMethods = new CommonMethods();

        Log.e("TripDetail","Booking Trip Details");

        AppController.getAppComponent().inject(this);
        tripdetailsback = (RelativeLayout) findViewById(R.id.tripdetailsback);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        tripdetailsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
        edt = (EditText) findViewById(R.id.edt);
        wishlistdetails_close =(ImageView)findViewById(R.id.wishlistdetails_close);
        commonMethods.rotateArrow(wishlistdetails_close,this);
        tripsdetails_dot_loader = (ImageView) findViewById(R.id.tripsdetails_dot_loader);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(tripsdetails_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        triptype=localSharedPreferences.getSharedPreferences(Constants.TripType);
        totaltrips=localSharedPreferences.getSharedPreferences(Constants.TripTypeCount);
        triptypes=triptype.toLowerCase().replaceAll(" ","_");
        System.out.println("Total Trips "+totaltrips+","+triptype);


        if (triptype != null) {
            if (triptype.equals("Pending Bookings")) {
                triptype = getResources().getString(R.string.PendingTrip);
            } else if (triptype.equals("Current Bookings")) {
                triptype = getResources().getString(R.string.CurrentTrips);
            } else if (triptype.equals("Previous Bookings")) {
                triptype = getResources().getString(R.string.PreviousTrips);
            } else if (triptype.equals("Upcoming Bookings")) {
                triptype = getResources().getString(R.string.UpcomingTrips);
            }
        }

        localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,totaltrips+","+triptype);
        listView = (RecyclerView)findViewById(R.id.tripsdetailslist);
        searchlist = new ArrayList<>();
        adapter = new TripsDetailsListAdapter(getHeader(),this, Trips);

        listView.setHasFixedSize(false);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                adapter.clear();
                loadTripDetail();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        tripsdetails_dot_loader.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            loadTripDetail();
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,edt,edt,getResources(),this);
        }
    }
    public Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
        if (jsonResp.isSuccess()) {
            onSuccessBooking(jsonResp);
           /* if(triptypes.equals("pending_bookings")){
            }else if(triptypes.equals("previous_bookings")){
                onSuccessPrevious(jsonResp);
            }else if (triptypes.equals("upcoming_bookings")){
                onSuccessUpcoming(jsonResp);
            }else if (triptypes.equals("current_bookings")){
                onSuccessCurrent(jsonResp);
            }*/
            adapter.notifyDataChanged();
            tripsdetails_dot_loader.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,"0"+","+triptype);
            listView.setAdapter(adapter);
            tripsdetails_dot_loader.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
        tripsdetails_dot_loader.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,edt,edt,getResources(),this);
    }

    public void loadTripDetail(){
        tripsdetails_dot_loader.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        apiService.tripsDetail(localSharedPreferences.getSharedPreferences(Constants.AccessToken),triptypes).enqueue(new RequestCallback(this));
    }
    public void onSuccessBooking(JsonResponse jsonResp){
        tripsResult = gson.fromJson(jsonResp.getStrResponse(), TripDetailPendingResult.class);
        ArrayList <TripDetailData> tripDetailData = tripsResult.getPendingTrips();
        System.out.println("tripsResult.getPendingTrips() "+String.valueOf(tripsResult.getPendingTrips().size()));
        localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,String.valueOf(tripsResult.getPendingTrips().size())+","+triptype);
        Trips.addAll(tripDetailData);
    }

    public void onSuccessPrevious(JsonResponse jsonResp){
        tripsPreviousResult = gson.fromJson(jsonResp.getStrResponse(), TripDetailPreviousResult.class);
        ArrayList <TripDetailData> tripDetailData = tripsPreviousResult.getPreviousTrips();
        localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,String.valueOf(tripsPreviousResult.getPreviousTrips().size())+","+triptype);
        Trips.addAll(tripDetailData);
    }

    public void onSuccessUpcoming(JsonResponse jsonResp){
        tripsUpcomingResult = gson.fromJson(jsonResp.getStrResponse(), TripDetailUpcomingResult.class);
        ArrayList <TripDetailData> tripDetailData = tripsUpcomingResult.getUpcomingtrips();
        localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,String.valueOf(tripsUpcomingResult.getUpcomingtrips().size())+","+triptype);
        Trips.addAll(tripDetailData);
    }

    public void onSuccessCurrent(JsonResponse jsonResp){
        tripCurrentResult = gson.fromJson(jsonResp.getStrResponse(), TripDetailCurrentResult.class);
        ArrayList <TripDetailData> tripDetailData = tripCurrentResult.getCurrentTrips();
        localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,String.valueOf(tripCurrentResult.getCurrentTrips().size())+","+triptype);
        Trips.addAll(tripDetailData);
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
