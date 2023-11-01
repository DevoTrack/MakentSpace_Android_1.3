package com.makent.trioangle.profile;

/**
 * @package com.makent.trioangle
 * @subpackage Profile
 * @category SettingActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.makent.trioangle.BaseActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.LanguageAdapter;
import com.makent.trioangle.adapter.host.CurrencyListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.BasicStepsActivity;

import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.host.optionaldetails.description.OD_LengthOfStay;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.language.LanguageConverter;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.LanguageModel;
import com.makent.trioangle.model.host.Makent_host_model;
import com.makent.trioangle.model.settings.CurrencyListModel;
import com.makent.trioangle.model.settings.CurrencyResult;
import com.makent.trioangle.model.settings.DeleteAccountModel;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.makent.trioangle.adapter.host.CalendarListingdetailsAdapter.lastCheckedPosition;
import static com.makent.trioangle.helper.Constants.LanguageCode;
import static com.makent.trioangle.helper.Constants.LanguageName;
import static com.makent.trioangle.helper.Constants.LanguageRecreate;
import static com.makent.trioangle.util.Enums.REQ_CURRENCYLIST;
import static com.makent.trioangle.util.Enums.REQ_DELETE_ACCOUNT;
import static com.makent.trioangle.util.Enums.REQ_LANGUAGE_CHANGE;
import static com.makent.trioangle.util.Enums.REQ_UPDATE_CURRENCY;


/* ************************************************************
                   Setting  Page
Show user basic setting option in profile page
*************************************************************** */

public class SettingActivity extends BaseActivity implements View.OnClickListener, ServiceListener, LanguageConverter.onSuccessLanguageChangelistner , CompoundButton.OnCheckedChangeListener{

    public static android.app.AlertDialog alertDialogStores1;
    public static android.app.AlertDialog languageAlertDialog;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public TextView currency_code, currency_symbol;
    public CurrencyResult currencyResult;
    public EditText edt;
    public ArrayList<CurrencyListModel> currencyList = new ArrayList<>();
    public ArrayList<LanguageModel> languagelist;
    public TextView language_txt;
    public ImageView payout_img, term_img, logout_img, settings_title_back;
    protected boolean isInternetAvailable;
    RelativeLayout rltSecureLock;
    SwitchCompat scSecurity;
    // ***Experience Start***

    // ***Experience End***
    RelativeLayout settings_title, settings_notification, settings_currency, settings_payout, settings_terms, settings_logout;
    SwitchCompat notification_switch;
    LocalSharedPreferences localSharedPreferences;
    RecyclerView recyclerView1;
    List<Makent_host_model> makent_host_modelList;
    CurrencyListAdapter currencyListAdapter;
    Dialog_loading mydialog;
    String[] symbol;//=new String[50];
    String[] currencycode;//=new String[50];
    String userid;
    String currency, currency_codes, default_currency, currency_symbols;
    Context context;
    String payout_count;
    String[] languageNameArray;
    String[] languageCodeArray;
    RelativeLayout settings_language;
    private RecyclerView rvLanguage;
    private LanguageAdapter languagelistAdapter;
    private String language;
    private LanguageConverter languageConverter;

    private Button delete_account;
    private AlertDialog dialog;
    DeleteAccountModel deleteAccountModel;
    boolean isConfirmed = false;

    boolean isPayout = false;
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
      //  AppController.getAppComponent().inject(this);
        // Basic network exception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        localSharedPreferences = new LocalSharedPreferences(this);

        Log.e("Setting Activity", "Setting Activity");

        context = this;
        delete_account = findViewById(R.id.btn_delete_account);
        dialog = commonMethods.getCloseAlertDialog(this);

        payout_img = findViewById(R.id.payout_img);
        term_img = findViewById(R.id.term_img);
        logout_img = findViewById(R.id.logout_img);
        settings_title_back = findViewById(R.id.settings_title_back);

        commonMethods.rotateArrow(payout_img, this);
        commonMethods.rotateArrow(term_img, this);
        commonMethods.rotateArrow(logout_img, this);
        commonMethods.rotateArrow(settings_title_back, this);


        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        currency = localSharedPreferences.getSharedPreferences(Constants.Currency);
        System.out.println("Currency in Profile page" + currency);

        currency_codes = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
        currency_symbols =localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);

        System.out.println("Currency in Profile page 1" + currency_codes);
        payout_count = localSharedPreferences.getSharedPreferences(Constants.PayoutCount);

        currency_code = findViewById(R.id.currency_code);

        settings_title = findViewById(R.id.settings_title);
        settings_notification = findViewById(R.id.settings_notification);
        settings_currency = findViewById(R.id.settings_currency);
        settings_payout = findViewById(R.id.settings_payout);
        settings_terms = findViewById(R.id.settings_terms);
        settings_logout = findViewById(R.id.settings_logout);
        currency_symbol = findViewById(R.id.currency_symbol);
        edt = findViewById(R.id.edt);
        language_txt = findViewById(R.id.language);
        notification_switch = findViewById(R.id.notification_switch);
        settings_language = findViewById(R.id.settings_language);
        settings_language.setOnClickListener(this);
        // settings_title.setOnClickListener(this);
        settings_title_back.setOnClickListener(this);
        settings_notification.setOnClickListener(this);
        settings_currency.setOnClickListener(this);
        settings_payout.setOnClickListener(this);
        settings_terms.setOnClickListener(this);
        settings_logout.setOnClickListener(this);
        delete_account.setOnClickListener(this);
        rltSecureLock = (RelativeLayout) findViewById(R.id.rlt_secure_lock);
        scSecurity = (SwitchCompat) findViewById(R.id.sc_security);

        if (BiometricManager.from(getApplicationContext()).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS)
        {
            rltSecureLock.setVisibility(View.GONE);
        }
        else
        {
            rltSecureLock.setVisibility(View.VISIBLE);
        }

//        if(getResources().getString(R.string.layout_direction).equals("1")){
//            currency_symbol.setGravity(Gravity.START);
//            currency_code.setGravity(Gravity.END);
//        }
//        else{
//            currency_symbol.setGravity(Gravity.END);
//            currency_code.setGravity(Gravity.START);
//        }


        language = localSharedPreferences.getSharedPreferences(LanguageName);
        System.out.println("Language " + localSharedPreferences.getSharedPreferences(LanguageName));
        // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
        if (language != null) {
            language_txt.setText(language);
        }


        if (currency_codes != null) {
            currency_code.setText(currency_codes);
            currency_symbol.setText(currency_symbols);
        } else {
//            default_currency =getResources().getString(R.string.usd);
//            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,default_currency);
            currency_code.setText(getResources().getString(R.string.usd));
            currency_symbol.setText(getResources().getString(R.string.symbol));

            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, currency_code.getText().toString());
        }
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //myDialog.show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        currency = localSharedPreferences.getSharedPreferences(Constants.Currency);
        currency_codes = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
        currency_symbols = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);

        if (BiometricManager.from(getApplicationContext()).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {

            scSecurity.setOnCheckedChangeListener (null);
            scSecurity.setEnabled(false);
            localSharedPreferences.saveSharedPreferences(Constants.SecurityCheck,false);
        }else{

            scSecurity.setEnabled(true);
            scSecurity.setOnCheckedChangeListener (this);
        }

        scSecurity.setChecked(localSharedPreferences.getSharedPreferencesBool(Constants.SecurityCheck));


        if (currency_codes != null) {
            currency_code.setText(currency_codes);
            currency_symbol.setText(currency_symbols);
        } else {
//            default_currency = getResources().getString(R.string.usd);
            currency_code.setText(getResources().getString(R.string.usd));
            currency_symbol.setText(getResources().getString(R.string.symbol));
            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, currency_code.getText().toString());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_title_back: {

               /* Intent x = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(x);
                finish();*/
                onBackPressed();
                finish();
            }
            break;
            case R.id.settings_notification: {
                if (notification_switch.isChecked()) {
                    notification_switch.setChecked(false);
                } else {
                    notification_switch.setChecked(true);
                }
            }
            break;
            case R.id.settings_currency: {
                settings_currency.setClickable(false);
                currency_list(); // Show curtency list
            }
            break;
            case R.id.settings_language: {
                String[] languages = getResources().getStringArray(R.array.languages);
                String[] langCode = getResources().getStringArray(R.array.languageCode);
                languageConverter = new LanguageConverter(SettingActivity.this, true, languages, langCode, SettingActivity.this);
            }
            break;
            case R.id.settings_payout: {
                // Call Payout email list page
                Intent x = new Intent(getApplicationContext(), PayoutEmailListActivity.class);
                startActivity(x);//PayoutBankDetailsActivity PayoutEmailListActivity
            }
            break;
            case R.id.settings_terms: {
                // Call Terms and policyList webview
                Intent x = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(x);
                finish();

                /*Intent x = new Intent(getApplicationContext(), BasicStepsActivity.class);
                startActivity(x);
                finish();*/
            }
            break;
            case R.id.settings_logout: {
                logout();// Logout dialog
            }
            break;

            case R.id.btn_delete_account: {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    callDeleteAccount(false);// check valid for Delete account
                } else {
                    snackBar();
                }
            }
            break;
            default:
                break;
        }
    }



    // Load currency list deatils in dialog
    public void currency_list() {

        recyclerView1 = new RecyclerView(SettingActivity.this);
        makent_host_modelList = new ArrayList<>();

        currencyListAdapter = new CurrencyListAdapter(this, currencyList);
        currencyListAdapter.setLoadMoreListener(new CurrencyListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView1.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = currencyList.size() - 1;
                    }
                });
            }
        });

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(currencyListAdapter);
        // loadcurrencylist(0);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if (currencyList.size() < 1)
                currencyList();
        } else {
            snackBar();
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.currency_header, null);
        alertDialogStores1 = new android.app.AlertDialog.Builder(SettingActivity.this)
                .setCustomTitle(view)
                .setView(recyclerView1)
                .setCancelable(true)
                .show();
        settings_currency.setClickable(true);

        alertDialogStores1.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

                currency = localSharedPreferences.getSharedPreferences(Constants.Currency);
                currency_codes = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
                currency_symbols = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);
                // Toast.makeText(getApplicationContext(),"Dismiss dialog "+currency_codes,Toast.LENGTH_SHORT).show();
                if (currency != null) {
                    currency_code.setText(currency_codes);
                    currency_symbol.setText(currency_symbols);
                } else {
                    currency_code.setText(getResources().getString(R.string.usd));
                    //default_currency = getResources().getString(R.string.usd);
                    currency_symbol.setText(getResources().getString(R.string.symbol));
                    localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, currency_code.getText().toString());
                }
                updateCurrency();
            }
        });


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
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                }
                break;
            case REQ_UPDATE_CURRENCY:
                if (jsonResp.isSuccess()) {
                    localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload");
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, edt, getResources(), this);
                }
                break;

            case REQ_LANGUAGE_CHANGE:
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                if (jsonResp.isSuccess()) {
                    redirection();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, edt, getResources(), this);
                }
                break;

            case REQ_DELETE_ACCOUNT:{
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this,dialog,jsonResp.getStatusMsg());
                } else{
                    onSuccessDeleteAccount(jsonResp.getStrResponse());
                }
            }
            break;
            default:
                break;

        }


    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        Constants.backpressed = 2;
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    // Get currency list from API
    public void currencyList() {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.currencyList(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_CURRENCYLIST, this));
    }

    public void onSuccessCurrency(JsonResponse jsonResp) {
        Constants.backpressed = 0;
        currencyResult = gson.fromJson(jsonResp.getStrResponse(), CurrencyResult.class);
        ArrayList<CurrencyListModel> currencyListModels = currencyResult.getCurrencyList();
        currencyList.clear();
        currencyList.addAll(currencyListModels);
        currencyListAdapter.notifyDataChanged();
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }

    // After Get currency to Update currency
    public void updateCurrency() {
        currency = localSharedPreferences.getSharedPreferences(Constants.Currency);
        currency_codes = localSharedPreferences.getSharedPreferences(Constants.CurrencyCode);
        currency_symbols = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);

        if (currency != null) {
            currency_code.setText(currency_codes);
            currency_symbol.setText(currency_symbols);
        } else {
            currency_code.setText(getResources().getString(R.string.usd));
            currency_symbol.setText(getResources().getString(R.string.symbol));
//            default_currency = getString(R.string.usd);
//
//            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,default_currency);

            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, currency_code.getText().toString());

            System.out.println("inside else condition ");
        }
        apiService.currencyChange(localSharedPreferences.getSharedPreferences(Constants.AccessToken), currency_codes).enqueue(new RequestCallback(REQ_UPDATE_CURRENCY, this));
    }

    // Logout dialog
    public void logout() {
        final Dialog dialog = new Dialog(SettingActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        // set the custom dialog components - text, ivPhoto and button

        OD_LengthOfStay.lenghtofstayDiscount = null;
        Button dialogButton = dialog.findViewById(R.id.logout_cancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogButton1 = dialog.findViewById(R.id.logout_ok);
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.backpressed = 0;
                String langugaeName = localSharedPreferences.getSharedPreferences(Constants.LanguageName);
                String languageCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);

                dialog.dismiss();  // To close dialog
                localSharedPreferences.clearSharedPreferences(); // Clear local saved data
                // ***Experience Start***

                // ***Experience End***
                clearApplicationData(); // Clear cache data

                localSharedPreferences.saveSharedPreferences(Constants.LanguageName, langugaeName);
                localSharedPreferences.saveSharedPreferences(Constants.LanguageCode, languageCode);

                if (languageCode != null && !languageCode.equals("")) {
                    setLocaleLogout(languageCode);
                } else {
                    setLocaleLogout("en");
                }
                System.out.println("checking local " + localSharedPreferences.getSharedPreferences(Constants.LengthOfStayOptions));
            }
        });

        dialog.show();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");

                    // clearApplicationData();
                }
            }
        }
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    // show network error and exception
    public void snackBar() {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(settings_title, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable) {
            //textViewTop.setText(statusmessage);
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


    public void setLocaleLogout(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


       /* if (!myDialog.isShowing()) {
            myDialog.show();
        }*/


        //redirection();
        //String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.languageChange(lang, localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_LANGUAGE_CHANGE, this));


        localSharedPreferences.saveSharedPreferences(LanguageRecreate, true);
    }

    private void redirection() {
        Intent x = new Intent(getApplicationContext(), HomeActivity.class);
        overridePendingTransition(0, 0);
        x.putExtra("tabsaved", 0);
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(x);
        finish();


    }

    @Override
    public void onSuccess(String language, String languageCode) {
        Constants.backpressed = 0;
        if (language != null)
        {
            setLocale(languageCode);
        }
        else
        {
            language_txt.setText(getResources().getString(R.string.default_language));
        }
        clearSavedData();
    }

    private void clearSavedData() {

        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null);

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null);

        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0");
        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckIsFilterApplied, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckAvailableScreen, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);


        localSharedPreferences.saveSharedPreferences(Constants.GuestLastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestFirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Schedule_id, null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedExpPrice, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExpId, null);
        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestImage, null);

        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType_key, null);
        localSharedPreferences.saveSharedPreferences(Constants.HomePage, "Home");
        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
        localSharedPreferences.saveSharedPreferences(Constants.HomeShowAll, 0);
        System.out.println("Saved Data cleared  : ");

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        localSharedPreferences.saveSharedPreferences(Constants.SecurityCheck,isChecked);
    }


    /** Delete Account Functions Starts */
    private void callDeleteAccount(Boolean confirmed) {
        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        isConfirmed = confirmed;
        apiService.deleteAccount(localSharedPreferences.getSharedPreferences(Constants.AccessToken), (isConfirmed)?"yes": "").enqueue(new RequestCallback(REQ_DELETE_ACCOUNT, this));
    }

    public void onSuccessDeleteAccount(String jsonResp){
        deleteAccountModel = gson.fromJson(jsonResp, DeleteAccountModel.class);
        if (deleteAccountModel.getStatusCode().equals("1")){
            if (isConfirmed){
                isConfirmed = false;
                Toast.makeText(context, getString(R.string.delete_success), Toast.LENGTH_LONG).show();
                clearAll();
                redirection();
            }else{
                deleteAccount();//delete account dialog
            }
        }else{
            commonMethods.showMessage(this,dialog,deleteAccountModel.getStatusMessage());
        }
    }

    // Delete Account dialog
    public void deleteAccount() {
        final Dialog dialog = new Dialog(SettingActivity.this,R.style.MyAlertTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.white_background_latest));
        // set the custom dialog components - text, image and button
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.logout_msg);
        if (isPayout) {
            dialogTitle.setText(getString(R.string.payout_refund_msg));
        }else{
            dialogTitle.setText(getString(R.string.delete_account_msg));
        }

        OD_LengthOfStay.lenghtofstayDiscount = null;
        Button dialogButton = (Button) dialog.findViewById(R.id.logout_cancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPayout = false;
                dialog.dismiss();
            }
        });

        Button dialogButton1 = (Button) dialog.findViewById(R.id.logout_ok);
        if (isPayout) {
            dialogButton1.setText(getString(R.string.delete));
        }else{
            dialogButton1.setText(getString(R.string.confirm));
        }



        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    if(deleteAccountModel.isPayout) {
                        isPayout = true;
                        deleteAccountModel.setisPayout(false);
                        deleteAccount();
                    }else{
                        //Toast.makeText(getApplicationContext(),"account deleted",Toast.LENGTH_SHORT).show();
                        callDeleteAccount(true);
                        isPayout = false;//delete account
                    }
                    dialog.dismiss();  // To close dialog
                } else {
                    snackBar();
                }
            }
        });

        dialog.show();
    }

    private void clearAll() {
        Constants.backpressed = 0;
        String langugaeName = localSharedPreferences.getSharedPreferences(Constants.LanguageName);
        String languageCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        String appleClientId = localSharedPreferences.getSharedPreferences(Constants.AppleServiceId);
        localSharedPreferences.clearSharedPreferences(); // Clear local saved data
        // ***Experience Start***
      //  complexPreferences = ComplexPreferences.getComplexPreferences(context, "mypref", MODE_PRIVATE);
      //  complexPreferences.clearSharedPreferences();
        // ***Experience End***
        clearApplicationData(); // Clear cache data
       // clearNotifications(SettingActivity.this);

        localSharedPreferences.saveSharedPreferences(Constants.LanguageName, langugaeName);
        localSharedPreferences.saveSharedPreferences(Constants.LanguageCode, languageCode);
        localSharedPreferences.saveSharedPreferences(Constants.AppleServiceId, appleClientId);
        Constants.languageCode = localSharedPreferences.getSharedPreferences(LanguageCode);
        if (languageCode != null && !languageCode.equals("")) {
            setLocaleLogout(languageCode);
        } else {
            setLocaleLogout("en");
        }
        System.out.println("checking local " + localSharedPreferences.getSharedPreferences(Constants.LengthOfStayOptions));
    }

}
