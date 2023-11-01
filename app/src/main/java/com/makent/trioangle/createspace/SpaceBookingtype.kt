package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.parseIntent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ReadyHostModel.CalendarData
import com.makent.trioangle.createspace.ReadyHostModel.ReadyToHost
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.READYTOHOSTSTEP
import com.makent.trioangle.host.RoomsBeds.HelperListActivity
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class SpaceBookingtype : Fragment(), ServiceListener {

    lateinit var mydialog: Dialog_loading


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var dialog: AlertDialog

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var readyToHost: ReadyToHost


    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var res: Resources

    lateinit var listner: GetReadyHostInterface

    lateinit var mActivity: GetReadyToHostActivity



    //internal var policyList: MutableList<CancelltionPolicy> = ArrayList()
    var policyList: ArrayList<CancelltionPolicy> = ArrayList()
    var bookingTypeList: ArrayList<BookingType> = ArrayList()


    lateinit var btnContinue: Button
    lateinit var tvCancelpolicy: TextView
    lateinit var tvBookingtype: TextView
    lateinit var tvCurrencyCode: TextView
    lateinit var edtSecuritydeposit: EditText
    private var cancelPolicyPosition: Int = 0
    private var bookingTypePosition: Int = 0

    lateinit var calendarData: CalendarData
    var passval : Boolean = false


    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.space_booking_layout,
                container, false)

        AppController.getAppComponent().inject(this)

        mContext = container!!.context

        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        tvCancelpolicy = view.findViewById<TextView>(R.id.tv_cancelpolicy)
        tvBookingtype = view.findViewById<TextView>(R.id.tv_bookingtype)
        tvCurrencyCode = view.findViewById<TextView>(R.id.tv_currency_code)
        edtSecuritydeposit = view.findViewById<EditText>(R.id.edt_securitydeposit)
        initListner()
        init()

        return view
    }





    private fun initListner() {

        AppController.getAppComponent().inject(this)

        res = listner.getRes()
        mActivity = listner.getInstance()


        //For Calendar Data:
        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else activity!!.resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as GetReadyToHostActivity?)!!


        btnContinue.setOnClickListener {
            if(commonMethods.isOnline(mContext)){
                updateBookingType()

            }else{
                commonMethods.showMessage(mContext,dialog,getString(R.string.no_internet_available))
            }
        }

        tvCancelpolicy.setOnClickListener {
            val listHelper = Intent(mContext, HelperListActivity::class.java)
            listHelper.putExtra("list", policyList as Serializable)
            listHelper.putExtra("value", tvCancelpolicy.text.toString())
            startActivityForResult(listHelper, Constants.ViewType.CancellationPolicy)
        }

        tvBookingtype.setOnClickListener {
            val listHelper = Intent(mContext, HelperListActivity::class.java)
            listHelper.putExtra("list", bookingTypeList as Serializable)
            listHelper.putExtra("value", tvBookingtype.text.toString())
            startActivityForResult(listHelper, Constants.ViewType.Bookingtype)
        }

    }


    private fun updateBookingType() {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.updateSpace(bookingTypeParams()).enqueue(RequestCallback(this))

    }


    private fun bookingTypeParams():HashMap<String,String>{
        val updateBookingTypeMap = HashMap<String, String>()
        updateBookingTypeMap["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateBookingTypeMap["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateBookingTypeMap["step"] = "ready_to_host"
        updateBookingTypeMap["booking_type"] = getBookingType()
        updateBookingTypeMap["cancellation_policy"] = getPolicyListKey()
        updateBookingTypeMap["security_deposit"] = edtSecuritydeposit.text.toString()
        updateBookingTypeMap["activity_currency"] = tvCurrencyCode.text.toString()

        return updateBookingTypeMap
    }

    private fun getBookingType(): String {

        if(tvBookingtype.text.contains("Instant")){
            return "instant_book"
        }else{
            return "request_to_book"
        }

    }

    private fun updateBookingTypePosition() {

        if(mActivity.getReadytoHostModel().bookingType.contains("instant_book")){
            bookingTypePosition = 0
        }else{
            bookingTypePosition = 1
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == Constants.ViewType.CancellationPolicy) {
                val selectedPosition = Integer.parseInt(data.getStringExtra("clickedPos"))
                cancelPolicyPosition = selectedPosition
                updatePolicyList(cancelPolicyPosition)
                //tvCancelpolicy.text = policyList.get(cancelPolicyPosition).name
                tvCancelpolicy.text = getPolicyList()

            }else if (resultCode == Constants.ViewType.Bookingtype) {
                val selectedPosition = Integer.parseInt(data.getStringExtra("clickedPos"))
                bookingTypePosition = selectedPosition
                //updateBookingTypePosition()
                tvBookingtype.text = bookingTypeList.get(bookingTypePosition).name

            }
             if(resultCode == Constants.ViewType.CalendarData){
                val calendarDatas  = data.getSerializableExtra(Constants.READYTOHOSTSTEP)
                mActivity.readyToHost.calendarData = calendarDatas as CalendarData?
            }
        }

    }

    private fun updatePolicyList(position : Int) {
        for(x in 0..policyList.size-1){
            policyList.get(x).isSeleceted= false
            if(x==position){
                policyList.get(x).isSeleceted= true
            }
        }

    }

    private fun getPolicyList(): String {
        for(x in 0..policyList.size-1){
            if(policyList.get(x).isSeleceted){
                return policyList.get(x).name
            }
        }
        return policyList.get(0).name
    }


    private fun getPolicyListKey(): String {
        for(x in 0..policyList.size-1){
            if(policyList.get(x).isSeleceted){
                return policyList.get(x).key
            }
        }
        return policyList.get(0).name
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {

        if (mydialog.isShowing)
            mydialog.dismiss()

        if(jsonResp!!.isSuccess){
            onSuccessResponse(jsonResp)
        }else{
            commonMethods.showMessage(mContext,dialog,jsonResp.statusMsg)
        }
    }



    private fun onSuccessResponse(jsonResp: JsonResponse?) {
        mActivity.getReadytoHostModel().security = edtSecuritydeposit.text.toString()
        mActivity.getReadytoHostModel().bookingType = getBookingType()
        mActivity.getReadytoHostModel().cancellationPolicy = policyList
        if (mActivity.getProgress()!=80) {
            mActivity.progressBarUpdate(60, 80)
        }
        val calender = Intent(mContext,EditCalendar::class.java)
        calender.putExtra(READYTOHOSTSTEP,mActivity.readyToHost as Serializable)
        startActivityForResult(calender, Constants.ViewType.MultipleTime)
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }




    private fun init() {


        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog = commonMethods.getAlertDialog(mContext)

        localSharedPreferences = LocalSharedPreferences(mContext)

        readyToHost = mActivity.getReadytoHostModel()


        populatePolicyList()
        populateBookingTypeList()

        initViews()


    }

    private fun initViews() {
        tvCancelpolicy.text = getPolicyList()
        updateBookingTypePosition()
        tvBookingtype.text = bookingTypeList.get(bookingTypePosition).name
        tvCurrencyCode.text = mActivity.getReadytoHostModel().currencyCode
        edtSecuritydeposit.setText(mActivity.getReadytoHostModel().security)


    }

    private fun populatePolicyList() {

        policyList.clear()
        policyList = mActivity.getReadytoHostModel().cancellationPolicy



    }


    private fun populateBookingTypeList() {

        //bookingTypeList = mActivity.getReadytoHostModel().cancellationPolicy
        val bookingTypeArray = resources.getStringArray(R.array.booking_type)

        for (x in 0..bookingTypeArray.size - 1) {
            var bookingType = BookingType()
            bookingType.name = bookingTypeArray[x]
            bookingTypeList.add(bookingType)

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is GetReadyHostInterface) {
            listner = context
        }
        else {
            throw ClassCastException(
                    context.toString())
        }
    }



}
