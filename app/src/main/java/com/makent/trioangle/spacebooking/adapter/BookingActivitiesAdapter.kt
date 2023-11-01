package com.makent.trioangle.spacebooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.spacebooking.model.BookingActivitiesItem
import kotlinx.android.synthetic.main.row_activities_item.view.*

class BookingActivitiesAdapter(val activitiesItemList: List<BookingActivitiesItem?>?, val context: Context) : RecyclerView.Adapter<ActivitiesViewHolder>(), BookingSubActivityListAdapter.SubActivitiesClickListener {

    private lateinit var clickListener: ActivitiesClickListener
    override fun getItemCount(): Int {
        if (activitiesItemList != null) {
            return activitiesItemList.size
        } else {
            return 0
        }
    }

    var activitiesPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        return ActivitiesViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activities_item, parent, false))
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        var activitiesItem: BookingActivitiesItem = activitiesItemList?.get(position)!!
        holder.tvActivities.text = activitiesItem.name

        holder.cbActivities.isChecked = activitiesItem.isSelected

        if (activitiesItem.subActivities?.size!! > 0) {
            holder.rvSubActivity.visibility = View.VISIBLE
            holder.rltActivities.isEnabled = false
            holder.cbActivities.visibility = View.GONE
            holder.rvSubActivity.layoutManager = LinearLayoutManager(context)
            var adapters = BookingSubActivityListAdapter(activitiesItem.subActivities, context)
            adapters.setClickListener(this)
            holder.rvSubActivity.adapter = adapters
        } else {
            holder.cbActivities.visibility = View.VISIBLE
            holder.rltActivities.isEnabled = true
            holder.rvSubActivity.visibility = View.GONE
        }
        holder.rltActivities.setOnClickListener {
            println("holder.rltActivities ${holder.rltActivities.isClickable}")
            activitiesPosition = position
            holder.cbActivities.isChecked = !holder.cbActivities.isChecked
            clickListener.onActivitiesClick(holder.itemView, activitiesPosition)
        }
    }

    fun setClickListener(clickListeners: ActivitiesClickListener) {
        clickListener = clickListeners
    }

    interface ActivitiesClickListener {
        fun onActivitiesClick(v: View, position: Int)
        fun onSubActivitiesClick(v: View, activitiesPosition: Int, position: Int)
    }

    override fun onSubActivityClick(v: View, position: Int, id: String) {
        clickListener.onSubActivitiesClick(v, activitiesPosition, position)
    }
}

class ActivitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvActivities = view.tvActivities
    val cbActivities = view.cbActivities
    val rltActivities = view.rltActivities
    val rvSubActivity = view.rvSubActivity
}