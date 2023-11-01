package com.makent.trioangle.createspace

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.http.AndroidHttpClient
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonArray
import com.makent.trioangle.BaseActivity
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.LYS_Location
import com.makent.trioangle.travelling.HomeActivity
import com.makent.trioangle.util.CommonMethods
import kotlinx.android.synthetic.main.activity_create_space_map.*
import kotlinx.android.synthetic.main.address_layout_fragment.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CreateSpaceMapActivity : BaseActivity() , OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var commonMethods: CommonMethods

    internal var googleMap: GoogleMap? = null
    lateinit var latLng:LatLng
    var markerPoints : ArrayList<LatLng> = ArrayList(2)
    lateinit var rltSave:RelativeLayout

    internal var addressList: ArrayList<Address>? = null
    internal var address: String? = null
    internal var getAddress: String? = null
    internal val ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(CreateSpaceMapActivity::class.java.name)

    var mAddress1:String=""
    var mAddress2:String=""
    var mCity:String=""
    var mState:String=""
    var mPostal:String=""
    var mCountryCode:String=""
    var mCountryName:String=""
    var mlatitude:String=""
    var mlongitude:String=""

    var getFullAddress:String=""

    val localSharedPreferences:LocalSharedPreferences = LocalSharedPreferences(this)

    var addressModel:AddressModel=AddressModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_space_map)
        AppController.getAppComponent().inject(this)

        rltSave=findViewById(R.id.rl_save)
        markerPoints = this.intent.extras!!.getParcelableArrayList("latlng")!!
        latLng =  markerPoints.get(0)

        commonMethods.rotateArrow(iv_back,this)
        try {
            // Loading map
            initilizeMap()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        iv_back.setOnClickListener {
            onBackPressed()
        }

        rltSave.setOnClickListener {
            if (addressModel.mlatitude.isNotEmpty() && addressModel.mlongitude.isNotEmpty()) {
                intent.putExtra("addressModel", addressModel)
                intent.putExtra("getFullAddress", getFullAddress)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                commonMethods.snackBar(resources.getString(R.string.map_error),"",false,2,rltSave,resources,this)
            }
        }

    }

    override fun onStop() {
        super.onStop()
       // ANDROID_HTTP_CLIENT.close()
    }


    /**
     * function to load map. If map is not created it will create it for you
     */
    private fun initilizeMap() {
        if (googleMap == null) {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.search_map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            //  googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mainmap)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                //  Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map

        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))

        println("Map is OnReady ")
        googleMap!!.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener { cameraPosition ->
            latLng = cameraPosition.target
            fetchAddress(latLng,"moveonmap")

        })
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        ANDROID_HTTP_CLIENT.close()
    }
    override fun onDestroy() {
        super.onDestroy()
        ANDROID_HTTP_CLIENT.close()
    }*/

    fun fetchAddress(location: LatLng, type: String) {
        address = null
        object : AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {

            }

            override fun doInBackground(vararg params: Void): String? {

                if (Geocoder.isPresent()) {
                    try {

                        val geocoder = Geocoder(applicationContext, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses != null) {
                            val countrys = addresses[0].countryName

                            val adress0 = addresses[0].getAddressLine(0)
                            val adress1 = addresses[0].getAddressLine(1)


                            //mAddress1=addresses[0].getAddressLine(0)
                            mAddress1=addresses[0].thoroughfare.replace("null","")
                            address = "$adress0 $adress1" // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            mCity = addresses[0].locality.replace("null","")
                            mState = addresses[0].adminArea.replace("null","")
                            mPostal = addresses[0].postalCode.replace("null","")
                            mCountryName = addresses[0].countryName.replace("null","")
                            mCountryCode = addresses[0].countryCode.replace("null","")
                            val knownName = addresses[0].featureName.replace("null","")

                            mlatitude=location.latitude.toString()
                            mlongitude=location.longitude.toString()

                            println("get City $mCity")
                            println("get postal $mPostal")
                            println("get mAddress1 $mAddress1")
                            /*if (address != null) {
                                //   pickupaddresss=address;
                                return address;
                            }*/
                        }
                    } catch (ignored: Exception) {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }

                }
                return if (address != null)
                // i.e., Geocoder succeed
                {
                    address
                } else
                // i.e., Geocoder failed
                {
                    fetchAddressUsingGoogleMap()
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private fun fetchAddressUsingGoogleMap(): String? {
                val st: String
                addressList = ArrayList<Address>()
                val googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.latitude + "," + location.longitude + "&sensor=false&key=" + resources.getString(R.string.google_key)
                println("Map Url $googleMapUrl")
                try {
                    val googleMapResponse = JSONObject(ANDROID_HTTP_CLIENT.execute(HttpGet(googleMapUrl), BasicResponseHandler()))

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    val results = googleMapResponse.get("results") as JSONArray
                    Log.v("googleMapResponse", "googleMapResponse$googleMapResponse")
                    st = googleMapResponse.get("status").toString()
                    if (st == "ZERO_RESULTS") {
                        Log.v("ZERO_RESULTS", "ZERO_RESULTS")
                        address = null
                        addressList!!.clear()
                        return null
                    }
                    for (i in 0 until results.length()) {


                        val result = results.getJSONObject(i)


                        val indiStr = result.getString("formatted_address")


                        val addr = Address(Locale.getDefault())


                        addr.setAddressLine(0, indiStr)
                        //  country=addr.getCountryName();

                        addressList!!.add(addr)


                    }

                    val len = (googleMapResponse.get("results") as JSONArray).getJSONObject(0).getJSONArray("address_components").length()
                    for (i in 0 until len) {
                        if ((googleMapResponse.get("results") as JSONArray).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0) == "country") {
                            val countrys = (googleMapResponse.get("results") as JSONArray).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name")
                            println("countrys $countrys")
                        }
                    }


                    if (addressList != null) {

                        val adress0 = addressList!!.get(0).getAddressLine(0)
                        val adress1 = addressList!!.get(0).getAddressLine(1)
                        address = adress0//+" "+adress1;
                        //address = adress0+" "+adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        address = address!!.replace("null".toRegex(), "")


                        mAddress1=addressList!!.get(0).thoroughfare.replace("null","")
                        address = "$adress0 $adress1" // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        mCity = addressList!!.get(0).locality.replace("null","")
                        mState = addressList!!.get(0).adminArea.replace("null","")
                        mPostal = addressList!!.get(0).postalCode.replace("null","")
                        mCountryName = addressList!!.get(0).countryName.replace("null","")
                        mCountryCode = addressList!!.get(0).countryCode.replace("null","")
                        val knownName = addressList!!.get(0).featureName.replace("null","")

                        mlatitude=location.latitude.toString()
                        mlongitude=location.longitude.toString()

                        println("get City google api $mCity")
                        println("get postal google api $mPostal")
                        println("get mAddress1 google api $mAddress1")


                        if (address != null) {
                            return address
                        }
                    }

                } catch (ignored: Exception) {
                    ignored.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(address: String?) {
                var address = address
                if (address != null) {
                    if (type == "moveonmap") {
                        if (address != null) {
                            address = address!!.replace("null".toRegex(), "")
                            println("get AutoComplete Address " + address!!)
                            getFullAddress=address!!

                            addressModel.mAddress1=mAddress1
                            addressModel.mCity=mCity
                            addressModel.mState=mState
                            addressModel.mCountryCode=mCountryCode
                            addressModel.mCountryName=mCountryName
                            addressModel.mlatitude=mlatitude
                            addressModel.mlongitude=mlongitude

                            rltSave.isEnabled=true

                        }
                    }
                }
            }
        }.execute()
    }


    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {

    }
}
