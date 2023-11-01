package com.makent.trioangle.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.adapter.PriceBreakDownListAdapter
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.RESERVATIONID
import com.makent.trioangle.helper.Constants.USERTYPE
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.model.PriceBreakDownList
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_price_break_down.*
import java.util.HashMap
import javax.inject.Inject

class PriceBreakDown : AppCompatActivity() , ServiceListener {

    private var reservationId: String = ""

    private var userType: String = ""
    var sessionId: String = ""

    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var priceBreakDownModel: PriceBreakDownModel

    private lateinit var priceBreakDownListAdapter: PriceBreakDownListAdapter

    var priceBreakDownList : List<PriceBreakDownList> = ArrayList<PriceBreakDownList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_break_down)
        AppController.getAppComponent().inject(this)
        Log.e("Price Break","Price break down page");

        init()
        getIntentValues()
        getPriceBreakDown()
    }

    private fun getIntentValues() {
        reservationId = (intent.extras!!.getString(RESERVATIONID) as String)
        sessionId = (intent.extras!!.getString(Constants.SESSIONID) as String)
        userType = (intent.extras!!.getString(USERTYPE) as String)
    }

    private fun init() {

        localSharedPreferences  = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        var imageViewTarget = DrawableImageViewTarget(ivDotLoader)
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget)
        ivDotLoader.setVisibility(View.VISIBLE)

        btnClose.setOnClickListener(){
            finish()
        }

    }

    /**
     * Function to call Api to fetch price break down details
     */
    private fun getPriceBreakDown() {

        ivDotLoader.setVisibility(View.VISIBLE)
        rltPriceBreakDown.visibility = View.GONE

        if(commonMethods.isOnline(this)) {
            apiService.priceBreakDown(updatePriceBreakDown()).enqueue(RequestCallback(this))
        }else{
            Toast.makeText(this,resources.getString(R.string.internet_connection_error),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePriceBreakDown(): HashMap<String, String> {
        val priceBreakDownHashMap = HashMap<String, String>()
        priceBreakDownHashMap["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        priceBreakDownHashMap["reservation_id"] = reservationId
        priceBreakDownHashMap["s_key"] = sessionId
        priceBreakDownHashMap["user_type"] = userType
        return priceBreakDownHashMap
    }

    override fun onSuccess(jsonResp: JsonResponse, data: String?) {
        ivDotLoader.setVisibility(View.GONE)
        rltPriceBreakDown.visibility = View.VISIBLE

        priceBreakDownModel = gson.fromJson(jsonResp.strResponse, PriceBreakDownModel::class.java)
        priceBreakDownList = priceBreakDownModel.priceBreakDownList

        initRecyclerViewAndAdapter()
        priceBreakDownListAdapter.notifyDataSetChanged()

    }

    private fun initRecyclerViewAndAdapter() {
        val mLayoutManager = LinearLayoutManager(this)
        priceBreakDownListAdapter = PriceBreakDownListAdapter(priceBreakDownList, this)
        rvPriceBreakDown.layoutManager = mLayoutManager
        rvPriceBreakDown.adapter = priceBreakDownListAdapter
        rvPriceBreakDown.isNestedScrollingEnabled = false
    }

    override fun onFailure(jsonResp: JsonResponse, data: String?) {
        ivDotLoader.setVisibility(View.GONE)
        rltPriceBreakDown.visibility = View.VISIBLE

    }
}
