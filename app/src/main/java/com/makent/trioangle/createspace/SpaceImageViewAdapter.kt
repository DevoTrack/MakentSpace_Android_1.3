package com.makent.trioangle.createspace

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makent.trioangle.R
import com.makent.trioangle.createspace.interfaces.ImageDeleteListner
import kotlinx.android.synthetic.main.space_grid_view.view.*
import java.util.*

//class SpaceImageViewAdapter(var activity: Context, items: ArrayList<SpacePhotoModelClass>) : RecyclerView.Adapter<ViewHolder>()  {
class SpaceImageViewAdapter(val items: ArrayList<SpacePhotoModelClass>, val context: Context,val imageDeleteListner: ImageDeleteListner) : RecyclerView.Adapter<ViewHolders>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        return ViewHolders(LayoutInflater.from(context).inflate(R.layout.space_grid_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {

        holder.tvGuidance.setText(items.get(position).highlights)
        try {

            Glide.with(context).load(items.get(position).imageUrl).into(holder.ivPhoto)


        } catch (e: Exception) {
        }



        holder.tvGuidance.addTextChangedListener(EditTextTextWatcher(holder.tvGuidance,position,imageDeleteListner))

        holder.ivDelete.setOnClickListener {
            imageDeleteListner.onItemDelete(position)
        }
    }
}

private class EditTextTextWatcher constructor(val view: View, val position: Int,val imageDeleteListner: ImageDeleteListner) : TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(editable: Editable) {
        if (view.hasFocus()) {
            imageDeleteListner.onTextChange(position,editable.toString())

        }


    }
}

class ViewHolders(view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto = view.iv_photo
    val ivDelete = view.iv_delete
    val tvGuidance = view.tv_guidance


}
