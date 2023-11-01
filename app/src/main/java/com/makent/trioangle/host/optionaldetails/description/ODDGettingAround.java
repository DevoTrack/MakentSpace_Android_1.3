package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDGettingAround
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
descripe about Getting arround the room
*************************************************************** */
public class ODDGettingAround extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout getting_title,gettingmsg_title;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomgetting_msg,getting_msg;
    TextView getting_count_txt;
    EditText getting_edittext;
    ImageView gettingmsg_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddgetting_around);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        getting_title = (RelativeLayout) findViewById(R.id.getting_title);
        getting_title.setOnClickListener(this);

        gettingmsg_title = (RelativeLayout) findViewById(R.id.gettingmsg_title);
        gettingmsg_title.setOnClickListener(this);

        gettingmsg_dot_loader = (ImageView) findViewById(R.id.gettingmsg_dot_loader);
        gettingmsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(gettingmsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomgetting_msg=localSharedPreferences.getSharedPreferences(Constants.DesGettingMsg);

        getting_count_txt=(TextView) findViewById(R.id.getting_count_txt);
        getting_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        getting_edittext=(EditText)findViewById(R.id.getting_edittext);
        getting_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomgetting_msg!=null&&!roomgetting_msg.equals("")) {
            getting_edittext.setText(roomgetting_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getting_title: {
                updateGetting();
            }
            break;

            case R.id.gettingmsg_title: {
                // onBackPressed();
                updateGetting();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            gettingmsg_title.setVisibility(View.VISIBLE);
            gettingmsg_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            gettingmsg_title.setVisibility(View.VISIBLE);
            gettingmsg_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,gettingmsg_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        gettingmsg_title.setVisibility(View.VISIBLE);
        gettingmsg_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, gettingmsg_title, getResources(), this);
        }
    }

    // Update Getting arround details
    public void updateGettingMessage(){
        gettingmsg_title.setVisibility(View.GONE);
        gettingmsg_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.DesGettingMsg,getting_msg);
        apiService.updateGettingAround(userid,roomid,getting_msg).enqueue(new RequestCallback(this));
    }





    // Text field text watcher
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
            String input=getting_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                getting_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {getting_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                getting_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    public void updateGetting(){
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        getting_msg=getting_edittext.getText().toString();
        getting_msg= getting_msg.replaceAll("^\\s+|\\s+$", "");

        if (isInternetAvailable) {

            if (roomgetting_msg == null || roomgetting_msg.equals("") || roomgetting_msg.equals(getting_msg) || getting_msg.equals("")) {
                if (!getting_msg.equals("")) {
                    updateGettingMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateGettingMessage();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, gettingmsg_title, getResources(), this);
        }
    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }



}
