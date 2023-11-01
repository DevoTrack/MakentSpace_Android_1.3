package com.makent.trioangle.interfaces;
/**
 * @package com.trioangle.gofereats
 * @subpackage interfaces
 * @category ApiService
 * @author Trioangle Product Team
 * @version 1.0
 **/

//import com.stripe.android.model.PaymentMethod;

import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*****************************************************************
 ApiService
 ****************************************************************/

public interface ApiService {

    //Login
    @GET("login")
    Call<ResponseBody> login(@Query("email") String email, @Query("password") String pass,@Query("timezone") String time, @Query("language") String language);

    //Forget password
    @GET("forgotpassword")
    Call<ResponseBody> forgetpwd(@Query("email") String email, @Query("language") String language);

    //emailvalidation
    @GET("emailvalidation")
    Call<ResponseBody> emailvalidation(@Query("email") String email, @Query("language") String language, @Query("auth_type") String authtype, @Query("auth_id") String authid);

    //Signup
    @GET("signup")
    Call<ResponseBody> signup(@Query("first_name") String fname, @Query("last_name") String lname, @Query("email") String email, @Query("password") String password, @Query("dob") String dob, @Query("auth_type") String authtype, @Query("auth_id") String authid, @Query("timezone") String time, @Query("language") String language);

    //Facebook Signup
    @GET("signup")
    Call<ResponseBody> fbsignup(@Query("first_name") String fname, @Query("last_name") String lname, @Query("email") String email, @Query("auth_id") String fbid, @Query("auth_type") String authtype, @Query("auth_id") String authid, @Query("profile_pic") String profile_pic,@Query("timezone") String time, @Query("dob") String dob);

    //Google Signup
    @GET("signup")
    Call<ResponseBody> gpsignup(@Query("first_name") String fname, @Query("last_name") String lname, @Query("email") String email, @Query("auth_id") String fbid, @Query("auth_type") String authtype, @Query("auth_id") String authid, @Query("profile_pic") String profile_pic,@Query("timezone") String time);

    //Room Properties
    @GET("room_property_type")
    Call<ResponseBody> roomproperty(@Query("token") String token, @Query("language") String language);

    //User Profile Details
    @POST("common_data")
    Call<ResponseBody> requestCommonData();

    //Home and experience
    @GET("home")
    Call<ResponseBody> home(@Query("search_type") String searchType,@Query("list") String listtype, @Query("guests") String guests, @Query("location") String location, @Query("latitude") String latitude,@Query("longitude") String longitude,@Query("token") String token, @Query("language") String language,@Query("checkin") String checkin,@Query("checkout") String checkout,@Query("page") String page);

    /*
    Newly Added Search API
     */
    // Search_type_filter
    @GET("search_filters")
    Call<ResponseBody> search_filter(@Query("token") String token);


    //Explore categories filter
    @GET("host_experience_categories")
    Call<ResponseBody> hostExperienceCategories(@Query("token") String token ,@Query("language") String language);


    //Explore experience
    @GET("explore_experiences")
    Call<ResponseBody> exploreExperiences(@Query("token") String token, @Query("page") String page, @Query("guests") String guests, @Query("location") String location,@Query("latitude") String latitude,@Query("longitude") String longitude, @Query("checkin") String checkin, @Query("checkout") String checkout, @Query("category") String category, @Query("language") String language);

    //choose date experience
    @GET("choose_date")
    Call<ResponseBody> chooseDate(@Query("token") String token, @Query("host_experience_id") String host_experience_id, @Query("date") String date);

    //Experience review details
    @GET("experience_review_detail")
    Call<ResponseBody> experienceReviewDetail(@Query("token") String token, @Query("host_experience_id") String host_experience_id, @Query("page") int page);

    //Room Detail
    @GET("experience_pre_payment")
    Call<ResponseBody> experiencePrePayment(@Query("token") String token, @Query("host_experience_id") String host_experience_id, @Query("date") String date, @Query("scheduled_id") String scheduledId, @Query("guest_details") String guestDetails);

    //Room Detail
    @GET("space")
    Call<ResponseBody> spaceDetail(@Query("token") String token, @Query("space_id") String room_id, @Query("language") String language);

    //Experience Detail
    @GET("experience")
    Call<ResponseBody> expDetails(@Query("token") String token, @Query("host_experience_id") String exp_id, @Query("language") String language);

    //Experience contact host
    @GET("experiences/contact_host")
    Call<ResponseBody> contactHostExp(@Query("token") String token, @Query("host_experience_id") String exp_id, @Query("message") String exp_message,@Query("list_type") String list_type);


    //Trips Types and Count
    @GET("booking_types")
    Call<ResponseBody> tripsType(@Query("token") String token);

    //Trips Details
    @GET("booking_details")
    Call<ResponseBody> tripsDetail(@Query("token") String token, @Query("booking_type") String trips_type);

    //Guest Cancels pending reservation
    @GET("guest_cancel_pending_reservation")
    Call<ResponseBody> guestCancelPending(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("cancel_reason") String cancel_reason, @Query("cancel_message") String cancel_message);

    //Guest Cancels reservation
    @GET("guest_cancel_reservation")
    Call<ResponseBody> guestCancelReservation(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("cancel_reason") String cancel_reason, @Query("cancel_message") String cancel_message);

    //Host Cancels reservation
    @GET("host_cancel_reservation")
    Call<ResponseBody> hostCancelReservation(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("cancel_reason") String cancel_reason, @Query("cancel_message") String cancel_message);

    //Preapprove reservation
    @GET("pre_approve")
    Call<ResponseBody> preapprove(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("template") String template, @Query("message") String cancel_message);

    //Decline request
    @GET("accept")
    Call<ResponseBody> accept(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("message_to_guest") String message_to_guest);

    //Decline request
    @GET("decline")
    Call<ResponseBody> decline(@Query("token") String token, @Query("reservation_id") String reservation_id, @Query("decline_reason") String cancel_reason, @Query("decline_message") String cancel_message);


    @FormUrlEncoded
    @POST("update_space")
    Call<ResponseBody> updateSpace(@FieldMap HashMap<String, String> hashMap);

    @GET("price_breakdown")
    Call<ResponseBody> priceBreakDown(@QueryMap HashMap<String, String> hashMap);

    @GET("update_space_description")
    Call<ResponseBody> updateSpaceDescription(@QueryMap HashMap<String, String> hashMap);

    //Inboc Reservation
    @GET("inbox")
    Call<ResponseBody> inboxList(@Query("token") String token, @Query("type") String type);

    //Send Message
    @GET("send_message")
    Call<ResponseBody> sendMessage(@Query("token") String token, @Query("space_id") String room_id, @Query("host_user_id") String host_user_id, @Query("reservation_id") String reservation_id, @Query("message_type") String message_type, @Query("message") String message);

    //Chat Details
    @GET("conversation_list")
    Call<ResponseBody> conversationList(@Query("token") String token, @Query("host_user_id") String host_user_id, @Query("reservation_id") String reservation_id);

    //User Profile Details
    @GET("view_profile")
    Call<ResponseBody> viewProfile(@Query("token") String token);

    //User Profile Details
    @GET("room_bed_details")
    Call<ResponseBody> getBedTypeDetails(@Query("token") String token,@Query("room_id") String roomId);

    //User Profile Details
    @FormUrlEncoded
    @POST("update_bed_detail")
    Call<ResponseBody> saveBedTypeDetails(@FieldMap HashMap<String, String> data);

    //Currency List
    @GET("currency_list")
    Call<ResponseBody> currencyList(@Query("token") String token);

    //Currency List
    @GET("country_list")
    Call<ResponseBody> countrylist(@Query("token") String token);

    //Update Currency
    @GET("currency_change")
    Call<ResponseBody> currencyChange(@Query("token") String token, @Query("currency_code") String currency_code);

    //Update Currency
    @GET("update_room_currency")
    Call<ResponseBody> updateRoomCurrency(@Query("token") String token, @Query("currency_code") String currency_code, @Query("room_id") String room_id);

    //Edit Profile Details
    @GET("edit_profile")
    Call<ResponseBody> editProfile(@Query("token") String token, @Query("first_name") String firstName, @Query("last_name") String lastName, @Query("user_thumb_url") String userThumbUrl, @Query("dob") String dob, @Query("user_location") String user_location, @Query("about_me") String about_me, @Query("school") String school, @Query("gender") String gender, @Query("email") String email, @Query("phone") String phone, @Query("work") String work);

    //Other User Progile Details
    @GET("user_profile_details")
    Call<ResponseBody> otherUserProfile(@Query("token") String token, @Query("user_id") String other_user_id);

    //Payout Details
    @GET("payout_details")
    Call<ResponseBody> payoutDetails(@Query("token") String token);

    //Payout Details
    @GET("space_listing_details")
    Call<ResponseBody> getListingDetails(@Query("token") String token,@Query("space_id") String spaceId);


    //Booking details
    @GET("get_payment_data")
    Call<ResponseBody> getPaymentData(@QueryMap HashMap<String, String> hashMap);

    //Booking details
    @GET("complete_booking")
    Call<ResponseBody> completeBooking(@QueryMap HashMap<String, String> hashMap);

    //Booking details
    @GET("apply_coupon")
    Call<ResponseBody> applyCoupon(@QueryMap HashMap<String, String> hashMap);

    //Booking details
    @GET("remove_coupon")
    Call<ResponseBody> removeCoupon(@QueryMap HashMap<String, String> hashMap);

//Payout Details
    @GET("listing")
    Call<ResponseBody> getListing(@Query("token") String token);



    //Payout Details
    @GET("setup_step_items")
    Call<ResponseBody> getSetupItems(@Query("token") String token);


    //Add payout perference
    @FormUrlEncoded
    @POST("update_space")
    Call<ResponseBody> updateReadyToHost(@FieldMap HashMap<String, String> data);

    //Add payout perference
    @FormUrlEncoded
    @POST("add_payout_perference")
    Call<ResponseBody> addPayoutPreference(@Field("token") String token, @Field("address1") String address1, @Field("address2") String address2, @Field("paypal_email") String paypal_email, @Field("city") String city, @Field("state") String state, @Field("country") String country, @Field("postal_code") String postal_code, @Field("payout_method") String payout_method);

    //Calenday Availability check
    @GET("calendar_availability_status")
    Call<ResponseBody> checkavailability(@Query("token") String token, @Query("room_id") String room_id, @Query("start_date") String start_date, @Query("end_date") String end_date, @Query("total_guest") String total_guest);

    //Reservation Detail
    @GET("reservation_list")
    Call<ResponseBody> reservationDetail(@Query("token") String token);

    //Reservation Detail
    @GET("listing")
    Call<ResponseBody> listingDetail(@Query("token") String token, @Query("page") String page);

    //Reservation Detail
    @GET("rooms_list_calendar")
    Call<ResponseBody> roomListCalendar(@Query("token") String token);

    //Update Price Rule
    @GET("update_price_rule")
    Call<ResponseBody> updatePriceRule(@Query("token") String token, @Query("room_id") String room_id, @Query("type") String type, @Query("period") String period, @Query("discount") String discount, @Query("id") String id);

    //Update Price Rule
    @GET("delete_price_rule")
    Call<ResponseBody> deletePriceRule(@Query("token") String token, @Query("id") String id, @Query("room_id") String room_id);

    //update calendar
    @GET("new_update_calendar")
    Call<ResponseBody> updateListCalendar(@Query("token") String token, @Query("room_id") String room_id, @Query("blocked_dates") String blocked_dates, @Query("is_avaliable_selected") String is_avaliable_selected, @Query("nightly_price") String nightly_price);//


    //save amenities in optional details
    @GET("update_amenities")
    Call<ResponseBody> updateAmenitiesList(@Query("token") String token, @Query("room_id") String room_id, @Query("selected_amenities") String selectedAmenities);


    //Add new Room

    @FormUrlEncoded
    @POST("new_add_room")
    Call<ResponseBody> newAddRoom(@FieldMap HashMap<String, String> data);


   /* @GET("new_add_room")
    Call<ResponseBody> newAddRoom(@Query("token") String token, @Query("room_type") String room_type, @Query("bed_type") String bed_type, @Query("property_type") String property_type,
                                  @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("max_guest") String max_guest, @Query("bathrooms") String bathrooms,
                                  @Query("common_room_bed_details") String commonBedDetails,@Query("bedroom_bed_details") String bedDetails,@Query("new_add_room") String new_add_room);
*/
    //Enable Disable Listing
    @GET("disable_listing")
    Call<ResponseBody> disableListing(@Query("token") String token, @Query("room_id") String room_id);

    //Enable Disable Listing
    @GET("add_rooms_price")
    Call<ResponseBody> addRoomsprice(@Query("token") String token, @Query("room_id") String room_id, @Query("room_price") String room_price);

    //Add title to Listing
    @GET("update_title_description")
    Call<ResponseBody> updateTitledescription(@Query("token") String token, @Query("room_id") String room_id, @Query("room_title") String room_title);

    //Get Description of Listing
    @GET("update_description")
    Call<ResponseBody> getDescription(@Query("token") String token, @Query("room_id") String room_id);

    //Add Description to Listing
    @GET("update_title_description")
    Call<ResponseBody> updateDescription(@Query("token") String token, @Query("room_id") String room_id, @Query("room_description") String room_description);

    //Update Description space
    @GET("update_description")
    Call<ResponseBody> updateDescriptionSpace(@Query("token") String token, @Query("room_id") String room_id, @Query("space") String room_description);

    //Update Description space
    @GET("update_description")
    Call<ResponseBody> updateGuestSpace(@Query("token") String token, @Query("room_id") String room_id, @Query("guest_access") String guest_access);

    //Update Description space
    @GET("update_description")
    Call<ResponseBody> updateGuestInteratction(@Query("token") String token, @Query("room_id") String room_id, @Query("interaction_guests") String guest_access);//Update Description space

    @GET("update_description")
    Call<ResponseBody> updateNeighborhoodOverview(@Query("token") String token, @Query("room_id") String room_id, @Query("neighborhood_overview") String neighborhoodOverview);

    //Update Getting around
    @GET("update_description")
    Call<ResponseBody> updateGettingAround(@Query("token") String token, @Query("room_id") String room_id, @Query("getting_arround") String guest_access);

    //Update ExtraMessage
    @GET("update_description")
    Call<ResponseBody> updateExtraMessage(@Query("token") String token, @Query("room_id") String room_id, @Query("notes") String guest_access);

    //Update HouseRules
    @GET("update_description")
    Call<ResponseBody> updateHouseRulesMessage(@Query("token") String token, @Query("room_id") String room_id, @Query("house_rules") String house_rules);

    //Update listing location
    @GET("update_location")
    Call<ResponseBody> updateLocation(@Query("token") String token, @Query("room_id") String room_id, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("street_name") String street_name, @Query("street_address") String street_address, @Query("city") String city, @Query("state") String state, @Query("zip") String zip, @Query("country") String country, @Query("is_success") String is_success);

    //Update House rules
    @GET("update_house_rules")
    Call<ResponseBody> updateHouseRules(@Query("token") String token, @Query("room_id") String room_id, @Query("house_rules") String house_rules);

    //Update Booking type
    @GET("update_booking_type")
    Call<ResponseBody> updateBookingType(@Query("token") String token, @Query("room_id") String room_id, @Query("booking_type") String house_rules);

    //Update Min Max Stay
    @GET("update_minimum_maximum_stay")
    Call<ResponseBody> updateMinimumMaximumStay(@Query("token") String token, @Query("room_id") String room_id, @Query("minimum_stay") String minimum_stay, @Query("maximum_stay") String maximum_stay);

    //Update Min Max Stay for Days
    @GET("update_availability_rule")
    Call<ResponseBody> updateAvailabilityRule(@Query("token") String token, @Query("room_id") String room_id, @Query("type") String type, @Query("minimum_stay") String minimum_stay, @Query("maximum_stay") String maximum_stay, @Query("start_date") String start_date, @Query("end_date") String end_date, @Query("id") String id);

    //Update Min Max Stay for Days
    @GET("listing_rooms_beds")
    Call<ResponseBody> listingRoomsBeds(@Query("token") String token, @Query("room_id") String room_id, @Query("property_type") String property_type, @Query("room_type") String room_type, @Query("person_capacity") String person_capacity, @Query("bedrooms") String bedrooms, @Query("beds") String beds, @Query("bathrooms") String bathrooms, @Query("bed_type") String bed_type);

    //Update Min Max Stay for Days
    @GET("update_Long_term_prices")
    Call<ResponseBody> updateLongTermPrices(@Query("token") String token, @Query("room_id") String room_id, @Query("cleaning_fee") String cleaning_fee, @Query("additional_guests") String additional_guests, @Query("for_each_guest") String for_each_guest, @Query("security_deposit") String security_deposit, @Query("weekend_pricing") String weekend_pricing);

    //Update Cancel policyList
    @GET("update_policy")
    Call<ResponseBody> updatePolicy(@Query("token") String token, @Query("room_id") String room_id, @Query("policy_type") String policy_type);


    //Upload profile ivPhoto
    @POST("upload_profile_image")
    Call<ResponseBody> uploadImage(@Body RequestBody RequestBody, @Query("token") String token);


    //Upload room ivPhoto
    @POST("room_image_upload")
    Call<ResponseBody> uploadRoomImage(@Body RequestBody RequestBody, @Query("token") String token);


    //Payout Details
    @POST("space_image_upload")
    Call<ResponseBody> uploadSpaceImage(@Body RequestBody RequestBody);

    //Contact host
    @GET("contact_request")
    Call<ResponseBody> contactHost(@Query("token") String token, @Query("room_id") String roomId, @Query("check_in_date") String checkInDate, @Query("check_out_date") String checkOutDate, @Query("no_of_guest") String noOfGuest, @Query("message_to_host") String messageToHost);


    //Get Country List
    @GET("country_list")
    Call<ResponseBody> getCountryList(@Query("token") String token);

    //Get Country List
    @GET("delete_availability_rule")
    Call<ResponseBody> deleteAvailabilityRule(@Query("token") String token, @Query("room_id") String roomId, @Query("id") String id);

    //Get pre_payment
    @GET("pre_payment")
    Call<ResponseBody> prePayment(@Query("token") String token, @Query("room_id") String roomId, @Query("start_date") String startDate, @Query("end_date") String endDate, @Query("total_guest") String totalGuest);

    //Get pre_payment
    @GET("payout_changes")
    Call<ResponseBody> payoutChanges(@Query("token") String token, @Query("payout_id") String payout_id, @Query("type") String type);

    //Get review
    @GET("review_detail")
    Call<ResponseBody> getReviewDetails(@Query("token") String token, @Query("space_id") String roomId, @Query("page") String pageNo);

    //Get Amenities List
    @GET("amenities_list")
    Call<ResponseBody> getAmenitiesList(@Query("token") String token, @Query("language") String language);

    // Add stripe payout preference
    @POST("add_payout_perference")
    Call<ResponseBody> uploadStripe(@Body RequestBody RequestBody, @Query("token") String token, @Query("stripe_token") String stripetoken);

    //Get Amenities List
    @GET("remove_uploaded_image")
    Call<ResponseBody> removeUploadedImage(@Query("token") String token, @Query("room_id") String room_id, @Query("image_id") String image_id);//Get Amenities List


    @GET("delete_image")
    Call<ResponseBody> deleteSpaceImage(@Query("token") String token, @Query("space_id") String room_id, @Query("image_id") String image_id);


    @GET("update_space")
    Call<ResponseBody> updateImageDescription(@Query("step") String step,@Query("token") String token, @Query("space_id") String room_id, @Query("space_photos") String image_id);

    //List of Stripe Supported Countries
    @GET("stripe_supported_country_list")
    Call<ResponseBody> stripeSupportedCountry(@Query("token") String token);

    //Update Language
    @GET("language")
    Call<ResponseBody> languageChange(@Query("language") String lang,@Query("token") String token);



    //getSpace Basic Steps
    @GET("basics_step_items")
    Call<ResponseBody> spaceBasicListing(@Query("token") String token);

   // Call<ResponseBody> explore(@Query("token") String token, @Query("page") String page, @Query("guests") String guests, @Query("location") String location, @Query("latitude") String latitude,@Query("longitude") String longitude,@Query("checkin") String checkin, @Query("checkout") String checkout, @Query("instant_book") String instant_book, @Query("amenities") String amenities, @Query("min_price") String min_price, @Query("max_price") String max_price, @Query("room_type") String room_type, @Query("beds") String beds, @Query("bedrooms") String bedrooms, @Query("bathrooms") String bathrooms, @Query("language") String language);


    @GET("explore")
    Call<ResponseBody> exploreHome(@Query("token") String token, @Query("page") String page,@Query("space_type") String space_type_name,@Query("instant_book") String instant_book,@Query("min_price") String min_price, @Query("max_price") String max_price,
                                   @Query("activity_type") String activity_type, @Query("location") String location,
                                   @Query("amenities") String aminites, @Query("services") String services, @Query("space_rules") String space_rules, @Query("space_style") String space_style, @Query("special_feature") String special_feature, @Query("guests") String guests, @Query("start_time") String start_time,@Query("end_time") String end_time
                                        ,@Query("checkin") String checkin,@Query("checkout") String checkout);

    @GET("explore")
    Call<ResponseBody> exploreSpace(@Query("token") String token, @Query("page") String page);

    //getSpace Basic Steps
    @GET("update_space")
    Call<ResponseBody> updateSpaceDetails(@Query("step") String step, @Query("token") String token,@QueryMap LinkedHashMap<String,String> spaceDatas);


    //getSpace Basic Steps
    @FormUrlEncoded
    @POST("update_activities")
    Call<ResponseBody> updateSpaceActivities(@FieldMap HashMap<String, String> data);

    //get minimum amount while change currency
    @GET("get_min_amount")
    Call<ResponseBody> getMinAmount(@Query("token") String token, @Query("currency_code") String currency_code);


    @GET("update_calendar")
    Call<ResponseBody> updateCalendar(@Query("token") String token, @Query("space_id") String space_id, @Query("start_date") String start_date, @Query("start_time") String start_time, @Query("notes") String notes, @Query("available_status") String available_status);

    @GET("space_activities")
    Call<ResponseBody> getSpaceActivities(@Query("token") String token, @Query("space_id") String spaceId);

    @GET("get_availability_times")
    Call<ResponseBody> getAvailabilityTimes(@Query("token") String token, @Query("space_id") String spaceId, @Query("start_date") String startDate, @Query("end_date") String endDate, @Query("time_zone") String TimeZone);

    @GET("store_payment_data")
    Call<ResponseBody> storePaymentData(@QueryMap HashMap<String,String >bookSpace);

    @GET("contact_request")
    Call<ResponseBody> contactHost(@QueryMap HashMap<String,String >contactHost);

    @GET("confirm_booking")
    Call<ResponseBody> confirmBooKing(@Query("token") String token, @Query("pay_key") String payKey, @Query("s_key") String sessionKey,@Query("message") String messageToHost);

    @FormUrlEncoded
    @POST("generate_stripe_key")
    Call<ResponseBody> generateStripeKey(@FieldMap LinkedHashMap<String, String> hashMap);

    //Get Wish List
    @GET("get_wishlist")
    Call<ResponseBody> getWishlist(@Query("token") String token);

    //Add to wishlist
    @GET("add_wishlist")
    Call<ResponseBody> addWishList(@QueryMap LinkedHashMap<String,String> addWishlistParams);

    //Get selected wishlist
    @GET("get_particular_wishlist")
    Call<ResponseBody> getSelectedWishlist(@Query("token") String token, @Query("list_id") String listId);

    @GET("edit_wishlist")
    Call<ResponseBody> editWishList(@QueryMap LinkedHashMap<String,String> editWishlistParams);

    //Delete wishlist
    @GET("delete_wishlist")
    Call<ResponseBody> deleteWishList(@QueryMap LinkedHashMap<String,String> deleteWishlistParams);

    @GET("currency_conversion")
    Call<ResponseBody> currencyConversion(@QueryMap HashMap<String,String> currencyConversion);

    //Delete Account
    @GET("delete_account")
    Call<ResponseBody> deleteAccount(@Query("token") String token, @Query("confirm") String confirm);


}
