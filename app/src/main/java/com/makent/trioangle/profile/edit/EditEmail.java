package com.makent.trioangle.profile.edit;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  Profile/edit
 * @category    EditEmail
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;

/* ************************************************************
                  Edit email address Page
User give Email address for contact
*************************************************************** */
public class EditEmail extends AppCompatActivity {

    RelativeLayout editemail_title;
    LocalSharedPreferences localSharedPreferences;
    String email;
    EditText edit_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        localSharedPreferences=new LocalSharedPreferences(this);
        email=localSharedPreferences.getSharedPreferences(Constants.Email);

        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_email.setText(email);
        edit_email.addTextChangedListener(new EmailTextWatcher(edit_email));
        editemail_title=(RelativeLayout)findViewById(R.id.editemail_title);

        editemail_title.setOnClickListener(new View.OnClickListener(){
            public void onClick(View vw)
            {
                if(validateEmail())
                {
                    email=edit_email.getText().toString();
                    email= email.replaceAll("^\\s+|\\s+$", "");
                    if(!email.equals(""))
                    {
                        localSharedPreferences.saveSharedPreferences(Constants.Email,email);
                    }
                    onBackPressed();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(editemail_title, "Enter valid email address", Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                    snackbar.show();
                }

            }
        });
    }

    // Check edit email address field validation
    private class EmailTextWatcher implements TextWatcher {

        private View view;

        private EmailTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.edit_email:
                    validateEmail();
                    break;

            }

            if(validateEmail())
            {
               // forgot_next.setAlpha(1f);
              //  forgot_next.setEnabled(true);
            }else {
              //  forgot_next.setAlpha(.5f);
              //  forgot_next.setEnabled(false);
            }
        }
    }

    private boolean validateEmail() {

        String email = edit_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {

            return false;
        } else {
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
