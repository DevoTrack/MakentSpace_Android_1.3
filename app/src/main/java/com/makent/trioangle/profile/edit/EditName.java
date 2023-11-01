package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditName
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
                  Edit user name  Page
User give or edit first and last name
*************************************************************** */

public class EditName extends AppCompatActivity {

    RelativeLayout editname_title;
    LocalSharedPreferences localSharedPreferences;
    String firstname,lastname;
    EditText editname_firstname,editname_lastname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        localSharedPreferences=new LocalSharedPreferences(this);
        firstname=localSharedPreferences.getSharedPreferences(Constants.FirstName);
        lastname=localSharedPreferences.getSharedPreferences(Constants.LastName);


        editname_firstname=(EditText)findViewById(R.id.editname_firstname);
        editname_lastname=(EditText)findViewById(R.id.editname_lastname);

        editname_firstname.setText(firstname);
        editname_lastname.setText(lastname);

        editname_title=(RelativeLayout)findViewById(R.id.editname_title);

        editname_title.setOnClickListener(new View.OnClickListener(){
        public void onClick(View vw)
        {
            firstname=editname_firstname.getText().toString();
            lastname=editname_lastname.getText().toString();

            firstname= firstname.replaceAll("^\\s+|\\s+$", "");
            lastname= lastname.replaceAll("^\\s+|\\s+$", "");
            if(!firstname.equals(""))
            {
                localSharedPreferences.saveSharedPreferences(Constants.FirstName,firstname);
            }
            if(!lastname.equals(""))
            {
                localSharedPreferences.saveSharedPreferences(Constants.LastName,lastname);
            }
            onBackPressed();
        }
        });
    }
}
