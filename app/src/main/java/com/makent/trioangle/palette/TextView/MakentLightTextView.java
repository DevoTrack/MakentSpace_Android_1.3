package com.makent.trioangle.palette.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/* ************************************************************
                  Custom TextView for set fonts
*************************************************************** */
public class MakentLightTextView extends TextView {

    public MakentLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/CircularAir-Light.otf"));
    }

}

