package com.makent.trioangle.spacebooking.model.confirmbooking

import com.makent.trioangle.spacebooking.model.SaveDateAndTime
import com.makent.trioangle.spacebooking.model.SaveEventType
import java.io.Serializable

class LocalSavedDatas : Serializable {

    var LocalsavedEventType = SaveEventType()
    var LocalsavedDateTime = SaveDateAndTime()
    var LocalsavedGuestCount = ""
    var LocalEventType = ""
}