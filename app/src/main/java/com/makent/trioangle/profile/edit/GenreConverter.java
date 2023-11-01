package com.makent.trioangle.profile.edit;

import android.app.AlertDialog;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;

import static com.makent.trioangle.helper.Constants.GenreName;

public class GenreConverter {

    private static AlertDialog alertDialoglanguage;
    public GenreConverter.onSuccessLanguageChangelistner onLanguageChange;
    private Context context;
    private boolean isCancelable;
    private RecyclerView genreView;
    private GenreAdapter genreAdapter;
    private String[] genreList;
    private LocalSharedPreferences localSharedPreferences;

    public GenreConverter(Context context, boolean isCancelable, String[] genreList, GenreConverter.onSuccessLanguageChangelistner listener) {
        //AppController.getAppComponent().inject(this);
        this.context = context;
        this.isCancelable = isCancelable;
        this.genreList = genreList;
        this.onLanguageChange = listener;
        localSharedPreferences = new LocalSharedPreferences(this.context);
        showAlert();
    }

    private void showAlert() {
        System.out.println("Language " + localSharedPreferences.getSharedPreferences(GenreName));
        genreView = new RecyclerView(context);
        genreAdapter = new GenreAdapter(context, genreList, localSharedPreferences.getSharedPreferences(GenreName));
        genreView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        genreView.setAdapter(genreAdapter);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.langugae_header, null);
        TextView T = (TextView) view.findViewById(R.id.header);
        T.setText(context.getResources().getString(R.string.gender1));

        alertDialoglanguage = new android.app.AlertDialog.Builder(context).setCustomTitle(view).setView(genreView).setCancelable(isCancelable).show();
        genreAdapter.setOnItemClickListner(new GenreAdapter.OnLanguageClick() {
            @Override
            public void onLanguageClick(String genreName) {
                localSharedPreferences.saveSharedPreferences(GenreName, genreName);
                alertDialoglanguage.dismiss();
                onLanguageChange.onSuccess(genreName);

            }
        });
    }

    public interface onSuccessLanguageChangelistner {
        void onSuccess(String language);
    }
}


