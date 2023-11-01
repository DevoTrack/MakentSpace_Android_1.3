package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step2_AddSummaryActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
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
/* ***********************************************************************
This is List Your Space AddSummary Contain Summary Details
**************************************************************************  */

public class LYS_Step2_AddSummary extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    RelativeLayout describe_addsummary_title,summary_title;

    LocalSharedPreferences localSharedPreferences;
    EditText addsummary_edittext;
    String roomsummary,summary;
    TextView describe_addsummary_character_count_txt;
    String userid,roomid;
    ImageView summary_dot_loader;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step2_add_summary);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        summary_dot_loader = (ImageView) findViewById(R.id.summary_dot_loader);
        summary_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(summary_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        addsummary_edittext=(EditText)findViewById(R.id.addsummary_edittext);
        describe_addsummary_character_count_txt=(TextView)findViewById(R.id.describe_addsummary_character_count_txt);
        describe_addsummary_character_count_txt.setText("500"+" "+getResources().getString(R.string.characters));

        addsummary_edittext.addTextChangedListener(mTextEditorWatcher);

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomsummary=localSharedPreferences.getSharedPreferences(Constants.ListRoomSummary);

        if(roomsummary!=null&&!roomsummary.equals("")) {
            addsummary_edittext.setText(roomsummary);
        }

        summary_title=(RelativeLayout) findViewById(R.id.summary_title);
        summary_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                updateDescriptionfuntion();// this is used to update room description function
            }
        });

        describe_addsummary_title=(RelativeLayout) findViewById(R.id.describe_addsummary_title);
        describe_addsummary_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                updateDescriptionfuntion();
            }
        });

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
            describe_addsummary_character_count_txt.setText(String.valueOf(500-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            summary_title.setVisibility(View.VISIBLE);
            summary_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            summary_title.setVisibility(View.VISIBLE);
            summary_dot_loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        summary_title.setVisibility(View.VISIBLE);
        summary_dot_loader.setVisibility(View.GONE);
    }

    public void updateDescription(){
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomSummary,summary);
        summary_title.setVisibility(View.GONE);
        summary_dot_loader.setVisibility(View.VISIBLE);
        apiService.updateDescription(userid,roomid,summary).enqueue(new RequestCallback(this));
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(summary_dot_loader, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable) {
            // textViewTop.setText(statusmessage);
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }


    public  void updateDescriptionfuntion()
    {
        summary=addsummary_edittext.getText().toString();
        summary= summary.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (roomsummary == null || roomsummary.equals("") || roomsummary.equals(summary) || summary.equals("")) {
                if (!summary.equals("")) {
                    updateDescription();
                } else {
                    onBackPressed();
                    finish();
                }
            } else {
                updateDescription();
            }
        }else {
            snackBar();
        }
    }

}
