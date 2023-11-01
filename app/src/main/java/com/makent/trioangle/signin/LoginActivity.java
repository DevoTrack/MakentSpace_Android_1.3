package com.makent.trioangle.signin;

/**
 * @package com.makent.trioangle
 * @subpackage signin
 * @category LoginActivity
 * @author Trioangle Product Team
 * @version 1.1
 */


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.newhome.map.ShowAllMapActivity;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.spacebooking.views.SpaceAvailableActivity;
import com.makent.trioangle.spacedetail.model.SpaceResult;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.MapActivity;
import com.makent.trioangle.travelling.RequestActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.isContactHost;

//  ***Experience Start***
//  ***Experience End***

/* ************************************************************
                   Login Page
Login page get Email id and password from user check with server and allow to user to use the Makent
*************************************************************** */

public class LoginActivity extends AppCompatActivity implements ServiceListener {

    Button login_forgot;  // login back to go back to Main Page  // Login forgot go to forgot password page
    ImageView login_back, login_next; // Chcek with server the email id and password and go to Home page
    TextView login_show;// Show or hide password
    EditText login_email, login_password; // Get Email id and password from users
    String loginemail, loginpassword; // To store email id and password as string
    protected boolean isInternetAvailable;
    LocalSharedPreferences localSharedPreferences;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    RelativeLayout activity_main;
    String[] blockedDates;

    //  ***Experience Start***

    //  ***Experience End***

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Layout design Name
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences = new LocalSharedPreferences(this);
        //  ***Experience Start***

        //  ***Experience End***

        blockedDates = getIntent().getStringArrayExtra("blockdate");
        // Assign Design Id to Java variable
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        login_back = (ImageView) findViewById(R.id.login_back);
        commonMethods.rotateArrow(login_back,this);
        login_forgot = (Button) findViewById(R.id.login_forgot);
        login_next = (ImageView) findViewById(R.id.login_next);
        login_show = (TextView) findViewById(R.id.login_show);

        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        //commonMethods.rotateArrow(login_next,this);

        login_next.setAlpha(.5f);
        login_next.setEnabled(false);
        login_email.addTextChangedListener(new LoginTextWatcher(login_email));
        login_password.addTextChangedListener(new LoginTextWatcher(login_password));

        // On Click function used to click action go to Main Page
        login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        commonMethods.rotateArrow(login_next,this);
        login_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                /*Intent x = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(x);*/
                onBackPressed();

            }
        });

        // On Click function used to click action for show or hide password

        login_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (login_show.getText().toString().equals(getResources().getString(R.string.show))) {
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    login_password.setSelection(login_password.length());
                    login_show.setText(R.string.hide);

                } else {
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    login_password.setSelection(login_password.length());
                    login_show.setText(R.string.show);
                }
            }
        });

        // On Click function used to click action for check Email id and password in server and then go to home page

        login_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    loginemail = login_email.getText().toString();
                    loginpassword = login_password.getText().toString();
                    login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                                      // login_next.setImageResource(0);
                   // login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                    userLogin();
                    //new Login().execute();
                    // AndroidNetworking.post(ApiEndPoint.BASE_URL + ApiEndPoint.GET_JSON_ARRAY)
                    //  Intent x = new Intent(getApplicationContext(),HomeActivity.class);
                    //  startActivity(x);

                } else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, login_password, login_show, getResources(), LoginActivity.this);
                }
            }
        });

        // On Click function used to click action for go to forgot password page

        login_forgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                // Call forgot password page
                Intent x = new Intent(getApplicationContext(), Forgot_PasswordActivity.class);
                x.putExtra("email", loginemail);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
        });

// Get email address from signup page while email alredy exits
        Intent login = getIntent();
        loginemail = login.getStringExtra("email");
        if (loginemail != null) login_email.setText(loginemail);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent x = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(x, bndlanimation);
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessLogin(jsonResp); // onSuccessLogin call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            System.out.print("jsonResp.getStatusMsg()" + jsonResp.getStatusMsg());
            login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
            Glide.with(this).clear(login_next);
            //commonMethods.snackBar(getResources().getString(R.string.invalidelogin), getResources().getString(R.string.showpassword), true, 2, login_password, login_show, getResources(), this);
            Snackbar snackbar = Snackbar.make(login_password,jsonResp.getStatusMsg(), Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
            snackbar.show();
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            System.out.print("jsonResp.getStatusMsg()" + jsonResp.getStatusMsg());
            System.out.print("jsonResp.data()" + data);
        }
        login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        Glide.with(this).clear(login_next);
        commonMethods.snackBar(getResources().getString(R.string.invalidelogin), getResources().getString(R.string.showpassword), true, 2, login_password, login_show, getResources(), this);
    }

    private class LoginTextWatcher implements TextWatcher {

        private View view;

        private LoginTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.login_email:
                    validateEmail();
                    break;
                case R.id.login_password:
                    validatePassword();
                    break;
            }

            if (validateEmail() && validatePassword()) {
                login_next.setAlpha(1f);
                login_next.setEnabled(true);
            } else {
                login_next.setAlpha(.5f);
                login_next.setEnabled(false);
            }
        }
    }

    private boolean validateEmail() {
        String email = login_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {

            return false;
        } else {
        }

        return true;
    }

    private boolean validatePassword() {
        //8>7
        if (login_password.getText().toString().trim().length()>= Constants.PasswordLength) {
            return true;
        }

        return false;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Check Given Email and Password is valid or not usign API
    public void userLogin() {

        TimeZone tz = TimeZone.getDefault();
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(login_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        apiService.login(loginemail, loginpassword,tz.getID(),langCode).enqueue(new RequestCallback(this));
    }

    public void onSuccessLogin(JsonResponse jsonResponse) {
        localSharedPreferences.saveSharedPreferences(Constants.UserId, (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.UserId, int.class));
        localSharedPreferences.saveSharedPreferences(Constants.AccessToken, (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.AccessToken, String.class));

        String currencysymbol=(String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.CurrencySymbolApi, String.class);
        String cs= Html.fromHtml(currencysymbol).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, cs);


        commonMethods.NotrotateArrow(login_next,this);
        String currencySymbol = Html.fromHtml((String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "currency_symbol", String.class)).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencySymbol);
        localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "currency_code", String.class));

        login_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
       // commonMethods.rotateArrow(login_next,this);
        Glide.with(this).clear(login_next);
        System.out.println("get Local Value of LastPage " + localSharedPreferences.getSharedPreferences(Constants.LastPage));
        if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null) {
            if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && (localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("inbox") || localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("saved"))) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            } if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && (localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("listing") || localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("saved"))) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("profile")) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Space_detail")) {
                Intent x = new Intent(getApplicationContext(), SpaceDetailActivity.class);
                x.putExtra("isSpaceBack", 1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
            // ***Experience Start***

            // ***Experience End***
            else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("ExploreSearch")) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                x.putExtra("tabsaved", 0);
                startActivity(x);
                finish();
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Map")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString("explore", "");
                Type type = new TypeToken<ArrayList<ExploreDataModel>>() {
                }.getType();
                ArrayList<ExploreDataModel> arrayList = gson.fromJson(json, type);

                Intent x = new Intent(getApplicationContext(), MapActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("searchlist", (Serializable) arrayList);
                x.putExtra("BUNDLE", args);
                x.putExtra("isMapBack", 1);
                startActivity(x);
                finish();

            }else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("ShowAll")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString("showAllMap", "");
                Type type = new TypeToken<ArrayList<Detail>>() {
                }.getType();
                ArrayList<Detail> arrayList = gson.fromJson(json, type);

                Intent x = new Intent(getApplicationContext(), ShowAllMapActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("showAllMap", (Serializable) arrayList);
                x.putExtra("BUNDLE", args);
                x.putExtra("isMapBack", 1);
                startActivity(x);
                finish();
            }
            //  ***Experience Start***

            //  ***Experience End***
            else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("RequestAccept")) {

                System.out.println("BLODCED  " + blockedDates);

                Intent x = new Intent(getApplicationContext(), RequestActivity.class);
                x.putExtra("location", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENADDRESS));
                x.putExtra("roomname", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENROOMNAME));
                x.putExtra("bedroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBEDROOM));
                x.putExtra("bathroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBATHROOM));
                x.putExtra("blockdate", blockedDates);
                x.putExtra("ReqBack", 1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Contact_host")) {

                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                Intent x = new Intent(getApplicationContext(), SpaceAvailableActivity.class);
                x.putExtra(Constants.SpaceResults, spaceResult());
                x.putExtra(isContactHost, true);
                x.putExtra("ContactBack", 1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Check_availability")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString(Constants.SpaceResults, "");
                Type type = new TypeToken<SpaceResult>() {
                }.getType();
                SpaceResult spaceResult = gson.fromJson(json, type);

                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                Intent x = new Intent(getApplicationContext(), SpaceAvailableActivity.class);
                x.putExtra(Constants.SpaceResults, spaceResult);
                x.putExtra(isContactHost, false);
                x.putExtra("ContactBack", 2);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");

            }
            //  ***Experience Start***


            //  ***Experience End***

            else {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
        } else {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }
    }

    private SpaceResult spaceResult() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.SpaceResults, "");
        Type type = new TypeToken<SpaceResult>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    // check network connection available or not
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
