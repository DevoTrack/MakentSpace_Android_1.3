package com.makent.trioangle.model;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model
 * @category    MakentTextWatcher
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.makent.trioangle.R;

public class MakentTextWatcher implements TextWatcher {
    String text;

    private View view;

    public MakentTextWatcher(View view) {
        this.view = view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        text = editable.toString();
        switch (view.getId()) {
            case R.id.login_email:
                if(!isValidEmail(text))
                {
                }
               // validate(1);
                break;
            case R.id.login_password:
               // validate(1);
                break;
        }
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //trioangle Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

