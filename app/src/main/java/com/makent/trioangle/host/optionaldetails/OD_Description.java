package com.makent.trioangle.host.optionaldetails;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails
 * @category    OD_Description
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.optionaldetails.description.ODDExtraDetails;
import com.makent.trioangle.host.optionaldetails.description.ODDGettingAround;
import com.makent.trioangle.host.optionaldetails.description.ODDGuest;
import com.makent.trioangle.host.optionaldetails.description.ODDGuestInteraction;
import com.makent.trioangle.host.optionaldetails.description.ODDHouseRules;
import com.makent.trioangle.host.optionaldetails.description.ODDOverview;
import com.makent.trioangle.host.optionaldetails.description.ODDSpace;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.optionaldetail.ODdescriptionResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
Optional details for room full detail description
*************************************************************** */
public class OD_Description extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public RelativeLayout description_title;
    public RelativeLayout description_space;
    public RelativeLayout description_guestaccess;
    public RelativeLayout description_interaction_guest;
    public RelativeLayout description_overview;
    public RelativeLayout description_getting;
    public RelativeLayout description_otherthings;
    public RelativeLayout description_houserules;

    public LocalSharedPreferences localSharedPreferences;
    public String space_msg;
    public String guest_access_msg;
    public String interaction_with_guest_msg;
    public String overview_msg;
    public String getting_arround_msg;
    public String other_things_to_note_msg;
    public String house_rules_msg;

    public String userid;
    public String roomid;

    public TextView space_msg_txt;
    public TextView guest_msg;
    public TextView interaction_guest_msg;
    public TextView overview_msg_txt;
    public TextView gettingaround_msg;
    public TextView otherthing_msg;
    public TextView des_houserules_msg;
    public ImageView description_back;

    public Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    public ODdescriptionResult oDdescriptionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od_description);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        commonMethods = new CommonMethods();


        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        space_msg_txt = (TextView) findViewById(R.id.space_msg);
        guest_msg = (TextView) findViewById(R.id.guest_msg);
        interaction_guest_msg = (TextView) findViewById(R.id.interaction_guest_msg);
        overview_msg_txt = (TextView) findViewById(R.id.overview_msg);
        gettingaround_msg = (TextView) findViewById(R.id.gettingaround_msg);
        otherthing_msg = (TextView) findViewById(R.id.otherthing_msg);
        des_houserules_msg = (TextView) findViewById(R.id.des_houserules_msg);

        description_title = (RelativeLayout) findViewById(R.id.description_title);
        description_space = (RelativeLayout) findViewById(R.id.description_space);
        description_guestaccess = (RelativeLayout) findViewById(R.id.description_guestaccess);
        description_interaction_guest = (RelativeLayout) findViewById(R.id.description_interaction_guest);
        description_overview = (RelativeLayout) findViewById(R.id.description_overview);
        description_getting = (RelativeLayout) findViewById(R.id.description_getting);
        description_otherthings = (RelativeLayout) findViewById(R.id.description_otherthings);
        description_houserules = (RelativeLayout) findViewById(R.id.description_houserules);
        description_back =(ImageView)findViewById(R.id.description_back);
        commonMethods.rotateArrow(description_back,this);

        description_title.setOnClickListener(this);
        description_space.setOnClickListener(this);
        description_guestaccess.setOnClickListener(this);
        description_interaction_guest.setOnClickListener(this);
        description_overview.setOnClickListener(this);
        description_getting.setOnClickListener(this);
        description_otherthings.setOnClickListener(this);
        description_houserules.setOnClickListener(this);

        if (isInternetAvailable) {

            updateDescription();
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,description_houserules,getResources(),this);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.description_title: {

                localSharedPreferences.saveSharedPreferences(Constants.DesSpaceMsg,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesGuestMsg,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesGuestInteractionMsg,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesOverviewMsg,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesGettingMsg,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesOtherThings,null);
                localSharedPreferences.saveSharedPreferences(Constants.DesHouseMsg,null);
                onBackPressed();
            }
            break;
            case R.id.description_space: {
                // Room description about the space
                Intent price=new Intent(getApplicationContext(), ODDSpace.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_guestaccess: {
                // Room description about the Guest access
                Intent price=new Intent(getApplicationContext(), ODDGuest.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_interaction_guest: {
                // Room description about the guest interaction
                Intent price=new Intent(getApplicationContext(), ODDGuestInteraction.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_overview: {
                // Room description about the overview
                Intent price=new Intent(getApplicationContext(), ODDOverview.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_getting: {
                // Room description about the getting around
                Intent price=new Intent(getApplicationContext(), ODDGettingAround.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_otherthings: {
                // Room description about the extra details
                Intent price=new Intent(getApplicationContext(), ODDExtraDetails.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            case R.id.description_houserules: {
                // Room description about the house rules
                Intent price=new Intent(getApplicationContext(), ODDHouseRules.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(price,bndlanimation);
            }
            break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessDes(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing())
                mydialog.dismiss();
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,description_houserules,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing())
            mydialog.dismiss();
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,description_houserules,getResources(),this);
    }

    public void updateDescription(){
        if(!mydialog.isShowing())
            mydialog.show();
        apiService.getDescription(userid,roomid).enqueue(new RequestCallback(this));
    }

    public void onSuccessDes(JsonResponse jsonResp){
        oDdescriptionResult = gson.fromJson(jsonResp.getStrResponse(), ODdescriptionResult.class);
        space_msg = oDdescriptionResult.getSpaceMsg();
        guest_access_msg = oDdescriptionResult.getGuestAccessMsg();
        interaction_with_guest_msg = oDdescriptionResult.getInteractionWithGuestMsg();
        overview_msg = oDdescriptionResult.getOverviewMsg();
        getting_arround_msg = oDdescriptionResult.getGettingArroundMsg();
        other_things_to_note_msg = oDdescriptionResult.getOtherThingsToNoteMsg();
        house_rules_msg = oDdescriptionResult.getHouseRulesMsg();

        localSharedPreferences.saveSharedPreferences(Constants.DesSpaceMsg,space_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesGuestMsg,guest_access_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesGuestInteractionMsg,interaction_with_guest_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesOverviewMsg,overview_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesGettingMsg,getting_arround_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesOtherThings,other_things_to_note_msg);
        localSharedPreferences.saveSharedPreferences(Constants.DesHouseMsg,house_rules_msg);

        loadDetails();
        if (mydialog.isShowing())
            mydialog.dismiss();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDetails();
    }

    // Load edited details
    public void loadDetails()
    {

        space_msg=localSharedPreferences.getSharedPreferences(Constants.DesSpaceMsg);
        guest_access_msg=localSharedPreferences.getSharedPreferences(Constants.DesGuestMsg);
        interaction_with_guest_msg=localSharedPreferences.getSharedPreferences(Constants.DesGuestInteractionMsg);
        overview_msg=localSharedPreferences.getSharedPreferences(Constants.DesOverviewMsg);
        getting_arround_msg=localSharedPreferences.getSharedPreferences(Constants.DesGettingMsg);
        other_things_to_note_msg=localSharedPreferences.getSharedPreferences(Constants.DesOtherThings);
        house_rules_msg=localSharedPreferences.getSharedPreferences(Constants.DesHouseMsg);


        if(space_msg!=null&&!space_msg.equals("")) {
            space_msg_txt.setText(space_msg);
        }

        if(guest_access_msg!=null&&!guest_access_msg.equals("")) {
            guest_msg.setText(guest_access_msg);
        }

        if(interaction_with_guest_msg!=null&&!interaction_with_guest_msg.equals("")) {
            interaction_guest_msg.setText(interaction_with_guest_msg);
        }

        if(overview_msg!=null&&!overview_msg.equals("")) {
            overview_msg_txt.setText(overview_msg);
        }

        if(getting_arround_msg!=null&&!getting_arround_msg.equals("")) {
            gettingaround_msg.setText(getting_arround_msg);
        }

        if(other_things_to_note_msg!=null&&!other_things_to_note_msg.equals("")) {
            otherthing_msg.setText(other_things_to_note_msg);
        }


        if(house_rules_msg!=null&&!house_rules_msg.equals("")) {
            des_houserules_msg.setText(house_rules_msg);
        }

    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
