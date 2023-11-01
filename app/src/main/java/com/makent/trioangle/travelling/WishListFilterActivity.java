package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestWishListFilterActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
/* ***********************************************************************
This is WishListFilterPage Contain Dates, Guest
**************************************************************************  */
public class WishListFilterActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout wishlist_guest,wishlist_dates;
    RelativeLayout wishlistfilterdone;
    Button wishlist_filter_close;
    TextView wishlist_filter_reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_filter);

        wishlist_guest=(LinearLayout)findViewById(R.id.wishlist_guest);
        wishlist_dates=(LinearLayout)findViewById(R.id.wishlist_dates);
        wishlistfilterdone=(RelativeLayout) findViewById(R.id.wishlistfilterdone);
        wishlist_filter_close=(Button) findViewById(R.id.wishlist_filter_close);
        wishlist_filter_reset=(TextView) findViewById(R.id.wishlist_filter_reset);

        wishlist_guest.setOnClickListener(this);
        wishlist_dates.setOnClickListener(this);
        wishlistfilterdone.setOnClickListener(this);
        wishlist_filter_close.setOnClickListener(this);
        wishlist_filter_reset.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.wishlist_guest:
            {
                // This is used to call the WishListFilterActivity to  Serach Guest Activity
                Intent x=new Intent(getApplicationContext(),Search_Guest_Bed.class);
                startActivity(x);
            }break;
            case R.id.wishlist_dates:
            {
                // This is used to call the WishListFilterActivity to  CalendarActivity
                Intent x=new Intent(getApplicationContext(),CalendarActivity.class);
                startActivity(x);
            }break;
            case R.id.wishlist_filter_close:
            {
                onBackPressed();
            }break;
            case R.id.wishlistfilterdone:
            {
                onBackPressed();
            }break;
            case R.id.wishlist_filter_reset:
            {
            }break;
        }
    }
}
