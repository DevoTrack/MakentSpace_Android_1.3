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
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

public class OD_LastMin extends AppCompatActivity {
    public @Inject
    CommonMethods commonMethods;

    public ArrayList<Makent_model> list;
    public String listdata;
    public JSONObject jo;
    public static  JSONArray ja;
    public Button add;
    public RecyclerView recyclerView;
    String roomid,types;
    public TextView no_discount;
    Dialog_loading mydialog;
    RelativeLayout describe_title,header_lay;
    RecyclerView.Adapter adapter;
    public LocalSharedPreferences localSharedPreferences;
    public ImageView describe_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od__last_min);
        commonMethods = new CommonMethods();

        add = (Button) findViewById(R.id.add);
        no_discount = (TextView) findViewById(R.id.no_discount);
        localSharedPreferences=new LocalSharedPreferences(this);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        header_lay = (RelativeLayout) findViewById(R.id.header_lay);
        describe_title = (RelativeLayout) findViewById(R.id.describe_title);
        describe_back =(ImageView)findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        System.out.println("LastMinCount "+localSharedPreferences.getSharedPreferences(Constants.LastMinCount));





        list= new ArrayList();



        adapter = new LastminAdapter(this,list,no_discount,header_lay);
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

                System.out.println("room id and type check "+roomid+" "+types);
                Intent intent = new Intent(OD_LastMin.this,AddLastMin.class);
                intent.putExtra("room_id",roomid);
                intent.putExtra("type",types);
                startActivity(intent);

            }
        });

      /*  listdata = localSharedPreferences.getSharedPreferences(Constants.ListData);
        try {
            jo = new JSONObject(listdata);*/

      try{

            JSONArray lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LastMinCount));
            ja = lasmindiscount;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();


        noDiscountMethod();


    }


    public void noDiscountMethod(){
        try {
            JSONArray js = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LastMinCount));
            if(js.length()==0){
                no_discount.setVisibility(View.VISIBLE);
                header_lay.setVisibility(View.GONE);
                no_discount.setText(getResources().getString(R.string.no_lastmin_discounts_found));
            }else{
                no_discount.setVisibility(View.GONE);
                header_lay.setVisibility(View.VISIBLE);
            }

            lastminParsing(ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void lastminParsing(JSONArray ja) throws JSONException {

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
