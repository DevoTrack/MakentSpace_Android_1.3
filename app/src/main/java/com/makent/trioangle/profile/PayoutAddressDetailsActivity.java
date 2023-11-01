package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    PayoutAddressDetailsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.PayoutCountryListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.payout.CountryModel;
import com.makent.trioangle.model.payout.CountryResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
                   Payout get user address detail Page
Get  address details for payout option
*************************************************************** */

public class PayoutAddressDetailsActivity extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout payoutaddress_title;
    Button payout_next;
    ImageView payoutaddress_back;

    EditText payoutaddress_street,payoutaddress_apt,payoutaddress_city,payoutaddress_state,payoutaddress_pin,payoutaddress_country;
    String address_street,address_apt,address_city,address_state,address_pin,address_country,userid;

    RecyclerView recyclerView1;
    PayoutCountryListAdapter countryListAdapter;
    public static android.app.AlertDialog alertDialogStores;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    CountryResult countryResult;
    ArrayList<CountryModel> countryModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_address_details);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        payoutaddress_street=(EditText)findViewById(R.id.payoutaddress_street);
        payoutaddress_apt=(EditText)findViewById(R.id.payoutaddress_apt);
        payoutaddress_city=(EditText)findViewById(R.id.payoutaddress_city);
        payoutaddress_pin=(EditText)findViewById(R.id.payoutaddress_pin);
        payoutaddress_state=(EditText)findViewById(R.id.payoutaddress_state);
        payoutaddress_country=(EditText)findViewById(R.id.payoutaddress_country);

        payoutaddress_back =(ImageView)findViewById(R.id.payoutaddress_back);

        payoutaddress_title=(RelativeLayout)findViewById(R.id.payoutaddress_title);
        payout_next=(Button)findViewById(R.id.payout_next);

        payout_next.setOnClickListener(this);
        payoutaddress_title.setOnClickListener(this);
        payoutaddress_country.setOnClickListener(this);
        commonMethods.rotateArrow(payoutaddress_back,this);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        localSharedPreferences.saveSharedPreferences(Constants.CountryName,"");

    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.payoutaddress_title: {
                onBackPressed();
            }
            break;
            case R.id.payoutaddress_country: {
                // Show country list in dialog
                countryList();
            }
            break;
            case R.id.payout_next: {


                address_street=payoutaddress_street.getText().toString();
                address_apt=payoutaddress_apt.getText().toString();
                address_city=payoutaddress_city.getText().toString();
                address_state=payoutaddress_state.getText().toString();
                address_pin=payoutaddress_pin.getText().toString();
                address_country=payoutaddress_country.getText().toString();

                address_street= address_street.replaceAll("^\\s+|\\s+$", "");
                address_apt= address_apt.replaceAll("^\\s+|\\s+$", "");
                address_city= address_city.replaceAll("^\\s+|\\s+$", "");
                address_state= address_state.replaceAll("^\\s+|\\s+$", "");
                address_pin= address_pin.replaceAll("^\\s+|\\s+$", "");
                address_country= address_country.replaceAll("^\\s+|\\s+$", "");


                payoutaddress_street.setText(address_street);
                payoutaddress_apt.setText(address_apt);
                payoutaddress_city.setText(address_city);
                payoutaddress_state.setText(address_state);
                payoutaddress_pin.setText(address_pin);
                payoutaddress_country.setText(address_country);


                try {
                    address_street= URLEncoder.encode(address_street, "UTF-8");
                    address_apt= URLEncoder.encode(address_apt, "UTF-8");
                    address_city= URLEncoder.encode(address_city, "UTF-8");
                    address_state= URLEncoder.encode(address_state, "UTF-8");
                    address_pin= URLEncoder.encode(address_pin, "UTF-8");
                    address_country= URLEncoder.encode(address_country, "UTF-8");
                    address_street = address_street.replace("+"," ");
                    address_apt = address_apt.replace("+"," ");
                    address_city = address_city.replace("+"," ");
                    address_state = address_state.replace("+"," ");
                    address_pin = address_pin.replace("+"," ");
                    address_country = address_country.replace("+"," ");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(address_country.equals(""))
                {commonMethods.snackBar("Please select country for payout.","",false,2,payoutaddress_street,payoutaddress_street,getResources(),this);
                    countryList();
                }else if(address_street.equals("")) {
                    commonMethods.snackBar("Please provide an address.","",false,2,payoutaddress_street,payoutaddress_street,getResources(),this);
                }else if(address_city.equals("")) {
                    commonMethods.snackBar("Please provide a city.","",false,2,payoutaddress_street,payoutaddress_street,getResources(),this);
                }  else if(address_pin.equals("")) {
                    commonMethods.snackBar("Please provide a zip code.","",false,2,payoutaddress_street,payoutaddress_street,getResources(),this);
                }
                else
                {
                    // Call payout email page and pass datas
                    Intent x = new Intent(getApplicationContext(), PayoutEmailActivity.class);
                    x.putExtra("address1",address_street);
                    x.putExtra("address2",address_apt);
                    x.putExtra("city",address_city);
                    x.putExtra("state",address_state);
                    x.putExtra("postal_code",address_pin);
                    x.putExtra("country",localSharedPreferences.getSharedPreferences(Constants.PayPalCountryCode));
                    startActivity(x);
                    finish();
                }

            }
            break;
        }
    }

    // Get country list details
    public void countryList()
    {

        recyclerView1 = new RecyclerView(PayoutAddressDetailsActivity.this);

        countryListAdapter = new PayoutCountryListAdapter(this, countryModels);


        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(countryListAdapter);
        // loadcurrencylist(0);

        countryListSearch();

        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.currency_header, null);
        TextView title=(TextView) view.findViewById(R.id.header);
        title.setText(getResources().getString(R.string.selectcountry));
        alertDialogStores = new android.app.AlertDialog.Builder(PayoutAddressDetailsActivity.this)
                .setCustomTitle(title)
                .setView(recyclerView1)
                .setCancelable(true)
                .show();

        alertDialogStores.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
             String   CountryName=localSharedPreferences.getSharedPreferences(Constants.CountryName);
                // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
                if(CountryName!=null)
                {
                    payoutaddress_country.setText(CountryName);
                }else {
                    payoutaddress_country.setText("");
                }

            }
        });


    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessC(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }
    // Get country list from API
    public void countryListSearch(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.countrylist(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
    }

    public void onSuccessC(JsonResponse jsonResp){
        countryResult =gson.fromJson(jsonResp.getStrResponse(), CountryResult.class);
        ArrayList <CountryModel> countryModel = countryResult.getCountryList();
        countryModels.addAll(countryModel);
        countryListAdapter.notifyDataChanged();
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }



}
