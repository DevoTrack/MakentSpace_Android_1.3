package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    ProfilePageActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/* ************************************************************
                   Profile Page
User can view his profile and full details and show other user profile also
*************************************************************** */
public class ProfilePageActivity extends AppCompatActivity implements View.OnClickListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    ImageView profilemain_back,profilemain_edit;

    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;
    EditText edt;
    String userid,UserDetails;
    String otheruserid=null,UserId;
    protected boolean isInternetAvailable;
    ImageView profilemain_img;
    private TextView profilemain_username;
    private TextView profilemain_userlocation;
    private TextView profilemain_aboutuser;
    private TextView profilemain_member;
    private TextView profilemain_work;
    private TextView profilemain_school;
    private TextView profilemain_verified;
    private TextView profilemain_verified_text;
    private RelativeLayout emailconnect;
    private RelativeLayout facebookconnect;
    private RelativeLayout googleconnect;
    private RelativeLayout twitterconnect;
    private RelativeLayout profilemain_membersince;
    LinearLayout profile_work,profile_school;

    private boolean isFromProfile = false;
    @BindView(R.id.rlt_oh_no)
    RelativeLayout rlt_oh_no;
    @BindView(R.id.rlt_layout)
    RelativeLayout rlt_layout;

    @BindView(R.id.error_profilemain_back)
    ImageView ErrorProfileback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
    /*   StrictMode.ThreadPolicy policyList = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policyList);*/

        //Get other user id for show other user profile
        Intent x=getIntent();
        otheruserid=x.getStringExtra("otheruserid");
        isFromProfile = x.getBooleanExtra("profile",false);

        edt = (EditText) findViewById(R.id.edt);
        localSharedPreferences= new LocalSharedPreferences(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        UserId=String.valueOf(localSharedPreferences.getSharedPreferencesInt(Constants.UserId));
        UserDetails=localSharedPreferences.getSharedPreferences(Constants.UserDetails);

        profilemain_back=(ImageView)findViewById(R.id.profilemain_back);
        profilemain_edit=(ImageView)findViewById(R.id.profilemain_edit);
        // Check current user and other user is same or not if same to show edit button
            if(UserId.equals(otheruserid))
            {
                otheruserid=null;
                UserDetails=null;
            }
        if(otheruserid!=null)
        {
            profilemain_edit.setVisibility(View.GONE);
            UserDetails=null;
        }

        commonMethods.rotateArrow(profilemain_back,this);
        commonMethods.rotateArrow(ErrorProfileback,this);
        profilemain_username=(TextView)findViewById(R.id.profilemain_username);
        profilemain_aboutuser=(TextView)findViewById(R.id.profilemain_aboutuser);
        profilemain_member=(TextView)findViewById(R.id.profilemain_member);
        profilemain_work=(TextView)findViewById(R.id.profilemain_work);
        profilemain_school=(TextView)findViewById(R.id.profilemain_school);
        profilemain_verified=(TextView)findViewById(R.id.profilemain_verified);
        profilemain_verified_text=(TextView)findViewById(R.id.profilemain_verified_text);
        profilemain_userlocation=(TextView)findViewById(R.id.profilemain_userlocation);

        emailconnect=(RelativeLayout)findViewById(R.id.emailconnect);
        facebookconnect=(RelativeLayout)findViewById(R.id.facebookconnect);
        googleconnect=(RelativeLayout)findViewById(R.id.googleconnect);
        twitterconnect=(RelativeLayout)findViewById(R.id.twitterconnect);

        emailconnect.setVisibility(View.GONE);
        facebookconnect.setVisibility(View.GONE);
        googleconnect.setVisibility(View.GONE);
        twitterconnect.setVisibility(View.GONE);

        profilemain_membersince=(RelativeLayout)findViewById(R.id.profilemain_membersince);
        profile_work=(LinearLayout)findViewById(R.id.profile_work);
        profile_school=(LinearLayout)findViewById(R.id.profile_school);

        profilemain_back.setOnClickListener(this);
        profilemain_edit.setOnClickListener(this);
        ErrorProfileback.setOnClickListener(this);

        profilemain_img=(ImageView)findViewById(R.id.profilemain_img);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    if (isInternetAvailable) {

     if (UserDetails == null) {
         mydialog.show();
         // Get user details from API
         getUserProfile();
     } else {
         // Get user details from Local shared preference
         getUserDetails(UserDetails);
     }
 }else {
        commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,edt,edt,getResources(),this);
 }
    }

    @Override
    public void onBackPressed() {
        if(otheruserid!=null)
        {
            localSharedPreferences.saveSharedPreferences(Constants.UserDetails,null);
            finish();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDetails=localSharedPreferences.getSharedPreferences(Constants.UserDetails);
        if(UserDetails==null) {
            mydialog.show();
            getUserProfile();
        }else
        {
            getUserDetails(UserDetails);
        }

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.error_profilemain_back:
            case R.id.profilemain_back: {
                onBackPressed();
            }
            break;
            case R.id.profilemain_edit:
            {
                // Call Edit profile page
                Intent x= new Intent(getApplicationContext(),EditProfileActivity.class);
                startActivity(x);
            }
            break;
        }
    }

    // Get User details
    public void getUserDetails(String UserDetails)
    {
        try {
            JSONObject user_jsonobj = new JSONObject(UserDetails);
            for (int i = 0; i < user_jsonobj.length(); i++) {

                String username,user_thumb_image,user_location,member_from,about_me,school="",
                        work="";

                boolean isDeletedUser = false;
                if(!isFromProfile) {
                    isDeletedUser = user_jsonobj.getBoolean("host_user_deleted");
                }
                if (isDeletedUser){
                    rlt_layout.setVisibility(View.GONE);
                    rlt_oh_no.setVisibility(View.VISIBLE);
                }
                else {
                    rlt_layout.setVisibility(View.VISIBLE);
                    rlt_oh_no.setVisibility(View.GONE);
                }
                if(otheruserid!=null)
                {
                    username = user_jsonobj.getString("first_name") + " " + user_jsonobj.getString("last_name");
                    user_thumb_image = user_jsonobj.getString("large_image");
                    user_location = user_jsonobj.getString("user_location");
                    member_from = user_jsonobj.getString("member_from");
                    about_me = user_jsonobj.getString("about_me");

                }else {
                    username = user_jsonobj.getString("first_name") + " " + user_jsonobj.getString("last_name");
                    user_thumb_image = user_jsonobj.getString("large_image_url");
                    user_location = user_jsonobj.getString("user_location");
                    member_from = user_jsonobj.getString("member_from");
                    about_me = user_jsonobj.getString("about_me");
                    school = user_jsonobj.getString("school");
                    work = user_jsonobj.getString("work");
                }

                username=username.replaceAll("%20","");
                username=username.replaceAll("20","");
                profilemain_username.setText(username);

                profilemain_verified.setVisibility(View.GONE);
                profilemain_verified_text.setVisibility(View.GONE);

                if(school.equals(""))
                {
                    profile_school.setVisibility(View.GONE);
                }else
                {
                    profile_school.setVisibility(View.VISIBLE);
                    profilemain_school.setText(school);
                }

                if(work.equals(""))
                {
                    profile_work.setVisibility(View.GONE);
                }else
                {
                    profile_work.setVisibility(View.VISIBLE);
                    profilemain_work.setText(work);
                }

                if(member_from.equals(""))
                {
                    profilemain_membersince.setVisibility(View.GONE);
                }else
                {
                    profilemain_membersince.setVisibility(View.VISIBLE);
                    profilemain_member.setText(getResources().getString(R.string.membersince)+""+member_from);
                }

                if(user_location.equals(""))
                {
                    profilemain_userlocation.setVisibility(View.GONE);
                }else
                {
                    profilemain_userlocation.setVisibility(View.VISIBLE);
                    profilemain_userlocation.setText(user_location);
                }
                if(about_me.equals(""))
                {
                    profilemain_aboutuser.setVisibility(View.GONE);
                }else
                {
                    profilemain_aboutuser.setVisibility(View.VISIBLE);
                    profilemain_aboutuser.setText(about_me);
                }


              //profilemain_img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getApplicationContext())
                        .load(user_thumb_image)

                        .into(new DrawableImageViewTarget(profilemain_img) {
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                            }
                        });

                profilemain_img.setBackgroundColor(getResources().getColor(R.color.title_text_color));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessProfile(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    public void getUserProfile(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        if(otheruserid!=null)
        {
            apiService.otherUserProfile(localSharedPreferences.getSharedPreferences(Constants.AccessToken),otheruserid).enqueue(new RequestCallback(this));
        }else {
            apiService.viewProfile(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
        }
    }

    public void onSuccessProfile(JsonResponse jsonResp){
        try{
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONObject user_jsonobj = response.getJSONObject("user_details");
            localSharedPreferences.saveSharedPreferences(Constants.UserDetails,user_jsonobj.toString());
            getUserDetails(user_jsonobj.toString());
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }catch (JSONException j){
            j.printStackTrace();
        }

    }

    // Check network exception
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
