package com.makent.trioangle.travelling;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.WishlistDetailAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;

import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;


import static com.makent.trioangle.util.Enums.REQ_GET_WISHLIST;

/**
 * Created by trioangle on 6/23/18.
 */


public class WishlistHomeFragment extends Fragment implements ServiceListener{

    public @Inject
    ApiService apiService;

    protected boolean isInternetAvailable;
    RecyclerView recyclerView;
    List<Makent_model> searchlist;
    LinearLayout wishlist_empty, wishlist_subempty;
    LocalSharedPreferences localSharedPreferences;
    String userid, listid;
    Dialog_loading mydialog;
    public @Inject
    CommonMethods commonMethods;

    TextView nothing;

    WishlistDetailAdapter adapter;

    RelativeLayout explore_filter;
    ImageView wishlist_dot_loader;

    private Snackbar snackbar;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_wishlist_home, container, false);


        ButterKnife.bind(getActivity());
        AppController.getAppComponent().inject(this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.wishlist_roomlistView);
        searchlist = new ArrayList<>();
        localSharedPreferences = new LocalSharedPreferences(getContext());
        nothing = (TextView) rootView.findViewById(R.id.nothing);
        mydialog = new Dialog_loading(getContext());
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);


        adapter = new WishlistDetailAdapter(getHeader(), getActivity(), getContext(), searchlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        wishlist_empty = (LinearLayout) rootView.findViewById(R.id.wishlist_empty);
        wishlist_subempty = (LinearLayout) rootView.findViewById(R.id.wishlist_subempty);
        wishlist_dot_loader = (ImageView) rootView.findViewById(R.id.wishlist_dot_loader);
        wishlist_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(wishlist_dot_loader);
        Glide.with(getContext()).load(R.drawable.dot_loading).into(imageViewTarget1);


        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            // new getWishListTitle().execute();  //This function is called to getwishlistlitle api.
            getWishListDetails();
        } else {
            snackBar();
        }


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void getWishListDetails() {

        if (!mydialog.isShowing())
            mydialog.show();

        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        wishlist_empty.setVisibility(View.VISIBLE);
        wishlist_subempty.setVisibility(View.GONE);
        wishlist_dot_loader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        apiService.getSelectedWishlist(localSharedPreferences.getSharedPreferences(Constants.AccessToken), listid).enqueue(new RequestCallback(REQ_GET_WISHLIST, this));

    }

    public Header getHeader() {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
//            if (myDialog.isShowing()) {
//                myDialog.dismiss();
//            }
        try {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            mydialog.dismiss();
        }

        // Handle or log or ignore

        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, nothing, getResources(), getActivity());
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
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, nothing, getResources(), getActivity());
                    wishlist_empty.setVisibility(View.VISIBLE);
                    wishlist_subempty.setVisibility(View.VISIBLE);
                    wishlist_dot_loader.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
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
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, nothing, getResources(), getActivity());
    }


    public void getWishList(JsonResponse jsonResponse){
        try {

            if (jsonResponse != null) {

                JSONObject response=new JSONObject(jsonResponse.getStrResponse());
                JSONArray wishlist = response.getJSONArray("wishlist_details");

                localSharedPreferences.saveSharedPreferences(Constants.WishListHomeCount,Integer.toString(wishlist.length()));

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


    /*//Calling wishlist api

    public  class getWishListTitle extends AsyncTask<Void, Void, String> {
        String statuscode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            wishlist_empty.setVisibility(View.GONE);
            wishlist_subempty.setVisibility(View.GONE);
            wishlist_dot_loader.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        protected  String  doInBackground(Void... params){

            Webservice service = new Webservice(getContext());
            String url="get_particular_wishlist?list_id="+listid+"&token="+userid;
            url=url.replaceAll(" ","%20");
            service.makeJsonObjReq(url,new Webservice.VolleyCallback(){
                @Override
                public void onSuccess(JSONObject response){

                    System.out.println("resultTemp from url in email check "+response);
                    // Parsing json
                    try {

                        if (response != null) {
                            statuscode = response.getString("status_code");
                            String statusmessage = response.getString("success_message");

                            Log.d("OUTPUT IS", statuscode);
                            Log.d("OUTPUT IS", statusmessage);

                            // JSONArray wishlist
                            if (statuscode.matches("1")) {

                                JSONArray wishlist = response.getJSONArray("wishlist_details");
                                if(wishlist.length()>0) {
                                    for (int i = 0; i < wishlist.length(); i++) {
                                        JSONObject dataJSONObject = wishlist.getJSONObject(i);

                                        String room_thumb_image = dataJSONObject.getString("room_thumb_image");

                                        String currencysymbol= Html.fromHtml(dataJSONObject.getString("currency_symbol")).toString();
                                        localSharedPreferences.saveSharedPreferences(Constants.CurrencySymbol,currencysymbol);

                                        String  currency=dataJSONObject.getString("currency_code")+" ("+currencysymbol+")";
                                        localSharedPreferences.saveSharedPreferences(Constants.Currency,currency);
                                        localSharedPreferences.saveSharedPreferences(Constants.CurrencyCode,dataJSONObject.getString("currency_code"));

                                        Makent_model listdata = new Makent_model();
                                        listdata.type="item";
                                        listdata.setExplore_room_image(room_thumb_image);
                                        listdata.setRoomid(dataJSONObject.getString("room_id"));
                                        listdata.setRoomprice(dataJSONObject.getString("room_price"));
                                        listdata.setRoomname(dataJSONObject.getString("room_name"));
                                        listdata.setRoomrating(dataJSONObject.getString("rating_value"));
                                        listdata.setRoomreview(dataJSONObject.getString("reviews_count"));
                                        listdata.setRoomiswishlist(dataJSONObject.getString("is_wishlist"));
                                        listdata.setRoomcountryname(dataJSONObject.getString("country_name"));
                                        listdata.setCurrencycode(dataJSONObject.getString("currency_code"));
                                        listdata.setCurrencysymbol(currencysymbol);
                                        listdata.setRoomlat(dataJSONObject.getString("latitude"));
                                        listdata.setRoomlong(dataJSONObject.getString("longitude"));
                                        listdata.setRoomtype(dataJSONObject.getString("room_type"));
                                        listdata.setInstantBook(dataJSONObject.getString("instant_book"));

                                        System.out.println("checking incoming errr ");
                                        searchlist.add(listdata);

                                    }

                                    adapter = new WishlistDetailAdapter(getHeader(),getActivity(),getContext(), searchlist,"home");
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                    recyclerView.setAdapter(adapter);


                                    adapter.notifyDataChanged();


                                    wishlist_empty.setVisibility(View.GONE);
                                    wishlist_subempty.setVisibility(View.GONE);
                                    wishlist_dot_loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);



                                }else
                                {
                                    wishlist_empty.setVisibility(View.VISIBLE);
                                    wishlist_subempty.setVisibility(View.VISIBLE);
                                    wishlist_dot_loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } else {
                                wishlist_empty.setVisibility(View.VISIBLE);
                                wishlist_subempty.setVisibility(View.VISIBLE);
                                wishlist_dot_loader.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                        wishlist_empty.setVisibility(View.GONE);
                        wishlist_subempty.setVisibility(View.VISIBLE);
                        wishlist_dot_loader.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });

            return null;

        }

        @Override
        protected void onPostExecute(String user) {

            super.onPostExecute(user);


        }
    }*/


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(getContext());
        return connectionDetector;
    }


    public void snackBar() {
        // Create the Snackbar
        snackbar = Snackbar.make(rootView.findViewById(R.id.rlt_whole), "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getActivity().getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                }else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable) {
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
            button.setVisibility(View.VISIBLE);
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }

}