package com.makent.trioangle.spacebooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.spacebooking.model.GetStartEndTimes
import kotlinx.android.synthetic.main.bottom_sheet_time.view.*

class ChooseTimeAdapter(val context: Context, val timings: ArrayList<GetStartEndTimes>, val TimeType: String, val clickListener: onTimeClickListener) : RecyclerView.Adapter<ChooseTimeAdapter.ViewHolder>() {


    interface onTimeClickListener {
        fun onTimeClicked(type:String,selectedTime:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bottom_sheet_time, parent, false))
    }

    override fun getItemCount(): Int {
        return timings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTime.text = timings[position].timeValue
        /**Below Code is hided for removing blocked times selection restriction*/
        //holder.tvTime.isEnabled=!timings[position].isBlocked
        println("timings[position].isSelected ${timings[position].isSelected}")
        if (timings[position].isSelected){
            holder.tvTime.background = ContextCompat.getDrawable(context, R.drawable.time_enable_green)
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_light_blue))
        }else{
            holder.tvTime.background = ContextCompat.getDrawable(context, R.drawable.time_enable)
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_shadow))
        }
        /**Below Code is hided for removing blocked times selection restriction*/
       /* if(timings[position].isBlocked){
            holder.tvTime.background = ContextCompat.getDrawable(context, R.drawable.time_enable)
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_light_gray))
        }*/

        holder.tvTime.setOnClickListener {
            for (i in 0 until  timings.size){
                if (timings[i].isSelected){
                    timings[i].isSelected=false
                    notifyDataSetChanged()
                    break
                }
            }

            timings[position].isSelected=true
            holder.tvTime.background = ContextCompat.getDrawable(context, R.drawable.time_enable_green)
            holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_light_blue))
            clickListener.onTimeClicked(TimeType, timings[position].timeValue)
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime = view.tvTime
    }
}