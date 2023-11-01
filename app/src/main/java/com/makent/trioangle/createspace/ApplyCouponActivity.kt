package com.makent.trioangle.createspace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_apply_coupon.*
import javax.inject.Inject

class ApplyCouponActivity : AppCompatActivity(),ServiceListener{


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var mydialog: Dialog_loading


    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_coupon)

        init()
    }

    private fun init() {

        localSharedPreferences  = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        onClickListeners()
    }

    private fun onClickListeners() {

        ivBack.setOnClickListener {
            finish()
        }

        btnApply.setOnClickListener {
            applyCoupon()
        }
    }

    private fun applyCoupon() {
        if (!mydialog.isShowing)
            mydialog.show()

        //apiService.applyCoupon(completeBookingParams()).enqueue(RequestCallback(this))

    }





    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()

        onSuccessCouponApply(jsonResp)
    }

    private fun onSuccessCouponApply(jsonResp: JsonResponse?) {

    }


    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }
}
