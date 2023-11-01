package com.makent.trioangle.palette.Button;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Button
 * @category    MakentAwesomeButton
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.makent.trioangle.R;
/* ************************************************************
                  Custom button for set Logo
*************************************************************** */
public class MakentAwesomeButton extends Button {
    public MakentAwesomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.fonts_logo)));
    }
}
