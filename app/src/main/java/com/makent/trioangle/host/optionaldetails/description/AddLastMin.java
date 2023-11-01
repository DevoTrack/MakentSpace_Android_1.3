package com.makent.trioangle.host.optionaldetails.description;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.LengthOfStayArrayModel;
import com.makent.trioangle.model.host.LengthOfStayModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AddLastMin extends AppCompatActivity implements ServiceListener {


    Button add;
    Dialog_loading mydialog;
    public String id,roomid,type,period,discount,userid;
    public TextView day_text,percentage_txt;
    RelativeLayout describe_title;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    LocalSharedPreferences localSharedPreferences;
    private LengthOfStayModel hostAddLmModel;
    private ArrayList<LengthOfStayArrayModel> hostAddLmArrayModel;
    public ImageView describe_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_last_min);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences= new LocalSharedPreferences(this);

        add = (Button) findViewById(R.id.add);
        day_text = (TextView) findViewById(R.id.day_text);
        percentage_txt = (TextView) findViewById(R.id.percentage_txt);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);


        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                period  = day_text.getText().toString();
                discount = percentage_txt.getText().toString();
                addLastMin();


            }
        });

        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            day_text.setGravity(Gravity.END);
            day_text.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();


            }
        });

        id = getIntent().getStringExtra("id");
        roomid = getIntent().getStringExtra("room_id");
        type = getIntent().getStringExtra("type");
        period = getIntent().getStringExtra("period");
        discount = getIntent().getStringExtra("discount");

        System.out.println("Period and discount "+period+" "+discount);

        if(period!=null)
            day_text.setText(period);
        else
            day_text.setText("");

        if(discount!=null)
            percentage_txt.setText(discount);
        else
            percentage_txt.setText("");


        if (id==null)
            id="";

        if(period==null)
            period="";

        if(discount==null)
            discount="";

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        if (jsonResp.isSuccess()) {

            onSuccessAddLm(jsonResp); // onSuccess call method
        }else if(!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            System.out.println("Checkin ing ");
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,percentage_txt,getResources(),this);
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    private void addLastMin() {

        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        type = "last_min";

        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.updatePriceRule(userid,roomid,type,period,discount,id).enqueue(new RequestCallback(this));

    }


    private void onSuccessAddLm(JsonResponse jsonResp) {

        hostAddLmModel = gson.fromJson(jsonResp.getStrResponse(), LengthOfStayModel.class);



        if(hostAddLmModel.getStatusCode()!=null&&hostAddLmModel.getStatusCode().equals("1")){

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }


            try {
                hostAddLmArrayModel = hostAddLmModel.getPriceRules();
                localSharedPreferences.saveSharedPreferences(Constants.LastMinCount,gson.toJson(hostAddLmArrayModel));
                JSONArray lasmindiscount = null;

                lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LastMinCount));
                OD_LastMin.ja= lasmindiscount;

                onBackPressed();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void snackBar(String statusmessage)
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(add, statusmessage, Snackbar.LENGTH_LONG);
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

            textViewTop.setText(statusmessage);


        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }
}
