package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestAdditionalPriceActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.AvailabilityAdapter;
import com.makent.trioangle.model.roomModels.RoomAvailabilityRules;
import com.makent.trioangle.util.CommonMethods;

import java.util.ArrayList;

import javax.inject.Inject;

public class TripLengthActivity extends AppCompatActivity {


    TextView additionalprice_close,minimumstay,maximumstay;
    public @Inject
    CommonMethods commonMethods;


    AvailabilityAdapter availabilityAdapter;

    RecyclerView availability_list;
    LinearLayout minimum_stay,maximum_stay,availability;
    NestedScrollView nestedScrollView;
    ArrayList <RoomAvailabilityRules> roomAvailabilityRules=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_length);

        commonMethods = new CommonMethods();

        Intent x=getIntent();
        String minimum_stays= x.getStringExtra("minimum_stay");
        String maximum_stays= x.getStringExtra("maximum_stay");
        Bundle bundle = x.getExtras();
        roomAvailabilityRules.addAll((ArrayList<RoomAvailabilityRules>) bundle.getSerializable("availability_rules"));
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscroll);


        nestedScrollView.scrollTo(0,0);
        nestedScrollView.fullScroll(View.FOCUS_UP);



        availability_list=(RecyclerView)findViewById(R.id.availability_list);

        availability=(LinearLayout)findViewById(R.id.availability);

        minimum_stay=(LinearLayout)findViewById(R.id.minimum_stay);
        maximum_stay=(LinearLayout)findViewById(R.id.maximum_stay);

        additionalprice_close=(TextView)findViewById(R.id.additionalprice_close);
        commonMethods.setTvAlign(additionalprice_close,this);
        minimumstay=(TextView)findViewById(R.id.minimumstay);
        maximumstay=(TextView)findViewById(R.id.maximumstay);

        if(minimum_stays.equals(""))
        {
            minimum_stay.setVisibility(View.GONE);
        }else{
            if(minimum_stays.equals(1))
                minimumstay.setText(minimum_stays+" "+getResources().getString(R.string.night));
            else
                minimumstay.setText(minimum_stays+" "+getResources().getString(R.string.nightss));
        }

        if(maximum_stays.equals(""))
        {
            maximum_stay.setVisibility(View.GONE);
        }else{
            if(maximum_stays.equals(1))
                maximumstay.setText(maximum_stays+" "+getResources().getString(R.string.night));
            else
                maximumstay.setText(maximum_stays+" "+getResources().getString(R.string.nightss));
        }

        if(roomAvailabilityRules.size()>0)
            availability.setVisibility(View.VISIBLE);
        else
            availability.setVisibility(View.GONE);

        availabilityAdapter = new AvailabilityAdapter(this, roomAvailabilityRules,"trip");


        availability_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        availability_list.setAdapter(availabilityAdapter);



// On Click function used to click action for check Email id in server send link to Email
        additionalprice_close.setOnClickListener(new View.OnClickListener() {

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
