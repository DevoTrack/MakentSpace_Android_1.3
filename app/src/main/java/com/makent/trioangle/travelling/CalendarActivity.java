package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestCalendarActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.spacebooking.model.GetAvailableTime;
import com.makent.trioangle.spacebooking.views.ChooseBookingTimingsActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.LogManager;
import com.makent.trioangle.util.RequestCallback;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.Logt;
import static com.makent.trioangle.helper.Constants.SavedDateAndTime;
import static com.makent.trioangle.helper.Constants.SpaceAvailableTime;
import static com.makent.trioangle.helper.Constants.SpaceBlockedDays;
import static com.makent.trioangle.helper.Constants.isFromBookingFlow;
import static com.makent.trioangle.util.Enums.REQUEST_CODE_ON_SPACE_BOOKING;
import static com.makent.trioangle.util.Enums.REQ_SPACE_AVAILABILITY;


public class CalendarActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    private static final String TAG = "CalendarActivity";
    private CalendarPickerView calendar;

    private CalendarPickerView.OnDateSelectedListener dateListener;
    String searchstartdate, searchenddate, checkinout;
    private String tempCheckinCheckout="";

    LocalSharedPreferences localSharedPreferences;
    final Calendar nextYear = Calendar.getInstance();
    final Calendar lastYear = Calendar.getInstance();
    Locale locale;
    public static TextView cal_chin, cal_chout;
    public EditText edt;
    String searchcheckin, searchcheckout, isCheckAvailability, spaceId, userid;
    String[] blocked_dates;
    Date[] Dates;
    protected boolean isInternetAvailable;

    Dialog_loading mydialog;
    private String type = "";
    public Button calendar_close;
    private String getPreferedLanguageCode;

    /*For makent space:
     * */
    public Spinner spn_start_time;
    public Spinner spn_end_time;
    public Date d1, d2;
    public String startTime, endTime;
    private boolean isSelectable;
    public static LinearLayout rltPickDrop;
    private boolean isFrombooking;
    private String instantBook;
    private boolean isTimingSetted;
    private Button saveDates;
    private ArrayList<Integer> blockedDays = new ArrayList<>();

    private GetAvailableTime getAvailableTime;
    private TimeZone tz = TimeZone.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        commonMethods = new CommonMethods();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        Constants.dialogshow = 0;
        localSharedPreferences = new LocalSharedPreferences(this);

        saveDates = findViewById(R.id.done_button);
        //For Makent Space:
        initialize();

        startTime = localSharedPreferences.getSharedPreferences(Constants.StartTime);
        endTime = localSharedPreferences.getSharedPreferences(Constants.EndTime);

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_date_item, getResources().getStringArray(R.array.hour));
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> Adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_date_item, getResources().getStringArray(R.array.endhour));
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_start_time.setAdapter(Adapter);
        spn_end_time.setAdapter(Adapter);
  //      spn_start_time.getBackground().setColorFilter(getResources().getColor(R.color.title_text_color), PorterDuff.Mode.SRC_ATOP);
   //     spn_end_time.getBackground().setColorFilter(getResources().getColor(R.color.title_text_color), PorterDuff.Mode.SRC_ATOP);
        rltPickDrop = findViewById(R.id.rltPickDrop);
        rltPickDrop.setVisibility(View.GONE);

        spn_start_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_start_time.getSelectedItem().toString().equals(getResources().getString(R.string.select))) {
                    LogManager.e("spinner 1" + spn_start_time.getSelectedItem().toString());
                    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.forLanguageTag(getPreferedLanguageCode));
                    Date date = null;
                    try {
                        date = parseFormat.parse(spn_start_time.getSelectedItem().toString());
                        d1 = parseFormat.parse(spn_start_time.getSelectedItem().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    LogManager.e(parseFormat.format(date) + " = " + displayFormat.format(date));
                    Log.d("#####--starttime-->",   new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(date));
                    //localSharedPreferences.saveSharedPreferences(Constants.StartTime, displayFormat.format(date));

                    startTime = displayFormat.format(date);
                    localSharedPreferences.saveSharedPreferences(Constants.StartTimeAMPM, spn_start_time.getSelectedItem().toString());
                    System.out.println("Start Time " + displayFormat.format(date));
                    searchcheckin = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
                    searchcheckout = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.StartTime, null);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spn_end_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spn_end_time.getSelectedItem().toString().equals(getResources().getString(R.string.select))) {
                    LogManager.e("spinner 2" + spn_end_time.getSelectedItem().toString());
                    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.forLanguageTag(getPreferedLanguageCode));
                    Date date = null;
                    try {
                        date = parseFormat.parse(spn_end_time.getSelectedItem().toString());
                        d2 = parseFormat.parse(spn_end_time.getSelectedItem().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    LogManager.e(parseFormat.format(date) + " = " + displayFormat.format(date));
                    localSharedPreferences.saveSharedPreferences(Constants.EndTime, displayFormat.format(date));
                    endTime = displayFormat.format(date);
                    localSharedPreferences.saveSharedPreferences(Constants.EndTimeAMPM, spn_end_time.getSelectedItem().toString());
                    System.out.println("End Time " + displayFormat.format(date));
                    searchcheckin = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
                    searchcheckout = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
                    if (localSharedPreferences.getSharedPreferences(Constants.CheckInOuttime) == null)
                        loadCalendar();
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.EndTime, null);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        isFrombooking = getIntent().getBooleanExtra(isFromBookingFlow, false);
        if (isFrombooking) {
            blockedDays = (ArrayList<Integer>) getIntent().getSerializableExtra(SpaceBlockedDays);
        }

        Log.e("Calender Activity", "Calender Activity");
    /*   if(localSharedPreferences.getSharedPreferences(Constants.LanguageCode).equalsIgnoreCase("ar"))
       {
           locale=new Locale("ar","AE");
       }
       else
       {
           locale=Locale.ENGLISH;
       }
*/
      /*  nextYear=Calendar.getInstance(TimeZone.getDefault(),locale);
       lastYear=Calendar.getInstance(TimeZone.getDefault(),locale);*/

        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);

        isCheckAvailability = localSharedPreferences.getSharedPreferences(Constants.isCheckAvailability);
        spaceId = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        getPreferedLanguageCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent x = getIntent();
        if (isCheckAvailability.equals("1")) {
            if (blocked_dates == null) {
                blocked_dates = x.getStringArrayExtra("blockdate");
            }
            if (searchstartdate != null && searchenddate != null) {
                searchcheckin = searchstartdate;
                searchcheckout = searchenddate;
            }
        }

        type = x.getStringExtra("type");

        cal_chin = findViewById(R.id.cal_chin);
        cal_chout = findViewById(R.id.cal_chout);

        if (type != null && type.equals("search"))
        {
            cal_chin.setText(getResources().getString(R.string.start_date));
            cal_chout.setText(getResources().getString(R.string.end_date));
        }

        nextYear.add(Calendar.YEAR, 1);
        lastYear.add(Calendar.YEAR, -1);

        calendar = findViewById(R.id.calendar_view);

        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE);

        Calendar cal = Calendar.getInstance();

        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter()
        {
            @Override
            public boolean isDateSelectable(Date date) {

                boolean isSelecteable = true;
                cal.setTime(date);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                for (int i = 0; i < blockedDays.size(); i++) {
                    if (dayOfWeek == blockedDays.get(i)) {
                        isSelecteable = false;
                    }
                }
                return isSelecteable;
            }
        });

        calendar.setCustomDayView(new DefaultDayViewAdapter());

        calendar.setDecorators(Collections.emptyList());

        loadCalendar(); // This function Chick in Check Out and start date end date the calender formade all are usd

        calendar.setFocusable(true);
        calendar_close = findViewById(R.id.calendar_close);

        commonMethods.setTvAlign(calendar_close, this);
        calendar_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Check avalability one " + isCheckAvailability);

                if (isCheckAvailability.equals("1")) {
                    System.out.println("Check avalability  " + isCheckAvailability);

                    if (localSharedPreferences.getSharedPreferences(Constants.CheckInOut) == null) {
                        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
                        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);
                        //For makent space:
                        localSharedPreferences.saveSharedPreferences(Constants.CheckInOuttime, null);
                    }
                }
//                Intent x = new Intent(getApplicationContext(), SpaceDetailActivity.class);
//                x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                startActivity(x, bndlanimation);
//                //startActivity(x);
//                finish();
               /* Constants.dialogshow=1;
                onBackPressed();*/
                //onBackPressed();
                spn_start_time.setSelection(0);
                spn_end_time.setSelection(0);
                finish();
            }
        });

        findViewById(R.id.calendar_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type != null && type.equals("search")) {
                    cal_chin.setText(getResources().getString(R.string.start_date));
                    cal_chout.setText(getResources().getString(R.string.end_date));
                }
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);

                searchcheckin = null;
                searchcheckout = null;
                loadCalendar();

                //For makent space:
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOuttime, null);
                rltPickDrop.setVisibility(View.GONE);
                startTime = null;
                endTime = null;
                spn_start_time.setSelection(0);
                spn_end_time.setSelection(0);

            }
        });

        saveDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn);
                searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut);
                checkinout = localSharedPreferences.getSharedPreferences(Constants.CheckInOut);
                //  Log.e("searstartdate",searchstartdate);
                System.out.println("IsCheck in out " + checkinout);
                isInternetAvailable = getNetworkState().isConnectingToInternet();

                if (isInternetAvailable) {
                    System.out.println("IsCheckAvailabilty " + isCheckAvailability);
                    if (isCheckAvailability.equals("1")) {
                        if (searchstartdate != null && searchenddate != null) {
                            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1");
                            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                            System.out.println("CHECK IN IsCheckAvailabilty " + checkinout);
                            //checkSelectedDate(); // This function called on CheckSelected api
                            /**
                             * when tempCheckinCheckout and checkinout are same no need to call api directly move to the next page
                             */
                            if (tempCheckinCheckout.equalsIgnoreCase(checkinout)){
                                Intent chooseTime = new Intent(CalendarActivity.this, ChooseBookingTimingsActivity.class);
                                chooseTime.putExtra(SpaceAvailableTime, getAvailableTime);
                                startActivityForResult(chooseTime, REQUEST_CODE_ON_SPACE_BOOKING);
                            }else {
                                CheckAvailability();
                            }

                        } else {
                            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
                            saveDatesAndSetAction();
                        }
                        //  saveDatesAndSetAction();
                    } else {
                        if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen) != null) {
                            if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen).equalsIgnoreCase("explore")) {
                                localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "yes");
                            } else {
                                localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "showAll");
                            }
                        }

                        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchstartdate);
                        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchenddate);
                        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout);
                        localSharedPreferences.saveSharedPreferences(Constants.StartTime,startTime);
                        localSharedPreferences.saveSharedPreferences(Constants.EndTime,endTime);

                        System.out.println("CHECK IN DATE " + checkinout);
                        if (searchstartdate != null && searchenddate != null) {
                            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1");
                        } else {
                            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
                        }
                        saveDatesAndSetAction();
                    }
                } else {
                    commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, cal_chin, getResources(), CalendarActivity.this);
                }
            }
        });

    }

    private String changeFormat(String inputDateStr)
    {
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try
        {
            date = inputFormat.parse(inputDateStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return outputFormat.format(date);
    }

    public void initialize()
    {
        spn_start_time = findViewById(R.id.spnStart);
        spn_end_time = findViewById(R.id.spnEnd);
    }

    public void loadCalendar()
    {
        System.out.print("searchinout" + searchcheckin + "//" + searchcheckout);
        if (searchcheckin != null && searchcheckout != null && blocked_dates == null)
        {
            System.out.print("step1" + "true");
            ArrayList<Date> dates = new ArrayList<Date>();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale(getPreferedLanguageCode));

            Date startDate = null, endDate = null;
            try {
                startDate = df.parse(searchcheckin);
                endDate = df.parse(searchcheckout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dates.add(startDate);
            dates.add(endDate);
            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
                    .withSelectedDates(dates);

        }
        else if (searchcheckin != null && searchcheckout != null && blocked_dates.length > 0)
        {
            System.out.print("step2" + "true");
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale(getPreferedLanguageCode));
            Date startDate = null, endDate = null;
            ArrayList<Date> hdates = new ArrayList<Date>();
            ArrayList<Date> dates = new ArrayList<Date>();
            try {
                for (String blocked_date : blocked_dates) {
                    System.out.print("hDatesstep2 " + blocked_date);
                    hdates.add(df.parse(blocked_date));
                }
                startDate = df.parse(searchcheckin);
                endDate = df.parse(searchcheckout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dates.add(startDate);
            dates.add(endDate);
            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDates(dates)
                    .withHighlightedDates(hdates);
        }
        else if (searchcheckin == null && searchcheckout == null && blocked_dates != null)
        {
            System.out.print("step3" + "true");
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale(getPreferedLanguageCode));
            ArrayList<Date> hdates = new ArrayList<Date>();
            try {
                for (String blocked_date : blocked_dates) {
                    System.out.println("hDatesstep3 " + blocked_date);
                    hdates.add(df.parse(blocked_date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withHighlightedDates(hdates);
        }
        else if (searchcheckin != null && searchcheckout != null)
        {
            System.out.print("step4" + "true");
            ArrayList<Date> dates = new ArrayList<Date>();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale(getPreferedLanguageCode));
            Date startDate = null, endDate = null;
            try {
                startDate = df.parse(searchcheckin);
                endDate = df.parse(searchcheckout);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dates.add(startDate);
            dates.add(endDate);
            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
                    .withSelectedDates(dates);
        }
        else
        {
            System.out.print("step5" + "true");
            calendar.init(new Date(), nextYear.getTime()) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE); //
        }

        if (null != startTime && !"".equals(startTime))
        {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            try {
                Date date = displayFormat.parse(startTime);
                startTime = parseFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String[] arr = getResources().getStringArray(R.array.hour);
            int pos = Arrays.asList(arr).indexOf(startTime);
            spn_start_time.setSelection(pos);
        }
        if (null != endTime && !"".equals(endTime))
        {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            try {
                Date date = displayFormat.parse(endTime);
                endTime = parseFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String[] arr = getResources().getStringArray(R.array.hour);
          //  String[] arr1 = getResources().getStringArray(R.array.endhour);
            int pos = Arrays.asList(arr).indexOf(endTime);
            LogManager.e("" + pos);
            spn_end_time.setSelection(pos);
        }
    }

    private void saveDatesAndSetAction()
    {
        Log.e("savedate","saveDateAndActions");
        if (isCheckAvailability.equals("0"))
        {
            Log.e("checkavailability","checkavailability"+isCheckAvailability);
            if (spn_start_time.getSelectedItemId() != 0|| spn_end_time.getSelectedItemId() != 0)
            {
                //if (spn_start_time.getSelectedItemId() == 0  || spn_end_time.getSelectedItemId() <= spn_start_time.getSelectedItemId()) {
                if (spn_start_time.getSelectedItemId() != 0 && spn_end_time.getSelectedItemId() == 0) {
                    Log.e("end time","end time zero");
                    commonMethods.snackBar(getResources().getString(R.string.select_end_time), "", false, 2, cal_chin, getResources(), CalendarActivity.this);
                }else if (spn_start_time.getSelectedItemId() == 0 && spn_end_time.getSelectedItemId() != 0) {
                    Log.e("start time","start time zero");
                    commonMethods.snackBar(getResources().getString(R.string.select_start_time), "", false, 2, cal_chin, getResources(), CalendarActivity.this);
                }else if(spn_end_time.getSelectedItemId() <= spn_start_time.getSelectedItemId()){
                    Log.e("start end time","start time greater");
                    commonMethods.snackBar(getResources().getString(R.string.end_time_greater_than_start_time), "", false, 2, cal_chin, getResources(), CalendarActivity.this);
                }
                else {
                    Log.e("conditions","all conditions true");
                    // This is used to call the CalendarActivity to  HomeActivity
                    localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied, true);
                    Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                    x.putExtra("filter", "filter");
                    //  x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(x, bndlanimation);
                    //startActivity(x);
                    finish();
                }
            }
            else
            {
                localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied, true);
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                x.putExtra("filter", "filter");
                //  x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                //startActivity(x);
                finish();
            }
        }
        else if (isCheckAvailability.equals("1"))
        {
            Log.e("checkavailability","checkavailability"+isCheckAvailability);
            finish();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (jsonResp.isSuccess()) {
            Log.e("success","success response");
            switch (jsonResp.getRequestCode()) {
                case REQ_SPACE_AVAILABILITY:
                    onSuccesAvailable(jsonResp);
                    break;
                default:
                    onSuccessCheck(jsonResp);// onSuccess call method
                    break;
            }
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            Log.e("failure","failure response"+jsonResp.getStatusMsg());
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, cal_chin, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data)
    {

    }

    public void onSuccesAvailable(JsonResponse jsonResponse)
    {
        Log.e("getAvailable","getAvailability success");
        tempCheckinCheckout = checkinout;
        getAvailableTime = gson.fromJson(jsonResponse.getStrResponse(), GetAvailableTime.class);
        Intent chooseTime = new Intent(this, ChooseBookingTimingsActivity.class);
        chooseTime.putExtra(SpaceAvailableTime, getAvailableTime);
        startActivityForResult(chooseTime, REQUEST_CODE_ON_SPACE_BOOKING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ON_SPACE_BOOKING && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(SavedDateAndTime, data.getSerializableExtra(SavedDateAndTime));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public void onSuccessCheck(JsonResponse jsonResp)
    {
        if (mydialog.isShowing())
        {
            mydialog.dismiss();
        }
        Integer roomPrice = (Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), "pernight_price", Integer.class);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, String.valueOf(roomPrice));
        saveDatesAndSetAction();
    }

    private void CheckAvailability()
    {
        if (!mydialog.isShowing())
        {
            mydialog.show();
        }
        apiService.getAvailabilityTimes(localSharedPreferences.getSharedPreferences(Constants.AccessToken), spaceId, changeFormat(searchstartdate), changeFormat(searchenddate), tz.getID()).enqueue(new RequestCallback(REQ_SPACE_AVAILABILITY, CalendarActivity.this));
    }

    private void checkSelectedDate()
    {
        if (!mydialog.isShowing())
        {
            mydialog.show();
        }
        String total_guest = localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        if (total_guest == null)
        {
            total_guest = localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
            if (total_guest == null)
                total_guest = "1";
        }
        //  Log.e("strat&End",searchstartdate+".."+searchenddate);
        apiService.checkavailability(localSharedPreferences.getSharedPreferences(Constants.AccessToken), spaceId, searchstartdate, searchenddate, total_guest).enqueue(new RequestCallback(this));
    }


    public ConnectionDetector getNetworkState()
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
