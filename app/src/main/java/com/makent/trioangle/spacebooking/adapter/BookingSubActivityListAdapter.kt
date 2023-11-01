package com.makent.trioangle.spacebooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.spacebooking.model.BookingSubActivitiesItem
import kotlinx.android.synthetic.main.row_activities_item.view.*

class BookingSubActivityListAdapter(val activitiesItemList: List<BookingSubActivitiesItem?>?, val context: Context) : RecyclerView.Adapter<SubActivitiesViewHolder>() {


    private lateinit var clickListener: SubActivitiesClickListener
    override fun getItemCount(): Int {
        if (activitiesItemList != null) {
            return activitiesItemList.size
        } else {
            return 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubActivitiesViewHolder {
        return SubActivitiesViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activities_item, parent, false))
    }

    override fun onBindViewHolder(holder: SubActivitiesViewHolder, position: Int) {
        val activitiesItem: BookingSubActivitiesItem = activitiesItemList?.get(position)!!
        holder.tvActivities.text = activitiesItem.name

        holder.cbActivities.isChecked = activitiesItem.isSelected

        holder.rltActivities.setOnClickListener {
            if (clickListener != null) {
                for (x in 0 until activitiesItemList.size){
                    if (activitiesItemList[x]!!.isSelected) activitiesItemList[x]!!.isSelected=false
                }
                notifyDataSetChanged()
                holder.cbActivities.isChecked = !holder.cbActivities.isChecked
                clickListener.onSubActivityClick(holder.itemView, position,activitiesItem.id)
            }
        }
    }

    fun setClickListener(clickListeners: SubActivitiesClickListener) {
        clickListener = clickListeners
    }

    interface SubActivitiesClickListener {
        fun onSubActivityClick(v: View, position: Int,id:String)
    }
}

class SubActivitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvActivities = view.tvActivities
    val cbActivities = view.cbActivities
    val rltActivities = view.rltActivities
}

