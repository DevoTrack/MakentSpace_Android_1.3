package com.makent.trioangle.travelling.tabs;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestSavedActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.SavedWishListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.wishlist.WishListData;
import com.makent.trioangle.model.wishlist.WishListResult;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SavedActivity extends ActivityGroup implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;


    private SwipeRefreshLayout swipeContainer;
    RecyclerView savedwishlist;
    SavedWishListAdapter listAdapter;
    Snackbar snackbar;
    String roomImgItems[] = {"http://makentspace.trioangle.com/images/home_cities/home_city_1461439512.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439589.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439550.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439621.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439819.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439887.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439922.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439957.jpg"};
    private int backPressed = 0;    // used by onBackPressed()
    LinearLayout savedempty;
    TextView saved_explore;
    ImageView saved_dot_loader;
    LocalSharedPreferences localSharedPreferences;
    String userid;
    public WishListResult wishListResult;
    public ArrayList <WishListData> wishListData=new ArrayList<>();


    public View viewNotoken;
    public TextView tvTitle,tvdescription;
    public Button btnNoTokenLogin;
    public ImageView ivNoToken;

    public ImageView ivBack;
    public TextView tvHeaderTitle;

    protected boolean isInternetAvailable;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        Log.e("SavedWishlist","SavedWishlist");

        saved_dot_loader = (ImageView) findViewById(R.id.saved_dot_loader);
        saved_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(saved_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        savedwishlist = (RecyclerView) findViewById(R.id.wishlist);
        savedempty = (LinearLayout) findViewById(R.id.savedempty);
        ivBack = (ImageView) findViewById(R.id.iv_Back);
        tvHeaderTitle = (TextView) findViewById(R.id.tv_header_title);

        viewNotoken = (View) findViewById(R.id.viewNotoken);
        btnNoTokenLogin = (Button) findViewById(R.id.btnNoTokenLogin);
        saved_explore = (TextView) findViewById(R.id.saved_explore);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvdescription = (TextView) findViewById(R.id.tvdescription);
        ivNoToken = (ImageView) findViewById(R.id.ivNoToken);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvHeaderTitle.setText(getResources().getString(R.string.wishlisted));

        btnNoTokenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                home.putExtra("isback",1);
                startActivity(home);
            }
        });

        saved_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                x.putExtra("tabsaved", 0);
                startActivity(x);
                finish();
            }
        });

        if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {

            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
            viewNotoken.setVisibility(View.GONE);
            savedwishlist.setHasFixedSize(true);
            savedwishlist.setNestedScrollingEnabled(false);

            listAdapter = new SavedWishListAdapter(getHeader(), this, this, wishListData);


            savedwishlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
            savedwishlist.setAdapter(listAdapter);
            savedwishlist.setNestedScrollingEnabled(false);

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    listAdapter.clear();

                    if (isInternetAvailable) {
                        getWishList();
                    } else {
                        snackBar();
                        // commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, saved_explore, getResources(), this);
                    }
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

            // api = ServiceGenerator.createService(MoviesApi.class);
            // load1(0);
            if (isInternetAvailable) {
                getWishList();
            } else {
                snackBar();
               // commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, saved_explore, getResources(), this);
            }
        }else{
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"saved");
            viewNotoken.setVisibility(View.VISIBLE);
            tvTitle.setText(getResources().getString(R.string.saved));
            tvdescription.setText(getResources().getString(R.string.no_token_saved));
            ivNoToken.setImageDrawable(getResources().getDrawable(R.drawable.token_saved));
        }
    }
    public Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
        if (jsonResp.isSuccess()) {
            onSuccessWish(jsonResp); // onSuccess call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            savedwishlist.setVisibility(View.GONE);
            savedempty.setVisibility(View.VISIBLE);
            saved_dot_loader.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
    }

    public void getWishList(){
        savedwishlist.setVisibility(View.GONE);
        savedempty.setVisibility(View.GONE);
        saved_dot_loader.setVisibility(View.VISIBLE);
        apiService.getWishlist(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
    }

    public void onSuccessWish(JsonResponse jsonResp){
        wishListResult =gson.fromJson(jsonResp.getStrResponse(), WishListResult.class);

        ArrayList <WishListData> wish = wishListResult.getWishListData();
       // wishListData.add(new WishListData("0"));
        wishListData.addAll(wish);
        System.out.println("wishListData"+wishListData.size());
        for (int i=0;i<wishListData.size();i++){
            System.out.println("wishListData"+wishListData.get(i).getListName());
        }

            swipeContainer.setRefreshing(false);
        listAdapter.notifyDataChanged();
        savedwishlist.setVisibility(View.VISIBLE);
        savedempty.setVisibility(View.GONE);
        saved_dot_loader.setVisibility(View.GONE);

    }




    /*public void onBackPressed()
    {
        if (backPressed >=1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
            // clean up
            backPressed = backPressed + 1;
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }*/
    public void onBackPressed(){
        super.onBackPressed();
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }




    @Override
    protected void onResume() {
        super.onResume();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            //new getWishListTitle().execute();
        } else {
            snackBar();
           // commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,saved_explore,getResources(),this);
        }
    }
    public void snackBar()
    {
        // Create the Snackbar
        snackbar = Snackbar.make(savedempty, "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
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
                    if (localSharedPreferences.getSharedPreferences(Constants.AccessToken)!=null) {
                        getWishList();
                    }
                    // new UpdateDateDetails().execute();
                }else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable){
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
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
