package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestWishListChooseDialogActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.WishListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
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

import static com.makent.trioangle.helper.Constants.ChooseWishListType;

/* ***********************************************************************
This is WishListCreate Page Contain Create a list
**************************************************************************  */

public class WishListChooseDialog extends Dialog implements android.view.View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public Activity c;
    RecyclerView recyclerView;
    protected boolean isInternetAvailable;
    List<Makent_model> searchlist;
    WishListAdapter listAdapter;
    LocalSharedPreferences localSharedPreferences;
    String userid,roomid;
    ImageView chooselist_dot_loader;
    private String isFrom="";
    private int position;
    public WishListChooseDialog(Activity a) {
        super(a,R.style.DialogTheme);
        // TODO Auto-generated constructor stub
        System.out.println("Activity a "+a.getClass().getSimpleName());
        this.c = a;
    }

    public WishListChooseDialog(@NonNull Activity a, String isFrom,int position) {
        super(a,R.style.DialogTheme);
        // TODO Auto-generated constructor stub
        System.out.println("Activity a "+a.getClass().getSimpleName());
        this.c = a;
        this.isFrom = isFrom;
        this.position = position;
    }

    ImageView createwishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wishlist_choose_item);
        Window window = getWindow();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences=new LocalSharedPreferences(c.getApplicationContext());
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        roomid=localSharedPreferences.getSharedPreferences(Constants.WishlistRoomId);

        chooselist_dot_loader = (ImageView) findViewById(R.id.chooselist_dot_loader);
        chooselist_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(chooselist_dot_loader);
        Glide.with(c.getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

       WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        wlp.dimAmount=0.65f;
        wlp.width=WindowManager.LayoutParams.MATCH_PARENT;

        wlp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);



        createwishlist = (ImageView) findViewById(R.id.createwishlist);

        createwishlist.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.wishlist_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        searchlist = new ArrayList<>();
        listAdapter = new WishListAdapter(this,c,c, searchlist,isFrom,position);
        listAdapter.setLoadMoreListener(new WishListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = searchlist.size() - 1;
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(listAdapter);

        getWishListDetails();

    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(getContext());
        return connectionDetector;
    }

    public void getWishListDetails() {

        recyclerView.setVisibility(View.INVISIBLE);
        chooselist_dot_loader.setVisibility(View.VISIBLE);
        createwishlist.setVisibility(View.INVISIBLE);
        if (isInternetAvailable) {
            apiService.getWishlist(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
        }else {
            commonMethods.snackBar(getContext().getResources().getString(R.string.interneterror),"",false,2,chooselist_dot_loader,getContext().getResources(),c);
        }

    }

    public void getWishList(JsonResponse jsonResponse){
        try {

            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            JSONArray wishlist = response.getJSONArray("wishlist_data");
            for (int i = 0; i < wishlist.length(); i++) {
                Makent_model listdata = new Makent_model();
                JSONObject wishlistdetails = wishlist.getJSONObject(i);

                listdata.setWishlistId(wishlistdetails.getString("list_id"));
                listdata.setWishlistName(wishlistdetails.getString("list_name"));
                listdata.setWishlistPrivacy(wishlistdetails.getString("privacy"));
                JSONArray whishlistimages = wishlistdetails.getJSONArray("space_thumb_images");

                String listimages=null;
                listdata.setWishlistImage(whishlistimages.toString());
                searchlist.add(listdata);
            }
            listAdapter.notifyDataChanged();
            createwishlist.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            chooselist_dot_loader.setVisibility(View.GONE);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
            return;
        }
        System.out.println("Checking in one : ");

        if (jsonResp.isSuccess()) {
            getWishList(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            createwishlist.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            chooselist_dot_loader.setVisibility(View.GONE);
            dismiss();

            Intent x =new Intent(c,WishListCreateActivity.class);
            c.startActivity(x);


        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createwishlist:
                Intent x =new Intent(c,WishListCreateActivity.class);
                x.putExtra("isFrom",isFrom);
                x.putExtra("position",position);
                c.startActivity(x);
                break;
            default:
                break;
        }
        dismiss();
        String isWish = localSharedPreferences.getSharedPreferences(Constants.WishFromExp);


        if(isWish!=null&&!isWish.equals("")&&isWish.equals("Yes")){
            localSharedPreferences.saveSharedPreferences(Constants.WishDismissExp,"Yes");
            localSharedPreferences.saveSharedPreferences(Constants.WishFromExp,"Yes");
        }
        isWish = localSharedPreferences.getSharedPreferences(Constants.WishFromExp);
        System.out.println("isWish Two : "+isWish);

    }


}
