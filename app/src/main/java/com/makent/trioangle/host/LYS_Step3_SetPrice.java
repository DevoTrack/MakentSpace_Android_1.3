package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step3_SetPriceActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.CurrencyListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.Makent_host_model;
import com.makent.trioangle.model.settings.CurrencyListModel;
import com.makent.trioangle.model.settings.CurrencyResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_CURRENCYLIST;
import static com.makent.trioangle.util.Enums.REQ_UPDATE_CURRENCY;
import static com.makent.trioangle.util.Enums.REQ_UPDATE_PRICE;

/* ***********************************************************************
This is List Your Space Step3  Contain SetPrice
**************************************************************************  */
public class LYS_Step3_SetPrice extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout setprice_title,price_title;
    TextView setprice_txt,setprice_msg;
    Button changecurrency;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid,roomprice,price,roomcurrencysymbol,roomcurrencycode;
    EditText price_edittext;
    ImageView price_dot_loader;
    TextView setprice_symbol;
    protected boolean isInternetAvailable;


    RecyclerView recyclerView1;
    List<Makent_host_model> makent_host_modelList;
    CurrencyListAdapter currencyListAdapter;
    public static android.app.AlertDialog alertDialogStores3;
    public CurrencyResult currencyResult;
    public ArrayList <CurrencyListModel> currencyList = new ArrayList<>();
    boolean host=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_step3_set_price);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        changecurrency=(Button) findViewById(R.id.changecurrency);
        price_dot_loader = (ImageView) findViewById(R.id.price_dot_loader);
        price_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(price_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        Intent x=getIntent();// this is used to get the string to another activity
        String hosts = x.getStringExtra("host");



        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomprice=localSharedPreferences.getSharedPreferences(Constants.ListRoomPrice);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
        System.out.println("roomcurrencysymbol"+localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol));
        System.out.println("currencysymbol"+localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol));
        if(hosts.equals("host"))
        {
            host=true;
            roomprice=localSharedPreferences.getSharedPreferences(Constants.ListRoomPricea);
            changecurrency.setVisibility(View.INVISIBLE);
        }

        price_edittext=(EditText)findViewById(R.id.price_edittext);



        if(roomprice!=null)//||!roomprice.equals("")) {
        {
            price_edittext.setText(roomprice);
        }

        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            price_edittext.setGravity(Gravity.END);
            price_edittext.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setprice_symbol=(TextView)findViewById(R.id.setprice_symbol);
        setprice_txt=(TextView) findViewById(R.id.setprice_txt);
        if(roomcurrencysymbol!=null){
            String cs= Html.fromHtml(roomcurrencysymbol).toString();
            setprice_symbol.setText(cs);


            String colorText=getResources().getString(R.string.price_tip)
                    +"<font color=\"#72bdb7\"><bold>"
                    +cs
                    + getResources().getString(R.string.price_tip1)
                    + "</bold></font>"
                    + getResources().getString(R.string.price_tip2);
            setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);

        }

        setprice_msg=(TextView) findViewById(R.id.setprice_msg);
        String colorText1=getResources().getString(R.string.price_msg)
               /* +"<font color=\"#FF5A5F\"><bold>"
                + getResources().getString(R.string.price_msg1)
                + "</bold></font>"*/;
        setprice_msg.setText(Html.fromHtml(colorText1), TextView.BufferType.SPANNABLE);

        price_title=(RelativeLayout) findViewById(R.id.price_title);
        price_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {

                updateprices();// this is used to update the prices dedails
            }
        });

        setprice_title=(RelativeLayout) findViewById(R.id.setprice_title);
        setprice_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                updateprices();
            }
        });


        changecurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                currency_list();// this is used to get all currency details list
            }
        });
    }

    public  void updateprices()
    {
        price=price_edittext.getText().toString();
        price= price.replaceAll("^\\s+|\\s+$", "");
        isInternetAvailable = getNetworkState().isConnectingToInternet();



            if (!host) {
                if (roomprice == null || roomprice.equals("") || roomprice.equals(price) || price.equals("")) {
                    if (!price.equals("")) {
                        updatePrice();// this is used to call on updateprice api.
                    } else {
                       // localSharedPreferences.saveSharedPreferences(Constants.ListRoomPricea, price);

                        //   onBackPressed();
                        finish();
                    }
                } else {
                    updatePrice();
                }
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomPricea, price);
               // onBackPressed();
                finish();
            }

}

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        updateprices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
        if(roomcurrencysymbol!=null) {
            String cs= Html.fromHtml(roomcurrencysymbol).toString();
            setprice_symbol.setText(cs);
            String colorText = getResources().getString(R.string.price_tip)
                    + "<font color=\"#72bdb7\"><bold>"
                    +roomcurrencysymbol
                    + getResources().getString(R.string.price_tip1)
                    + "</bold></font>"
                    + getResources().getString(R.string.price_tip2);
            setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
        }else {
            setprice_symbol.setText("$");
            String colorText=getResources().getString(R.string.price_tip)
                    +"<font color=\"#72bdb7\"><bold>"
                    +"$"
                    + getResources().getString(R.string.price_tip1)
                    + "</bold></font>"
                    + getResources().getString(R.string.price_tip2);
            setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_CURRENCYLIST:
                if (jsonResp.isSuccess()) {
                    onSuccessCurrency(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                }
                break;
            case REQ_UPDATE_CURRENCY:
                if (jsonResp.isSuccess()) {
                    onSuccessUpdate(jsonResp); //Room and Additional Prices changed to Minimun Price
                    commonMethods.snackBar(getResources().getString(R.string.additional_price),"",false,2,price_edittext,price_edittext,getResources(),this);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,price_edittext,price_edittext,getResources(),this);
                }
                break;
            case REQ_UPDATE_PRICE:
                if (jsonResp.isSuccess()) {
                    price_title.setVisibility(View.VISIBLE);
                    price_dot_loader.setVisibility(View.GONE);
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomPrice,price);
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    price_title.setVisibility(View.VISIBLE);
                    price_dot_loader.setVisibility(View.GONE);
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,price_edittext,price_edittext,getResources(),this);
                }
                break;
            default:
                break;

        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            price_title.setVisibility(View.VISIBLE);
            price_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, price_edittext, price_edittext, getResources(), this);
        }

    }

    // Get currency list from API
    public void currencyList(){
        apiService.currencyList(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_CURRENCYLIST,this));
    }

    public void onSuccessCurrency(JsonResponse jsonResp){
        currencyResult =gson.fromJson(jsonResp.getStrResponse(), CurrencyResult.class);
        ArrayList <CurrencyListModel> currencyListModels = currencyResult.getCurrencyList();
        currencyList.addAll(currencyListModels);
        currencyListAdapter.notifyDataChanged();
    }

    public void onSuccessUpdate(JsonResponse jsonResp){
        int roomprice=(Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), "room_price", Integer.class); //response.getString("room_price")
        price_edittext.setText(""+roomprice);
    }

    // After Get currency to Update currency
    public void updateCurrency(){
        roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
        roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
        // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
        if(roomcurrencysymbol!=null)
        {
            setprice_symbol.setText(roomcurrencysymbol);
            String colorText = getResources().getString(R.string.price_tip)
                    + "<font color=\"#72bdb7\"><bold>"
                    +roomcurrencysymbol
                    + getResources().getString(R.string.price_tip1)
                    + "</bold></font>"
                    + getResources().getString(R.string.price_tip2);
            setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
        }else {
            setprice_symbol.setText("$");
            String colorText = getResources().getString(R.string.price_tip)
                    + "<font color=\"#72bdb7\"><bold>"
                    +"$"
                    + getResources().getString(R.string.price_tip1)
                    + "</bold></font>"
                    + getResources().getString(R.string.price_tip2);
            setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
        }
        apiService.updateRoomCurrency(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomcurrencycode,roomid).enqueue(new RequestCallback(REQ_UPDATE_CURRENCY,this));
    }

    // Get currency list from API
    public void updatePrice(){
        price_title.setVisibility(View.GONE);
        price_dot_loader.setVisibility(View.VISIBLE);
        apiService.addRoomsprice(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,price).enqueue(new RequestCallback(REQ_UPDATE_PRICE,this));
    }

    public void currency_list()
    {

        recyclerView1 = new RecyclerView(this);
        makent_host_modelList = new ArrayList<>();

        currencyListAdapter = new CurrencyListAdapter(this, currencyList);
        currencyListAdapter.setLoadMoreListener(new CurrencyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView1.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = makent_host_modelList.size() - 1;
                    }
                });
            }
        });

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(currencyListAdapter);

        // loadcurrencylist(0);
        if (currencyList.size()<1)
        currencyList(); // this is used to search all the currency
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.currency_header, null);
        alertDialogStores3 = new android.app.AlertDialog.Builder(this)
                .setCustomTitle(view)
                .setView(recyclerView1)
                .setCancelable(true)
                .show();

        alertDialogStores3.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

                roomcurrencysymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol);
                roomcurrencycode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode);
                // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
                if(roomcurrencysymbol!=null)
                {
                    setprice_symbol.setText(roomcurrencysymbol);
                    String colorText = getResources().getString(R.string.price_tip)
                            + "<font color=\"#72bdb7\"><bold>"
                            +roomcurrencysymbol
                            + getResources().getString(R.string.price_tip1)
                            + "</bold></font>"
                            + getResources().getString(R.string.price_tip2);
                    setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
                }else {
                    setprice_symbol.setText("$");
                    String colorText = getResources().getString(R.string.price_tip)
                            + "<font color=\"#72bdb7\"><bold>"
                            +"$"
                            + getResources().getString(R.string.price_tip1)
                            + "</bold></font>"
                            + getResources().getString(R.string.price_tip2);
                    setprice_txt.setText(Html.fromHtml(colorText), TextView.BufferType.SPANNABLE);
                }
                updateCurrency();

            }
        });
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }



}
