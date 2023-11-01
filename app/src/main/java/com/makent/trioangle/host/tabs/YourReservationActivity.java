package com.makent.trioangle.host.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage host/tabs
 * @category YourReservationActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makent.trioangle.BaseActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.ReservationListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.Host_home_model;
import com.makent.trioangle.model.host.ReservationDetailsModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makent.trioangle.helper.Constants.LanguageCode;
import static com.makent.trioangle.helper.Constants.LanguageRecreate;

/* ************************************************************
Host room reservation details
*************************************************************** */
public class YourReservationActivity extends BaseActivity implements ServiceListener {

    private SwipeRefreshLayout swipeContainer;

    private ImageView reservation_dot_loader;
    private RelativeLayout emptyreservation, listreservation;
    private LocalSharedPreferences localSharedPreferences;
    private String userid;
    private List<ReservationDetailsModel> reservationDetails;

    protected boolean isInternetAvailable;
    private Snackbar snackbar;
    private RecyclerView listView;

    public ReservationListAdapter adapter;
    Host_home_model hostHome;

    TextView pending;

    ImageView ivBackReservation;
    ImageView ivBack;

    @OnClick(R.id.iv_back)
    public void backPress() {
        onBackPressed();
    }

    @OnClick(R.id.iv_back_reservation)
    public void backPressReservation() {
        onBackPressed();
    }

    Context context;
    private int backPressed = 0;    // used by onBackPressed()
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host_home);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        pending = (TextView) findViewById(R.id.pending);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        String lang = localSharedPreferences.getSharedPreferences(LanguageCode);
        ivBackReservation = (ImageView) findViewById(R.id.iv_back_reservation);
        ivBack = (ImageView) findViewById(R.id.iv_back);


       /* if (lang != null && !lang.equals("")) {
            setLocale(lang);
        } else {
            setLocale("ar");
        }*/
        commonMethods.rotateArrow(ivBackReservation,this);
        commonMethods.rotateArrow(ivBack,this);
        reservation_dot_loader = (ImageView) findViewById(R.id.reservation_dot_loader);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(reservation_dot_loader);
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1);


        emptyreservation = (RelativeLayout) findViewById(R.id.reservationempty);
        listreservation = (RelativeLayout) findViewById(R.id.listreservation);

        reservation_dot_loader.setVisibility(View.VISIBLE);
        emptyreservation.setVisibility(View.GONE);
        listreservation.setVisibility(View.GONE);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listView = (RecyclerView) findViewById(R.id.reservationlist);

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                adapter.clear();
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                apiService.reservationDetail(userid).enqueue(new RequestCallback(YourReservationActivity.this));
                //new LoadReservationDetails().execute();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        if (isInternetAvailable)
        {
            //new LoadRoomsDetails().execute();
            loadReservationDetail();
        }
        else
        {
            snackBar();
            // commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2, edt,pending,getResources(),this);
        }

    }

    /**
     * To call api and fetch relevant data
     */

    private void loadReservationDetail() {

        reservation_dot_loader.setVisibility(View.VISIBLE);
        emptyreservation.setVisibility(View.GONE);
        listreservation.setVisibility(View.GONE);

        apiService.reservationDetail(userid).enqueue(new RequestCallback(this));
    }

    public void setLocale(String lang) {
        boolean isrecreate = localSharedPreferences.getSharedPreferencesBool(LanguageRecreate);

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        if (isrecreate) {
            Intent x = new Intent(getApplicationContext(), HostHome.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
            localSharedPreferences.saveSharedPreferences(LanguageRecreate, false);
        }

    }


    public Header getHeader() {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
        if (jsonResp.isSuccess()) {
            onSuccessReservation(jsonResp); // onSuccess call method
        } else {
            reservation_dot_loader.setVisibility(View.GONE);
            emptyreservation.setVisibility(View.VISIBLE);
            listreservation.setVisibility(View.GONE);
        }
    }

    /**
     * Json parsing and fetching datas
     * @param jsonResp Successfull json response
     */


    private void onSuccessReservation(JsonResponse jsonResp) {


        hostHome = gson.fromJson(jsonResp.getStrResponse(), Host_home_model.class);


        reservationDetails = hostHome.getReservationDetails();

        if (jsonResp.isSuccess() && reservationDetails.size() >= 1) {
            reservation_dot_loader.setVisibility(View.GONE);
            emptyreservation.setVisibility(View.GONE);
            listreservation.setVisibility(View.VISIBLE);

            try {
                JSONObject response = new JSONObject(jsonResp.getStrResponse());
                JSONArray messages = response.getJSONArray("data");

                localSharedPreferences.saveSharedPreferences(Constants.ReservationCount, messages.length());
                localSharedPreferences.saveSharedPreferences(Constants.ReservationDetails, messages.toString());
                long current_millis = System.currentTimeMillis();
                localSharedPreferences.saveSharedPreferences(Constants.ReservationLoadTime, current_millis);

            } catch (JSONException j) {
                j.printStackTrace();
            }


            adapter = new ReservationListAdapter(getHeader(), this, reservationDetails);
            adapter = new ReservationListAdapter(getHeader(), this, reservationDetails);
            listView.setAdapter(adapter);
        } else {
            reservation_dot_loader.setVisibility(View.GONE);
            emptyreservation.setVisibility(View.VISIBLE);
            listreservation.setVisibility(View.GONE);
        }
    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
    }


    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


    public void onBackPressed() {
        localSharedPreferences.saveSharedPreferences(Constants.isHost, 0);
        super.onBackPressed();       // bye

    }


    public void snackBar() {
        // Create the Snackbar
        snackbar = Snackbar.make(emptyreservation, "", Snackbar.LENGTH_INDEFINITE);

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
                    loadReservationDetail();

                    // new UpdateDateDetails().execute();
                } else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable) {
        } else {
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

