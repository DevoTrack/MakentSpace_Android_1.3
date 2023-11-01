package com.makent.trioangle.createspace

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.interfaces.OnItemClick
import kotlinx.android.synthetic.main.availability_list_layout.view.*


class AvailabilityAdapter(val items: ArrayList<AvailabiltyModel>, val context: Context, val listItemClickListner: OnItemClick) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.availability_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvWeek.text = items.get(position).day
        holder.tvSunClose.text = items.get(position).status

        holder.rltAvailable.setOnClickListener {
            listItemClickListner.onItemClick(position)
        }


    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvWeek = view.tv_week
    val tvSunClose = view.tv_sun_close
    val rltAvailable = view.rlt_available


}