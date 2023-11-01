package com.makent.trioangle.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/tabs
 * @category    HostLYS_Step2_AddTitleActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/* ***********************************************************************
This is List Your Space Step2  Contain AddTitle
**************************************************************************  */
public class LYS_Step2_AddTitle extends AppCompatActivity implements ServiceListener{

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    RelativeLayout describe_addtitle_title,describe_title;
    LocalSharedPreferences localSharedPreferences;
    String roomtitle,title;
    EditText addtitle_edittext;
    TextView describe_character_count_txt;
    String userid,roomid;
    ImageView describe_dot_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_setp2_add_title);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        describe_dot_loader = (ImageView) findViewById(R.id.describe_dot_loader);
        describe_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(describe_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);


        addtitle_edittext=(EditText)findViewById(R.id.addtitle_edittext);
        describe_character_count_txt=(TextView) findViewById(R.id.describe_character_count_txt);

        describe_character_count_txt.setText("35"+" "+getResources().getString(R.string.characters));

        addtitle_edittext.addTextChangedListener(mTextEditorWatcher);

        localSharedPreferences=new LocalSharedPreferences(this);
        userid=localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        roomtitle=localSharedPreferences.getSharedPreferences(Constants.ListRoomTitle);

        if(roomtitle!=null&&!roomtitle.equals("")) {
            addtitle_edittext.setText(roomtitle);
        }

        describe_title=(RelativeLayout) findViewById(R.id.describe_title);
        describe_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                title=addtitle_edittext.getText().toString();
                title= title.replaceAll("^\\s+|\\s+$", "");

                if(roomtitle==null||roomtitle.equals("")||roomtitle.equals(title)||title.equals(""))
                {
                    if(!title.equals(""))
                    {
                        updateTitle();
                    }else {
                        onBackPressed();
                        finish();
                    }
                }else
                {
                    updateTitle();
                }
            }
        });

        describe_addtitle_title=(RelativeLayout) findViewById(R.id.describe_addtitle_title);
        describe_addtitle_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                title=addtitle_edittext.getText().toString();
                title= title.replaceAll("^\\s+|\\s+$", "");

                if(roomtitle==null||roomtitle.equals("")||roomtitle.equals(title)||title.equals(""))
                {
                    if(!title.equals(""))
                    {
                        updateTitle();
                    }else {
                        onBackPressed();
                        finish();
                    }
                }else
                {
                    updateTitle(); // this is used to update title call api function
                }
            }
        });

    }

    private final TextWatcher mTextEditorWatcher=new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            describe_character_count_txt.setText(String.valueOf(35-s.length())+" "+getResources().getString(R.string.characters));
        }
    };

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        onSuccessTitle();
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        describe_title.setVisibility(View.VISIBLE);
        describe_dot_loader.setVisibility(View.GONE);
    }

    public void updateTitle(){
        describe_title.setVisibility(View.GONE);
        describe_dot_loader.setVisibility(View.VISIBLE);
        localSharedPreferences.saveSharedPreferences(Constants.ListRoomTitle,title);

        apiService.updateTitledescription(userid,roomid,title).enqueue(new RequestCallback(this));
    }

    public void onSuccessTitle(){
        describe_title.setVisibility(View.GONE);
        describe_dot_loader.setVisibility(View.VISIBLE);

        finish();
    }


}
