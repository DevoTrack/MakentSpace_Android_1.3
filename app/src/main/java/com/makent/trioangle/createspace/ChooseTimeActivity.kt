package com.makent.trioangle.createspace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ReadyHostModel.AvailabilityTimesItem
import com.makent.trioangle.createspace.ReadyHostModel.CalendarData
import com.makent.trioangle.createspace.ReadyHostModel.SelectedTimesItem
import com.makent.trioangle.createspace.ReadyHostModel.UpdatedCalenderData
import com.makent.trioangle.createspace.model.hostlisting.readytohost.SpaceTimings
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.RoomsBeds.HelperListActivity
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_choose_time.*
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChooseTimeActivity : AppCompatActivity(), AvailabilityTimeAdapter.OnItemClickListener , ServiceListener {

    private val availableTime: String = ""
    var status: String = ""
    @Inject
    lateinit var apiService: ApiService

    var updateApiCall: Boolean = true

    @Inject
    lateinit var commonMethods: CommonMethods

    protected var isInternetAvailable: Boolean = false



    var timeIdList: MutableList<Int> = ArrayList()

    internal var multipleTime: MutableList<SpaceTimings> = ArrayList()
    lateinit var localSharedPreferences: LocalSharedPreferences
    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var gson: Gson

    lateinit private var availabilityTimeAdapter: AvailabilityTimeAdapter
    private var position: Int = 0
    private var timeType = ""
    private val timeZone: String? = null


    lateinit var availabilityTimes: AvailabilityTimesItem
    var updatedCalenderData=UpdatedCalenderData()

    var day : Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_time)

        AppController.getAppComponent().inject(this)
        ButterKnife.bind(this)
        localSharedPreferences = LocalSharedPreferences(this)

        getIntentValues()
        init()
    }

    private fun getIntentValues() {

        availabilityTimes = (intent.extras!!.getSerializable(Constants.AVAILABILITYTIMES) as? AvailabilityTimesItem)!!
        day = (intent.extras!!.getSerializable("Day") as Int)

        status = availabilityTimes.status

        Log.e("status","day status" + status);
    }

    private fun init() {

        //dialog = commonMethods.getAlertDialog(this)

        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        initMultipleTime()
        initExperienceTime()

        val mLayoutManager = LinearLayoutManager(this)
        availabilityTimeAdapter = AvailabilityTimeAdapter(availabilityTimes.selectedTimes, timeZone, this, this)

        rv_time!!.layoutManager = mLayoutManager
        rv_time!!.adapter = availabilityTimeAdapter
        rv_time!!.isNestedScrollingEnabled = false

        initListners()

        if (status.equals("Closed")) {
            scOpen.isChecked = false
            scHourType.isChecked = false
        } else if (status.equals("All")) {
            scOpen.isChecked = true
            scHourType.isChecked = true
        } else if (status.equals("Open")) {
            scOpen.isChecked = true
            scHourType.isChecked = false

            rlt_time.visibility = View.VISIBLE
            tvHourType.text = getString(R.string.set_hours)
            availabilityTimes.status = "Open"
        }

    }

    private fun initListners() {
        scOpen.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                rlt_hours.visibility = View.VISIBLE
                tvClose.text = getString(R.string.openss)
                availabilityTimes.status = "All" //"All" "Open" "Closed"
            } else {
                tvClose.text = getString(R.string.close)
                rlt_hours.visibility = View.GONE
                scHourType.isChecked = false
                availabilityTimes.status = "Closed" //"All" "Open" "Closed"
            }


        }

        scHourType.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                rlt_time.visibility = View.VISIBLE
                tvHourType.text = getString(R.string.set_hours)
                availabilityTimes.status = "Open" //"All" "Open" "Closed"
            } else {
                tvHourType.text = getString(R.string.all_hours)
                rlt_time.visibility = View.GONE
                availabilityTimes.status = "All" //"All" "Open" "Closed"
            }
        }

        iv_back.setOnClickListener(){
            finish()
        }

        tv_next.setOnClickListener{
            updateTime()
        }

        tv_add.setOnClickListener(){
            addSelectedItem()
            availabilityTimeAdapter.notifyDataSetChanged()
        }



    }


    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {

        if (mydialog.isShowing)
            mydialog.dismiss()

        onSuccessUpdatetime(jsonResp);
    }

    private fun onSuccessUpdatetime(jsonResp: JsonResponse?) {
        updatedCalenderData=gson.fromJson(jsonResp!!.strResponse,UpdatedCalenderData::class.java)
        val intent = Intent()
        intent.putExtra("Day", day)
        intent.putExtra(Constants.AVAILABILITYTIMES, availabilityTimes as Serializable)
        intent.putExtra(Constants.CalenderData, updatedCalenderData.calendarData as Serializable)
        setResult(Constants.ViewType.AvailabilityTime, intent)
        finish()
    }


    private fun initExperienceTime() {


        for (i in availabilityTimes.selectedTimes.indices) {
            availabilityTimes.selectedTimes.get(i).startTime = _24To12Hours(availabilityTimes.selectedTimes.get(i).startTime)
            availabilityTimes.selectedTimes.get(i).endTime = _24To12Hours(availabilityTimes.selectedTimes.get(i).endTime)
        }


        if(availabilityTimes.selectedTimes.size==0){
           addSelectedItem()
        }

    }

    private fun addSelectedItem() {
        val selectedTimesItem = SelectedTimesItem()
        selectedTimesItem.startTime = ""
        selectedTimesItem.endTime=""
        selectedTimesItem.id=""
        availabilityTimes.selectedTimes.add(selectedTimesItem)
    }

    private fun initMultipleTime() {

        val multipleTimeArray = resources.getStringArray(R.array.hour)
        var experienceTimings: SpaceTimings
        for (i in multipleTimeArray.indices) {
            experienceTimings = SpaceTimings()
            experienceTimings.time = multipleTimeArray[i]
            experienceTimings.timeZone = ""
            experienceTimings.id = i
            multipleTime.add(experienceTimings)
        }


    }


    override fun onRemoveClicked(position: Int) {
        this.position = position

        availabilityTimes.selectedTimes.removeAt(position)
        availabilityTimeAdapter.notifyDataSetChanged()
        tv_add!!.visibility = View.VISIBLE


    }

    override fun onItemClick(position: Int, selectedItem: String, timeType: String) {


        val listHelper = Intent(this, HelperListActivity::class.java)
        listHelper.putExtra("list", multipleTime as Serializable)
        listHelper.putExtra("value", selectedItem)
        this.position = position
        this.timeType = timeType
        startActivityForResult(listHelper, Constants.ViewType.MultipleTime)


    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == Constants.ViewType.MultipleTime) {
                val selectedPosition = Integer.parseInt(data.getStringExtra("clickedPos"))
                if (position != -1) {

                    if (timeType == "start") {


                        if (availabilityTimes.selectedTimes.get(position).endTime == "" || multipleTime.get(selectedPosition).id < timeId(availabilityTimes.selectedTimes.get(position).endTime)) {

                            availabilityTimes.selectedTimes.get(position).startTime = multipleTime.get(selectedPosition).time


                        } else {

                            Toast.makeText(this, getString(R.string.starttime_lesser_error_msg), Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        if (availabilityTimes.selectedTimes.get(position).startTime == "" || multipleTime.get(selectedPosition).id > timeId(availabilityTimes.selectedTimes.get(position).startTime)) {

                            availabilityTimes.selectedTimes.get(position).endTime = multipleTime.get(selectedPosition).time

                        } else {

                            Toast.makeText(this, getString(R.string.end_time_greater_error_msg), Toast.LENGTH_SHORT).show()
                        }

                    }

                    availabilityTimeAdapter.notifyDataSetChanged()


                }


            }
        }
    }

    private fun updateTime() {

        updateApiCall = true
        if(availabilityTimes.status.equals("Open"))
            validateTime()



        if(updateApiCall){

            updateAvailableTime()

        }


    }

    private fun updateAvailableTime() {

        if (!mydialog.isShowing)
            mydialog.show()

        apiService.updateReadyToHost(updateAvailabilityParams()).enqueue(RequestCallback(this))
    }

    private fun validateTime() {
        timeIdList.clear()

        var x = 0
        while( x < availabilityTimes.selectedTimes.size ){
            var y = x+1
            while( y < availabilityTimes.selectedTimes.size ){
                if(availabilityTimes.selectedTimes.get(x).startTime==availabilityTimes.selectedTimes.get(y).startTime){
                    updateApiCall = false
                    Toast.makeText(this,getString(R.string.please_choose_valid_time),Toast.LENGTH_SHORT).show()
                }
                y++
            }
            x++
        }




        for (i in availabilityTimes.selectedTimes.indices) {
            if(availabilityTimes.selectedTimes.get(i).startTime.equals("")||availabilityTimes.selectedTimes.get(i).endTime.equals("")){
                updateApiCall = false
                Toast.makeText(this,getString(R.string.please_choose_valid_time),Toast.LENGTH_SHORT).show()
                break
            }
        }


        for (i in availabilityTimes.selectedTimes.indices) {



            var strTime : Int =  timeId(availabilityTimes.selectedTimes.get(i).startTime)
            var endTime : Int =  timeId(availabilityTimes.selectedTimes.get(i).endTime)

            for (j in strTime..endTime) {

                if(!timeIdList.contains(j)||strTime==j||endTime==j){
                    timeIdList.add(j)
                }else{
                    updateApiCall = false
                    Toast.makeText(this,getString(R.string.please_choose_valid_time),Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }

    private fun updateAvailabilityParams(): HashMap<String, String>? {

        for (i in availabilityTimes.selectedTimes.indices) {

            availabilityTimes.selectedTimes.get(i).startTime = _12To24Hours(availabilityTimes.selectedTimes.get(i).startTime)
            availabilityTimes.selectedTimes.get(i).endTime = _12To24Hours(availabilityTimes.selectedTimes.get(i).endTime)

        }

        var availabilityData : String = gson.toJson(availabilityTimes);
        println("availability data :"+availabilityData)
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "ready_to_host"
        updateSpace["availability_data"] = availabilityData

        return updateSpace
    }


    private fun _12To24Hours(time: String): String {

        val displayFormat = SimpleDateFormat("HH:mm:ss")
        val parseFormat = SimpleDateFormat("hh:mm a")
        var date: Date? = null
        try {
            date = parseFormat.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return time
        }

        //println(parseFormat.format(date) + " = " + displayFormat.format(date))

        return displayFormat.format(date)
    }


    private fun _24To12Hours(time: String): String {


        val displayFormat = SimpleDateFormat("hh:mm a")
        val parseFormat = SimpleDateFormat("HH:mm:ss")
        var date: Date? = null
        try {
            date = parseFormat.parse(time)
        } catch (e: ParseException) {
            return ""
            e.printStackTrace()
        }

        //println("Display format : " + parseFormat.format(date) + " = " + displayFormat.format(date))

        return displayFormat.format(date)
    }

    private fun timeId(timeId: String): Int {
        for (i in multipleTime.indices) {
            if (timeId == multipleTime.get(i).time) {
                return multipleTime.get(i).id
            }
        }
        return -1
    }


}
