package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import javax.inject.Inject

class SpaceDescriptionFragment : Fragment(), ServiceListener {

    lateinit var mydialog: Dialog_loading

    private var listNameEnable: Boolean = false
    private var summaryEnable: Boolean = false


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson


    lateinit var res: Resources

    lateinit var listner: SetupActivityInterface

    lateinit var mActivity: SetupActivity

    lateinit var edtLstName: EditText
    lateinit var edtSummaryName: EditText
    lateinit var edtSpaceName: EditText
    lateinit var edtGuestAccessName: EditText
    lateinit var edtGuestInteraction: EditText
    lateinit var lltMoreSpace: LinearLayout
    lateinit var edtOtherThingsNotes: EditText
    lateinit var edtSpaceRulesName: EditText
    lateinit var tvMoreDetailsTxt: TextView
    lateinit var tvListingCountTxt: TextView
    lateinit var tvSummaryCountTxt: TextView
    val maxLstName : Int = 35
    val maxSummary : Int = 500

    lateinit var btnContinue: Button

    lateinit var setupStepModel: SetupStepModel

    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.space_description_layout,
                container, false)

        mContext = container!!.context

        init()
        initListner()

        edtLstName = view!!.findViewById<EditText>(R.id.edt_lst_name)
        edtSummaryName = view.findViewById<EditText>(R.id.edt_summary_name)
        edtSpaceName = view.findViewById<EditText>(R.id.edt_space_name)
        edtGuestAccessName = view.findViewById<EditText>(R.id.edt_guest_access_name)
        edtGuestInteraction = view.findViewById<EditText>(R.id.edt_guest_interaction_name)
        edtOtherThingsNotes = view.findViewById<EditText>(R.id.edt_other_things_name)
        edtSpaceRulesName = view.findViewById<EditText>(R.id.edt_space_rules_name)
        tvMoreDetailsTxt = view.findViewById(R.id.tv_more_details_txt)
        lltMoreSpace = view.findViewById(R.id.llt_more_space)
        tvListingCountTxt = view.findViewById(R.id.tv_listing_count_txt)
        tvSummaryCountTxt = view.findViewById(R.id.tv_summary_count_txt)


        edtLstName.addTextChangedListener(EditTextWatcher(edtLstName))
        edtSummaryName.addTextChangedListener(EditTextWatcher(edtSummaryName))


        //tvMoreDetailsTxt.text = Html.fromHtml("You can add more" + "<font color=red>" + " details " + "</font>" + "to tell travelers about your space and hosting style.")

        tvMoreDetailsTxt.text = Html.fromHtml(mActivity.getRes().getString(R.string.add_more) + "<font color=red>" +mActivity.getRes().getString(R.string.details_small)  + "</font>" +mActivity.getRes().getString(R.string.tell_travelers))

        tvMoreDetailsTxt.setOnClickListener {

            tvMoreDetailsTxt.visibility = View.GONE
            lltMoreSpace.visibility = View.VISIBLE

        }

        btnContinue = view.findViewById<Button>(R.id.btn_continue)

        setupStepModel = mActivity.getSetupDetailsModel()

        edtLstName.setText(setupStepModel.name)
        edtSummaryName.setText(setupStepModel.summary)
        edtSpaceName.setText(setupStepModel.space)
        edtGuestAccessName.setText(setupStepModel.access)
        edtGuestInteraction.setText(setupStepModel.interaction)
        edtOtherThingsNotes.setText(setupStepModel.notes)
        edtSpaceRulesName.setText(setupStepModel.houseRules)

        listNameEnable = !setupStepModel.name.equals("")
        summaryEnable = !setupStepModel.summary.equals("")

        buttonEnableDisable()

        btnContinue.setOnClickListener {

            if(commonMethods.isOnline(mActivity)) {
                if (!mydialog.isShowing) mydialog.show()
                apiService.updateSpaceDescription(updateSpaceDescriptionParams()).enqueue(RequestCallback(this))
            }else{
                commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
            }
        }
        return view
    }


    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
        if (jsonResp!!.isSuccess) {
            mActivity.finish()
        }else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg,"",false,2,btnContinue,mActivity.resources,mActivity)
        }
    }


    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }


    private fun updateSpaceDescriptionParams(): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "setup"
        updateSpace["name"] = edtLstName.text.toString()
        updateSpace["summary"] = edtSummaryName.text.toString()
        updateSpace["space"] = edtSpaceName.text.toString()
        updateSpace["access"] = edtGuestAccessName.text.toString()
        updateSpace["interaction"] = edtGuestInteraction.text.toString()
        updateSpace["notes"] = edtOtherThingsNotes.text.toString()
        updateSpace["house_rules"] = edtSpaceRulesName.text.toString()

        return updateSpace
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else activity!!.resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as SetupActivity?)!!

        mActivity.nsv_setup.scrollTo(0, 0)
    }


    private fun init() {

        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        localSharedPreferences = LocalSharedPreferences(mContext)

    }


    // Validate email field
    inner class EditTextWatcher(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            when (view.id) {

                R.id.edt_lst_name -> {
                    tvListingCountTxt.visibility  = View.VISIBLE
                    val chars = maxLstName - edtLstName.text.length

                    tvListingCountTxt.text =   chars.toString() + " characters left"
                    listNameEnable = edtLstName.text.length>0
                    buttonEnableDisable()
                }
                R.id.edt_summary_name -> {

                    tvSummaryCountTxt.visibility  = View.VISIBLE

                    val chars = maxSummary - edtSummaryName.text.length

                    summaryEnable = edtSummaryName.text.length>0
                    tvSummaryCountTxt.text = chars.toString() + " characters left"
                    buttonEnableDisable()
                }
            }


        }
    }


    private fun buttonEnableDisable() {
        btnContinue.isEnabled = listNameEnable && summaryEnable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SetupActivity) {
            listner = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }

}




