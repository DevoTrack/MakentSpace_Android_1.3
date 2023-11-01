package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestWishListCreateActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.WishListObjects;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is WishListCreate Page Contain Create a list
**************************************************************************  */
public class WishListCreateActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    Button wishlistcreate_close;
    RelativeLayout wishlistcrete_btn,everyone,inviteonly;
    CheckBox everyonecheckBox,inviteonlycheckBox;
    EditText addtitle_edittext;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,privacy,listname;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    private String roomExp;
    ImageView arrow_create;
    private String isFrom;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_create);
        commonMethods = new CommonMethods();

        localSharedPreferences=new LocalSharedPreferences(this);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.WishlistRoomId);
        listname=localSharedPreferences.getSharedPreferences(Constants.WishListAddress);
        Log.e("WishlistAddress","WisAddress"+listname);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        arrow_create =(ImageView)findViewById(R.id.arrow_create);
        commonMethods.rotateArrow(arrow_create,this);

        wishlistcreate_close=(Button) findViewById(R.id.wishlistcreate_close);
        commonMethods.setTvAlign(wishlistcreate_close,this);
        wishlistcrete_btn=(RelativeLayout)findViewById(R.id.wishlistcrete_btn);
        everyone=(RelativeLayout)findViewById(R.id.everyone);
        inviteonly=(RelativeLayout)findViewById(R.id.inviteonly);

        addtitle_edittext=(EditText) findViewById(R.id.addtitle_edittext);

        isFrom=getIntent().getStringExtra("isFrom");
        position=getIntent().getIntExtra("position",-1);


//        enableSubmitIfReady();

        String wishlistExp = localSharedPreferences.getSharedPreferences(Constants.WishFromExp);
        System.out.println("listnamelistname3 : "+wishlistExp);

        System.out.println("Wishlist checks : "+ wishlistExp);

        addtitle_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {

                //enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(TextUtils.isEmpty(s.toString().trim())){
                    wishlistcrete_btn.setEnabled(false);
                    wishlistcrete_btn.setBackgroundResource(R.drawable.d_white_filled_radius);

                }else{
                    wishlistcrete_btn.setEnabled(true);
                    wishlistcrete_btn.setBackgroundResource(R.drawable.d_blue_filled_radius);
                }
//                wishlistcrete_btn.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
//                wishlistcrete_btn.setBackgroundResource(R.drawable.d_blue_filled_radius);

            }
        });


if(listname!=null) {
    String[] listarray = listname.split(",");
    Log.e("Countryname","listarray"+listname);
    addtitle_edittext.setText(listarray[0]);
    addtitle_edittext.setSelected(true);
    addtitle_edittext.setHint(listname);
}
        everyonecheckBox=(CheckBox)findViewById(R.id.everyonecheckBox);
        inviteonlycheckBox=(CheckBox)findViewById(R.id.inviteonlycheckBox);

        wishlistcreate_close.setOnClickListener(this);
        wishlistcrete_btn.setOnClickListener(this);
        everyone.setOnClickListener(this);
        inviteonly.setOnClickListener(this);
        inviteonlycheckBox.setOnClickListener(this);
        everyonecheckBox.setOnClickListener(this);

    }

    private void enableSubmitIfReady() {

        if(addtitle_edittext.getText().toString().equals("")){
            wishlistcrete_btn.setEnabled(false);
        }
        else{
            wishlistcreate_close.setEnabled(true);
        }
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.wishlistcreate_close:
            {
                onBackPressed();
            }break;
            case R.id.everyone:
            {
                //onBackPressed();
                everyonecheckBox.setChecked(true);
                inviteonlycheckBox.setChecked(false);
            }break;
            case R.id.inviteonly:
            {
               // onBackPressed();
                everyonecheckBox.setChecked(false);
                inviteonlycheckBox.setChecked(true);
            }break;
            case R.id.everyonecheckBox:
            {
               // onBackPressed();
                everyonecheckBox.setChecked(true);
                inviteonlycheckBox.setChecked(false);
            }break;
            case R.id.inviteonlycheckBox:
            {
                //onBackPressed();
                everyonecheckBox.setChecked(false);
                inviteonlycheckBox.setChecked(true);
            }break;
            case R.id.wishlistcrete_btn:
            {
                listname=addtitle_edittext.getText().toString();
                System.out.println("listnamelistname1"+listname);
                listname= listname.replaceAll("^\\s+|\\s+$", "");
                localSharedPreferences.saveSharedPreferences(Constants.WishTitle,listname);

//                try {
//                    listname= URLEncoder.encode(listname, "UTF-8");
//                    System.out.println("listnamelistname2"+listname);
//                    listname=listname.replace("+"," ");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                if(!listname.equals("")) {
                    if (everyonecheckBox.isChecked()) {
                        privacy = "0";
                    } else {
                        privacy = "1";
                    }
                    isInternetAvailable = getNetworkState().isConnectingToInternet();
                    if (isInternetAvailable) {
                        addWishList();
                    }else {
                        commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, addtitle_edittext, getResources(), this); //this is  used to snackbar  dialog function
                    }
                }else
                { Snackbar snackbar = Snackbar.make(addtitle_edittext, getResources().getString(R.string.invalid_title), Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                    snackbar.show(); //this is  used to snackbar  dialog function

                }
              //  onBackPressed();
            }break;
        }
    }


    /**
     * @Reference Get amenities list from server
     */
    public void addWishList(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.addWishList(addWishListing()).enqueue(new RequestCallback(this));
    }

    private LinkedHashMap<String,String> addWishListing(){
        LinkedHashMap<String,String> addParams= new LinkedHashMap<>();
        addParams.put("space_id",roomid);
        addParams.put("list_name",listname);
        addParams.put("privacy_settings",privacy);
        addParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return addParams;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        } if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, addtitle_edittext, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload,"reload");

            boolean isWishlistFromMap = localSharedPreferences.getSharedPreferencesBool(Constants.MapFilterWishlist);

            WishListObjects wishListObjects=new WishListObjects();
            if (isFrom!=null&&!TextUtils.isEmpty(isFrom)){
                wishListObjects.setWishListFrom(isFrom);
                wishListObjects.setWishListPosition(position);
                EventBus.getDefault().postSticky(wishListObjects);
            }else {
                wishListObjects.setWishListFrom("");
                wishListObjects.setWishListPosition(-1);
                EventBus.getDefault().postSticky(wishListObjects);
            }
            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
               System.out.println("Status Msg"+jsonResp.getStatusMsg());
            commonMethods.snackBar(getResources().getString(R.string.invalid_room), "", false, 2, addtitle_edittext, getResources(), this);
            //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, addtitle_edittext, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
