package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestCancellationPolicyActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

public class CancellationPolicyActivity extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;

    Button cancellation_close;
    String cancellationpolicy;

    TextView cancellation_type,cancellation_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        commonMethods = new CommonMethods();

        Intent x=getIntent();
        cancellationpolicy=x.getStringExtra("cancellationpolicy");

        cancellation_type= (TextView)findViewById(R.id.cancellation_type);
        cancellation_msg= (TextView)findViewById(R.id.cancellation_msg);

        cancellation_type.setText(cancellationpolicy);

        if(cancellationpolicy.equals(getResources().getString(R.string.cancellation_type1)))
        {
            cancellation_msg.setText(getResources().getString(R.string.cancellation_msg1));
        }else if(cancellationpolicy.equals(getResources().getString(R.string.cancellation_type2)))
        {
            cancellation_msg.setText(getResources().getString(R.string.cancellation_msg2));
        }else if(cancellationpolicy.equals(getResources().getString(R.string.cancellation_type3)))
        {
            cancellation_msg.setText(getResources().getString(R.string.cancellation_msg3));
        }else{
            cancellation_msg.setText(getResources().getString(R.string.cancellation_msg1));
        }

        cancellation_close = (Button) findViewById(R.id.cancellation_close);
        commonMethods.setTvAlign(cancellation_close,this);

        cancellation_close.setOnClickListener(new View.OnClickListener() {
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
