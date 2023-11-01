package com.makent.trioangle.autocomplete;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.util.CommonMethods;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class PlacesAutoComplete implements GoogleMapPlaceSearchAutoCompleteRecyclerView.AutoCompleteAddressTouchListener{

    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;

    Context context;
    RecyclerView recyclerView;
    protected boolean isInternetAvailable;

    public AlertDialog dialog;
    EditText editText;

    PlacesClient placesClient;
    AutocompleteSessionToken googleAutoCompleteToken;
    GoogleMapPlaceSearchAutoCompleteRecyclerView googleMapPlaceSearchAutoCompleteRecyclerView;

    private List<AutocompletePrediction> addressAutoCompletePredictions = new ArrayList<>() ;
    private String oldstring="";
    private int counti=0;
    private String searchlocation;

    private onGetLocation getLocation;
    private String getAddress;
    private String countrys;
    private String lat;
    private String log;
    private AndroidHttpClient ANDROID_HTTP_CLIENT=null;
    private LatLng getLocations=null;
    private String address;
    private List<Address> addressList;

    public PlacesAutoComplete() {
        AppController.getAppComponent().inject(this);
        initMapPlaceAPI();
    }

    public PlacesAutoComplete(Context context) {
        this.context=context;
        initMapPlaceAPI();
    }


    public void initRecyclerview(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView=recyclerView;
        dialog = commonMethods.getAlertDialog(context);
        ANDROID_HTTP_CLIENT= AndroidHttpClient.newInstance("PlaceAutoComplete");
        initMapPlaceAPI();
    }

    private void initMapPlaceAPI() {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getResources().getString(R.string.google_key));
        }
    }

    public void getAutoCompletePlaces(Context context, EditText editText){
        placesClient = Places.createClient(context);
        googleAutoCompleteToken = AutocompleteSessionToken.newInstance();
        googleMapPlaceSearchAutoCompleteRecyclerView = new GoogleMapPlaceSearchAutoCompleteRecyclerView(addressAutoCompletePredictions, context, this);
        this.editText=editText;
        editText.addTextChangedListener(new NameTextWatcher(editText));
    }

    @Override
    public void selectedAddress(AutocompletePrediction autocompletePrediction) {
        if (counti > 0) {
            autocompletePrediction = null;
        }
        if (autocompletePrediction != null) {
            counti++;
            System.out.println("Autocomplete item selected: "+autocompletePrediction.getFullText(null));

            searchlocation = autocompletePrediction.getFullText(null).toString();

            getLocation.onSuccess(searchlocation,"","");
            oldstring = autocompletePrediction.getPrimaryText(null).toString();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            recyclerView.setVisibility(View.GONE);

            fetchLocation(searchlocation, "searchitemclick");

            System.out.println("Clicked "+autocompletePrediction.getPrimaryText(null));
            System.out.println("getPlaceById "+autocompletePrediction.getPlaceId());
        }
    }


    private class NameTextWatcher implements TextWatcher {

        private View view;

        private NameTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().equals("")) {
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().equals("")) {
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        public void afterTextChanged(final Editable s) {
            isInternetAvailable = commonMethods.isOnline(context);
            if (!isInternetAvailable) {
                commonMethods.showMessage(context, dialog, context.getResources().getString(R.string.network_failure));
            }

            if (view.hasFocus()) {
                if (!s.toString().equals("")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!oldstring.equals(s.toString())) {
                                oldstring = s.toString();
                                //mAutoCompleteAdapter.getFilter().filter(s.toString()); // Place search
                                getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(s.toString());
                                recyclerView.setVisibility(View.VISIBLE);
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
                            }


                        })
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        System.out.println("Place Not Found "+apiException.getStatusCode());
                    }
                    exception.printStackTrace();
                });
    }

    public void clearAddressAndHideRecyclerView(){
        googleMapPlaceSearchAutoCompleteRecyclerView.clearAddresses();
        recyclerView.setVisibility(View.GONE);
    }

    public interface onGetLocation{
        void onSuccess(String location, String lat, String lng);
    }

    public interface onClearLocation{
        void onClear(String location);
    }


    /**
     * Fetch Location from address if Geocode available get from geocode otherwise get location from google
     */
    public void fetchLocation(String addresss, final String type) {
        getAddress = addresss;

        new AsyncTask<Void, Void, String>() {
            String locations = null;


            @Override
            protected String doInBackground(Void... params) {

                if (Geocoder.isPresent()) // Check geo code available or not
                {
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> address;

                        // May throw an IOException
                        address = geocoder.getFromLocationName(getAddress, 5);
                        if (address == null) {
                            return null;
                        }
                        Address location = address.get(0);

                        countrys = address.get(0).getCountryName();

                        location.getLatitude();
                        location.getLongitude();

                        lat = String.valueOf(location.getLatitude());
                        log = String.valueOf(location.getLongitude());
                        locations = lat + "," + log;
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to throw "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (locations != null) // i.e., Geocoder succeed
                {
                    return locations;
                } else // i.e., Geocoder failed
                {
                    return fetchLocationUsingGoogleMap(); // If geocode not available or location null call google API
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchLocationUsingGoogleMap() {
                getAddress = getAddress.replaceAll(" ", "%20");
                String googleMapUrl = "https://maps.google.com/maps/api/geocode/json?address=" + getAddress + "&sensor=false"
                        +"&key=" + context.getResources().getString(R.string.google_key);

                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    if (googleMapResponse.length() > 0) {
                        String longitute = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lng");

                        String latitude = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lat");

                        int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                .getJSONArray("address_components").length();
                        for (int i = 0; i < len; i++) {
                            if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                    .getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country")) {
                                countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0)
                                        .getJSONArray("address_components").getJSONObject(i).getString("long_name");

                            }
                        }

                        return latitude + "," + longitute;
                    } else {
                        return null;
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String location) {
                if (location != null) {
                    String[] parts = location.split(",");
                    Double lat = Double.valueOf(parts[0]);
                    Double lng = Double.valueOf(parts[1]);
                    LatLng latLng = new LatLng(lat, lng);

                    if ("searchitemclick".equals(type)) {
                        if ("".equals(countrys))
                        searchItemClick(latLng, countrys);
                    }
                } else {
                    commonMethods.showMessage(context, dialog, "Unable to get location please try again...");
                    commonMethods.hideProgressDialog();
                }
            }

            ;
        }.execute();
    }
    /**
     * Place search item click
     */
    public void searchItemClick(LatLng cur_Latlng, String country) {
        final LatLng ll = new LatLng(1, 1);
        getLocation.onSuccess(address,String.valueOf(cur_Latlng.latitude),String.valueOf(cur_Latlng.longitude));
        counti = 0;
    }
}
