package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SimilarSpaceModel : Serializable {
    @SerializedName("space_id")
    @Expose
    var spaceId = ""
    

    @SerializedName("space_type_name")
    @Expose
    var spaceTypeName = ""

    @SerializedName("size")
    @Expose
    var size = ""

    @SerializedName("size_type")
    @Expose
    var sizeType = ""

    @SerializedName("name")
    @Expose
    var name = ""

    @SerializedName("city_name")
    @Expose
    var cityName = ""

    @SerializedName("photo_name")
    @Expose
    var SpacephotoName = ""

    @SerializedName("rating")
    @Expose
    var ratingValue = ""

    @SerializedName("reviews_count")
    @Expose
    var reviewsCount = ""

    @SerializedName("is_wishlist")
    @Expose
    var isWhishlist = ""

    @SerializedName("instant_book")
    @Expose
    var instantBook = ""

    @SerializedName("hourly")
    @Expose
    var hourlyPrice = ""

    @SerializedName("currency_symbol")
    @Expose
    var currencySymbol = ""

}