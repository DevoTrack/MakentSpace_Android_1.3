package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestAdditionalPriceActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.EarlyBirdAdapter;
import com.makent.trioangle.adapter.LastMinAdapter;
import com.makent.trioangle.adapter.LengthOfStayAdapter;
import com.makent.trioangle.model.roomModels.RoomLengthOfStay;
import com.makent.trioangle.util.CommonMethods;

import java.util.ArrayList;

import javax.inject.Inject;

public class AdditionalPriceActivity extends AppCompatActivity {


    TextView additionalprice_close, extrapeople, weeklyprice, monthlyprice, securitydeposit, cleaningfee;


    private ArrayList<RoomLengthOfStay> lengthOfStays = new ArrayList<>();
    private ArrayList<RoomLengthOfStay> earlyBirdList = new ArrayList<>();
    private ArrayList<RoomLengthOfStay> lastMinList = new ArrayList<>();

    LengthOfStayAdapter lengthofstayadapter;
    EarlyBirdAdapter earlyBirdAdapter;
    LastMinAdapter lastMinAdapter;

    public @Inject
    CommonMethods commonMethods;

    RecyclerView length_of_stay_list, early_bird_list, last_min_list;
    LinearLayout length_of_stay, early_bird, last_min, lltExtraPeople, lltSecurity, lltCleaning, lltMonthy, lltweekly,lltweekend;
    TextView length_of_stay_arrow, early_bird_arrow, last_min_arrow,weekendprice;
    boolean length_of_stay_show = true, early_bird_show = true, last_min_show = true;
    String currencysymbol = "";
    String weekendPrice ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_price);

        commonMethods = new CommonMethods();


        length_of_stay_list = (RecyclerView) findViewById(R.id.length_of_stay_list);
        early_bird_list = (RecyclerView) findViewById(R.id.early_bird_list);
        last_min_list = (RecyclerView) findViewById(R.id.last_min_list);

        length_of_stay = (LinearLayout) findViewById(R.id.length_of_stay);
        early_bird = (LinearLayout) findViewById(R.id.early_bird);
        last_min = (LinearLayout) findViewById(R.id.last_min);
        lltExtraPeople = (LinearLayout) findViewById(R.id.lltExtraPeople);
        lltSecurity = (LinearLayout) findViewById(R.id.lltSecurity);
        lltCleaning = (LinearLayout) findViewById(R.id.lltCleaning);
        lltweekend = (LinearLayout)findViewById(R.id.lltWeekend);
        lltMonthy = (LinearLayout) findViewById(R.id.lltMonthy);
        lltweekly = (LinearLayout) findViewById(R.id.lltweekly);
        weekendprice = (TextView) findViewById(R.id.weekendprice);

        length_of_stay_arrow = (TextView) findViewById(R.id.length_of_stay_arrow);
        early_bird_arrow = (TextView) findViewById(R.id.early_bird_arrow);
        last_min_arrow = (TextView) findViewById(R.id.last_min_arrow);

        additionalprice_close = (TextView) findViewById(R.id.additionalprice_close);
        extrapeople = (TextView) findViewById(R.id.extrapeople);
        weeklyprice = (TextView) findViewById(R.id.weeklyprice);
        monthlyprice = (TextView) findViewById(R.id.monthlyprice);
        securitydeposit = (TextView) findViewById(R.id.securitydeposit);
        cleaningfee = (TextView) findViewById(R.id.cleaningfee);
        commonMethods.setTvAlign(additionalprice_close,this);

        Intent x = getIntent();
        String additionalprices = x.getStringExtra("additionalprice");
        currencysymbol = x.getStringExtra("currency_symbol");
        weekendPrice = x.getStringExtra("Weekend_price");
        Bundle bundle = x.getExtras();
        try {
            lengthOfStays.addAll((ArrayList<RoomLengthOfStay>) x.getSerializableExtra("length_of_stay_rules"));
            earlyBirdList.addAll((ArrayList<RoomLengthOfStay>) x.getSerializableExtra("early_bird_rules"));
            lastMinList.addAll((ArrayList<RoomLengthOfStay>) x.getSerializableExtra("last_min_rules"));

            if (lengthOfStays.size() <= 0) length_of_stay.setVisibility(View.GONE);
            if (earlyBirdList.size() <= 0) early_bird.setVisibility(View.GONE);
            if (lastMinList.size() <= 0) last_min.setVisibility(View.GONE);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        String[] additionalprice = additionalprices.split(",");

        String extraPeople = additionalprice[0].replace(currencysymbol, " ");
        String weekly = additionalprice[1].replace(currencysymbol, " ");
        String monthly = additionalprice[2].replace(currencysymbol, " ");
        String security = additionalprice[3].replace(currencysymbol, " ");
        String cleaning = additionalprice[4].replace(currencysymbol, " ");

        String weekendprice_val = currencysymbol+weekendPrice;

        System.out.println("additionalprice[0] " + additionalprice[0]);

        if (Float.valueOf(extraPeople) > 0) {
            extrapeople.setText(additionalprice[0]);
        } else {
            lltExtraPeople.setVisibility(View.GONE);
        }

        if (Float.valueOf(weekly) > 0) {
            weeklyprice.setText(additionalprice[1]);
        } else {
            lltweekly.setVisibility(View.GONE);
        }

        if (Float.valueOf(monthly) > 0) {
            monthlyprice.setText(additionalprice[2]);
        } else {
            lltMonthy.setVisibility(View.GONE);
        }

        if (Float.valueOf(security) > 0) {
            securitydeposit.setText(additionalprice[3]);
        } else {
            lltSecurity.setVisibility(View.GONE);
        }

        if (Float.valueOf(cleaning) > 0) {
            cleaningfee.setText(additionalprice[4]);
        } else {
            lltCleaning.setVisibility(View.GONE);
        }

        if(Float.valueOf(weekendPrice) > 0){
            weekendprice.setText(weekendprice_val);
        }else{
            lltweekend.setVisibility(View.GONE);
        }


        //extrapeople.setText(additionalprice[0]);
        //weeklyprice.setText(additionalprice[1]);
        //monthlyprice.setText(additionalprice[2]);
        //securitydeposit.setText(additionalprice[3]);
        //cleaningfee.setText(additionalprice[4]);

        lengthofstayadapter = new LengthOfStayAdapter(this, lengthOfStays);
        earlyBirdAdapter = new EarlyBirdAdapter(this, earlyBirdList);
        lastMinAdapter = new LastMinAdapter(this, lastMinList);

        length_of_stay_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        length_of_stay_list.setAdapter(lengthofstayadapter);

        early_bird_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        early_bird_list.setAdapter(earlyBirdAdapter);

        last_min_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        last_min_list.setAdapter(lastMinAdapter);


// On Click function used to click action for check Email id in server send link to Email
        additionalprice_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        length_of_stay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (length_of_stay_show) {
                    length_of_stay_list.setVisibility(View.VISIBLE);
                    length_of_stay_arrow.setText("u");
                    length_of_stay_show = false;
                } else {
                    length_of_stay_list.setVisibility(View.GONE);
                    length_of_stay_arrow.setText("t");
                    length_of_stay_show = true;
                }
            }
        });

        early_bird.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (early_bird_show) {
                    early_bird_list.setVisibility(View.VISIBLE);
                    early_bird_arrow.setText("u");
                    early_bird_show = false;
                } else {
                    early_bird_list.setVisibility(View.GONE);
                    early_bird_arrow.setText("t");
                    early_bird_show = true;
                }
            }
        });

        last_min.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (last_min_show) {
                    last_min_list.setVisibility(View.VISIBLE);
                    last_min_arrow.setText("u");
                    last_min_show = false;
                } else {
                    last_min_list.setVisibility(View.GONE);
                    last_min_arrow.setText("t");
                    last_min_show = true;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
