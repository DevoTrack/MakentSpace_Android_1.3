package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestPriceBrakDownActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;

/* ***********************************************************************
This is PriceBreak Page Contain Payment Breakdown details
**************************************************************************  */
public class PriceBreak extends AppCompatActivity {

    private String travelCreditFee;
    private String nightfee;
    private String servicefee;
    private String securityfee;
    private String cleaningfee;
    private String hostfee;
    private String additionalguestfee;
    private String totalfee;
    private String couponamount;
    private String penaltyamount;
    private String nightcounts;
    private String currency_code;
    private String currency_symbol;
    private String currency_symbols;
    private String location;
    private String title;


    private TextView nightfee_txt;
    private TextView servicefee_txt;
    private TextView securityfee_txt;
    private TextView cleaningfee_txt;
    private TextView hostfee_txt;
    private TextView additionalguestfee_txt;
    private TextView totalfee_txt;
    private TextView price_currencycode;
    private TextView nightcounts_txt;
    private TextView nightprice_txt;
    private TextView pricebreak_close;
    private TextView break_title;
    private TextView travel_credit_txt;
    private TextView coupon_txt;
    private TextView penalty;
    private TextView servicefee_hint;
    private TextView totalfee_title;
    private RelativeLayout penalty_lay;
    private RelativeLayout travel_credit_lay;
    private RelativeLayout coupon_lay;
    private RelativeLayout nightfee_lay;
    private RelativeLayout servicefee_lay;
    private RelativeLayout cleaningfee_lay;
    private RelativeLayout hostfee_lay;
    private RelativeLayout additionalguestfee_lay;
    private RelativeLayout totalfee_lay;

    int isHost;
    LocalSharedPreferences localSharedPreferences;
    private String list_type;
    private String guest_count;
    private String subtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_break);

        localSharedPreferences = new LocalSharedPreferences(this);
        //isHost=localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        isHost = getIntent().getIntExtra("isHost", 0);

        Intent x = getIntent();
        title = x.getStringExtra("title");
        nightfee = x.getStringExtra("nightfee");
        servicefee = x.getStringExtra("servicefee");
        securityfee = x.getStringExtra("securityfee");
        cleaningfee = x.getStringExtra("cleaningfee");
        hostfee = x.getStringExtra("hostfee");
        additionalguestfee = x.getStringExtra("additionalguestfee");
        guest_count = x.getStringExtra("guest_count");
        totalfee = x.getStringExtra("totalfee");
        nightcounts = x.getStringExtra("nightcounts");
        currency_code = x.getStringExtra("currency_code");
        currency_symbols = x.getStringExtra("currency_symbol");
        location = x.getStringExtra("location");
        penaltyamount = x.getStringExtra("penalty_amount");
        couponamount = x.getStringExtra("coupon_amount");
        travelCreditFee = x.getStringExtra("travel_credit");
        list_type = x.getStringExtra("list_type");
        subtotal = x.getStringExtra("subtotal");

        System.out.println("Penalty amount " + penaltyamount + " and coupon amount  " + couponamount);



        currency_symbol = Html.fromHtml(currency_symbols).toString();


        servicefee_hint = (TextView) findViewById(R.id.servicefee_hint);
        penalty = (TextView) findViewById(R.id.penalty_txt);
        coupon_txt = (TextView) findViewById(R.id.coupon_txt);
        travel_credit_txt = (TextView) findViewById(R.id.travel_credit_txt);
        totalfee_title =(TextView)findViewById(R.id.totalfee_title);
      //  totalfee_title.setText(getResources().getString(R.string.total)+" " + currency_symbol);
        totalfee_title.setText(getResources().getString(R.string.total));





        break_title = (TextView) findViewById(R.id.break_title);
        nightfee_txt = (TextView) findViewById(R.id.nightfee_txt);
        nightprice_txt = (TextView) findViewById(R.id.nightprice_txt);
        servicefee_txt = (TextView) findViewById(R.id.servicefee_txt);
        securityfee_txt = (TextView) findViewById(R.id.securityfee_txt);
        cleaningfee_txt = (TextView) findViewById(R.id.cleaningfee_txt);
        hostfee_txt = (TextView) findViewById(R.id.hostfee_txt);
        additionalguestfee_txt = (TextView) findViewById(R.id.additionalguestfee_txt);
        totalfee_txt = (TextView) findViewById(R.id.totalfee_txt);
        price_currencycode = (TextView) findViewById(R.id.price_currencycode);
        nightcounts_txt = (TextView) findViewById(R.id.nightcounts_txt);
        pricebreak_close = (TextView) findViewById(R.id.pricebreak_close);

        nightfee_lay = (RelativeLayout) findViewById(R.id.nightfee);
        servicefee_lay = (RelativeLayout) findViewById(R.id.servicefee);
        cleaningfee_lay = (RelativeLayout) findViewById(R.id.cleaningfee);
        hostfee_lay = (RelativeLayout) findViewById(R.id.hostfee);
        coupon_lay = (RelativeLayout) findViewById(R.id.coupon_lay);
        travel_credit_lay = (RelativeLayout) findViewById(R.id.travel_credit_lay);
        penalty_lay = (RelativeLayout) findViewById(R.id.penalty_lay);
        additionalguestfee_lay = (RelativeLayout) findViewById(R.id.additionalguestfee);
        totalfee_lay = (RelativeLayout) findViewById(R.id.total_payment);

        break_title.setText(title);

        nightfee_lay.setVisibility(View.GONE);
        coupon_lay.setVisibility(View.GONE);
        penalty_lay.setVisibility(View.GONE);
        servicefee_lay.setVisibility(View.GONE);
        cleaningfee_lay.setVisibility(View.GONE);
        hostfee_lay.setVisibility(View.GONE);
        additionalguestfee_lay.setVisibility(View.GONE);
        totalfee_lay.setVisibility(View.GONE);
        travel_credit_lay.setVisibility(View.GONE);
        if (list_type != null && list_type.equals("Experiences")) {
            nightcounts_txt.setText(location);
            servicefee_hint.setVisibility(View.GONE);
            if (nightfee != null && !nightfee.equals("0")) {
                nightfee_lay.setVisibility(View.VISIBLE);

                    /*if (Integer.parseInt(guest_count) > 1) {
                        nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + guest_count + " " + getResources().getString(R.string.guests));
                    } else {
                        nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + guest_count + " " + getResources().getString(R.string.s_guest));
                    }*/


                String totalnighrfee = String.valueOf(Integer.parseInt(nightfee) * Integer.parseInt(guest_count));

                    nightfee_txt.setText(currency_symbol + "" + totalnighrfee);


            }

        } else {
            servicefee_hint.setVisibility(View.VISIBLE);
            if (Integer.parseInt(nightcounts) > 1) {
                nightcounts_txt.setText(nightcounts + " " + getResources().getString(R.string.nightss) + " in  " + location);

                if (nightfee != null && !nightfee.equals("0")) {
                    nightfee_lay.setVisibility(View.VISIBLE);

                        if (Integer.parseInt(nightcounts) > 1) {
                            nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + nightcounts + " " + getResources().getString(R.string.nightss));
                        } else {
                            nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + nightcounts + " " + getResources().getString(R.string.night));
                        }


                    String totalnighrfee = String.valueOf(Integer.parseInt(nightfee) * Integer.parseInt(nightcounts));

                        nightfee_txt.setText(currency_symbol + "" + totalnighrfee);

                }



            } else {
                nightcounts_txt.setText(nightcounts + " " + getResources().getString(R.string.night) + " in " + location);
            }



            if (nightfee != null && !nightfee.equals("0")) {
                nightfee_lay.setVisibility(View.VISIBLE);


                    if (Integer.parseInt(nightcounts) > 1) {
                        nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + nightcounts + " " + getResources().getString(R.string.nightss));
                    } else {
                        nightprice_txt.setText(currency_symbol + "" + nightfee + "  x " + nightcounts + " " + getResources().getString(R.string.night));
                    }


                String totalnighrfee = String.valueOf(Integer.parseInt(nightfee) * Integer.parseInt(nightcounts));
                nightfee_txt.setText(currency_symbol + "" + totalnighrfee);
            }
        }


            if (penaltyamount != null && !penaltyamount.equals("0") && !penaltyamount.equals("")) {
                penalty_lay.setVisibility(View.VISIBLE);
                penalty.setText("-" + currency_symbol + penaltyamount);
            } else {
                penalty_lay.setVisibility(View.GONE);
            }


            if (couponamount != null && !couponamount.equals("0") && !couponamount.equals("")) {
                coupon_lay.setVisibility(View.VISIBLE);
                coupon_txt.setText("-" + currency_symbol + couponamount);
            } else {
                coupon_lay.setVisibility(View.GONE);
            }



            if (travelCreditFee != null && Float.valueOf(travelCreditFee) > 0 && !travelCreditFee.equals("")) {
                travel_credit_lay.setVisibility(View.VISIBLE);
                travel_credit_txt.setText("-" + currency_symbol + travelCreditFee);
            } else {
                travel_credit_lay.setVisibility(View.GONE);
            }





            if (servicefee != null && !servicefee.equals("0")) {
                if (isHost != 1) {
                    servicefee_lay.setVisibility(View.VISIBLE);
                    servicefee_txt.setText(currency_symbol + "" + servicefee);
                }
            }



            if (securityfee != null && !securityfee.equals("0")) {
                securityfee_txt.setText(currency_symbol + "" + securityfee);
            }


            if (cleaningfee != null && !cleaningfee.equals("0")) {
                cleaningfee_lay.setVisibility(View.VISIBLE);
                cleaningfee_txt.setText(currency_symbol + "" + cleaningfee);
            }




            if (hostfee != null && !hostfee.equals("0")) {
                if (isHost == 1) {
                    hostfee_lay.setVisibility(View.VISIBLE);
                    hostfee_txt.setText("- " + currency_symbol + "" + hostfee);
                }

            }



            if (additionalguestfee != null && !additionalguestfee.equals("0")) {
                additionalguestfee_lay.setVisibility(View.VISIBLE);
                additionalguestfee_txt.setText(currency_symbol + "" + additionalguestfee);
            }



            if (totalfee != null && !totalfee.equals("0")) {
                totalfee_lay.setVisibility(View.VISIBLE);
                totalfee_txt.setText(currency_symbol + "" + totalfee);
                price_currencycode.setText(currency_code);
            }



            pricebreak_close.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        pricebreak_close.setOnClickListener(new View.OnClickListener() {

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
