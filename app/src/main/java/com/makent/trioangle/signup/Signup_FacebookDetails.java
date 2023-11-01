package com.makent.trioangle.signup;

/**
 * @package com.makent.trioangle
 * @subpackage signup
 * @category Signup_FacebookDetails  signup using Facebook mobile number without email address
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
//  ***Experience Start***

//  ***Experience End***
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
import com.makent.trioangle.util.RequestCallback;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.isContactHost;
import static com.makent.trioangle.util.Enums.REQ_EMAIL_VALID;
import static com.makent.trioangle.util.Enums.REQ_FBSIGNUP;

public class Signup_FacebookDetails extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    ImageView facebook_back; // Back to Email Page
    ImageView facebook_next; // Validate facebook and move to DOB page
    EditText facebook_firstname, facebook_lastname, facebook_dob, facebook_email;
  //  TextView facebook_email;
    String fbFirstName, fbLastName, fbDOB, email, dobs;
    boolean fname, lname, bemail, bdob; // Check first and last name is valid or not

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    DatePickerDialog datePickerDialog;
    //  ***Experience Start***
    //  ***Experience End***

    LocalSharedPreferences localSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_facebook_details);
        commonMethods = new CommonMethods();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        localSharedPreferences = new LocalSharedPreferences(this);
        // ***Experience Start***
        // ***Experience End***
        //  Toast.makeText(Signup_FacebookDetails.this,"FaceBookActivity",Toast.LENGTH_SHORT).show();
        // Assign Design Id to Java variable
        facebook_back = (ImageView) findViewById(R.id.facebook_back);
        commonMethods.rotateArrow(facebook_back,this);
        facebook_next = (ImageView) findViewById(R.id.facebook_next);

        facebook_firstname = (EditText) findViewById(R.id.facebook_firstname);
        facebook_lastname = (EditText) findViewById(R.id.facebook_lastname);
        facebook_dob = (EditText) findViewById(R.id.facebook_dob);
        facebook_email = (EditText) findViewById(R.id.facebook_email);

        loadFaceBookData();

        facebook_firstname.addTextChangedListener(new FacebookTextWatcher(facebook_firstname));
        facebook_lastname.addTextChangedListener(new FacebookTextWatcher(facebook_firstname));
        facebook_email.addTextChangedListener(new FacebookTextWatcher(facebook_email));
        facebook_dob.addTextChangedListener(new FacebookTextWatcher(facebook_dob));

        facebook_next.setEnabled(false);
        facebook_next.setAlpha(.5f);
        facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        commonMethods.rotateArrow(facebook_next,this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // showDate(year, month+1, day);

        // On Click function used to click action
        facebook_dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                setDate();
            }
        });

        // On Click function used to click action
        facebook_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                Log.e("Apple","Apple login submit");
                fbFirstName = facebook_firstname.getText().toString();
                fbLastName = facebook_lastname.getText().toString();
                email = facebook_email.getText().toString();
                // dobs=facebook_dob.getText().toString();
                email = email.replaceAll(" ", "");

                localSharedPreferences.saveSharedPreferences(Constants.FirstName, fbFirstName);
                localSharedPreferences.saveSharedPreferences(Constants.LastName, fbLastName);
                localSharedPreferences.saveSharedPreferences(Constants.Email, email);
                localSharedPreferences.saveSharedPreferences(Constants.DOB, dobs);
                hideSoftKeyboard();

                facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));

                bemail = validateEmail();

                if (bemail)
                {
                    checkEmail();  // Call Signup API Call
                }
                else
                {
                    Toast.makeText(Signup_FacebookDetails.this,"Enter valid email",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On Click function used to click action
        facebook_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                onBackPressed();
            }
        });
    }

    // Call Main page while back press
    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        localSharedPreferences.saveSharedPreferences(Constants.FirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.LastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Email, null);
        localSharedPreferences.saveSharedPreferences(Constants.DOB, null);
        Intent x = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(x, bndlanimation);
        finish();
    }

    // Load facebook available data
    public void loadFaceBookData() {
        fbFirstName = localSharedPreferences.getSharedPreferences(Constants.FirstName);
        fbLastName = localSharedPreferences.getSharedPreferences(Constants.LastName);
        fbDOB = localSharedPreferences.getSharedPreferences(Constants.DOB);
        email = localSharedPreferences.getSharedPreferences(Constants.Email);

        if (fbFirstName != null)
            facebook_firstname.setText(fbFirstName);
        if (fbLastName != null)
            facebook_lastname.setText(fbLastName);
        if (fbDOB != null)
            facebook_dob.setText(fbDOB);
        if (email != null)
            facebook_email.setText(email);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_EMAIL_VALID:
                if (jsonResp.isSuccess()) {
                    fbSignup();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(getResources().getString(R.string.emailalreadyexits), getResources().getString(R.string.login_title), true, 2,  facebook_email, getResources(), Signup_FacebookDetails.this);
                }
                break;
            case REQ_FBSIGNUP:
                if (jsonResp.isSuccess()) {
                    localSharedPreferences.saveSharedPreferences(Constants.UserId, (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.UserId, int.class));
                    localSharedPreferences.saveSharedPreferences(Constants.AccessToken, (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.AccessToken, String.class));
                    commonMethods.NotrotateArrow(facebook_next,this);
                    String currencySymbol = Html.fromHtml((String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "currency_symbol", String.class)).toString();
                    localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencySymbol);
                    localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "currency_code", String.class));
                    facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
                    Glide.with(this).clear(facebook_next);
                    onSuccessFbLogin();

                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
                    Glide.with(this).clear(facebook_next);
                    commonMethods.snackBar(getResources().getString(R.string.invalidesignup), "", false, 2,  facebook_email, getResources(), this);
                }
                break;
            default:
                break;

        }
    }

    private void onSuccessFbLogin() {
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
                Type type = new TypeToken<String[]>() {
                }.getType();
                String[] blocked = gson.fromJson(json, type);

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
            }
            // ***Experience Start***

            // ***Experience End***

            else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("Contact_host")) {
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
            }
        } else {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
//            if (localSharedPreferences.getSharedPreferences(Constants.UserDOB)!=null || !localSharedPreferences.getSharedPreferences(Constants.UserDOB).equals("null") || !localSharedPreferences.getSharedPreferences(Constants.UserDOB).isEmpty() || !localSharedPreferences.getSharedPreferences(Constants.UserDOB).equals(""))
//            {
//                Log.e("DOB Empty","DOB NOT NULL");
//                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                startActivity(x, bndlanimation);
//            }
//            else
//            {
//                Log.e("DOB Empty","DOB NULL");
//                Intent x = new Intent(getApplicationContext(), Signup_DobActivity.class);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                startActivity(x, bndlanimation);
//            }

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        Glide.with(this).clear(facebook_next);
        commonMethods.snackBar(getResources().getString(R.string.emailalreadyexits), getResources().getString(R.string.login_title), true, 2,  facebook_email, getResources(), Signup_FacebookDetails.this);
    }

    // EditText validation class
    private class FacebookTextWatcher implements TextWatcher {

        private View view;

        private FacebookTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.facebook_firstname:
                    fname = validateName(facebook_firstname);
                    break;
                case R.id.facebook_lastname:
                    lname = validateName(facebook_lastname);
                    break;
                case R.id.facebook_email:
//                    fname = validateName(facebook_firstname);
//                    lname = validateName(facebook_lastname);
//                    bemail = validateEmail();
//                    bdob = validateDOB(facebook_dob);
                    break;
                case R.id.facebook_dob:
                    fname = validateName(facebook_firstname);
                    lname = validateName(facebook_lastname);
                    bdob = validateDOB(facebook_dob);
                    break;
            }

//            if (fname && lname && bemail && bdob) {
//                facebook_next.setEnabled(true);
//                facebook_next.setAlpha(1f);
//            }
            if (fname && lname && bdob)
            {
                Log.e("addTextchanged","addTextchanged completed");
                facebook_next.setEnabled(true);
                facebook_next.setAlpha(1f);
            }
            else
            {
                Log.e("addTextchanged","addTextchanged not completed");
                facebook_next.setEnabled(false);
                facebook_next.setAlpha(.5f);
            }
        }
    }

    private boolean validateName(EditText first_name) {
        if (first_name.getText().toString().trim().isEmpty()) {
            requestFocus(first_name);
            return false;
        } else {
        }

        return true;
    }

    private boolean validateEmail() {

        email = facebook_email.getText().toString().trim();
        email = email.replaceAll(" ", "");
        //facebook_email.setText(email);
        if (email.isEmpty() || !isValidEmail(email)) {

            return false;
        } else {
        }

        return true;
    }


    private static boolean isValidEmail(String email)
    {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validateDOB(EditText edt) throws NumberFormatException
    {
        if (edt.getText().toString().trim().length() <= 0)
        {
            return false;
        } else if (edt.getText().toString().equals("   /   /"))
        {
            return false;
        } else
        {

        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            if (facebook_dob.isFocused()) {
                setDate();
                hideSoftKeyboard();
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

    }

    // Set Date of Birth

    @SuppressWarnings("deprecation")
    public void setDate() {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR) - 18; // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(Signup_FacebookDetails.this, AlertDialog.THEME_HOLO_LIGHT,
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
                        facebook_dob.setText(monthSt + "/"
                                + daySt + "/" + year);
                        dobs = daySt + "-" + monthSt + "-" + year;
                    }
                }, mYear - 3, mMonth, mDay);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.MONTH, mMonth);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        datePickerDialog.setTitle(getResources().getString(R.string.setdob));
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancelc), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.okay), datePickerDialog);
        datePickerDialog.show();
    }


    // Create datepicker daialog
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
        facebook_dob.setText(new StringBuilder().append(daySt).append("/")
                .append(monthSt).append("/").append(year));

    }

    public void fbSignup()
    {
        facebook_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
        Glide.with(this).clear(facebook_next);
        TimeZone tz = TimeZone.getDefault();

        apiService.fbsignup(localSharedPreferences.getSharedPreferences(Constants.FirstName), localSharedPreferences.getSharedPreferences(Constants.LastName)
                , localSharedPreferences.getSharedPreferences(Constants.Email)
                , localSharedPreferences.getSharedPreferences(Constants.APPLEID),
                "facebook",
                localSharedPreferences.getSharedPreferences(Constants.FBID)
                , localSharedPreferences.getSharedPreferences(Constants.ProfilePicture)
                ,tz.getID()
        ,localSharedPreferences.getSharedPreferences(Constants.DOB)).enqueue(new RequestCallback(REQ_FBSIGNUP, Signup_FacebookDetails.this));
    }

    // Check email id already exits or not using API call
    public void checkEmail()
    {
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(facebook_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        apiService.emailvalidation(email,langCode,"apple","").enqueue(new RequestCallback(REQ_EMAIL_VALID, Signup_FacebookDetails.this));
    }

// Call network error or exception

    // Hide soft keyboard
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
