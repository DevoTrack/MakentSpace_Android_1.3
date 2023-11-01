package com.makent.trioangle.host.RoomsBeds;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.BedTypeList;
import com.makent.trioangle.model.BedTypeModel;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makent.trioangle.util.Enums.REQ_BEDTYPE_GET;
import static com.makent.trioangle.util.Enums.REQ_BEDTYPE_UPDATE;

public class BedsActivity extends AppCompatActivity implements SleepingArangementAdapter.OnItemClickListener, BedTypeAdapter.OnBedClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    String commonBedDetails = "", bedDetails = "", bedRoomCount = "";

    boolean done=false;

    List<AdditionalBedModel> additionalBedModels = new ArrayList<>();
    @BindView(R.id.rv_bed_type)
    RecyclerView rvBedType;

    @BindView(R.id.iv_done_dot_loader)
    ImageView ivDoneDotLoader;

    @BindView(R.id.tv_done)
    TextView tvDone;


    Typeface font1;
    Drawable minusenable, minusdisable, plusenable, plusdisable;
    @BindView(R.id.iv_bedminus)
    ImageView ivBedminus;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_bed_count)
    TextView tvBedCount;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    ArrayList<BedTypesList> bedTypeList = new ArrayList<>();
    List<BedTypesList> bedTypesLists = new ArrayList<>();
    //List<BedTypesList> bedTypeList = new ArrayList<>();
    List<BedTypesList> bedTypeListTemp = new ArrayList<>();
    @BindView(R.id.iv_bedplus)
    ImageView ivBedplus;
    int maxBeds = 0;
    @BindView(R.id.rvBedType)
    RecyclerView rvBedTypeChild;
    @BindView(R.id.roomsdetailsnestedscrool)
    NestedScrollView roomsdetailsnestedscrool;

    @BindView(R.id.rlt_header)
    RelativeLayout rlt_header;

    CoordinatorLayout coordinatorLayout;
    ViewGroup vg;
    BottomSheetBehavior behavior;


    View bottomSheet;

    private BedTypeAdapter bedTypeAdapter;
    private RelativeLayout rltDoneButton;
    private RelativeLayout rltDone;
    private SleepingArangementAdapter adapter;
    //Need to b done
    private int guestOldPosition = -1;
    boolean checkBedIsEmpty=true;
    private int guestPosition;
    private BedTypeModel bedTypeModel;
    private BedTypeList bedTypeListModel;
    private ArrayList<ArrayList<BedTypesList>> bedRoomModel = new ArrayList<>();
    private ArrayList<BedTypesList> commonBedList;
    List<BedTypesList> oldBedTypesLists = new ArrayList<>();


    @OnClick(R.id.iv_close)
    public void onClose() {
        getDynamicBedsList();
    }

    //Need to b done **

    @OnClick(R.id.iv_bedminus)
    public void onBedMinus() {

        tvBedCount.setText(String.valueOf(--maxBeds));

        enablebuttons();


        if (additionalBedModels.size() > 1)
            additionalBedModels.remove(additionalBedModels.size() - 2);

        adapter.notifyDataSetChanged();
    }

    private void enablebuttons() {

        if (maxBeds == 0) {
            ivBedplus.setEnabled(true);
            ivBedminus.setEnabled(false);
        } else if (maxBeds == 10) {
            ivBedplus.setEnabled(false);
            ivBedminus.setEnabled(true);
        } else {
            ivBedplus.setEnabled(true);
            ivBedminus.setEnabled(true);
        }


        plusMinus(ivBedminus, ivBedplus);

    }

    @OnClick(R.id.iv_bedplus)
    public void onBedPlus() {

        tvBedCount.setText(String.valueOf(++maxBeds));

        enablebuttons();


        AdditionalBedModel additionalBedModel = new AdditionalBedModel();
        additionalBedModel.setBedName("");
        additionalBedModel.setBedTypesLists(getBedList(bedTypeList));
        additionalBedModel.setBedTypesListsTemp(getBedList(bedTypeListTemp));

        additionalBedModels.add(additionalBedModel);


        AdditionalBedModel swapModel = additionalBedModels.get(additionalBedModels.size() - 1);
        AdditionalBedModel swapModel1 = additionalBedModels.get(additionalBedModels.size() - 2);
        additionalBedModels.set(additionalBedModels.size() - 1, swapModel1);
        additionalBedModels.set(additionalBedModels.size() - 2, swapModel);
        adapter.notifyDataSetChanged();
    }


    private List<BedTypesList> getBedList(List<BedTypesList> bedTypeList,ArrayList<BedTypesList> bedTypes) {
        List<BedTypesList> bedTypeLists = new ArrayList<>();
        BedTypesList bedTypesList;


        for (int i = 0; i < bedTypeList.size(); i++) {
            bedTypesList = new BedTypesList();
            bedTypesList.setId(bedTypeList.get(i).getId());
            bedTypesList.setName(bedTypeList.get(i).getName());
            bedTypesList.setCount(bedTypeList.get(i).getCount());


            for(int j=0;j<bedTypes.size();j++){
                if(bedTypeList.get(i).getId()==bedTypes.get(j).getId()){

                    bedTypesList.setCount(bedTypes.get(j).getCount());

                    break;
                }
            }


            bedTypeLists.add(bedTypesList);
        }

        return bedTypeLists;
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
        setContentView(R.layout.activity_beds);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        getIntentValues();
        init();
        enablebuttons();

    }

    private void getIntentValues() {
        bedTypeList = (ArrayList<BedTypesList>) getIntent().getSerializableExtra("bedTypeList");
        //initBedTypeListTemp();

    }




    public void getBedtType() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.getBedTypeDetails(localSharedPreferences.getSharedPreferences(Constants.AccessToken), localSharedPreferences.getSharedPreferences(Constants.mSpaceId)).enqueue(new RequestCallback(REQ_BEDTYPE_GET, this));

    }


    private void init() {


        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rltDoneButton = findViewById(R.id.rltDone);
        rltDone = findViewById(R.id.rlt_done);

        vg = findViewById (R.id.rootView);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        ivDoneDotLoader.setVisibility(View.GONE);
        rltDone.setVisibility(View.VISIBLE);

        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(ivDoneDotLoader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences = new LocalSharedPreferences(this);


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


        getBedtType();
        initAdditionalModels();


        rltDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                getDynamicBedsList();
            }
        });

        rltDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                done=true;
                updateBedRooms(0);
                onBackPressed();
            }
        });




        adapter = new SleepingArangementAdapter(additionalBedModels, this, this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (resultCode == Constants.ViewType.BedType) {

                int selectedPosition = Integer.parseInt(data.getStringExtra("clickedPos"));

                String selectedItem = additionalBedModels.get(guestPosition).getBedTypesListsTemp().get(selectedPosition).getName();

                additionalBedModels.get(guestPosition).getBedTypesLists().add(addBedTypeFromTemp(selectedItem));
                additionalBedModels.get(guestPosition).getBedTypesListsTemp().remove(selectedPosition);
                adapter.notifyDataSetChanged();

                //updateBedType();
                updateBedRooms(guestPosition);

            }
        }
    }


    private BedTypesList addBedTypeFromTemp(String selectedItem) {

        BedTypesList bedTypesList;
        bedTypesList = new BedTypesList();
        bedTypesList.setName(selectedItem);
        bedTypesList.setCount(0);

        return bedTypesList;
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
        //updateBedType();
        //adapter.updateList(additionalBedModels);
        guestOldPosition = position;


    }


    public void updateBedRooms(int position) {
        adapter.updateList(additionalBedModels);
        updateBedType(position);
    }

    public void updateBedType(int position) {
        bedTypesLists = additionalBedModels.get(position).getBedTypesLists();
        bedTypeAdapter.updateList(bedTypesLists);
    }

    @Override
    public void onBedClick(int position, int count) {
        bottomSheet.setBackgroundDrawable(getResources().getDrawable(R.drawable.d_gradient_background));

        additionalBedModels.get(guestPosition).getBedTypesLists().get(position).setCount(count);


        //updateBedType();
        updateBedRooms(guestPosition);
    }


    private void showBottomSheet() {

        bottomSheet = findViewById(R.id.lltBottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        /*behavior.setHideable(true);
        behavior.setSkipCollapsed(true);*/
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                if (newState == BottomSheetBehavior.STATE_COLLAPSED&&!done) {
                    additionalBedModels.get(guestPosition).setBedTypesLists(oldBedTypesLists);
                    adapter.updateList(additionalBedModels);
                }

                if(newState == BottomSheetBehavior.STATE_COLLAPSED&&done)
                    done=false;
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
            /*behavior.setHideable(true);
            behavior.setSkipCollapsed(true);*/
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            /*behavior.setHideable(true);
            behavior.setSkipCollapsed(true);*/
            adapter.notifyDataSetChanged();
        }
        else{
            finish();
        }
    }

    public void saveBedType() {
        

        ivDoneDotLoader.setVisibility(View.VISIBLE);
        rltDone.setVisibility(View.GONE);
        HashMap<String, String> updateRoom = new HashMap<>();
        updateRoom.put("token", localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        updateRoom.put("room_id", localSharedPreferences.getSharedPreferences(Constants.mSpaceId));
        updateRoom.put("bedrooms", bedRoomCount);
        updateRoom.put("common_room_bed_details", commonBedDetails);
        updateRoom.put("bedroom_bed_details",bedDetails);

        apiService.saveBedTypeDetails(updateRoom).enqueue(new RequestCallback(REQ_BEDTYPE_UPDATE, this));

    }

    private void getDynamicBedsList(){
        checkBedIsEmpty=true;
        List<AdditionalBedModel> tempAdditionalBedModels = new ArrayList<>();
        tempAdditionalBedModels.addAll(additionalBedModels);

        List<BedTypesAPIList> commonBedAPI = null;
        List<List<BedTypesAPIList>>bedDetailAPI=new ArrayList<>();
        List<BedTypesAPIList>bedDetail=null;
        if(additionalBedModels.size()>=1) {
            AdditionalBedModel commonBedDetail = additionalBedModels.get(additionalBedModels.size() - 1);

            commonBedAPI = totalBeds(commonBedDetail.getBedTypesLists());

            tempAdditionalBedModels.remove(tempAdditionalBedModels.size()-1);
            if(tempAdditionalBedModels.size()>=1) {
                for (AdditionalBedModel additionalBedModel: tempAdditionalBedModels){
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
            saveBedType();
        }else{
            commonMethods.snackBar(getResources().getString(R.string.select_beds_type),"",false,2,tvBedCount,getResources(), this);
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


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_BEDTYPE_GET:
                if (jsonResp.isSuccess()) {
                    onSuccessBedtype(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                }
                break;
            case REQ_BEDTYPE_UPDATE:
                ivDoneDotLoader.setVisibility(View.GONE);
                rltDone.setVisibility(View.VISIBLE);
                if (jsonResp.isSuccess()) {
                    localSharedPreferences.saveSharedPreferences(Constants.ListBedRooms,additionalBedModels.size()-1);

                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, dob_img, getResources(), this);
                }
                break;

            default:
                break;

        }

    }

    private void onSuccessBedtype(JsonResponse jsonResp) {

        bedTypeModel = gson.fromJson(jsonResp.getStrResponse(), BedTypeModel.class);
        bedTypeListModel = bedTypeModel.getData();

        updateList(bedTypeListModel);

        //bedTypeList = bedTypeListModel.getCommonBeds();
    }

    private void updateList(BedTypeList bedTypeListModel) {

        //bedRoomModel = bedTypeListModel.getBedRoomBeds();
        //commonBedList = bedTypeListModel.getCommonBeds();
        bedRoomModel.clear();
        bedRoomModel.addAll(bedTypeListModel.getBedRoomBeds());
        bedRoomModel.add(bedTypeListModel.getCommonBeds());
        additionalBedModels.clear();
        AdditionalBedModel additionalBedModel;
        System.out.println("Size check One "+bedRoomModel.get(0).size());
        System.out.println("Size check Two "+bedTypeListModel.getBedRoomBeds().size());
        System.out.println("Size check Three "+bedTypeListModel.getCommonBeds().size());

        if (bedRoomModel.size() > 0) {
            for (int j = 0; j < bedRoomModel.size(); j++) {
                additionalBedModel = new AdditionalBedModel();
                additionalBedModel.setBedName("");
                additionalBedModel.setId("");
                additionalBedModel.setBedTypeShow(false);


                additionalBedModel.setBedTypesLists(getBedList(bedTypeList,bedRoomModel.get(j)));
                //additionalBedModel.setBedTypesListsTemp(getBedList(bedTypeListTemp));
                additionalBedModels.add(additionalBedModel);
            }
        }

        maxBeds = additionalBedModels.size()-1;
        tvBedCount.setText(String.valueOf(maxBeds));
        enablebuttons();


        adapter.notifyDataSetChanged();


    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        ivDoneDotLoader.setVisibility(View.GONE);
        rltDone.setVisibility(View.VISIBLE);
    }
}
