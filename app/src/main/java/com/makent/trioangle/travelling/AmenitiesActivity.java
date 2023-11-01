package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestAmenitiesActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.AmenitiesListAdapter;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.palette.ListView.MakentListView;
import com.makent.trioangle.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AmenitiesActivity extends Activity implements MakentListView.IXListViewListener {


    private List<Makent_model> searchList = new ArrayList<Makent_model>();
    private ArrayList<String> items = new ArrayList<String>();

    private static int refreshCnt = 0;
    private AmenitiesListAdapter adapter;
    MakentListView listView;

    TextView amenities_close;
    String roomImgItems[];
    String amenitiesIcon[];
    public @Inject
    CommonMethods commonMethods;

    //String roomImgItems[] = {"Wifi","TV","MiniBar","Dining","Vending","Exercise","Recreation","Swimming pools","Kitchen facilities","Television","Computer and Internet access","Personal items","Hair dryer"};
    int start=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenities);
        commonMethods = new CommonMethods();

        Bundle b=this.getIntent().getExtras();
        roomImgItems=b.getStringArray("amenities");
        amenitiesIcon=b.getStringArray("amenitiesimages");
        listView = (MakentListView)findViewById(R.id.amenities_list);
        adapter = new AmenitiesListAdapter(this, searchList);
        listView.setAdapter(adapter);
        System.out.print("listView"+listView.getCount());



        listView.setItemsCanFocus(false);
        listView.setPullLoadEnable(false);// Footer load enalbe
        listView.setXListViewListener(this);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSmoothScrollbarEnabled(true);
        listView.setScrollingCacheEnabled(false);
        listView.hide();


        for(int i=0;i<roomImgItems.length;i++) {
            Makent_model listdata = new Makent_model();
            listdata.setAmenities(roomImgItems[i]);
            listdata.setAmenities_image(amenitiesIcon[i]);

            searchList.add(listdata);
        }

        adapter.notifyDataSetChanged();

        amenities_close=(TextView)findViewById(R.id.amenities_close);
        commonMethods.setTvAlign(amenities_close,this);
// On Click function used to click action for check Email id in server send link to Email
        amenities_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("3");
    }

    public void onRefresh() {
        System.out.println("refresh");

        start = ++refreshCnt;
        items.clear();



        onLoad(); //This function is used to listview  stop reload and stop refersh
    }




    public void onLoadMore() {
        //   new TestAsync().execute();



    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
