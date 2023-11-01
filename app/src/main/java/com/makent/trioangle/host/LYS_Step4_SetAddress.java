package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step4_SetAddressActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Dialog;
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
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.makent.trioangle.R;
import com.makent.trioangle.autocomplete.GoogleMapPlaceSearchAutoCompleteRecyclerView;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.AppLocationService;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.placesearch.PlacesAutoCompleteAdapter;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

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

/* ***********************************************************************
This is List Your Space Step4  Contain Set Address
**************************************************************************  */
public class LYS_Step4_SetAddress extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,ServiceListener,GoogleMapPlaceSearchAutoCompleteRecyclerView.AutoCompleteAddressTouchListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    RunTimePermission runTimePermission;

    ImageView setAddress_back,setaddress_close,setaddress_gps_image;
    Boolean isgps=false;
    TextView dont_see_address,step4_map_marker;
    EditText search;
    RelativeLayout step4_location_next;
    LatLng cur_Latlng;
    boolean mapmove=true;
    LocalSharedPreferences localSharedPreferences;
    String searchlocation;

    protected GoogleApiClient mGoogleApiClient;
    GoogleMap googleMap;

    AppLocationService appLocationService;

    String Listlat,Listlong;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    String roomaddress,userid,roomid,roomlat,roomlong,latitude,longitude;

    ImageView location_dot_loader;
    TextView step4_location_next_txt;
    String state,steetline="",country,googlekeyone;
    String zip;
    String city,valueresult;
    double lat;
    double logt;


    final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(LYS_Location.class.getName());
    List<Address> addressList = null;
    String address=null;
    String countrys=null;
    LatLng getLocations=null;
    String getAddress=null;
    String lats,logs;
    Dialog_loading mydialog;

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
        setContentView(R.layout.activity_lys_step4_set_address);
        commonMethods =new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        localSharedPreferences=new LocalSharedPreferences(this);
        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        roomaddress=localSharedPreferences.getSharedPreferences(Constants.ListRoomAddress);
        roomaddress=localSharedPreferences.getSharedPreferences(Constants.ListRoomAddress);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        step4_map_marker= (TextView) findViewById(R.id.step4_map_marker);
        step4_location_next_txt= (TextView) findViewById(R.id.step4_location_next_txt);

        location_dot_loader = (ImageView) findViewById(R.id.location_dot_loader);

        location_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(location_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        googlekeyone= "AIzaSyDJ6wbuMgKlCG4hCFlYBuKha4AeV7PaxSs";

        appLocationService = new AppLocationService(this);
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


        setAddress_back=(ImageView)findViewById(R.id.setAddress_back);
        commonMethods.rotateArrow(setAddress_back,this);
        setAddress_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        dont_see_address=(TextView) findViewById(R.id.dont_see_address);
        dont_see_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {


                /*Intent addressdetails=new Intent(getApplicationContext(),LYS_Step4_AddressDetails.class);
                startActivity(addressdetails);*/
            }
        });


       /* step4_location_next_txt.setOnClickListener(new View.OnClickListener() {
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
        });*/

        search=(EditText) findViewById(R.id.search);
        setaddress_close=(ImageView) findViewById(R.id.setaddress_close);
        setaddress_gps_image=(ImageView) findViewById(R.id.setaddress_gps_image);
        step4_location_next=(RelativeLayout) findViewById(R.id.step4_location_next);
        step4_location_next.setEnabled(false);
        step4_location_next.setVisibility(View.GONE);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str= search.getText().toString().trim();
                if (!str.equals("")){
                    /*cur_Latlng= getLocationFromAddress(LYS_Step4_SetAddress.this,str);
                    Intent x = new Intent(getApplicationContext(),LYS_Step4_AddressDetails.class);
                    startActivity(x);*/
                    if(!mydialog.isShowing())
                    {
                        mydialog.show();
                    }
                    fetchLocation(str,"send");


                }
            }
        });

        setaddress_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                search.setText("");
                mapmove = false;
                search.addTextChangedListener(PlaceTextWatcher);
            }
        });
        setaddress_gps_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                checkAllPermission(Constants.PERMISSIONS_LOCATION);
            }
        });

        step4_location_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                // String address=location_edit.getText().toString();

                if(Listlat!=null&&Listlong!=null) {

                    sendLocation(); // this is used to update location call api function
                }else
                {
                    onBackPressed();
                    finish();
                }
            }
        });

        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.locationsearch_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null,mRecyclerView);

        mRecyclerView=(RecyclerView)findViewById(R.id.location_placesearch);
        mapmove=true;

        placesClient = com.google.android.libraries.places.api.Places.createClient(LYS_Step4_SetAddress.this);
        googleAutoCompleteToken = AutocompleteSessionToken.newInstance();
        googleMapPlaceSearchAutoCompleteRecyclerView = new GoogleMapPlaceSearchAutoCompleteRecyclerView(addressAutoCompletePredictions, this, this);

        search.addTextChangedListener(PlaceTextWatcher);

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(googleMapPlaceSearchAutoCompleteRecyclerView);

        /*search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mapmove = false;
                search.addTextChangedListener(PlaceTextWatcher);
                return false;
            }
        });*/

        /*mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        searchlocation=(String)item.description;
                        mRecyclerView.setVisibility(View.GONE);
                        search.removeTextChangedListener(PlaceTextWatcher);
                        String aaddress= (String) item.description;
                        if(aaddress!=null) {
                            aaddress = aaddress.replaceAll("null", "");
                            search.setText(aaddress);
                        }
                        mRecyclerView.setVisibility(View.GONE);
                        mapmove=true;

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);



                        *//*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         *//*

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    if(!myDialog.isShowing())
                                    {
                                        myDialog.show();
                                    }
                                    fetchLocation(searchlocation,"search");
                                    //cur_Latlng= getLocationFromAddress(LYS_Step4_SetAddress.this,searchlocation);
                                    *//*Listlat=String.valueOf(cur_Latlng.latitude);
                                    Listlong=String.valueOf(cur_Latlng.longitude);

                                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = gcd.getFromLocation(cur_Latlng.latitude, cur_Latlng.longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (addresses.size() > 0)
                                    {
                                        localSharedPreferences.saveSharedPreferences(Constants.City,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.State,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);

                                        System.out.println(addresses.get(0).getLocality());
                                        city= addresses.get(0).getLocality();
                                        zip=addresses.get(0).getPostalCode();
                                        state=addresses.get(0).getAdminArea();
                                        lat=addresses.get(0).getLatitude();
                                        logt=addresses.get(0).getLongitude();
                                        steetline=addresses.get(0).getThoroughfare();
                                        country=addresses.get(0).getCountryName();
                                       System.out.print(addresses);

                                        localSharedPreferences.saveSharedPreferences(Constants.City,city);
                                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,country);
                                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,steetline);
                                        localSharedPreferences.saveSharedPreferences(Constants.State,state);
                                        localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);
                                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                                        localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(lat));
                                        localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(logt));

                                        Intent x =new Intent(getApplicationContext(),LYS_Step4_AddressDetails.class);
                                        startActivity(x);
                                    }
                                    else
                                    {
                                        // do your staff
                                    }

                                    step4_location_next.setEnabled(true);
                                    //LatLng cur_Latlng=new LatLng(21.0000,78.0000); // giving your marker to zoom to your location area.
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));*//*

                                    //Do the things here on Click.....
                                    //  Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                }else {
                                    // Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                       // mapmove=true;
                    }
                })
        );*/


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
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        //Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        valueresult=localSharedPreferences.getSharedPreferences(Constants.Value);

        if (valueresult.equals("1")){
            dialog();
        }else if (valueresult.equals("2")){

        }
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
        initilizeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LYS_Step4_SetAddress.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        LYS_Step4_SetAddress.this.startActivity(intent);
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

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.step4_map);
            mapFragment.getMapAsync(this);

            //  googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mainmap)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                //  Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }



        }else
        {

        }
    }
    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;



        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //if(mapmove) {
                mRecyclerView.setVisibility(View.GONE);
                    search.removeTextChangedListener(PlaceTextWatcher);
                   // String address = getCompleteAddressString(cameraPosition.target.latitude, cameraPosition.target.longitude);
                //localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("1"));
                fetchAddress(cameraPosition.target,"mapmove");
                    /*if(address.equals(""))
                    {
                        step4_location_next.setEnabled(false);
                    }else {
                        valueresult=localSharedPreferences.getSharedPreferences(Constants.Value);

                        if (valueresult.equals("1")){
                            step4_location_next.setEnabled(true);
                            step4_location_next.setVisibility(View.VISIBLE);

                            search.setText(address);
                            Listlat=String.valueOf(cameraPosition.target.latitude);
                            Listlong=String.valueOf(cameraPosition.target.longitude);
                            Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));
                            Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                        }

                    }

                    search.addTextChangedListener(PlaceTextWatcher);
                mapmove=true;*/
               // }
            }
        });



         if(roomaddress!=null||!roomaddress.equals(""))
        //if(roomaddress!=null)
        {
            roomaddress=roomaddress.replaceAll("null","");
            search.setText(roomaddress);
            String loclat=localSharedPreferences.getSharedPreferences(Constants.ListLocationLat);
            String loclong=localSharedPreferences.getSharedPreferences(Constants.ListLocationLong);
            cur_Latlng=new LatLng(Double.valueOf(loclat),Double.valueOf(loclong));
            System.out.println("cur_Latlng"+cur_Latlng);
            //roomlat=String.valueOf(cur_Latlng.latitude);
            //roomlong=String.valueOf(cur_Latlng.longitude);
            step4_location_next.setEnabled(true);
            //LatLng cur_Latlng=new LatLng(21.0000,78.0000); // giving your marker to zoom to your location area.
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            step4_map_marker.setVisibility(View.GONE);

            googleMap.addMarker(new MarkerOptions().position(cur_Latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
            mapmove=false;
        }




    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context,Locale.getDefault());
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
            city= address.get(0).getLocality();
            zip=address.get(0).getPostalCode();
            state=address.get(0).getAdminArea();
            lat=address.get(0).getLatitude();
            logt=address.get(0).getLongitude();

            localSharedPreferences.saveSharedPreferences(Constants.City,city);
            localSharedPreferences.saveSharedPreferences(Constants.State,state);
            localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);
            localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(lat));
            localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(logt));

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

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
                if(city!=null)
                {
                    strAdd=city+" ";
                }

                if(state!=null)
                {
                    strAdd=strAdd+state+" ";
                }
                if(country!=null)
                {
                    strAdd=strAdd+country+" ";
                }

                // strAdd=city+" "+state+" "+country;
                // String postalCode = addresses.get(0).getPostalCode();
                // String knownName = addresses.get(0).getFeatureName();
                //   Log.w("My Current loction address", "" + strReturnedAddress.toString());
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
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                // TODO: do what you need here (refresh list)
                                // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Toast.makeText(getApplicationContext(), "Hello "+s.toString(), Toast.LENGTH_SHORT).show();
                                        //if(s.toString().length()>0&&!mapmove) {
                                        if(s.toString().length()>0) {
                                            oldstring = s.toString();
                                            getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(s.toString());
                                            mRecyclerView.setVisibility(View.VISIBLE);
                                            mapmove = false;
                                        }
                                    }
                                });

                            }
                        },
                        DELAY
                );
                // location_next.setEnabled(true);
            }else if(!mGoogleApiClient.isConnected()){
                //   Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                Log.w(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                step4_location_next.setEnabled(false);
            }

            if(s.toString().equals(""))
            {
                step4_location_next.setEnabled(false);
                mRecyclerView.setVisibility(View.GONE);
                clearAddressAndHideRecyclerView();
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress,(String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "location_name", String.class)); //response.getString("location_name"));
            localSharedPreferences.saveSharedPreferences(Constants.ListLocationLat, Listlat);
            localSharedPreferences.saveSharedPreferences(Constants.ListLocationLong, Listlong);
            step4_location_next_txt.setVisibility(View.GONE);
            location_dot_loader.setVisibility(View.VISIBLE);
            finish();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            step4_location_next_txt.setVisibility(View.VISIBLE);
            location_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,search,search,getResources(),LYS_Step4_SetAddress.this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    public void sendLocation(){
        apiService.updateLocation(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,Listlat,Listlong,"","","","","","","No").enqueue(new RequestCallback(this));
    }


    public void dialog()
    {
        final Dialog dialog = new Dialog(LYS_Step4_SetAddress.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);
        // set the custom dialog components - text, ivPhoto and button

        TextView logout_msg=(TextView)dialog.findViewById(R.id.logout_msg);
        TextView logout_tiltle=(TextView)dialog.findViewById(R.id.logout_tiltle);
        if(localSharedPreferences.getSharedPreferencesBool(Constants.IsLocationFound))
            logout_tiltle.setText(getResources().getString(R.string.tiltledialog1));
        else
            logout_tiltle.setText(getResources().getString(R.string.tiltledialog));

        logout_tiltle.setPadding(0,0,0,50);
        logout_msg.setGravity(Gravity.CENTER | Gravity.BOTTOM);

        logout_tiltle.setVisibility(View.VISIBLE);
        logout_msg.setText(R.string.tiltledialogtwo);

        Button dialogButton = (Button) dialog.findViewById(R.id.logout_cancel);
        dialogButton.setText(getResources().getString(R.string.editaddress));
        dialogButton.setPadding(0,0,20,0);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setVisibility(View.GONE);
                commonMethods.hideProgressDialog();
                Intent addressdetails=new Intent(getApplicationContext(),LYS_Step4_AddressDetails.class);
                startActivity(addressdetails);
                dialog.dismiss();
            }
        });

        Button dialogButton1 = (Button) dialog.findViewById(R.id.logout_ok);
        dialogButton1.setText(R.string.pin);
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(googleMap!=null)
                googleMap.clear();
                step4_map_marker.setVisibility(View.VISIBLE);

                String latd=localSharedPreferences.getSharedPreferences(Constants.Lat);
                String longd=localSharedPreferences.getSharedPreferences(Constants.Logt);
                LatLng latLng=new LatLng(Double.valueOf(latd),Double.valueOf(longd));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
               // googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));

                localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
                localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
                localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                dialog.dismiss();
                // localSharedPreferences.clearSharedPreferences();
                // clearApplicationData();
            }
        });

        dialog.show();
    }


    public void fetchLocation(String addresss, final String type)
    {
        getAddress=addresss;

        new AsyncTask<Void, Void, String>()
        {
            String locations=null;
            protected void onPreExecute()
            {

            };

            @Override
            protected String doInBackground(Void... params)
            {

                if (Geocoder.isPresent())
                {
                    try
                    {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> address;

                        // May throw an IOException
                        address = geocoder.getFromLocationName(getAddress, 5);
                        if (address == null) {
                            return null;
                        }
                        Address location = address.get(0);

                        countrys=address.get(0).getCountryName();
                        System.out.println("1Chcek country countrys"+countrys);

                        location.getLatitude();
                        location.getLongitude();
                        localSharedPreferences.saveSharedPreferences(Constants.City,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
                        localSharedPreferences.saveSharedPreferences(Constants.State,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);

                        city= address.get(0).getLocality();
                        zip=address.get(0).getPostalCode();
                        state=address.get(0).getAdminArea();
                        lat=address.get(0).getLatitude();
                        logt=address.get(0).getLongitude();
                        steetline=address.get(0).getThoroughfare();
                        country=address.get(0).getCountryName();

                        localSharedPreferences.saveSharedPreferences(Constants.City,city);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,country);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,steetline);
                        localSharedPreferences.saveSharedPreferences(Constants.State,state);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);
                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(lat));
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(logt));

                        lats= String.valueOf(location.getLatitude());
                        logs= String.valueOf(location.getLongitude());
                        locations=lats+","+logs;
                       // locations=null;
                    }
                    catch (Exception ignored)
                    {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (locations != null) // i.e., Geocoder succeed
                {
                    System.out.println("LATLNG mobile" + locations);
                    return locations;
                }
                else // i.e., Geocoder failed
                {
                    return fetchLocationUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchLocationUsingGoogleMap()
            {
                getAddress=getAddress.replaceAll(" ","%20");
                String googleMapUrl="https://maps.google.com/maps/api/geocode/json?address=" + getAddress + "&sensor=false&key="+getResources().getString(R.string.google_key);
                System.out.println("Google Map Url "+googleMapUrl);
                try
                {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results



                    if(googleMapResponse.length()>0) {

                        localSharedPreferences.saveSharedPreferences(Constants.City,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
                        localSharedPreferences.saveSharedPreferences(Constants.State,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);
                        steetline="";

                        String longitute = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lng");

                        String latitude = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lat");

                        int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONArray("address_components").length();
                        for (int i = 0; i < len; i++) {

                            String types=((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0);
                            JSONObject getAddress=((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(i);
                            if (types.equals("country")) {
                                countrys = getAddress.getString("long_name");
                                country=countrys;
                                System.out.println("countrys " + countrys);
                            }else if (types.equals("premise")||types.equals("political")||types.equals("route")||type.equals("locality")) {
                                if(!getAddress.getString("long_name").equals("null")||!getAddress.getString("long_name").equals("Unnamed Road")) {
                                    if (steetline.equals(""))
                                    steetline = getAddress.getString("long_name");
                                else
                                    steetline += "," + getAddress.getString("long_name");
                            }

                                System.out.println("steetline " + steetline);
                            }else if (types.equals("locality")) {
                            //else if (types.equals("administrative_area_level_2")||types.equals("locality")) {
                                city = getAddress.getString("long_name");
                                System.out.println("city " + city);
                            }else if (types.equals("administrative_area_level_1")) {
                                state = getAddress.getString("long_name");
                                System.out.println("state " + state);
                            }else if (types.equals("postal_code")) {
                                zip = getAddress.getString("long_name");
                                System.out.println("zip " + zip);
                            }

                        }

                        System.out.println("City "+city+" state "+state+" country "+country+" Street "+steetline);
                        localSharedPreferences.saveSharedPreferences(Constants.City,city);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname,country);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline,steetline);
                        localSharedPreferences.saveSharedPreferences(Constants.State,state);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);
                        localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(lat));
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(logt));
                            /*countrys = ((JSONArray)googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(3).getString("long_name");*/
                        System.out.println("LATLNG google" + latitude + "," + longitute);
                        return latitude + "," + longitute;
                    }else{
                        return null;
                    }

                }
                catch (Exception ignored)
                {
                    ignored.printStackTrace();
                }
                return null;
            }


            protected void onPostExecute(String location)
            {
                if (location != null)
                {
                    String[] parts = location.split(",");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                    Double lat=Double.valueOf(parts[0]);
                    Double lng=Double.valueOf(parts[1]);
                    LatLng latLng =new LatLng(lat,lng);

                    if(type.equals("search")) {

                        Listlat=String.valueOf(latLng.latitude);
                        Listlong=String.valueOf(latLng.longitude);

                        step4_location_next.setEnabled(true);
                        //LatLng cur_Latlng=new LatLng(21.0000,78.0000); // giving your marker to zoom to your location area.
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        if(Geocoder.isPresent()) {
                        if(mydialog.isShowing())
                            mydialog.dismiss();

                        mRecyclerView.setVisibility(View.GONE);
                            commonMethods.hideProgressDialog();
                        Intent x =new Intent(getApplicationContext(),LYS_Step4_AddressDetails.class);
                        startActivity(x);
                        }else{
                           // fetchAddressUsingGoogleMap(latLng);
                        }
                    }else if(type.equals("send"))
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        commonMethods.hideProgressDialog();
                        Intent x = new Intent(getApplicationContext(), LYS_Step4_AddressDetails.class);
                        startActivity(x);
                        if (mydialog.isShowing())
                            mydialog.dismiss();
                        /*if(Geocoder.isPresent()) {
                            System.out.println(" GeoCoder Present");
                        }else{
                            System.out.println(" GeoCoder Not Present");
                            fetchAddressUsingGoogleMap(latLng);
                        }*/
                    }else if(type.equals("searchitemclick"))
                    {

                    }
                }else
                {
                    commonMethods.snackBar(getResources().getString(R.string.unable_get_location),"",false,2,search,search,getResources(),LYS_Step4_SetAddress.this);
                    if(mydialog.isShowing())
                        mydialog.dismiss();
                }
            };
        }.execute();
    }

    public void fetchAddress(LatLng location, final String type)
    {
        getLocations=location;
        address=null;
        new AsyncTask<Void, Void, String>()
        {

            protected void onPreExecute()
            {

            };

            @Override
            protected String doInBackground(Void... params)
            {

                if (Geocoder.isPresent())
                {
                    try
                    {

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(getLocations.latitude, getLocations.longitude, 1);
                        if (addresses != null) {
                            countrys = addresses.get(0).getCountryName();

                            String adress0 = addresses.get(0).getAddressLine(0);
                            String adress1 = addresses.get(0).getAddressLine(1);

                            System.out.println("GeoCode Address 0 "+adress0+" \n Address 1 "+adress1);
                            address = adress0 + " " + adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            address=address.replaceAll("null","");
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            /*if (address != null) {
                                //   pickupaddresss=address;
                                return address;
                            }*/
                        }
                    }
                    catch (Exception ignored)
                    {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }
                if (address != null) // i.e., Geocoder succeed
                {
                    return address;
                }
                else // i.e., Geocoder failed
                {
                    return fetchAddressUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchAddressUsingGoogleMap()
            {

                addressList = new ArrayList<Address>();
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + getLocations.latitude + ","
                        + getLocations.longitude + "&sensor=false&key="+getResources().getString(R.string.google_key);

                try
                {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    for (int i = 0; i < results.length(); i++) {


                        JSONObject result = results.getJSONObject(i);


                        String indiStr = result.getString("formatted_address");


                        Address addr = new Address(Locale.getDefault());


                        addr.setAddressLine(0, indiStr);
                        //  country=addr.getCountryName();

                        addressList.add(addr);


                    }

                    int len= ((JSONArray)googleMapResponse.get("results")).getJSONObject(0)
                            .getJSONArray("address_components").length();
                    for(int i=0;i<len;i++) {
                        if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country"))
                        {
                            countrys = ((JSONArray)googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(i).getString("long_name");
                            System.out.println("countrys " + countrys);
                        }
                    }

                    if (addressList != null) {

                        String adress0= addressList.get(0).getAddressLine(0);
                        String adress1=addressList.get(0).getAddressLine(1);
                        System.out.println("Google Address 0 "+adress0+" \n Address 1 "+adress1);
                        address = adress0;//+" "+adress1;
                        //address = adress0+" "+adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        address=address.replaceAll("null","");

                        if (address != null) {
                            return address;
                        }
                    }

                }
                catch (Exception ignored)
                {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String address)
            {
                if (address != null)
                {
                    System.out.println("1 valueresult "+valueresult +" Address "+address);
                    if(type.equals("mapmove"))
                    {
                        System.out.println("2 valueresult "+valueresult +" Address "+address);
                        if(address.equals(""))
                        {
                            step4_location_next.setEnabled(false);
                        }else {
                            valueresult=localSharedPreferences.getSharedPreferences(Constants.Value);

                            System.out.println("3 valueresult "+valueresult +" Address "+address);
                            if (valueresult.equals("1")){
                                isgps=false;
                                step4_location_next.setEnabled(true);
                                step4_location_next.setVisibility(View.VISIBLE);

                                address=address.replaceAll("null","");
                                search.setText(address);
                                Listlat=String.valueOf(location.latitude);
                                Listlong=String.valueOf(location.longitude);
                                Log.i("centerLat", String.valueOf(location.latitude));
                                Log.i("centerLong", String.valueOf(location.longitude));
                            }else if(isgps)
                            {
                                isgps=false;

                                address=address.replaceAll("null","");
                                search.setText(address);
                                Listlat=String.valueOf(location.latitude);
                                Listlong=String.valueOf(location.longitude);
                                Log.i("centerLat", String.valueOf(location.latitude));
                                Log.i("centerLong", String.valueOf(location.longitude));
                            }

                        }

                        //search.addTextChangedListener(PlaceTextWatcher);
                        mapmove=true;

                    }else if(type.equals("country")){
                    }
                }
            };
        }.execute();
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
        }else {
            getLocation();
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

        }else {
            getLocation();
        }
    }

    private void getLocation() {
        Location location = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        if(location==null)
        {
            location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        }

        System.out.println("location"+location);

        if (location != null) {
            mapmove=true;
            isgps=true;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            step4_location_next.setEnabled(true);
            Listlat=String.valueOf(latitude);
            Listlong=String.valueOf(longitude);
            LatLng cur_Latlng= new LatLng(latitude,longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                   /* LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getApplicationContext(), new GeocoderHandler());*/
        } else {
            showSettingsAlert();
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

    @Override
    public void selectedAddress(AutocompletePrediction autocompletePrediction) {
        if (counti > 0) {
            autocompletePrediction = null;
        }
        System.out.println("autocompletePrediction "+autocompletePrediction);
        if (autocompletePrediction != null) {
            counti++;

            searchlocation = autocompletePrediction.getFullText(null).toString();
            search.removeTextChangedListener(PlaceTextWatcher);
            if (searchlocation != null) {
                searchlocation = searchlocation.replaceAll("null", "");
                search.setText(searchlocation);
            }
            mRecyclerView.setVisibility(View.GONE);
            mapmove = true;

            search.setText(autocompletePrediction.getPrimaryText(null));
            oldstring = autocompletePrediction.getPrimaryText(null).toString();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

            if (!mydialog.isShowing()) {
                mydialog.show();
            }
            fetchLocation(searchlocation, "search");
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
    }
}
