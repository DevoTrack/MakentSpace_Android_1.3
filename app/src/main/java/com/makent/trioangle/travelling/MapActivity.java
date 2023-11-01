package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.MapListAdapter;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.WishListObjects;
import com.makent.trioangle.newhome.makentspacehome.Model.HomeListModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

/* ***********************************************************************
This is Map Page Contain to Serach map view
**************************************************************************  */
public class MapActivity extends AppCompatActivity
        implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener
{

    public @Inject
    CommonMethods commonMethods;

    GoogleMap googleMap;
    boolean checkmap=true;

    int cp=0;

    MapListAdapter mapListAdapter;

    ArrayList<HomeListModel> mapmodel;
    Context context;
    Button map_close,map_filter;

    RelativeLayout vp_layout;

    public ArrayList<Marker> mMarkerList = new ArrayList<>();

    String userid,searchlocation;
    public ViewPager mViewPager;

    private static boolean flag = true;
    Marker prevMarker;
    String prevVendorName,isInstantBook;

    RelativeLayout map_details;
    ImageView map_loader;
    float zoom=0;
    int isMyMapBack;
    protected boolean isInternetAvailable;

    EditText edt;

    LocalSharedPreferences localSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        commonMethods = new CommonMethods();
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        edt = (EditText)findViewById(R.id.edt);

        Log.e("Map Activity","Map Activity");
       // Toast.makeText(MapActivity.this,"Map Activity",Toast.LENGTH_SHORT).show();

        mapmodel = new ArrayList<HomeListModel>();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mapmodel= (ArrayList<HomeListModel>) args.getSerializable("searchlist");

        System.out.println("Makent model data from explore"+mapmodel+"\nLength"+mapmodel.size());

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        searchlocation=localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        context = this;

        vp_layout = (RelativeLayout) findViewById(R.id.vp_layout);

        mViewPager = (ViewPager) findViewById(R.id.vp_details);

        if(mapmodel.size()>0) {

            if (searchlocation != null) {
                zoom = 10f;
            } else {
                zoom = 0f;
            }
        }else
        {
            mViewPager.setVisibility(View.GONE);
            vp_layout.setVisibility(View.GONE);
            zoom = 0f;
        }
        map_details=(RelativeLayout) findViewById(R.id.map_details);
        map_loader=(ImageView)findViewById(R.id.map_loader);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(map_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        map_details.setVisibility(View.GONE);

        try
        {
            // Loading map
            initilizeMap();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        map_close = (Button) findViewById(R.id.contacthost_close);
        commonMethods.setTvAlign(map_close,this);

        // On Click function used to click action for check Email id in server send link to Email
        map_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        map_filter = (Button) findViewById(R.id.map_filter);

        // On Click function used to click action for check Email id in server send link to Email
        map_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {

                if (isInternetAvailable) {
                    Intent x = new Intent(getApplicationContext(), FilterActivity.class);
                    startActivity(x);
                }
                else{
                    commonMethods.snackBar(getResources().getString(R.string.network_failure), "", false, 2, edt, getResources(), MapActivity.this);
                }
            }
        });


        int w=mViewPager.getWidth();
        int w1=mViewPager.getMeasuredWidth();
        System.out.println("W"+w+" W1"+w1);
        mViewPager.setPadding(15, 0, 15, 0);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(8);

        mapListAdapter = new MapListAdapter(getSupportFragmentManager(), mapmodel, this,cp);;
        mViewPager.setAdapter(mapListAdapter);

        mapListAdapter.notifyDataSetChanged();

        mViewPager.setOffscreenPageLimit(4);
        if(getResources().getString(R.string.layout_direction).equalsIgnoreCase("1"))
        {
            mViewPager.setRotationY(180);
        }
        else
        {
            mViewPager.setRotationX(180);
        }

      //  vp_layout.setRotationY(180);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mapmodel.size() == position) {
                    position = position - 1;
                }
                if (mapmodel.size() != position) {
                    if (flag) {
                        flag = false;
                        final HomeListModel temp = mapmodel.get(position);
                        LatLng newLatLng = new LatLng(Double.parseDouble(temp.getLatitude()), Double.parseDouble(temp.getLongitude()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, zoom));

                        Marker marker = mMarkerList.get(position);

                        if (!marker.equals(prevMarker)) {
                            if (prevMarker != null) {
                                //Set prevMarker back to default color
                                IconGenerator iconFactory = new IconGenerator(MapActivity.this);
                                iconFactory.setRotation(0);
                                iconFactory.setBackground(null);
                                View view = View.inflate(MapActivity.this, R.layout.map_marker_text, null);
                                TextView tvVendorTitle, tv_instant;
                                FrameLayout marker_icon;

                                marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                                tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                                tv_instant = (TextView) view.findViewById(R.id.tv_instant);

                                tvVendorTitle.setText(prevVendorName);
                                if (isInstantBook.equals("No")) {
                                    tv_instant.setVisibility(View.GONE);
                                } else {
                                    tv_instant.setVisibility(View.VISIBLE);
                                }
                                marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m_w));
                                tvVendorTitle.setTextColor(getResources().getColor(R.color.text_shadow));
                                tv_instant.setTextColor(getResources().getColor(R.color.yellow));
                                iconFactory.setContentView(view);
                                //prevVendorName = myDealsList.get(position).getmVendorName();
                                isInstantBook = temp.getInstantBook();
                                prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCountryName() + "" + temp.getHourly())));

                            }
                        }

                        //leave Marker default color if re-click current Marker
                        if (!marker.equals(prevMarker)) {
                            IconGenerator iconFactory = new IconGenerator(MapActivity.this);
                            iconFactory.setRotation(0);
                            iconFactory.setBackground(null);
                            View view = View.inflate(MapActivity.this, R.layout.map_marker_text_active, null);
                            TextView tvVendorTitle, tv_instant;
                            FrameLayout marker_icon;

                            marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                            tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                            tv_instant = (TextView) view.findViewById(R.id.tv_instant);
                            tvVendorTitle.setText(mapmodel.get(position).getCurrencySymbol() + "" + mapmodel.get(position).getHourly());
                            if (mapmodel.get(position).getInstantBook().equals("No")) {
                                tv_instant.setVisibility(View.GONE);
                            } else {
                                tv_instant.setVisibility(View.VISIBLE);
                            }
                            iconFactory.setContentView(view);
                            marker.setZIndex(100);
                            if (prevMarker != null) {
                                prevMarker.setZIndex(50);
                            }
                            //marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCurrencySymbol()+""+temp.getHourly())));
                            prevMarker = marker;
                            isInstantBook = mapmodel.get(position).getInstantBook();
                            prevVendorName = mapmodel.get(position).getCurrencySymbol() + "" + mapmodel.get(position).getHourly();
                        }
                        prevMarker = marker;
                        isInstantBook = mapmodel.get(position).getInstantBook();
                        prevVendorName = mapmodel.get(position).getCurrencySymbol() + "" + mapmodel.get(position).getHourly();
                        flag = true;
                    }
                    else {
                        Log.i("", "" + mMarkerList);
                        Log.i("", "" + position);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeAllStickyEvents();
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WishListObjects wishListType) {
        if (wishListType.isWishListFrom().equalsIgnoreCase("Map")) {
          mapmodel.get(wishListType.isWishListPosition()).isWishlist="yes";
          mapListAdapter.notifyDataSetChanged();
        }

        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId,"");
        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist,false);

        mapListAdapter = new MapListAdapter(getSupportFragmentManager(), mapmodel, this,cp);
        mViewPager.setAdapter(mapListAdapter);

        mapListAdapter.notifyDataSetChanged();

    };

    @Override
    public void onBackPressed() {
        System.out.println("IS In Back");
        if(localSharedPreferences.getSharedPreferences(Constants.Reload)!=null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload,null);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
            System.out.println("IS In Back Reload");
        }else
        {
            if (isMyMapBack==1){
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                overridePendingTransition(0, 0);
                x.putExtra("tabsaved", 0);
                x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(x);
                finish();
                System.out.println("IS In Back else");
            }else {
                super.onBackPressed();
                System.out.println("IS In Back OnBackPress");
            }
        }

        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId,"");
        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist,false);
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mainmap);
            mapFragment.getMapAsync(this);

            // check if map is created successfully or not
            if (googleMap == null) {
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        System.out.println("Check Map outsite"+checkmap);
        if(checkmap) {
            System.out.println("Check Map insite"+checkmap);
            checkmap=false;
            if(mapmodel.size()>0)
                loadDataModel();// this is used to map marker loadDataModel function
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (flag) {
            flag = false;
            vp_layout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);

            String aid = marker.getId().substring(1, marker.getId().length());
            cp=Integer.parseInt(aid);
            System.out.println("Marker ID"+marker.getId().toString());
            final HomeListModel temp = mapmodel.get(Integer.parseInt(aid));

            mViewPager.setCurrentItem(Integer.parseInt(aid));

            System.out.println("AID"+aid);
            System.out.println("Temp Markers"+temp);
            System.out.println("Marker"+marker);
            System.out.println("Previous Marker"+prevMarker);


            if (!marker.equals(prevMarker)) {
                if (prevMarker != null) {
                    System.out.println("Previous Marker empty");
                    //Set prevMarker back to default color
                    IconGenerator iconFactory = new IconGenerator(MapActivity.this);
                    iconFactory.setRotation(0);
                    iconFactory.setBackground(null);
                    View view = View.inflate(MapActivity.this, R.layout.map_marker_text, null);

                    TextView tvVendorTitle,tv_instant;
                    FrameLayout marker_icon;

                    marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                    tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                    tv_instant = (TextView) view.findViewById(R.id.tv_instant);

                    tvVendorTitle.setText(prevVendorName);
                    if(isInstantBook.equals("No"))
                    {
                        tv_instant.setVisibility(View.GONE);
                    }else
                    {
                        tv_instant.setVisibility(View.VISIBLE);
                    }
                    marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m_w));
                    tvVendorTitle.setTextColor(getResources().getColor(R.color.text_shadow));
                    tv_instant.setTextColor(getResources().getColor(R.color.yellow));

                    iconFactory.setContentView(view);
                    System.out.println("set Previous Marker white");
                    prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCurrencySymbol() + "" + temp.getHourly())));

                }
            }

            //leave Marker default color if re-click current Marker
            if (!marker.equals(prevMarker)) {
                System.out.println("Current Marker Not Previous");

                IconGenerator iconFactory = new IconGenerator(MapActivity.this);
                iconFactory.setRotation(0);
                iconFactory.setBackground(null);
                View view = View.inflate(MapActivity.this, R.layout.map_marker_text, null);
                TextView tvVendorTitle,tv_instant;
                FrameLayout marker_icon;

                marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                tv_instant = (TextView) view.findViewById(R.id.tv_instant);
                if(mapmodel.get(Integer.parseInt(marker.getSnippet())).getInstantBook().equals("No"))
                {
                    tv_instant.setVisibility(View.GONE);
                }else
                {
                    tv_instant.setVisibility(View.VISIBLE);
                }

                tvVendorTitle.setText(mapmodel.get(Integer.parseInt(marker.getSnippet())).getCurrencySymbol()+""+mapmodel.get(Integer.parseInt(marker.getSnippet())).getHourly());
                marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m));
                tvVendorTitle.setTextColor(getResources().getColor(R.color.title_text_color));
                tv_instant.setTextColor(getResources().getColor(R.color.title_text_color));
                iconFactory.setContentView(view);

                marker.setZIndex(100);
                if(prevMarker!=null) {
                    prevMarker.setZIndex(50);
                }
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCurrencySymbol()+""+temp.getHourly())));

                System.out.println("set Current Marker green");
                prevMarker = marker;
                isInstantBook=mapmodel.get(Integer.parseInt(marker.getSnippet())).getInstantBook();
                prevVendorName = mapmodel.get(Integer.parseInt(marker.getSnippet())).getCurrencySymbol()+""+mapmodel.get(Integer.parseInt(marker.getSnippet())).getHourly();

            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            flag = true;
        }

        return false;
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mViewPager.setVisibility(View.GONE);
        vp_layout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMyMapBack=getIntent().getIntExtra("isMapBack",0);
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        }else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"Map");
        }
        initilizeMap();

        System.out.println("On resume execution check : ");
    }


    public void loadDataModel() {
        for(int i=0;i<mapmodel.size();i++)
        {
            LatLng newLatLngTemp = new LatLng(Double.parseDouble(mapmodel.get(i).getLatitude()), Double.parseDouble((mapmodel.get(i).getLongitude())));

            System.out.println("latlng" + newLatLngTemp);
            MarkerOptions options = new MarkerOptions();
            IconGenerator iconFactory = new IconGenerator(MapActivity.this);
            iconFactory.setRotation(0);
            iconFactory.setBackground(null);
            View view;// = View.inflate(MapActivity.this, R.layout.map_marker_text, null);
            if (i == 0) {
                view = View.inflate(MapActivity.this, R.layout.map_marker_text_active, null);
                isInstantBook = mapmodel.get(i).getInstantBook();
                prevVendorName = mapmodel.get(i).getCurrencySymbol() + "" + mapmodel.get(i).getHourly();
            } else {
                view = View.inflate(MapActivity.this, R.layout.map_marker_text, null);
            }
            TextView tvVendorTitle, tv_instant;
            FrameLayout marker_icon;
            marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
            tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
            tv_instant = (TextView) view.findViewById(R.id.tv_instant);
            tvVendorTitle.setText(mapmodel.get(i).getCurrencySymbol() + "" + mapmodel.get(i).getHourly());
            if(mapmodel.get(i).getInstantBook().equals("No"))
            {
                tv_instant.setVisibility(View.GONE);
            }else
            {
                tv_instant.setVisibility(View.VISIBLE);
            }
            iconFactory.setContentView(view);

            options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mapmodel.get(i).getCurrencySymbol() + "" + mapmodel.get(i).getHourly())));
            options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            options.position(newLatLngTemp);
            options.snippet(String.valueOf(i));
            if (i == 0) {
                options.zIndex(100);
            } else {
                options.zIndex(50);
            }

            Marker mapMarker = googleMap.addMarker(options);
            mMarkerList.add(mapMarker);

        }

        if (mapmodel.size() > 0) {
            prevMarker = mMarkerList.get(0);
            LatLng latlngOne = new LatLng(Double.parseDouble(mapmodel.get(0).getLatitude()), Double.parseDouble(mapmodel.get(0).getLongitude()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngOne, zoom));
        }
        mapListAdapter.notifyDataSetChanged();


        map_loader.setVisibility(View.GONE);
        map_details.setVisibility(View.VISIBLE);

    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

}
