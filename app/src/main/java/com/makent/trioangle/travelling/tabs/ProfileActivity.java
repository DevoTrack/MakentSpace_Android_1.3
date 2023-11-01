package com.makent.trioangle.travelling.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestProfileActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.host.Switch_to_HostActivity;
import com.makent.trioangle.host.tabs.YourReservationActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.profile.HelpActivity;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.profile.SettingActivity;
import com.makent.trioangle.profile.WhyHostActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    Snackbar snackbar;
    RelativeLayout profile_layout;
    LinearLayout switch_to, invite_friends, settings, help, whyhost,lltYourResevation,lltYourWishList;
    TextView view_edit;
    public EditText edt;
    LocalSharedPreferences localSharedPreferences;
    int isHost;
    String userid;
    String UserDetails;
    TextView switch_to_txt, user_name;
    ImageView user_profile, profile_dotloader;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    private boolean doubleBackToExitPressedOnce=false;
    // used by onBackPressed()
    private String currencyCode = "";
    private String currencySymbol = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tab);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences = new LocalSharedPreferences(this);
        edt = (EditText) findViewById(R.id.edt);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        UserDetails = localSharedPreferences.getSharedPreferences(Constants.UserDetails);
        isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        edt = (EditText) findViewById(R.id.edit_aboutme);
        switch_to_txt = (TextView) findViewById(R.id.switch_to_txt);

        if (isHost == 0) {
            switch_to_txt.setText(getResources().getString(R.string.switch_to));
        } else {
            switch_to_txt.setText(getResources().getString(R.string.switch_to_t));
        }

        view_edit = (TextView) findViewById(R.id.view_edit);
        profile_layout = (RelativeLayout) findViewById(R.id.profile_layout1);
        invite_friends = (LinearLayout) findViewById(R.id.invite_friends);
        switch_to = (LinearLayout) findViewById(R.id.switch_to);
        settings = (LinearLayout) findViewById(R.id.settings);
        help = (LinearLayout) findViewById(R.id.help);
        whyhost = (LinearLayout) findViewById(R.id.whyhost);
        lltYourResevation = (LinearLayout) findViewById(R.id.lltYourResevation);
        lltYourWishList = (LinearLayout) findViewById(R.id.lltYourWishList);

        Typeface font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent1));
        Typeface fontlogo = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent_logo));
        Drawable icon1 = new FontIconDrawable(ProfileActivity.this, getResources().getString(R.string.flogoinvite), fontlogo).sizeDp(25).colorRes(R.color.text_black);
        Drawable icon2 = new FontIconDrawable(ProfileActivity.this, getResources().getString(R.string.f1like), font1).sizeDp(25).colorRes(R.color.text_black);
        Drawable icon3 = new FontIconDrawable(ProfileActivity.this, getResources().getString(R.string.flogosetting), fontlogo).sizeDp(25).colorRes(R.color.text_black);
        Drawable icon4 = new FontIconDrawable(ProfileActivity.this, getResources().getString(R.string.flogohelp), fontlogo).sizeDp(25).colorRes(R.color.text_black);

        Drawable icon5 = new FontIconDrawable(ProfileActivity.this, getResources().getString(R.string.f1whyhost), font1).sizeDp(25).colorRes(R.color.text_black);
        ImageView gift, switch_mode, settingsimg, lifesaver, add_listing;
        gift = (ImageView) findViewById(R.id.gift);
        switch_mode = (ImageView) findViewById(R.id.switch_mode);
        settingsimg = (ImageView) findViewById(R.id.settingsimg);
        lifesaver = (ImageView) findViewById(R.id.lifesaver);
        add_listing = (ImageView) findViewById(R.id.add_listing);

        gift.setImageDrawable(icon1);
        // switch_mode.setImageDrawable(icon2);
        settingsimg.setImageDrawable(icon3);
        lifesaver.setImageDrawable(icon4);
        add_listing.setImageDrawable(icon5);

        user_name = (TextView) findViewById(R.id.user_name);
        user_profile = (ImageView) findViewById(R.id.user_profile);
        profile_dotloader = (ImageView) findViewById(R.id.profile_dotloader);

        invite_friends.setOnClickListener(this);
        switch_to.setOnClickListener(this);
        settings.setOnClickListener(this);
        help.setOnClickListener(this);
        whyhost.setOnClickListener(this);
        lltYourResevation.setOnClickListener(this);
        lltYourWishList.setOnClickListener(this);
        profile_layout.setOnClickListener(this);
        view_edit.setOnClickListener(this);
        user_profile.setOnClickListener(this);
        user_name.setOnClickListener(this);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(profile_dotloader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //if(UserDetails==null) {
        //  myDialog.show();

        profile_dotloader.setVisibility(View.VISIBLE);
        profile_layout.setVisibility(View.INVISIBLE);
        System.out.println("User Details Null");
            /*if (isInternetAvailable) {
                 getUserProfile();
            }else {
                commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,edt,edt,getResources(),this);
            }*/
        /*}else
        {
            System.out.println("User Details"+UserDetails);
            getUserDetails();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");

        UserDetails = localSharedPreferences.getSharedPreferences(Constants.UserDetails);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable)
        {
            getUserProfile();
        }
        else
        {
            snackBar();
           // commonMethods.snackBar(getResources().getString(R.string.interneterror), getResources().getString(R.string.retry), true, 2, view_edit, getResources(), this);
        }
        if (UserDetails == null)
        {
            //myDialog.show();
            System.out.println("User Details Null");
            profile_dotloader.setVisibility(View.VISIBLE);
            profile_layout.setVisibility(View.INVISIBLE);
        }
        else
        {
            System.out.println("User Details" + UserDetails);
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
            getUserDetails();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_friends:
            {

            }
            break;
            case R.id.switch_to:
            {
                Intent x = new Intent(getApplicationContext(), Switch_to_HostActivity.class);
                startActivity(x);
                finish();
            }
            break;
            case R.id.settings:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), SettingActivity.class);
                x.putExtra("currency_code",currencyCode);
                x.putExtra("currency_symbol",currencySymbol);
                startActivity(x);
            }
            break;
            case R.id.help:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(x);
            }
            break;
            case R.id.whyhost:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), WhyHostActivity.class);
                startActivity(x);
            }
            break;
            case R.id.view_edit:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                x.putExtra("profile",true);
                startActivity(x);
            }
            break;

            case R.id.user_name:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                x.putExtra("profile",true);
                startActivity(x);
            }
            break;
            case R.id.user_profile:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                x.putExtra("profile",true);
                startActivity(x);
            }
            break;
            case R.id.lltYourResevation:
            {
                localSharedPreferences.saveSharedPreferences(Constants.isHost,1);
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), YourReservationActivity.class);
                startActivity(x);
            }
            break;
            case R.id.lltYourWishList:
            {
                Constants.backpressed=2;
                Intent x = new Intent(getApplicationContext(), SavedActivity.class);
                startActivity(x);
            }
            break;
        }
    }

    public void getUserDetails() {
        try {
            JSONObject user_jsonobj = new JSONObject(UserDetails);
            for (int i = 0; i < user_jsonobj.length(); i++) {

                String username, user_thumb_image;

                username = user_jsonobj.getString("first_name");//+" "+user_jsonobj.getString("last_name");
                user_thumb_image = user_jsonobj.getString("small_image_url");
                String payout_count = user_jsonobj.getString("payout_count");
                localSharedPreferences.saveSharedPreferences(Constants.UserDOB, user_jsonobj.getString("dob"));
                System.out.println("USER DOB " + localSharedPreferences.getSharedPreferences(Constants.UserDOB));
                localSharedPreferences.saveSharedPreferences(Constants.PayoutCount, payout_count);
                currencyCode = user_jsonobj.getString("currency_code");
                currencySymbol = user_jsonobj.getString("currency_symbol");

                localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,currencyCode);
                localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol,currencySymbol);


                System.out.println("###-->currency code"+currencyCode);
                System.out.println("###-->currency symbol"+currencySymbol);

                username = username.replaceAll("%20", "");
                username = username.replaceAll("20", "");
                user_name.setText(username);

                Glide.with(getApplicationContext()).asBitmap().load(user_thumb_image).into(new BitmapImageViewTarget(user_profile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        user_profile.setImageDrawable(circularBitmapDrawable);
                    }
                });

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void getUserProfile() {
        if(Constants.backpressed==2)
        {
            Constants.backpressed=1;
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
        else
        {
            if (!mydialog.isShowing()) {
                mydialog.show();
            }
        }
        apiService.viewProfile(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
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

    }

    public void onSuccessProfile(JsonResponse jsonResp) {
        try {
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONObject user_jsonobj = response.getJSONObject("user_details");
            localSharedPreferences.saveSharedPreferences(Constants.UserDetails, user_jsonobj.toString());
            UserDetails = localSharedPreferences.getSharedPreferences(Constants.UserDetails);
            getUserDetails();
            profile_dotloader.setVisibility(View.INVISIBLE);
            profile_layout.setVisibility(View.VISIBLE);
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        } catch (JSONException j) {
            j.printStackTrace();
            profile_dotloader.setVisibility(View.INVISIBLE);
            profile_layout.setVisibility(View.VISIBLE);
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }

    }


    public void onBackPressed() {
        System.out.println(Constants.backpressed+"Backpressed");
        if (Constants.backpressed >= 1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
            // clean up
            Constants.backpressed = Constants.backpressed + 1;
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Constants.backpressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    public void snackBar()
    {
        // Create the Snackbar
        snackbar = Snackbar.make(profile_layout, "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    getUserProfile();

                    // new UpdateDateDetails().execute();
                }else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable){
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }
}
