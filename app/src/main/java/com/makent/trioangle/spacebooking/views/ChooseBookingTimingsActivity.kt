package com.makent.trioangle.spacebooking.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.helper.Constants.SavedDateAndTime
import com.makent.trioangle.helper.Constants.SpaceAvailableTime
import com.makent.trioangle.spacebooking.adapter.ChooseTimeAdapter
import com.makent.trioangle.spacebooking.model.GetAvailableTime
import com.makent.trioangle.spacebooking.model.SaveDateAndTime
import com.makent.trioangle.util.CommonMethods
import kotlinx.android.synthetic.main.activity_choose_booking_timings.*
import kotlinx.android.synthetic.main.app_header.*
import java.io.Serializable
import javax.inject.Inject


class ChooseBookingTimingsActivity : AppCompatActivity(),ChooseTimeAdapter.onTimeClickListener {

    @Inject
    lateinit var commonMethods: CommonMethods

    private lateinit var chooseTimeAdapter:ChooseTimeAdapter
    private lateinit var dialog2:BottomSheetDialog

    private var getAvailableTime=GetAvailableTime()
    private var selectedStartTime=""
    private var selectedEndTime=""
    private var saveDateAndTime = SaveDateAndTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_booking_timings)
        AppController.getAppComponent().inject(this)

        tv_header_title.text=resources.getString(R.string.choose_timings)
        iv_Back.setOnClickListener {
            onBackPressed()
        }

        Log.e("ChooseTime","Choose Timing Booking");

        rltCheckinCheckOutDates.setOnClickListener {
            onBackPressed()
        }

        dialog2 = BottomSheetDialog(this@ChooseBookingTimingsActivity)
        dialog2.setContentView(R.layout.bottomsheet_choose_timing)
        val tvChoosedTime=dialog2.findViewById<TextView>(R.id.tvChoosedTime)
        val rvTimings=dialog2.findViewById<RecyclerView>(R.id.rvTimings)

        rvTimings!!.setHasFixedSize(false)
        rvTimings.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        getAvailableTime= intent.getSerializableExtra(SpaceAvailableTime) as GetAvailableTime

        tvSelectedCheckinDate.text=resources.getString(R.string.chin)+"\n"+commonMethods.DateFirstUserFormat(getAvailableTime.startDate)
        tvSelectedCheckOutdate.text=resources.getString(R.string.chout)+"\n"+commonMethods.DateFirstUserFormat(getAvailableTime.EndDate)

        tvSelectedStartTime.setOnClickListener {
            tvChoosedTime!!.text=resources.getString(R.string.start_time)
            chooseTimeAdapter=ChooseTimeAdapter(this,getAvailableTime.startTimes,"start_time",this)
            rvTimings.adapter=chooseTimeAdapter
            dialog2.show()
        }

        tvSelectedEndTime.setOnClickListener {
            if (!TextUtils.isEmpty(selectedStartTime)) {
                tvChoosedTime!!.text = resources.getString(R.string.end_time)
                chooseTimeAdapter = ChooseTimeAdapter(this, getAvailableTime.endTimes, "end_time", this)
                rvTimings.adapter = chooseTimeAdapter
                dialog2.show()
            }else{
                commonMethods.snackBar(resources.getString(R.string.error_start_time),"",false,2,tvSelectedEndTime,resources,this)
            }
        }

        btnDone.setOnClickListener {
            if (getAvailableTime.startDate == getAvailableTime.EndDate) {
                if (!commonMethods.checktimings(commonMethods.change12To24Hr(selectedStartTime), commonMethods.change12To24Hr(selectedEndTime))) {
                    commonMethods.snackBar(resources.getString(R.string.valid_time),"",false,2,tvSelectedEndTime,resources,this)
                    return@setOnClickListener
                }
            }
            /**
             * Api Passed Datas
             * Date as yyyy-MM-dd
             * time as HH:mm:ss
             */
            saveDateAndTime.StartDate = getAvailableTime.startDate
            saveDateAndTime.endDate = getAvailableTime.EndDate
            saveDateAndTime.startTime = commonMethods.change12To24Hr(selectedStartTime)
            saveDateAndTime.endTime = commonMethods.change12To24Hr(selectedEndTime)

            val intent = Intent()
            intent.putExtra(SavedDateAndTime, saveDateAndTime as Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onTimeClicked(type: String, selectedTime: String) {
        dialog2.dismiss()
        if (type.equals("start_time")){
            selectedStartTime=selectedTime
            tvSelectedStartTime.text=resources.getString(R.string.start_time)+"\n"+selectedTime
            if (!TextUtils.isEmpty(selectedEndTime)){
                selectedEndTime=""
                btnDone.isEnabled=false
                tvSelectedEndTime.text=resources.getString(R.string.end_time)
            }
        }else{
            selectedEndTime=selectedTime
            tvSelectedEndTime.text=resources.getString(R.string.end_time)+"\n"+selectedTime
            btnDone.isEnabled=true
        }
    }
}
