package com.makent.trioangle.palette.EditText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/* ************************************************************
                  Custom EditText for set fonts
*************************************************************** */
public class MakentEditText extends EditText {

    public MakentEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/CircularAir-Bold.otf"));
    }

}
