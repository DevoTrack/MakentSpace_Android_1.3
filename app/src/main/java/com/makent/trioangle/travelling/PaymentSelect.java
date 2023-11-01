package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestPaymentSelectActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;
/* ***********************************************************************
This is PaymentSelect Page Contain to selected the Payment Method Selected Details
**************************************************************************  */

public class PaymentSelect extends Activity {
    public @Inject
    CommonMethods commonMethods;


    TextView select_payment_creditcard,select_payment_paypal;
    ImageView select_payment_next,select_payment_back;

    LocalSharedPreferences localSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_select);
        localSharedPreferences= new LocalSharedPreferences(this);
        commonMethods = new CommonMethods();

        select_payment_creditcard = (TextView) findViewById(R.id.select_payment_creditcard);

        select_payment_next = (ImageView) findViewById(R.id.select_payment_next);

        select_payment_next.setEnabled(false);
        select_payment_next.setAlpha(.5f);

        // On Click function used to click action for check Email id in server send link to Email
        select_payment_creditcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                select_payment_next.setEnabled(true);
                select_payment_next.setAlpha(1f);
                select_payment_creditcard.setTextColor(getResources().getColor(R.color.title_text_color));
                select_payment_paypal.setTextColor(getResources().getColor(R.color.text_shadow));
                localSharedPreferences.saveSharedPreferences(Constants.ReqPaymentType,"Credit Card");
            }
        });

        select_payment_paypal = (TextView) findViewById(R.id.select_payment_paypal);

        // On Click function used to click action for check Email id in server send link to Email
        select_payment_paypal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                select_payment_next.setEnabled(true);
                select_payment_next.setAlpha(1f);
                select_payment_paypal.setTextColor(getResources().getColor(R.color.title_text_color));
                select_payment_creditcard.setTextColor(getResources().getColor(R.color.text_shadow));
                localSharedPreferences.saveSharedPreferences(Constants.ReqPaymentType,"PayPal");
            }
        });

        commonMethods.rotateArrow(select_payment_next,this);

        // On Click function used to click action for check Email id in server send link to Email
        select_payment_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                localSharedPreferences.saveSharedPreferences(Constants.stepPayment,1);
                onBackPressed();

            }
        });

        select_payment_back = (ImageView) findViewById(R.id.select_payment_back);
        commonMethods.rotateArrow(select_payment_back,this);

        // On Click function used to click action for check Email id in server send link to Email
        select_payment_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(),PaymentCountryList.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
                startActivity(x,bndlanimation);
                finish();

            }
        });


    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
