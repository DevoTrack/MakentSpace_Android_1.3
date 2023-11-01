package com.makent.trioangle.adapter.host;

/**
 * @package com.makent.trioangle
 * @subpackage adapter/host
 * @category RoomImageViewAdapter
 * @author Trioangle Product Team
 * @version 1.1
 * <p>
 * Created by test on 2/27/16.
 */

/**
 * Created by test on 2/27/16.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makent.trioangle.R;
import com.makent.trioangle.model.host.RoomImageItem;

import java.util.ArrayList;

/**
 * Created by Eugene Alvizo on 12/26/2014.
 */
public class RoomImageViewAdapter extends BaseAdapter {

    public Context activity;
    public ArrayList data;
    public static LayoutInflater inflater = null;
    public View vi;
    public ViewHolder viewHolder;
    ProgressBar progress;


    public Boolean showDelete = false;


    ArrayList<String> selectedStrings = new ArrayList<String>();


    public RoomImageViewAdapter(Context context, ArrayList<RoomImageItem> items) {
        this.activity = context;
        this.data = items;
        Log.d("Adapter", "Room Items " + this.data.size());
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        vi = view;
        //Populate the Listview
        final int pos = position;
        final RoomImageItem items = (RoomImageItem) data.get(pos);
        if (view == null) {
            vi = inflater.inflate(R.layout.gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) vi.findViewById(R.id.checkBox);
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
            progress = (ProgressBar) vi.findViewById(R.id.progressBar1);
            viewHolder.image = (ImageView) vi.findViewById(R.id.imageView26);

            vi.setTag(viewHolder);

        } else viewHolder = (ViewHolder) view.getTag();


        if (showDelete) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(false);
        } else {
            viewHolder.checkBox.setVisibility(View.INVISIBLE);

        }

        /*ProgressItem mItem = getItem(position);
        progress.setProgress(mItem.getProgress());*/


        try {
            //  if((!items.getImage().equals("null"))&&items.getImage()!=null){

            //progress.setVisibility(View.VISIBLE);
            System.out.println("adapter ivPhoto " + items.getImage());
            Glide.with(activity.getApplicationContext()).load(items.getImage())

                    .into(new DrawableImageViewTarget(viewHolder.image) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });

    /*}

    else{
       viewHolder.ivPhoto.setImageResource(R.drawable.default_photo_bg);
    }*/

        } catch (Exception e) {
        }


        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((isChecked)) {

                    selectedStrings.add(items.getId());
                    System.out.println("add");
                    System.out.println("checkkkkkkkkkkkkkyyyyyyyyyyyyyyyyy" + selectedStrings);

                } else {

                    selectedStrings.remove(items.getId());
                    System.out.println("checkkkkkkkkkkkkk" + selectedStrings);

                }
            }
        });

            return vi;
    }


    public ArrayList getAllData() {
        return data;
    }


    public ArrayList<String> getSelectedString() {
        return selectedStrings;
    }

    public void clearSelectedString() {
        selectedStrings.clear();
    }

    public class ViewHolder {
        CheckBox checkBox;
        ImageView image;
    }
/*
    public class ProgressItem {
        private int mProgress;

        public ProgressItem(int mProgress) {
            this.mProgress = mProgress;
        }

        public int getProgress() {

            return mProgress;
        }
    }*/

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }


}
