package com.makent.trioangle.palette.Button;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Button
 * @category    MakentButton
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

public class MakentButton extends Button {

    public MakentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/CircularAir-Bold.otf"));
    }

}
