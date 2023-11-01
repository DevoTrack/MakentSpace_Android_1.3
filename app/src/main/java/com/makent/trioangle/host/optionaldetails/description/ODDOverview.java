package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDOverview
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
Descripe about overview of rooms
*************************************************************** */

public class ODDOverview extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout overview_title,overviewmsg_title;

    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomoverview_msg,overview_msg;
    TextView overview_count_txt;
    EditText overview_edittext;
    ImageView overviewmsg_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oddoverview);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        overview_title = (RelativeLayout) findViewById(R.id.overview_title);
        overview_title.setOnClickListener(this);

        overviewmsg_title = (RelativeLayout) findViewById(R.id.overviewmsg_title);
        overviewmsg_title.setOnClickListener(this);

        overviewmsg_dot_loader = (ImageView) findViewById(R.id.overviewmsg_dot_loader);
        overviewmsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(overviewmsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomoverview_msg=localSharedPreferences.getSharedPreferences(Constants.DesOverviewMsg);

        overview_count_txt=(TextView) findViewById(R.id.overview_count_txt);
        overview_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        overview_edittext=(EditText)findViewById(R.id.overview_edittext);
        overview_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomoverview_msg!=null&&!roomoverview_msg.equals("")) {
            overview_edittext.setText(roomoverview_msg);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.overview_title: {
                updateOverView();
            }
            break;

            case R.id.overviewmsg_title: {
                // onBackPressed();
                updateOverView();
            }
            break;

        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            overviewmsg_title.setVisibility(View.GONE);
            overviewmsg_dot_loader.setVisibility(View.VISIBLE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            overviewmsg_title.setVisibility(View.GONE);
            overviewmsg_dot_loader.setVisibility(View.VISIBLE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,overviewmsg_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        overviewmsg_title.setVisibility(View.GONE);
        overviewmsg_dot_loader.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, overviewmsg_title, getResources(), this);
        }
    }

    // Update Overview message
    public void updateGuestInteractionMessage(){
        overviewmsg_title.setVisibility(View.GONE);
        overviewmsg_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.DesOverviewMsg,overview_msg);
        apiService.updateNeighborhoodOverview(userid,roomid,overview_msg).enqueue(new RequestCallback(this));
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
            String input=overview_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                overview_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {overview_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                overview_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    public void updateOverView(){
        overview_msg=overview_edittext.getText().toString();
        overview_msg= overview_msg.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (roomoverview_msg == null || roomoverview_msg.equals("") || roomoverview_msg.equals(overview_msg) || overview_msg.equals("")) {
                if (!overview_msg.equals("")) {
                    updateGuestInteractionMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateGuestInteractionMessage();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, overview_edittext, getResources(), this);
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
