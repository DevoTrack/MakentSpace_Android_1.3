package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditPhone
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

/* ************************************************************
                  Edit phone number Page
User give or edit phone number with verification
*************************************************************** */


public class EditPhone extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout editphone_title;
    Button editphone_sendcode;
    LocalSharedPreferences localSharedPreferences;
    String phone;
    EditText edit_phone;
    ImageView editphone_back;

    public @Inject
    CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        commonMethods = new CommonMethods();
        localSharedPreferences=new LocalSharedPreferences(this);
        phone=localSharedPreferences.getSharedPreferences(Constants.Phone);

        edit_phone=(EditText) findViewById(R.id.edit_phone);
        edit_phone.setText(phone);

        editphone_sendcode=(Button)findViewById(R.id.editphone_sendcode);
        editphone_title=(RelativeLayout)findViewById(R.id.editphone_title);
        editphone_back =(ImageView)findViewById(R.id.editphone_back);
        commonMethods.rotateArrow(editphone_back,this);

        editphone_title.setOnClickListener(this);
        editphone_sendcode.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.editphone_sendcode:
            {
                phone=edit_phone.getText().toString();
                phone= phone.replaceAll("^\\s+|\\s+$", "");

                if(!phone.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone,phone);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone,null);
                }
                onBackPressed();
            }break;
            case R.id.editphone_title:
            {
                phone=edit_phone.getText().toString();
                phone= phone.replaceAll("^\\s+|\\s+$", "");
                if(!phone.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone,phone);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Phone,null);
                }

                onBackPressed();
            }break;
        }
    }
}
