package com.makent.trioangle.palette.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/* ************************************************************
                  Custom TextView for set logo in amenities
*************************************************************** */

public class MakentAmenitiesTextView extends TextView {

    public MakentAmenitiesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/makent-amenities.ttf"));
    }

}
