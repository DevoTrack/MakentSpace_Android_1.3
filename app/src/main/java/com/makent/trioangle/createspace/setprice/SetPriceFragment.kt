package com.makent.trioangle.createspace.setprice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.adapter.host.CurrencyListAdapter
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.GetReadyHostInterface
import com.makent.trioangle.createspace.GetReadyToHostActivity
import com.makent.trioangle.createspace.model.hostlisting.readytohost.ChangeCurrency
import com.makent.trioangle.createspace.setprice.model.ActivityPrice
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.model.host.Makent_host_model
import com.makent.trioangle.model.settings.CurrencyListModel
import com.makent.trioangle.model.settings.CurrencyResult
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.*
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.fragment_set_price.*
import java.util.*
import javax.inject.Inject

class SetPriceFragment : Fragment(), ServiceListener, ActivityPriceAdapter.ClickListener {

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

    lateinit var tvRate: TextView
    lateinit var tvcurrency : TextView
    lateinit var btnContinue: Button
    lateinit var rvEventList: RecyclerView
    lateinit var btnChangeCurrency: Button

    lateinit var mContext: Context
    lateinit var activityPrice: List<ActivityPrice>

    internal lateinit var recyclerView1: RecyclerView
    internal lateinit var makent_host_modelList: List<Makent_host_model>
    internal lateinit var currencyListAdapter: CurrencyListAdapter
    companion object
    {
        lateinit var alertDialogStores2: android.app.AlertDialog
    }
    lateinit var currencyResult: CurrencyResult
    var currencyList = ArrayList<CurrencyListModel>()

    var minimumPrice: Int = 100
    var spaceCurrency: String ="$"
    var spaceCurrencySymbol : String ="&#36;"
    var spaceCurrencyCode : String ="USD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListner()
        init()
        activityPrice = mActivity.getPriceModel().activityPrice
        spaceCurrency = mActivity.getPriceModel().currencySymbol
        spaceCurrencyCode = mActivity.getPriceModel().currencyCode
        spaceCurrencySymbol = Html.fromHtml(spaceCurrency).toString()
        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,spaceCurrencyCode)
        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,spaceCurrencySymbol)
        minimumPrice=mActivity.getPriceModel().minimumAmount

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_set_price,
                container, false)

        mContext = container!!.context
        tvRate = view.findViewById(R.id.tvRate)
        btnContinue = view.findViewById(R.id.btn_continue)
        btnChangeCurrency = view.findViewById(R.id.btnChangeCurrency)
        rvEventList = view.findViewById(R.id.rvEventList)
        tvcurrency = view.findViewById(R.id.tvcurrency)

        loadActivitiesPrice()

        btnContinue.setOnClickListener {
            if (validateActivityPrice())
                updateActivities()
            else
                commonMethods.snackBar(mContext.getResources().getString(R.string.validate_amount), "", false, 2, tvRate, mContext.getResources(), mContext as Activity?)

//            for (activityPrices in activityPrice) {
//                if (activityPrices.minHours < 0 || activityPrices.hourly < minimumPrice)
//                {
//                    commonMethods.snackBar(mContext.getResources().getString(R.string.validate_amount), "", false, 2, tvRate, mContext.getResources(), mContext as Activity?)
//
//                }
//                else if (activityPrices.fullDay < minimumPrice)
//                {
//                    commonMethods.snackBar(mContext.getResources().getString(R.string.fulldayvalidate_amount), "", false, 2, tvRate, mContext.getResources(), mContext as Activity?)
//
//                }
//                else if (activityPrices.weekly < minimumPrice)
//                {
//                    commonMethods.snackBar(mContext.getResources().getString(R.string.weeklyvalidate_amount), "", false, 2, tvRate, mContext.getResources(), mContext as Activity?)
//
//                }
//                else if (activityPrices.monthly < minimumPrice)
//                {
//                    commonMethods.snackBar(mContext.getResources().getString(R.string.monthlyvalidate_amount), "", false, 2, tvRate, mContext.getResources(), mContext as Activity?)
//
//                }
//                else
//                {
//                    updateActivities()
//                }
//            }
        }

        btnChangeCurrency.setOnClickListener {
            currency_list()
        }
        return view
    }

    /**
     *Load Activities Header list
     */
    private fun loadActivitiesPrice() {

        rvEventList.layoutManager = LinearLayoutManager(mContext)

        val minimumRate = res.getString(R.string.minimum_hourly_rate) + " " + spaceCurrencySymbol + minimumPrice

        tvRate.text = minimumRate

        tvcurrency.text = spaceCurrencyCode

        val adapter = ActivityPriceAdapter(activityPrice, mContext, spaceCurrencySymbol)
        adapter.setClickListener(this)
        rvEventList.adapter = adapter

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
     * Update Selected activities list
     */
    private fun updateActivities() {
        val updateSpace = HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "ready_to_host"
        val activityPriceData: String = gson.toJson(activityPrice);
        updateSpace["activity_price"] = activityPriceData
        if (commonMethods.isOnline(mActivity)) {
            if (!mydialog.isShowing)
                mydialog.show()
            println("activity_price $activityPriceData")
            apiService.updateSpace(updateSpace).enqueue(RequestCallback(REQ_UPDATE_PRICE, this))
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
            when (jsonResp.requestCode) {
                REQ_UPDATE_PRICE -> {
                    onSuccessResponse(jsonResp)
                }
                REQ_UPDATE_CURRENCY -> {
                    onSuccesscurrencyChange(jsonResp)
                }
                REQ_CURRENCYLIST -> {
                    onSuccessCurrency(jsonResp)
                }
            }
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, btnContinue, mActivity.resources, mActivity)
        }
    }

    /**
     * onSuccess API response
     */
    private fun onSuccessResponse(jsonResp: JsonResponse?) {
        mActivity.getReadytoHostModel().currencyCode = commonMethods.getJsonValue(jsonResp!!.getStrResponse(), "currency_code", String) as String
        mActivity.getReadytoHostModel().security = commonMethods.getJsonValue(jsonResp.getStrResponse(), "security", String) as String
        val navController = Navigation.findNavController(activity!!, R.id.ready_host_nav_host_fragment)
        navController.navigate(R.id.action_setPriceFragment_to_availabilityFragment)
        mActivity.progressBarUpdate(20, 40)
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
        mydialog = Dialog_loading(mActivity)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mActivity)
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
     * Interface from adapter while change values in edit text
     */
    override fun onHourlyRateChanged(v: View, position: Int, value: String, type: String) {
        updateValues(type, position, value)
    }

    override fun onWeeklyRateChanged(v: View, position: Int, value: String, type: String) {
        updateValues(type, position, value)//To change body of created functions use File | Settings | File Templates.
    }

    override fun onMonthlyRateChange(v: View, position: Int, value: String, type: String) {
        updateValues(type, position, value)//To change body of created functions use File | Settings | File Templates.
    }

    override fun onMinHrChanged(v: View, position: Int, value: String, type: String) {
        updateValues(type, position, value)
    }

    override fun onDayRateChanged(v: View, position: Int, value: String, type: String) {
        updateValues(type, position, value)
    }

    /**
     * Update values in model
     */
    private fun updateValues(type: String, position: Int, value: String) {
        if (type == Constants.Day)
            activityPrice.get(position).fullDay = (if (value.trim().isNotEmpty()) value.trim().toLong() else 0)
        if (type == Constants.WeekHr)
            activityPrice.get(position).weekly = (if (value.trim().isNotEmpty()) value.trim().toLong() else 0)
        if (type == Constants.MonHr)
            activityPrice.get(position).monthly = (if (value.trim().isNotEmpty()) value.trim().toLong() else 0)
        else if (type == Constants.Hour)
            activityPrice.get(position).hourly = if (value.trim().isNotEmpty()) value.trim().toLong() else 0
        else if (type == Constants.MinHr)
            activityPrice.get(position).minHours = if (value.trim().isNotEmpty()) value.toInt() else 0
    }

    /**
     * Validate activity price values
     */
    private fun validateActivityPrice(): Boolean {
        for (activityPrices in activityPrice) {
            if (activityPrices.minHours < 0 || activityPrices.hourly < minimumPrice|| activityPrices.weekly < minimumPrice|| activityPrices.monthly < minimumPrice|| activityPrices.fullDay < minimumPrice)
                return false
        }
        return true
    }


    private fun onSuccessCurrency(jsonResp: JsonResponse) {
        currencyResult = gson.fromJson(jsonResp.strResponse, CurrencyResult::class.java)
        val currencyListModels = currencyResult.getCurrencyList()
        currencyList.addAll(currencyListModels)
        currencyListAdapter.notifyDataChanged()
    }
    /**
     * Get currency list
     */
    private fun currency_list() {

        recyclerView1 = RecyclerView(mContext)
        makent_host_modelList = ArrayList()

        currencyListAdapter = CurrencyListAdapter(mContext, currencyList)
        currencyListAdapter.setLoadMoreListener { recyclerView1.post(Runnable { val index = makent_host_modelList.size - 1 }) }

        recyclerView1.setLayoutManager(LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false))
        recyclerView1.setAdapter(currencyListAdapter)

        // loadcurrencylist(0);
        if (currencyList.size < 1)
            currencyListAPI() // this is used to search all the currency
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.currency_header, null)
        alertDialogStores2 = android.app.AlertDialog.Builder(mContext)
                .setCustomTitle(view)
                .setView(recyclerView1)
                .setCancelable(true)
                .show()

        alertDialogStores2.setOnDismissListener {
            if (commonMethods.isOnline(mActivity)) {
                spaceCurrencyCode=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencyCode)
                spaceCurrencySymbol=localSharedPreferences.getSharedPreferences(Constants.ListSpaceCurrencySymbol)
                //Added
                tvcurrency.text = spaceCurrencyCode
                changeCurrency(spaceCurrencyCode)
            } else {
                commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure), "", false, 2, btnContinue, mActivity.resources, mActivity)
            }

        }
    }

    /**
     * While change currency get the minimum amount for selected currency
     */
    private fun changeCurrency(currencyCode : String) {
        if (!mydialog.isShowing) mydialog.show()
        apiService.getMinAmount(localSharedPreferences.getSharedPreferences(Constants.AccessToken), currencyCode).enqueue(RequestCallback(REQ_UPDATE_CURRENCY, this))
    }

    /**
     * Get Currency List from API
     */
    fun currencyListAPI() {
        localSharedPreferences.saveSharedPreferences(Constants.IsSpaceList, 1)
        apiService.currencyList(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(RequestCallback(REQ_CURRENCYLIST, this))
    }

    private fun onSuccesscurrencyChange(jsonResp: JsonResponse) {

        val changeCurrency = gson.fromJson(jsonResp.strResponse, ChangeCurrency::class.java)
        spaceCurrencyCode=changeCurrency.currencyCode
        spaceCurrency=changeCurrency.currencySymbol
        spaceCurrencySymbol = Html.fromHtml(spaceCurrency).toString()
        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencyCode,spaceCurrencyCode)
        localSharedPreferences.saveSharedPreferences(Constants.ListSpaceCurrencySymbol,spaceCurrencySymbol)
        minimumPrice=changeCurrency.minimumAmount

        for(i : Int in activityPrice.indices){
            activityPrice.get(i).currencyCode=spaceCurrencyCode
            activityPrice.get(i).currencySymbol=spaceCurrency
        }
        loadActivitiesPrice()
    }
    @Override
    fun onBackPressed() {
        val intent = Intent();
        intent.putExtra("Currency", spaceCurrencyCode)
        println("working")
        mActivity.setResult(Constants.ViewType.currencyData, intent)
        mActivity.finish()
        //super.onBackPressed();
        getFragmentManager()?.popBackStack();
    }
}

