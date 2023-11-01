package com.makent.trioangle.profile;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile
 * @category    TermsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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

import static com.makent.trioangle.helper.Constants.LanguageCode;

/* ************************************************************
                   Terms and Oplicy Page
Show webview for terms and policyList
*************************************************************** */
public class TermsActivity extends AppCompatActivity implements View.OnClickListener{

    public @Inject
    CommonMethods commonMethods;
    WebView webView;
    Dialog_loading mydialog;
    RelativeLayout terms_title;
    String userid;
    ImageView terms_title_back;
    LocalSharedPreferences localSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        commonMethods = new CommonMethods();

        localSharedPreferences =new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        String langCode = localSharedPreferences.getSharedPreferences(LanguageCode);
        //Get webview
        webView = (WebView) findViewById(R.id.webView1);
        terms_title_back =(ImageView)findViewById(R.id.terms_title_back);
        commonMethods.rotateArrow(terms_title_back,this);
        System.out.println(getResources().getString(R.string.termslink)+"?token="+userid +"&languange=" + langCode);
        startWebView(getResources().getString(R.string.termslink)+"?token="+userid +"&languange=" + langCode + "&device=mobile");

        terms_title = (RelativeLayout) findViewById(R.id.terms_title);
        terms_title.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terms_title: {
                //onBackPressed();
                Intent x = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(x);
                finish();
            }
            break;
        }
    }

    // Start Web page
    private void startWebView(String url) {

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
                    mydialog = new Dialog_loading(TermsActivity.this);
                    mydialog.setCancelable(false);
                    mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mydialog.show();
                }
                /*if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ShowWebView.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }*/
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    /*if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
        webView.loadUrl(url); // Load URL for web page


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