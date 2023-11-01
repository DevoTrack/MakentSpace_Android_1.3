package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestHomeActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.SpaceListingActivity;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.homeModels.RoomPropertyResult;
import com.makent.trioangle.model.homeModels.RoomTypeModel;
import com.makent.trioangle.model.host.Room_Type_model;
import com.makent.trioangle.newhome.views.NewHomeActivity;
import com.makent.trioangle.travelling.tabs.InboxActivity;
import com.makent.trioangle.travelling.tabs.ProfileActivity;
import com.makent.trioangle.travelling.tabs.SavedActivity;
import com.makent.trioangle.travelling.tabs.TripsActivity;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ************************************************************
                   Home Page  or Tab Page
This is main or Home page for travelling Makent Its contain five tabs and fragments
*************************************************************** */
public class HomeActivity extends TabActivity implements TabHost.OnTabChangeListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;

    int backPressed = 0;
    TabHost tabHost;
    int tabsaved = 0;
    TabWidget tabWidget;
    public static boolean isrecreated;

    String userid;
    public static List<Room_Type_model> searchlist;
    public RoomPropertyResult roomPropertyResult;
    private boolean doubleBackToExitPressedOnce =false;
    private String filterApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        Intent x = getIntent();
        tabsaved = x.getIntExtra("tabsaved", 0);

        localSharedPreferences = new LocalSharedPreferences(this);
        localSharedPreferences.saveSharedPreferences(Constants.isHost,0);

        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null)
        {
            System.out.println("IS HOST " + localSharedPreferences.getSharedPreferencesInt(Constants.isHost));
            if (localSharedPreferences.getSharedPreferencesInt(Constants.isHost) == 1)
            {
                Intent a = new Intent(getApplicationContext(), HostHome.class);
                startActivity(a);
                finish();
            }
        }

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        searchlist = new ArrayList<>();
        searchlist.clear();

        // Get rooms property list from API
        roomPropertyList();

        // Bottom tab
        tabHost = getTabHost();
        if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("inbox"))
        {
            tabsaved=4; // Inbox
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("listing"))
        {
            tabsaved=2; // Saved
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        else if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("trips"))
        {
            tabsaved=5; // Trips
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }
        tabHost.setOnTabChangedListener(this);

        tabHost.getTabWidget().setDividerDrawable(null);
        String color = getResources().getString(0 + R.color.text_shadow);
        Intent y = getIntent();
        filterApply = y.getStringExtra("filter");
        // Intents for bottom tab activity
        //Intent search = new Intent(getApplicationContext(), ExploreSearchActivity.class);
        Intent search = new Intent(getApplicationContext(), NewHomeActivity.class);
       // search.putExtra("type",y.getStringExtra("type"));
        if(filterApply != null)
        {
            search.putExtra("filter", filterApply);
        }
        Intent saved = new Intent(getApplicationContext(), SavedActivity.class);
        Intent trips = new Intent(getApplicationContext(), TripsActivity.class);
        Intent inbox = new Intent(getApplicationContext(), InboxActivity.class);
        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        Intent savedlistdetails = new Intent(getApplicationContext(), WishListDetailsActivity.class);
        Intent tripsdetails = new Intent(getApplicationContext(), TripsDetails.class);
        Intent hostlist=new Intent(getApplicationContext(), SpaceListingActivity.class);

        this.setNewTab(this, tabHost, "tab1", R.string.explore, R.drawable.home, search, color);

        if (tabsaved == 0 ||tabsaved == 1 || tabsaved == 2 || tabsaved == 4 || tabsaved == 5)
        {
            this.setNewTab(this, tabHost, "tab2", R.string.bookings, R.drawable.booking, trips, color);
        }
        else if (tabsaved == 3)
        {
            this.setNewTab(this, tabHost, "tab2", R.string.bookings, R.drawable.booking, tripsdetails, color);
        }

        this.setNewTab(this, tabHost, "tab3", R.string.listing, R.drawable.listing, hostlist, color);
        this.setNewTab(this, tabHost, "tab4", R.string.inbox, R.drawable.inbox, inbox, color);

        if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken)))
        {
            this.setNewTab(this, tabHost, "tab5", R.string.login, R.drawable.user_profile, main, color);
        }
        else
        {
            if (localSharedPreferences.getSharedPreferences(Constants.LastPage) != null && localSharedPreferences.getSharedPreferences(Constants.LastPage).equals("profile"))
            {
                tabsaved=6; // Trips
                localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
            }
            this.setNewTab(this, tabHost, "tab5", R.string.profile, R.drawable.user_profile, profile, color);
        }

        showtab(); // this is used to change the tab controler

        if (tabsaved == 0)
        {
            tabHost.setCurrentTab(0);
        }
        else if (tabsaved == 5 || tabsaved == 3)
        {
            tabHost.setCurrentTab(1);
        }
        else if (tabsaved == 4)
        {
            tabHost.setCurrentTab(3);
        }
        else if (tabsaved==6)
        {
            tabHost.setCurrentTab(4);
        }
        else if (tabsaved==2)
        {
            tabHost.setCurrentTab(2);
        }

        changetab();
    }

    // Add new tab
    private void setNewTab(Context context, TabHost tabHost, String tag, int title, int icon, Intent contentID, String color)
    {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabIndicator(tabHost.getContext(), title, icon, color)); // new function to inject our own tab layout
        tabSpec.setContent(contentID.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        tabHost.addTab(tabSpec);
    }

    // Set Image and text
    private View getTabIndicator(Context context, int title, int icon, String color)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setTextColor(Color.parseColor(color));
        tv.setText(title);
        return view;
    }

    // While tab chage, to chage the text and ivPhoto color
    public void onTabChanged(String tabId)
    {
        // TODO Auto-generated method stub
        System.out.println("Tab Id " + tabId);

        if (tabId.equals("tab5") && TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken)))
        {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"profile");
            tabWidget.setVisibility(View.GONE);
        }
        showtab();
    }

    public void changetab()
    {
        String  languageCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        String languageName = localSharedPreferences.getSharedPreferences(Constants.LanguageName);

        if (languageCode != null && !languageCode.equals(""))
        {
            setLocale(languageCode);
        } else
        {
            setLocale("en");
        }
    }

    // Set Text and ivPhoto in tabs
    public void showtab()
    {
        String color = getResources().getString(0 + R.color.text_shadow);
        String color1 = getResources().getString(0 + R.color.red_text);
        int icona[] = {R.drawable.home, R.drawable.booking,R.drawable.listing, R.drawable.inbox, R.drawable.user_profile};

        TabWidget widget = tabHost.getTabWidget();

        for (int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView) v.findViewById(R.id.textView);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView);

            System.out.println("tabHost.getCurrentTab() " + tabHost.getCurrentTab());
            System.out.println("tv " + tv);
            System.out.println("i " + i);
            if (tv == null)
            {
                continue;
            }
            else
            {
                if (i == tabHost.getCurrentTab())
                {
                    System.out.println("tabHost.getCurrentTab() " + tabHost.getCurrentTab());
                    tv.setTextColor(Color.parseColor(color1));
                    iv.setImageResource(icona[i]);
                    iv.setColorFilter(Color.parseColor(color1));
                }
                else
                {
                    tv.setTextColor(Color.parseColor(color));
                    iv.setImageResource(icona[i]);
                    iv.setColorFilter(Color.parseColor(color));
                }
            }
        }
    }

    // onBack button pressed
    public void onBackPressed()
    {
        if (backPressed >= 1)
        {
            //super.onBackPressed();
            this.finishAffinity();
        }
        else
        {
            backPressed = backPressed + 1;
            if (doubleBackToExitPressedOnce)
            {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    backPressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data)
    {
        if (jsonResp.isSuccess())
        {
            onSuccessRes(jsonResp);
        }
        else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        {
            //commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,editTextMessage,editTextMessage,getResources(),this);
        }
    }

    public  void finishitoff()
    {
        super.onBackPressed();
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data)
    {

    }

    public void roomPropertyList()
    {
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        //apiService.roomproperty(localSharedPreferences.getSharedPreferences(Constants.AccessToken),langCode).enqueue(new RequestCallback(HomeActivity.this));
    }

    public void setLocale(String lang)
    {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //recreate();
        isrecreated =true;
    }


    public void onSuccessRes(JsonResponse jsonResp)
    {
        try
        {
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONArray data = response.getJSONArray("room_type");
            JSONArray data1 = response.getJSONArray("property_type");
            JSONArray bettype = response.getJSONArray("bed_type");
            localSharedPreferences.saveSharedPreferences(Constants.Bedtype, bettype.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Roomtype, data.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Propertytype, data1.toString());
        } catch (JSONException j)
        {
            j.printStackTrace();
        }

        roomPropertyResult = gson.fromJson(jsonResp.getStrResponse(), RoomPropertyResult.class);
        ArrayList<RoomTypeModel> roomTypeModels = roomPropertyResult.getRoomTypeModels();
        searchlist.clear();
        for (int i = 0; i < roomTypeModels.size(); i++)
        {
            Room_Type_model listdata = new Room_Type_model();
            listdata.type = "item";
            listdata.setName(roomTypeModels.get(i).getName());
            listdata.setDesc(roomTypeModels.get(i).getDescription());
            listdata.setId(roomTypeModels.get(i).getId());
            listdata.setIsShared(roomTypeModels.get(i).getIsShared());
            listdata.setImage(roomTypeModels.get(i).getImageName());
            listdata.setType("room_type");
            searchlist.add(listdata);
        }
    }
}

