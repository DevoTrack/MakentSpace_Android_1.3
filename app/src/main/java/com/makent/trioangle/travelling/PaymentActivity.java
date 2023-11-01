package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestPaymentActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.makent.trioangle.R;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ***********************************************************************
This is Payment Page Contain to Add the Payment
**************************************************************************  */
public class PaymentActivity extends AppCompatActivity {
    public @Inject
    CommonMethods commonMethods;

    Button add_payment,payment_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        commonMethods = new CommonMethods();
        add_payment = (Button) findViewById(R.id.add_payment);

        // On Click function used to click action for check Email id in server send link to Email
        add_payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(),PaymentCountryList.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(x,bndlanimation);
                finish();
            }
        });

        payment_close = (Button) findViewById(R.id.payment_close);
        commonMethods.setTvAlign(payment_close,this);

        // On Click function used to click action for check Email id in server send link to Email
        payment_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
