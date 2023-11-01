package com.makent.trioangle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.explore.ExploreDataModel;
import com.makent.trioangle.model.explore.ExploreResult;
import com.makent.trioangle.travelling.FilterActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.MapActivity;

import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by trioangle on 6/7/18.
 */

public class HomeFragment extends Fragment implements ServiceListener {
    TextView explorefiltercount;

    int totalpages=1;
    LocalSharedPreferences localSharedPreferences;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public @Inject
    CustomDialog customDialog;
    ExploreResult exploreResult;
    RecyclerView recyclerView;

    int filtercount=0;
    Button explore_map;
    protected boolean isInternetAvailable;
    public String searchlocationcheck = "";

    String searchinstantbook,searchamenities,searchroomtypes,searchbeds,searchbedrooms,searchbathrooms,searchminprice,searchmaxprice;

    ArrayList<ExploreDataModel> exploreDataModel = new ArrayList<>();

    ArrayList<ExploreDataModel> searchlist;
    LinearLayoutManager linearLayoutManager;
    String pageno,userid;
    LinearLayout mapfilter,explorenodata;
    RelativeLayout explore_filter;

    private AlertDialog dialog;

    String searchguest,searchlocation,searchcheckin,searchcheckout;
    private Button explore_remove;
    private Snackbar snackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_exploresearch_home, container, false);
        ButterKnife.bind(getActivity());
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(getContext());
        mapfilter = (LinearLayout) rootView.findViewById(R.id.mapfilter);
        explorenodata = (LinearLayout) rootView.findViewById(R.id.explore_nodata);
        explore_filter = (RelativeLayout) rootView.findViewById(R.id.explore_filter);
        explorefiltercount=(TextView) rootView.findViewById(R.id.explore_filter_count);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        //  swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.explore_room_list);
        Typeface font4 = Typeface.createFromAsset( getActivity().getAssets(), getResources().getString(R.string.fonts_makent4) );

        explore_map = (Button) rootView.findViewById(R.id.explore_map);
        Drawable icon3 = new FontIconDrawable(getContext(),getResources().getString(R.string.f4searchmap),font4)
                .sizeDp(40)
                .colorRes(R.color.text_black);
        explore_map.setCompoundDrawables(null, null, icon3, null);
        explore_map.setCompoundDrawablePadding(20);
        explore_map = (Button) rootView.findViewById(R.id.explore_map);

        // On Click function used to click action for check Email id in server send link to Email
        explore_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                localSharedPreferences.saveSharedPreferences(Constants.WishlistRoomExp,"Rooms");
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(exploreDataModel);
                editor.putString("explore", json);
                editor.apply();

                Intent x = new Intent(getContext(), MapActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("searchlist",(Serializable)exploreDataModel);
                x.putExtra("BUNDLE",args);
                startActivity(x);

            }
        });

        searchlist = new ArrayList<>();
        localSharedPreferences=new LocalSharedPreferences(getContext());
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        searchguest=localSharedPreferences.getSharedPreferences(Constants.SearchGuest);
        searchlocation=localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        searchcheckin=localSharedPreferences.getSharedPreferences(Constants.SearchCheckIn);
        searchcheckout=localSharedPreferences.getSharedPreferences(Constants.SearchCheckOut);
        searchlocationcheck = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);

        searchinstantbook=localSharedPreferences.getSharedPreferences(Constants.FilterInstantBook);
        searchamenities=localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);
        searchroomtypes=localSharedPreferences.getSharedPreferences(Constants.FilterRoomTypes);
        searchbeds=localSharedPreferences.getSharedPreferences(Constants.FilterBeds);
        searchbedrooms=localSharedPreferences.getSharedPreferences(Constants.FilterBedRoom);
        searchbathrooms=localSharedPreferences.getSharedPreferences(Constants.FilterBathRoom);
        searchminprice=localSharedPreferences.getSharedPreferences(Constants.FilterMinPriceCheck);
        searchmaxprice=localSharedPreferences.getSharedPreferences(Constants.FilterMaxPriceCheck);

        String anywhere, nearby;
        anywhere = getResources().getString(R.string.anywhere);
        nearby = getResources().getString(R.string.nearby);
        if (searchlocationcheck != null) {
            if (searchlocationcheck.equals(anywhere)) {
                searchlocation = null;
            } else if (searchlocationcheck.equals(nearby)) {
                searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocationNearby);
            }
        }


        //adapter = new ExploreSearchListAdapter(getActivity(),getContext(), exploreDataModel);

         //recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));


        pageno="1";
        if (isInternetAvailable)

        {
            exploreSearch();
           // new ExploreSearch().execute(pageno2);
        } else {
            //snackBar();
            commonMethods.showMessage(getContext(), dialog, getResources().getString(R.string.Interneterror));
        }

        if(searchinstantbook!=null)
        {
            filtercount=filtercount+1;
        }
        if(searchamenities!=null)
        {
            filtercount=filtercount+1;
        }
        if(searchminprice!=null&&searchmaxprice!=null)
        {
            filtercount=filtercount+1;
        }
        if(searchroomtypes!=null)
        {
            filtercount=filtercount+1;
        }
        if(searchbeds!=null||searchbedrooms!=null||searchbathrooms!=null)
        {
            filtercount=filtercount+1;
        }

        if(filtercount==0)
        {
            explorefiltercount.setBackground(getResources().getDrawable(R.drawable.n2_ic_filters));
            explorefiltercount.setText("");
        }else
        {
            explorefiltercount.setBackground(getResources().getDrawable(R.drawable.d_blue_round));
            explorefiltercount.setText(Integer.toString(filtercount));
        }

        explore_filter = (RelativeLayout) rootView.findViewById(R.id.explore_filter);

        // On Click function used to click action for check Email id in server send link to Email
        explore_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                Intent x = new Intent(getContext(), FilterActivity.class);
                startActivity(x);
                //getActivity().finish();

            }
        });

        explorenodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                clearSavedData();
                Intent x=new Intent(getContext(),HomeActivity.class);
                startActivity(x);


            }
        });


        explore_remove=(Button) rootView.findViewById(R.id.explore_remove);
        explore_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                if (TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
                    localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
                }
                clearSavedData();
                Intent x=new Intent(getContext(),HomeActivity.class);
                startActivity(x);
            }
        });

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        searchlist.add(new ExploreDataModel("load"));
        exploreDataModel.add(new ExploreDataModel("load"));
        System.out.println("Search guestList size"+searchlist.size());

        System.out.println("Search guestList size"+searchlist.size());

        return rootView;
    }
    private void loadMore(int index) {

        if (index > 1) {
            exploreDataModel.add(new ExploreDataModel("load"));
        }

        pageno = Integer.toString(index);
        exploreSearch();

    }
    @Override
    public void onResume() {
        super.onResume();
        /*if (isInternetAvailable){
            //new ExploreSearch().execute();
            exploreSearch();
        }else {
            snackBar();
        }*/

    }
    public void exploreSearch() {
        mapfilter.setVisibility(View.GONE);
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        //apiService.explore(localSharedPreferences.getSharedPreferences(Constants.AccessToken), pageno, searchguest, searchlocation, searchcheckin, searchcheckout, searchinstantbook, searchamenities, searchminprice, searchmaxprice, searchroomtypes, searchbeds, searchbedrooms, searchbathrooms,langCode).enqueue(new RequestCallback(this));
    }

    public void clearSavedData()
    {
        localSharedPreferences.saveSharedPreferences(Constants.SearchGuest,null);
        localSharedPreferences.saveSharedPreferences(Constants.RoomGuest,null);
        //localSharedPreferences.saveSharedPreferences(Constants.SearchLocation,null);
        localSharedPreferences.saveSharedPreferences(Constants.searchlocationAddress,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckIn,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckOut,null);
        localSharedPreferences.saveSharedPreferences(Constants.SearchCheckInOut,null);

        localSharedPreferences.saveSharedPreferences(Constants.CheckIn, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckOut, null);
        localSharedPreferences.saveSharedPreferences(Constants.CheckInOut,null);

        localSharedPreferences.saveSharedPreferences(Constants.isRequestCheck,"0");
        localSharedPreferences.saveSharedPreferences(Constants.FilterInstantBook,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterRoomTypes,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBeds,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBedRoom,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterBathRoom,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPriceCheck,null);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPriceCheck,null);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        mapfilter.setVisibility(View.VISIBLE);
        if (jsonResp.isSuccess()) {
            onSuccessExplore(jsonResp); // onSuccess call method
        }
        else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()))
        {
            if (pageno.equals("1")) {
                if (exploreDataModel.size() > 0) {
                    exploreDataModel.remove(exploreDataModel.size() - 1);

                }
                explorenodata.setVisibility(View.VISIBLE);
                mapfilter.setVisibility(View.GONE);
            } else {
                if (exploreDataModel.size() > 0)
                    exploreDataModel.remove(exploreDataModel.size() - 1);
                explorenodata.setVisibility(View.GONE);

                Snackbar snackbar = Snackbar.make(explore_filter, "No more data available...", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                snackbar.show();

            }
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    public void onSuccessExplore(JsonResponse jsonResp) {
        exploreResult = gson.fromJson(jsonResp.getStrResponse(), ExploreResult.class);
        mapfilter.setVisibility(View.VISIBLE);
        String total_page = exploreResult.getTotalPage();
        totalpages = Integer.parseInt(total_page);

        String min_price = exploreResult.getMinPrice();
        String max_price = exploreResult.getMaxPrice();

        localSharedPreferences.saveSharedPreferences(Constants.FilterMinPrice, min_price);
        localSharedPreferences.saveSharedPreferences(Constants.FilterMaxPrice, max_price);
        if (exploreDataModel.size() > 0) exploreDataModel.remove(exploreDataModel.size() - 1);

        ArrayList<ExploreDataModel> expDatamodel = exploreResult.getExploreDataModels();
        exploreDataModel.addAll(expDatamodel);
        for (int i = 0; i < exploreDataModel.size(); i++) {
            exploreDataModel.get(i).setType("item");
            System.out.println("exploreDataModel.get(i).getCurrencySymbol() "+exploreDataModel.get(i).getCurrencySymbol());
            String currencysymbol = Html.fromHtml(exploreDataModel.get(i).getCurrencySymbol()).toString();
            exploreDataModel.get(i).setCurrencySymbol(currencysymbol);
            System.out.println("Success incoming check "+exploreDataModel.get(i).getType()+" "+exploreDataModel.get(i).getRoomId());
        }

    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(getContext());
        return connectionDetector;
    }

    public void snackBar() {
        // Create the Snackbar
        snackbar = Snackbar.make(explore_map, "", Snackbar.LENGTH_INDEFINITE);

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
                //     new ExploreSearch().execute();
                    exploreSearch();
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