package com.makent.trioangle.host.optionaldetails;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails
 * @category    OD_AmenitiesNew
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.ODAmenitiesAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.retrofithost.OdAmenitiesArray;
import com.makent.trioangle.model.retrofithost.OdAmenitiesModel;
import com.makent.trioangle.model.retrofithost.OdAmenitiesUpdate;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.util.Enums.REQ_AMENITIES;
import static com.makent.trioangle.util.Enums.REQ_UPDATE_AMENITIES;

/* ************************************************************
Optional Details Amenities list
*************************************************************** */
public class OD_AmenitiesNew extends AppCompatActivity implements ServiceListener {

    RecyclerView listView;
    List<Makent_model> searchlist;
    List<OdAmenitiesArray> amenitiesList;

    ODAmenitiesAdapter adapter;
    Context context;
    JSONArray amenities_list;

    String userid,roomid,listamenities;
    List<String> searchamenities = new ArrayList<String>();
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    ImageView odamenities_dot_loader;
    TextView odamenities_save;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout odamenities_title,odamenitie_title;
    private OdAmenitiesModel odAmenitiesModel;

    private OdAmenitiesUpdate odAmenitiesUpdate;


    private ArrayList<OdAmenitiesArray> odAmenitiesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_od__amenities_new);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        odamenities_dot_loader = (ImageView) findViewById(R.id.odamenitie_dot_loader);
        odamenities_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(odamenities_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);



        localSharedPreferences=new LocalSharedPreferences(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        listamenities= localSharedPreferences.getSharedPreferences(Constants.ListAmenities);


        if(listamenities!=null&&!listamenities.equals("null")&&!listamenities.equals(""))
        {
            String[] split = listamenities.split(",");
            for(int i=0;i<split.length;i++) {
                if(!searchamenities.contains(split[i])) {
                    searchamenities.add(split[i]);
                }
            }
            System.out.println("searchamenities Home"+searchamenities);
        }

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.show();


        odamenitie_title=(RelativeLayout) findViewById(R.id.odamenitie_title);
        odamenitie_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                updateAmenities();

            }
        });
        odamenities_title=(RelativeLayout) findViewById(R.id.odamenities_title);
        odamenities_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                updateAmenities();

            }
        });




        listView = (RecyclerView)findViewById(R.id.odamenities_list);
        searchlist = new ArrayList<>();
        amenitiesList = new ArrayList<>();
        adapter = new ODAmenitiesAdapter(this,this, amenitiesList);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(adapter);

        if (isInternetAvailable) {
            //new AmenitiesList().execute(); // amenities list API Call
            odAmenities();
        }else {
            snackBar();
        }
    }


    private void odAmenities() {


        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        //apiService.getServiceList(userid,localSharedPreferences.getSharedPreferences(Constants.LanguageCode)).enqueue(new RequestCallback(REQ_AMENITIES,this));

    }

    // Update selected amenites list details
    public void updateAmenities()
    {
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            for (int i = 0; i < amenities_list.length(); i++) {
                String amenitiesid = amenitiesList.get(i).getId();
                String amenitiesname = amenitiesList.get(i).getName();
                if (amenitiesList.get(i).getAmenitiesselected()) {
                    System.out.println("searchamenities Item=s" + searchamenities);
                    if (searchamenities != null && searchamenities.size() > 0) {
                        if (!searchamenities.contains(amenitiesid)) {
                            searchamenities.add(amenitiesid);
                        }
                    } else {
                        searchamenities.add(amenitiesid);
                    }

                    System.out.println("Selected Amenities Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                } else {
                    System.out.println("searchamenities Item=" + searchamenities);
                    if (searchamenities != null) {
                        if (searchamenities.contains(amenitiesid)) {
                            searchamenities.remove(amenitiesid);
                        }
                    }

                }
                listamenities = null;
                for (String s : searchamenities) {
                    if (listamenities != null && !listamenities.equals("")) {
                        listamenities = listamenities + "," + s;
                        //filteramenities += s + ",";
                    } else {
                        listamenities = s;
                    }
                }
                //searchamenities= (Arrays.asList(filteramenities));
                //   System.out.println("searchamenities get "+listamenities);
                //  localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,listamenities);

            }


            odamenitie_title.setVisibility(View.GONE);
            odamenities_dot_loader.setVisibility(View.VISIBLE);
            localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,null);

            apiService.updateAmenitiesList(userid,roomid,listamenities).enqueue(new RequestCallback(REQ_UPDATE_AMENITIES,this));
        }else {
            snackBar();
        }

    }



    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {




        switch (jsonResp.getRequestCode()) {

            case REQ_AMENITIES:
                if (jsonResp.isSuccess()) {

                    onSuccessOdAmen(jsonResp); // onSuccess call method
                }else if(!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,odamenitie_title,context.getResources(), (Activity) context);
                }
                break;

            case REQ_UPDATE_AMENITIES:
                if (jsonResp.isSuccess()) {
                    onSuccessOdAmenUpdate(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                    odamenitie_title.setVisibility(View.VISIBLE);
                    odamenities_dot_loader.setVisibility(View.GONE);

                }
                break;
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    private void onSuccessOdAmenUpdate(JsonResponse jsonResp) {

        odAmenitiesUpdate = gson.fromJson(jsonResp.getStrResponse(), OdAmenitiesUpdate.class);


        if(odAmenitiesUpdate.getStatusCode()!=null&&odAmenitiesUpdate.getStatusCode().equals("1")){
            odamenitie_title.setVisibility(View.GONE);
            odamenities_dot_loader.setVisibility(View.VISIBLE);
            localSharedPreferences.saveSharedPreferences(Constants.ListAmenities,listamenities);
            finish();

        }else{
                odamenitie_title.setVisibility(View.VISIBLE);
                odamenities_dot_loader.setVisibility(View.GONE);

        }

    }



    private void onSuccessOdAmen(JsonResponse jsonResp) {

        odAmenitiesModel = gson.fromJson(jsonResp.getStrResponse(), OdAmenitiesModel.class);

        System.out.println("Checkins one amenities ");

        if(odAmenitiesModel.getStatusCode()!=null&&odAmenitiesModel.getStatusCode().equals("1")){
            try {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }

            odAmenitiesArray = odAmenitiesModel.getData();


                amenities_list = new JSONArray(gson.toJson(odAmenitiesArray));

            System.out.println("Checkins two amenities ");


            for (int i = 0; i < odAmenitiesArray.size(); i++) {
                if(searchamenities!=null&&searchamenities.contains(odAmenitiesArray.get(i).getId()))
                {
                    odAmenitiesArray.get(i).setAmenitiesselected(true);
                }else
                {
                    odAmenitiesArray.get(i).setAmenitiesselected(false);
                }
            }



            amenitiesList.addAll(odAmenitiesArray);
            adapter.notifyDataChanged();
            System.out.println("Checkins three amenities "+amenitiesList.get(0).getName());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Check network connection
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    // Show error message for network error and exception
    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(odamenities_dot_loader, "", Snackbar.LENGTH_LONG);
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

}
