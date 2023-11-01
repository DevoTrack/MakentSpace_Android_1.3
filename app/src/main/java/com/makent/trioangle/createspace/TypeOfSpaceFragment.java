package com.makent.trioangle.createspace;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface;
import com.makent.trioangle.createspace.model.hostlisting.basics.BasicStepsModel;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TypeOfSpaceFragment extends Fragment implements ServiceListener {
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public @Inject
    CustomDialog customDialog;

    private View view;
    private BasicStepsActivityInterface listener;
    private Resources res;
    private BasicStepsActivity mActivity;

    private BasicStepsModel basicStepsModel;
    private BasicStepModel basicStepModel;

    @BindView(R.id.tv_space)
    TextView tvSpaceType;

    @BindView(R.id.tv_listing_txt)
    TextView tvListingText;


    @BindView(R.id.btn_continue)
    Button btnContinue;

    private static int REQ_SELECT_TYPE = 100;
    private boolean isSpaceEdit = false;
    private String selectSpaceTypeId = "";
    public Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;

    @OnClick(R.id.btn_continue)
    public void onContinueClick() {
        if (isSpaceEdit) {
            if(commonMethods.isOnline(mActivity)) {
                if (!mydialog.isShowing()) mydialog.show();
                apiService.updateSpace(updateSpaceType()).enqueue(new RequestCallback(this));
            }else{
                commonMethods.snackBar(mActivity.getResources().getString(R.string.network_failure),"",false,2,btnContinue,mActivity.getResources(),mActivity);
            }
        }else {
            if (basicStepModel==null) {
                mActivity.putHashMap("space_type", selectSpaceTypeId);
            }
            NavController navController = Navigation.findNavController(getActivity(), R.id.basic_nav_host_fragment);
            navController.navigate(R.id.action_spaceTypeFragment_to_roomCountFragment);
            mActivity.progressBarUpdate(0, 15);

        }

    }

    private HashMap<String, String> updateSpaceType(){
        HashMap<String,String> spaceTypeUpdate=new HashMap<>();
        spaceTypeUpdate.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        spaceTypeUpdate.put("space_id",localSharedPreferences.getSharedPreferences(Constants.mSpaceId));
        spaceTypeUpdate.put("step","basics");
        spaceTypeUpdate.put("space_type",selectSpaceTypeId);
        return spaceTypeUpdate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.type_of_space_fragment, container, false);
            ButterKnife.bind(this, view);
            initView();
        }

        return view;
    }

    private void initView() {
        mydialog = new Dialog_loading(mActivity);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        localSharedPreferences = new LocalSharedPreferences(mActivity);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            basicStepsModel = (BasicStepsModel) bundle.getSerializable("spaceType");
        }

        tvListingText.setText(commonMethods.changeColorForStar(res.getString(R.string.what_type_of_space_do_you_have)));
        basicStepModel = mActivity.getBasicStepModelData();
        if (basicStepModel != null && Integer.parseInt(basicStepModel.getSpaceType().getId()) != 0) {
            tvSpaceType.setText(basicStepModel.getSpaceType().getName());
            tvSpaceType.setTextColor(getResources().getColor(R.color.text_black));
            selectSpaceTypeId = basicStepModel.getSpaceType().getId();
            btnContinue.setEnabled(true);
        } else {
            btnContinue.setEnabled(false);
        }

        tvSpaceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent spaceType = new Intent(mActivity, SpaceTypeListActivity.class);
                spaceType.putExtra("spaceType", basicStepsModel);
                startActivityForResult(spaceType, REQ_SELECT_TYPE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode==-1) {
            if (basicStepModel!=null){
                isSpaceEdit=true;
            }
            btnContinue.setEnabled(true);
            tvSpaceType.setTextColor(getResources().getColor(R.color.text_black));
            tvSpaceType.setText(data.getStringExtra("selectedItem"));
            selectSpaceTypeId = data.getStringExtra("selectedItemId");
            System.out.println("Selected id" + data.getStringExtra("selectedItemId"));
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()){
            return;
        }

        if (jsonResp.isSuccess()){
            if (mydialog.isShowing()) mydialog.dismiss();
            Toast.makeText(mActivity, jsonResp.getStatusMsg(), Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(getActivity(), R.id.basic_nav_host_fragment);
            navController.navigate(R.id.action_spaceTypeFragment_to_roomCountFragment);
            mActivity.progressBarUpdate(0, 15);

        }else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,btnContinue,mActivity.getResources(),mActivity);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void init() {


        AppController.getAppComponent().inject(this);

        if (listener == null) return;
        res = (listener.getRes() != null) ? listener.getRes() : getActivity().getResources();
       // mActivity = (listener.getInstance() != null) ? listener.getInstance() : (BasicStepsActivity) getActivity();

        mActivity = (BasicStepsActivity) getActivity();

       // mActivity.nsvBasic.scrollTo(0, 0);
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

    @Override
    public void onDetach() {
        if (listener != null) listener = null;
        super.onDetach();
    }


}