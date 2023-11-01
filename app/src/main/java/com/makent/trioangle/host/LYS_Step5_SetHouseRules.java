package com.makent.trioangle.host;


/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step5_SetHouseRulesActivity
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
This is List Your Space Step5  Contain Set House Rules
**************************************************************************  */
public class LYS_Step5_SetHouseRules extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout sethouse_title,additional_rules;
    ImageView houserules1_unselect,houserules2_unselect,houserules3_unselect,houserules4_unselect,houserules5_unselect;
    ImageView houserules1_select,houserules2_select,houserules3_select,houserules4_select,houserules5_select;
    ImageView additional_rules_img;

    public @Inject
    CommonMethods commonMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step5_set_house_rules);
        commonMethods = new CommonMethods();

        additional_rules_img = (ImageView)findViewById(R.id.additional_rules_img);
        commonMethods.rotateArrow(additional_rules_img,this);

        sethouse_title=(RelativeLayout) findViewById(R.id.sethouse_title);
        sethouse_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        additional_rules=(RelativeLayout) findViewById(R.id.additional_rules);
        additional_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent additionalrules=new Intent(getApplicationContext(),LYS_Step5_Additional_Rules.class);
                startActivity(additionalrules);
            }
        });

        houserules1_unselect=(ImageView) findViewById(R.id.houserules1_unselect);
        houserules2_unselect=(ImageView) findViewById(R.id.houserules2_unselect);
        houserules3_unselect=(ImageView) findViewById(R.id.houserules3_unselect);
        houserules4_unselect=(ImageView) findViewById(R.id.houserules4_unselect);
        houserules5_unselect=(ImageView) findViewById(R.id.houserules5_unselect);

        houserules1_unselect.setSelected(false);
        houserules2_unselect.setSelected(false);
        houserules3_unselect.setSelected(false);
        houserules4_unselect.setSelected(false);
        houserules5_unselect.setSelected(false);

        houserules1_select=(ImageView) findViewById(R.id.houserules1_select);
        houserules2_select=(ImageView) findViewById(R.id.houserules2_select);
        houserules3_select=(ImageView) findViewById(R.id.houserules3_select);
        houserules4_select=(ImageView) findViewById(R.id.houserules4_select);
        houserules5_select=(ImageView) findViewById(R.id.houserules5_select);

        houserules1_select.setSelected(false);
        houserules2_select.setSelected(false);
        houserules3_select.setSelected(false);
        houserules4_select.setSelected(false);
        houserules5_select.setSelected(false);

        houserules1_select.setOnClickListener(this);
        houserules2_select.setOnClickListener(this);
        houserules3_select.setOnClickListener(this);
        houserules4_select.setOnClickListener(this);
        houserules5_select.setOnClickListener(this);

        houserules1_unselect.setOnClickListener(this);
        houserules2_unselect.setOnClickListener(this);
        houserules3_unselect.setOnClickListener(this);
        houserules4_unselect.setOnClickListener(this);
        houserules5_unselect.setOnClickListener(this);




    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.houserules1_select:
            {
                houserules1_select.setSelected(true);
                houserules1_unselect.setSelected(false);
            }break;
            case R.id.houserules2_select:
            {
                houserules2_select.setSelected(true);
                houserules2_unselect.setSelected(false);
            }break;
            case R.id.houserules3_select:
            {
                houserules3_select.setSelected(true);
                houserules3_unselect.setSelected(false);
            }break;
            case R.id.houserules4_select:
            {
                houserules4_select.setSelected(true);
                houserules4_unselect.setSelected(false);
            }break;
            case R.id.houserules5_select:
            {
                houserules5_select.setSelected(true);
                houserules5_unselect.setSelected(false);
            }break;

            case R.id.houserules1_unselect:
            {
                houserules1_select.setSelected(false);
                houserules1_unselect.setSelected(true);
            }break;
            case R.id.houserules2_unselect:
            {
                houserules2_select.setSelected(false);
                houserules2_unselect.setSelected(true);
            }break;
            case R.id.houserules3_unselect:
            {
                houserules3_select.setSelected(false);
                houserules3_unselect.setSelected(true);
            }break;
            case R.id.houserules4_unselect:
            {
                houserules4_select.setSelected(false);
                houserules4_unselect.setSelected(true);
            }break;
            case R.id.houserules5_unselect:
            {
                houserules5_select.setSelected(false);
                houserules5_unselect.setSelected(true);
            }break;

        }
    }

}
