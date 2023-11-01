package com.makent.trioangle.language;

import android.app.AlertDialog;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.util.CommonMethods;

import javax.inject.Inject;

import static com.makent.trioangle.helper.Constants.LanguageCode;
import static com.makent.trioangle.helper.Constants.LanguageName;


public class LanguageConverter {
    @Inject
    CommonMethods commonMethods;

    private static AlertDialog alertDialoglanguage;
    public onSuccessLanguageChangelistner onLanguageChange;
    private Context context;
    private boolean isCancelable;
    private RecyclerView languageView;
    private LanguageAdapter languageAdapter;
    private String[] languageList;
    private String[] languageCode;
    private LocalSharedPreferences localSharedPreferences;

    public LanguageConverter(Context context, boolean isCancelable, String[] languageList, String[] languageCode, onSuccessLanguageChangelistner listener) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.isCancelable = isCancelable;
        this.languageList = languageList;
        this.languageCode = languageCode;
        this.onLanguageChange = listener;
        localSharedPreferences = new LocalSharedPreferences(this.context);
        showAlert();
    }

    private void showAlert() {
        System.out.println("Language " + localSharedPreferences.getSharedPreferences(LanguageName));
        languageView = new RecyclerView(context);
        languageAdapter = new LanguageAdapter(context, languageList, languageCode, localSharedPreferences.getSharedPreferences(LanguageName));
        languageView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        languageView.setAdapter(languageAdapter);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.langugae_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(context.getResources().getString(R.string.select_language));

        alertDialoglanguage = new android.app.AlertDialog.Builder(context).setCustomTitle(view).setView(languageView).setCancelable(isCancelable).show();
        languageAdapter.setOnItemClickListner(new LanguageAdapter.OnLanguageClick() {
            @Override
            public void onLanguageClick(String languageCode, String languageName) {
                if (commonMethods.isOnline(context)) {
                    if (localSharedPreferences.getSharedPreferences(LanguageCode) != null) {
                        if (!localSharedPreferences.getSharedPreferences(LanguageCode).equalsIgnoreCase(languageCode)) {
                            localSharedPreferences.saveSharedPreferences(LanguageCode, languageCode);
                            localSharedPreferences.saveSharedPreferences(LanguageName, languageName);
                            onLanguageChange.onSuccess(languageName, languageCode);
                        } else {

                            //Toast.makeText(context, context.getResources().getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        localSharedPreferences.saveSharedPreferences(LanguageCode, languageCode);
                        localSharedPreferences.saveSharedPreferences(LanguageName, languageName);
                        onLanguageChange.onSuccess(languageName, languageCode);
                    }
                }
                alertDialoglanguage.dismiss();
            }
        });
    }



    public interface onSuccessLanguageChangelistner {
        void onSuccess(String language, String languageCode);
    }
}
