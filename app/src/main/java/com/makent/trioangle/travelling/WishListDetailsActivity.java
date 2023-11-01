package com.makent.trioangle.travelling;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.WishlistDetailAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;

import com.makent.trioangle.travelling.ViewPagerAdapter;
// ***Experience Start***

// ***Experience End***

import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.travelling.tabs.SavedActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_DELETE_WISHLIST;
import static com.makent.trioangle.util.Enums.REQ_EDIT_WISHLIST;
import static com.makent.trioangle.util.Enums.REQ_GET_WISHLIST;



/* ***********************************************************************
This is WishListDetails Page Contain Saved WishListDetails
**************************************************************************  */
public class WishListDetailsActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;


    ImageView wishlistdetails_close, wishlist_roomfilter, menu;

    RecyclerView recyclerView;
    List<Makent_model> searchlist;
    WishlistDetailAdapter adapter;

    LinearLayout wishlist_empty, wishlist_subempty;
    TextView wishlist_name, wishlist_explore;
    ImageView wishlist_dot_loader;
    LocalSharedPreferences localSharedPreferences;
    String listname, privacy;
    Dialog_loading mydialog;
    String userid, listid, newlistname;
    AlertDialog alertDialog;
    boolean deleteprivacy = false;
    protected boolean isInternetAvailable;
    public TabLayout tabLayout;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_details);
        commonMethods = new CommonMethods();

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);


        localSharedPreferences = new LocalSharedPreferences(this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager = (ViewPager) findViewById(R.id.pager);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);


        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        listname = localSharedPreferences.getSharedPreferences(Constants.WishListName);
        privacy = localSharedPreferences.getSharedPreferences(Constants.WishListPrivacy);

        wishlist_dot_loader = (ImageView) findViewById(R.id.wishlist_dot_loader);
        wishlist_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(wishlist_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        wishlist_name = (TextView) findViewById(R.id.wishlist_name);
        wishlist_explore = (TextView) findViewById(R.id.wishlist_explore);
        wishlist_empty = (LinearLayout) findViewById(R.id.wishlist_empty);
        wishlist_subempty = (LinearLayout) findViewById(R.id.wishlist_subempty);
        wishlist_name.setText(listname);


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, 10000, true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menu = (ImageView) findViewById(R.id.menu);
        registerForContextMenu(menu); //ONLY USE THIS FOR METHOD1
        menu.setClickable(true);

        wishlistdetails_close = (ImageView) findViewById(R.id.wishlistdetails_close);
        commonMethods.rotateArrow(wishlistdetails_close,this);
        wishlist_roomfilter = (ImageView) findViewById(R.id.wishlist_roomfilter);


        wishlist_name.setOnClickListener(this);
        wishlistdetails_close.setOnClickListener(this);
        wishlist_roomfilter.setOnClickListener(this);
        wishlist_explore.setOnClickListener(this);
        menu.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.wishlist_roomlist);
        searchlist = new ArrayList<>();

        /*adapter = new WishlistDetailAdapter(getHeader(), this, this, searchlist);
        adapter.setLoadMoreListener(new WishlistDetailAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = searchlist.size() - 1;
                        //   loadMore(index);
                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);*/


        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            // getWishListDetails();  //This function is called to getwishlistlitle api.
        } else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, wishlist_name, getResources(), this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager(viewPager);
    }

    //     ***Exp***

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WishlistHomeFragment(),getString(R.string.cap_spaces));
        // ***Experience Start***

        // ***Experience End***

        viewPager.setAdapter(adapter);
    }



    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.wishlist_explore: {
                // This is used to call the WishListDetailsActivity to  HomeActivity
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                x.putExtra("tabsaved", 0);
                overridePendingTransition(0, 0);
                x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(x);
                finish();
            }
            break;
            case R.id.wishlistdetails_close: {
                onBackPressed();
            }
            break;
            case R.id.wishlist_roomfilter: {
                // This is used to call the WishListDetailsActivity to  WishListFilterActivity
                Intent x = new Intent(getApplicationContext(), WishListFilterActivity.class);
                startActivity(x);
            }
            break;
            case R.id.wishlist_name: {
                deleteprivacy = false;
                showDialog();
            }
            break;

            case R.id.menu: {
                //openOptionsMenu();
                PopupMenu popup = new PopupMenu(this, menu, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.wishlist_menu, popup.getMenu());
                privacy = localSharedPreferences.getSharedPreferences(Constants.WishListPrivacy);
                if (privacy.equals("0")) {
                    popup.getMenu().findItem(R.id.item2).setTitle(getResources().getString(R.string.menuonlyme));
                } else {
                    popup.getMenu().findItem(R.id.item2).setTitle(getResources().getString(R.string.menueveryone));
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals(getResources().getString(R.string.delete))) {
                            if (isInternetAvailable) {
                                logout(); // this function is called on logout
                            }else {
                                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, wishlist_name, getResources(), WishListDetailsActivity.this);
                            }
                        } else {
                            if (isInternetAvailable) {
                                deleteprivacy = true;
                                editWishList();// this function is called on editwishListTitle api

                                privacy = localSharedPreferences.getSharedPreferences(Constants.WishListPrivacy);
                                if (privacy.equals("0")) {
                                    item.setTitle(getResources().getString(R.string.menuonlyme));
                                } else {
                                    item.setTitle(getResources().getString(R.string.menueveryone));
                                }
                            }else {
                                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, wishlist_name, getResources(), WishListDetailsActivity.this);
                            }
                        }
                        return true;
                    }
                });


                popup.show();//showing popup menu


            }
            break;

        }
    }

    public Header getHeader() {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("tabsaved", 2);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();*/
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, wishlist_name, getResources(), this);
            return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_GET_WISHLIST:
                if (jsonResp.isSuccess()) {
                    getWishList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
                    wishlist_empty.setVisibility(View.VISIBLE);
                    wishlist_subempty.setVisibility(View.VISIBLE);
                    wishlist_dot_loader.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            case REQ_EDIT_WISHLIST:
                if (jsonResp.isSuccess()) {
                    editWishList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
                }
                break;
            case REQ_DELETE_WISHLIST:
                if (jsonResp.isSuccess()) {
                    if (mydialog.isShowing())
                        mydialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }

                    //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
    }


    public void getWishListDetails() {

        if (!mydialog.isShowing())
            mydialog.show();
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        wishlist_empty.setVisibility(View.VISIBLE);
        wishlist_subempty.setVisibility(View.GONE);
        wishlist_dot_loader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        apiService.getSelectedWishlist(localSharedPreferences.getSharedPreferences(Constants.AccessToken),listid).enqueue(new RequestCallback(REQ_GET_WISHLIST, this));

    }

    public void editWishList() {
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        privacy = localSharedPreferences.getSharedPreferences(Constants.WishListPrivacy);

        if (deleteprivacy) {
            String privacys="0";
            if (privacy.equals("0")) {
               privacys="1";
            } else {
                privacys="0";
            }
            apiService.editWishList(edtWishlistSettings(privacys)).enqueue(new RequestCallback(REQ_EDIT_WISHLIST, this));
        } else {
            listname = newlistname;
            try {
                newlistname = URLEncoder.encode(newlistname, "UTF-8");
                newlistname = newlistname.replace("+"," ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            apiService.editWishList(edtWishlist()).enqueue(new RequestCallback(REQ_EDIT_WISHLIST, this));
        }

    }

    private LinkedHashMap<String ,String > edtWishlistSettings(String privacy){
        LinkedHashMap<String,String > edtSettingsParams= new LinkedHashMap<>();
        edtSettingsParams.put("list_id",listid);
        edtSettingsParams.put("privacy_type",privacy);
        edtSettingsParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return edtSettingsParams;
    }

    /**
     * Edit Name
     * @return
     */
    private LinkedHashMap<String ,String > edtWishlist(){
        LinkedHashMap<String,String > edtParams= new LinkedHashMap<>();
        edtParams.put("list_id",listid);
        edtParams.put("list_name",listname);
        edtParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return edtParams;
    }

    public void deleteWishList() {
        if (!mydialog.isShowing())
            mydialog.show();
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        apiService.deleteWishList(deleteWishlist()).enqueue(new RequestCallback(REQ_DELETE_WISHLIST, this));

    }

    private LinkedHashMap<String ,String > deleteWishlist(){
        LinkedHashMap<String,String > deleteParams= new LinkedHashMap<>();
        deleteParams.put("list_id",listid);
        deleteParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return deleteParams;
    }


    public void getWishList(JsonResponse jsonResponse){
        try {

            if (jsonResponse != null) {

                JSONObject response=new JSONObject(jsonResponse.getStrResponse());
                    JSONArray wishlist = response.getJSONArray("wishlist_details");
                    if (wishlist.length() > 0) {
                        for (int i = 0; i < wishlist.length(); i++) {
                            JSONObject dataJSONObject = wishlist.getJSONObject(i);

                            String room_name = dataJSONObject.getString("space_name");
                            String room_thumb_image = dataJSONObject.getString("space_thumb_image");

                            String currencysymbol = Html.fromHtml(dataJSONObject.getString("currency_symbol")).toString();
                            localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol, currencysymbol);

                            String currency = dataJSONObject.getString("currency_code") + " (" + currencysymbol + ")";
                            localSharedPreferences.saveSharedPreferences(Constants.Currency, currency);
                            localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode, dataJSONObject.getString("currency_code"));

                            Makent_model listdata = new Makent_model();
                            listdata.type = "item";
                            listdata.setExplore_room_image(room_thumb_image);
                            listdata.setRoomid(dataJSONObject.getString("space_id"));
                            listdata.setRoomprice(dataJSONObject.getString("hourly_price"));
                            listdata.setRoomname(dataJSONObject.getString("space_name"));
                            listdata.setRoomrating(dataJSONObject.getString("rating_value"));
                            listdata.setRoomreview(dataJSONObject.getString("reviews_count"));
                            listdata.setRoomiswishlist(dataJSONObject.getString("is_wishlist"));
                            listdata.setRoomcountryname(dataJSONObject.getString("country_name"));
                            listdata.setCurrencycode(dataJSONObject.getString("currency_code"));
                            listdata.setCurrencysymbol(currencysymbol);
                            listdata.setSpaceTypeName(dataJSONObject.getString("space_type_name"));
                            listdata.setSpaceMaxGuestCount(dataJSONObject.getString("number_of_guests"));
                            /*listdata.setRoomlat(dataJSONObject.getString("latitude"));
                            listdata.setRoomlong(dataJSONObject.getString("longitude"));*/
                            listdata.setInstantBook(dataJSONObject.getString("is_instant_book"));

                            searchlist.add(listdata);

                        }
                        adapter.notifyDataChanged();
                        wishlist_empty.setVisibility(View.GONE);
                        wishlist_subempty.setVisibility(View.GONE);
                        wishlist_dot_loader.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        wishlist_empty.setVisibility(View.VISIBLE);
                        wishlist_subempty.setVisibility(View.VISIBLE);
                        wishlist_dot_loader.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();

            wishlist_empty.setVisibility(View.VISIBLE);
            wishlist_subempty.setVisibility(View.VISIBLE);
            wishlist_dot_loader.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void editWishList(JsonResponse jsonResponse){
        localSharedPreferences.saveSharedPreferences(Constants.WishListName, listname);
        if (deleteprivacy) {
            if (privacy.equals("0")) {
                localSharedPreferences.saveSharedPreferences(Constants.WishListPrivacy, "1");
                Snackbar snackbar = Snackbar.make(wishlist_name, getResources().getString(R.string.wish_private), Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                snackbar.show();
            } else {
                localSharedPreferences.saveSharedPreferences(Constants.WishListPrivacy, "0");
                Snackbar snackbar = Snackbar.make(wishlist_name, getResources().getString(R.string.wish_public), Snackbar.LENGTH_LONG);

                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                snackbar.show();
            }
        }
    }
    public void logout() {
        final Dialog dialog = new Dialog(WishListDetailsActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);

        // set the custom dialog components - text, ivPhoto and button
        TextView logout_msg = (TextView) dialog.findViewById(R.id.logout_msg);
        logout_msg.setText(getResources().getString(R.string.delete_msg) + " '" + listname + "'?");
        Button dialogButton = (Button) dialog.findViewById(R.id.logout_cancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogButton1 = (Button) dialog.findViewById(R.id.logout_ok);
        dialogButton1.setText(getResources().getString(R.string.delete));
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteWishList();
            }
        });

        dialog.show();
    }

    public void showDialog() {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.getLayoutDirection();
        lp.setMargins(20, 0, 20, 0);
        input.setLayoutParams(lp);

        String name = localSharedPreferences.getSharedPreferences(Constants.WishListName);
        input.setText(name);

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getString(R.string.wish_list));
        alertDialog.setCancelable(true);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok_val), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                newlistname = input.getText().toString();
                wishlist_name.setText(newlistname);
                deleteprivacy = false;
                editWishList();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel_val), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(input);
        alertDialog.show();
    }



    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}
