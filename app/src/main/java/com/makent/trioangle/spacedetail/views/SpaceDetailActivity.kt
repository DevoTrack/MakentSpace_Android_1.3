package com.makent.trioangle.spacedetail.views

import android.app.Activity
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.makent.trioangle.MainActivity
import com.makent.trioangle.R
import com.makent.trioangle.adapter.*
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.*
import com.makent.trioangle.helper.Constants.Host_Preview
import com.makent.trioangle.helper.Constants.isContactHost
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.model.WishListObjects
import com.makent.trioangle.profile.ProfilePageActivity
import com.makent.trioangle.spacebooking.views.SpaceAvailableActivity
import com.makent.trioangle.spacedetail.adapter.SimilarSpaceAdapter
import com.makent.trioangle.spacedetail.adapter.SpaceRulesAdapter
import com.makent.trioangle.spacedetail.adapter.TheSpaceAdapter
import com.makent.trioangle.spacedetail.interfaces.OnItemClickListener
import com.makent.trioangle.spacedetail.model.SpaceResult
import com.makent.trioangle.travelling.*
import com.makent.trioangle.util.CommonConstantKeys.Companion.SpaceDetailWishList
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_space_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable
import javax.inject.Inject


open class SpaceDetailActivity : AppCompatActivity(), ServiceListener, OnItemClickListener,
    View.OnClickListener {
    @Inject
    lateinit var runTimePermission: RunTimePermission

    @Inject
    lateinit var customDialog: CustomDialog

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    /**
     * Classes init
     */
    lateinit var mydialog: Dialog_loading
    val localSharedPreferences = LocalSharedPreferences(this)
    lateinit var adapter: ImageAdapter
    internal var handler: Handler? = null
    internal var runnable: Runnable? = null

    /**
     * Adapter Classes
     */
    lateinit var guestAccessListAdapter: GuestAccessListAdapter
    lateinit var serviceOfferAdapter: ServiceOfferAdapter
    lateinit var specialFeatureAdapter: SpecialFeatureAdapter
    lateinit var spaceRulesAdapter: SpaceRulesAdapter
    lateinit var spaceStyleAdapter: SpaceStyleAdapter
    lateinit var bedArrangementAdapter: BedArrangementAdapter
    lateinit var theSpaceAdapter: TheSpaceAdapter
    lateinit var similarSpaceAdapter: SimilarSpaceAdapter

    /**
     * init Variables
     */

    var spaceId = ""
    var hostUserId = ""
    var isHost: Boolean = false
    var bottomheight: Int = 0
    var is_whishlist = ""
    var searchstartdate: String? = ""
    var searchenddate: String? = ""
    var searchcheckin: String? = ""
    var searchcheckout: String? = ""
    var checkinout: String? = ""
    var startTime: String? = ""
    var endTime: String? = ""
    internal var spaceNameToshare: String = ""
    internal var imagetoshare: String = ""
    lateinit var spaceImagesItems: Array<String?>
    lateinit var spaceImagesDescription: Array<String?>

    /**
     * Array Lists
     */
    lateinit var amenities_id: Array<String?>
    lateinit var amenities_name: Array<String?>
    lateinit var amenities_icon: Array<String?>
    lateinit var amenities_image: Array<String?>
    lateinit var amenitiescount: String

    /**
     * init Model Classes
     */
    lateinit var spaceResult: SpaceResult

    lateinit var rltSpaceFooter: RelativeLayout

    var blockedDates : Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_detail)
        ButterKnife.bind(this)
        AppController.getAppComponent().inject(this)

        Log.e("Spacedetail", "Space details activity");

        spaceId = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        rltSpaceFooter = findViewById(R.id.rlt_space_footer);
        shimmerFrame.visibility = View.VISIBLE
        ab_space_detail.visibility = View.GONE
        rlt_space_detail.visibility = View.GONE
        rltSpaceFooter.visibility = View.GONE

        commonMethods.rotateArrow(iv_space_back, this)
        tv_review_message.movementMethod = ScrollingMovementMethod()

        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        /**
         * Dot loader is Hidden due to Shimmer is implemented
         */
        iv_space_dot_loader.visibility = View.GONE
        val imageViewTarget = DrawableImageViewTarget(iv_space_dot_loader)
        Glide.with(applicationContext).load(R.drawable.dot_loading).into(imageViewTarget)

        isHost = intent.getBooleanExtra(Constants.isFromHost, false)

        if (commonMethods.isOnline(this)) {
           // loadSpaceDetail() //get the Space Detail From Api
        } else {
            commonMethods.snackBar(
                resources.getString(R.string.interneterror),
                "",
                false,
                2,
                tv_space_host_name,
                resources,
                this
            )
        }

        iv_space_host_image.setOnClickListener(this)
        tv_space_host_name.setOnClickListener(this)

        /**
         * Get Review Datas
         */

        rlt_review_count.setOnClickListener {
            val x = Intent(applicationContext, ReviewActivityHome::class.java)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
        }

        iv_space_back.setOnClickListener {
            onBackPressed()
        }

        /**
         * Check Availabilty
         */
        btn_request_space.setOnClickListener {
            val checkAvailabilty = Intent(this, SpaceAvailableActivity::class.java)
            checkAvailabilty.putExtra(Constants.SpaceResults, spaceResult as Serializable)
            checkAvailabilty.putExtra(Constants.BlockDates, blockedDates)
            startActivity(checkAvailabilty)
        }

        /**
         *  Set Space to be Wishlisted
         */
        iv_space_wishlist.setOnClickListener {
            if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload")
                localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, spaceId)
                if (is_whishlist.equals("yes", ignoreCase = true)) {
                    is_whishlist = "No"
                    iv_space_wishlist.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.n2_heart_light_outline
                        )
                    )
                    val task = deleteWishList(this)
                    task.execute()
                } else {
                    localSharedPreferences.saveSharedPreferences(
                        Constants.WishListAddress,
                        spaceResult.locationData.city + "," + spaceResult.locationData.state
                    )
                    localSharedPreferences.saveSharedPreferences(
                        Constants.ChooseWishListType,
                        SpaceDetailWishList
                    )
                    val cdd = WishListChooseDialog(this)
                    cdd.show()
                }
            } else {
                val home = Intent(applicationContext, MainActivity::class.java)
                home.putExtra("isback", 1)
                startActivity(home)
            }
        }

        tv_space_pricings.setOnClickListener {
            SimpleTooltip.Builder(this)
                .anchorView(tv_space_pricings)
                .text(resources.getString(R.string.fullday_price))
                .gravity(Gravity.END)
                .animated(false)
                .contentView(R.layout.tooltip_custom, R.id.toottip)
                .build()
                .show()
        }
    }

    /**
     *  init EventBus Reg
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     * Get the Update From Event Post method
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(wishListType: WishListObjects) {
        if (wishListType.isWishListFrom.equals("SimilarSpace")) {
            spaceResult.similarListing[wishListType.isWishListPosition].isWhishlist = "yes"
            similarSpaceAdapter.notifyDataSetChanged()
        } else {
            is_whishlist = "yes"
            iv_space_wishlist.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.n2_heart_red_fill
                )
            )
        }
    }

    /**
     *  On BackPress
     *  Here Conforming that get Selected dialog
     */
    override fun onBackPressed() {

        isHost = getHostOrNot()

        if (!isHost) localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, null)

        localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, null)
        localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 0)

        searchstartdate = localSharedPreferences.getSharedPreferences(Constants.CheckIn)
        searchenddate = localSharedPreferences.getSharedPreferences(Constants.CheckOut)
        searchcheckin = localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn)
        searchcheckout = localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut)
        checkinout = localSharedPreferences.getSharedPreferences(Constants.CheckInOut)
        startTime = localSharedPreferences.getSharedPreferences(Constants.StartTime)
        endTime = localSharedPreferences.getSharedPreferences(Constants.EndTime)

        if (searchstartdate != null && searchcheckin != null) {
            dialog()
        } else {
            if (searchcheckin != null && searchcheckout != null) {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1")
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0")
                localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null)
                localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null)
                localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null)
                localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null)
            }
            close()
            super.onBackPressed()
        }
    }

    private fun getHostOrNot(): Boolean {
        return try {
            var isHost = localSharedPreferences.getSharedPreferences(Host_Preview)
            (isHost != null && isHost == "1")
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Show Date Change Confirm Dialog
     */
    protected fun dialog() {
        val dialog = Dialog(this@SpaceDetailActivity)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

        // set the custom dialog components - text, ivPhoto and button
        val dialogMessage = dialog.findViewById<View>(R.id.logout_msg) as TextView
        dialogMessage.text = resources.getString(R.string.datedialog3, checkinout)
        val dialogButton = dialog.findViewById<View>(R.id.logout_cancel) as Button
        dialogButton.text = resources.getString(R.string.no)
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener {
            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1")
            localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, null)
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.StartTime, null)
            localSharedPreferences.saveSharedPreferences(Constants.EndTime, null)

            val x = Intent(applicationContext, HomeActivity::class.java)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
            finish()

            dialog.dismiss()
        }

        val dialogButton1 = dialog.findViewById<View>(R.id.logout_ok) as Button
        dialogButton1.text = resources.getString(R.string.yes)
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener {
            dialog.dismiss()
            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "1")
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn, searchstartdate)
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut, searchenddate)
            localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut, checkinout)
            localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.StartTime, null)
            localSharedPreferences.saveSharedPreferences(Constants.EndTime, null)

            val x = Intent(applicationContext, HomeActivity::class.java)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
            finish()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    /**
     * Reload Home Page When Changes added in Space Detail
     */
    protected fun close() {
        if ((localSharedPreferences.getSharedPreferences(Constants.Reload) != null
                    || (intent.getIntExtra("isSpaceBack", 0) == 1 || intent.getIntExtra(
                "finish_it",
                0
            ) == 1)) && (!isHost)
        ) {

            localSharedPreferences.saveSharedPreferences(Constants.Reload, null)
            val x = Intent(applicationContext, HomeActivity::class.java)
            overridePendingTransition(0, 0)
            x.putExtra("tabsaved", 0)
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(x)
            finish()
        } else {
            finish()
        }
    }

    /**
     * Setting the Footer height
     */
    private fun getRelativeLayoutInfo() {
        rltSpaceFooter.width
        if (!isHost) {
            val layoutParams = nsv_spacedetail.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.setMargins(0, 0, 0, bottomheight)
            nsv_spacedetail.layoutParams = layoutParams
            rltSpaceFooter.visibility = View.VISIBLE
            llt_contact_host.visibility = View.VISIBLE
            if (spaceResult.similarListing.isNotEmpty()) {
                rlt_similarlisting.visibility = View.VISIBLE
            } else {
                rlt_similarlisting.visibility = View.GONE
            }
            iv_space_wishlist.visibility = View.VISIBLE
        } else {
            val params = nsv_spacedetail.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            nsv_spacedetail.setLayoutParams(params)
            rltSpaceFooter.visibility = View.GONE
            llt_contact_host.visibility = View.GONE
            rlt_similarlisting.visibility = View.GONE
            iv_space_wishlist.visibility = View.GONE
        }
    }

    /**
     * call SpaceDetail Api
     */
    private fun loadSpaceDetail() {
        apiService.spaceDetail(
            localSharedPreferences.getSharedPreferences(Constants.AccessToken),
            spaceId,
            localSharedPreferences.getSharedPreferences(Constants.LanguageCode)
        ).enqueue(RequestCallback(this))
    }

    /**
     * Get success Result
     */
    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (jsonResp!!.isSuccess) {
            onSucessGettingSpaceDetails(jsonResp)
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            if(jsonResp.statusMsg == "Space Not available"){
                Toast.makeText(this,jsonResp.statusMsg,Toast.LENGTH_LONG).show()
                finish()
            }else{
                commonMethods.snackBar(
                    jsonResp.statusMsg,
                    "",
                    false,
                    2,
                    tv_space_host_name,
                    resources,
                    this
                )
            }

        }
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        commonMethods.snackBar(
            resources.getString(R.string.internal_server_error),
            "",
            false,
            2,
            tv_space_host_name,
            resources,
            this
        )
    }

    /**
     * Getting Space Detail from JsonResponse
     */
    private fun onSucessGettingSpaceDetails(jsonResp: JsonResponse?) {
        spaceResult = gson.fromJson(jsonResp?.strResponse, SpaceResult::class.java)
        hostUserId = spaceResult.userId
        val blockedDate: ArrayList<String> = spaceResult.notAvailableDates!!

        for (days in 0 until blockedDate.size) {
            blockedDates = arrayOf(blockedDate[days])
        }
        localSharedPreferences.saveSharedPreferences(Constants.HostUser, hostUserId)
        val spaceImages = spaceResult.spacePhotos
        spaceImagesItems = arrayOfNulls<String>(spaceImages.size)
        spaceImagesDescription = arrayOfNulls<String>(spaceImages.size)
        for (i in spaceImages.indices) {
            spaceImagesItems[i] = spaceImages[i].photoName
            spaceImagesDescription[i] = spaceImages[i].photoHighlights
        }
        if (spaceImagesItems.size > 0) {
            imagetoshare = spaceImagesItems[0]!!
            tv_description.setText(spaceImagesDescription[0]!!)
        }
        spaceNameToshare = spaceResult.spaceUrl

        if (spaceImagesItems.size > 0) {
            loadImage() // this function to use load the ivPhoto
        }
        tv_space_title.text = spaceResult.spacename

        tv_space_host_name.text = spaceResult.hostName
        tveventType.text = spaceResult.theSpace.get(0).value
        tv_no_of_guest.text =
            spaceResult.theSpace.get(1).value + " " + resources.getString(R.string.people)

        Glide.with(applicationContext).asBitmap().load(spaceResult.hostProfilePic)
            .into(object : BitmapImageViewTarget(iv_space_host_image) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(resources, resource)
                    circularBitmapDrawable.isCircular = true
                    iv_space_host_image.setImageDrawable(circularBitmapDrawable)
                }
            })

        is_whishlist = spaceResult.isWishlist

        if (is_whishlist.equals("yes", ignoreCase = true)) {
            iv_space_wishlist.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.n2_heart_red_fill
                )
            )
        } else {
            iv_space_wishlist.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.n2_heart_light_outline
                )
            )
        }

        iv_space_wishlist.visibility = View.VISIBLE

        tv_space_area.text = spaceResult.spaceSize

        if (spaceResult.summary != "") {
            llt_about_space.setVisibility(View.VISIBLE)
            tv_about_space.text = spaceResult.summary
            if (spaceResult.summary.length < 120 && spaceResult.space == "" && spaceResult.interaction == "" && spaceResult.notes == "" && spaceResult.neighborhoodOverview == "" && spaceResult.space == "") {
                tv_about_readmore.setVisibility(View.GONE)
            }
        } else {

            llt_about_space.setVisibility(View.GONE)
        }
        iv_space_share.setOnClickListener {
            val x = Intent(applicationContext, ShareActivity::class.java)
            x.putExtra("imagetoshare", imagetoshare)
            x.putExtra("nametoshare", spaceResult.spaceUrl)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
        }

        tv_about_readmore.setOnClickListener {
            val x = Intent(applicationContext, RoomAboutList::class.java)
            x.putExtra("aboutlist", spaceResult.summary)
            x.putExtra("desc_interaction", spaceResult.interaction)
            x.putExtra("desc_notes", spaceResult.notes)
            x.putExtra("desc_house_rules", "")
            x.putExtra("desc_neighborhood_overview", spaceResult.neighborhoodOverview)
            x.putExtra("desc_getting_around", "")
            x.putExtra("desc_space", spaceResult.space)
            x.putExtra("desc_access", "")
            x.putExtra("otherService", "")
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
        }

        if (spaceResult.servicesExtra != "") {
            llt_other_service.visibility = View.VISIBLE
            lltServiceOffer.setBackgroundResource(0)
            tv_about_other_service.text = spaceResult.servicesExtra
            if (spaceResult.servicesExtra.length < 120) {
                tv_service_readmore.setVisibility(View.GONE)
            }

            tv_service_readmore.setOnClickListener {
                val x = Intent(applicationContext, RoomAboutList::class.java)
                x.putExtra("aboutlist", "")
                x.putExtra("desc_interaction", "")
                x.putExtra("desc_notes", "")
                x.putExtra("desc_house_rules", "")
                x.putExtra("desc_neighborhood_overview", "")
                x.putExtra("desc_getting_around", "")
                x.putExtra("desc_space", "")
                x.putExtra("desc_access", "")
                x.putExtra("otherService", spaceResult.servicesExtra)
                val bundleAnimation = ActivityOptions.makeCustomAnimation(
                    applicationContext,
                    R.anim.trans_left_in,
                    R.anim.trans_left_out
                ).toBundle()
                startActivity(x, bundleAnimation)
            }
        }

        settheSpace()

        if (spaceResult.spaceActivityArray.size > 0) {
            setPricing()
        } else {
            lltPricing.visibility = View.GONE
        }

        if (spaceResult.guestAccess.size > 0) {
            setGuestAccess()
        } else {
            lltWholeGuestAccess.visibility = View.GONE
        }

        if (spaceResult.amenities.size > 0) {
            setAmenity()
        } else {
            llt_amenities.visibility = View.GONE
        }

        if (spaceResult.specialFeature.size > 0) {
            setSpecialFeature()
        } else {
            lltSpecialfeature.visibility = View.GONE
        }

        if (spaceResult.spaceRules.size > 0) {
            setSpaceRules()
        } else {
            lltSpaceRules.visibility = View.GONE
        }

        if (spaceResult.spaceStyle.size > 0) {
            setSpaceStyle()
        } else {
            lltspacestyle.visibility = View.GONE
        }

        if (spaceResult.services.size > 0) {
            setserviceOffered()
        } else {
            lltServiceOffer.visibility = View.GONE
        }

        if (!spaceResult.reviewCount.equals("0", ignoreCase = true) && !TextUtils.isEmpty(
                spaceResult.reviewCount
            )
        ) {
            setReviewDatas()
        } else {
            rlt_space_review.visibility = View.GONE
            rb_space_rate.visibility = View.GONE
            tv_space_rate_count.visibility = View.GONE
        }

        if (!spaceResult.rating.equals("0", ignoreCase = true)) {
            rlt_space_review.visibility = View.VISIBLE
            rb_space_rate.visibility = View.VISIBLE
            tv_space_rate_count.visibility = View.VISIBLE
        }

        if (spaceResult.spaceAvailabilityTimes.size > 0) {
            setSpaceAvailability()
        } else {
            lltSpaceAvailableTime.visibility = View.GONE
        }
        if (spaceResult.instantBook.equals("yes", ignoreCase = true)) {
            tv_instant.visibility = View.VISIBLE
        } else {
            tv_instant.visibility = View.GONE
        }

        val address =
            spaceResult.locationData.city + "," + spaceResult.locationData.state + "," + spaceResult.locationData.country
        val latLng = LatLng(
            spaceResult.locationData.latitude.toDouble(),
            spaceResult.locationData.longitude.toDouble()
        )
        loadMapImage(address, latLng)

        tv_space_cancel.text = spaceResult.cancellation_policy
        llt_space_cancel.setOnClickListener {
            val x = Intent(applicationContext, CancellationPolicyActivity::class.java)
            x.putExtra("cancellationpolicy", spaceResult.cancellation_policy)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
        }
        llt_contact_host.setOnClickListener {
            if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
                val checkAvailability = Intent(this, SpaceAvailableActivity::class.java)
                checkAvailability.putExtra(Constants.SpaceResults, spaceResult as Serializable)
                checkAvailability.putExtra(isContactHost, true)
                startActivity(checkAvailability)
            } else {
                if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) == null || TextUtils.isEmpty(
                        localSharedPreferences.getSharedPreferences(Constants.AccessToken)
                    )
                ) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "Contact_host")
                }
                val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPrefs.edit()
                val gson = Gson()
                val json = gson.toJson(spaceResult)
                editor.putString(Constants.SpaceResults, json)
                editor.apply()

                val home = Intent(applicationContext, MainActivity::class.java)
                home.putExtra("isback", 1)
                startActivity(home)
            }
        }

        shimmerFrame.visibility = View.GONE
        ab_space_detail.visibility = View.VISIBLE
        rlt_space_detail.visibility = View.VISIBLE
        if (spaceResult.canBook.equals("Yes", ignoreCase = true)) {
            val bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottomup)
            rltSpaceFooter.startAnimation(bottomUp)
            val vto = rltSpaceFooter.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rltSpaceFooter.measuredWidth
                    bottomheight = rltSpaceFooter.height
                    println("bottomHeight $bottomheight")
                    if (bottomheight == 0) {
                        bottomheight = 200
                    }
                    println("bottomHeight now $bottomheight")
                    rltSpaceFooter.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    getRelativeLayoutInfo()
                }
            })
        } else {
            rltSpaceFooter.visibility = View.GONE
            llt_contact_host.visibility = View.GONE
        }
        if (spaceResult.similarListing.isNotEmpty()) {
            if (!isHost) {
                setSimilarSpace()
            } else {
                rlt_similarlisting.visibility = View.GONE
            }
        } else {
            rlt_similarlisting.visibility = View.GONE
        }

        updateDataFromLocal()
        val font1 = Typeface.createFromAsset(assets, resources.getString(R.string.fonts_makent1))
        val icon3 = FontIconDrawable(
            this@SpaceDetailActivity,
            resources.getString(R.string.f1share),
            font1
        ).sizeDp(25).colorRes(R.color.title_text_color)
        iv_space_share.setImageDrawable(icon3)
    }

    /**
     * Here Setting the Filtered Datas
     * the Start and end date are in dd-MM-yyyy
     * i am converting dd-MM-yyyy Format to yyyy-MM-dd For the normal Function also to pass on api
     */
    private fun updateDataFromLocal() {
        if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn) != null) {
            spaceResult.LocalSavedDateTime.StartDate = commonMethods.YearFirstAPIFormat(
                localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn)
            )
        }
        if (localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut) != null) {
            spaceResult.LocalSavedDateTime.endDate = commonMethods.YearFirstAPIFormat(
                localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut)
            )
        }
        if (localSharedPreferences.getSharedPreferences(Constants.StartTime) != null) {
            spaceResult.LocalSavedDateTime.startTime =
                localSharedPreferences.getSharedPreferences(Constants.StartTime)
        }
        if (localSharedPreferences.getSharedPreferences(Constants.EndTime) != null) {
            spaceResult.LocalSavedDateTime.endTime =
                localSharedPreferences.getSharedPreferences(Constants.EndTime)
        }
        if (localSharedPreferences.getSharedPreferences(Constants.SearchGuest) != null) {
            spaceResult.LocalFilteredGuestCount =
                localSharedPreferences.getSharedPreferences(Constants.SearchGuest)
        }
    }

    /**
     * Show The Space Images
     */
    fun loadImage() {
        adapter = ImageAdapter(this)
        vp_space_image.setAdapter(adapter)

        vp_space_image?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                tv_description.setText(spaceImagesDescription[vp_space_image.currentItem])
            }

            override fun onPageSelected(position: Int) {

            }

        })

        handler = Handler()
        runnable = object : Runnable {
            var i = 0

            override fun run() {
                vp_space_image.setCurrentItem(i)
                //tv_description.setText(spaceImagesDescription[i])
                imagetoshare = spaceImagesItems[i]!!
                i++
                if (i > spaceImagesItems.size - 1) {
                    i = 0
                }
                handler!!.postDelayed(this, 5000)
            }
        }
        handler!!.postDelayed(runnable!!, 5000)
    }

    /**
     * Setting Space Data
     */
    fun settheSpace() {
        rv_the_space.isNestedScrollingEnabled = false
        rv_the_space.setHasFixedSize(true)
        theSpaceAdapter = TheSpaceAdapter(this, spaceResult.theSpace)
        rv_the_space.layoutManager = GridLayoutManager(this, 1)
        rv_the_space.adapter = theSpaceAdapter
    }

    /**
     * Setting Price
     */
    private fun setPricing() {

        rv_event_type.isNestedScrollingEnabled = false
        rv_event_type.setHasFixedSize(true)
        bedArrangementAdapter = BedArrangementAdapter(this, this, spaceResult.spaceActivityArray)
        rv_event_type.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv_event_type.adapter = bedArrangementAdapter

        tv_space_amount.text =
            HtmlCompat.fromHtml(spaceResult.currency_symbol, HtmlCompat.FROM_HTML_MODE_COMPACT)
                .toString() + " " + spaceResult.hourly
    }

    /**
     *  View Guest Access
     */
    private fun setGuestAccess() {
        rv_guest_access.isNestedScrollingEnabled = false
        rv_guest_access.setHasFixedSize(true)
        guestAccessListAdapter = GuestAccessListAdapter(this, spaceResult.guestAccess, this)
        rv_guest_access.layoutManager = GridLayoutManager(this, 2)
        rv_guest_access.adapter = guestAccessListAdapter
    }

    /**
     * Set Aminities For the Space
     */
    private fun setAmenity() {
        amenities_id = arrayOfNulls(spaceResult.amenities.size)
        amenities_name = arrayOfNulls(spaceResult.amenities.size)
        amenities_icon = arrayOfNulls(spaceResult.amenities.size)
        amenities_image = arrayOfNulls(spaceResult.amenities.size)
        amenitiescount = (spaceResult.amenities.size - 4).toString()

        tv_amenities_text.text = "+$amenitiescount"

        for (i in spaceResult.amenities.indices) {
            amenities_id[i] = spaceResult.amenities.get(i).id
            amenities_name[i] = spaceResult.amenities.get(i).name
            amenities_icon[i] = spaceResult.amenities.get(i).imageName
            amenities_image[i] = spaceResult.amenities.get(i).imageName
        }

        if (spaceResult.amenities.size == 0) {
            llt_amenities.visibility = View.GONE
        }
        if (spaceResult.amenities.size == 1) {
            if (!(this as Activity).isFinishing) {
                iv_amenities1.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[0]).into(iv_amenities1)
            }
        } else if (spaceResult.amenities.size == 2) {
            if (!(this as Activity).isFinishing) {
                iv_amenities1.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[0]).into(iv_amenities1)
                iv_amenities2.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[1]).into(iv_amenities2)
            }
        } else if (spaceResult.amenities.size == 3) {
            if (!(this as Activity).isFinishing) {
                iv_amenities1.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[0]).into(iv_amenities1)
                iv_amenities2.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[1]).into(iv_amenities2)
                iv_amenities3.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[2]).into(iv_amenities3)
            }
        } else if (spaceResult.amenities.size == 4) {
            if (!(this as Activity).isFinishing) {
                iv_amenities1.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[0]).into(iv_amenities1)
                iv_amenities2.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[1]).into(iv_amenities2)
                iv_amenities3.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[2]).into(iv_amenities3)
                iv_amenities4.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[3]).into(iv_amenities4)
            }
        } else if (spaceResult.amenities.size > 4) {
            if (!(this as Activity).isFinishing) {
                iv_amenities1.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[0]).into(iv_amenities1)
                iv_amenities2.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[1]).into(iv_amenities2)
                iv_amenities3.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[2]).into(iv_amenities3)
                iv_amenities4.setVisibility(View.VISIBLE)
                Glide.with(this).load(amenities_image[3]).into(iv_amenities4)
                iv_amenities1.setVisibility(View.VISIBLE)
                tv_amenities_text.setVisibility(View.VISIBLE)
            }
        }

        /**
         * View All Amenities
         */
        llt_amenities.setOnClickListener {
            val b = Bundle()
            b.putStringArray("amenities", amenities_name)
            b.putStringArray("amenitiesimages", amenities_image)
            b.putStringArray("amenitiesicons", amenities_icon)
            val x = Intent(applicationContext, AmenitiesActivity::class.java)
            x.putExtras(b)
            val bundleAnimation = ActivityOptions.makeCustomAnimation(
                applicationContext,
                R.anim.trans_left_in,
                R.anim.trans_left_out
            ).toBundle()
            startActivity(x, bundleAnimation)
        }
    }

    /**
     * Service Offered
     */
    private fun setserviceOffered() {
        serviceOfferAdapter = ServiceOfferAdapter(this, spaceResult.services, this)
        rv_service_offer.layoutManager = GridLayoutManager(this, 2)
        rv_service_offer.adapter = serviceOfferAdapter
        rv_service_offer.isNestedScrollingEnabled = false
        rv_service_offer.setHasFixedSize(true)
    }

    /**
     * Special Feature in rooms
     */
    private fun setSpecialFeature() {
        specialFeatureAdapter = SpecialFeatureAdapter(this, spaceResult.specialFeature, this)
        rv_special_feature.layoutManager = GridLayoutManager(this, 2)
        rv_special_feature.adapter = specialFeatureAdapter
        rv_special_feature.isNestedScrollingEnabled = false
        rv_special_feature.setHasFixedSize(true)
    }


    /**
     * Set Space Rules
     */
    private fun setSpaceRules() {
        spaceRulesAdapter = SpaceRulesAdapter(this, spaceResult.spaceRules, this)
        rv_space_rules.layoutManager = GridLayoutManager(this, 2)
        rv_space_rules.adapter = spaceRulesAdapter
        rv_space_rules.isNestedScrollingEnabled = false
        rv_space_rules.setHasFixedSize(true)
    }

    private fun setSpaceStyle() {
        spaceStyleAdapter = SpaceStyleAdapter(this, spaceResult.spaceStyle, this)
        rv_space_style.layoutManager = GridLayoutManager(this, 2)
        rv_space_style.adapter = spaceStyleAdapter
        rv_space_style.isNestedScrollingEnabled = false
        rv_space_style.setHasFixedSize(true)
    }

    /**
     * Setting user Review For the Space
     */
    private fun setReviewDatas() {
        tv_review_name.text = spaceResult.reviewUserName
        tv_review_date.text = spaceResult.reviewDate
        tv_review_message.text = spaceResult.reviewMessage
        tv_review_count.text =
            resources.getString(R.string.read) + " " + spaceResult.reviewCount + " " + resources.getString(
                R.string.review_s_one
            )
        tv_space_rate_count.text = spaceResult.reviewCount
        rb_review_rate.rating = spaceResult.rating.toFloat()
        rb_space_rate.rating = spaceResult.rating.toFloat()

        Glide.with(applicationContext).asBitmap().load(spaceResult.reviewUserImage)
            .into(object : BitmapImageViewTarget(iv_space_review) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(resources, resource)
                    circularBitmapDrawable.isCircular = true
                    iv_space_review.setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    /**
     * Setting Similar Spaces
     */
    private fun setSimilarSpace() {
        similarSpaceAdapter = SimilarSpaceAdapter(this, this, spaceResult.similarListing)
        rv_similar_list.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv_similar_list.adapter = similarSpaceAdapter
        rv_similar_list.isNestedScrollingEnabled = false
        rv_similar_list.setHasFixedSize(true)
    }

    /**
     * Setting Space Available timings
     */
    private fun setSpaceAvailability() {
        lltSpaceAvailable.removeAllViews()
        for (i in spaceResult.spaceAvailabilityTimes.indices) {
            val view =
                LayoutInflater.from(applicationContext).inflate(R.layout.space_available_time, null)
            val lltDates = view.findViewById<LinearLayout>(R.id.lltDates)
            val tvDay = view.findViewById<TextView>(R.id.tvDay)
            val tvAvailable = view.findViewById<TextView>(R.id.tvAvailable)

            if (spaceResult.spaceAvailabilityTimes[i].status.equals("Closed", ignoreCase = true)) {
                lltDates.visibility = View.GONE
                val days = spaceResult.spaceAvailabilityTimes[i].datType + 1
                spaceResult.blockedDay.add(days)
            } else {
                tvDay.text = spaceResult.spaceAvailabilityTimes[i].key
                tvAvailable.text = spaceResult.spaceAvailabilityTimes[i].value.replace(",", "\n")
                lltDates.visibility = View.VISIBLE
            }
            lltSpaceAvailable.addView(view)
        }
    }

    /**
     * Load Location on Map
     */
    private fun loadMapImage(location_address: String, latLng: LatLng) {
        try {
            val staticMaps = GoogleStaticMapsAPIServices()

            Glide.with(applicationContext).load(
                staticMaps.getStaticMapURL(
                    latLng,
                    100,
                    resources.getString(R.string.google_key)
                )
            )

                .into(object : DrawableImageViewTarget(iv_map) {})
            tv_map_address.text = location_address
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Image Slider Adapter
     */
    inner class ImageAdapter internal constructor(internal var context: Context) : PagerAdapter() {

        private var mThumbIds = spaceImagesItems
        private var mThumbDescription = spaceImagesDescription
        override fun getCount(): Int {
            return mThumbIds.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as ImageView
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val ivFirstSpaceImage = ImageView(context)
            ivFirstSpaceImage.setScaleType(ImageView.ScaleType.FIT_XY)

            Glide.with(applicationContext).load(mThumbIds[position])

                .into(object : DrawableImageViewTarget(ivFirstSpaceImage) {})
            (container as ViewPager).addView(ivFirstSpaceImage, 0)
            return ivFirstSpaceImage
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            (container as ViewPager).removeView(`object` as ImageView)
        }

    }

    /**
     * OnResume To Start Shimmer
     */
    override fun onResume() {
        super.onResume()
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "")
            if (commonMethods.isOnline(this)) {
                shimmerFrame.visibility = View.VISIBLE
                loadSpaceDetail() //get the Space Detail From Api
            } else {
                commonMethods.snackBar(
                    resources.getString(R.string.interneterror),
                    "",
                    false,
                    2,
                    tv_space_host_name,
                    resources,
                    this
                )
            }
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "Space_detail")
        }
        shimmerFrame.startShimmer()
    }

    /**
     * onStop When shimmer Effect
     */
    override fun onStop() {
        super.onStop()
        shimmerFrame.stopShimmer()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().removeAllStickyEvents()
    }

    /**
     * Click listeners From Adapter For ShowMore
     */
    override fun onItemClick(type: String) {
        if (type.equals("GuestAccess", ignoreCase = true)) {
            guestAccessListAdapter.UpdateAdapter()
        } else if (type.equals("ServiceOffered", ignoreCase = true)) {
            serviceOfferAdapter.UpdateAdapter()
        } else if (type.equals("SpecialFeatures", ignoreCase = true)) {
            specialFeatureAdapter.UpdateAdapter()
        } else if (type.equals("SpaceRules", ignoreCase = true)) {
            spaceRulesAdapter.UpdateAdapter()
        } else if (type.equals("SpaceStyle", ignoreCase = true)) {
            spaceStyleAdapter.UpdateAdapter()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_space_host_name,
            R.id.iv_space_host_image -> {
                val x = Intent(applicationContext, ProfilePageActivity::class.java)
                println("Other user id$hostUserId")
                x.putExtra("otheruserid", hostUserId)
                startActivity(x)
            }
        }
    }
}