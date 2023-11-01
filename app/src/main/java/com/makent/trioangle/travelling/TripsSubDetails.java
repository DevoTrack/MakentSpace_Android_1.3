package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestTripsSubDetailsActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.chat.ChatActivity;
import com.makent.trioangle.controller.LocalSharedPreferences;
// ***Experience Start***

// ***Experience End***
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.PriceBreakDown;
import com.makent.trioangle.model.tripsModel.TripDetailData;
import com.makent.trioangle.profile.HelpActivity;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.spacebooking.views.RequestSpaceActivity;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

import static com.makent.trioangle.helper.Constants.Bookingtype;
import static com.makent.trioangle.helper.Constants.RESERVATIONID;
import static com.makent.trioangle.helper.Constants.SESSIONID;

import static com.makent.trioangle.helper.Constants.USERTYPE;

/* ***********************************************************************
This is TripsSubDetails Page Contain Checkin, Checkout, Guests
**************************************************************************  */
public class TripsSubDetails extends AppCompatActivity {

    Toolbar tripsubdetailstoolbar;
    TextView end_time,time, tvStatus;
    TextView tsdroomname, tsdnightcountlocation, tsdhometype, tsdhostedby, tsdchin, tsdchout, tsd_guest, tsdroomdetail, tsdtotalcost, tsd_help, tsd_messagehistory, tsd_cancel;
    ImageView tsdhostprofile, tsdroomimage;
    Button tsd_booknow;
    String reservationid, tripstatus;
    LocalSharedPreferences localSharedPreferences;
    LinearLayout total_costlay;

    private String startTime;
    private String endTime;
    public String room_id;
    public String room_name;
    public String room_location;
    public String host_user_name;
    public String host_thumb_image;
    public String trip_date;
    public String trip_status;
    public String booking_status;
    public String user_name;
    public String user_thumb_image;
    public String check_in;
    public String check_out;
    public String room_type;
    public String total_nights;
    public String guest_count;
    public String room_image;
    public String total_cost;
    public String host_user_id;





    public String can_view_receipt;
    public String currency_symbol;

    public String currencysymbol;

    public TripDetailData tripDetailData;
    LinearLayout image_lay;
    public ImageView back_arrow;
    public @Inject
    CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_sub_details);
        commonMethods = new CommonMethods();

        Log.e("TripSub","TripSubDetails");

        localSharedPreferences = new LocalSharedPreferences(this);
        Intent x = getIntent();
        reservationid = x.getStringExtra("reservationid");
        tripstatus = x.getStringExtra("tripstatus");
        tripDetailData = (TripDetailData) x.getSerializableExtra("roomDetail");
        System.out.println("tripDetailData"+tripDetailData.getSpaceId());
        tripsubdetailstoolbar = (Toolbar) findViewById(R.id.tripsubdetailstoolbar);
        tripsubdetailstoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tsdroomname = (TextView) findViewById(R.id.tripsubdetails_roomname);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tsdnightcountlocation = (TextView) findViewById(R.id.tripsubdetails_nightcountlocation);
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
        tsd_messagehistory = (TextView) findViewById(R.id.tsd_messagehistory);
        tsd_cancel = (TextView) findViewById(R.id.tsd_cancel);
        tsd_booknow = (Button) findViewById(R.id.tsd_booknow);
        image_lay = (LinearLayout) findViewById(R.id.image_lay);
        back_arrow =(ImageView)findViewById(R.id.back_arrow);
        commonMethods.rotateArrow(back_arrow,this);


        end_time = (TextView) findViewById(R.id.end_time);
        time = (TextView) findViewById(R.id.time);

        total_costlay = (LinearLayout) findViewById(R.id.total_cost);
        if (tripDetailData != null)
            getTripSubDetail();
        tsd_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), RequestSpaceActivity.class);
                x.putExtra(SESSIONID,"");
                x.putExtra(RESERVATIONID,reservationid);
                x.putExtra(Bookingtype,"Yes");
                x.putExtra("host_user_id",tripDetailData.getHostUserId());
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

        tsd_messagehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), ChatActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("hostid", host_user_id);
                x.putExtra("host_username", user_name);
                x.putExtra("trip_status", trip_status);
                x.putExtra("roomid", room_id);

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        String triptype = localSharedPreferences.getSharedPreferences(Constants.TripType);
        if (triptype.equals("Pending Bookings")) {
            tsd_cancel.setVisibility(View.VISIBLE);
            tsd_cancel.setText(getResources().getString(R.string.cancelrequest));
        } else if (triptype.equals("Current Bookings") || triptype.equals("Upcoming Bookings")) {
            if (tripstatus.equals("Cancelled") || tripstatus.equals("Declined") || tripstatus.equals("Expired")) {
                tsd_cancel.setVisibility(View.GONE);
            } else {
                tsd_cancel.setVisibility(View.VISIBLE);
                tsd_cancel.setText(getResources().getString(R.string.cancel));
            }
        } else {
            tsd_cancel.setVisibility(View.GONE);
        }
        tsd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), CancelReservationActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("tripstatus", localSharedPreferences.getSharedPreferences(Constants.TripType));
                startActivity(x);
            }
        });

        tsdhostedby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + host_user_id);
                x.putExtra("otheruserid", host_user_id);
                startActivity(x);
            }
        });

        tsdhostprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(), ProfilePageActivity.class);
                System.out.println("Other user id" + host_user_id);
                x.putExtra("otheruserid", host_user_id);
                startActivity(x);
            }
        });
    }

    public void getTripSubDetail() {

        if (tripDetailData.getReservationId().equals(reservationid)) {

            room_id = tripDetailData.getSpaceId();
            room_name = tripDetailData.getSpaceName();
            room_location = tripDetailData.getSpaceLocation();
            host_user_name = tripDetailData.getHostUserName();
            host_thumb_image = tripDetailData.getHostThumbImage();
            trip_date = tripDetailData.getReservationDate();
            trip_status = tripDetailData.getReservationStatus();
            booking_status = tripDetailData.getBookingStatus();


            user_name = tripDetailData.getUserName();
            user_thumb_image = tripDetailData.getGuestThumbImage();
            check_in = tripDetailData.getCheckIn();
            check_out = tripDetailData.getCheckOut();
            room_type = tripDetailData.getSpaceType();
            total_nights = tripDetailData.getTotalNights();
            guest_count = tripDetailData.getGuestCount();
            room_image = tripDetailData.getSpaceImage();
            total_cost = tripDetailData.getTotalCost();
            host_user_id = tripDetailData.getHostUserId();

            can_view_receipt = tripDetailData.getCanViewReceipt();

            currency_symbol = tripDetailData.getCurrencySymbol();
            currencysymbol = Html.fromHtml(currency_symbol).toString();


            startTime = tripDetailData.getStart_time();
            endTime = tripDetailData.getEnd_time();
            time.setText(startTime);
            end_time.setText(endTime);
            tvStatus.setText(tripDetailData.getReservationStatus());



            setData();
        }
    }

    public void setData() {
        tsdroomname.setText(room_name);
        tsdnightcountlocation.setText(room_location);


        tsdhometype.setText(room_type);
        tsdhostedby.setText(host_user_name);

        Glide.with(getApplicationContext()).asBitmap().load(host_thumb_image).into(new BitmapImageViewTarget(tsdhostprofile) {
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
        if (Integer.parseInt(guest_count) > 1) {
            tsd_guest.setText(guest_count + " " + getResources().getString(R.string.guests));
        } else {
            tsd_guest.setText(guest_count + " " + getResources().getString(R.string.s_guest));
        }


        Glide.with(getApplicationContext())
                .load(room_image)
                .into(new DrawableImageViewTarget(tsdroomimage) {
                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                    }
                });

        tsdroomdetail.setText(room_name);
        tsdtotalcost.setText(currencysymbol + "" + total_cost);

                    /*if(can_view_receipt.equals("Yes"))
                    {*/

        if(getResources().getString(R.string.layout_direction).equals("1")){
            Drawable img = getResources().getDrawable(R.drawable.left_arrow);
            tsdtotalcost.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }else{
            Drawable img = getResources().getDrawable(R.drawable.n2_standard_row_right_caret_gray);
            tsdtotalcost.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }


        Drawable img1 = getResources().getDrawable(R.drawable.n2_icon_chevron_right);
        //img.setBounds(20, 0, 0, 0 );

        //tsdtotalcost.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        tsdtotalcost.setCompoundDrawablePadding(20);
        System.out.println("booking_status"+booking_status);
        if (booking_status.equals("")) {
            tsd_booknow.setVisibility(View.GONE);
        } else if (booking_status.equals("Available")) {
            tsd_booknow.setVisibility(View.VISIBLE);
        } else {
            tsd_booknow.setVisibility(View.GONE);
        }
        onClickSub();
    }

    public void onClickSub() {

        total_costlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), PriceBreakDown.class);
                x.putExtra(RESERVATIONID, reservationid);
                x.putExtra(USERTYPE, "guest");
                x.putExtra(SESSIONID, "");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);


            }
        });

        tsdroomimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("inComing check one two");
                // ***Experience Start***


                // ***Experience End***



                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, room_id);
                    localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, null);
                    Intent x = new Intent(getApplicationContext(), SpaceDetailActivity.class);
                    startActivity(x);
                // ***Experience Start***

                // ***Experience End***
            }
        });
    }

}
