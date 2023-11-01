package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostHome
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alamkanak.weekview.WeekViewEvent;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.SpaceListingActivity;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.tabs.HostCalenderActivity;
import com.makent.trioangle.host.tabs.YourReservationActivity;
import com.makent.trioangle.travelling.tabs.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* ***********************************************************************
This is Host Home Page Contain Reservation, Calander,Listing,Profile, Tabs
**************************************************************************  */

public class HostHome extends TabActivity implements TabHost.OnTabChangeListener{

    LocalSharedPreferences localSharedPreferences;
    TabHost tabHost;
    int tabsaved=0;
    public static boolean isrecreated;
    public static List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    public static List<WeekViewEvent> dayevents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_home);
        localSharedPreferences= new LocalSharedPreferences(this);


        Intent x= getIntent();// this is used to get the value to one activity to another activity
        tabsaved=x.getIntExtra("tabsaved",0);


        tabHost = getTabHost();

        tabHost.setOnTabChangedListener(this);

        tabHost.getTabWidget().setDividerDrawable(null);
        // setNewTab(context, tabHost, tag, title, icon, contentID);
        String color = getResources().getString(0 + R.color.text_shadow);

        Intent hosthome=new Intent(getApplicationContext(), YourReservationActivity.class);
        Intent hostcal=new Intent(getApplicationContext(), HostCalenderActivity.class);
        //Intent hostlist=new Intent(getApplicationContext(), HostListingActivity.class);
        Intent hostlist=new Intent(getApplicationContext(), SpaceListingActivity.class);
        Intent hostprofile=new Intent(getApplicationContext(), ProfileActivity.class);

       // this.setNewTab(this, tabHost, "tab1", R.string.host_home, R.drawable.ic_core_nav_hosthome_old, hosthome, color);

        this.setNewTab(this, tabHost, "tab1", R.string.reservation, R.drawable.inbox, hosthome, color);
        // this.setNewTab(this, tabHost, "tab2", R.string.wishlist, R.drawable.wishlists, R.id.tab2);
        this.setNewTab(this, tabHost, "tab3", R.string.calendar, R.drawable.ic_core_nav_hostcalendar, hostcal, color);
        this.setNewTab(this, tabHost, "tab4", R.string.listing, R.drawable.ic_core_nav_hostlisting, hostlist, color);
        this.setNewTab(this, tabHost, "tab5", R.string.profilec, R.drawable.user_profile, hostprofile, color);


            if(tabsaved==2) {
                tabHost.setCurrentTab(2);
            }

        showtab(); // this is used to change the tab control
        changetab();


    }

    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        showtab();

    }
    public void showtab()
    {
        String color = getResources().getString(0 + R.color.text_shadow);

        String color1 = getResources().getString(0 + R.color.background);
        int icona[] = {R.drawable.inbox, R.drawable.ic_core_nav_hostcalendar, R.drawable.ic_core_nav_hostlisting, R.drawable.user_profile};
        TabWidget widget = tabHost.getTabWidget();


        for (int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView) v.findViewById(R.id.textView);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView);


            if (tv == null) {
                continue;
            } else {
                if (i == tabHost.getCurrentTab()) {
                    tv.setTextColor(Color.parseColor(color1));
                    iv.setImageResource(icona[i]);
                    iv.setColorFilter(Color.parseColor(color1));
                } else {
                    tv.setTextColor(Color.parseColor(color));
                    iv.setImageResource(icona[i]);
                    iv.setColorFilter(Color.parseColor(color));
                }
            }
        }
    }

    public void changetab(){
        String  languageCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        String languageName = localSharedPreferences.getSharedPreferences(Constants.LanguageName);



        if (languageCode != null && !languageCode.equals("")) {
            setLocale(languageCode);
        } else {
            setLocale("en");
        }
    }


    private void setNewTab(Context context, TabHost tabHost, String tag, int title, int icon, Intent contentID, String color) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabIndicator(tabHost.getContext(), title, icon, color)); // new function to inject our own tab layout
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //recreate();
        isrecreated =true;
    }

    private View getTabIndicator(Context context, int title, int icon, String color) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setTextColor(Color.parseColor(color));
        tv.setText(title);
        return view;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
