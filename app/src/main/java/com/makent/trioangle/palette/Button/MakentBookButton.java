package com.makent.trioangle.palette.Button;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Button
 * @category    MakentBookButton
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/* ************************************************************
                  Custom button for set fonts
*************************************************************** */

public class MakentBookButton extends Button {

    public MakentBookButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/CircularAir-Book.otf"));
    }

}
