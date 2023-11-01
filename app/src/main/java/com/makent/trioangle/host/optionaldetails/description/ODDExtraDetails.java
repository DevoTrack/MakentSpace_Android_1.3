package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDExtraDetails
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
descripe about room extra details
*************************************************************** */
public class ODDExtraDetails extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout extradetails_title,extradetailsmsg_title;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomextra_msg,extra_msg;
    TextView extradetails_count_txt;
    EditText extradetails_edittext;
    ImageView extradetailsmsg_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddextra_details);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        extradetails_title = (RelativeLayout) findViewById(R.id.extradetails_title);
        extradetails_title.setOnClickListener(this);

        extradetailsmsg_title = (RelativeLayout) findViewById(R.id.extradetailsmsg_title);
        extradetailsmsg_title.setOnClickListener(this);

        extradetailsmsg_dot_loader = (ImageView) findViewById(R.id.extradetailsmsg_dot_loader);
        extradetailsmsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(extradetailsmsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomextra_msg=localSharedPreferences.getSharedPreferences(Constants.DesOtherThings);

        extradetails_count_txt=(TextView) findViewById(R.id.extradetails_count_txt);
        extradetails_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        extradetails_edittext=(EditText)findViewById(R.id.extradetails_edittext);
        extradetails_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomextra_msg!=null&&!roomextra_msg.equals("")) {
            extradetails_edittext.setText(roomextra_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.extradetails_title: {
                updateExtra();
            }
            break;

            case R.id.extradetailsmsg_title: {
                // onBackPressed();
                updateExtra();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            extradetailsmsg_title.setVisibility(View.VISIBLE);
            extradetailsmsg_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            extradetailsmsg_title.setVisibility(View.VISIBLE);
            extradetailsmsg_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,extradetailsmsg_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        extradetailsmsg_title.setVisibility(View.VISIBLE);
        extradetailsmsg_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, extradetailsmsg_title, getResources(), this);
        }
    }

    // Update extra details
    public void updateGettingMessage(){
        extradetailsmsg_title.setVisibility(View.GONE);
        extradetailsmsg_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.DesOtherThings,extra_msg);
        apiService.updateExtraMessage(userid,roomid,extra_msg).enqueue(new RequestCallback(this));
    }


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
            String input=extradetails_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                extradetails_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {extradetails_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                extradetails_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    public void updateExtra(){

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        extra_msg=extradetails_edittext.getText().toString();
        extra_msg= extra_msg.replaceAll("^\\s+|\\s+$", "");

        if (isInternetAvailable) {

            if (roomextra_msg == null || roomextra_msg.equals("") || roomextra_msg.equals(extra_msg) || extra_msg.equals("")) {
                if (!extra_msg.equals("")) {
                    updateGettingMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateGettingMessage();
            }
        }
        else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, extradetails_edittext, getResources(), this);
        }
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}

