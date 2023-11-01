package com.makent.trioangle.createspace

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
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
import com.makent.trioangle.util.Enums
import com.makent.trioangle.util.RequestCallback
import javax.inject.Inject


class GuestAccessFragment : Fragment(), ServiceListener {


    private lateinit var myDialog: Dialog_loading


    @Inject
    lateinit var commonMethods: CommonMethods

    lateinit var localSharedPreferences: LocalSharedPreferences

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    private var resultTemp: List<String> = ArrayList()
    private var result: ArrayList<String> = ArrayList()


    private lateinit var res: Resources

    private lateinit var listener: BasicStepsActivityInterface

    private lateinit var mActivity: BasicStepsActivity

    private var basicStepsModel: BasicStepModel?=null

    private lateinit var basicStepsModelList: BasicStepsModel

    private var guestAccessList: ArrayList<GuestAccessModel> = ArrayList()


    private lateinit var rvGuestAccessSpace: RecyclerView
    private lateinit var btnContinue: Button
    private lateinit var tvTitle: TextView

    private lateinit var mContext: Context


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.guest_access_layout,
                container, false)

        mContext = container!!.context
        rvGuestAccessSpace = view!!.findViewById(R.id.rvGuestAccessSpace)
        rvGuestAccessSpace.layoutManager = LinearLayoutManager(mContext)

        btnContinue = view.findViewById(R.id.btn_continue)
        tvTitle = view.findViewById(R.id.tv_title)

        initListener()
        init()


        return view
    }


    private fun initListener() {

        AppController.getAppComponent().inject(this)

        res = if (listener.res != null) listener.res else activity!!.resources
        mActivity = if (listener.instance != null) listener.instance else (activity as BasicStepsActivity?)!!
        mActivity.nsvBasic.scrollTo(0, 0)
    }


    private fun init() {
        myDialog = Dialog_loading(mContext)
        myDialog.setCancelable(false)
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

        /**
         * All GuestAccess Form Api
         */
        basicStepsModelList = mActivity.basicStepsList
        guestAccessList = ArrayList()
        guestAccessList.addAll(basicStepsModelList.guestAccess)

        tvTitle.text=commonMethods.changeColorForStar(res.getString(R.string.guest_access_your_space))

        /**
         * Selected GuessAccess From Api as String
         */
        basicStepsModel = mActivity.basicStepModelData
        if(basicStepsModel!=null) {

            val guestAccess: String = basicStepsModel!!.guestAccess

            result.clear()

            resultTemp = guestAccess.split(",").map { it.trim() }

            result.addAll(resultTemp)

            updateMainList()

        }else{
            result.clear()
            for (x in 0 until guestAccessList.size){
                if(guestAccessList[x].isSelected){
                    resultTemp = guestAccessList[x].id.split(",").map { it.trim() }
                    result.addAll(resultTemp)
                }
            }

            btnContinue.isEnabled = result.size>0

        }
        val myAdapter = object : GenericAdapter<Any>(guestAccessList) {
            override fun onItemClick(pos: Int, id: String, name: String) {


                if (result.contains(id))
                    result.remove(id)
                else
                    result.add(id)
                println("getResults $result")
                updateMainList()
            }

            override fun getLayoutId(position: Int, obj: Any): Int {
                return R.layout.guest_access_list_item
            }

            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
                return JavaViewHolderFactory.create(view, viewType, this)
            }
        }
        rvGuestAccessSpace.adapter = myAdapter



        btnContinue.setOnClickListener {

            if(basicStepsModel!=null) {

                val guestAccess = TextUtils.join(",", result)
                basicStepsModel!!.guestAccess = guestAccess
                basicStepsModelList.guestAccess = guestAccessList
                if (commonMethods.isOnline(mActivity)) {
                    if (!myDialog.isShowing) myDialog.show()
                    apiService.updateSpace(updateGuestAccessParams(guestAccess)).enqueue(RequestCallback(Enums.REQ_EDIT_BASIC,this))
                } else {
                    commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure), "", false, 2, btnContinue, mActivity.resources, mActivity)
                }
            }else{
                if(result.isNotEmpty()) {
                    val selectedGuestAccess = TextUtils.join(",", result)
                    println("getGuestAccess $result")
                    mActivity.putHashMap("guest_access", selectedGuestAccess)
                }


                if (!myDialog.isShowing) myDialog.show()
                apiService.updateSpaceDetails("basics", localSharedPreferences.getSharedPreferences(Constants.AccessToken), mActivity.hashMap).enqueue(RequestCallback(Enums.REQ_NEW_BASIC,this))
            }

        }
    }


    private fun updateGuestAccessParams(guestAccess: String): HashMap<String, String>? {
        val updateSpace = java.util.HashMap<String, String>()
        updateSpace["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateSpace["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateSpace["step"] = "basics"
        updateSpace["guest_access"] = guestAccess

        return updateSpace
    }


    private fun updateMainList() {
        /**
         * Note : if selected guest access is inactive it will not be "guestAccessList" from api
         *        But the inactive guest access will be on "result" from api
         * For that isSelected is used
         */
        var isSelected = false
        for (y in 0 until guestAccessList.size) {

            for (x in 0 until result.size) {
                if (result[x] == guestAccessList[y].id) {
                    guestAccessList[y].isSelected = true
                    isSelected = true
                    break
                } else {
                    guestAccessList[y].isSelected = false
                }
            }

        }

        if (result.isEmpty()){
            for (z in 0 until guestAccessList.size) {
                guestAccessList[z].isSelected = false
            }
        }

        btnContinue.isEnabled = result.isNotEmpty() && isSelected
    }



    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if(jsonResp!!.isSuccess) {
            val SpaceId= commonMethods.getJsonValue(jsonResp.strResponse, "space_id", String::class.java) as String
            localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, SpaceId)
            when (jsonResp.requestCode) {
                Enums.REQ_EDIT_BASIC -> {
                    if (myDialog.isShowing) myDialog.dismiss()
                    val intent = Intent(mContext, EditListingActivity::class.java)
                    mActivity.setResult(Constants.ActivityBasic, intent)
                    mActivity.finish()
                }
                Enums.REQ_NEW_BASIC ->{
                    if(myDialog.isShowing) myDialog.dismiss()
                    val intent = Intent(mContext, EditListingActivity::class.java)
                    startActivity(intent)
                    mActivity.finish()
                }
            }
        } else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg, "", false, 2, btnContinue, mActivity.resources, mActivity)
        }
    }


    override fun onFailure(jsonResp: JsonResponse?, data: String?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is BasicStepsActivityInterface) {
            listener = context
        } else {
            throw ClassCastException(
                    context.toString())
        }
    }


}