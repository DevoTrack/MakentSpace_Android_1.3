package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step5_Additional_RulesActivity
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
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is List Your Space Step5  Contain Additional Rules
**************************************************************************  */
public class LYS_Step5_Additional_Rules extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    RelativeLayout additional_rules_title,houserules_title;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomhouserules,houserules;
    TextView additional_rules_count_txt;
    EditText additional_rules_edittext;
    ImageView houserules_dot_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step5_additional_rules);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        houserules_dot_loader = (ImageView) findViewById(R.id.houserules_dot_loader);
        houserules_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(houserules_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomhouserules=localSharedPreferences.getSharedPreferences(Constants.ListRoomHouseRules);

        additional_rules_count_txt=(TextView) findViewById(R.id.additional_rules_count_txt);
        additional_rules_count_txt.setText("0"+" "+getResources().getString(R.string.word));

        additional_rules_edittext=(EditText)findViewById(R.id.additional_rules_edittext);
        additional_rules_edittext.addTextChangedListener(mTextEditorWatcher);
        if(roomhouserules!=null&&!roomhouserules.equals("")) {
            additional_rules_edittext.setText(roomhouserules);
        }

        additional_rules_title=(RelativeLayout) findViewById(R.id.additional_rules_title);
        additional_rules_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                houserules=additional_rules_edittext.getText().toString();
                houserules= houserules.replaceAll("^\\s+|\\s+$", "");

                if(roomhouserules==null||roomhouserules.equals("")||roomhouserules.equals(houserules)||houserules.equals(""))
                {
                    if(!houserules.equals(""))
                    {
                        updateHouseRules(); // this is used to update house rules call on api function
                    }else {
                        onBackPressed();
                        finish();
                    }
                }else
                {
                    updateHouseRules();
                }
            }
        });

        houserules_title=(RelativeLayout) findViewById(R.id.houserules_title);
        houserules_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                houserules=additional_rules_edittext.getText().toString();
                houserules= houserules.replaceAll("^\\s+|\\s+$", "");

                if(roomhouserules==null||roomhouserules.equals("")||roomhouserules.equals(houserules)||houserules.equals(""))
                {
                    if(!houserules.equals(""))
                    {
                        updateHouseRules();
                    }else {
                        onBackPressed();
                        finish();
                    }
                }else
                {
                    updateHouseRules();
                }
            }
        });

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            houserules_title.setVisibility(View.VISIBLE);
            houserules_dot_loader.setVisibility(View.GONE);
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomHouseRules,houserules);
            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            houserules_title.setVisibility(View.VISIBLE);
            houserules_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,additional_rules_edittext,additional_rules_edittext,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        houserules_title.setVisibility(View.VISIBLE);
        houserules_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,additional_rules_edittext,additional_rules_edittext,getResources(),this);
    }

    public void updateHouseRules(){
        houserules_title.setVisibility(View.GONE);
        houserules_dot_loader.setVisibility(View.VISIBLE);
        apiService.updateHouseRules(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,houserules).enqueue(new RequestCallback(this));
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
            String input=additional_rules_edittext.getText().toString().trim().replaceAll("\n", "");
            String[] wordCount=input.split("\\s");
            if(wordCount.length>1) {
                additional_rules_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.words));
            }else
            {additional_rules_count_txt.setText(String.valueOf(wordCount.length)+" "+getResources().getString(R.string.word));
            }
            if(slenght==0)
            {
                additional_rules_count_txt.setText(String.valueOf(0)+" "+getResources().getString(R.string.word));
            }

            //additional_rules_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };
}
