package com.makent.trioangle.host;

/**
 * @package com.makent.trioangle
 * @subpackage host/tabs
 * @category HostLYS_LocationActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.AppLocationService;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.placesearch.PlacesAutoCompleteAdapter;
import com.makent.trioangle.placesearch.RecyclerItemClickListener;
import com.makent.trioangle.util.CommonMethods;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.ButterKnife;

/************************************************************************
This is List Your Space Home Page Contain the Room Location
***************************************************************************/

public class LYS_Location extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    RunTimePermission runTimePermission;

    ImageView location_back, location_gps_image;
    RelativeLayout location_next, location_title;
    EditText location_edit;
    String status = "";
    boolean mapmove = true;
    LocalSharedPreferences localSharedPreferences;
    String searchlocation;

    protected GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;

    AppLocationService appLocationService;

    String Listlat, Listlong;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;

    final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(LYS_Location.class.getName(),null);
    List<Address> addressList = null;
    String address = null;
    String countrys = null;
    LatLng getLocations = null;
    String getAddress = null;
    String lat, log;
    Dialog_loading mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_lys_location);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        commonMethods = new CommonMethods();

        localSharedPreferences = new LocalSharedPreferences(this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        checkAllPermission(Constants.PERMISSIONS_LOCATION);

        appLocationService = new AppLocationService(this);
        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        location_next = (RelativeLayout) findViewById(R.id.location_next);
        location_next.setEnabled(false);

        location_edit = (EditText) findViewById(R.id.location_edit);
        location_back = (ImageView) findViewById(R.id.location_back);
        commonMethods.rotateArrow(location_back,this);
        location_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        location_gps_image = (ImageView) findViewById(R.id.location_gps_image);
        location_gps_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {

                //appLocationService.checkLocationPermission();
                Location location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
              if(location == null){
                  location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
              }



                    System.out.println("Location not found" + location);




                if (location != null) {
                    mapmove = true;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    location_next.setEnabled(true);
                    Listlat = String.valueOf(latitude);
                    Listlong = String.valueOf(longitude);
                    LatLng cur_Latlng = new LatLng(latitude, longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                   /* LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());*/
                } else {
                    showSettingsAlert(); //this is used to Enable Location Provider getting function
                }
            }
        });

        location_title = (RelativeLayout) findViewById(R.id.location_title);

        location_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        location_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                System.out.println("latlng"+Listlat+"" +Listlong);
                // String address=location_edit.getText().toString();
                if (Listlat != null && Listlong != null) {
                    localSharedPreferences.saveSharedPreferences(Constants.ListLocationLat, Listlat);
                    localSharedPreferences.saveSharedPreferences(Constants.ListLocationLong, Listlong);
                    Intent lysproperty = new Intent(getApplicationContext(), LYS_Rooms_Beds.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(lysproperty, bndlanimation);
                }
            }
        });


        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.locationsearch_adapter, mGoogleApiClient, BOUNDS_INDIA, null, mRecyclerView);

        mRecyclerView = (RecyclerView) findViewById(R.id.location_placesearch);
        mRecyclerView.setVisibility(View.GONE);
        mapmove = true;

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        location_edit.addTextChangedListener(PlaceTextWatcher);

        location_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mapmove = false;
                location_edit.addTextChangedListener(PlaceTextWatcher);
                return false;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                location_edit.removeTextChangedListener(PlaceTextWatcher);
                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i("TAG", "Autocomplete item selected: " + item.description);
                if (!mydialog.isShowing()) mydialog.show();
                searchlocation = (String) item.description;
                if (searchlocation != null) {
                    searchlocation = searchlocation.replaceAll("null", "");
                    location_edit.setText(searchlocation);
                }
                mRecyclerView.setVisibility(View.GONE);
                mapmove = true;

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(location_edit.getWindowToken(), 0);



                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            fetchLocation(searchlocation, "search");
                                    /*LatLng cur_Latlng= getLocationFromAddress(LYS_Location.this,searchlocation);

                                    Listlat=String.valueOf(cur_Latlng.latitude);
                                    Listlong=String.valueOf(cur_Latlng.longitude);
                                    location_next.setEnabled(true);
                                    //LatLng cur_Latlng=new LatLng(21.0000,78.0000); // giving your marker to zoom to your location area.
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));*/
                            //Do the things here on Click.....
                            //  Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.i("TAG", "Clicked: " + item.description);
                Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
            }
        }));

    }

    private void getLocation() {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
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
        initilizeMap();
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

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    System.out.println("Bundle" + bundle);
                    locationAddress = bundle.getString("address");
                    System.out.println("Address" + locationAddress);
                    break;
                default:
                    locationAddress = null;
            }
            /*localSharedPreferences.saveSharedPreferences(Constants.SearchLocationNearby,locationAddress);
            Intent x=new Intent(getApplicationContext(),HomeActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
            startActivity(x,bndlanimation);
            finish();*/
            // tvAddress.setText(locationAddress);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LYS_Location.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                LYS_Location.this.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.lysmap);
            mapFragment.getMapAsync(this);

            //  googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mainmap)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                //  Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //   if(mapmove) {
                location_edit.removeTextChangedListener(PlaceTextWatcher);
                mRecyclerView.setVisibility(View.GONE);
                fetchAddress(cameraPosition.target, "mapmove");
                if (status.equals("ZERO_RESULTS")) {
                    location_edit.setText("");
                    location_next.setEnabled(false);
                }
                /*String address = getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude);
                if(address.equals(""))
                {
                    location_next.setEnabled(false);
                }else {
                    location_next.setEnabled(true);
                }
                location_edit.setText(address);
                Listlat=String.valueOf(cameraPosition.target.latitude);
                Listlong=String.valueOf(cameraPosition.target.longitude);
                Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));
                Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                location_edit.addTextChangedListener(PlaceTextWatcher);
                mapmove=true;*/

                //}
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                /*Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.get.getAddressLine(i));//.append("\n");
                }
                strAdd = strReturnedAddress.toString();*/

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                if (city != null) {
                    strAdd = city + " ";
                }

                if (state != null) {
                    strAdd = strAdd + state + " ";
                }
                if (country != null) {
                    strAdd = strAdd + country + " ";
                }

                // strAdd=city+" "+state+" "+country;
                // String postalCode = addresses.get(0).getPostalCode();
                // String knownName = addresses.get(0).getFeatureName();
                // Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                //   Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    protected TextWatcher PlaceTextWatcher = new TextWatcher() {
        private Timer timer = new Timer();
        private final long DELAY = 1500; // milliseconds

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO: do what you need here (refresh list)
                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(getApplicationContext(), "Hello "+s.toString(), Toast.LENGTH_SHORT).show();
                                if (s.toString().length() > 0 && !mapmove) {
                                    mRecyclerView.getRecycledViewPool().clear();
                                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    mapmove = false;
                                }
                            }
                        });

                    }
                }, DELAY);

                // location_next.setEnabled(true);
            } else if (!mGoogleApiClient.isConnected()) {
                //   Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                Log.w(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
                location_next.setEnabled(false);
            }

            if (s.toString().equals("")) {
                location_next.setEnabled(false);
                mRecyclerView.setVisibility(View.GONE);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void afterTextChanged(Editable s) {

        }
    };


    public void fetchLocation(String addresss, final String type) {
        getAddress = addresss;

        new AsyncTask<Void, Void, String>() {
            String locations = null;

            protected void onPreExecute() {

            }

            ;

            @Override
            protected String doInBackground(Void... params) {

                if (Geocoder.isPresent()) {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> address;

                        // May throw an IOException
                        address = geocoder.getFromLocationName(getAddress, 5);
                        if (address == null) {
                            return null;
                        }
                        Address location = address.get(0);

                        countrys = address.get(0).getCountryName();
                        System.out.println("1Chcek country countrys" + countrys);

                        location.getLatitude();
                        location.getLongitude();

                        lat = String.valueOf(location.getLatitude());
                        log = String.valueOf(location.getLongitude());
                        locations = lat + "," + log;
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (locations != null) // i.e., Geocoder succeed
                {
                    System.out.println("LATLNG mobile" + locations);
                    return locations;
                } else // i.e., Geocoder failed
                {
                    return fetchLocationUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchLocationUsingGoogleMap() {
                getAddress = getAddress.replaceAll(" ", "%20");
                String googleMapUrl = "https://maps.google.com/maps/api/geocode/json?address=" + getAddress + "&sensor=false&key=" + getResources().getString(R.string.google_key);
                System.out.println("Google Map Url " + googleMapUrl);
                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    if (googleMapResponse.length() > 0) {
                        String longitute = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

                        String latitude = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");

                        int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").length();
                        for (int i = 0; i < len; i++) {
                            if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country")) {
                                countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name");
                                System.out.println("countrys " + countrys);
                            }
                        }
                            /*countrys = ((JSONArray)googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(3).getString("long_name");*/
                        System.out.println("LATLNG google" + latitude + "," + longitute);
                        return latitude + "," + longitute;
                    } else {
                        return null;
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                finally {
                    if (ANDROID_HTTP_CLIENT!=null)
                    {
                        ANDROID_HTTP_CLIENT.close();
                    }
                }
                return null;
            }

            protected void onPostExecute(String location) {
                if (location != null) {
                    String[] parts = location.split(",");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                    Double lat = Double.valueOf(parts[0]);
                    Double lng = Double.valueOf(parts[1]);
                    LatLng latLng = new LatLng(lat, lng);

                    if (type.equals("search")) {
                        Listlat = String.valueOf(latLng.latitude);
                        Listlong = String.valueOf(latLng.longitude);
                        location_next.setEnabled(true);
                        //LatLng cur_Latlng=new LatLng(21.0000,78.0000); // giving your marker to zoom to your location area.
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        location_edit.removeTextChangedListener(PlaceTextWatcher);
                        if (mydialog.isShowing()) mydialog.dismiss();
                    } else if (type.equals("workclick")) {


                    } else if (type.equals("searchitemclick")) {

                    }
                } else {
                    snackBar("Unable to get location please try again...");
                    if (mydialog.isShowing()) mydialog.dismiss();
                }
            }

            ;
        }.execute();
    }

    public void fetchAddress(LatLng location, final String type) {
        getLocations = location;
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
                        List<Address> addresses = geocoder.getFromLocation(getLocations.latitude, getLocations.longitude, 1);
                        if (addresses != null) {
                            countrys = addresses.get(0).getCountryName();

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
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + getLocations.latitude + "," + getLocations.longitude + "&sensor=false&key=" + getResources().getString(R.string.google_key);

                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    Log.v("googleMapResponse", "googleMapResponse" + googleMapResponse);
                    st = (String) googleMapResponse.get("status");
                    if (st.equals("ZERO_RESULTS")) {
                        Log.v("ZERO_RESULTS", "ZERO_RESULTS" + status);
                        address = null;
                        status = st;
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
                            countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name");
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
                finally {
                    if (ANDROID_HTTP_CLIENT!=null)
                    {
                        ANDROID_HTTP_CLIENT.close();
                    }
                }
                return null;
            }

            protected void onPostExecute(String address) {
                if (address != null) {
                    if (type.equals("mapmove")) {
                        if (address.equals("")) {
                            location_next.setEnabled(false);
                        } else {
                            location_next.setEnabled(true);
                        }
                        if (address != null) {
                            address = address.replaceAll("null", "");
                            location_edit.setText(address);
                        }
                        Listlat = String.valueOf(location.latitude);
                        Listlong = String.valueOf(location.longitude);
                        Log.i("centerLat", String.valueOf(location.latitude));
                        Log.i("centerLong", String.valueOf(location.longitude));
                        //location_edit.addTextChangedListener(PlaceTextWatcher);
                        mapmove = true;
                    } else if (type.equals("country")) {
                    }
                }
            }

            ;
        }.execute();
    }

    public void snackBar(String message) {
        // Create the Snackbar

        System.out.println("Snack bar started");
        Snackbar snackbar = Snackbar.make(location_back, "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.title_text_color));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        if (message.equals(getResources().getString(R.string.interneterror))) {
            button.setVisibility(View.VISIBLE);
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar.setDuration(Snackbar.LENGTH_SHORT);
            button.setVisibility(View.GONE);
        }
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(login, bndlanimation);
                finish();
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        String s = getResources().getString(R.string.emailalreadyuse1);
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 0, 5, 0);
        textViewTop.setText(message);
        textViewTop.setTextColor(getResources().getColor(R.color.text_light_shadow));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.title_text_color));
        snackbar.show();
        System.out.println("Snack bar ended");

    }

    /**
     * Method to check permissions
     *
     * @param permission
     */

    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);

        }
    }

    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog("Alert", message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * To check call permissions
     */

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }
}

