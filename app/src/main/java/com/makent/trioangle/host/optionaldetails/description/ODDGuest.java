package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDGuest
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
Descripe about Guest
*************************************************************** */
public class ODDGuest extends AppCompatActivity implements View.OnClickListener,ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout guest_title,guestmsg_title;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomguest_msg,guest_msg;
    TextView guest_count_txt;
    EditText guest_edittext;
    ImageView guestmsg_dot_loader;
    protected boolean isInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddguest);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        guest_title = (RelativeLayout) findViewById(R.id.guest_title);
        guest_title.setOnClickListener(this);

        guestmsg_title = (RelativeLayout) findViewById(R.id.guestmsg_title);
        guestmsg_title.setOnClickListener(this);

        guestmsg_dot_loader = (ImageView) findViewById(R.id.guestmsg_dot_loader);
        guestmsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(guestmsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomguest_msg=localSharedPreferences.getSharedPreferences(Constants.DesGuestMsg);

        guest_count_txt=(TextView) findViewById(R.id.guest_count_txt);
        guest_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        guest_edittext=(EditText)findViewById(R.id.guest_edittext);
        guest_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomguest_msg!=null&&!roomguest_msg.equals("")) {
            guest_edittext.setText(roomguest_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guest_title: {
                updateGuest();
            }
            break;

            case R.id.guestmsg_title: {
                // onBackPressed();
                updateGuest();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            guestmsg_title.setVisibility(View.VISIBLE);
            guestmsg_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            guestmsg_title.setVisibility(View.VISIBLE);
            guestmsg_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,guest_edittext,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        guestmsg_title.setVisibility(View.VISIBLE);
        guestmsg_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, guest_edittext, getResources(), this);
        }
    }

    // Update message for guest
    public void updateGuestMessage(){
        localSharedPreferences.saveSharedPreferences(Constants.DesGuestMsg,guest_msg);
        guestmsg_title.setVisibility(View.GONE);
        guestmsg_dot_loader.setVisibility(View.VISIBLE);
        apiService.updateGuestSpace(userid,roomid,guest_msg).enqueue(new RequestCallback(this));
    }


    // Text field validation
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
            String input=guest_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                guest_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {guest_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                guest_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    public void updateGuest(){
        guest_msg=guest_edittext.getText().toString();
        guest_msg= guest_msg.replaceAll("^\\s+|\\s+$", "");

        isInternetAvailable = getNetworkState().isConnectingToInternet();
if (isInternetAvailable) {
    if (roomguest_msg == null || roomguest_msg.equals("") || roomguest_msg.equals(guest_msg) || guest_msg.equals("")) {
        if (!guest_msg.equals("")) {
            updateGuestMessage();
        } else {
            onBackPressed();
            finish();
        }
    } else {
        updateGuestMessage();
    }
}else {
    commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, guest_edittext, getResources(), this);
}
    }


    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
