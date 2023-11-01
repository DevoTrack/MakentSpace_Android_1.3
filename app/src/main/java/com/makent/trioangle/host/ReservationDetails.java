package com.makent.trioangle.host;

/**
 * @package com.makent.trioangle
 * @subpackage host/tabs
 * @category ReservationDetailsActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.chat.ChatActivity;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.profile.HelpActivity;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.CancelReservationActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.makent.trioangle.helper.Constants.RESERVATIONID;
import static com.makent.trioangle.helper.Constants.SESSIONID;
import static com.makent.trioangle.helper.Constants.USERTYPE;

// ***Experience Start***
// ***Experience End***

/* ***********************************************************************
This is  Contain Reservation Details
**************************************************************************  */
public class ReservationDetails extends AppCompatActivity {

    public @Inject
    CommonMethods commonMethods;

    Toolbar tripsubdetailstoolbar;
    TextView tsd_expire_timer, tsdroomname, tsdnightcountlocation, tsdhometype, tsdhostedby, tsdchin, tsdchout, tsd_guest, tsdroomdetail, tsdtotalcost, tsd_help, tsd_cancel, conversation, reservation_roomname;
    RelativeLayout tsd_expire_timer_lay;
    ImageView tsdhostprofile, tsdroomimage, tsd_back_arrow;
    Button tsd_booknow;
    String TripsDetails, reservationid, tripstatus, member_from, expire_timer;
    int isHost, isHosti;
    LocalSharedPreferences localSharedPreferences;
    LinearLayout total_costlay;
    private String startTime;
    private String endTime;
    private String guestCount;
    TextView end_time, time;


    String spaceId;
    String spaceName;
    String spaceLocation;
    String reservationStatus;
    String user_name;
    String user_thumb_image;
    String check_in;
    String check_out;
    String spaceImage;
    String total_cost;
    String host_user_id;
    String payment_recieved_date;
    String can_view_receipt;
    String currency_symbol;

    CountDownTimer countDownTimer = null;

    private boolean isDeletedUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        localSharedPreferences = new LocalSharedPreferences(this);
        isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        System.out.println("isHostisHost" + isHost);
        isHost = getIntent().getIntExtra("isHost", 0);
        TripsDetails = localSharedPreferences.getSharedPreferences(Constants.ReservationDetails);
        Intent x = getIntent();
        reservationid = x.getStringExtra("reservationid");
        tripstatus = x.getStringExtra("reservationtatus");
        commonMethods = new CommonMethods();

        end_time = (TextView) findViewById(R.id.end_time);
        time = (TextView) findViewById(R.id.time);

        Log.e("Details","Reservation details");

        tripsubdetailstoolbar = (Toolbar) findViewById(R.id.tripsubdetailstoolbar);

        tsd_expire_timer = (TextView) findViewById(R.id.tsd_expire_timer);
        tsd_expire_timer_lay = (RelativeLayout) findViewById(R.id.tsd_expire_timer_lay);

        tsdroomname = (TextView) findViewById(R.id.reservation_status);
        tsdnightcountlocation = (TextView) findViewById(R.id.reservation_details);
        tsdhometype = (TextView) findViewById(R.id.tripsubdetails_hometype);
        tsdhostedby = (TextView) findViewById(R.id.tripsubdetails_hostedby);
        tsdhostprofile = (ImageView) findViewById(R.id.tripsubdetails_hostprofile);
        tsdchin = (TextView) findViewById(R.id.tripsubdetails_chin);
        tsdchout = (TextView) findViewById(R.id.tripsubdetails_chout);
        tsd_guest = (TextView) findViewById(R.id.tripsubdetails_guest);
        tsdroomimage = (ImageView) findViewById(R.id.tripsubdetails_roomimage);
        tsdroomdetail = (TextView) findViewById(R.id.tripsubdetails_roomdetail);
        tsdtotalcost = (TextView) findViewById(R.id.tripsubdetails_totalcost);
        tsd_help = (TextView) findViewById(R.id.tsd_help);
        tsd_cancel = (TextView) findViewById(R.id.tsd_cancel);
        conversation = (TextView) findViewById(R.id.conversation);
        reservation_roomname = (TextView) findViewById(R.id.reservation_roomname);
        tsd_booknow = (Button) findViewById(R.id.tsd_booknow);
        tsd_back_arrow = (ImageView) findViewById(R.id.tsd_back_arrow);
        commonMethods.rotateArrow(tsd_back_arrow, this);

        tsd_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        total_costlay = (LinearLayout) findViewById(R.id.total_cost);

        getTripsDetails(TripsDetails); // this is used to get trips details  function

        tsd_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), PreAcceptActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("tripstatus", reservationStatus);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });
        tsd_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(x);
            }
        });

        tsdhostedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + host_user_id);
                x.putExtra("otheruserid", host_user_id);
                startActivity(x);
            }
        });

        tsdhometype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + host_user_id);
                x.putExtra("otheruserid", host_user_id);
                startActivity(x);
            }
        });

        tsdhostprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + host_user_id);
                x.putExtra("otheruserid", host_user_id);
                startActivity(x);
            }
        });


        tsd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), CancelReservationActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("tripstatus", reservationStatus);
                x.putExtra("fromHost",1);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), ChatActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("hostid", host_user_id);
                x.putExtra("listtype", "");
                x.putExtra("host_username", user_name);
                x.putExtra("trip_status", reservationStatus);
                x.putExtra("roomid", spaceId);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });
    }

    public void getTripsDetails(String TripsDetails) {
        try {
            JSONArray trips_jsonarray = new JSONArray(TripsDetails);
            for (int i = 0; i < trips_jsonarray.length(); i++) {

                JSONObject triptypeJSONObject = trips_jsonarray.getJSONObject(i);
                String currencysymbol;
                if (triptypeJSONObject.getString("reservation_id").equals(reservationid)) {
                    isDeletedUser = triptypeJSONObject.getBoolean("host_user_deleted");
                    System.out.println("#### deleteduser"+isDeletedUser);

                    if (isHost == 1) {
                        spaceId = triptypeJSONObject.getString("space_id");
                        spaceName = triptypeJSONObject.getString("space_name");
                        spaceLocation = triptypeJSONObject.getString("space_location");

                        reservationStatus = triptypeJSONObject.getString("reservation_status");

                        user_name = triptypeJSONObject.getString("other_user_name");
                        user_thumb_image = triptypeJSONObject.getString("other_thumb_image");
                        check_in = triptypeJSONObject.getString("checkin");
                        check_out = triptypeJSONObject.getString("checkout");
                        guestCount = triptypeJSONObject.getString("number_of_guests");


                        spaceImage = triptypeJSONObject.getString("space_image");
                        total_cost = triptypeJSONObject.getString("total_cost");

                        host_user_id = triptypeJSONObject.getString("other_user_id");

                        payment_recieved_date = triptypeJSONObject.getString("payment_recieved_date");
                        can_view_receipt = triptypeJSONObject.getString("can_view_receipt");
                        currency_symbol = triptypeJSONObject.getString("currency_symbol");
                        member_from = triptypeJSONObject.getString("member_from");
                        expire_timer = triptypeJSONObject.getString("expire_timer");
                        currencysymbol = Html.fromHtml(currency_symbol).toString();
                        startTime = triptypeJSONObject.getString("start_time");
                        endTime = triptypeJSONObject.getString("end_time");


                    } else {
                        spaceId = triptypeJSONObject.getString("space_id");
                        spaceName = triptypeJSONObject.getString("space_name");
                        spaceLocation = triptypeJSONObject.getString("space_location");
                        reservationStatus = triptypeJSONObject.getString("reservation_status");
                        user_name = triptypeJSONObject.getString("other_user_name");
                        user_thumb_image = triptypeJSONObject.getString("other_thumb_image");
                        check_in = triptypeJSONObject.getString("checkin");
                        check_out = triptypeJSONObject.getString("checkout");
                        startTime = triptypeJSONObject.getString("start_time");
                        endTime = triptypeJSONObject.getString("end_time");
                        spaceImage = triptypeJSONObject.getString("space_image");
                        total_cost = triptypeJSONObject.getString("total_cost");
                        host_user_id = triptypeJSONObject.getString("other_user_id");
                        member_from = triptypeJSONObject.getString("member_from");
                        expire_timer = triptypeJSONObject.getString("expire_timer");
                        currency_symbol = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);
                        currencysymbol = Html.fromHtml(currency_symbol).toString();
                        guestCount = triptypeJSONObject.getString("number_of_guests");
                    }

                    reservation_roomname.setText(spaceName);
                    tsdroomname.setText(reservationStatus);

                    tsdnightcountlocation.setText(currencysymbol + "" + total_cost);

                    tsdhometype.setText(user_name);
                    tsdhostedby.setText(member_from);

                    Glide.with(getApplicationContext()).asBitmap().load(user_thumb_image).into(new BitmapImageViewTarget(tsdhostprofile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            tsdhostprofile.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                    tsdchin.setText(check_in);
                    tsdchout.setText(check_out);

                    time.setText(startTime);
                    end_time.setText(endTime);

                    tsd_guest.setText(guestCount+getResources().getString(R.string.guests));

                    Glide.with(getApplicationContext())
                            .load(spaceImage)
                            .into(new DrawableImageViewTarget(tsdroomimage) {
                                @Override
                                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                }
                            });

                    tsdroomdetail.setText(spaceName);
                    tsdtotalcost.setText(currencysymbol + "" + total_cost);

                    /*if(can_view_receipt!=null&&can_view_receipt.equals("Yes"))
                    {*/
                    Drawable img = getResources().getDrawable(R.drawable.n2_standard_row_right_caret_gray);
                    Drawable img1 = getResources().getDrawable(R.drawable.n2_icon_chevron_right);
                    //img.setBounds(20, 0, 0, 0 );

                    tsdtotalcost.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    tsdtotalcost.setCompoundDrawablePadding(20);

                    total_costlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent x = new Intent(getApplicationContext(), PriceBreakDown.class);
                            x.putExtra(RESERVATIONID, reservationid);
                            x.putExtra(USERTYPE, "host");
                            x.putExtra(SESSIONID, "");
                            startActivity(x);

                                /*Intent x = new Intent(getApplicationContext(), PriceBreak.class);
                                x.putExtra("isHost",isHosti);
                                x.putExtra("title",getResources().getString(R.string.customerreceipt));
                                x.putExtra("securityfee",security_deposit);
                                x.putExtra("cleaningfee","");
                                x.putExtra("hostfee",host_fee);
                                x.putExtra("additionalguestfee","");
                                x.putExtra("totalfee",total_cost);
                                x.putExtra("penalty_amount",penalty_amount);
                                x.putExtra("currency_symbol",currency_symbol);
                                x.putExtra("location", spaceLocation);
                                x.putExtra("list_type","");


                                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                                startActivity(x,bndlanimation);*/
                        }
                    });
                    //}

                    if (reservationStatus.equals("Pending")) {
                        reservationStatus = getString(R.string.pending);
                        tsd_expire_timer_lay.setVisibility(View.VISIBLE);
                        tsd_booknow.setVisibility(View.VISIBLE);
                        tsd_cancel.setVisibility(View.VISIBLE);
                        tsd_cancel.setText(getResources().getString(R.string.decline));
                        conversation.setText(getResources().getString(R.string.discuss));
                        String[] values = expire_timer.split(":");
                        long millisec = 0;
                        millisec = millisec + TimeUnit.HOURS.toMillis(Long.valueOf(values[0]));
                        millisec = millisec + TimeUnit.MINUTES.toMillis(Long.valueOf(values[1]));
                        millisec = millisec + TimeUnit.SECONDS.toMillis(Long.valueOf(values[2]));

                        long reservationloadtime = localSharedPreferences.getSharedPreferencesLong(Constants.ReservationLoadTime);

                        long current_millis = System.currentTimeMillis();

                        System.out.println(" reservationloadtime " + reservationloadtime);
                        System.out.println(" current_millis " + current_millis);
                        reservationloadtime = current_millis - reservationloadtime;
                        System.out.println(" reservationloadtime " + reservationloadtime);
                        System.out.println(" millisec before " + millisec);
                        millisec = millisec - reservationloadtime;
                        System.out.println(" millisec after " + millisec);

                        countDownTimer = new CountDownTimer(millisec, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                long millis = millisUntilFinished;
                                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                                System.out.println(hms);
                                tsd_expire_timer.setText(getResources().getString(R.string.expires) + hms);
                            }

                            public void onFinish() {
                                reservationStatus = getString(R.string.expire);
                                tsd_expire_timer.setText(getResources().getString(R.string.expired));
                                tsd_booknow.setVisibility(View.GONE);
                                tsd_cancel.setVisibility(View.GONE);
                            }
                        }.start();
                    } else if (reservationStatus.equals("Accepted") || reservationStatus.equals("Upcoming Trips")) {
                        reservationStatus = getString(R.string.accepted);
                        tsd_expire_timer_lay.setVisibility(View.GONE);
                        tsd_booknow.setVisibility(View.GONE);
                        tsd_cancel.setVisibility(View.VISIBLE);
                        tsd_cancel.setText(getResources().getString(R.string.cancel));
                        conversation.setText(getResources().getString(R.string.messagehistory));
                    } else if (reservationStatus.equals("Pre-Accepted")) {
                        tsd_cancel.setVisibility(View.VISIBLE);
                        tsd_cancel.setText(getResources().getString(R.string.cancel));
                        tsd_expire_timer_lay.setVisibility(View.GONE);
                        tsd_booknow.setVisibility(View.GONE);
                    } else {
                        reservationStatus = getString(R.string.declined);
                        tsd_expire_timer_lay.setVisibility(View.GONE);
                        tsd_booknow.setVisibility(View.GONE);
                        tsd_cancel.setVisibility(View.GONE);
                        conversation.setText(getResources().getString(R.string.messagehistory));
                    }


                    tsdroomimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // ***Experience Start***


                            //***Experience End***
                            localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, spaceId);
                            localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, null);
                            Intent x = new Intent(getApplicationContext(), SpaceDetailActivity.class);
                            startActivity(x);

                            // ***Experience Start***

                            // ***Experience End***

                        }
                    });

                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        super.onBackPressed();
    }
}
