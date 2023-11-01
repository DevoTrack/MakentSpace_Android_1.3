package com.makent.trioangle.travelling.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestTripsActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.TripTypeListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.tripsModel.BookingTypeModel;
import com.makent.trioangle.model.tripsModel.TripsResult;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TripsActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RecyclerView recyclerView;
    // private SwipeRefreshLayout swipeContainer;
    TripTypeListAdapter listAdapter;
    RelativeLayout emptytrips, listtrips;
    ImageView trips_dot_loader;
    LocalSharedPreferences localSharedPreferences;
    String tripstype[] = {"Pending \nBookings", "Current \nBookings", "Previous \nBookings", "Upcoming \nBookings"};
    String tripscount[];
    String userid;
    Button trips_start_explore;
    protected boolean isInternetAvailable;
    Snackbar snackbar;
    private int backPressed = 0;    // used by onBackPressed()
    TripsResult tripsResult;

    ArrayList<BookingTypeModel> trips = new ArrayList();
    public EditText edt;
    RelativeLayout rltTrips;

    public View viewNotoken;
    public TextView tvTitle,tvdescription;
    public Button btnNoTokenLogin;
    public ImageView ivNoToken;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_tab);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        Log.e("Booking","Booking Activity");

        viewNotoken = (View) findViewById(R.id.viewNotoken);
        btnNoTokenLogin = (Button) findViewById(R.id.btnNoTokenLogin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvdescription = (TextView) findViewById(R.id.tvdescription);
        ivNoToken = (ImageView) findViewById(R.id.ivNoToken);
        edt = (EditText) findViewById(R.id.edt);
        btnNoTokenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                home.putExtra("isback",1);
                startActivity(home);
            }
        });

        if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            // swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
            viewNotoken.setVisibility(View.GONE);
            ImageView imageView = (ImageView) findViewById(R.id.tripgifImageView);
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);
            Glide.with(getApplicationContext()).load(R.drawable.anim_trips).into(imageViewTarget);


            trips_dot_loader = (ImageView) findViewById(R.id.trips_dot_loader);
            DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(trips_dot_loader);
            Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

            trips_start_explore = (Button) findViewById(R.id.trips_start_explore);
            trips_start_explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                    x.putExtra("tabsaved", 0);
                    startActivity(x);
                    finish();
                }
            });

            emptytrips = (RelativeLayout) findViewById(R.id.emptytrips);
            listtrips = (RelativeLayout) findViewById(R.id.listtrips);
            rltTrips = (RelativeLayout) findViewById(R.id.activity_main);

            recyclerView = (RecyclerView) findViewById(R.id.tripslist);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            trips_dot_loader.setVisibility(View.VISIBLE);
            emptytrips.setVisibility(View.GONE);
            listtrips.setVisibility(View.GONE);

            if (isInternetAvailable) {
                loadTripsType();
            } else {
                snackBar();
               // commonMethods.snackBar(getResources().getString(R.string.interneterror), getResources().getString(R.string.retry), true, 2, edt, edt, getResources(), this);
            }
        }else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"trips");
            viewNotoken.setVisibility(View.VISIBLE);
            tvTitle.setText(getResources().getString(R.string.bookings));
            tvdescription.setText(getResources().getString(R.string.no_token_trips));
            ivNoToken.setImageDrawable(getResources().getDrawable(R.drawable.token_trips));
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessTrip(jsonResp); // onSuccess call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            trips_dot_loader.setVisibility(View.GONE);
            emptytrips.setVisibility(View.VISIBLE);
            listtrips.setVisibility(View.GONE);
            //commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,edt,edt,getResources(),this);
        }
    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        trips_dot_loader.setVisibility(View.GONE);
        emptytrips.setVisibility(View.VISIBLE);
        listtrips.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,edt,edt,getResources(),this);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void loadTripsType() {
        trips_dot_loader.setVisibility(View.VISIBLE);
        emptytrips.setVisibility(View.GONE);
        listtrips.setVisibility(View.GONE);
        apiService.tripsType(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
    }

    public void onSuccessTrip(JsonResponse jsonResp) {
        tripsResult = gson.fromJson(jsonResp.getStrResponse(), TripsResult.class);
        trips.clear();
        trips = tripsResult.getBookingTypeModelArrayList();

        System.out.println("Trips : "+trips.get(0).getValue());
        listAdapter = new TripTypeListAdapter(this, trips);

        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataChanged();

        trips_dot_loader.setVisibility(View.GONE);
        emptytrips.setVisibility(View.GONE);
        listtrips.setVisibility(View.VISIBLE);

    }

    public void onBackPressed() {
        if (backPressed >= 1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
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

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        String tripsdetails= null;
        try {
            tripsdetails = localSharedPreferences.getSharedPreferences(Constants.TotalTripDetails);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (isInternetAvailable) {
            System.out.println("Total Trips adapter"+tripsdetails);
            if (tripsdetails!=null&&!TextUtils.isEmpty(tripsdetails)) {

                String[] tripsdetailin = tripsdetails.split(",");
                if (tripsdetailin[0].equals("0")) {
                    localSharedPreferences.saveSharedPreferences(Constants.TotalTripDetails,"2"+","+"");
                    loadTripsType();
                }
            }
        } else {
            snackBar();
          //  commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,edt,edt,getResources(),this);
        }
    }

    public void snackBar()
    {
        // Create the Snackbar
        snackbar = Snackbar.make(findViewById(R.id.activity_main), "", Snackbar.LENGTH_INDEFINITE);

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
                    if (localSharedPreferences.getSharedPreferences(Constants.AccessToken)!=null) {
                        loadTripsType();
                    }
                    // new UpdateDateDetails().execute();
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
        snackbar.show();

    }
}

