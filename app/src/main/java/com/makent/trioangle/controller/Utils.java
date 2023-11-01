package com.makent.trioangle.controller;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  controller
 * @category    Utils
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Context;
import android.content.res.TypedArray;

import com.makent.trioangle.R;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }


}
