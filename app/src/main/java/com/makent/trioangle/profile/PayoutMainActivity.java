package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    PayoutMainActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;

/* ************************************************************
                   Payout Main Page
Show payment message in static page
*************************************************************** */
public class PayoutMainActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout payout_title;
    TextView payoutmain_link;
    Button payout_start,payout_stripe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_main);

        payout_title=(RelativeLayout)findViewById(R.id.payout_title);
        payoutmain_link=(TextView)findViewById(R.id.payoutmain_link);
        payout_start=(Button)findViewById(R.id.payout_start);
        payout_stripe=(Button) findViewById(R.id.payout_stripe);

        payout_title.setOnClickListener(this);
        payoutmain_link.setOnClickListener(this);
        payout_start.setOnClickListener(this);
        payout_stripe.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.payout_title: {
                onBackPressed();
            }
            break;
            case R.id.payoutmain_link: {
                // Call help page
                Intent x = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(x);
                finish();
            }
            break;
            case R.id.payout_start: {
                // Call payout address page
                Intent x = new Intent(getApplicationContext(), PayoutAddressDetailsActivity.class);
                startActivity(x);
                finish();
            }
            break;
            case R.id.payout_stripe: {
                // Call payout address page
                Intent x = new Intent(getApplicationContext(), PayoutBankDetailsActivity.class);
                startActivity(x);
                finish();
            }
            break;

        }
    }
}
