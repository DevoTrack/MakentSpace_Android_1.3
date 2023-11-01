package com.makent.trioangle.createspace

import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.setprice.SetPriceFragment
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.SETUPSTEP
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import java.io.Serializable
import javax.inject.Inject

class SetupActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SetupActivityInterface, ServiceListener {


    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var navController: NavController

    lateinit var setUpDetailsList: SetupSetListModel

    lateinit var setupStepModel: SetupStepModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        localSharedPreferences = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


        AppController.getAppComponent().inject(this)
        nsv_setup.isNestedScrollingEnabled = false

        getIntentValues()
        initView()
        if(commonMethods.isOnline(this)) {
            getSpaceListingInfo()
        }else{
            commonMethods.snackBar(resources.getString(R.string.network_failure),"",false,2,nsv_setup,resources,this)
        }


    }

    private fun initView() {

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.setup_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController


        val bundle = Bundle()
        bundle.putSerializable("photos", setupStepModel as Serializable)
        navController.setGraph(navController.graph, bundle)
    }

    private fun getIntentValues() {

        setupStepModel = (intent.extras!!.getSerializable(SETUPSTEP) as? SetupStepModel)!!

    }

    override fun onBackPressed() {
        super.onBackPressed()

        fragmentSwitch()
    }

    /**
     * To find out current fragment while back press
     */
    private fun fragmentSwitch() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.setup_nav_host_fragment) as NavHostFragment?
        val fragment = navHostFragment!!.childFragmentManager.fragments[0]

        when (fragment) {
            is PhotoFragment -> progressBarUpdate(20, 0)
            is SpaceStyleFragment -> progressBarUpdate(40, 20)
            is SpecialFeatures -> progressBarUpdate(60, 40)
            is SpaceRulesFragment -> progressBarUpdate(80, 60)
            is SpaceDescriptionFragment -> progressBarUpdate(100, 80)
        }

    }

    fun progressBarUpdate(fromProgress: Int, toProgress: Int) {
        val anim = ProgressBarAnimation(pb, fromProgress.toFloat(), toProgress.toFloat())
        anim.duration = 1000
        pb.startAnimation(anim)

    }

    private fun getSpaceListingInfo() {

        if (!mydialog.isShowing)
            mydialog.show()
        apiService.getSetupItems(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(RequestCallback(this))
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()

        if (jsonResp!!.isSuccess) {
            onSuccessSetup(jsonResp) // onSuccess call method
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg,"",false,2,nsv_setup,resources,this)
        }
    }

    private fun onSuccessSetup(jsonResp: JsonResponse) {
        setUpDetailsList = gson.fromJson(jsonResp.strResponse, SetupSetListModel::class.java)
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    fun getSetupDetailsList(): SetupSetListModel {
        return setUpDetailsList
    }

    fun getSetupDetailsModel(): SetupStepModel {
        return setupStepModel
    }


    override fun getRes(): Resources {
        return this.resources
    }

    override fun getInstance(): SetupActivity {
        return this
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return false
    }


}
