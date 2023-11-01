package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveEventType : Serializable {

    /**
     * Header activity
     */
    @SerializedName("activity_type")
    var activityTypeId="0"

    /**
     * Activity
     */
    @SerializedName("activity")
    var activityId="0"

    /**
     * Sub Activity
     */
    @SerializedName("sub_activity")
    var activitySubTypeId="0"
}