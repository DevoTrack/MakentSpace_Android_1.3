package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestRequestActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.BOOKINGMESSAGE;

/* ***********************************************************************
This is Request Page Contain Request the details
**************************************************************************  */
public class RequestActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {


    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    LinearLayout request_house_rules, request_message_host, request_payment;
    Button request_book;
    ImageView request_close;
    LocalSharedPreferences localSharedPreferences;
    int request_count = 0;
    String isInstantBook;
    String userid, roomid;
    String room_name, bedroom, bathroom, location, checkin_detail, checkout_detail, searchguest, searchcheckin, searchcheckout;
    String[] blocked_dates;
    String searchstartdate, searchenddate;
    String room_type, host_user_name, description, host_user_image, nightcounts, guestcounts, total_price, currency_code, currency_symbol;
    String nightfee, servicefee, securityfee, cleaningfee, additionalguestfee, totalfee;
    TextView request_title, request_room, request_name, request_room_description, request_details_hometype, request_details_hostedby;
    TextView nightcount, guestcount, request_checkin, request_checkout, request_amount, request_currencycode;
    TextView reqpayment, reqmessage, reqhouserules, total_amount;
    LinearLayout request_layout, request_night, request_guest;
    RelativeLayout request_date, price_dreak, price_dreak_inside;
    String houserules_content;
    int payment, houserules, messagehost;
    String PaymentType;
    String length_of_stay_type, length_of_stay_discount, length_of_stay_discount_price, booked_period_type, booked_period_discount, booked_period_discount_price;
    ImageView request_dotloader, price_dotloader, request_details_hostprofile;
    protected boolean isInternetAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods = new CommonMethods();
        Log.e("RequestActivity","RequestActivity");

        request_book = (Button) findViewById(R.id.request_book);
        request_close = (ImageView) findViewById(R.id.request_close);
        request_payment = (LinearLayout) findViewById(R.id.request_payment);
        request_house_rules = (LinearLayout) findViewById(R.id.request_house_rules);
        request_message_host = (LinearLayout) findViewById(R.id.request_message_host);
        request_date = (RelativeLayout) findViewById(R.id.request_date);
        request_night = (LinearLayout) findViewById(R.id.request_night);
        request_guest = (LinearLayout) findViewById(R.id.request_guest);
        price_dreak = (RelativeLayout) findViewById(R.id.price_dreak);
        price_dreak_inside = (RelativeLayout) findViewById(R.id.price_dreak_inside);
        total_amount = (TextView) findViewById(R.id.total_amount);

        reqpayment = (TextView) findViewById(R.id.reqpayment);
        reqmessage = (TextView) findViewById(R.id.reqmessage);
        reqhouserules = (TextView) findViewById(R.id.reqhouserules);
        commonMethods.rotateArrow(request_close, this);

        request_book.setOnClickListener(this);
        request_close.setOnClickListener(this);
        request_payment.setOnClickListener(this);
        request_house_rules.setOnClickListener(this);
        request_message_host.setOnClickListener(this);
        request_date.setOnClickListener(this);
        request_night.setOnClickListener(this);
        request_guest.setOnClickListener(this);
        price_dreak.setOnClickListener(this);

        Intent x = getIntent();
        room_name = x.getStringExtra("roomname");
        bedroom = x.getStringExtra("bedroom");
        bathroom = x.getStringExtra("bathroom");
        blocked_dates = x.getStringArrayExtra("blockdate");
        location = x.getStringExtra("location");

        localSharedPreferences = new LocalSharedPreferences(this);


        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        checkin_detail = localSharedPreferences.getSharedPreferences(Constants.CheckInDetail);
        checkout_detail = localSharedPreferences.getSharedPreferences(Constants.CheckOutDetail);

        searchguest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);

        if (searchguest == null) {
            searchguest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
            if (searchguest == null) searchguest = "1";
        }

        isInstantBook = localSharedPreferences.getSharedPreferences(Constants.isInstantBook);

        houserules_content = localSharedPreferences.getSharedPreferences(Constants.HouseRules);

        payment = localSharedPreferences.getSharedPreferencesInt(Constants.stepPayment);
        houserules = localSharedPreferences.getSharedPreferencesInt(Constants.stepHouserules);
        messagehost = localSharedPreferences.getSharedPreferencesInt(Constants.stepHostmessage);

        if (payment == 1) {
            PaymentType = localSharedPreferences.getSharedPreferences(Constants.ReqPaymentType);

            if(PaymentType.equals("Credit Card")){
                reqpayment.setText(getResources().getString(R.string.creditcard));
            }else if(PaymentType.equals("PayPal")){
                reqpayment.setText(getResources().getString(R.string.paypal));
            }


        }

        if (houserules == 1) {
            reqhouserules.setText(getResources().getString(R.string.agreed));
        }
        if (messagehost == 1) {
            reqmessage.setText(getResources().getString(R.string.done));
        }else {
            reqmessage.setText(getResources().getString(R.string.add));
        }

        if (houserules_content != null) {
            request_count = payment + houserules + messagehost;
            request_house_rules.setVisibility(View.VISIBLE);
        } else {
            request_count = payment + messagehost + 1;
            request_house_rules.setVisibility(View.GONE);
        }

        System.out.println("Total Value" + request_count);
        if (request_count == 0) {
            request_book.setText(R.string.setps_left3);
        } else if (request_count == 1) {
            request_book.setText(R.string.setps_left2);
        } else if (request_count == 2) {
           request_book.setText(R.string.setps_left1);
            //request_book.setText(R.string.step_left);
        } else {
            request_book.setText(R.string.requestbook);
        }

        request_layout = (LinearLayout) findViewById(R.id.request_layout);
        request_dotloader = (ImageView) findViewById(R.id.request_dotloader);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(request_dotloader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        request_layout.setVisibility(View.GONE);

        price_dotloader = (ImageView) findViewById(R.id.price_dotloader);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(price_dotloader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);
        price_dotloader.setVisibility(View.GONE);
        request_title = (TextView) findViewById(R.id.request_title);
        request_room_description = (TextView) findViewById(R.id.request_room_description);
        request_room = (TextView) findViewById(R.id.request_room);
        request_name = (TextView) findViewById(R.id.request_name);
        request_details_hometype = (TextView) findViewById(R.id.request_details_hometype);
        request_details_hostedby = (TextView) findViewById(R.id.request_details_hostedby);

        request_amount = (TextView) findViewById(R.id.request_amount);
        request_currencycode = (TextView) findViewById(R.id.request_currencycode);
        request_details_hostedby.setOnClickListener(this);
        request_details_hostprofile.setOnClickListener(this);

        //commonMethods.setTvAlign(nightcount,this);

        request_title.setText(room_name);
        String bedrooms;
        String bathrooms;
        if (bedroom == null || bedroom.equals("")) {
            bedroom = "0";
        }

        if (bathroom == null || bathroom.equals("")) {
            bathroom = "0";
        }

        if (searchguest == null || searchguest.equals("")) {
            searchguest = "0";
        }

        if (Integer.parseInt(bedroom) > 1) {
            bedrooms = getResources().getString(R.string.s_bedroom);
        } else {
            bedrooms = getResources().getString(R.string.bedroom);
        }
        if (Float.valueOf(bathroom) > 1) {
            bathrooms = getResources().getString(R.string.s_bathroom);
        } else {
            bathrooms = getResources().getString(R.string.bathroom);
        }

        request_room.setText(bedroom + " " + bedrooms + " . " + bathroom + " " + bathrooms);
        request_checkin.setText(checkin_detail);
        request_checkout.setText(checkout_detail);

        if (Integer.parseInt(searchguest) > 1) {
            guestcount.setText(searchguest + " " + getResources().getString(R.string.guests));
        } else {
            guestcount.setText(searchguest + " " + getResources().getString(R.string.s_guest));
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("isCheckAvailability " + localSharedPreferences.getSharedPreferences(Constants.isCheckAvailability));

        if (!localSharedPreferences.getSharedPreferences(Constants.isCheckAvailability).equals("1")) {
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);
        }

        if (getIntent().getIntExtra("ReqBack", 0) == 1) {
            localSharedPreferences.saveSharedPreferences(Constants.REQBACK, "goback");
        }
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchguest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        checkin_detail = localSharedPreferences.getSharedPreferences(Constants.CheckInDetail);
        checkout_detail = localSharedPreferences.getSharedPreferences(Constants.CheckOutDetail);

        request_checkin.setText(checkin_detail);
        request_checkout.setText(checkout_detail);
        if (searchguest == null) {
            searchguest = "1";
        }
        if (Integer.parseInt(searchguest) > 1) {
            guestcount.setText(searchguest + " " + getResources().getString(R.string.guests));
        } else {
            guestcount.setText(searchguest + " " + getResources().getString(R.string.s_guest));
        }

        price_dotloader.setVisibility(View.VISIBLE);
        price_dreak_inside.setVisibility(View.INVISIBLE);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            prePayment();
        } else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, request_guest, getResources(), this);
        }

        houserules_content = localSharedPreferences.getSharedPreferences(Constants.HouseRules);

        payment = localSharedPreferences.getSharedPreferencesInt(Constants.stepPayment);
        houserules = localSharedPreferences.getSharedPreferencesInt(Constants.stepHouserules);
        messagehost = localSharedPreferences.getSharedPreferencesInt(Constants.stepHostmessage);

        if (payment == 1) {
            PaymentType = localSharedPreferences.getSharedPreferences(Constants.ReqPaymentType);
            if(PaymentType.equals("Credit Card")){
                reqpayment.setText(getResources().getString(R.string.creditcard));
            }else if(PaymentType.equals("PayPal")) {
                reqpayment.setText(getResources().getString(R.string.paypal));
            }
        }

        if (houserules == 1) {
            reqhouserules.setText(getResources().getString(R.string.agreed));
        }
        if (messagehost == 1) {
            reqmessage.setText(getResources().getString(R.string.done));
        }else {
            reqmessage.setText(getResources().getString(R.string.add));
        }
        if (houserules_content != null) {
            request_count = payment + houserules + messagehost;
            request_house_rules.setVisibility(View.VISIBLE);
        } else {
            request_count = payment + messagehost + 1;
            request_house_rules.setVisibility(View.GONE);
        }
        System.out.println("Total Value" + request_count);
        if (request_count == 0) {
            request_book.setText(R.string.setps_left3);
        } else if (request_count == 1) {
            request_book.setText(R.string.setps_left2);
           // request_book.setText(R.string.step_left);
        } else if (request_count == 2) {
           // request_book.setText(R.string.setps_left1);
            request_book.setText(R.string.step_left);
        } else {
            if (isInstantBook != null && isInstantBook.equals("Yes")) {
                request_book.setText(getResources().getString(R.string.instantbook));
            } else {
                request_book.setText(getResources().getString(R.string.requestbook));
            }
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.request_details_hostedby: {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                x.putExtra("otheruserid", localSharedPreferences.getSharedPreferences(Constants.HostUser));
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(x,bndlanimation);
            }
            break;
            case R.id.rltHostProfile: {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                x.putExtra("otheruserid", localSharedPreferences.getSharedPreferences(Constants.HostUser));
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(x,bndlanimation);
            }
            break;
            case R.id.lltRequestMessageHost: {
                Intent x = new Intent(getApplicationContext(), MessageToHostActivity.class);
                x.putExtra("hostprofile", host_user_image);
                x.putExtra(BOOKINGMESSAGE,"");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.request_house_rules: {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestrRoom, 1);
                Intent x = new Intent(getApplicationContext(), HouseRulesActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.request_payment: {
//                Intent x = new Intent(getApplicationContext(), PaymentActivity.class);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                startActivity(x, bndlanimation);
                Intent x = new Intent(getApplicationContext(),PaymentCountryList.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(x,bndlanimation);
            }
            break;
            case R.id.request_close: {
                localSharedPreferences.saveSharedPreferences(Constants.stepHouserules, 0);
                localSharedPreferences.saveSharedPreferences(Constants.stepPayment, 0);
                localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 0);
                onBackPressed();
            }
            break;
            case R.id.request_book:
                if (request_count == 3) {
                    String weburl, baseurl = getResources().getString(R.string.baseurl);
                    String Country = localSharedPreferences.getSharedPreferences(Constants.CountryName);
                    String message = localSharedPreferences.getSharedPreferences(Constants.ReqMessage);
                    PaymentType = localSharedPreferences.getSharedPreferences(Constants.ReqPaymentType);

                    if (isInstantBook.equals("Yes")) {
                        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
                        String url = "book_now?room_id=" + roomid + "&check_in=" + searchcheckin + "&check_out=" + searchcheckout + "&number_of_guests=" + searchguest + "&country=" + Country + "&card_type=" + PaymentType + "&message=" + message + "&payment_booking_type=" + "instant_book" + "&token=" + userid + "&language=" + langCode;
                        weburl = baseurl + url;
                        weburl = weburl.replaceAll(" ", "%20");
                        Intent x = new Intent(getApplicationContext(), PaymentWebView.class);
                        x.putExtra("weburl", weburl);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                        startActivity(x, bndlanimation);
                    } else {
                        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
                        String url = "book_now?room_id=" + roomid + "&check_in=" + searchcheckin + "&check_out=" + searchcheckout + "&number_of_guests=" + searchguest + "&country=" + Country + "&card_type=" + PaymentType + "&message=" + message + "&token=" + userid + "&language=" + langCode;
                        weburl = baseurl + url;
                        weburl = weburl.replaceAll(" ", "%20");
                        Intent x = new Intent(getApplicationContext(), PaymentWebView.class);
                        x.putExtra("weburl", weburl);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                        startActivity(x, bndlanimation);
                    }
                }
                else if(request_count==0) {
                    Toast.makeText(getApplicationContext(), "3" + " " + getResources().getString(R.string.steps_remain), Toast.LENGTH_SHORT).show();
                }else if(request_count==1) {
                    Toast.makeText(getApplicationContext(),"2"+" "+getResources().getString(R.string.steps_remain),Toast.LENGTH_SHORT).show();
                }else if(request_count==2){
                    Toast.makeText(getApplicationContext(),"1"+" "+getResources().getString(R.string.step_remain),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.request_date: {
                Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                x.putExtra("blockdate", blocked_dates);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.request_night: {
                Intent x = new Intent(getApplicationContext(), CalendarActivity.class);
                localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
                x.putExtra("blockdate", blocked_dates);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.request_guest: {
                Intent x = new Intent(getApplicationContext(), Search_Guest_Bed.class);
                x.putExtra("guest", "guest");
                x.putExtra("guestcounts", guestcounts);
                startActivity(x);
            }
            break;
            case R.id.price_dreak: {
                Intent x = new Intent(getApplicationContext(), PriceBreak.class);
                x.putExtra("isHost", 0);
                x.putExtra("title", getResources().getString(R.string.pricebreakdown));
                x.putExtra("nightfee", nightfee);
                x.putExtra("servicefee", servicefee);
                x.putExtra("securityfee", securityfee);
                x.putExtra("cleaningfee", cleaningfee);
                x.putExtra("additionalguestfee", additionalguestfee);
                x.putExtra("totalfee", totalfee);
                x.putExtra("nightcounts", nightcounts);
                x.putExtra("currency_code", currency_code);
                x.putExtra("currency_symbol", currency_symbol);
                x.putExtra("location", location);
                x.putExtra("length_of_stay_type", length_of_stay_type);
                x.putExtra("length_of_stay_discount", length_of_stay_discount);
                x.putExtra("length_of_stay_discount_price", length_of_stay_discount_price);
                x.putExtra("booked_period_type", booked_period_type);
                x.putExtra("booked_period_discount", booked_period_discount);
                x.putExtra("booked_period_discount_price", booked_period_discount_price);

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
        }
    }

    /**
     * @Reference Get amenities list from server
     */
    public void prePayment() {
        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
        if ((searchstartdate != null && searchenddate != null) && (!searchstartdate.equals("") && !searchenddate.equals("")) && (!searchstartdate.equals("null") && !searchenddate.equals("null"))) {
            searchcheckin = searchstartdate;
            searchcheckout = searchenddate;
        }
        apiService.prePayment(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid, searchcheckin, searchcheckout, searchguest).enqueue(new RequestCallback(this));
    }

    public void getRequestResult(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            room_type = response.getString("room_type");
            description = response.getString("description");

            host_user_name = response.getString("host_user_name");


            if(host_user_name.length() > 15){
                String[] name = host_user_name.split(" ");
                request_details_hostedby.setText(name[0]);
            } else{
                request_details_hostedby.setText(host_user_name);
            }


            host_user_image = response.getString("host_user_thumb_image");
            request_details_hometype.setText(room_type);

            request_room_description.setText(description);


            Glide.with(getApplicationContext()).asBitmap().load(host_user_image).into(new BitmapImageViewTarget(request_details_hostprofile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    request_details_hostprofile.setImageDrawable(circularBitmapDrawable);
                }
            });

            nightcounts = response.getString("nights_count");
            guestcounts = response.getString("rooms_total_guest");
            nightcount.setText(nightcounts);

            total_price = response.getString("total_price");
            currency_code = response.getString("currency_code");
            currency_symbol = response.getString("currency_symbol");

            String currencysymbol = Html.fromHtml(currency_symbol).toString();

            request_amount.setText(currencysymbol + " " + total_price);
            //total_amount.setText(getResources().getString(R.string.total) + " " + currencysymbol);
            total_amount.setText(getResources().getString(R.string.total));

            request_currencycode.setText(currency_code);

            nightfee = response.getString("per_night_price");
            servicefee = response.getString("service_fee");
            securityfee = response.getString("security_fee");
            cleaningfee = response.getString("cleaning_fee");
            additionalguestfee = response.getString("additional_guest");
            totalfee = total_price;

            length_of_stay_type = response.getString("length_of_stay_type");
            length_of_stay_discount = response.getString("length_of_stay_discount");
            length_of_stay_discount_price = response.getString("length_of_stay_discount_price");
            booked_period_type = response.getString("booked_period_type");
            booked_period_discount = response.getString("booked_period_discount");
            booked_period_discount_price = response.getString("booked_period_discount_price");

            request_layout.setVisibility(View.VISIBLE);
            request_dotloader.setVisibility(View.GONE);
            price_dotloader.setVisibility(View.GONE);
            price_dreak_inside.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, request_guest, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
            getRequestResult(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            String statusMessage = jsonResp.getStatusMsg();

            if (statusMessage.contains("Available guests count")) {
                String msg[] = statusMessage.split(" ");
                localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, msg[msg.length - 1]);
            } else {
                onBackPressed();
            }

            /*else if(statusMessage.equals("You Can Not Book Your Own Listing")) {
                Intent rooms = new Intent(this,Room_details);
                startActivity(rooms);
            }*/
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, request_guest, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, request_guest, getResources(), this);
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
