package com.makent.trioangle.spacebooking.views

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.makent.trioangle.MainActivity
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.*
import com.makent.trioangle.helper.CustomDialog
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.profile.ProfilePageActivity
import com.makent.trioangle.spacebooking.adapter.BookingActivitiesAdapter
import com.makent.trioangle.spacebooking.adapter.BookingActivityListHeaderAdapter
import com.makent.trioangle.spacebooking.model.BookingActivitiesTypeItems
import com.makent.trioangle.spacebooking.model.GetSpaceActivitesModel
import com.makent.trioangle.spacebooking.model.SaveDateAndTime
import com.makent.trioangle.spacebooking.model.SaveEventType
import com.makent.trioangle.spacebooking.model.confirmbooking.LocalSavedDatas
import com.makent.trioangle.spacedetail.model.SpaceResult
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity
import com.makent.trioangle.travelling.CalendarActivity
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.*
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_space_available.*
import kotlinx.android.synthetic.main.app_header.*
import kotlinx.android.synthetic.main.row_activities_item.view.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SpaceAvailableActivity : AppCompatActivity(), View.OnClickListener, ServiceListener, BookingActivityListHeaderAdapter.ClickListener, BookingActivitiesAdapter.ActivitiesClickListener {
    @Inject
    lateinit var commonMethods: CommonMethods
    @Inject
    lateinit var commonDialog: CustomDialog
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var gson: Gson

    lateinit var myDialog: Dialog_loading
    var localSharedPreferences: LocalSharedPreferences = LocalSharedPreferences(this)

    lateinit var activityTypes: List<BookingActivitiesTypeItems>
    lateinit var getSpaceActivitesModel: GetSpaceActivitesModel
    var headerActivitiesPosition: Int = 0
    var activitiesPosition: Int = 0
    var subActivitiesPosition: Int = 0
    var spaceId = ""
    var isContactHost = false

    private var saveEventType = SaveEventType()
    private var saveDateAndTime = SaveDateAndTime()
    private var spaceResult = SpaceResult()
    private var localSaveDatas = LocalSavedDatas()

    var Contactback = 0

    var blockedDates:Array<String> = arrayOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_available)
        AppController.getAppComponent().inject(this)

        myDialog = Dialog_loading(this)
        myDialog.setCancelable(false)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        spaceResult = intent.getSerializableExtra(SpaceResults)as SpaceResult
        blockedDates = intent.getStringArrayExtra(BlockDates) as Array<String>

        /**
         * isContactHost is to check the user is form booking flow or contact host
         */
        isContactHost = intent.getBooleanExtra(Constants.isContactHost,false)

        spaceId=spaceResult.spaceId

        Contactback = intent.getIntExtra("ContactBack", 0)
        svBooking.visibility=View.GONE
        rlt_ReqspaceFooter.visibility=View.GONE

        if (commonMethods.isOnline(this)) {
            getSpaceActivities()
        } else {
            commonMethods.snackBar(resources.getString(R.string.network_failure), "", false, 2, rv_activity_header, resources, this)
        }
        ivBookingSpaceHostImage.setOnClickListener(this)
        tvBookingSpaceHostName.setOnClickListener(this)

        iv_Back.setOnClickListener {
            onBackPressed()
        }

        if (spaceResult.instantBook.equals("yes", ignoreCase = true)) {
            tv_instant.visibility = View.VISIBLE
        } else {
            tv_instant.visibility = View.GONE
        }

        tvGuest.text=resources.getString(R.string.Guests)+" "+resources.getString(R.string.max_guest1,spaceResult.maximumGuests)
        rltCheckinCheckOutDates.setOnClickListener {
            localSharedPreferences.saveSharedPreferences(Constants.isCheckAvailability, "1")
            val chooseCalender=Intent(this,CalendarActivity::class.java)
            chooseCalender.putExtra(isFromBookingFlow,true)
            /**
             * This is to block the Current Date when it is Request book
             * By using the existing function a array of blocked days here I'm adding only current date to the array
             */
            if (!spaceResult.instantBook.equals("yes",ignoreCase = true)) {
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val currentDate = sdf.format(Date())
                val blockToday=arrayOf(currentDate)
                chooseCalender.putExtra("blockdate",blockToday)
            }
            chooseCalender.putExtra(SpaceBlockedDays,spaceResult.blockedDay as Serializable)
            chooseCalender.putExtra("blockdate",blockedDates)
            startActivityForResult(chooseCalender,REQUEST_CODE_ON_SPACE_BOOKING)
        }

        btnReadyToBook.setOnClickListener {
            println("saveEventType ${saveEventType.activityId}")
            if(saveEventType.activityId.equals("0")){
                commonMethods.snackBar(resources.getString(R.string.choose_activity),"",false,2,btnReadyToBook,resources,this)
            }else if(TextUtils.isEmpty(saveDateAndTime.startTime)){
                commonMethods.snackBar(resources.getString(R.string.choose_dates),"",false,2,btnReadyToBook,resources,this)
            }else if (TextUtils.isEmpty(edtGuestCount.text.toString())){
                commonMethods.snackBar(resources.getString(R.string.choose_guest),"",false,2,btnReadyToBook,resources,this)
            }else if (TextUtils.isEmpty(edtMessageToHost.text.toString())&&isContactHost){
                commonMethods.snackBar(resources.getString(R.string.error_contact_host),"",false,2,btnReadyToBook,resources,this)
            }else{
                if (edtGuestCount.text.toString().toInt()==spaceResult.maximumGuests.toInt()
                        ||edtGuestCount.text.toString().toInt()<spaceResult.maximumGuests.toInt()){
                    if (localSharedPreferences.getSharedPreferences(AccessToken) == null || TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(AccessToken))) {
                        localSharedPreferences.saveSharedPreferences(LastPage, "Check_availability")

                        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = sharedPrefs.edit()
                        val gson = Gson()
                        localSaveDatas.LocalsavedEventType = saveEventType
                        localSaveDatas.LocalsavedDateTime = saveDateAndTime
                        localSaveDatas.LocalsavedGuestCount = edtGuestCount.text.toString()
                        localSaveDatas.LocalEventType = tvSpaceEventType.text.toString()
                        val json = gson.toJson(localSaveDatas)
                        val json1 = gson.toJson(spaceResult)
                        editor.putString(CheckAvailabiltyResults, json)
                        editor.putString(SpaceResults, json1)
                        editor.apply()

                        val home = Intent(applicationContext, MainActivity::class.java)
                        home.putExtra("isback", 1)
                        startActivity(home)
                    } else {
                        if (!myDialog.isShowing) {
                            myDialog.show()
                        }
                        if (isContactHost) {
                            apiService.contactHost(updateSpaceAvailability()).enqueue(RequestCallback(REQ_SPACE_CONTACT_HOST, this))
                        } else {
                            apiService.storePaymentData(updateSpaceAvailability()).enqueue(RequestCallback(REQ_UPDATE_SPACE_AVAILABILITY, this))
                        }

                    }
                }else{
                    commonMethods.snackBar(resources.getString(R.string.max_guest,spaceResult.maximumGuests),"",false,2,btnReadyToBook,resources,this)
                }
            }
        }

        if (isContactHost) {
            tv_header_title.text = resources.getString(R.string.contacthost)
            tvBookingSpaceTitle.text = spaceResult.spacename
            tvBookingSpaceHostName.text = spaceResult.hostName
            tvBookingSpaceType.text = spaceResult.theSpace[0].value
            Glide.with(applicationContext).asBitmap().load(spaceResult.hostProfilePic).into(object : BitmapImageViewTarget(ivBookingSpaceHostImage) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                    circularBitmapDrawable.isCircular = true
                    ivBookingSpaceHostImage.setImageDrawable(circularBitmapDrawable)
                }
            })
            rltHostDetails.visibility=View.VISIBLE
            rltContactHost.visibility=View.VISIBLE
            btnReadyToBook.text=resources.getString(R.string.send_message)
        }else{
            if (spaceResult.instantBook.equals("yes", ignoreCase = true)) {
                btnReadyToBook.text=resources.getString(R.string.instantbook)
            } else {
                btnReadyToBook.text=resources.getString(R.string.requestbook)
            }
            rltHostDetails.visibility=View.GONE
            rltContactHost.visibility=View.GONE
            tv_header_title.text = resources.getString(R.string.booking)
        }

        if (!TextUtils.isEmpty(spaceResult.LocalSavedDateTime.StartDate))
            settingTheFilteredValues()

        edtGuestCount.setText(spaceResult.LocalFilteredGuestCount)
    }

    /**
     * Setting the data and time Already applied in the Filter
     */
    private fun settingTheFilteredValues(){
        saveDateAndTime=spaceResult.LocalSavedDateTime
        if (!TextUtils.isEmpty(saveDateAndTime.startTime)) {
            tvCheckinDate.text = commonMethods.getDay(saveDateAndTime.StartDate) + " " + commonMethods.DateFirstUserFormat(saveDateAndTime.StartDate) + "\n" + commonMethods.change24To12Hr(saveDateAndTime.startTime)
            tvCheckOutdate.text = commonMethods.getDay(saveDateAndTime.endDate) + " " + commonMethods.DateFirstUserFormat(saveDateAndTime.endDate) + "\n" + commonMethods.change24To12Hr(saveDateAndTime.endTime)
        }
    }

    /**
     * Get Space Activities
     */
    private fun getSpaceActivities() {
        if (!myDialog.isShowing) myDialog.show()
        apiService.getSpaceActivities(localSharedPreferences.getSharedPreferences(AccessToken), spaceId).enqueue(RequestCallback(REQ_SPACE_ACTIVITIES,this))
    }


    /**
     * Save all Api params as Hashmap
     */
    private fun updateSpaceAvailability(): HashMap<String, String>? {
        val gson = GsonBuilder().create()
        val eventTypeObject = gson.toJsonTree(saveEventType).asJsonObject
        val saveDateAndTimeObject = gson.toJsonTree(saveDateAndTime).asJsonObject

        val updateSpaceAvailables = HashMap<String, String>()
        updateSpaceAvailables["token"] = localSharedPreferences.getSharedPreferences(AccessToken)
        updateSpaceAvailables["space_id"] = localSharedPreferences.getSharedPreferences(mSpaceId)
        updateSpaceAvailables["event_type"] =eventTypeObject.toString()
        updateSpaceAvailables["booking_date_times"] =saveDateAndTimeObject.toString()
        updateSpaceAvailables["number_of_guests"] = edtGuestCount.text.toString()
        /**
         * For Contact host need to pass only message param no need booking type
         * The above params are Common for both storePayment and Contact host
         */
        if (isContactHost){
            updateSpaceAvailables["message"] = edtMessageToHost.text.toString()
        }else {
            updateSpaceAvailables["booking_type"] = checkSpaceBookingType()
        }

        return updateSpaceAvailables
    }


    /**
     * Check booking Type
     */
    private fun checkSpaceBookingType():String{
        /**
         * The Return datas are no need to change for translate
         * It should be remains same for all languages, so don't change
         */
        return if (spaceResult.instantBook.equals("yes", ignoreCase = true)) {
            "instant_book"
        } else {
            "request_book"
        }
    }

    /**
     * onSuccess Form Api
     */
    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (myDialog.isShowing) myDialog.dismiss()

        if (jsonResp!!.isSuccess) {
            when(jsonResp.requestCode){
                REQ_SPACE_ACTIVITIES-> onLoadSpaceActivities(jsonResp)
                REQ_UPDATE_SPACE_AVAILABILITY-> onSuccessSpaceUpdate(jsonResp)
                REQ_SPACE_CONTACT_HOST->{
                    Toast.makeText(applicationContext,jsonResp.statusMsg,Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        } else {
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, rv_activity_header, resources, this)
        }
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (myDialog.isShowing) myDialog.dismiss()
        commonMethods.snackBar(resources.getString(R.string.internal_server_error),"",false,2,btnReadyToBook,resources,this)
    }


    /**
     * Get Space Activities
     */
    private fun onLoadSpaceActivities(jsonResp: JsonResponse?) {
        getSpaceActivitesModel = gson.fromJson(jsonResp!!.strResponse, GetSpaceActivitesModel::class.java)
        activityTypes = getSpaceActivitesModel.activityTypes as List<BookingActivitiesTypeItems>
        loadActivitiesHeader()

        if (Contactback == 2) {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val gson = Gson()
            val json = sharedPrefs.getString(CheckAvailabiltyResults, "")
            val type = object : TypeToken<LocalSavedDatas>() {}.type
            val localSavedDatas = gson.fromJson<LocalSavedDatas>(json, type)
            saveDateAndTime = localSavedDatas.LocalsavedDateTime
            saveEventType = localSavedDatas.LocalsavedEventType

            /**
             * Setting the Event Type Header,Activity and SubActivity (if present)
             */
            setHeaderSelected()
            setActivities()
            setSubActivities()

            tvCheckinDate.text = commonMethods.getDay(saveDateAndTime.StartDate) + " " + commonMethods.DateFirstUserFormat(saveDateAndTime.StartDate) + "\n" + commonMethods.change24To12Hr(saveDateAndTime.startTime)
            tvCheckOutdate.text = commonMethods.getDay(saveDateAndTime.endDate) + " " + commonMethods.DateFirstUserFormat(saveDateAndTime.endDate) + "\n" + commonMethods.change24To12Hr(saveDateAndTime.endTime)

            edtGuestCount.setText(localSavedDatas.LocalsavedGuestCount)
            tvSpaceEventType.text = localSavedDatas.LocalEventType

        }

    }

    /**
     * get the PaymentDetails By using a key
     */
    private fun onSuccessSpaceUpdate(jsonResp: JsonResponse?){
        val sessionKey = commonMethods.getJsonValue(jsonResp!!.strResponse, "s_key", String::class.java) as String
        val intent =Intent(this,RequestSpaceActivity::class.java)
        intent.putExtra(SESSIONID,sessionKey)
        intent.putExtra("host_user_id",spaceResult.userId)
        intent.putExtra(RESERVATIONID,"")
        intent.putExtra(Bookingtype,spaceResult.instantBook)
        startActivity(intent)
    }

    /**
     *Load Activities Header list
     */
    fun loadActivitiesHeader() {

        rv_activity_header.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = BookingActivityListHeaderAdapter(activityTypes, this)
        adapter.setClickListener(this)
        val bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottomup)
        rlt_ReqspaceFooter.startAnimation(bottomUp)
        svBooking.visibility=View.VISIBLE
        rlt_ReqspaceFooter.visibility = View.VISIBLE
        rv_activity_header.adapter = adapter
        HourlyPrice()
        if (activityTypes.isNotEmpty()) {
            loadActivities()
        }
    }

    /**
     * Setting Selected headers From the Local
     */
    private fun setHeaderSelected() {
        for (i in 0 until activityTypes.size) {
            if (saveEventType.activityTypeId.equals(activityTypes[i].id)) {
                activityTypes[i].isSelected = true
                headerActivitiesPosition = i
                loadActivitiesHeader()
                rv_activity_header.scrollToPosition(headerActivitiesPosition)
                break
            }
        }
    }

    /**
     * Setting Selected Activity
     */
    private fun setActivities() {
        for (i in 0 until activityTypes[headerActivitiesPosition].activities!!.size) {
            if (saveEventType.activityId == activityTypes[headerActivitiesPosition].activities?.get(i)!!.id) {
                activityTypes[headerActivitiesPosition].activities?.get(i)!!.isSelected = true
                loadActivities()
                break
            }
        }
    }

    private fun setSubActivities() {

        firstLoop@ for (x in 0 until activityTypes.get(headerActivitiesPosition).activities!!.size) {
            for (y in 0 until activityTypes.get(headerActivitiesPosition).activities!![x]?.subActivities!!.size) {
                if (saveEventType.activitySubTypeId == activityTypes.get(headerActivitiesPosition).activities!![x]?.subActivities!!.get(y)!!.id) {
                    activityTypes.get(headerActivitiesPosition).activities!![x]?.subActivities!!.get(y)!!.isSelected = true
                    loadActivities()
                    break@firstLoop
                }
            }
        }
    }

    /**
     * set Space Price
     */
    private fun HourlyPrice() {
        val price = HtmlCompat.fromHtml(activityTypes.get(headerActivitiesPosition).currencySymbol, HtmlCompat.FROM_HTML_MODE_COMPACT).toString() + " " + activityTypes[headerActivitiesPosition].hourlyPrice
        tv_spacehourlyPrice.text = price
    }


    /**
     *Load Activities Main list
     */
    fun loadActivities() {
        rv_activity.layoutManager = LinearLayoutManager(this)
        val adapters = BookingActivitiesAdapter(activityTypes[headerActivitiesPosition].activities, this)
        adapters.setClickListener(this)
        rv_activity.adapter = adapters
    }
    /**
     *  Header activities list click
     */
    override fun onHeaderClick(v: View, position: Int, oldPostion: Int) {
        print("Name " + activityTypes.get(position).name)
        rv_activity_header.adapter?.notifyDataSetChanged()
        clearSelectedActivities()
        clearSelectedSubActivities()
        headerActivitiesPosition = position
        HourlyPrice()
        loadActivities()
        clearSelectedActivity()
    }

    /**
     *  Activities list click listener
     */
    override fun onActivitiesClick(v: View, position: Int) {
        val cbActivities = v.cbActivities
        activitiesPosition = position
        clearSelectedActivities()
        clearSelectedSubActivities()
        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.isSelected = cbActivities.isChecked
        setActivityHeader()
        loadActivities()

        if (cbActivities.isChecked) {
            saveEventType.activityTypeId = activityTypes.get(headerActivitiesPosition).id
            saveEventType.activityId = activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.id!!
            saveEventType.activitySubTypeId = "0"
            if (activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.isSelected!!) {
                tvSpaceEventType.text = activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.name!!
            } else {
                clearSelectedActivity()
            }
        }else{
            clearSelectedActivity()

        }
    }

    /**
     * Sub activities list click listener
     */
    override fun onSubActivitiesClick(v: View, activitiesPosition: Int, position: Int) {
        val cbsubActivities = v.cbActivities
        var isSubActivitySelected = false
        subActivitiesPosition = position
        clearSelectedActivities()
        clearSelectedSubActivities()
        //this.activitiesPosition = activitiesPosition
        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.get(subActivitiesPosition)?.isSelected = cbsubActivities.isChecked

        for (index: Int in activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.indices!!) {
            if (activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities!!.get(index)?.isSelected!!)
                isSubActivitySelected = true
        }

        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.isSelected = isSubActivitySelected
        setActivityHeader()
        loadActivities()

        if (isSubActivitySelected) {
            saveEventType.activityTypeId = activityTypes.get(headerActivitiesPosition).id
            saveEventType.activityId = activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.id!!
            saveEventType.activitySubTypeId = activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.get(subActivitiesPosition)?.id!!
            if (activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.get(subActivitiesPosition)?.isSelected!!) {
                tvSpaceEventType.text = activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.get(subActivitiesPosition)?.name!!
            } else {
                clearSelectedActivity()
            }
        }else{
            clearSelectedActivity()
        }
    }

    private fun clearSelectedActivity(){
        saveEventType.activityId="0"
        tvSpaceEventType.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ON_SPACE_BOOKING &&resultCode==Activity.RESULT_OK){
            saveDateAndTime=data!!.getSerializableExtra(SavedDateAndTime) as SaveDateAndTime
            tvCheckinDate.text=commonMethods.getDay(saveDateAndTime.StartDate)+" "+commonMethods.DateFirstUserFormat(saveDateAndTime.StartDate)+"\n"+commonMethods.change24To12Hr(saveDateAndTime.startTime)
            tvCheckOutdate.text=commonMethods.getDay(saveDateAndTime.endDate)+" "+commonMethods.DateFirstUserFormat(saveDateAndTime.endDate)+"\n"+commonMethods.change24To12Hr(saveDateAndTime.endTime)
        }
    }

    /**
     * Clear and reset the activity datas
     */
    private fun clearSelectedActivities(){
        for (x in 0 until activityTypes.get(headerActivitiesPosition).activities!!.size){
            activityTypes[headerActivitiesPosition].activities?.get(x)?.isSelected = false
        }
    }

    /**
     * Clear and reset Activity sub dates
     */
    private fun clearSelectedSubActivities(){

        for (x in 0 until activityTypes.get(headerActivitiesPosition).activities!!.size){
            for (y in 0 until activityTypes.get(headerActivitiesPosition).activities!![x]?.subActivities!!.size){
                activityTypes.get(headerActivitiesPosition).activities!![x]?.subActivities!!.get(y)!!.isSelected= false
            }
        }
    }

    /**
     * Set selected or not in header activity
     */
    private fun setActivityHeader() {
        for (index: Int in activityTypes.get(headerActivitiesPosition).activities?.indices!!) {
            if (activityTypes.get(headerActivitiesPosition).activities?.get(index)?.isSelected!!) {
                activityTypes.get(headerActivitiesPosition).isSelected = true
                return
            }
        }
        activityTypes.get(headerActivitiesPosition).isSelected = false
    }

    /**
     * For Both With and Without Login Flow
     */
    override fun onBackPressed() {
        if (Contactback == 1 || Contactback == 2) {
            localSharedPreferences.saveSharedPreferences(Reload, "reload")
            if (localSharedPreferences.getSharedPreferences(SearchCheckIn) != null && localSharedPreferences.getSharedPreferences(SearchCheckOut) != null) {
                localSharedPreferences.saveSharedPreferences(isRequestCheck, "1")
            }
            val rooms = Intent(applicationContext, SpaceDetailActivity::class.java)
            startActivity(rooms)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvBookingSpaceHostName,
            R.id.ivBookingSpaceHostImage -> {
                val x = Intent(applicationContext, ProfilePageActivity::class.java)
                println("Other user id${spaceResult.userId}")
                x.putExtra("otheruserid", spaceResult.userId)
                startActivity(x)
            }

        }
    }
}