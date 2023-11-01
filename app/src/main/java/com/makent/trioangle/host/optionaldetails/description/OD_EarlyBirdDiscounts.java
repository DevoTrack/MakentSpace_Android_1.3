package com.makent.trioangle.host.optionaldetails.description;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

public class OD_EarlyBirdDiscounts extends AppCompatActivity {

    public ArrayList<Makent_model> list;
    Button add;
    TextView no_discount;
    String roomid,types;
    RelativeLayout describe_title,header_lay;
    public  static JSONArray Earlybird_ja;
    RecyclerView.Adapter adapter;
    public LocalSharedPreferences localSharedPreferences;
    public ImageView describe_back;
    public @Inject
    CommonMethods commonMethods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od__early_bird_discounts);
        commonMethods = new CommonMethods();


        list= new ArrayList();
        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        header_lay = (RelativeLayout) findViewById(R.id.header_lay);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);



        localSharedPreferences=new LocalSharedPreferences(this);

        add = (Button) findViewById(R.id.add);
        no_discount = (TextView) findViewById(R.id.no_discount);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

         adapter = new TypeObjectAdapter(this,list,no_discount,header_lay);
        recyclerView.setAdapter(adapter);

        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onBackPressed();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OD_EarlyBirdDiscounts.this,AddEarlyBird.class);
                intent.putExtra("room_id",roomid);
                intent.putExtra("type",types);
                startActivity(intent);

            }
        });


        try{

            JSONArray lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.EarlyBirdDiscount));
            Earlybird_ja = lasmindiscount;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        noDiscountMethod();
    }


    public void noDiscountMethod(){
        try {
            JSONArray js = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.EarlyBirdDiscount));
            if(js.length()==0){
                no_discount.setVisibility(View.VISIBLE);
                header_lay.setVisibility(View.GONE);
            }else{
                no_discount.setVisibility(View.GONE);
                header_lay.setVisibility(View.VISIBLE);
            }

            earlybirdparsing(Earlybird_ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void earlybirdparsing(JSONArray ja) throws JSONException {

        list.clear();
        for(int i =0 ; i<ja.length();i++){
            String id = ja.getJSONObject(i).getString("id");
            String room_id = ja.getJSONObject(i).getString("room_id");
            String type = ja.getJSONObject(i).getString("type");
            String period = ja.getJSONObject(i).getString("period");
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
