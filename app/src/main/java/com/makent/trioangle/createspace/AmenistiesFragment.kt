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

class AmenistiesFragment : Fragment(),ServiceListener {


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


    var basicStepsModel: BasicStepModel? = null

    lateinit var basicStepsModellist: BasicStepsModel

    var resultTemp: List<String> = ArrayList<String>()
    var result: ArrayList<String> = ArrayList<String>()

    var amenitiesList: ArrayList<AmenitiesModel> = ArrayList()
    lateinit var rvAmenities: RecyclerView
    lateinit var btnContinue: Button

    lateinit var mContext: Context



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.amenities_layout,
                container, false)

        mContext = container!!.context
        rvAmenities = view!!.findViewById<RecyclerView>(R.id.rvAmenities)
        rvAmenities.layoutManager = LinearLayoutManager(mContext)

        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        initListner()
        init()


        return view
    }


    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.res != null) listner.res else activity!!.resources
        mActivity = if (listner.instance != null) listner.instance else (activity as BasicStepsActivity?)!!

        mActivity.nsvBasic.scrollTo(0,0)
    }


    private fun init() {

        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)


        /**
         * All Amenities Form Api
         */
        basicStepsModellist = mActivity.basicStepsList
        amenitiesList = ArrayList()
        amenitiesList.addAll(basicStepsModellist.amenitiesList)


        /**
         * Selected Amenities from Api as string
         */
        basicStepsModel = mActivity.basicStepModelData
        if (basicStepsModel!=null) {

            var amenities: String = basicStepsModel!!.amenities

            result.clear()

            resultTemp = amenities.split(",").map { it.trim() }

            result.addAll(resultTemp)

            updateMainList()

        }else{
            result.clear()
            for (x in 0 until amenitiesList.size){
                if(amenitiesList[x].isSelected){
                    resultTemp = amenitiesList[x].id.split(",").map { it.trim() }
                    result.addAll(resultTemp)
                }
            }

            //btnContinue.isEnabled = result.size>0
        }

        val myAdapter = object : GenericAdapter<Any>(amenitiesList) {
            override fun onItemClick(pos: Int, id: String, name: String) {

                if (result.contains(id))
                    result.remove(id)
                else
                    result.add(id)

                updateMainList()
            }

            override fun getLayoutId(position: Int, obj: Any): Int {
                return R.layout.amenities_list_item
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view, viewType, this)
            }
        }
        rvAmenities.adapter = myAdapter



        btnContinue.setOnClickListener {
                if (basicStepsModel != null) {
                    val amenities = TextUtils.join(",", result)
                    basicStepsModel!!.amenities = amenities
                    basicStepsModellist.amenitiesList = amenitiesList

                    if(commonMethods.isOnline(mActivity)) {
                        if (!mydialog.isShowing) mydialog.show()
                        apiService.updateSpace(updateAmenitiesParams(amenities)).enqueue(RequestCallback(this))
                    }else{
                        commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
                    }
                } else {
                    if(result.isNotEmpty()) {
                        val selectedAmenities = TextUtils.join(",", result)
                        println("getAmeitis $result")
                        mActivity.putHashMap("amenities", selectedAmenities)
                    }
                    val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
                    navController.navigate(R.id.action_amenitiesFragment_to_serviceFragment)
                    mActivity.progressBarUpdate(45, 60)
                }
        }

    }

    private fun updateAmenitiesParams(amenities: String): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "basics"
        updateSpace["amenities"] = amenities

        return updateSpace
    }


    private fun updateMainList() {
        for (y in 0 until amenitiesList.size) {

            for (x in 0 until result.size) {


                if (result.get(x).equals(amenitiesList.get(y).id)) {
                    amenitiesList.get(y).isSelected = true
                    break
                } else {
                    amenitiesList.get(y).isSelected = false
                }
            }

        }

        if (result.isEmpty()){
            for (z in 0 until amenitiesList.size) {
                amenitiesList.get(z).isSelected = false
            }
        }

        //btnContinue.isEnabled = result.isNotEmpty()
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
        navController.navigate(R.id.action_amenitiesFragment_to_serviceFragment)
        mActivity.progressBarUpdate(45, 60)
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