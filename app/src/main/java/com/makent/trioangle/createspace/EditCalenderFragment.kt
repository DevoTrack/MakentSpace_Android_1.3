package com.makent.trioangle.createspace

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.telephony.RadioAccessSpecifier
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ReadyHostModel.*
import com.makent.trioangle.createspace.interfaces.listItemClickListner
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.HostHome
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import kotlinx.android.synthetic.main.calendar_bottom_sheet.view.*
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditCalenderFragment : Fragment(), ServiceListener, MonthLoader.MonthChangeListener {

    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
        val matchedEvents = java.util.ArrayList<WeekViewEvent>()
        for (event : WeekViewEvent  in HostHome.events) {

            if (eventMatches(event, newYear, newMonth)) {
                //if (true) {
                // Log.v("events"+event.getStartTime(),""+event.getEndTime());
                matchedEvents.add(event)
            }
        }

        localSharedPreferences.saveSharedPreferences(Constants.NewMonth,newMonth)
        localSharedPreferences.saveSharedPreferences(Constants.NewYear, newYear)

        val calendar1 = Calendar.getInstance()
        if (newMonth == calendar1.get(Calendar.MONTH) + 1)
            _weekview.goToToday()
        // HostHome.events.add(weekViewEvent);

        //return getEvents(newYear,newMonth)
        return matchedEvents
    }

    private var dayList: ArrayList<String>  = ArrayList<String>()
    lateinit var mydialog: Dialog_loading


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var res: Resources

    lateinit var listner: GetReadyHostInterface

    lateinit var mActivity: GetReadyToHostActivity


    lateinit var btnContinue: Button

    lateinit var mContext: Context

    lateinit var  _weekview : WeekView


    lateinit var bottom_sheet : LinearLayout
    val _calendar = Calendar.getInstance()



    lateinit var blockedTimes: List<BlockedTimesItem>

    lateinit var availableTimes : List<AvailableTimesItem>

    lateinit var  notAvailableTimes : NotAvailableTimes

    lateinit var notAvailablehours : List<String>

    lateinit var notAvailablehour2 : List<String>
    lateinit var notAvailablehour3 : List<String>
    lateinit var notAvailablehour4 : List<String>
    lateinit var notAvailablehour5 : List<String>
    lateinit var notAvailablehour6 : List<String>
    lateinit var notAvailablehour7 : List<String>

    lateinit var readyToHostlist: ReadyToHost

    lateinit var datastatus :RadioGroup
    lateinit var radiobutton :RadioButton
    lateinit var selectedCals :List<Date>

    lateinit var notAvailableDays :List<Int>
    lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    var week : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_edit_calender,
                container, false)

        mContext = container!!.context

        _weekview = view.findViewById(R.id._weekView)
        _weekview.numberOfVisibleDays = 7
        _weekview.setMonthChangeListener(this)
        _weekview.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
        _weekview.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8f, resources.displayMetrics).toInt()
        _weekview.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
        _weekview.goToToday()
        _weekview.isShowDistinctPastFutureColor = true

        //sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet)

       // bottom_sheet.visibility = View.GONE


        initListner()
        init()

        loadCalendardetail()

        btnContinue = view.findViewById<Button>(R.id.btn_continue)

        btnContinue.setOnClickListener {

        }

        _weekview.setEmptyViewClickListener {

            val dateStr = _calendar.getTime()
            dateStr.setMinutes(0)
            dateStr.setSeconds(0)
            val readFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val weekFormat = SimpleDateFormat("EEE", Locale.ENGLISH)

            val writeFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
            val timeformat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            var date: Date? = null
            var day: String? = null
            try {
                date = readFormat.parse(dateStr.toString())
                day = weekFormat.format(_calendar.getTime())
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            var formattedDate = ""
            var formattedtime = ""
            if (date != null) {
                formattedDate = writeFormat.format(date)
                formattedtime = timeformat.format(date)
            }


            val selected_date_hours = StringBuilder()
            val selected_time_hours = StringBuilder()
            selected_date_hours.append(formattedDate)
            selected_time_hours.append(formattedtime)


            System.out.println("Selected Time " + selected_time_hours)

            val view = layoutInflater.inflate(R.layout.calendar_bottom_sheet, null)
            val dialog = BottomSheetDialog(mContext)
            dialog.setContentView(view)
            dialog.show()

            view.hostcalendar_cancel.setOnClickListener {
                dialog.dismiss()
            }
            view.hostcalendar_cancel_save.setOnClickListener {

            }

        }

        /*savechanges.setOnClickListener {

            val selectedId = datastatus.getCheckedRadioButtonId()
            radiobutton = view.findViewById(selectedId)
            val isavailable : String

            if (radiobutton.getText().toString() == resources.getString(R.string.available)) {
                isavailable = "Yes"
            } else {
                isavailable = "No"
            }

        }
*/
            return view
    }


    private fun updateAvailabilityParams(): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)

        return updateSpace
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else activity!!.resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as GetReadyToHostActivity?)!!

    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {

        if (mydialog.isShowing)
            mydialog.dismiss()

        onSuccessResponse(jsonResp)
    }

    private fun onSuccessResponse(jsonResp: JsonResponse?) {

    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    private fun init() {
        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is GetReadyHostInterface) {
            listner = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }
    fun getWeekView(): WeekView {
        return _weekview
    }

    private fun loadCalendardetail(){


        val year : SimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val yearvalue = Integer.parseInt(year.format(_calendar.getTime()))
        val monthval : SimpleDateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val all_month = Integer.parseInt(monthval.format(_calendar.getTime()))


        var month = _calendar.get(Calendar.MONTH)
        val week_of_month = _calendar.get(Calendar.WEEK_OF_MONTH)
        val day = _calendar.get(Calendar.DAY_OF_WEEK)

        val newMonth : Int = localSharedPreferences.getSharedPreferencesInt(Constants.NewMonth)
        val newYear : Int = localSharedPreferences.getSharedPreferencesInt(Constants.NewYear)

        if(month == all_month)
        {

            week = week_of_month + 1
        }
        else{
            week = 1
        }


        blockedTimes = mActivity.calendarData.blockedTimes as List<BlockedTimesItem>

        availableTimes = mActivity.calendarData.availableTimes as List<AvailableTimesItem>

        for( j in 0 until availableTimes.size)
        {
                val _enddate: String? = availableTimes.get(j).endDate
                val _startdate: String? = availableTimes.get(j).startDate
                val _starttime: String? = availableTimes.get(j).startTime
                val _endtime: String? = availableTimes.get(j).endTime
                val notes: String? = availableTimes.get(j).notes

                val _enddateArray = _enddate?.split("-")?.toTypedArray()
                val _startdateArray = _startdate?.split("-")?.toTypedArray()
                val _starttimeArray = _starttime?.split(":")?.toTypedArray()
                val _endtimeArray = _endtime?.split(":")?.toTypedArray()

                 val start_available : Calendar = Calendar.getInstance()

                //System.out.println("Avaliable End Time " + (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))

                start_available.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                start_available.set(Calendar.MINUTE, _starttimeArray?.get(1)!!.toInt())
                start_available.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                val end_available :Calendar = start_available.clone() as Calendar
                end_available.set(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                val weekViewEvent = WeekViewEvent()
                weekViewEvent.name =notes
                weekViewEvent.startTime = start_available
                weekViewEvent.endTime = end_available
                weekViewEvent.color = resources.getColor(R.color.brown)
                HostHome.events.add(weekViewEvent)

        }


        for (i in 0 until blockedTimes.size ) {
            try {
                val _enddate: String? = blockedTimes.get(i).endDate
                val _startdate: String? = blockedTimes.get(i).startDate
                val _starttime: String? = blockedTimes.get(i).startTime
                val _endtime: String? = blockedTimes.get(i).endTime
                val _notes : String? = blockedTimes.get(i).notes

                val _enddateArray = _enddate?.split("-")?.toTypedArray()
                val _startdateArray = _startdate?.split("-")?.toTypedArray()
                val _starttimeArray = _starttime?.split(":")?.toTypedArray()
                val _endtimeArray = _endtime?.split(":")?.toTypedArray()

                val start_block = Calendar.getInstance()

                System.out.println("End Time " + _endtimeArray?.get(0)!!.toInt())

                start_block.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                start_block.set(Calendar.MINUTE,_starttimeArray?.get(1)!!.toInt() )
                start_block.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                val end_block = start_block.clone() as Calendar
                end_block.add(Calendar.HOUR_OF_DAY,_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt())
                val weekViewEvent = WeekViewEvent()
                weekViewEvent.name = _notes
                weekViewEvent.startTime = start_block
                weekViewEvent.endTime = end_block
                weekViewEvent.color = resources.getColor(R.color.text_shadow)
                HostHome.events.add(weekViewEvent)
            }catch (b : Exception ){
                b.printStackTrace()
            }

        }

        val dateFormat_years = SimpleDateFormat("yyyyMMdd")
        val currentyear = Integer.parseInt(dateFormat_years.format(_calendar.getTime()))

        for(i in month until 12){

            /*for(w in week until 5) {
                if (w == 1) {
                    for (d in 0 until notAvailableDays.size) {
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, notAvailableDays[d])
                        startTime.set(Calendar.HOUR_OF_DAY, 0)
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 23)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        if(startTime.get(Calendar.WEEK_OF_MONTH) < week_of_month){
                            weekViewEvent.color = resources.getColor(R.color.text_light_gray)
                        }else{
                            weekViewEvent.color = resources.getColor(R.color.text_light_blue)
                        }
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (w == 2) {
                    for (d in 0 until notAvailableDays.size) {
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, week + 1)
                        startTime.set(Calendar.DAY_OF_WEEK, notAvailableDays[d])
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.HOUR_OF_DAY, 0)
                        val endTime = startTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 23)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        if(startTime.get(Calendar.WEEK_OF_MONTH) < week_of_month){
                            weekViewEvent.color = resources.getColor(R.color.text_light_gray)
                        }else{
                            weekViewEvent.color = resources.getColor(R.color.text_light_blue)
                        }
                        HostHome.events.add(weekViewEvent)
                    }
                }

                if (w == 3) {
                    for (d in 0 until notAvailableDays.size) {
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, notAvailableDays[d])
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.HOUR_OF_DAY, 0)
                        val endTime = startTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 23)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        if(startTime.get(Calendar.WEEK_OF_MONTH) < week_of_month){
                            weekViewEvent.color = resources.getColor(R.color.text_light_gray)
                        }else{
                            weekViewEvent.color = resources.getColor(R.color.text_light_blue)
                        }
                        HostHome.events.add(weekViewEvent)

                    }
                }

                if (w == 4) {
                    for (d in 0 until notAvailableDays.size) {
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, 0)
                        startTime.set(Calendar.DAY_OF_WEEK, notAvailableDays[d])
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime = startTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 23)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        if(startTime.get(Calendar.WEEK_OF_MONTH) < week_of_month){
                            weekViewEvent.color = resources.getColor(R.color.text_light_gray)
                        }else{
                            weekViewEvent.color = resources.getColor(R.color.text_light_blue)
                        }
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (w == 5) {
                    for (d in 0 until notAvailableDays.size) {
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, notAvailableDays[d])
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.HOUR_OF_DAY, 0)
                        val endTime = startTime.clone() as Calendar
                        endTime.add(Calendar.HOUR_OF_DAY, 23)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        if(startTime.get(Calendar.WEEK_OF_MONTH) < week_of_month){
                            weekViewEvent.color = resources.getColor(R.color.text_light_gray)
                        }else{
                            weekViewEvent.color = resources.getColor(R.color.text_light_blue)
                        }
                        HostHome.events.add(weekViewEvent)

                    }
                }
            }*/
        }

        notAvailableTimes = mActivity.calendarData.notAvailableTimes as NotAvailableTimes

        //notAvailablehours = notAvailableTimes.jsonMember1 as List<String>
       // notAvailablehour2 = notAvailableTimes.jsonMember2 as List<String>
        notAvailablehour3 = notAvailableTimes.jsonMember3 as List<String>
       // notAvailablehour4 = notAvailableTimes.jsonMember4 as List<String>
        //notAvailablehour5 = notAvailableTimes.jsonMember5 as List<String>
       // notAvailablehour6 = notAvailableTimes.jsonMember6 as List<String>
        //notAvailablehour7 = notAvailableTimes.jsonMember7 as List<String>


        /*if(notAvailablehours.size != null) {
            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 3) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
            }
        }

        if(notAvailablehour2.size != null){
            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }

                if (n == 3) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
            }
        }*/
        if(yearvalue == currentyear || yearvalue > currentyear) {
            if (notAvailablehour3.size != null) {
                for (n in week until 5) {
                    if (n == 1) {
                        for (h in 0 until notAvailablehour3.size) {
                            val not_avl_hours = notAvailablehour3.get(h)
                            val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                            val startTime: Calendar = Calendar.getInstance()
                            startTime.set(Calendar.WEEK_OF_MONTH, 1)
                            startTime.set(Calendar.DAY_OF_WEEK, 3)
                            startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                            startTime.set(Calendar.MINUTE, 0)
                            val endTime: Calendar = startTime.clone() as Calendar
                            endTime.add(Calendar.MINUTE, 59)
                            val weekViewEvent = WeekViewEvent()
                            /*if(n == week && startTime.get(Calendar.MONTH) == month) {
                                if (startTime.get(Calendar.DAY_OF_WEEK) == day) {
                                    weekViewEvent.color = R.color.day_back_color
                                } else {
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }
                            }
                                else{
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }*/

                            weekViewEvent.startTime = startTime
                            weekViewEvent.endTime = endTime
                            weekViewEvent.color = resources.getColor(R.color.yellow)
                            HostHome.events.add(weekViewEvent)

                        }
                    }
                    if (n == 2) {
                        for (h in 0 until notAvailablehour3.size) {
                            val not_avl_hours = notAvailablehour3.get(h)
                            val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                            val startTime: Calendar = Calendar.getInstance()
                            startTime.set(Calendar.WEEK_OF_MONTH, 2)
                            startTime.set(Calendar.DAY_OF_WEEK, 3)
                            startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                            startTime.set(Calendar.MINUTE, 0)
                            val endTime: Calendar = startTime.clone() as Calendar
                            endTime.add(Calendar.MINUTE, 59)
                            val weekViewEvent = WeekViewEvent()
                            /*if(n == week && startTime.get(Calendar.MONTH) == month) {
                                if (startTime.get(Calendar.DAY_OF_WEEK) == day) {
                                    weekViewEvent.color = R.color.day_back_color
                                } else {
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }
                            }
                            else{
                                weekViewEvent.color = resources.getColor(R.color.yellow)
                            }*/
                            weekViewEvent.startTime = startTime
                            weekViewEvent.endTime = endTime
                           weekViewEvent.color = resources.getColor(R.color.yellow)
                            HostHome.events.add(weekViewEvent)

                        }
                    }
                    if (n == 3) {
                        for (h in 0 until notAvailablehour3.size) {
                            val not_avl_hours = notAvailablehour3.get(h)
                            val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                            val startTime: Calendar = Calendar.getInstance()
                            startTime.set(Calendar.WEEK_OF_MONTH, 3)
                            startTime.set(Calendar.DAY_OF_WEEK, 3)
                            startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                            startTime.set(Calendar.MINUTE, 0)
                            val endTime: Calendar = startTime.clone() as Calendar
                            endTime.add(Calendar.MINUTE, 59)
                            val weekViewEvent = WeekViewEvent()
                            /*if(n == week && startTime.get(Calendar.MONTH) == month) {
                                if (startTime.get(Calendar.DAY_OF_WEEK) == day) {
                                    weekViewEvent.color = R.color.day_back_color
                                } else {
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }
                            }
                            else{
                                weekViewEvent.color = resources.getColor(R.color.yellow)
                            }*/
                            weekViewEvent.startTime = startTime
                            weekViewEvent.endTime = endTime
                            weekViewEvent.color = resources.getColor(R.color.yellow)
                            HostHome.events.add(weekViewEvent)

                        }
                    }
                    if (n == 4) {
                        for (h in 0 until notAvailablehour3.size) {
                            val not_avl_hours = notAvailablehour3.get(h)
                            val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                            val startTime: Calendar = Calendar.getInstance()
                            startTime.set(Calendar.WEEK_OF_MONTH, 4)
                            startTime.set(Calendar.DAY_OF_WEEK, 3)
                            startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                            startTime.set(Calendar.MINUTE, 0)
                            val endTime: Calendar = startTime.clone() as Calendar
                            endTime.add(Calendar.MINUTE, 59)
                            val weekViewEvent = WeekViewEvent()
                            /*if(n == week && startTime.get(Calendar.MONTH) == month) {
                                if (startTime.get(Calendar.DAY_OF_WEEK) == day) {
                                    weekViewEvent.color = R.color.day_back_color
                                } else {
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }
                            }
                            else{
                                weekViewEvent.color = resources.getColor(R.color.yellow)
                            }*/
                            weekViewEvent.startTime = startTime
                            weekViewEvent.endTime = endTime
                            weekViewEvent.color = resources.getColor(R.color.yellow)
                            HostHome.events.add(weekViewEvent)

                        }
                    }
                    if (n == 5) {
                        for (h in 0 until notAvailablehour3.size) {
                            val not_avl_hours = notAvailablehour3.get(h)
                            val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                            val startTime: Calendar = Calendar.getInstance()
                            startTime.set(Calendar.WEEK_OF_MONTH, 5)
                            startTime.set(Calendar.DAY_OF_WEEK, 3)
                            startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                            startTime.set(Calendar.MINUTE, 0)
                            val endTime: Calendar = startTime.clone() as Calendar
                            endTime.add(Calendar.MINUTE, 59)
                            val weekViewEvent = WeekViewEvent()
                            /*if(n == week && startTime.get(Calendar.MONTH) == month) {
                                if (startTime.get(Calendar.DAY_OF_WEEK) == day) {
                                    weekViewEvent.color = R.color.day_back_color
                                } else {
                                    weekViewEvent.color = resources.getColor(R.color.yellow)
                                }
                            }
                            else{
                                weekViewEvent.color = resources.getColor(R.color.yellow)
                            }*/
                            weekViewEvent.startTime = startTime
                            weekViewEvent.endTime = endTime
                            weekViewEvent.color = resources.getColor(R.color.yellow)
                            HostHome.events.add(weekViewEvent)

                        }
                    }
                }
            }

            /*if(notAvailablehour4.size != null){

            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 2){
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 3){
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 4){
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 5){
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
            }
        }

        if(notAvailablehour5.size != null){
            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 2){
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 3){
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n ==4){
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 5){
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                }
        }

        if(notAvailablehour6.size != null){
            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 2){
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 3){
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 4){
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n ==5){
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
            }
        }

        if(notAvailablehour7.size != null){
            for (n in week until 5) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 2){
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 3){
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if(n == 4){
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)

                    }
                }
                if( n == 5){
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.yellow)
                        HostHome.events.add(weekViewEvent)
                    }
                }
            }
        }*/

        }
        getWeekView().notifyDatasetChanged()


    }




    private fun eventMatches(event: WeekViewEvent, year: Int, month: Int): Boolean {


        return event.startTime.get(Calendar.YEAR) == year && event.startTime.get(Calendar.MONTH) == month - 1 || event.endTime.get(Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1
    }


    private fun setTimeInterptreter(startdate : Boolean){

        _calendar
       // val weekDateFormat : SimpleDateFormat
       val pattern: String?  = "yyyy-MM-dd"
       val weekDateFormat :SimpleDateFormat = SimpleDateFormat(pattern)
       val date : String? = weekDateFormat.format(Date())
        val hour : Int?

        _weekview.dateTimeInterpreter.interpretTime(12)
       System.out.println(date)


    }


}