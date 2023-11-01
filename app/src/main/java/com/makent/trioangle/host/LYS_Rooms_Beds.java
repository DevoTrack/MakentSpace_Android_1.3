package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Rooms_BedsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.host.RoomsBeds.AdditionalBedModel;
import com.makent.trioangle.host.RoomsBeds.BedTypeAdapter;
import com.makent.trioangle.host.RoomsBeds.BedTypesAPIList;
import com.makent.trioangle.host.RoomsBeds.BedTypesList;
import com.makent.trioangle.host.RoomsBeds.SleepingArangementAdapter;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* ***********************************************************************
This is List Your Space Rooms Beds  Page Contain Max Guest,Beds,Bedrooms
**************************************************************************  */


public class LYS_Rooms_Beds extends AppCompatActivity implements ServiceListener ,SleepingArangementAdapter.OnItemClickListener, BedTypeAdapter.OnBedClickListener{

    public @Inject
    ApiService apiService;
    public @Inject
    Gson gson;
    public @Inject
    CommonMethods commonMethods;

    ImageView roomsbed_back;
    RelativeLayout roomsbed_next, roomsbed_title, rltDone;
    ArrayList<String>arrayList=new ArrayList<String>();

    ArrayList<BedTypesList>bedTypeList=new ArrayList<>();

    ArrayList<HashMap<String,String>>bedtype=new ArrayList<HashMap<String, String>>();
    ImageView roomsbed_guest_minus,roomsbed_bathrooms_minus;
    ImageView roomsbed_guest_plus,roomsbed_bathrooms_plus;
    TextView roomsbed_guest_count,roomsbed_bathrooms_count;
    int guestcount=1;
    double bathroomcount=1;
    boolean checkBedIsEmpty=true;
    LocalSharedPreferences localSharedPreferences;
    String userid,propertytype,roomtype,loclat,loclong,bettype;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    String commonBedDetails = "", bedDetails = "", bedRoomCount = "";
    BottomSheetBehavior behavior;
    // Rooms and bed dynamic
    List<AdditionalBedModel> additionalBedModels = new ArrayList<>();
    List<BedTypesList> bedTypesLists = new ArrayList<>();
    List<BedTypesList> oldBedTypesLists = new ArrayList<>();
    boolean done=false;

    @BindView(R.id.rv_bed_type)
    RecyclerView rvBedType;


    @BindView(R.id.rvBedType)
    RecyclerView rvBedTypeChild;

    Typeface font1;
    Drawable minusenable, minusdisable, plusenable, plusdisable;

    @BindView(R.id.iv_bedminus)
    ImageView ivBedminus;

    @BindView(R.id.tv_bed_count)
    TextView tvBedCount;
    List<BedTypesList> bedTypeListTemp = new ArrayList<>();
    @BindView(R.id.iv_bedplus)
    ImageView ivBedplus;
    int maxBeds = 0;
    private SleepingArangementAdapter adapter;
    private BedTypeAdapter bedTypeAdapter;
    //Need to b done
    private int guestOldPosition = -1;
    private int guestPosition;
    private View rootView;

    @OnClick(R.id.iv_bedminus)
    public void onBedMinus() {

        tvBedCount.setText(String.valueOf(--maxBeds));

        enablebutton();


        if (additionalBedModels.size() > 1) {
            additionalBedModels.remove(additionalBedModels.size() - 2);
        }

        adapter.notifyDataSetChanged();
    }

    private void enablebutton() {

        if (maxBeds <= 0) {
            ivBedplus.setEnabled(true);
            ivBedminus.setEnabled(false);
        } else if (maxBeds >= 10) {
            ivBedplus.setEnabled(false);
            ivBedminus.setEnabled(true);
        } else {
            ivBedplus.setEnabled(true);
            ivBedminus.setEnabled(true);
        }


        enablebuttons();
        guestOldPosition=-1;
        //plusMinus(ivBedminus, ivBedplus);

    }

    @OnClick(R.id.iv_bedplus)
    public void onBedPlus() {

        tvBedCount.setText(String.valueOf(++maxBeds));

        enablebutton();


        AdditionalBedModel additionalBedModel = new AdditionalBedModel();
        additionalBedModel.setBedName("");
        additionalBedModel.setBedTypesLists(getBedList(bedTypeList));
        additionalBedModel.setBedTypesListsTemp(getBedList(bedTypeListTemp));
        additionalBedModels.add(additionalBedModel);

        Collections.swap(additionalBedModels, additionalBedModels.size()-1, additionalBedModels.size()-2);
        adapter.notifyDataSetChanged();
    }

    private List<BedTypesList> getBedList(List<BedTypesList> bedTypeList) {
        List<BedTypesList> bedTypeLists = new ArrayList<>();
        BedTypesList bedTypesList;
        for (int i = 0; i < bedTypeList.size(); i++) {
            bedTypesList = new BedTypesList();
            bedTypesList.setId(bedTypeList.get(i).getId());
            bedTypesList.setName(bedTypeList.get(i).getName());
            bedTypesList.setCount(bedTypeList.get(i).getCount());
            bedTypeLists.add(bedTypesList);
        }

        return bedTypeLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_rooms_beds);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods =new CommonMethods();

        localSharedPreferences=new LocalSharedPreferences(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        propertytype=localSharedPreferences.getSharedPreferences(Constants.ListPropertyType);
        roomtype=localSharedPreferences.getSharedPreferences(Constants.ListRoomType);
        loclat=localSharedPreferences.getSharedPreferences(Constants.ListLocationLat);
        loclong=localSharedPreferences.getSharedPreferences(Constants.ListLocationLong);
        bettype=localSharedPreferences.getSharedPreferences(Constants.Bedtype);
        roomsbed_back=(ImageView)findViewById(R.id.roomsbed_back);

        rootView  = findViewById(android.R.id.content);
        commonMethods.rotateArrow(roomsbed_back,this);
        roomsbed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        roomsbed_title=(RelativeLayout) findViewById(R.id.roomsbed_title);
//        roomsbed_title.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View vw) {
//                onBackPressed();
//            }
//        });

        rltDone = (RelativeLayout) findViewById(R.id.rltDone);
        rltDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                done=true;
                updateBedRooms(0);
                onBackPressed();
                rootView.setBackgroundColor(getResources().getColor(R.color.title_text_color));
            }
        });

        roomsbed_next=(RelativeLayout) findViewById(R.id.roomsbed_next);

        try {
            BedTypesList bedTypesList;
            JSONArray jsonArray = new JSONArray(bettype);
            for (int i=0;i<jsonArray.length();i++) {
                bedTypesList = new BedTypesList();
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject obj = jsonArray.getJSONObject(i); //0 for just retrieving first object you can loop it
                String id = obj.getString("id"); //To retrieve vehicleId
                String name = obj.getString("name");
                arrayList.add(name);
                bedTypesList.setCount(0);
                bedTypesList.setId(Integer.valueOf(id));
                bedTypesList.setName(name);
                bedTypeList.add(bedTypesList);
                map.put("id",id);
                map.put("name",name);
                //Similarly do it for others as well
                bedtype.add(map);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        roomsbed_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {


                /*Intent intent =new Intent(getApplicationContext(), BedsActivity.class);
                intent.putExtra("bedTypeList", bedTypeList);
                startActivity(intent);*/

                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    getDynamicBedsList();
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,roomsbed_next,getResources(),LYS_Rooms_Beds.this);
                }
            }
        });


        roomsbed_guest_count=(TextView)findViewById(R.id.roomsbed_guest_count);
        roomsbed_bathrooms_count=(TextView)findViewById(R.id.roomsbed_bathrooms_count);

        roomsbed_guest_count.setText(Integer.toString(guestcount));
        roomsbed_bathrooms_count.setText(Double.toString(bathroomcount));


        roomsbed_guest_minus=(ImageView)findViewById(R.id.roomsbed_guest_minus);
        roomsbed_bathrooms_minus=(ImageView)findViewById(R.id.roomsbed_bathrooms_minus);

        roomsbed_guest_minus.setEnabled(false);
        roomsbed_bathrooms_minus.setEnabled(true);
        ivBedminus.setEnabled(false);

        roomsbed_guest_plus=(ImageView)findViewById(R.id.roomsbed_guest_plus);
        roomsbed_bathrooms_plus=(ImageView)findViewById(R.id.roomsbed_bathrooms_plus);

        enablebuttons(); // this is used to change the button colors and enable and disable color function

        roomsbed_guest_minus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(guestcount>1)
                {
                    guestcount--;
                    roomsbed_guest_count.setText(Integer.toString(guestcount));
                }
                if(guestcount==1)
                {
                    roomsbed_guest_minus.setEnabled(false);
                    enablebuttons();
                }else
                {
                    roomsbed_guest_minus.setEnabled(true);
                    enablebuttons();
                }

            }
        });

        roomsbed_guest_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(guestcount<16)
                {
                    guestcount++;
                    roomsbed_guest_count.setText(Integer.toString(guestcount));
                }
                if(guestcount==1)
                {
                    roomsbed_guest_minus.setEnabled(false);
                    enablebuttons();
                }else
                {
                    roomsbed_guest_minus.setEnabled(true);
                    enablebuttons();
                }
                if(guestcount==16)
                {
                    roomsbed_guest_count.setText(Integer.toString(guestcount)+"+");
                }

            }
        });

        roomsbed_bathrooms_minus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(bathroomcount>0)
                {
                    bathroomcount=bathroomcount-0.5;
                    roomsbed_bathrooms_count.setText(Double.toString(bathroomcount));
                }
                if(bathroomcount==0.0)
                {
                    roomsbed_bathrooms_minus.setEnabled(false);
                    enablebuttons();
                }else
                {
                    roomsbed_bathrooms_minus.setEnabled(true);
                    enablebuttons();
                }

            }
        });

        roomsbed_bathrooms_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(bathroomcount<8)
                {
                    bathroomcount=bathroomcount+0.5;
                    roomsbed_bathrooms_count.setText(Double.toString(bathroomcount));
                }
                if(bathroomcount==0.0)
                {
                    roomsbed_bathrooms_minus.setEnabled(false);
                    enablebuttons();
                }else
                {
                    roomsbed_bathrooms_minus.setEnabled(true);
                    enablebuttons();
                }
                if(bathroomcount==10)
                {
                    roomsbed_bathrooms_count.setText(Double.toString(bathroomcount)+"+");
                }

            }
        });

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //initBedTypeListTemp();
        initBedTypeList();
        init();
       // enablebutton();

    }

    public void enablebuttons() {


        if (roomsbed_guest_minus.isEnabled()) {
            roomsbed_guest_minus.setColorFilter(getResources().getColor(R.color.red_text));
        } else {
            roomsbed_guest_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if (roomsbed_bathrooms_minus.isEnabled()) {
            roomsbed_bathrooms_minus.setColorFilter(getResources().getColor(R.color.red_text));
        } else {
            roomsbed_bathrooms_minus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if (ivBedminus.isEnabled()) {
            ivBedminus.setColorFilter(getResources().getColor(R.color.red_text));
        } else {
            ivBedminus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }

        if (ivBedplus.isEnabled()) {
            ivBedplus.setColorFilter(getResources().getColor(R.color.red_text));
        } else {
            ivBedplus.setColorFilter(getResources().getColor(R.color.text_light_gray));
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (jsonResp.isSuccess()) {
            onSuccessRes(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {

            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,roomsbed_next,getResources(),this);
        }



    }

    public void onSuccessRes(JsonResponse jsonResp){
        try{
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONArray arrJson1 = response.getJSONArray("availability_rules_options");
            JSONArray arrJson2 = response.getJSONArray("length_of_stay_options");
            localSharedPreferences.saveSharedPreferences(Constants.AvailableRulesOption, arrJson1.toString());
            localSharedPreferences.saveSharedPreferences(Constants.LengthOfStayOptions,arrJson2.toString());
            localSharedPreferences.saveSharedPreferences(Constants.ListBathRooms,Float.valueOf(String.valueOf(bathroomcount)));
            localSharedPreferences.saveSharedPreferences(Constants.ListGuests,guestcount);
            localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,additionalBedModels.size()-1);

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            String roomid = response.getString("spaceId");
            String roomlocation = response.getString("location");
            localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, roomid);
            localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress, roomlocation);

            localSharedPreferences.saveSharedPreferences(Constants.ListWeeklyPrice,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListMonthlyPrice,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListGuestAfter,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit,null);
            localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice,null);

            showDialog();
        }catch (JSONException j){
            j.printStackTrace();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,roomsbed_next,getResources(),this);
    }

    public void addRoomDetails(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        HashMap<String, String> addRoom = new HashMap<>();
        addRoom.put("token", userid);
        addRoom.put("room_type", roomtype);
        addRoom.put("property_type", propertytype);
        addRoom.put("latitude", loclat);
        addRoom.put("longitude", loclong);
        addRoom.put("max_guest", String.valueOf(guestcount));
        addRoom.put("bathrooms",String.valueOf(bathroomcount) );
        addRoom.put("bedrooms_count", bedRoomCount);
        addRoom.put("common_room_bed_details", commonBedDetails);
        addRoom.put("bedroom_bed_details",bedDetails);

        apiService.newAddRoom(addRoom).enqueue(new RequestCallback(this));
    }

    public void showDialog()
    {
        final Dialog dialog = new Dialog(LYS_Rooms_Beds.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addlist);

        // set the custom dialog components - text, ivPhoto and button



        dialog.show();

        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                dialog.cancel();
                dialog.dismiss();

                Intent lysproperty=new Intent(getApplicationContext(),LYS_Home.class);
                localSharedPreferences.saveSharedPreferences(Constants.IsSpaceList,1);
                lysproperty.putExtra("from","newlist");
                lysproperty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(lysproperty,bndlanimation);
                finish();

            }
        }, 1500);
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }




    // Rooms bed dynamic code started

    private void initBedTypeList() {
        bedTypeList.removeAll(bedTypeListTemp);

    }

    private void initBedTypeListTemp() {
        bedTypeListTemp.addAll(bedTypeList);
        if (bedTypeListTemp.size() > 4) {
            for (int i = 0; i < 4; i++) {
                bedTypeListTemp.remove(i);
            }
        }
    }

    private void init() {

        initAdditionalModels();


        font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent4));
        minusenable = new FontIconDrawable(this, getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30).colorRes(R.color.text_light_blue);
        plusenable = new FontIconDrawable(this, getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30).colorRes(R.color.text_light_blue);
        minusdisable = new FontIconDrawable(this, getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30)
                .colorRes(R.color.light_blue_disable);
        plusdisable = new FontIconDrawable(this, getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30)
                .colorRes(R.color.light_blue_disable);


        adapter = new SleepingArangementAdapter(additionalBedModels, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setRecycleChildrenOnDetach(true);
        rvBedType.setLayoutManager(layoutManager);
        rvBedType.setNestedScrollingEnabled(false);
        rvBedType.setAdapter(adapter);

        bedTypeAdapter = new BedTypeAdapter(bedTypesLists, this, this);
        LinearLayoutManager layoutManagerBed = new LinearLayoutManager(this);
        layoutManagerBed.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManagerBed.setSmoothScrollbarEnabled(true);
        rvBedTypeChild.setLayoutManager(layoutManagerBed);
        rvBedTypeChild.setHasFixedSize(true);
        //rvBedTypeChild.setNestedScrollingEnabled(false);
        rvBedTypeChild.setAdapter(bedTypeAdapter);


    }



    private void initAdditionalModels() {

        additionalBedModels.clear();
        AdditionalBedModel additionalBedModel = new AdditionalBedModel();
        additionalBedModel.setBedName("Common Space");
        additionalBedModel.setBedTypesLists(getBedList(bedTypeList));
        additionalBedModel.setBedTypesListsTemp(getBedList(bedTypeListTemp));
        additionalBedModels.add(additionalBedModel);
    }


    public void plusMinus(ImageView minus, ImageView plus) {
        if (minus.isEnabled()) {
            minus.setBackground(minusenable);
        } else {
            minus.setBackground(minusdisable);
        }

        if (plus.isEnabled()) {
            plus.setBackground(plusenable);
        } else {
            plus.setBackground(plusdisable);
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        guestPosition = position;


        if (guestOldPosition != -1 && guestOldPosition == position) {
            additionalBedModels.get(position).setBedTypeShow(!additionalBedModels.get(position).isBedTypeShow());

        } else {

            if (guestOldPosition != -1)
                additionalBedModels.get(guestOldPosition).setBedTypeShow(false);


            additionalBedModels.get(position).setBedTypeShow(true);
        }

        oldBedTypesLists=getBedList(additionalBedModels.get(position).getBedTypesLists());
        updateBedRooms(position);
        showBottomSheet();
        guestOldPosition = position;

    }

    @Override
    public void onBedClick(int position, int count) {

        additionalBedModels.get(guestPosition).getBedTypesLists().get(position).setCount(count);

        updateBedType(guestPosition);
    }

    public void updateBedRooms(int position) {
        adapter.updateList(additionalBedModels);
        updateBedType(position);
    }

    public void updateBedType(int position) {
        bedTypesLists = additionalBedModels.get(position).getBedTypesLists();
        bedTypeAdapter.updateList(bedTypesLists);
    }

    private void getDynamicBedsList(){
        checkBedIsEmpty=true;
            List<AdditionalBedModel> AdditionalBedModelsTemp = new ArrayList<>();
        AdditionalBedModelsTemp.addAll(additionalBedModels);

        List<BedTypesAPIList> commonBedAPI = null;
        List<List<BedTypesAPIList>>bedDetailAPI=new ArrayList<>();
        List<BedTypesAPIList>bedDetail=null;
            if(additionalBedModels.size()>=1) {
                AdditionalBedModel commonBedDetail = additionalBedModels.get(additionalBedModels.size() - 1);

                commonBedAPI = totalBeds(commonBedDetail.getBedTypesLists());

                AdditionalBedModelsTemp.remove(AdditionalBedModelsTemp.size() - 1);
                if(AdditionalBedModelsTemp.size()>=1) {
                    for (AdditionalBedModel additionalBedModel: AdditionalBedModelsTemp){
                        bedDetail=totalBeds(additionalBedModel.getBedTypesLists());
                        if(bedDetail.size()>0)
                            checkBedIsEmpty=false;
                        bedDetailAPI.add(bedDetail);
                    }
                }
                 // this is used to addroomdetails call on api
            }
        if(bedDetailAPI!=null&&bedDetailAPI.size()>0&&!checkBedIsEmpty){

            if (commonBedAPI != null)
                commonBedDetails = gson.toJson(commonBedAPI);
            if (bedDetailAPI != null) {
                bedDetails = gson.toJson(bedDetailAPI);
                bedRoomCount = String.valueOf(bedDetailAPI.size());
            }
            addRoomDetails();
        }else{
            commonMethods.snackBar(getResources().getString(R.string.select_beds_type),"",false,2,roomsbed_next,getResources(),LYS_Rooms_Beds.this);
        }
    }

    private List<BedTypesAPIList> totalBeds(List<BedTypesList> bedTypesLists) {
        List<BedTypesAPIList> bedTypesAPILists=new ArrayList<>();
        for(int i=0;i<bedTypesLists.size();i++){
            if(bedTypesLists.get(i).getCount()>0){
                BedTypesAPIList bedTypesAPIList=new BedTypesAPIList();
                bedTypesAPIList.setId(bedTypesLists.get(i).getId());
                bedTypesAPIList.setCount(bedTypesLists.get(i).getCount());
                bedTypesAPILists.add(bedTypesAPIList);
            }
        }
        return bedTypesAPILists;
    }


    private void showBottomSheet() {

        View bottomSheet = findViewById(R.id.lltBottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_COLLAPSED&&!done) {
                    additionalBedModels.get(guestPosition).setBedTypesLists(oldBedTypesLists);
                    adapter.updateList(additionalBedModels);
                    rootView.setBackgroundColor(getResources().getColor(R.color.title_text_color));
                }

                if(newState == BottomSheetBehavior.STATE_COLLAPSED&&done){
                    rootView.setBackgroundColor(getResources().getColor(R.color.title_text_color));
                    done=false;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onBackPressed() {
        if (behavior != null && behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            rootView.setBackgroundColor(getResources().getColor(R.color.title_text_color));
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            rootView.setBackgroundColor(getResources().getColor(R.color.title_text_color));

        }
        else
            super.onBackPressed();
    }
}
