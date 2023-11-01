package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestRoomAboutListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/* ***********************************************************************
This is RoomAboutList Page Contain About the details
**************************************************************************  */
public class RoomAboutList extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;

    @BindView(R.id.space)
    RelativeLayout space;
    @BindView(R.id.guest_access)
    RelativeLayout guest_access;
    @BindView(R.id.interaction)
    RelativeLayout interaction;
    @BindView(R.id.neighbourhood)
    RelativeLayout neighbourhood;
    @BindView(R.id.gettingaround)
    RelativeLayout gettingaround;
    @BindView(R.id.notes)
    RelativeLayout notes;
    @BindView(R.id.houserules)
    RelativeLayout houserules;
    @BindView(R.id.rltotherservice)
    RelativeLayout rltOtherService;

    @BindView(R.id.aboutlist_details)
    TextView aboutlist_details;
    @BindView(R.id.desc_space)
    TextView tvdesc_space;
    @BindView(R.id.desc_access)
    TextView tvdesc_access;
    @BindView(R.id.desc_interaction)
    TextView tvdesc_interaction;
    @BindView(R.id.desc_neighbourhood)
    TextView tvdesc_neighbourhood;
    @BindView(R.id.desc_gettingaround)
    TextView tvdesc_gettingaround;
    @BindView(R.id.desc_notes)
    TextView tvdesc_notes;
    @BindView(R.id.desc_houserules)
    TextView tvdesc_houserules;
    @BindView(R.id.tv_other_services)
    TextView tvOtherServices;


    @BindView(R.id.aboutlist_close)
    Button aboutlist_close;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String desc_space;
    private String desc_access;
    private String desc_getting_around;

    private String desc_interaction;
    private String desc_house_rules;
    private String desc_neighborhood_overview;
    private String desc_transit;
    private String desc_notes;
    private String aboutlist;
    private String mOtherService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_about_list);

        commonMethods = new CommonMethods();

        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent x=getIntent();
        aboutlist=x.getStringExtra("aboutlist");
        desc_interaction = x.getStringExtra("desc_interaction");
        desc_house_rules = x.getStringExtra("desc_house_rules");
        desc_neighborhood_overview = x.getStringExtra("desc_neighborhood_overview");
        desc_transit = x.getStringExtra("desc_transit");
        desc_notes = x.getStringExtra("desc_notes");
        desc_space = x.getStringExtra("desc_space");
        desc_access = x.getStringExtra("desc_access");
        desc_getting_around = x.getStringExtra("desc_getting_around");
        mOtherService = x.getStringExtra("otherService");


        if(desc_space!=null&&!desc_space.equals("")){
            space.setVisibility(View.VISIBLE);
            tvdesc_space.setText(desc_space);
        }else{
            space.setVisibility(View.GONE);
        }


        if(desc_interaction!=null&&!desc_interaction.equals("")){
            interaction.setVisibility(View.VISIBLE);
            tvdesc_interaction.setText(desc_interaction);
        }else{
            interaction.setVisibility(View.GONE);
        }


        if(desc_house_rules!=null&&!desc_house_rules.equals("")){
            houserules.setVisibility(View.VISIBLE);
            tvdesc_houserules.setText(desc_house_rules);
        }else{
            houserules.setVisibility(View.GONE);
        }



        if(desc_notes!=null&&!desc_notes.equals("")){
            notes.setVisibility(View.VISIBLE);
            tvdesc_notes.setText(desc_notes);
        }else{
            notes.setVisibility(View.GONE);
        }



        if(desc_neighborhood_overview!=null&&!desc_neighborhood_overview.equals("")){
            neighbourhood.setVisibility(View.VISIBLE);
            tvdesc_neighbourhood.setText(desc_neighborhood_overview);
        }else{
            neighbourhood.setVisibility(View.GONE);
        }


        if(desc_access!=null&&!desc_access.equals("")){
            guest_access.setVisibility(View.VISIBLE);
            tvdesc_access.setText(desc_access);
        }else{
            guest_access.setVisibility(View.GONE);
        }


        /*if(desc_transit!=null&&!desc_transit.equals("")){
            gettingaround.setVisibility(View.VISIBLE);
            tvdesc_gettingaround.setText(desc_transit);
        }else{
            gettingaround.setVisibility(View.GONE);
        }*/

        if(desc_getting_around!=null&&!desc_getting_around.equals("")){
            gettingaround.setVisibility(View.VISIBLE);
            tvdesc_gettingaround.setText(desc_getting_around);
        }else{
            gettingaround.setVisibility(View.GONE);
        }

        if(mOtherService!=null&&!mOtherService.equals("")){
            rltOtherService.setVisibility(View.VISIBLE);
            tvOtherServices.setText(mOtherService);
        }else{
            rltOtherService.setVisibility(View.GONE);
        }

        aboutlist_details.setText(aboutlist);

        commonMethods.setTvAlign(aboutlist_close,this);
        aboutlist_close.setOnClickListener(new View.OnClickListener() {

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
