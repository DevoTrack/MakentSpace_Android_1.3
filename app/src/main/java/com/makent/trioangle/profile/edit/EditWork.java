package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditWork
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
                  Edit job details Page
User give or edit job details
*************************************************************** */
public class EditWork extends AppCompatActivity {

    RelativeLayout editwork_title;
    LocalSharedPreferences localSharedPreferences;
    String work;
    EditText edit_work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work);

        localSharedPreferences=new LocalSharedPreferences(this);
        work=localSharedPreferences.getSharedPreferences(Constants.Work);

        edit_work=(EditText) findViewById(R.id.edit_work);
        edit_work.setText(work);

        editwork_title=(RelativeLayout)findViewById(R.id.editwork_title);

        editwork_title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View vw)
            {
                work=edit_work.getText().toString();

                work= work.replaceAll("^\\s+|\\s+$", "");

                if(!work.equals(""))
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Work,work);
                }else
                {
                    localSharedPreferences.saveSharedPreferences(Constants.Work,null);
                }
                onBackPressed();
            }
        });
    }
}
