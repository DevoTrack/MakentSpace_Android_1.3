package com.makent.trioangle.palette.TextView;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/* ************************************************************
                  Custom TextView for set logo
*************************************************************** */
public class MakentLogoIcon extends TextView {
    public MakentLogoIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Makent-Logo.ttf"));
    }
}
