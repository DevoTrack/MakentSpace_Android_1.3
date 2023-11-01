package com.makent.trioangle.spacedetail.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makent.trioangle.R
import com.makent.trioangle.controller.LocalSharedPreferences
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.helper.FontIconDrawable
import com.makent.trioangle.spacedetail.model.SimilarSpaceModel
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity
import com.makent.trioangle.travelling.AdditionalPriceActivity
import com.makent.trioangle.travelling.WishListChooseDialog
import com.makent.trioangle.travelling.deleteWishList
import com.makent.trioangle.util.CommonConstantKeys.Companion.SpaceDetailSimilarWishList
import kotlinx.android.synthetic.main.space_similar_list.view.*

class SimilarSpaceAdapter (val activity:Activity,
                           val context: Context,
                           val similarSpaceModelList: ArrayList<SimilarSpaceModel>): RecyclerView.Adapter<SimilarSpaceAdapter.ViewHolder>() {

    var localSharedPreferences=LocalSharedPreferences(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarSpaceAdapter.ViewHolder {
        val rootview=LayoutInflater.from(context).inflate(R.layout.space_similar_list, null, false)
        return ViewHolder(rootview)
    }

    override fun getItemCount(): Int {
        return similarSpaceModelList.size
    }

    override fun onBindViewHolder(holder: SimilarSpaceAdapter.ViewHolder, position: Int) {
        holder.tvSimSpaceType.text=similarSpaceModelList.get(position).spaceTypeName+" • "+similarSpaceModelList.get(position).size+" "+similarSpaceModelList.get(position).sizeType
        holder.tvSimilarSpaceName.text = similarSpaceModelList.get(position).name
        holder.tvSpacePrice.text= HtmlCompat.fromHtml(similarSpaceModelList.get(position).currencySymbol, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()+" "+similarSpaceModelList.get(position).hourlyPrice+" "+context.resources.getString(R.string.per_hour)
        if(similarSpaceModelList.get(position).instantBook.equals("yes",ignoreCase = true)){
            holder.tvInstant.visibility=View.VISIBLE
        }else{
            holder.tvInstant.visibility=View.GONE
        }
        Glide.with(context).load(similarSpaceModelList.get(position).SpacephotoName).into(holder.ivSimSpaceImage)

        if(!similarSpaceModelList.get(position).reviewsCount.equals("0",ignoreCase = true)){
            holder.rbSpaceAvg.rating=similarSpaceModelList.get(position).ratingValue.toFloat()
            if (similarSpaceModelList.get(position).reviewsCount.equals("1")){
                holder.tvSpaceTotalRating.text = similarSpaceModelList.get(position).reviewsCount + " " + context.resources.getString(R.string.review_one)
            }else {
                holder.tvSpaceTotalRating.text = similarSpaceModelList.get(position).reviewsCount + " " + context.resources.getString(R.string.reviews)
            }
            holder.rbSpaceAvg.visibility=View.VISIBLE
            holder.tvSpaceTotalRating.visibility=View.VISIBLE
        }else{
            holder.rbSpaceAvg.visibility=View.GONE
            holder.tvSpaceTotalRating.visibility=View.GONE
        }

        if(!similarSpaceModelList.get(position).ratingValue.equals("0",ignoreCase = true)){
            holder.rbSpaceAvg.visibility=View.VISIBLE
            holder.tvSpaceTotalRating.visibility=View.VISIBLE
        }else{
            holder.rbSpaceAvg.visibility=View.GONE
            holder.tvSpaceTotalRating.visibility=View.GONE
        }



            if (similarSpaceModelList[position].isWhishlist.equals("yes",ignoreCase = true)) {
            holder.ivSpaceWishlist.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.n2_heart_red_fill))
        } else {
            holder.ivSpaceWishlist.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.n2_heart_light_outline))
        }

        holder.ivSpaceWishlist.setOnClickListener {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, "reload")
            localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomId, similarSpaceModelList.get(position).spaceId)

            if (similarSpaceModelList.get(position).isWhishlist.equals("yes",ignoreCase = true)) {
                similarSpaceModelList.get(position).isWhishlist ="No"
                holder.ivSpaceWishlist.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.n2_heart_light_outline))
                val task = deleteWishList(context)
                task.execute()
            } else {
               localSharedPreferences.saveSharedPreferences(Constants.WishListAddress, similarSpaceModelList.get(position).cityName)
               val cdd = WishListChooseDialog(activity,"SimilarSpace",position)
               println("isFinishing " + activity.isFinishing)
               if (!activity.isFinishing) {
                   cdd.show()
                   //holder.ivSpaceWishlist.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.n2_heart_red_fill))
                   //similarSpaceModelList.get(position).isWhishlist="yes"
               }
            }
        }
        holder.itemView.setOnClickListener {
            localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck, "0")
            localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.CheckInOut, null)
            localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, similarSpaceModelList.get(position).spaceId)
            localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, similarSpaceModelList.get(position).hourlyPrice)
            val x = Intent(context, SpaceDetailActivity::class.java)
            context.startActivity(x)
        }
    }

    class ViewHolder(view :View):RecyclerView.ViewHolder(view){
        val ivSimSpaceImage=view.iv_space_image
        val tvSimSpaceType=view.tv_space_type
        val tvSimilarSpaceName=view.tv_similarSpace_name
        val tvSpacePrice=view.tv_space_price
        val tvInstant=view.tv_instant
        val rbSpaceAvg=view.rb_space_avg
        val tvSpaceTotalRating=view.tv_space_total_rating
        val ivSpaceWishlist=view.iv_space_wishlist

    }
}