package com.makent.trioangle.spacedetail.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.spacedetail.model.CommonSpaceDataModel
import kotlinx.android.synthetic.main.listview_item.view.item_text
import kotlinx.android.synthetic.main.spacedetailsitem.view.*


class TheSpaceAdapter(val context: Context, val thespaces: ArrayList<CommonSpaceDataModel>) : RecyclerView.Adapter<TheSpaceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheSpaceAdapter.ViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.spacedetailsitem, null, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return thespaces.size
    }

    override fun onBindViewHolder(holder: TheSpaceAdapter.ViewHolder, position: Int) {
        if (!thespaces.get(position).value.equals("0", ignoreCase = true) && !TextUtils.isEmpty(thespaces.get(position).value)) {

            val str = SpannableStringBuilder(thespaces.get(position).value)
            str.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, thespaces.get(position).value.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            //val sourceString = HtmlCompat.fromHtml("<b>${thespaces.get(position).value}</b>",HtmlCompat.FROM_HTML_MODE_COMPACT)
            holder.tvSpaceKey.text = thespaces.get(position).key + ":"
            holder.tvSpaceValues.text = thespaces.get(position).value
            holder.tvSpaceKey.visibility = View.VISIBLE
        } else {
            holder.tvSpaceKey.visibility = View.GONE
            holder.tvSpaceValues.visibility = View.GONE
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSpaceKey = view.item_text
        val tvSpaceValues = view.tv_space_value
    }
}