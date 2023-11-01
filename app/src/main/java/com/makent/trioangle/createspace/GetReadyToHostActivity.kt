package com.makent.trioangle.createspace

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ReadyHostModel.ActivityTypesItem
import com.makent.trioangle.createspace.ReadyHostModel.AvailabilityTimesItem
import com.makent.trioangle.createspace.ReadyHostModel.CalendarData
import com.makent.trioangle.createspace.ReadyHostModel.ReadyToHost
import com.makent.trioangle.createspace.setprice.SetPriceFragment
import com.makent.trioangle.createspace.setprice.model.ActivityPrice
import com.makent.trioangle.createspace.setprice.model.SetPriceModel
import com.makent.trioangle.helper.Constants.READYTOHOSTSTEP
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.util.CommonMethods
import kotlinx.android.synthetic.main.activity_get_ready_to_host.*
import kotlinx.android.synthetic.main.activity_setup.*
import java.io.Serializable
import javax.inject.Inject

class GetReadyToHostActivity : AppCompatActivity() , GetReadyHostInterface
{
    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var navController: NavController

    lateinit var readyToHost: ReadyToHost
    lateinit var activityTypes: List<ActivityTypesItem>
    lateinit var calendarData: CalendarData

    lateinit var setPriceModel: SetPriceModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_ready_to_host)

        localSharedPreferences = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        AppController.getAppComponent().inject(this)
        nsv_ready_to_host.isNestedScrollingEnabled = false
        getIntentValues()
        initView()
    }

    private fun getIntentValues()
    {
        readyToHost = (intent.extras!!.getSerializable(READYTOHOSTSTEP) as? ReadyToHost)!!
        activityTypes = readyToHost.activityTypes as List<ActivityTypesItem>
        calendarData = readyToHost.calendarData as CalendarData
    }

    fun setPriceModel(setPriceModel: SetPriceModel)
    {
        this.setPriceModel=setPriceModel
    }

    fun getPriceModel() = setPriceModel

    fun getReadytoHostModel(): ReadyToHost
    {
        return readyToHost
    }

    private fun initView() {

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.ready_host_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        val bundle = Bundle()
        bundle.putSerializable("activityTypes", activityTypes as Serializable)
        navController.setGraph(navController.graph, bundle)
    }

    fun progressBarUpdate(fromProgress: Int, toProgress: Int) {
        val anim = ProgressBarAnimation(pb_ready_host, fromProgress.toFloat(), toProgress.toFloat())
        anim.duration = 1000
        pb_ready_host.startAnimation(anim)
    }

    public fun getProgress():Int{
        return pb_ready_host.progress
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fragmentSwitch()
    }

    /**
     * To find out current fragment while back press
     */
    private fun fragmentSwitch() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.ready_host_nav_host_fragment) as NavHostFragment?
        val fragment = navHostFragment!!.childFragmentManager.fragments[0]
        when (fragment) {
            is ActivitysFragment -> progressBarUpdate(20, 0)
            is SetPriceFragment -> progressBarUpdate(40, 20)
            is AvailabilityFragment -> progressBarUpdate(60, 40)
            is SpaceBookingtype -> progressBarUpdate(80, 60)
        }
    }

    override fun getRes(): Resources {
        return this.resources
    }

    override fun getInstance(): GetReadyToHostActivity {
        return this
    }
}
