package com.makent.trioangle.createspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.net.http.AndroidHttpClient
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makent.trioangle.R
import com.makent.trioangle.autocomplete.GoogleMapPlaceSearchAutoCompleteRecyclerView
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.ActivityBasic
import com.makent.trioangle.helper.CustomDialog
import com.makent.trioangle.helper.RunTimePermission
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.REQ_EDIT_BASIC
import com.makent.trioangle.util.Enums.REQ_NEW_BASIC
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class AddressFragment : Fragment(), GoogleMapPlaceSearchAutoCompleteRecyclerView.AutoCompleteAddressTouchListener,ServiceListener {

    @Inject
    lateinit var runTimePermission: RunTimePermission

    @Inject
    lateinit var customDialog: CustomDialog

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    val ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(AddressFragment::class.java!!.getName())

    lateinit var dialog: AlertDialog
    lateinit var res: Resources

    lateinit var listner: BasicStepsActivityInterface

    lateinit var mActivity: BasicStepsActivity

    var markerPoints:ArrayList<LatLng> = ArrayList(2)


    private val addressAutoCompletePredictions = ArrayList<AutocompletePrediction>()
    lateinit var googleMapPlaceSearchAutoCompleteRecyclerView: GoogleMapPlaceSearchAutoCompleteRecyclerView
    lateinit var googleAutoCompleteToken: AutocompleteSessionToken
    lateinit var placesClient: PlacesClient
    var oldstring = ""
    var counti = 0
    lateinit var btnContinue: Button
    lateinit var rvPlaceSearch: RecyclerView
    lateinit var edtAddress: EditText
    lateinit var edtAddressTwo: EditText
    lateinit var edtGuidance: EditText
    lateinit var tvChooseOnMap: TextView
    lateinit var tvMarkExactGeo: TextView
    lateinit var tvAddressTitle: TextView
    protected var isInternetAvailable: Boolean = false
    private var searchlocation: String? = null
    lateinit var mContext: Context
    lateinit var getAddress:String
    lateinit var lat:String
    lateinit var log:String

    val setLocationOnMap:Int=100

    var addressModel:AddressModel= AddressModel()

    lateinit var localSharedPreferences:LocalSharedPreferences

    lateinit var mydialog: Dialog_loading

    var basicStepModels: BasicStepModel?=null
    var isLocationisUpdate=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.address_layout_fragment,
                container, false)

        mContext = container!!.context
        initMapPlaceAPI()

        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        rvPlaceSearch = view.findViewById<RecyclerView>(R.id.rv_place_search)
        tvChooseOnMap = view.findViewById(R.id.tv_choose_on_map)
        tvMarkExactGeo = view.findViewById(R.id.tv_mark_exact_geo)
        edtAddress = view.findViewById<EditText>(R.id.edt_address)
        edtAddressTwo = view.findViewById<EditText>(R.id.edt_address_two)
        edtGuidance = view.findViewById<EditText>(R.id.edt_guidance)
        tvAddressTitle = view.findViewById<EditText>(R.id.tv_address_title)

        initListner()
        init()

        return view
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.res != null) listner.res else activity!!.resources
        mActivity = if (listner.instance != null) listner.instance else (activity as BasicStepsActivity?)!!
        mActivity.nsvBasic.scrollTo(0,0)
        mydialog = Dialog_loading(mActivity)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }


    fun initMapPlaceAPI(){
        if(!Places.isInitialized())
            Places.initialize(mContext,resources.getString(R.string.google_key))
    }


    private fun init() {

        googleMapPlaceSearchAutoCompleteRecyclerView = GoogleMapPlaceSearchAutoCompleteRecyclerView(addressAutoCompletePredictions, mContext, this)
        googleAutoCompleteToken = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(mContext)
        dialog = commonMethods!!.getAlertDialog(mContext)

        localSharedPreferences=LocalSharedPreferences(mActivity)
        rvPlaceSearch.visibility = View.GONE
        rvPlaceSearch.isNestedScrollingEnabled = false
        val mLinearLayoutManager = LinearLayoutManager(mContext)
        rvPlaceSearch.layoutManager = mLinearLayoutManager
        rvPlaceSearch.adapter = googleMapPlaceSearchAutoCompleteRecyclerView

        edtAddress.addTextChangedListener(PlaceSearchTextWatcher(edtAddress))

        basicStepModels=mActivity.basicStepModelData

        if(basicStepModels!=null){
            setDatas()
        }else {
            btnContinue.isEnabled = false
        }
        tvAddressTitle.text=commonMethods.changeColorForStar(res.getString(R.string.address_space))
        tvChooseOnMap.setOnClickListener {
            if (markerPoints.size>0) {
                edtAddress.clearFocus()
                val intent = Intent(mContext, CreateSpaceMapActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelableArrayList("latlng", markerPoints)
                intent.putExtras(bundle)
                startActivityForResult(intent, setLocationOnMap)
            }else{
                commonMethods.showMessage(mContext, dialog, mActivity.resources.getString(R.string.unable_get_location))
            }
        }





        btnContinue.setOnClickListener {
            addressModel.mAddress2 = edtAddressTwo.text.toString()
            addressModel.mGuidance = edtGuidance.text.toString()
            val gson = GsonBuilder().create()
            val jsobj = gson.toJsonTree(addressModel).asJsonObject
            val location = jsobj.toString()
            if (basicStepModels!=null){
                if(commonMethods.isOnline(mActivity)) {
                    if (!mydialog.isShowing) mydialog.show()
                    apiService.updateSpace(updateLocation(location)).enqueue(RequestCallback(this))
                }else{
                    commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
                }
            }else {
                clearAddressAndHideRecyclerView()
                mActivity.putHashMap("location_data", location)
                val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
                navController.navigate(R.id.action_addressFragment_to_guestAccessFragment)
                mActivity.progressBarUpdate(75, 90)
            }

        }

    }
    private fun updateLocation(location:String):HashMap<String,String>{
        val updateLocationMap = java.util.HashMap<String, String>()
        updateLocationMap["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateLocationMap["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateLocationMap["step"] = "basics"
        updateLocationMap["location_data"] = location
        return updateLocationMap

    }

    override fun onStop() {
        super.onStop()
       // ANDROID_HTTP_CLIENT.close()
    }

    private fun setDatas(){
        isLocationisUpdate=true
        addressModel.mAddress1=basicStepModels!!.addressLine1
        addressModel.mAddress2=basicStepModels!!.addressLine2
        addressModel.mCity=basicStepModels!!.city
        addressModel.mState=basicStepModels!!.state
        addressModel.mCountryName=basicStepModels!!.country
        addressModel.mCountryCode=basicStepModels!!.country
        addressModel.mlatitude=basicStepModels!!.latitude
        addressModel.mlongitude=basicStepModels!!.longitude
        addressModel.mGuidance=basicStepModels!!.guidance

        val latLng=LatLng(addressModel.mlatitude.toDouble(),addressModel.mlongitude.toDouble())
        markerPoints.clear()
        markerPoints.add(latLng)

        val setAddress = addressModel.mAddress1+" "+addressModel.mCity+" "+addressModel.mState+" "+addressModel.mCountryCode
        edtAddress.setText(setAddress)
        edtAddressTwo.setText(addressModel.mAddress2)
        edtGuidance.setText(addressModel.mGuidance)
        tvChooseOnMap.visibility=View.VISIBLE
        tvMarkExactGeo.visibility=View.VISIBLE

    }
    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if(jsonResp!!.isSuccess) {
            if (mydialog.isShowing) mydialog.dismiss()
            onSuccessResponse()
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, btnContinue, mActivity.resources, mActivity)
        }
    }

    private fun onSuccessResponse() {
        val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
        navController.navigate(R.id.action_addressFragment_to_guestAccessFragment)
        mActivity.progressBarUpdate(75, 90)

    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==setLocationOnMap&&resultCode==-1){
            if(rvPlaceSearch.visibility==View.VISIBLE) {
                clearAddressAndHideRecyclerView()
            }
            addressModel= data?.getSerializableExtra("addressModel") as AddressModel
            val updatedlatLng=LatLng(addressModel.mlatitude.toDouble(),addressModel.mlongitude.toDouble())
            markerPoints.clear()
            markerPoints.add(updatedlatLng)
            edtAddress.setText(data.getStringExtra("getFullAddress"))
            edtAddress.clearFocus()
            btnContinue.isEnabled=true
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is BasicStepsActivityInterface) {
            listner = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }


    override fun selectedAddress(autocompletePrediction: AutocompletePrediction?) {
        var autocompletePrediction = autocompletePrediction
        /*if (counti > 0) {
            autocompletePrediction = null
        }*/
        if (autocompletePrediction != null) {
            counti++

            searchlocation = autocompletePrediction.getPrimaryText(null).toString()
            edtAddress.setText(autocompletePrediction.getPrimaryText(null))
            tvChooseOnMap.setVisibility(View.VISIBLE)
            tvMarkExactGeo.setVisibility(View.VISIBLE)
            oldstring = autocompletePrediction.getPrimaryText(null).toString()
            val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edtAddress.getWindowToken(), 0)

            rvPlaceSearch.visibility = View.GONE
            //rltLocationOnMap.setVisibility(View.VISIBLE)
            fetchLocation(searchlocation!!)


        }
    }


    fun clearAddressAndHideRecyclerView() {
        googleMapPlaceSearchAutoCompleteRecyclerView.clearAddresses()
        rvPlaceSearch.visibility = View.GONE
    }


    private inner class PlaceSearchTextWatcher(private val view: View) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
            if (s.toString() == "") {
                rvPlaceSearch.visibility = View.GONE
            } else {
                rvPlaceSearch.visibility = View.VISIBLE
            }
        }

        override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
            if (s.toString() == "") {
                rvPlaceSearch.visibility = View.GONE
                tvChooseOnMap.visibility = View.GONE
                tvMarkExactGeo.visibility = View.GONE
            } else {
                rvPlaceSearch.visibility = View.VISIBLE
            }
        }

        override fun afterTextChanged(s: Editable) {
            isInternetAvailable = commonMethods!!.isOnline(mContext)
            if (!isInternetAvailable) {
                commonMethods!!.showMessage(mContext, dialog, getString(R.string.no_internet_available))
            }

            if(s.toString()==""){
                btnContinue.isEnabled=false
            }

            if (view.hasFocus()) {
                if (s.toString() != "") {
                    rvPlaceSearch.visibility = View.VISIBLE
                    Handler().postDelayed({
                        if (oldstring != s.toString()) {
                            oldstring = s.toString()
                            //mAutoCompleteAdapter.getFilter().filter(s.toString()); // Place search
                            rvPlaceSearch.visibility = View.VISIBLE
                            getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(s.toString())
                        }
                    }, 1000)

                } else {
                    clearAddressAndHideRecyclerView()
                }


            } else {
                clearAddressAndHideRecyclerView()
            }

        }
    }


    fun getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(queryAddress: String) {

        //RectangularBounds bounds = RectangularBounds.newInstance(first, sec);
        val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(googleAutoCompleteToken)
                .setQuery(queryAddress)
                .build()

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    /*Toast toast = Toast.makeText(this, "address auto comp succ" + response.getAutocompletePredictions().size(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                    toast.show();*/

                    if (response.autocompletePredictions.size > 0) {
                        googleMapPlaceSearchAutoCompleteRecyclerView.updateList(response.autocompletePredictions)
                    } else {
                        clearAddressAndHideRecyclerView()
                        //showUserMessage(getResources().getString(R.string.no_address_found));
                    }


                }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        val apiException = exception

                    }
                    exception.printStackTrace()

                }
    }


    /**
     * Fetch Location from address if Geocode available get from geocode otherwise get location from google
     */
    fun fetchLocation(addresss: String) {
        getAddress = addresss

        object : AsyncTask<Void, Void, String>() {
            var locations: String? = null


            override fun doInBackground(vararg params: Void): String? {

                if (Geocoder.isPresent())
                // Check geo code available or not
                {
                    try {
                        val geocoder = Geocoder(mContext, Locale.getDefault())
                        val address: List<Address>?

                        // May throw an IOException
                        address = geocoder.getFromLocationName(getAddress, 5)
                        if (address == null) {
                            return null
                        }
                        val location = address[0]

                        location.latitude
                        location.longitude

                        lat = location.latitude.toString()
                        log = location.longitude.toString()
                        locations = lat + "," + log

                        addressModel.mlongitude=log
                        addressModel.mlatitude=lat
                        addressModel.mCity=location.locality
                        addressModel.mState=location.adminArea
                        addressModel.mPostal=location.postalCode
                        addressModel.mCountryCode=location.countryCode
                        addressModel.mCountryName=location.countryName

                    } catch (ignored: Exception) {
                        // after a while, Geocoder start to throw "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }

                }

                if (locations != null)
                // i.e., Geocoder succeed
                {
                    println("locations : " + locations!!)
                    return locations
                } else
                // i.e., Geocoder failed
                {
                    // If geocode not available or location null call google API
                    return fetchLocationUsingGoogleMap()
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private fun fetchLocationUsingGoogleMap(): String? {
                getAddress = getAddress.replace(" ".toRegex(), "%20")
                val googleMapUrl = ("https://maps.google.com/maps/api/geocode/json?address=" + getAddress + "&sensor=false"
                        + "&key=" + getString(R.string.google_key))

                println(" Google map Url : $googleMapUrl")
                try {
                    val googleMapResponse = JSONObject(ANDROID_HTTP_CLIENT.execute(HttpGet(googleMapUrl),
                            BasicResponseHandler()))

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    if (googleMapResponse.length() > 0) {
                        val longitute = (googleMapResponse.get("results") as JSONArray).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lng")

                        val latitude = (googleMapResponse.get("results") as JSONArray).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getString("lat")

                        val len = (googleMapResponse.get("results") as JSONArray).getJSONObject(0)
                                .getJSONArray("address_components").length()


                        return "$latitude,$longitute"
                    } else {
                        return null
                    }

                } catch (ignored: Exception) {
                    ignored.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(location: String?) {
                if (location != null) {

                    //buttonStatusChange(experienceWhereWeMeet.get(0).getCountry(),countryName,tvNext);
                    //commonMethods.buttonStatus(experienceWhereWeMeet.get(0).getCountry(),countryName, tvNext,tvUndo,getActivity().getResources());

                    val parts = location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val lat = java.lang.Double.valueOf(parts[0])
                    val lng = java.lang.Double.valueOf(parts[1])
                    val latLng = LatLng(lat, lng)

                    markerPoints.add(latLng)
                    edtAddress.setText(getAddress)
                    tvChooseOnMap.setVisibility(View.VISIBLE)
                    tvMarkExactGeo.setVisibility(View.VISIBLE)
                    rvPlaceSearch.visibility = View.GONE

                    searchItemClick(latLng)
                } else {
                    commonMethods.showMessage(mContext, dialog, mActivity.resources.getString(R.string.unable_get_location))
                    commonMethods.hideProgressDialog()
                }
            }

        }.execute()
    }

    fun searchItemClick(cur_Latlng: LatLng) {


        markerPoints.set(0, cur_Latlng)

        val l1 = markerPoints.get(0) as LatLng

        val lat = l1.latitude.toString()
        val lng = l1.longitude.toString()

        println("Lat and Lng  : $lat : $lng")

        commonMethods.hideProgressDialog()
        counti = 0
    }

}