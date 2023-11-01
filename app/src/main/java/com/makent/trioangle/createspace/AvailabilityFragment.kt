package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ReadyHostModel.AvailabilityTimesItem
import com.makent.trioangle.createspace.ReadyHostModel.CalendarData
import com.makent.trioangle.createspace.ReadyHostModel.ReadyToHost
import com.makent.trioangle.createspace.interfaces.OnItemClick
import com.makent.trioangle.createspace.interfaces.listItemClickListner
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import java.io.Serializable

import javax.inject.Inject

class AvailabilityFragment : Fragment(), OnItemClick {

    private lateinit var localSharedPreferences: LocalSharedPreferences

    private lateinit var readyToHost: ReadyToHost

    private lateinit var availabilityTimes: ArrayList<AvailabilityTimesItem>

    private lateinit var res: Resources

    private lateinit var listner: GetReadyHostInterface

    private lateinit var mActivity: GetReadyToHostActivity

    private lateinit var btnContinue: Button

    private lateinit var rvAvailability: RecyclerView

    private lateinit var mContext: Context

    private var dayList: ArrayList<AvailabiltyModel>  = ArrayList()

    private var weeks: ArrayList<String>  = ArrayList()

    private lateinit var mydialog: Dialog_loading

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.availability_layout,
                container, false)

        AppController.getAppComponent().inject(this)

        mContext = container!!.context

        btnContinue = view.findViewById<Button>(R.id.btn_continue)

        rvAvailability = view.findViewById<RecyclerView>(R.id.rv_availability)

        rvAvailability.layoutManager = LinearLayoutManager(mContext)

        btnContinue.setOnClickListener {
            val navController = Navigation.findNavController(activity!!, R.id.ready_host_nav_host_fragment)
            navController.navigate(R.id.action_availabilityFragment_to_SpaceBookingtypeFragement)
            mActivity.progressBarUpdate(40, 60)
        }

        initListner()
        init()
        populatedayList();

        return view
    }


    override fun onItemClick(pos: Int) {
        val intent = Intent(mContext, ChooseTimeActivity::class.java)
        intent.putExtra(Constants.AVAILABILITYTIMES, availabilityTimes.get(pos) as Serializable)
        intent.putExtra("Day", pos)
        startActivityForResult(intent, Constants.ViewType.AvailabilityTime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Constants.ViewType.AvailabilityTime) {
                var day : Int = data.getIntExtra("Day",0)
                availabilityTimes.set(day, data.getSerializableExtra(Constants.AVAILABILITYTIMES) as AvailabilityTimesItem)
                mActivity.getReadytoHostModel().calendarData=data.getSerializableExtra(Constants.CalenderData) as CalendarData
                populatedayList()
                //availabilityTimes.get(day).to(data.getSerializableExtra(Constants.AVAILABILITYTIMES) as AvailabilityTimesItem)
            }
        }

    }

    private fun populatedayList() {
        weeks.clear()
        weeks.add("Sunday")
        weeks.add("Monday")
        weeks.add("TuesDay")
        weeks.add("Wednesday")
        weeks.add("Thursday")
        weeks.add("Friday")
        weeks.add("Saturday")

        dayList = ArrayList<AvailabiltyModel>()

        for (j in 0..6) {
            val availabiltyModel = AvailabiltyModel()
            availabiltyModel.day = weeks.get(j)
            availabiltyModel.status = availabilityTimes.get(j).status
            dayList.add(availabiltyModel)

        }
        rvAvailability.adapter = AvailabilityAdapter(dayList,mContext,this)
    }

    private fun initListner() {
        AppController.getAppComponent().inject(this)
        res = listner.getRes()
        mActivity = listner.getInstance()
    }


    private fun init() {
        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

        readyToHost = mActivity.getReadytoHostModel()
        availabilityTimes  = readyToHost.availabilityTimes
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
}