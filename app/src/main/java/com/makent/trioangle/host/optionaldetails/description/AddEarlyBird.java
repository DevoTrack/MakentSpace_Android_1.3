package com.makent.trioangle.host.optionaldetails.description;

import android.os.Bundle;
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

public class AddEarlyBird extends AppCompatActivity implements ServiceListener {

    Button add;
    public String id,roomid,type,period,discount;
    public String userid;
    Dialog_loading mydialog;
    RelativeLayout describe_title;
    public TextView day_text,percentage_txt;
    LocalSharedPreferences localSharedPreferences;
    public ImageView describe_back;


    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    private LengthOfStayModel hostAddLosModel;
    private ArrayList<LengthOfStayArrayModel> hostAddLosArrayModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_early_bird);
        commonMethods = new CommonMethods();

        localSharedPreferences= new LocalSharedPreferences(this);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);


        add = (Button) findViewById(R.id.add);
        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        day_text = (TextView) findViewById(R.id.day_text);
        percentage_txt = (TextView) findViewById(R.id.percentage_txt);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                period  = day_text.getText().toString();
                discount = percentage_txt.getText().toString();
                addEarlyBird();

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

    private void addEarlyBird() {

        if(roomid==null){
            roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        }

        if(type==null){
            type = "early_bird";
        }

        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.updatePriceRule(userid,roomid,type,period,discount,id).enqueue(new RequestCallback(this));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {

            onSuccessAddEb(jsonResp); // onSuccess call method
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




    private void onSuccessAddEb(JsonResponse jsonResp) {

        hostAddLosModel = gson.fromJson(jsonResp.getStrResponse(), LengthOfStayModel.class);



        if(hostAddLosModel.getStatusCode()!=null&&hostAddLosModel.getStatusCode().equals("1")){

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }


            try {
                hostAddLosArrayModel = hostAddLosModel.getPriceRules();
                localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount,gson.toJson(hostAddLosArrayModel));
                JSONArray earlybird = null;

                earlybird = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.EarlyBirdDiscount));
                OD_EarlyBirdDiscounts.Earlybird_ja= earlybird;

                onBackPressed();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }




}
