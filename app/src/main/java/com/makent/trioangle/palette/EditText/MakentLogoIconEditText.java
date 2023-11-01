package com.makent.trioangle.palette.EditText;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
/* ************************************************************
                  Custom EditText for set logo
*************************************************************** */

public class MakentLogoIconEditText extends EditText {
    public MakentLogoIconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Makent-Logo.ttf"));
    }
}
