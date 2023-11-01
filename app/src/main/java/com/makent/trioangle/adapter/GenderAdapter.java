package com.makent.trioangle.adapter;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  adapter
 * @category    GenderAdapter
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.model.Makent_model;

import java.util.List;

import static com.makent.trioangle.profile.EditProfileActivity.alertDialogStores;


/**
 * Created by bowshulsheikrahaman on 27/12/16.
 */

@SuppressLint("ViewHolder")
public class GenderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    private Activity activity;
    private LayoutInflater inflater;
    private static List<Makent_model> modelItems;

    public static int lastCheckedPosition = -1;

    TextView explore_amount;
    TextView price;
    TextView address;
    TextView symbol;
    TextView explore_reviewrate;
    ProgressBar explore_progress;
    RatingBar review;



    public GenderAdapter(Context context, List<Makent_model> Items) {
        this.context = context;
        this.modelItems = Items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_Explore){
            return new MovieHolder(inflater.inflate(R.layout.gendaritem,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_Explore){
            ((com.makent.trioangle.adapter.GenderAdapter.MovieHolder)holder).bindData(modelItems.get(position),position);
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(modelItems.get(position).getGenderType().equals("load")){
            return TYPE_LOAD;
        }else{
            return TYPE_Explore;
        }
    }

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    /* VIEW HOLDERS */

    static class MovieHolder extends RecyclerView.ViewHolder{
        TextView gendertype;
        RadioButton genderradiobutton;
        RelativeLayout selectgender;

        public MovieHolder(View itemView) {
            super(itemView);
            gendertype=(TextView) itemView.findViewById(R.id.gender_txt);
            genderradiobutton = (RadioButton) itemView.findViewById(R.id.gender_redio);
            selectgender=(RelativeLayout) itemView.findViewById(R.id.selectgender);
        }

        void bindData(Makent_model movieModel,int position){

            System.out.println("inside position "+position);
            System.out.println("Inside gender type "+movieModel.getGenderType().toString());
            gendertype.setText(movieModel.getGenderType());

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
                genderradiobutton.setButtonTintList(colorStateList);
            }
            if(lastCheckedPosition == position){

                System.out.println("inside if condition ");

                genderradiobutton.setChecked(true);

            }
            else {


                //  radiobutton.setSupportButtonTintList(colorStateList);
                System.out.println("inside else condition ");
                genderradiobutton.setChecked(false);


               /* if (lastCheckedPosition==-1&&movieModel.getGenderType().equals("USD")) {
                    genderradiobutton.setChecked(true);
                }*/
            }

            selectgender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent x = new Intent(context,SpaceDetailActivity.class);
                    // context.startActivity(x);
                    lastCheckedPosition = getAdapterPosition();
                    // radiobutton.setChecked(true);
                    if(alertDialogStores!=null) {
                        alertDialogStores.cancel();
                    }

                }
            });
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
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
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


}

