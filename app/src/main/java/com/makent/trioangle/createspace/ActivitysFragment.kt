package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.makent.trioangle.createsp.ActivityListAdapter
import com.makent.trioangle.createspace.ReadyHostAdapter.ActivityListHeaderAdapter
import com.makent.trioangle.createspace.ReadyHostModel.ActivityTypesItem
import com.makent.trioangle.createspace.setprice.model.SetPriceModel
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.CustomDialog
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.row_activities_item.view.*
import javax.inject.Inject

class ActivitysFragment : Fragment(), ServiceListener, ActivityListHeaderAdapter.ClickListener, ActivityListAdapter.ActivitiesClickListener {

    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var commonDialog: CustomDialog

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var res: Resources

    lateinit var listner: GetReadyHostInterface

    lateinit var mActivity: GetReadyToHostActivity

    lateinit var btnContinue: Button
    lateinit var rv_activity_header: RecyclerView
    lateinit var rv_activity: RecyclerView

    lateinit var mContext: Context
    lateinit var activityTypes: List<ActivityTypesItem>
    var headerActivitiesPosition: Int = 0
    var activitiesPosition: Int = 0
    var subActivitiesPosition: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_activitys,
                container, false)

        mContext = container!!.context

        initListner()
        init()

        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        rv_activity_header = view.findViewById<RecyclerView>(R.id.rv_activity_header)
        rv_activity = view.findViewById<RecyclerView>(R.id.rv_activity)

        headerActivitiesPosition = 0
        loadActivitiesHeader()

        btnContinue.setOnClickListener {
            if(checkActivitySelected())
                updateActivities()
            else
                commonMethods.snackBar(res.getString(R.string.please_select_any_one_event), "", false, 2, rv_activity_header, getResources(), mActivity)
        }

        return view
    }

    /**
     *Load Activities Header list
     */
    fun loadActivitiesHeader() {

        rv_activity_header.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)

        activityTypes = mActivity.activityTypes

        val adapter = ActivityListHeaderAdapter(activityTypes, mContext)
        adapter.setClickListener(this)
        rv_activity_header.adapter = adapter
        if (activityTypes.size > 0)
            loadActivities()
    }

    /**
     *Load Activities Main list
     */
    fun loadActivities() {

        rv_activity.layoutManager = LinearLayoutManager(mContext)

        val adapters = ActivityListAdapter(activityTypes.get(headerActivitiesPosition).activities, mContext)
        adapters.setClickListener(this)
        rv_activity.adapter = adapters
    }

    /**
     * Initialize listener
     */
    private fun initListner() {

        AppController.getAppComponent().inject(this)

        res = listner.getRes()
        mActivity = listner.getInstance()

    }

    /**
     * Validate is any one activity or event is selected or not
     */
    private fun checkActivitySelected() : Boolean{
        for(activitiesTypesItem in activityTypes){
            if(activitiesTypesItem.isSelected!!)
                return true
        }
        return false
    }

    /**
     * Update Selected activities list
     */
    private fun updateActivities() {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        val activityData: String = gson.toJson(activityTypes)
        updateSpace["activity_data"] = activityData
        Log.e("update","update act"+updateSpace)
        if (commonMethods.isOnline(mActivity)) {
            if (!mydialog.isShowing)
                mydialog.show()
            apiService.updateSpaceActivities(updateSpace).enqueue(RequestCallback(this))
        } else {
            commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure), "", false, 2, btnContinue, mActivity.resources, mActivity)
        }
    }

    /**
     * onSuccess from update activities API call
     */
    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {

        if (mydialog.isShowing)
            mydialog.dismiss()

        if (jsonResp!!.isSuccess) {
            onSuccessResponse(jsonResp)
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, btnContinue, mActivity.resources, mActivity)
        }
    }

    /**
     * onSuccess API response
     */
    private fun onSuccessResponse(jsonResp: JsonResponse?) {
        if(jsonResp?.isSuccess!!) {
            val setPriceModel = gson.fromJson(jsonResp.strResponse, SetPriceModel::class.java)
            mActivity.setPriceModel(setPriceModel)
            val navController = Navigation.findNavController(activity!!, R.id.ready_host_nav_host_fragment)
            navController.navigate(R.id.action_activitysFragment_to_setPriceFragment)
            mActivity.progressBarUpdate(0, 20)
        }else{
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, rv_activity_header, getResources(), mActivity)
        }
    }

    /**
     * update activities API call failer
     */
    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    /**
     * initialize base
     */
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

    /**
     *  Header activities list click
     */
    override fun onHeaderClick(v: View, position: Int, oldPosition: Int) {
        print("Name " + activityTypes.get(position).name)
        rv_activity_header.adapter?.notifyDataSetChanged()
        headerActivitiesPosition = position
        loadActivities()
    }

    /**
     *  Activities list click listener
     */
    override fun onActivitiesClick(v: View, position: Int) {
        val cbActivities = v.cbActivities
        activitiesPosition=position
        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.isSelected=cbActivities.isChecked
        for (index : Int in activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.indices!!) {
            activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities!!.get(index)?.isSelected =cbActivities.isChecked
        }
        setActivityHeader()
        loadActivities()
    }

    /**
     * Sub activities list click listener
     */
    override fun onSubActivitiesClick(v: View, activitiesPosition: Int, position: Int) {
        val cbsubActivities = v.cbActivities
        var isSubActivitySelected=false
        subActivitiesPosition=position
        //this.activitiesPosition = activitiesPosition
        println("Tagged : "+headerActivitiesPosition+":"+activitiesPosition+":"+subActivitiesPosition)
        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.get(subActivitiesPosition)?.isSelected=cbsubActivities.isChecked

        for (index : Int in activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities?.indices!!) {
            if(activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.subActivities!!.get(index)?.isSelected!!)
                isSubActivitySelected=true
        }

        activityTypes.get(headerActivitiesPosition).activities?.get(activitiesPosition)?.isSelected=isSubActivitySelected
        setActivityHeader()
        loadActivities()
    }

    /**
     * Set selected or not in header activity
     */
    private fun setActivityHeader(){
        for (index : Int in activityTypes.get(headerActivitiesPosition).activities?.indices!!) {
            if(activityTypes.get(headerActivitiesPosition).activities?.get(index)?.isSelected!!) {
                activityTypes.get(headerActivitiesPosition).isSelected = true
                return
            }
        }
        activityTypes.get(headerActivitiesPosition).isSelected = false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if(resultCode == Constants.ViewType.currencyData){
                val currency = data.getStringExtra("Currency")
                mActivity.getPriceModel().currencyCode = currency!!
            }
        }
    }
}

