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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SpaceStyleFragment : Fragment(), ServiceListener {


    private var btnContinueEnable: Boolean = false
    lateinit var mydialog: Dialog_loading


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

    lateinit var setupStepModel: SetupStepModel

    var spaceStylelist: ArrayList<SpacesStyleList> = ArrayList<SpacesStyleList>()

    var resultTemp: List<String> = ArrayList<String>()
    var result: ArrayList<String> = ArrayList<String>()

    lateinit var myAdapter: GenericAdapter<Any>

    lateinit var setUpDetailsList: SetupSetListModel

    lateinit var rvSpaceStyle: RecyclerView
    lateinit var btnContinue: Button

    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.space_style_layout,
                container, false)

        AppController.getAppComponent().inject(this)

        mContext = container!!.context



        rvSpaceStyle = view!!.findViewById<RecyclerView>(R.id.rv_style)

        rvSpaceStyle.layoutManager = LinearLayoutManager(mContext)

        btnContinue = view.findViewById<Button>(R.id.btn_continue)

        btnContinue.setOnClickListener {
            val spaceStyle = TextUtils.join(",", result)
            setupStepModel.spaceStyle = spaceStyle
            setUpDetailsList.spaceStyles = spaceStylelist

            if(commonMethods.isOnline(mActivity)) {
                if (!mydialog.isShowing) mydialog.show()
                apiService.updateSpace(updateSpaceStyleParams(spaceStyle)).enqueue(RequestCallback(this))
            }else{
                commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
            }
        }

        initListner()

        init()

        myAdapter = object : GenericAdapter<Any>(spaceStylelist) {
            override fun onItemClick(pos: Int, id: String, name: String) {

                if (result.contains(id))
                    result.remove(id)
                else
                    result.add(id)
                /*for (x in 0 until result.size)
                    println("Space style list : "+result.get(x))*/

                updateMainList()

            }

            override fun getLayoutId(position: Int, obj: Any): Int {

                return R.layout.space_style_list
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view, viewType, this)
            }
        }

        rvSpaceStyle.adapter = myAdapter

        return view
    }




    private fun updateSpaceStyleParams(spaceStyle: String): HashMap<String, String>? {
        println("Space style : "+spaceStyle)
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "setup"
        updateSpace["space_style"] = spaceStyle
        return updateSpace
    }




    private fun initListner() {

        AppController.getAppComponent().inject(this)

        if (listner == null) return
        res = if (listner.getRes() != null) listner.getRes() else activity!!.resources
        mActivity = if (listner.getInstance() != null) listner.getInstance() else (activity as SetupActivity?)!!

        mActivity.nsv_setup.scrollTo(0, 0)
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
        navController.navigate(R.id.action_spaceFragment_to_specialFeaturesFragment)
        mActivity.progressBarUpdate(20, 45)


    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {
        if (mydialog.isShowing)
            mydialog.dismiss()
    }

    private fun init() {

        setupStepModel = mActivity.getSetupDetailsModel()
        setUpDetailsList = mActivity.getSetupDetailsList()
        spaceStylelist = ArrayList<SpacesStyleList>()
        spaceStylelist.addAll(setUpDetailsList.spaceStyles)


        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)


        var spaceStyle: String? = setupStepModel.spaceStyle

        if (spaceStyle!=null) {
            result.clear()

            resultTemp = spaceStyle.split(",").map { it.trim() }

            result.addAll(resultTemp)

            updateMainList()
        }else{
            result.clear()
            for (x in 0 until spaceStylelist.size){
                if(spaceStylelist[x].isSelected){
                    resultTemp = spaceStylelist[x].id.split(",").map { it.trim() }
                    result.addAll(resultTemp)
                }
            }
            //btnContinue.isEnabled = result.size>0
        }
    }

    private fun updateMainList() {
        for (y in 0 until spaceStylelist.size) {

            for (x in 0 until result.size) {
                if (result[x] == spaceStylelist[y].id) {
                    spaceStylelist[y].isSelected = true
                    btnContinueEnable = true
                    break
                } else {
                    spaceStylelist[y].isSelected = false
                }
            }

        }

        if (result.isEmpty()){
            for (z in 0 until spaceStylelist.size) {
                spaceStylelist[z].isSelected = false
            }
        }

        //btnContinue.isEnabled = result.isNotEmpty()
    }
    /*private fun updateMainList() {
        for (y in 0 until spaceStylelist.size) {

            for (x in 0 until result.size) {


                if (result.get(x).equals(spaceStylelist.get(y).id)) {
                    spaceStylelist.get(y).isSelected = true
                    btnContinueEnable = true
                    break
                } else {
                    spaceStylelist.get(y).isSelected = false
                }

            }

        }
        if (result.isEmpty()){
            for (z in 0 until spaceStylelist.size) {
                spaceStylelist.get(z).isSelected = false
            }
        }

        //btnContinue.isEnabled = result.isNotEmpty()

        //buttonEnableDisable()

    }*/

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