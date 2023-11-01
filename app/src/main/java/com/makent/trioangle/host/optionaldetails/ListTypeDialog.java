package com.makent.trioangle.host.optionaldetails;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails
 * @category    ListTypeDialog
 * @author      Trioangle Product Team
 * @version     1.1
 */


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
/* ************************************************************
Show List of house type // Entire room,Private room, Shared room Static page as dialog
*************************************************************** */

public class ListTypeDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public RelativeLayout dialog_entire_rooms,dialog_private_rooms,dialog_shared_rooms;
    TextView propertytype;
    LocalSharedPreferences localSharedPreferences;

    public ListTypeDialog(Activity a,TextView propertytype) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.propertytype=propertytype;
        localSharedPreferences=new LocalSharedPreferences(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listtype_dialog);
        dialog_entire_rooms = (RelativeLayout) findViewById(R.id.dialog_entire_rooms);
        dialog_private_rooms = (RelativeLayout) findViewById(R.id.dialog_private_rooms);
        dialog_shared_rooms = (RelativeLayout) findViewById(R.id.dialog_shared_rooms);

        dialog_entire_rooms.setOnClickListener(this);
        dialog_private_rooms.setOnClickListener(this);
        dialog_shared_rooms.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_entire_rooms:
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName,c.getResources().getString(R.string.entire_place));
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,"1");
                this.propertytype.setText(c.getResources().getString(R.string.entire_place));
                dismiss();
                break;
            case R.id.dialog_private_rooms:
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName,c.getResources().getString(R.string.private_room));
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,"2");
                this.propertytype.setText(c.getResources().getString(R.string.private_room));
                dismiss();
                break;
            case R.id.dialog_shared_rooms:
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomTypeName,c.getResources().getString(R.string.shared_room));
                localSharedPreferences.saveSharedPreferences(Constants.ListRoomType,"3");
                this.propertytype.setText(c.getResources().getString(R.string.shared_room));
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}