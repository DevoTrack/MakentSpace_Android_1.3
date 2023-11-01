package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestSearchGuestBedsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is Guest Page Contain to add the Guest Count
**************************************************************************  */
public class Search_Guest_Bed extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    Button guestbed_close,guestbed_save;
    ImageView guestplus,guestminus;
    TextView guesttext;
    int guestcount=1;
    int maxguest,maxguestcount=99999;
    String guestcounts,guest,maxguests,guestcounttype;
    LocalSharedPreferences localSharedPreferences;
    Typeface font1;
    Drawable minusenable,minusdisable,plusenable,plusdisable;
    EditText edtGuestCount;

    String[] blocked_dates;
    String hostuserimage,hostusername,roomtypeusername;

    Dialog_loading mydialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_guest_bed);
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        guestbed_save = (Button) findViewById(R.id.guestbed_save);
        edtGuestCount = findViewById(R.id.edtGuestCount);

        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        guestcounttype=Constants.type;
        Log.e("search values","earc"  +    search+"  "+guestcounttype);

        if (search!=null && !search.equals("null"))
        {
            if(search.equals("1")){
                guestbed_save.setText(getResources().getString(R.string.see_result));
            }
            else{
                guestbed_save.setText(getResources().getString(R.string.save));
            }
        }
        else{
            guestbed_save.setText(getResources().getString(R.string.see_result));
        }

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        font1 = Typeface.createFromAsset( getAssets(), getResources().getString(R.string.fonts_makent4));
        minusenable = new FontIconDrawable(Search_Guest_Bed.this,getResources().getString(R.string.f4checkminus),font1)
                .sizeDp(30).colorRes(R.color.guestButton);
        plusenable = new FontIconDrawable(Search_Guest_Bed.this,getResources().getString(R.string.f4checkplus),font1)
                .sizeDp(30).colorRes(R.color.guestButton);
        minusdisable = new FontIconDrawable(Search_Guest_Bed.this,getResources().getString(R.string.f4checkminus),font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable);
        plusdisable = new FontIconDrawable(Search_Guest_Bed.this,getResources().getString(R.string.f4checkplus),font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable);



        localSharedPreferences=new LocalSharedPreferences(this);
        guestcounts= localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        String roomGuest=localSharedPreferences.getSharedPreferences(Constants.RoomGuest);
        if(roomGuest!=null)
        {
            guestcounts=roomGuest;
        }

        Intent x=getIntent();// this function to used on get the string another activity
        guest=x.getStringExtra("guest");
        maxguests=x.getStringExtra("guestcounts");
        blocked_dates=x.getStringArrayExtra("blockdate");
        hostuserimage=x.getStringExtra("hostuserimage");
        hostusername=x.getStringExtra("hostusername");
        roomtypeusername=x.getStringExtra("roomtypeusername");

        if(maxguests!=null)
        {
            maxguest=Integer.parseInt(maxguests);
            if(maxguest>maxguestcount)
            {
                maxguest=maxguestcount;
            }
        }

        guestbed_close = (Button) findViewById(R.id.guestbed_close);
        commonMethods.setTvAlign(guestbed_close,this);


        guestplus = (ImageView) findViewById(R.id.guestplus);
        guestminus = (ImageView) findViewById(R.id.guestminus);
        guestminus.setEnabled(false);

        guesttext = (TextView) findViewById(R.id.guesttext);
        if(guestcounts!=null)
        {
            guestcount=Integer.parseInt(guestcounts);
            enablebuttons();  //
        }


        // On Click function used to click action for check Email id in server send link to Email
        guestbed_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
//                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
//                x.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                startActivity(x, bndlanimation);
//                //startActivity(x);
//                finish();
                onBackPressed();

            }
        });

        guestbed_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if(getIntent().hasExtra("guest")&&getIntent().getStringExtra("guest").equals("guest")) {
                    prePayment();
                }else{
                    if(getIntent().hasExtra("guest"))
                        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, Integer.toString(guestcount));
                    else
                    localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, Integer.toString(guestcount));
                    if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen)!=null) {
                        if (localSharedPreferences.getSharedPreferences(Constants.CheckAvailableScreen).equalsIgnoreCase("explore")) {
                            localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "yes");
                        } else {
                            localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, "showAll");
                        }
                    }
                    onBackPressed();
                }

            }
        });

        edtGuestCount.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
        edtGuestCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    guestcount = Integer.parseInt(s.toString());
                    edtGuestCount.setSelection(edtGuestCount.getText().length());
                    if (guestcount == 1) {
                        guesttext.setText(getResources().getString(R.string.guest));
                        guestminus.setEnabled(false);
                        guestplus.setEnabled(true);
                        plusMinus(guestminus,guestplus);
                    } else if(guestcount==maxguestcount) {
                        guestplus.setEnabled(false);
                        guestminus.setEnabled(true);
                        plusMinus(guestminus,guestplus);
                    }else {
                        guestminus.setEnabled(true);
                        guestplus.setEnabled(true);
                        plusMinus(guestminus,guestplus);
                    }
                }else {
                    guestcount=1;
                    guestminus.setEnabled(false);
                    guestplus.setEnabled(true);
                    plusMinus(guestminus,guestplus);
                }
            }
        });

        guestminus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (guestcount >= 2) {
                    guestcount--;
                }
                enablebuttons();
            }
        });
        guestplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                guestcount++;
                System.out.println("Bed Count" + guestcount);

                enablebuttons();
            }
        });
        enablebuttons();
    }
    public void enablebuttons() // this function to used on guest count enable and disable function
    {
        Log.e("GuestCount","GuestCount"+guestcounts + "Max Count"+maxguests);
        Log.e("guestcount ",String.valueOf(guestcount));

        if(guestcount==1)
        {
            guesttext.setText(getResources().getString(R.string.guest));
            guestminus.setEnabled(false);
            guestplus.setEnabled(true);
            edtGuestCount.setText(String.valueOf(guestcount));
        }else if(guestcount==maxguestcount)
        {
            guestminus.setEnabled(true);
            guestplus.setEnabled(false);
            guesttext.setText(getResources().getString(R.string.guests));
            edtGuestCount.setText(String.valueOf(guestcount));
        }
        else
        {
            guestplus.setEnabled(true);
            guesttext.setText(getResources().getString(R.string.guests));
            edtGuestCount.setText(String.valueOf(guestcount));
            guestminus.setEnabled(true);
        }

        plusMinus(guestminus,guestplus);
    }

    @Override
    public void onBackPressed() {
        if(guest==null) {
            localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,true);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);// this is used to one activity to another activity
            x.putExtra("blockdate",blocked_dates);
            x.putExtra("hostuserimage",hostuserimage);
            x.putExtra("roomtypeusername",roomtypeusername);
            x.putExtra("hostusername",hostusername);
            x.putExtra("filter", "filter");
            x.putExtra("guestcounts",maxguests);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            //startActivity(x);
            finish();
        }else if (guest.equals("Contacthostback"))
        {

            Intent x = new Intent(getApplicationContext(), ContactHostActivity.class);
            x.putExtra("blockdate",blocked_dates);
            x.putExtra("hostuserimage",hostuserimage);
            x.putExtra("roomtypeusername",roomtypeusername);
            x.putExtra("hostusername",hostusername);
            x.putExtra("guestcounts",maxguests);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }else {
            finish();
            super.onBackPressed();
        }

    }

    public void plusMinus(ImageView view,ImageView view1)
    {
        if(view.isEnabled()) {
            view.setBackground(minusenable);
        }else
        {
            view.setBackground(minusdisable);
        }

        if(view1.isEnabled()) {
            view1.setBackground(plusenable);
        }else
        {
            view1.setBackground(plusdisable);
        }
    }

    /**
     * @Reference Get amenities list from server
     */
    public void prePayment(){
        if(!mydialog.isShowing())
            mydialog.show();
        String searchguest=Integer.toString(guestcount);
        String searchcheckin=localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        String searchcheckout=localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        String checkin_detail = localSharedPreferences.getSharedPreferences(Constants.CheckInDetail);
        String checkout_detail = localSharedPreferences.getSharedPreferences(Constants.CheckOutDetail);
        String userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        String roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        if(searchguest==null)
        {
            searchguest="1";
        }

        String searchstartdate=localSharedPreferences.getSharedPreferences(Constants.CheckIn);
        String searchenddate=localSharedPreferences.getSharedPreferences(Constants.CheckOut);
        if((searchstartdate!=null&&searchenddate!=null)&&(!searchstartdate.equals("")&&!searchenddate.equals(""))&&(!searchstartdate.equals("null")&&!searchenddate.equals("null")))
        {
            searchcheckin=searchstartdate;
            searchcheckout= searchenddate;
        }
        apiService.prePayment(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,searchcheckin,searchcheckout,searchguest).enqueue(new RequestCallback(this));
    }

    public void getRequestResult(JsonResponse jsonResponse){
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if(getIntent().hasExtra("guest"))
                localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, Integer.toString(guestcount));
            else
                localSharedPreferences.saveSharedPreferences(Constants.SearchGuest,Integer.toString(guestcount));
            onBackPressed();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class InputFilterMinMax implements InputFilter {

        private int min;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int c) {
            return c>=1;
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if(mydialog.isShowing())
            mydialog.dismiss();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, guestbed_save, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
            getRequestResult(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            String statusMessage=jsonResp.getStatusMsg();
            //Toast.makeText(getApplicationContext(),statusMessage,Toast.LENGTH_LONG).show();

            if(statusMessage.contains("Available guests count"))
            {
                String msg[]=statusMessage.split(" ");
                if(getIntent().hasExtra("guest"))
                    localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, Integer.toString(guestcount));
                else
                    localSharedPreferences.saveSharedPreferences(Constants.SearchGuest,msg[msg.length-1]);
                guestcount=Integer.parseInt(msg[msg.length-1]);
                enablebuttons();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, guestbed_save, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, guestbed_save, getResources(), this);
    }
}