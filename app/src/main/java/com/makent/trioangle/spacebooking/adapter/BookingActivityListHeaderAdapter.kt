package com.makent.trioangle.spacebooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.spacebooking.model.BookingActivitiesTypeItems
import kotlinx.android.synthetic.main.row_activity_item.view.*


class BookingActivityListHeaderAdapter(val activityTypesItemList: List<BookingActivitiesTypeItems>, val context: Context) : RecyclerView.Adapter<HeaderViewHolder>() {

    var oldPosition: Int = checkSelectedEvent(activityTypesItemList)
    private lateinit var clickListener: ClickListener
    override fun getItemCount(): Int {
        return activityTypesItemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        var activityTypesItem: BookingActivitiesTypeItems = activityTypesItemList.get(position)
        holder.tvItems.text = activityTypesItem.name
        if (oldPosition==position)
            holder.cvActivityHeader.setCardBackgroundColor(context.resources.getColor(R.color.colorAccent))
        else
            holder.cvActivityHeader.setCardBackgroundColor(context.resources.getColor(R.color.line_gray))

        holder.cvActivityHeader.setOnClickListener {
            oldPosition=position
            if (clickListener != null) {
                clickListener.onHeaderClick(holder.itemView, position, oldPosition)
            }
        }
    }

    fun checkSelectedEvent(activityTypesItemList: List<BookingActivitiesTypeItems>):Int{
        for (i in 0 until activityTypesItemList.size){
            if(activityTypesItemList[i].isSelected){
                return i
            }
        }
        return 0
    }

    fun setClickListener(clickListeners: ClickListener) {
        clickListener = clickListeners
    }

    interface ClickListener {
        fun onHeaderClick(v: View, position: Int, oldPostion: Int)
    }
}

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvItems = view.tv_items
    val cvActivityHeader = view.cvActivityHeader
}

