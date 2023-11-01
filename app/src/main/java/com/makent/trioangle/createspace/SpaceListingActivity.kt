package com.makent.trioangle.createspace

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.gson.Gson
import com.makent.trioangle.MainActivity
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.adapter.SpaceListingAdapter
import com.makent.trioangle.createspace.model.hostlisting.SpaceListingDetails
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.ListFunc
import com.makent.trioangle.helper.Constants.ListFuncAdd
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_space_listing.*
import kotlinx.android.synthetic.main.no_token_layout.*
import javax.inject.Inject

class SpaceListingActivity : AppCompatActivity() ,SpaceListingAdapter.OnItemClickListener,ServiceListener{

    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    private lateinit var spaceListingAdapter: SpaceListingAdapter

    private lateinit var spaceListingModel: SpaceListingDetails


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_listing)
        AppController.getAppComponent().inject(this)
        init()
    }

    private fun getSpaceDetailsApi() {
        val imageViewTarget1 = DrawableImageViewTarget(ivSpaceListingLoader)
        Glide.with(this).load(R.drawable.dot_loading).into(imageViewTarget1)
        nsvSpaceList.visibility = View.GONE
        vEmptyListing.visibility = View.GONE

        //ivSpaceListingLoader.visibility = View.VISIBLE
        shimmer_frame.startShimmer()
        shimmer_frame.visibility = View.VISIBLE
        apiService.getListing(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(RequestCallback(this))
    }

    private fun init() {
        localSharedPreferences  = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        viewNotoken.setVisibility(View.GONE)

        btnNoTokenLogin.setOnClickListener {
            val home = Intent(applicationContext, MainActivity::class.java)
            home.putExtra("isback", 1)
            startActivity(home)
        }

        fab.setOnClickListener {
            val addListing = Intent(applicationContext, BasicStepsActivity::class.java)
            addListing.putExtra(ListFunc,ListFuncAdd)
            startActivity(addListing)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            shimmer_frame.startShimmer()
            getSpaceDetailsApi()
        }else{
            shimmer_frame.visibility = View.GONE
            nsvSpaceList.visibility = View.GONE
            vEmptyListing.visibility = View.GONE
            fab.visibility = View.GONE
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "listing")
            viewNotoken.setVisibility(View.VISIBLE)
            tvTitle.setText(resources.getString(R.string.listings))
            tvdescription.setText(resources.getString(R.string.no_token_listing))
            ivNoToken.setImageDrawable(resources.getDrawable(R.drawable.token_listing))
        }
    }

    override fun onStop() {
        super.onStop()
        shimmer_frame.stopShimmer()
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        //ivSpaceListingLoader.visibility = View.GONE
        shimmer_frame.stopShimmer()
        shimmer_frame.visibility = View.GONE
        if(jsonResp!!.isSuccess) {
            onSuccessListing(jsonResp)
        }else{
            if (jsonResp.statusMsg.equals("No Data Found",ignoreCase = true)){
                nsvSpaceList.visibility = View.GONE
                vEmptyListing.visibility = View.VISIBLE
            }else {
                commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, rvCompleted, resources, this)
            }
        }
    }

    override fun onItemClick(position: Int, spaceId: String) {
        val x = Intent(applicationContext, EditListingActivity::class.java)
        localSharedPreferences.saveSharedPreferences(Constants.mSpaceId,spaceId)
        startActivity(x)
    }



    private fun onSuccessListing(jsonResp: JsonResponse?) {
        nsvSpaceList.visibility = View.VISIBLE
        vEmptyListing.visibility = View.GONE

        spaceListingModel = gson.fromJson(jsonResp?.strResponse, SpaceListingDetails::class.java)
        val mLayoutManager = LinearLayoutManager(this)
        val mLayoutManagerListed = LinearLayoutManager(this)

        if (spaceListingModel.listed.isNotEmpty()) {
            lltListed.visibility = View.VISIBLE
            spaceListingAdapter = SpaceListingAdapter(spaceListingModel.listed, this, this)
            rvCompleted!!.layoutManager = mLayoutManagerListed
            rvCompleted!!.adapter = spaceListingAdapter
            rvCompleted!!.isNestedScrollingEnabled = false
        } else {
            lltListed.visibility = View.GONE
        }

        if (spaceListingModel.unlisted.isNotEmpty()) {
            lltUnListed.visibility = View.VISIBLE
            spaceListingAdapter = SpaceListingAdapter(spaceListingModel.unlisted, this, this)
            rvInProgress!!.layoutManager = mLayoutManager
            rvInProgress!!.adapter = spaceListingAdapter
            rvInProgress!!.isNestedScrollingEnabled = false
        } else {
            lltUnListed.visibility = View.GONE
        }

    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        //ivSpaceListingLoader.visibility = View.GONE
        shimmer_frame.stopShimmer()
        shimmer_frame.visibility = View.GONE
        nsvSpaceList.visibility = View.GONE
        vEmptyListing.visibility = View.VISIBLE
    }
}
