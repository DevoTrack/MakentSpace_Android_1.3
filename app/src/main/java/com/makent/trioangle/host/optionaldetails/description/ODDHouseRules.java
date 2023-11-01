package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDHouseRules
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
Descripe about House Rules
*************************************************************** */
public class ODDHouseRules extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout des_house_rules_title,des_house_rulesmsg_title;

    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomhouse_rules_msg,house_rules_msg;
    EditText des_house_rules_edittext;
    ImageView des_house_rulesmsg_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddhouse_rules);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        des_house_rules_title = (RelativeLayout) findViewById(R.id.des_house_rules_title);
        des_house_rules_title.setOnClickListener(this);
        des_house_rulesmsg_title = (RelativeLayout) findViewById(R.id.des_house_rulesmsg_title);
        des_house_rulesmsg_title.setOnClickListener(this);

        des_house_rulesmsg_dot_loader = (ImageView) findViewById(R.id.des_house_rulesmsg_dot_loader);
        des_house_rulesmsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(des_house_rulesmsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomhouse_rules_msg=localSharedPreferences.getSharedPreferences(Constants.DesHouseMsg);



        des_house_rules_edittext=(EditText)findViewById(R.id.des_house_rules_edittext);
        if(roomhouse_rules_msg!=null&&!roomhouse_rules_msg.equals("")) {
            des_house_rules_edittext.setText(roomhouse_rules_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.des_house_rules_title: {
                updateHouseRules();
            }
            break;

            case R.id.des_house_rulesmsg_title: {
                // onBackPressed();
                updateHouseRules();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            des_house_rulesmsg_title.setVisibility(View.VISIBLE);
            des_house_rulesmsg_dot_loader.setVisibility(View.GONE);


            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            des_house_rulesmsg_title.setVisibility(View.VISIBLE);
            des_house_rulesmsg_dot_loader.setVisibility(View.GONE);

            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,des_house_rulesmsg_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        des_house_rulesmsg_title.setVisibility(View.VISIBLE);
        des_house_rulesmsg_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, des_house_rulesmsg_title, getResources(), this);
        }
    }

    // Update extra details
    public void updateHouseRulesMessage(){
        des_house_rulesmsg_title.setVisibility(View.GONE);
        des_house_rulesmsg_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.DesHouseMsg,house_rules_msg);
        apiService.updateHouseRulesMessage(userid,roomid,house_rules_msg).enqueue(new RequestCallback(this));
    }



    public void updateHouseRules(){
        house_rules_msg=des_house_rules_edittext.getText().toString();
        house_rules_msg= house_rules_msg.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (roomhouse_rules_msg == null || roomhouse_rules_msg.equals("") || roomhouse_rules_msg.equals(house_rules_msg) || house_rules_msg.equals("")) {
                if (!house_rules_msg.equals("")) {
                    updateHouseRulesMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateHouseRulesMessage();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, des_house_rules_edittext, getResources(), this);
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}


