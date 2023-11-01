package com.makent.trioangle.host;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.BedtypeAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BedtypeListActivity extends AppCompatActivity {


    private ArrayList<BedRoomBed> bedTypeList = new ArrayList<>();
    @BindView(R.id.rv_bedroom_list)
    public RecyclerView rvBedroomList;

    @BindView(R.id.tv_bedroom)
    public TextView tvBedroom;

    private BedtypeAdapter bedtypeAdapter;

    String bedTypeTitle;

    @OnClick(R.id.iv_close)
    public void ivClose() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedtype_list);

        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        bedTypeList = (ArrayList<BedRoomBed>) getIntent().getSerializableExtra("bedtypelist");
        bedTypeTitle = getIntent().getStringExtra("bedtypetitle");

        tvBedroom.setText(bedTypeTitle);

        rvBedroomList.setNestedScrollingEnabled(false);
        rvBedroomList.setHasFixedSize(true);

        bedtypeAdapter = new BedtypeAdapter(this, this, bedTypeList);
        rvBedroomList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBedroomList.setAdapter(bedtypeAdapter);
    }
}
