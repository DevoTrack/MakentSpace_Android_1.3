package com.makent.trioangle.createspace

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.controller.Dialog_loading
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.interfaces.BasicStepsActivityInterface
import com.makent.trioangle.createspace.model.hostlisting.basics.BasicStepsModel
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.FontIconDrawable
import com.makent.trioangle.interfaces.ApiService
import com.makent.trioangle.interfaces.ServiceListener
import com.makent.trioangle.model.JsonResponse
import com.makent.trioangle.util.CommonMethods
import com.makent.trioangle.util.RequestCallback
import kotlinx.android.synthetic.main.activity_setup.*
import javax.inject.Inject

class GuestNumberFragment : Fragment() ,ServiceListener{

    @Inject
    lateinit var commonMethods: CommonMethods

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var gson: Gson

    lateinit var font1: Typeface

    lateinit var minusenable: Drawable

    lateinit var minusdisable:Drawable

    lateinit var plusenable:Drawable

    lateinit var plusdisable:Drawable

    lateinit var res: Resources

    lateinit var listner: BasicStepsActivityInterface

    lateinit var mActivity: BasicStepsActivity

    lateinit var ivGuestMinus: ImageView

    lateinit var ivGuestPlus: ImageView

    lateinit var edtGuestCount: EditText

    val maxGuestCount : Int = 100000

    lateinit var btnContinue: Button
    lateinit var tvMaxGuestText: TextView
    var guestCount = 0


    lateinit var mContext: Context

    var basicStepModel: BasicStepModel?=null

    lateinit var localSharedPreferences: LocalSharedPreferences
    lateinit var mydialog: Dialog_loading

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.guest_number_layout,
                container, false)

        mContext = container!!.context

        ivGuestMinus = view!!.findViewById<ImageView>(R.id.iv_guest_minus)
        ivGuestPlus = view.findViewById<ImageView>(R.id.iv_guest_plus)
        edtGuestCount = view.findViewById<EditText>(R.id.edt_guest_count)
        btnContinue = view.findViewById<Button>(R.id.btn_continue)
        tvMaxGuestText = view.findViewById<Button>(R.id.tv_guest_num_txt)

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

        font1 = Typeface.createFromAsset(res.assets, resources.getString(R.string.fonts_makent4))
        minusenable = FontIconDrawable(context, resources.getString(R.string.f4checkminus), font1)
                .sizeDp(30).colorRes(R.color.guestButton)
        plusenable = FontIconDrawable(context, resources.getString(R.string.f4checkplus), font1)
                .sizeDp(30).colorRes(R.color.guestButton)
        minusdisable = FontIconDrawable(context, resources.getString(R.string.f4checkminus), font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable)
        plusdisable = FontIconDrawable(context, resources.getString(R.string.f4checkplus), font1)
                .sizeDp(30)
                .colorRes(R.color.guestButtonDisable)

        mydialog = Dialog_loading(mContext)
        mydialog.setCancelable(false)
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        localSharedPreferences = LocalSharedPreferences(mContext)

    }

    private fun init() {




                //edtGuestCount.filters(arrayOf<InputFilter>(InputFilterMinMax(0, 100000)))

        edtGuestCount.setFilters(arrayOf<InputFilter>(InputFilterMinMax(0, maxGuestCount)))

        tvMaxGuestText.text=commonMethods.changeColorForStar(res.getString(R.string.maximum_number_of_guests))

        ivGuestPlus.setOnClickListener {
            val myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce)
            ivGuestPlus.startAnimation(myAnim)

            if (edtGuestCount.getText().toString() == "")
                guestCount = 0
            else
                guestCount = Integer.parseInt(edtGuestCount.getText().toString())


            guestCount++
            enableDisabeButton(ivGuestPlus, ivGuestMinus, guestCount, edtGuestCount)
        }

        ivGuestMinus.setOnClickListener {

            val myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce)
            ivGuestMinus.startAnimation(myAnim)

            if (edtGuestCount.getText().toString() == "")
                guestCount = 0
            else
                guestCount = Integer.parseInt(edtGuestCount.getText().toString())


            guestCount--
            enableDisabeButton(ivGuestPlus, ivGuestMinus, guestCount, edtGuestCount)

        }
        basicStepModel=mActivity.getBasicStepModelData()

        btnContinue.setOnClickListener {
            if(basicStepModel!=null){
                if(commonMethods.isOnline(mActivity)) {
                    mActivity.basicStepModelData.numberOfGuests=checkGuestCount().toString()
                    if (!mydialog.isShowing)
                        mydialog.show()
                    apiService.updateSpace(updateGuestNumber()).enqueue(RequestCallback(this))
                }else{
                    commonMethods.snackBar(mActivity.resources.getString(R.string.network_failure),"",false,2,btnContinue,mActivity.resources,mActivity)
                }
            }else {
                mActivity.putHashMap("number_of_guests", checkGuestCount().toString())
                val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
                navController.navigate(R.id.action_guestNumberFragment_to_amenitiesFragment)
                mActivity.progressBarUpdate(30, 45)
            }
        }



        if(basicStepModel!=null){
            setDatas()
        }else{
            setDatasOnCreate();

        }

        enableDisabeButton(ivGuestPlus,ivGuestMinus,guestCount,edtGuestCount)
        edtGuestCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isEmpty() || 0 == Integer.valueOf(p0.toString())) {
                    btnContinue.isEnabled = false
                    ivGuestMinus.isEnabled=false
                    ivGuestMinus.background = minusdisable
                } else {
                    btnContinue.isEnabled = true
                    ivGuestMinus.isEnabled=true
                    ivGuestMinus.background = minusenable
                }





                if(!p0.toString().isEmpty()){
                    if (maxGuestCount > Integer.valueOf(p0.toString()) ) {
                        ivGuestPlus.background = plusenable
                        ivGuestPlus.isEnabled=true
                    }else{
                        ivGuestPlus.background = plusdisable
                        ivGuestPlus.isEnabled=false
                    }
                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    private fun setDatasOnCreate() {
        edtGuestCount.setText(mActivity.hashMap.get("number_of_guests"))
        guestCount= mActivity.hashMap.get("number_of_guests")?.toInt()?: 0

        if(guestCount>0) btnContinue.isEnabled=true

    }

    private fun updateGuestNumber(): HashMap<String, String> {
        val updateGuestNumber = HashMap<String,String>()
        updateGuestNumber["token"] = localSharedPreferences.getSharedPreferences(Constants.AccessToken)
        updateGuestNumber["space_id"] = localSharedPreferences.getSharedPreferences(Constants.mSpaceId)
        updateGuestNumber["step"] = "basics"
        updateGuestNumber["number_of_guests"] = checkGuestCount().toString()
        return updateGuestNumber
    }

    private fun checkGuestCount():Int{
        guestCount = if(edtGuestCount.text.toString().isEmpty()){
            0
        }else{
            edtGuestCount.text.toString().toInt()
        }
        return guestCount
    }

    private fun setDatas(){
        edtGuestCount.setText(basicStepModel!!.numberOfGuests)
        guestCount=basicStepModel!!.numberOfGuests.toInt()
        btnContinue.isEnabled=true
        ivGuestMinus.isEnabled=true
        ivGuestMinus.background=minusenable

    }

    override fun onSuccess(jsonResp: JsonResponse?, data: String?) {
        if (jsonResp!!.isSuccess){
            if(mydialog.isShowing) mydialog.dismiss()
            val navController = Navigation.findNavController(activity!!, R.id.basic_nav_host_fragment)
            navController.navigate(R.id.action_guestNumberFragment_to_amenitiesFragment)
            mActivity.progressBarUpdate(30, 45)
        }else if (!TextUtils.isEmpty(jsonResp.statusMsg)) {
            commonMethods.snackBar(jsonResp.statusMsg,"",false,2,btnContinue,mActivity.resources,mActivity)
        }
    }

    override fun onFailure(jsonResp: JsonResponse?, data: String?) {

    }

    private fun enableDisabeButton(plus: ImageView, minus: ImageView, count: Int, edtView: EditText) {

        minus.isEnabled = count != 0

        plus.isEnabled = count < maxGuestCount

        //edtView.setText(Integer.toString(count))
        edtView.setText(String.format("%d",count))
        edtView.setSelection(edtView.getText().toString().length)

        plusMinus(minus, plus)

    }

    private fun plusMinus(minus : ImageView,plus:ImageView){

        if(minus.isEnabled){
            minus.setBackground(minusenable)
        }else{
            minus.setBackground(minusdisable)
        }


        if(plus.isEnabled){
            plus.setBackground(plusenable)
        }else{
            plus.setBackground(plusdisable)
        }

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


    class MinMaxFilter(minVal : Int, maxValue : Int) : InputFilter{

        val min : Int = minVal
        val max  : Int = maxValue

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            var input = Integer.parseInt(dest.toString() + source.toString())


                if(isInRange(min,max,input))
                    return null

            return ""


        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if(b > a) {
                c >= a && c <= b
            }else{
                c >= b && c <= a
            }
        }


    }

    class InputFilterMinMax(min:Int, max:Int): InputFilter {
        private var min:Int = 0
        private var max:Int = 0

        init{
            this.min = min
            this.max = max
        }

        override fun filter(source:CharSequence, start:Int, end:Int, dest: Spanned, dstart:Int, dend:Int): CharSequence? {
            try
            {
                val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length)).toInt()
                if (isInRange(min, max, input))
                    return null
            }
            catch (nfe:NumberFormatException) {}
            return ""
        }

        private fun isInRange(a:Int, b:Int, c:Int):Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }


}