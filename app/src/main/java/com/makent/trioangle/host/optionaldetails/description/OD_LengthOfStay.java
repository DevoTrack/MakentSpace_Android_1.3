package com.makent.trioangle.host.optionaldetails.description;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

public class OD_LengthOfStay extends AppCompatActivity {
    public @Inject
    CommonMethods commonMethods;

    public RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    public ArrayList<Makent_model> list;
    Dialog_loading mydialog;
    Button add;
    RelativeLayout describe_title,header_lay;
    public TextView no_discount;
    public static  JSONArray lenghtofstayDiscount;
    String roomid,types,lengthofstaystring;
    public LocalSharedPreferences localSharedPreferences;
    public ImageView describe_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od__length_of_stay);
        commonMethods =new CommonMethods();

        add = (Button) findViewById(R.id.add);
        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        header_lay = (RelativeLayout) findViewById(R.id.header_lay);
        no_discount = (TextView) findViewById(R.id.no_discount);
        no_discount.setText(R.string.no_length_discounts_found);
        localSharedPreferences=new LocalSharedPreferences(this);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        list= new ArrayList();

        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("room id and type check "+roomid+" "+types);
                Intent intent = new Intent(OD_LengthOfStay.this,AddLengthOfstay.class);
                intent.putExtra("room_id",roomid);
                intent.putExtra("type",types);
                startActivity(intent);

            }
        });
        adapter = new   LengthOfStayAdapter(this,list,no_discount,header_lay);
        recyclerView.setAdapter(adapter);

        try{

            lengthofstaystring = localSharedPreferences.getSharedPreferences(Constants.LengthOfStay);
            System.out.println("Length of stay option 2 "+lenghtofstayDiscount+" : "+lengthofstaystring);
            JSONArray lasmindiscount = new JSONArray(lengthofstaystring);
            lenghtofstayDiscount = lasmindiscount;
            System.out.println("Length of stay option "+lenghtofstayDiscount+" "+lasmindiscount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();


        noDiscountMethod();
        addButtonHide();

    }

    private void addButtonHide() {
        String lengthofstayoption = localSharedPreferences.getSharedPreferences(Constants.LengthOfStayOptions);

        if(lengthofstayoption!=null&&lenghtofstayDiscount!=null){


        try {
            JSONArray lengthOfStayOpstionArray = new JSONArray(lengthofstayoption);
            System.out.println("Length Of Stay Option "+lengthofstayoption+" check "+OD_LengthOfStay.lenghtofstayDiscount+" "+OD_LengthOfStay.lenghtofstayDiscount.length()+" "+lengthOfStayOpstionArray.length());

            if(OD_LengthOfStay.lenghtofstayDiscount.length()==lengthOfStayOpstionArray.length()){
                System.out.println("Checking incoming ");
                add.setVisibility(View.GONE);
            }else{
                add.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }}
    }


    public void noDiscountMethod(){
        try {
            JSONArray js = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LengthOfStay));

            if(js.length()==0){
                no_discount.setVisibility(View.VISIBLE);
                header_lay.setVisibility(View.GONE);
            }else{
                header_lay.setVisibility(View.VISIBLE);
                no_discount.setVisibility(View.GONE);
            }
            if(lenghtofstayDiscount!=null){
                lenghtofstay(lenghtofstayDiscount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    private void lenghtofstay(JSONArray ja) throws JSONException {

        System.out.println("Ja check "+ja.toString());
        list.clear();
        for(int i =0 ; i<ja.length();i++){
            String id = ja.getJSONObject(i).getString("id");
            String room_id = ja.getJSONObject(i).getString("room_id");
            String type = ja.getJSONObject(i).getString("type");
            String period = ja.getJSONObject(i).getString("period_text");
            String discount = ja.getJSONObject(i).getString("discount");


            roomid = ja.getJSONObject(i).getString("room_id");
            types = ja.getJSONObject(i).getString("type");

            System.out.println("datas check "+id+" "+room_id+" "+type+" "+period+" "+discount);
            Makent_model m = new Makent_model(id,room_id,type,period,discount);
            list.add(m);

        }
        adapter.notifyDataSetChanged();

    }
}
