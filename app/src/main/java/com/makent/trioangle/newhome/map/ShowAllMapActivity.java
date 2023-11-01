package com.makent.trioangle.newhome.map;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

// ***Experience Start***

// ***Experience End***
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.newhome.models.Detail;
import com.makent.trioangle.travelling.FilterActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

public class ShowAllMapActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener, OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    public @Inject
    CommonMethods commonMethods;

    GoogleMap googleMap;
    boolean checkmap=true;

    int cp=0;

    ShowAllMapAdapter showAllMapAdapter;
    ArrayList<Detail> detailArrayList;
    Context context;

    Button map_close, map_filter;
    RelativeLayout vp_layout;

    public ArrayList<Marker> mMarkerList = new ArrayList<>();

    String userid, searchlocation;
    private ViewPager mViewPager;

    private static boolean flag = true;
    Marker prevMarker;
    String prevVendorName;
    int isMyMapBack;
    RelativeLayout map_details;
    ImageView map_loader;
    float zoom = 0;

    LocalSharedPreferences localSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_map);
        commonMethods = new CommonMethods();

        detailArrayList = new ArrayList<Detail>();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");

        Log.e("Show All","Show All Map");

       // Toast.makeText(ShowAllMapActivity.this,"Show All",Toast.LENGTH_SHORT).show();
        detailArrayList = (ArrayList<Detail>) args.getSerializable("showAllMap");

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        searchlocation = localSharedPreferences.getSharedPreferences(Constants.SearchLocation);
        context = this;

        vp_layout = (RelativeLayout) findViewById(R.id.vp_layout);

        mViewPager = (ViewPager) findViewById(R.id.vp_details);

        if (detailArrayList.size() > 0) {

            if (searchlocation != null) {
                zoom = 10f;
            } else {
                zoom = 0f;
            }
        } else {
            mViewPager.setVisibility(View.GONE);
            vp_layout.setVisibility(View.GONE);
            zoom = 0f;
        }
        map_details = (RelativeLayout) findViewById(R.id.map_details);
        map_loader = (ImageView) findViewById(R.id.map_loader);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(map_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);
        map_details.setVisibility(View.GONE);
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        map_close = (Button) findViewById(R.id.contacthost_close);
        commonMethods.setTvAlign(map_close,this);

        // On Click function used to click action for check Email id in server send link to Email
        map_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                //onBackPressed();
                close();
            }
        });

        map_filter = (Button) findViewById(R.id.map_filter);

        map_filter.setVisibility(View.GONE);
        /*if (detailArrayList.get(0).getType().equalsIgnoreCase("Experiences")) {
        }*/
        // On Click function used to click action for check Email id in server send link to Email
        map_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                Intent x = new Intent(getApplicationContext(), FilterActivity.class);
//                x.putExtra("type", "home");
                startActivity(x);
            }
        });


        int w = mViewPager.getWidth();
        int w1 = mViewPager.getMeasuredWidth();
        System.out.println("W" + w + " W1" + w1);
        mViewPager.setPadding(15, 0, 15, 0);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(8);

        showAllMapAdapter = new ShowAllMapAdapter(getSupportFragmentManager(), detailArrayList, this, cp);
        mViewPager.setAdapter(showAllMapAdapter);

        showAllMapAdapter.notifyDataSetChanged();

        mViewPager.setOffscreenPageLimit(4);
        if(getResources().getString(R.string.layout_direction).equalsIgnoreCase("1"))
        {
            mViewPager.setRotationY(180);
        }
        else
        {
            mViewPager.setRotationX(180);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("detailArrayList.size() " + detailArrayList.size());
                System.out.println("mposition " + position);
                if (detailArrayList.size() == position) {
                    position = position - 1;
                }
                if (detailArrayList.size() != position) {
                    if (flag) {
                        flag = false;
                        final Detail temp = detailArrayList.get(position);
                        LatLng newLatLng = new LatLng(Double.parseDouble(temp.getLatitude()), Double.parseDouble(temp.getLongitude()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, zoom));

                        Marker marker = mMarkerList.get(position);

                        if (!marker.equals(prevMarker)) {
                            if (prevMarker != null) {
                                //Set prevMarker back to default color
                                IconGenerator iconFactory = new IconGenerator(ShowAllMapActivity.this);
                                iconFactory.setRotation(0);
                                iconFactory.setBackground(null);
                                View view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text, null);
                                TextView tvVendorTitle, tv_instant;
                                FrameLayout marker_icon;

                                marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                                tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                                tv_instant = (TextView) view.findViewById(R.id.tv_instant);
                                tv_instant.setVisibility(View.GONE);
                                tvVendorTitle.setText(prevVendorName);

                                marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m_w));
                                tvVendorTitle.setTextColor(getResources().getColor(R.color.text_shadow));
                                iconFactory.setContentView(view);

                                prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCountryName() + "" + temp.getPrice())));

                            }
                        }

                        //leave Marker default color if re-click current Marker
                        if (!marker.equals(prevMarker)) {
                            IconGenerator iconFactory = new IconGenerator(ShowAllMapActivity.this);
                            iconFactory.setRotation(0);
                            iconFactory.setBackground(null);

                            View view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text_active, null);
                            TextView tvVendorTitle, tv_instant;
                            FrameLayout marker_icon;

                            marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                            tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                            tv_instant = (TextView) view.findViewById(R.id.tv_instant);
                            tvVendorTitle.setText(detailArrayList.get(position).getCurrencySymbol() + "" + detailArrayList.get(position).getPrice());
                           // tv_instant.setVisibility(View.GONE);

                            iconFactory.setContentView(view);

                            marker.setZIndex(100);
                            if (prevMarker != null) {
                                prevMarker.setZIndex(50);
                            }

                            if(detailArrayList.get(position).getInstantBook().equals("No"))
                            {
                                tv_instant.setVisibility(View.GONE);
                            }else
                            {
                                tv_instant.setVisibility(View.VISIBLE);
                            }

                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
                            prevMarker = marker;
                            // isInstantBook=detailArrayList.get(position).getInstantBook();
                            prevVendorName = detailArrayList.get(position).getCurrencySymbol() + "" + detailArrayList.get(position).getPrice();
                        }
                        prevMarker = marker;
                        // isInstantBook=detailArrayList.get(position).getInstantBook();
                        prevVendorName = detailArrayList.get(position).getCurrencySymbol() + "" + detailArrayList.get(position).getPrice();
                        flag = true;

                    } else {
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

    public void close() {
        if (localSharedPreferences.getSharedPreferences(Constants.Reload) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, null);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
        } else {
            onBackPressed();
        }
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
    public void onMessageEvent(String roomId) {

        System.out.println(" Messsage event : "+roomId);

        for(int i=0;i<detailArrayList.size();i++){
            if(detailArrayList.get(i).getId()==Integer.parseInt(roomId)){
                detailArrayList.get(i).setIsWishlist("Yes");
                break;
            }
        }

        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlistId,"");
        localSharedPreferences.saveSharedPreferences(Constants.MapFilterWishlist,false);

        showAllMapAdapter = new ShowAllMapAdapter(getSupportFragmentManager(), detailArrayList, this,cp);
        mViewPager.setAdapter(showAllMapAdapter);

        showAllMapAdapter.notifyDataSetChanged();

    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.out.println("IS In Back");
        if (localSharedPreferences.getSharedPreferences(Constants.Reload) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.Reload, null);
            Intent x = new Intent(getApplicationContext(), HomeActivity.class);
            overridePendingTransition(0, 0);
            x.putExtra("tabsaved", 0);
            x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(x);
            finish();
            System.out.println("IS In Back Reload");
        } else {
            if (isMyMapBack == 1) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                overridePendingTransition(0, 0);
                x.putExtra("tabsaved", 0);
                x.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(x);
                finish();
                System.out.println("IS In Back else");
            } else {
                super.onBackPressed();
                System.out.println("IS In Back OnBackPress");
            }
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mainmap);
            mapFragment.getMapAsync(this);

            //  googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mainmap)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                //  Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mViewPager.setVisibility(View.GONE);
        vp_layout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isMyMapBack = getIntent().getIntExtra("isMapBack", 0);
        if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "");
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage, "ShowAll");
        }

        initilizeMap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (flag) {
            flag = false;
            vp_layout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);

            String aid = marker.getId().substring(1, marker.getId().length());
            cp = Integer.parseInt(aid);
            System.out.println("Marker ID" + marker.getId().toString());
            final Detail temp = detailArrayList.get(Integer.parseInt(aid));

            mViewPager.setCurrentItem(Integer.parseInt(aid));

            System.out.println("AID" + aid);
            System.out.println("Temp Markers" + temp);
            System.out.println("Marker" + marker);
            System.out.println("Previous Marker" + prevMarker);


            if (!marker.equals(prevMarker)) {
                if (prevMarker != null) {
                    System.out.println("Previous Marker empty");
                    //Set prevMarker back to default color
                    IconGenerator iconFactory = new IconGenerator(ShowAllMapActivity.this);
                    iconFactory.setRotation(0);
                    iconFactory.setBackground(null);
                    View view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text, null);

                    TextView tvVendorTitle, tv_instant;
                    FrameLayout marker_icon;

                    marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                    tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                    tv_instant = (TextView) view.findViewById(R.id.tv_instant);

                    tvVendorTitle.setText(prevVendorName);

                    tv_instant.setVisibility(View.GONE);
                    marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m_w));
                    tvVendorTitle.setTextColor(getResources().getColor(R.color.text_shadow));

                    iconFactory.setContentView(view);
                    prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCurrencySymbol() + "" + temp.getPrice())));

                }
            }

            //leave Marker default color if re-click current Marker
            if (!marker.equals(prevMarker)) {

                IconGenerator iconFactory = new IconGenerator(ShowAllMapActivity.this);
                iconFactory.setRotation(0);
                iconFactory.setBackground(null);
                View view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text, null);
                TextView tvVendorTitle, tv_instant;
                FrameLayout marker_icon;

                marker_icon = (FrameLayout) view.findViewById(R.id.marker_icon);
                tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                tv_instant = (TextView) view.findViewById(R.id.tv_instant);

                tv_instant.setVisibility(View.GONE);
                tvVendorTitle.setText(detailArrayList.get(Integer.parseInt(marker.getSnippet())).getCurrencySymbol() + "" + detailArrayList.get(Integer.parseInt(marker.getSnippet())).getPrice());
                marker_icon.setBackground(getResources().getDrawable(R.drawable.map_m));
                tvVendorTitle.setTextColor(getResources().getColor(R.color.title_text_color));
                tv_instant.setTextColor(getResources().getColor(R.color.title_text_color));
                iconFactory.setContentView(view);

                marker.setZIndex(100);
                if (prevMarker != null) {
                    prevMarker.setZIndex(50);
                }
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(temp.getCurrencySymbol() + "" + temp.getPrice())));


                System.out.println("set Current Marker green");
                prevMarker = marker;
                prevVendorName = detailArrayList.get(Integer.parseInt(marker.getSnippet())).getCurrencySymbol() + "" + detailArrayList.get(Integer.parseInt(marker.getSnippet())).getPrice();

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
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        //  googleMap.fetchAddressFromServer();
        System.out.println("Check Map outsite" + checkmap);
        if (checkmap) {
            System.out.println("Check Map insite" + checkmap);
            checkmap = false;
            if (detailArrayList.size() > 0)
                loadDataModel();// this is used to map marker loadDataModel function
        }
    }

    /**
     * to load data's from list
     */

    public void loadDataModel() {
        if(detailArrayList.size() > 0) {
            for (int i = 0; i < detailArrayList.size(); i++) {
                LatLng newLatLngTemp = new LatLng(Double.parseDouble(detailArrayList.get(i).getLatitude()), Double.parseDouble((detailArrayList.get(i).getLongitude())));

                System.out.println("latlng" + newLatLngTemp);
                MarkerOptions options = new MarkerOptions();
                IconGenerator iconFactory = new IconGenerator(ShowAllMapActivity.this);
                iconFactory.setRotation(0);
                iconFactory.setBackground(null);
                View view;
                if (i == 0) {
                    view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text_active, null);
                    prevVendorName = detailArrayList.get(i).getCurrencySymbol() + "" + detailArrayList.get(i).getPrice();
                } else {
                    view = View.inflate(ShowAllMapActivity.this, R.layout.map_marker_text, null);
                }
                TextView tvVendorTitle, tv_instant;

                tvVendorTitle = (TextView) view.findViewById(R.id.tv_vendor_title);
                tv_instant = (TextView) view.findViewById(R.id.tv_instant);
                tvVendorTitle.setText(detailArrayList.get(i).getCurrencySymbol() + "" + detailArrayList.get(i).getPrice());

                tv_instant.setVisibility(View.GONE);

                iconFactory.setContentView(view);

                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(detailArrayList.get(i).getCurrencySymbol() + "" + detailArrayList.get(i).getPrice())));
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
        }
        if (detailArrayList.size() > 0) {
            prevMarker = mMarkerList.get(0);
            LatLng latlngOne = new LatLng(Double.parseDouble(detailArrayList.get(0).getLatitude()), Double.parseDouble(detailArrayList.get(0).getLongitude()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlngOne, zoom));
        }
        showAllMapAdapter.notifyDataSetChanged();


        map_loader.setVisibility(View.GONE);
        map_details.setVisibility(View.VISIBLE);

    }
}
