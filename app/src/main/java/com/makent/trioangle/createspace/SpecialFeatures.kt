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
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import javax.inject.Inject

class SpecialFeatures : Fragment() ,ServiceListener {



    private var btnContinueEnable: Boolean = false

    lateinit var mydialog: Dialog_loading


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    var resultTemp: List<String> = ArrayList<String>()
    var result: ArrayList<String> = ArrayList<String>()


    lateinit var res: Resources

    lateinit var setupStepModel: SetupStepModel

    lateinit var setUpDetailsList: SetupSetListModel


    lateinit var listner: SetupActivityInterface

    lateinit var mActivity: SetupActivity

    var specialFeatureList: ArrayList<SpecailFeaturesList> = ArrayList<SpecailFeaturesList>()


    lateinit var rvSpeacialFeature: RecyclerView
    lateinit var btnContinue: Button

    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.special_feature_style,
                container, false)

        mContext = container!!.context



        rvSpeacialFeature = view!!.findViewById<RecyclerView>(R.id.rv_speacial_feature)

        rvSpeacialFeature.layoutManager = LinearLayoutManager(mContext)



        btnContinue = view.findViewById<Button>(R.id.btn_continue)


        btnContinue.setOnClickListener {

            val specialFeatures = TextUtils.join(",", result)
            setupStepModel.specialFeature = specialFeatures
            setUpDetailsList.specialFeatures = specialFeatureList

            if (commonMethods.isOnline(mActivity)) {
                if (!mydialog.isShowing) mydialog.show()
                apiService.updateSpace(updateSpecialFeaturesParams(specialFeatures)).enqueue(RequestCallback(this))
            } else {
                commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure), "", false, 2, btnContinue, mActivity.resources, mActivity)
            }

        }

        initListner()
        init()
        val myAdapter = object : GenericAdapter<Any>(specialFeatureList) {
            override fun onItemClick(pos: Int, id: String, name: String) {
                if (result.contains(id))
                    result.remove(id)
                else
                    result.add(id)
                updateMainList()
            }

            override fun getLayoutId(position: Int, obj: Any): Int {

                return R.layout.special_feature_list
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view,viewType,this)
            }
        }

        rvSpeacialFeature.adapter=myAdapter

        return view
    }


    private fun updateSpecialFeaturesParams(specialFeatures: String): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "setup"
        updateSpace["special_feature"] = specialFeatures

        return updateSpace
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

    private fun onSuccessResponse() {
        val navController = Navigation.findNavController(activity!!, R.id.setup_nav_host_fragment)
        navController.navigate(R.id.action_specialFeaturesFragment_to_spaceRulesFragment)
        mActivity.progressBarUpdate(45, 70)
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else activity!!.resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as SetupActivity?)!!

        mActivity.nsv_setup.scrollTo(0,0)
    }







    private fun init() {


        setupStepModel = mActivity.getSetupDetailsModel()
        setUpDetailsList = mActivity.getSetupDetailsList()
        specialFeatureList = ArrayList<SpecailFeaturesList>()
        specialFeatureList.addAll(setUpDetailsList.specialFeatures)


        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

        var specialFeature: String? = setupStepModel.specialFeature

        if(specialFeature!=null) {
            result.clear()

            resultTemp = specialFeature.split(",").map { it.trim() }

            result.addAll(resultTemp)

            for (x in 0 until result.size)
                println("Space style list : " + result.get(x))

            updateMainList()
        }else{
            result.clear()
            for (x in 0 until specialFeatureList.size){
                if(specialFeatureList[x].isSelected){
                    resultTemp = specialFeatureList[x].id.split(",").map { it.trim() }
                    result.addAll(resultTemp)
                }
            }

        }


    }





    private fun updateMainList() {

        btnContinueEnable = false
        for (y in 0 until specialFeatureList.size) {

            for (x in 0 until result.size) {


                if (result.get(x).equals(specialFeatureList.get(y).id)) {
                    specialFeatureList.get(y).isSelected = true
                    btnContinueEnable = true
                    break
                } else {
                    specialFeatureList.get(y).isSelected = false
                }
            }

        }
        if (result.isEmpty()){
            for (z in 0 until specialFeatureList.size) {
                specialFeatureList.get(z).isSelected = false
            }
        }


        //buttonEnableDisable()

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