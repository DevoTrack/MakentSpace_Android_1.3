package com.makent.trioangle.createspace.ReadyHostAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.ReadyHostModel.ActivitiesItem
import com.makent.trioangle.createspace.ReadyHostModel.SubActivitiesItem
import kotlinx.android.synthetic.main.row_activities_item.view.*
import kotlinx.android.synthetic.main.row_activity_item.view.*

class SubActivityListAdapter(val activitiesItemList: List<SubActivitiesItem?>?, val context: Context,val activityPosition : Int) : RecyclerView.Adapter<SubActivitiesViewHolder>() {


    private lateinit var clickListener: SubActivitiesClickListener
    override fun getItemCount(): Int {
        if (activitiesItemList != null) {
            return activitiesItemList.size
        }else{
            return 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubActivitiesViewHolder {
        return SubActivitiesViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activities_item, parent, false))
    }

    override fun onBindViewHolder(holder: SubActivitiesViewHolder, position: Int) {
        var activitiesItem : SubActivitiesItem
        activitiesItem= activitiesItemList?.get(position)!!
        holder.tvActivities.text  = activitiesItem.name

        holder.cbActivities.isChecked = activitiesItem.isSelected!!

        holder.rltActivities.setTag(activityPosition)
        holder.rltActivities.setOnClickListener {
            if (clickListener != null) {
                holder.cbActivities.isChecked= !holder.cbActivities.isChecked
                clickListener.onSubActivityClick(holder.itemView, position, holder.rltActivities.getTag() as Int);
            }
        }
    }

    fun setClickListener(clickListeners: SubActivitiesClickListener) {
        clickListener = clickListeners
    }

    interface SubActivitiesClickListener {
        fun onSubActivityClick(v: View, position: Int,headerPosition : Int)
    }
}

class SubActivitiesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvActivities = view.tvActivities
    val cbActivities = view.cbActivities
    val rltActivities = view.rltActivities
}

