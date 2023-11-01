package com.makent.trioangle.signup;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  signup
 * @category    Signup_PasswordActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ************************************************************
                Password page
One part of registeration to get Password from user and the register the user
*************************************************************** */

public class Signup_PasswordActivity extends Activity {

    ImageView password_back,arrow_back; // Back to Email Page
    ImageView password_next; // Validate password and move to DOB page
    EditText signup_password; // Get password form user
    String signuppassword; // store password as string
    TextView signup_show; // Show or hide password

    public @Inject
    CommonMethods commonMethods;
    public Handler handler;
    Runnable myRunnable;

    LocalSharedPreferences localSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__password); // Layout design Name

        commonMethods = new CommonMethods();

        localSharedPreferences=new LocalSharedPreferences(this);

        // Assign Design Id to Java variable
        password_back=(ImageView)findViewById(R.id.password_back);
        password_next=(ImageView)findViewById(R.id.password_next);
        signup_password=(EditText) findViewById(R.id.signup_password);
        signup_show=(TextView)findViewById(R.id.signup_show);
        arrow_back =(ImageView)findViewById(R.id.password_back);
        commonMethods.rotateArrow(arrow_back,this);
        commonMethods.rotateArrow(password_next,this);

        // On Click function used to click action
        signup_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(signup_show.getText().toString().equals(getResources().getString(R.string.show))) {
                    signup_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    signup_password.setSelection(signup_password.length());
                    signup_show.setText(R.string.hide);

                }else
                {
                    signup_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    signup_password.setSelection(signup_password.length());
                    signup_show.setText(R.string.show);
                }

            }
        });

        // On Click function used to click action
        password_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                signuppassword=signup_password.getText().toString();

                localSharedPreferences.saveSharedPreferences(Constants.Password,signuppassword);
                password_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(password_next);
                Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
                handler = new Handler();
                myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        try {
                            Intent x = new Intent(getApplicationContext(), Signup_DobActivity.class);
                            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                            startActivity(x, bndlanimation);
                            finish();
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(myRunnable,2000);

            }
        });

        // On Click function used to click action
        password_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                /*Intent x = new Intent(getApplicationContext(),Signup_EmailActivity.class);
                startActivity(x);*/
                onBackPressed();

            }
        });


        password_next.setAlpha(.5f);
        password_next.setEnabled(false);
        signup_password.addTextChangedListener(new PasswordTextWatcher(signup_password));

        // On Click function used to click action go to Main Page
      password_next.setBackground(getResources().getDrawable(R.drawable.d_next_button));
        loadSavedData();
    }

    // Load local saved data for password
    public void loadSavedData()
    {
        signuppassword=localSharedPreferences.getSharedPreferences(Constants.Password);

        if(signuppassword!=null)
            signup_password.setText(signuppassword);

    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        localSharedPreferences.saveSharedPreferences(Constants.Password,null);
        Intent x = new Intent(getApplicationContext(),Signup_EmailActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(x,bndlanimation);
        finish();
    }

    // Password Field validation
    private class PasswordTextWatcher implements TextWatcher {

        private View view;

        private PasswordTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                    case R.id.signup_password:
                    validatePassword();
                    break;
            }

            if(validatePassword())
            {
                password_next.setAlpha(1f);
                password_next.setEnabled(true);
            }else {
                password_next.setAlpha(.5f);
                password_next.setEnabled(false);
            }
        }
    }


    private boolean validatePassword() {
        if (signup_password.getText().toString().trim().isEmpty()||signup_password.getText().toString().trim().length()<Constants.PasswordLength) {
            requestFocus(signup_password);
            return false;
        } else {
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
