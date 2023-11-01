package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestWishListActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.SavedWishListAdapter;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.Makent_model;

import java.util.ArrayList;
import java.util.List;
/* ***********************************************************************
This is WishList Page Contain Saved WishList
**************************************************************************  */
public class WishListActivity extends AppCompatActivity {

    String roomImgItems[] = {"http://makentspace.trioangle.com/images/home_cities/home_city_1461439512.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439589.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439550.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439621.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439819.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439887.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439922.jpg", "http://makentspace.trioangle.com/images/home_cities/home_city_1461439957.jpg"};
    RecyclerView recyclerView;
    List<Makent_model> searchlist;
    SavedWishListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        recyclerView = (RecyclerView) findViewById(R.id.wishlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        searchlist = new ArrayList<>();
        listAdapter.setLoadMoreListener(new SavedWishListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = searchlist.size() - 1;
                        //   loadMore(index);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdapter);
        load(0);
    }
    public Header getHeader()
    {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }
    private void load(int index) {

        if (roomImgItems.length > 0) {

            for (int i = 0; i < roomImgItems.length; i++) {
                Makent_model listdata = new Makent_model();
                listdata.setWishlistImage(roomImgItems[i]);

                searchlist.add(listdata);
            }
            listAdapter.notifyDataChanged();
        }

    }
}