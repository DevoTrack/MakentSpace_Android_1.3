package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestSearchanywhereActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.makent.trioangle.R;
import com.makent.trioangle.autocomplete.GoogleMapPlaceSearchAutoCompleteRecyclerView;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.AppLocationService;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.placesearch.PlacesAutoCompleteAdapter;
import com.makent.trioangle.util.CommonMethods;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/* ***********************************************************************
This is Search Page Contain Anywhere and Nearby Search
**************************************************************************  */
public class Search_anywhere extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,GoogleMapPlaceSearchAutoCompleteRecyclerView.AutoCompleteAddressTouchListener {


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    protected GoogleApiClient mGoogleApiClient;
    Button search_where_close, search_where_reset;
    TextView explore_anywhere, explore_nearby;
    AppLocationService appLocationService;
    LinearLayout anywhere_nearby;
    LocalSharedPreferences localSharedPreferences;
    String searchlocation;
    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView;



    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;

    private String address;
    List<Address> addressList = null;
    String getNearByLocation="";

    final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(Search_anywhere.class.getName());

    PlacesClient placesClient;
    AutocompleteSessionToken googleAutoCompleteToken;
    GoogleMapPlaceSearchAutoCompleteRecyclerView googleMapPlaceSearchAutoCompleteRecyclerView;
    private boolean isInternetAvailable;
    public int counti = 0;
    public String oldstring = "";
    public AlertDialog dialog;
    private List<AutocompletePrediction>addressAutoCompletePredictions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_search_anywhere);
        commonMethods = new CommonMethods();

        dialog = commonMethods.getAlertDialog(this);

        appLocationService = new AppLocationService(this);

        localSharedPreferences = new LocalSharedPreferences(this);

        anywhere_nearby = (LinearLayout) findViewById(R.id.anywhere_nearby);

        mAutocompleteView = (EditText) findViewById(R.id.search_anywhere);
        mRecyclerView = findViewById(R.id.list_search);

        search_where_close = (Button) findViewById(R.id.search_where_close);
        commonMethods.setTvAlign(search_where_close,this);
        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), getString(R.string.google_key));

        placesClient = com.google.android.libraries.places.api.Places.createClient(Search_anywhere.this);
        googleAutoCompleteToken = AutocompleteSessionToken.newInstance();
        googleMapPlaceSearchAutoCompleteRecyclerView = new GoogleMapPlaceSearchAutoCompleteRecyclerView(addressAutoCompletePredictions, this, this);
        mAutocompleteView.addTextChangedListener(new NameTextWatcher(mAutocompleteView));

        search_where_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });


        explore_anywhere = (TextView) findViewById(R.id.explore_anywhere);

        explore_anywhere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                searchlocation = getResources().getString(R.string.anywhere);
                localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, searchlocation);
                //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
                String[] address = searchlocation.split(",");
                System.out.println("address " + address[0]);
                localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,address[0]);
                localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,true);
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                x.putExtra("filter", "filter");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
                finish();
            }
        });


        explore_nearby = (TextView) findViewById(R.id.explore_nearby);

        explore_nearby.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                searchlocation = getResources().getString(R.string.nearby);
                localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, searchlocation);
                //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
                String[] address = searchlocation.split(",");
                System.out.println("address " + address[0]);
                localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,address[0]);

                Location location = null;


                if (ActivityCompat.checkSelfPermission(Search_anywhere.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Search_anywhere.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Search_anywhere.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                } else {
                    location = appLocationService
                            .getLocation(LocationManager.NETWORK_PROVIDER);
                }


                if (location == null) {


                    if (ActivityCompat.checkSelfPermission(Search_anywhere.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Search_anywhere.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Search_anywhere.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        return;
                    } else {
                        location = appLocationService
                                .getLocation(LocationManager.GPS_PROVIDER);
                    }
                }


                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng nearBy = new LatLng(latitude,longitude);
                    fetchAddress(nearBy,"nearby");
                    /*LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());*/
                } else {
                    showSettingsAlert(); // this is used to location settings alert dialog
                }
            }
        });


        search_where_reset = (Button) findViewById(R.id.search_where_reset);

        search_where_reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                mAutocompleteView.setText("");
                mRecyclerView.removeAllViews();
            }
        });
        searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        if (searchlocation != null) {
            mAutocompleteView.setText(searchlocation);
        }



        // Place search list
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(googleMapPlaceSearchAutoCompleteRecyclerView);


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        //Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Search_anywhere.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Search_anywhere.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void fetchAddress(LatLng location, final String type) {
        address = null;
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            ;

            @Override
            protected String doInBackground(Void... params) {

                if (Geocoder.isPresent()) {
                    try {

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        if (addresses != null) {
                            String countrys = addresses.get(0).getCountryName();

                            String adress0 = addresses.get(0).getAddressLine(0);
                            String adress1 = addresses.get(0).getAddressLine(1);

                            address = adress0 + " " + adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            /*if (address != null) {
                                //   pickupaddresss=address;
                                return address;
                            }*/
                        }
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }
                if (address != null) // i.e., Geocoder succeed
                {
                    return address;
                } else // i.e., Geocoder failed
                {
                    return fetchAddressUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchAddressUsingGoogleMap() {
                String st;
                addressList = new ArrayList<Address>();
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.latitude + "," + location.longitude + "&sensor=false&key=" + getResources().getString(R.string.google_key);

                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    Log.v("googleMapResponse", "googleMapResponse" + googleMapResponse);
                    st = (String) googleMapResponse.get("status");
                    if (st.equals("ZERO_RESULTS")) {
                        Log.v("ZERO_RESULTS", "ZERO_RESULTS");
                        address = null;
                        addressList.clear();
                        return null;
                    }
                    for (int i = 0; i < results.length(); i++) {


                        JSONObject result = results.getJSONObject(i);


                        String indiStr = result.getString("formatted_address");


                        Address addr = new Address(Locale.getDefault());


                        addr.setAddressLine(0, indiStr);
                        //  country=addr.getCountryName();

                        addressList.add(addr);


                    }

                    int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").length();
                    for (int i = 0; i < len; i++) {
                        if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country")) {
                            String countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name");
                            System.out.println("countrys " + countrys);
                        }
                    }

                    if (addressList != null) {

                        String adress0 = addressList.get(0).getAddressLine(0);
                        String adress1 = addressList.get(0).getAddressLine(1);
                        address = adress0;//+" "+adress1;
                        //address = adress0+" "+adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        address = address.replaceAll("null", "");

                        if (address != null) {
                            return address;
                        }
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String address) {
                if (address != null) {
                    if (type.equals("nearby")) {
                        if (address != null) {
                            address = address.replaceAll("null", "");
                            System.out.println("get AutoComplete Address "+address);
                            getNearByLocation=address;
                            localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLatitude, String.valueOf(location.latitude));
                            localSharedPreferences.saveSharedPreferences(Constants.SearchLocationLongitude, String.valueOf(location.longitude));
                            localSharedPreferences.saveSharedPreferences(Constants.SearchLocationNearby, getNearByLocation);
                            localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,true);
                            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                            x.putExtra("filter", "filter");
                            startActivity(x, bndlanimation);
                            finish();
                        }
                    }
                }
            }

            ;
        }.execute();
    }

    @Override
    public void selectedAddress(AutocompletePrediction autocompletePrediction) {
        if (counti > 0) {
            autocompletePrediction = null;
        }
        System.out.println("autocompletePrediction "+autocompletePrediction);
        if (autocompletePrediction != null) {
            counti++;

            searchlocation = autocompletePrediction.getFullText(null).toString();

            mAutocompleteView.setText(autocompletePrediction.getPrimaryText(null));
            oldstring = autocompletePrediction.getPrimaryText(null).toString();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAutocompleteView.getWindowToken(), 0);

            mRecyclerView.setVisibility(View.GONE);
            System.out.println("searchlocation "+searchlocation);
            localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, searchlocation);
            //localSharedPreferences.saveSharedPreferences(Constants.searchIconChanged, 1);
            String[] address = searchlocation.split(",");
            System.out.println("address " + address[0]);
            localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,address[0]);
            localSharedPreferences.saveSharedPreferences(Constants.isFilterApplied,true);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            x.putExtra("filter", "filter");
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(x, bndlanimation);
            finish();
        }
    }

    private class NameTextWatcher implements TextWatcher {

        private View view;

        private NameTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().equals("")) {
                mRecyclerView.setVisibility(View.GONE);
            }else{
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }

        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().equals("")) {
                mRecyclerView.setVisibility(View.GONE);
                anywhere_nearby.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.VISIBLE);
                anywhere_nearby.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(final Editable s) {
            isInternetAvailable = commonMethods.isOnline(getApplicationContext());
            if (!isInternetAvailable) {
                commonMethods.showMessage(Search_anywhere.this, dialog, getString(R.string.network_failure));
            }

            if (view.hasFocus()) {
                if (!s.toString().equals("")) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!oldstring.equals(s.toString())) {
                                oldstring = s.toString();
                                getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(s.toString());
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 1500);

                } else  {
                    clearAddressAndHideRecyclerView();
                }

               /* if (s.toString().equals("")) {
                    googleMapPlaceSearchAutoCompleteRecyclerView.clearAddresses();
                    mRecyclerView.setVisibility(View.GONE);
                }*/
            } else {
                clearAddressAndHideRecyclerView();
            }

        }
    }

    public void getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(String queryAddress){

        //RectangularBounds bounds = RectangularBounds.newInstance(first, sec);
        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(googleAutoCompleteToken)
                        .setQuery(queryAddress)
                        .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(
                        (response) -> {
                            if(response.getAutocompletePredictions().size()>0){
                                googleMapPlaceSearchAutoCompleteRecyclerView.updateList(response.getAutocompletePredictions());
                            }else{
                                clearAddressAndHideRecyclerView();
                                //showUserMessage(getResources().getString(R.string.no_address_found));
                            }


                        })
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;

                    }
                    exception.printStackTrace();
                });
    }

    public void clearAddressAndHideRecyclerView(){
        googleMapPlaceSearchAutoCompleteRecyclerView.clearAddresses();
        mRecyclerView.setVisibility(View.GONE);
        anywhere_nearby.setVisibility(View.VISIBLE);
    }

    public void onStop() {
        super.onStop();
        //localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null);
    }

}
