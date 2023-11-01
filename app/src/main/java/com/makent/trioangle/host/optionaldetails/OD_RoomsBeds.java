package com.makent.trioangle.host.optionaldetails;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails
 * @category    OD_RoomsBeds
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.RoomsBeds.BedTypesList;
import com.makent.trioangle.host.RoomsBeds.BedsActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_LISTING_ROOM_BED;
import static com.makent.trioangle.util.Enums.REQ_ROOM_PROPERTY;


/* ************************************************************
Optional details Rooms and Beds details
*************************************************************** */

public class OD_RoomsBeds extends AppCompatActivity implements View.OnClickListener,ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout odroomsbeds_title,odroomsbed_title,hometype_head,listingtype_head,bedtype_head;

    ImageView odroomsbed_guest_minus,odroomsbed_bed_minus,odroomsbed_bedrooms_minus,odroomsbed_bathrooms_minus;
    ImageView odroomsbed_guest_plus,odroomsbed_bed_plus,odroomsbed_bedrooms_plus,odroomsbed_bathrooms_plus;
    TextView odroomsbed_guest_count,odroomsbed_bed_count,odroomsbed_bedrooms_count,odroomsbed_bathrooms_count;

    public TextView listingtype_txt,hometype_txt,bedtype_txt;

    int odguestcount=1,odbedcount=1,odbedroomcount=1;
    double odbathroomcount=1;
    protected boolean isInternetAvailable;
    android.app.AlertDialog alertDialogStores;
    String selectedidroomtype;
    List<ObjectItem> ObjectItemData;
    List<PropertyObjectItem> propertyAdapterItems;
    ArrayList<String>arrayList=new ArrayList<String>();
    ArrayList<HashMap<String, String>> arrayListbedtype=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arrayListroompropertiytype;
    ArrayList<HashMap<String, String>> arrayListroomtype;
    int[] listviewImage;
    LocalSharedPreferences localSharedPreferences;
    ArrayList<BedTypesList>bedTypeList=new ArrayList<>();

    String userid,roomid,propertytype,roomtype,propertytypecount,roomtypecount,bettype,roomtypelist,propertylist;
    String bedtypestr;
    ImageView odroomsbed_dot_loader;
    String roomtypeid,propertiestypeid;
    Spinner myspinner1;
    String selectedtext;
    int propertyclick;
    Dialog_loading mydialog;
    private int odbedroomscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od_rooms_beds);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        odroomsbed_dot_loader = (ImageView) findViewById(R.id.odroomsbed_dot_loader);
        odroomsbed_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(odroomsbed_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);
        listviewImage = new int[]{
                R.drawable.icon_entire_home_selected,R.drawable.icon_private_room_selected,R.drawable.icon_shared_space_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,
                R.drawable.icon_entire_home_selected,R.drawable.icon_private_room_selected,R.drawable.icon_shared_space_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,
                R.drawable.icon_entire_home_selected,R.drawable.icon_private_room_selected,R.drawable.icon_shared_space_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,
                R.drawable.icon_entire_home_selected,R.drawable.icon_private_room_selected,R.drawable.icon_shared_space_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,
                R.drawable.icon_entire_home_selected,R.drawable.icon_private_room_selected,R.drawable.icon_shared_space_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,R.drawable.icon_entire_home_selected,
                R.drawable.icon_entire_home_selected
        };

        localSharedPreferences=new LocalSharedPreferences(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        propertytype=localSharedPreferences.getSharedPreferences(Constants.ListPropertyTypeName);
        roomtype=localSharedPreferences.getSharedPreferences(Constants.ListRoomTypeName);
        propertytypecount=localSharedPreferences.getSharedPreferences(Constants.ListPropertyType);
        roomtypecount=localSharedPreferences.getSharedPreferences(Constants.ListRoomType);
        odbedcount = localSharedPreferences.getSharedPreferencesInt(Constants.ListBeds);

        odbedroomcount=localSharedPreferences.getSharedPreferencesInt(Constants.ListBedRooms);
        odbathroomcount=localSharedPreferences.getSharedPreferencesFloat(Constants.ListBathRooms);
        odbathroomcount=Double.valueOf(odbathroomcount);
        odguestcount=localSharedPreferences.getSharedPreferencesInt(Constants.ListGuests);

        listingtype_txt=(TextView)findViewById(R.id.listingtype_txt);
        hometype_txt=(TextView)findViewById(R.id.hometype_txt);
        bedtype_txt=(TextView)findViewById(R.id.bedtype_txt);
        myspinner1=(Spinner)findViewById(R.id.myspinner1);
        //listingtype_txt.setText(propertytype);
       // hometype_txt.setText(roomtype);
        odroomsbed_title=(RelativeLayout) findViewById(R.id.odroomsbed_title);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            //roomPropertyList(); // this is used to call on reviewsearch api
            //load(0);
        }else {
            commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,listingtype_txt,getResources(),this);
        }
        odroomsbeds_title=(RelativeLayout) findViewById(R.id.odroomsbeds_title);
        odroomsbeds_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                String selectedroom=hometype_txt.getText().toString();
                String selectedproperty=listingtype_txt.getText().toString();
                String selectedbed=bedtype_txt.getText().toString();
               // int bedtypestrpos = (myspinner1.getSelectedItemPosition());
               // bedtypestr=arrayListbedtype.get(bedtypestrpos).get("id");

                for (int i=0;i<arrayListbedtype.size();i++){

                    String str=arrayListbedtype.get(i).get("name");

                    if (selectedbed.equals(str)){
                        bedtypestr=arrayListbedtype.get(i).get("id");
                    }

                }

                for (int i=0;i<arrayListroompropertiytype.size();i++){

                    String str=arrayListroompropertiytype.get(i).get("name");

                    if (selectedproperty.equals(str)){
                        propertiestypeid=arrayListroompropertiytype.get(i).get("id");
                    }

                }

                for (int i=0;i<arrayListroomtype.size();i++){

                    if (selectedroom.equals(arrayListroomtype.get(i).get("name"))){
                        roomtypeid=arrayListroomtype.get(i).get("id");
                    }

                }

                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    updateRoomsBed();
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,listingtype_txt,getResources(),OD_RoomsBeds.this);
                }
            }
        });




        odroomsbed_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                String selectedroom=hometype_txt.getText().toString();
                String selectedproperty=listingtype_txt.getText().toString();
                String selectedbed=bedtype_txt.getText().toString();

                //int bedtypestrpos = (myspinner1.getSelectedItemPosition());
               // bedtypestr=arrayListbedtype.get(bedtypestrpos).get("id");
                for (int i=0;i<arrayListbedtype.size();i++){

                    String str=arrayListbedtype.get(i).get("name");

                    if (selectedbed.equals(str)){
                        bedtypestr=arrayListbedtype.get(i).get("id");
                    }

                }

                for (int i=0;i<arrayListroompropertiytype.size();i++){

                    String str=arrayListroompropertiytype.get(i).get("name");

                    if (selectedproperty.equals(str)){
                        propertiestypeid=arrayListroompropertiytype.get(i).get("id");
                    }

                }

                for (int i=0;i<arrayListroomtype.size();i++){

                    if (selectedroom.equals(arrayListroomtype.get(i).get("name"))){
                        roomtypeid=arrayListroomtype.get(i).get("id");
                    }

                }


                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    updateRoomsBed();
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,listingtype_txt,getResources(),OD_RoomsBeds.this);
                }
            }
        });

        hometype_head=(RelativeLayout) findViewById(R.id.hometype_head);
        hometype_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
               /* Intent lysproperty=new Intent(getApplicationContext(),LYS_Home.class);
                startActivity(lysproperty);*/
             //  sampledialogone();
                propertyclick =1;
                (new hometype()).execute();


            }
        });

        listingtype_head=(RelativeLayout) findViewById(R.id.listingtype_head);
        listingtype_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                /*Intent lysproperty=new Intent(getApplicationContext(),LYS_Home.class);
                startActivity(lysproperty);*/
                propertyclick =2;
                (new hometype()).execute();
                /*ListTypeDialog cdd=new ListTypeDialog(OD_RoomsBeds.this,listingtype_txt);
                cdd.show();*/
            }
        });

        bedtype_head=(RelativeLayout) findViewById(R.id.bedtype_head);
        bedtype_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                //  sampledialogone();
                propertyclick =3;
                (new hometype()).execute();


            }
        });

        odroomsbed_guest_count=(TextView)findViewById(R.id.odroomsbed_guest_count);
        odroomsbed_bed_count=(TextView)findViewById(R.id.odroomsbed_bed_count);
        odroomsbed_bedrooms_count=(TextView)findViewById(R.id.odroomsbed_bedrooms_count);
        odroomsbed_bathrooms_count=(TextView)findViewById(R.id.odroomsbed_bathrooms_count);

        odroomsbed_guest_count.setText(Integer.toString(odguestcount)+" "+getResources().getString(R.string.s_guest));
        odroomsbed_bed_count.setText(Integer.toString(odbedcount)+" "+getResources().getString(R.string.bed));
        odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.bedroom));
        odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.bathroom));

        odroomsbed_guest_plus=(ImageView)findViewById(R.id.odroomsbed_guest_plus);
        odroomsbed_bed_plus=(ImageView)findViewById(R.id.odroomsbed_bed_plus);
        odroomsbed_bedrooms_plus=(ImageView)findViewById(R.id.odroomsbed_bedrooms_plus);
        odroomsbed_bathrooms_plus=(ImageView)findViewById(R.id.odroomsbed_bathrooms_plus);

        odroomsbed_guest_minus=(ImageView)findViewById(R.id.odroomsbed_guest_minus);
        odroomsbed_bed_minus=(ImageView)findViewById(R.id.odroomsbed_bed_minus);
        odroomsbed_bedrooms_minus=(ImageView)findViewById(R.id.odroomsbed_bedrooms_minus);
        odroomsbed_bathrooms_minus=(ImageView)findViewById(R.id.odroomsbed_bathrooms_minus);

        // Change the minus symbol enable and disable
        if(odguestcount>1) {
            odroomsbed_guest_minus.setEnabled(true);
        }else {
            odroomsbed_guest_minus.setEnabled(false);
        }

        if(odbedcount>1) {
            odroomsbed_bed_minus.setEnabled(true);
        }else {
            odroomsbed_bed_minus.setEnabled(false);
        }

        if(odbedroomcount>0) {
            odroomsbed_bedrooms_minus.setEnabled(true);
        }else {
            odroomsbed_bedrooms_minus.setEnabled(false);
        }

        if(odbathroomcount>0.0) {
            odroomsbed_bathrooms_minus.setEnabled(true);
        }else {
            odroomsbed_bathrooms_minus.setEnabled(false);
        }

        // Change the plus symbol enable and disable
        if(odguestcount>=16) {
            odroomsbed_guest_count.setText(Integer.toString(odguestcount) + "+ "+getResources().getString(R.string.guests));
            odroomsbed_guest_plus.setEnabled(false);
        }else {
            odroomsbed_guest_plus.setEnabled(true);
        }

        if(odbedcount>=16) {
            odroomsbed_bed_count.setText(Integer.toString(odbedcount)+"+ "+getResources().getString(R.string.beds));
            odroomsbed_bed_plus.setEnabled(false);
        }else {
            odroomsbed_bed_plus.setEnabled(true);
        }

        if(odbedroomcount>=10) {
            odroomsbed_bedrooms_plus.setEnabled(false);
        }else {
            odroomsbed_bedrooms_plus.setEnabled(true);
        }

        if(odbathroomcount>=8) {
            odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+"+ "+getResources().getString(R.string.s_bathroom));
            odroomsbed_bathrooms_plus.setEnabled(false);
        }else {
            odroomsbed_bathrooms_plus.setEnabled(true);
        }

        odroomsbed_guest_minus.setOnClickListener(this);
        odroomsbed_bed_minus.setOnClickListener(this);
        odroomsbed_bedrooms_minus.setOnClickListener(this);
        odroomsbed_bathrooms_minus.setOnClickListener(this);



        odroomsbed_guest_plus.setOnClickListener(this);
        odroomsbed_bed_plus.setOnClickListener(this);
        odroomsbed_bedrooms_plus.setOnClickListener(this);
        odroomsbed_bathrooms_plus.setOnClickListener(this);

        enablebuttons();

    }

    @Override
    protected void onResume() {
        super.onResume();

        propertytype=localSharedPreferences.getSharedPreferences(Constants.ListPropertyTypeName);
        roomtype=localSharedPreferences.getSharedPreferences(Constants.ListRoomTypeName);
        propertytypecount=localSharedPreferences.getSharedPreferences(Constants.ListPropertyType);
        roomtypecount=localSharedPreferences.getSharedPreferences(Constants.ListRoomType);

        odbedroomscount = localSharedPreferences.getSharedPreferencesInt(Constants.ListBedRooms);
        bedtype_txt.setText(String.valueOf(odbedroomscount));



        listingtype_txt.setText(propertytype);
        hometype_txt.setText(roomtype);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.odroomsbed_guest_minus: {
                odroomsbed_guest_minus();
            }
            break;
            case R.id.odroomsbed_bed_minus: {
                odroomsbed_bed_minus();
            }
            break;
            case R.id.odroomsbed_bedrooms_minus: {
                odroomsbed_bedrooms_minus();
            }
            break;
            case R.id.odroomsbed_bathrooms_minus: {
                odroomsbed_bathrooms_minus();
            }
            break;

            case R.id.odroomsbed_guest_plus: {
                odroomsbed_guest_plus();
            }
            break;
            case R.id.odroomsbed_bed_plus: {
                odroomsbed_bed_plus();
            }
            break;
            case R.id.odroomsbed_bedrooms_plus: {
                odroomsbed_bedrooms_plus();
            }
            break;
            case R.id.odroomsbed_bathrooms_plus: {
                odroomsbed_bathrooms_plus();
            }
            break;
        }
    }

    // Enable or disable button based on the value
    public void enablebuttons()
    {
        if(odroomsbed_guest_minus.isEnabled())
        {
            odroomsbed_guest_minus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_guest_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bed_minus.isEnabled())
        {
            odroomsbed_bed_minus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bed_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bedrooms_minus.isEnabled())
        {
            odroomsbed_bedrooms_minus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bedrooms_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bathrooms_minus.isEnabled())
        {
            odroomsbed_bathrooms_minus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bathrooms_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }


        if(odroomsbed_guest_plus.isEnabled())
        {
            odroomsbed_guest_plus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_guest_plus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bed_plus.isEnabled())
        {
            odroomsbed_bed_plus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bed_plus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bedrooms_plus.isEnabled())
        {
            odroomsbed_bedrooms_plus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bedrooms_plus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if(odroomsbed_bathrooms_plus.isEnabled())
        {
            odroomsbed_bathrooms_plus.setColorFilter(getResources().getColor(R.color.red_text));
        }else
        {
            odroomsbed_bathrooms_plus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }
    }

    public void odroomsbed_guest_minus()
    {
        if(odguestcount>1)
        {
            odroomsbed_guest_minus.setEnabled(true);
            odguestcount--;
            odroomsbed_guest_count.setText(Integer.toString(odguestcount)+" "+getResources().getString(R.string.guests));
        }
        if(odguestcount==1)
        {
            odroomsbed_guest_count.setText(Integer.toString(odguestcount)+" "+getResources().getString(R.string.s_guest));
            odroomsbed_guest_minus.setEnabled(false);
        }else
        {
            odroomsbed_guest_minus.setEnabled(true);
            odroomsbed_guest_plus.setEnabled(true);
        }
        enablebuttons();
    }

    public void odroomsbed_guest_plus() {
        if (odguestcount < 16) {
            odguestcount++;
            odroomsbed_guest_plus.setEnabled(true);
            odroomsbed_guest_count.setText(Integer.toString(odguestcount)+" "+getResources().getString(R.string.guests));
        }
        if (odguestcount == 1) {
            odroomsbed_guest_count.setText(Integer.toString(odguestcount)+" "+getResources().getString(R.string.s_guest));
            odroomsbed_guest_minus.setEnabled(false);
        } else {
            odroomsbed_guest_minus.setEnabled(true);
        }
        if (odguestcount == 16) {
            odroomsbed_guest_plus.setEnabled(false);
            odroomsbed_guest_count.setText(Integer.toString(odguestcount) + "+ "+getResources().getString(R.string.guests));
        }
        enablebuttons();
    }

    public void odroomsbed_bed_minus(){

            if(odbedcount>1)
            {
                odroomsbed_bed_minus.setEnabled(true);
                odbedcount--;
                odroomsbed_bed_count.setText(Integer.toString(odbedcount)+" "+getResources().getString(R.string.beds));
            }
            if(odbedcount==1)
            {
                odroomsbed_bed_minus.setEnabled(false);
                odroomsbed_bed_count.setText(Integer.toString(odbedcount)+" "+getResources().getString(R.string.bed));
            }else
            {
                odroomsbed_bed_minus.setEnabled(true);
                odroomsbed_bed_plus.setEnabled(true);
            }
        enablebuttons();
    }

    public void odroomsbed_bed_plus(){
            if(odbedcount<16)
            {
                odroomsbed_bed_plus.setEnabled(true);
                odbedcount++;
                odroomsbed_bed_count.setText(Integer.toString(odbedcount)+" "+getResources().getString(R.string.beds));
            }
            if(odbedcount==1)
            {
                odroomsbed_bed_count.setText(Integer.toString(odbedcount)+" "+getResources().getString(R.string.bed));
                odroomsbed_bed_minus.setEnabled(false);
            }else
            {
                odroomsbed_bed_minus.setEnabled(true);
            }
            if(odbedcount==16)
            {
                odroomsbed_bed_plus.setEnabled(false);
                odroomsbed_bed_count.setText(Integer.toString(odbedcount)+"+ "+getResources().getString(R.string.beds));
            }
        enablebuttons();
        }

    public void odroomsbed_bedrooms_minus(){
            if(odbedroomcount>0)
            {
                odbedroomcount--;
                odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.s_bedroom));
            }
            if(odbedroomcount==1)
            {
                odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.bedroom));
            }
            if(odbedroomcount==0)
            {
                odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.bedroom));
                odroomsbed_bedrooms_minus.setEnabled(false);
            }else
            {
                odroomsbed_bedrooms_minus.setEnabled(true);
                odroomsbed_bedrooms_plus.setEnabled(true);
            }
        enablebuttons();
        }

    public void odroomsbed_bedrooms_plus(){
            if(odbedroomcount<10)
            {
                odroomsbed_bedrooms_plus.setEnabled(true);
                odbedroomcount++;
                odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.s_bedroom));
            }
            if(odbedroomcount==1)
            {
            odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.bedroom));
            odroomsbed_bedrooms_minus.setEnabled(false);
            }
            if(odbedroomcount==0)
            {
                odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+" "+getResources().getString(R.string.bedroom));
                odroomsbed_bedrooms_minus.setEnabled(false);
            }else
            {
                odroomsbed_bedrooms_minus.setEnabled(true);
            }
            if(odbedroomcount==10)
            {
                odroomsbed_bedrooms_plus.setEnabled(false);
               // odroomsbed_bedrooms_count.setText(Integer.toString(odbedroomcount)+"+");
            }
        enablebuttons();
    }

    public void odroomsbed_bathrooms_minus(){
            if(odbathroomcount>0)
            {
                odbathroomcount=odbathroomcount-0.5;
                odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.s_bathroom));
            }

            if(odbathroomcount==0.0||odbathroomcount==0)
            {
                odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.bathroom));
                odroomsbed_bathrooms_minus.setEnabled(false);
            }else
            {
                odroomsbed_bathrooms_minus.setEnabled(true);
                odroomsbed_bathrooms_plus.setEnabled(true);
            }
        if(odbathroomcount<=1.0)
        {
            odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.bathroom));
        }
        enablebuttons();
    }

    public void odroomsbed_bathrooms_plus(){
            if(odbathroomcount<8)
            {
                odroomsbed_bathrooms_plus.setEnabled(true);
                odbathroomcount=odbathroomcount+0.5;
                odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.s_bathroom));
            }
            if(odbathroomcount==0.0)
            {
                odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+" "+getResources().getString(R.string.bathroom));
                odroomsbed_bathrooms_minus.setEnabled(false);
            }else
            {
                odroomsbed_bathrooms_minus.setEnabled(true);
            }
            if(odbathroomcount==8)
            {
                odroomsbed_bathrooms_plus.setEnabled(false);
                odroomsbed_bathrooms_count.setText(Double.toString(odbathroomcount)+"+ "+getResources().getString(R.string.s_bathroom));
            }
        enablebuttons();

        }



    // Get Home Type values
    class hometype extends AsyncTask<Void, Integer, String> {
        protected void onPreExecute() {
            Log.d("PreExceute", "On pre Exceute......");


        }

        protected String doInBackground(Void... arg0) {



            if (propertyclick==3) {

                ObjectItemData = new ArrayList<ObjectItem>();
                for (int i = 0; i < arrayListbedtype.size(); i++) {

                    // System.out.println("i" + i + " " + hometype_string[i]);image_name
                    ObjectItem item = new ObjectItem(arrayListbedtype.get(i).get("name"),"","");
                    ObjectItemData.add(item);
                }
            }else if (propertyclick==2) {

                propertyAdapterItems = new ArrayList<PropertyObjectItem>();
                for (int i = 0; i < arrayListroompropertiytype.size(); i++) {

                    // System.out.println("i" + i + " " + hometype_string[i]);
                    PropertyObjectItem item = new PropertyObjectItem(arrayListroompropertiytype.get(i).get("name"),"",arrayListroompropertiytype.get(i).get("image_name"));
                    propertyAdapterItems.add(item);
                }
            }else {

                ObjectItemData = new ArrayList<ObjectItem>();
                for (int i = 0; i < arrayListroomtype.size(); i++) {

                    // System.out.println("i" + i + " " + hometype_string[i]);
                    ObjectItem item = new ObjectItem(arrayListroomtype.get(i).get("name"),arrayListroomtype.get(i).get("is_shared"),arrayListroomtype.get(i).get("image_name"));
                    ObjectItemData.add(item);
                }

            }
            return "You are at PostExecute";
        }

        protected void onPostExecute(String result) {

//            OD_RoomsBeds.ArrayAdapterItem adapter = new OD_RoomsBeds.ArrayAdapterItem(OD_RoomsBeds.this, R.layout.nested_search_item_child, ObjectItemData);
//
//            ListView listViewItems = new ListView(OD_RoomsBeds.this);
//            //View header = getLayoutInflater().inflate(R.layout.currency_header, listViewItems, false);
//
//            // listViewItems.addHeaderView(header, null, false);
//            listViewItems.setDivider(null);
//            listViewItems.setDividerHeight(0);
//            listViewItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//            listViewItems.getCheckedItemCount();
//            listViewItems.setAdapter(adapter);
//            listViewItems.setOnItemClickListener(new OD_RoomsBeds.OnItemClickListenerListViewItem());


            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.currency_header, null);
            TextView header=(TextView)view.findViewById(R.id.header);

            if (propertyclick==3) {
                /*header.setText(getApplicationContext().getString(R.string.select_bed_type));

                OD_RoomsBeds.ArrayAdapterItem adapter = new OD_RoomsBeds.ArrayAdapterItem(OD_RoomsBeds.this, R.layout.nested_search_item_child, ObjectItemData);

                ListView listViewItems = new ListView(OD_RoomsBeds.this);
                //View header = getLayoutInflater().inflate(R.layout.currency_header, listViewItems, false);

                // listViewItems.addHeaderView(header, null, false);
                listViewItems.setDivider(null);
                listViewItems.setDividerHeight(0);
                listViewItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listViewItems.getCheckedItemCount();
                listViewItems.setAdapter(adapter);
                listViewItems.setOnItemClickListener(new OD_RoomsBeds.OnItemClickListenerListViewItem());

                alertDialogStores = new android.app.AlertDialog.Builder(OD_RoomsBeds.this)
                        .setCustomTitle(view)
                        .setView(listViewItems)
                        .setCancelable(true)
                        .show();*/

                Intent lysproperty=new Intent(getApplicationContext(), BedsActivity.class);
                lysproperty.putExtra("bedTypeList", bedTypeList);
                startActivity(lysproperty);

            }else if (propertyclick==2) {
                header.setText(getApplicationContext().getString(R.string.select_property_type));
                OD_RoomsBeds.PropertyAdapterItem propertyAdapter = new OD_RoomsBeds.PropertyAdapterItem(OD_RoomsBeds.this, R.layout.nested_search_item_child, propertyAdapterItems);

                ListView listViewItem = new ListView(OD_RoomsBeds.this);
                listViewItem.setDivider(null);
                listViewItem.setDividerHeight(0);
                listViewItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listViewItem.getCheckedItemCount();
                listViewItem.setAdapter(propertyAdapter);
                listViewItem.setOnItemClickListener(new OD_RoomsBeds.OnItemClickListenerListViewItem());

                alertDialogStores = new android.app.AlertDialog.Builder(OD_RoomsBeds.this)
                        .setCustomTitle(view)
                        .setView(listViewItem)
                        .setCancelable(true)
                        .show();



            }else {
                header.setText(getApplicationContext().getString(R.string.select_room_type));

                OD_RoomsBeds.ArrayAdapterItem adapter = new OD_RoomsBeds.ArrayAdapterItem(OD_RoomsBeds.this, R.layout.nested_search_item_child, ObjectItemData);

                ListView listViewItems = new ListView(OD_RoomsBeds.this);
                //View header = getLayoutInflater().inflate(R.layout.currency_header, listViewItems, false);

                // listViewItems.addHeaderView(header, null, false);
                listViewItems.setDivider(null);
                listViewItems.setDividerHeight(0);
                listViewItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listViewItems.getCheckedItemCount();
                listViewItems.setAdapter(adapter);
                listViewItems.setOnItemClickListener(new OD_RoomsBeds.OnItemClickListenerListViewItem());

                alertDialogStores = new android.app.AlertDialog.Builder(OD_RoomsBeds.this)
                        .setCustomTitle(view)
                        .setView(listViewItems)
                        .setCancelable(true)
                        .show();
            }
            header.setTextColor(getResources().getColor(R.color.text_black));
            Typeface blockFonts = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.fonts_Bold));
            header.setTypeface(blockFonts);

//            alertDialogStores = new android.app.AlertDialog.Builder(OD_RoomsBeds.this)
//                    .setCustomTitle(view)
//                    .setView(listViewItems)
//                    .setCancelable(true)
//                    .show();
        }
    }


    public class ObjectItem {

        public String home,isSharedRoom;
                String images;


        // constructor
        public ObjectItem(String home,String isSharedRoom,String images) {
            this.home = home;
            this.isSharedRoom=isSharedRoom;
            this.images = images;
        }

    }


    public class PropertyObjectItem {

        public String home,isSharedRoom;
        String images;


        // constructor
        public PropertyObjectItem(String home,String isSharedRoom, String images) {
            this.home = home;
            this.isSharedRoom=isSharedRoom;
             this.images = images;
        }

    }

    //here's our beautiful adapter
    public class PropertyAdapterItem extends ArrayAdapter<PropertyObjectItem> {
        int selectedIndex = -1;
        Context mContext;


        public PropertyAdapterItem(Context mContext, int layoutResourceId,  List<PropertyObjectItem> ObjectItemData) {

            super(mContext, layoutResourceId, ObjectItemData);


            this.mContext = mContext;

        }
        public void setSelectedIndex(int index){
            selectedIndex = index;
        }
        private class ViewHolder {

            public TextView hometype,tv_isShared;
            public LinearLayout homelinear;
            public ImageView image_child,icons;



        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            PropertyObjectItem rowitem = getItem(position);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {


                convertView = inflater.inflate(R.layout.nested_search_item_child, null);

                holder = new ViewHolder();


                holder.hometype = (TextView) convertView.findViewById(R.id.tv_child);
                holder.tv_isShared = (TextView) convertView.findViewById(R.id.tv_isShared);
                holder.image_child = (ImageView) convertView.findViewById(R.id.image_child);
                holder.homelinear = (LinearLayout) convertView.findViewById(R.id.tv_linear);
                holder.icons =(ImageView)convertView.findViewById(R.id.icons);
            }else
            {
                holder = new ViewHolder();

                holder.image_child = (ImageView) convertView.findViewById(R.id.image_child);
                holder.hometype = (TextView) convertView.findViewById(R.id.tv_child);
                holder.tv_isShared = (TextView) convertView.findViewById(R.id.tv_isShared);
                holder.homelinear = (LinearLayout) convertView.findViewById(R.id.tv_linear);
                holder.icons =(ImageView)convertView.findViewById(R.id.icons);

            }
            holder.image_child.setVisibility(View.INVISIBLE);
            holder.hometype.setText(rowitem.home);
            Glide.with(mContext).load(rowitem.images).into(holder.icons);


//            Glide.with(mContext)
//                    .load(rowitem.ivPhoto)
//                    .into(holder.icons);
            if(rowitem.isSharedRoom.equals("Yes"))
                holder.tv_isShared.setVisibility(View.VISIBLE);
            else
                holder.tv_isShared.setVisibility(View.GONE);

            holder.hometype.setTextColor(getResources().getColor(R.color.text_light_shadow));
            holder.hometype.setBackgroundColor(getResources().getColor(R.color.title_text_color));
            holder.homelinear.setBackgroundColor(getResources().getColor(R.color.title_text_color));

            return convertView;

        }
    }




    //here's our beautiful adapter
    public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {
        int selectedIndex = -1;
        Context mContext;


        public ArrayAdapterItem(Context mContext, int layoutResourceId,  List<ObjectItem> ObjectItemData) {

            super(mContext, layoutResourceId, ObjectItemData);


            this.mContext = mContext;

        }
        public void setSelectedIndex(int index){
            selectedIndex = index;
        }
        private class ViewHolder {

            public TextView hometype,tv_isShared;
            public LinearLayout homelinear;
            public ImageView image_child,icons;



        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            ObjectItem rowitem = getItem(position);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {


                convertView = inflater.inflate(R.layout.nested_search_item_child, null);

                holder = new ViewHolder();


                holder.hometype = (TextView) convertView.findViewById(R.id.tv_child);
                holder.tv_isShared = (TextView) convertView.findViewById(R.id.tv_isShared);
                holder.image_child = (ImageView) convertView.findViewById(R.id.image_child);
                holder.homelinear = (LinearLayout) convertView.findViewById(R.id.tv_linear);
                holder.icons =(ImageView)convertView.findViewById(R.id.icons);
            }else
            {
                holder = new ViewHolder();

                holder.image_child = (ImageView) convertView.findViewById(R.id.image_child);
                holder.hometype = (TextView) convertView.findViewById(R.id.tv_child);
                holder.tv_isShared = (TextView) convertView.findViewById(R.id.tv_isShared);
                holder.homelinear = (LinearLayout) convertView.findViewById(R.id.tv_linear);
                holder.icons =(ImageView)convertView.findViewById(R.id.icons);

            }
            holder.image_child.setVisibility(View.INVISIBLE);
            holder.hometype.setText(rowitem.home);
            //holder.icons.setVisibility(View.GONE);
            Glide.with(mContext).load(rowitem.images).into(holder.icons);

//            Glide.with(mContext)
//                    .load(rowitem.ivPhoto)
//                    .into(holder.icons);
            if(rowitem.isSharedRoom.equals("Yes"))
                holder.tv_isShared.setVisibility(View.VISIBLE);
            else
                holder.tv_isShared.setVisibility(View.GONE);

            holder.hometype.setTextColor(getResources().getColor(R.color.text_light_shadow));
            holder.hometype.setBackgroundColor(getResources().getColor(R.color.title_text_color));
            holder.homelinear.setBackgroundColor(getResources().getColor(R.color.title_text_color));

            return convertView;

            }
    }


    /*
     * Here you can control what to do next when the user selects an item
     */
    public class OnItemClickListenerListViewItem implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Context context = view.getContext();

            TextView TextViewItem = ((TextView) view.findViewById(R.id.tv_child));
            if (propertyclick==3) {


                selectedtext = TextViewItem.getText().toString();
                bedtypestr = arrayListbedtype.get(position).get("id");
                //bedtype_txt.setText(selectedtext);
                localSharedPreferences.saveSharedPreferences(Constants.ListBedTypeName,selectedtext);
                propertyclick=0;

            }else if (propertyclick==2) {


                selectedtext = TextViewItem.getText().toString();
                propertiestypeid = arrayListroompropertiytype.get(position).get("id");
                listingtype_txt.setText(selectedtext);
                localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,selectedtext);
                propertyclick=0;

            }else {
                roomtypeid = (String) arrayListroomtype.get(position).get("id");
                String nameproperty= (String) arrayListroomtype.get(position).get("name");
                hometype_txt.setText(nameproperty);

                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName,nameproperty);

                propertyclick=0;
            }


            System.out.println("Selected Text"+selectedtext);
            System.out.println("Selected Text Position"+position);

            //localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,selectedtext);
            //localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,String.valueOf(position+1));


            propertytype=localSharedPreferences.getSharedPreferences(Constants.ListPropertyTypeName);
            propertytypecount=localSharedPreferences.getSharedPreferences(Constants.ListPropertyType);

            //listingtype_txt.setText(propertytype);
           // hometype_txt.setText(roomtype);

            ((OD_RoomsBeds) context).alertDialogStores.cancel();

        }

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_ROOM_PROPERTY:
                if (jsonResp.isSuccess()) {
                        if (mydialog.isShowing())
                            mydialog.dismiss();
                    onSuccessRes(jsonResp);

                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing())
                        mydialog.dismiss();
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,listingtype_txt,getResources(),this);
                }
                break;
            case REQ_LISTING_ROOM_BED:
                if (jsonResp.isSuccess()) {
                    odroomsbed_title.setVisibility(View.GONE);
                    odroomsbed_dot_loader.setVisibility(View.VISIBLE);


                    localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,odbedroomscount);
                    localSharedPreferences.saveSharedPreferences(Constants.ListGuests,odguestcount);
                    localSharedPreferences.saveSharedPreferences(Constants.ListBathRooms,Float.valueOf(String.valueOf(odbathroomcount)));

                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    odroomsbed_title.setVisibility(View.VISIBLE);
                    odroomsbed_dot_loader.setVisibility(View.GONE);
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,listingtype_txt,getResources(),this);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,listingtype_txt,getResources(),this);
    }

    public void roomPropertyList(){
        if (!mydialog.isShowing())
            mydialog.show();
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        //apiService.roomproperty(localSharedPreferences.getSharedPreferences(Constants.AccessToken),langCode).enqueue(new RequestCallback(REQ_ROOM_PROPERTY,OD_RoomsBeds.this));
    }

    public void onSuccessRes(JsonResponse jsonResp){
        try{
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONArray data = response.getJSONArray("room_type");
            JSONArray data1 = response.getJSONArray("property_type");
            JSONArray bettypes = response.getJSONArray("bed_type");
            localSharedPreferences.saveSharedPreferences(Constants.Bedtype,bettypes.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Roomtype,data.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Propertytype,data1.toString());

            roomtypelist=localSharedPreferences.getSharedPreferences(Constants.Roomtype);
            propertylist=localSharedPreferences.getSharedPreferences(Constants.Propertytype);
            bettype=localSharedPreferences.getSharedPreferences(Constants.Bedtype);

            loadListsData();
        }catch (JSONException j){
        j.printStackTrace();}

    }

    // Update Rooms and beds details API
    public void updateRoomsBed(){
        odroomsbed_title.setVisibility(View.GONE);
        odroomsbed_dot_loader.setVisibility(View.VISIBLE);
        propertytype=localSharedPreferences.getSharedPreferences(Constants.ListPropertyTypeName);
        roomtype=localSharedPreferences.getSharedPreferences(Constants.ListRoomTypeName);
        propertytypecount=localSharedPreferences.getSharedPreferences(Constants.ListPropertyType);
        roomtypecount=localSharedPreferences.getSharedPreferences(Constants.ListRoomType);
        localSharedPreferences.saveSharedPreferences(Constants.ListBedType,bedtypestr);
        apiService.listingRoomsBeds(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,propertiestypeid,roomtypeid,String.valueOf(odguestcount),
                String.valueOf(odbedroomcount),String.valueOf(odbedcount),String.valueOf(odbathroomcount),bedtypestr).enqueue(new RequestCallback(REQ_LISTING_ROOM_BED,OD_RoomsBeds.this));
    }



    // Check network validation
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


    public void loadListsData()
    {

        int pos = 0;
        roomtypelist=localSharedPreferences.getSharedPreferences(Constants.Roomtype);


        try {
            JSONArray jsonArrayroomtype = new JSONArray(roomtypelist);
            HashMap<String, String> map1;
            arrayListroomtype=new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jsonArrayroomtype.length(); i++) {
                map1 = new HashMap<String, String>();
                JSONObject obj = jsonArrayroomtype.getJSONObject(i); //0 for just retrieving first object you can loop it
                String id = obj.getString("id"); //To retrieve vehicleId
                String name = obj.getString("name");
                String description = obj.getString("description");
                String image_name = obj.getString("image_name");
                String isSharedRoom="";
                if(obj.has("is_shared"))
                    isSharedRoom=obj.getString("is_shared");

                map1.put("id", id);
                map1.put("name", name);
                map1.put("description", description);
                map1.put("is_shared",isSharedRoom);
                map1.put("image_name",image_name);
                arrayListroomtype.add(map1);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }


        propertylist=localSharedPreferences.getSharedPreferences(Constants.Propertytype);

        try {
            arrayListroompropertiytype=new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            JSONArray jsonArrayroomtype = new JSONArray(propertylist);
            for (int i=0;i<jsonArrayroomtype.length();i++) {
                map = new HashMap<String, String>();
                JSONObject obj = jsonArrayroomtype.getJSONObject(i); //0 for just retrieving first object you can loop it
                String id = obj.getString("id"); //To retrieve vehicleId
                String name = obj.getString("name");
                String description = obj.getString("description");
                String imageName = obj.getString("image_name");
                map.put("id",id);
                map.put("name",name);
                map.put("description",description);
                map.put("image_name",imageName);
                arrayListroompropertiytype.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        bettype=localSharedPreferences.getSharedPreferences(Constants.Bedtype);

        try {

            BedTypesList bedTypesList;
            JSONArray jsonArray = new JSONArray(bettype);
            for (int i=0;i<jsonArray.length();i++) {
                bedTypesList = new BedTypesList();
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject obj = jsonArray.getJSONObject(i); //0 for just retrieving first object you can loop it
                String id = obj.getString("id"); //To retrieve vehicleId
                String name = obj.getString("name");
                map.put("id",id);
                map.put("name",name);

                bedTypesList.setCount(0);
                bedTypesList.setId(Integer.valueOf(id));
                bedTypesList.setName(name);
                bedTypeList.add(bedTypesList);

                if(id.equals(localSharedPreferences.getSharedPreferences(Constants.ListBedType))) {
                    
                    pos=i;
                    //bedtype_txt.setText(obj.getString("name"));
                }
                arrayListbedtype.add(map);
                arrayList.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner1.setAdapter(dataAdapter);
        myspinner1.setSelection(pos);
       // myspinner1.setSelection(Integer.parseInt(localSharedPreferences.getSharedPreferences(Constants.ListBedType)));
        myspinner1.getBackground().setColorFilter(getResources().getColor(R.color.red_text), PorterDuff.Mode.SRC_ATOP);
    }

    }
