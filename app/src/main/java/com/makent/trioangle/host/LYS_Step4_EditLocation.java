package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step4_EditLocationActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makent.trioangle.R;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ***********************************************************************
This is List Your Space Step4  Contain EdiLocation
**************************************************************************  */
public class LYS_Step4_EditLocation extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;

    ImageView editlocation_back;

    RelativeLayout editlocation_title,editlocation_address_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step4_edit_location);
        commonMethods = new CommonMethods();
        editlocation_title=(RelativeLayout) findViewById(R.id.editlocation_title);
        editlocation_back =(ImageView)findViewById(R.id.editlocation_back);
        commonMethods.rotateArrow(editlocation_back,this);

        editlocation_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        editlocation_address_details=(RelativeLayout) findViewById(R.id.editlocation_address_details);

        editlocation_address_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent lysproperty=new Intent(getApplicationContext(),LYS_Step4_SetAddress.class);
                startActivity(lysproperty);
            }
        });
    }
}
