package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Property_Type
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.DynamicHeight;
import com.makent.trioangle.adapter.host.MorePropertyAdapter;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.host.Property_Type_model;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/* ***********************************************************************
This is List Your Space Property Type  Page Contain Apartment, House, Bed And BreakFast
**************************************************************************  */

public class LYS_Property_Type extends AppCompatActivity implements DynamicHeight{
    public @Inject
    CommonMethods commonMethods;

    ImageView property_type_back;
    //TableRow property_type_apartment_tb,property_type_house_tb,property_type_bedbreak_tb,property_type_more_tb,property_type_more_show_tb,property_type_more_list_tb;
    public static  String str;
    RelativeLayout property_type_title,property_type_apartment,property_type_house,property_type_bedbreak,property_type_more,property_type_more_show;

    LocalSharedPreferences localSharedPreferences;
   public static RecyclerView recyclerView;
    List<Property_Type_model> makent_host_modelList;
    MorePropertyAdapter adapter;
    String TAG = "MainActivity - ";
    Context context;
    String roomImgItems[];
    private LinearLayoutManager mLayoutManager;
    TextView property_type_apartment_txt,property_type_hose_txt,property_type_bedbreak_txt;
    JSONArray data1;

    ImageView propertyType1,propertyType2,propertyType3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_property_type);
        commonMethods = new CommonMethods();


        localSharedPreferences=new LocalSharedPreferences(this);
        roomImgItems = getResources().getStringArray(R.array.property_type_more);

        property_type_apartment_txt=(TextView)findViewById(R.id.property_type_apartment_txt);
        property_type_hose_txt=(TextView)findViewById(R.id.property_type_hose_txt);
        property_type_bedbreak_txt=(TextView)findViewById(R.id.property_type_bedbreak_txt);

        propertyType1= findViewById(R.id.property_type_apartment_image);
        propertyType2=findViewById(R.id.property_type_hose_image);
        propertyType3=findViewById(R.id.property_type_bedbreak_image);

        recyclerView = (RecyclerView) findViewById(R.id.property_type_more_list);
        makent_host_modelList = new ArrayList<>();

        property_type_back=(ImageView)findViewById(R.id.property_type_back);
        commonMethods.rotateArrow(property_type_back,this);
        property_type_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        property_type_title=(RelativeLayout) findViewById(R.id.property_type_title);
        property_type_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        property_type_apartment=(RelativeLayout) findViewById(R.id.property_type_apartment);
        property_type_house=(RelativeLayout) findViewById(R.id.property_type_house);
        property_type_bedbreak=(RelativeLayout) findViewById(R.id.property_type_bedbreak);
        property_type_more=(RelativeLayout) findViewById(R.id.property_type_more);
        property_type_more_show=(RelativeLayout) findViewById(R.id.property_type_more_show);

       /* property_type_apartment_tb=(TableRow) findViewById(R.id.property_type_apartment_tb);
        property_type_house_tb=(TableRow) findViewById(R.id.property_type_house_tb);
        property_type_bedbreak_tb=(TableRow) findViewById(R.id.property_type_bedbreak_tb);
        property_type_more_tb=(TableRow) findViewById(R.id.property_type_more_tb);
        property_type_more_show_tb=(TableRow) findViewById(R.id.property_type_more_show_tb);
        //property_type_more_list_tb=(TableRow) findViewById(R.id.property_type_more_list_tb);*/


       /* property_type_more_show_tb.setVisibility(View.GONE);
        //property_type_more_list_tb.setVisibility(View.GONE);
        property_type_more_tb.setVisibility(View.VISIBLE);*/

        property_type_more_show.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        property_type_more.setVisibility(View.VISIBLE);

        property_type_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                /*property_type_more_show_tb.setVisibility(View.VISIBLE);
               // property_type_more_list_tb.setVisibility(View.VISIBLE);
                property_type_more_tb.setVisibility(View.GONE);*/
                property_type_more_show.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                property_type_more.setVisibility(View.GONE);
                recyclerView.setFocusableInTouchMode(true);
                recyclerView.setFocusable(true);
                recyclerView.smoothScrollToPosition(5);
            }
        });

        property_type_apartment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                try {
                    JSONObject dataJSONObject = data1.getJSONObject(0);
                    localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,dataJSONObject.getString("id"));
                    localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,dataJSONObject.getString("name"));

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent lysproperty = new Intent(getApplicationContext(), LYS_Location.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(lysproperty, bndlanimation);
            }
        });

        property_type_house.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                try {
                        JSONObject dataJSONObject = data1.getJSONObject(1);
                            localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,dataJSONObject.getString("id"));
                            localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,dataJSONObject.getString("name"));

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent lysproperty=new Intent(getApplicationContext(),LYS_Location.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(lysproperty,bndlanimation);
            }
        });
        property_type_bedbreak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                try {
                    JSONObject dataJSONObject = data1.getJSONObject(2);
                    localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,dataJSONObject.getString("id"));
                    localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,dataJSONObject.getString("name"));

                }catch (JSONException e) {
                    e.printStackTrace();
                }  Intent lysproperty=new Intent(getApplicationContext(),LYS_Location.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(lysproperty,bndlanimation);
            }
        });




        adapter = new MorePropertyAdapter(this, makent_host_modelList,recyclerView);



        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);
        load(localSharedPreferences.getSharedPreferences(Constants.PropertyListJSON));
    }

    @Override
    public void HeightChange(int position, int height) {
        HashMap itemHeight = new HashMap();
        itemHeight.put(position, height);
        int sumHeight = SumHashItem (itemHeight);

       // float density = getResources().getDisplayMetrics().density;
       // float viewHeight = sumHeight * density;
        System.out.print("Text View SumHeight "+sumHeight);
        //recyclerView.getLayoutParams().height = sumHeight;
       // int i = this.recyclerView.getLayoutParams().height;
    }

    int SumHashItem (HashMap<Integer, Integer> hashMap) {
        int sum = 0;

        for(Map.Entry<Integer, Integer> myItem: hashMap.entrySet())  {
            sum += myItem.getValue();
        }

        return sum;
    }

    private void load(String response) {

        try {

            if(response!=null){
                JSONObject responsejsonobj = new JSONObject(response);

               String  statuscode = responsejsonobj.getString("status_code");
                String statusmessage = responsejsonobj.getString("success_message");
                localSharedPreferences.saveSharedPreferences(Constants.PropertyListJSON,response.toString());
                // remove_status= remove_jsonobj.getString("status");
                Log.d("OUTPUT IS",statuscode);
                Log.d("OUTPUT IS",statusmessage);

                //remove loading view
                System.out.println("Remove Search list size"+makent_host_modelList.size());
                            /*if(index==1)
                            {
                                searchlist.remove(0);
                                searchlist.remove(1);
                            }else {
                                if (searchlist.size() > 0)
                                    searchlist.remove(searchlist.size() - 1);
                            }*/

                if (makent_host_modelList.size() > 0)
                    makent_host_modelList.remove(makent_host_modelList.size() - 1);

                if(statuscode.matches("1")){

                    data1 = responsejsonobj.getJSONArray("property_type");

                    adapter.notifyItemInserted(0);



                    for (int i = 0; i < data1.length(); i++) {
                        JSONObject dataJSONObject = data1.getJSONObject(i);
                        if(i==0)
                        {
                            Glide.with(this).load(dataJSONObject.getString("image_name")).into(propertyType1);
                            property_type_apartment_txt.setText(dataJSONObject.getString("name"));
                        }else if(i==1)
                        {
                            Glide.with(this).load(dataJSONObject.getString("image_name")).into(propertyType2);
                            property_type_hose_txt.setText(dataJSONObject.getString("name"));
                        }else if(i==2)
                        {
                            Glide.with(this).load(dataJSONObject.getString("image_name")).into(propertyType3);
                            property_type_bedbreak_txt.setText(dataJSONObject.getString("name"));
                        }

                        if(i>3) {
                            property_type_more.setVisibility(View.VISIBLE);
                            Property_Type_model listdata = new Property_Type_model();
                            listdata.type = "item";
                            listdata.setName(dataJSONObject.getString("name"));
                            listdata.setDesc(dataJSONObject.getString("description"));
                            listdata.setId(dataJSONObject.getString("id"));
                            listdata.setImage(dataJSONObject.getString("image_name"));
                            listdata.setType("property_type");
                            makent_host_modelList.add(listdata);
                        }else{
                            property_type_more.setVisibility(View.INVISIBLE);
                        }

                    }
                    adapter.notifyDataChanged();


                }

                else  {

                    adapter.setMoreDataAvailable(false);
                    Snackbar snackbar = Snackbar
                            .make(property_type_back, "No more data available...", Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                    snackbar.show();
                    //Toast.makeText(getApplicationContext(), "Missing Fields ", Toast.LENGTH_LONG).show();
                    // System.out.println("Amount data not cleared");
                }

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            snackBar();
        }
    }

    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(property_type_back, "", Snackbar.LENGTH_LONG);
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

        textViewTop.setText(getResources().getString(R.string.Interneterror));
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
