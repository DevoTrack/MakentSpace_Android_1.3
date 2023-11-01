package com.makent.trioangle.host.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage host/tabs
 * @category HostCalenderActivity
 * @author Trioangle Product Team
 * @version 1.1
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.CalendarListingdetailsAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.host.LYSActivity;
import com.makent.trioangle.host.LYS_Step3_SetPrice;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.HostCalendarListModel;
import com.makent.trioangle.model.host.HostCalendarModel;
import com.makent.trioangle.model.host.Makent_host_model;
import com.makent.trioangle.model.host.updateModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.DefaultDayViewAdapter;
import com.squareup.timessquare.HostCalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_HOSTLOAD;
import static com.makent.trioangle.util.Enums.REQ_HOSTUPDATE;

/* ************************************************************
Host listing calendar page contain available, blocked, reserved dates
*************************************************************** */
public class HostCalenderActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener, MonthLoader.MonthChangeListener {

    public static android.app.AlertDialog alertDialogStores;
    RecyclerView recyclerView1;
    List<Makent_host_model> makent_host_modelList;
    CalendarListingdetailsAdapter calendaradapter;
    Context context;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    String roomid, calendarlist, isavailable;

    private HostCalendarPickerView calendar;
    public static Button savechanges, cancel;
    public static LinearLayout hostcalendar_bottom;
    LinearLayout hostcalendar_title;
    RelativeLayout nightly_price, calendar_list;
    public static TextView nightlyprice_edittext;

    String userid, update_nightprice;
    LocalSharedPreferences localSharedPreferences;

    ImageView calendar_list_image;
    TextView calendar_list_title, nightlyprice_symbol, calendar_list_unlist, calendar_list_hometype;

    Calendar nextYear = null;

    private HostCalendarPickerView.OnDateSelectedListener dateListener;
    String[] blocked_dates, reserved_dates;
    ArrayList<Date> hdates, bdates;
    List<Date> selectedCals = new ArrayList<>();
    StringBuilder selected_dates;
    Snackbar snackbar;
    public static RadioGroup datestatus;
    public RadioButton radioButton;
    boolean updatecal = false;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    HostCalendarModel hostCalendar;

    updateModel updateCalendar;

    EditText sample;
    ImageView calendar_dot_loader;
    RelativeLayout emptycalendar;
    LinearLayout listcalendar;
    Button calendar_addlisting;
    private int backPressed = 0;    // used by onBackPressed()
    private ArrayList<HostCalendarListModel> hostCalendarList;
    private boolean doubleBackToExitPressedOnce=false;


    //Added for Makent Space Calendar:
    WeekView weekView;
    StringBuilder selected_dates_hour;
    StringBuilder selected_time_hour;
    Animation slide_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_calendar);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        /*For Makent Space*/
        initialize();
        weekView.setMonthChangeListener(this);
        weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
        weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        weekView.goToToday();

        weekView.setShowDistinctPastFutureColor(true);
        weekView.setNumberOfVisibleDays(7);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        calendar_dot_loader = (ImageView) findViewById(R.id.calendar_dot_loader);
        calendar_dot_loader.setVisibility(View.GONE);
       /* DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(calendar_dot_loader);
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1);*/

        emptycalendar = (RelativeLayout) findViewById(R.id.emptycalendar);
        listcalendar = (LinearLayout) findViewById(R.id.calendar_available);
        sample = (EditText) findViewById(R.id.edt);


        weekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                //timed=time;
                boolean currenttime = false, avail = false;
                System.out.println("onclickempty" + time);

                /*
                Got time and format it
                 */

                Date dateStr = time.getTime();
                dateStr.setMinutes(00);
                dateStr.setSeconds(00);
                DateFormat readFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                SimpleDateFormat weekFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);

                DateFormat writeFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                DateFormat timeformat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                Date date = null;
                String day = null;
                try {
                    date = readFormat.parse(dateStr.toString());
                    day = weekFormat.format(time.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                System.out.println("available=" + avail);
                String formattedDate = "";
                String formattedtime = "";
                if (date != null) {
                    formattedDate = writeFormat.format(date);
                    formattedtime = timeformat.format(date);
                }

                System.out.println("Selected date in search format " + formattedDate);

                System.out.println("Selected date in search timeformat " + formattedtime);

          /*
            Append in String builder
           */

                selected_dates_hour = new StringBuilder();
                selected_time_hour = new StringBuilder();
                selected_dates_hour.append(formattedDate);
                selected_time_hour.append(formattedtime);

                System.out.println("selected hour : " + selected_dates_hour);
                System.out.println("selected time : " + selected_time_hour);




                savechanges.setVisibility(View.VISIBLE);
                hostcalendar_bottom.setVisibility(View.VISIBLE);

                slide_up = AnimationUtils.loadAnimation(HostCalenderActivity.this,
                        R.anim.slide_up_bottom);
                hostcalendar_bottom.startAnimation(slide_up);

                hostcalendar_bottom.animate()
                        .translationY(0)
                        .setDuration(200);

            }
        });


       /* calendar_dot_loader.setVisibility(View.VISIBLE);
        emptycalendar.setVisibility(View.GONE);
        listcalendar.setVisibility(View.GONE);*/

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        calendar_list_image = (ImageView) findViewById(R.id.calendar_list_image);
        calendar_list = (RelativeLayout) findViewById(R.id.calendar_list);
        calendar_list_title = (TextView) findViewById(R.id.calendar_list_title);
        calendar_list_unlist = (TextView) findViewById(R.id.calendar_list_unlist);
        calendar_list_hometype = (TextView) findViewById(R.id.calendar_list_hometype);
        nightlyprice_symbol = (TextView) findViewById(R.id.nightlyprice_symbol);
        datestatus = (RadioGroup) findViewById(R.id.datestatus);

        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = (HostCalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(HostCalendarPickerView.SelectionMode.SINGLE); //


        calendar.setCustomDayView(new DefaultDayViewAdapter());
        Calendar today = Calendar.getInstance();
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextYear.getTime()) //
                .inMode(HostCalendarPickerView.SelectionMode.MULTIPLE); //

        calendar.setFocusable(true);

        calendar_addlisting = (Button) findViewById(R.id.calendar_addlisting);
        savechanges = (Button) findViewById(R.id.hostcalendar_save);
        savechanges.setVisibility(View.INVISIBLE);
        cancel = (Button) findViewById(R.id.hostcalendar_cancel);
        hostcalendar_bottom = (LinearLayout) findViewById(R.id.hostcalendar_bottom);
        hostcalendar_title = (LinearLayout) findViewById(R.id.hostcalendar_title);
        nightly_price = (RelativeLayout) findViewById(R.id.hostcalendar_nightly_price);


        nightlyprice_edittext = (TextView) findViewById(R.id.nightlyprice_edittext);


        calendar_addlisting.setOnClickListener(this);
        savechanges.setOnClickListener(this);
        cancel.setOnClickListener(this);
        nightly_price.setOnClickListener(this);
        hostcalendar_title.setOnClickListener(this);
        calendar_list.setOnClickListener(this);
        nightlyprice_edittext.setOnClickListener(this);
        makent_host_modelList = new ArrayList<>();


        if (isInternetAvailable) {
            //loadHostCalendar();
        } else {
            snackBar();
            //  commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,sample,sample,getResources(),this);
        }


    }



    private void initialize(){
        weekView = (WeekView)findViewById(R.id.weekView);

    }

    public WeekView getWeekView() {
        return weekView;
    }

    private void loadHostCalendar() {
        calendar_dot_loader.setVisibility(View.VISIBLE);
        emptycalendar.setVisibility(View.GONE);
        listcalendar.setVisibility(View.GONE);

        //apiService.roomListCalendar(userid).enqueue(new RequestCallback(REQ_HOSTLOAD, this));
    }

    private void updateHostCalendar() {
        if (!mydialog.isShowing()) mydialog.show();
        //apiService.updateListCalendar(userid, roomid, selected_dates.toString(), isavailable, update_nightprice).enqueue(new RequestCallback(REQ_HOSTUPDATE, this));

    }


    // Get calendar listed rooms list
    public void calendar_list() {

        if (updatecal) {
            makent_host_modelList.clear(); //clear list
            calendaradapter.notifyDataSetChanged();
            if (isInternetAvailable) {
                //loadHostCalendar();

            } else {
                commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, sample, sample, getResources(), this);
            }
        }
        recyclerView1 = new RecyclerView(HostCalenderActivity.this);
        //  makent_host_modelList = new ArrayList<>();


        // recyclerView.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView1.setAdapter(calendaradapter);


        alertDialogStores = new android.app.AlertDialog.Builder(HostCalenderActivity.this).setView(recyclerView1).setCancelable(true).show();

        alertDialogStores.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
               // loadCalendarDetails();

            }
        });

    }

    // Load selected rooms details in calendar
    public void loadCalendarDetails() {

        calendarlist = localSharedPreferences.getSharedPreferences(Constants.CalendarRoomList);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        try {
            JSONArray data = new JSONArray(calendarlist);
            for (int i = 0; i < data.length(); i++) {

                String room_id = hostCalendarList.get(i).getRoomId();
                if (room_id.equals(roomid)) {
                    calendar_list_title.setText(hostCalendarList.get(i).getRoomName());
                    calendar_list_hometype.setText(hostCalendarList.get(i).getRoomType());

                    nightlyprice_edittext.setText(hostCalendarList.get(i).getRoomPrice().toString());
                    String room_currency_symbols = Html.fromHtml(hostCalendarList.get(i).getRoomCurrencySymbol()).toString();
                    nightlyprice_symbol.setText(room_currency_symbols);

                    System.out.println("currency symbol check one " + room_currency_symbols + " Price " + hostCalendarList.get(i).getRoomPrice().toString());

                    localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode, hostCalendarList.get(i).getRoomCurrencyCode());
                    localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol, room_currency_symbols);
                    localSharedPreferences.saveSharedPreferences(Constants.RoomPrice, hostCalendarList.get(i).getRoomPrice());
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomPricea, hostCalendarList.get(i).getRoomPrice());

                    if (Integer.parseInt(hostCalendarList.get(i).getRemainingSteps()) != 0) {
                        calendar_list_unlist.setText(getResources().getString(R.string.unlisted));
                    } else {
                        calendar_list_unlist.setText(getResources().getString(R.string.listed));
                    }
                    Glide.with(getApplicationContext())
                            .load(hostCalendarList.get(i).getRoomThumbImages()[0])
                            .into(new DrawableImageViewTarget(calendar_list_image) {
                                @Override
                                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    super.onResourceReady(resource, transition);
                                }
                            });
                    System.out.println("Nightly incoming check one one " + hostCalendarList.get(i).getNightlyPrice().toString());

                    localSharedPreferences.saveSharedPreferences(Constants.NightPrice, hostCalendarList.get(i).getNightlyPrice().toString());

                    reserved_dates = new String[hostCalendarList.get(i).getReservedDates().length];
                    for (int j = 0; j < hostCalendarList.get(i).getReservedDates().length; j++) {
                        reserved_dates[j] = hostCalendarList.get(i).getReservedDates()[j];
                    }


                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    hdates = new ArrayList<Date>();
                    try {
                        for (int j = 0; j < reserved_dates.length; j++) {
                            System.out.println("reserved_dates" + df.parse(reserved_dates[j]));
                            hdates.add(df.parse(reserved_dates[j]));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Get Blocked Date from webservice
                    blocked_dates = new String[hostCalendarList.get(i).getBlockedDates().length];
                    for (int k = 0; k < hostCalendarList.get(i).getBlockedDates().length; k++) {
                        blocked_dates[k] = hostCalendarList.get(i).getBlockedDates()[k];
                    }


                    bdates = new ArrayList<Date>();
                    System.out.println("blocked_dates_check " + hostCalendarList.get(i).getBlockedDates().length);

                    if (hostCalendarList.get(i).getBlockedDates().length > 0) {

                        System.out.println("blocked_dates_checkingin " + hostCalendarList.get(i).getBlockedDates()[0] + " checking in " + blocked_dates[0]);

                    }


                    try {
                        for (int j = 0; j < blocked_dates.length; j++) {

                            bdates.add(df.parse(blocked_dates[j]));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    calendar.init(new Date(), nextYear.getTime()).inMode(HostCalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(hdates).withBlockedDates(bdates);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //snackBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String roomprice = localSharedPreferences.getSharedPreferences(Constants.ListRoomPricea);
        System.out.println("currency symbol check five " + " empty symbol " + " Price " + roomprice);


        if (roomprice != null && !roomprice.equals("")) {
            nightlyprice_edittext.setText(roomprice);
        }
        // nightlyprice_edittext.setText();

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
           // loadHostCalendar();
            //new ExploreSearch().execute();
        } else {
            snackBar();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hostcalendar_nightly_price: {
               /* nightlyprice_edittext.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nightlyprice_edittext, InputMethodManager.SHOW_IMPLICIT);*/

                // Set nightly for selected dates to call set price page
                Intent price = new Intent(getApplicationContext(), LYS_Step3_SetPrice.class);
                price.putExtra("host", "host");
                startActivity(price);

                //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //nightlyprice_edittext.setFocusable(true);

            }
            break;
            case R.id.nightlyprice_edittext: {
               /* nightlyprice_edittext.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nightlyprice_edittext, InputMethodManager.SHOW_IMPLICIT);*/

                // Set nightly for selected dates to call set price page
                Intent price = new Intent(getApplicationContext(), LYS_Step3_SetPrice.class);
                price.putExtra("host", "host");
                startActivity(price);

                //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //nightlyprice_edittext.setFocusable(true);

            }
            break;

            case R.id.hostcalendar_save: {

                // get selected radio button from radioGroup
                int selectedId = datestatus.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                //Toast.makeText(this,radioButton.getText(), Toast.LENGTH_SHORT).show();

                if (radioButton.getText().toString().equals(getResources().getString(R.string.available))) {
                    isavailable = "Yes";
                } else {
                    isavailable = "No";
                }

                roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

                selectedCals = calendar.getSelectedDates();
                selected_dates = new StringBuilder();
                for (int i = 0; i < selectedCals.size(); i++) {

                    if (i == 0) {
                        selected_dates.append(getFormatedDateSearch(selectedCals.get(i).toString()));
                    } else {
                        selected_dates.append("," + getFormatedDateSearch(selectedCals.get(i).toString()));
                    }
                }
                update_nightprice = nightlyprice_edittext.getText().toString();

                update_nightprice = update_nightprice.replaceAll("^\\s+|\\s+$", "");
                if (!update_nightprice.equals("")) {
                    //new UpdateDateDetails().execute();


                    updateHostCalendar();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nightlyprice_edittext.getWindowToken(), 0);
                    //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    // hostcalendar_bottom.startAnimation(slide_down);
                    hostcalendar_bottom.animate().translationY(500).setDuration(200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hostcalendar_bottom.setVisibility(View.GONE);
                        }
                    }, 200);
                } else {

                    commonMethods.snackBar("Invalide night price...", "", false, 2, sample, sample, getResources(), this);
                }
                System.out.println("Selected Dates " + selected_dates.toString());


                // hostcalendar_bottom.startAnimation(slide_down);
            }
            break;
            case R.id.hostcalendar_cancel: {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nightlyprice_edittext.getWindowToken(), 0);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //hostcalendar_bottom.startAnimation(slide_down);
                hostcalendar_bottom.animate().translationY(500).setDuration(200);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hostcalendar_bottom.setVisibility(View.GONE);
                    }
                }, 200);

                calendar.init(new Date(), nextYear.getTime()) //
                        .inMode(HostCalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(hdates).withBlockedDates(bdates);
                ;
            }
            break;
            case R.id.hostcalendar_title: {
                calendar_list();  // calendar room list
            }
            break;
            case R.id.calendar_list: {
                calendar_list(); // calendar room list
            }
            break;

            case R.id.calendar_addlisting: {
                // Call List your space activity to create new listing
                localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, "");
                localSharedPreferences.saveSharedPreferences(Constants.MinimumStay, "");
                localSharedPreferences.saveSharedPreferences(Constants.MaximumStay, "");
                localSharedPreferences.saveSharedPreferences(Constants.AvailableRulesOption, "");
                localSharedPreferences.saveSharedPreferences(Constants.LastMinCount, "");
                localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount, "");
                localSharedPreferences.saveSharedPreferences(Constants.LengthOfStay, "");
                Intent lys = new Intent(getApplicationContext(), LYSActivity.class);
                startActivity(lys);
            }
            break;
        }
    }

    // Convet selected date format
    public static String getFormatedDateSearch(String strDate) {
        String dateStr = strDate;
        DateFormat readFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyy", Locale.ENGLISH);

        DateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        System.out.println("Selected date in search format " + formattedDate);

        StringTokenizer tokens = new StringTokenizer(formattedDate, "-");
        String first = tokens.nextToken();// this will contain "Fruit"
        String second = tokens.nextToken();
        String third = tokens.nextToken();
        // int month=Integer.parseInt(second)+1;
        String dateformated = "" + first + "-" + second + "-" + third;
        System.out.println("Selected date in search format " + dateformated);
        return dateformated;

    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();

        switch (jsonResp.getRequestCode()) {

            case REQ_HOSTLOAD:
                if (jsonResp.isSuccess()) {
                    onSuccessCalendarLoad(jsonResp); // onSuccess call method
                } else {
                    calendar_dot_loader.setVisibility(View.GONE);
                    emptycalendar.setVisibility(View.VISIBLE);
                    listcalendar.setVisibility(View.GONE);
                }
                break;
            case REQ_HOSTUPDATE:
                if (jsonResp.isSuccess()) {
                    onSuccessCalendarUpdate(jsonResp);
                } else {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, sample, calendar_list_title, getResources(), this);
                }


        }
    }

    private void onSuccessCalendarUpdate(JsonResponse jsonResp) {

        updateCalendar = gson.fromJson(jsonResp.getStrResponse(), updateModel.class);


        if (mydialog.isShowing()) mydialog.dismiss();


        if (hostCalendar.getStatusCode().matches("1")) {
            updatecal = true;


            localSharedPreferences.saveSharedPreferences(Constants.NightPrice, updateCalendar.getNightlyPrice().toString());


            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            hdates = new ArrayList<Date>();
            try {
                for (int j = 0; j < updateCalendar.getReservedDates().length; j++) {
                    System.out.println("reserved_dates" + df.parse(updateCalendar.getReservedDates()[j]));
                    hdates.add(df.parse(updateCalendar.getReservedDates()[j]));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            bdates = new ArrayList<Date>();
            try {
                for (int j = 0; j < updateCalendar.getBlockedDates().length; j++) {
                    bdates.add(df.parse(updateCalendar.getBlockedDates()[j]));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(HostCalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(hdates).withBlockedDates(bdates);

            if (mydialog.isShowing()) mydialog.dismiss();
        } else {
            Snackbar snackbar = Snackbar.make(calendar, "", Snackbar.LENGTH_LONG);

            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
            snackbar.show();
            if (mydialog.isShowing()) mydialog.dismiss();

            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(HostCalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(hdates).withBlockedDates(bdates);

            hostcalendar_bottom.setVisibility(View.GONE);
        }
    }

    private void onSuccessCalendarLoad(JsonResponse jsonResp) {

        hostCalendar = gson.fromJson(jsonResp.getStrResponse(), HostCalendarModel.class);

        hostCalendarList = hostCalendar.getCalendarListModels();

        calendar_dot_loader.setVisibility(View.GONE);
        listcalendar.setVisibility(View.VISIBLE);
        emptycalendar.setVisibility(View.GONE);
        localSharedPreferences.saveSharedPreferences(Constants.CalendarRoomList, hostCalendarList.toString());

        for (int i = 0; i < hostCalendarList.size(); i++) {
            updatecal = false;




            if (i == 0) {



                nightlyprice_edittext.setText(hostCalendarList.get(i).getRoomPrice());

                nightlyprice_symbol.setText(Html.fromHtml(hostCalendarList.get(i).getRoomCurrencySymbol().toString()));

                localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode, hostCalendarList.get(i).getRoomCurrencyCode());
                localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol, hostCalendarList.get(i).getRoomCurrencySymbol());
                localSharedPreferences.saveSharedPreferences(Constants.RoomPrice, hostCalendarList.get(i).getRoomPrice());
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomPricea, hostCalendarList.get(i).getRoomPrice());



                localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, hostCalendarList.get(0).getRoomId());
                localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, null);


                calendar_list_title.setText(hostCalendarList.get(i).getRoomName());
                calendar_list_hometype.setText(hostCalendarList.get(i).getRoomType());

                if (Integer.parseInt(hostCalendarList.get(0).getRemainingSteps()) != 0) {
                    calendar_list_unlist.setText(getResources().getString(R.string.unlisted));
                } else {
                    calendar_list_unlist.setText(getResources().getString(R.string.listed));
                }

                Glide.with(getApplicationContext())

                        .load(hostCalendarList.get(0).getRoomThumbImages()[0])

                        .into(new DrawableImageViewTarget(calendar_list_image) {
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                            }
                        });


                localSharedPreferences.saveSharedPreferences(Constants.NightPrice, hostCalendarList.get(i).getNightlyPrice().toString());

                DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                hdates = new ArrayList<Date>();
                try {
                    for (int j = 0; j < hostCalendarList.get(i).getReservedDates().length; j++) {
                        hdates.add(df.parse(hostCalendarList.get(i).getReservedDates()[j]));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                bdates = new ArrayList<Date>();
                try {
                    for (int j = 0; j < hostCalendarList.get(i).getBlockedDates().length; j++) {
                        bdates.add(df.parse(hostCalendarList.get(i).getBlockedDates()[j]));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendar.init(new Date(), nextYear.getTime()) //
                        .inMode(HostCalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDates(hdates).withBlockedDates(bdates);
            }

        }

        calendaradapter = new CalendarListingdetailsAdapter(this, hostCalendarList);

        //calendaradapter.notifyDataChanged();
        calendar_dot_loader.setVisibility(View.GONE);
        emptycalendar.setVisibility(View.GONE);
        listcalendar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) mydialog.dismiss();
    }


    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    // show network error and exception
    public void snackBar() {
        // Create the Snackbar
        snackbar = Snackbar.make(calendar_list_image, "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                   // loadHostCalendar();

                    // new UpdateDateDetails().execute();
                } else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable) {
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        if (!snackbar.isShown()) {
            snackbar.show();
        }

    }

    public void onBackPressed() {
        if (backPressed >= 1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
            // clean up
            backPressed = backPressed + 1;
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = getEvents(newYear, newMonth);
        return events;
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.WEEK_OF_MONTH, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MONTH, newMonth);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 24);
        WeekViewEvent weekViewEvent = new WeekViewEvent();
        weekViewEvent.setName("");
        weekViewEvent.setStartTime(startTime);
        weekViewEvent.setEndTime(endTime);
        weekViewEvent.setColor(getResources().getColor(R.color.text_light_gray));
        HostHome.events.add(weekViewEvent);

        return HostHome.events;
    }
}