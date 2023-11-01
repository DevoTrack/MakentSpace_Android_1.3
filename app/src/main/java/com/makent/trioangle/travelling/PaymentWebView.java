package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestPaymentWebViewActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/* ***********************************************************************
This is PaymentWebView Page Contain Payment Webview details
**************************************************************************  */
public class PaymentWebView extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    RelativeLayout help_title;
    Dialog_loading mydialog;
    String weburl;
    LocalSharedPreferences localSharedPreferences;
    String userid;
    private static final String TAG = "DebugWebChromeClient";

    private boolean isRedirected;
    public ImageView help_title_back;
    public @Inject
    CommonMethods commonMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);
        commonMethods = new CommonMethods();
        localSharedPreferences =new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        mydialog = new Dialog_loading(PaymentWebView.this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent x = getIntent();
        weburl = x.getStringExtra("weburl");
        System.out.println("webURl" + weburl);

        //Get webview
        webView = (WebView) findViewById(R.id.webView1);
        help_title_back =(ImageView)findViewById(R.id.help_title_back);
        commonMethods.rotateArrow(help_title_back,this);
        startWebView(weburl);

        help_title = (RelativeLayout) findViewById(R.id.help_title);
        help_title.setOnClickListener(this);
        help_title_back.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_title: {
               // onBackPressed();
            }
            break;
            case R.id.help_title_back: {
                 onBackPressed();
            }
            break;
        }
    }

    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {

            // ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                System.out.println("Webview url "+url);
//
//                if(url.contains("cancel?s_key=")||url.contains("success?s_key=")||url.contains("pre_accept")) {
//                    webView.setVisibility(View.INVISIBLE);
//                }else{
//                    //Load url in webview
//                    webView.setVisibility(View.VISIBLE);
//                }
//
//                view.loadUrl(url);
//                isRedirected = true;
//                return false;
//            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isRedirected = true;
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isRedirected = false;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (!isRedirected) {
                    if (mydialog == null) {
                        mydialog = new Dialog_loading(PaymentWebView.this);
                        mydialog.setCancelable(false);
                        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        if (!mydialog.isShowing()&&!(PaymentWebView.this).isFinishing()) {
                            mydialog.show();
                        }
                    }else{
                        if (!mydialog.isShowing()&&!(PaymentWebView.this).isFinishing())  {
                            mydialog.show();
                        }
                    }

                }
            }

            public void onPageFinished(WebView view, String url) {

                if(url.contains("cancel?s_key=")||url.contains("success?s_key=")||url.contains("pre_accept")) {
                    webView.setVisibility(View.INVISIBLE);
                }else{
                    //Load url in webview
                    webView.setVisibility(View.VISIBLE);
                }
                    webView.loadUrl("javascript:android.showHTML(document.getElementById('json').innerHTML);");

                try {
                    isRedirected=true;
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "android");



        webView.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int progress) {
                try {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage m) {
                Log.d(TAG, m.lineNumber() + ": " + m.message() + " - " + m.sourceId());

                return true;
            }
        });

        if(url.contains("cancel?s_key=")||url.contains("success?s_key=")||url.contains("pre_accept")) {
            System.out.println("finish load Webview url 1  "+url);
            webView.setVisibility(View.INVISIBLE);
        }else{
            System.out.println("finload Webview url "+url);
            //Load url in webview
            webView.setVisibility(View.VISIBLE);
        }
        webView.loadUrl(url);

    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            try {
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            System.out.println("HTML" + html);
            JSONObject response = null;
            try {

                response = new JSONObject(html);

                if (response != null) {
                    String statuscode = response.getString("status_code");
                    String statusmessage = response.getString("success_message");

                    // remove_status= remove_jsonobj.getString("status");
                    Log.d("OUTPUT IS", statuscode);
                    Log.d("OUTPUT IS", statusmessage);


                    if (statuscode.matches("1")) {

                        //Toast.makeText(getApplicationContext(),statusmessage, Toast.LENGTH_SHORT).show();
                        clearSavedData();

                        if (statusmessage.matches("Request Booking Send to Host"))
                        {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.request_book_to_host), Toast.LENGTH_SHORT).show();
                            Intent x = new Intent(getApplicationContext(),HomeActivity.class);
                            x.putExtra("tabsaved",5);
                            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(x);
                            finish();
                        }if (statusmessage.matches("Payment Successfully Paid"))
                        {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.payment_successfull_paid), Toast.LENGTH_SHORT).show();
                            Intent x = new Intent(getApplicationContext(),HomeActivity.class);
                            x.putExtra("tabsaved",5);
                            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(x);
                            finish();
                        }


                        if (statusmessage.matches("Payment has Successfully Completed.")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_completed), Toast.LENGTH_SHORT).show();
                            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                            x.putExtra("tabsaved", 5);
                            x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(x);
                            finish();
                        }

                    }else
                    {
                        Toast.makeText(getApplicationContext(),statusmessage, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    Log.d("My App", response.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
            }
        }
    }

    public void clearSavedData()
    {

        localSharedPreferences.saveSharedPreferences(Constants.stepHouserules,0);
        localSharedPreferences.saveSharedPreferences(Constants.stepPayment,0);
        localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage,0);

        localSharedPreferences.saveSharedPreferences(Constants.HouseRules,null);

        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest,null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation,null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut,null);

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut,null);
        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage,null);

        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck,"0");

        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck,null);
    }
}
