package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestReviewActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.ReviewsListAdapter;
import com.makent.trioangle.controller.AppController;
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

/* ***********************************************************************
This is Review Page Contain Review the details
**************************************************************************  */
public class ReviewActivityHome extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RecyclerView listView;
    List<Makent_model> searchlist;
    ReviewsListAdapter adapter;
    Context context;

    TextView review_close;

    String pageno;
    int index=1;
    int checkindex=0;
    String userid,roomid;
    LocalSharedPreferences localSharedPreferences;
    protected boolean isInternetAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        commonMethods = new CommonMethods();
        localSharedPreferences=new LocalSharedPreferences(this);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        listView = (RecyclerView)findViewById(R.id.review_list);
        searchlist = new ArrayList<>();
        adapter = new ReviewsListAdapter(getHeader(),this,this, searchlist);
        adapter.setLoadMoreListener(new ReviewsListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                listView.post(new Runnable() {
                    @Override
                    public void run() {

                        if(index>0) {

                            checkindex=checkindex+1;
                            if(checkindex>1) {
                                index = index + 1;
                                loadMore(index); //this function call listview index postion
                            }
                        }
                    }
                });
            }
        });


        searchlist.add(new Makent_model("load"));
        adapter.notifyItemInserted(searchlist.size()-1);

        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);


        pageno="1";
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            getReviews(); // this is used to call on reviewsearch api
        }else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, listView, getResources(), this);
        }



        review_close=(TextView)findViewById(R.id.review_close);
        commonMethods.setTvAlign(review_close,this);
// On Click function used to click action for check Email id in server send link to Email
        review_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    private void loadMore(int index) {

        if(index>1) {
            searchlist.add(new Makent_model("load"));
            adapter.notifyItemInserted(searchlist.size() - 1);
        }

        pageno=Integer.toString(index);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            getReviews();

        }else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, listView, getResources(), this);
        }
    }

    /**
     * @Reference Get reviews from server
     */
    public void getReviews(){
        apiService.getReviewDetails(localSharedPreferences.getSharedPreferences(Constants.AccessToken),roomid,pageno).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, listView, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
            if (searchlist.size() > 0)
                searchlist.remove(searchlist.size() - 1);
            getReviews(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            adapter.setMoreDataAvailable(false);
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, listView, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
    }

    public void getReviews(JsonResponse jsonResponse){
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            String ratingValue = response.getString("rating");
            String total_review,accuracy_value,check_in_value,cleanliness_value,communication_value,location_value,value;
            total_review = response.getString("total_review");
            accuracy_value = response.getString("accuracy");
            check_in_value = response.getString("check_in");
            cleanliness_value = response.getString("cleanliness");
            communication_value = response.getString("communication");
            location_value = response.getString("location");
            value = response.getString("value");

            JSONArray data = response.getJSONArray("data");

            String total_count=Integer.toString(data.length());

            String review_rate=total_count+","+total_review+","+accuracy_value+","+check_in_value+","+cleanliness_value+","+communication_value+","+location_value+","+value+","+ratingValue;
            localSharedPreferences.saveSharedPreferences(Constants.RoomReviewRate,review_rate);
            String reviewrate=localSharedPreferences.getSharedPreferences(Constants.RoomReviewRate);
            System.out.println("Review Rate"+reviewrate);
            searchlist.add(new Makent_model("head"));
            System.out.println("Search list size"+searchlist.size());
            adapter.notifyItemInserted(0);
            for (int i = 0; i < data.length(); i++) {


                JSONObject dataJSONObject = data.getJSONObject(i);


                Makent_model listdata = new Makent_model();
                listdata.type="item";
                listdata.setReviewUserName(dataJSONObject.getString("review_user_name"));
                listdata.setReviewUserImage(dataJSONObject.getString("review_user_image"));
                listdata.setReviewDate(dataJSONObject.getString("review_date"));
                listdata.setReviewMessage(dataJSONObject.getString("review_message"));
                searchlist.add(listdata);


            }
            adapter.notifyDataChanged();
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
