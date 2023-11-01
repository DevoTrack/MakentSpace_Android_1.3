package com.makent.trioangle.host.optionaldetails.description;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.model.host.LengthOfStayArrayModel;
import com.makent.trioangle.model.host.LengthOfStayModel;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public  class LastminAdapter extends RecyclerView.Adapter<LastminAdapter.ViewHolder> implements ServiceListener {
    private ArrayList<Makent_model> countries;
    private Context context;
    public String userid;
    public Dialog_loading mydialog;
    protected boolean isInternetAvailable;
    public TextView nodiscount;
    public RelativeLayout header_lay;
    public LocalSharedPreferences localSharedPreferences;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    private LengthOfStayModel hostDeleteLmModel;
    private ArrayList<LengthOfStayArrayModel> hostDeleteLmArrayModel;

    public LastminAdapter(Context context,  ArrayList<Makent_model> countries,TextView nodiscount,RelativeLayout header_lay) {
        this.countries = countries;
        this.context = context;
        this.nodiscount = nodiscount;
        this.header_lay = header_lay;
        mydialog = new Dialog_loading(context);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind((Activity) context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public LastminAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lengthstaylist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LastminAdapter.ViewHolder viewHolder, int i) {

        localSharedPreferences = new LocalSharedPreferences(context);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);



        Makent_model user = countries.get(i);
        viewHolder.day_txt.setText(user.getReviewDate());
        viewHolder.percentage_txt.setText(user.getReviewMessage());


        viewHolder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //http://smash.trioangle.com/makent/api/delete_price_rule?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9tYWtlbnRzcGFjZS50cmlvYW5nbGVkZW1vLmNvbVwvYXBpXC9zaWdudXAiLCJpYXQiOjE1NjkzOTMyMTYsImV4cCI6MTU2OTQ3OTYxNiwibmJmIjoxNTY5MzkzMjE2LCJqdGkiOiJIQmVaN2d0SFNaVThzaE1iIiwic3ViIjoxMDA1OCwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.8uC2CPGD6_-miXkKrJEZekdPcZuYQuSxxFUpzGDd7L0&id=2&room_id=10013
                isInternetAvailable = getNetworkState().isConnectingToInternet();


//                if (isInternetAvailable) {

               // new getWishListTitle().execute(countries.get(i).getType(),countries.get(i).getReviewUserName());
                deleteLastMinOfStay(countries.get(i).getType(),countries.get(i).getReviewUserName());

                System.out.println("Day Text and Percentage text "+countries.get(i).getDays());
                countries.remove(i);
                notifyDataSetChanged();
               /* }else{


                }*/

            }});

        viewHolder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("period and discount "+countries.get(i).getReviewUserImage()+""+countries.get(i).getReviewUserName()+" "+countries.get(i).getReviewMessage()+" "+countries.get(i).getReviewDate());
                Intent intent = new Intent(context,AddLastMin.class);
                intent.putExtra("id",countries.get(i).getType());
                intent.putExtra("room_id",countries.get(i).getReviewUserName());
                intent.putExtra("type",countries.get(i).getReviewUserImage());
                intent.putExtra("period",countries.get(i).getReviewDate());
                intent.putExtra("discount",countries.get(i).getReviewMessage());
                context.startActivity(intent);
            }});


    }

    private ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        return connectionDetector;
    }

    private void deleteLastMinOfStay(String ids, String roomid) {

        System.out.println("Ids check "+ids);

        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.deletePriceRule(userid,ids,roomid).enqueue(new RequestCallback(this));

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {


        if (jsonResp.isSuccess()) {

            onSuccessDeleteLm(jsonResp); // onSuccess call method
        }else if(!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            System.out.println("Checkin ing ");
            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }
            commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,nodiscount,context.getResources(), (Activity) context);
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }



    private void onSuccessDeleteLm(JsonResponse jsonResp) {

        hostDeleteLmModel = gson.fromJson(jsonResp.getStrResponse(), LengthOfStayModel.class);



        if(hostDeleteLmModel.getStatusCode()!=null&&hostDeleteLmModel.getStatusCode().equals("1")){

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }





            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }


            hostDeleteLmArrayModel = hostDeleteLmModel.getPriceRules();
            try {
                localSharedPreferences.saveSharedPreferences(Constants.LastMinCount,gson.toJson(hostDeleteLmArrayModel));
                JSONArray lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.LastMinCount));

                OD_LastMin.ja = lasmindiscount;


                if(lasmindiscount.length()>0){
                    nodiscount.setVisibility(View.GONE);
                    header_lay.setVisibility(View.VISIBLE);
                }else{
                    nodiscount.setVisibility(View.VISIBLE);
                    header_lay.setVisibility(View.GONE);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day_txt, percentage_txt;
        private TextView days_txt,percent_txt;
        private ImageView edit_icon,delete_icon;
        private LinearLayout last_min_lay;

        public ViewHolder(View view) {
            super(view);

            day_txt = (TextView) view.findViewById(R.id.day_text);
            percentage_txt = (TextView) view.findViewById(R.id.percentage_txt);
            delete_icon = (ImageView) view.findViewById(R.id.delete_icon);
            edit_icon = (ImageView) view.findViewById(R.id.edit_icon);

        }

    }


}
