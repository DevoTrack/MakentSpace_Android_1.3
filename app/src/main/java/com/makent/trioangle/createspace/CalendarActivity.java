package com.makent.trioangle.createspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.ReadyHostModel.CalendarData;
import com.makent.trioangle.createspace.ReadyHostModel.ReadyToHost;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class CalendarActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener, ServiceListener {

   WeekView _weekview;

    EditText sample;
    LinearLayout _calendar_lay;
    ImageView calendar_dot_loader;
    LinearLayout hostcalendar_bottom;

    StringBuilder selected_dates;
    StringBuilder selected_dates_hour;
    StringBuilder selected_time_hour;
    SharedPreferences pref;

    public static Button savechanges, cancel;
    Animation slide_up;

    protected boolean isInternetAvailable;

    public @Inject
    CommonMethods commonMethods;

    public @Inject
    ApiService apiService;

    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout bottom_sheet;


    LocalSharedPreferences localSharedPreferences;

     ReadyToHost  readyToHost;
     CalendarData calendarData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_calendar);
        initialize();

       /* View bottom_sheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior =BottomSheetBehavior.from(bottom_sheet);*/

        localSharedPreferences = new LocalSharedPreferences(this);
        Intent x =getIntent();
        readyToHost = (ReadyToHost) x.getExtras().getSerializable(Constants.READYTOHOSTSTEP);



        isInternetAvailable = getNetworkState().isConnectingToInternet();

        _weekview = findViewById(R.id._weekView);


        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(calendar_dot_loader);
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1);

        calendar_dot_loader.setVisibility(View.GONE);
        //_calendar_lay.setVisibility(View.VISIBLE);
        //bottom_sheet.setVisibility(View.GONE);

        _weekview.setNumberOfVisibleDays(7);
        _weekview.setMonthChangeListener(this);
        _weekview.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        _weekview.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
        _weekview.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        _weekview.goToToday();
        _weekview.setShowDistinctPastFutureColor(true);
        setupDateTimeInterpreter(true);
        //loadCalendardetail();


       /* cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        _weekview.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
                                               @Override
                                               public void onEmptyViewClicked(Calendar time) {

                                                   View view = getLayoutInflater().inflate(R.layout.calendar_bottom_sheet, null);
                                                   BottomSheetDialog dialog = new BottomSheetDialog(getApplicationContext());
                                                   dialog.setContentView(view);
                                                   dialog.show();

                                                   final Button cancel = view.findViewById(R.id.hostcalendar_cancel);
                                                   cancel.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           dialog.dismiss();
                                                       }
                                                   });

                                               }


                                           });


        /*_weekview.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                //timed=time;
                boolean currenttime = false, avail = false;
                System.out.println("onclickempty" + time);

                *//*
                Got time and format it
                 *//*

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

          *//*
            Append in String builder
           *//*

                selected_dates_hour = new StringBuilder();
                selected_time_hour = new StringBuilder();
                selected_dates_hour.append(formattedDate);
                selected_time_hour.append(formattedtime);

                System.out.println("selected hour : " + selected_dates_hour);
                System.out.println("selected time : " + selected_time_hour);




                savechanges.setVisibility(View.VISIBLE);
                bottom_sheet.setVisibility(View.VISIBLE);


                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

*//*
                hostcalendar_bottom.setVisibility(View.VISIBLE);

                slide_up = AnimationUtils.loadAnimation(CalendarActivity.this,
                        R.anim.slide_up_bottom);
                hostcalendar_bottom.startAnimation(slide_up);

                hostcalendar_bottom.animate()
                        .translationY(0)
                        .setDuration(200);*//*
            }
        });*/

       /* bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        bottom_sheet.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottom_sheet.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });*/



    }

    private void initialize(){
        //_weekview = (WeekView)findViewById(R.id._weekView);
        sample = (EditText) findViewById(R.id.edt);
       // _calendar_lay =(LinearLayout)findViewById(R.id._calendar_lay);
        calendar_dot_loader = (ImageView)findViewById(R.id.calendar_dot_loader);
        hostcalendar_bottom =(LinearLayout)findViewById(R.id.hostcalendar_bottom);
        savechanges = (Button)findViewById(R.id.hostcalendar_save);
        cancel =(Button)findViewById(R.id.hostcalendar_cancel);
    }

    /*@Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : HostHome.events) {

            if (eventMatches(event, newYear, newMonth)) {
                //if (true) {
                // Log.v("events"+event.getStartTime(),""+event.getEndTime());
                matchedEvents.add(event);
            }
        }

        Calendar calendar1 = Calendar.getInstance();
        if (newMonth == (calendar1.get(Calendar.MONTH) + 1))
            _weekview.goToToday();
        // HostHome.events.add(weekViewEvent);
        return matchedEvents;
    }*/


   /* public WeekView getWeekView() {
        return _weekview;
    }*/


    /*Load the space calendar list:
    * */
    private void calendar_list(){
        if (isInternetAvailable) {
            loadHostCalendar();

        } else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, sample, sample, getResources(), this);
        }

    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    private void loadHostCalendar(){
        calendar_dot_loader.setVisibility(View.VISIBLE);
        apiService.getListingDetails(localSharedPreferences.getSharedPreferences(Constants.AccessToken),localSharedPreferences.getSharedPreferences(Constants.mSpaceId)).enqueue(new RequestCallback(this));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
   /* private void loadCalendardetail(){*/
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        final DateFormat dateFormat_years = new SimpleDateFormat("yyyyMMdd");
        int currentyear = Integer.parseInt((dateFormat_years.format(calendar.getTime())));

       for(int i = 0 ; i<readyToHost.getCalendarData().getAvailableTimes().size(); i++){

           String startDate = readyToHost.getCalendarData().getAvailableTimes().get(i).getStartDate();
           String endDate = readyToHost.getCalendarData().getAvailableTimes().get(i).getEndDate();
           String start_time = readyToHost.getCalendarData().getAvailableTimes().get(i).getStartTime();
           String end_time = readyToHost.getCalendarData().getAvailableTimes().get(i).getEndTime();
           String notes = readyToHost.getCalendarData().getAvailableTimes().get(i).getNotes();

           String[] endTime = end_time.split("\\:");
           String[] startTime = start_time.split("\\:");
           String[] start_date = startDate.split("-");
           String[] end_date = endDate.split("-");

          //
           //
           // System.out.println("Start Time value " +(Integer.valueOf(endTime[0]) - Integer.valueOf(startTime[0])) );

           Calendar startcal = Calendar.getInstance();
           startcal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
           startcal.add(Calendar.MINUTE,Integer.valueOf(startTime[1]));
           startcal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(start_date[2]));
           startcal.set(Calendar.MONTH, Integer.valueOf(start_date[1])-1);
           startcal.set(Calendar.YEAR, Integer.valueOf(start_date[0]));

           Calendar endcal = (Calendar) startcal.clone();
           endcal.set(Calendar.HOUR_OF_DAY,Integer.valueOf(endTime[0]));
           endcal.add(Calendar.MINUTE,59);
           //endTime.set(Calendar.MONTH, Integer.valueOf(words[1])-1);
           endcal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(start_date[2]));
           endcal.set(Calendar.MONTH, Integer.valueOf(start_date[1])-1);
           WeekViewEvent weekViewEvent = new WeekViewEvent();
           weekViewEvent.setName(notes);
           weekViewEvent.setStartTime(startcal);
           weekViewEvent.setEndTime(endcal);
           weekViewEvent.setColor(getResources().getColor(R.color.background));
           HostHome.events.add(weekViewEvent);


       }

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int yearval = pref.getInt("Year",-1);
       /* int newMonth = pref.getInt("MonthValue", -1);*/
        int weekval = 0;
        if(yearval == currentyear || yearval > currentyear) {
                if (month == newMonth) {
                    weekval = week + 1;
                }
                else{
                    weekval = 1;
                }
                /*for (int hour = weekval; hour <= 5; hour++) {
                   if(hour == 1){
                       for (int j = 0; j < readyToHost.getCalendarData().getNotAvailableDays().size(); j++) {
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.WEEK_OF_MONTH, 1);
                        startTime.set(Calendar.DAY_OF_WEEK, readyToHost.getCalendarData().getNotAvailableDays().get(j));
                        startTime.set(Calendar.MINUTE, 0);
                        startTime.set(Calendar.HOUR_OF_DAY, 0);
                        startTime.set(Calendar.MONTH,newMonth);
                        startTime.set(Calendar.YEAR,newYear);
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.add(Calendar.HOUR_OF_DAY, 24);
                        WeekViewEvent event = new WeekViewEvent();
                        if(hour == week && startTime.get(Calendar.MONTH) == month){
                            if(startTime.get(Calendar.DAY_OF_WEEK) == day){
                                event.setColor(getResources().getColor(R.color.text_light_gray));
                            }else {
                                event.setColor(getResources().getColor(R.color.text_light_blue));
                            }
                        }
                        else{
                            event.setColor(getResources().getColor(R.color.text_light_blue));
                        }
                        event.setStartTime(startTime);
                        event.setEndTime(endTime);
                        event.setName("");
                        //event.setColor(getResources().getColor(R.color.blue_selected));
                        HostHome.events.add(event);
                }
            }
                    if(hour == 2){
                        for (int j = 0; j < readyToHost.getCalendarData().getNotAvailableDays().size(); j++) {

                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.WEEK_OF_MONTH, 2);
                            startTime.set(Calendar.DAY_OF_WEEK, readyToHost.getCalendarData().getNotAvailableDays().get(j));
                            startTime.set(Calendar.MINUTE, 0);
                            startTime.set(Calendar.HOUR_OF_DAY, 0);
                            startTime.set(Calendar.MONTH,newMonth);
                            startTime.set(Calendar.YEAR,newYear);
                            Calendar endTime = (Calendar) startTime.clone();
                            endTime.add(Calendar.HOUR_OF_DAY, 24);
                            WeekViewEvent event = new WeekViewEvent();
                            if(hour == week && startTime.get(Calendar.MONTH) == month){
                                if(startTime.get(Calendar.DAY_OF_WEEK) == day){
                                    event.setColor(getResources().getColor(R.color.text_light_gray));
                                }else {
                                    event.setColor(getResources().getColor(R.color.text_light_blue));
                                }
                            }
                            else{
                                event.setColor(getResources().getColor(R.color.text_light_blue));
                            }
                            event.setStartTime(startTime);
                            event.setEndTime(endTime);
                            event.setName("");
                            //event.setColor(getResources().getColor(R.color.blue_selected));
                            HostHome.events.add(event);
                        }
                    }
                    if(hour == 3){
                        for (int j = 0; j < readyToHost.getCalendarData().getNotAvailableDays().size(); j++) {

                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.WEEK_OF_MONTH, 3);
                            startTime.set(Calendar.DAY_OF_WEEK, readyToHost.getCalendarData().getNotAvailableDays().get(j));
                            startTime.set(Calendar.MINUTE, 0);
                            startTime.set(Calendar.HOUR_OF_DAY, 0);
                            startTime.set(Calendar.MONTH,newMonth);
                            startTime.set(Calendar.YEAR,newYear);
                            Calendar endTime = (Calendar) startTime.clone();
                            endTime.add(Calendar.HOUR_OF_DAY, 24);
                            WeekViewEvent event = new WeekViewEvent();
                            if(hour == week && startTime.get(Calendar.MONTH) == month){
                                if(startTime.get(Calendar.DAY_OF_WEEK) == day){
                                    event.setColor(getResources().getColor(R.color.text_light_gray));
                                }else {
                                    event.setColor(getResources().getColor(R.color.text_light_blue));
                                }
                            }
                            else{
                                event.setColor(getResources().getColor(R.color.text_light_blue));
                            }
                            event.setStartTime(startTime);
                            event.setEndTime(endTime);
                            event.setName("");
                            //event.setColor(getResources().getColor(R.color.blue_selected));
                            HostHome.events.add(event);
                        }
                    }
                    if(hour == 4){
                        for (int j = 0; j < readyToHost.getCalendarData().getNotAvailableDays().size(); j++) {

                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.WEEK_OF_MONTH, 4);
                            startTime.set(Calendar.DAY_OF_WEEK, readyToHost.getCalendarData().getNotAvailableDays().get(j));
                            startTime.set(Calendar.MINUTE, 0);
                            startTime.set(Calendar.HOUR_OF_DAY, 0);
                            startTime.set(Calendar.MONTH,newMonth);
                            startTime.set(Calendar.YEAR,newYear);
                            Calendar endTime = (Calendar) startTime.clone();
                            endTime.add(Calendar.HOUR_OF_DAY, 24);
                            WeekViewEvent event = new WeekViewEvent();
                            if(hour == week && startTime.get(Calendar.MONTH) == month){
                                if(startTime.get(Calendar.DAY_OF_WEEK) == day){
                                    event.setColor(getResources().getColor(R.color.text_light_gray));
                                }else {
                                    event.setColor(getResources().getColor(R.color.text_light_blue));
                                }
                            }
                            else{
                                event.setColor(getResources().getColor(R.color.text_light_blue));
                            }
                            event.setStartTime(startTime);
                            event.setEndTime(endTime);
                            event.setName("");
                            //event.setColor(getResources().getColor(R.color.blue_selected));
                            HostHome.events.add(event);
                        }
                    }
                    if(hour == 5){
                        for (int j = 0; j < readyToHost.getCalendarData().getNotAvailableDays().size(); j++) {

                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.WEEK_OF_MONTH, 5);
                            startTime.set(Calendar.DAY_OF_WEEK, readyToHost.getCalendarData().getNotAvailableDays().get(j));
                            startTime.set(Calendar.MINUTE, 0);
                            startTime.set(Calendar.HOUR_OF_DAY, 0);
                            startTime.set(Calendar.MONTH,newMonth);
                            startTime.set(Calendar.YEAR,newYear);
                            Calendar endTime = (Calendar) startTime.clone();
                            endTime.add(Calendar.HOUR_OF_DAY, 24);
                            WeekViewEvent event = new WeekViewEvent();
                            if(hour == week && startTime.get(Calendar.MONTH) == month){
                                if(startTime.get(Calendar.DAY_OF_WEEK) == day){
                                    event.setColor(getResources().getColor(R.color.text_light_gray));
                                }else {
                                    event.setColor(getResources().getColor(R.color.text_light_blue));
                                }
                            }
                            else{
                                event.setColor(getResources().getColor(R.color.text_light_blue));
                            }
                            event.setStartTime(startTime);
                            event.setEndTime(endTime);
                            event.setName("");
                            //event.setColor(getResources().getColor(R.color.blue_selected));
                            HostHome.events.add(event);
                        }
                    }

            }*/
        } getWeekView().notifyDatasetChanged();

        for (WeekViewEvent event : HostHome.events) {

            if (eventMatches(event, newYear, newMonth)) {
                //if (true) {
                // Log.v("events"+event.getStartTime(),""+event.getEndTime());
                matchedEvents.add(event);
            }
        }
        Calendar calendar1 = Calendar.getInstance();
        if (newMonth == (calendar1.get(Calendar.MONTH) + 1))
            _weekview.goToToday();

        return matchedEvents;



    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }
    public WeekView getWeekView() {
        return _weekview;
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        _weekview.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());



                SimpleDateFormat year = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
                SimpleDateFormat month = new SimpleDateFormat("MM",Locale.getDefault());
                int yearvalue = Integer.parseInt(year.format(date.getTime()));
                int monthvalue = Integer.parseInt(month.format(date.getTime()));
                pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("Year", yearvalue);
                editor.putInt("MonthValue" , monthvalue);
                editor.commit();



                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }
}
