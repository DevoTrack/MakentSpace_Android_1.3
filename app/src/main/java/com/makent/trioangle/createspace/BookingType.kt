package com.makent.trioangle.createspace

import com.makent.trioangle.helper.Constants
import com.makent.trioangle.host.RoomsBeds.BaseModel
import java.io.Serializable

class BookingType : Serializable, BaseModel {


    lateinit var name: String

    override fun getViewType(): Int {

        return Constants.ViewType.Bookingtype

    }

}
