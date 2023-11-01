package com.makent.trioangle.host.optionaldetails;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makent.trioangle.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> arrayListroomtype;
    OD_RoomsBeds od_roomsBeds;
    LayoutInflater inflter;
    public MyAdapter(OD_RoomsBeds od_roomsBeds, ArrayList<HashMap<String, String>> arrayListroomtype) {
        this.arrayListroomtype=arrayListroomtype;
        this.od_roomsBeds=od_roomsBeds;
        inflter = (LayoutInflater.from(od_roomsBeds));
    }

    @Override
    public int getCount() {
        return arrayListroomtype.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.listtype_dialog, null);
        TextView lys_entire_rooms_txt = (TextView) convertView.findViewById(R.id.lys_entire_rooms_txt);
        TextView lys_entire_rooms_subtxt = (TextView) convertView.findViewById(R.id.lys_entire_rooms_subtxt);
        ImageView lys_entire_rooms_image = (ImageView) convertView.findViewById(R.id.lys_entire_rooms_image);

        lys_entire_rooms_txt.setText(  arrayListroomtype.get(position).get("name"));
        lys_entire_rooms_subtxt.setText(  arrayListroomtype.get(position).get("description"));


        return convertView;
    }
}
