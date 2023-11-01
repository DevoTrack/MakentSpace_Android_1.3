package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.Constants.*
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity
import com.makent.trioangle.util.CommonMethods

import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_edit_listing.*
import java.io.Serializable
import javax.inject.Inject

class EditListingActivity : AppCompatActivity(), ServiceListener {

    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    lateinit var mydialog: Dialog_loading

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson



    lateinit var spaceListingModel: SpaceListingModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_listing)

        AppController.getAppComponent().inject(this)
        lltSpaceSteps.visibility=View.GONE
        init()

        tv_basic_edit.setOnClickListener {
            val intent = Intent(this, BasicStepsActivity::class.java)
            intent.putExtra(ListFunc, ListFuncEdit)
            intent.putExtra(BASICSTEP, spaceListingModel.basics as Serializable)
            startActivity(intent)
        }

        tv_setup_edit.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            intent.putExtra(SETUPSTEP, spaceListingModel.setup as Serializable)
            startActivity(intent)
        }
        tv_get_ready_edit.setOnClickListener {
            val intent = Intent(this, GetReadyToHostActivity::class.java)
            intent.putExtra(READYTOHOSTSTEP, spaceListingModel.ready_to_host as Serializable)
            startActivity(intent)
        }

        btn_preview.setOnClickListener {
            val preview = Intent(this, SpaceDetailActivity::class.java)
            preview.putExtra(isFromHost, true)
            localSharedPreferences.saveSharedPreferences(Host_Preview,"1")
            startActivity(preview)
        }
        /*tv_get_ready.setOnClickListener{
            val intent = Intent(this,EditCalendar::class.java)
            intent.putExtra(READYTOHOSTSTEP, spaceListingModel.ready_to_host as Serializable)
            startActivity(intent)
        }*/

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * When it is from addressFragment
         * finish and recreate the Create using intent
         */
        if (resultCode==ActivityBasic)
        {
            finish()
        }
    }

    private fun init()
    {
        localSharedPreferences  = LocalSharedPreferences(this)
        mydialog = Dialog_loading(this)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }


    override fun onResume()
    {
        super.onResume()
        getListingInfo()
        localSharedPreferences.saveSharedPreferences(Host_Preview,"0")
        localSharedPreferences.saveSharedPreferences(Host_Preview,"0")
    }

    private fun getListingInfo()
    {
        if (!mydialog.isShowing)
            mydialog.show()
        apiService.getListingDetails(localSharedPreferences.getSharedPreferences(AccessToken), localSharedPreferences.getSharedPreferences(mSpaceId)).enqueue(RequestCallback(this))

    }

    private fun onChangeSpaceStatus(status : String)
    {
        if (!mydialog.isShowing) mydialog.show()
        apiService.updateSpace(updateStatus(status)).enqueue(RequestCallback(this))
    }

    private fun updateStatus(status : String):HashMap<String,String>
    {
        val statusUpdate=HashMap<String,String>()
        statusUpdate.put("space_id",localSharedPreferences.getSharedPreferences(mSpaceId))
        statusUpdate.put("token",localSharedPreferences.getSharedPreferences(AccessToken))
        statusUpdate.put("step","basics")
        statusUpdate.put("status",status)
        return statusUpdate
    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?)
    {
        if (mydialog.isShowing)
            mydialog.dismiss()

        onSuccessListingDetails(jsonResp)


        //onSuccessListingDetails(jsonResp)
    }

    private fun onSuccessChangeStatus(jsonResp: JsonResponse?)
    {

    }

    private fun onSuccessListingDetails(jsonResp: JsonResponse?) {
        spaceListingModel = gson.fromJson(jsonResp?.strResponse, SpaceListingModel::class.java)
        updateView()
    }

    private fun getAdminStatus(adminStatus: String): String {
        return when(adminStatus){
            APPROVED -> resources.getString(R.string.approved)
            PENDING -> resources.getString(R.string.pending)
            RESUBMIT -> resources.getString(R.string.resubmit)
            else -> ""
        }

    }

    private fun updateView() {
        pb.progress = spaceListingModel.completed

        println("admin status : "+spaceListingModel.admin_status)
        if(spaceListingModel.status!=null&&!spaceListingModel.status.equals("")){
            btnStatus.visibility = View.VISIBLE

            btnStatus.text = getAdminStatus(spaceListingModel.admin_status)
        }
        else
            btnStatus.visibility = View.GONE

        if(spaceListingModel.basics.status.equals("completed",ignoreCase = true)){
            iv_basic_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified_success))
            iv_basic_tick.setColorFilter(getResources().getColor(R.color.background), android.graphics.PorterDuff.Mode.MULTIPLY);
            tv_basic_remainingsteps.visibility=View.GONE
            tv_basic_edit.text=resources.getString(R.string.need_to_make_a_change)
        }else{
            tv_basic_edit.text=resources.getString(R.string.continue_)
            tv_basic_remainingsteps.visibility=View.VISIBLE
            if(spaceListingModel.basics.remainingSteps.equals("1")) {
                tv_basic_remainingsteps.text = resources.getString(R.string.more_step_to_complete, spaceListingModel.basics.remainingSteps)
            }else{
                tv_basic_remainingsteps.text = resources.getString(R.string.more_steps_to_complete, spaceListingModel.basics.remainingSteps)
            }
            iv_basic_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified))
        }

        if(spaceListingModel.setup.status.equals("completed",ignoreCase = true)){
            iv_setup_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified_success))
            tv_setup_remainingsteps.visibility=View.GONE
            tv_setup_edit.text=resources.getString(R.string.edit)
        }else{
            tv_setup_remainingsteps.visibility=View.VISIBLE
            tv_setup_edit.text=resources.getString(R.string.continue_)
            if(spaceListingModel.setup.remainingSteps.equals("1")) {
                tv_setup_remainingsteps.text = resources.getString(R.string.more_step_to_complete, spaceListingModel.setup.remainingSteps)
            }else{
                tv_setup_remainingsteps.text = resources.getString(R.string.more_steps_to_complete, spaceListingModel.setup.remainingSteps)
            }
          iv_setup_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified))
        }

        if(spaceListingModel.ready_to_host.status.equals("completed",ignoreCase = true)){
            iv_readyTohost_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified_success))
            iv_readyTohost_tick.setColorFilter(getResources().getColor(R.color.background), android.graphics.PorterDuff.Mode.MULTIPLY);
            tv_RTH_remainingsteps.visibility=View.GONE
            tv_get_ready_edit.text=resources.getString(R.string.edit)
        }else{
            tv_get_ready_edit.text=resources.getString(R.string.continue_)
            tv_RTH_remainingsteps.visibility=View.VISIBLE
            if(spaceListingModel.ready_to_host.remainingSteps.equals("1")) {
                tv_RTH_remainingsteps.text = resources.getString(R.string.last_step_to_complete, spaceListingModel.ready_to_host.remainingSteps)
            }else{
                tv_RTH_remainingsteps.text = resources.getString(R.string.last_steps_to_complete, spaceListingModel.ready_to_host.remainingSteps)
            }
            iv_readyTohost_tick.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_verified))
        }

        tv_title.text=resources.getString(R.string.makentspace_we_are_almost_done)
        tv_pb_status.text = getString(R.string.your_listing_txt)+spaceListingModel.completed+getString(R.string.percentage_done)
        lltSpaceSteps.visibility=View.VISIBLE
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

}
