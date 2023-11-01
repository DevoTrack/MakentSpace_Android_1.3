package com.makent.trioangle.stripepayment

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.model.PaymentData
import com.makent.trioangle.travelling.HomeActivity
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.REQ_STRIPE_CLIENT_ID
import com.makent.trioangle.util.Enums.REQ_UPDATE_STRIPE_KEY
import com.makent.trioangle.util.RequestCallback
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.model.StripeIntent
import kotlinx.android.synthetic.main.activity_pay_with_stripe.*
import kotlinx.android.synthetic.main.app_header.*
import kotlinx.android.synthetic.main.billing_info_details.view.*
import org.json.JSONObject
import javax.inject.Inject

class PayWithStripeActivity : AppCompatActivity(), ServiceListener {

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    private lateinit var mydialog: Dialog_loading
    private var localSharedPreferences: LocalSharedPreferences = LocalSharedPreferences(this)

    private lateinit var stripe: Stripe
    private var paymentData = PaymentData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_with_stripe)
        AppController.getAppComponent().inject(this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        init()

        val selectCustomization = PaymentAuthConfig.Stripe3ds2ButtonCustomization.Builder()
            .setBackgroundColor("#EC4847")
            .setTextColor("#000000")
            .build()
        val uiCustomization =
            PaymentAuthConfig.Stripe3ds2UiCustomization.Builder.createWithAppTheme(this)
                .setButtonCustomization(
                    selectCustomization,
                    PaymentAuthConfig.Stripe3ds2UiCustomization.ButtonType.SELECT
                )
                .build()
        PaymentAuthConfig.init(
            PaymentAuthConfig.Builder()
                .set3ds2Config(
                    PaymentAuthConfig.Stripe3ds2Config.Builder()
                        .setUiCustomization(uiCustomization)
                        .build()
                )
                .build()
        )
        iv_Back.setOnClickListener { onBackPressed() }

        /**
         * initialize stripe data
         */
        PaymentConfiguration.init(this, paymentData.paymentCredentials.stripePublishKey)
        stripe = Stripe(this, PaymentConfiguration.getInstance(this).publishableKey)
        vFirstName.tvBillinginfo.text=resources.getString(R.string.firstname)
        vLastName.tvBillinginfo.text=resources.getString(R.string.lastname)
        vPostal.tvBillinginfo.text=resources.getString(R.string.postal_code)
        addpayment.setOnClickListener {
            //hideKeyboard()
            /**
             * Creating a PaymentMethod using card Details
             */
            if (card_multiline_widget.paymentMethodCard != null) {
                if (TextUtils.isEmpty(vFirstName.edtFields.text.toString())) {
                    vFirstName.edtFields.requestFocus()
                    commonMethods.snackBar(
                        resources.getString(R.string.valid_firstname),
                        "",
                        false,
                        2,
                        vFirstName.edtFields,
                        resources,
                        this
                    )
                } else if (TextUtils.isEmpty(vLastName.edtFields.text.toString())) {
                    vLastName.edtFields.requestFocus()
                    commonMethods.snackBar(
                        resources.getString(R.string.valid_lastname),
                        "",
                        false,
                        2,
                        vFirstName.edtFields,
                        resources,
                        this
                    )
                } else if (TextUtils.isEmpty(vPostal.edtFields.text.toString())) {
                    vPostal.edtFields.requestFocus()
                    commonMethods.snackBar(
                        resources.getString(R.string.valid_postal),
                        "",
                        false,
                        2,
                        vFirstName.edtFields,
                        resources,
                        this
                    )
                }else{
                    if (commonMethods.isOnline(this)) {
                        if (!mydialog.isShowing){
                            mydialog.show()
                        }
                        val paymentMethodCreateParamsCard =
                            card_multiline_widget.paymentMethodCard
                        val paymentMethodCreateParams =
                            PaymentMethodCreateParams.create(paymentMethodCreateParamsCard!!, null)
                        val paymentMethod =
                            stripe.createPaymentMethodSynchronous(paymentMethodCreateParams)
                        apiService.generateStripeKey(getPaymentData(paymentMethod!!.id))
                            .enqueue(RequestCallback(REQ_STRIPE_CLIENT_ID, this))
                    } else {
                        commonMethods.snackBar(
                            resources.getString(R.string.network_failure),
                            "",
                            false,
                            2,
                            vFirstName.edtFields,
                            resources,
                            this
                        )
                    }
                }
            }

        }
    }

    /**
     * get Client Secret Params
     */
    private fun getPaymentData(paymentmethodId: String?): LinkedHashMap<String, String> {
        val paymentDatas = LinkedHashMap<String, String>()
        paymentDatas["s_key"] = paymentData.spaceBookingData.sessionId
        paymentDatas["currency_code"] = paymentData.spaceBookingData.payableCurrency
        paymentDatas["amount"] = paymentData.spaceBookingData.payableAmount
        paymentDatas["payment_method_id"] = paymentmethodId!!
        paymentDatas["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        return paymentDatas
    }

    /**
     *  Complete Payment
     */
    private fun completeBooking(paymentIntentId: String?): LinkedHashMap<String, String> {
        val completePaymentDetails = LinkedHashMap<String, String>()
        completePaymentDetails["s_key"] = paymentData.spaceBookingData.sessionId
        completePaymentDetails["currency_code"] = paymentData.spaceBookingData.payableCurrency
        completePaymentDetails["booking_type"] = "instant_book"
        completePaymentDetails["pay_key"] = paymentIntentId!!
        completePaymentDetails["country"] = localSharedPreferences.getSharedPreferences(Constants.CountryCode)
        completePaymentDetails["message"] = localSharedPreferences.getSharedPreferences(Constants.ReqMessage)
        completePaymentDetails["paymode"] = "stripe"
        completePaymentDetails["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        return completePaymentDetails
    }

    /**
     * init loader
     */
    private fun init() {
        tv_header_title.text=resources.getString(R.string.payments)
        paymentData = intent.getSerializableExtra(Constants.PaymentDetailsForStripe) as PaymentData
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**
     * Get Success From API
     */
    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing){
            mydialog.dismiss()
        }
        if (jsonResp!!.isSuccess) {
            when(jsonResp.requestCode){
                REQ_STRIPE_CLIENT_ID->{
                    onSuccessGettingSecretKey(jsonResp)
                }
                REQ_UPDATE_STRIPE_KEY->{
                    onSuccessOnPayment()
                }
            }
        }
    }

    /**
     *  Get Failure From Api
     */
    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        commonMethods.snackBar(resources.getString(R.string.internal_server_error), "", false, 2, vFirstName.edtFields, resources, this@PayWithStripeActivity)
    }

    /**
     * Get Success For Client Secret id
     */
    private fun onSuccessGettingSecretKey(jsonResp:JsonResponse?){
        val key = commonMethods.getJsonValue(jsonResp!!.getStrResponse(), "client_secret", String::class.java) as String
        val jsonObject = JSONObject()
        confirmPayment(
            createPaymentIntentParams(
                key,
                PaymentMethod.fromJson(jsonObject)!!
            )
        )
    }

    /**
     * Successfully completed the Payment
     */
    private fun onSuccessOnPayment(){
        Toast.makeText(applicationContext, resources.getString(R.string.payment_successfull_paid), Toast.LENGTH_SHORT).show()
        val x = Intent(applicationContext, HomeActivity::class.java)
        x.putExtra("tabsaved", 5)
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(x)
        finish()
    }

    /**
     * Create a Payment to Start Payment Process
     */
    private fun createPaymentIntentParams(
        clientSecret: String,
        paymentMethod: PaymentMethod
    ): ConfirmPaymentIntentParams {
        val paymentMethodParamsCard = card_multiline_widget!!.paymentMethodCard
        val paymentMethodCreateParams =
            PaymentMethodCreateParams.create(paymentMethodParamsCard!!, paymentMethod.billingDetails)
        return ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(paymentMethodCreateParams, clientSecret)
    }

    /**
     * Confirm Payment using Payment Intent
     */
    private fun confirmPayment(params: ConfirmPaymentIntentParams) {
        stripe.confirmPayment(this, params)
    }

    /**
     * Get Result when Complete Payment Form Stripe
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        stripe.onPaymentResult(requestCode, data,
            object : ApiResultCallback<PaymentIntentResult> {
                override fun onSuccess(result: PaymentIntentResult) {
                    // If authentication succeeded, the PaymentIntent will have
                    // user actions resolved; otherwise, handle the PaymentIntent
                    // status as appropriate (e.g. the customer may need to choose
                    // a new payment method)
                    val paymentIntent = result.intent
                    val status = paymentIntent.status
                    if (status == StripeIntent.Status.Succeeded) {
                        // show success UI
                        if (!mydialog.isShowing) {
                            mydialog.show()
                        }
                        apiService.completeBooking(completeBooking(paymentIntent.id)).enqueue(
                            RequestCallback(
                                REQ_UPDATE_STRIPE_KEY,
                                this@PayWithStripeActivity
                            )
                        )
                    } else {
                        // ask for a new Payment Method
                        commonMethods.snackBar(
                            resources.getString(R.string.payment_failure),
                            "",
                            false,
                            2,
                            vFirstName.edtFields,
                            resources,
                            this@PayWithStripeActivity
                        )
                    }
                }

                override fun onError(e: Exception) {
                    // handle error
                    e.printStackTrace()
                }
            })
    }
}