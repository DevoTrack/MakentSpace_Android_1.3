package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/6/18.
 */

public class HostListedModel implements Serializable {


    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("room_status")
    @Expose
    private String roomStatus;

    @SerializedName("room_thumb_images")
    @Expose
    private ArrayList<String> roomThumbImages;

    @SerializedName("room_image_id")
    @Expose
    private ArrayList<String> roomImageId;

    @SerializedName("room_type")
    @Expose
    private String roomType;

    @SerializedName("bed_type")
    @Expose
    private String bedType;

    @SerializedName("room_name")
    @Expose
    private String roomName;


    @SerializedName("room_description")
    @Expose
    private String roomDescription;

    @SerializedName("amenities")
    @Expose
    private String amenities;

    @SerializedName("room_title")
    @Expose
    private String roomTitle;


    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("room_location")
    @Expose
    private String roomLocation;


    @SerializedName("room_location_name")
    @Expose
    private String roomLocationName;

    @SerializedName("additional_rules_msg")
    @Expose
    private String additionalRulesMsg;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;


    @SerializedName("remaining_steps")
    @Expose
    private String remainingSteps;

    @SerializedName("max_guest_count")
    @Expose
    private String maxGuestCount;

    @SerializedName("bedroom_count")
    @Expose
    private String bedroomCount;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("beds_count")
    @Expose
    private String bedsCount;

    @SerializedName("bathrooms_count")
    @Expose
    private String bathroomsCount;

    @SerializedName("availability_rules")
    @Expose
    private ArrayList<HostAvailabilityRulesModel> availabilityRules;


    @SerializedName("length_of_stay_rules")
    @Expose
    private ArrayList<HostLosRulesModel> lengthOfStayRules;


    @SerializedName("early_bird_rules")
    @Expose
    private ArrayList<HostEarlyBirdRulesModel> earlyBirdRules;

    @SerializedName("last_min_rules")
    @Expose
    private ArrayList<HostLastMinModel> last_min_rules;


    @SerializedName("minimum_stay")
    @Expose
    private String minimumStay;

    @SerializedName("maximum_stay")
    @Expose
    private String maximumStay;


    @SerializedName("length_of_stay_options")
    @Expose
    private ArrayList<HostLosOptionsModel> lengthOfStayOptions;

    @SerializedName("availability_rules_options")
    @Expose
    private ArrayList<AvailabilityRulesOptionModel> availabilityRulesOptions;


    @SerializedName("home_type")
    @Expose
    private String homeType;

    @SerializedName("property_type")
    @Expose
    private String propertyType;

    @SerializedName("cleaning_fee")
    @Expose
    private String cleaningFee;

    @SerializedName("additional_guests_fee")
    @Expose
    private String additionalGuestsFee;

    @SerializedName("for_each_guest_after")
    @Expose
    private String forEachGuestAfter;

    @SerializedName("security_deposit")
    @Expose
    private String securityDeposit;

    @SerializedName("weekend_pricing")
    @Expose
    private String weekendPricing;

    @SerializedName("room_currency_symbol")
    @Expose
    private String roomCurrencySymbol;

    @SerializedName("room_currency_code")
    @Expose
    private String roomCurrencyCode;

    @SerializedName("is_list_enabled")
    @Expose
    private String isListEnabled;

    @SerializedName("policy_type")
    @Expose
    private String policyType;

    @SerializedName("booking_type")
    @Expose
    private String bookingType;

    @SerializedName("street_name")
    @Expose
    private String streetName;

    @SerializedName("street_address")
    @Expose
    private String streetAddress;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("zip")
    @Expose
    private String zip;

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public HostListedModel(String type) {
        System.out.println("Load Check "+type);
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public ArrayList<String> getRoomThumbImages() {
        return roomThumbImages;
    }

    public void setRoomThumbImages(ArrayList<String> roomThumbImages) {
        this.roomThumbImages = roomThumbImages;
    }

    public ArrayList<String> getRoomImageId() {
        return roomImageId;
    }

    public void setRoomImageId(ArrayList<String> roomImageId) {
        this.roomImageId = roomImageId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public String getRoomLocationName() {
        return roomLocationName;
    }

    public void setRoomLocationName(String roomLocationName) {
        this.roomLocationName = roomLocationName;
    }

    public String getAdditionalRulesMsg() {
        return additionalRulesMsg;
    }

    public void setAdditionalRulesMsg(String additionalRulesMsg) {
        this.additionalRulesMsg = additionalRulesMsg;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRemainingSteps() {
        return remainingSteps;
    }

    public void setRemainingSteps(String remainingSteps) {
        this.remainingSteps = remainingSteps;
    }

    public String getMaxGuestCount() {
        return maxGuestCount;
    }

    public void setMaxGuestCount(String maxGuestCount) {
        this.maxGuestCount = maxGuestCount;
    }

    public String getBedroomCount() {
        return bedroomCount;
    }

    public void setBedroomCount(String bedroomCount) {
        this.bedroomCount = bedroomCount;
    }

    public String getBedsCount() {
        return bedsCount;
    }

    public void setBedsCount(String bedsCount) {
        this.bedsCount = bedsCount;
    }

    public String getBathroomsCount() {
        return bathroomsCount;
    }

    public void setBathroomsCount(String bathroomsCount) {
        this.bathroomsCount = bathroomsCount;
    }

    public ArrayList<HostAvailabilityRulesModel> getAvailabilityRules() {
        return availabilityRules;
    }

    public void setAvailabilityRules(ArrayList<HostAvailabilityRulesModel> availabilityRules) {
        this.availabilityRules = availabilityRules;
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

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCleaningFee() {
        return cleaningFee;
    }

    public void setCleaningFee(String cleaningFee) {
        this.cleaningFee = cleaningFee;
    }

    public String getAdditionalGuestsFee() {
        return additionalGuestsFee;
    }

    public void setAdditionalGuestsFee(String additionalGuestsFee) {
        this.additionalGuestsFee = additionalGuestsFee;
    }

    public String getForEachGuestAfter() {
        return forEachGuestAfter;
    }

    public void setForEachGuestAfter(String forEachGuestAfter) {
        this.forEachGuestAfter = forEachGuestAfter;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getWeekendPricing() {
        return weekendPricing;
    }

    public void setWeekendPricing(String weekendPricing) {
        this.weekendPricing = weekendPricing;
    }

    public String getRoomCurrencySymbol() {
        return roomCurrencySymbol;
    }

    public void setRoomCurrencySymbol(String roomCurrencySymbol) {
        this.roomCurrencySymbol = roomCurrencySymbol;
    }

    public String getRoomCurrencyCode() {
        return roomCurrencyCode;
    }

    public void setRoomCurrencyCode(String roomCurrencyCode) {
        this.roomCurrencyCode = roomCurrencyCode;
    }

    public String getIsListEnabled() {
        return isListEnabled;
    }

    public void setIsListEnabled(String isListEnabled) {
        this.isListEnabled = isListEnabled;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public ArrayList<HostLosRulesModel> getLengthOfStayRules() {
        return lengthOfStayRules;
    }

    public void setLengthOfStayRules(ArrayList<HostLosRulesModel> lengthOfStayRules) {
        this.lengthOfStayRules = lengthOfStayRules;
    }

    public ArrayList<HostEarlyBirdRulesModel> getEarlyBirdRules() {
        return earlyBirdRules;
    }

    public void setEarlyBirdRules(ArrayList<HostEarlyBirdRulesModel> earlyBirdRules) {
        this.earlyBirdRules = earlyBirdRules;
    }

    public ArrayList<HostLastMinModel> getLast_min_rules() {
        return last_min_rules;
    }

    public void setLast_min_rules(ArrayList<HostLastMinModel> last_min_rules) {
        this.last_min_rules = last_min_rules;
    }

    public ArrayList<HostLosOptionsModel> getLengthOfStayOptions() {
        return lengthOfStayOptions;
    }

    public void setLengthOfStayOptions(ArrayList<HostLosOptionsModel> lengthOfStayOptions) {
        this.lengthOfStayOptions = lengthOfStayOptions;
    }

    public ArrayList<AvailabilityRulesOptionModel> getAvailabilityRulesOptions() {
        return availabilityRulesOptions;
    }

    public void setAvailabilityRulesOptions(ArrayList<AvailabilityRulesOptionModel> availabilityRulesOptions) {
        this.availabilityRulesOptions = availabilityRulesOptions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
