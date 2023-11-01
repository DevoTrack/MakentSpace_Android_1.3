package com.makent.trioangle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;


public class MapFooterFragment extends Fragment {

    int page_position;
    TextView map_restaurant_name;
    ImageView map_room_image;
    LocalSharedPreferences localSharedPreferences;
    Context context;
    int currentposition;

    public MapFooterFragment() {

    }

    @SuppressLint("ValidFragment")
    public MapFooterFragment(Context context, int cp, int total) {
        this.context = context;
        this.currentposition = cp;
        localSharedPreferences = new LocalSharedPreferences(this.context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_footer_viewpage, container, false);
        try {
            map_room_image = rootView.findViewById(R.id.map_room_image);
            map_restaurant_name = rootView.findViewById(R.id.map_rest_name);
            return rootView;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PlacesMapFragment:" + e.getStackTrace());
        }

    }
}