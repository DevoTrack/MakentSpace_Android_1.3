package com.makent.trioangle.createsp

import com.makent.trioangle.createspace.ReadyHostAdapter.SubActivityListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.ReadyHostModel.ActivitiesItem
import kotlinx.android.synthetic.main.row_activities_item.view.*

class ActivityListAdapter(val activitiesItemList: List<ActivitiesItem?>?, val context: Context) : RecyclerView.Adapter<ActivitiesViewHolder>(),SubActivityListAdapter.SubActivitiesClickListener {

    private lateinit var clickListener: ActivitiesClickListener
    override fun getItemCount(): Int {
        if (activitiesItemList != null) {
            return activitiesItemList.size
        }else{
            return 0
        }
    }

    var activitiesPosition : Int =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        return ActivitiesViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activities_item, parent, false))
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        var activitiesItem : ActivitiesItem
        activitiesItem= activitiesItemList?.get(position)!!
        holder.tvActivities.text  = activitiesItem.name

        holder.cbActivities.isChecked = activitiesItem.isSelected!!

        if(activitiesItem.subActivities?.size!! >0) {
            holder.rvSubActivity.visibility = View.VISIBLE

            holder.rvSubActivity.layoutManager = LinearLayoutManager(context)
            var adapters = SubActivityListAdapter(activitiesItem.subActivities, context,position)
            adapters.setClickListener(this)
            holder.rvSubActivity.adapter = adapters
        }else{
            holder.rvSubActivity.visibility = View.GONE
        }
        holder.rltActivities.setOnClickListener {
            if (clickListener != null) {
                activitiesPosition=position
                holder.cbActivities.isChecked= !holder.cbActivities.isChecked
                clickListener.onActivitiesClick(holder.itemView, activitiesPosition);
            }
        }
    }

    fun setClickListener(clickListeners: ActivitiesClickListener) {
        clickListener = clickListeners
    }

    interface ActivitiesClickListener {
        fun onActivitiesClick(v: View, position: Int)
        fun onSubActivitiesClick(v : View,activitiesPosition : Int, position : Int)
    }

    override fun onSubActivityClick(v: View, position: Int,headerPosition : Int) {
        activitiesPosition = headerPosition
        clickListener.onSubActivitiesClick(v,activitiesPosition, position)
    }
}

class ActivitiesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvActivities = view.tvActivities
    val cbActivities = view.cbActivities
    val rltActivities = view.rltActivities
    val rvSubActivity = view.rvSubActivity
}

