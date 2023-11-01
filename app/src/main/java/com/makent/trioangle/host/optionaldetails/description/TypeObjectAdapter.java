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
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TypeObjectAdapter extends RecyclerView.Adapter<TypeObjectAdapter.ViewHolder> implements ServiceListener {
    private ArrayList<Makent_model> countries;
    private Context context;
     public String userid;
     public TextView nodiscount;
     public RelativeLayout header_lay;
     public Dialog_loading mydialog;
     public LocalSharedPreferences localSharedPreferences;

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    private LengthOfStayModel hostDeleteToaModel;
    private ArrayList<LengthOfStayArrayModel> hostDeleteToaArrayModel;

    public TypeObjectAdapter(Context context, ArrayList<Makent_model> countries, TextView nodiscount, RelativeLayout header_lay) {
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
    public TypeObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lengthstaylist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TypeObjectAdapter.ViewHolder viewHolder, int i) {

        Makent_model user = countries.get(i);

        localSharedPreferences = new LocalSharedPreferences(context);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);


        viewHolder.day_txt.setText(user.getReviewDate());
        viewHolder.percentage_txt.setText(user.getReviewMessage());
        viewHolder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteEarlyBird(countries.get(i).getType(),countries.get(i).getReviewUserName());
                countries.remove(i);
                notifyDataSetChanged();

            }});
        viewHolder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,AddEarlyBird.class);
                intent.putExtra("id",countries.get(i).getType());
                intent.putExtra("room_id",countries.get(i).getReviewUserName());
                intent.putExtra("type",countries.get(i).getReviewUserImage());
                intent.putExtra("period",countries.get(i).getReviewDate());
                intent.putExtra("discount",countries.get(i).getReviewMessage());
                context.startActivity(intent);

            }});

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

        hostDeleteToaModel = gson.fromJson(jsonResp.getStrResponse(), LengthOfStayModel.class);



        if(hostDeleteToaModel.getStatusCode()!=null&&hostDeleteToaModel.getStatusCode().equals("1")){

            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }





            if (mydialog.isShowing()) {
                mydialog.dismiss();
            }


            hostDeleteToaArrayModel = hostDeleteToaModel.getPriceRules();
            try {
                localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount,gson.toJson(hostDeleteToaArrayModel));
                JSONArray lasmindiscount = new JSONArray(localSharedPreferences.getSharedPreferences(Constants.EarlyBirdDiscount));

                OD_EarlyBirdDiscounts.Earlybird_ja = lasmindiscount;


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


    private void deleteEarlyBird(String ids, String roomid) {

        System.out.println("Ids check "+ids);

        if (!mydialog.isShowing()) {
            mydialog.show();
        }
        apiService.deletePriceRule(userid,ids,roomid).enqueue(new RequestCallback(this));

    }

     public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day_txt, percentage_txt;
        private ImageView delete_icon,edit_icon;

        public ViewHolder(View view) {
            super(view);

            day_txt = (TextView) view.findViewById(R.id.day_text);
            percentage_txt = (TextView) view.findViewById(R.id.percentage_txt);
            delete_icon = (ImageView) view.findViewById(R.id.delete_icon);
            edit_icon = (ImageView) view.findViewById(R.id.edit_icon);
        }

    }

}