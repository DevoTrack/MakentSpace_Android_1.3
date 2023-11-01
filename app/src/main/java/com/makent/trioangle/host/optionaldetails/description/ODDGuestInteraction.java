package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDGuestInteraction
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
Descripe about Guest Interaction
*************************************************************** */

public class ODDGuestInteraction extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    RelativeLayout guest_interaction_title,guestinteraction_title;

    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomguestinteraction_msg,guestinteration_msg;
    TextView guest_interaction_count_txt;
    EditText guest_interaction_edittext;
    ImageView guestinteraction_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddguest_interaction);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        guest_interaction_title = (RelativeLayout) findViewById(R.id.guest_interaction_title);
        guest_interaction_title.setOnClickListener(this);

        guestinteraction_title = (RelativeLayout) findViewById(R.id.guestinteraction_title);
        guestinteraction_title.setOnClickListener(this);

        guestinteraction_dot_loader = (ImageView) findViewById(R.id.guestinteraction_dot_loader);
        guestinteraction_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(guestinteraction_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomguestinteraction_msg=localSharedPreferences.getSharedPreferences(Constants.DesGuestInteractionMsg);

        guest_interaction_count_txt=(TextView) findViewById(R.id.guest_interaction_count_txt);
        guest_interaction_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        guest_interaction_edittext=(EditText)findViewById(R.id.guest_interaction_edittext);
        guest_interaction_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomguestinteraction_msg!=null&&!roomguestinteraction_msg.equals("")) {
            guest_interaction_edittext.setText(roomguestinteraction_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guest_interaction_title: {
                updateGuestInteraction();
            }
            break;

            case R.id.guestinteraction_title: {
                // onBackPressed();
                updateGuestInteraction();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            guestinteraction_title.setVisibility(View.GONE);
            guestinteraction_dot_loader.setVisibility(View.VISIBLE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            guestinteraction_title.setVisibility(View.GONE);
            guestinteraction_dot_loader.setVisibility(View.VISIBLE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,guestinteraction_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        guestinteraction_title.setVisibility(View.GONE);
        guestinteraction_dot_loader.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, guestinteraction_title, getResources(), this);
        }
    }

    // Update guest interaction message
    public void updateGuestInteractionMessage(){
        guestinteraction_title.setVisibility(View.GONE);
        guestinteraction_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.DesGuestInteractionMsg,guestinteration_msg);
        apiService.updateGuestInteratction(userid,roomid,guestinteration_msg).enqueue(new RequestCallback(this));
    }


    // Textfield validation
    private final TextWatcher mTextEditorWatcher=new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            int slenght=s.length();
            String input=guest_interaction_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                guest_interaction_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {guest_interaction_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                guest_interaction_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };
    public void updateGuestInteraction(){
        guestinteration_msg=guest_interaction_edittext.getText().toString();
        guestinteration_msg= guestinteration_msg.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (roomguestinteraction_msg == null || roomguestinteraction_msg.equals("") || roomguestinteraction_msg.equals(guestinteration_msg) || guestinteration_msg.equals("")) {
                if (!guestinteration_msg.equals("")) {
                    updateGuestInteractionMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateGuestInteractionMessage();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, guest_interaction_edittext, getResources(), this);
        }
    }



    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
