package com.makent.trioangle.createspace.ReadyHostAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.ReadyHostModel.ActivityTypesItem
import kotlinx.android.synthetic.main.row_activity_item.view.*


class ActivityListHeaderAdapter(val activityTypesItemList :  List<ActivityTypesItem>, val context: Context) : RecyclerView.Adapter<HeaderViewHolder>() {

    var oldPosition : Int= 0
    private lateinit var clickListener: ClickListener
    override fun getItemCount(): Int {
        return activityTypesItemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.row_activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        var activityTypesItem : ActivityTypesItem
        activityTypesItem=activityTypesItemList.get(position)
        holder.tvItems.text  = activityTypesItem.name

        if(oldPosition==position)
            holder.cvActivityHeader.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent))
        else
            holder.cvActivityHeader.setCardBackgroundColor(context.getResources().getColor(R.color.background))

        holder.cvActivityHeader.setOnClickListener {
            oldPosition=position
            if (clickListener != null) {
                clickListener.onHeaderClick(holder.itemView, position,oldPosition);
            }
        }
    }

    fun setClickListener(clickListeners: ClickListener) {
        clickListener = clickListeners
    }

    interface ClickListener {
        fun onHeaderClick(v: View, position: Int,oldPostion : Int)
    }
}

class HeaderViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvItems = view.tv_items
    val cvActivityHeader=view.cvActivityHeader
}

