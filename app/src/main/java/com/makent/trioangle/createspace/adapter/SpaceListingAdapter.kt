package com.makent.trioangle.createspace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.makent.trioangle.R
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.createspace.ProgressBarAnimation
import com.makent.trioangle.createspace.adapter.SpaceListingAdapter.MyViewHolder
import com.makent.trioangle.createspace.model.SpaceListingDetailsList

class SpaceListingAdapter(private val spaceListingDetails: List<SpaceListingDetailsList>, var context: Context, private val onItemClickListener: SpaceListingAdapter.OnItemClickListener) : RecyclerView.Adapter<MyViewHolder>() {
    var time: String? = null
    protected var isInternetAvailable: Boolean = false
    internal var localSharedPreferences: LocalSharedPreferences = LocalSharedPreferences(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.space_listing_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val spaceModelList = spaceListingDetails[position]

        holder.rltListingParent.setOnClickListener {
            onItemClickListener.onItemClick(position,spaceModelList.space_id)
        }
        val imageViewTarget1 = DrawableImageViewTarget(holder.ivSpaceImage)
        Glide.with(context).load(spaceModelList.photo_name).into(imageViewTarget1)

        holder.tvListingName.text = spaceModelList.name

        val anim = ProgressBarAnimation(holder.pb, 0F, spaceModelList.completed.toFloat())
        anim.duration = 1000
        holder.pb.startAnimation(anim)
        val remainingPercentage=spaceModelList.completed+"%"
        holder.tvProgress.text = context.resources.getString(R.string.step_completed,remainingPercentage)
        if (!spaceModelList.remainingSteps.equals("0",ignoreCase = false)){
            if (spaceModelList.remainingSteps.equals("1")){
                holder.tvSpaceRemainingsteps.text = context.resources.getString(R.string.space_remaining_step, spaceModelList.remainingSteps)
            }else {
                holder.tvSpaceRemainingsteps.text = context.resources.getString(R.string.space_remaining_steps, spaceModelList.remainingSteps)
            }
            holder.tvFinishListing.text=context.resources.getString(R.string.finish_your_listing)
            holder.tvSpaceRemainingsteps.visibility=View.VISIBLE
            holder.tvFinishListing.visibility=View.VISIBLE
        }else{
            holder.tvSpaceRemainingsteps.visibility=View.GONE
            holder.tvFinishListing.visibility=View.GONE
        }

    }


    override fun getItemCount(): Int {
        return spaceListingDetails.size
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int,spaceId : String)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivSpaceImage: ImageView = view.findViewById(R.id.iv_space_image)
        var tvListingName: TextView = view.findViewById(R.id.tv_listing_name)
        var tvFinishListing: TextView = view.findViewById(R.id.tv_finish_listing)
        var tvSpaceRemainingsteps: TextView = view.findViewById(R.id.tv_spaceRemainingsteps)
        var tvProgress: TextView = view.findViewById(R.id.tv_progress)
        var rltListingParent: RelativeLayout = view.findViewById(R.id.rlt_listing_parent)
        var pb: ProgressBar = view.findViewById(R.id.pb)

    }

}
