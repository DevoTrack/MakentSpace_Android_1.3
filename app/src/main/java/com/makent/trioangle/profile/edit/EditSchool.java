package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditSchool
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
                  Edit school Page
User give or edit school name
*************************************************************** */

public class EditSchool extends AppCompatActivity {

    RelativeLayout editschool_title;
    LocalSharedPreferences localSharedPreferences;
    String school;
    EditText edit_school;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school);


        localSharedPreferences=new LocalSharedPreferences(this);
        school=localSharedPreferences.getSharedPreferences(Constants.School);

        edit_school=(EditText) findViewById(R.id.edit_school);
        edit_school.setText(school);


        editschool_title=(RelativeLayout)findViewById(R.id.editschool_title);

        editschool_title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View vw)
            {
                school=edit_school.getText().toString();

                school= school.replaceAll("^\\s+|\\s+$", "");
                if(!school.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.School,school);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.School,null);
                }
                onBackPressed();
            }
        });
    }
}
