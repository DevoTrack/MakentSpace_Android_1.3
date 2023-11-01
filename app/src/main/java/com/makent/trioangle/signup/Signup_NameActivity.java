package com.makent.trioangle.signup;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  signup
 * @category    Signup_NameActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ************************************************************
                Name page
One part of registeration to get First and last name from user and the register the user
*************************************************************** */
public class Signup_NameActivity extends Activity {

    ImageView name_back;  // Back to Main Page
    ImageView name_next; // Validate names and move to Email page
    EditText first_name,last_name; // Get first and last name from user
    String firstname,lastname; // Store first and last name as string
    boolean fname,lname; // Check first and last name is valid or not

    public @Inject
    CommonMethods commonMethods;
    LocalSharedPreferences localSharedPreferences;
    public Handler handler;
    Runnable myRunnable;
    boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__name);  // Layout design Name
        localSharedPreferences=new LocalSharedPreferences(this);
        Log.e("SignupTest","SignupTest");
        commonMethods = new CommonMethods();
        // Assign Design Id to Java variable
        name_back=(ImageView)findViewById(R.id.name_back);
        name_next=(ImageView)findViewById(R.id.name_next);
        first_name=(EditText) findViewById(R.id.first_name);
        last_name=(EditText) findViewById(R.id.last_name);
        commonMethods.rotateArrow(name_back,this);

        first_name.addTextChangedListener(new NameTextWatcher(first_name));
        last_name.addTextChangedListener(new NameTextWatcher(last_name));

        name_next.setEnabled(false);
        name_next.setAlpha(.5f);
        commonMethods.rotateArrow(name_next,this);

        // On Click function used to click action
        name_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                firstname=first_name.getText().toString();
                lastname=last_name.getText().toString();

                localSharedPreferences.saveSharedPreferences(Constants.FirstName,firstname);
                localSharedPreferences.saveSharedPreferences(Constants.LastName,lastname);
                name_next.setBackground(getResources().getDrawable(R.drawable.d_next_button_load));
                DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(name_next);
                Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
                handler = new Handler();
                myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        try {
                            Intent x = new Intent(getApplicationContext(), Signup_EmailActivity.class);
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
        name_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        // Load local saved data
        loadSavedData();
   }

    public void loadSavedData()
    {
        firstname=localSharedPreferences.getSharedPreferences(Constants.FirstName);
        lastname=localSharedPreferences.getSharedPreferences(Constants.LastName);

        if(firstname!=null)
            first_name.setText(firstname);
        if(lastname!=null)
            last_name.setText(lastname);

    }

    // While back pressed;
    public void onBackPressed()
    {
       // super.onBackPressed();
        if(myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        localSharedPreferences.saveSharedPreferences(Constants.FirstName,null);
        localSharedPreferences.saveSharedPreferences(Constants.LastName,null);
        Intent x = new Intent(getApplicationContext(),MainActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
        startActivity(x,bndlanimation);
        finish();
    }




    // Name field text watcher for validation
    private class NameTextWatcher implements TextWatcher {

        private View view;

        private NameTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.first_name:
                    fname=validateName(first_name);
                    break;
                case R.id.last_name:
                    lname=validateName(last_name);
                    break;
            }
            if(fname&&lname)
            {
                name_next.setEnabled(true);
                name_next.setAlpha(1f);
            }else {
                name_next.setEnabled(false);
                name_next.setAlpha(.5f);
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
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
