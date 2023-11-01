package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.makent.trioangle.spacebooking.model.SaveDateAndTime
import com.makent.trioangle.spacebooking.model.SaveEventType
import com.makent.trioangle.spacebooking.model.SpaceAvailabilityTimings
import java.io.Serializable
import java.lang.reflect.Array

class SpaceResult : Serializable {

    @SerializedName("status_code")
    @Expose
    var statusCode = ""

    @SerializedName("success_message")
    @Expose
    var successMessage = ""

    @SerializedName("space_id")
    @Expose
    var spaceId = ""

    @SerializedName("instant_book")
    @Expose
    var instantBook = ""

    @SerializedName("is_wishlist")
    @Expose
    var isWishlist = ""

    @SerializedName("host_profile_pic")
    @Expose
    var hostProfilePic = ""

    @SerializedName("hourly")
    @Expose
    var hourly = ""

    @SerializedName("currency_symbol")
    @Expose
    var currency_symbol = ""

    @SerializedName("space_url")
    @Expose
    var spaceUrl = ""

    @SerializedName("can_book")
    @Expose
    var canBook = ""


    @SerializedName("name")
    @Expose
    var spacename = ""

    @SerializedName("summary")
    @Expose
    var summary = ""


    @SerializedName("user_id")
    @Expose
    var userId: String = ""


    @SerializedName("host_name")
    @Expose
    var hostName = ""

    @SerializedName("space_type_name")
    @Expose
    var spaceTypeName = ""


    @SerializedName("number_of_guests")
    @Expose
    var numberofGuests = ""

    @SerializedName("number_of_rooms")
    @Expose
    var numberofRooms = ""

    @SerializedName("number_of_restrooms")
    @Expose
    var numberofRestrooms = ""


    @SerializedName("floor_number")
    @Expose
    var floorNumber = ""

    @SerializedName("sq_ft_text")
    @Expose
    var spaceSize = ""

    @SerializedName("size_type")
    @Expose
    var sizeType = ""

    @SerializedName("cancellation_policy")
    @Expose
    var cancellation_policy = ""

    @SerializedName("space")
    @Expose
    var space = ""


    @SerializedName("access")
    @Expose
    var access = ""

    @SerializedName("interaction")
    @Expose
    val interaction = ""


    @SerializedName("notes")
    val notes = ""

    @SerializedName("neighborhood_overview")
    val neighborhoodOverview = ""

    @SerializedName("services_extra")
    val servicesExtra = ""


    @SerializedName("review_count")
    @Expose
    var reviewCount = ""

    @SerializedName("review_message")
    @Expose
    var reviewMessage = ""

    @SerializedName("review_user_name")
    @Expose
    var reviewUserName = ""

    @SerializedName("review_user_image")
    @Expose
    var reviewUserImage = ""

    @SerializedName("review_date")
    @Expose
    var reviewDate = ""

    @SerializedName("rating")
    @Expose
    var rating = ""

    @SerializedName("maximum_guests")
    @Expose
    var maximumGuests = ""


    @SerializedName("space_photos")
    @Expose
    var spacePhotos = ArrayList<SpacePhotoList>()

    @SerializedName("the_space")
    @Expose
    var theSpace = ArrayList<CommonSpaceDataModel>()

    @SerializedName("guest_access")
    @Expose
    val guestAccess = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("amenities")
    @Expose
    val amenities = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("services")
    @Expose
    val services = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("special_feature")
    @Expose
    val specialFeature = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("space_rules")
    @Expose
    val spaceRules = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("space_style")
    @Expose
    val spaceStyle = ArrayList<SpacesAccessServicesAndOthers>()

    @SerializedName("location_data")
    @Expose
    val locationData = LocationDatas()

    @SerializedName("")
    @Expose
    val spaceDetailArray = SpaceDetailArray()


    @SerializedName("space_activities")
    @Expose
    val spaceActivityArray = ArrayList<SpaceActivityArray>()

    @SerializedName("similar_listings")
    @Expose
    val similarListing = ArrayList<SimilarSpaceModel>()


    @SerializedName("availability_times")
    @Expose
    var spaceAvailabilityTimes = ArrayList<SpaceAvailabilityTimings>()

    @SerializedName("not_available_dates")
    @Expose
    var notAvailableDates:ArrayList<String>?=null


    var blockedDay = ArrayList<Int>()



    class SpacePhotoList : Serializable {
        @SerializedName("id")
        @Expose
        var photoId = ""

        @SerializedName("name")
        @Expose
        var photoName = ""

        @SerializedName("highlights")
        @Expose
        var photoHighlights = ""
    }


    class LocationDatas :Serializable{
        @SerializedName("address_line_1")
        val addressLine1 = ""

        @SerializedName("address_line_2")
        val addressLine2 = ""

        @SerializedName("city")
        val city = ""

        @SerializedName("state")
        val state = ""

        @SerializedName("country")
        val country = ""

        @SerializedName("postal_code")
        val postalCode = ""

        @SerializedName("latitude")
        val latitude = ""

        @SerializedName("longitude")
        val longitude = ""

        @SerializedName("guidance")
        val guidance = ""
    }


    /**
     * Saving Filtered Datas when it is not Null
     */
    var LocalSavedDateTime = SaveDateAndTime()
    var LocalFilteredGuestCount = ""


}