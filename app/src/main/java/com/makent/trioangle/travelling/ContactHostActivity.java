package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category ContactHostActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ContactHostActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    Button contacthost_close, typeyourmessage;
    LinearLayout contacthost_date, contacthost_guest, contacthost_yourmessage;
    String[] blocked_dates;
    LocalSharedPreferences localSharedPreferences;
    String checkinouttext, searchguest, reqmessagetext, roomIdsend, checkinsend, checkoutsend, accesstokensend, roomtypeusername;
    String statusmessage, hostuserimage, guest_counts, hostusername;
    TextView currentbookdate_txt, guestcount_txt, Yourmesssage_text, contacthost_roomtype;
    ImageView room_details_hostprofile;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_host);
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences = new LocalSharedPreferences(this);

        contacthost_close = (Button) findViewById(R.id.contacthost_close);
        typeyourmessage = (Button) findViewById(R.id.typeyourmessage);
        contacthost_date = (LinearLayout) findViewById(R.id.contacthost_date);
        contacthost_guest = (LinearLayout) findViewById(R.id.contacthost_guest);
        contacthost_yourmessage = (LinearLayout) findViewById(R.id.contacthost_yourmessage);
        currentbookdate_txt = (TextView) findViewById(R.id.currentbookdate_txt);
        guestcount_txt = (TextView) findViewById(R.id.guestcount_txt);
        Yourmesssage_text = (TextView) findViewById(R.id.Yourmesssage_text);
        contacthost_roomtype = (TextView) findViewById(R.id.contacthost_roomtype);
        room_details_hostprofile = (ImageView) findViewById(R.id.contacthostprofile);
        commonMethods.setTvAlign(contacthost_close,this);

        contacthost_close.setOnClickListener(this);
        typeyourmessage.setOnClickListener(this);
        contacthost_date.setOnClickListener(this);
        contacthost_guest.setOnClickListener(this);
        contacthost_yourmessage.setOnClickListener(this);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(Yourmesssage_text.getText().toString().trim() == null || Yourmesssage_text.getText().toString().trim().equals("") || Yourmesssage_text.getText().toString().trim().isEmpty() || Yourmesssage_text.getText().toString().trim().equals("null")){
            typeyourmessage.setEnabled(false);
            typeyourmessage.setBackgroundResource(R.drawable.d_redbutton_enable_disable);
        }
        else {
            typeyourmessage.setEnabled(true);
            typeyourmessage.setBackgroundResource(R.drawable.d_red_button);
        }

        Intent x = getIntent();
        blocked_dates = x.getStringArrayExtra("blockdate");
        hostuserimage = x.getStringExtra("hostuserimage");
        hostusername = x.getStringExtra("hostusername");
        roomtypeusername = x.getStringExtra("roomtypeusername");
        guest_counts = x.getStringExtra("guestcounts");

        localSharedPreferences.saveSharedPreferences(Constants.HostUserName2, hostusername);

        contacthost_roomtype.setText(roomtypeusername);
        Glide.with(getApplicationContext()).asBitmap().load(hostuserimage).into(new BitmapImageViewTarget(room_details_hostprofile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                room_details_hostprofile.setImageDrawable(circularBitmapDrawable);
            }
        });

        checkinouttext = localSharedPreferences.getSharedPreferences(Constants.CheckInOut);
        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        reqmessagetext = localSharedPreferences.getSharedPreferences(Constants.ReqMessage);

        if (checkinouttext != null) {
            currentbookdate_txt.setText(checkinouttext);
        } else {
            checkinouttext = localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut);
            if (checkinouttext != null) {
                currentbookdate_txt.setText(checkinouttext);
            } else {
                currentbookdate_txt.setText(getString(R.string.date));
            }
        }
        Yourmesssage_text.setText(reqmessagetext);
        if(reqmessagetext == null || reqmessagetext.equals("") || reqmessagetext.isEmpty() || reqmessagetext.equals("null")){
            typeyourmessage.setEnabled(false);
            typeyourmessage.setBackgroundResource(R.drawable.d_redbutton_enable_disable);
        }
        else {
            typeyourmessage.setEnabled(true);
            typeyourmessage.setBackgroundResource(R.drawable.d_red_button);
        }

        if (searchguest == null) {
            searchguest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
            if (searchguest == null)
                searchguest = "1";
        }

        if (searchguest != null) {
            if (searchguest.equals("1")) {
                guestcount_txt.setText(searchguest + " "+ getString(R.string.s_guest));
            } else {
                guestcount_txt.setText(searchguest + " "+ getString(R.string.guests));
            }
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacthost_close: {
                onBackPressed();
            }
            break;
            case R.id.typeyourmessage: {
                /*Intent x=new Intent(getApplicationContext(),MessageToHostActivity.class);
                startActivity(x);*/
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                roomIdsend = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
                checkinsend = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
                checkoutsend = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
                accesstokensend = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

                if (checkinsend == null && checkoutsend == null) {
                    checkinsend = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
                    checkoutsend = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
                }
                if (checkinsend != null && checkoutsend != null) {
                    if (isInternetAvailable) {

                        sendDetails();
                    } else {
                        commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,typeyourmessage,getResources(),ContactHostActivity.this);
                    }
                } else {
                    statusmessage = getResources().getString(R.string.date_error);
                    commonMethods.snackBar(statusmessage,"",false,2,typeyourmessage,getResources(),ContactHostActivity.this);
                }
                //SendUser();
            }
            break;
            case R.id.contacthost_date: {
                Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                x.putExtra("blockdate", blocked_dates);
                x.putExtra("nodate", checkinouttext);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.contacthost_guest: {
                // This is used to call the ContactHostActivity to  Search_Guest_Bed and pass the values to aother activity
                Intent x = new Intent(getApplicationContext(), Search_Guest_Bed.class);
                x.putExtra("search", "0");
                x.putExtra("guest", "Contacthostback");
                x.putExtra("blockdate", blocked_dates);
                x.putExtra("hostuserimage", hostuserimage);
                x.putExtra("roomtypeusername", roomtypeusername);
                x.putExtra("hostusername", hostusername);
                x.putExtra("guestcounts", guest_counts);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
            break;
            case R.id.contacthost_yourmessage: {
                // This is used to call the ContactHostActivity to  MessageToHostActivity
                Intent x = new Intent(getApplicationContext(), MessageToHostActivity.class);
                x.putExtra("hostprofile", hostuserimage);
                x.putExtra("hostprofilename", hostusername);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;

        }

    }


    @Override

    protected void onResume() {
        super.onResume();
        checkinouttext = localSharedPreferences.getSharedPreferences(Constants.CheckInOut);
        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        reqmessagetext = localSharedPreferences.getSharedPreferences(Constants.ReqMessage);

        Log.e("RequestMessage","RequestMessage"+reqmessagetext);
        if (checkinouttext != null) {
            currentbookdate_txt.setText(checkinouttext);
        } else {
            checkinouttext = localSharedPreferences.getSharedPreferences(Constants.SearchCheckInOut);
            if (checkinouttext != null) {
                currentbookdate_txt.setText(checkinouttext);
            } else {
                currentbookdate_txt.setText(getResources().getString(R.string.date));
            }
        }
        Yourmesssage_text.setText(reqmessagetext);

        if(reqmessagetext == null || reqmessagetext.isEmpty() || reqmessagetext.equals("") || reqmessagetext.equals("null")){
            typeyourmessage.setEnabled(false);
            typeyourmessage.setClickable(false);
            typeyourmessage.setBackgroundResource(R.drawable.d_redbutton_enable_disable);
        }
        else {
            typeyourmessage.setEnabled(true);
            typeyourmessage.setClickable(true);
            typeyourmessage.setBackgroundResource(R.drawable.d_red_button);
        }

        if (searchguest == null) {
            searchguest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
            if (searchguest == null)
                searchguest = "1";
        }

        if (searchguest != null) {
            if (searchguest.equals("1")) {
                guestcount_txt.setText(searchguest + " " + getResources().getString(R.string.s_guest));
            } else {
                guestcount_txt.setText(searchguest + " "+ getResources().getString(R.string.g_guest));
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getIntExtra("ContactBack",0)==1){
            localSharedPreferences.saveSharedPreferences(Constants.REQBACK,"goback");
            if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn)!=null
            &&localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut)!=null){
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck,"1");
            }
            Intent rooms=new Intent(getApplicationContext(), SpaceDetailActivity.class);
            startActivity(rooms);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onBackPressed();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            String hostusername2 = localSharedPreferences.getSharedPreferences(Constants.HostUserName2);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.contacthost_msg) + hostusername2, Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, currentbookdate_txt, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, currentbookdate_txt, getResources(), this);
    }

    public void sendDetails() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        reqmessagetext = localSharedPreferences.getSharedPreferences(Constants.ReqMessage);
        if (reqmessagetext == null) {
            reqmessagetext = "";
        }
        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        if (searchguest == null) {
            searchguest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
            if (searchguest == null)
                searchguest = "1";
        }
        try {
            reqmessagetext = URLEncoder.encode(reqmessagetext, "UTF-8");
            reqmessagetext = reqmessagetext.replace("+"," ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        apiService.contactHost(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomIdsend,checkinsend, checkoutsend , searchguest , reqmessagetext).enqueue(new RequestCallback(this));
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
