package com.makent.trioangle.createspace;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomsCountFragment extends Fragment implements ServiceListener {

    @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    Gson gson;

    public @Inject
    CustomDialog customDialog;

    Typeface font1;
    Drawable minusenable, minusdisable, plusenable, plusdisable;

    @BindView(R.id.iv_room_plus)
    ImageView ivRoomPlus;
    @BindView(R.id.iv_room_minus)
    ImageView ivRoomMinus;
    @BindView(R.id.edt_room_count)
    EditText edtRoomCount;
    int roomCount = 0;

    String roomCountMin="0" , roomCountMax = "999";
    String sqrroomCountMin="0" , sqrroomCountMax = "999999";

    @BindView(R.id.iv_rest_room_plus)
    ImageView ivRestRoomPlus;
    @BindView(R.id.iv_rest_room_minus)
    ImageView ivRestRoomMinus;
    @BindView(R.id.edt__rest_room_count)
    EditText edtRestRoomCount;
    @BindView(R.id.tv_room_txt)
    TextView tvRoomTxt;

    int restRoomCount = 0;

    @BindView(R.id.iv_floor_plus)
    ImageView ivFloorPlus;
    @BindView(R.id.iv_floor_minus)
    ImageView ivFloorMinus;
    @BindView(R.id.edt__floor_count)
    EditText edtFloorCount;
    int floorNumberCount = 0;


    @BindView(R.id.iv_sqr_room_minus)
    ImageView ivSqrRoomMinus;
    @BindView(R.id.iv_sqr_room_plus)
    ImageView ivSqrRoomPlus;
    @BindView(R.id.edt__sqr_room_count)
    EditText edtSqrRoomCount;
    @BindView(R.id.tv_acres)
    TextView tvAcress;
    int sqrFootageCount = 0;


    @BindView(R.id.btn_continue)
    Button btnContinue;

    @BindView(R.id.tv_sqr_footage_txt)
    TextView tvSqrFootageText;

    @BindView(R.id.tv_furnished_txt)
    TextView tvFurnishedTxt;


    @BindView(R.id.tv_venue_txt)
    TextView tvVenueTxt;

    @BindView(R.id.rb_yes)
    RadioButton rbYes;

    @BindView(R.id.rb_no)
    RadioButton rbNo;


    @BindView(R.id.rb_shared)
    RadioButton rbShared;

    @BindView(R.id.rb_private)
    RadioButton rbPrivate;

    @BindView(R.id.rg_fully_furnished)
    RadioGroup rgFullyFurnished;

    @BindView(R.id.rg_working_station)
    RadioGroup rgWorkingStation;

    @BindView(R.id.rg_new_experienced)
    RadioGroup rgNewExperienced;

    @BindView(R.id.rb_new_to_all)
    RadioButton rbNewToAll;

    @BindView(R.id.rb_experienced)
    RadioButton rbExperienced;

    @BindView(R.id.tv_working_station_txt)
    TextView tvWorkingStationTxt;


    private View view;
    private BasicStepsActivityInterface listener;
    private Resources res;
    private BasicStepsActivity mActivity;
    private String sizeType = "";

    private BasicStepModel basicStepModel;
    public Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    private boolean boolFullyFurnished = false;
    private boolean boolWorkingStation = false;
    private boolean boolNewExperienced = false;
    private boolean boolWorkingStationCount = false;
    private boolean boolSqrRoomCount = false;


    @OnClick(R.id.tv_acres)
    public void showBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();


        RelativeLayout rltAcress = (RelativeLayout) view.findViewById(R.id.rlt_acress);
        RelativeLayout rltSqftss = (RelativeLayout) view.findViewById(R.id.rlt_sqft);

        rltAcress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAcress.setText(getContext().getResources().getString(R.string.acres));
                dialog.dismiss();
            }
        });

        rltSqftss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAcress.setText(getContext().getResources().getString(R.string.sq_ft));
                dialog.dismiss();
            }
        });


    }


    @OnClick(R.id.iv_room_plus)
    public void onRoomPlus() {

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivRoomPlus.startAnimation(myAnim);

        if (edtRoomCount.getText().toString().equals(""))
            roomCount = 0;
        else
            roomCount = Integer.parseInt(edtRoomCount.getText().toString());

        roomCount++;
        enablebuttons(ivRoomPlus, ivRoomMinus, roomCount, edtRoomCount,roomCountMax);

    }

    @OnClick(R.id.iv_room_minus)
    public void onRoomMinus() {

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivRoomMinus.startAnimation(myAnim);


        if (edtRoomCount.getText().toString().equals(""))
            roomCount = 0;
        else
            roomCount = Integer.parseInt(edtRoomCount.getText().toString());


        if (roomCount > 0)
            roomCount--;

        enablebuttons(ivRoomPlus, ivRoomMinus, roomCount, edtRoomCount,roomCountMax);

    }


    @OnClick(R.id.iv_rest_room_plus)
    public void onRestRoomPlus() {

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivRestRoomPlus.startAnimation(myAnim);

        if (edtRestRoomCount.getText().toString().equals(""))
            restRoomCount = 0;
        else
            restRoomCount = Integer.parseInt(edtRestRoomCount.getText().toString());


        restRoomCount++;
        //enablebuttons(ivRestRoomPlus, ivRestRoomMinus, restRoomCount, edtRestRoomCount);

    }

    @OnClick(R.id.iv_rest_room_minus)
    public void onRestRoomMinus() {
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivRestRoomMinus.startAnimation(myAnim);


        if (edtRestRoomCount.getText().toString().equals(""))
            restRoomCount = 0;
        else
            restRoomCount = Integer.parseInt(edtRestRoomCount.getText().toString());


        if (restRoomCount > 0)
            restRoomCount--;


        //enablebuttons(ivRestRoomPlus, ivRestRoomMinus, restRoomCount, edtRestRoomCount);

    }


    @OnClick(R.id.iv_floor_plus)
    public void onFloorPlus() {

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivFloorPlus.startAnimation(myAnim);

        if (edtFloorCount.getText().toString().equals(""))
            floorNumberCount = 0;
        else
            floorNumberCount = Integer.parseInt(edtFloorCount.getText().toString());


        floorNumberCount++;

        //enablebuttons(ivFloorPlus, ivFloorMinus, floorNumberCount, edtFloorCount);
    }

    @OnClick(R.id.iv_floor_minus)
    public void onFloorMinus() {
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivFloorMinus.startAnimation(myAnim);


        if (edtFloorCount.getText().toString().equals(""))
            floorNumberCount = 0;
        else
            floorNumberCount = Integer.parseInt(edtFloorCount.getText().toString());

        if (floorNumberCount > 0)
            floorNumberCount--;

        //enablebuttons(ivFloorPlus, ivFloorMinus, floorNumberCount, edtFloorCount);

    }


    @OnClick(R.id.iv_sqr_room_plus)
    public void onSquarePlus() {

        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivSqrRoomPlus.startAnimation(myAnim);

        if (edtSqrRoomCount.getText().toString().equals(""))
            sqrFootageCount = 0;
        else
            sqrFootageCount = Integer.parseInt(edtSqrRoomCount.getText().toString());


        sqrFootageCount++;

        enablebuttons(ivSqrRoomPlus, ivSqrRoomMinus, sqrFootageCount, edtSqrRoomCount,sqrroomCountMax);
    }

    @OnClick(R.id.iv_sqr_room_minus)
    public void onSquareMinus() {
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        ivSqrRoomMinus.startAnimation(myAnim);


        if (edtSqrRoomCount.getText().toString().equals(""))
            sqrFootageCount = 0;
        else
            sqrFootageCount = Integer.parseInt(edtSqrRoomCount.getText().toString());


        if (sqrFootageCount > 0)
            sqrFootageCount--;


        enablebuttons(ivSqrRoomPlus, ivSqrRoomMinus, sqrFootageCount, edtSqrRoomCount,sqrroomCountMax);
    }


    @OnClick(R.id.btn_continue)
    void onContinueClick() {
        if (basicStepModel != null) {
            if (commonMethods.isOnline(mActivity)) {
                System.out.println("Number of work stations : " + String.valueOf(checkCount("roomCount")));
                mActivity.getBasicStepModelData().setNoOfWorkstations(String.valueOf(checkCount("roomCount")));
                mActivity.getBasicStepModelData().setFullyFurnished(boolToYesNo(rbYes.isChecked()));
                mActivity.getBasicStepModelData().setSharedOrPrivate(boolToYesNo(rbShared.isChecked()));
                mActivity.getBasicStepModelData().setRentingSpaceFirsttime(boolToYesNo(rbNewToAll.isChecked()));

                mActivity.getBasicStepModelData().setSqFt(String.valueOf(checkCount("area")));
                mActivity.getBasicStepModelData().setSizeType(getSizeType());
                if (!mydialog.isShowing()) mydialog.show();
                apiService.updateSpace(updateRoomCount()).enqueue(new RequestCallback(this));
            } else {
                commonMethods.snackBar(mActivity.getResources().getString(R.string.network_failure), "", false, 2, btnContinue, mActivity.getResources(), mActivity);
            }
        } else {
            mActivity.putHashMap("no_of_workstations", String.valueOf(checkCount("roomCount")));
            mActivity.putHashMap("fully_furnished", boolToYesNo(rbYes.isChecked()));
            mActivity.putHashMap("shared_or_private", boolToYesNo(rbShared.isChecked()));
            mActivity.putHashMap("renting_space_firsttime", boolToYesNo(rbNewToAll.isChecked()));

            mActivity.putHashMap("sq_ft", String.valueOf(checkCount("area")));
            mActivity.putHashMap("size_type", getSizeType());

            NavController navController = Navigation.findNavController(getActivity(), R.id.basic_nav_host_fragment);
            navController.navigate(R.id.action_roomCountFragment_to_guestNumberFragment3);
            mActivity.progressBarUpdate(15, 30);

            mActivity.nsvBasic.scrollTo(0, 0);
        }

    }

    private String boolToYesNo(boolean checked) {

        if (checked)
            return "Yes";
        else
            return "No";

    }

    private int checkCount(String Type) {
        switch (Type) {
            case "roomCount":
                if (edtRoomCount.getText().toString().isEmpty()) {
                    return roomCount = 0;
                } else {
                    return roomCount = Integer.parseInt(edtRoomCount.getText().toString());
                }
            case "restroomCount":
                if (edtRestRoomCount.getText().toString().isEmpty()) {
                    return restRoomCount = 0;
                } else {
                    return restRoomCount = Integer.parseInt(edtRestRoomCount.getText().toString());
                }
            case "floorNumber":
                if (edtFloorCount.getText().toString().isEmpty()) {
                    return floorNumberCount = 0;
                } else {
                    return floorNumberCount = Integer.parseInt(edtFloorCount.getText().toString());
                }
            case "area":
                if (edtSqrRoomCount.getText().toString().isEmpty()) {
                    return sqrFootageCount = 0;
                } else {
                    return sqrFootageCount = Integer.parseInt(edtSqrRoomCount.getText().toString());
                }
            default:
                return 0;
        }
    }

    private HashMap<String, String> updateRoomCount() {
        HashMap<String, String> roomCountUpdate = new HashMap<>();
        roomCountUpdate.put("token", localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        roomCountUpdate.put("space_id", localSharedPreferences.getSharedPreferences(Constants.mSpaceId));
        roomCountUpdate.put("step", "basics");
        roomCountUpdate.put("no_of_workstations", String.valueOf(checkCount("roomCount")));


        roomCountUpdate.put("fully_furnished", boolToYesNo(rbYes.isChecked()));
        roomCountUpdate.put("shared_or_private", boolToYesNo(rbShared.isChecked()));
        roomCountUpdate.put("renting_space_firsttime", boolToYesNo(rbNewToAll.isChecked()));

        roomCountUpdate.put("sq_ft", String.valueOf(checkCount("area")));
        roomCountUpdate.put("size_type", getSizeType());
        return roomCountUpdate;
    }

    private String getSizeType() {
        if (tvAcress.getText().toString().equalsIgnoreCase(getContext().getResources().getString(R.string.sq_ft))) {
            sizeType = "sq_ft";
        } else {
            sizeType = "acre";
        }
        return sizeType;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            return;
        }

        if (jsonResp.isSuccess()) {
            if (mydialog.isShowing()) mydialog.dismiss();
            NavController navController = Navigation.findNavController(mActivity, R.id.basic_nav_host_fragment);
            navController.navigate(R.id.action_roomCountFragment_to_guestNumberFragment3);
            mActivity.progressBarUpdate(15, 30);

            mActivity.nsvBasic.scrollTo(0, 0);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, btnContinue, mActivity.getResources(), mActivity);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.room_count_fragment, container, false);
            ButterKnife.bind(this, view);
        }

        initListner();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private void initListner() {
        AppController.getAppComponent().inject(this);

        if (listener == null) return;
        res = (listener.getRes() != null) ? listener.getRes() : getActivity().getResources();
        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (BasicStepsActivity) getActivity();


        mydialog = new Dialog_loading(mActivity);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        localSharedPreferences = new LocalSharedPreferences(mActivity);

        tvSqrFootageText.setText(commonMethods.changeColorForStar(res.getString(R.string.how_big_your_space)));
        tvFurnishedTxt.setText(commonMethods.changeColorForStar(res.getString(R.string.is_your_space_fully_furnished)));
        tvVenueTxt.setText(commonMethods.changeColorForStar(res.getString(R.string.have_you_ever_hosted_your_venue_with_a_website_like_makentspace_before)));
        tvWorkingStationTxt.setText(commonMethods.changeColorForStar(res.getString(R.string.are_your_working_stations_desks_shared_or_private)));
        tvRoomTxt.setText(commonMethods.changeColorForStar(res.getString(R.string.how_many_working_stations_are_available_in_your_space)));


        basicStepModel = mActivity.getBasicStepModelData();
        if (basicStepModel != null) {
            setDatas();
        } else {
            if(mActivity.getHashMap().get("no_of_workstations")!=null)
                setDatasOnCreate();
            else
                btnContinue.setEnabled(false);
        }

        edtRoomCount.addTextChangedListener(new GenericTextWatcher(edtRoomCount));
        edtRestRoomCount.addTextChangedListener(new GenericTextWatcher(edtRestRoomCount));
        edtFloorCount.addTextChangedListener(new GenericTextWatcher(edtFloorCount));
        edtSqrRoomCount.addTextChangedListener(new GenericTextWatcher(edtSqrRoomCount));

        edtRoomCount.setFilters(new InputFilter[]{ new InputFilterMinMax(roomCountMin, roomCountMax)});
        edtSqrRoomCount.setFilters(new InputFilter[]{ new InputFilterMinMax(sqrroomCountMin, sqrroomCountMax)});

        radioGroupChangeListner(rgFullyFurnished);
        radioGroupChangeListner(rgWorkingStation);
        radioGroupChangeListner(rgNewExperienced);

        edtSqrRoomCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || Integer.parseInt(s.toString()) == 0) {


                    boolSqrRoomCount = false;
                    ivSqrRoomMinus.setEnabled(false);
                    ivSqrRoomMinus.setBackground(minusdisable);
                } else {

                    boolSqrRoomCount = true;
                    ivSqrRoomMinus.setEnabled(true);
                    ivSqrRoomMinus.setBackground(minusenable);
                }
                updateButtonContinue();
            }
        });
        enableDisableButtons();
    }

    private void setDatasOnCreate() {


        System.out.println("Number of work stations : " + mActivity.getHashMap().get("no_of_workstations"));
        edtRoomCount.setText(mActivity.getHashMap().get("no_of_workstations"));
        try {
            roomCount = Integer.parseInt(mActivity.getHashMap().get("no_of_workstations"));
        } catch (Exception e) {
            roomCount = 0;
        }


        boolNewExperienced = true;
        boolWorkingStation = true;
        boolFullyFurnished = true;


        if (Objects.equals(mActivity.getHashMap().get("fully_furnished"), "Yes")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }




        if (Objects.equals(mActivity.getHashMap().get("shared_or_private"), "Yes")) {
            rbShared.setChecked(true);
        } else {
            rbPrivate.setChecked(true);
        }


        if (Objects.equals(mActivity.getHashMap().get("renting_space_firsttime"), "Yes")) {
            rbNewToAll.setChecked(true);
        } else {
            rbExperienced.setChecked(true);
        }

        edtSqrRoomCount.setText(mActivity.getHashMap().get("sq_ft"));
        sqrFootageCount = Integer.parseInt(mActivity.getHashMap().get("sq_ft"));

        tvAcress.setText(setSizeType(mActivity.getHashMap().get("size_type")));

        btnContinue.setEnabled(true);


    }

    public void setDatas() {
        System.out.println("Number of work stations : " + basicStepModel.getNoOfWorkstations());
        edtRoomCount.setText(basicStepModel.getNoOfWorkstations());
        try {
            roomCount = Integer.parseInt(basicStepModel.getNoOfWorkstations());
        } catch (Exception e) {
            roomCount = 0;
        }

        edtRestRoomCount.setText(basicStepModel.getNumberOfRestrooms());
        restRoomCount = Integer.parseInt(basicStepModel.getNumberOfRestrooms());
        boolNewExperienced = true;
        boolWorkingStation = true;
        boolFullyFurnished = true;


        if (basicStepModel.getFullyFurnished().equals("Yes")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }


        if (basicStepModel.getFullyFurnished().equals("Yes")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }


        if (basicStepModel.getSharedOrPrivate().equals("Yes")) {
            rbShared.setChecked(true);
        } else {
            rbPrivate.setChecked(true);
        }


        if (basicStepModel.getRentingSpaceFirsttime().equals("Yes")) {
            rbNewToAll.setChecked(true);
        } else {
            rbExperienced.setChecked(true);
        }

        edtSqrRoomCount.setText(basicStepModel.getSqFt());
        sqrFootageCount = Integer.parseInt(basicStepModel.getSqFt());

        tvAcress.setText(setSizeType(basicStepModel.getSizeType()));

        btnContinue.setEnabled(true);

    }

    private void radioGroupChangeListner(RadioGroup rGroup) {
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getId()) {
                    case R.id.rg_fully_furnished:
                        boolFullyFurnished = true;
                        updateButtonContinue();
                        break;
                    case R.id.rg_working_station:
                        boolWorkingStation = true;
                        updateButtonContinue();
                        break;
                    case R.id.rg_new_experienced:
                        boolNewExperienced = true;
                        updateButtonContinue();
                        break;
                }

            }
        });
    }

    /**
     * To enable and disable continue button
     */
    private void updateButtonContinue() {
        if (boolFullyFurnished && boolWorkingStation && boolNewExperienced && boolWorkingStationCount && boolSqrRoomCount) {
            btnContinue.setEnabled(true);
        } else {
            btnContinue.setEnabled(false);
        }
    }

    private String setSizeType(String getsizeType) {
        if (getsizeType.equalsIgnoreCase("sq_ft")) {
            sizeType = getContext().getResources().getString(R.string.sq_ft);
        } else {
            sizeType = getContext().getResources().getString(R.string.acres);
        }
        return sizeType;
    }

    public void enableDisableButtons() {
        enablebuttons(ivRoomPlus, ivRoomMinus, roomCount, edtRoomCount,roomCountMax);
       // enablebuttons(ivRestRoomPlus, ivRestRoomMinus, restRoomCount, edtRestRoomCount);
      //  enablebuttons(ivFloorPlus, ivFloorMinus, floorNumberCount, edtFloorCount);
        enablebuttons(ivSqrRoomPlus, ivSqrRoomMinus, sqrFootageCount, edtSqrRoomCount,sqrroomCountMax);
    }


    public void enablebuttons(ImageView plus, ImageView minus, int count, EditText edtView,String countMax) // this function to used on guest count enable and disable function
    {

        if (count == 0)
            minus.setEnabled(false);
        else
            minus.setEnabled(true);

        if(count < Integer.parseInt(countMax)){
            plus.setEnabled(true);
        }else{
            plus.setEnabled(false);
        }



        edtView.setText(Integer.toString(count));
        edtView.setSelection(edtView.getText().toString().length());


        plusMinus(minus, plus);
    }


    public void plusMinus(ImageView view, ImageView view1) {

        initFonts();


        if (view.isEnabled()) {
            view.setBackground(minusenable);
        } else {
            view.setBackground(minusdisable);
        }

        if (view1.isEnabled()) {
            view1.setBackground(plusenable);
        } else {
            view1.setBackground(plusdisable);
        }
    }

    private void initFonts() {
        font1 = Typeface.createFromAsset(res.getAssets(), getResources().getString(R.string.fonts_makent4));
        minusenable = new FontIconDrawable(getContext(), getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30).colorRes(R.color.guestButton);
        plusenable = new FontIconDrawable(getContext(), getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30).colorRes(R.color.guestButton);
        minusdisable = new FontIconDrawable(getContext(), getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable);
        plusdisable = new FontIconDrawable(getContext(), getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BasicStepsActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }


    @Override
    public void onDetach() {
        if (listener != null) listener = null;
        super.onDetach();
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edt_room_count:
                    if (text.isEmpty() || Integer.parseInt(text) == 0) {
                        ivRoomMinus.setEnabled(false);
                        boolWorkingStationCount = false;
                        ivRoomMinus.setBackground(minusdisable);
                    } else {
                        ivRoomMinus.setEnabled(true);
                        boolWorkingStationCount = true;
                        ivRoomMinus.setBackground(minusenable);
                    }

                    if(!text.isEmpty()){
                        if (Integer.parseInt(roomCountMax) > Integer.valueOf(text) ) {
                            ivRoomPlus.setBackground(plusenable);
                            ivRoomPlus.setEnabled(true);
                        }else{
                            ivRoomPlus.setBackground(plusdisable);
                            ivRoomPlus.setEnabled(false);
                        }
                    }

                    updateButtonContinue();
                    break;
                case R.id.edt__rest_room_count:
                    if (text.isEmpty() || Integer.parseInt(text) == 0) {
                        ivRestRoomMinus.setEnabled(false);
                        ivRestRoomMinus.setBackground(minusdisable);
                    } else {
                        ivRestRoomMinus.setEnabled(true);
                        ivRestRoomMinus.setBackground(minusenable);
                    }
                    break;
                case R.id.edt__floor_count:
                    if (text.isEmpty() || Integer.parseInt(text) == 0) {
                        ivFloorMinus.setEnabled(false);
                        ivFloorMinus.setBackground(minusdisable);
                    } else {
                        ivFloorMinus.setEnabled(true);
                        ivFloorMinus.setBackground(minusenable);
                    }
                    break;
                case R.id.edt__sqr_room_count:
                    if (text.isEmpty() || Integer.parseInt(text) == 0) {
                        ivSqrRoomMinus.setEnabled(false);
                        ivSqrRoomMinus.setBackground(minusdisable);
                    } else {
                        ivSqrRoomMinus.setEnabled(true);
                        ivSqrRoomMinus.setBackground(minusenable);
                    }



                    if(!text.isEmpty()){
                        if (Integer.parseInt(sqrroomCountMax) > Integer.valueOf(text) ) {
                            ivSqrRoomPlus.setBackground(plusenable);
                            ivSqrRoomPlus.setEnabled(true);
                        }else{
                            ivSqrRoomPlus.setBackground(plusdisable);
                            ivSqrRoomPlus.setEnabled(false);
                        }
                    }

                    break;
            }
        }
    }
}