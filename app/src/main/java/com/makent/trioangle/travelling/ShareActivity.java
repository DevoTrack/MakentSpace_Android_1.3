package com.makent.trioangle.travelling;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  travelling/tabs
 * @category    GuestShareActivity
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.ShareAdapter;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.util.ConnectionDetector;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* ***********************************************************************
This is Share Page Contain Show and Share the Trips
**************************************************************************  */
public class ShareActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BitmapDrawable ob;
    List<Makent_model> sharelist;
    List activities;
    ShareAdapter adapter;
    Bitmap bitmap;
    RelativeLayout share_back;
    ImageView shareimage;
    TextView sharebbackimage,room_name;
    URL url;
    Palette.Swatch textSwatch=null;
    String imagetoshare,nametoshare;
    protected boolean isInternetAvailable;
    private String roomExp;
    ImageView share_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent x=getIntent();
        imagetoshare=x.getStringExtra("imagetoshare");
        nametoshare=x.getStringExtra("nametoshare");
        roomExp = x.getStringExtra("experience");
        share_close=(ImageView) findViewById(R.id.share_close);
        share_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sharebbackimage=(TextView)findViewById(R.id.sharebbackimage);
        shareimage=(ImageView)findViewById(R.id.shareimage);
        room_name=(TextView) findViewById(R.id.room_name);
        share_close =(ImageView)findViewById(R.id.share_close);
        room_name.setText(nametoshare);

        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        activities = ShareActivity.this.getPackageManager().queryIntentActivities(sendIntent, 0);

        System.out.println("activities List "+activities);

        recyclerView = (RecyclerView) findViewById(R.id.sharerecyclerview);
        sharelist = new ArrayList<>();

        adapter = new ShareAdapter(this, sharelist, roomExp);
        adapter.setLoadMoreListener(new ShareAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = sharelist.size() - 1;
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(ShareActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

       load(0);
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            (new LoadPalette()).execute("100");// this function used to called on Loadpalette api
        }else {
            snackBar();
        }
    }
    private void load(int index) {

        if(activities.size()>0)
        {
            for (int i = 0; i < activities.size(); i++) {
                Makent_model listdata = new Makent_model();
                listdata.setShareItem((ResolveInfo) activities.get(i));
                listdata.setSharename(((ResolveInfo) activities.get(i)).activityInfo.applicationInfo.loadLabel(getApplicationContext().getPackageManager()).toString());
                listdata.setShareIcon(((ResolveInfo) activities.get(i)).activityInfo.applicationInfo.loadIcon(getApplicationContext().getPackageManager()));
                System.out.println("activities listdata "+listdata);
                sharelist.add(listdata);
            }
            adapter.notifyDataChanged();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public class LoadPalette extends AsyncTask<String, Integer, Palette.Swatch> {

        protected Palette.Swatch doInBackground(String... arg0) {
            try {


                if(imagetoshare!=null) {
                    url = new URL(imagetoshare);
                }else
                {
                    url = new URL("http://makentspace.trioangle.com/images/home_cities/home_city_1461439922.jpg");
                }


                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ob = new BitmapDrawable(getResources(), bitmap);

                Palette.from(bitmap)
                        .generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                System.out.println("Palette "+palette);
                                textSwatch = palette.getDarkVibrantSwatch();
                                System.out.println("TaxtSwtch first "+textSwatch);
                                if(textSwatch == null)
                                {
                                    textSwatch = palette.getDarkMutedSwatch();
                                }else  if(textSwatch == null)
                                {
                                    textSwatch = palette.getVibrantSwatch();
                                }else if(textSwatch == null)
                                {
                                    textSwatch = palette.getMutedSwatch();
                                }else if (textSwatch == null) {
                                    Toast.makeText(getApplicationContext(), "Null swatch :(", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }
                        });
            } catch(IOException e) {
                System.out.println(e);
            }



    try{
        int time = Integer.parseInt(arg0[0]);
        Thread.sleep(time);
        if(textSwatch!=null) {
            return textSwatch;
        }else {
            return null;
        }
    }catch (InterruptedException e) {
        e.printStackTrace();
        return textSwatch;
    }


        }


        protected void onPostExecute(Palette.Swatch palette) {
            shareimage.setBackgroundDrawable(ob);
            try{
            if(palette.getRgb()!=0)
            sharebbackimage.setBackgroundColor(palette.getRgb());
            }catch (NullPointerException e){
            }
        }
    }

    public void snackBar()
    {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(shareimage, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable){
            //textViewTop.setText(statusmessage);
        }else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }
    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }
}

