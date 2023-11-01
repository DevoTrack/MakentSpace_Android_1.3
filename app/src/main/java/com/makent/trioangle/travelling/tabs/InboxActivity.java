package com.makent.trioangle.travelling.tabs;

/**
 * @package com.makent.trioangle
 * @subpackage travelling/tabs
 * @category GuestInboxActivity
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.MainActivity;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.InboxListAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.inbox.InboxDataModel;
import com.makent.trioangle.model.inbox.InboxResult;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class InboxActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    Snackbar snackbar;
    private SwipeRefreshLayout swipeContainer,swipeContainerstart;
    RecyclerView listView;
    List<Makent_model> searchlist;
    InboxListAdapter adapter;
    Context context;
    public EditText edt;
    ImageView inbox_dot_loader;
    RelativeLayout emptyinbox, listinbox;
    Button inbox_start_explore;
    LocalSharedPreferences localSharedPreferences;
    String userid;
    private int backPressed = 0;    // used by onBackPressed()
    protected boolean isInternetAvailable;
    public InboxResult inboxResult;
    public ArrayList<InboxDataModel> inboxDataModels = new ArrayList<>();

    public View viewNotoken;
    public TextView tvTitle, tvdescription;
    public Button btnNoTokenLogin;
    public ImageView ivNoToken,notification;
    RelativeLayout.LayoutParams layoutparams;
    private boolean doubleBackToExitPressedOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox_tab);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        commonMethods = new CommonMethods();
        edt = (EditText) findViewById(R.id.edt);

        Log.e("InboxActivity","InboxActivity");

        viewNotoken = (View) findViewById(R.id.viewNotoken);
        btnNoTokenLogin = (Button) findViewById(R.id.btnNoTokenLogin);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvdescription = (TextView) findViewById(R.id.tvdescription);
        ivNoToken = (ImageView) findViewById(R.id.ivNoToken);
        notification =(ImageView)findViewById(R.id.notification);
        swipeContainerstart = (SwipeRefreshLayout)findViewById(R.id.swipeContainerstart);
        if(getResources().getString(R.string.layout_direction).equals("1")){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) notification.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            notification.setLayoutParams(lp);
            //layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        else{
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) notification.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            notification.setLayoutParams(lp);
        }

        btnNoTokenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                home.putExtra("isback", 1);
                startActivity(home);
            }
        });

        if (!TextUtils.isEmpty(localSharedPreferences.getSharedPreferences(Constants.AccessToken))) {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"");
            viewNotoken.setVisibility(View.GONE);
            inbox_dot_loader = (ImageView) findViewById(R.id.inbox_dot_loader);
            DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(inbox_dot_loader);
            Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

            emptyinbox = (RelativeLayout) findViewById(R.id.emptyinbox);
            listinbox = (RelativeLayout) findViewById(R.id.listinbox);

            inbox_dot_loader.setVisibility(View.VISIBLE);
            emptyinbox.setVisibility(View.GONE);
            swipeContainerstart.setVisibility(View.GONE);

            listinbox.setVisibility(View.GONE);

            inbox_start_explore = (Button) findViewById(R.id.inbox_start_explore);
            inbox_start_explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                    x.putExtra("tabsaved", 0);
                    startActivity(x);
                    finish();
                }
            });
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

            listView = (RecyclerView) findViewById(R.id.inboxlist);
            searchlist = new ArrayList<>();
            adapter = new InboxListAdapter(getHeader(), this, inboxDataModels);
            adapter.setLoadMoreListener(new InboxListAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            int index = searchlist.size() - 1;
                            //   loadMore(index);
                        }
                    });
                    //Calling loadMore function in Runnable to fix the
                    // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
                }
            });
            if(getResources().getString(R.string.layout_direction).equals("1")){
                listView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else{
                listView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            listView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//            linearLayoutManager.setStackFromEnd(true);
            listView.setLayoutManager(linearLayoutManager);
            //recyclerView.addItemDecoration(new VerticalLineDecorator(2));
            listView.setAdapter(adapter);
            // api = ServiceGenerator.createService(MoviesApi.class);
            // load(0);

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    adapter.clear();
                    loadInbox();
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

            swipeContainerstart.setOnRefreshListener((new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeContainerstart.setRefreshing(true);
                    refreshList();
                    adapter.clear();
                    loadInbox();
                }
            }));
            swipeContainerstart.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

            if (isInternetAvailable) {
                loadInbox();
            } else {
                snackBar();
             //   commonMethods.snackBar(getResources().getString(R.string.interneterror), getResources().getString(R.string.retry), true, 2, edt, edt, getResources(), this);
            }
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.LastPage,"inbox");
            viewNotoken.setVisibility(View.VISIBLE);
            tvTitle.setText(getResources().getString(R.string.inbox));
            tvdescription.setText(getResources().getString(R.string.no_token_inbox));
            ivNoToken.setImageDrawable(getResources().getDrawable(R.drawable.token_inbox));
        }
    }

    private void refreshList() {
        swipeContainerstart.setRefreshing(false);

    }

    public Header getHeader() {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        swipeContainer.setRefreshing(false);
        if (jsonResp.isSuccess()) {
            onSuccessInbox(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            inbox_dot_loader.setVisibility(View.GONE);
            emptyinbox.setVisibility(View.VISIBLE);
            swipeContainerstart.setVisibility(View.VISIBLE);
            listinbox.setVisibility(View.GONE);
            commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, edt, getResources(), this);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        inbox_dot_loader.setVisibility(View.GONE);
        emptyinbox.setVisibility(View.VISIBLE);
        swipeContainerstart.setVisibility(View.VISIBLE);
        listinbox.setVisibility(View.GONE);
        commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, edt, edt, getResources(), this);
    }

    public void loadInbox() {
        inbox_dot_loader.setVisibility(View.VISIBLE);
        emptyinbox.setVisibility(View.GONE);
        swipeContainerstart.setVisibility(View.GONE);
        listinbox.setVisibility(View.GONE);
        apiService.inboxList(localSharedPreferences.getSharedPreferences(Constants.AccessToken), "inbox").enqueue(new RequestCallback(this));
    }

    public void onSuccessInbox(JsonResponse jsonResp) {
        inboxDataModels.clear();
        inboxResult = gson.fromJson(jsonResp.getStrResponse(), InboxResult.class);
        ArrayList<InboxDataModel> inb = inboxResult.getData();
        inboxDataModels.addAll(inb);
        if(inboxDataModels.size()>=1) {
            try {
                JSONObject response = new JSONObject(jsonResp.getStrResponse());
                JSONArray messages = response.getJSONArray("data");

                localSharedPreferences.saveSharedPreferences(Constants.ReservationCount, messages.length());
                localSharedPreferences.saveSharedPreferences(Constants.ReservationDetails, messages.toString());
                long current_millis = System.currentTimeMillis();
                localSharedPreferences.saveSharedPreferences(Constants.ReservationLoadTime, current_millis);

            } catch (JSONException j) {
                j.printStackTrace();
            }
            adapter.notifyDataChanged();
            inbox_dot_loader.setVisibility(View.GONE);
            emptyinbox.setVisibility(View.GONE);
            swipeContainerstart.setVisibility(View.GONE);
            listinbox.setVisibility(View.VISIBLE);
        }else{
            inbox_dot_loader.setVisibility(View.GONE);
            emptyinbox.setVisibility(View.VISIBLE);
            swipeContainerstart.setVisibility(View.VISIBLE);
            listinbox.setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        if (backPressed >= 1) {
            super.onBackPressed();       // bye

        } else {    // this guy is serious
            // clean up
            backPressed = backPressed + 1;
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressed = 0;
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("InboxResume","InboxOnResume");
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            if(adapter!=null)
            {
                adapter.clear();
                loadInbox();

            }
           // loadInbox(); //new LoadInboxDetails().execute();
        } else {
            snackBar();
          //  commonMethods.snackBar(getResources().getString(R.string.interneterror), "", false, 2, edt, edt, getResources(), this);
        }
    }

    public void snackBar()
    {
        // Create the Snackbar
        snackbar = Snackbar.make(findViewById(R.id.emptyinbox), "", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.background));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setText(getResources().getString(R.string.retry));
        button.setTextColor(getResources().getColor(R.color.title_text_color));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                isInternetAvailable = getNetworkState().isConnectingToInternet();
                if (isInternetAvailable) {
                    if (localSharedPreferences.getSharedPreferences(Constants.AccessToken)!=null) {
                        loadInbox();
                    }
                    // new UpdateDateDetails().execute();
                }else {
                    snackBar();
                }
            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);

        if (isInternetAvailable){
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }
}
