package com.makent.trioangle.newhome.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private String items;

    public SpacesItemDecoration(int space,String items) {
        this.space = space;
        this.items = items;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!items.equalsIgnoreCase("detailAdapter")) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
        }else {
            outRect.left = space;
            outRect.right = space;
        }
    }
}
