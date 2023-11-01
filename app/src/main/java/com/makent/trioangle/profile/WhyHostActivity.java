package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    WhyHostActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ************************************************************
                   WhyHost Page
Show static content for User known Why Host
*************************************************************** */
public class WhyHostActivity extends AppCompatActivity implements View.OnClickListener{

    public @Inject
    CommonMethods commonMethods;
   LocalSharedPreferences localSharedPreferences;
    RelativeLayout whyhost_title;
    ImageView whyhost_title_back;
    String userid;
    Dialog_loading mydialog;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_why_host);




        commonMethods = new CommonMethods();

        localSharedPreferences =new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        //Get webview
        webView = (WebView) findViewById(R.id.webView1);

        Log.e("HelpActivity","HelpActivity");

        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        Log.e("userid",userid);
        System.out.println(getResources().getString(R.string.whyhostlink)+"?token="+userid + "&language=" + langCode);
        startWebView(getResources().getString(R.string.whyhostlink)+"?token="+userid + "&language=" + langCode + "&device=mobile");



        whyhost_title = (RelativeLayout) findViewById(R.id.whyhost_title);
        whyhost_title_back =(ImageView)findViewById(R.id.whyhost_title_back);
        commonMethods.rotateArrow(whyhost_title_back,this);
        whyhost_title_back.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whyhost_title_back: {
                onBackPressed();
            }
            break;
        }
    }




    // Call web view
    private void startWebView(String url) {

        System.out.println("Help URl "+url);
        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            // ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

                if (mydialog == null) {
                    mydialog = new Dialog_loading(WhyHostActivity.this);
                    mydialog.setCancelable(false);
                    mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mydialog.show();
                }

            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }

                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        //Load url in webview
        webView.loadUrl(url); // Load URL for web view
    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}
