package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestFilterAmenitiesListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.FilterAmenitiesListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.newhome.makentspacehome.Adapter.AmenityAdapter;
import com.makent.trioangle.newhome.makentspacehome.Model.Amenities;
import com.makent.trioangle.newhome.makentspacehome.Model.Filter_Model;
import com.makent.trioangle.newhome.makentspacehome.Model.HostActivityModel;
import com.makent.trioangle.newhome.makentspacehome.Model.Services;
import com.makent.trioangle.newhome.makentspacehome.Model.Space_rules;
import com.makent.trioangle.newhome.makentspacehome.Model.Space_styles;
import com.makent.trioangle.newhome.makentspacehome.Model.Special_features;
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
This is FilterAmenitiesList Page Contain to Filter 
**************************************************************************  */
public class FilterAmenitiesList extends Activity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;



    //For Makent Space:
    HostActivityModel activityModel;


    String amenities_val ="";
    String service_val = "";
    String space_val = "";
    String special_val = "";
    String style_val = "";
    String space_type_val = "";
    String event_type_val ="";
    RecyclerView style_list,space_list,special_list,service_list,space_type,event_type;
    String filteramenity, filterService, filterspace,filterspecial, filterstyle,filterspacetype, filtereventtype;
    int filteram;

    List<String> searchamenity = new ArrayList<String>();
    List<String> searchService = new ArrayList<>();
    List<String> searchSpace = new ArrayList<>();
    List<String> searchStyle = new ArrayList<>();
    List<String> searchSpecial = new ArrayList<>();
    List<String> searchSpaceType = new ArrayList<>();
    List<String> searchEventType = new ArrayList<>();
    List<String> space_type_name = new ArrayList<>();

    List<Integer> searcham = new ArrayList<>();



    List<Filter_Model> filter_models;
    AmenityAdapter amenityAdapter;


    ImageView selct_country_back;
    FloatingActionButton selct_country_next;
    RecyclerView listView;
    List<Makent_model> searchlist;
    int val = 0;



    FilterAmenitiesListAdapter adapter;
    Context context;
    JSONArray amenities_list;
    JSONArray amenitylist,serviceList,speciallist,stylelist,spacelist,space_type_list,event_type_list;

    protected boolean isInternetAvailable;
    String userid,filteramenities;

    List<String> searchamenities = new ArrayList<String>();
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_amenities_list);
        commonMethods = new CommonMethods();

        initialize();
        listView = (RecyclerView)findViewById(R.id.country_list);

        Intent x = getIntent();
        amenities_val = x.getStringExtra("Amenities");
        service_val = x.getStringExtra("Service");
        style_val = x.getStringExtra("Style");
        space_val = x.getStringExtra("Space");
        special_val = x.getStringExtra("Special");
        space_type_val = x.getStringExtra("SpaceType");
        event_type_val = x.getStringExtra("EventType");



        if(amenities_val != null){
            listView.setVisibility(View.VISIBLE);
            val = 1;
        }
        if(service_val != null){
            service_list.setVisibility(View.VISIBLE);
            val = 2;

        }
        if(space_val != null){
            space_list.setVisibility(View.VISIBLE);
            val = 5;

        }
        if(special_val != null){
            special_list.setVisibility(View.VISIBLE);
            val = 4;
        }
        if(style_val != null){
            style_list.setVisibility(View.VISIBLE);
            val = 3;
        }
        if(space_type_val != null){
            space_type.setVisibility(View.VISIBLE);
            val = 6;
        }
        if(event_type_val != null){
            event_type.setVisibility(View.VISIBLE);
            val = 7;
        }
       /* else{
            listView.setVisibility(View.VISIBLE);
            val = 1;
        }*/


        localSharedPreferences=new LocalSharedPreferences(this);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);

        //For makent Space:
/*
        filteram = localSharedPreferences.getSharedPreferencesInt(Constants.Amenity);
        if(filteram != 0){
            searcham.add(filteram);
        }
        System.out.println("searchamenities Space integer "+searcham);*/

        filteramenity = localSharedPreferences.getSharedPreferences(Constants.FilterAmenity);
        if(filteramenity != null) {
            String[] split = filteramenity.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchamenity.contains(split[i])) {
                    searchamenity.add(split[i]);
                }
            }System.out.println("searchamenities Space"+searchamenity);
        }

        filterService = localSharedPreferences.getSharedPreferences(Constants.FilterService);
        if(filterService != null) {
            String[] split = filterService.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchService.contains(split[i])) {
                    searchService.add(split[i]);
                }
            }System.out.println("searchservice Space"+searchService);
        }

        filterspace = localSharedPreferences.getSharedPreferences(Constants.FilterSpace);
        if(filterspace != null) {
            String[] split = filterspace.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchSpace.contains(split[i])) {
                    searchSpace.add(split[i]);
                }
            }System.out.println("searchspace Space"+searchSpace);
        }

        filterstyle = localSharedPreferences.getSharedPreferences(Constants.FilterStyle);
        if(filterstyle != null) {
            String[] split = filterstyle.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchStyle.contains(split[i])) {
                    searchStyle.add(split[i]);
                }
            }System.out.println("searchstyle Space"+searchStyle);
        }

        filterspecial = localSharedPreferences.getSharedPreferences(Constants.FilterSpecial);
        if(filterspecial != null) {
            String[] split = filterspecial.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!searchSpecial.contains(split[i])) {
                    searchSpecial.add(split[i]);
                }
            }System.out.println("searchspecial Space"+searchSpecial);
        }

        filterspacetype = localSharedPreferences.getSharedPreferences(Constants.FilterSpaceType);
        if(filterspacetype != null){
            String[] split = filterspacetype.split(",");
            for(int i =0 ; i< split.length; i++){
                if(!searchSpaceType.contains(split[i])){
                    searchSpaceType.add(split[i]);
                }

            }System.out.println("search SpaceType " + searchSpaceType.size());
        }



        /*filteramenities= localSharedPreferences.getSharedPreferences(Constants.FilterAmenities);
        if(filteramenities!=null)
        {
            String[] split = filteramenities.split(",");
            for(int i=0;i<split.length;i++) {
                if(!searchamenities.contains(split[i])) {
                    searchamenities.add(split[i]);
                }
            }
            System.out.println("searchamenities Home"+searchamenities);
        }*/

        mydialog = new Dialog_loading(this);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.show();

        selct_country_back = (ImageView) findViewById(R.id.selct_country_back);
        commonMethods.rotateArrow(selct_country_back,this);

        // On Click function used to click action for check Email id in server send link to Email
        selct_country_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                finish();
            }
        });

        selct_country_next = (FloatingActionButton) findViewById(R.id.selct_country_next);
        commonMethods.rotateArrow(selct_country_next,this);

        // On Click function used to click action for check Email id in server send link to Email
        /*selct_country_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                for(int i=0;i<amenities_list.length();i++) {
                    String amenitiesid=searchlist.get(i).getAmenitiesId();
                    String amenitiesname=searchlist.get(i).getAmenities();
                    if(searchlist.get(i).getAmenitiesSelected())
                    {
                        System.out.println("if searchamenities Item="+searchamenities);
                        if(searchamenities!=null) {
                            if(!searchamenities.contains(amenitiesid)) {
                                searchamenities.add(amenitiesid);
                            }
                            }else
                            {
                                searchamenities.add(amenitiesid);
                            }

                        System.out.println("Selected Amenities Selected Item="+amenitiesid+" NAme  "+amenitiesname);
                    }else{
                        System.out.println("else searchamenities Item="+searchamenities);
                        if(searchamenities!=null) {
                            if(searchamenities.contains(amenitiesid)) {
                                searchamenities.remove(amenitiesid);
                            }
                        }

                    }
                    filteramenities=null;
                    for (String s : searchamenities)
                    {
                        if(filteramenities!=null) {
                            filteramenities=filteramenities+","+s;
                        }else {
                            filteramenities=s;
                        }
                    }
                    localSharedPreferences.saveSharedPreferences(Constants.FilterAmenities,filteramenities);
                }

                finish();
            }
        });*/


        selct_country_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                if (val == 1) {
                    for (int i = 0; i < amenitylist.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchamenities Item=" + searchamenity);
                            if (searchamenity != null) {
                                if (!searchamenity.contains(amenitiesid)) {
                                    searchamenity.add(amenitiesid);
                                }
                            } else {
                                searchamenity.add(amenitiesid);
                            }

                            System.out.println("Selected Amenities Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchamenities Item=" + searchamenity);
                            if (searchamenity != null) {
                                if (searchamenity.contains(amenitiesid)) {
                                    searchamenity.remove(amenitiesid);
                                }
                            }

                        }



                        filteramenity = null;
                        for (String s : searchamenity) {
                            if (filteramenity != null) {
                                filteramenity = filteramenity + "," + s;
                            } else {
                                filteramenity = s;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.FilterAmenity, filteramenity);
                    }

                    finish();
                }

                  else  if(val == 2){
                    for (int i = 0; i < serviceList.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchservice Item=" + searchService);
                            if (searchService != null) {
                                if (!searchService.contains(amenitiesid)) {
                                    searchService.add(amenitiesid);
                                }
                            } else {
                                searchService.add(amenitiesid);
                            }

                            System.out.println("Selected Service Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchservice Item=" + searchService);
                            if (searchService != null) {
                                if (searchService.contains(amenitiesid)) {
                                    searchService.remove(amenitiesid);
                                }
                            }

                        }
                        filterService = null;
                        for (String s : searchService) {
                            if (filterService != null) {
                                filterService = filterService + "," + s;
                            } else {
                                filterService = s;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.FilterService, filterService);
                    }

                    finish();
                }

              else  if(val == 5){
                    for (int i = 0; i < spacelist.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchspace Item=" + searchSpace);
                            if (searchSpace != null) {
                                if (!searchSpace.contains(amenitiesid)) {
                                    searchSpace.add(amenitiesid);
                                }
                            } else {
                                searchSpace.add(amenitiesid);
                            }

                            System.out.println("Selected Space Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchspace Item=" + searchSpace);
                            if (searchSpace != null) {
                                if (searchSpace.contains(amenitiesid)) {
                                    searchSpace.remove(amenitiesid);
                                }
                            }

                        }
                        filterspace = null;
                        for (String s : searchSpace) {
                            if (filterspace != null) {
                                filterspace = filterspace + "," + s;
                            } else {
                                filterspace = s;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.FilterSpace, filterspace);
                    }

                    finish();

                }
              else  if(val == 3){
                    for (int i = 0; i < stylelist.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchstyle Item=" + searchStyle);
                            if (searchStyle != null) {
                                if (!searchStyle.contains(amenitiesid)) {
                                    searchStyle.add(amenitiesid);
                                }
                            } else {
                                searchStyle.add(amenitiesid);
                            }

                            System.out.println("Selected Style Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchstyle Item=" + searchStyle);
                            if (searchStyle != null) {
                                if (searchStyle.contains(amenitiesid)) {
                                    searchStyle.remove(amenitiesid);
                                }
                            }

                        }
                        filterstyle = null;
                        for (String s : searchStyle) {
                            if (filterstyle != null) {
                                filterstyle = filterstyle + "," + s;
                            } else {
                                filterstyle = s;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.FilterStyle, filterstyle);
                    }

                    finish();
                }
               else if(val == 4){
                    for (int i = 0; i < speciallist.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchspecial Item=" + searchSpecial);
                            if (searchSpecial != null) {
                                if (!searchSpecial.contains(amenitiesid)) {
                                    searchSpecial.add(amenitiesid);
                                }
                            } else {
                                searchSpecial.add(amenitiesid);
                            }

                            System.out.println("Selected Special Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchspecial Item=" + searchSpecial);
                            if (searchSpecial != null) {
                                if (searchSpecial.contains(amenitiesid)) {
                                    searchSpecial.remove(amenitiesid);
                                }
                            }

                        }
                        filterspecial = null;
                        for (String s : searchSpecial) {
                            if (filterspecial != null) {
                                filterspecial = filterspecial + "," + s;
                            } else {
                                filterspecial = s;
                            }
                        }
                        localSharedPreferences.saveSharedPreferences(Constants.FilterSpecial, filterspecial);
                    }

                    finish();
                }
                if(space_type_val != null){
                    for (int i = 0; i < space_type_list.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchspace Item=" + searchSpaceType);
                            if (searchSpaceType != null) {
                                if (!searchSpaceType.contains(amenitiesid)) {
                                    searchSpaceType.add(amenitiesid);
                                }
                            } else {
                                searchSpaceType.add(amenitiesid);
                            }


                            System.out.println("Selected Space Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchspace Item=" + searchSpaceType);
                            if (searchSpaceType != null) {
                                if (searchSpaceType.contains(amenitiesid)) {
                                    searchSpaceType.remove(amenitiesid);
                                }
                            }

                        }
                        filterspacetype = null;
                        for (String s : searchSpaceType) {
                            if (filterspacetype != null) {
                                filterspacetype = filterspacetype + "," + s;
                            } else {
                                filterspacetype = s;
                            }
                        }

                        System.out.println("Space Type Value " + searchSpaceType);
                        localSharedPreferences.saveSharedPreferences(Constants.FilterSpaceType, filterspacetype);
                    }

                    finish();

                }
                if(event_type_val != null){
                    for (int i = 0; i < event_type_list.length(); i++) {
                        String amenitiesid = filter_models.get(i).getId();
                        String amenitiesname = filter_models.get(i).getName();
                        if (filter_models.get(i).getStatus()) {
                            System.out.println("if searchevent Item=" + searchEventType);
                            if (searchEventType != null) {
                                if (!searchEventType.contains(amenitiesid)) {
                                    searchEventType.add(amenitiesid);
                                }
                            } else {
                                searchEventType.add(amenitiesid);
                            }

                            System.out.println("Selected event Selected Item=" + amenitiesid + " NAme  " + amenitiesname);
                        } else {
                            System.out.println("else searchevent Item=" + searchEventType);
                            if (searchEventType != null) {
                                if (searchEventType.contains(amenitiesid)) {
                                    searchEventType.remove(amenitiesid);
                                }
                            }

                        }
                        filtereventtype = null;
                        for (String s : searchEventType) {
                            if (filtereventtype != null) {
                                filtereventtype = filtereventtype + "," + s;
                            } else {
                                filtereventtype = s;
                            }
                        }
                        System.out.println("Event ID " + filtereventtype);
                       // localSharedPreferences.saveSharedPreferences(Constants.FilterEventType, String.valueOf(filtereventtype));
                      // localSharedPreferences.getSharedPreferences(Constants.FilterEventType);
                    }

                    finish();

                }
            }
        });





        //changes made:
        filter_models = new ArrayList<>();
        amenityAdapter = new AmenityAdapter(getHeader(),this,filter_models, val);


        //adapter = new FilterAmenitiesListAdapter(getHeader(),this, searchlist);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(amenityAdapter);


        service_list.setHasFixedSize(true);
        service_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        service_list.setAdapter(amenityAdapter);

        style_list.setHasFixedSize(true);
        style_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        style_list.setAdapter(amenityAdapter);


        special_list.setHasFixedSize(true);
        special_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        special_list.setAdapter(amenityAdapter);

        space_list.setHasFixedSize(true);
        space_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        space_list.setAdapter(amenityAdapter);


        space_type.setHasFixedSize(true);
        space_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        space_type.setAdapter(amenityAdapter);

        event_type.setHasFixedSize(true);
        event_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        event_type.setAdapter(amenityAdapter);
        amenityAdapter.setOnItemClickListener(new AmenityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View viewItem, int position) {
                //Your item click code
            }
        });

        load(0);
        }


        private void initialize(){
            style_list = (RecyclerView)findViewById(R.id.style_list);
            space_list =(RecyclerView)findViewById(R.id.space_list);
            special_list =(RecyclerView)findViewById(R.id.special_list);
            service_list =(RecyclerView)findViewById(R.id.service_list);
            space_type = (RecyclerView)findViewById(R.id.space_type);
            event_type =(RecyclerView)findViewById(R.id.event_type);

        }

    private void load(int index) {
        //String eventName=localSharedPreferences.getSharedPreferences(Constants.EventName);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        /*if(eventName == null || eventName.equals("")){
            localSharedPreferences.saveSharedPreferences(Constants.EventName,"");
        }else{

        }*/
        if (isInternetAvailable){
            //getServiceList();
            getFilterData();
        }else {
            commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, selct_country_back, getResources(), this);
        }
    }

    public Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    /**
     * @Reference Get Filter data from server
     */

    public void getFilterData(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        apiService.search_filter(localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(this));
    }

    public void getFilterData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if (amenities_val != null) {
                amenitylist = response.getJSONArray("amenities");
                for (int i = 0; i < amenitylist.length(); i++) {
                    JSONObject dataJSONObject = amenitylist.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchamenity != null && searchamenity.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

     public void getServiceData(JsonResponse jsonResponse) {
                try {
                    JSONObject response = new JSONObject(jsonResponse.getStrResponse());
                    if (service_val != null) {
                        serviceList = response.getJSONArray("services");
                        for (int i = 0; i < serviceList.length(); i++) {
                            JSONObject dataJSONObject = serviceList.getJSONObject(i);
                            Filter_Model listdata = new Filter_Model();
                            listdata.setId(dataJSONObject.getString("id"));
                            listdata.setName(dataJSONObject.getString("name"));
                            if (searchService != null && searchService.contains(dataJSONObject.getString("id"))) {
                                listdata.setStatus(true);
                            } else {
                                listdata.setStatus(false);
                            }

                            filter_models.add(listdata);

                        }
                        Filter_Model listdata = new Filter_Model();
                        listdata.setId("32");
                        listdata.setName("");
                        listdata.setStatus(false);
                        filter_models.add(listdata);
                        amenityAdapter.notifyDataChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                }
            }
    public void getSpaceData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());

            if (space_val != null) {
                spacelist = response.getJSONArray("space_rules");
                for (int i = 0; i < spacelist.length(); i++) {
                    JSONObject dataJSONObject = spacelist.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchSpace != null && searchSpace.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }
    public void getStyleData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if (style_val != null) {
                stylelist = response.getJSONArray("space_styles");
                for (int i = 0; i < stylelist.length(); i++) {
                    JSONObject dataJSONObject = stylelist.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchStyle != null && searchStyle.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }
        }catch (JSONException e) {
                e.printStackTrace();
                if (mydialog.isShowing()) {
                    mydialog.dismiss();
                }
            }
        }
    public void getSpecialData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if (special_val != null) {
                speciallist = response.getJSONArray("special_features");
                for (int i = 0; i < speciallist.length(); i++) {
                    JSONObject dataJSONObject = speciallist.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchSpecial != null && searchSpecial.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }
    public void getSpaceTypeData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if (space_type_val != null) {
                space_type_list = response.getJSONArray("space_types");
                for (int i = 0; i < space_type_list.length(); i++) {
                    JSONObject dataJSONObject = space_type_list.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchSpaceType != null && searchSpaceType.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }
    public void getEventTypeData(JsonResponse jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            if (event_type_val != null) {
                event_type_list = response.getJSONArray("activities");
                for (int i = 0; i < event_type_list.length(); i++) {
                    JSONObject dataJSONObject = event_type_list.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchEventType != null && searchEventType.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }



            /*else{
                amenitylist = response.getJSONArray("amenities");
                for (int i = 0; i < amenitylist.length(); i++) {
                    JSONObject dataJSONObject = amenitylist.getJSONObject(i);
                    Filter_Model listdata = new Filter_Model();
                    listdata.setId(dataJSONObject.getString("id"));
                    listdata.setName(dataJSONObject.getString("name"));
                    if (searchamenity != null && searchamenity.contains(dataJSONObject.getString("id"))) {
                        listdata.setStatus(true);
                    } else {
                        listdata.setStatus(false);
                    }

                    filter_models.add(listdata);

                }
                Filter_Model listdata = new Filter_Model();
                listdata.setId("32");
                listdata.setName("");
                listdata.setStatus(false);
                filter_models.add(listdata);
                amenityAdapter.notifyDataChanged();
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }

    }

    /**
     * @Reference Get amenities list from server
     */
    public void getAmenitiesList(){
        if (!mydialog.isShowing()) {
            mydialog.show();
        }

        //apiService.getServiceList(localSharedPreferences.getSharedPreferences(Constants.AccessToken),localSharedPreferences.getSharedPreferences(Constants.LanguageCode)).enqueue(new RequestCallback(this));
    }

    public void getAmenitiesList(JsonResponse jsonResponse){
        try {
            JSONObject response = new JSONObject(jsonResponse.getStrResponse());
            amenities_list = response.getJSONArray("data");
            for (int i = 0; i < amenities_list.length(); i++) {


                JSONObject dataJSONObject = amenities_list.getJSONObject(i);
                Makent_model listdata = new Makent_model();
                listdata.setAmenitiesId(dataJSONObject.getString("id"));
                listdata.setAmenities(dataJSONObject.getString("name"));
                if(searchamenities!=null&&searchamenities.contains(dataJSONObject.getString("id")))
                {
                    listdata.setAmenitiesSelected(true);
                }else
                {
                    listdata.setAmenitiesSelected(false);
                }

                searchlist.add(listdata);

            }
            Makent_model listdata = new Makent_model();
            listdata.setAmenitiesId("32");
            listdata.setAmenities("");
            listdata.setAmenitiesSelected(false);
            searchlist.add(listdata);
            adapter.notifyDataChanged();
        }catch (JSONException e) {
            e.printStackTrace();
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        } if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, selct_country_back, getResources(), this);
            return;
        }

        if (jsonResp.isSuccess()) {
           // getServiceList(jsonResp);
                getFilterData(jsonResp);
                getServiceData(jsonResp);
                getSpaceData(jsonResp);
                getSpaceTypeData(jsonResp);
                getSpecialData(jsonResp);
                getStyleData(jsonResp);
                getEventTypeData(jsonResp);

        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, selct_country_back, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
    }
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
