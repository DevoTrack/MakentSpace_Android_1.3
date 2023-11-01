package com.makent.trioangle.host.RoomsBeds;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelperListActivity extends AppCompatActivity implements ExperienceListHelperAdapter.OnItemClickListener {

    @BindView(R.id.rv_helper_list)
    public RecyclerView rvHelperList;
    ExperienceListHelperAdapter experienceListHelperAdapter;
    List<? extends BaseModel> helperList;
    private String value;

    @OnClick(R.id.selct_country_back)
    public void ivNext() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_list_helper);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        getIntentValues();
        init();

    }

    private void getIntentValues() {

        helperList = (ArrayList<? extends BaseModel>) getIntent().getSerializableExtra("list");
        value = getIntent().getStringExtra("value");


    }

    private void init() {

        rvHelperList.setHasFixedSize(true);
        rvHelperList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        experienceListHelperAdapter = new ExperienceListHelperAdapter(helperList, this, this, value);
        rvHelperList.setAdapter(experienceListHelperAdapter);

    }

    @Override
    public void onItemClick(int position, int ViewType) {

        Intent intent = new Intent();
        intent.putExtra("clickedPos", String.valueOf(position));
        setResult(ViewType, intent);
        finish();
    }
}
