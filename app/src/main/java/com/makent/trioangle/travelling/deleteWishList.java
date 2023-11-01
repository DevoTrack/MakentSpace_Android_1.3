package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestdeleteWishListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.gson.Gson;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_DELETE_WISHLIST;

public  class deleteWishList extends AsyncTask<Void, Void, String> implements ServiceListener{
    String userid,roomid;
    Context mContext;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    CoordinatorLayout coordinatorLayout;

    LocalSharedPreferences localSharedPreferences;
    WishAddRemoveDialog wishAddRemoveDialog;
    private String roomExp;

    public deleteWishList (Context context){
        mContext = context;
        localSharedPreferences=new LocalSharedPreferences(context);
        //wishAddRemoveDialog = new WishAddRemoveDialog((Activity) context);
        ButterKnife.bind((Activity) mContext);
        AppController.getAppComponent().inject(this);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
            /*if (!myDialog.isShowing())
                myDialog.show();*/

    }

    protected  String  doInBackground(Void... params){

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.WishlistRoomId);

        String wishtitle = localSharedPreferences.getSharedPreferences(Constants.WishTitle);
        deleteWishList();



//        Snackbar snackbar = Snackbar
//                .make(coordinatorLayout,mContext.getResources().getString(R.string.removefrom) + " " + wishtitle , Snackbar.LENGTH_LONG)
//                .setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
////                        snackbar1.show();
//                    }
//                });
//
//        snackbar.show();


        //commonMethods.snackBar(mContext.getResources().getString(R.string.invalidelogin), mContext.getResources().getString(R.string.showpassword), true, 2, wishtitle, wishtitle, mContext.getResources(), this);
       // commonMethods.snackBar(mContext.getResources().getString(R.string.removefrom),mContext.getResources().getString(R.string.undo),true,2,wishtitle,signup_email,mContext.getResources(), this);
        return null;

    }

    @Override
    protected void onPostExecute(String user) {

        super.onPostExecute(user);


    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public void deleteWishList() {
        apiService.deleteWishList(deleteParticularList()).enqueue(new RequestCallback(REQ_DELETE_WISHLIST, this));
    }

    private LinkedHashMap<String,String> deleteParticularList(){
        LinkedHashMap<String,String> deleteSpaceWishListParams= new LinkedHashMap<>();
        deleteSpaceWishListParams.put("space_id",roomid);
        deleteSpaceWishListParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return deleteSpaceWishListParams;
    }
}
