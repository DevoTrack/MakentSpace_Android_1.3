package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step4_AddressDetailsActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.placesearch.PlacesAutoCompleteAdapter;
import com.makent.trioangle.placesearch.RecyclerItemClickListener;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is List Your Space Step4  Contain Address Details
**************************************************************************  */

public class LYS_Step4_AddressDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    RelativeLayout addressdetails_title,describe_title;
    EditText address_country,address_city,address_state,address_pin,address_street,address_apt;
    String userid,city,steetline,country,state,zip,lat,longt,roomIdsend,accesstokensend,latitudesend,longitudesend,CountryName,streetname,streetaddress,statusmessagetwo;
    public static android.app.AlertDialog alertDialogStorestwo;
    Dialog_loading mydialog;
    LocalSharedPreferences localSharedPreferences;
    ImageView describe_dot_loader;
    String contryname,streetline;
    String searchlocation,statetext;
    String fulladress,zipcode;
    protected boolean isInternetAvailable;
    boolean islistclick=false;
    boolean islocationfound=false;

    protected GoogleApiClient mGoogleApiClient;

    final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(LYS_Location.class.getName());
    String countrys=null;
    String getAddress=null;
    String latt,logt;


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_lys_step4_address_details);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        describe_title=(RelativeLayout) findViewById(R.id.describe_title);

        describe_dot_loader = (ImageView) findViewById(R.id.describe_dot_loader);
        describe_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(describe_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomIdsend=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        accesstokensend=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        city=localSharedPreferences.getSharedPreferences(Constants.City);
        state=localSharedPreferences.getSharedPreferences(Constants.State);
        zip=localSharedPreferences.getSharedPreferences(Constants.Zip);
        lat=localSharedPreferences.getSharedPreferences(Constants.Lat);
        longt=localSharedPreferences.getSharedPreferences(Constants.Logt);
        contryname=localSharedPreferences.getSharedPreferences(Constants.Countyname);
        streetline=localSharedPreferences.getSharedPreferences(Constants.Streetline);
        streetaddress=localSharedPreferences.getSharedPreferences(Constants.Building);


        addressdetails_title=(RelativeLayout) findViewById(R.id.addressdetails_title);
        address_country=(EditText)findViewById(R.id.address_country);
        address_city=(EditText)findViewById(R.id.address_city);
        address_state=(EditText)findViewById(R.id.address_state);
        address_pin=(EditText)findViewById(R.id.address_pin);
        address_street=(EditText)findViewById(R.id.address_street);
        address_apt=(EditText)findViewById(R.id.address_apt);

        if(city!=null) {
            city = city.replaceAll("null", "");
            address_city.setText(city);
        }else{
            address_city.setText("");
        }
        if(state==null)
        {
            address_state.setText(city);
        }else {
            state=state.replaceAll("null","");
            address_state.setText(state);
        }

        if(zip!=null) {
            zip = zip.replaceAll("null", "");
            address_pin.setText(zip);
        }else
        {
            address_pin.setText("");
        }

        if(contryname!=null) {
            contryname = contryname.replaceAll("null", "");
            address_country.setText(contryname);
        }else
        {
            address_country.setText("");
        }

        if(streetline!=null) {
            streetline = streetline.replaceAll("null", "");
            address_street.setText(streetline);
        }else
        {
            address_street.setText("");
        }

        if(streetaddress!=null)
        {
            streetaddress=streetaddress.replaceAll("null","");
            address_apt.setText(streetaddress);
        }else
        {
            address_apt.setText("");
        }

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        address_street.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                address_street.addTextChangedListener(PlaceTextWatcher);
                islistclick=true;
                return false;
            }
        });

        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.locationsearch_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null,mRecyclerView);

        mRecyclerView=(RecyclerView)findViewById(R.id.location_placesearch);
        mRecyclerView.setVisibility(View.GONE);

        mLinearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);

                        if (item != null) {

                            final String placeId = String.valueOf(item.placeId);
                            Log.i("TAG", "Autocomplete item selected: " + item.description);
                            searchlocation = (String) item.description;
                            mRecyclerView.setVisibility(View.GONE);
                            address_street.removeTextChangedListener(PlaceTextWatcher);
                            islistclick=false;
                            //address_street.setText(item.description);
                            mRecyclerView.setVisibility(View.GONE);


                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(address_street.getWindowToken(), 0);



                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getCount() == 1) {
                                        if (!mydialog.isShowing()) {
                                            mydialog.show();
                                        }
                                        address_street.removeTextChangedListener(PlaceTextWatcher);
                                        islistclick=false;
                                        mRecyclerView.setVisibility(View.GONE);
                                        islocationfound=false;
                                        fetchLocation(searchlocation, "send");
                                    } else {
                                        // Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Log.i("TAG", "Clicked: " + item.description);
                            Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                        }
                    }
                })
        );

        addressdetails_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    streetname = address_street.getText().toString().trim();
                    streetaddress = address_apt.getText().toString().trim();
                    zipcode = address_pin.getText().toString().trim();
                    CountryName = address_country.getText().toString().trim();
                    statetext = address_state.getText().toString();
                    searchlocation = address_city.getText().toString();

//                if (!streetaddress.equals("")&&!CountryName.equals("")&&!streetname.equals("")&&!statetext.equals("")&&!zipcode.equals("")&&!searchlocation.equals("")){

                    if (!CountryName.equals("") && !statetext.equals("") && !searchlocation.equals("")) {
                        fulladress = streetname + "," + searchlocation + "," + statetext + "," + CountryName;

                        //LatLng cur_Latlng = getGeoCoordsFromAddress(LYS_Step4_AddressDetails.this, searchlocation);
                        if(!mydialog.isShowing())
                            mydialog.show();
                        localSharedPreferences.saveSharedPreferences(Constants.IsLocationFound,islocationfound);
                        fetchLocation(fulladress,"search");
                        //new SendLocation().execute(); //this is used for send the location field

                    } else {
                        if (searchlocation.equals("")) {
                            commonMethods.snackBar(getResources().getString(R.string.invalid_city),"",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
                        } else if (statetext.equals("")) {
                            commonMethods.snackBar(getResources().getString(R.string.invalid_state),"",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
                            //snackBar(getResources().getString(R.string.invalid_state));
                        } else if (CountryName.equals("")) {
                            commonMethods.snackBar(getResources().getString(R.string.invalid_country),"",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
                            //snackBar(getResources().getString(R.string.invalid_country));
                        }
                    }
                }else {
                    commonMethods.snackBar(getResources().getString(R.string.Interneterror),"",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
                  //  snackBar(getResources().getString(R.string.Interneterror));
                }
            }
        });

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomAddress,(String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "location_name", String.class)); //response.getString("location_name"));
            describe_title.setVisibility(View.GONE);
            describe_dot_loader.setVisibility(View.VISIBLE);
            localSharedPreferences.saveSharedPreferences(Constants.Value, String.valueOf("1"));
            onBackPressed();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            describe_title.setVisibility(View.VISIBLE);
            describe_dot_loader.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
            //snackBar(jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public void sendLocation(){
        apiService.updateLocation(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomIdsend,lat,longt,streetname,streetaddress,searchlocation,statetext,zipcode,CountryName,"Yes").enqueue(new RequestCallback(this));
    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
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

                            String adress0= address.get(0).getAddressLine(0);
                            String adress1=address.get(0).getAddressLine(1);
                            System.out.println("Fetch Location Google Address 0 "+adress0+" \n Address 1 "+adress1);

                            localSharedPreferences.saveSharedPreferences(Constants.City,null);
                            localSharedPreferences.saveSharedPreferences(Constants.Countyname,null);
                            localSharedPreferences.saveSharedPreferences(Constants.Streetline,null);
                            localSharedPreferences.saveSharedPreferences(Constants.State,null);
                            localSharedPreferences.saveSharedPreferences(Constants.Zip,null);
                            localSharedPreferences.saveSharedPreferences(Constants.Building,null);
                            localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
                            localSharedPreferences.saveSharedPreferences(Constants.Logt, null);
                            city="";country="";steetline="";state="";zip="";

                            city= address.get(0).getLocality();
                            zip=address.get(0).getPostalCode();
                            state=address.get(0).getAdminArea();
                            steetline=address.get(0).getThoroughfare();
                            country=address.get(0).getCountryName();

                            System.out.println("City "+city+" state "+state+" country "+country+" Street "+steetline);
                            localSharedPreferences.saveSharedPreferences(Constants.City,city);
                            localSharedPreferences.saveSharedPreferences(Constants.Countyname,country);
                            localSharedPreferences.saveSharedPreferences(Constants.Streetline,steetline);
                            localSharedPreferences.saveSharedPreferences(Constants.State,state);
                            localSharedPreferences.saveSharedPreferences(Constants.Zip,zip);
                            localSharedPreferences.saveSharedPreferences(Constants.Building,null);



                        Address location = address.get(0);

                        countrys=address.get(0).getCountryName();
                        System.out.println("1Chcek country countrys"+countrys);

                        location.getLatitude();
                        location.getLongitude();

                        latt= String.valueOf(location.getLatitude());
                        logt= String.valueOf(location.getLongitude());
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(latt));
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(logt));
                        locations=latt+","+logt;
                        //locations=null;
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

                        localSharedPreferences.saveSharedPreferences(Constants.City, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline, null);
                        localSharedPreferences.saveSharedPreferences(Constants.State, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Building, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, null);
                    city="";country="";steetline="";state="";zip="";
                    steetline="";

                    if(googleMapResponse.length()>0) {
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
                            }else if (types.equals("premise")||types.equals("political")||types.equals("route")||types.equals("airport")||types.equals("street_number")||types.equals("street_address")) {
                                if (steetline.equals(""))
                                    steetline = getAddress.getString("long_name");
                                else
                                    steetline =steetline+"," + getAddress.getString("long_name");
                                System.out.println("steetline " + steetline);
                            }else if (types.equals("locality")) {
                            //else if (types.equals("administrative_area_level_2")||types.equals("locality")) {
                                if (city.equals(""))
                                    city = getAddress.getString("long_name");
                                else
                                    if(!city.contains(getAddress.getString("long_name")))
                                    city =city+"," + getAddress.getString("long_name");
                                //city = getAddress.getString("long_name");
                                System.out.println("city " + city);
                            }else if (types.equals("administrative_area_level_1")) {
                                state = getAddress.getString("long_name");
                                System.out.println("state " + state);
                            }else if (types.equals("postal_code")) {
                                zip = getAddress.getString("long_name");
                                System.out.println("zip " + zip);
                            }

                        }

                        int len0=((JSONArray) googleMapResponse.get("results")).getJSONObject(0).length();
                        System.out.println("lenth of "+len0);
                        int len1 = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONArray("types").length();
                        System.out.println("lenth of "+len1);
                        for (int i = 0; i < len1; i++) {

                            String types=((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("types").getString(i);
                            if (types.equals("premise")||types.equals("street_address"))
                            {
                                islocationfound=true;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.City, city);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname, country);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline, steetline);
                        localSharedPreferences.saveSharedPreferences(Constants.State, state);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip, zip);
                        localSharedPreferences.saveSharedPreferences(Constants.Building, null);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, latitude);
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, longitute);
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
                    Double lats=Double.valueOf(parts[0]);
                    Double lng=Double.valueOf(parts[1]);
                    LatLng latLng =new LatLng(lats,lng);

                    if(type.equals("search")) {
                        lat = String.valueOf(latLng.latitude);
                        longt = String.valueOf(latLng.longitude);
                        localSharedPreferences.saveSharedPreferences(Constants.Lat, String.valueOf(lat));
                        localSharedPreferences.saveSharedPreferences(Constants.Logt, String.valueOf(longt));

                       /* localSharedPreferences.saveSharedPreferences(Constants.City, searchlocation);
                        localSharedPreferences.saveSharedPreferences(Constants.Countyname, CountryName);
                        localSharedPreferences.saveSharedPreferences(Constants.Streetline, streetname);
                        localSharedPreferences.saveSharedPreferences(Constants.State, statetext);
                        localSharedPreferences.saveSharedPreferences(Constants.Building, streetaddress);
                        localSharedPreferences.saveSharedPreferences(Constants.Zip, zipcode);*/

                        sendLocation(); //this is used for send the location field
                        //new SendLocation().execute();
                        if(mydialog.isShowing())
                            mydialog.dismiss();
                    }else if(type.equals("send"))
                    {
                        mRecyclerView.setVisibility(View.GONE);
                        address_street.removeTextChangedListener(PlaceTextWatcher);
                        islistclick=false;

                        city=localSharedPreferences.getSharedPreferences(Constants.City);
                        state=localSharedPreferences.getSharedPreferences(Constants.State);
                        zip=localSharedPreferences.getSharedPreferences(Constants.Zip);
                        lat=localSharedPreferences.getSharedPreferences(Constants.Lat);
                        longt=localSharedPreferences.getSharedPreferences(Constants.Logt);
                        contryname=localSharedPreferences.getSharedPreferences(Constants.Countyname);
                        streetline=localSharedPreferences.getSharedPreferences(Constants.Streetline);
                        streetaddress=localSharedPreferences.getSharedPreferences(Constants.Building);

                        if(city!=null) {
                            city = city.replaceAll("null", "");
                            address_city.setText(city);
                        }else
                        {
                            address_city.setText("");
                        }
                        if(state==null)
                        {
                            address_state.setText(city);
                        }else {
                            state=state.replaceAll("null","");
                            address_state.setText(state);
                        }

                        if(zip!=null) {
                            zip = zip.replaceAll("null", "");
                            address_pin.setText(zip);
                        }else
                        {
                            address_pin.setText("");
                        }

                        if(contryname!=null) {
                            contryname = contryname.replaceAll("null", "");
                            address_country.setText(contryname);
                        }else
                        {
                            address_country.setText("");
                        }

                        if(streetline!=null) {
                            streetline = streetline.replaceAll("null", "");
                            address_street.setText(streetline);
                        }else
                        {
                            address_street.setText("");
                        }

                        if(streetaddress!=null)
                        {
                            streetaddress=streetaddress.replaceAll("null","");
                            address_apt.setText(streetaddress);
                        }else
                        {
                            address_apt.setText("");
                        }

                        if(mydialog.isShowing())
                            mydialog.dismiss();
                    }else if(type.equals("searchitemclick"))
                    {

                    }
                }else
                {
                    commonMethods.snackBar("Unable to get location please try again...","",false,2,address_apt,address_apt,getResources(),LYS_Step4_AddressDetails.this);
                    //snackBar("Unable to get location please try again...");
                    if(mydialog.isShowing())
                        mydialog.dismiss();
                }
            };
        }.execute();
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
                                            mAutoCompleteAdapter.getFilter().filter(s.toString());
                                            if(islistclick)
                                            mRecyclerView.setVisibility(View.VISIBLE);
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
            }

            if(s.toString().equals(""))
            {
                mRecyclerView.setVisibility(View.GONE);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void afterTextChanged(Editable s) {
        }
    };

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

        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }
}
