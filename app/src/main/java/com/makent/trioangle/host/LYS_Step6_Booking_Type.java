package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step6_Booking_TypeActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

/* ***********************************************************************
This is List Your Space Step6  Contain Booking Type
**************************************************************************  */
public class LYS_Step6_Booking_Type extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    EditText edt;
    RelativeLayout book_title,booktype_title,instantbook_lay,requestbook_lay;
    CheckBox requestbook_check,instantbook_check;
    protected boolean isInternetAvailable;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roombooktype,booktype;
    String instantbook,requestbook;
    ImageView book_dot_loader;
    Snackbar snackbar;
    public ImageView booktype_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step6_booking_type);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods = new CommonMethods();

        book_dot_loader = (ImageView) findViewById(R.id.book_dot_loader);
        book_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(book_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        edt = (EditText) findViewById(R.id.edt);
        book_title=(RelativeLayout) findViewById(R.id.book_title);
        booktype_title=(RelativeLayout) findViewById(R.id.booktype_title);
        instantbook_lay=(RelativeLayout) findViewById(R.id.instantbook_lay);
        requestbook_lay=(RelativeLayout) findViewById(R.id.requestbook_lay);
        booktype_back = (ImageView)findViewById(R.id.booktype_back);
        commonMethods.rotateArrow(booktype_back,this);

        requestbook_check=(CheckBox) findViewById(R.id.requestbook_check);
        instantbook_check=(CheckBox) findViewById(R.id.instantbook_check);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roombooktype=localSharedPreferences.getSharedPreferences(Constants.ListRoomBookType);

        instantbook=getResources().getString(R.string.instantbook);
        requestbook=getResources().getString(R.string.requestbook);




        if(roombooktype!=null&&!roombooktype.equals("")) {

            if(roombooktype.equals(instantbook))
            {   instantbook_check.setChecked(true);
                requestbook_check.setChecked(false);
            }else
            {
                requestbook_check.setChecked(true);
                instantbook_check.setChecked(false);
            }
        }else
        {
            requestbook_check.setChecked(true);
            instantbook_check.setChecked(false);
        }


        book_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    update();// this is used to update the request book function
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,edt,edt,getResources(),LYS_Step6_Booking_Type.this);
                }
            }
        });

        booktype_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    update();
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,edt,edt,getResources(),LYS_Step6_Booking_Type.this);
                }
            }
        });

        instantbook_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                instantbook_check.setChecked(instantbook_check.isChecked());
                requestbook_check.setChecked(!requestbook_check.isChecked());
            }
        });
        requestbook_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                instantbook_check.setChecked(!instantbook_check.isChecked());
                requestbook_check.setChecked(requestbook_check.isChecked());
            }
        });

        instantbook_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if(instantbook_check.isChecked())
                {
                    instantbook_check.setChecked(false);
                    requestbook_check.setChecked(true);
                }else
                {
                    instantbook_check.setChecked(true);
                    requestbook_check.setChecked(false);
                }
            }
        });

        requestbook_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if(!requestbook_check.isChecked())
                {
                    instantbook_check.setChecked(false);
                    requestbook_check.setChecked(true);
                }else
                {
                    instantbook_check.setChecked(true);
                    requestbook_check.setChecked(false);
                }
            }
        });

    }

    public void update()
    {
        if(requestbook_check.isChecked())
        {
            booktype=requestbook;
        }else
        {
            booktype=instantbook;
        }

        System.out.println("Room Book type"+roombooktype);
        System.out.println("Room booktype"+booktype);

        if(roombooktype==null||roombooktype.equals("")||roombooktype.equals(booktype)||booktype.equals(""))
        {
            if(!booktype.equals(""))
            {
                updateBookType();
            }else {
                onBackPressed();
                finish();
            }
            onBackPressed();
            finish();
        }else
        {
            updateBookType();
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
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,edt,edt,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        book_title.setVisibility(View.VISIBLE);
        book_dot_loader.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,edt,edt,getResources(),this);
    }

    public void updateBookType(){
        book_title.setVisibility(View.GONE);
        book_dot_loader.setVisibility(View.VISIBLE);
        String bookingtype;
        if(booktype.equals(requestbook))
        {
            bookingtype=getResources().getString(R.string.request_book);
        }else
        {
            bookingtype=getResources().getString(R.string.instant_book);
        }
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomBookType,booktype);
        apiService.updateBookingType(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,bookingtype).enqueue(new RequestCallback(this));
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
