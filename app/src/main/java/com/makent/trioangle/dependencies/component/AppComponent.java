package com.makent.trioangle.dependencies.component;
/**
 * @package com.trioangle.com.makent.trioangle
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.makent.trioangle.HomeFragment;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.createspace.ActivitysFragment;
import com.makent.trioangle.createspace.AvailabilityFragment;
import com.makent.trioangle.createspace.BasicStepsActivity;
import com.makent.trioangle.createspace.ChooseTimeActivity;
import com.makent.trioangle.createspace.EditCalendar;
import com.makent.trioangle.createspace.EditCalenderFragment;
import com.makent.trioangle.createspace.EditListingActivity;
import com.makent.trioangle.createspace.ChooseTimeFragment;
import com.makent.trioangle.createspace.GetReadyToHostActivity;
import com.makent.trioangle.createspace.SetupActivity;
import com.makent.trioangle.createspace.SpaceBookingtype;
import com.makent.trioangle.createspace.SpaceDescriptionFragment;
import com.makent.trioangle.adapter.AvailabilityAdapter;
import com.makent.trioangle.adapter.WishListAdapter;
import com.makent.trioangle.adapter.WishlistDetailAdapter;
import com.makent.trioangle.adapter.host.ListingdetailsAdapter;
import com.makent.trioangle.adapter.host.PayPalEmailAdapter;
import com.makent.trioangle.autocomplete.PlacesAutoComplete;
import com.makent.trioangle.backgroundtask.ImageCompressAsyncTask;
import com.makent.trioangle.chat.ChatActivity;
import com.makent.trioangle.createspace.AddressFragment;
import com.makent.trioangle.createspace.AmenistiesFragment;
import com.makent.trioangle.createspace.CreateSpaceMapActivity;
import com.makent.trioangle.createspace.GuestAccessFragment;
import com.makent.trioangle.createspace.GuestNumberFragment;
import com.makent.trioangle.createspace.PhotoFragment;
import com.makent.trioangle.createspace.RoomsCountFragment;
import com.makent.trioangle.createspace.ServiceFragment;
import com.makent.trioangle.createspace.SpaceListingActivity;
import com.makent.trioangle.createspace.SpaceRulesFragment;
import com.makent.trioangle.createspace.SpaceStyleFragment;
import com.makent.trioangle.createspace.SpecialFeatures;
import com.makent.trioangle.createspace.TypeOfSpaceFragment;
import com.makent.trioangle.createspace.setprice.ActivityPriceAdapter;
import com.makent.trioangle.createspace.setprice.SetPriceFragment;
import com.makent.trioangle.dependencies.module.AppContainerModule;
import com.makent.trioangle.dependencies.module.ApplicationModule;
import com.makent.trioangle.dependencies.module.NetworkModule;

//***Experience Start***




//***Experience End***



import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.host.BedtypeListActivity;
import com.makent.trioangle.host.LYSActivity;
import com.makent.trioangle.host.LYS_Home;
import com.makent.trioangle.host.LYS_Location;
import com.makent.trioangle.host.LYS_OptionalDetails;
import com.makent.trioangle.host.LYS_Rooms_Beds;
import com.makent.trioangle.host.LYS_Setp1_AddPhoto;
import com.makent.trioangle.host.LYS_Step2_AddSummary;
import com.makent.trioangle.host.LYS_Step2_AddTitle;
import com.makent.trioangle.host.LYS_Step3_SetPrice;
import com.makent.trioangle.host.LYS_Step4_AddressDetails;
import com.makent.trioangle.host.LYS_Step4_SetAddress;
import com.makent.trioangle.host.LYS_Step5_Additional_Rules;
import com.makent.trioangle.host.LYS_Step6_Booking_Type;
import com.makent.trioangle.host.PreAcceptActivity;
import com.makent.trioangle.host.PriceBreakDown;
import com.makent.trioangle.host.RoomsBeds.BedsActivity;
import com.makent.trioangle.host.RoomsBeds.HelperListActivity;
import com.makent.trioangle.host.optionaldetails.AddMinMax;
import com.makent.trioangle.host.optionaldetails.OD_AmenitiesNew;
import com.makent.trioangle.host.optionaldetails.OD_CancelPolicy;
import com.makent.trioangle.host.optionaldetails.OD_Description;
import com.makent.trioangle.host.optionaldetails.OD_LongTermPrice;
import com.makent.trioangle.host.optionaldetails.OD_RoomsBeds;
import com.makent.trioangle.host.optionaldetails.ReservationSettings;
import com.makent.trioangle.host.optionaldetails.description.AddEarlyBird;
import com.makent.trioangle.host.optionaldetails.description.AddLastMin;
import com.makent.trioangle.host.optionaldetails.description.AddLengthOfstay;
import com.makent.trioangle.host.optionaldetails.description.LastminAdapter;
import com.makent.trioangle.host.optionaldetails.description.LengthOfStayAdapter;
import com.makent.trioangle.host.optionaldetails.description.ODDExtraDetails;
import com.makent.trioangle.host.optionaldetails.description.ODDGettingAround;
import com.makent.trioangle.host.optionaldetails.description.ODDGuest;
import com.makent.trioangle.host.optionaldetails.description.ODDGuestInteraction;
import com.makent.trioangle.host.optionaldetails.description.ODDHouseRules;
import com.makent.trioangle.host.optionaldetails.description.ODDOverview;
import com.makent.trioangle.host.optionaldetails.description.ODDSpace;
import com.makent.trioangle.host.optionaldetails.description.TypeObjectAdapter;
import com.makent.trioangle.host.tabs.HostCalenderActivity;
import com.makent.trioangle.host.tabs.YourReservationActivity;
import com.makent.trioangle.host.tabs.HostListingActivity;
import com.makent.trioangle.language.LanguageConverter;
import com.makent.trioangle.newhome.views.NewHomeActivity;
import com.makent.trioangle.profile.EditProfileActivity;
import com.makent.trioangle.profile.PayoutAddressDetailsActivity;
import com.makent.trioangle.profile.PayoutBankDetailsActivity;
import com.makent.trioangle.profile.PayoutEmailActivity;
import com.makent.trioangle.profile.PayoutEmailListActivity;
import com.makent.trioangle.profile.ProfilePageActivity;
import com.makent.trioangle.profile.SettingActivity;
import com.makent.trioangle.signin.Forgot_PasswordActivity;
import com.makent.trioangle.signin.LoginActivity;
import com.makent.trioangle.signup.Signup_DobActivity;
import com.makent.trioangle.signup.Signup_EmailActivity;
import com.makent.trioangle.signup.Signup_FacebookDetails;
import com.makent.trioangle.spacebooking.views.ChooseBookingTimingsActivity;
import com.makent.trioangle.spacebooking.views.RequestSpaceActivity;
import com.makent.trioangle.spacebooking.views.SpaceAvailableActivity;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.stripepayment.PayWithStripeActivity;
import com.makent.trioangle.travelling.CalendarActivity;
import com.makent.trioangle.travelling.CancelReservationActivity;
import com.makent.trioangle.travelling.ContactHostActivity;
import com.makent.trioangle.travelling.FilterActivity;
import com.makent.trioangle.travelling.FilterAmenitiesList;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.PaymentCountryList;
import com.makent.trioangle.travelling.RequestActivity;
import com.makent.trioangle.travelling.ReviewActivityHome;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.travelling.Search_Guest_Bed;
import com.makent.trioangle.travelling.Search_anywhere;
import com.makent.trioangle.travelling.TripsDetails;
import com.makent.trioangle.travelling.WishListChooseDialog;
import com.makent.trioangle.travelling.WishListCreateActivity;
import com.makent.trioangle.travelling.WishListDetailsActivity;
import com.makent.trioangle.travelling.WishlistHomeFragment;
import com.makent.trioangle.travelling.deleteWishList;
import com.makent.trioangle.travelling.tabs.ExploreSearchActivity;
import com.makent.trioangle.travelling.tabs.InboxActivity;
import com.makent.trioangle.travelling.tabs.ProfileActivity;
import com.makent.trioangle.travelling.tabs.SavedActivity;
import com.makent.trioangle.travelling.tabs.TripsActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;
import com.makent.trioangle.util.WebServiceUtils;

import javax.inject.Singleton;

import dagger.Component;


/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    // ***Experience Start***



    // ***Experience End***

    void inject(HelperListActivity experienceListHelperActivity);

    void inject(ChooseTimeFragment experienceTimeFragment);

    void inject(ReviewActivityHome reviewActivityhome);

    void inject(BedtypeListActivity bedtypeListActivity);

    void inject(BedsActivity reviewActivityhome);

    void inject(WishlistHomeFragment wishlistHomeFragment);

    void inject(HomeFragment homeFragment);

    void inject(LoginActivity loginActivity);

    void inject(LYS_OptionalDetails lys_OptionalDetails);

    void inject(OD_AmenitiesNew od_AmenitiesNew);

    void inject(TypeObjectAdapter typeObjectAdapter);

    void inject(LengthOfStayAdapter lengthOfStayAdapter);

    void inject(LastminAdapter lastminAdapter);

    void inject(AddLastMin addLastMin);

    void inject(AddLengthOfstay addLengthOfstay);

    void inject(AddEarlyBird addEarlyBird);

    void inject(ListingdetailsAdapter listingdetailsAdapter);

    void inject(AvailabilityAdapter availabilityAdapter);

    void inject(PayPalEmailAdapter payPalEmailAdapter);

    void inject(ActivityPriceAdapter activityPriceAdapter);

    void inject(LYS_Location lys_location);


    void inject(Forgot_PasswordActivity forgotPasswordActivity);

    void inject(Signup_EmailActivity signup_emailActivity);

    void inject(Signup_DobActivity signupDobActivity);

    void inject(Signup_FacebookDetails signupFacebookDetails);

    void inject(MainActivity mainActivity);

    void inject(HomeActivity homeActivity);

    void inject(ExploreSearchActivity exploreSearchActivity);

    //void inject(ChooseTimeActivity chooseTimeActivity);

    void inject(Room_details room_details);

    void inject(SavedActivity savedActivity);

    void inject(CalendarActivity calendarActivity);

    void inject(TripsActivity tripsActivity);

    void inject(TripsDetails tripsDetails);

    void inject(CancelReservationActivity cancelReservationActivity);

    void inject(InboxActivity inboxActivity);

    void inject(ChatActivity chatActivity);

    void inject(ProfileActivity profilePageActivity);

    void inject(ProfilePageActivity profilePageActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(SettingActivity settingActivity);

    void inject(PayoutAddressDetailsActivity settingActivity);

    void inject(PayoutEmailListActivity settingActivity);

    void inject(PayoutEmailActivity settingActivity);

    void inject(HostCalenderActivity hostCalenderActivity);

    void inject(YourReservationActivity hostHomeActivity);

    void inject(LYS_Step3_SetPrice lys_step3_setPrice);

    void inject(SpecialFeatures specialFeatures);

    void inject(HostListingActivity hostListingActivity);

    void inject(LYSActivity lysActivity);

    void inject(LYS_Rooms_Beds lysRoomsBeds);

    void inject(LYS_Home lys_home);

    void inject(PriceBreakDown priceBreakDown);

    void inject(LYS_Step2_AddTitle lys_step2_addTitle);

    void inject(LYS_Step2_AddSummary lys_step2_addTitle);

    void inject(LYS_Step4_AddressDetails lys_step4_addressDetails);

    void inject(LYS_Step4_SetAddress lys_step4_addressDetails);

    void inject(LYS_Step5_Additional_Rules lys_step5_additional_rules);

    void inject(LYS_Step6_Booking_Type lys_step6_booking_type);

    void inject(PreAcceptActivity preAcceptActivity);

    void inject(ReservationSettings reservationSettings);

    void inject(AddMinMax addMinMax);

	void inject(ContactHostActivity contactHostActivity);

    void inject(WishListDetailsActivity wishListDetailsActivity);

    void inject(WishlistDetailAdapter wishlistDetailAdapter);

    void inject(FilterActivity filterActivity);

    void inject(FilterAmenitiesList filterAmenitiesList);

    void inject(PaymentCountryList paymentCountryList);

    void inject(RequestActivity requestActivity);

    void inject(RequestSpaceActivity requestSpaceActivity);

    void inject(Search_anywhere search_anywhere);

    void inject(SpaceAvailableActivity spaceAvailableActivity);


    void inject(Search_Guest_Bed search_guest_bed);

    void inject(WishListChooseDialog wishListChooseDialog);

    void inject(WishListCreateActivity wishListCreateActivity);

    void inject(WishListAdapter wishListAdapter);

    void inject(PayoutBankDetailsActivity payoutBankDetailsActivity);
    //void inject(ChooseTimeActivity chooseTimeActivity);

    void inject(SpaceDetailActivity spaceDetailActivity);

    void inject(SpaceBookingtype spaceBookingtype);

    void inject(OD_RoomsBeds od_roomsBeds);

    void inject(OD_LongTermPrice od_longTermPrice);

    void inject(OD_Description od_description);

    void inject(ODDSpace oddSpace);

    void inject(ODDGuest oddGuest);

    void inject(ODDOverview oddOverview);

    void inject(ODDGuestInteraction oddGuestInteraction);

    void inject(ODDGettingAround oddGettingAround);

    void inject(ODDExtraDetails oddExtraDetails);

    void inject(ODDHouseRules oddHouseRules);

    void inject(OD_CancelPolicy odCancelPolicy);

    void inject(LYS_Setp1_AddPhoto lys_setp1_addPhoto);

    void inject(deleteWishList lys_setp1_addPhoto);

    void inject(RequestCallback requestCallback);

    void inject(CommonMethods commonMethods);

    void inject(WebServiceUtils webServiceUtils);

    void inject(RunTimePermission runTimePermission);
    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);


    //New Home Page
    void inject(NewHomeActivity newHomeActivity);

    void inject(LanguageConverter languageConverter);

    void inject(GuestNumberFragment guestNumberFragment);


    void inject(PlacesAutoComplete placesAutoComplete);

    void inject(TypeOfSpaceFragment typeOfSpaceFragment);

    void inject(RoomsCountFragment roomsCountFragment);

    void inject(GuestAccessFragment guestAccessFragment);

    void inject(AmenistiesFragment amenistiesFragment);

    void inject(ServiceFragment amenistiesFragment);

    void inject(AddressFragment addressFragment);

    void inject(ActivitysFragment activitysFragment);

    void inject(SetPriceFragment setPriceFragment);

    void inject(EditCalenderFragment editCalenderFragment);

    void inject(CreateSpaceMapActivity createSpaceMapActivity);

    void inject(PhotoFragment photoFragment);

    void inject(SpaceStyleFragment spaceStyleFragment);

    void inject(SpaceRulesFragment spaceRulesFragment);

    void inject(SpaceDescriptionFragment spaceDescriptionFragment);

    void inject(SetupActivity setupActivity);

    void inject(EditListingActivity editListingActivity);

    void inject(BasicStepsActivity basicStepsActivity);

    void inject(AvailabilityFragment availabilityFragment);

    void inject(GetReadyToHostActivity getReadyToHostActivity);

    void inject(ChooseTimeActivity getReadyToHostActivity);

    void inject(SpaceListingActivity spaceListingActivity);

    void inject(EditCalendar editCalendar);

    void inject(ChooseBookingTimingsActivity chooseBookingTimingsActivity);

    void inject(PayWithStripeActivity payWithStripeActivity);





}
