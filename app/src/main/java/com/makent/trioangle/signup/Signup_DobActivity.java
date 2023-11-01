package com.makent.trioangle.signup;

/**
 * @package com.makent.trioangle
 * @subpackage signup
 * @category SignupDobActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;

// ***Experience Start***



// ***Experience End***
import com.makent.trioangle.model.ExploreExpModel;

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
import com.makent.trioangle.travelling.ContactHostActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.MapActivity;
import com.makent.trioangle.travelling.RequestActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.isContactHost;


/* *******************************************************************************
                    Date of Birth
One part of registeration to get Date of Birth from user and the register the user
********************************************************************************** */
public class Signup_DobActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    ImageView dob_back;  // back action move Date of Birth page to Password page
    ImageView dob_next; // Valitate the date and register the user and move to Home Page
    EditText dob; // Get Date of Birth form user
    String dobs, dobss, ip; // Store Date of Birth as String
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    protected boolean isInternetAvailable;  // check network is available or not
    DatePickerDialog datePickerDialog;
    // ***Experience Start***
    private  String getPreferedlanguageCode;
    // ***Experience End***
    LocalSharedPreferences localSharedPreferences;  //  local shared preference to store and get data from local
    public Handler handler;
    Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__dob); // Layout design Name
        commonMethods = new CommonMethods();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences = new LocalSharedPreferences(this);

        // Assign Design Id to Java variable
        dob_back = (ImageView) findViewById(R.id.dob_back);
        commonMethods.rotateArrow(dob_back,this);
        dob_next = (ImageView) findViewById(R.id.dob_next);
        commonMethods.rotateArrow(dob_next,this);
        dob = (EditText) findViewById(R.id.dob);
        getPreferedlanguageCode =localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        getLocalIpAddress();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // showDate(year, month+1, day);

        // On Click function used to click action
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                setDate();
            }
        });


        // On Click function used to click action
        dob_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    dobs = dob.getText().toString();
                    localSharedPreferences.saveSharedPreferences(Constants.DOB, dobss);

                    handler = new Handler();
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            dob_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                            signUp();   // Sign up API call
                            //  Intent x = new Intent(getApplicationContext(),HomeActivity.class);
                            //startActivity(x);
                        }
                    }; handler.postDelayed(myRunnable,2000);
                } else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, dob, dob, getResources(), Signup_DobActivity.this);
                }
            }
        });

        // On Click function used to click action
        dob_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                /*Intent x = new Intent(getApplicationContext(),Signup_PasswordActivity.class);
                startActivity(x);*/
                onBackPressed();
            }
        });

        dob_next.setAlpha(.5f);
        dob_next.setEnabled(false);
        dob_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

        dob.addTextChangedListener(new DOBTextWatcher(dob));
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        // Call Signup password Page while back key press
        if(myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        Intent x = new Intent(getApplicationContext(), Signup_PasswordActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(x, bndlanimation);
        finish();


    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        dob_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

    }

    //get Date from date picker
    @SuppressWarnings("deprecation")
    public void setDate() {
        /*Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);*/
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR) - 18; // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(Signup_DobActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String daySt = "" + dayOfMonth;
                        if (dayOfMonth < 10)
                            daySt = "0" + dayOfMonth;
                        String monthSt = "" + (monthOfYear + 1);
                        if ((monthOfYear + 1) < 10)
                            monthSt = "0" + (monthOfYear + 1);
                        dob.setText(monthSt + "/"
                                + daySt + "/" + year);
                        dobss = daySt + "-" + monthSt + "-" + year;
                    }
                }, mYear - 3, mMonth, mDay);

        //datePickerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.MONTH, mMonth);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        if (datePickerDialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            datePickerDialog.getWindow().setLayout(width, height);
        }

        datePickerDialog.setTitle(getResources().getString(R.string.setdob));
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancelc), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.okay), datePickerDialog);
//
//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }
        else
        {
            if(getResources().getString(R.string.layout_direction).equals("1")){
                datePickerDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }else {
                datePickerDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        }

        datePickerDialog.show();
    }

    // Create date picker dialog

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String daySt = "" + day;
        if (day < 10)
            daySt = "0" + day;
        String monthSt = "" + month;
        if (month < 10)
            monthSt = "0" + month;

//        if (!getPreferedlanguageCode.equalsIgnoreCase("en")) {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
//        }

        dob.setText(new StringBuilder().append(daySt).append("/")
                .append(monthSt).append("/").append(year));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessSignup(jsonResp); // onSuccessSignup call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {

            if(!this.isFinishing()){
                dob_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
                Glide.with(this).clear(dob_next);
                commonMethods.snackBar(getResources().getString(R.string.invalidelogin), "", false, 2, dob, dob, getResources(), this);
            }

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    private class DOBTextWatcher implements TextWatcher {

        private View view;

        private DOBTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.dob:
                    validateDOB(dob);
                    break;

            }

            if (validateDOB(dob)) {
                dob_next.setAlpha(1f);
                dob_next.setEnabled(true);
            } else {
                dob_next.setAlpha(.5f);
                dob_next.setEnabled(false);
            }
        }
    }

    public boolean validateDOB(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().trim().length() <= 0) {
            return false;
        } else if (edt.getText().toString().equals("   /   /")) {
            return false;
        } else {
        }
        return true;
    }

    public void onSuccessSignup(JsonResponse jsonResponse) {

        if(myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        localSharedPreferences.saveSharedPreferences(Constants.UserId, (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.UserId, int.class));
        localSharedPreferences.saveSharedPreferences(Constants.AccessToken, (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.AccessToken, String.class));

        String currencysymbol=(String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.CurrencySymbolApi, String.class);
        String cs= Html.fromHtml(currencysymbol).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, cs);


        //localSharedPreferences.saveSharedPreferences(Constants.UserDOB, (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.UserDOB, String.class));
        System.out.println("USER DOB " + localSharedPreferences.getSharedPreferences(Constants.UserDOB));
        //  Toast.makeText(getApplicationContext(), "Updated Successfully ", Toast.LENGTH_LONG).show();
        //System.out.println("Amount data will be cleared");
        String currencySymbol = Html.fromHtml((String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "currency_symbol", String.class)).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencySymbol);
        localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "currency_code", String.class));
        commonMethods.NotrotateArrow(dob_next,this);
        dob_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
        Glide.with(this).clear(dob_next);
        onSuccessCreateAccount();
    }

    public void onSuccessCreateAccount() {
        System.out.println("USER DOB " + localSharedPreferences.getSharedPreferences(Constants.UserDOB));
        System.out.println("get Local VAlue of LastPage " + localSharedPreferences.getSharedPreferences(Constants.LastPage));
        if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null) {

            if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("inbox")
                    || localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("saved")) {
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
            }
            else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("ShowAll")) {
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
            // ***Experience Start***

            // ***Experience End***
            else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("RequestAccept")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString("blockdates", "");
                System.out.println("My Json Value " + json);
                Type type = new TypeToken<String[]>() {
                }.getType();
                String[] blocked = gson.fromJson(json, type);
                System.out.println("My Json Value2 " + Arrays.toString(blocked));

                Intent x = new Intent(getApplicationContext(), RequestActivity.class);
                x.putExtra("location", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENADDRESS));
                x.putExtra("roomname", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENROOMNAME));
                x.putExtra("bedroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBEDROOM));
                x.putExtra("bathroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBATHROOM));
                x.putExtra("blockdate", blocked);
                x.putExtra("ReqBack", 1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
            } else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Contact_host")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString(Constants.SpaceResults, "");
                Type type = new TypeToken<SpaceResult>() {}.getType();
                SpaceResult spaceResult = gson.fromJson(json, type);

                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                Intent x = new Intent(getApplicationContext(), SpaceAvailableActivity.class);
                x.putExtra(Constants.SpaceResults, spaceResult);
                x.putExtra(isContactHost, true);
                x.putExtra("ContactBack", 1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
            }else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Check_availability")) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = sharedPrefs.getString(Constants.SpaceResults, "");
                Type type = new TypeToken<SpaceResult>() {}.getType();
                SpaceResult spaceResult = gson.fromJson(json, type);

                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                Intent x = new Intent(getApplicationContext(), SpaceAvailableActivity.class);
                x.putExtra(Constants.SpaceResults, spaceResult);
                x.putExtra(isContactHost, false);
                x.putExtra("ContactBack", 2);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
                localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");

            }
            // ***Experience Start***

            // ***Experience End***
            else {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
        } else {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }

    }

    // Sign up API call
    public void signUp() {
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(dob_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        TimeZone tz = TimeZone.getDefault();
        apiService.signup(localSharedPreferences.getSharedPreferences(Constants.FirstName), localSharedPreferences.getSharedPreferences(Constants.LastName), localSharedPreferences.getSharedPreferences(Constants.Email)
                , localSharedPreferences.getSharedPreferences(Constants.Password)
                    , localSharedPreferences.getSharedPreferences(Constants.DOB),"email","",tz.getID(),localSharedPreferences.getSharedPreferences(Constants.LanguageCode)).enqueue(new RequestCallback(this));
    }


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("Siva", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Siva", ex.toString());
        }
        return null;
    }

    // Check Network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}

