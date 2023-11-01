package com.makent.trioangle.host.optionaldetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.AvailabilityAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.roomModels.RoomAvailabilityRules;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by trioangle on 16/5/18.
 */

public class ReservationSettings extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public RecyclerView listView;
    public LocalSharedPreferences localSharedPreferences;
    public AvailabilityAdapter availabilityAdapter;
    public String minmax = "";
    public String roomid;
    public String userid;
    public String min_stay;
    public String max_stay;
    public TextView addreserv;
    public EditText maximumstay;
    public EditText minimumstay;
    public RelativeLayout reservationheader;
    public RelativeLayout addreservelay;
    public Dialog_loading mydialog;
    public LinearLayout min_lay, max_lay;
    public ArrayList<RoomAvailabilityRules> roomAvailabilityRules = new ArrayList<>();
    public ImageView bottom_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_settings);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        localSharedPreferences = new LocalSharedPreferences(this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        commonMethods =new CommonMethods();

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        try {
            if (localSharedPreferences.getSharedPreferences(Constants.ReserveSettings).toString() != null) {
                minmax = localSharedPreferences.getSharedPreferences(Constants.ReserveSettings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        min_stay = localSharedPreferences.getSharedPreferences(Constants.MinimumStay);
        max_stay = localSharedPreferences.getSharedPreferences(Constants.MaximumStay);

        System.out.println("Minstay 1 " + min_stay + " " + localSharedPreferences.getSharedPreferences(Constants.MinimumStay));
        System.out.println("Maxstay 1 " + max_stay + " " + localSharedPreferences.getSharedPreferences(Constants.MaximumStay));


        listView = (RecyclerView) findViewById(R.id.reservedsett);
        addreserv = (TextView) findViewById(R.id.addreserv);
        minimumstay = (EditText) findViewById(R.id.minimumstay);
        maximumstay = (EditText) findViewById(R.id.maximumstay);
        reservationheader = (RelativeLayout) findViewById(R.id.reservationheader);
        addreservelay = (RelativeLayout) findViewById(R.id.reservation_settings);
        min_lay = (LinearLayout) findViewById(R.id.min_lay);
        max_lay = (LinearLayout) findViewById(R.id.max_lay);
        bottom_arrow =(ImageView)findViewById(R.id.bottom_arrow);
        commonMethods.rotateArrow(bottom_arrow,this);

        addreserv.setOnClickListener(this);
        addreservelay.setOnClickListener(this);
        reservationheader.setOnClickListener(this);
        min_lay.setOnClickListener(this);
        max_lay.setOnClickListener(this);
        if (!minmax.isEmpty()) {
            loaddata();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (localSharedPreferences.getSharedPreferences(Constants.ReserveSettings).toString() != null) {
                minmax = localSharedPreferences.getSharedPreferences(Constants.ReserveSettings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!minmax.isEmpty()) {
            loaddata();
        }

        if (!min_stay.isEmpty() && !max_stay.isEmpty()) {
            minimumstay.setText(min_stay);
            maximumstay.setText(max_stay);
        }

    }

    public void loaddata() {
        try {
            minimumstay.setText(min_stay);
            maximumstay.setText(max_stay);
            JSONArray jsonArray = new JSONArray(minmax);
            roomAvailabilityRules.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject length_jsonobj = jsonArray.getJSONObject(i);

                String minimum_stay;
                String maximum_stay;
                String start_date;
                String end_date;
                String during;
                String id;
                String type;
                String Start_date_formatted;
                String End_date_formatted;

                id = length_jsonobj.getString("id");
                type = length_jsonobj.getString("type");
                minimum_stay = length_jsonobj.getString("minimum_stay");
                maximum_stay = length_jsonobj.getString("maximum_stay");
                start_date = length_jsonobj.getString("start_date");
                end_date = length_jsonobj.getString("end_date");
                during = length_jsonobj.getString("during");
                Start_date_formatted = length_jsonobj.getString("start_date_formatted");
                End_date_formatted = length_jsonobj.getString("end_date_formatted");
                Log.v("start_date", "start_date" + start_date);
                roomAvailabilityRules.add(new RoomAvailabilityRules(id, roomid, type, minimum_stay, maximum_stay, start_date, end_date, during, Start_date_formatted, End_date_formatted));

            }
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (roomAvailabilityRules != null) {
            availabilityAdapter = new AvailabilityAdapter(this, roomAvailabilityRules, "setting");
            listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            listView.setAdapter(availabilityAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reservation_settings:
                /*Intent r = new Intent(getApplicationContext(), AddMinMax.class);
                r.putExtra("from","add");
                startActivity(r);*/
            case R.id.addreserv:
                Intent i = new Intent(getApplicationContext(), AddMinMax.class);
                i.putExtra("from", "add");
                startActivity(i);
                break;
            case R.id.reservationheader:
                min_stay = minimumstay.getText().toString();
                max_stay = maximumstay.getText().toString();
                closekeyboard();
                updateMinMax();
                break;
            case R.id.min_lay:
                if (maximumstay.isFocused()) {
                    maximumstay.clearFocus();
                    closekeyboard();
                    minimumstay.requestFocus();
                    openkeyboard(maximumstay);
                } else
                    minimumstay.requestFocus();
                openkeyboard(minimumstay);
                break;
            case R.id.max_lay:
                if (minimumstay.isFocused()) {
                    minimumstay.clearFocus();
                    closekeyboard();
                    maximumstay.requestFocus();
                    openkeyboard(maximumstay);
                } else {
                    maximumstay.requestFocus();
                    openkeyboard(maximumstay);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (jsonResp.isSuccess()) {
            localSharedPreferences.saveSharedPreferences(Constants.MinimumStay, min_stay);
            localSharedPreferences.saveSharedPreferences(Constants.MaximumStay, max_stay);
            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, maximumstay, maximumstay, getResources(), this);
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    // Update longterm prices and other prices
    public void updateMinMax() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.updateMinimumMaximumStay(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid, min_stay, max_stay).enqueue(new RequestCallback(this));
    }


    void openkeyboard(EditText edit) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.toggleSoftInputFromWindow(
                edit.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    void closekeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
