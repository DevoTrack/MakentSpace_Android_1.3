package com.makent.trioangle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.model.PriceBreakDownList

class PriceBreakDownListAdapter(private val priceBreakDownList: List<PriceBreakDownList>, var context: Context) : RecyclerView.Adapter<PriceBreakDownListAdapter.MyViewHolder>() {


    var time: String? = null
    protected var isInternetAvailable: Boolean = false
    internal var localSharedPreferences: LocalSharedPreferences = LocalSharedPreferences(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.price_break_down_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PriceBreakDownListAdapter.MyViewHolder, position: Int) {

        holder.tv_price_txt.setText(priceBreakDownList.get(position).key)
        holder.tv_price.setText(priceBreakDownList.get(position).value)
    }

    override fun getItemCount(): Int {
        return priceBreakDownList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_price_txt: TextView = view.findViewById(R.id.tv_price_txt)
        var tv_price: TextView = view.findViewById(R.id.tv_price)
    }

}
