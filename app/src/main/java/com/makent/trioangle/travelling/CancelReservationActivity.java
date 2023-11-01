package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestCancelReservationActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.host.tabs.YourReservationActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class CancelReservationActivity extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    private Spinner spinner;
    RelativeLayout cancel_close;
    EditText cancel_reason;
    Button cancelreservation,cancelclose;
    protected boolean isInternetAvailable;
    String userid,reservationid,tripstatus,cancelreason,cancelmessage;
    int isHost;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    int tabsaved=5;
    int fromHost = 0;
    String statusmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods = new CommonMethods();

        // Get the reservationid and tripstatus
        Intent x=getIntent();
        reservationid=x.getStringExtra("reservationid");
        tripstatus=x.getStringExtra("tripstatus");
        fromHost = x.getIntExtra("fromHost",0);

        System.out.println("tripstatus : "+tripstatus);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        isHost=localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> canceladapter;
        if(tripstatus.equals("Inquiry")||tripstatus.equals("Pending"))
        {
            isHost=1;
            tabsaved=4;
        }
        if(isHost==0)
        {
            canceladapter = ArrayAdapter.createFromResource(
                    this, R.array.cancel_types, R.layout.spinner_layout);
            canceladapter.setDropDownViewResource(R.layout.spinner_layout);
        }
        else
        {
            canceladapter = ArrayAdapter.createFromResource(
                    this, R.array.cancel_types_host, R.layout.spinner_layout);
            canceladapter.setDropDownViewResource(R.layout.spinner_layout);
        }
        spinner.setAdapter(canceladapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code for setting the ivPhoto based on the item clicked....here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        cancel_close=(RelativeLayout)findViewById(R.id.cancel_close);
        cancel_reason=(EditText) findViewById(R.id.cancel_reason);
        cancelreservation=(Button) findViewById(R.id.cancelreservation);
        cancelclose=(Button) findViewById(R.id.cancelclose);
        commonMethods.setTvAlign(cancelclose,this);

        cancelclose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        cancel_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
              finish();
            }
        });

        cancelreservation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String spinnerpos= String.valueOf(spinner.getSelectedItemPosition());
                if (spinnerpos.equals("0"))
                {
                    cancelreason="";
                }
                else
                {
                    cancelreason = spinner.getSelectedItem().toString();
                }
                cancelmessage=cancel_reason.getText().toString();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable)
                {
                    if (cancelreason.isEmpty())
                    {
                        commonMethods.snackBar("Please select action","",false,2,cancel_reason,cancel_reason,getResources(),CancelReservationActivity.this);
                    }
                    else if (cancelreason.equals("why are you declining"))
                    {
                        commonMethods.snackBar("Please select action","",false,2,cancel_reason,cancel_reason,getResources(),CancelReservationActivity.this);
                    }
                    else
                    {
                        updateCancelReason();
                    }
                }
                else
                {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,cancel_reason,cancel_reason,getResources(),CancelReservationActivity.this);
                }
            }
        });

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data)
    {
        if (jsonResp.isSuccess())
        {
            onSuccessCancel();
        }
        else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        {
            if (mydialog.isShowing())
            {
                mydialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data)
    {
        if (mydialog.isShowing())
        {
            mydialog.dismiss();
        }
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,cancel_reason,cancel_reason,getResources(),this);
    }

    public void updateCancelReason(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        if(isHost==0) {
            apiService.guestCancelReservation(localSharedPreferences.getSharedPreferences(Constants.AccessToken),reservationid,cancelreason,cancelmessage).enqueue(new RequestCallback(this));

        }else {
            apiService.hostCancelReservation(localSharedPreferences.getSharedPreferences(Constants.AccessToken),reservationid,cancelreason,cancelmessage).enqueue(new RequestCallback(this));

        }
    }




    public void onSuccessCancel(){
        if (mydialog.isShowing())
        {
            mydialog.dismiss();
        }
        if(isHost==0)
        {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            x.putExtra("tabsaved", tabsaved);
            startActivity(x);
            finish();
        }
        else if(fromHost==1&&tripstatus.equals("Accepted"))
        {
//            Intent x = new Intent(getApplicationContext(), HostHome.class);
//            startActivity(x);
//            finish();

            Intent x = new Intent(getApplicationContext(), YourReservationActivity.class);
            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(x);
            finish();

        }else{
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            x.putExtra("tabsaved", tabsaved);
            startActivity(x);
            finish();
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
