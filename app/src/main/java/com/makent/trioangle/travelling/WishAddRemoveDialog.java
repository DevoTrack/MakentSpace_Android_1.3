package com.makent.trioangle.travelling;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.ConnectionDetector;

public class WishAddRemoveDialog extends Dialog implements  ServiceListener {

    public LocalSharedPreferences localSharedPreferences;

    public Activity c;
    String roomid;

    public WishAddRemoveDialog(Activity a) {
        super(a, R.style.DialogTheme);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    public WishAddRemoveDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wishlist_add_remove);
        Window window = getWindow();

        localSharedPreferences = new LocalSharedPreferences(getContext());

        roomid=localSharedPreferences.getSharedPreferences(Constants.WishlistRoomId);




        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        wlp.dimAmount=0.65f;
        wlp.width=WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);


    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(getContext());
        return connectionDetector;
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

    }


}
