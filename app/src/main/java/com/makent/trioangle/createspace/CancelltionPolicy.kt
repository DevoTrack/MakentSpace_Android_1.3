package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.RoomsBeds.BaseModel
import java.io.Serializable

class CancelltionPolicy : Serializable, BaseModel {


    @SerializedName("key")
    lateinit var key: String

    @SerializedName("title")
    lateinit var name: String

    @SerializedName("is_selected")
    var isSeleceted: Boolean = false

    override fun getViewType(): Int {

        return Constants.ViewType.CancellationPolicy

    }

}
