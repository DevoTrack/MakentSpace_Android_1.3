package com.makent.trioangle.host.optionaldetails;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails
 * @category    OD_CancelPolicy
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
Optional details host cancellation policyList for seleced room and satic page
*************************************************************** */

public class OD_CancelPolicy extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout book_title, booktype_title, flexible_lay, moderate_lay,strict_lay;
    CheckBox flexible_check, moderate_check,strict_check;

    LocalSharedPreferences localSharedPreferences;
    String userid, roomid, roomcancelpolicy, cancelpolicy;
    String flexible, moderate,strict;
    ImageView book_dot_loader;
    protected boolean isInternetAvailable;
    public ImageView booktype_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        setContentView(R.layout.activity_od_cancel_policy);
        book_dot_loader = (ImageView) findViewById(R.id.book_dot_loader);
        book_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(book_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);
        commonMethods = new CommonMethods();


        book_title = (RelativeLayout) findViewById(R.id.book_title);
        booktype_title = (RelativeLayout) findViewById(R.id.booktype_title);
        flexible_lay = (RelativeLayout) findViewById(R.id.flexible_lay);
        moderate_lay = (RelativeLayout) findViewById(R.id.moderate_lay);
        strict_lay = (RelativeLayout) findViewById(R.id.strict_lay);

        flexible_check = (CheckBox) findViewById(R.id.flexible_check);
        moderate_check = (CheckBox) findViewById(R.id.moderate_check);
        strict_check = (CheckBox) findViewById(R.id.strict_check);
        booktype_back =(ImageView)findViewById(R.id.booktype_back);
        commonMethods.rotateArrow(booktype_back,this);

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        roomcancelpolicy = localSharedPreferences.getSharedPreferences(Constants.ListRoomPolicyType);
        System.out.println("List Room PolicyType "+roomcancelpolicy);
        flexible = getResources().getString(R.string.cancellation_type3);
        moderate = getResources().getString(R.string.cancellation_type2);
        strict = getResources().getString(R.string.cancellation_type1);


        if (roomcancelpolicy != null)// && !roomcancelpolicy.equals("")) {
        {
            if (roomcancelpolicy.equals(strict)) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(false);
                strict_check.setChecked(true);
            }else if (roomcancelpolicy.equals(moderate)) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(true);
                strict_check.setChecked(false);
            } else {
                flexible_check.setChecked(true);
                moderate_check.setChecked(false);
                strict_check.setChecked(false);
            }
        } else {
            flexible_check.setChecked(true);
            moderate_check.setChecked(false);
            strict_check.setChecked(false);
        }


        book_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {

                update();
            }
        });

        booktype_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                update();
            }
        });

        flexible_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(true);
                moderate_check.setChecked(false);
                strict_check.setChecked(false);
            }
        });
        moderate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(true);
                strict_check.setChecked(false);
            }
        });
        strict_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(false);
                strict_check.setChecked(true);
            }
        });

        flexible_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(true);
                moderate_check.setChecked(false);
                strict_check.setChecked(false);
            }
        });

        moderate_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(true);
                strict_check.setChecked(false);
            }
        });

        strict_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                flexible_check.setChecked(false);
                moderate_check.setChecked(false);
                strict_check.setChecked(true);
            }
        });

    }

    public void update() {
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (strict_check.isChecked()) {
                cancelpolicy = strict;
            } else if (moderate_check.isChecked()) {
                cancelpolicy = moderate;
            } else {
                cancelpolicy = flexible;
            }

            System.out.println("Room cancelpolicy" + roomcancelpolicy);
            System.out.println("Room cancelpolicy" + cancelpolicy);

            if (roomcancelpolicy == null || roomcancelpolicy.equals("") || roomcancelpolicy.equals(cancelpolicy) || cancelpolicy.equals("")) {
                if (!cancelpolicy.equals("")) {
                    updateBookType();
                } else {
                    onBackPressed();
                    finish();
                }
                onBackPressed();
                finish();
            } else {
                updateBookType();
            }
        }else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, book_title, getResources(), this);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            book_title.setVisibility(View.VISIBLE);
            book_dot_loader.setVisibility(View.GONE);

            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            book_title.setVisibility(View.VISIBLE);
            book_dot_loader.setVisibility(View.GONE);

            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,book_title,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        book_title.setVisibility(View.VISIBLE);
        book_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, book_title, getResources(), this);
        }
    }

    // Update cancellation policyList
    public void updateBookType(){

        book_title.setVisibility(View.GONE);
        book_dot_loader.setVisibility(View.VISIBLE);
        String policytype;
        if (cancelpolicy.equals(strict)) {
            policytype = getResources().getString(R.string.cancellation_type1);
        }else if (cancelpolicy.equals(moderate)) {
            policytype = getResources().getString(R.string.cancellation_type2);
        }  else {
            policytype = getResources().getString(R.string.cancellation_type3);
        }

        localSharedPreferences.saveSharedPreferences(Constants.ListRoomPolicyType, cancelpolicy);
        apiService.updatePolicy(userid,roomid,policytype).enqueue(new RequestCallback(this));
    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
