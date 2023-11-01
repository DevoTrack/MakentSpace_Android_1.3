package com.makent.trioangle.profile;

/**
 * @package com.makent.trioangle
 * @subpackage Profile
 * @category PayoutEmailActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
   Payout Get Email Page
Get  PayPal email address for payout option
*************************************************************** */
public class PayoutEmailActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public ImageView payoutemail_back;
    protected boolean isInternetAvailable;
    RelativeLayout payoutemail_title;
    Button payout_submit;
    EditText payoutemail_edittext;
    String payoutemail, address1, address2, city, state, pincode, country, userid;
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;
    private Boolean buttonBoolean = true;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_email);
        commonMethods = new CommonMethods();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        Intent x = getIntent();
        address1 = x.getStringExtra("address1");
        address2 = x.getStringExtra("address2");
        city = x.getStringExtra("city");
        state = x.getStringExtra("state");
        country = x.getStringExtra("country");
        pincode = x.getStringExtra("postal_code");

        payoutemail_title = findViewById(R.id.payoutemail_title);
        payout_submit = findViewById(R.id.payout_submit);
        payoutemail_back = findViewById(R.id.payoutemail_back);
        commonMethods.rotateArrow(payoutemail_back, this);

        payout_submit.setOnClickListener(this);
        payoutemail_title.setOnClickListener(this);


        payoutemail_edittext = findViewById(R.id.payoutemail_edittext);

        payout_submit.setEnabled(false);
        payoutemail_edittext.addTextChangedListener(new EmailTextWatcher(payoutemail_edittext));

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            if (mydialog.isShowing())
                mydialog.dismiss();
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, payoutemail_edittext, payoutemail_edittext, getResources(), this);
            finish();
            buttonBoolean = true;
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, payoutemail_edittext, payoutemail_edittext, getResources(), this);
    }

    private boolean validateEmail() {

        String email = payoutemail_edittext.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {

            return false;
        } else {
        }

        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payoutemail_title: {
                onBackPressed();
            }
            break;

            case R.id.payout_submit: {
                payoutemail = payoutemail_edittext.getText().toString();

                hideSoftKeyboard();  // Hide keyboard
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    if (buttonBoolean == true) {
                        localSharedPreferences.saveSharedPreferences(Constants.Backpaymailshow, "addmailid");
                        Map<String, String> imageObject = new HashMap<String, String>();

                        imageObject.put("address1", address1);
                        imageObject.put("address2", address2);
                        imageObject.put("token", localSharedPreferences.getSharedPreferences(Constants.AccessToken));
                        imageObject.put("paypal_email", payoutemail);
                        imageObject.put("city", city);
                        imageObject.put("state", state);
                        imageObject.put("country", country);
                        imageObject.put("postal_code", pincode);
                        imageObject.put("payout_method", "paypal");
                        updateProf();
                    }
                    // updateProfile(imageObject); // Call update API to update Email and address details
                } else {
                    snackBar(getResources().getString(R.string.Interneterror));
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror), "", false, 2, payoutemail_edittext, payoutemail_edittext, getResources(), this);
                }
            }
            break;
        }
    }

    // Hide keyboard function
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void updateProf() {
        buttonBoolean = false;
        apiService.addPayoutPreference(localSharedPreferences.getSharedPreferences(Constants.AccessToken), address1, address2, payoutemail
                , city, state, country, pincode, "paypal").enqueue(new RequestCallback(this));

    }

    // Show network error and exception
    public void snackBar(String statusmessage) {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(payout_submit, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable) {
            textViewTop.setText(statusmessage);
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }

    // Check network is avalable or not
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
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

            if (validateEmail()) {
                payout_submit.setEnabled(true);
            } else {
                payout_submit.setEnabled(false);
            }
        }
    }
}
