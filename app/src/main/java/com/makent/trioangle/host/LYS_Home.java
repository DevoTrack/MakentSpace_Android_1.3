package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_HomeActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.optionaldetails.description.OD_LengthOfStay;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class LYS_Home extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    ImageView lyshome_back;
    Button lyshome_preivew,lyshome_steps_btn,optionaldetails;
    String from,city,state,zip,citytwo;
    RelativeLayout lyshome_addphoto,lyshome_describe,lyshome_setprice,lyshome_setaddress,lyshome_sethouserules,lyshome_setbooksetting;

    LocalSharedPreferences localSharedPreferences;

    String userid,roomid;
    String roomaddressresume;
    ImageView lyshome_addphoto_img,lyshome_steps1,lyshome_steps2,lyshome_steps3,lyshome_steps4,lyshome_steps5,lyshome_steps6;
    CheckBox checkstep1,checkstep2,checkstep3,checkstep4,checkstep5,checkstep6;
    TextView lyshome,lyshome_addphoto_txt,describe_txt,decribe_msg,setprice_txt,setprice_msg,setaddress_txt,setaddress_msg,sethouserules_txt,sethouserules_msg,setbooksetting_txt,setbooksetting_msg;
    Button listyourspace;
    protected boolean isInternetAvailable;
    String listData;

    int stepcount;

    String roomname,room_description,amenities,room_title,room_latitude,room_longitude,room_location,additional_rules_msg,
            room_price,remaining_steps=null,max_guest_count,bedroom_count,beds_count,bathrooms_count,weekly_price,monthly_price,
            home_type,cleaning_fee,additional_guests,for_each_guest_after,security_deposit,weekend_pricing,
            room_currency_symbol,room_currency_code,is_list_enabled,policy_type,booking_type;
    String roomimages[];
    Dialog_loading mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys__home);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods =new CommonMethods();

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        lyshome_addphoto_img=(ImageView) findViewById(R.id.lyshome_addphoto_img);
        Log.e("LYS Home","LYS Home");

        lyshome_steps1=(ImageView) findViewById(R.id.lyshome_steps1);
        lyshome_steps2=(ImageView) findViewById(R.id.lyshome_steps2);
        lyshome_steps3=(ImageView) findViewById(R.id.lyshome_steps3);
        lyshome_steps4=(ImageView) findViewById(R.id.lyshome_steps4);
        lyshome_steps5=(ImageView) findViewById(R.id.lyshome_steps5);
        lyshome_steps6=(ImageView) findViewById(R.id.lyshome_steps6);

        listyourspace=(Button)findViewById(R.id.listyourspace);

        lyshome=(TextView) findViewById(R.id.lyshome);
        lyshome_addphoto_txt=(TextView) findViewById(R.id.lyshome_addphoto_txt);
        describe_txt=(TextView) findViewById(R.id.describe_txt);
        decribe_msg=(TextView) findViewById(R.id.decribe_msg);
        setprice_txt=(TextView) findViewById(R.id.setprice_txt);
        setprice_msg=(TextView) findViewById(R.id.setprice_msg);
        setaddress_txt=(TextView) findViewById(R.id.setaddress_txt);
        setaddress_msg=(TextView) findViewById(R.id.setaddress_msg);
        sethouserules_txt=(TextView) findViewById(R.id.sethouserules_txt);
        sethouserules_msg=(TextView) findViewById(R.id.sethouserules_msg);
        setbooksetting_txt=(TextView) findViewById(R.id.setbooksetting_txt);
        setbooksetting_msg=(TextView) findViewById(R.id.setbooksetting_msg);

        checkstep1=(CheckBox) findViewById(R.id.checkBox1);
        checkstep2=(CheckBox) findViewById(R.id.checkBox2);
        checkstep3=(CheckBox) findViewById(R.id.checkBox3);
        checkstep4=(CheckBox) findViewById(R.id.checkBox4);
        checkstep5=(CheckBox) findViewById(R.id.checkBox5);
        checkstep6=(CheckBox) findViewById(R.id.checkBox6);

        lyshome_addphoto=(RelativeLayout) findViewById(R.id.lyshome_addphoto);
        lyshome_describe=(RelativeLayout)findViewById(R.id.lyshome_describe);
        lyshome_setprice=(RelativeLayout)findViewById(R.id.lyshome_setprice);
        lyshome_setaddress=(RelativeLayout)findViewById(R.id.lyshome_setaddress);
        lyshome_sethouserules=(RelativeLayout)findViewById(R.id.lyshome_sethouserules);
        lyshome_setbooksetting=(RelativeLayout)findViewById(R.id.lyshome_setbooksetting);

        Intent x=getIntent();// this is used to get the value to one activity to another activity
        from= x.getStringExtra("from");

        if(from.equals("newlist"))
        {
            updateListData(); //this is used to update details data in api
        }else if(from.equals("oldlist"))
        {
            listData=localSharedPreferences.getSharedPreferences(Constants.ListData);
            loadListDetails(listData); // this is  used to load the room details data
        }
        lyshome_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Setp1_AddPhoto.class);
                startActivity(addphoto);
            }
        });

        lyshome_describe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step2_Describe_Space.class);
                startActivity(addphoto);            }
        });

        lyshome_setprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step3_SetPrice.class);
                addphoto.putExtra("host","price");
                startActivity(addphoto);            }
        });

        lyshome_setaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step4_SetAddress.class);
                //localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
                localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
                startActivity(addphoto);
                /*if(checkstep5.isChecked()){
                    Intent addphoto=new Intent(getApplicationContext(),LYS_Step4_SetAddress.class);
                    //localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
                    localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
                    startActivity(addphoto);
                }else{

                    Intent lysproperty = new Intent(getApplicationContext(), LYS_Location.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(lysproperty, bndlanimation);

                }*/

//                Intent addphoto=new Intent(getApplicationContext(),LYS_Step4_SetAddress.class);
//                //localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
//                localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("2"));
//                startActivity(addphoto);
            }
        });

        lyshome_sethouserules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step5_Additional_Rules.class);
                startActivity(addphoto);
            }
        });

        lyshome_setbooksetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step6_Booking_Type.class);
                startActivity(addphoto);
            }
        });

        listyourspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    listmodeChange();
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, lyshome, getResources(), LYS_Home.this); //this is used to the snackbar dialog function
                }
            }
        });

        lyshome_back=(ImageView)findViewById(R.id.lyshome_back);
        commonMethods.rotateArrow(lyshome_back,this);
        lyshome_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        lyshome_preivew=(Button)findViewById(R.id.lyshome_preview);
        lyshome_preivew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent roomdetails=new Intent(getApplicationContext(), SpaceDetailActivity.class);
                startActivity(roomdetails);
            }
        });

        lyshome_steps_btn=(Button)findViewById(R.id.lyshome_steps_btn);
        lyshome_steps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent roomdetails=new Intent(getApplicationContext(), SpaceDetailActivity.class);
                startActivity(roomdetails);
            }
        });
        optionaldetails=(Button)findViewById(R.id.optionaldetails);
        optionaldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent roomdetails=new Intent(getApplicationContext(), LYS_OptionalDetails.class);
                roomdetails.putExtra("stepcount",stepcount);

                startActivity(roomdetails);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomTitle,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomSummary,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListLocationLat,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListLocationLong,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomHouseRules,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomBookType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomPolicyType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId,null);
       // localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,null);
       // localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,null);

        localSharedPreferences.saveSharedPreferences(Constants.ListBeds,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListBathRooms,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListGuests,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount,String.valueOf(0));

        localSharedPreferences.saveSharedPreferences(Constants.ListWeeklyPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListMonthlyPrice,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListGuestAfter,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit,null);
        localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice,null);


        localSharedPreferences.saveSharedPreferences(Constants.IsSpaceList,0);

        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);

        localSharedPreferences.saveSharedPreferences(Constants.City,null);
        localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
        localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
        localSharedPreferences.saveSharedPreferences(Constants.State,null);
        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
        localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
        localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, "");
        localSharedPreferences.saveSharedPreferences(Constants.MinimumStay, "");
        localSharedPreferences.saveSharedPreferences(Constants.MaximumStay, "");
        localSharedPreferences.saveSharedPreferences(Constants.AvailableRulesOption, "");
        localSharedPreferences.saveSharedPreferences(Constants.LastMinCount,"");
        localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount,"");
        localSharedPreferences.saveSharedPreferences(Constants.LengthOfStay,"");
        OD_LengthOfStay.lenghtofstayDiscount = null;

        Intent lysproperty=new Intent(getApplicationContext(),HostHome.class);
        lysproperty.putExtra("tabsaved",2);
        lysproperty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(lysproperty,bndlanimation);
        finish();
    }


    public void loadListDetails(String listData)
    {

       JSONArray data = null;
        try {
           /* data = new JSONArray(listData);

        for (int i = 0; i < data.length(); i++) {*/
         
            JSONObject dataJSONObject = new JSONObject(listData);

            if(dataJSONObject.getString("spaceId").equals(roomid)) {

                String room_type,property_type;//bedtypeid;
                roomname = dataJSONObject.getString("spaceName");
                room_type = dataJSONObject.getString("room_type");
                property_type = dataJSONObject.getString("property_type");
                //bedtypeid = dataJSONObject.getString("bed_type");
                JSONArray arrJson = dataJSONObject.getJSONArray("room_thumb_images");

                System.out.println("arrJson "+arrJson);
                System.out.println("arrJson length "+arrJson.length());


                roomimages = new String[arrJson.length()];
                for (int j = 0; j < arrJson.length(); j++) {
                    roomimages[j] = arrJson.getString(j);
                    System.out.println("arrJson roomimages "+roomimages[j]);
                }

                JSONArray roomimageid = dataJSONObject.getJSONArray("room_image_id");



                room_description = dataJSONObject.getString("room_description");
                amenities = dataJSONObject.getString("amenities");
                room_title = dataJSONObject.getString("room_title");
                room_latitude = dataJSONObject.getString("latitude");
                room_longitude = dataJSONObject.getString("longitude");
                //spaceLocation = dataJSONObject.getString("spaceLocation");
                room_location = dataJSONObject.getString("room_location_name");
                additional_rules_msg = dataJSONObject.getString("additional_rules_msg");
                room_price = dataJSONObject.getString("room_price");
                max_guest_count = dataJSONObject.getString("max_guest_count");
                bedroom_count = dataJSONObject.getString("bedroom_count");
                beds_count = dataJSONObject.getString("beds_count");
                bathrooms_count = dataJSONObject.getString("bathrooms_count");
                home_type = dataJSONObject.getString("home_type");
               // weekly_price = dataJSONObject.getString("weekly_price");
               // monthly_price = dataJSONObject.getString("monthly_price");
                cleaning_fee = dataJSONObject.getString("cleaning_fee");
                additional_guests = dataJSONObject.getString("additional_guests_fee");
                for_each_guest_after = dataJSONObject.getString("for_each_guest_after");
                security_deposit = dataJSONObject.getString("security_deposit");
                weekend_pricing = dataJSONObject.getString("weekend_pricing");
                room_currency_symbol= dataJSONObject.getString("room_currency_symbol");
                room_currency_code= dataJSONObject.getString("room_currency_code");
                is_list_enabled= dataJSONObject.getString("is_list_enabled");
                policy_type= dataJSONObject.getString("policy_type");
                booking_type= dataJSONObject.getString("booking_type");
                remaining_steps= dataJSONObject.getString("remaining_steps");
                city= dataJSONObject.getString("city");
                state= dataJSONObject.getString("state");
                zip= dataJSONObject.getString("zip");
                String country,building,streetline;
                country= dataJSONObject.getString("country");
                building= dataJSONObject.getString("street_address");
                streetline= dataJSONObject.getString("street_name");

                localSharedPreferences.saveSharedPreferences(Constants.Streetline,streetline);
                localSharedPreferences.saveSharedPreferences(Constants.Building,building);

                localSharedPreferences.saveSharedPreferences(Constants.City,city);
                localSharedPreferences.saveSharedPreferences(Constants.Countyname,country);
                localSharedPreferences.saveSharedPreferences(Constants.State,state);
                localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);

                localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,amenities);
                localSharedPreferences.saveSharedPreferences(Constants.ListIsEnable,is_list_enabled);
                localSharedPreferences.saveSharedPreferences(Constants.ListLocationLat, room_latitude);
                localSharedPreferences.saveSharedPreferences(Constants.ListLocationLong, room_longitude);
                if(beds_count == null||beds_count.equals(""))
                {
                     beds_count="0";
                }

                if(bedroom_count==null||bedroom_count.equals(""))
                {
                    bedroom_count="0";
                }

                if(bathrooms_count==null||bathrooms_count.equals(""))
                {
                    bathrooms_count="0";
                }

                if(max_guest_count==null||max_guest_count.equals(""))
                {
                    max_guest_count="0";
                }

                localSharedPreferences.saveSharedPreferences(Constants.ListBeds,Integer.parseInt(beds_count));
                localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,Integer.parseInt(bedroom_count));
                localSharedPreferences.saveSharedPreferences(Constants.ListBathRooms,Float.valueOf(bathrooms_count));
                localSharedPreferences.saveSharedPreferences(Constants.ListGuests,Integer.parseInt(max_guest_count));
                localSharedPreferences.saveSharedPreferences(Constants.ListPropertyType,room_type);
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,property_type);
                localSharedPreferences.saveSharedPreferences(Constants.ListBedType,"");
                localSharedPreferences.saveSharedPreferences(Constants.ListPropertyTypeName,home_type);
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName,roomname);

                //localSharedPreferences.saveSharedPreferences(Constants.ListWeeklyPrice,weekly_price);
               // localSharedPreferences.saveSharedPreferences(Constants.ListMonthlyPrice,monthly_price);
                localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee,cleaning_fee);
                localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest,additional_guests);
                localSharedPreferences.saveSharedPreferences(Constants.ListGuestAfter,for_each_guest_after);
                localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit,security_deposit);
                localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice,weekend_pricing);



                String room_currency_symbols= Html.fromHtml(room_currency_symbol).toString();

                localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,room_currency_code);
                localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,room_currency_symbols);
                localSharedPreferences.saveSharedPreferences(Constants.IsSpaceList,1);

                if(!room_title.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTitle,room_title);

                if(!room_description.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomSummary,room_description);

                if(!room_price.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomPrice,room_price);

                if(!room_location.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress,room_location);

                if(!additional_rules_msg.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomHouseRules,additional_rules_msg);

                if(!booking_type.equals(""))
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomBookType,booking_type);

                if(!policy_type.equals(""))
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomPolicyType,policy_type);
                System.out.println("List Room PolicyType "+policy_type);
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount,String.valueOf(roomimages.length));
                if(roomimages.length>0) {
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages, roomimages[0]);
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage,arrJson.toString());
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId,roomimageid.toString());
                }
                updateListData();
              //  break;
           // }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateListData()
    {
        String roomtitle,roomsummary,roomprice,roomaddress,roomhouserules,roombooktype,roomimage,roomimagecount,roomcurrencysymbol,roomcurrencycode;
        stepcount=1;
        roomtitle=localSharedPreferences.getSharedPreferences(Constants.ListRoomTitle);
        roomsummary=localSharedPreferences.getSharedPreferences(Constants.ListRoomSummary);
        roomprice=localSharedPreferences.getSharedPreferences(Constants.ListRoomPrice);
        roomaddress=localSharedPreferences.getSharedPreferences(Constants.ListRoomAddress);
        roomhouserules=localSharedPreferences.getSharedPreferences(Constants.ListRoomHouseRules);
        roombooktype=localSharedPreferences.getSharedPreferences(Constants.ListRoomBookType);
        roomimage=localSharedPreferences.getSharedPreferences(Constants.ListRoomImages);
        roomimagecount=localSharedPreferences.getSharedPreferences(Constants.ListRoomImageCount);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);

if(roomcurrencysymbol==null||roomcurrencysymbol.equals(""))
{
    roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);
    roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
    localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,roomcurrencysymbol);
    localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,roomcurrencycode);

}
        System.out.println("Room ivPhoto "+roomimage);
        System.out.println("roomtitle "+roomtitle+"roomsummary "+roomsummary);
        System.out.println("roomprice "+roomprice);
        System.out.println("roomaddress "+roomaddress);
        System.out.println("roomhouserules "+roomhouserules);
        System.out.println("roombooktype "+roombooktype);
        System.out.println("roomcurrencysymbol "+roomcurrencysymbol);

        if(roomimagecount!=null&&!roomimagecount.equals("")) {
            if (Integer.parseInt(roomimagecount) == 0) {
                lyshome_addphoto_txt.setText(getResources().getString(R.string.add_photo));
            } else if (Integer.parseInt(roomimagecount) == 1) {
                lyshome_addphoto_txt.setText(roomimagecount + " " + getResources().getString(R.string.photo));
            } else if (Integer.parseInt(roomimagecount) > 1) {
                lyshome_addphoto_txt.setText(roomimagecount + " " + getResources().getString(R.string.photos));
            }
        }
        else
        {
            lyshome_addphoto_txt.setText(getResources().getString(R.string.add_photo));
        }

        if(roomimage!=null&&!roomimage.equals("")) {
            Glide.with(getApplicationContext())
                    .load(roomimage)

                    .into(new DrawableImageViewTarget(lyshome_addphoto_img) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });
            checkstep1.setChecked(true);
            stepcount=stepcount+1;
        }else
        {
            checkstep1.setChecked(false);
            lyshome_addphoto_img.setImageResource(0);
            lyshome_addphoto_img.setBackground(getResources().getDrawable(R.drawable.default_photo_bg));
        }

        if(roomtitle!=null&&!roomtitle.equals("")) {
            describe_txt.setText(roomtitle);
            describe_txt.setTextColor(getResources().getColor(R.color.text_shadow));
        }
        if(roomsummary!=null&&!roomsummary.equals("")) {
            decribe_msg.setText(roomsummary);
        }
        if(roomtitle!=null&&roomsummary!=null&&!roomtitle.equals("")&&!roomsummary.equals(""))
        {
            checkstep2.setChecked(true);
            stepcount=stepcount+1;
        }

        if(roomprice!=null&&!roomprice.equals(""))
        {
                setprice_txt.setText(roomcurrencysymbol+""+roomprice+" "+getResources().getString(R.string.pernight));
                setprice_txt.setTextColor(getResources().getColor(R.color.text_shadow));
                //setprice_msg
                checkstep3.setChecked(true);
                stepcount=stepcount+1;
        }

        if(roomaddress!=null&&!roomaddress.equals(""))
        {
            setaddress_txt.setText(roomaddress);
            roomaddressresume =roomaddress;
            setaddress_txt.setTextColor(getResources().getColor(R.color.text_shadow));
            setaddress_msg.setVisibility(View.GONE);
            checkstep4.setChecked(true);
            stepcount=stepcount+1;
        }
        if(roomhouserules!=null&&!roomhouserules.equals(""))
        {
            //sethouserules_txt.setText(roomhouserules);
            sethouserules_txt.setTextColor(getResources().getColor(R.color.text_shadow));
            sethouserules_msg.setVisibility(View.GONE);
            checkstep5.setChecked(true);
            //stepcount=stepcount+1;
        }
        if(roombooktype!=null&&!roombooktype.equals(""))
        {
            setbooksetting_txt.setText(roombooktype);
            setbooksetting_txt.setTextColor(getResources().getColor(R.color.text_shadow));
            setbooksetting_msg.setVisibility(View.GONE);
            checkstep6.setChecked(true);
        }
        if(stepcount==2)
        {
            lyshome_steps1.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps2.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps3.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
            lyshome_steps5.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
            //lyshome_steps6.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
        }else if(stepcount==3)
        {
            lyshome_steps1.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps2.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps3.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
            //lyshome_steps6.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
        }else if(stepcount==4)
        {
            lyshome_steps1.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps2.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps3.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps5.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
           // lyshome_steps6.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
        }else if(stepcount==5)
        {
            lyshome_steps1.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps2.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps3.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.red_text));
            lyshome_steps5.setBackgroundColor(getResources().getColor(R.color.red_text));
           // lyshome_steps6.setBackgroundColor(getResources().getColor(R.color.text_light_gray));
        }else if(stepcount==6)
        {
//            lyshome_steps1.setBackgroundColor(getResources().getColor(R.color.red_text));
//            lyshome_steps2.setBackgroundColor(getResources().getColor(R.color.red_text));
//            lyshome_steps3.setBackgroundColor(getResources().getColor(R.color.red_text));
//            lyshome_steps4.setBackgroundColor(getResources().getColor(R.color.red_text));
//            lyshome_steps5.setBackgroundColor(getResources().getColor(R.color.red_text));
//            lyshome_steps6.setBackgroundColor(getResources().getColor(R.color.red_text));
        }
        int remainsteps=5-(stepcount);
        listyourspace.setVisibility(View.VISIBLE);
        if(remainsteps==0) {
            listyourspace.setEnabled(true);
            listyourspace.setText(getResources().getString(R.string.lys));
        }else
        {listyourspace.setEnabled(false);
            listyourspace.setText(remainsteps+" " + getResources().getString(R.string.setptolist));
        }

        if(remaining_steps!=null&&!remaining_steps.equals("")) {
            if (Integer.parseInt(remaining_steps) == 0 &&remainsteps==0) {

                listyourspace.setVisibility(View.GONE);
                //lyshome.setText(getResources().getString(R.string.lys));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       updateListData();

//        if(from.equals("newlist"))
//        {
//            updateListData(); //this is used to update details data in api
//        }else if(from.equals("oldlist"))
//        {
//            listData=localSharedPreferences.getSharedPreferences(Constants.ListData);
//            loadListDetails(listData); // this is  used to load the room details data
//        }
        // lo

       /* citytwo = localSharedPreferences.getSharedPreferences(Constants.City);
        if (citytwo!=null) {
            setaddress_txt.setText(citytwo);
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress, citytwo);
        }*/



    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        listyourspace.setVisibility(View.GONE);
        onBackPressed();
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, lyshome, getResources(), LYS_Home.this); //this is used to the snackbar dialog function
    }

    public void listmodeChange(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.disableListing(userid,roomid).enqueue(new RequestCallback(this));
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
