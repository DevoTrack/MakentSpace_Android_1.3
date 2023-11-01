package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditAboutme
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
                  Edit About me Page
Write about user details
*************************************************************** */
public class EditAboutme extends AppCompatActivity {

    RelativeLayout editaboutme_title;
    EditText edit_aboutme;
    LocalSharedPreferences localSharedPreferences;
    String aboutme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aboutme);

        localSharedPreferences=new LocalSharedPreferences(this);
        aboutme=localSharedPreferences.getSharedPreferences(Constants.AboutMe);

        edit_aboutme=(EditText)findViewById(R.id.edit_aboutme);
        if(aboutme!=null)
        {
            edit_aboutme.setText(aboutme);
        }
        editaboutme_title=(RelativeLayout)findViewById(R.id.editaboutme_title);

        editaboutme_title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View vw)
            {
              aboutme=edit_aboutme.getText().toString();
                aboutme= aboutme.replaceAll("^\\s+|\\s+$", "");
                if(!aboutme.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.AboutMe,aboutme);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.AboutMe,null);
                }
                onBackPressed();
            }
        });
    }
}
