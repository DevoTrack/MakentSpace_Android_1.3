package com.makent.trioangle.createspace

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.genericadapter.JavaViewHolderFactory
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface
import com.makent.trioangle.createspace.model.hostlisting.basics.BasicStepsModel
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ServiceFragment : Fragment(), ServiceListener {

    lateinit var mydialog: Dialog_loading


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson


    lateinit var res: Resources
    lateinit var listner: BasicStepsActivityInterface
    lateinit var mActivity: BasicStepsActivity
    lateinit var rvService: RecyclerView
    lateinit var edtAdditional: EditText
    lateinit var btnContinue: Button
    lateinit var mContext: Context


    var basicStepsModel: BasicStepModel?=null

    lateinit var basicStepsModellist: BasicStepsModel

    var resultTemp: List<String> = ArrayList<String>()
    var result: ArrayList<String> = ArrayList<String>()

    var serviceList: ArrayList<ServiceModel> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.service_layout,
                container, false)

        mContext = container!!.context
        rvService = view!!.findViewById<RecyclerView>(R.id.rvService)
        rvService.layoutManager = LinearLayoutManager(mContext)
        edtAdditional = view.findViewById<EditText>(R.id.edt_additional)
        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        initListner()
        init()
        val sample :String = "If there is any additional information about the services you listed above, like available packages and/or rate options, please share more details below.If there is any additional information about the services you listed above, like available packages and/or rate options, please share more details below.If there is any additional information about the services you listed above, like available packages and/or rate options, please share more details below.If there is any additional information ab"
        println("Sample : "+sample.length)

        return view
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.res != null) listner.res else activity!!.resources
        mActivity = if (listner.instance != null) listner.instance else (activity as BasicStepsActivity?)!!
        mActivity.nsvBasic.scrollTo(0, 0)
    }


    private fun init() {
        rvService.layoutManager = LinearLayoutManager(mContext)
        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

        /**
         * All Service Form Api
         */
        basicStepsModellist = mActivity.basicStepsList
        serviceList = ArrayList<ServiceModel>()
        serviceList.addAll(basicStepsModellist.serviceList)

        /**
         * Selected Service from api as String
         */
        basicStepsModel = mActivity.basicStepModelData

        if(basicStepsModel!=null) {

            edtAdditional.setText(basicStepsModel!!.servicesExtra)

            var services: String = basicStepsModel!!.services

            result.clear()

            resultTemp = services.split(",").map { it.trim() }

            result.addAll(resultTemp)


            updateMainList()

        }else{
            result.clear()
            for (x in 0 until serviceList.size){
                if(serviceList[x].isSelected){
                    resultTemp = serviceList[x].id.split(",").map { it.trim() }
                    result.addAll(resultTemp)
                }
            }
            //btnContinue.isEnabled = result.size>0
        }

        val myAdapter = object : GenericAdapter<Any>(serviceList) {
            override fun onItemClick(pos: Int, id: String, name: String) {


                if (result.contains(id))
                    result.remove(id)
                else
                    result.add(id)

                updateMainList()
            }

            override fun getLayoutId(position: Int, obj: Any): Int {
                return R.layout.service_list_item
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view, viewType, this)
            }
        }
        rvService.adapter = myAdapter



        btnContinue.setOnClickListener {


            if (basicStepsModel!=null) {
                val services = TextUtils.join(",", result)
                basicStepsModel!!.services = services
                basicStepsModellist.serviceList = serviceList

                if(commonMethods.isOnline(mActivity)) {
                    if (!mydialog.isShowing)
                        mydialog.show()
                    apiService.updateSpace(updateServicesParams(services)).enqueue(RequestCallback(this))
                }else{
                    commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
                }
            }else{
                if(result.isNotEmpty()) {
                    val selectedService = TextUtils.join(",", result)
                    mActivity.putHashMap("services", selectedService)
                    mActivity.putHashMap("services_extra", edtAdditional.text.toString())
                }
                val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
                navController.navigate(R.id.action_serviceFragment_to_addressFragment)
                mActivity.progressBarUpdate(60, 75)
            }

        }


    }


    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
        if (jsonResp!!.isSuccess) {
            onSuccessResponse()
        }else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg,"",false,2,btnContinue,mActivity.resources,mActivity)
        }
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {

    }

    private fun onSuccessResponse() {
        val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
        navController.navigate(R.id.action_serviceFragment_to_addressFragment)
        mActivity.progressBarUpdate(60, 75)

    }

    private fun updateServicesParams(services: String): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "basics"
        updateSpace["services"] = services
        updateSpace["services_extra"] = edtAdditional.text.toString()

        return updateSpace
    }


    private fun updateMainList() {
        for (y in 0 until serviceList.size) {

            for (x in 0 until result.size) {


                if (result.get(x).equals(serviceList.get(y).id)) {
                    serviceList.get(y).isSelected = true
                    break
                } else {
                    serviceList.get(y).isSelected = false
                }
            }

        }
        if (result.isEmpty()){
            for (z in 0 until serviceList.size) {
                serviceList.get(z).isSelected = false
            }
        }

        //btnContinue.isEnabled = result.isNotEmpty()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is BasicStepsActivityInterface) {
            listner = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }

}