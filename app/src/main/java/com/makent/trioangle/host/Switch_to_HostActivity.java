package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    Switch_to_HostActivityActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.travelling.HomeActivity;
/* ***********************************************************************
This is  Contain Switch to Host Activity
**************************************************************************  */
public class Switch_to_HostActivity extends AppCompatActivity {

    LocalSharedPreferences localSharedPreferences;
    int isHost;
    RelativeLayout activity_switch_to__host;
    ImageView switch_logoimage;
    TextView switch_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_to__host);

        localSharedPreferences= new LocalSharedPreferences(this);
        isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);

        activity_switch_to__host=(RelativeLayout)findViewById(R.id.activity_switch_to__host);
        switch_logoimage=(ImageView)findViewById(R.id.switch_logoimage);
        switch_text=(TextView) findViewById(R.id.switch_text);


        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
              if(isHost==1)
        {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(x);
            finish();
        }else
        {
            Intent x = new Intent(getApplicationContext(), HostHome.class);
            startActivity(x);
            finish();
        }
            }
        };handler.postDelayed(runnable, 2000);

    }
}
