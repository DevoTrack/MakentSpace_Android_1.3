package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category   EditLocation
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;

/* ************************************************************
                  Edit user location or address Page
User give location or address for contact
*************************************************************** */

public class EditLocation extends AppCompatActivity {

    RelativeLayout editlocation_title;
    LocalSharedPreferences localSharedPreferences;
    String location;
    EditText edit_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        localSharedPreferences=new LocalSharedPreferences(this);
        location=localSharedPreferences.getSharedPreferences(Constants.Location);

        edit_location=(EditText) findViewById(R.id.edit_location);
        edit_location.setText(location);

        editlocation_title=(RelativeLayout)findViewById(R.id.editlocation_title);

        editlocation_title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View vw)
            {
                location=edit_location.getText().toString();
                location= location.replaceAll("^\\s+|\\s+$", "");
                if(!location.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Location,location);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Location,null);
                }
                onBackPressed();
            }
        });
    }
}
