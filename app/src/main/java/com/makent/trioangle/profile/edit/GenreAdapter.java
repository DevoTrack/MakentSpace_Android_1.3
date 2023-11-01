package com.makent.trioangle.profile.edit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    public GenreAdapter.OnLanguageClick onLanguageClick;
    private Context context;
    private String[] genreList;
    private String savedgenre;
    private LocalSharedPreferences localSharedPreferences;

    public GenreAdapter(Context context, String[] genreList, String savedgenre) {
        this.context = context;
        this.genreList = genreList;
        this.savedgenre = savedgenre;
    }

    public void setOnItemClickListner(GenreAdapter.OnLanguageClick onLanguageClick) {
        this.onLanguageClick = onLanguageClick;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_list, viewGroup, false);
        return new GenreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvLangugaeTitle.setText(genreList[i]);

        if (savedgenre!= null && savedgenre.equalsIgnoreCase(genreList[i])) {
            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}}, new int[]{

                    context.getResources().getColor(R.color.text_light_gray), context.getResources().getColor(R.color.guestButton),});
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.rbLanguage.setButtonTintList(colorStateList);
            }


            viewHolder.rbLanguage.setChecked(false);
            if (savedgenre!= null && savedgenre.equalsIgnoreCase(genreList[i])) {
                viewHolder.rbLanguage.setChecked(true);
            }

        }

        viewHolder.rltlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLanguageClick.onLanguageClick(genreList[i]);
               // localSharedPreferences.saveSharedPreferences(GenreName, genreList[i]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.length;
    }

    public interface OnLanguageClick {
        void onLanguageClick(String genreName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rltlanguage;
        TextView tvLangugaeTitle;
        RadioButton rbLanguage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rltlanguage = itemView.findViewById(R.id.selectlanguage);
            tvLangugaeTitle = itemView.findViewById(R.id.languagename_txt);
            rbLanguage = itemView.findViewById(R.id.radioButton1);
        }
    }
}