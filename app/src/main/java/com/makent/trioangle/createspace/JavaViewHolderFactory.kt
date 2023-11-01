package com.makent.trioangle.createspace.genericadapter

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makent.trioangle.R
import com.makent.trioangle.createspace.*
import com.makent.trioangle.createspace.interfaces.listItemClickListner
import com.makent.trioangle.createspace.model.hostlisting.basics.SpaceTypeList

/**
 * Created by mohang on 12/12/17.
 */

object JavaViewHolderFactory {

    fun create(view: View, viewType: Int, listener: Any? = null): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.guest_access_list_item -> GuestAccessViewHolder(view,listener)
            R.layout.service_list_item -> ServiceViewHolder(view,listener)
            R.layout.amenities_list_item -> AmenitiesViewHolder(view,listener)
            R.layout.space_style_list -> SpaceStyleViewHolder(view, listener)
            R.layout.special_feature_list -> SpecialFeatureViewHolder(view, listener)
            R.layout.space_rule_list -> SpaceRuleViewHolder(view, listener)
            R.layout.list_items -> SpaceTypeViewHolder(view)
            else -> {
                GuestAccessViewHolder(view,listener)
            }
        }
    }

    class GuestAccessViewHolder(itemView:View,listener: Any ?= null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<GuestAccessModel> {

        var textView: TextView = itemView.findViewById(R.id.tv_items)
        var lltGuestAccess: LinearLayout = itemView.findViewById(R.id.llt_guest_access)
        var cbGuestAccessItem: CheckBox = itemView.findViewById(R.id.cb_guest_access_item)
        var listner: listItemClickListner = (listener as listItemClickListner?)!!

        override fun bind(data: List<GuestAccessModel>, position: Int) {
            textView.text = data.get(position).name
            cbGuestAccessItem.isChecked = data.get(position).isSelected

            lltGuestAccess.setOnClickListener {
                cbGuestAccessItem.isChecked = !cbGuestAccessItem.isChecked


            }
            cbGuestAccessItem.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }
        }
    }

    class ServiceViewHolder(itemView: View,listener: Any ?= null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<ServiceModel> {

        var textView: TextView = itemView.findViewById(R.id.tv_items)
        var lltService: LinearLayout = itemView.findViewById(R.id.llt_service)
        var cbServiceItem: CheckBox = itemView.findViewById(R.id.cb_service_item)
        var listner: listItemClickListner = (listener as listItemClickListner?)!!

        override fun bind(data: List<ServiceModel>, position: Int) {
            textView.text = data.get(position).name
            cbServiceItem.isChecked = data.get(position).isSelected


            lltService.setOnClickListener {
                cbServiceItem.isChecked = !cbServiceItem.isChecked

            }
            cbServiceItem.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }

        }
    }


    class AmenitiesViewHolder(itemView: View,listener: Any?=null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<AmenitiesModel> {
        var textView: TextView = itemView.findViewById(R.id.tv_items)
        var lltAmenities: LinearLayout = itemView.findViewById(R.id.llt_amenities)
        var cbAmenities: CheckBox = itemView.findViewById(R.id.cb_amenities_item)
        var listner: listItemClickListner = (listener as listItemClickListner?)!!

        override fun bind(data: List<AmenitiesModel>, position: Int) {
            textView.text = data.get(position).name

            cbAmenities.isChecked = data.get(position).isSelected

            lltAmenities.setOnClickListener {
                cbAmenities.isChecked = !cbAmenities.isChecked
            }

            cbAmenities.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }
        }



    }


    class SpaceStyleViewHolder(itemView: View, listener: Any? = null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<SpacesStyleList> {
        override fun bind(data: List<SpacesStyleList>, position: Int) {
            textView.text = data.get(position).name

            cbSpaceStyle.isChecked = data.get(position).isSelected

            lltSpaceStyle.setOnClickListener {
                cbSpaceStyle.isChecked = !cbSpaceStyle.isChecked
            }

            cbSpaceStyle.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }
        }

        var textView: TextView
        var lltSpaceStyle: LinearLayout
        var cbSpaceStyle: CheckBox
        var listner: listItemClickListner

        init {
            textView = itemView.findViewById(R.id.tv_items)
            lltSpaceStyle = itemView.findViewById(R.id.llt_space_style)
            cbSpaceStyle = itemView.findViewById(R.id.cb_space_style_item)
            listner = (listener as listItemClickListner?)!!

        }

    }


    class SpecialFeatureViewHolder(itemView: View, listener: Any? = null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<SpecailFeaturesList> {
        override fun bind(data: List<SpecailFeaturesList>, position: Int) {
            textView.text = data.get(position).name
            cbSpecialFeature.isChecked = data.get(position).isSelected

            lltSpecialFeature.setOnClickListener {
                cbSpecialFeature.isChecked = !cbSpecialFeature.isChecked

            }

            cbSpecialFeature.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }
        }

        var textView: TextView
        var lltSpecialFeature: LinearLayout
        var cbSpecialFeature: CheckBox
        var listner: listItemClickListner


        init {
            textView = itemView.findViewById(R.id.tv_items)
            lltSpecialFeature = itemView.findViewById(R.id.llt_special_feature)
            cbSpecialFeature = itemView.findViewById(R.id.cb_special_feature_item)
            listner = (listener as listItemClickListner?)!!

        }

    }


    class SpaceRuleViewHolder(itemView: View, listener: Any? = null) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<SpaceRulesList> {

        var textView: TextView
        var lltSpaceRule: LinearLayout
        var cbSpaceRuleItem: CheckBox
        var listner: listItemClickListner


        init {
            textView = itemView.findViewById(R.id.tv_items)
            lltSpaceRule = itemView.findViewById(R.id.llt_space_rule)
            cbSpaceRuleItem = itemView.findViewById(R.id.cb_space_rule_item)
            listner = (listener as listItemClickListner?)!!

        }

        override fun bind(data: List<SpaceRulesList>, position: Int) {
            textView.text = data.get(position).name
            cbSpaceRuleItem.isChecked = data.get(position).isSelected
            lltSpaceRule.setOnClickListener {
                cbSpaceRuleItem.isChecked = !cbSpaceRuleItem.isChecked

            }

            cbSpaceRuleItem.setOnCheckedChangeListener { buttonView, isChecked ->
                listner.onItemClick(position, data.get(position).id, data.get(position).name)
            }
        }


    }

    class SpaceTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericAdapter.Binder<SpaceTypeList> {
        override fun bind(data: List<SpaceTypeList>, position: Int) {
            textView.text = data.get(position).name

        }

        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.tv_name)
            textView.setOnClickListener {

            }
        }

    }

}