package com.makent.trioangle.model.roomModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*****************************************************************
 Room Result Model Class
 ****************************************************************/


public class RoomResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("can_book")
    @Expose
    private String canBook;

    @SerializedName("instant_book")
    @Expose
    private String instantBook;

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;

    @SerializedName("room_name")
    @Expose
    private String roomName;

    @SerializedName("room_images")
    @Expose
    private ArrayList roomImages;

    @SerializedName("room_share_url")
    @Expose
    private String roomShareUrl;

    @SerializedName("is_whishlist")
    @Expose
    private String isWhishlist;

    @SerializedName("rating_value")
    @Expose
    private String ratingValue;

    @SerializedName("host_user_id")
    @Expose
    private String hostUserId;

    @SerializedName("host_user_name")
    @Expose
    private String hostUserName;

    @SerializedName("room_type")
    @Expose
    private String roomType;

    @SerializedName("host_user_image")
    @Expose
    private String hostUserImage;

    @SerializedName("no_of_guest")
    @Expose
    private String noOfGuest;

    @SerializedName("no_of_beds")
    @Expose
    private String noOfBeds;

    @SerializedName("no_of_bedrooms")
    @Expose
    private String noOfBedrooms;

    @SerializedName("no_of_bathrooms")
    @Expose
    private String noOfBathrooms;

    @SerializedName("amenities_values")
    @Expose
    private ArrayList <RoomAmenitiesModel> amenities_values;

    @SerializedName("locaiton_name")
    @Expose
    private String locaitonName;

    @SerializedName("loc_latidude")
    @Expose
    private String locLatidude;

    @SerializedName("loc_longidude")
    @Expose
    private String locLongidude;

    @SerializedName("review_user_name")
    @Expose
    private String reviewUserName;

    @SerializedName("review_user_image")
    @Expose
    private String reviewUserImage;

    @SerializedName("review_date")
    @Expose
    private String reviewDate;

    @SerializedName("review_message")
    @Expose
    private String reviewMessage;

    @SerializedName("review_count")
    @Expose
    private String reviewCount;

    @SerializedName("room_detail")
    @Expose
    private String roomDetail;

    @SerializedName("cancellation_policy")
    @Expose
    private String cancellationPolicy;

    @SerializedName("cleaning")
    @Expose
    private String cleaning;

    @SerializedName("additional_guest")
    @Expose
    private String additionalGuest;

    @SerializedName("guests")
    @Expose
    private String guests;

    @SerializedName("security")
    @Expose
    private String security;

    @SerializedName("weekend")
    @Expose
    private String weekend;

    @SerializedName("house_rules")
    @Expose
    private String houseRules;

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    @SerializedName("blocked_dates")
    @Expose
    private ArrayList blockedDates;

    @SerializedName("common_beds")
    @Expose
    private ArrayList <BedRoomBed> commonBeds;

    @SerializedName("bed_room_beds")
    @Expose
    private ArrayList<ArrayList<BedRoomBed>> bedRoomBeds = null;



    @SerializedName("similar_list_details")
    @Expose
    private ArrayList <RoomSimilarModel> similarListDetails;

    @SerializedName("availability_rules")
    @Expose
    private ArrayList <RoomAvailabilityRules> availabilityRules;

    @SerializedName("length_of_stay_rules")
    @Expose
    private ArrayList <RoomLengthOfStay> lengthOfStayRules;

    @SerializedName("early_bird_rules")
    @Expose
    private ArrayList <RoomLengthOfStay> earlyBirdRules;

    @SerializedName("last_min_rules")
    @Expose
    private ArrayList <RoomLengthOfStay> lastMinRules;

    @SerializedName("minimum_stay")
    @Expose
    private String minimumStay;

    @SerializedName("maximum_stay")
    @Expose
    private String maximumStay;

    @SerializedName("space")
    @Expose
    private String space;

    @SerializedName("access")
    @Expose
    private String access;

    @SerializedName("interaction")
    @Expose
    private String interaction;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("neighborhood_overview")
    @Expose
    private String neighborhood_overview;

    @SerializedName("getting_around")
    @Expose
    private String getting_around;

    @SerializedName("is_shared")
    @Expose
    private String isShared;

    public ArrayList<BedRoomBed> getCommonBeds() {
        return commonBeds;
    }

    public void setCommonBeds(ArrayList<BedRoomBed> commonBeds) {
        this.commonBeds = commonBeds;
    }

    public ArrayList<ArrayList<BedRoomBed>> getBedRoomBeds() {
        return bedRoomBeds;
    }

    public void setBedRoomBeds(ArrayList<ArrayList<BedRoomBed>> bedRoomBeds) {
        this.bedRoomBeds = bedRoomBeds;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getNeighborhood_overview() {
        return neighborhood_overview;
    }

    public void setNeighborhood_overview(String neighborhood_overview) {
        this.neighborhood_overview = neighborhood_overview;
    }

    public String getGetting_around() {
        return getting_around;
    }

    public void setGetting_around(String getting_around) {
        this.getting_around = getting_around;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCanBook() {
        return canBook;
    }

    public void setCanBook(String canBook) {
        this.canBook = canBook;
    }

    public String getInstantBook() {
        return instantBook;
    }

    public void setInstantBook(String instantBook) {
        this.instantBook = instantBook;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(ArrayList roomImages) {
        this.roomImages = roomImages;
    }

    public String getRoomShareUrl() {
        return roomShareUrl;
    }

    public void setRoomShareUrl(String roomShareUrl) {
        this.roomShareUrl = roomShareUrl;
    }

    public String getIsWhishlist() {
        return isWhishlist;
    }

    public void setIsWhishlist(String isWhishlist) {
        this.isWhishlist = isWhishlist;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public String getHostUserName() {
        return hostUserName;
    }

    public void setHostUserName(String hostUserName) {
        this.hostUserName = hostUserName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getHostUserImage() {
        return hostUserImage;
    }

    public void setHostUserImage(String hostUserImage) {
        this.hostUserImage = hostUserImage;
    }

    public String getNoOfGuest() {
        return noOfGuest;
    }

    public void setNoOfGuest(String noOfGuest) {
        this.noOfGuest = noOfGuest;
    }

    public String getNoOfBeds() {
        return noOfBeds;
    }

    public void setNoOfBeds(String noOfBeds) {
        this.noOfBeds = noOfBeds;
    }

    public String getNoOfBedrooms() {
        return noOfBedrooms;
    }

    public void setNoOfBedrooms(String noOfBedrooms) {
        this.noOfBedrooms = noOfBedrooms;
    }

    public String getNoOfBathrooms() {
        return noOfBathrooms;
    }

    public void setNoOfBathrooms(String noOfBathrooms) {
        this.noOfBathrooms = noOfBathrooms;
    }

    public ArrayList<RoomAmenitiesModel> getAmenities_values() {
        return amenities_values;
    }

    public void setAmenities_values(ArrayList<RoomAmenitiesModel> amenities_values) {
        this.amenities_values = amenities_values;
    }

    public String getLocaitonName() {
        return locaitonName;
    }

    public void setLocaitonName(String locaitonName) {
        this.locaitonName = locaitonName;
    }

    public String getLocLatidude() {
        return locLatidude;
    }

    public void setLocLatidude(String locLatidude) {
        this.locLatidude = locLatidude;
    }

    public String getLocLongidude() {
        return locLongidude;
    }

    public void setLocLongidude(String locLongidude) {
        this.locLongidude = locLongidude;
    }

    public String getReviewUserName() {
        return reviewUserName;
    }

    public void setReviewUserName(String reviewUserName) {
        this.reviewUserName = reviewUserName;
    }

    public String getReviewUserImage() {
        return reviewUserImage;
    }

    public void setReviewUserImage(String reviewUserImage) {
        this.reviewUserImage = reviewUserImage;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getRoomDetail() {
        return roomDetail;
    }

    public void setRoomDetail(String roomDetail) {
        this.roomDetail = roomDetail;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getCleaning() {
        return cleaning;
    }

    public void setCleaning(String cleaning) {
        this.cleaning = cleaning;
    }

    public String getAdditionalGuest() {
        return additionalGuest;
    }

    public void setAdditionalGuest(String additionalGuest) {
        this.additionalGuest = additionalGuest;
    }

    public String getGuests() {
        return guests;
    }

    public void setGuests(String guests) {
        this.guests = guests;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getWeekend() {
        return weekend;
    }

    public void setWeekend(String weekend) {
        this.weekend = weekend;
    }

    public String getHouseRules() {
        return houseRules;
    }

    public void setHouseRules(String houseRules) {
        this.houseRules = houseRules;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public ArrayList getBlockedDates() {
        return blockedDates;
    }

    public void setBlockedDates(ArrayList blockedDates) {
        this.blockedDates = blockedDates;
    }

    public ArrayList<RoomSimilarModel> getSimilarListDetails() {
        return similarListDetails;
    }

    public void setSimilarListDetails(ArrayList<RoomSimilarModel> similarListDetails) {
        this.similarListDetails = similarListDetails;
    }

    public ArrayList<RoomAvailabilityRules> getAvailabilityRules() {
        return availabilityRules;
    }

    public void setAvailabilityRules(ArrayList<RoomAvailabilityRules> availabilityRules) {
        this.availabilityRules = availabilityRules;
    }

    public ArrayList<RoomLengthOfStay> getLengthOfStayRules() {
        return lengthOfStayRules;
    }

    public void setLengthOfStayRules(ArrayList<RoomLengthOfStay> lengthOfStayRules) {
        this.lengthOfStayRules = lengthOfStayRules;
    }

    public ArrayList<RoomLengthOfStay> getEarlyBirdRules() {
        return earlyBirdRules;
    }

    public void setEarlyBirdRules(ArrayList<RoomLengthOfStay> earlyBirdRules) {
        this.earlyBirdRules = earlyBirdRules;
    }

    public ArrayList<RoomLengthOfStay> getLastMinRules() {
        return lastMinRules;
    }

    public void setLastMinRules(ArrayList<RoomLengthOfStay> lastMinRules) {
        this.lastMinRules = lastMinRules;
    }

    public String getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(String minimumStay) {
        this.minimumStay = minimumStay;
    }

    public String getMaximumStay() {
        return maximumStay;
    }

    public void setMaximumStay(String maximumStay) {
        this.maximumStay = maximumStay;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }
}
