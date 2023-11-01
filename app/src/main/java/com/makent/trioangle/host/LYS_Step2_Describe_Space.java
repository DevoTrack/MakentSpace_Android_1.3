package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step2_Describe_SpaceActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ***********************************************************************
This is List Your Space Step2  Contain Describe Space
**************************************************************************  */
public class LYS_Step2_Describe_Space extends AppCompatActivity {


    public @Inject
    CommonMethods commonMethods;


    RelativeLayout describe_addtitle,describe_writesummary,describe_title;
    ImageView describe_back,addtitle_back,addtitle_back1;
    TextView addtitle_msg,sum_msg;
    LocalSharedPreferences localSharedPreferences;
    String roomtitle,roomsummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step2_describe_space);
        commonMethods = new CommonMethods();

        addtitle_msg=(TextView) findViewById(R.id.addtitle_msg);
        sum_msg=(TextView) findViewById(R.id.sum_msg);

        localSharedPreferences=new LocalSharedPreferences(this);
        roomtitle=localSharedPreferences.getSharedPreferences(Constants.ListRoomTitle);
        roomsummary=localSharedPreferences.getSharedPreferences(Constants.ListRoomSummary);

        if(roomtitle!=null&&!roomtitle.equals("")) {
            addtitle_msg.setText(roomtitle);
        }
        if(roomsummary!=null&&!roomsummary.equals("")) {
            sum_msg.setText(roomsummary);
        }



        describe_addtitle=(RelativeLayout)findViewById(R.id.describe_addtitle);
        describe_writesummary=(RelativeLayout)findViewById(R.id.describe_writesummary);
        addtitle_back1 =(ImageView)findViewById(R.id.addtitle_back1);
        addtitle_back = (ImageView)findViewById(R.id.addtitle_back);
        commonMethods.rotateArrow(addtitle_back,this);
        commonMethods.rotateArrow(addtitle_back1,this);

        describe_addtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step2_AddTitle.class);
                startActivity(addphoto);
            }
        });

        describe_writesummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent addphoto=new Intent(getApplicationContext(),LYS_Step2_AddSummary.class);
                startActivity(addphoto);
            }
        });

        describe_title=(RelativeLayout)findViewById(R.id.describe_title);

        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });


        describe_back=(ImageView) findViewById(R.id.describe_back);
        commonMethods.rotateArrow(describe_back,this);

        describe_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        roomtitle=localSharedPreferences.getSharedPreferences(Constants.ListRoomTitle);
        roomsummary=localSharedPreferences.getSharedPreferences(Constants.ListRoomSummary);

        if(roomtitle!=null&&!roomtitle.equals("")) {
            addtitle_msg.setText(roomtitle);
        }
        if(roomsummary!=null&&!roomsummary.equals("")) {
            sum_msg.setText(roomsummary);
        }
    }
}
