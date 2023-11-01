package com.makent.trioangle.createspace;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface;
import com.makent.trioangle.createspace.model.hostlisting.basics.BasicStepsModel;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.ListFunc;
import static com.makent.trioangle.helper.Constants.ListFuncEdit;

public class BasicStepsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BasicStepsActivityInterface, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    @BindView(R.id.pb)
    ProgressBar progressBar;

    @BindView(R.id.nsv_basic)
    NestedScrollView nsvBasic;

    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;

    BasicStepsModel basicStepsModelList;

    private BasicStepModel basicStepModelList;

    public LinkedHashMap<String, String> getBasicSteps = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getAppComponent().inject(this);
        setContentView(R.layout.activity_basic_steps);
        ButterKnife.bind(this);

        localSharedPreferences = new LocalSharedPreferences(this);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getIntentValues();
        getBasicDetails();
    }

    private void getBasicDetails() {
        if (commonMethods.isOnline(this)) {
            if (!mydialog.isShowing()) mydialog.show();
            apiService.spaceBasicListing(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
        } else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, nsvBasic, getResources(), this);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        fragmentSwitch();

    }

    private void getIntentValues() {

        if (getIntent().getStringExtra(ListFunc).equalsIgnoreCase(ListFuncEdit)) {
            basicStepModelList = (BasicStepModel) getIntent().getExtras().getSerializable(Constants.BASICSTEP);
        }

    }

    public BasicStepModel getBasicStepModelData() {
        return basicStepModelList;
    }


    /**
     * To find out current fragment while back press
     */

    private void fragmentSwitch() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.basic_nav_host_fragment);
        final Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        if (fragment instanceof TypeOfSpaceFragment) progressBarUpdate(15, 0);
        else if (fragment instanceof RoomsCountFragment) progressBarUpdate(30, 15);
        else if (fragment instanceof GuestNumberFragment) progressBarUpdate(45, 30);
        else if (fragment instanceof AmenistiesFragment) progressBarUpdate(60, 45);
        else if (fragment instanceof ServiceFragment) progressBarUpdate(75, 60);
        else if (fragment instanceof AddressFragment) progressBarUpdate(90, 75);

    }

    public void putHashMap(String key, String value) {
        if (getBasicSteps.containsKey(key)) getBasicSteps.remove(key);

        getBasicSteps.put(key, value);
    }

    public LinkedHashMap<String, String> getHashMap() {
        return getBasicSteps;
    }

    /**
     * update progress bar while switching between fragments
     *
     * @param fromProgress start value of pg bar
     * @param toProgress   end value of pg bar
     */

    public void progressBarUpdate(int fromProgress, int toProgress) {

        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, fromProgress, toProgress);
        anim.setDuration(1000);
        progressBar.startAnimation(anim);
    }

    @Override
    public Resources getRes() {
        return this.getResources();
    }

    @Override
    public BasicStepsActivity getInstance() {
        return this;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();
        if (!jsonResp.isOnline()) {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, nsvBasic, getResources(), this);
            return;
        }
        if (jsonResp.isSuccess()) {
            basicStepsModelList = gson.fromJson(jsonResp.getStrResponse(), BasicStepsModel.class);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.basic_nav_host_fragment);
            final Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
            NavController navController = navHostFragment.findNavController(fragment);
            //navController.setGraph(R.navigation.basic_step_graph,);
            Bundle bundle = new Bundle();
            bundle.putSerializable("spaceType", basicStepsModelList);
            navController.setGraph(navController.getGraph(), bundle);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, nsvBasic, getResources(), this);
        }
    }

    public BasicStepsModel getBasicStepsList() {
        return basicStepsModelList;
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

}
