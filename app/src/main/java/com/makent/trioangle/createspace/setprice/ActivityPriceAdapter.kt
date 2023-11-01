package com.makent.trioangle.createspace.setprice

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makent.trioangle.R
import com.makent.trioangle.controller.AppController
import com.makent.trioangle.createspace.ReadyHostModel.ActivityTypesItem
import com.makent.trioangle.createspace.setprice.model.ActivityPrice
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.util.CommonMethods
import kotlinx.android.synthetic.main.price_layout.view.*
import kotlinx.android.synthetic.main.row_activity_item.view.*
import kotlinx.android.synthetic.main.row_activity_item.view.cvActivityHeader
import kotlinx.android.synthetic.main.row_set_price.view.*
import javax.inject.Inject

class ActivityPriceAdapter(val activityPrice :  List<ActivityPrice>, val context: Context,val spaceCurrencySymbol:String) : RecyclerView.Adapter<HeaderViewHolder>() {
    var minHours : Int= 4

    @Inject
    lateinit var commonMethods: CommonMethods
    private lateinit var clickListener: ClickListener
    override fun getItemCount(): Int {
        return activityPrice.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        AppController.getAppComponent().inject(this)

        return HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.row_set_price, parent, false))
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        var activityPriceItem =activityPrice.get(position)
        holder.tvActivity.text  = activityPriceItem.activityName
        Glide.with(context).load(activityPriceItem.imageUrl).into(holder.ivActivity)

        holder.tvDayCurrency.text=spaceCurrencySymbol
        holder.tvHourCurrency.text=spaceCurrencySymbol
        holder.tvWeeklyCurrency.text=spaceCurrencySymbol
        holder.tvMonthlyCurrency.text=spaceCurrencySymbol

        if(activityPriceItem.fullDay>0)
            holder.etDayAmount.setText(activityPriceItem.fullDay.toString())
        if(activityPriceItem.weekly>0)
            holder.etWeeklyAmount.setText(activityPriceItem.weekly.toString())
        if(activityPriceItem.monthly>0)
            holder.etMonthlyAmount.setText(activityPriceItem.monthly.toString())
        if(activityPriceItem.hourly>0)
            holder.etHourAmount.setText(activityPriceItem.hourly.toString())
        if(activityPriceItem.minHours>0)
            holder.etMinHour.setText(activityPriceItem.minHours.toString())

        holder.etDayAmount.addTextChangedListener(EditTextWatcher(holder.etDayAmount,position,holder.itemView,Constants.Day))
        holder.etHourAmount.addTextChangedListener(EditTextWatcher(holder.etHourAmount,position,holder.itemView,Constants.Hour))
        holder.etMinHour.addTextChangedListener(EditTextWatcher(holder.etMinHour,position,holder.itemView,Constants.MinHr))
        holder.etWeeklyAmount.addTextChangedListener(EditTextWatcher(holder.etWeeklyAmount,position,holder.itemView,Constants.WeekHr))
        holder.etMonthlyAmount.addTextChangedListener(EditTextWatcher(holder.etMonthlyAmount,position,holder.itemView,Constants.MonHr))

        holder.ivMinus.setOnClickListener {
            var minHour: Int =holder.etMinHour.text.toString().toInt()
            if(minHour>1)
            {
                minHour--
                holder.etMinHour.setText(minHour.toString())
            }
            else
            {
                commonMethods.snackBar(context.getResources().getString(R.string.validate_minhour)+" 1-"+minHours, "", false, 2, holder.ivPlus, context.getResources(), context as Activity?)
            }
        }
        holder.ivPlus.setOnClickListener {
            var maxHour: Int =holder.etMinHour.text.toString().toInt()
            if(maxHour<minHours) {
                maxHour++
                holder.etMinHour.setText(maxHour.toString())
            }else{
                commonMethods.snackBar(context.getResources().getString(R.string.validate_minhour)+" 1-"+minHours, "", false, 2, holder.ivPlus, context.getResources(), context as Activity?)
            }
        }

    }

    fun setClickListener(clickListeners: ClickListener) {
        clickListener = clickListeners
    }

    interface ClickListener {
        fun onHourlyRateChanged(v: View, position: Int,value : String,type : String)
        fun onWeeklyRateChanged(v: View, position: Int,value : String,type : String)
        fun onMonthlyRateChange(v: View, position: Int,value : String,type : String)
        fun onMinHrChanged(v: View, position: Int,value : String,type:String)
        fun onDayRateChanged(v: View, position: Int,value : String,type:String)
    }

    // Validate email field
    inner class EditTextWatcher(var edt: EditText,var position: Int,val view:View, val type:String) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            when (edt.id) {
                R.id.etDayAmount -> {
                    clickListener.onDayRateChanged(view,position, edt.text.toString(),type)
                }R.id.etHourAmount -> {
                clickListener.onHourlyRateChanged(view,position,edt.text.toString(),type)
            }R.id.etWeeklyAmount -> {
                clickListener.onWeeklyRateChanged(view,position,edt.text.toString(),type)
            }R.id.etMonthlyAmount -> {
                clickListener.onMonthlyRateChange(view,position,edt.text.toString(),type)
            }R.id.etMinHour -> {
                //edt.text.trimStart('0')

                if(!TextUtils.isEmpty(edt.text.toString())&&edt.text.toString().toInt()>minHours) {
                    edt.setText(minHours.toString())
                    commonMethods.snackBar(context.getResources().getString(R.string.validate_minhour)+" 1-"+minHours, "", false, 2,edt, context.getResources(), context as Activity?)
                }else
                    clickListener.onMinHrChanged(view,position,edt.text.toString(),type)
            }
            }
        }
    }
}

class HeaderViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvActivity = view.tvActivities
    val ivActivity = view.ivActivities
    val cvActivityHeader=view.cvActivityHeader

    val tvHourCurrency = view.tvHourCurrency
    val tvWeeklyCurrency = view.tvWeeklyCurrency
    val tvMonthlyCurrency = view.tvMonthlyCurrency
    val etHourAmount = view.etHourAmount

    val tvDayCurrency = view.tvDayCurrency
    val etDayAmount = view.etDayAmount
    val etWeeklyAmount = view.etWeeklyAmount
    val etMonthlyAmount = view.etMonthlyAmount

    val etMinHour= view.etMinHour
    val ivMinus =view.ivMinus
    val ivPlus= view.ivPlus
}

