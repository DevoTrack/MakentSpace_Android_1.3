package com.makent.trioangle.host.optionaldetails.description;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AddLengthOfstay extends AppCompatActivity implements ServiceListener {

    Spinner spinner1;
    EditText percentage_txt;
    String listdata;
    JSONObject joLengthstay;
    Dialog_loading mydialog;
    JSONArray jaLengthstay;
    String[] nights,nights2;
    String userid;
    RelativeLayout describe_title;
    int j=0;
    public ArrayAdapter<String> Adapter;
    public LocalSharedPreferences localSharedPreferences;
    private String roomid,type;
    private String period;
    int k;
    public Button add;
    private String discount,id;
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
        setContentView(R.layout.activity_add_length_ofstay);


        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        percentage_txt = (EditText) findViewById(R.id.percentage_txt);

        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            percentage_txt.setGravity(Gravity.END);
            percentage_txt.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        add = (Button) findViewById(R.id.add);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);


        localSharedPreferences=new LocalSharedPreferences(this);

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        listdata = localSharedPreferences.getSharedPreferences(Constants.ListData);

        id = getIntent().getStringExtra("id");
        roomid = getIntent().getStringExtra("room_id");
        type = getIntent().getStringExtra("type");
        period = getIntent().getStringExtra("period");
        discount = getIntent().getStringExtra("discount");

        System.out.println("Period and discount "+period+" "+discount);

        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        System.out.println(" listdata check 1 "+listdata);
        System.out.println("listdatacheck 2 "+localSharedPreferences.getSharedPreferences(Constants.LengthOfStayOptions));
            try {
                JSONArray ja = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LengthOfStayOptions));
                jaLengthstay = ja;
                nights = new String[jaLengthstay.length() + 1];
                nights2 = new String[jaLengthstay.length() + 1];
                nights[j] = getResources().getString(R.string.selectnights);
                nights2[j] = "";
                for (int i = 0; i < jaLengthstay.length(); i++) {
                    j++;
                    nights[j] = jaLengthstay.getJSONObject(i).getString("text");
                    nights2[j] = jaLengthstay.getJSONObject(i).getString("nights");
                    System.out.println("List Data check " + nights[i]);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        System.out.println("Crash issue "+nights[0]+" "+nights[1]);
        spinnerAdapter();




        if(period!=null){
            System.out.println("Period "+period);
          int spinnerposition =   Adapter.getPosition(period);
          spinner1.setSelection(spinnerposition);
        }
        else{

            System.out.println("Period "+period);
        }

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





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k  = spinner1.getSelectedItemPosition();

                period = nights2[k];

                System.out.println("period check "+period);
                discount = percentage_txt.getText().toString();
                //new getWishListTitle().execute();
                addLengthOfStay();


            }
        });



    }


    private void addLengthOfStay() {

        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        type = "length_of_stay";

        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.updatePriceRule(userid,roomid,type,period,discount,id).enqueue(new RequestCallback(this));

    }

    public void spinnerAdapter(){

        Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,nights) ;
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(Adapter);


    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        if (jsonResp.isSuccess()) {

            onSuccessAddLof(jsonResp); // onSuccess call method
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

    private void onSuccessAddLof(JsonResponse jsonResp) {

        hostAddLosModel = gson.fromJson(jsonResp.getStrResponse(), LengthOfStayModel.class);



        if(hostAddLosModel.getStatusCode()!=null&&hostAddLosModel.getStatusCode().equals("1")){

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }


            try {
            hostAddLosArrayModel = hostAddLosModel.getPriceRules();
            localSharedPreferences.saveSharedPreferences(Constants.LengthOfStay,gson.toJson(hostAddLosArrayModel));
            JSONArray lasmindiscount = null;

                lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LengthOfStay));
                OD_LengthOfStay.lenghtofstayDiscount= lasmindiscount;

                onBackPressed();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



}
