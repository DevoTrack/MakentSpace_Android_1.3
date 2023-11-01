package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestFilterActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.FilterAmenitiesListAdapter;
import com.makent.trioangle.adapter.host.RoomTypeAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.helper.RangeSeekBar;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.homeModels.RoomPropertyResult;
import com.makent.trioangle.model.homeModels.RoomTypeModel;
import com.makent.trioangle.model.host.Room_Type_model;
import com.makent.trioangle.newhome.makentspacehome.Model.Amenities;
import com.makent.trioangle.newhome.makentspacehome.Model.Filter_Model;
import com.makent.trioangle.newhome.makentspacehome.Model.HostActivityModel;
import com.makent.trioangle.newhome.makentspacehome.Model.Services;
import com.makent.trioangle.newhome.makentspacehome.Model.Space_rules;
import com.makent.trioangle.newhome.makentspacehome.Model.Space_styles;
import com.makent.trioangle.newhome.makentspacehome.Model.Special_features;
import com.makent.trioangle.newhome.views.NewHomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.view.View.OnClickListener;
import static com.makent.trioangle.util.Enums.REQ_ACT_DATA;
import static com.makent.trioangle.util.Enums.REQ_DATA;
import static com.makent.trioangle.util.Enums.REQ_ROOMTYPE;


public class FilterActivity extends AppCompatActivity implements ServiceListener {

    boolean isreset=false;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    TextView filter_reset, show_all_amenities,show_all_services,show_all_space,show_all_style,show_all_special;
    Button filter_close, filter_save;
    ImageView bedplus, bedminus, roomplus, roomminus, bathplus, bathminus;
    TextView bedtext, bathtext, roomtext, min_amount, max_amount;
    int bedcount = 0, roomcount = 0, bathcount = 0;
    Toolbar toolbar;
    LinearLayout wifi, pool, kitchen;
    LocalSharedPreferences localSharedPreferences;
    SwitchCompat instant_switch;
    CheckBox entireplace, privateroom, sharedroom;
    CheckBox wifi_check, pool_check, kitchen_check;

    //check box added for makent space:
    CheckBox checkBoxfood, checkBoxlight,checkBoxfur;
    CheckBox checkBoxsmoke,checkBoxcook,checkBoxloud;
    CheckBox checkBoxclassic,checkBoxindustrial,checkBoxinti;
    CheckBox checkBoxart,checkBoxdin,checkBoxfire;

    String[] amenitiesid = new String[3];

    //For space filter:
    int[] amenites = new int[3];
    String[] amenitesid =new String[3];
    String[] serviceid = new String[3];
    String[] spaceid = new String[3];
    String[] styleid = new String[3];
    String[] specialid = new String[3];


    String filteramenity,filterservices,filterstyles,filterfeatures,filterspace,filterspacetype;
    boolean isServiceclick = false;
    boolean isSpaceClick = false;
    boolean isStyleClick = false;

    int filteram;

    String searchinstantbook, filteramenities, searchroomtypes, searchbeds, searchbedrooms, searchbathrooms, searchminprice, searchmaxprice;
    String filterminprice, filtermaxprice, min, max, currencysymbol;
    List<String> searchamenities = new ArrayList<String>();

    //For Makent Space:
    List<String> searchamenity = new ArrayList<String>();
    List<String> searchservice = new ArrayList<String>();
    List<String> searchspecial = new ArrayList<String>();
    List<String> searchstyle = new ArrayList<String>();
    List<String> searchspace = new ArrayList<String>();
    List<String> searchspaceType = new ArrayList<>();
    String searchspacetype;

    List<Integer> searcham = new ArrayList<>();

    RangeSeekBar seekBar;
    Typeface font1;
    Drawable icon, icon1, minusenable, minusdisable, plusenable, plusdisable;
    public static List<Room_Type_model> searchlist;

    List<Room_Type_model> searchlist_val;

    RelativeLayout rootypelayout;
    public static AlertDialog.Builder alertDialog;
    RecyclerView relationshipList;
    List<Room_Type_model> room_type_models;
    RoomTypeAdapter roomTypeAdapter;
    public TextView roomtype_count;
    EditText edt;

    FilterAmenitiesListAdapter adapter;
    Context context;
    JSONArray amenities_list,serviceList,speciallist,stylelist,spacelist,space_type_list,event_type_list;
    protected boolean isInternetAvailable;
    String userid,eventName,eventType;
    Dialog_loading mydialog;
    Room_Type_model listdata;

    TextView amenities1_txt, amenities2_txt, amenities3_txt;

    TextView special1_txt,special2_txt,special3_txt;
    TextView style1_txt,style2_txt,style3_txt;
    TextView space1_txt,space2_txt,space3_txt;
    TextView service1_txt,service2_txt,service3_txt;
    TextView tv_space,tv_event;
    ImageView iv_next_event,iv_next;
    RelativeLayout space_lay,event_lay;

    public RoomPropertyResult roomPropertyResult;
    public static boolean isrecreated;
    ArrayList<RoomTypeModel> roomTypeModels;
    private String bathCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        localSharedPreferences = new LocalSharedPreferences(this);
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        initialize();

        roomTypeModels = new ArrayList<RoomTypeModel>();
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        eventName = localSharedPreferences.getSharedPreferences(Constants.EventName);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // searchlist = HomeActivity.searchlist;

        //searchlist_val = searchlist;

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //myDialog.show();

        roomtype_count = (TextView) findViewById(R.id.roomtype_count);
        rootypelayout = (RelativeLayout) findViewById(R.id.rootypelayout);
        edt = (EditText) findViewById(R.id.edt);
        rootypelayout.setEnabled(true);
        rootypelayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isreset=false;
                rootypelayout.setEnabled(false);
                // showPopUp();
                searchroomtypes = localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes);
                if (searchroomtypes != null)
                {
                    RelationshipList(searchroomtypes);
                    System.out.println("searchroomtypes" + searchroomtypes);
                }
                else
                {
                    RelationshipList("");
                    System.out.println("searchroomtypes" + searchroomtypes);
                }
                //dialogsample();
            }
        });

        font1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_makent4));
        icon = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4checknormal), font1).sizeDp(30).colorRes(R.color.text_shadow);
        icon1 = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4check), font1).sizeDp(30).colorRes(R.color.guestButton);
        minusenable = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4checkminus), font1).sizeDp(30).colorRes(R.color.guestButton);
        plusenable = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4checkplus), font1).sizeDp(30).colorRes(R.color.guestButton);
        minusdisable = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4checkminus), font1).sizeDp(30).colorRes(R.color.guestButtonDisable);
        plusdisable = new FontIconDrawable(FilterActivity.this, getResources().getString(R.string.f4checkplus), font1).sizeDp(30).colorRes(R.color.guestButtonDisable);


        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPrice);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPrice);
        currencysymbol = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);
        filteramenities = localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);

        //Makent Space filteramenity data:
        filteramenity = localSharedPreferences.getSharedPreferences(Constants.FilterAmenity);
        filterservices = localSharedPreferences.getSharedPreferences(Constants.FilterService);
        filterspace = localSharedPreferences.getSharedPreferences(Constants.FilterSpace);
        filterstyles = localSharedPreferences.getSharedPreferences(Constants.FilterStyle);
        filterfeatures = localSharedPreferences.getSharedPreferences(Constants.FilterSpecial);
        searchspacetype = localSharedPreferences.getSharedPreferences(Constants.FilterSpaceType);

        if(searchspacetype != null)
        {
            String[] split = searchspacetype.split(",");
            for (int i = 0; i < split.length; i++)
            {
                if (!searchspaceType.contains(split[i]))
                {
                    searchspaceType.add(split[i]);
                }
            }
            System.out.println("searchtypes of  Space" + searchspacetype);

        }

        if(filteramenity != null){
            String[] split = filteramenity.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenity.contains(split[i])) {
                    searchamenity.add(split[i]);
                }
            }
            System.out.println("searchamenities Space" + searchamenity);

        }

        if(filterservices != null){
            String[] split = filterservices.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchservice.contains(split[i])) {
                    searchservice.add(split[i]);
                }
            }
            System.out.println("searchservice Space" + searchservice);

        }

        if(filterspace != null){
            String[] split = filterspace.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchspace.contains(split[i])) {
                    searchspace.add(split[i]);
                }
            }
            System.out.println("searchspace Space" + searchspace);

        }

        if(filterstyles != null){
            String[] split = filterstyles.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchstyle.contains(split[i])) {
                    searchstyle.add(split[i]);
                }
            }
            System.out.println("searchstyle Space" + searchstyle);

        }
        if(filterfeatures != null){
            String[] split = filterfeatures.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchspecial.contains(split[i])) {
                    searchspecial.add(split[i]);
                }
            }
            System.out.println("searchamenities Space" + searchspecial);

        }
        if(eventName == null){
            tv_event.setText(getResources().getString(R.string.select));

        }
        else{
            tv_event.setVisibility(View.VISIBLE);
            tv_event.setText(eventName);

        }

       /* if (filteramenities != null) {
            String[] split = filteramenities.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenities.contains(split[i])) {
                    searchamenities.add(split[i]);
                }
            }
            System.out.println("searchamenities Home" + searchamenities);
        }*/

        bedplus = (ImageView) findViewById(R.id.bedplus);
        roomplus = (ImageView) findViewById(R.id.roomplus);
        bathplus = (ImageView) findViewById(R.id.bathplus);
        bedminus = (ImageView) findViewById(R.id.bedminus);
        roomminus = (ImageView) findViewById(R.id.roomminus);
        bathminus = (ImageView) findViewById(R.id.bathminus);

        bedminus.setEnabled(false);
        roomminus.setEnabled(false);
        bathminus.setEnabled(false);

        bedtext = (TextView) findViewById(R.id.bedtext);
        roomtext = (TextView) findViewById(R.id.roomtext);
        bathtext = (TextView) findViewById(R.id.bathtext);

        min_amount = (TextView) findViewById(R.id.min_amount);
        max_amount = (TextView) findViewById(R.id.max_amount);

        instant_switch = (SwitchCompat) findViewById(R.id.instant_switch);
        entireplace = (CheckBox) findViewById(R.id.checkBox);
        privateroom = (CheckBox) findViewById(R.id.checkBox1);
        sharedroom = (CheckBox) findViewById(R.id.checkBox2);
        wifi_check = (CheckBox) findViewById(R.id.checkBox3);
        kitchen_check = (CheckBox) findViewById(R.id.checkBox5);
        pool_check = (CheckBox) findViewById(R.id.checkBox4);

        amenities1_txt = (TextView) findViewById(R.id.amenities1_txt);
        amenities2_txt = (TextView) findViewById(R.id.amenities2_txt);
        amenities3_txt = (TextView) findViewById(R.id.amenities3_txt);

        seekBar = (RangeSeekBar) findViewById(R.id.seekBar1);

        if (!TextUtils.isEmpty(searchmaxprice) && TextUtils.isDigitsOnly(searchmaxprice))
        {
            if (!TextUtils.isEmpty(searchminprice) && TextUtils.isDigitsOnly(searchminprice))
                seekBar.setRangeValues(Integer.valueOf(searchminprice), Integer.valueOf(searchmaxprice));
            seekBar.setSelectedMinValue(Integer.valueOf(searchminprice));
            seekBar.setSelectedMaxValue(Integer.valueOf(searchmaxprice));
            System.out.println("get MaxValue "+seekBar.getSelectedMinValue());
        }


        if (getResources().getString(R.string.layout_direction).equals("1"))
        {
            seekBar.setScaleX(-1);
        }

        if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            amenities1_txt.setGravity(Gravity.END);

            amenities2_txt.setGravity(Gravity.END);

            amenities3_txt.setGravity(Gravity.END);
            // edtMobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
//        seekBar.setRangeValues(Integer.valueOf(searchminprice), Integer.valueOf(searchmaxprice));
//
//        seekBar.setSelectedMinValue(Integer.valueOf(searchminprice) + 1);
//        seekBar.setSelectedMaxValue(Integer.valueOf(searchmaxprice) - 1);

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>()
        {

            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);

                isreset=false;
                min =String.valueOf(minValue);
                max = maxValue.toString();
               /*if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {

               }*/

                if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                    min_amount.setText(" - "+currencysymbol + String.valueOf(minValue) );
                }else{
                    min_amount.setText(currencysymbol + String.valueOf(minValue) + " - ");
                }


                if (maxValue.toString().equals(searchmaxprice)) {

                        //max_amount.setText(currencysymbol + maxValue.toString()+ "+");

                    if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        max_amount.setText("+"+currencysymbol + maxValue.toString());
                    }else {
                        max_amount.setText(currencysymbol + maxValue.toString() + "+");
                    }
                } else {
                    max_amount.setText(currencysymbol + maxValue.toString());
                }

            }

        });


        show_all_amenities = (TextView) findViewById(R.id.show_all_amenities);

        // On Click function used to click action for check Email id in server send link to Email
        show_all_amenities.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                //checkAmenaties();// this funtion used to check amenaties
                //checkAmenityData();
                checkvalues();
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("Amenities", "Amenities");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        show_all_services.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isreset=false;
                //checkService();
                checkvalues();
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("Service","Service");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        show_all_space.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isreset=false;
               // checkSpace();
                checkvalues();
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("Space","Space");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        show_all_style.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                //checkStyle();
                checkvalues();
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("Style","Style");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        show_all_special.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                //checkSpecial();
                checkvalues();
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("Special","Special");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });


        iv_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("SpaceType","SpaceType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        tv_space.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("SpaceType","SpaceType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });


        tv_event.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("event","event type");
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("EventType","EventType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });
        iv_next_event.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("EventType","EventType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        event_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("EventType","EventType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });

        space_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                Intent x = new Intent(getApplicationContext(), FilterAmenitiesList.class);
                x.putExtra("SpaceType","SpaceType");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
        });


        filter_reset = (TextView) findViewById(R.id.filter_reset);

        // On Click function used to click action for check Email id in server send link to Email
        filter_reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {

                ResetAll(); //this function reset all the shared prference data

            }
        });


        filter_close = (Button) findViewById(R.id.filter_close);
        commonMethods.setTvAlign(filter_close, this);

        // On Click function used to click action for check Email id in server send link to Email
        filter_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        filter_save = (Button) findViewById(R.id.filtersave);

        // On Click function used to click action for check Email id in server send link to Email
        filter_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                saveFilterData();
            }
        });
        SetClick(wifi_check);
        SetClick(pool_check);
        SetClick(kitchen_check);
        SetClick(checkBoxart);
        checkBoxcook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isSpaceClick = true;
                isreset = false;
                checkBoxcook.setChecked(checkBoxcook.isChecked());
                checkStatuschange(checkBoxcook);
            }
        });

        checkBoxloud.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isSpaceClick = true;
                isreset = false;
                checkBoxloud.setChecked(checkBoxloud.isChecked());
                checkStatuschange(checkBoxloud);
            }
        });
        checkBoxsmoke.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // isSpaceClick = true;
                isreset = false;
                checkBoxsmoke.setChecked(checkBoxsmoke.isChecked());
                checkStatuschange(checkBoxsmoke);
            }
        });


        SetClick(checkBoxdin);
        SetClick(checkBoxfire);
        checkBoxindustrial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // isStyleClick = true;
                isreset = false;
                checkBoxindustrial.setChecked(checkBoxindustrial.isChecked());
                checkStatuschange(checkBoxindustrial);

            }
        });
        checkBoxclassic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isStyleClick = true;
                isreset = false;
                checkBoxclassic.setChecked(checkBoxclassic.isChecked());
                checkStatuschange(checkBoxclassic);
            }
        });

        checkBoxinti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // isStyleClick = true;
                isreset = false;
                checkBoxinti.setChecked(checkBoxinti.isChecked());
                checkStatuschange(checkBoxinti);
            }
        });

        checkBoxlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isServiceclick = true;
                isreset = false;
                checkBoxlight.setChecked(checkBoxlight.isChecked());
                checkStatuschange(checkBoxlight);
            }
        });
        checkBoxfood.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isServiceclick = true;
                isreset = false;
                checkBoxfood.setChecked(checkBoxfood.isChecked());
                checkStatuschange(checkBoxfood);
            }
        });

        checkBoxfur.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //isServiceclick = true;
                isreset = false;
                checkBoxfur.setChecked(checkBoxfur.isChecked());
                checkStatuschange(checkBoxfur);
            }
        });


        wifi = (LinearLayout) findViewById(R.id.wifi);

        wifi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                wifi_check.setChecked(!wifi_check.isChecked());
                checkStatuschange(wifi_check);
            }
        });



        pool = (LinearLayout) findViewById(R.id.pool);

        pool.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                pool_check.setChecked(!pool_check.isChecked());
                checkStatuschange(pool_check);
            }
        });

        kitchen = (LinearLayout) findViewById(R.id.kitchen);

        kitchen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                kitchen_check.setChecked(!kitchen_check.isChecked());
                checkStatuschange(kitchen_check);
            }
        });

        bedminus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                if (bedcount >= 1) {
                    bedcount--;
                }
                enablebuttons();
            }
        });
        roomminus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                if (roomcount >= 1) {
                    roomcount--;
                }
                enablebuttons();
            }
        });
        bathminus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                if (bathcount >= 1) {
                    bathcount--;
                }
                enablebuttons();
            }
        });
        bedplus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                bedcount++;
                enablebuttons();
            }
        });
        roomplus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                roomcount++;

                enablebuttons();
            }
        });
        bathplus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View vw) {
                isreset=false;
                bathcount++;

                enablebuttons();
            }
        });

        searchlist = new ArrayList<>();
        //roomPropertyList();

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            //getServiceList();
          //  roomPropertyList();
            //Get Makent Space filter data:
            getActivityData();



        } else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, edt, getResources(), this);
            mydialog.dismiss();
        }
        relationshipList = new RecyclerView(FilterActivity.this);
        room_type_models = new ArrayList<Room_Type_model>();

        roomTypeAdapter = new RoomTypeAdapter(this, room_type_models);
        roomTypeAdapter.setOnDataChangeListener(new RoomTypeAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(String searchroomtypes) {
                isreset=false;
                if (searchroomtypes != null) {
                    String arrs[] = searchroomtypes.split(",");
                    if (arrs.length > 0) roomtype_count.setText(String.valueOf(arrs.length));
                    else roomtype_count.setText(getResources().getString(R.string.select));
                } else {
                    roomtype_count.setText(getResources().getString(R.string.select));
                }
            }
        });

        loadFilterData();
    }



    public void initialize()
    {

        checkBoxfood = (CheckBox)findViewById(R.id.checkBoxfood);
        checkBoxlight =(CheckBox)findViewById(R.id.checkBoxlight);
        checkBoxfur = (CheckBox)findViewById(R.id.checkBoxfur);
        checkBoxsmoke = (CheckBox)findViewById(R.id.checkBoxsmoke);
        checkBoxcook = (CheckBox)findViewById(R.id.checkBoxcook);
        checkBoxloud = (CheckBox)findViewById(R.id.checkBoxloud);
        checkBoxclassic = (CheckBox)findViewById(R.id.checkBoxclassic);
        checkBoxindustrial = (CheckBox)findViewById(R.id.checkBoxindustrial);
        checkBoxinti = (CheckBox)findViewById(R.id.checkBoxinti);
        checkBoxart = (CheckBox)findViewById(R.id.checkBoxart);
        checkBoxdin = (CheckBox)findViewById(R.id.checkBoxdin);
        checkBoxfire =(CheckBox)findViewById(R.id.checkBoxfire);

        special1_txt = (TextView)findViewById(R.id.special1_txt);
        special2_txt = (TextView)findViewById(R.id.special2_txt);
        special3_txt =(TextView)findViewById(R.id.special3_txt);
        style1_txt = (TextView)findViewById(R.id.style1_txt);
        style2_txt =(TextView)findViewById(R.id.style2_txt);
        style3_txt =(TextView)findViewById(R.id.style3_txt);
        space1_txt =(TextView)findViewById(R.id.space1_txt);
        space2_txt =(TextView)findViewById(R.id.space2_txt);
        space3_txt =(TextView)findViewById(R.id.space3_txt);
        service1_txt =(TextView)findViewById(R.id.service1_txt);
        service2_txt =(TextView)findViewById(R.id.service2_txt);
        service3_txt =(TextView)findViewById(R.id.service3_txt);

        show_all_services = (TextView)findViewById(R.id.show_all_services);
        show_all_space= (TextView)findViewById(R.id.show_all_space);
        show_all_style = (TextView)findViewById(R.id.show_all_style);
        show_all_special =(TextView)findViewById(R.id.show_all_special);
        tv_space = (TextView)findViewById(R.id.tv_space);
        tv_event =(TextView)findViewById(R.id.tv_event);
        iv_next_event =(ImageView)findViewById(R.id.iv_next_event);
        iv_next =(ImageView)findViewById(R.id.iv_next);
        event_lay = (RelativeLayout)findViewById(R.id.event_lay);
        space_lay =(RelativeLayout)findViewById(R.id.space_lay);

        commonMethods.rotateArrow(iv_next, this);
        commonMethods.rotateArrow(iv_next_event, this);

    }

    public void saveFilterData() {
        if(isreset)
        {

            localSharedPreferences.saveSharedPreferences(Constants.EventName,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterSpaceType,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterAmenity, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterService,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterSpace,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterSpecial,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterStyle,null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterEventType,null);
           // localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
            localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);
            localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);


            searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPrice);
            searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPrice);

            if (instant_switch.isChecked()) {
                localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, Integer.toString(1));
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
            }
            //loadFilterData();

        }

        else {

            if (instant_switch.isChecked()) {
                localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, Integer.toString(1));
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
            }
            //checkAmenaties();

            //For Makent Space:
            checkAmenityData();
                checkService();
                checkSpace();
                checkStyle();
            checkSpecial();




            if(bathcount==0){
                localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
            }else{
                localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, Integer.toString(bathcount));
            }

            if(roomcount==0){
                localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
            }else{
                localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, Integer.toString(roomcount));
            }

            if(bedcount==0){
                localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
            }else{
                localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, Integer.toString(bedcount));
            }



            if (min == null) {
                min = filterminprice;
            }
            if (max == null) {
                max = filtermaxprice;
            }

            if ((min != null && !min.equals(searchminprice)) || (max != null && !max.equals(searchmaxprice))) {
                localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, min);
                localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, max);
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
                localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);

            }
            if (localSharedPreferences.getSharedPreferences(Constants.FilterInstantBook) != null || localSharedPreferences.getSharedPreferences(Constants.FilterAmenities) != null || localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes) != null || localSharedPreferences.getSharedPreferences(Constants.FilterBeds) != null || localSharedPreferences.getSharedPreferences(Constants.FilterBedRoom) != null || localSharedPreferences.getSharedPreferences(Constants.FilterBathRoom) != null || localSharedPreferences.getSharedPreferences(Constants.FilterMinPriceCheck) != null || localSharedPreferences.getSharedPreferences(Constants.FilterMaxPriceCheck) != null) {

                if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen) != null) {
                    if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen).equalsIgnoreCase("explore")) {
                        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "yes");
                    } else {
                        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "showAll");
                    }
                }
            }

        }


        System.out.println("Search location : filter "+localSharedPreferences.getSharedPreferences(Constants.SearchLocation));
        System.out.println("Check Filter Applied " +localSharedPreferences.getSharedPreferences(Constants.CheckIsFilterApplied));

        System.out.println("HomeType " + localSharedPreferences.getSharedPreferences(Constants.ExploreHomeType));

        localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,true);

        Intent x = new Intent(getApplicationContext(), HomeActivity.class);
        //Intent x = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
        //x.putExtra("type", "home");
        x.putExtra("filter", "filter");
        startActivity(x, bndlanimation);
        finishAffinity();
        //finish();
    }

    public void ResetAll() {
        loadResetFilter();
        isreset=true;

        // loadFilterData();// this function used to get filter  the data
    }

    private void loadResetFilter() {
        Log.e("Reset","Reset All");
        localSharedPreferences.saveSharedPreferences(Constants.EventName,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpaceType,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterEventType,null);

        //For makentspace:
        searchamenity.clear();
        filteramenity = null;
        if (filteramenity != null) {
            String[] split = filteramenity.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenity.contains(split[i])) {
                    searchamenity.add(split[i]);
                }
            }
        }

        // For service in filter:

        filterservices = null;
        searchservice.clear();
        if(filterservices != null){
            String[] split = filterservices.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchservice.contains(split[i])){
                    searchservice.add(split[i]);
                }
            }
        }

        //For special feature in filter

        filterfeatures = null;
        searchspecial.clear();
        if(filterfeatures != null){
            String[] split = filterfeatures.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchspecial.contains(split[i])){
                    searchspecial.add(split[i]);
                }
            }
        }


        // For style in Filter

        filterstyles = null;
        searchstyle.clear();
        if(filterstyles != null){
            String[] split = filterstyles.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchstyle.contains(split[i])){
                    searchstyle.add(split[i]);
                }
            }
        }

        //For space rules in filter

        filterspace = null;
        searchspace.clear();
        if(filterspace != null){
            String[] split = filterspace.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchspace.contains(split[i])){
                    searchspace.add(split[i]);
                }
            }
        }



        /*filteramenities = null;
        searchamenities.clear();
        if (filteramenities != null) {
            String[] split = filteramenities.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenities.contains(split[i])) {
                    searchamenities.add(split[i]);
                }
            }
        }*/
        searchroomtypes = null;
        searchbeds = null;
        searchbedrooms = null;
        searchbathrooms = null;
        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPrice);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPrice);
        filterminprice = null;
        filtermaxprice = null;

        if(instant_switch.isChecked())
        {
            instant_switch.setChecked(false);
        }


        //Makent Space:

        if (searchamenity != null && searchamenity.contains(amenitesid[0])) {
            wifi_check.setChecked(true);
        } else {
            wifi_check.setChecked(false);
        }
        if (searchamenity != null && searchamenity.contains(amenitesid[1])) {
            pool_check.setChecked(true);
        } else {
            pool_check.setChecked(false);
        }
        if (searchamenity != null && searchamenity.contains(amenitesid[2])) {
            kitchen_check.setChecked(true);
        } else {
            kitchen_check.setChecked(false);
        }

        //For service in filter:

        if(searchservice != null && searchservice.contains(serviceid[0])) {
            checkBoxfood.setChecked(true);
        } else {
            checkBoxfood.setChecked(false);
        }
        if (searchservice != null && searchservice.contains(serviceid[1])) {
            checkBoxlight.setChecked(true);
        } else {
            checkBoxlight.setChecked(false);
        }
        if (searchservice != null && searchservice.contains(serviceid[2])) {
            checkBoxfur.setChecked(true);
        } else {
            checkBoxfur.setChecked(false);
        }

        //For space rules in filter:

        if(searchspace != null && searchspace.contains(spaceid[0])) {
            checkBoxsmoke.setChecked(true);
        } else {
            checkBoxsmoke.setChecked(false);
        }
        if (searchspace != null && searchspace.contains(spaceid[1])) {
            checkBoxcook.setChecked(true);
        } else {
            checkBoxcook.setChecked(false);
        }
        if (searchspace != null && searchspace.contains(spaceid[2])) {
            checkBoxloud.setChecked(true);
        } else {
            checkBoxloud.setChecked(false);
        }

        //For space styles  in filter:

        if(searchstyle != null && searchstyle.contains(styleid[0])) {
            checkBoxclassic.setChecked(true);
        } else {
            checkBoxclassic.setChecked(false);
        }
        if (searchstyle != null && searchstyle.contains(styleid[1])) {
            checkBoxindustrial.setChecked(true);
        } else {
            checkBoxindustrial.setChecked(false);
        }
        if (searchstyle != null && searchstyle.contains(styleid[2])) {
            checkBoxinti.setChecked(true);
        } else {
            checkBoxinti.setChecked(false);
        }

        //For special features in filter:

        if(searchspecial != null && searchspecial.contains(specialid[0])) {
            checkBoxart.setChecked(true);
        } else {
            checkBoxart.setChecked(false);
        }
        if (searchspecial != null && searchspecial.contains(specialid[1])) {
            checkBoxdin.setChecked(true);
        } else {
            checkBoxdin.setChecked(false);
        }
        if (searchspecial != null && searchspecial.contains(specialid[2])) {
            checkBoxfire.setChecked(true);
        } else {
            checkBoxfire.setChecked(false);
        }

        searchspacetype = null;
        searchspacetype = localSharedPreferences.getSharedPreferences(Constants.FilterSpaceType);
        if(searchspacetype != null){
            String arrs[] = searchspacetype.split(",");
            if (arrs.length > 0) {
                tv_space.setVisibility(View.VISIBLE);
                tv_space.setText(String.valueOf(arrs.length));
            }
            else tv_space.setVisibility(View.GONE);
        } else {
            tv_space.setVisibility(View.GONE);
        }

        eventName = localSharedPreferences.getSharedPreferences(Constants.EventName);
        if(eventName == null){
        }
        else{
            tv_event.setVisibility(View.VISIBLE);
            tv_event.setText(eventName);
        }


        /*if (searchamenities != null && searchamenities.contains(amenitiesid[0])) {
            wifi_check.setChecked(true);
        } else {
            wifi_check.setChecked(false);
        }
        if (searchamenities != null && searchamenities.contains(amenitiesid[1])) {
            pool_check.setChecked(true);
        } else {
            pool_check.setChecked(false);
        }
        if (searchamenities != null && searchamenities.contains(amenitiesid[2])) {
            kitchen_check.setChecked(true);
        } else {
            kitchen_check.setChecked(false);
        }*/
        if (searchroomtypes != null) {
            String arrs[] = searchroomtypes.split(",");
            if (arrs.length > 0) roomtype_count.setText(String.valueOf(arrs.length));
            else roomtype_count.setText(getResources().getString(R.string.select));
        } else {
            roomtype_count.setText(getResources().getString(R.string.select));
        }

        if (searchroomtypes != null && searchroomtypes.contains("1")) {
            //entireplace.setChecked(true);
        } else {
            // entireplace.setChecked(false);
        }
        if (searchroomtypes != null && searchroomtypes.contains("2")) {
            //privateroom.setChecked(true);
        } else {
            //privateroom.setChecked(false);
        }
        if (searchroomtypes != null && searchroomtypes.contains("3")) {
            //sharedroom.setChecked(true);
        } else {
            //sharedroom.setChecked(false);
        }

        checkStatuschange(wifi_check);
        checkStatuschange(pool_check);
        checkStatuschange(kitchen_check);
        checkStatuschange(checkBoxfood);
        checkStatuschange(checkBoxfur);
        checkStatuschange(checkBoxlight);
        checkStatuschange(checkBoxart);
        checkStatuschange(checkBoxdin);
        checkStatuschange(checkBoxfire);
        checkStatuschange(checkBoxclassic);
        checkStatuschange(checkBoxindustrial);
        checkStatuschange(checkBoxinti);
        checkStatuschange(checkBoxsmoke);
        checkStatuschange(checkBoxcook);
        checkStatuschange(checkBoxloud);

        if (searchbeds != null) {
            bedcount = Integer.parseInt(searchbeds);
            enablebuttons();
        } else {
            bedcount = 0;
            enablebuttons();
        }
        if (searchbedrooms != null) {
            roomcount = Integer.parseInt(searchbedrooms);
            enablebuttons();
        } else {
            roomcount = 0;
            enablebuttons();
        }
        if (searchbathrooms != null) {
            bathcount = Integer.parseInt(searchbathrooms);
            enablebuttons();
        } else {
            bathcount = 0;
            enablebuttons();
        }

       /* if (filterminprice != null) {
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText("+"+currencysymbol + filterminprice.toString());
            }else {
                min_amount.setText(currencysymbol + filterminprice.toString() + "-");
            }
        } else {
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText("+"+currencysymbol + searchminprice);
            }else {
                min_amount.setText(currencysymbol + searchminprice + "-");
            }

        }*/
        if (filterminprice != null) {


            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText("-"+currencysymbol + filterminprice );
            }else{
                min_amount.setText(currencysymbol + filterminprice + " - ");
            }


            seekBar.setSelectedMinValue(Integer.valueOf(filterminprice));
               seekBar.setSelectedMaxValue(Integer.valueOf(filtermaxprice));
        } else {


            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText(" - "+currencysymbol + searchminprice );
            }else{
                min_amount.setText(currencysymbol + searchminprice + " - ");
            }


            if ((!TextUtils.isEmpty(searchminprice) && TextUtils.isDigitsOnly(searchminprice)) && (!TextUtils.isEmpty(searchmaxprice) && TextUtils.isDigitsOnly(searchmaxprice))) {
                seekBar.setSelectedMinValue(Integer.valueOf(searchminprice));
                 seekBar.setSelectedMaxValue(Integer.valueOf(searchmaxprice));
            }
        }

        if (filtermaxprice != null) {
                //max_amount.setText(currencysymbol + filtermaxprice.toString() + "+");


            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                max_amount.setText("+"+currencysymbol + filtermaxprice.toString());
            }else {
                max_amount.setText(currencysymbol + filtermaxprice.toString() + "+");
            }
        } else {

                //max_amount.setText(currencysymbol + searchmaxprice);




            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                max_amount.setText("+"+currencysymbol + searchmaxprice);
            }else {
                max_amount.setText(currencysymbol + searchmaxprice + "+");
            }

        }
       /* localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);

        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPrice);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPrice);*/


    }

    public void loadFilterData() {


        checkStatuschange(wifi_check);
        // For Makent Space:
        filteramenity = localSharedPreferences.getSharedPreferences(Constants.FilterAmenity);
        searchamenity.clear();
        if (filteramenity != null) {
            String[] split = filteramenity.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenity.contains(split[i])) {
                    searchamenity.add(split[i]);
                }
            }
        }

        for(int i = 0; i<searchamenity.size();i++){
            System.out.println("Amnenity values " +searchamenity.get(i));
        }

       /* System.out.println("Amnenity value 0 " +searchamenity.size());
        System.out.println("Amenity id " +amenitesid[0]);*/
        System.out.println("Amenity id " +amenitesid[0]);

        if (searchamenity != null && searchamenity.contains(amenitesid[0])) {
            wifi_check.setChecked(true);
        } else {
            wifi_check.setChecked(false);
        }
        if (searchamenity != null && searchamenity.contains(amenitesid[1])) {
            pool_check.setChecked(true);
        } else {
            pool_check.setChecked(false);
        }
        if (searchamenity != null && searchamenity.contains(amenitesid[2])) {
            kitchen_check.setChecked(true);
        } else {
            kitchen_check.setChecked(false);
        }
        checkStatuschange(wifi_check);
        checkStatuschange(pool_check);
        checkStatuschange(kitchen_check);

        // Fro service in filter
        filterservices = localSharedPreferences.getSharedPreferences(Constants.FilterService);
        searchservice.clear();
        if (filterservices != null) {
            String[] split = filterservices.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchservice.contains(split[i])) {
                    searchservice.add(split[i]);
                }
            }
        }

        //For service in filter:

        if(searchservice != null && searchservice.contains(serviceid[0])) {
            checkBoxfood.setChecked(true);
        } else {
            checkBoxfood.setChecked(false);
        }
        if (searchservice != null && searchservice.contains(serviceid[1])) {
            checkBoxlight.setChecked(true);
        } else {
            checkBoxlight.setChecked(false);
        }
        if (searchservice != null && searchservice.contains(serviceid[2])) {
            checkBoxfur.setChecked(true);
        } else {
            checkBoxfur.setChecked(false);
        }

        checkStatuschange(checkBoxlight);
        checkStatuschange(checkBoxfur);
        checkStatuschange(checkBoxfood);

        //For special feature in filter

        filterfeatures = localSharedPreferences.getSharedPreferences(Constants.FilterSpecial);
        searchspecial.clear();
        if(filterfeatures != null){
            String[] split = filterfeatures.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchspecial.contains(split[i])){
                    searchspecial.add(split[i]);
                }
            }
        }

        //For special features in filter:

        if(searchspecial != null && searchspecial.contains(specialid[0])) {
            checkBoxart.setChecked(true);
        } else {
            checkBoxart.setChecked(false);
        }
        if (searchspecial != null && searchspecial.contains(specialid[1])) {
            checkBoxdin.setChecked(true);
        } else {
            checkBoxdin.setChecked(false);
        }
        if (searchspecial != null && searchspecial.contains(specialid[2])) {
            checkBoxfire.setChecked(true);
        } else {
            checkBoxfire.setChecked(false);
        }

        checkStatuschange(checkBoxart);
        checkStatuschange(checkBoxdin);
        checkStatuschange(checkBoxfire);
        // For style in Filter
        filterstyles = localSharedPreferences.getSharedPreferences(Constants.FilterStyle);
        searchstyle.clear();
        if(filterstyles != null){
            String[] split = filterstyles.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchstyle.contains(split[i])){
                    searchstyle.add(split[i]);
                }
            }
        }

        //For space styles  in filter:

        if(searchstyle != null && searchstyle.contains(styleid[0])) {
            checkBoxclassic.setChecked(true);
        } else {
            checkBoxclassic.setChecked(false);
        }
        if (searchstyle != null && searchstyle.contains(styleid[1])) {
            checkBoxindustrial.setChecked(true);
        } else {
            checkBoxindustrial.setChecked(false);
        }
        if (searchstyle != null && searchstyle.contains(styleid[2])) {
            checkBoxinti.setChecked(true);
        } else {
            checkBoxinti.setChecked(false);
        }

        checkStatuschange(checkBoxclassic);
        checkStatuschange(checkBoxindustrial);
        checkStatuschange(checkBoxinti);
        //For space rules in filter
        filterspace = localSharedPreferences.getSharedPreferences(Constants.FilterSpace);
        searchspace.clear();
        if(filterspace != null){
            String[] split = filterspace.split(",");
            for(int i = 0; i< split.length; i++){
                if(!searchspace.contains(split[i])){
                    searchspace.add(split[i]);
                }
            }
        }

        //For space rules in filter:

        if(searchspace != null && searchspace.contains(spaceid[0])) {
            checkBoxsmoke.setChecked(true);
        } else {
            checkBoxsmoke.setChecked(false);
        }
        if (searchspace != null && searchspace.contains(spaceid[1])) {
            checkBoxcook.setChecked(true);
        } else {
            checkBoxcook.setChecked(false);
        }
        if (searchspace != null && searchspace.contains(spaceid[2])) {
            checkBoxloud.setChecked(true);
        } else {
            checkBoxloud.setChecked(false);
        }

        checkStatuschange(checkBoxsmoke);
        checkStatuschange(checkBoxcook);
        checkStatuschange(checkBoxloud);

        searchinstantbook = localSharedPreferences.getSharedPreferences(Constants.FilterInstantBook);
        /*filteramenities = localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);
        searchamenities.clear();
        if (filteramenities != null) {
            String[] split = filteramenities.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenities.contains(split[i])) {
                    searchamenities.add(split[i]);
                }
            }
        }*/
        searchroomtypes = localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes);
        searchbeds = localSharedPreferences.getSharedPreferences(Constants.FilterBeds);
        searchbedrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBedRoom);
        searchbathrooms = localSharedPreferences.getSharedPreferences(Constants.FilterBathRoom);
        searchminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPrice);
        searchmaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPrice);
        filterminprice = localSharedPreferences.getSharedPreferences(Constants.FilterMinPriceCheck);
        filtermaxprice = localSharedPreferences.getSharedPreferences(Constants.FilterMaxPriceCheck);

        if (searchinstantbook != null && Integer.parseInt(searchinstantbook) == 1) {
            isreset=false;
            instant_switch.setChecked(true);
        } else {
            isreset=false;
            instant_switch.setChecked(false);
        }


        searchspacetype = null;
        searchspacetype = localSharedPreferences.getSharedPreferences(Constants.FilterSpaceType);
        if(searchspacetype != null){
            String arrs[] = searchspacetype.split(",");
            if (arrs.length > 0) {
                tv_space.setVisibility(View.VISIBLE);
                tv_space.setText(String.valueOf(arrs.length));
            }
            else tv_space.setVisibility(View.GONE);
        } else {
            tv_space.setVisibility(View.GONE);
        }



        eventName = localSharedPreferences.getSharedPreferences(Constants.EventName);
        if(eventName == null){
           // tv_event.setText(getResources().getString(R.string.select));
        }
        else{
            tv_event.setVisibility(View.VISIBLE);
            tv_event.setText(eventName);
        }



       /* if (searchamenities != null && searchamenities.contains(amenitiesid[0])) {
            wifi_check.setChecked(true);
        } else {
            wifi_check.setChecked(false);
        }
        if (searchamenities != null && searchamenities.contains(amenitiesid[1])) {
            pool_check.setChecked(true);
        } else {
            pool_check.setChecked(false);
        }
        if (searchamenities != null && searchamenities.contains(amenitiesid[2])) {
            kitchen_check.setChecked(true);
        } else {
            kitchen_check.setChecked(false);
        }*/
        if (searchroomtypes != null) {
            String arrs[] = searchroomtypes.split(",");
            if (arrs.length > 0) roomtype_count.setText(String.valueOf(arrs.length));
            else roomtype_count.setText(getResources().getString(R.string.select));
        } else {
            roomtype_count.setText(getResources().getString(R.string.select));
        }

        if (searchroomtypes != null && searchroomtypes.contains("1")) {
            //entireplace.setChecked(true);
        } else {
            // entireplace.setChecked(false);
        }
        if (searchroomtypes != null && searchroomtypes.contains("2")) {
            //privateroom.setChecked(true);
        } else {
            //privateroom.setChecked(false);
        }
        if (searchroomtypes != null && searchroomtypes.contains("3")) {
            //sharedroom.setChecked(true);
        } else {
            //sharedroom.setChecked(false);
        }




        if (searchbeds != null) {
            bedcount = Integer.parseInt(searchbeds);
            enablebuttons();
        } else {
            bedcount = 0;
            enablebuttons();
        }
        if (searchbedrooms != null) {
            roomcount = Integer.parseInt(searchbedrooms);
            enablebuttons();
        } else {
            roomcount = 0;
            enablebuttons();
        }
        if (searchbathrooms != null) {
            bathcount = Integer.parseInt(searchbathrooms);
            enablebuttons();
        } else {
            bathcount = 0;
            enablebuttons();
        }

        if (filterminprice != null) {


            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText(" - "+currencysymbol + filterminprice);
            }else{
                min_amount.setText(currencysymbol + filterminprice + " - ");
            }


            seekBar.setSelectedMinValue(Integer.valueOf(filterminprice));
            seekBar.setSelectedMaxValue(Integer.valueOf(filtermaxprice));
        } else {


            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText(" - "+currencysymbol + searchminprice );
            }else{
                min_amount.setText(currencysymbol + searchminprice + " - ");
            }

            if ((!TextUtils.isEmpty(searchminprice) && TextUtils.isDigitsOnly(searchminprice)) && (!TextUtils.isEmpty(searchmaxprice) && TextUtils.isDigitsOnly(searchmaxprice))) {
                seekBar.setSelectedMinValue(Integer.valueOf(searchminprice));
                seekBar.setSelectedMaxValue(Integer.valueOf(searchmaxprice));
            }
        }


       /* if (filterminprice != null) {
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText("+"+currencysymbol + filterminprice.toString());
            }else {
                min_amount.setText(currencysymbol + filterminprice.toString() + "-");
            }
        } else {
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                min_amount.setText("+"+currencysymbol + searchminprice);
            }else {
                min_amount.setText(currencysymbol + searchminprice + "-");
            }

        }*/
        if (filtermaxprice != null) {
            max_amount.setText(currencysymbol + filtermaxprice.toString());

           /* if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                max_amount.setText("+"+currencysymbol + filtermaxprice.toString());
            }else {
                max_amount.setText(currencysymbol + filtermaxprice.toString() + "+");
            }*/
        } else {
            //max_amount.setText(currencysymbol + searchmaxprice + "+");
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                max_amount.setText("+"+currencysymbol + searchmaxprice);
            }else {
                max_amount.setText(currencysymbol + searchmaxprice + "+");
            }

        }

    }


    public void checkAmenityData(){

        if (wifi_check.isChecked()) {
            if (!searchamenity.contains(amenitesid[0])) {

                searchamenity.add(amenitesid[0]);
            }
        } else {
            if (searchamenity.contains(amenitesid[0])) {
                searchamenity.remove(amenitesid[0]);
            }
        }
        if (pool_check.isChecked()) {
            if (!searchamenity.contains(amenitesid[1])) {
                searchamenity.add(amenitesid[1]);
            }

        } else {
            if (searchamenity.contains(amenitesid[1])) {
                searchamenity.remove(amenitesid[1]);
            }
        }
        if (kitchen_check.isChecked()) {
            if (!searchamenity.contains(amenitesid[2])) {
                searchamenity.add(amenitesid[2]);
            }

        } else {
            if (searchamenity.contains(amenitesid[2])) {
                searchamenity.remove(amenitesid[2]);
            }
        }
        filteramenity = null;
        for (String s : searchamenity) {
            if (filteramenity != null) {
                filteramenity = filteramenity + "," + s;
            } else {
                filteramenity = s;
            }
        }
        int value = searchamenity.size();
        System.out.println("Count of Filter Amenity " + searchamenity.size());
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenity, filteramenity);
        //localSharedPreferences.saveSharedPreferences(Constants.Amenity,value);
    }


    public void checkService(){

        if (checkBoxfood.isChecked()) {
            if (!searchservice.contains(serviceid[0])) {

                searchservice.add(serviceid[0]);
            }
        } else {
            if (searchservice.contains(serviceid[0])) {
                searchservice.remove(serviceid[0]);
            }
        }
        if (checkBoxlight.isChecked()) {
            if (!searchservice.contains(serviceid[1])) {
                searchservice.add(serviceid[1]);
            }

        } else {
            if (searchservice.contains(serviceid[1])) {
                searchservice.remove(serviceid[1]);
            }
        }
        if (checkBoxfur.isChecked()) {
            if (!searchservice.contains(serviceid[2])) {
                searchservice.add(serviceid[2]);
            }

        } else {
            if (searchservice.contains(serviceid[2])) {
                searchservice.remove(serviceid[2]);
            }
        }
        filterservices = null;
        for (String s : searchservice) {
            if (filterservices != null) {
                filterservices = filterservices + "," + s;
            } else {
                filterservices = s;
            }
        }
        localSharedPreferences.saveSharedPreferences(Constants.FilterService, filterservices);
    }

    public  void  checkSpecial(){

        if (checkBoxart.isChecked()) {
            if (!searchspecial.contains(specialid[0])) {

                searchspecial.add(specialid[0]);
            }
        } else {
            if (searchspecial.contains(specialid[0])) {
                searchspecial.remove(specialid[0]);
            }
        }
        if (checkBoxdin.isChecked()) {
            if (!searchspecial.contains(specialid[1])) {
                searchspecial.add(specialid[1]);
            }

        } else {
            if (searchspecial.contains(specialid[1])) {
                searchspecial.remove(specialid[1]);
            }
        }
        if (checkBoxfire.isChecked()) {
            if (!searchspecial.contains(specialid[2])) {
                searchspecial.add(specialid[2]);
            }

        } else {
            if (searchspecial.contains(specialid[2])) {
                searchspecial.remove(specialid[2]);
            }
        }
        filterfeatures = null;
        for (String s : searchspecial) {
            if (filterfeatures != null) {
                filterfeatures = filterfeatures + "," + s;
            } else {
                filterfeatures = s;
            }
        }
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpecial, filterfeatures);
    }

    public void checkStyle(){

        if (checkBoxclassic.isChecked()) {
            if (!searchstyle.contains(styleid[0])) {

                searchstyle.add(styleid[0]);
            }
        } else {
            if (searchstyle.contains(styleid[0])) {
                searchstyle.remove(styleid[0]);
            }
        }
        if (checkBoxindustrial.isChecked()) {
            if (!searchstyle.contains(styleid[1])) {
                searchstyle.add(styleid[1]);
            }

        } else {
            if (searchstyle.contains(styleid[1])) {
                searchstyle.remove(styleid[1]);
            }
        }
        if (checkBoxinti.isChecked()) {
            if (!searchstyle.contains(styleid[2])) {
                searchstyle.add(styleid[2]);
            }

        } else {
            if (searchstyle.contains(styleid[2])) {
                searchstyle.remove(styleid[2]);
            }
        }
        filterstyles = null;
        for (String s : searchstyle) {
            if (filterstyles != null) {
                filterstyles = filterstyles + "," + s;
            } else {
                filterstyles = s;
            }
        }
        localSharedPreferences.saveSharedPreferences(Constants.FilterStyle, filterstyles);
    }

    public void checkSpace(){

        if (checkBoxsmoke.isChecked()) {
            if (!searchspace.contains(spaceid[0])) {

                searchspace.add(spaceid[0]);
            }
        } else {
            if (searchspace.contains(spaceid[0])) {
                searchspace.remove(spaceid[0]);
            }
        }
        if (checkBoxcook.isChecked()) {
            if (!searchspace.contains(spaceid[1])) {
                searchspace.add(spaceid[1]);
            }

        } else {
            if (searchspace.contains(spaceid[1])) {
                searchspace.remove(spaceid[1]);
            }
        }
        if (checkBoxloud.isChecked()) {
            if (!searchspace.contains(spaceid[2])) {
                searchspace.add(spaceid[2]);
            }

        } else {
            if (searchspace.contains(spaceid[2])) {
                searchspace.remove(spaceid[2]);
            }
        }
        filterspace = null;
        for (String s : searchspace) {
            if (filterspace != null) {
                filterspace = filterspace + "," + s;
            } else {
                filterspace = s;
            }
        }
        localSharedPreferences.saveSharedPreferences(Constants.FilterSpace, filterspace);
    }



    public void checkAmenaties() {

        if (wifi_check.isChecked()) {
            if (!searchamenities.contains(amenitiesid[0])) {

                searchamenities.add(amenitiesid[0]);
            }
        } else {
            if (searchamenities.contains(amenitiesid[0])) {
                searchamenities.remove(amenitiesid[0]);
            }
        }
        if (pool_check.isChecked()) {
            if (!searchamenities.contains(amenitiesid[1])) {
                searchamenities.add(amenitiesid[1]);
            }

        } else {
            if (searchamenities.contains(amenitiesid[1])) {
                searchamenities.remove(amenitiesid[1]);
            }
        }
        if (kitchen_check.isChecked()) {
            if (!searchamenities.contains(amenitiesid[2])) {
                searchamenities.add(amenitiesid[2]);
            }

        } else {
            if (searchamenities.contains(amenitiesid[2])) {
                searchamenities.remove(amenitiesid[2]);
            }
        }
        filteramenities = null;
        for (String s : searchamenities) {
            if (filteramenities != null) {
                filteramenities = filteramenities + "," + s;
            } else {
                filteramenities = s;
            }
        }

        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, filteramenities);
    }

    public void enablebuttons() {
        if (bedcount == 0) {
            bedtext.setText(getResources().getString(R.string.anybed));
            bedminus.setEnabled(false);
            bedplus.setEnabled(true);
            //localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
        } else if (bedcount >= 16) {
            bedminus.setEnabled(true);
            bedplus.setEnabled(false);
            bedtext.setText(bedcount + " " + getResources().getString(R.string.beds));
           // localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, Integer.toString(bedcount));
        } else {
            bedtext.setText(bedcount + "" + getResources().getString(R.string.bedss));
            bedminus.setEnabled(true);
            bedplus.setEnabled(true);
            //localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, Integer.toString(bedcount));
        }

        if (roomcount == 0) {
            roomtext.setText(getResources().getString(R.string.anybedrooms));
            roomminus.setEnabled(false);
            roomplus.setEnabled(true);
           // localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
        } else if (roomcount >= 10) {
            roomminus.setEnabled(true);
            roomplus.setEnabled(false);
            roomtext.setText(roomcount + " " + getResources().getString(R.string.bedrooms));
            //localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, Integer.toString(roomcount));
        } else {
            roomtext.setText(roomcount + "" + getResources().getString(R.string.bedrooms));
            roomminus.setEnabled(true);
            roomplus.setEnabled(true);
            //localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, Integer.toString(roomcount));
        }

        if (bathcount == 0) {
            bathtext.setText(getResources().getString(R.string.anybathrooms));
            bathminus.setEnabled(false);
            bathplus.setEnabled(true);


        } else if (bathcount >= 8) {
            bathminus.setEnabled(true);
            bathplus.setEnabled(false);
            bathtext.setText(bathcount + " " + getResources().getString(R.string.bathrooms));


        } else {
            bathtext.setText(bathcount + "" + getResources().getString(R.string.bathrooms));
            bathminus.setEnabled(true);
            bathplus.setEnabled(true);

        }

        plusMinus(bathminus, bathplus);
        plusMinus(roomminus, roomplus);
        plusMinus(bedminus, bedplus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFilterData();
    }

    public void SetClick(CheckBox checkBox){
        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isreset=false;
                checkBox.setChecked(checkBox.isChecked());
                checkStatuschange(checkBox);
            }
        });
    }

    public void checkStatuschange(CheckBox view) {
        if (view.equals(R.id.checkBox))

            Toast.makeText(getApplicationContext(), "isChecked 2 - " + view.isChecked(), Toast.LENGTH_SHORT).show();
        if (view.isChecked()) {
            view.setButtonDrawable(icon1);
        } else {
            view.setButtonDrawable(icon);
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

    public void roomPropertyList() {
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
       // apiService.roomproperty(localSharedPreferences.getSharedPreferences(Constants.AccessToken),langCode).enqueue(new RequestCallback(REQ_ROOMTYPE,this));
    }

    public void RelationshipList(String selectedtext) {
        relationshipList = new RecyclerView(FilterActivity.this);
        room_type_models = new ArrayList<Room_Type_model>();

        roomTypeAdapter = new RoomTypeAdapter(this, room_type_models);


        relationshipList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        relationshipList.setAdapter(roomTypeAdapter);
        loadrelationship(selectedtext);
        //loadrelationships(selectedtext);

        LayoutInflater inflater = getLayoutInflater();

        alertDialog = new android.app.AlertDialog.Builder(FilterActivity.this)
                //  .setCustomTitle(view)
                .setView(relationshipList).setCancelable(true);

        alertDialog.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                rootypelayout.setEnabled(true);
            }
        });

        Button b1 = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (b1 != null) {
            b1.setTextColor(getResources().getColor(R.color.red_text));
        }

    }

    private void checkvalues(){
        checkAmenityData();
        checkSpace();
        checkService();
        checkStyle();
        checkSpecial();
    }

    public void loadrelationship(String selectedtext) {
        searchlist_val = searchlist;
        if(searchlist_val!=null && searchlist_val.size()>0) {

            for (int i = 0; i < searchlist_val.size(); i++) {

                Room_Type_model listdata = new Room_Type_model();
                listdata.type = searchlist_val.get(i).type;
                listdata.setId(searchlist_val.get(i).getId());
                listdata.setDesc(searchlist_val.get(i).getDesc());
                listdata.setName(searchlist_val.get(i).getName());
                listdata.setIsShared(searchlist_val.get(i).getIsShared());
                listdata.setImage(searchlist_val.get(i).getImage());
                listdata.setType("filter");

                String[] split = selectedtext.split(",");
                for (int j = 0; j < split.length; j++) {

                    if (searchlist_val.get(i).getId().equals(split[j])) {
                        // searchamenities.add(split[j]);
                        listdata.setIsSelected(true);
                        break;
                    } else {
                        listdata.setIsSelected(false);
                    }
                }
                room_type_models.add(listdata);
            }
        }
        roomTypeAdapter.notifyDataSetChanged();
    }

    /**
     * @Reference Get more filter data from server
     */

    public void getActivityData(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.search_filter(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_ACT_DATA,this));
    }


    /**
     * @Reference Get amenities list from server
     */
    public void getAmenitiesList() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        //apiService.getServiceList(localSharedPreferences.getSharedPreferences(Constants.AccessToken), localSharedPreferences.getSharedPreferences(Constants.LanguageCode)).enqueue(new RequestCallback(REQ_DATA,this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, edt, getResources(), this);
            return;
        }


        switch (jsonResp.getRequestCode()) {
            case REQ_DATA:
                if (jsonResp.isSuccess()) {
                    getAmenitiesList(jsonResp);
                }else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, getResources(), this);
                }
                break;

            case REQ_ROOMTYPE:
                if (jsonResp.isSuccess()) {
                    onSuccessRes(jsonResp);
                }else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, getResources(), this);
                }
                break;

            case REQ_ACT_DATA:
                if (jsonResp.isSuccess()) {
                    getActivityData(jsonResp);
                    getServicedata(jsonResp);
                    getSpecialData(jsonResp);
                    getSpaceData(jsonResp);
                    getStyleData(jsonResp);
                    //getSpaceTypeData(jsonResp);
                }else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, getResources(), this);
                }
                break;
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }


    /*
    Get filter data from webservice:
    * */
    public void getActivityData(JsonResponse jsonResponse) {
        try {

            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            amenities_list = response.getJSONArray("amenities");

            for (int i = 0; i < amenities_list.length(); i++) {

                JSONObject dataJSONObject = amenities_list.getJSONObject(i);
                Filter_Model listdata = new Filter_Model();
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setName(dataJSONObject.getString("name"));
                if (i == 0) {
                    amenities1_txt.setText(dataJSONObject.getString("name"));
                    amenitesid[i] = dataJSONObject.getString("id");
                    System.out.println("Amenities Id  " + amenitesid[i]);
                } else if (i == 1) {
                    amenities2_txt.setText(dataJSONObject.getString("name"));
                    amenitesid[i] = dataJSONObject.getString("id");
                    System.out.println("Amenities Id 1" + amenitesid[i]);
                } else if (i == 2) {
                    amenities3_txt.setText(dataJSONObject.getString("name"));
                    amenitesid[i] = dataJSONObject.getString("id");
                    System.out.println("Amenities Id 2" + amenitesid[i]);
                }
            }
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            loadFilterData();
        } catch (Exception e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }


    }


    public void getServicedata(JsonResponse jsonResponse) {


        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            serviceList = response.getJSONArray("services");

            for (int i = 0; i < serviceList.length(); i++) {
                JSONObject dataJSONObject = serviceList.getJSONObject(i);
                Filter_Model listdata = new Filter_Model();
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setName(dataJSONObject.getString("name"));
                if (i == 0) {
                    service1_txt.setText(dataJSONObject.getString("name"));
                    serviceid[i] = dataJSONObject.getString("id");
                } else if (i == 1) {
                    service2_txt.setText(dataJSONObject.getString("name"));
                    serviceid[i] = dataJSONObject.getString("id");
                    System.out.println("Service id " + serviceid[i]);
                } else if (i == 2) {
                    service3_txt.setText(dataJSONObject.getString("name"));
                    serviceid[i] = dataJSONObject.getString("id");
                }

            }
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            loadFilterData();
        } catch (Exception e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

         /*   services = activityModel.getServices();
            servicelist.clear();
            if(services!=null && services.size()>0){
                for (int i = 0; i < services.size(); i++) {
                    Services servicedata = new Services();
                    servicedata.setId(services.get(i).getId());
                    servicedata.setName(services.get(i).getName());
                    servicelist.add(servicedata);
                }
            }

            for(int i = 0; i<servicelist.size();i++){

                if(i == 0){
                    service1_txt.setText(servicelist.get(i).getName());
                    serviceid[i] = String.valueOf(servicelist.get(i).getId());
                }else if(i == 1){
                    service2_txt.setText(servicelist.get(i).getName());
                    serviceid[i] = String.valueOf(servicelist.get(i).getId());
                }else if(i == 2){
                    service3_txt.setText(servicelist.get(i).getName());
                    serviceid[i] = String.valueOf(servicelist.get(i).getId());
                }

            }*/

    public void getSpaceData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());

            spacelist = response.getJSONArray("space_rules");

            for (int i = 0; i < spacelist.length(); i++) {
                JSONObject dataJSONObject = spacelist.getJSONObject(i);
                Filter_Model listdata = new Filter_Model();
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setName(dataJSONObject.getString("name"));
                if (i == 0) {
                    space1_txt.setText(dataJSONObject.getString("name"));
                    spaceid[i] = dataJSONObject.getString("id");
                } else if (i == 1) {
                    space2_txt.setText(dataJSONObject.getString("name"));
                    spaceid[i] = dataJSONObject.getString("id");
                } else if (i == 2) {
                    space3_txt.setText(dataJSONObject.getString("name"));
                    spaceid[i] = dataJSONObject.getString("id");
                }

            }
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            loadFilterData();
        } catch (Exception e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }


          /*  space_rules = activityModel.getSpace_rules();
            spacerules.clear();
            if(space_rules!=null && space_rules.size()>0){
                for (int i = 0; i < space_rules.size(); i++) {
                    Space_rules servicedata = new Space_rules();
                    servicedata.setId(space_rules.get(i).getId());
                    servicedata.setName(space_rules.get(i).getName());
                    spacerules.add(servicedata);
                }
            }

            for(int i = 0; i<spacerules.size();i++){

                if(i == 0){
                    space1_txt.setText(spacerules.get(i).getName());
                    spaceid[i] = String.valueOf(spacerules.get(i).getId());
                }else if(i == 1){
                    space2_txt.setText(spacerules.get(i).getName());
                    spaceid[i] = String.valueOf(spacerules.get(i).getId());
                }else if(i == 2){
                    space3_txt.setText(spacerules.get(i).getName());
                    spaceid[i] = String.valueOf(spacerules.get(i).getId());
                }

            }*/
          public void getStyleData(JsonResponse jsonResponse) {
              try {
                  JSONObject response = new JSONObject(jsonResponse.getStrResponse());

                  stylelist = response.getJSONArray("space_styles");
                  for (int i = 0; i < stylelist.length(); i++) {
                      JSONObject dataJSONObject = stylelist.getJSONObject(i);
                      Filter_Model listdata = new Filter_Model();
                      listdata.setId(dataJSONObject.getString("id"));
                      listdata.setName(dataJSONObject.getString("name"));
                      if (i == 0) {
                          style1_txt.setText(dataJSONObject.getString("name"));
                          styleid[i] = dataJSONObject.getString("id");
                      } else if (i == 1) {
                          style2_txt.setText(dataJSONObject.getString("name"));
                          styleid[i] = dataJSONObject.getString("id");
                      } else if (i == 2) {
                          style3_txt.setText(dataJSONObject.getString("name"));
                          styleid[i] = dataJSONObject.getString("id");
                      }

                  }
                  if (mydialog.isShowing()) {
                      mydialog.dismiss();
                  }
                  loadFilterData();
              } catch (Exception e) {
                  e.printStackTrace();
                  if (mydialog.isShowing()) {
                      mydialog.dismiss();
                  }
              }
          }



    public void getSpecialData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            speciallist = response.getJSONArray("special_features");
            for (int i = 0; i < speciallist.length(); i++) {
                JSONObject dataJSONObject = speciallist.getJSONObject(i);
                Filter_Model listdata = new Filter_Model();
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setName(dataJSONObject.getString("name"));

                if (i == 0) {
                    special1_txt.setText(dataJSONObject.getString("name"));
                    specialid[i] = dataJSONObject.getString("id");
                } else if (i == 1) {
                    special2_txt.setText(dataJSONObject.getString("name"));
                    specialid[i] = dataJSONObject.getString("id");
                } else if (i == 2) {
                    special3_txt.setText(dataJSONObject.getString("name"));
                    specialid[i] = dataJSONObject.getString("id");
                }

            }

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            loadFilterData();
        } catch (Exception e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }


    public void getAmenitiesList(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            amenities_list = response.getJSONArray("data");
            for (int i = 0; i < amenities_list.length(); i++) {


                JSONObject dataJSONObject = amenities_list.getJSONObject(i);
                Makent_model listdata = new Makent_model();
                listdata.setAmenitiesId(dataJSONObject.getString("id"));
                listdata.setAmenities(dataJSONObject.getString("name"));
                if (i == 0) {
                    amenities1_txt.setText(dataJSONObject.getString("name"));
                    amenitiesid[i] = dataJSONObject.getString("id");
                } else if (i == 1) {
                    amenities2_txt.setText(dataJSONObject.getString("name"));
                    amenitiesid[i] = dataJSONObject.getString("id");
                } else if (i == 2) {
                    amenities3_txt.setText(dataJSONObject.getString("name"));
                    amenitiesid[i] = dataJSONObject.getString("id");
                }
            }

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            loadFilterData();
        } catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    public void onSuccessRes(JsonResponse jsonResp) {
        try {
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            JSONArray data = response.getJSONArray("room_type");
            JSONArray data1 = response.getJSONArray("property_type");
            JSONArray bettype = response.getJSONArray("bed_type");
            localSharedPreferences.saveSharedPreferences(Constants.Bedtype, bettype.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Roomtype, data.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Propertytype, data1.toString());
        } catch (JSONException j) {
            j.printStackTrace();
        }

        roomPropertyResult = gson.fromJson(jsonResp.getStrResponse(), RoomPropertyResult.class);
        roomTypeModels = roomPropertyResult.getRoomTypeModels();
        searchlist.clear();
        if(roomTypeModels!=null && roomTypeModels.size()>0){
            for (int i = 0; i < roomTypeModels.size(); i++) {
                Room_Type_model listdata = new Room_Type_model();
                listdata.type = "item";
                listdata.setName(roomTypeModels.get(i).getName());
                listdata.setDesc(roomTypeModels.get(i).getDescription());
                listdata.setId(roomTypeModels.get(i).getId());
                listdata.setIsShared(roomTypeModels.get(i).getIsShared());
                listdata.setImage(roomTypeModels.get(i).getImageName());
                listdata.setType("filter");
                searchlist.add(listdata);
            }
        }

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
}