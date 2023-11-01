package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestHouseRulesActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
/* ***********************************************************************
This is HouseRules Page Contain to Agree The HouseRules
**************************************************************************  */
public class HouseRulesActivity extends AppCompatActivity {

    Button agree,houserules_close;
    LocalSharedPreferences localSharedPreferences;
    TextView houserules_title,houserules_msg;
    String houserules,hostname;
    int isRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_rules);
        localSharedPreferences= new LocalSharedPreferences(this);
        isRequest=localSharedPreferences.getSharedPreferencesInt(Constants.isRequestrRoom);
        houserules =localSharedPreferences.getSharedPreferences(Constants.HouseRules);
        hostname =localSharedPreferences.getSharedPreferences(Constants.HostUserName);
        agree = (Button) findViewById(R.id.agree);

        if(isRequest==1)
        {
        agree.setVisibility(View.VISIBLE);
        }else
        {
        agree.setVisibility(View.GONE);
        }
        // On Click function used to click action for check Email id in server send link to Email
        agree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(),RequestActivity.class);
                localSharedPreferences.saveSharedPreferences(Constants.stepHouserules,1);
                onBackPressed();
            }
        });

        houserules_close = (Button) findViewById(R.id.contacthost_close);

        // On Click function used to click action for check Email id in server send link to Email
        houserules_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
               onBackPressed();
            }
        });


        houserules_title = (TextView) findViewById(R.id.houserules_title);
        houserules_msg = (TextView) findViewById(R.id.houserules_msg);



        houserules_title.setText(hostname+" "+getResources().getString(R.string.house_rules));
        houserules_msg.setText(houserules);
    }
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}
