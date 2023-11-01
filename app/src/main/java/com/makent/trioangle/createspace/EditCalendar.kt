package com.makent.trioangle.createspace

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.RectF
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alamkanak.weekview.DateTimeInterpreter
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
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.HostHome
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import com.squareup.timessquare.HostCalendarPickerView
import kotlinx.android.synthetic.main.calendar_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_edit_calender.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import java.text.ParseException as ParseException1

class EditCalendar : AppCompatActivity(), MonthLoader.MonthChangeListener, ServiceListener, WeekView.EventClickListener {

    @Inject
    lateinit var commonMethods: CommonMethods
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var gson: Gson

    private lateinit var _weekview: WeekView
    private val _calendar = Calendar.getInstance()
    lateinit var listener: GetReadyHostInterface
    lateinit var mydialog: Dialog_loading
    lateinit var bundle: Bundle
    lateinit var spaceFragment: SpaceBookingtype

    lateinit var blockedTimes: List<BlockedTimesItem>

    lateinit var availableTimes: List<AvailableTimesItem>

    lateinit var notAvailableTimes: NotAvailableTimes

    lateinit var localSharedPreferences: LocalSharedPreferences

    var notAvailablehours: List<String> = ArrayList()
    var notAvailablehour2: List<String> = ArrayList()
    var notAvailablehour3: List<String> = ArrayList()
    var notAvailablehour4: List<String> = ArrayList()
    var notAvailablehour5: List<String> = ArrayList()
    var notAvailablehour6: List<String> = ArrayList()
    var notAvailablehour7: List<String> = ArrayList()

    val TAGGED_PRINT: String = "TAGGED PRINT : "

    lateinit var readyToHostlist: ReadyToHost

    lateinit var datastatus: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var selectedCals: List<Date>

    lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    var week: Int = 0
    lateinit var mActivity: GetReadyToHostActivity
    lateinit var readyToHost: ReadyToHost
    lateinit var calendarData: CalendarData
    var calendarDatas: Serializable? = null
    lateinit var calendar: HostCalendarPickerView
    lateinit var pref: SharedPreferences

    lateinit var updateModel: UpdateModel
    lateinit var updateCalendardatas: CalendarData
    lateinit var updateAvailableTimesItem: List<AvailableTimesItem>
    lateinit var updateBlockedTimesItem: List<UpdateBlockedTimesItem>
    lateinit var notAvailableDaysUpdate: List<Int>
    lateinit var notAvailableTimesUpdate: UpdateNotAvailableTimes

    internal var updatecal = false
    protected var isInternetAvailable: Boolean = false
    internal var availablelist = ArrayList<HashMap<String, String>>()
    internal var blocklist = ArrayList<HashMap<String, String>>()
    lateinit var tv_week_days: TextView
    var _enddate: String = ""
    var _startdate: String = ""
    var _starttime: String = ""
    var _endtime: String = ""
    var notes: String = ""
    var isback: Boolean = false

    var value: Int = 0
    val month = _calendar.get(Calendar.MONTH)
    var day1: Int = 0
    var day2: Int = 0
    var day3: Int = 0
    var day4: Int = 0
    var day5: Int = 0
    var day6: Int = 0
    var day7: Int = 0

    var newMonth: Int = 0

    val CurrentDateAndTime = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_calender)
        AppController.getAppComponent().inject(this)

        init()

        tv_week_days = findViewById(R.id.tv_week_days)
        _weekview = findViewById(R.id._weekView)
        _weekview.numberOfVisibleDays = 7
        //_weekview.goToToday()
        _weekview.goToDate(_calendar)
        _weekview.setMonthChangeListener(this)
        _weekview.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
        _weekview.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8f, resources.displayMetrics).toInt()
        _weekview.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13f, resources.displayMetrics).toInt()
        _weekview.isShowDistinctPastFutureColor = true
        _weekview.setOnEventClickListener(this)

        setupDateTimeInterpreter(true)

        getIntentValue()
        loadCalendardetail()
        //initListner()

        // calendar_list()

        _weekview.setEmptyViewClickListener(WeekView.EmptyViewClickListener { time ->
            //timed=time;
            val avail = false
            if (time.time.after(CurrentDateAndTime.time)) {
                val dateStr = time.time
                dateStr.minutes = 0
                dateStr.seconds = 0
                val readFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val weekFormat = SimpleDateFormat("EEE", Locale.ENGLISH)

                val writeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val timeformat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
                var date: Date? = null
                var day: String? = null
                try {
                    date = readFormat.parse(dateStr.toString())
                    day = weekFormat.format(time.time)
                } catch (e: ParseException1) {
                    e.printStackTrace()
                }


                println("available=$avail")
                var formattedDate = ""
                var formattedtime = ""

                if (date != null) {
                    formattedDate = writeFormat.format(date)
                    formattedtime = timeformat.format(date)
                }

                println("Selected date in search format $formattedDate")
                println("Selected date in search timeformat $formattedtime")


                val selected_dates_hour = StringBuilder()
                val selected_time_hour = StringBuilder()
                selected_dates_hour.append(formattedDate)
                selected_time_hour.append(formattedtime)

                println("selected hour : $selected_dates_hour")
                println("selected time : $selected_time_hour")

                val view = layoutInflater.inflate(R.layout.calendar_bottom_sheet, null)
                val dialog = BottomSheetDialog(this)
                dialog.setContentView(view)
                dialog.show()
                datastatus = view.findViewById(R.id.datestatus)

                view.hostcalendar_cancel.setOnClickListener {
                    dialog.dismiss()
                }
                view.hostcalendar_save.setOnClickListener {

                    val selectedId = datastatus.checkedRadioButtonId

                    // find the radiobutton by returned id
                    radioButton = view.findViewById(selectedId) as RadioButton

                    //Toast.makeText(this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                    val isavailable: String

                    if (radioButton.getText().toString() == resources.getString(R.string.available)) {
                        isavailable = "No"
                    } else {
                        isavailable = "Yes"
                    }

                    var notes: String = ""
                    if (view._et_add_note.text.toString().equals("")) {
                        notes = ""
                    } else {
                        notes = view._et_add_note.text.toString()
                    }

                    System.out.println("Notes is " + notes)
                    val userid: String = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
                    if (!mydialog.isShowing)
                        mydialog.show()
                    apiService.updateCalendar(userid, localSharedPreferences.getSharedPreferences(Constants.mSpaceId), selected_dates_hour.toString(), selected_time_hour.toString(), notes, isavailable).enqueue(RequestCallback(this))
                    updatecal = true
                    dialog.dismiss()

                }

                view._add_arrow.setOnClickListener {
                    view._et_add_note.visibility = View.VISIBLE
                }

                view.start_date_num.text = selected_dates_hour.toString()
                view.end_date_num.text = selected_dates_hour.toString()
                view.start_time_num.text = selected_time_hour.toString()
                _calendar.setTime(date);
                _calendar.add(Calendar.HOUR, 1);
                val timeval = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
                view.end_time_num.text = timeval.format(_calendar.time)
            }

        })

        btn_continue.setOnClickListener {
            val movetoEdit = Intent(this, EditListingActivity::class.java)
            clearEvents()
            movetoEdit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            movetoEdit.putExtra("tabsaved", 2)
            startActivity(movetoEdit)
            finish()
        }
    }

    private fun clearEvents() {
        HostHome.events.clear()
        HostHome.dayevents.clear()
    }

    private fun getIntentValue() {
        readyToHost = (intent.extras!!.getSerializable(Constants.READYTOHOSTSTEP) as? ReadyToHost)!!
        calendarData = readyToHost.calendarData as CalendarData

        Log.e("calendar","calendar data"+calendarData.availableTimes);
    }

    override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
        /**
         * Checking if the cell has any available or blocked dates
         * if it had Bottomsheet will Show
         */
        if (event!!.startTime.time.after(CurrentDateAndTime.time) && (event!!.color == ContextCompat.getColor(this, R.color.blocked_hours) || event!!.color == ContextCompat.getColor(this, R.color.available_hours))) {

            val dateStr = event?.startTime?.time
            dateStr?.minutes = 0
            dateStr?.seconds = 0

            val readFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val weekFormat = SimpleDateFormat("EEE", Locale.ENGLISH)

            val writeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val timeformat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            var startTime: Date? = null

            try {
                startTime = readFormat.parse(dateStr.toString())
            } catch (e: ParseException1) {
                e.printStackTrace()
            }
            var formattedDate = ""
            var formattedtime = ""
            if (startTime != null) {
                formattedDate = writeFormat.format(startTime)
                formattedtime = timeformat.format(startTime)
            }

            val selected_dates_hour = StringBuilder()
            val selected_time_hour = StringBuilder()
            selected_dates_hour.append(formattedDate)
            selected_time_hour.append(formattedtime)

            // val endTime : String = event?.endTime.toString()

            val view = layoutInflater.inflate(R.layout.calendar_bottom_sheet, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()
            datastatus = view.findViewById(R.id.datestatus)

            view.hostcalendar_cancel.setOnClickListener {
                dialog.dismiss()
            }
            view.hostcalendar_save.setOnClickListener {

                val selectedId = datastatus.checkedRadioButtonId

                // find the radiobutton by returned id
                radioButton = view.findViewById(selectedId) as RadioButton

                //Toast.makeText(this,radioButton.getText(), Toast.LENGTH_SHORT).show();
                val isavailable: String

                if (radioButton.getText().toString() == resources.getString(R.string.available)) {
                    isavailable = "No"
                } else {
                    isavailable = "Yes"
                }

                var notes: String = ""
                if (view._et_add_note.text.toString().equals("")) {
                    notes = ""
                } else {
                    notes = view._et_add_note.text.toString()
                }
                System.out.println("Notes is " + notes)
                val userid: String = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
                if (!mydialog.isShowing)
                    mydialog.show()
                apiService.updateCalendar(userid, localSharedPreferences.getSharedPreferences(Constants.mSpaceId), selected_dates_hour.toString(), selected_time_hour.toString(), notes, isavailable).enqueue(RequestCallback(this))
                updatecal = true
                dialog.dismiss()

            }

            view._note_lay.setOnClickListener {
                view._et_add_note.visibility = View.VISIBLE
            }

            var empty: String = ""
            view.start_date_num.text = selected_dates_hour.toString()
            view.end_date_num.text = selected_dates_hour.toString()
            view.start_time_num.text = selected_time_hour.toString()
            _calendar.setTime(startTime);
            _calendar.add(Calendar.HOUR, 1);
            val timeval = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            view.end_time_num.text = timeval.format(_calendar.time)
           /* if (event?.name != null) {
                view._et_add_note.text = event?.name!!.toEditable()
            } else {
                view._et_add_note.text = empty.toEditable()
            }*/
            view._et_add_note.text = empty.toEditable()
        }

    }

    /* private fun updateHour() {
         val userid : String = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
         apiService.updateCalendar(userid,localSharedPreferences.getSharedPreferences(Constants.mSpaceId), selected_dates_hour.toString(), selected_time_hour.toString(), isavailable, update_nightprice, "Hour").enqueue(RequestCallback())
     }*/

    private fun init() {
        localSharedPreferences = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun loadCalendardetail() {

        HostHome.events.clear()
        val monthval: SimpleDateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val all_month = Integer.parseInt(monthval.format(_calendar.getTime()))

        val week_of_month = _calendar.get(Calendar.WEEK_OF_MONTH)
        val day = _calendar.get(Calendar.DAY_OF_WEEK)

        System.out.println("Current Day " + day)


        val date: java.util.Date = Date()
        val _cal = Calendar.getInstance()
        _cal.setTime(date);
        val currentMonth = _cal.get(Calendar.MONTH) + 1

        /*if (currentMonth == all_month) {
            week = week_of_month + 1
        } else {
            week = 1
        }*/
        week = 1
        println("Load calendar details : " + month)

        pref = applicationContext.getSharedPreferences("MyPref", 0)
        val all_year: Int = pref.getInt("Year", -1)
        println("All Year $all_year")
        readyToHost = (intent.extras!!.getSerializable(Constants.READYTOHOSTSTEP) as? ReadyToHost)!!
        calendarData = readyToHost.calendarData as CalendarData
        blockedTimes = calendarData.blockedTimes as List<BlockedTimesItem>

        availableTimes = calendarData.availableTimes as List<AvailableTimesItem>
        availablelist.clear()

        Log.e("available","available times"+availableTimes.size);
        Log.e("availabel","available list"+availablelist.size);

        for (j in 0 until availableTimes.size) {

            val hm = HashMap<String, String>()
            hm["startTime"] = availableTimes.get(j).startTime.toString()
            hm["startDate"] = availableTimes.get(j).startDate.toString()
            hm["endDate"] = availableTimes.get(j).endDate.toString()
            hm["endTime"] = availableTimes.get(j).endTime.toString()
            hm["notes"] = availableTimes.get(j).notes.toString()
            availablelist.add(hm)
        }

        for (j in 0 until availablelist.size) {

            _enddate = availablelist.get(j).get("endDate").toString()
            _startdate = availablelist.get(j).get("startDate").toString()
            _starttime = availablelist.get(j).get("startTime").toString()
            _endtime = availablelist.get(j).get("endTime").toString()
            notes = availablelist.get(j).get("notes").toString()

            val _enddateArray = _enddate.split("-").toTypedArray()
            val _startdateArray = _startdate?.split("-")?.toTypedArray()
            val _starttimeArray = _starttime?.split(":")?.toTypedArray()
            val _endtimeArray = _endtime?.split(":")?.toTypedArray()

            val start_available: Calendar = Calendar.getInstance()

            //System.out.println("Avaliable End Time " + (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
            start_available.set(Calendar.MINUTE, 0)
            start_available.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
            start_available.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
            start_available.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
            start_available.set(Calendar.MONTH, _startdateArray?.get(1).toInt() - 1)
            val end_available: Calendar = start_available.clone() as Calendar
            // end_available.set(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
            end_available.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
            end_available.set(Calendar.MINUTE, 59)
            val weekViewEvent = WeekViewEvent()
            weekViewEvent.name = notes
            weekViewEvent.startTime = start_available
            weekViewEvent.endTime = end_available
            weekViewEvent.color = resources.getColor(R.color.available_hours)
            HostHome.events.add(weekViewEvent)

        }

        blocklist.clear()
        for (i in 0 until blockedTimes.size) {

            // _et_add_note.text = blockedTimes.get(i).notes.toString().toEditable()

            val hm = HashMap<String, String>()
            hm["startTime"] = blockedTimes.get(i).startTime.toString()
            hm["startDate"] = blockedTimes.get(i).startDate.toString()
            hm["endDate"] = blockedTimes.get(i).endDate.toString()
            hm["endTime"] = blockedTimes.get(i).endTime.toString()
            hm["notes"] = blockedTimes.get(i).notes.toString()
            hm["source"] = blockedTimes.get(i).source.toString()
            blocklist.add(hm)
        }

        for (i in 0 until blocklist.size) {
            try {
                val _enddate: String? = blocklist.get(i).get("endDate").toString()
                val _startdate: String? = blocklist.get(i).get("startDate").toString()
                val _starttime: String? = blocklist.get(i).get("startTime").toString()
                val _endtime: String? = blocklist.get(i).get("endTime").toString()
                var _notes: String? = blocklist.get(i).get("notes").toString()
                val source: String? = blocklist.get(i).get("source").toString()

                if (_notes.equals("null"))
                {
                    _notes = ""
                }

                val _enddateArray = _enddate?.split("-")?.toTypedArray()
                val _startdateArray = _startdate?.split("-")?.toTypedArray()
                val _starttimeArray = _starttime?.split(":")?.toTypedArray()
                val _endtimeArray = _endtime?.split(":")?.toTypedArray()

                if (source.equals("Calendar")) {
                    val start_block = Calendar.getInstance()
                    System.out.println("End Time " + _endtimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, 0)
                    start_block.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
                    start_block.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                    start_block.set(Calendar.MONTH, _startdateArray.get(1).toInt() - 1)
                    val end_block = start_block.clone() as Calendar
                    // end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt())
                    if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()).equals(1)) {
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 59)
                    } else if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && _endtimeArray?.get(0)!!.toInt() <= 12) {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    } else {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        if (_endtimeArray?.get(0)!!.toInt() > 12) {
                            end_block.add(Calendar.HOUR_OF_DAY, (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
                        } else {
                            end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        }
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    }

                    val weekViewEvent = WeekViewEvent()
                    weekViewEvent.name = _starttimeArray?.get(0).toString() + ":" + _starttimeArray?.get(1).toString() + "-" + _endtimeArray?.get(0).toString() + ":" + _endtimeArray?.get(1) + " " + _notes
                    weekViewEvent.startTime = start_block
                    weekViewEvent.endTime = end_block
                    weekViewEvent.color = resources.getColor(R.color.blocked_hours)
                    HostHome.events.add(weekViewEvent)
                }
                if (source.equals("Reservation")) {
                    val start_block = Calendar.getInstance()
                    System.out.println("End Time " + _endtimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, 0)
                    start_block.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
                    start_block.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                    start_block.set(Calendar.MONTH, _startdateArray.get(1).toInt() - 1)
                    val end_block = start_block.clone() as Calendar
                    if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()).equals(1)) {
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 59)
                    } else {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        if (_endtimeArray?.get(0)!!.toInt() > 12) {
                            end_block.add(Calendar.HOUR_OF_DAY, (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
                        } else {
                            end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        }
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    }

                    val weekViewEvent = WeekViewEvent()
                    weekViewEvent.name = _starttimeArray?.get(0).toString() + ":" + _starttimeArray?.get(1).toString() + "-" + _endtimeArray?.get(0).toString() + ":" + _endtimeArray?.get(1) + " " + _notes
                    weekViewEvent.startTime = start_block
                    weekViewEvent.endTime = end_block
                    weekViewEvent.color = resources.getColor(R.color.reservation_clr)
                    HostHome.events.add(weekViewEvent)
                }
            } catch (b: Exception) {
                b.printStackTrace()
            }

        }

        notAvailableTimes = calendarData.notAvailableTimes as NotAvailableTimes

        notAvailablehours = notAvailableTimes.jsonMember1 as List<String>
        notAvailablehour2 = notAvailableTimes.jsonMember2 as List<String>
        notAvailablehour3 = notAvailableTimes.jsonMember3 as List<String>
        notAvailablehour4 = notAvailableTimes.jsonMember4 as List<String>
        notAvailablehour5 = notAvailableTimes.jsonMember5 as List<String>
        notAvailablehour6 = notAvailableTimes.jsonMember6 as List<String>
        notAvailablehour7 = notAvailableTimes.jsonMember7 as List<String>

        if (notAvailablehours.size != null) {
            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehours.size) {
                        val not_avl_hours = notAvailablehours.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 1)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)
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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour2.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour2.size) {
                        val not_avl_hours = notAvailablehour2.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 2)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour3.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour3.size) {
                        val not_avl_hours = notAvailablehour3.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 3)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
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
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

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
                        startTime.set(Calendar.MONTH, month)
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
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour4.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 3) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehour4.size) {
                        val not_avl_hours = notAvailablehour4.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 4)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour5.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 3) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehour5.size) {
                        val not_avl_hours = notAvailablehour5.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 5)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour6.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 3) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array[0].toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehour6.size) {
                        val not_avl_hours = notAvailablehour6.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 6)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
            }
        }

        if (notAvailablehour7.size != null) {

            for (n in week until 6) {
                if (n == 1) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 1)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 2) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 2)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 3) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 3)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 4) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 4)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)

                    }
                }
                if (n == 5) {
                    for (h in 0 until notAvailablehour7.size) {
                        val not_avl_hours = notAvailablehour7.get(h)
                        val not_avl_hours_array = not_avl_hours.split(":").toTypedArray()
                        val startTime: Calendar = Calendar.getInstance()
                        startTime.set(Calendar.WEEK_OF_MONTH, 5)
                        startTime.set(Calendar.DAY_OF_WEEK, 7)
                        startTime.set(Calendar.HOUR_OF_DAY, not_avl_hours_array.get(0).toInt())
                        startTime.set(Calendar.MINUTE, 0)
                        startTime.set(Calendar.MONTH, month)
                        val endTime: Calendar = startTime.clone() as Calendar
                        endTime.add(Calendar.MINUTE, 59)
                        val weekViewEvent = WeekViewEvent()
                        weekViewEvent.startTime = startTime
                        weekViewEvent.endTime = endTime
                        weekViewEvent.color = resources.getColor(R.color.blocked_days)
                        HostHome.dayevents.add(weekViewEvent)
                    }
                }
            }
        }

        for (d in day until 8) {
            when (d) {
                1 -> getNotAvailableDays(notAvailablehours, week_of_month, d)
                2 -> getNotAvailableDays(notAvailablehour2, week_of_month, d)
                3 -> getNotAvailableDays(notAvailablehour3, week_of_month, d)
                4 -> getNotAvailableDays(notAvailablehour4, week_of_month, d)
                5 -> getNotAvailableDays(notAvailablehour5, week_of_month, d)
                6 -> getNotAvailableDays(notAvailablehour6, week_of_month, d)
                7 -> getNotAvailableDays(notAvailablehour7, week_of_month, d)
            }
        }


        /* val dateFormat_years = SimpleDateFormat("yyyyMMdd")
         val currentyear = Integer.parseInt(dateFormat_years.format(_calendar.getTime()))*/


        getWeekView().notifyDatasetChanged()

    }

    private fun getNotAvailableDays(notAvailableHours: List<String>, week_of_month: Int, d: Int) {
        for (h in 0 until notAvailableHours.size) {
            val notAvailHours = notAvailableHours[h]
            val notAvailHoursArray = notAvailHours.split(":").toTypedArray()
            val startTime: Calendar = Calendar.getInstance()
            startTime.set(Calendar.WEEK_OF_MONTH, week_of_month)
            startTime.set(Calendar.DAY_OF_WEEK, d)
            startTime.set(Calendar.HOUR_OF_DAY, notAvailHoursArray[0].toInt())
            startTime.set(Calendar.MINUTE, 0)
            startTime.set(Calendar.MONTH, month)
            val endTime: Calendar = startTime.clone() as Calendar
            endTime.add(Calendar.MINUTE, 59)
            val weekViewEvent = WeekViewEvent()
            weekViewEvent.startTime = startTime
            weekViewEvent.endTime = endTime
            weekViewEvent.color = ContextCompat.getColor(this, R.color.blocked_days)
            HostHome.dayevents.add(weekViewEvent)
        }
    }

    private fun getWeekView(): WeekView {
        return _weekview
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
        onSuccessCalendarUpdate(jsonResp)
        // startActivity(getIntent());

    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    fun refresh() {
        val intent: Intent = getIntent()
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    override fun onMonthChange(newYear: Int, newMonth: Int): List<out WeekViewEvent> {
        val matchedEvents = java.util.ArrayList<WeekViewEvent>()
        val week_of_month = _calendar.get(Calendar.WEEK_OF_MONTH)
        //val current_day = _calendar.get(Calendar.)
        val current_year = _calendar.get(Calendar.YEAR)
        //val date = _calendar.get(Calendar.DATE);
        //val day = _calendar.get(Calendar.DAY_OF_WEEK)'
        val dates: java.util.Date = Date()
        val _cal = Calendar.getInstance()
        _cal.setTime(dates)
        val currentMonth = _cal.get(Calendar.MONTH) //+ 1
        System.out.println("New Month " + newMonth)
        System.out.println("Current Month Value " + currentMonth)
        System.out.println("week_of_month" + week_of_month)

        /*if (currentMonth == newMonth) {
            week = week_of_month + 1
        } else {
            week = 1
        }*/
        week = 1
        println("week $week")
        val monthval: SimpleDateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val all_month = Integer.parseInt(monthval.format(_calendar.getTime()))

        /*val startTime: Calendar = Calendar.getInstance()
     startTime.set(Calendar.DAY_OF_YEAR, --value)
     startTime.set(Calendar.MINUTE, 0)
     val endTime: Calendar = startTime.clone() as Calendar
     endTime.set(Calendar.DATE,date-1);
     endTime.add(Calendar.MINUTE, 59)
     val weekViewEvent = WeekViewEvent()
     weekViewEvent.startTime = startTime
     weekViewEvent.endTime = endTime
     weekViewEvent.color = resources.getColor(R.color.text_light_gray)
     HostHome.events.add(weekViewEvent)*/

        notAvailableTimes = calendarData.notAvailableTimes as NotAvailableTimes

        /*notAvailablehours.clear()
        notAvailablehour2.clear()
        notAvailablehour3.clear()
        notAvailablehour4.clear()
        notAvailablehour5.clear()
        notAvailablehour6.clear()
        notAvailablehour7.clear()*/


        notAvailablehours = notAvailableTimes.jsonMember1 as List<String>
        notAvailablehour2 = notAvailableTimes.jsonMember2 as List<String>
        notAvailablehour3 = notAvailableTimes.jsonMember3 as List<String>
        notAvailablehour4 = notAvailableTimes.jsonMember4 as List<String>
        notAvailablehour5 = notAvailableTimes.jsonMember5 as List<String>
        notAvailablehour6 = notAvailableTimes.jsonMember6 as List<String>
        notAvailablehour7 = notAvailableTimes.jsonMember7 as List<String>

        println("$TAGGED_PRINT Week $week")
        println(TAGGED_PRINT + " currentMonth " + currentMonth)
        println(TAGGED_PRINT + " newMonth " + newMonth)


        checkWithDays(notAvailablehours, newYear, newMonth, 1)
        checkWithDays(notAvailablehour2, newYear, newMonth, 2)
        checkWithDays(notAvailablehour3, newYear, newMonth, 3)
        checkWithDays(notAvailablehour4, newYear, newMonth, 4)
        checkWithDays(notAvailablehour5, newYear, newMonth, 5)
        checkWithDays(notAvailablehour6, newYear, newMonth, 6)
        checkWithDays(notAvailablehour7, newYear, newMonth, 7)

        getWeekView().notifyDatasetChanged()

        for (j in HostHome.dayevents.size - 1 downTo 0) {
            for (i in HostHome.events.size - 1 downTo 0) {
                if (isEventsCollide(HostHome.events.get(i), HostHome.dayevents.get(j))) {
                    HostHome.dayevents.removeAt(j)
                }
            }
        }

        for (j in HostHome.dayevents.size - 1 downTo 0) {
            if (isPastDate(HostHome.dayevents.get(j).startTime)) {
                HostHome.dayevents.removeAt(j)
            }
        }

        for (event: WeekViewEvent in HostHome.events) {

            println("Host home events : " + event.id)
            println("Host home events : " + event.startTime.get(Calendar.HOUR_OF_DAY))
            println("Host home events : " + event.endTime.get(Calendar.HOUR_OF_DAY))

            if (eventMatches(event, newYear, newMonth)) {
                //fetchEventsInBackground()
                //if (true) {
                // Log.v("events"+event.getStartTime(),""+event.getEndTime());
                matchedEvents.add(event)
            }
        }

        for (event: WeekViewEvent in HostHome.dayevents) {

            println("Host day events : " + event.id)
            println("Host day events : " + event.startTime.get(Calendar.HOUR_OF_DAY))
            println("Host day events : " + event.endTime.get(Calendar.HOUR_OF_DAY))
            if (eventMatches(event, newYear, newMonth)) {
                //fetchEventsInBackground()
                //if (true) {
                // Log.v("events"+event.getStartTime(),""+event.getEndTime());


                matchedEvents.add(event)
            }
        }

        /*val calendar1 = Calendar.getInstance()
     if (newMonth == calendar1.get(Calendar.MONTH) + 1)
         _weekview.goToToday()*/
        // HostHome.events.add(weekViewEvent);

        //return getEvents(newYear,newMonth)

        val year: SimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val yearvalue = Integer.parseInt(year.format(_calendar.getTime()))
        pref = applicationContext.getSharedPreferences("MyPref", 0)
        val yearval = pref.getInt("Year", -1)
        /*System.out.println(" Year Value " + yearval)*/

        return matchedEvents

    }


    /**
     * Checks if two events overlap.
     * @param event1 The first event.
     * @param event2 The second event.
     * @return true if the events overlap.
     */
    private fun isEventsCollide(event1: WeekViewEvent, event2: WeekViewEvent): Boolean {
        event1.startTime.time
        val start1 = event1.startTime.timeInMillis
        val end1 = event1.endTime.timeInMillis
        val start2 = event2.startTime.timeInMillis
        val end2 = event2.endTime.timeInMillis

        return !(start1 >= end2 || end1 <= start2)
    }


    private fun isPastDate(weekView: Calendar): Boolean {

        if (weekView.before(Calendar.getInstance())) {
            return true
        } else {
            return false
        }

    }

    private fun checkWithDays(notAvailableDayhours: List<String>, newYear: Int, newMonth: Int, day: Int) {

        if (notAvailableDayhours.isNotEmpty()) {
            for (n in week until 6) {

                for (h in 0 until notAvailableDayhours.size) {
                    val notAvailableHours = notAvailableDayhours[h]
                    val notAvailableHoursArray = notAvailableHours.split(":").toTypedArray()
                    val startTime: Calendar = Calendar.getInstance()
                    startTime.set(Calendar.WEEK_OF_MONTH, n)
                    startTime.set(Calendar.DAY_OF_WEEK, day)
                    startTime.set(Calendar.HOUR_OF_DAY, notAvailableHoursArray[0].toInt())
                    startTime.set(Calendar.MINUTE, 0)
                    startTime.set(Calendar.MONTH, newMonth)
                    startTime.set(Calendar.YEAR, newYear)
                    val endTime: Calendar = startTime.clone() as Calendar
                    endTime.add(Calendar.MINUTE, 59)
                    val weekViewEvent = WeekViewEvent()
                    weekViewEvent.startTime = startTime
                    weekViewEvent.endTime = endTime
                    weekViewEvent.color = ContextCompat.getColor(this, R.color.blocked_days)
                    HostHome.dayevents.add(weekViewEvent)

                }
            }

        }
    }

    private fun onSuccessCalendarUpdate(jsonResp: JsonResponse?) {

        updateModel = gson.fromJson(jsonResp?.strResponse, UpdateModel::class.java)


        /*   val year: SimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
           val yearvalue = Integer.parseInt(year.format(_calendar.getTime()))
           val monthval: SimpleDateFormat = SimpleDateFormat("MM", Locale.getDefault())
           val all_month = Integer.parseInt(monthval.format(_calendar.getTime()))

           val week_of_month = _calendar.get(Calendar.WEEK_OF_MONTH)
           val day = _calendar.get(Calendar.DAY_OF_WEEK)

           System.out.println("Current Day " + day)


           val date : java.util.Date = Date()
           val _cal = Calendar.getInstance()
           _cal.setTime(date);
           val currentMonth = _cal.get(Calendar.MONTH) + 1

           if (currentMonth == all_month) {
               week = week_of_month + 1
           } else {
               week = 1
           }*/


        calendarData = updateModel.updateCalendarData as CalendarData

        updateAvailableTimesItem = calendarData.availableTimes as List<AvailableTimesItem>

        blockedTimes = calendarData.blockedTimes as List<BlockedTimesItem>


        notAvailableTimes = calendarData.notAvailableTimes as NotAvailableTimes



        notAvailablehours = notAvailableTimes.jsonMember1 as List<String>
        notAvailablehour2 = notAvailableTimes.jsonMember2 as List<String>
        notAvailablehour3 = notAvailableTimes.jsonMember3 as List<String>
        notAvailablehour4 = notAvailableTimes.jsonMember4 as List<String>
        notAvailablehour5 = notAvailableTimes.jsonMember5 as List<String>
        notAvailablehour6 = notAvailableTimes.jsonMember6 as List<String>
        notAvailablehour7 = notAvailableTimes.jsonMember7 as List<String>




        availablelist.clear()


        for (j in 0 until updateAvailableTimesItem.size) {

            // _et_add_note.text = updateAvailableTimesItem.get(j).notes.toString().toEditable()

            val hm = HashMap<String, String>()
            hm["startTime"] = updateAvailableTimesItem.get(j).startTime.toString()
            hm["startDate"] = updateAvailableTimesItem.get(j).startDate.toString()
            hm["endDate"] = updateAvailableTimesItem.get(j).endDate.toString()
            hm["endTime"] = updateAvailableTimesItem.get(j).endTime.toString()
            hm["notes"] = updateAvailableTimesItem.get(j).notes.toString()
            availablelist.add(hm)

        }

        HostHome.events.clear()
        for (j in 0 until availablelist.size) {

            _enddate = availablelist.get(j).get("endDate").toString()
            _startdate = availablelist.get(j).get("startDate").toString()
            _starttime = availablelist.get(j).get("startTime").toString()
            _endtime = availablelist.get(j).get("endTime").toString()
            notes = availablelist.get(j).get("notes").toString()


            val _enddateArray = _enddate?.split("-")?.toTypedArray()
            val _startdateArray = _startdate?.split("-")?.toTypedArray()
            val _starttimeArray = _starttime?.split(":")?.toTypedArray()
            val _endtimeArray = _endtime?.split(":")?.toTypedArray()

            val start_available: Calendar = Calendar.getInstance()

            //System.out.println("Avaliable End Time " + (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
            start_available.set(Calendar.MINUTE, 0)
            start_available.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
            start_available.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
            start_available.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
            start_available.set(Calendar.MONTH, _startdateArray?.get(1)!!.toInt() - 1)
            val end_available: Calendar = start_available.clone() as Calendar
            end_available.set(Calendar.MINUTE, 59)
            end_available.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
            val weekViewEvent = WeekViewEvent()
            weekViewEvent.name = notes
            weekViewEvent.startTime = start_available
            weekViewEvent.endTime = end_available
            weekViewEvent.color = resources.getColor(R.color.available_hours)
            HostHome.events.add(weekViewEvent)

        }

        blocklist.clear()
        for (i in 0 until blockedTimes.size) {
            //  _et_add_note.text = updateBlockedTimesItem.get(i).notes.toString().toEditable()
            val hm = HashMap<String, String>()
            hm["startTime"] = blockedTimes.get(i).startTime.toString()
            hm["startDate"] = blockedTimes.get(i).startDate.toString()
            hm["endDate"] = blockedTimes.get(i).endDate.toString()
            hm["endTime"] = blockedTimes.get(i).endTime.toString()
            hm["notes"] = blockedTimes.get(i).notes.toString()
            hm["source"] = blockedTimes.get(i).source.toString()
            blocklist.add(hm)
        }

        for (i in 0 until blocklist.size) {
            try {
                val _enddate: String? = blocklist.get(i).get("endDate").toString()
                val _startdate: String? = blocklist.get(i).get("startDate").toString()
                val _starttime: String? = blocklist.get(i).get("startTime").toString()
                val _endtime: String? = blocklist.get(i).get("endTime").toString()
                var _notes: String? = blocklist.get(i).get("notes").toString()
                val source: String? = blocklist.get(i).get("source").toString()

                if (_notes.equals("null")) {
                    _notes = ""
                }

                val _enddateArray = _enddate?.split("-")?.toTypedArray()
                val _startdateArray = _startdate?.split("-")?.toTypedArray()
                val _starttimeArray = _starttime?.split(":")?.toTypedArray()
                val _endtimeArray = _endtime?.split(":")?.toTypedArray()

                if (source.equals("Calendar")) {
                    val start_block = Calendar.getInstance()
                    start_block.set(Calendar.MINUTE, 0)
                    start_block.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
                    start_block.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                    start_block.set(Calendar.MONTH, _startdateArray.get(1).toInt() - 1)
                    start_block.set(Calendar.YEAR, _startdateArray.get(0).toInt())
                    val end_block = start_block.clone() as Calendar
                    // end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt())
                    end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())

                    if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()).equals(1)) {
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 59)
                    } else if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && _endtimeArray?.get(0)!!.toInt() <= 12) {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    } else {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        if (_endtimeArray?.get(0)!!.toInt() > 12) {
                            end_block.add(Calendar.HOUR_OF_DAY, (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
                        } else {
                            end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        }
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    }
                    val weekViewEvent = WeekViewEvent()
                    weekViewEvent.name = _starttimeArray?.get(0).toString() + ":" + _starttimeArray?.get(1).toString() + "-" + _endtimeArray?.get(0).toString() + ":" + _endtimeArray?.get(1) + "\n" + _notes
                    weekViewEvent.startTime = start_block
                    weekViewEvent.endTime = end_block
                    weekViewEvent.color = resources.getColor(R.color.blocked_hours)
                    HostHome.events.add(weekViewEvent)
                }
                if (source.equals("Reservation")) {
                    val start_block = Calendar.getInstance()
                    System.out.println("End Time " + _endtimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, 0)
                    start_block.set(Calendar.HOUR_OF_DAY, _starttimeArray?.get(0)!!.toInt())
                    start_block.set(Calendar.MINUTE, _starttimeArray?.get(2)!!.toInt())
                    start_block.set(Calendar.DAY_OF_MONTH, _startdateArray?.get(2)!!.toInt())
                    start_block.set(Calendar.MONTH, _startdateArray.get(1).toInt() - 1)
                    start_block.set(Calendar.YEAR, _startdateArray.get(0).toInt())
                    val end_block = start_block.clone() as Calendar
                    end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())

                    if (_startdateArray?.get(2)!!.toInt() == _enddateArray?.get(2)!!.toInt() && (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()).equals(1)) {
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 59)
                    } else {
                        end_block.set(Calendar.DAY_OF_MONTH, _enddateArray?.get(2)!!.toInt())
                        if (_endtimeArray?.get(0)!!.toInt() > 12) {
                            end_block.add(Calendar.HOUR_OF_DAY, (_endtimeArray?.get(0)!!.toInt() - _starttimeArray?.get(0)!!.toInt()))
                        } else {
                            end_block.add(Calendar.HOUR_OF_DAY, _endtimeArray?.get(0)!!.toInt())
                        }
                        end_block.set(Calendar.MONTH, _enddateArray?.get(1)!!.toInt() - 1)
                        end_block.set(Calendar.MINUTE, 0)
                    }
                    val weekViewEvent = WeekViewEvent()
                    weekViewEvent.name = _starttimeArray?.get(0).toString() + ":" + _starttimeArray?.get(1).toString() + "-" + _endtimeArray?.get(0).toString() + ":" + _endtimeArray?.get(1) + "\n" + _notes
                    weekViewEvent.startTime = start_block
                    weekViewEvent.endTime = end_block
                    weekViewEvent.color = resources.getColor(R.color.reservation_clr)
                    HostHome.events.add(weekViewEvent)
                }
            } catch (b: Exception) {
                b.printStackTrace()
            }

        }

        getWeekView().notifyDatasetChanged()

        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


    private fun eventMatches(event: WeekViewEvent, year: Int, month: Int): Boolean {

        return event.startTime.get(Calendar.YEAR) == year && event.startTime.get(Calendar.MONTH) == month - 1 || event.endTime.get(Calendar.YEAR) == year && event.endTime.get(Calendar.MONTH) == month - 1
    }

    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        _weekview.setDateTimeInterpreter(object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())
                val month = SimpleDateFormat("MMM", Locale.getDefault())
                val week_days = SimpleDateFormat("dd", Locale.getDefault())
                val yearval = SimpleDateFormat("yyyy", Locale.getDefault())
                val week_value = week_days.format(date.time)
                val current_year = yearval.format(date.time)
                val current_month = month.format(date.time)
                val firstdayOfWeek = week_days.format(date.timeInMillis - 518400000L)

                tv_week_days.text = current_month.toString() + " " + firstdayOfWeek.toString() + " - " + week_value.toString() + ", " + current_year.toString()

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657

                val currentmonth = SimpleDateFormat("MM", Locale.getDefault())
                val current_month_val = Integer.parseInt(currentmonth.format(date.time))


                val year = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val yearvalue = Integer.parseInt(year.format(date.time))
                pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
                val editor = pref.edit()
                editor.putInt("Year", yearvalue)
                editor.putInt("Month", current_month_val)
                editor.commit()

                if (shortDate)
                    weekday = weekday[0].toString()
                return weekday.toUpperCase() + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 AM" else "$hour AM"
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Constants.READYTOHOSTSTEP, calendarData)
        println("working")
        setResult(Constants.ViewType.CalendarData, intent)
        clearEvents()
        super.onBackPressed()
    }


}