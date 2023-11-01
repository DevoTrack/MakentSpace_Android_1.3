package com.makent.trioangle;
/**
 * @package com.makent.trioangle
 * @subpackage -
 * @category Home Page
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;



// ***Experience Start***



// ***Experience End***
import com.makent.trioangle.model.ExploreExpModel;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.host.HostHome;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.language.LanguageConverter;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.newhome.map.ShowAllMapActivity;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.newhome.views.SplashActivity;
import com.makent.trioangle.signin.LoginActivity;
import com.makent.trioangle.signup.Signup_DobActivity;
import com.makent.trioangle.signup.Signup_FacebookDetails;
import com.makent.trioangle.signup.Signup_NameActivity;
import com.makent.trioangle.spacebooking.views.SpaceAvailableActivity;
import com.makent.trioangle.spacedetail.model.SpaceResult;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.ContactHostActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.MapActivity;
import com.makent.trioangle.travelling.RequestActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleCallback;
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration;
import com.willowtreeapps.signinwithapplebutton.view.BaseUrl;
import com.willowtreeapps.signinwithapplebutton.view.SignInWithAppleButton;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.LanguageRecreate;
import static com.makent.trioangle.helper.Constants.isContactHost;
import static com.makent.trioangle.util.Enums.REQ_COMMON_DATA;
import static com.makent.trioangle.util.Enums.REQ_FBSIGNUP;
import static com.makent.trioangle.util.Enums.REQ_GPSIGNUP;

/* ************************************************************
                    Main Activity
 Main Activity is the launch page its contain Facebook, google plus, create account details
*************************************************************** */

public class MainActivity extends AppCompatActivity implements ServiceListener, LanguageConverter.onSuccessLanguageChangelistner
//implements GoogleApiClient.OnConnectionFailedListener
{

// Close button to close the app
// Login button to link to login page
// facebook button to login or signup using facebook
// google button to login or signup using google
// create account button to link signup page ( Name page)

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    RunTimePermission runTimePermission;
    public @Inject
    CustomDialog customDialog;

    private SignInWithAppleButton btnapplelogin;

    private int backPressed = 0;    // used by onBackPressed()

    RelativeLayout /*home_facebook,*/ home_google;

    @BindView(R.id.home_facebook)
    RelativeLayout home_facebook;

    Button home_login, home_createaccount;
    Button home_close;
    EditText edt;
    TextView policy; // Show privacy policyList
    LocalSharedPreferences localSharedPreferences;
    AlertDialog alert;
    //Facebook Declaration
    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    Bundle parameters;
    String[] blockedDates;

    String fbEmail, fbFullName, fbFirstName, fbLastName, fbUserProfile, fbID, fbToken, ip,dob;

    // Google API Client Declearation
    TimeZone tz = TimeZone.getDefault();

    //Google plus varibles
    private static final int RC_SIGN_IN = 0;

    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient googleApiClient;

    int count = 1;
    protected boolean isInternetAvailable;

    String googleEmail, googleFullName, googleUserProfile, googleID, googleIDToken;

    Dialog_loading mydialog;
    String userid;
    private int mBack = 0;

    private String appleClientId;

    public static android.app.AlertDialog languagemainAlertDialog;

    RelativeLayout rltLanguage;
    TextView tvChange;
    String[] languageNameArray;
    String[] languageCodeArray;

    private LanguageConverter languageConverter;
    private ImageView ivNext;
    private TextView tv_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);  // Layout design Name
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        edt = (EditText) findViewById(R.id.edt);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions();
        }*/

        languageNameArray = getResources().getStringArray(R.array.languages);
        languageCodeArray = getResources().getStringArray(R.array.languageCode);

        mBack = getIntent().getIntExtra("isback", 0);
        blockedDates=getIntent().getStringArrayExtra("blockdate");
        System.out.println("blockedDates "+blockedDates);

        btnapplelogin = findViewById(R.id.btnapplelogin);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Assign Design Id to Java variable
        home_close = (Button) findViewById(R.id.home_close);
        tv_change = (TextView)findViewById(R.id.tv_change);
        ivNext = findViewById(R.id.iv_next);
        home_login = (Button) findViewById(R.id.home_login);
       // home_facebook = (RelativeLayout) findViewById(R.id.home_facebook);
        home_google = (RelativeLayout) findViewById(R.id.home_google);
        home_createaccount = (Button) findViewById(R.id.createaccount);
        rltLanguage = findViewById(R.id.rlt_language);
        tvChange = findViewById(R.id.tv_change);
        policy = (TextView) findViewById(R.id.policy);
        // policyList.setText(Html.fromHtml(getResources().getString(R.string.policyList)));

        if (localSharedPreferences.getSharedPreferences(Constants.LanguageCode)!= null)
        {
            tvChange.setText(localSharedPreferences.getSharedPreferences(Constants.LanguageName));
        }
        if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            ivNext.setRotation(180);
        }
        else
        {
            ivNext.setRotation(0);
        }
        commonMethods.setTvAlign(home_close,this);
        customTextView(policy);
        if (isInternetAvailable)
        {
            new GetVersionCode().execute();
        }
        else
        {
            createNetErrorDialog(); // Check Network Error Dialog function
        }
        if (isInternetAvailable)
        {
            reqCommonData();
        }
        else
        {
            createNetErrorDialog(); // Check Network Error Dialog function
        }
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null)
        {
            if (localSharedPreferences.getSharedPreferencesInt(Constants.isHost) == 0)
            {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
                finish();
            }
            else
            {
                Intent x = new Intent(getApplicationContext(), HostHome.class);
                startActivity(x);
                finish();
            }
        }
        rltLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                String[] languages = getResources().getStringArray(R.array.languages);
//                String[] langCode = getResources().getStringArray(R.array.languageCode);
//                languageConverter = new LanguageConverter(MainActivity.this, true, languages, langCode, MainActivity.this);
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String[] languages = getResources().getStringArray(R.array.languages);
                String[] langCode = getResources().getStringArray(R.array.languageCode);
                languageConverter = new LanguageConverter(MainActivity.this, true, languages, langCode, MainActivity.this);
            }
        });
        tv_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String[] languages = getResources().getStringArray(R.array.languages);
                String[] langCode = getResources().getStringArray(R.array.languageCode);
                languageConverter = new LanguageConverter(MainActivity.this, true, languages, langCode, MainActivity.this);
            }
        });
        // On Click function used to click action
        home_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                /*if (mBack == 1 || mBack == 2 || (localSharedPreferences.getSharedPreferences(Constants.AccessToken)==null||TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken)))) {
                    onBackPressed();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);//System.exit(0);
                }*/
                onBackPressed();
            }
        });
        // On Click function used to click action
        home_facebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                if (!mydialog.isShowing()) mydialog.show();
                LoginManager.getInstance().logOut();//Logout Facebook
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
            }
        });
        // On Click function used to click action for check Email id in server send link to Email
        home_google.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                if (!mydialog.isShowing()) mydialog.show();
                signIn();
            }
        });
        // On Click function used to click action for check Email id in server send link to Email
        home_createaccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                Intent x = new Intent(getApplicationContext(), Signup_NameActivity.class);
                startActivity(x);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
            }
        });

        // On Click function used to click action
        home_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View vw)
            {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                if (blockedDates!=null)
                {
                    login.putExtra("blockdate", blockedDates);
                }
                startActivity(login);
               /*Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(login, bndlanimation);*/
                finish();
            }
        });

        //checkForUpdates();  // Hockey app update checking

        //Facebook Initialize
        faceBookInitialize();

        // Google Login initialize
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              //  .requestIdToken(MainActivity.this.getResources().getString(R.string.Clientid))
                .requestScopes(new Scope(Scopes.EMAIL))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void onAppleSignIn() {

        BaseUrl.Companion.setAppleCallbackUrl(getResources().getString(R.string.appleredirecturl));
        if (appleClientId == null || TextUtils.isEmpty(appleClientId)) {
            appleClientId = BuildConfig.APPLICATION_ID + ".serviceid";
        }
        SignInWithAppleConfiguration configuration = new SignInWithAppleConfiguration.Builder()
                .clientId(appleClientId)
                .redirectUri(getResources().getString(R.string.appleredirecturl))
                .scope("name email").build();

        SignInWithAppleCallback signInWithAppleCallback = new SignInWithAppleCallback() {
            @Override
            public void onSignInWithAppleSuccess(@NotNull String authorizationCode) {

            }

            @Override
            public void onSignInWithAppleFailure(@NotNull Throwable error) {
               error.printStackTrace();
            }

            @Override
            public void onSignInWithAppleCancel() {

            }

            @Override
            public void onSuccessOnSignIn(@NotNull String response) {

                JSONObject json = new JSONObject();
                try {
                    json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String statuscode = "";
                String statusmessage = "";

                Log.e("AppleResponse","Apple login Res"+json.toString());

                try {
                    statuscode = json.getString("status_code");
                    statusmessage = json.getString("success_message");
                    Log.e("apple login","apple login id"+json.getString("apple_id"));
                    localSharedPreferences.saveSharedPreferences(Constants.APPLEID,json.getString("apple_id"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!statuscode.equals("0"))
                {
                    commonMethods.showProgressDialog(MainActivity.this, customDialog);
                }

                if (statuscode.equals("1")) {
                    //New User
                    commonMethods.hideProgressDialog();
                    createNewUser(response);
                } else if (statuscode.equals("2")) {
                    // Already user
                    commonMethods.hideProgressDialog();
                    onSuccessAppleLogin(response);
                } else {
                    //Error or Other Response
                    Toast.makeText(MainActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnapplelogin.setUpSignInWithAppleOnClick(getSupportFragmentManager(), configuration, getResources().getString(R.string.sign_apple), signInWithAppleCallback);
    }

    private void createNewUser(String response)
    {
        localSharedPreferences.saveSharedPreferences(Constants.isAppleLogin, true);

        // localSharedPreferences.saveSharedPreferences(Constants.AuthId, "");
        JSONObject json = new JSONObject();
        try
        {
            json = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            localSharedPreferences.saveSharedPreferences(Constants.AuthId,json.getString("apple_id"));
            localSharedPreferences.saveSharedPreferences(Constants.Authtype, "apple");
            localSharedPreferences.saveSharedPreferences(Constants.Email,json.getString("email_id"));
            /*socialEmail = json.getString("email_id");
            sessionManager.setSocialMail(socialEmail);*/
        }
        catch (Exception e)
        {
            //socialEmail="";
            e.printStackTrace();
        }
        //getParams(socialEmail, "", "", "", "", "", "", "", "");
        Intent x = new Intent(getApplicationContext(), Signup_FacebookDetails.class);
        startActivity(x);
    }

    public void onSuccessAppleLogin(String jsonResp)
    {
        if (mydialog.isShowing())
        {
            mydialog.dismiss();
            try
            {
                JSONObject jsonObject = new JSONObject(jsonResp);
                System.out.println("Dismiss 2" + jsonResp);
                System.out.println("dob" + jsonObject.getString("dob"));
                dob = jsonObject.getString("dob");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        String currencysymbol = (String) commonMethods.getJsonValue(jsonResp, Constants.CurrencySymbolApi, String.class);
        String cs = Html.fromHtml(currencysymbol).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, cs);        //localSharedPreferences.saveSharedPreferences(Constants.UserDOB, (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.UserDOB, String.class));

        localSharedPreferences.saveSharedPreferences(Constants.UserId, (int) commonMethods.getJsonValue(jsonResp, Constants.UserId, int.class));
        localSharedPreferences.saveSharedPreferences(Constants.AccessToken, (String) commonMethods.getJsonValue(jsonResp, Constants.AccessToken, String.class));
        noToken();
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void customTextView(TextView view)
    {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getResources().getString(R.string.sigin_terms1));
        spanTxt.append(getResources().getString(R.string.sigin_terms2));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = getResources().getString(R.string.termslink);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms2).length(), spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_color)), spanTxt.length() - getResources().getString(R.string.sigin_terms2).length(), spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.Comma));
        spanTxt.append(getResources().getString(R.string.sigin_terms5));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = getResources().getString(R.string.guestrefund);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        },spanTxt.length()-getResources().getString(R.string.sigin_terms5).length(),spanTxt.length(),0);
       // spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_color)),spanTxt.length()-getResources().getString(R.string.sigin_terms3).length(),spanTxt.length(),0);
        spanTxt.append(getResources().getString(R.string.Comma));
        spanTxt.append(getResources().getString(R.string.sigin_terms6));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = getResources().getString(R.string.hostguarantee);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms6).length(), spanTxt.length(), 0);
      //  spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_color)), spanTxt.length() - getResources().getString(R.string.sigin_terms4).length(), spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.and));
        spanTxt.append(getResources().getString(R.string.sigin_terms4));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = getResources().getString(R.string.privacylink);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms4).length(), spanTxt.length(), 0);
       // spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_color)), spanTxt.length() - getResources().getString(R.string.sigin_terms5).length(), spanTxt.length(), 0);
       /* spanTxt.append(getResources().getString(R.string.sigin_terms3));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = getResources().getString(R.string.privacylink);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms3).length(), spanTxt.length(), 0);*/
      //  spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_color)), spanTxt.length() - getResources().getString(R.string.sigin_terms6).length(), spanTxt.length(), 0);
        spanTxt.append(".");
        view.setMovementMethod(LinkMovementMethod.getInstance());
       // view.setHighlightColor(getResources().getColor(R.color.title_text_color));
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
    @Override
    public void onSuccess(String language, String languageCode)
    {

        if (language != null)
        {
            tvChange.setText(language);
            setLocale(languageCode);
        }
        else
        {
            tvChange.setText(getResources().getString(R.string.default_language));
        }
    }
    
    private class GetVersionCode extends AsyncTask<Void, String, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            String newVersion = null;
            try {
                Elements data = Jsoup.connect("https://apkpure.com/search?q=" + MainActivity.this.getPackageName()).userAgent("Mozilla").get().select(".search-dl .search-title a");
                if (data.size() > 0) {
                    Elements data2 = Jsoup.connect("https://apkpure.com" + data.attr("href")).userAgent("Mozilla").get().select(".faq_cat dd p");
                    if (data2.size() > 0) {
                        System.out.println(data2.get(0).text());
                        Pattern pattern = Pattern.compile("Version:\\s+(.*)\\s+\\((\\d+)\\)");
                        Matcher matcher = pattern.matcher(data2.get(0).text());
                        if (matcher.find()) {
                            System.out.println("version name : " + matcher.group(1));
                            System.out.println("version code : " + matcher.group(2));
                        }
                    }
                }
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=it").timeout(30000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get().select(".hAyfc .htlgb").get(7).ownText();
                return newVersion;
            }
            catch (Exception e)
            {
                return newVersion;
            }
        }
        @Override
        protected void onPostExecute(String onlineVersion)
        {
            super.onPostExecute(onlineVersion);
            Log.d("update", "playstore version " + onlineVersion);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //startActivity(getIntent());
        // ... your own onResume implementation
        //checkForCrashes();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable)
        {

        }
        else
        {
            commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, edt, policy, getResources(), MainActivity.this);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(mydialog.isShowing()){mydialog.dismiss();}
        //unregisterManagers();
    }

    @Override
    public void onBackPressed()
    {
        String lastPage=localSharedPreferences.getSharedPreferences(Constants.LastPage);
        if (!TextUtils.isEmpty(lastPage)&&(lastPage.equals("RequestAccept")||lastPage.equals("Contact_host")))
        {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"Space_detail");
        }
        else if (!TextUtils.isEmpty(lastPage)&&(lastPage.equals("Contact_host_exp")))
        {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"Experience_detail");
        }
        if (mBack == 1)
        {
            super.onBackPressed();
        } else if (localSharedPreferences.getSharedPreferences(Constants.AccessToken)==null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken)))
        {
           noToken();
        }else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //unregisterManagers();
    }

    private void checkForCrashes()
    {
        CrashManager.register(this);
    }

    private void checkForUpdates()
    {
        //Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers()
    {
        UpdateManager.unregister();
    }

    public void faceBookInitialize() {
        //Facebook Initialize
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

//        home_facebook.setRead(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends"));

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        getFbKeyHash(getString(R.string.packagename));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                if (Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            profileTracker.stopTracking();
                        }
                    };
                    profileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                }

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            System.out.println("FB jsonobject" + response);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        fbEmail = object.optString("email");
                        fbFullName = object.optString("name");
                        fbID = object.optString("id");
                        fbUserProfile = "https://graph.facebook.com/" + fbID + "/picture?height=100&width=100";
                        String[] namearray = fbFullName.split(" ");
                        localSharedPreferences.saveSharedPreferences(Constants.FirstName, namearray[0]);
                        if (namearray.length > 1)
                            localSharedPreferences.saveSharedPreferences(Constants.LastName, namearray[1]);

                        Log.e("facebook","facebook email"+fbEmail);

                        localSharedPreferences.saveSharedPreferences(Constants.ProfilePicture, fbUserProfile);
                        localSharedPreferences.saveSharedPreferences(Constants.Email, fbEmail);
                        localSharedPreferences.saveSharedPreferences(Constants.FBID, fbID);
                        localSharedPreferences.saveSharedPreferences(Constants.IP, ip);

//                        if (!fbEmail.equals("")) {
//                            if (localSharedPreferences.getSharedPreferences(Constants.DOB)!=null && !localSharedPreferences.getSharedPreferences(Constants.DOB).equals("null") && !localSharedPreferences.getSharedPreferences(Constants.DOB).isEmpty() && !localSharedPreferences.getSharedPreferences(Constants.DOB).equals(""))
//                            {
//                                fbSignup(localSharedPreferences.getSharedPreferences(Constants.Email)); // signup Api calling function
//                            }
//                            else
//                            {
//                                Log.e("DOB Empty","DOB NULL");
//                                Intent x = new Intent(getApplicationContext(), Signup_DobActivity.class);
//                                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                startActivity(x, bndlanimation);
//                            }
//
//                        } else if (fbEmail.isEmpty()) {
//                            if (localSharedPreferences.getSharedPreferences(Constants.DOB)!=null && !localSharedPreferences.getSharedPreferences(Constants.DOB).equals("null") && !localSharedPreferences.getSharedPreferences(Constants.DOB).isEmpty() && !localSharedPreferences.getSharedPreferences(Constants.DOB).equals(""))
//                            {
//                                fbSignup(""); // signup Api calling function
//                            }
//                            else
//                            {
//                                Log.e("DOB Empty","DOB NULL");
//                                Intent x = new Intent(getApplicationContext(), Signup_DobActivity.class);
//                                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
//                                startActivity(x, bndlanimation);
//                            }
//                        }

                        fbSignup(fbEmail);
                    }
                });

                parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();

                Bundle bundle = new Bundle();
                bundle.putString("fields", "token_for_business");

            }

            @Override
            public void onCancel() {
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                System.out.println("Facebook Login failed!!");
            }

            @Override
            public void onError(FacebookException e) {
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "An unknown network error has occured", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }

        });
    }

    //Create FB KeyHash
    public void getFbKeyHash(String packageName)
    {
        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                System.out.println("hash key value" + something);
                Log.e("hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }
    }

    //get Facebook Details
    private void displayMessage(Profile profile) {

        if (profile != null) {
            fbFullName = profile.getName();
            fbFirstName = profile.getFirstName();
            fbLastName = profile.getLastName();
            fbID = profile.getId();
            fbUserProfile = profile.getProfilePictureUri(100, 100).toString();
            localSharedPreferences.saveSharedPreferences(Constants.FirstName, fbFirstName);
            localSharedPreferences.saveSharedPreferences(Constants.LastName, fbLastName);
            if (fbEmail != null) {
                if (!fbEmail.equals("")) {

                } else if (fbEmail.isEmpty()) {
                    // LoginManager.getInstance().logOut();
                }
            }
            localSharedPreferences.saveSharedPreferences(Constants.ProfilePicture, fbUserProfile);
        }
    }

    //Call Facebook StartActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mydialog.isShowing()){mydialog.dismiss();}
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        //    Log.e("account", String.valueOf(task.getResultTemp()));
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.e("handleSigninResult","entered");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        //    Log.e("GoogleSignInAccount",account.getDisplayName());
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google login", "signInResult:failed code=" + e.getStatusCode()+e.getLocalizedMessage());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if(account!=null) {
            googleID = account.getId();
            googleFullName = account.getDisplayName();
            System.out.println("Get Email : "+account.getEmail());
            if (account.getPhotoUrl() != null)
                googleUserProfile = account.getPhotoUrl().toString();
            else
                googleUserProfile = "";
            googleEmail = account.getEmail();

            if(googleFullName==null||googleFullName.equals("")){
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
                signOut();
                return;
            }

            String[] splitStr = googleFullName.split("\\s+");
            String firstName = splitStr[0];
            String lastName = "";
            for (int i = 1; i < splitStr.length; i++) {
                lastName = lastName + " " + splitStr[i];
            }
            if (lastName.equals("")) lastName = " ";

            localSharedPreferences.saveSharedPreferences(Constants.Email, googleEmail);
            // localSharedPreferences.saveSharedPreferences(Constants.DOB," ");
            localSharedPreferences.saveSharedPreferences(Constants.GPID, googleID);
            localSharedPreferences.saveSharedPreferences(Constants.FirstName, firstName);
            localSharedPreferences.saveSharedPreferences(Constants.LastName, lastName);
            localSharedPreferences.saveSharedPreferences(Constants.ProfilePicture, googleUserProfile);
            localSharedPreferences.saveSharedPreferences(Constants.IP, ip);

            gpSignup();
        }else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.unable_get_details),Toast.LENGTH_SHORT).show();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public void fbSignup(String fbEmail)
    {
        apiService.fbsignup(localSharedPreferences.getSharedPreferences(Constants.FirstName), localSharedPreferences.getSharedPreferences(Constants.LastName), fbEmail, localSharedPreferences.getSharedPreferences(Constants.FBID), "facebook",localSharedPreferences.getSharedPreferences(Constants.FBID),localSharedPreferences.getSharedPreferences(Constants.ProfilePicture),tz.getID(), dob).enqueue(new RequestCallback(REQ_FBSIGNUP, MainActivity.this));
    }

    public void gpSignup()
    {
        apiService.gpsignup(localSharedPreferences.getSharedPreferences(Constants.FirstName), localSharedPreferences.getSharedPreferences(Constants.LastName), localSharedPreferences.getSharedPreferences(Constants.Email), localSharedPreferences.getSharedPreferences(Constants.GPID), "google",localSharedPreferences.getSharedPreferences(Constants.GPID),localSharedPreferences.getSharedPreferences(Constants.ProfilePicture),tz.getID()).enqueue(new RequestCallback(REQ_GPSIGNUP, MainActivity.this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        Log.e("FaceBook","FaceBook");
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_FBSIGNUP:
                if (mydialog.isShowing()) mydialog.dismiss();
                if (jsonResp.isSuccess()) {
                    Log.e("SuccessLogin","");
                    onSuccessLogin(jsonResp); // onSuccessLogin call method
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    String statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
                    if (statusCode.equals("2")) {
                        Intent x = new Intent(getApplicationContext(), Signup_FacebookDetails.class);
                        startActivity(x);
                        finish();
                    } else {
                        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, policy, getResources(), this);
                    }
                }
                break;

            case REQ_COMMON_DATA:

                if (mydialog.isShowing()) mydialog.dismiss();
                if (jsonResp.isSuccess()) {
                    Log.e("SuccessLogin", "");
                    onSuccessCommonData(jsonResp); // onSuccessLogin call method
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, policy, getResources(), this);
                }
                break;

            case REQ_GPSIGNUP:
                signOut();
                if (jsonResp.isSuccess())
                {
                    onSuccessLogin(jsonResp);
                }
                else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
                {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, policy, getResources(), this);
                }
                break;
            default:
                break;

        }
    }

    private void onSuccessCommonData(JsonResponse jsonResp)
    {
        appleClientId = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.AppleServiceId, String.class);
        onAppleSignIn();
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data)
    {

    }

    public void onSuccessLogin(JsonResponse jsonResp) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonResp.getStrResponse());
                System.out.println("Dismiss 2"+jsonResp.getStrResponse());
                System.out.println("dob"+jsonObject.getString("dob"));
                dob = jsonObject.getString("dob");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        String currencysymbol=(String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.CurrencySymbolApi, String.class);
        String cs= Html.fromHtml(currencysymbol).toString();
        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, cs);        //localSharedPreferences.saveSharedPreferences(Constants.UserDOB, (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.UserDOB, String.class));


        localSharedPreferences.saveSharedPreferences(Constants.UserId, (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.UserId, int.class));
        localSharedPreferences.saveSharedPreferences(Constants.AccessToken, (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.AccessToken, String.class));

        noToken();

    }

    public void reqCommonData() {
        if (!mydialog.isShowing()) mydialog.show();
        apiService.requestCommonData().enqueue(new RequestCallback(REQ_COMMON_DATA, MainActivity.this));
    }

    private void noToken(){
        System.out.println("USER DATE OF BIRTH " + localSharedPreferences.getSharedPreferences(Constants.UserDOB));
        String lastPage=localSharedPreferences.getSharedPreferences(Constants.LastPage);
        System.out.println("get Local VAlue of LastPage "+lastPage);

        if (!TextUtils.isEmpty(lastPage) && lastPage.equals("inbox")
                ||lastPage.equals("saved")) {
            Log.e("Saved","Saved");
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        } else if (!TextUtils.isEmpty(lastPage) &&lastPage.equals("profile")) {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
           // startActivity(x, bndlanimation);
            if (Build.VERSION.SDK_INT >= 21) {
                startActivity(x);
                finish();
            }
            else {
                startActivity(x, bndlanimation);
                finish();
            }
        } else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("Room_detail")) {
            Intent x = new Intent(getApplicationContext(), SpaceDetailActivity.class);
            x.putExtra("isSpaceBack",1);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }
        //  ***Experience Start***

        // ***Experience End***

        else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("ExploreSearch")) {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            //x.putExtra("tabsaved", 0);
            startActivity(x);
            finish();
        }else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("Map")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPrefs.getString("explore", "");
            Type type = new TypeToken<ArrayList<ExploreDataModel>>() {}.getType();
            ArrayList<ExploreDataModel> arrayList = gson.fromJson(json, type);

            Intent x = new Intent(getApplicationContext(), MapActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("searchlist", (Serializable) arrayList);
            x.putExtra("BUNDLE", args);
            x.putExtra("isMapBack", 1);
            startActivity(x);
            finish();
        }else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("ShowAll")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPrefs.getString("showAllMap", "");
            Type type = new TypeToken<ArrayList<Detail>>() {
            }.getType();
            ArrayList<Detail> arrayList = gson.fromJson(json, type);

            Intent x = new Intent(getApplicationContext(), ShowAllMapActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("showAllMap", (Serializable) arrayList);
            x.putExtra("BUNDLE", args);
            x.putExtra("isMapBack", 1);
            startActivity(x);
            finish();
        }
        // ***Experience Start***

        // ***Experience End***
        else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("RequestAccept")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPrefs.getString("blockdates", "");
            Type type = new TypeToken<String[]>() {}.getType();
            String[] blocked= gson.fromJson(json, type);

            Intent x = new Intent(getApplicationContext(), RequestActivity.class);
            x.putExtra("location", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENADDRESS));
            x.putExtra("roomname", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENROOMNAME));
            x.putExtra("bedroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBEDROOM));
            x.putExtra("bathroom", localSharedPreferences.getSharedPreferences(Constants.EMPTYTOKENBATHROOM));
            x.putExtra("blockdate", blocked);
            x.putExtra("ReqBack", 1);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
        }
        else if (!TextUtils.isEmpty(lastPage) && lastPage.equals("Check_availability")) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Gson gson = new Gson();
            String json = sharedPrefs.getString(Constants.SpaceResults, "");
            Type type = new TypeToken<SpaceResult>() {}.getType();
            SpaceResult spaceResult = gson.fromJson(json, type);

            localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1");
            Intent x = new Intent(getApplicationContext(), SpaceAvailableActivity.class);
            x.putExtra(Constants.SpaceResults, spaceResult);
            x.putExtra(isContactHost, false);
            x.putExtra("ContactBack", 2);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
        }

        else {
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }
    }

    /********************************************************************
     Google Signin Start
     ********************************************************************/

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
            accessTokenTracker.stopTracking();
            profileTracker.stopTracking();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    // This fuctioned used turn on wifi and mobile data setting dialog
    protected void createNetErrorDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.connection_msg)).setTitle(getString(R.string.connect_error)).setCancelable(false).setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alert.dismiss();
                alert.cancel();
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                startActivity(i);

            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
            }
        });
        alert = builder.create();

        alert.show();
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        redirection();

        localSharedPreferences.saveSharedPreferences(LanguageRecreate, true);
    }

    private void redirection() {
        Intent x = new Intent(getApplicationContext(), SplashActivity.class);
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(x);
        finish();
    }

    public interface LocaleListener {
        void setLocale(String values);
    }
}
