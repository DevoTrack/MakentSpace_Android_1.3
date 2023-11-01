package com.makent.trioangle.newhome.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.DeviceCredentialHandlerBridge;

import com.google.android.libraries.places.api.Places;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.travelling.HomeActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.Executor;

import static com.makent.trioangle.helper.Constants.Host_Preview;
import static com.makent.trioangle.helper.Constants.LanguageCode;
import static com.makent.trioangle.helper.Constants.LanguageName;
import static com.makent.trioangle.helper.Constants.LanguageRecreate;

public class SplashActivity extends AppCompatActivity {
    LocalSharedPreferences localSharedPreferences;
    String langCode,langName;
    BiometricPrompt.PromptInfo promptInfo;
    BiometricPrompt biometricPrompt;
    PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
            initMapPlaceAPI();
        localSharedPreferences = new LocalSharedPreferences(this);
        localSharedPreferences.saveSharedPreferences(LanguageRecreate, true);
        langCode = localSharedPreferences.getSharedPreferences(LanguageCode);
        langName = localSharedPreferences.getSharedPreferences(LanguageName);
        localSharedPreferences.saveSharedPreferences(Constants.LastPage, "ExploreSearch");
        clearSavedData();

        if (BiometricManager.from(getApplicationContext()).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
            localSharedPreferences.saveSharedPreferences(Constants.SecurityCheck,false);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (langCode != null) {
                    setLocale(langCode);
                    localSharedPreferences.saveSharedPreferences(Constants.LanguageName, langName);
                } else {
                    localSharedPreferences.saveSharedPreferences(Constants.LanguageCode, "en");
                    localSharedPreferences.saveSharedPreferences(Constants.LanguageName, "English");
                    setLocale("en");
                }

            }
        }, 3000);

    }

    private void initMapPlaceAPI() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_key));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void clearSavedData()
    {
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
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategory, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategoryName, null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterCategorySize, 0);

        localSharedPreferences.saveSharedPreferences(Constants.CheckAvailableScreen, null);

        localSharedPreferences.saveSharedPreferences(Constants.GuestLastName, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestFirstName, null);
        localSharedPreferences.saveSharedPreferences(Constants.Schedule_id, null);
        localSharedPreferences.saveSharedPreferences(Constants.SelectedExpPrice, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExpId, null);
        localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp, null);
        localSharedPreferences.saveSharedPreferences(Constants.GuestImage, null);

        localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreHomeType_key, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType, null);
        localSharedPreferences.saveSharedPreferences(Constants.ExploreRoom_ExpType_key, null);
        localSharedPreferences.saveSharedPreferences(Constants.HomePage, "Home");
        localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 0);
        localSharedPreferences.saveSharedPreferences(Constants.HomeShowAll, 0);

        System.out.println("Saved Data cleared  : ");

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        redirection();
    }

    private void redirection() {
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {

            if(localSharedPreferences.getSharedPreferencesBool(Constants.SecurityCheck)){
                showBiometricPrompt();
            }else{
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
                finish();
            }
            //biometricCheck();
        } else {
            Intent x = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(x);
            finish();
        }
    }

//    keytool -exportcert -alias makentspace -keystore /home/trioangle/Documents/keystorefile\makentspace.jks -list -v

    private void biometricCheck() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Log.d("App can authenticate using biometrics.");
                showBiometricPrompt();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:

                // Log.d("No biometric features available on this device.");

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:

                // Log.d("Biometric features are currently unavailable.");

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Log.d("The user hasn't associated any biometric credentials " +"with their account.");
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
                finish();
                break;
        }
    }

    private Handler handler = new Handler();

    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    private void showBiometricPrompt() {
        promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Unlock Makentspace")
                        // .setSubtitle("Log in using your biometric credential")
                        //.setNegativeButtonText("Cancel")
                        .setDeviceCredentialAllowed(true)
                        .build();

         biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                /*Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();*/
                System.out.println("Error code : "+errorCode);
                System.out.println("Error code : "+errString);

               /* if (BiometricManager.from(getApplicationContext()).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
                    Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(x);
                    finish();
                }*/

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                BiometricPrompt.CryptoObject authenticatedCryptoObject =
                        result.getCryptoObject();
                // User has verified the signature, cipher, or message
                // authentication code (MAC) associated with the crypto object,
                // so you can use it in your app's crypto-driven workflows

                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // Displays the "log in" prompt.
        biometricPrompt.authenticate(promptInfo);
    }

//    @Override
//    protected void onPause() {
//        biometricPrompt.cancelAuthentication();
//        super.onPause();
//
//    }

    @Override
    protected void onPause () {
        super.onPause();
        //redirection();
       // Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop () {
      //  biometricPrompt.cancelAuthentication();
        super.onStop();
       // Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume Called");
        redirection();
//        if (BiometricManager.from(getApplicationContext()).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS)
//        {
//            localSharedPreferences.saveSharedPreferences(Constants.SecurityCheck,false);
//        }
//
//        langCode = localSharedPreferences.getSharedPreferences(LanguageCode);
//        langName = localSharedPreferences.getSharedPreferences(LanguageName);
//
//        if (langCode != null) {
//            setLocale(langCode);
//            localSharedPreferences.saveSharedPreferences(Constants.LanguageName, langName);
//        } else {
//            localSharedPreferences.saveSharedPreferences(Constants.LanguageCode, "en");
//            localSharedPreferences.saveSharedPreferences(Constants.LanguageName, "English");
//            setLocale("en");
//        }

    }

}


