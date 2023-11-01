package com.makent.trioangle.host.optionaldetails;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by trioangle on 17/5/18.
 */

public class AddMinMax extends AppCompatActivity implements View.OnClickListener,ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    TextView startdate, enddate;
    DatePickerDialog datePickerDialog;
    String from,start_date_formatted="",during,end_date_formatted="",type = "month", stdate="",endate="",id,min_stay, max_stay, roomid, userid, dobss, rulesoption;
    Spinner myspinner1;
    LocalSharedPreferences localSharedPreferences;
    ArrayList<String> arrayList = new ArrayList<String>();
    public ArrayList<Makent_model> rules = new ArrayList<>();
    RelativeLayout Addminmaxhead,spinnerlayout;
    LinearLayout Star_date, End_date,min_lay,max_lay;
    EditText maximumstay, minimumstay;
    int pos;
    Dialog_loading mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_minmax);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        rulesoption = localSharedPreferences.getSharedPreferences(Constants.AvailableRulesOption);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        Intent x=getIntent();
        from=x.getStringExtra("from");

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        startdate = (TextView) findViewById(R.id.startdate);
        enddate = (TextView) findViewById(R.id.enddate);
        myspinner1 = (Spinner) findViewById(R.id.myspinner1);
        Addminmaxhead = (RelativeLayout) findViewById(R.id.Addminmaxhead);
        spinnerlayout = (RelativeLayout) findViewById(R.id.spinnerlayout);
        Star_date = (LinearLayout) findViewById(R.id.Star_date);
        End_date = (LinearLayout) findViewById(R.id.End_date);
        min_lay = (LinearLayout) findViewById(R.id.min_lay);
        max_lay = (LinearLayout) findViewById(R.id.max_lay);
        minimumstay = (EditText) findViewById(R.id.minimumstay);
        maximumstay = (EditText) findViewById(R.id.maximumstay);
        Calendar cal = Calendar.getInstance();
        String daySt=""+cal.get(Calendar.DAY_OF_MONTH);
        if (cal.get(Calendar.DAY_OF_MONTH)<10)
            daySt="0"+cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        String monthSt=""+(cal.get(Calendar.MONTH)+1);
        if (month<10)
            monthSt="0"+month;
        startdate.setText(daySt+"-"+monthSt+"-"+cal.get(Calendar.YEAR));
        enddate.setText(daySt+"-"+monthSt+"-"+cal.get(Calendar.YEAR));
        startdate.setOnClickListener(this);
        enddate.setOnClickListener(this);
        Addminmaxhead.setOnClickListener(this);
        min_lay.setOnClickListener(this);
        max_lay.setOnClickListener(this);
        spinnerlayout.setOnClickListener(this);
        myspinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closekeyboard();
                return false;
            }
        });

        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            minimumstay.setGravity(Gravity.END);
            minimumstay.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        loaddata();
        Star_date.setVisibility(View.GONE);
        End_date.setVisibility(View.GONE);

        if(from.equals("edit")){
            stdate=x.getStringExtra("start_date");
            endate=x.getStringExtra("end_date");
            max_stay=x.getStringExtra("maximum_stay");
            min_stay=x.getStringExtra("minimum_stay");
            id=x.getStringExtra("id");
            type=x.getStringExtra("type");
            start_date_formatted=x.getStringExtra("start_date_formatted");
            end_date_formatted=x.getStringExtra("end_date_formatted");
            during=x.getStringExtra("during");
            pos=x.getIntExtra("postion",0);
            Log.v("during","during"+during);
            loadeditdata();
        }

        myspinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                closekeyboard();
                if (arrayList.get(position).equals(getResources().getString(R.string.custom))) {
                    type = "custom";
                    Star_date.setVisibility(View.VISIBLE);
                    End_date.setVisibility(View.VISIBLE);
                } else {
                    type = "month";
                    for (int i=0;i<rules.size();i++){
                        System.out.println("Rues Startdate" + rules.get(i).getStartDate());
                    }
                    stdate=rules.get(position).getStartDate();
                    endate=rules.get(position).getEndDate();
                    System.out.println("rulesposition="+position+"+getStartDate" + rules.get(position).getStartDate());
                    System.out.println("rules+getStartDate" + stdate);
                    System.out.println("rules+getEndDaterules="+rules.size()+"end" + endate);
                    Star_date.setVisibility(View.GONE);
                    End_date.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                closekeyboard();
            }
        });
    }

    public void setDate(String date) {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(AddMinMax.this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String daySt=""+dayOfMonth;
                        if (dayOfMonth<10)
                            daySt="0"+dayOfMonth;
                        int month= monthOfYear+1;
                        String monthSt=""+month;
                        if (month<10)
                            monthSt="0"+month;


                        if(date.equals("start")){
                            /*startdate.setText((monthOfYear + 1) + "-"
                                    + dayOfMonth + "-" + year);*/
                            stdate = daySt + "-" + monthSt + "-" + year;
                            startdate.setText(stdate);
                            if(from.equals("edit")){
                                start_date_formatted=stdate;
                            }
                            Log.v("stdate","stdate"+stdate);
                        }else{
                            /*enddate.setText((monthOfYear + 1) + "-"
                                    + dayOfMonth + "-" + year);*/
                            endate = daySt + "-" + monthSt + "-" + year;
                            if(from.equals("edit")){
                                end_date_formatted=endate;
                            }
                            enddate.setText(endate);
                            Log.v("endate","endate"+endate);
                        }


                    }
                }, mYear - 3, mMonth, mDay);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.MONTH, mMonth);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        datePickerDialog.setTitle(getResources().getString(R.string.customdates));
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancelc), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.okay), datePickerDialog);
        datePickerDialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startdate:
                setDate("start");
                break;
            case R.id.enddate:
                setDate("end");
                break;
            case R.id.Addminmaxhead:
                min_stay = minimumstay.getText().toString();
                max_stay = maximumstay.getText().toString();
                if((stdate.equals("")||endate.equals(""))&&(start_date_formatted.equals("")||end_date_formatted.equals(""))){
                    Toast.makeText(AddMinMax.this, getResources().getString(R.string.select_date_field), Toast.LENGTH_LONG).show();
                }else{
                    updateMinMaxForDays();
                }
                break;
            case R.id.min_lay:
                if(maximumstay.isFocused()){
                    maximumstay.clearFocus();
                    closekeyboard();
                    minimumstay.requestFocus();
                    openkeyboard(maximumstay);
                }else
                minimumstay.requestFocus();
                openkeyboard(minimumstay);
                break;
            case R.id.max_lay:
                if(minimumstay.isFocused()){
                    minimumstay.clearFocus();
                    closekeyboard();
                    maximumstay.requestFocus();
                    openkeyboard(maximumstay);
                }else
                maximumstay.requestFocus();
                openkeyboard(minimumstay);
                break;
                default:
                    break;
        }
    }

    public void loaddata() {
        try {

            JSONArray jsonArray = new JSONArray(rulesoption);
            Log.v("rulesoption","rulesoption="+rulesoption);
            arrayList.add(getResources().getString(R.string.select_dates));
            Makent_model listdata = new Makent_model();

            listdata.setStartDate("");
            listdata.setEndDate("");
            listdata.setText(getResources().getString(R.string.select_dates));
            rules.add(listdata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject length_jsonobj = jsonArray.getJSONObject(i);
                String text, maximum_stay, start_date, end_date, during, discount, id, type;
                text = length_jsonobj.getString("text");
                start_date = length_jsonobj.getString("start_date");
                end_date = length_jsonobj.getString("end_date");


                listdata = new Makent_model();
                listdata.setStartDate(start_date);
                listdata.setEndDate(end_date);
                listdata.setText(text);
                rules.add(listdata);
                arrayList.add(text);
            }

            arrayList.add(getResources().getString(R.string.custom));
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Makent_model listdata = new Makent_model();
        listdata.setStartDate("");
        listdata.setEndDate("");
        listdata.setText("");
        rules.add(listdata);
        if (arrayList != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, arrayList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            myspinner1.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (jsonResp.isSuccess()) {
            try{
                JSONObject response = new JSONObject(jsonResp.getStrResponse());
                JSONArray arrJson = response.getJSONArray("availability_rules");
                System.out.println("responseAvailability="+arrJson);
                localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, arrJson.toString());
                finish();
            }catch (JSONException j){
                j.printStackTrace();
            }

        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,maximumstay,maximumstay,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,maximumstay,maximumstay,getResources(),this);
    }

    public void updateMinMaxForDays(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        if(from.equals("edit")){
            if(type.equals("custom")){
                apiService.updateAvailabilityRule(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid,type, min_stay, max_stay,start_date_formatted,end_date_formatted,id).enqueue(new RequestCallback(this));
                //url = "update_availability_rule?room_id=" + roomid + "&type=" + type + "&minimum_stay=" + min_stay + "&maximum_stay=" + max_stay +"&maximum_stay=" + max_stay+ "&start_date="+start_date_formatted+"&end_date="+end_date_formatted+"&id="+id+"&token=" + userid;
            }else {
                apiService.updateAvailabilityRule(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid,type, min_stay, max_stay,stdate,endate,id).enqueue(new RequestCallback(this));
               // url = "update_availability_rule?room_id=" + roomid + "&type=" + type + "&minimum_stay=" + min_stay + "&maximum_stay=" + max_stay +"&maximum_stay=" + max_stay+ "&start_date="+stdate+"&end_date="+endate+"&id="+id+"&token=" + userid;
            }
        }else{
            apiService.updateAvailabilityRule(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid,type, min_stay, max_stay,stdate,endate,"").enqueue(new RequestCallback(this));
            //url = "update_availability_rule?room_id=" + roomid + "&type=" + type + "&minimum_stay=" + min_stay + "&maximum_stay=" + max_stay +"&maximum_stay=" + max_stay+ "&start_date="+stdate+"&end_date="+endate+"&token=" + userid;
        }

    }

    public void loadeditdata() {
        minimumstay.setText(min_stay);
        maximumstay.setText(max_stay);
        if (type.equals("custom")){
            startdate.setText(start_date_formatted);
            enddate.setText(end_date_formatted);
        }
            Makent_model listdata = new Makent_model();
            listdata.setStartDate(start_date_formatted);
            listdata.setEndDate(end_date_formatted);
            listdata.setText(during);
            rules.remove(0);
            arrayList.remove(0);
            rules.add(0,listdata);
            arrayList.add(0,during);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, arrayList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            myspinner1.setAdapter(dataAdapter);

            Log.v("getCount="+dataAdapter.getCount(),"arr"+arrayList);
           // myspinner1.notify();
            myspinner1.setSelection(0);
       // }
    }

    void openkeyboard(EditText edit){
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                edit.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }
    void closekeyboard(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}
