package com.makent.trioangle.createspace;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.createspace.model.hostlisting.readytohost.ExperienceTimeList;
import com.makent.trioangle.createspace.model.hostlisting.readytohost.SpaceTimings;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.RoomsBeds.HelperListActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.util.CommonMethods;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseTimeFragment extends Fragment implements AvailabilityTimeAdapter.OnItemClickListener {


    public AlertDialog dialog;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    Gson gson;
    public Context mContext;
    protected boolean isInternetAvailable;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @BindView(R.id.tv_add)
    TextView tvAdd;

    boolean updateTimeBool = true;

    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    List<ExperienceTimeList> experienceTimeLists = new ArrayList<>();
    List<SpaceTimings> multipleTime = new ArrayList<>();
    View view;
    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;

    private AvailabilityTimeAdapter experienceTimeAdapter;
    private int position;
    private String timeType = "";
    private String timeZone;

    /*
     *   Fragment to show driver weekly earning
     **/
    public static ChooseTimeFragment newInstance() {
        ChooseTimeFragment fragment = new ChooseTimeFragment();
        return fragment;
    }


    private boolean isTimeEmpty() {

        for (int i = 0; i < experienceTimeLists.size(); i++) {
            if (experienceTimeLists.get(i).getStartTime().equals("") || experienceTimeLists.get(i).getEndTime().equals("")) {
                return false;
            }
        }

        return true;
    }

    @OnClick(R.id.tv_add)
    public void add() {
        ExperienceTimeList experienceTimeList = new ExperienceTimeList();
        experienceTimeList.setStartTime("");
        experienceTimeList.setEndTime("");
        experienceTimeList.setId("");
        experienceTimeLists.add(experienceTimeList);
        experienceTimeAdapter.notifyDataSetChanged();

        addVisibility();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_choose_time, container, false);
        localSharedPreferences = new LocalSharedPreferences(mContext);
        AppController.getAppComponent().inject(this);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {

        dialog = commonMethods.getAlertDialog(mContext);

        mydialog = new Dialog_loading(mContext);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        tvNext.setText(mContext.getResources().getString(R.string.next));

        initMultipleTime();

        initExperienceTime();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        //experienceTimeAdapter = new AvailabilityTimeAdapter(experienceTimeLists, timeZone, mContext, this);
        rvTime.setLayoutManager(mLayoutManager);
        rvTime.setAdapter(experienceTimeAdapter);
        rvTime.setNestedScrollingEnabled(false);

    }

    private void initExperienceTime() {

        experienceTimeLists.clear();

        for (int i = 0; i < experienceTimeLists.size(); i++) {
            experienceTimeLists.get(i).setStartTime(_24To12Hours(experienceTimeLists.get(i).getStartTime()));
            experienceTimeLists.get(i).setEndTime(_24To12Hours(experienceTimeLists.get(i).getEndTime()));
            experienceTimeLists.get(i).setSubList(multipleTime.subList(timeId(experienceTimeLists.get(i).getStartTime()), timeId(experienceTimeLists.get(i).getEndTime())));
        }

        if (experienceTimeLists.size() == 0) {
            ExperienceTimeList experienceTime = new ExperienceTimeList();
            experienceTime.setStartTime("");
            experienceTime.setEndTime("");
            experienceTime.setId("");
            experienceTimeLists.add(experienceTime);
        }

        addVisibility();

    }

    private void initMultipleTime() {

        String[] multipleTimeArray = getResources().getStringArray(R.array.hour);
        SpaceTimings experienceTimings;
        for (int i = 0; i < multipleTimeArray.length; i++) {
            experienceTimings = new SpaceTimings();
            experienceTimings.setTime(multipleTimeArray[i]);
            experienceTimings.setTimeZone("");
            experienceTimings.setId(i);
            multipleTime.add(experienceTimings);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onRemoveClicked(int position) {
        this.position = position;


        if (experienceTimeLists.get(position).getId().equals("")) {
            experienceTimeLists.remove(position);
            experienceTimeAdapter.notifyDataSetChanged();
            tvAdd.setVisibility(View.VISIBLE);
        } else {
            removeTime();
        }

    }

    @Override
    public void onItemClick(int position, String selectedItem, String timeType) {


        Intent listHelper = new Intent(getActivity(), HelperListActivity.class);
        listHelper.putExtra("list", (Serializable) multipleTime);
        listHelper.putExtra("value", selectedItem);
        this.position = position;
        this.timeType = timeType;
        startActivityForResult(listHelper, Constants.ViewType.MultipleTime);


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (resultCode == Constants.ViewType.MultipleTime) {
                int selectedPosition = Integer.parseInt(data.getStringExtra("clickedPos"));
                if (position != -1) {


                    if (timeType.equals("start")) {


                        if (experienceTimeLists.get(position).getEndTime().equals("") || multipleTime.get(selectedPosition).getId() < timeId(experienceTimeLists.get(position).getEndTime()) - 1) {


                            if (!isValueinSublist(multipleTime.get(selectedPosition).getId())) {
                                experienceTimeLists.get(position).setStartTime(multipleTime.get(selectedPosition).getTime());

                            } else {
                                updateTimeBool = false;
                                Toast.makeText(getActivity(), getString(R.string.time_taken), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            updateTimeBool = false;
                            Toast.makeText(getActivity(), getString(R.string.starttime_lesser_error_msg), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        if (experienceTimeLists.get(position).getStartTime().equals("") || multipleTime.get(selectedPosition).getId() > timeId(experienceTimeLists.get(position).getStartTime()) + 1) {


                            if (!isValueinSublist(multipleTime.get(selectedPosition).getId())) {
                                experienceTimeLists.get(position).setEndTime(multipleTime.get(selectedPosition).getTime());
                            } else {
                                updateTimeBool = false;
                                Toast.makeText(getActivity(), getString(R.string.time_taken), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            updateTimeBool = false;
                            Toast.makeText(getActivity(), getString(R.string.end_time_greater_error_msg), Toast.LENGTH_SHORT).show();
                        }


                    }

                    if (!experienceTimeLists.get(position).getStartTime().equals("") && !experienceTimeLists.get(position).getEndTime().equals("") && updateTimeBool) {
                        //experienceTimeLists.get(position).setSubList(policyList.subList(timeId(experienceTimeLists.get(position).getStartTime()), timeId(experienceTimeLists.get(position).getEndTime())));

                        updateTime();
                        experienceTimeAdapter.notifyDataSetChanged();
                        addVisibility();
                    } else {
                        updateTimeBool = true;
                        experienceTimeAdapter.notifyDataSetChanged();
                    }

                }


            }
        }
    }

    private void updateTime() {

        isInternetAvailable = commonMethods.isOnline(mContext);
        if (isInternetAvailable) {

        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }


    }

    private void removeTime() {

        isInternetAvailable = commonMethods.isOnline(mContext);
        if (isInternetAvailable) {


        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }


    }


    private String _12To24Hours(String time) {

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = parseFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));

        return displayFormat.format(date);
    }


    private String _24To12Hours(String time) {

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = parseFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));

        return displayFormat.format(date);
    }

    public void addVisibility() {
        for (int i = 0; i < experienceTimeLists.size(); i++) {
            if (experienceTimeLists.get(i).getSubList().size() == 0) {
                tvAdd.setVisibility(View.VISIBLE);
                break;
            } else {
                tvAdd.setVisibility(View.VISIBLE);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isValueinSublist(int timeId) {
        boolean timeIdExists = false;
        boolean quitDoubleLoop = false;
        for (int i = 0; i < experienceTimeLists.size() && !quitDoubleLoop; i++) {
            if (position == i)
                continue;
            for (int j = 0; j < experienceTimeLists.get(i).getSubList().size() && !quitDoubleLoop; j++) {

                int finalI = i;
                int finalJ = j;
                timeIdExists = experienceTimeLists.get(i).getSubList().stream().anyMatch(user -> timeId == experienceTimeLists.get(finalI).getSubList().get(finalJ).getId());
                if (timeIdExists) {
                    quitDoubleLoop = true;
                }
            }
        }
        return timeIdExists;
    }

    private boolean rangeCheck(int id) {

        for (int i = 0; i < experienceTimeLists.size(); i++) {
            if (id >= timeId(experienceTimeLists.get(i).getStartTime()) && id < timeId(experienceTimeLists.get(i).getEndTime())) {
                return false;
            }
        }
        return true;
    }

    /*private boolean rangeCheck(int id) {

        for(int i=0;i<experienceTimeLists.size();i++){
            if(id>=timeId(experienceTimeLists.get(i).getStartTime())&&id<timeId(experienceTimeLists.get(i).getEndTime())){
                return false;
            }
        }
        return true;
    }*/

    private int timeId(String timeId) {
        for (int i = 0; i < multipleTime.size(); i++) {
            if (timeId.equals(multipleTime.get(i).getTime())) {
                return multipleTime.get(i).getId();
            }
        }
        return -1;
    }


}