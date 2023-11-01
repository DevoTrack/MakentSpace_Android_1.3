package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostPreAcceptActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.tabs.YourReservationActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is  Contain Pre Accept Activity
**************************************************************************  */
public class PreAcceptActivity extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout preaccept_close;
    EditText preaccept_msg;
    Button preaccept,preacceptclose;
    TextView preaccept_txt;
    protected boolean isInternetAvailable;
    String userid,reservationid,trip_status,preacceptmessage;
    int isHost;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_accept);
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        Intent x=getIntent();
        reservationid=x.getStringExtra("reservationid");
        trip_status=x.getStringExtra("tripstatus");

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        isHost=localSharedPreferences.getSharedPreferencesInt(Constants.isHost);

        preaccept_close=(RelativeLayout)findViewById(R.id.preaccept_close);
        preaccept_msg=(EditText) findViewById(R.id.preaccept_msg);
        preaccept=(Button) findViewById(R.id.preaccept);

        preaccept_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        preaccept_txt=(TextView)findViewById(R.id.preaccept_txt);
        commonMethods.setTvAlign(preaccept_close,this);
        preacceptclose=(Button) findViewById(R.id.preacceptclose);

        preacceptclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        preaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preacceptmessage=preaccept_msg.getText().toString();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    preApprove();// this is used to preAccept api call function
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,preaccept_msg,preaccept_msg,getResources(),PreAcceptActivity.this);
                }
            }
        });

        if(isHost==0&&trip_status.equals("Inquiry"))
        {
            preaccept_txt.setText(getResources().getString(R.string.preapproverequest));
            preaccept.setText(getResources().getString(R.string.preapprove));
        }
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessRes();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }

    }

    public void onSuccessRes(){
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        int isHost=localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        if(isHost==1) {
            Intent x = new Intent(getApplicationContext(), YourReservationActivity.class);
            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(x);
            finish();
        }else
        {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            x.putExtra("tabsaved", 4);
            startActivity(x);
            finish();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        Toast.makeText(getApplicationContext(),"Invalide data",Toast.LENGTH_SHORT).show();
    }
    public void preApprove(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        if(isHost==0&&trip_status.equals("Inquiry")) {
            apiService.preapprove(localSharedPreferences.getSharedPreferences(Constants.AccessToken), reservationid, "1", preacceptmessage).enqueue(new RequestCallback(this));
        }else{
            apiService.accept(localSharedPreferences.getSharedPreferences(Constants.AccessToken),reservationid,preacceptmessage).enqueue(new RequestCallback(this));
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}