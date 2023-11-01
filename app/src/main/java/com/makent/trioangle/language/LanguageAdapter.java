package com.makent.trioangle.language;

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


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    public OnLanguageClick onLanguageClick;
    private Context context;
    private String[] languageList;
    private String[] languageCode;
    private String savedlanguge;

    public LanguageAdapter(Context context, String[] languageList, String[] languageCode, String savedlanguge) {
        this.context = context;
        this.languageList = languageList;
        this.languageCode = languageCode;
        this.savedlanguge = savedlanguge;
    }

    public void setOnItemClickListner(OnLanguageClick onLanguageClick) {
        this.onLanguageClick = onLanguageClick;
    }

    @NonNull
    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvLangugaeTitle.setText(languageList[i]);

        if (savedlanguge!= null && savedlanguge.equalsIgnoreCase(languageList[i])) {
            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}}, new int[]{

                    context.getResources().getColor(R.color.text_light_gray), context.getResources().getColor(R.color.red_text),});
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.rbLanguage.setButtonTintList(colorStateList);
            }


            viewHolder.rbLanguage.setChecked(false);
            if (savedlanguge!= null && savedlanguge.equalsIgnoreCase(languageList[i])) {
                viewHolder.rbLanguage.setChecked(true);
            }

        }

        viewHolder.rltlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLanguageClick.onLanguageClick(languageCode[i], languageList[i]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.length;
    }

    public interface OnLanguageClick {
        void onLanguageClick(String languageCode, String languageName);
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
