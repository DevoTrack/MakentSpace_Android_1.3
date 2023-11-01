package com.makent.trioangle.host.optionaldetails.description;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionadetails/description
 * @category    ODDSpace
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
Describe about rooms space
*************************************************************** */
public class ODDSpace extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    RelativeLayout space_title,spacemsg_title;

    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomspace_msg,space_msg;
    TextView space_count_txt;
    EditText space_edittext;
    ImageView spacemsg_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odd_space);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        space_title = (RelativeLayout) findViewById(R.id.space_title);
        space_title.setOnClickListener(this);

        spacemsg_title = (RelativeLayout) findViewById(R.id.spacemsg_title);
        spacemsg_title.setOnClickListener(this);

        spacemsg_dot_loader = (ImageView) findViewById(R.id.spacemsg_dot_loader);
        spacemsg_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(spacemsg_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomspace_msg=localSharedPreferences.getSharedPreferences(Constants.DesSpaceMsg);

        space_count_txt=(TextView) findViewById(R.id.space_count_txt);
        space_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        space_edittext=(EditText)findViewById(R.id.space_edittext);
        space_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomspace_msg!=null&&!roomspace_msg.equals("")) {
            space_edittext.setText(roomspace_msg);
        }

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.space_title: {
               // onBackPressed();
                updateSpac();
            }
            break;
            case R.id.spacemsg_title: {
                // onBackPressed();
                updateSpac();
            }
            break;
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            spacemsg_title.setVisibility(View.VISIBLE);
            spacemsg_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            spacemsg_title.setVisibility(View.VISIBLE);
            spacemsg_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,spacemsg_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        spacemsg_title.setVisibility(View.VISIBLE);
        spacemsg_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, spacemsg_title, getResources(), this);
        }
        }

    public void updateSpaceMessage(){
        localSharedPreferences.saveSharedPreferences(Constants.DesSpaceMsg,space_msg);
        spacemsg_title.setVisibility(View.GONE);
        spacemsg_dot_loader.setVisibility(View.VISIBLE);
        apiService.updateDescriptionSpace(userid,roomid,space_msg).enqueue(new RequestCallback(this));
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
            String input=space_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                space_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {space_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                space_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    public void updateSpac(){
        space_msg=space_edittext.getText().toString();
        space_msg= space_msg.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (roomspace_msg == null || roomspace_msg.equals("") || roomspace_msg.equals(space_msg) || space_msg.equals("")) {
                if (!space_msg.equals("")) {
                    updateSpaceMessage();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateSpaceMessage();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,space_edittext,getResources(),this);
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
