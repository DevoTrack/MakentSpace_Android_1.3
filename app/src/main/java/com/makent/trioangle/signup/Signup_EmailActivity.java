package com.makent.trioangle.signup;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  signup
 * @category    Signup_EmailActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

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
                Email page
One part of registeration to get Email Address from user and the register the user
*************************************************************** */
public class Signup_EmailActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    ImageView email_back;  //  Back to Name page
    ImageView email_next; // Validate Email id and Move to DOB page
    EditText signup_email; // Get email id from user
    String signupemail; // To store email id as string
    LocalSharedPreferences localSharedPreferences;
    protected boolean isInternetAvailable;
    public String space = " ";
    public Handler handler;
    Runnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__email);  // Layout design Name
        ButterKnife.bind(this);
        commonMethods = new CommonMethods();
        AppController.getAppComponent().inject(this);
        localSharedPreferences=new LocalSharedPreferences(this);
        // Assign Design Id to Java variable
        email_back=(ImageView)findViewById(R.id.email_back);
        email_next=(ImageView)findViewById(R.id.email_next);
        signup_email=(EditText) findViewById(R.id.signup_email);
        commonMethods.rotateArrow(email_back,this);

        email_next.setEnabled(false);
        email_next.setAlpha(.5f);
        email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        signup_email.addTextChangedListener(new EmailTextWatcher(signup_email));

        commonMethods.rotateArrow(email_next,this);

        // On Click function used to click action
        email_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    hideSoftKeyboard();  // Hide keyboard
                    signupemail = signup_email.getText().toString();
                    signupemail = signupemail.replaceAll(" ", "");

                    localSharedPreferences.saveSharedPreferences(Constants.Email, signupemail);
                    email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                    DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(email_next);
                    Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
                    handler = new Handler();
                    myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                           checkEmail();
                        }
                    };
                    handler.postDelayed(myRunnable,2000);
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,signup_email,signup_email,getResources(),Signup_EmailActivity.this);
                }
            }
        });

        // On Click function used to click action
        email_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                /*Intent x = new Intent(getApplicationContext(),Signup_NameActivity.class);
                startActivity(x);*/
                onBackPressed();
            }
        });
        loadSavedData();
    }

    // Set local saved date in email field
    public void loadSavedData()
    {
        signupemail=localSharedPreferences.getSharedPreferences(Constants.Email);


        if(signupemail!=null)
            signup_email.setText(signupemail);
    }

    // While back press go to Signup name page
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        localSharedPreferences.saveSharedPreferences(Constants.Email,null);
        Intent x = new Intent(getApplicationContext(),Signup_NameActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(x,bndlanimation);
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(email_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        if (jsonResp.isSuccess()) {
            onSuccessEmail();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
            Glide.with(this).clear(email_next);
            commonMethods.snackBar(getResources().getString(R.string.emailalreadyexits),getResources().getString(R.string.login_title),true,2,signup_email,signup_email,getResources(),Signup_EmailActivity.this);

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    //Email valitaion in Text Watcher
    private class EmailTextWatcher implements TextWatcher {

        private View view;

        private EmailTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.signup_email:
                    validateEmail();
                    break;

            }

            if(validateEmail())
            {
                email_next.setAlpha(1f);
                email_next.setEnabled(true);
            }else {
                email_next.setAlpha(.5f);
                email_next.setEnabled(false);
            }
        }
    }

    private boolean validateEmail() {

        String email = signup_email.getText().toString().trim();
        email=email.replaceAll(" ","");
        //signup_email.setText(email);
        if (email.isEmpty() || !isValidEmail(email)) {

            return false;
        } else {
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onSuccessEmail(){

        try {
            if(myRunnable != null) {
                handler.removeCallbacks(myRunnable);
            }
            commonMethods.NotrotateArrow(email_next, this);
            email_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
            Glide.with(this).clear(email_next);

            localSharedPreferences.saveSharedPreferences(Constants.Email, signupemail);
            Intent x = new Intent(getApplicationContext(), Signup_PasswordActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    // Check email id already exits or not using API call
    public void checkEmail(){
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(email_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        apiService.emailvalidation(signupemail,langCode,"","").enqueue(new RequestCallback(this));
    }




    // Hide key board
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    // Check network connection is available or not
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
