package com.makent.trioangle.spacebooking.views

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.text.HtmlCompat
import butterknife.ButterKnife
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.PayPal
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.PayPalRequest
import com.braintreepayments.api.models.PaymentMethodNonce
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.makent.trioangle.BaseActivity
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.*
import com.makent.trioangle.host.PriceBreakDown
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.model.PaymentData
import com.makent.trioangle.profile.ProfilePageActivity
import com.makent.trioangle.stripepayment.PayWithStripeActivity
import com.makent.trioangle.travelling.HomeActivity
import com.makent.trioangle.travelling.MessageToHostActivity
import com.makent.trioangle.travelling.PaymentCountryList
import com.makent.trioangle.util.ActionSheet
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.Enums.*
import com.makent.trioangle.util.RequestCallback
import com.paypal.android.sdk.payments.*
import kotlinx.android.synthetic.main.activity_request_space.*
import org.json.JSONException
import java.io.Serializable
import java.math.BigDecimal
import javax.inject.Inject


class RequestSpaceActivity : BaseActivity(), ServiceListener, ActionSheet.ActionSheetListener, PaymentMethodNonceCreatedListener {


    private var specialOfferId: String? = ""
    private var isCouponApplied: Boolean = false
    private var paymentMethod: String = ""
    private var countryName: String = ""
    @Inject
    lateinit var commonMethods: CommonMethods
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var gson: Gson

    lateinit var tvSpaceTitle: TextView

    private var mBraintreeFragment: BraintreeFragment? = null

    /**
     * Paypal Variables
     */
    val REQUEST_CODE_PAYMENT = 100
    private val TAG = "paymentExample"
    private var config: PayPalConfiguration? = null
    private var CONFIG_ENVIRONMENT: String? = null
    // note that these credentials will differ between live & sandbox environments.
    private var CONFIG_CLIENT_ID: String? = null

    lateinit var mydialog: Dialog_loading

    lateinit var localSharedPreferences: LocalSharedPreferences
    lateinit var paymentDataModel: PaymentData

    var reservationId: String = ""
    var bookingMessage: String = ""
    var sessionId: String = ""
    var bookingType: String = ""
    var country: String = ""
    lateinit var couponDialog: android.app.AlertDialog
    lateinit var dialog: BottomSheetDialog
    var hostUserId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_space)
        AppController.getAppComponent().inject(this)
        ButterKnife.bind(this)
        tvSpaceTitle = findViewById(R.id.tvSpaceTitle)

        init()

        nsvSpaceDetail.visibility = View.GONE
        shimmer_frame.visibility = View.VISIBLE

        getIntentValues()
        commonMethods.rotateArrow(request_close,this)
        if (commonMethods.isOnline(this)) {
            getBookingDetailsApi()
        } else {
            commonMethods.snackBar(resources.getString(R.string.network_failure), "", false, 2, rltPriceBreakDown, resources, this)
        }

        lltChooseCountry.setOnClickListener {
            val x = Intent(applicationContext, PaymentCountryList::class.java)
            startActivity(x)
        }

        lltRequestMessageHost.setOnClickListener {
            val x = Intent(applicationContext, MessageToHostActivity::class.java)
            x.putExtra("hostprofile", paymentDataModel.spaceBookingData.hostThumbImage)
            x.putExtra("hostprofilename", paymentDataModel.spaceBookingData.hostUserName)
            //val bndlanimation = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.trans_left_in, R.anim.trans_left_out).toBundle()
            startActivityForResult(x, REQUEST_HOST_MESSAGE)
        }
        tvPriceBreakDown.setOnClickListener {
            val x = Intent(applicationContext, PriceBreakDown::class.java)
            x.putExtra(USERTYPE, "guest")
            x.putExtra(RESERVATIONID, "")
            x.putExtra(SESSIONID, sessionId)
            startActivity(x)
        }
        tvApplyCoupon.setOnClickListener {
           /* val x = Intent(applicationContext, ApplyCouponActivity::class.java)
            startActivity(x)*/
            //addPromo()
            if(!isCouponApplied)
                addPromoDialog()
            else
                removeCoupon()
        }
        request_close.setOnClickListener {
            onBackPressed()
        }

        btnBook.setOnClickListener {

            country = localSharedPreferences.getSharedPreferences(Constants.CountryCode);
            println("Booking Type : " + bookingType)
            println("Country : " + country)

            if (country != null && country != "") {

                if (bookingType.equals("Yes")) {
                    setTheme(R.style.ActionSheetCustomStyle)
                    showActionSheet()
                } else {
                    completeBookingApi("")
                }

            } else {
                val x = Intent(applicationContext, PaymentCountryList::class.java)
                startActivity(x)
            }

        }
        tvHostName.setOnClickListener {
           goToProfile()
        }
        iv_host_user_image.setOnClickListener {
           goToProfile()
        }
    }

    private fun goToProfile(){
        val x = Intent(applicationContext, ProfilePageActivity::class.java)
        println("Other user id${hostUserId}")
        x.putExtra("otheruserid", hostUserId)
        startActivity(x)
    }

    private fun addPromoDialog() {
        val view = layoutInflater.inflate(R.layout.activity_apply_coupon, null)
        couponDialog = android.app.AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show()

        var btnApply : TextView = view.findViewById(R.id.btnApply)
        var edtCoupon : EditText = view.findViewById(R.id.edt_apply_cpoupon)

        btnApply.setOnClickListener {
            println("Coupon Applied : "+isCouponApplied)
            applyCoupon(edtCoupon.text.toString())


        }
    }

    private fun removeCoupon() {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.removeCoupon(removeCouponParams()).enqueue(RequestCallback(REQ_REMOVE_COUPON, this))
    }

    private fun applyCoupon(coupon:String) {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.applyCoupon(applyCouponParams(coupon)).enqueue(RequestCallback(REQ_APPLY_COUPON, this))
    }

    private fun applyCouponParams(coupon:String): HashMap<String, String> {

        val applyCouponParams = java.util.HashMap<String, String>()
        applyCouponParams["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        applyCouponParams["s_key"] = sessionId
        applyCouponParams["coupon_code"] = coupon


        return applyCouponParams

    }

    private fun removeCouponParams(): HashMap<String, String> {

        val applyCouponParams = java.util.HashMap<String, String>()
        applyCouponParams["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        applyCouponParams["s_key"] = sessionId


        return applyCouponParams

    }

    private fun completeBookingApi(payKey: String) {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.completeBooking(completeBookingParams(payKey)).enqueue(RequestCallback(REQ_COMPLETED_BOOKING, this))
    }

    private fun init() {
        localSharedPreferences = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences.saveSharedPreferences(Constants.CountryCode, "");
        localSharedPreferences.saveSharedPreferences(Constants.CountyrNameBooking, "")
        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, "")
    }

    /**
     * select the Payment type
     */
    private fun showActionSheet() {
        ActionSheet.createBuilder(this, supportFragmentManager)
                .setCancelButtonTitle(resources.getString(R.string.cancel))
                .setOtherButtonTitles(resources.getString(R.string.paypal), resources.getString(R.string.credit_card))
                .setCancelableOnTouchOutside(true).setListener(this).show()
    }


    /**
     * Get the PrePayment Details
     */
    private fun getBookingDetailsApi() {

        apiService.getPaymentData(updatePaymentData()).enqueue(RequestCallback(REQ_BOOKING_DETAILS, this))
    }


    /**
     * Payment Passed datas
     */
    private fun updatePaymentData(): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        //updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["s_key"] = sessionId
        updateSpace["reservation_id"] = reservationId
        updateSpace["special_offer_id"] = specialOfferId!!
        return updateSpace
    }


    /**
     * Payment Passed datas
     */
    private fun completeBookingParams(payKey: String): HashMap<String, String>? {
        val completedBookingParam = java.util.HashMap<String, String>()
        completedBookingParam["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        completedBookingParam["s_key"] = sessionId
        completedBookingParam["booking_type"] = yesNoToBookType(bookingType)
        completedBookingParam["pay_key"] = payKey
        completedBookingParam["country"] = country
        completedBookingParam["message"] = bookingMessage
        completedBookingParam["currency_code"] = paymentDataModel.spaceBookingData.payableCurrency
        completedBookingParam["paymode"] = paymentMethod

        return completedBookingParam
    }

    private fun yesNoToBookType(bookingType: String): String {

        if (bookingType.equals("Yes"))
            return "instant_book"
        else
            return "request_book"

    }


    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {

        when (jsonResp!!.requestCode) {
            REQ_BOOKING_DETAILS -> {
                shimmer_frame.visibility = View.GONE
                nsvSpaceDetail.visibility = View.VISIBLE
                if(jsonResp.isSuccess)
                    onSuccessPaymentData(jsonResp)
                else
                    if (jsonResp.responseCode==500) {
                        shimmer_frame.visibility = View.VISIBLE
                        nsvSpaceDetail.visibility = View.GONE
                        commonMethods.snackBar(resources.getString(R.string.internal_server_error), "", false, 2, rltPriceBreakDown, resources, this)
                    }else {
                        val statusCode= commonMethods.getJsonValue(jsonResp.strResponse, "status_code", String::class.java) as String
                        if(statusCode.equals("2",ignoreCase = true)){
                            nsvSpaceDetail.visibility = View.GONE
                            onBackPressed()
                        }
                        Toast.makeText(applicationContext, jsonResp.statusMsg, Toast.LENGTH_SHORT).show()
                    }
            }
            REQ_COMPLETED_BOOKING -> {
                if (mydialog.isShowing)
                    mydialog.dismiss()
                onSuccessCompleteBooking(jsonResp)
            }
            REQ_CURRENCY_CONVERTION -> {
                if (mydialog.isShowing)
                    mydialog.dismiss()
                if(jsonResp.isSuccess)
                    onSuccessCurrencyConvert(jsonResp)
                else
                    Toast.makeText(this,jsonResp.statusMsg,Toast.LENGTH_SHORT).show()
            }
            REQ_APPLY_COUPON -> {
                if (mydialog.isShowing)
                    mydialog.dismiss()
                if(jsonResp.isSuccess)
                    onSuccessApplyCoupon(jsonResp)
                else
                    Toast.makeText(this,jsonResp.statusMsg,Toast.LENGTH_SHORT).show()
            }
            REQ_REMOVE_COUPON -> {
                if (mydialog.isShowing)
                    mydialog.dismiss()
                if(jsonResp.isSuccess)
                    onSuccessRemoveCoupon(jsonResp)
                else
                    Toast.makeText(this,jsonResp.statusMsg,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onSuccessRemoveCoupon(jsonResp: JsonResponse) {
        val totalAmount = commonMethods.getJsonValue(jsonResp!!.getStrResponse(), "payment_total", String::class.java) as String
        tvTotalPrice.text = totalAmount
        isCouponApplied = false
        couponTextChange()
    }

    private fun onSuccessApplyCoupon(jsonResp: JsonResponse) {
        val totalAmount = commonMethods.getJsonValue(jsonResp!!.getStrResponse(), "payment_total", String::class.java) as String
        tvTotalPrice.text = totalAmount
        isCouponApplied = true
        couponTextChange()
        couponDialog.dismiss()
    }

    private fun couponTextChange() {
        if(isCouponApplied)
            tvApplyCoupon.text = resources.getString(R.string.remove_coupon)
        else
            tvApplyCoupon.text = resources.getString(R.string.apply_coupon)
    }

    private fun onSuccessCompleteBooking(jsonResp: JsonResponse) {


        clearSavedData()

        if (bookingType.equals("Yes")) {
            Toast.makeText(applicationContext, resources.getString(R.string.success_completed), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext, getString(R.string.request_book_sent), Toast.LENGTH_SHORT).show()
        }

        val x = Intent(applicationContext, HomeActivity::class.java)
        x.putExtra("tabsaved", 5)
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(x)
        finish()

    }


    override fun onResume() {
        super.onResume()
        shimmer_frame.startShimmer()

        country = localSharedPreferences.getSharedPreferences(Constants.CountryCode)
        countryName = localSharedPreferences.getSharedPreferences(Constants.CountyrNameBooking)
        bookingMessage = localSharedPreferences.getSharedPreferences(Constants.ReqMessage)


        applyOrRemoveCouponAmount()
        if (country != null && country != "") {
            tvCountry.setText(countryName)
            btnBookingType()
        } else {
            btnBook.setText(getString(R.string.choose_country))
        }

        if (bookingMessage != null && bookingMessage != "") {
            tvReqMessage.text = resources.getString(R.string.change)
        }
    }

    private fun btnBookingType() {

        if (bookingType.equals("Yes",ignoreCase = true))
            if(reservationId.isNotEmpty()){
                btnBook.text=resources.getString(R.string.booknow)
            }else {
                btnBook.setText(getString(R.string.instantbook))
            }
        else
            btnBook.setText(getString(R.string.requestbook))

    }



    override fun onStop() {
        super.onStop()
        shimmer_frame.stopShimmer()
    }

    /**
     * onSuccess getting payment details
     */
    private fun onSuccessPaymentData(jsonResp: JsonResponse?) {
        paymentDataModel = gson.fromJson(jsonResp?.strResponse, PaymentData::class.java)
        updateView()
    }

    /**
     * get Intent values
     */
    private fun getIntentValues() {
        sessionId =     (intent.extras!!.getString(Constants.SESSIONID))!!
        reservationId = (intent.extras!!.getString(Constants.RESERVATIONID))!!
        bookingType =   (intent.extras!!.getString(Constants.Bookingtype))!!
        specialOfferId =   (intent.extras!!.getString(Constants.SPECIALOFFERID))
        hostUserId = intent.getStringExtra("host_user_id").toString()

        if(specialOfferId==null)
            specialOfferId=""
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        localSharedPreferences.saveSharedPreferences(Constants.CountryCode, "")
        localSharedPreferences.saveSharedPreferences(Constants.CountyrNameBooking, "")
        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, "")
    }


    // enable are disable coupon amount

    private fun applyOrRemoveCouponAmount() {

        if (bookingType.equals("Yes"))
            tvApplyCoupon.visibility = View.VISIBLE
        else
            tvApplyCoupon.visibility = View.GONE

    }

    /**
     * Set datas at View
     */
    private fun updateView() {
        tvSpaceTitle.setText(paymentDataModel.spaceBookingData.spaceName)
        //tvSpaceTitle.setText(paymentDataModel.spaceBookingData.spaceName)
        tvLocationName.text = paymentDataModel.spaceBookingData.spaceAddress
        tvChooseActivity.text = paymentDataModel.spaceBookingData.activityType
        tvHostName.text = paymentDataModel.spaceBookingData.hostUserName
        request_details_hometype.text = paymentDataModel.spaceBookingData.spaceTypeName

        isCouponApplied = paymentDataModel.spaceBookingData.couponApplied
        sessionId  = paymentDataModel.spaceBookingData.sessionId

        couponTextChange()


        Glide.with(applicationContext).asBitmap().load(paymentDataModel.spaceBookingData.hostThumbImage).into(object : BitmapImageViewTarget(iv_host_user_image) {
            override fun setResource(resource: Bitmap?) {
                val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource)
                circularBitmapDrawable.isCircular = true
                iv_host_user_image.setImageDrawable(circularBitmapDrawable)
            }
        })

        tvStartDateTime.text = commonMethods.getDay(paymentDataModel.spaceBookingData.bookingDateTimesData.startDate) + " " + commonMethods.DateFirstUserFormat(paymentDataModel.spaceBookingData.bookingDateTimesData.startDate) + "\n" + commonMethods.change24To12Hr(paymentDataModel.spaceBookingData.bookingDateTimesData.startTime)
        tvEndDateTime.text = commonMethods.getDay(paymentDataModel.spaceBookingData.bookingDateTimesData.endDate) + " " + commonMethods.DateFirstUserFormat(paymentDataModel.spaceBookingData.bookingDateTimesData.endDate) + "\n" + commonMethods.change24To12Hr(paymentDataModel.spaceBookingData.bookingDateTimesData.endTime)
        tvTotalHours.text = paymentDataModel.spaceBookingData.totalHours
        tvTotalGuest.text = paymentDataModel.spaceBookingData.numberOfGuests //Html.fromHtml(currency_symbol).toString()
        tvTotalPrice.text = HtmlCompat.fromHtml(paymentDataModel.spaceBookingData.currencySymbol, HtmlCompat.FROM_HTML_MODE_COMPACT).toString() + " " + paymentDataModel.spaceBookingData.paymentTotal
        tvCurrencyCode.text = paymentDataModel.spaceBookingData.currencyCode

        println("paymentDataModel spaceName : " + paymentDataModel.spaceBookingData.spaceName)
        println("tvSpaceTitle : " + tvSpaceTitle.text.toString())
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        shimmer_frame.visibility = View.VISIBLE
        nsvSpaceDetail.visibility = View.GONE
        commonMethods.snackBar(resources.getString(R.string.internal_server_error), "", false, 2, rltPriceBreakDown, resources, this)
    }

    override fun onDismiss(actionSheet: ActionSheet?, isCancel: Boolean) {

    }

    /**
     * Action Sheet Click
     */
    override fun onOtherButtonClick(actionSheet: ActionSheet?, index: Int) {
        /**
         * 0 - Paypal
         * 1 - Stripe (credit card)
         */
        if (index == 0) {
            paymentMethod = "paypal"
            convertCurrency()
        } else {
			paymentMethod = "stripe"
            val payWithStripe=Intent(this,PayWithStripeActivity::class.java)
            payWithStripe.putExtra(PaymentDetailsForStripe,paymentDataModel as Serializable)
            startActivity(payWithStripe)


        }
    }

    fun convertCurrency() {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.currencyConversion(currencyParams()).enqueue(RequestCallback(
            REQ_CURRENCY_CONVERTION, this))
    }

    private fun currencyParams(): HashMap<String, String>? {
        var params = HashMap<String, String>()
        params["amount"] = paymentDataModel.spaceBookingData.payableAmount
        params["cy_code"] = paymentDataModel.spaceBookingData.payableCurrency
        params["language"] = localSharedPreferences.getSharedPreferences(Constants.LanguageCode)
        params["payment_type"] = paymentMethod
        params["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        return params
    }


    fun onSuccessCurrencyConvert(jsonResp: JsonResponse) {
        val braintreeClientToken = commonMethods.getJsonValue(jsonResp!!.getStrResponse(), "braintree_clientToken", String::class.java) as String
        payWithPaypal(braintreeClientToken)
    }
    /**
     * pay With paypal with mode and id
     */
    fun payWithPaypal(braintreeClientToken: String) {

        initiateBraintree(braintreeClientToken)

        var amount = paymentDataModel.spaceBookingData.payableAmount
        var currencycode = paymentDataModel.spaceBookingData.payableCurrency

        val request = PayPalRequest(amount)
            .currencyCode(currencycode)
            .intent(PayPalRequest.INTENT_SALE)
        PayPal.requestOneTimePayment(mBraintreeFragment, request)

/*        paypalCredentialsUpdate(paymentDataModel.paymentCredentials.isPayPalLiveMode, paymentDataModel.paymentCredentials.paypalClientId)

        val thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, paymentDataModel.spaceBookingData.payableAmount, paymentDataModel.spaceBookingData.payableCurrency)

        val intent = Intent(this, PaymentActivity::class.java)
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy)

        startActivityForResult(intent, REQUEST_CODE_PAYMENT)*/
    }

    fun initiateBraintree(braintreeClientToken: String) {
        val mAuthorization: String = braintreeClientToken

        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // mBraintreeFragment is ready to use!

        mBraintreeFragment?.addListener(this)
    }

    /**
     * To get nounce from brain tree paypal
     */
    override fun onPaymentMethodNonceCreated(paymentMethodNonce: PaymentMethodNonce) {
        val nonce: String = paymentMethodNonce.nonce
        println("nonce : " + nonce)
        completeBookingApi(nonce) //paykey
    }

    /**
     * Checking and Setting the live or sandox of paypal
     */
    private fun paypalCredentialsUpdate(isPaypalLiveMode: Boolean, paypalClientId: String) {

        if (!isPaypalLiveMode) {
            CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX
        } else {
            CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION
        }

        CONFIG_CLIENT_ID = paypalClientId

        // Paypal configuration
        println("Confiq id : $CONFIG_ENVIRONMENT")
        println("Confiq client id  : $CONFIG_CLIENT_ID")
        config = PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
                // The following are only used in PayPalFuturePaymentActivity.
                .merchantName(resources.getString(R.string.app_name))
                .merchantPrivacyPolicyUri(Uri.parse(resources.getString(R.string.privacylink)))
                .merchantUserAgreementUri(Uri.parse(resources.getString(R.string.termslink)))
                .acceptCreditCards(false)

        // Call payal service
        val intent = Intent(this, PayPalService::class.java)
        var servicerRunningCheck = isMyServiceRunning(PayPalService::class.java)
        println("Service Running check : $servicerRunningCheck")
        if (servicerRunningCheck) {
            stopService(intent)
        }
        servicerRunningCheck = isMyServiceRunning(PayPalService::class.java)
        println("Service Running check twice : $servicerRunningCheck")
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        startService(intent)

    }

    /**
     * Paypal payment details
     */
    private fun getThingToBuy(paymentIntent: String, amount: String, currencycode: String): PayPalPayment {

        return PayPalPayment(BigDecimal(amount), currencycode, resources.getString(R.string.payment_name),
                paymentIntent)
    }

    /**
     * Checking Service is running
     */
    @SuppressWarnings("deprecation")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm = data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    try {
                        println("Response" + confirm.toJSONObject().toString(4))
                        println("Paykey" + confirm.toJSONObject().getJSONObject("response").get("id").toString())
                        /**
                         * TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        completeBookingApi(confirm.toJSONObject().getJSONObject("response").get("id").toString())
                        //completePayment(confirm.toJSONObject().getJSONObject("response").get("id").toString())
                        // displayResultText("PaymentConfirmation info received from PayPal");

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                println("RESULT_CANCELED" + "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                println("RESULT_EXTRAS_INVALID" + "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
            }
        }
    }


    fun clearSavedData() {

        localSharedPreferences.saveSharedPreferences(Constants.stepHouserules, 0)
        localSharedPreferences.saveSharedPreferences(Constants.stepPayment, 0)
        localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 0)

        localSharedPreferences.saveSharedPreferences(Constants.HouseRules, null)

        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest, null)
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest, null)
        localSharedPreferences.saveSharedPreferences(Constants.SearchLocation, null)
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress, null)
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null)
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null)
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null)

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null)
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null)
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null)
        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, null)

        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0")

        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck, null)
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck, null)

        localSharedPreferences.saveSharedPreferences(Constants.CountryCode, "");
        localSharedPreferences.saveSharedPreferences(Constants.CountyrNameBooking, "")
        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, "")


    }
}
