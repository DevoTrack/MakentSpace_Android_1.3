package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestPaymentCountryListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.CountryListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is PaymentCountryList Page Contain to selected the Payment Country Details Selected
**************************************************************************  */
public class PaymentCountryList extends Activity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    ImageView selct_country_back;
    FloatingActionButton selct_country_next;
    public RecyclerView listView;
    List<Makent_model> searchlist;
    CountryListAdapter adapter;
    Context context;
    protected boolean isInternetAvailable;
    String userid;
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;
    int currentcountryposition=99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_country_list);
        commonMethods = new CommonMethods();

        localSharedPreferences=new LocalSharedPreferences(this);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.show();

        selct_country_back = (ImageView) findViewById(R.id.selct_country_back);
        commonMethods.rotateArrow(selct_country_back,this);

        // On Click function used to click action for check Email id in server send link to Email
        selct_country_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                finish();
            }
        });

        selct_country_next = (FloatingActionButton) findViewById(R.id.selct_country_next);
        commonMethods.rotateArrow(selct_country_next,this);

        // On Click function used to click action for check Email id in server send link to Email
        selct_country_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                finish();
            }
        });



        listView = (RecyclerView)findViewById(R.id.country_list);
        searchlist = new ArrayList<>();
        adapter = new CountryListAdapter(getHeader(),this, searchlist);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);
        load(0);
        }

    private void load(int index) {
        String country=localSharedPreferences.getSharedPreferences(Constants.CountryName);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if(country==null||country.equals(""))
        {
            localSharedPreferences.saveSharedPreferences(Constants.CountryName,"United States");
        }
        if (isInternetAvailable) {
           getCountryList();// this is used to call country list search api
        }else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, listView, getResources(), this);
        }
    }

    public Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    /**
     * @Reference Get amenities list from server
     */
    public void getCountryList(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        apiService.getCountryList(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        } if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, listView, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
            getCountryList(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, listView, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    public void getCountryList(JsonResponse jsonResponse){
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            JSONArray country_list = response.getJSONArray("country_list");
            for (int i = 0; i < country_list.length(); i++) {


                JSONObject dataJSONObject = country_list.getJSONObject(i);
                Makent_model listdata = new Makent_model();
                listdata.setCountryId(dataJSONObject.getString("country_id"));
                listdata.setCountryName(dataJSONObject.getString("country_name"));
                listdata.setCountryCode(dataJSONObject.getString("country_code"));
                searchlist.add(listdata);
                if(dataJSONObject.getString("country_name").equals("India"))
                {
                    currentcountryposition=i;
                }

            }
            adapter.notifyDataChanged();
        }catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
