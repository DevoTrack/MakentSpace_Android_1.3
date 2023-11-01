package com.makent.trioangle.signin;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  signin
 * @category    Forgot_PasswordActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

// Header Files Declearation

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
                    Forgot password
Get e-Mail Id from user and check the server If Email Id Exits send reset Password link to Email Id

*************************************************************** */
public class Forgot_PasswordActivity extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    ImageView forgot_back; // Back to Login page
    ImageView forgot_next; // Check Email id in server
    EditText forgot_email;  // Get Email Address from user
    String forgotemail;  // String to store the user Email Id
    protected boolean isInternetAvailable;
    LocalSharedPreferences localSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);   // Layout design Name
        commonMethods = new CommonMethods();
        localSharedPreferences = new LocalSharedPreferences(this);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        // Assign Design Id to Java variable
        forgot_back=(ImageView)findViewById(R.id.forgot_back);
        commonMethods.rotateArrow(forgot_back,this);
        forgot_next=(ImageView)findViewById(R.id.forgot_next);
        commonMethods.rotateArrow(forgot_next,this);
        forgot_email=(EditText) findViewById(R.id.forgot_email);

        forgot_next.setEnabled(false);
        forgot_next.setAlpha(.5f);
        forgot_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        forgot_email.addTextChangedListener(new EmailTextWatcher(forgot_email));
        // On Click function used to click action for check Email id in server send link to Email
        forgot_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                forgotemail=forgot_email.getText().toString();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
              //  Intent x = new Intent(getApplicationContext(),LoginActivity.class);
              //  startActivity(x);
                if (isInternetAvailable) {
                    hideSoftKeyboard();
                    forgot_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                    //new CheckEmail().execute(forgotemail);
                    forgetPassword();
                }else {
                    //snackBar();
                    commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,forgot_email,forgot_email,getResources(),Forgot_PasswordActivity.this);
                }
            }
        });


        // On Click function used to click action Go Back
        forgot_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
               onBackPressed();

            }
        });

        Intent login=getIntent();
        forgotemail=login.getStringExtra("email");
        if(forgotemail!=null)
            forgot_email.setText(forgotemail);


    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent x = new Intent(getApplicationContext(),LoginActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(x,bndlanimation);
        finish();


    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        forgot_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(forgot_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        if (jsonResp.isSuccess()) {
            onSuccessPwd();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            forgot_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
            Glide.with(this).clear(forgot_next);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.forgot_invalid_mail), Toast.LENGTH_LONG).show();
            //commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,forgot_email,forgot_email,getResources(),Forgot_PasswordActivity.this);
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,forgot_email,forgot_email,getResources(),Forgot_PasswordActivity.this);
    }

    public void onSuccessPwd(){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.forgot_email_send)+"\t"+forgotemail, Toast.LENGTH_SHORT).show();
        //System.out.println("Amount data will be cleared");
        commonMethods.NotrotateArrow(forgot_next,this);
        forgot_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_tick));
        Glide.with(this).clear(forgot_next);
        // snackBar(getResources().getString(R.string.forgot_email_send)+""+forgotemail+".");
        Intent x = new Intent(getApplicationContext(),LoginActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(x,bndlanimation);
        finish();
    }

    // Validate email field
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
                forgot_next.setAlpha(1f);
                forgot_next.setEnabled(true);
            }else {
                forgot_next.setAlpha(.5f);
                forgot_next.setEnabled(false);
            }
        }
    }

    private boolean validateEmail() {

        String email = forgot_email.getText().toString().trim();

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

    // Check given email id exits or not
    public void forgetPassword() {
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(forgot_next);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        apiService.forgetpwd(forgotemail,langCode).enqueue(new RequestCallback(this));
    }



    // Hide keyboard while close
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
