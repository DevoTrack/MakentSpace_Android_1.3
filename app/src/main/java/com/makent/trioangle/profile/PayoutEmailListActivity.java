package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    PayoutEmailListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.PayPalEmailAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.Makent_host_model;
import com.makent.trioangle.model.payout.PayoutDetail;
import com.makent.trioangle.model.payout.PayoutDetailResult;
import com.makent.trioangle.model.payout.UserPayoutDetails;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_CURRENCYLIST;

/* ************************************************************
                   Payout Email list Page
Show list of PayPal email address for payout option and to change payout email delete, set default
*************************************************************** */
public class PayoutEmailListActivity extends AppCompatActivity implements View.OnClickListener,ServiceListener
{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RecyclerView recyclerView;
    List<Makent_host_model> makent_host_modelList;
    PayPalEmailAdapter adapter;
    String paypalemaillist[];

    RelativeLayout payoutemaillist_title;
    Button payout_addpayout,payout_addstripe;
    String payoutid;
    int payoutoption;
    public String userid;
    public LocalSharedPreferences localSharedPreferences;
    public Context context;
    public TextView payoutmain_title,payoutmain_msg,payoutmain_link;
    public ImageView payoutemaillist_back;
    protected boolean isInternetAvailable;
    Dialog_loading mydialog;

    PayoutDetailResult payoutDetailResult;
    ArrayList<PayoutDetail> payoutDetails = new ArrayList<>();

    UserPayoutDetails userPayoutDetails = new UserPayoutDetails();
    String firstname,lastname,email,gender,day,month,year,dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_email_list);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        payoutmain_title = (TextView) findViewById(R.id.payoutmain_title);
        payoutmain_msg = (TextView) findViewById(R.id.payoutmain_msg);

        payoutmain_link = (TextView) findViewById(R.id.payoutmain_link);
        payoutemaillist_title=(RelativeLayout)findViewById(R.id.payoutemaillist_title);
        payout_addpayout=(Button)findViewById(R.id.payout_addpayout);
        payout_addstripe=(Button) findViewById(R.id.payout_addstripe);
        payoutemaillist_back =(ImageView)findViewById(R.id.payoutemaillist_back);
        payout_addpayout.setVisibility(View.GONE);
        payout_addstripe.setVisibility(View.GONE);
        payout_addpayout.setOnClickListener(this);
        payout_addstripe.setOnClickListener(this);
        payoutemaillist_title.setOnClickListener(this);
        commonMethods.rotateArrow(payoutemaillist_back,this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        makent_host_modelList = new ArrayList<>();

        adapter = new PayPalEmailAdapter(this,this, payoutDetails);

        // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);
        //load(0);
        isInternetAvailable = getNetworkState().isConnectingToInternet();

    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            loadPayout();

        }else {
            snackBar(getResources().getString(R.string.Interneterror));
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            adapter = new PayPalEmailAdapter(this,this, makent_host_modelList);

        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.payoutemaillist_title: {
                onBackPressed();
            }
            break;

            case R.id.payout_addpayout: {
                // Get address details for add new payout option
                Intent x = new Intent(getApplicationContext(), PayoutAddressDetailsActivity.class);
                startActivity(x);
//                finish();
            }
            break;

            case R.id.payout_addstripe: {
                // Get address details for add new payout option
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.UserDOB)) || isEmpty(dob)){
                    snackBar(getResources().getString(R.string.updatedob));
                }else if(isEmpty(gender)){
                    snackBar(getResources().getString(R.string.updategender));
                }
                else {
                    Intent x = new Intent(getApplicationContext(), PayoutBankDetailsActivity.class);
                    x.putExtra("firstname",firstname);
                    x.putExtra("lastname",lastname);
                    x.putExtra("email",email);
                    x.putExtra("gender",gender);
                    x.putExtra("day",day);
                    x.putExtra("month",month);
                    x.putExtra("year",year);
                    startActivity(x);
//                 finish();
                }
            }
            break;
        }
    }
    public Boolean isEmpty(String value){
        return TextUtils.isEmpty(value);
    }
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing())
            mydialog.dismiss();
        if (jsonResp.isSuccess()) {
            onSuccessPayout(jsonResp); // onSuccess call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            payoutmain_msg.setVisibility(View.VISIBLE);
            payoutmain_title.setVisibility(View.VISIBLE);
            payoutmain_link.setVisibility(View.VISIBLE);
            payout_addpayout.setVisibility(View.VISIBLE);
            payout_addstripe.setVisibility(View.VISIBLE);
            noDataFound(jsonResp);
        }
    }
    private void noDataFound(JsonResponse jsonResp) {
        payoutDetailResult = gson.fromJson(jsonResp.getStrResponse(), PayoutDetailResult.class);

        firstname=payoutDetailResult.getUserPayoutDetails().getFirstname();
        lastname=payoutDetailResult.getUserPayoutDetails().getLastname();
        email=payoutDetailResult.getUserPayoutDetails().getEmail();
        gender=payoutDetailResult.getUserPayoutDetails().getGender();
        day= String.valueOf(payoutDetailResult.getUserPayoutDetails().getDay());
        month= String.valueOf(payoutDetailResult.getUserPayoutDetails().getMonth());
        year= String.valueOf(payoutDetailResult.getUserPayoutDetails().getYear());
        dob = String.valueOf(payoutDetailResult.getUserPayoutDetails().getDob());
        localSharedPreferences.saveSharedPreferences(Constants.STRIPE_KEY,payoutDetailResult.getStripe_publish_key());
        localSharedPreferences.saveSharedPreferences(Constants.STRIPEID_TOKEN,null);

    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing())
            mydialog.dismiss();
        //snackBar();
    }
    // Get PayPal email address from API
    public void loadPayout(){
        if (!mydialog.isShowing())
            mydialog.show();
        apiService.payoutDetails(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_CURRENCYLIST,this));
    }

    public void onSuccessPayout(JsonResponse jsonResp){
        Constants.backpressed=0;
        payoutDetailResult = gson.fromJson(jsonResp.getStrResponse(), PayoutDetailResult.class);
        payoutDetails.clear();
        ArrayList <PayoutDetail> payDetail= payoutDetailResult.getPayout_details();
        payoutDetails.addAll(payDetail);
        adapter.notifyDataChanged();

        userPayoutDetails = payoutDetailResult.getUserPayoutDetails();
        firstname=payoutDetailResult.getUserPayoutDetails().getFirstname();
        lastname=payoutDetailResult.getUserPayoutDetails().getLastname();
        email=payoutDetailResult.getUserPayoutDetails().getEmail();
        gender=payoutDetailResult.getUserPayoutDetails().getGender();
        day= String.valueOf(payoutDetailResult.getUserPayoutDetails().getDay());
        month= String.valueOf(payoutDetailResult.getUserPayoutDetails().getMonth());
        year= String.valueOf(payoutDetailResult.getUserPayoutDetails().getYear());
        dob= String.valueOf(payoutDetailResult.getUserPayoutDetails().getDob());

        localSharedPreferences.saveSharedPreferences(Constants.STRIPE_KEY,payoutDetailResult.getStripe_publish_key());

        if (mydialog.isShowing())
            mydialog.dismiss();
        if (payoutDetails.size()>0){
            payoutmain_msg.setVisibility(View.GONE);
            payoutmain_title.setVisibility(View.GONE);
            payoutmain_link.setVisibility(View.GONE);
            payout_addpayout.setVisibility(View.VISIBLE);
            payout_addstripe.setVisibility(View.VISIBLE);
        }else{
            payoutmain_msg.setVisibility(View.VISIBLE);
            payoutmain_title.setVisibility(View.VISIBLE);
            payoutmain_link.setVisibility(View.VISIBLE);
            payout_addpayout.setVisibility(View.VISIBLE);
            payout_addstripe.setVisibility(View.VISIBLE);
        }
    }

    //Show network error and exception
    public void snackBar(String statusmessage) {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(recyclerView, "", Snackbar.LENGTH_LONG);
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
            textViewTop.setText(statusmessage);
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }

    // Check network available or not
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
