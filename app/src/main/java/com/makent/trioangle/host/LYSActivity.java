package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYSActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.RoomTypeAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.Property_Type_model;
import com.makent.trioangle.model.host.Room_Type_model;
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
This is List Your Space  Contain Entire Place,Private Room ,Shared Room
**************************************************************************  */
public class LYSActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;

    ImageView lys_back;
    RelativeLayout lys_title;
    LocalSharedPreferences localSharedPreferences;
    protected boolean isInternetAvailable;
    RecyclerView listView;
    List<Room_Type_model> searchlist;
    List<Property_Type_model> propertylist;
    RoomTypeAdapter adapter;
    Context context;
    String userid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        commonMethods = new CommonMethods();

        lys_back=(ImageView)findViewById(R.id.lys_back);
        commonMethods.rotateArrow(lys_back,this);
        lys_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });


        lys_title=(RelativeLayout) findViewById(R.id.lys_title);
        lys_title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });


        listView = (RecyclerView)findViewById(R.id.room_type_list);
        searchlist = new ArrayList<>();
        propertylist = new ArrayList<>();


        adapter = new RoomTypeAdapter(this, searchlist);



        searchlist.add(new Room_Type_model("load"));
        System.out.println("Search list size"+searchlist.size());
        adapter.notifyItemInserted(searchlist.size()-1);
        System.out.println("Search list size"+searchlist.size());

        // recyclerView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        listView.setAdapter(adapter);



        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
           // new RoomPropertyList().execute(); // this is used to call on reviewsearch api
            roomPropertyList();
        }else {
            snackBar();
        }

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            onSuccessRes(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            //commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,editTextMessage,editTextMessage,getResources(),this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    public void roomPropertyList(){
        String langCode = localSharedPreferences.getSharedPreferences(Constants.LanguageCode);
        //apiService.roomproperty(localSharedPreferences.getSharedPreferences(Constants.AccessToken),langCode).enqueue(new RequestCallback(this));
    }

    public void onSuccessRes(JsonResponse jsonResp){
        try{
            JSONObject response = new JSONObject(jsonResp.getStrResponse());
            if (searchlist.size() > 0)
                searchlist.remove(searchlist.size() - 1);
            localSharedPreferences.saveSharedPreferences(Constants.PropertyListJSON,response.toString());
            JSONArray data = response.getJSONArray("room_type");
            JSONArray data1 = response.getJSONArray("property_type");
            JSONArray bettype = response.getJSONArray("bed_type");
            localSharedPreferences.saveSharedPreferences(Constants.Bedtype,bettype.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Roomtype,data.toString());
            localSharedPreferences.saveSharedPreferences(Constants.Propertytype,data1.toString());
            adapter.notifyItemInserted(0);

            for (int i = 0; i < data.length(); i++) {
                JSONObject dataJSONObject = data.getJSONObject(i);
                Room_Type_model listdata = new Room_Type_model();
                listdata.type="item";
                listdata.setName(dataJSONObject.getString("name"));
                listdata.setDesc(dataJSONObject.getString("description"));
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setIsShared(dataJSONObject.getString("is_shared"));
                listdata.setImage(dataJSONObject.getString("image_name"));
                listdata.setType("room_type");
                searchlist.add(listdata);
            }

            for (int i = 0; i < data1.length(); i++) {
                JSONObject dataJSONObject = data1.getJSONObject(i);
                Property_Type_model listdata = new Property_Type_model();
                listdata.type="item";
                listdata.setName(dataJSONObject.getString("name"));
                listdata.setDesc(dataJSONObject.getString("description"));
                listdata.setId(dataJSONObject.getString("id"));
                listdata.setImage(dataJSONObject.getString("image_name"));
                listdata.setType("property_type");
                propertylist.add(listdata);

            }



            adapter.notifyDataChanged();
        }catch (JSONException j){
            j.printStackTrace();
        }


    }

    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(lys_back, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable){
            //textViewTop.setText(statusmessage);
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
