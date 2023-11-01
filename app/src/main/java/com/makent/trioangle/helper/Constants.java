package com.makent.trioangle.helper;

import android.Manifest;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Trioangle Product Team
 * @version 1.1
 * @package com.makent.trioangle
 * @subpackage helper
 * @category Constants
 */

/* ************************************************************
Constants values for shared preferences
*************************************************************** */

public class Constants {

    public static final String APP_NAME = "Makent"; // App name
    public static final String FILE_NAME = "makent";

    public static long mLastClickTime = 0;
    public static final int REQUEST_CODE_GALLERY = 5;
    public static final String STATUS_MSG = "success_message";
    public static final String STATUS_CODE = "status_code";
    public static final String REFRESH_ACCESS_TOKEN = "refresh_token";

    public static final String AccessToken = "access_token"; //save access_token // User ID
    public static final String CurrencySymbolApi = "currency_symbol"; //save access_token // User ID
    public static final String UserId = "user_id"; //save access_token // User ID
    public static final String AppleServiceId= "apple_service_id";

    public static final String isHost = "isHost";   //0 for guest 1 for host check is host or guest  to show switch to hosting or travelling
    public static final String stepHouserules = "stepHouserules"; // check steps remaing for request to book  to show the steps remaining
    public static final String stepPayment = "stepPayment"; // check steps remaing for request to book  to show the steps remaining
    public static final String stepHostmessage = "stepHostmessage"; // check steps remaing for request to book  to show the steps remaining
    public static final String isRequestrRoom = "isRequestrRoom";// Check house rules form request page or rooms details page to show or hide agree button

    public static final String isCheckAvailability = "isCheckAvailability";   // * 0 for Home / Explore page *  1 for Rooms Details page *
    public static final String isRequestCheck = "isRequestCheck";   // * 0 for Check Availability *  1 for Request *

    // Signup

    public static final String FirstName = "firstname";   // save firstname
    public static final String LastName = "lastname"; //save lastname
    public static final String Email = "email"; //save email
    public static final String Password = "password"; //save password
    public static final String DOB = "DOB"; //save DOB
    public static final String FBID = "facebookid"; //save Facebook ID
    public static final String APPLEID = "appleid"; //save APPLE ID
    public static final String GPID = "googleid"; //save Google ID
    public static final String ProfilePicture = "profilepicture"; //save User profile picture in Facebook / Google / Normal Login or Signup    public static final String GPID= "googleplusid"; //save Google plus ID
    public static final String IP = "ip"; //save email

    /* Key used in makent

   isHost  -> check is host or guest  to show switch to hosting or travelling
   houserules, payment, host  -> check steps remaing for request to book  to show the steps remaining
   isRequest -> Check house rules form request page or rooms details page to show or hide agree button
    */
    // Google place Search
    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static String PlacesTag = "Google Places";

    //
    public static final String AddWish = "addwish";
    public static final String WishFromExp = "wishfromexp";
    public static final String WishDismissExp = "wishdismissexp";

    // Applelogin
    public static final String isAppleLogin ="isapplelogin";
    public static final String Authtype ="authType";
    public static final String AuthId ="authId";

    // Map Filter wishlist
    public static final String MapFilterWishlist = "mapFilterWishlist";
    public static final String MapFilterWishlistId = "mapFilterWishlistId";


    // Rooms Details

    public static final String mSpaceId = "selectedspaceid"; //save selected Room Id
    public static final String SelectedRoomPrice = "selectedroomprice"; //save selected Room Id
    public static final String WishlistRoomId = "selectedwishlistroomid"; //save selected Wish list Room Id
    public static final String RoomReviewRate = "selectedroomreviewrate"; //save selected room review rate
    public static final String HouseRules = "house_rules"; //save selected room house rules
    public static final String HostUserName = "houstusername"; //save selected host user name
    public static final String HostUserID = "houstuserid"; //save selected host user name


    // Explore search Home filter
    public static final String isFilterApplied = "isFilterApplied"; //save Search CheckOut for local
    public static final String CheckOutDetail = "checkoutdetail"; //save Search CheckOut for local
    public static final String CheckInDetail = "checkindetail"; //save Search CheckIn for local
    public static final String CheckOut = "checkout"; //save Search CheckOut for local
    public static final String CheckIn = "checkin"; //save Search CheckIn for local
    public static final String CheckInOut = "checkinout"; //save Search check in and check out in Jan-04 Jan-05 this format for local

    public static final String SearchGuest = "searchguest"; //save Search Guest
    public static final String RoomGuest = "roomguest"; //save Search Guest
    public static final String SearchCheckIn = "searchcheckin"; //save Search CheckIn
    public static final String SearchCheckOut = "searchcheckout"; //save Search CheckOut
    public static final String SearchCheckInOut = "searchcheckinout"; //save Search check in and check out in Jan-04 Jan-05 this format
    public static final String SearchLocation = "searchlocation"; //save Search Place
    public static final String SearchLocationNearby = "searchlocationnearby"; //save Search Place for nearby
    public static final String SearchLocationLatitude = "searchlocationlatitude"; //save Search Place for nearby
    public static final String SearchLocationLongitude = "searchlocationlongitude"; //save Search Place for nearby


    public static final String ReqPaymentType = "paymenttype";   //
    public static final String CountryName = "countryname"; //
    public static final String CountryCode = "countrynamebooking"; //
    public static final String CountryName2 = "countryname2"; //

    public static final String CurrencyName2 = "currencyname2"; //
    public static final String Genders = "genders"; //
    public static final String CountryCurrencyType = "countrycurrencytype"; //

    public static final String CurrencyCode = "currencycode"; //
    public static final String CurrencySymbol = "currencysymbol";
    public static final String Currency = "currency";
    public static final String HostUserName2 = "hostusername2";
    public static final String IsLocationFound = "islocationfound";

    public static final String UserDetails = "userdetails";// Store user details JSON Object from View profile
    public static final String AboutMe = "aboutme";
    public static final String Gender = "gender";
    public static final String Phone = "phone";
    public static final String Location = "locaiton";
    public static final String Work = "work";
    public static final String School = "school";

    public static int dialogshow = 0;
    public static String langcode = "";

    // Filter data
    public static final String FilterInstantBook = "filterinstantbook";
    public static final String FilterMinPrice = "filterminprice";
    public static final String FilterMaxPrice = "filtermaxprice";
    public static final String FilterMinPriceCheck = "filterminpricecheck";
    public static final String FilterMaxPriceCheck = "filtermaxpricecheck";
    public static final String FilterBeds = "bedcounts";
    public static final String FilterBedRoom = "bedrooms";
    public static final String FilterBathRoom = "bathrooms";
    public static final String FilterAmenities = "filteramenities";
    public static final String FilterRoomTypes = "filterroomtype";

    public static final String isInstantBook = "isInstantBook";
    public static final String ReqMessage = "requestbookmessage";
    public static final String TripType = "currenttriptype";
    public static final String TripTypeCount = "currenttriptypecount";
    public static final String TotalTripDetails = "totaltripdetails";
    public static final String TripsDetails = "TripsDetails";// Store Trips total details in  JSON Array
    public static final String ClearFilter = "ClearFilter";// Store Trips total details in  JSON Array


    //Makent Space Filter data:
    public static final String FilterAmenity = "fitleramenity";
    public static final String FilterSpecial = "fitlerspecial";
    public static final String FilterStyle = "fitlerstyle";
    public static final String FilterSpace = "fitlerspace";
    public static final String FilterService = "fitlerservice";
    public static final String FilterSpaceType = "filterspacetype";
    public static final String FilterEventType = "filtereventtype";
    public static final String EventName = "eventName";
    public static final String SpaceName = "spaceName";


    // Listing

    public static final String ListRoomType = "listroomtype";
    public static final String ListRoomTypeName = "listroomtypename";
    public static final String ListPropertyType = "listpropertytype";
    public static final String ListPropertyTypeName = "listpropertytypeName";
    public static final String ListBedType = "listbedtype";
    public static final String ListBedTypeName = "listbedtypename";
    public static final String ListLocationLat = "listlocationlat";
    public static final String ListLocationLong = "listlocationlong";
    public static final String ListGuests = "listguest";
    public static final String ListBeds = "listbed";
    public static final String ListBedRooms = "listbedrooms";
    public static final String ListBathRooms = "listbathrooms";
    public static final String PropertyListJSON = "propertylistjson";

    public static final String Bedtype = "bedtype";
    public static final String Roomtype = "roomtype";
    public static final String Propertytype = "propertytype";

    public static final String ListData = "listdata";
    public static final String ReserveSettings = "availability_rules";
    public static final String AvailableRulesOption = "availability_rules_options";

    public static final String LastMinCount = "LastMinCount";
    public static final String EarlyBirdDiscount = "Earlybirddiscount";
    public static final String LengthOfStay = "LengthOfStay";
    public static final String PayoutLength = "payoutlength";
    public static final String LengthOfStayOptions = "LengthOfStayOptions";

    public static final String Day = "Day";
    public static final String Hour = "Hour";
    public static final String MinHr = "Min hour";

    //  ***Exp***

    public static final String WishPostion = "wishposition"; //save selected Exp Id
    public static final String ExpId = "selectedexpid"; //save selected Exp Id
    public static final String SelectedExpPrice = "selectedexpprice"; //save selected Exp price
    public static final String Schedule_id = "schedule_id"; //schedule id
    public static final String GuestFirstName = "guestFirstName"; //guest user name
    public static final String GuestLastName = "guestLastName"; //guest user name
    public static final String GuestImage = "guestImage"; //guest user name
    public static final String WishlistRoomExp = "wishExpRoom"; //save selected Exp Id
    public static final String SpotsLeft = "spotsleft"; //save spots left
    public static final String ChooseDate = "choosedate"; //save spots left
    public static final String ExpStartTime = "starttime"; //save spots left
    public static final String ExpEndTime = "endtime"; //save spots left
    public static final String FilterCategory = "FilterCategory"; //save access_token // User ID
    public static final String FilterCategoryName = "FilterCategoryName"; //save access_token // User ID
    public static final String FilterCategorySize = "FilterCategorySize"; //save access_token // User ID
    public static final String WishListHomeCount = "WishListHomeCount"; // store wish list address


    public static final String ListRoomTitle = "listroomtitle";
    public static final String ListRoomSummary = "listroomsummary";
    public static final String ListRoomPrice = "listroomprice";
    public static final String ListRoomPricea = "listroompricea";
    public static final String IsSpaceList = "isspacelist";
    public static final String ListSpaceCurrencyCode = "spacecurrencycode";
    public static final String ListSpaceCurrencySymbol = "spacecurrencysymbol";
    public static final String ListRoomAddress = "listroomaddress";
    public static final String ListRoomHouseRules = "listroomhouserules";
    public static final String ListRoomBookType = "listroombooktype";
    public static final String ListRoomPolicyType = "listroompolicytype";
    public static final String ListRoomImages = "listroomimages";
    public static final String ListRoomImage = "listroomimage";
    public static final String ListRoomImageCount = "listroomimagecount";
    public static final String ListRoomImageId = "listroomimageid";

    // Additional Pricing data
    public static final String ListWeeklyPrice = "listweekly_price";
    public static final String ListMonthlyPrice = "listmonthly_price";
    public static final String ListCleaningFee = "listcleaning_fee";
    public static final String ListAdditionalGuest = "listadditional_guests";
    public static final String ListGuestAfter = "listguest_after";
    public static final String ListSecurityDeposit = "listsecurity_deposit";
    public static final String ListWeekendPrice = "listweekend_pricing";

    public static final String ListAmenities = "listamenities";
    public static final String ListIsEnable = "listisenable";
    public static final String City = "city";
    public static final String State = "state";
    public static final String Zip = "zip";
    public static final String Lat = "lat";
    public static final String Logt = "logt";
    public static final String Streetline = "streetline";
    public static final String Building = "building";
    public static final String Countyname = "contryname";
    public static final String Value = "value";

    public static final String DesSpaceMsg = "spacemsg";
    public static final String DesGuestMsg = "guestmsg";
    public static final String DesGuestInteractionMsg = "guestinteractionmsg";
    public static final String DesOverviewMsg = "overviewmsg";
    public static final String DesGettingMsg = "gettingarroundmsg";
    public static final String DesOtherThings = "otherthings";
    public static final String DesHouseMsg = "housemsg";

    public static final String ReservationDetails = "reservationdetails";
    public static final String ReservationCount = "reservationcount";
    public static final String ReservationLoadTime = "reservationloadtime";

    public static final String CalendarRoomList = "calendarroomlist";
    public static final String NightPrice = "nightly_price"; // selected room night price details in JSON array for host calendar
    public static final String RoomPrice = "roomprice"; // selected room price for host calendar

    public static final String PayoutCount = "payoutcount"; // Number of payout email available

    public static final String WishListId = "wishlistid"; // store wish list id
    public static final String WishListPrivacy = "wishlistprivacy"; // store wish list privacy setting type 0 for every one 1 for onely me
    public static final String WishListName = "wishlistname"; // store wish list name
    public static final String WishListCount = "wishlistcount"; // store wish list id
    public static final String WishListAddress = "wishlistaddress"; // store wish list address

    public static final String WishlistType = "WishlistType"; // store wish list address
    public static final String Reload = "reload"; // Reload explore page activity when wish list change

    public static final String Backpaymailshow = "backpaymailshow"; //save email

    public static final String MinimumStay = "minimum_stay";
    public static final String MaximumStay = "maximum_stay";

    public static final String StripeCountryCode = "countrycode";
    public static final String PayPalCountryCode = "paypalcountrycode";
    public static final String UserDOB = "dob";
    public static final String LastPage = "lastPage";
    public static final String ReloadBool = "lareloadbool";
    public static final String EMPTYTOKENADDRESS = "emptytokenaddress";
    public static final String EMPTYTOKENROOMNAME = "emptytokenroomname";
    public static final String EMPTYTOKENBEDROOM = "emptytokenbedroom";
    public static final String EMPTYTOKENBATHROOM = "emptytokenbathroom";
    public static final String REQBACK = "REQBACK";


    public static final String LanguageName = "languageName";
    public static final String LanguageCode = "languageCode";
    public static final String LanguageRecreate = "languageRecreate";

    public static final String ExploreHomeType = "explorehometype";
    public static final String ExploreHomeType_key = "explorehometypekey";
    public static final String ExploreRoom_ExpType = "exploreroomexptype";
    public static final String ExploreRoom_ExpType_key = "exploreroomexptypekey";
    public static final String searchIconChanged = "searchIconChanged";
    public static final String HomeShowAll = "homeshowall";
    public static final String defaultCurrency = "defaultCurrency";
    public static final String WishTitle = "WishTitle";
    public static final String GenreName = "GenreName";

    public static final String HomePage = "homepage";
    public static final String HostUser = "HostUser";
    public static String type = null;

    public static final String CheckIsFilterApplied = "checkisfilterapplied";
    public static final String CheckAvailableScreen = "checkavailablescreen";


    public static final String ListFunc = "listFunc";
    public static final String ListFuncAdd = "listFuncAdd";
    public static final String ListFuncEdit = "listFuncEdit";
    public static final String SETUPSTEP = "setupStep";
    public static final String BASICSTEP = "basicStep";
    public static final String READYTOHOSTSTEP = "readytohostStep";
    public static final String AVAILABILITYTIMES = "availabilityTimes";
    public static final String CalenderData = "calenderdata";
    public static final String BOOKINGMESSAGE = "bookingMessage";
    public static final String ISBACK = "isBack";


    //Price Break down
    public static final String RESERVATIONID = "reservationId";
    public static final String SPECIALOFFERID = "specialOfferId";
    public static final String SESSIONID = "skey";
    public static final String USERTYPE = "usertype"; // guest or host
    public static final int REQUEST_HOST_MESSAGE = 100; // guest or host


    public static final String NewMonth = "";
    public static final String NewYear = "";
    public static final String StartTime = "StartTime";
    public static final String StartTimeAMPM = "";
    public static final String EndTime = "endTime";
    public static final String EndTimeAMPM = "";
    public static final String CheckInOuttime = "";

    public static final String activityName ="activityName";
    public static final String activityId ="activityId";
    public static final String SecurityCheck = "securitycheck";


    public static boolean HomeLoadMore = false;
    public static boolean ExperienceLoadMore = false;
    public static boolean ShowAllLoadMore = false;
    public static boolean isBack = false;


    public static int PasswordLength = 8;
    public static int backpressed = 0;

    @IntDef({ViewType.BedType})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
        int BedType = 100;
        int MultipleTime = 102;
        int AvailabilityTime = 103;
        int Bookingtype = 104;
        int CancellationPolicy = 105;
        int CalendarData = 106;
        int currencyData = 107;

    }


    @IntDef({ListType.GuestAccess})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ListType {
        int GuestAccess = 100;
        int ServiceType = 101;

    }


    public static final String searchlocationAddress = "searchlocationAddress";


    public static final String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    /*For makentSpace new constant added:
    * */

   public static final String TotalPage = "totalPage";
   public static boolean LoadMore = false;
   public static final String isFromHost ="isFromHost";
   public static final String GetSpaceId ="getSpaceid";
   public static final String isFromBookingFlow ="isfrombookingflow";
   public static final String SpaceBookingType ="spaceBookingType";
   public static final String SpaceResults ="spaceresults";
   public static final String BlockDates ="blockdates";
   public static final String SpaceBlockedDays ="spaceblockeddays";
   public static final String SpaceAvailableTime ="spaceavailabletime";
   public static final String SavedDateAndTime ="savedDateAndTime";
   public static final String isContactHost ="iscontacthost";
   public static final String CheckAvailabiltyResults ="checkavailabiltyresults";

   public static final String Bookingtype ="bookingType";
   public static final String MessageToHost ="messagetoHost";
   public static final String CountyrNameBooking = "countryNameBooking";
   public static final String PaymentDetailsForStripe ="paymentdetailsforstripe";

   //Constant Datas
   public static final String WishListAdded ="wishlistadded";

   public static final String ChooseWishListType ="choosewishlisttype";

   public static final String StatusPending ="Pending";
   public static final String StatusListed ="Listed";
   public static final String StatusUnlisted ="UnListed";

    public static final int ActivityBasic =321;
    public static final String TranslateLanguage ="translateLanguage";
    public static final String WeekHr = "Week hour";
    public static final String MonHr = "Mon hour";



    public static String STRIPE_KEY ="STRIPE_KEY";
    public static String Image_ID ="Image_ID";
    public static String STRIPEID_TOKEN ="STRIPEID_TOKEN";

    public static String languageCode ="en";

    public static String APPROVED ="Approved";
    public static String PENDING ="Pending";
    public static String RESUBMIT ="Resubmit";

    public static String Host_Preview ="Host_Preview";

    public static String ARABIC ="ar";

}
