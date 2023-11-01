package com.makent.trioangle.profile;

/**
 * @package com.makent.trioangle
 * @subpackage Profile
 * @category EditProfileActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.BuildConfig;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.GenderAdapter;
import com.makent.trioangle.backgroundtask.ImageCompressAsyncTask;
import com.makent.trioangle.backgroundtask.ImageMinimumSizeCalculator;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ImageListener;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.profile.edit.EditAboutme;
import com.makent.trioangle.profile.edit.EditEmail;
import com.makent.trioangle.profile.edit.EditLocation;
import com.makent.trioangle.profile.edit.EditName;
import com.makent.trioangle.profile.edit.EditPhone;
import com.makent.trioangle.profile.edit.EditSchool;
import com.makent.trioangle.profile.edit.EditWork;
import com.makent.trioangle.profile.edit.GenreConverter;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import okhttp3.RequestBody;

import static com.makent.trioangle.helper.Constants.GenreName;
import static com.makent.trioangle.util.Enums.REQ_PROFILE;
import static com.makent.trioangle.util.Enums.REQ_SAVEPROFILE;
import static com.makent.trioangle.util.Enums.REQ_UPLOAD_PROFILE_IMG;

/* ************************************************************
                  Edit profile Page
User call edit all details and verification
*************************************************************** */
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener, ImageListener,GenreConverter.onSuccessLanguageChangelistner {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public @Inject
    RunTimePermission runTimePermission;

    private LinearLayout editprofile_editusername;
    private LinearLayout editprofile_editaboutme;
    private RelativeLayout editprofile_gender;
    private RelativeLayout editprofile_dob;
    private RelativeLayout editprofile_email;
    private RelativeLayout editprofile_phone;
    private RelativeLayout editprofile_location;
    private RelativeLayout editprofile_school;
    private RelativeLayout editprofile_work;
    private RelativeLayout editprofile_languages;

    private ImageView editprofile_img;
    private ImageView editprofile_camera;
    private ImageView editprofile_back;
    private ImageView gender_img;
    private ImageView dob_img;
    private ImageView email_img;
    private ImageView phone_img;
    private ImageView location_img;
    private ImageView school_img;
    private ImageView work_img;

    private TextView dob_txt;
    private TextView gender_txt;
    private TextView email_txt;
    private TextView phone_txt;
    private TextView location_txt;
    private TextView school_txt;
    private TextView work_txt;
    private TextView editprofile_save;
    private TextView editprofile_username;
    private TextView editprofile_aboutme;
    private GenreConverter genreConverter;

    boolean imageupload = false;
    protected boolean isInternetAvailable;
    int j;

    String dobs; // Store Date of Birth as String
    private DatePicker datePicker;//                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
//
//                    Date date = null;
//                    try {
//                        date = sdf.parse(dob);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//                    System.out.println(sdf.format(date));
//                    dob = sdf.format(date).toString();
//
//                    localSharedPreferences.saveSharedPreferences(Constants.DOB, dob);
//                    dob_txt.setText(dob);
//                    dob_txt.setVisibility(View.VISIBLE);
//                    dob_img.setVisibility(View.GONE);
    private Calendar calendar;
    private int year, month, day;
    DatePickerDialog datePickerDialog;

    RecyclerView recyclerView1;
    List<Makent_model> makentModels;
    GenderAdapter genderAdapter;
    public static android.app.AlertDialog alertDialogStores;
    AlertDialog.Builder ad;
    LinearLayout.LayoutParams layoutparams;
    String[] gendertype;
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;
    private String firstname;
    private String lastname;
    private String user_thumb_image;
    private String user_location;
    private String about_me;
    private String dob;
    private String school;
    private String gender;
    private String email;
    private String phone_number;
    private String work;
    String userid, UserDetails;

    private static final int RESULT_LOAD_IMAGE = 3;
    private static final int CAMERA_REQUEST = 1;


    private File imageFile = null;
    private Uri imageUri;
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        Log.e("EditProfileActivity","EditProfileActivity");

        gendertype= new String[]{getResources().getString(R.string.gender_male), getResources().getString(R.string.gender_female), getResources().getString(R.string.gender_other)};
        // Network exception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        localSharedPreferences = new LocalSharedPreferences(this);

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        UserDetails = localSharedPreferences.getSharedPreferences(Constants.UserDetails);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        dob_txt = (TextView) findViewById(R.id.dob_txt);
        gender_txt = (TextView) findViewById(R.id.gender_txt1);
        email_txt = (TextView) findViewById(R.id.email_txt);
        phone_txt = (TextView) findViewById(R.id.phone_txt);
        location_txt = (TextView) findViewById(R.id.location_txt);
        school_txt = (TextView) findViewById(R.id.school_txt);
        work_txt = (TextView) findViewById(R.id.work_txt);
        editprofile_username = (TextView) findViewById(R.id.editprofile_username);
        editprofile_aboutme = (TextView) findViewById(R.id.editprofile_aboutme);


        gender_img = (ImageView) findViewById(R.id.gender_img);
        dob_img = (ImageView) findViewById(R.id.dob_img);
        email_img = (ImageView) findViewById(R.id.email_img);
        phone_img = (ImageView) findViewById(R.id.phone_img);
        location_img = (ImageView) findViewById(R.id.location_img);
        school_img = (ImageView) findViewById(R.id.school_img);
        work_img = (ImageView) findViewById(R.id.work_img);

        editprofile_img = (ImageView) findViewById(R.id.editprofile_img);
        editprofile_camera = (ImageView) findViewById(R.id.editprofile_camera);
        editprofile_back = (ImageView) findViewById(R.id.editprofile_back);
        editprofile_save = (TextView) findViewById(R.id.editprofile_save);

        editprofile_editusername = (LinearLayout) findViewById(R.id.editprofile_editusername);
        editprofile_editaboutme = (LinearLayout) findViewById(R.id.editprofile_editaboutme);

        editprofile_gender = (RelativeLayout) findViewById(R.id.editprofile_gender);
        editprofile_dob = (RelativeLayout) findViewById(R.id.editprofile_dob);
        editprofile_email = (RelativeLayout) findViewById(R.id.editprofile_email);
        editprofile_phone = (RelativeLayout) findViewById(R.id.editprofile_phone);
        editprofile_location = (RelativeLayout) findViewById(R.id.editprofile_location);
        editprofile_school = (RelativeLayout) findViewById(R.id.editprofile_school);
        editprofile_work = (RelativeLayout) findViewById(R.id.editprofile_work);
        editprofile_languages = (RelativeLayout) findViewById(R.id.editprofile_languages);

        editprofile_gender.setOnClickListener(this);
        editprofile_dob.setOnClickListener(this);
        editprofile_email.setOnClickListener(this);
        editprofile_phone.setOnClickListener(this);
        editprofile_location.setOnClickListener(this);
        editprofile_school.setOnClickListener(this);
        editprofile_work.setOnClickListener(this);
        editprofile_languages.setOnClickListener(this);

        editprofile_editusername.setOnClickListener(this);
        editprofile_editaboutme.setOnClickListener(this);

        editprofile_img.setOnClickListener(this);
        editprofile_camera.setOnClickListener(this);
        editprofile_back.setOnClickListener(this);
        editprofile_save.setOnClickListener(this);
        commonMethods.rotateArrow(editprofile_back,this);
        commonMethods.rotateArrow(gender_img,this);
        commonMethods.rotateArrow(location_img,this);
        commonMethods.rotateArrow(school_img,this);
        commonMethods.rotateArrow(work_img,this);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (UserDetails == null) {
            mydialog.show();
            // Get user details from API
            getUserProfile();
        } else {// Get user details from local
            getUserDetails(UserDetails);
        }

        System.out.println("Language value " + localSharedPreferences.getSharedPreferences(Constants.LanguageCode));

//        if (gender == null) {
//            localSharedPreferences.getSharedPreferences(GenreName);
//            gender_txt.setVisibility(View.GONE);
//            gender_img.setVisibility(View.VISIBLE);
//        } else {
//            localSharedPreferences.getSharedPreferences(GenreName);
//            gender_txt.setText(gender);
//            gender_txt.setVisibility(View.VISIBLE);
//            gender_img.setVisibility(View.GONE);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        String firstname, lastname, username, user_location, about_me, dob, school, gender, email, phone_number, work;

        firstname = localSharedPreferences.getSharedPreferences(Constants.FirstName);
        lastname = localSharedPreferences.getSharedPreferences(Constants.LastName);
        username = firstname + " " + lastname;
        user_location = localSharedPreferences.getSharedPreferences(Constants.Location);

        about_me = localSharedPreferences.getSharedPreferences(Constants.AboutMe);
        dob = localSharedPreferences.getSharedPreferences(Constants.DOB);
        school = localSharedPreferences.getSharedPreferences(Constants.School);
        gender = localSharedPreferences.getSharedPreferences(GenreName);
        email = localSharedPreferences.getSharedPreferences(Constants.Email);
        phone_number = localSharedPreferences.getSharedPreferences(Constants.Phone);
        work = localSharedPreferences.getSharedPreferences(Constants.Work);
        gender_txt.setText(gender);

        editprofile_username.setText(username);
        editprofile_aboutme.setText(about_me);

        localSharedPreferences.saveSharedPreferences(Constants.FirstName, firstname);
        localSharedPreferences.saveSharedPreferences(Constants.LastName, lastname);
        if (about_me != null) {
            editprofile_aboutme.setText(about_me);
        } else {
            editprofile_aboutme.setText("");
        }

        if (work == null) {
            localSharedPreferences.saveSharedPreferences(Constants.Work, null);
            work_txt.setVisibility(View.GONE);
            work_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.Work, work);
            work_txt.setText(work);
            work_txt.setVisibility(View.VISIBLE);
            work_img.setVisibility(View.GONE);
        }

        if (school == null) {
            localSharedPreferences.saveSharedPreferences(Constants.School, null);
            school_txt.setVisibility(View.GONE);
            school_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.School, school);
            school_txt.setText(school);
            school_txt.setVisibility(View.VISIBLE);
            school_img.setVisibility(View.GONE);
        }


        if (user_location == null) {
            localSharedPreferences.saveSharedPreferences(Constants.Location, null);
            location_txt.setVisibility(View.GONE);
            location_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.Location, user_location);
            location_txt.setText(user_location);
            location_txt.setVisibility(View.VISIBLE);
            location_img.setVisibility(View.GONE);
        }

        if (phone_number == null) {
            localSharedPreferences.saveSharedPreferences(Constants.Phone, null);
            phone_txt.setVisibility(View.GONE);
            phone_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.Phone, phone_number);
            phone_txt.setText(phone_number);
            phone_txt.setVisibility(View.VISIBLE);
            phone_img.setVisibility(View.GONE);
        }

        if (email == null) {
            localSharedPreferences.saveSharedPreferences(Constants.Email, null);
            email_txt.setVisibility(View.GONE);
            email_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.Email, email);
            email_txt.setText(email);
            email_txt.setVisibility(View.VISIBLE);
            email_img.setVisibility(View.GONE);
        }

        if (gender == null) {
            localSharedPreferences.saveSharedPreferences(GenreName, null);
            gender_txt.setVisibility(View.GONE);
            gender_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(GenreName, gender);
            gender_txt.setText(gender);
            gender_txt.setVisibility(View.VISIBLE);
            gender_img.setVisibility(View.GONE);
        }

        if (dob == null) {
            localSharedPreferences.saveSharedPreferences(Constants.DOB, null);
            dob_txt.setVisibility(View.GONE);
            dob_img.setVisibility(View.VISIBLE);
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.DOB, dob);
            dob_txt.setText(dob);
            dob_txt.setVisibility(View.VISIBLE);
            dob_img.setVisibility(View.GONE);
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editprofile_back: {
                onBackPressed();
            }
            break;
            case R.id.editprofile_save: {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    // Update user data
                    updateUserProfile();
                } else {
                    commonMethods.snackBar(getResources().getString(R.string.internal_server_error), "", false, 2, dob_img, getResources(), this);
                }

                // onBackPressed();
            }
            break;

            case R.id.editprofile_img:
            case R.id.editprofile_camera: {  // Show camera and gallery dialog
                //cameraGallery();
                String[] permissions;
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
                    permissions= new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                }else{
                    permissions= new String[]{Manifest.permission.READ_MEDIA_IMAGES,  Manifest.permission.CAMERA};

                }
                checkAllPermission(permissions);

            }
            break;

            case R.id.editprofile_editusername: {
                // Call edit name page
                Intent x = new Intent(getApplicationContext(), EditName.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_editaboutme: {
                // Call edit About me page
                Intent x = new Intent(getApplicationContext(), EditAboutme.class);
                startActivity(x);
            }
            break;

            case R.id.editprofile_gender: {
                // Show Gender dialog
                //genderDialog();

                String[] languages = getResources().getStringArray(R.array.gender);
                genreConverter = new GenreConverter(EditProfileActivity.this, true, languages, EditProfileActivity.this);
            }
            break;
            case R.id.editprofile_dob: {
                // Show date picker for select
                setDate();
            }
            break;
            case R.id.editprofile_email: {
                // Call edit email address
                Intent x = new Intent(getApplicationContext(), EditEmail.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_phone: {
                // Call edit phone number page
                Intent x = new Intent(getApplicationContext(), EditPhone.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_location: {
                // Call edit user location page
                Intent x = new Intent(getApplicationContext(), EditLocation.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_school: {
                //Call Edit Scholl address page
                Intent x = new Intent(getApplicationContext(), EditSchool.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_work: {
                //Call edit work page
                Intent x = new Intent(getApplicationContext(), EditWork.class);
                startActivity(x);
            }
            break;
            case R.id.editprofile_languages: {
                //Call edit name page
                Intent x = new Intent(getApplicationContext(), EditName.class);
                startActivity(x);
            }
            break;
        }
    }


    // Load gender list
    private void loadgenderlist(int index) {

        if (gendertype.length > 0) {

            for (int i = 0; i < gendertype.length; i++) {
                Makent_model listdata = new Makent_model();
                listdata.setGenderType(gendertype[i]);
                makentModels.add(listdata);
            }
            genderAdapter.notifyDataChanged();
        }
    }

    // Show gender dialog
    public void genderDialog() {

        String gender = gender_txt.getText().toString();
        for (int i = 0; i < gendertype.length; i++) {
            if (gendertype[i].equals(gender)) {
                j = i;
                break;
            } else {
                j = -1;
            }
        }
        ad = new AlertDialog.Builder(this);
        AlertDialog alertDialog = ad.create();
        if(getResources().getString(R.string.layout_direction).equals("1")){
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
//        TextView title = new TextView(this);
//        title.setText(getResources().getString(R.string.gender));
//        title.setTextSize(16);
//        title.setPadding(0,10,10,0);
        ad.setTitle(getResources().getString(R.string.gender));
//        ad.setCustomTitle(title);
        ad.setCancelable(true);
//        if(getResources().getString(R.string.layout_direction).equals("1")){
//            title.setGravity(Gravity.RIGHT);
//        }
//        else{
//            title.setGravity(Gravity.LEFT);
//        }

        if(getResources().getString(R.string.layout_direction).equals("1")){
            gender_txt.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            gender_txt.setGravity(Gravity.END);
        }else{
            gender_txt.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            gender_txt.setGravity(Gravity.START);
        }
        ad.setSingleChoiceItems(new ArrayAdapter<String>(this, R.layout.alert_dialog,
                R.id.currencyname_txt, gendertype),
                j,  new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

               // RadioButton radioButton = (RadioButton)findViewById(R.id.radioButton1);

               /* Toast.makeText(getApplicationContext(),
                        "You Choose : " + myList[arg1],
                        Toast.LENGTH_LONG).show();*/
                gender_txt.setVisibility(View.VISIBLE);
                gender_txt.setText(gendertype[arg1]);
                //radioButton.setChecked(true);


                localSharedPreferences.saveSharedPreferences(Constants.Gender, gendertype[arg1]);
                gender_img.setVisibility(View.GONE);
                arg0.dismiss();
            }
        });
        ad.show();
    }

    // Show datepicker for get Date of birth
    @SuppressWarnings("deprecation")
    public void setDate() {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR) - 18; // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        Log.e("ResponseDate","ResponseDate"+dob_txt.getText().toString().trim());

        datePickerDialog = new DatePickerDialog(EditProfileActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,

                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String daySt = "" + dayOfMonth;
                        if (dayOfMonth < 10) daySt = "0" + dayOfMonth;
                        String monthSt = "" + (monthOfYear + 1);
                        if ((monthOfYear + 1) < 10) monthSt = "0" + (monthOfYear + 1);
                       // dob_txt.setText(daySt + "/" + monthSt + "/" + year);
                        String dobtext = daySt + "-" + monthSt + "-" + year;
                        Log.e("DateofBirth","DOB"+dobtext);

                        Locale locale = new Locale( "ar" , "SA" );
                        String convertdobtext ="";
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                        Date date = null;
                        try {
                            date = sdf.parse(dobtext);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.e("Language","LanguageSelections"+localSharedPreferences.getSharedPreferences(Constants.LanguageCode));
                        if (localSharedPreferences.getSharedPreferences(Constants.LanguageCode).equals("ar"))
                        {
                            sdf = new SimpleDateFormat("dd-MM-yyyy", locale);
                        }
                        else
                        {
                            sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        }

                        System.out.println(sdf.format(date));
                        convertdobtext = sdf.format(date).toString();
                        dob_txt.setText(convertdobtext);
                        localSharedPreferences.saveSharedPreferences(Constants.DOB, dobtext);
                    }
                }, mYear - 3, mMonth, mDay);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.MONTH, mMonth);
        String[] separated = dob_txt.getText().toString().trim().split("-");

        Log.e("Seperated Date","Sep Date"+separated[0]+ "Month"+ separated[1]+ "Year"+separated[2]);

        datePickerDialog.updateDate(Integer.parseInt(separated[2]), Integer.parseInt(separated[1])-1, Integer.parseInt(separated[0]));

        Log.e("Language","LanguageSelections"+localSharedPreferences.getSharedPreferences(Constants.LanguageCode));
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        Log.e("Max Date","Max Date"+cal.getTimeInMillis());

        datePickerDialog.setTitle(getResources().getString(R.string.setdob));

        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancelc), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.okay), datePickerDialog);

//        datePickerDialog.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        datePickerDialog.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        String daySt = "" + day;
        if (day < 10) daySt = "0" + day;
        String monthSt = "" + month;
        if (month < 10) monthSt = "0" + month;
       // dob_txt.setText(new StringBuilder().append(daySt).append("/").append(monthSt).append("/").append(year));
        String dobtext = new StringBuilder().append(daySt).append("-").append(monthSt).append("-").append(year).toString();

        localSharedPreferences.saveSharedPreferences(Constants.DOB, dobtext);
    }

    // Get user details from API
    public void getUserDetails(String UserDetails) {
        try {
            JSONObject user_jsonobj = new JSONObject(UserDetails);
            for (int i = 0; i < user_jsonobj.length(); i++) {

                Log.e("GetUserDetails","UserDetails");
                String user_id, firstname, lastname, username, user_thumb_image, user_location, member_from, about_me, dob, school, gender, email, phone_number, work, is_email_connect, is_facebook_connect, is_google_connect, is_linkedin_connect;

                firstname = user_jsonobj.getString("first_name");
                lastname = user_jsonobj.getString("last_name");
                username = user_jsonobj.getString("first_name") + " " + user_jsonobj.getString("last_name");
                user_thumb_image = user_jsonobj.getString("large_image_url");
                //user_thumb_image = user_jsonobj.getString("small_image_url");
                //user_thumb_image = user_jsonobj.getString("large_image_url");
                localSharedPreferences.saveSharedPreferences(Constants.ProfilePicture, user_thumb_image);
                user_location = user_jsonobj.getString("user_location");

                about_me = user_jsonobj.getString("about_me");
                dob = user_jsonobj.getString("dob");
                school = user_jsonobj.getString("school");
                gender = user_jsonobj.getString("gender");
                email = user_jsonobj.getString("email");
                phone_number = user_jsonobj.getString("phone_number");
                work = user_jsonobj.getString("work");


                username = username.replaceAll("%20", "");
                username = username.replaceAll("20", "");

                firstname = firstname.replaceAll("%20", "");
                firstname = firstname.replaceAll("20", "");

                lastname = lastname.replaceAll("%20", "");
                lastname = lastname.replaceAll("20", "");

                editprofile_username.setText(username);
                editprofile_aboutme.setText(about_me);

                localSharedPreferences.saveSharedPreferences(Constants.FirstName, firstname);
                localSharedPreferences.saveSharedPreferences(Constants.LastName, lastname);
                if (about_me.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.AboutMe, null);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.AboutMe, about_me);
                }

                if (work.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.Work, null);
                    work_txt.setVisibility(View.GONE);
                    work_img.setVisibility(View.VISIBLE);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.Work, work);
                    work_txt.setText(work);
                    work_txt.setVisibility(View.VISIBLE);
                    work_img.setVisibility(View.GONE);
                }

                if (school.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.School, null);
                    school_txt.setVisibility(View.GONE);
                    school_img.setVisibility(View.VISIBLE);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.School, school);
                    school_txt.setText(school);
                    school_txt.setVisibility(View.VISIBLE);
                    school_img.setVisibility(View.GONE);
                }


                if (user_location.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.Location, null);
                    location_txt.setVisibility(View.GONE);
                    location_img.setVisibility(View.VISIBLE);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.Location, user_location);
                    location_txt.setText(user_location);
                    location_txt.setVisibility(View.VISIBLE);
                    location_img.setVisibility(View.GONE);
                }

                if (phone_number.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone, null);
                    phone_txt.setVisibility(View.GONE);
                    phone_img.setVisibility(View.VISIBLE);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone, phone_number);
                    phone_txt.setText(phone_number);
                    phone_txt.setVisibility(View.VISIBLE);
                    phone_img.setVisibility(View.GONE);
                }

                if (email.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.Email, null);
                    email_txt.setVisibility(View.GONE);
                    email_img.setVisibility(View.VISIBLE);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.Email, email);
                    email_txt.setText(email);
                    email_txt.setVisibility(View.VISIBLE);
                    email_img.setVisibility(View.GONE);
                }

                if (gender.equals("")) {
                    localSharedPreferences.saveSharedPreferences(GenreName, null);
                    gender_txt.setVisibility(View.GONE);
                    gender_img.setVisibility(View.VISIBLE);
                } else {
                    String Updategender="";
                    if (gender.equalsIgnoreCase("Male")){
                        Updategender=getResources().getString(R.string.gender_male);
                    }else if (gender.equalsIgnoreCase("Female")){
                        Updategender=getResources().getString(R.string.gender_female);
                    }else if (gender.equalsIgnoreCase("Other")){
                        Updategender=getResources().getString(R.string.gender_other);
                    }
                    localSharedPreferences.saveSharedPreferences(GenreName, Updategender);
                    gender_txt.setText(gender);
                    gender_txt.setVisibility(View.VISIBLE);
                    gender_img.setVisibility(View.GONE);
                }

                if (dob.equals("")) {
                    localSharedPreferences.saveSharedPreferences(Constants.DOB, null);
                    dob_txt.setVisibility(View.GONE);
                    dob_img.setVisibility(View.VISIBLE);
                } else {
                    Locale locale = new Locale( "ar" , "SA" ) ;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

                    Date date = null;
                    try {
                        date = sdf.parse(dob);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.e("Language","LanguageSelections"+localSharedPreferences.getSharedPreferences(Constants.LanguageCode));
                    if (localSharedPreferences.getSharedPreferences(Constants.LanguageCode).equals("ar"))
                    {
                        sdf = new SimpleDateFormat("dd-MM-yyyy", locale);
                    }
                    else
                    {
                        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    }

                    System.out.println(sdf.format(date));
                    dob = sdf.format(date).toString();

                    localSharedPreferences.saveSharedPreferences(Constants.DOB, dob);
                    dob_txt.setText(dob);
                    dob_txt.setVisibility(View.VISIBLE);
                    dob_img.setVisibility(View.GONE);
                }

                editprofile_img.setBackgroundColor(getResources().getColor(R.color.title_text_color));

//                Glide.with(getApplicationContext()).load(user_thumb_image)
//
//                        .into(new DrawableImageViewTarget(editprofile_img) {
//                            @Override
//                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                super.onResourceReady(resource, transition);
//                            }
//                        });

//                Picasso
//                        .with(this)
//                        .load(user_thumb_image)
//                        .fit()// resizes the ivPhoto to these dimensions (in pixel). does not respect aspect ratio
//                        .into(editprofile_img);

                Glide.with(getApplicationContext())
                        .load(user_thumb_image)

                        .into(new DrawableImageViewTarget(editprofile_img) {
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                            }
                        });

                editprofile_img.setBackgroundColor(getResources().getColor(R.color.title_text_color));


               // Glide.with(getApplicationContext()).load(user_thumb_image).into(editprofile_img);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessProfile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                }
                break;
            case REQ_SAVEPROFILE:
                if (jsonResp.isSuccess()) {
                    localSharedPreferences.saveSharedPreferences(Constants.UserDetails, null);

                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    onBackPressed();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, dob_img, getResources(), this);
                }
                break;
            case REQ_UPLOAD_PROFILE_IMG:
                System.out.println("JSON REsponse " + jsonResp.getStrResponse());
                if (jsonResp.isSuccess()) {
                    user_thumb_image = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "large_image_url", String.class);
                    System.out.println("user_thumb_image" + user_thumb_image);
                    localSharedPreferences.saveSharedPreferences(Constants.ProfilePicture, user_thumb_image);
                    loadImage(user_thumb_image);
                    if (user_thumb_image != null) {
                        imageupload = true;
                    } else {
                        imageupload = false;
                    }
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.hideProgressDialog();
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, dob_img, getResources(), this);
                }
                break;
            default:
                break;

        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public void onSuccessProfile(JsonResponse jsonResp) {
        try {
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONObject user_jsonobj = response.getJSONObject("user_details");
            localSharedPreferences.saveSharedPreferences(GenreName, gender_txt.getText().toString());
            localSharedPreferences.saveSharedPreferences(Constants.UserDetails, user_jsonobj.toString());
            getUserDetails(user_jsonobj.toString());
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        } catch (JSONException j) {
            j.printStackTrace();
        }
    }

    public void updateUserProfile() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        getdata();
        System.out.println("lastnamelastname" + lastname);

        String convertdobtext ="";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = sdf.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(sdf.format(date));
        dob = sdf.format(date).toString();
        String Updategender="";
        if (gender.equalsIgnoreCase(getResources().getString(R.string.gender_male))){
            Updategender="Male";
        }else if (gender.equalsIgnoreCase(getResources().getString(R.string.gender_female))){
            Updategender="Female";
        }else if (gender.equalsIgnoreCase(getResources().getString(R.string.gender_other))){
            Updategender="Other";
        }
        apiService.editProfile(localSharedPreferences.getSharedPreferences(Constants.AccessToken), firstname, lastname, user_thumb_image, dob, user_location, about_me, school, Updategender, email, phone_number, work).enqueue(new RequestCallback(REQ_SAVEPROFILE, this));
    }

    private void getdata() {

        firstname = localSharedPreferences.getSharedPreferences(Constants.FirstName);
        lastname = localSharedPreferences.getSharedPreferences(Constants.LastName);
        user_location = localSharedPreferences.getSharedPreferences(Constants.Location);
        user_thumb_image = localSharedPreferences.getSharedPreferences(Constants.ProfilePicture);
        about_me = localSharedPreferences.getSharedPreferences(Constants.AboutMe);
        dob = localSharedPreferences.getSharedPreferences(Constants.DOB);
        school = localSharedPreferences.getSharedPreferences(Constants.School);
        //gender = localSharedPreferences.getSharedPreferences(Constants.Gender);
        gender = localSharedPreferences.getSharedPreferences(GenreName);
        email = localSharedPreferences.getSharedPreferences(Constants.Email);
        phone_number = localSharedPreferences.getSharedPreferences(Constants.Phone);
        work = localSharedPreferences.getSharedPreferences(Constants.Work);


        if (about_me == null) {
            about_me = "";
        }

        if (work == null) {
            work = "";
        }

        if (school == null) {
            school = "";
        }

        if (user_location == null) {
            user_location = "";
        }

        if (phone_number == null) {
            phone_number = "";
        }

        if (email == null) {
            email = "";
        }

        if (gender == null) {
            gender = "";
        }
        else {
            localSharedPreferences.getSharedPreferences(GenreName);
        gender_txt.setText(gender);
        gender_txt.setVisibility(View.VISIBLE);
        gender_img.setVisibility(View.GONE);
    }

        if (dob == null) {
            dob = "";
        }

        /*try {
            about_me = URLEncoder.encode(about_me, "UTF-8");
            firstname = URLEncoder.encode(firstname, "UTF-8");
            lastname = URLEncoder.encode(lastname, "UTF-8");
            user_location = URLEncoder.encode(user_location, "UTF-8");
            school = URLEncoder.encode(school, "UTF-8");
            work = URLEncoder.encode(work, "UTF-8");
            about_me = about_me.replace("+", " ");
            firstname = firstname.replace("+", " ");
            lastname = lastname.replace("+", " ");
            user_location = user_location.replace("+", " ");
            school = school.replace("+", " ");
            work = work.replace("+", " ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

    }


    public void getUserProfile() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.viewProfile(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_PROFILE, this));

    }

    // Show camera or Galley
    public void cameraGallery() {
        android.app.AlertDialog.Builder alertdialog1 = new android.app.AlertDialog.Builder(EditProfileActivity.this);
        alertdialog1.setMessage(getString(R.string.picture_select));
        alertdialog1.setTitle(getString(R.string.take_picture));
        alertdialog1.setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = commonMethods.cameraFilePath();
                imageUri = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                try {
                    List<ResolveInfo> resolvedIntentActivities = EditProfileActivity.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("return-data", true);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery(EditProfileActivity.this, imageFile);


            }
        });
        alertdialog1.setNegativeButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                imageFile = commonMethods.getDefaultFileName(EditProfileActivity.this,"Profile_");

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);

            }
        });


        alertdialog1.show();
    }

    /**
     * OnActivity Result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        if (inputStream != null) inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imagePath = result.getUri().getPath();
                        if (!TextUtils.isEmpty(imagePath)) {
                            commonMethods.showProgressDialog(this, customDialog);
                            System.out.println("Image path check " + imagePath);
                            new ImageCompressAsyncTask(EditProfileActivity.this, imagePath, this, "").execute();
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Input output Stream
     */
    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * Crop Profile Image
     */
    private void startCropImage() {
        if (imageFile == null) return;
        int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile)).setAspectRatio(10, 10).setOutputCompressQuality(100).setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1]).start(this);
    }

    /**
     * Image To Compress and Update Using Post Method
     */
    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            System.out.println("request object " + requestBody);
            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadImage(requestBody, localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_UPLOAD_PROFILE_IMG, this));
        }
    }

    /**
     * Load Image
     */
    private void loadImage(String imageurl) {
        commonMethods.hideProgressDialog();


//        Picasso
//                .with(this)
//                .load(imageurl)
//                .resize(600, 200) // resizes the ivPhoto to these dimensions (in pixel). does not respect aspect ratio
//                .into(editprofile_img);

        Glide.with(this)
                .load(imageurl)

                .into(new DrawableImageViewTarget(editprofile_img) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                    }
                });

        editprofile_img.setBackgroundColor(getResources().getColor(R.color.title_text_color));


       // Glide.with(this).load(imageurl).into(editprofile_img);
    }


    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    /**
     * Method to check permissions
     *
     * @param permission
     */

    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            cameraGallery();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);

        } else {
            cameraGallery();
        }
    }

    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog("Alert", message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * To check call permissions
     */

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onSuccess(String language) {
        gender_txt.setVisibility(View.VISIBLE);
        gender_txt.setText(language);
        gender_img.setVisibility(View.GONE);
    }
}
