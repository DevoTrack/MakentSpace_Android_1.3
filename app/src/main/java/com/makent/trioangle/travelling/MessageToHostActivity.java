package com.makent.trioangle.travelling;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category MessageToHostActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

import static com.makent.trioangle.helper.Constants.MessageToHost;
import static com.makent.trioangle.helper.Constants.REQUEST_HOST_MESSAGE;

/* ***********************************************************************
This is MessageToHost Page Contain to Host
**************************************************************************  */
public class MessageToHostActivity extends AppCompatActivity {
    public @Inject
    CommonMethods commonMethods;

    ImageView messagehost_close, message_to_hostprofile;
    TextView messagehost_done;
    TextView msg_host;
    EditText messagehost_etxt;
    LocalSharedPreferences localSharedPreferences;
    String hostprofile, reqMessagetext, hostprofilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_to_host);
        commonMethods = new CommonMethods();

        localSharedPreferences = new LocalSharedPreferences(this);
        messagehost_etxt = (EditText) findViewById(R.id.messagehost_etxt);
        msg_host         = (TextView) findViewById(R.id.room_details_hostedby);
        messagehost_close = (ImageView) findViewById(R.id.messagehost_close);
        message_to_hostprofile = (ImageView) findViewById(R.id.message_to_hostprofile);

        reqMessagetext = localSharedPreferences.getSharedPreferences(Constants.ReqMessage);
        Log.e("MessageHostActivity","MessageHostActivity");

        messagehost_etxt.setText(reqMessagetext);
        messagehost_etxt.setFilters(getFilter());
        messagehost_etxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        Intent x = getIntent();
        hostprofile = x.getStringExtra("hostprofile");
        hostprofilename = x.getStringExtra("hostprofilename");

        msg_host.setText("Tell suite " + hostprofilename +" a bit about yourself and your trip.");

        Glide.with(getApplicationContext()).asBitmap().load(hostprofile).into(new BitmapImageViewTarget(message_to_hostprofile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                message_to_hostprofile.setImageDrawable(circularBitmapDrawable);
            }
        });
        commonMethods.rotateArrow(messagehost_close,this);
        // On Click function used to click action for check Email id in server send link to Email
        messagehost_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                finish();
            }
        });

        messagehost_done = (TextView) findViewById(R.id.messagehost_done);
        // On Click function used to click action for check Email id in server send link to Email
        messagehost_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                String message = messagehost_etxt.getText().toString();
                if (!message.equals("null") && !message.isEmpty()) {
                    localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, message);
                    localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 1);
                }else {
                    localSharedPreferences.saveSharedPreferences(Constants.ReqMessage, null);
                    localSharedPreferences.saveSharedPreferences(Constants.stepHostmessage, 0);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

        public static InputFilter[] getFilter()
        {
            InputFilter EMOJI_FILTER = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int index = start; index < end; index++) {

                        int type = Character.getType(source.charAt(index));

                        if (type == Character.SURROGATE || type==Character.NON_SPACING_MARK
                                || type==Character.OTHER_SYMBOL) {
                            return "";
                        }
                    }
                    return null;
                }
            };
            return new InputFilter[]{EMOJI_FILTER};
        }

}
