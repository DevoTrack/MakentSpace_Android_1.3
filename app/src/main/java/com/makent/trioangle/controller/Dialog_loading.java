package com.makent.trioangle.controller;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  controller
 * @category    Dialog_loading
 * @author      Trioangle Product Team
 * @version     1.1
 */



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.makent.trioangle.R;

public class Dialog_loading extends Dialog{

    Context context;
    public Dialog_loading(Context context) {
        super(context);
        this.context=context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_anim_dialog);
       // getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imageView1 = (ImageView) findViewById(R.id.loadinggifImageView);

       AnimationDrawable frameAnimation =    (AnimationDrawable)imageView1.getDrawable();
        frameAnimation.setCallback(imageView1);
        frameAnimation.setVisible(true, true);
        frameAnimation.start();
        //imageView1.setVisibility(View.GONE);
       /* GlideDrawableImageViewTarget imageViewTarget1 = new GlideDrawableImageViewTarget(imageView1);
        Glide.with(context).load(R.drawable.anim_loader4).into(imageViewTarget1);*/

    }

}
