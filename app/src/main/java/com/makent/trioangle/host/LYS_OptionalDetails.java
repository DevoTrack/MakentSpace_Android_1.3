package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_OptionalDetails
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.CurrencyListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.optionaldetails.OD_AmenitiesNew;
import com.makent.trioangle.host.optionaldetails.OD_CancelPolicy;
import com.makent.trioangle.host.optionaldetails.OD_Description;
import com.makent.trioangle.host.optionaldetails.OD_LongTermPrice;
import com.makent.trioangle.host.optionaldetails.OD_RoomsBeds;
import com.makent.trioangle.host.optionaldetails.ReservationSettings;
import com.makent.trioangle.host.optionaldetails.description.OD_EarlyBirdDiscounts;
import com.makent.trioangle.host.optionaldetails.description.OD_LastMin;
import com.makent.trioangle.host.optionaldetails.description.OD_LengthOfStay;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.Makent_host_model;
import com.makent.trioangle.model.host.optionaldetail.UpdateCurrencyResult;
import com.makent.trioangle.model.settings.CurrencyListModel;
import com.makent.trioangle.model.settings.CurrencyResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_CURRENCYLIST;
import static com.makent.trioangle.util.Enums.REQ_DISABLE_LISTING;
import static com.makent.trioangle.util.Enums.REQ_UPDATE_CURRENCY;

/* ***********************************************************************
This is List Your Space Home Page Contain Optional Details , Prices, Details
**************************************************************************  */

public class LYS_OptionalDetails extends AppCompatActivity implements View.OnClickListener,ServiceListener {

    RelativeLayout optionaldetails_title, optionaldetails_price,early_bird_price,last_min_price,length_of_stay_price, optionaldetails_currency, optionaldetails_description, optionaldetails_amenities, optionaldetails_roomsbeds,optionaldetails_policy,reservation_settings;
    TextView deactivate_listing,currency_code,optionaldetails_amenities_code;
    int stepcount;
    String TAG = "MainActivity - ";

    ProgressDialog progressDialog;

    URL CurrencyUrl;
    String CurrencyUrl_inputline;
    JSONArray CurrencyUrl_jsonarray;
    JSONObject CurrencyUrl_jsonobj;
    protected boolean isInternetAvailable;
    RecyclerView recyclerView1;
    List<Makent_host_model> makent_host_modelList;
    CurrencyListAdapter currencyListAdapter;
    List<String> searchamenities = new ArrayList<String>();
    public static android.app.AlertDialog alertDialogStores;
    public ImageView optionaldetails_back,longterm_back,length_of_stay_back,early_bird_discount_back,lastmin_discount_back,reservation_arrow,description_back,roomsbeds_back,policy_back;


    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;



    String symbol[];//=new String[50];
    String currencycode[];//=new String[50];
    String currency1,bettype;
    Spinner myspinner1;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomcurrencysymbol,roomcurrencycode,amenitieslist,listisenable;

    Dialog_loading mydialog;
    public CurrencyResult currencyResult;
    public UpdateCurrencyResult updateCurrencyResult;
    public ArrayList <CurrencyListModel> currencyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_optionaldetails);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        Intent intent = getIntent();
        stepcount = intent.getIntExtra("stepcount",0);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
        amenitieslist=localSharedPreferences.getSharedPreferences(Constants.ListAmenities);
        listisenable=localSharedPreferences.getSharedPreferences(Constants.ListIsEnable);

        optionaldetails_title = (RelativeLayout) findViewById(R.id.optionaldetails_title);
        optionaldetails_price = (RelativeLayout) findViewById(R.id.optionaldetails_price);
        early_bird_price = (RelativeLayout) findViewById(R.id.early_bird_price);
        length_of_stay_price = (RelativeLayout) findViewById(R.id.length_of_stay_price);
        last_min_price = (RelativeLayout) findViewById(R.id.last_min_price);
        optionaldetails_currency = (RelativeLayout) findViewById(R.id.optionaldetails_currency);
        optionaldetails_description = (RelativeLayout) findViewById(R.id.optionaldetails_description);
        optionaldetails_amenities = (RelativeLayout) findViewById(R.id.optionaldetails_amenities);
        optionaldetails_roomsbeds = (RelativeLayout) findViewById(R.id.optionaldetails_roomsbeds);
        optionaldetails_policy= (RelativeLayout) findViewById(R.id.optionaldetails_policy);
        reservation_settings= (RelativeLayout) findViewById(R.id.reservation_settings);
        reservation_arrow =(ImageView)findViewById(R.id.reservation_arrow);
        longterm_back =(ImageView)findViewById(R.id.longterm_back);
        length_of_stay_back =(ImageView)findViewById(R.id.length_of_stay_back);
        early_bird_discount_back =(ImageView)findViewById(R.id.early_bird_discount_back);
        lastmin_discount_back =(ImageView)findViewById(R.id.lastmin_discount_back);
        description_back =(ImageView)findViewById(R.id.description_back);
        roomsbeds_back =(ImageView)findViewById(R.id.roomsbeds_back);
        policy_back =(ImageView)findViewById(R.id.policy_back);


        optionaldetails_back =(ImageView)findViewById(R.id.optionaldetails_back);
        commonMethods.rotateArrow(optionaldetails_back,this);
        commonMethods.rotateArrow(lastmin_discount_back,this);
        commonMethods.rotateArrow(early_bird_discount_back,this);
        commonMethods.rotateArrow(length_of_stay_back,this);
        commonMethods.rotateArrow(longterm_back,this);
        commonMethods.rotateArrow(reservation_arrow,this);
        commonMethods.rotateArrow(description_back,this);
        commonMethods.rotateArrow(roomsbeds_back,this);
        commonMethods.rotateArrow(policy_back,this);

        deactivate_listing = (TextView) findViewById(R.id.deactivate_listing);
        currency_code= (TextView) findViewById(R.id.currency_code);
        optionaldetails_amenities_code= (TextView) findViewById(R.id.optionaldetails_amenities_code);
        optionaldetails_title.setOnClickListener(this);
        optionaldetails_price.setOnClickListener(this);
        early_bird_price.setOnClickListener(this);
        length_of_stay_price.setOnClickListener(this);
        last_min_price.setOnClickListener(this);
        optionaldetails_currency.setOnClickListener(this);
        optionaldetails_description.setOnClickListener(this);
        optionaldetails_amenities.setOnClickListener(this);
        optionaldetails_roomsbeds.setOnClickListener(this);
        optionaldetails_policy.setOnClickListener(this);
        reservation_settings.setOnClickListener(this);


        if(listisenable!=null&&!listisenable.equals("")) {
    if (listisenable.equals("Yes")) {
        deactivate_listing.setText(getResources().getString(R.string.unlisted));
    } else {
        if (stepcount==5){
            deactivate_listing.setText(getResources().getString(R.string.listed));
    }else {
            deactivate_listing.setVisibility(View.GONE);
        }
    }
}else {
    if (stepcount==5) {
        deactivate_listing.setText(getResources().getString(R.string.listed));
    }else {
        deactivate_listing.setVisibility(View.GONE);
    }
}
        deactivate_listing.setOnClickListener(this);
        System.out.println("amenities Home"+amenitieslist);

        if(amenitieslist!=null&&!amenitieslist.equals("")) {
            if (amenitieslist.equals("null")) {
                optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
            } else {
                String[] split = amenitieslist.split(",");
                for (int i = 0; i < split.length; i++) {
                    if (!searchamenities.contains(split[i])) {
                        searchamenities.add(split[i]);
                    }
                }
                System.out.println("searchamenities Home" + searchamenities);

                if (searchamenities.size() == 0) {
                    optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
                } else {
                    optionaldetails_amenities_code.setText(String.valueOf(searchamenities.size()));
                }
            }
            }else{
                optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
            }



        if(roomcurrencycode!=null)
        {
            currency_code.setText(roomcurrencycode);
        }else {
            currency_code.setText("USD");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        amenitieslist=localSharedPreferences.getSharedPreferences(Constants.ListAmenities);
        if(amenitieslist!=null&&!amenitieslist.equals(""))
        {
            if(amenitieslist.equals("null")) {
                optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
            }else {
                searchamenities = new ArrayList<String>();
                String[] split = amenitieslist.split(",");
                for (int i = 0; i < split.length; i++) {

                    if (i == 0) {
                        searchamenities.add(split[i]);
                    } else if (!searchamenities.contains(split[i]) && split[i] != null && !split[i].equals("")) {
                        searchamenities.add(split[i]);
                    }
                }
                System.out.println("reseume searchamenities Home" + searchamenities);

                if (searchamenities.size() == 0) {
                    optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
                } else {
                    optionaldetails_amenities_code.setText(String.valueOf(searchamenities.size()));
                }
            }
        }else {
            optionaldetails_amenities_code.setText(getResources().getString(R.string.none));
        }
        /*listisenable=localSharedPreferences.getSharedPreferences(Constants.ListIsEnable);
        if(listisenable.equals("Yes")) {
            deactivate_listing.setText(getResources().getString(R.string.deactivatelist));
        }else
        {
            deactivate_listing.setText(getResources().getString(R.string.activatelist));
        }*/
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.optionaldetails_title: {
                onBackPressed();
            }
            break;
            case R.id.optionaldetails_price: {
                Intent price=new Intent(getApplicationContext(), OD_LongTermPrice.class);
                startActivity(price);
            }
            break;
            case R.id.length_of_stay_price: {
                Intent price=new Intent(getApplicationContext(), OD_LengthOfStay.class);
                startActivity(price);
            }
            break;
            case R.id.last_min_price: {
                Intent price=new Intent(getApplicationContext(), OD_LastMin.class);
                startActivity(price);
            }
            break;
            case R.id.early_bird_price: {
                Intent price=new Intent(getApplicationContext(), OD_EarlyBirdDiscounts.class);
                startActivity(price);
            }
            break;
            case R.id.optionaldetails_currency: {
                //optionaldetails_currency.setEnabled(false);
                currency_list(); // this function to used get the currency details
               //(new TestAsync()).execute();
            }
            break;
            case R.id.optionaldetails_description: {
                Intent price=new Intent(getApplicationContext(), OD_Description.class);
                startActivity(price);
            }
            break;
            case R.id.optionaldetails_amenities: {
                Intent price=new Intent(getApplicationContext(), OD_AmenitiesNew.class);
                startActivity(price);
            }
            break;
            case R.id.optionaldetails_roomsbeds: {

                Intent price=new Intent(getApplicationContext(), OD_RoomsBeds.class);
                startActivity(price);
            }
            break;
            case R.id.optionaldetails_policy: {
                Intent price=new Intent(getApplicationContext(), OD_CancelPolicy.class);
                startActivity(price);
            }
            break;
            case R.id.deactivate_listing: {
                if (isInternetAvailable) {
                    if (!mydialog.isShowing()) {
                        mydialog.show();
                    }
                    disableListing();  //this is used the call on listmodechange api
                }else {
                    snackBar(); // this is used show the alert to snackbar function
                }

                /*Intent price=new Intent(getApplicationContext(), HostHome.class);
                startActivity(price);*/
            }
            break;
            case R.id.reservation_settings: {
                Intent price=new Intent(getApplicationContext(), ReservationSettings.class);
                startActivity(price);
            }
            break;
        }
    }


    /*public void currency_list()
    {

        recyclerView1 = new RecyclerView(LYS_OptionalDetails.this);
        makent_host_modelList = new ArrayList<>();

        currencyListAdapter = new CurrencyListAdapter(this, makent_host_modelList);
        currencyListAdapter.setLoadMoreListener(new CurrencyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView1.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = makent_host_modelList.size() - 1;
                    }
                });
            }
        });

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(currencyListAdapter);
        loadcurrencylist(0);
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.currency_header, null);
        alertDialogStores = new android.app.AlertDialog.Builder(LYS_OptionalDetails.this)
                .setCustomTitle(view)
                .setView(recyclerView1)
                .setCancelable(true)
                .show();

        optionaldetails_currency.setEnabled(true);

    }*/

    public void currency_list()
    {

        recyclerView1 = new RecyclerView(this);
        makent_host_modelList = new ArrayList<>();
        currencyList = new ArrayList<>();


        currencyListAdapter = new CurrencyListAdapter(this, currencyList);
        currencyListAdapter.setLoadMoreListener(new CurrencyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView1.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = currencyList.size() - 1;
                    }
                });
            }
        });

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(currencyListAdapter);
        // loadcurrencylist(0);
        if (currencyList.size()<1)
        currencyList();
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.currency_header, null);
        alertDialogStores = new android.app.AlertDialog.Builder(this)
                .setCustomTitle(view)
                .setView(recyclerView1)
                .setCancelable(true)
                .show();

        alertDialogStores.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

                roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
                roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
                // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
                if(roomcurrencycode!=null)
                {
                    currency_code.setText(roomcurrencycode);
                }else {
                    currency_code.setText("USD");
                }
                updateCurrency();

            }
        });


    }

    public void currencyList(){
        apiService.currencyList(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_CURRENCYLIST,this));
    }


    public void disableListing(){
        apiService.disableListing(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid).enqueue(new RequestCallback(REQ_DISABLE_LISTING,this));
    }

    public void updateCurrency(){
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();

        if(roomcurrencycode!=null)
        {
            currency_code.setText(roomcurrencycode);
        }else {
            currency_code.setText("USD");
        }

        apiService.updateRoomCurrency(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomcurrencycode,roomid).enqueue(new RequestCallback(REQ_UPDATE_CURRENCY,this));
    }







    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }

        switch (jsonResp.getRequestCode()) {

            case REQ_CURRENCYLIST:
                if (jsonResp.isSuccess()) {

                    onSuccessCurrency(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                }
                break;
            case REQ_UPDATE_CURRENCY:
                if (jsonResp.isSuccess()) {
                    onSuccessCurrencyUpt(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,currency_code,getResources(),this);
                }
                break;
            case REQ_DISABLE_LISTING:
                if (jsonResp.isSuccess()) {
                    onSuccessUnListed(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,currency_code,getResources(),this);
                }
                break;

        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public void onSuccessCurrency(JsonResponse jsonResp){
        currencyResult =gson.fromJson(jsonResp.getStrResponse(), CurrencyResult.class);

        ArrayList <CurrencyListModel> currencyListModels = currencyResult.getCurrencyList();
        currencyList.addAll(currencyListModels);
        currencyListAdapter.notifyDataChanged();
    }

    public void onSuccessCurrencyUpt(JsonResponse jsonResp){
        updateCurrencyResult =gson.fromJson(jsonResp.getStrResponse(), UpdateCurrencyResult.class);
       // localSharedPreferences.saveSharedPreferences(Constants.ListWeeklyPrice,updateCurrencyResult.getWeeklyPrice());
       // localSharedPreferences.saveSharedPreferences(Constants.ListMonthlyPrice,updateCurrencyResult.getMonthlyPrice());
        localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee,updateCurrencyResult.getCleaningFee());
        localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest,updateCurrencyResult.getAdditionalGuestsFee());
        localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit,updateCurrencyResult.getSecurityDeposit());
        localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice,updateCurrencyResult.getWeekendPricing());
        System.out.println("Room cleaning_feeup" + localSharedPreferences.getSharedPreferences(Constants.ListCleaningFee));
        System.out.println("Room additional_guestsup" + localSharedPreferences.getSharedPreferences(Constants.ListAdditionalGuest));
    }


    public void onSuccessUnListed(JsonResponse jsonResp){


        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        listisenable=localSharedPreferences.getSharedPreferences(Constants.ListIsEnable);
        if(listisenable!=null&&!listisenable.equals("")) {
            if (listisenable.equals("Yes")) {
                localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,"No");
                if (stepcount==5) {
                    deactivate_listing.setText(getResources().getString(R.string.listed));
                }else {
                    deactivate_listing.setVisibility(View.GONE);
                }
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,"Yes");
                deactivate_listing.setText(getResources().getString(R.string.unlisted));
            }
        }else {
            localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,"Yes");
            deactivate_listing.setText(getResources().getString(R.string.unlisted));
        }



    }




    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(optionaldetails_amenities_code, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable) {
            // textViewTop.setText(statusmessage);
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
