package com.makent.trioangle.host.optionaldetails;

/**
 * @package com.makent.trioangle
 * @subpackage host/optionaldetails
 * @category OD_LongTermPrice
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
Optional details for room weekly and monthly pricing details and other pricing details
*************************************************************** */
public class OD_LongTermPrice extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    private RelativeLayout longterm_title;
    private RelativeLayout longtermprice_title;
    private RelativeLayout weekly_price;
    private RelativeLayout monthly_price;
    private RelativeLayout cleaningfee;
    private RelativeLayout additionalfee;
    private RelativeLayout guestafter;
    private RelativeLayout securitydeposit;
    private RelativeLayout weekendprice;
    private EditText weeklyprice_edittext;
    private EditText monthlyprice_edittext;
    private EditText cleaningfee_edittext;
    private EditText additionalfee_edittext;
    private EditText guestafter_edittext;
    private EditText securitydeposit_edittext;
    private EditText weekendprice_edittext;
    private TextView weeklyprice_symbol;
    private TextView monthlyprice_symbol;
    private TextView cleaningfee_symbol;
    private TextView additionalfee_symbol;
    private TextView guestafter_symbol;
    private TextView securitydeposit_symbol;
    private TextView weekendprice_symbol;

    private ImageView longterm_dot_loader;

    public String userid;
    public String roomid;
    public String currencysymbol;
    public String weekly_prices;
    public String monthly_prices;
    public String cleaning_fee;
    public String additional_guests;
    public String for_each_guest;
    public String security_deposit;
    public String weekend_pricing;

    public LocalSharedPreferences localSharedPreferences;

    public Typeface font1;
    public Drawable icon, icon1, minusenable, minusdisable, plusenable, plusdisable;

    public ImageView guestplus, guestminues;
    public TextView guest_text;
    public int guestcount = 1;
    protected boolean isInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od_long_term_price);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent4));
        icon = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4checknormal), font1)
                .sizeDp(30)
                .colorRes(R.color.text_shadow);
        icon1 = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4check), font1)
                .sizeDp(30)
                .colorRes(R.color.red_text);
        minusenable = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30)
                .colorRes(R.color.red_text);
        plusenable = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30)
                .colorRes(R.color.red_text_disable);
        minusdisable = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4checkminus), font1)
                .sizeDp(30)
                .colorRes(R.color.red_text_disable);
        plusdisable = new FontIconDrawable(OD_LongTermPrice.this, getResources().getString(R.string.f4checkplus), font1)
                .sizeDp(30)
                .colorRes(R.color.red_text_disable);

        guest_text = (TextView) findViewById(R.id.odguesttext);
        guestplus = (ImageView) findViewById(R.id.odguestplus);
        guestminues = (ImageView) findViewById(R.id.odguestminus);
        guest_text.setKeyListener(null);
        guestplus.setOnKeyListener(null);
        guestminues.setOnKeyListener(null);

//        guest_text.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int inType = guest_text.getInputType(); // backup the input type
//                guest_text.setInputType(InputType.TYPE_NULL); // disable soft input
//                guest_text.onTouchEvent(event); // call native handler
//                guest_text.setInputType(inType); // restore input type
//                return true; // consume touch even
//            }
//        });



        plusMinus(guestminues, guestplus);

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        currencysymbol = localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);

        weekly_prices = localSharedPreferences.getSharedPreferences(Constants.ListWeeklyPrice);
        monthly_prices = localSharedPreferences.getSharedPreferences(Constants.ListMonthlyPrice);
        cleaning_fee = localSharedPreferences.getSharedPreferences(Constants.ListCleaningFee);
        additional_guests = localSharedPreferences.getSharedPreferences(Constants.ListAdditionalGuest);
        for_each_guest = localSharedPreferences.getSharedPreferences(Constants.ListGuestAfter);
        security_deposit = localSharedPreferences.getSharedPreferences(Constants.ListSecurityDeposit);
        weekend_pricing = localSharedPreferences.getSharedPreferences(Constants.ListWeekendPrice);

        longterm_dot_loader = (ImageView) findViewById(R.id.longterm_dot_loader);
        longterm_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(longterm_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        longterm_title = (RelativeLayout) findViewById(R.id.longterm_title);

        longtermprice_title = (RelativeLayout) findViewById(R.id.longtermprice_title);
        weekly_price = (RelativeLayout) findViewById(R.id.weeklyPrice);
        monthly_price = (RelativeLayout) findViewById(R.id.monthlyPrice);
        cleaningfee = (RelativeLayout) findViewById(R.id.cleaningfee);
        additionalfee = (RelativeLayout) findViewById(R.id.additionalfee);
        guestafter = (RelativeLayout) findViewById(R.id.guestafter);
        securitydeposit = (RelativeLayout) findViewById(R.id.securitydeposit);
        weekendprice = (RelativeLayout) findViewById(R.id.weekendprice);

        weeklyprice_edittext = (EditText) findViewById(R.id.weeklyprice_edittext);
        monthlyprice_edittext = (EditText) findViewById(R.id.monthlyprice_edittext);
        cleaningfee_edittext = (EditText) findViewById(R.id.cleaningfee_edittext);
        additionalfee_edittext = (EditText) findViewById(R.id.additional_guest_edittext);
        guestafter_edittext = (EditText) findViewById(R.id.guestafter_edittext);
        securitydeposit_edittext = (EditText) findViewById(R.id.securitydeposit_edittext);
        weekendprice_edittext = (EditText) findViewById(R.id.weekendprice_edittext);

        setValidateAction(weeklyprice_edittext);
        setValidateAction(monthlyprice_edittext);
        setValidateAction(cleaningfee_edittext);
        setValidateAction(additionalfee_edittext);
        setValidateAction(guestafter_edittext);
        setValidateAction(securitydeposit_edittext);
        setValidateAction(weekendprice_edittext);

        weeklyprice_symbol = (TextView) findViewById(R.id.weeklyprice_symbol);
        monthlyprice_symbol = (TextView) findViewById(R.id.monthlyprice_symbol);
        cleaningfee_symbol = (TextView) findViewById(R.id.cleaningfee_symbol);
        additionalfee_symbol = (TextView) findViewById(R.id.additional_guest_symbol);
        guestafter_symbol = (TextView) findViewById(R.id.guestafter_symbol);
        securitydeposit_symbol = (TextView) findViewById(R.id.securitydeposit_symbol);
        weekendprice_symbol = (TextView) findViewById(R.id.weekendprice_symbol);

        weeklyprice_symbol.setText(currencysymbol);
        monthlyprice_symbol.setText(currencysymbol);
        cleaningfee_symbol.setText(currencysymbol);
        additionalfee_symbol.setText(currencysymbol);
        securitydeposit_symbol.setText(currencysymbol);
        weekendprice_symbol.setText(currencysymbol);


        loadPriceData();

        guestminues.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (guestcount > 1) {
                    guestcount--;
                }
                enablebuttons();
            }
        });


        guestplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                guestcount++;
                // System.out.println("Bed Count"+bedcount);

                enablebuttons();
            }
        });

        longtermprice_title.setOnClickListener(this);
        weekly_price.setOnClickListener(this);
        monthly_price.setOnClickListener(this);
        cleaningfee.setOnClickListener(this);
        additionalfee.setOnClickListener(this);
        guestafter.setOnClickListener(this);
        securitydeposit.setOnClickListener(this);
        weekendprice.setOnClickListener(this);
        longterm_title.setOnClickListener(this);

        weeklyprice_edittext.setOnClickListener(this);
        monthlyprice_edittext.setOnClickListener(this);
        cleaningfee_edittext.setOnClickListener(this);
        additionalfee_edittext.setOnClickListener(this);
        guestafter_edittext.setOnClickListener(this);
        securitydeposit_edittext.setOnClickListener(this);
        weekendprice_edittext.setOnClickListener(this);
    }

    public void enablebuttons() {
        if (guestcount == 1) {
            guest_text.setText(String.valueOf(guestcount));
            guestminues.setEnabled(false);
            guestplus.setEnabled(true);
        } else if (guestcount >= 16) {
            guestminues.setEnabled(true);
            guestplus.setEnabled(false);
            guest_text.setText(String.valueOf(guestcount));
        } else {
            guest_text.setText(String.valueOf(guestcount));
            guestminues.setEnabled(true);
            guestplus.setEnabled(true);
        }
    }



    public void getPriceData() {
        isInternetAvailable = getNetworkState().isConnectingToInternet();

        if (isInternetAvailable) {
            weekly_prices = weeklyprice_edittext.getText().toString();
            weekly_prices = weekly_prices.replaceAll("^\\s+|\\s+$", "");
           /* if (weekly_prices.equals("")) {
                weekly_prices = "0";
            }*/

            monthly_prices = monthlyprice_edittext.getText().toString();
            monthly_prices = monthly_prices.replaceAll("^\\s+|\\s+$", "");
            /*if (monthly_prices.equals("")) {
                monthly_prices = "0";
            }*/

            cleaning_fee = cleaningfee_edittext.getText().toString();
            cleaning_fee = cleaning_fee.replaceAll("^\\s+|\\s+$", "");
            if (cleaning_fee.equals("")) {
                cleaning_fee = "0";
            }

            additional_guests = additionalfee_edittext.getText().toString();
            additional_guests = additional_guests.replaceAll("^\\s+|\\s+$", "");
            if (additional_guests.equals("")) {
                additional_guests = "0";
            }

            for_each_guest = guest_text.getText().toString();
            for_each_guest = for_each_guest.replaceAll("^\\s+|\\s+$", "");
            if (for_each_guest.equals("")) {
                for_each_guest = "1";
            }

            security_deposit = securitydeposit_edittext.getText().toString();
            security_deposit = security_deposit.replaceAll("^\\s+|\\s+$", "");
            if (security_deposit.equals("")) {
                security_deposit = "0";
            }

            weekend_pricing = weekendprice_edittext.getText().toString();
            weekend_pricing = weekend_pricing.replaceAll("^\\s+|\\s+$", "");
            if (weekend_pricing.equals("")) {
                weekend_pricing = "0";
            }

            updateMinMax();

        } else {
            commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,additionalfee_edittext,getResources(),OD_LongTermPrice.this);
        }
    }

    // Load pricing data from API
    public void loadPriceData() {
        System.out.println("Room cleaning_feeup" + localSharedPreferences.getSharedPreferences(Constants.ListCleaningFee));
        System.out.println("Room additional_guestsup" + localSharedPreferences.getSharedPreferences(Constants.ListAdditionalGuest));
        System.out.println("Room weekly_prices" + weekly_prices);
        System.out.println("Room weekly_prices" + weekly_prices);
        System.out.println("Room monthly_prices" + monthly_prices);
        System.out.println("Room cleaning_fee" + cleaning_fee);
        System.out.println("Room additional_guests" + additional_guests);
        System.out.println("Room for_each_guest" + for_each_guest);
        System.out.println("Room security_deposit" + security_deposit);
        System.out.println("Room weekend_pricing" + weekend_pricing);

        if (weekly_prices != null && !weekly_prices.equals("0"))//||!weekly_prices.equals(""))
        {
            weeklyprice_edittext.setText(weekly_prices);
        } else {
            weeklyprice_edittext.setText("");
        }

        if (monthly_prices != null && !monthly_prices.equals("0"))//||!monthly_prices.equals(""))
        {
            monthlyprice_edittext.setText(monthly_prices);
        } else {
            monthlyprice_edittext.setText("");
        }

        if (cleaning_fee != null && !cleaning_fee.equals("0"))//||!cleaning_fee.equals(""))
        {
            cleaningfee_edittext.setText(cleaning_fee);
        } else {
            // cleaningfee_edittext.setText("0");
        }

        if (additional_guests != null && !additional_guests.equals("0"))//||!additional_guests.equals(""))
        {
            additionalfee_edittext.setText(additional_guests);
        } else {
            // additionalfee_edittext.setText("0");
        }

        if (for_each_guest != null)//||!for_each_guest.equals(""))
        {
            // guestafter_edittext.setText(for_each_guest);
            if (for_each_guest.equals("0")) {
                guestcount = 1;
            } else {
                guestcount = Integer.parseInt(for_each_guest);
            }
            enablebuttons();
        } else {
            // guestafter_edittext.setText("0");
            guestcount = 1;
            enablebuttons();
        }

        if (security_deposit != null && !security_deposit.equals("0"))//||!security_deposit.equals(""))
        {
            securitydeposit_edittext.setText(security_deposit);
        } else {
            //securitydeposit_edittext.setText("0");
        }

        if (weekend_pricing != null && !weekend_pricing.equals("0"))//||!weekend_pricing.equals(""))
        {
            weekendprice_edittext.setText(weekend_pricing);
        } else {
            // weekendprice_edittext.setText("0");
        }


    }

    public void plusMinus(ImageView view, ImageView view1) {
        if (view.isEnabled()) {
            view.setBackground(minusenable);
        } else {
            view.setBackground(minusdisable);
        }
        if (view1.isEnabled()) {
            view1.setBackground(plusenable);
        } else {
            view1.setBackground(plusdisable);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.longtermprice_title: {
                //onBackPressed();
                getPriceData();
                hideKeyBoard();
            }
            case R.id.longterm_title: {
                //onBackPressed();
                getPriceData();
                hideKeyBoard();
            }
            break;
            case R.id.weeklyprice_edittext: {
                if (weeklyprice_edittext.getText().toString().equals("0")) {
                    weeklyprice_edittext.setText("");
                }
                showKeyBoard();
                weeklyprice_edittext.setFocusable(true);
                weeklyprice_edittext.requestFocus();
            }
            break;
            case R.id.monthlyprice_edittext: {
                if (monthlyprice_edittext.getText().toString().equals("0")) {
                    monthlyprice_edittext.setText("");
                }
                showKeyBoard();
                monthlyprice_edittext.setFocusable(true);
                monthlyprice_edittext.requestFocus();
            }
            case R.id.cleaningfee_edittext: {
                if (cleaningfee_edittext.getText().toString().equals("0")) {
                    cleaningfee_edittext.setText("");
                }
                showKeyBoard();
                cleaningfee_edittext.setFocusable(true);
                cleaningfee_edittext.requestFocus();
            }
            break;
            case R.id.additional_guest_symbol: {
                if (additionalfee_edittext.getText().toString().equals("0")) {
                    additionalfee_edittext.setText("");
                }
                showKeyBoard();
                additionalfee_edittext.setFocusable(true);
                additionalfee_edittext.requestFocus();
            }
            break;
            case R.id.guestafter_edittext: {
                if (guestafter_edittext.getText().toString().equals("0")) {
                    guestafter_edittext.setText("");
                }
                showKeyBoard();
                guestafter_edittext.setFocusable(true);
                guestafter_edittext.requestFocus();
            }
            break;
            case R.id.securitydeposit_edittext: {
                if (securitydeposit_edittext.getText().toString().equals("0")) {
                    securitydeposit_edittext.setText("");
                }
                showKeyBoard();
                securitydeposit_edittext.setFocusable(true);
                securitydeposit_edittext.requestFocus();
            }
            break;
            case R.id.weekendprice_edittext: {
                if (weekendprice_edittext.getText().toString().equals("0")) {
                    weekendprice_edittext.setText("");
                }
                showKeyBoard();
                weekendprice_edittext.setFocusable(true);
                weekendprice_edittext.requestFocus();
            }
            break;

            case R.id.weeklyPrice: {
                if (weeklyprice_edittext.getText().toString().equals("0")) {
                    weeklyprice_edittext.setText("");
                }
                showKeyBoard();
                weeklyprice_edittext.setFocusable(true);
                weeklyprice_edittext.requestFocus();
            }
            break;
            case R.id.monthlyPrice: {
                if (monthlyprice_edittext.getText().toString().equals("0")) {
                    monthlyprice_edittext.setText("");
                }
                showKeyBoard();
                monthlyprice_edittext.setFocusable(true);
                monthlyprice_edittext.requestFocus();
            }
            break;
            case R.id.cleaningfee: {
                if (cleaningfee_edittext.getText().toString().equals("0")) {
                    cleaningfee_edittext.setText("");
                }
                showKeyBoard();
                cleaningfee_edittext.setFocusable(true);
                cleaningfee_edittext.requestFocus();
            }
            break;
            case R.id.additionalfee: {
                if (additionalfee_edittext.getText().toString().equals("0")) {
                    additionalfee_edittext.setText("");
                }
                showKeyBoard();
                additionalfee_edittext.setFocusable(true);
                additionalfee_edittext.requestFocus();
            }
            break;
            case R.id.guestafter: {
                if (guestafter_edittext.getText().toString().equals("0")) {
                    guestafter_edittext.setText("");
                }
                hideKeyBoard();
                guestafter_edittext.setFocusable(true);
                guestafter_edittext.requestFocus();
            }
            break;
            case R.id.securitydeposit: {
                if (securitydeposit_edittext.getText().toString().equals("0")) {
                    securitydeposit_edittext.setText("");
                }
                showKeyBoard();
                securitydeposit_edittext.setFocusable(true);
                securitydeposit_edittext.requestFocus();
            }
            break;
            case R.id.weekendprice: {
                if (weekendprice_edittext.getText().toString().equals("0")) {
                    weekendprice_edittext.setText("");
                }
                showKeyBoard();
                weekendprice_edittext.setFocusable(true);
                weekendprice_edittext.requestFocus();
            }
            break;

        }
    }

    // Show keyboard
    public void showKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    // Hide keyboard
    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setValidateAction(final EditText edit_action) {
        edit_action.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (edit_action.getText().toString().equals("0")) {
                    edit_action.setText("");
                }
                showKeyBoard();
            }
        });
    }


    // Update longterm prices and other prices
    public void updateMinMax() {
        longterm_title.setVisibility(View.GONE);
        longterm_dot_loader.setVisibility(View.VISIBLE);
        apiService.updateLongTermPrices(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid, cleaning_fee, additional_guests, for_each_guest, security_deposit, weekend_pricing).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessLongterm();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, weekendprice_edittext, getResources(), this);
            longterm_title.setVisibility(View.VISIBLE);
            longterm_dot_loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, weekendprice_edittext, getResources(), this);
            longterm_title.setVisibility(View.VISIBLE);
            longterm_dot_loader.setVisibility(View.GONE);
        }

    public void onSuccessLongterm() {
        localSharedPreferences.saveSharedPreferences(Constants.ListCleaningFee, cleaning_fee);
        localSharedPreferences.saveSharedPreferences(Constants.ListAdditionalGuest, additional_guests);
        localSharedPreferences.saveSharedPreferences(Constants.ListGuestAfter, for_each_guest);
        localSharedPreferences.saveSharedPreferences(Constants.ListSecurityDeposit, security_deposit);
        localSharedPreferences.saveSharedPreferences(Constants.ListWeekendPrice, weekend_pricing);

        longterm_title.setVisibility(View.GONE);
        longterm_dot_loader.setVisibility(View.VISIBLE);

        finish();
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}

