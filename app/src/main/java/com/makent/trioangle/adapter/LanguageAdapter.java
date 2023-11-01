package com.makent.trioangle.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.host.LanguageModel;
import com.makent.trioangle.profile.SettingActivity;

import java.util.ArrayList;

import static com.makent.trioangle.MainActivity.languagemainAlertDialog;
import static com.makent.trioangle.profile.SettingActivity.languageAlertDialog;

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    private Activity activity;
    private LayoutInflater inflater;
    private static ArrayList<LanguageModel> modelItems;

    static LocalSharedPreferences localSharedPreferences;
    static SettingActivity st = new SettingActivity();


    static String currency;


    public LanguageAdapter(Context context, ArrayList<LanguageModel> Items) {
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences = new LocalSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_Explore) {
            return new LanguageAdapter.MovieHolder(inflater.inflate(R.layout.language_list, parent, false));
        } else {
            return new LanguageAdapter.LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        if (getItemViewType(position) == TYPE_Explore) {
            ((LanguageAdapter.MovieHolder) holder).bindData(modelItems.get(position), position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_Explore;

    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder {
        TextView languageName;
        RadioButton radiobutton;
        RelativeLayout selectlanguage;

        public MovieHolder(View itemView) {
            super(itemView);
            languageName = (TextView) itemView.findViewById(R.id.languagename_txt);
            radiobutton = (RadioButton) itemView.findViewById(R.id.radioButton1);
            selectlanguage = (RelativeLayout) itemView.findViewById(R.id.selectlanguage);
        }

        void bindData(LanguageModel movieModel, int position) {

            String language = localSharedPreferences.getSharedPreferences(Constants.LanguageName);


            languageName.setText(movieModel.getLanguage());


            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            context.getResources().getColor(R.color.text_shadow)
                            , context.getResources().getColor(R.color.red_text),
                    }
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radiobutton.setButtonTintList(colorStateList);
            }

            radiobutton.setChecked(false);

            if (movieModel.getLanguage().equals(language)) {
                radiobutton.setChecked(true);
            }


            selectlanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("language name  : "+languageName.getText().toString());
                    localSharedPreferences.saveSharedPreferences(Constants.LanguageName, languageName.getText().toString());

                    if (languageAlertDialog != null) {
                        languageAlertDialog.cancel();
                    }

                    if (languagemainAlertDialog != null) {
                        languagemainAlertDialog.dismiss();
                    }
                }
            });
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

}
