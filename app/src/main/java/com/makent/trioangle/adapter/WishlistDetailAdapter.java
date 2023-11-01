package com.makent.trioangle.adapter;


/**
 * @package com.makent.trioangle
 * @subpackage adapter
 * @category WishlistDetailAdapter
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.FontIconDrawable;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.Makent_model;
import com.makent.trioangle.spacedetail.views.SpaceDetailActivity;
import com.makent.trioangle.travelling.HomeActivity;
import com.makent.trioangle.travelling.Room_details;
import com.makent.trioangle.travelling.ShareActivity;
import com.makent.trioangle.travelling.WishListDetailsActivity;
import com.makent.trioangle.travelling.tabs.SavedActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import static com.makent.trioangle.util.Enums.REQ_DELETE_WISHLIST;
import static com.makent.trioangle.util.Enums.REQ_EDIT_WISHLIST;

@SuppressLint("ViewHolder")
public class WishlistDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;

    public final int TYPE_Explore = 1;
    public final int TYPE_LOAD = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;
    static Activity activity;
    private List<Makent_model> modelItems;
    Header header;
    String listname;

    ProgressBar explore_progress;

    LocalSharedPreferences localSharedPreferences;
    String userid, listid, deletelistid, deletelistname, newlistname;
    AlertDialog alertDialog;
    Dialog_loading mydialog;
    private String roomExp;

    public OnEditWishList onEditWishList;

    public interface OnEditWishList{
        void onEditWishList();
    }

    public WishlistDetailAdapter(Header header, Activity activity, Context context, List<Makent_model> Items) {
        localSharedPreferences = new LocalSharedPreferences(context);
        AppController.getAppComponent().inject(this);
        this.userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        this.listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        this.context = context;
        this.modelItems = Items;
        this.activity = activity;
        this.header = header;

        mydialog = new Dialog_loading(context);
        mydialog.setCancelable(false);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        System.out.println("View Type" + viewType);
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.wishlist_detailsheader, parent, false));
        }
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.explore_room_tab, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        System.out.println("Holder name" + holder);
        System.out.println("Holder position" + position);

        if (holder instanceof HeaderHolder) {
            System.out.println("Header Holder position");
            HeaderHolder VHheader = (HeaderHolder) holder;
            //VHheader.txtTitle.setText("Siva");
        } else if (holder instanceof MovieHolder) {
            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }

            if (getItemViewType(position) == TYPE_Explore) {
                ((MovieHolder) holder).bindData(modelItems.get(position - 1), position - 1);
            }
            //No else part needed as load holder doesn't bind any data
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*if(modelItems.get(position).getExplore_room_image().equals("load")){
            return TYPE_LOAD;
        }else{
            return TYPE_Explore;
        }*/
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return modelItems.size() + 1;
    }


    /* VIEW HOLDERS */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, wishlist_availablehome;
        Button wishlist_invitefriends;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.wishlist_invitefriends = (Button) itemView.findViewById(R.id.wishlist_invitefriends);
            this.txtTitle = (TextView) itemView.findViewById(R.id.wishlist_title);
            this.wishlist_availablehome = (TextView) itemView.findViewById(R.id.wishlist_availablehome);

            String wishlistname = localSharedPreferences.getSharedPreferences(Constants.WishListName);
//            try {
//                newlistname = URLEncoder.encode(newlistname, "UTF-8");
//                newlistname = newlistname.replace("+"," ");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            System.out.println("Header wish list name" + wishlistname);


            this.txtTitle.setText(wishlistname);

            String wishlistcount = localSharedPreferences.getSharedPreferences(Constants.WishListHomeCount);
            System.out.println("Header wish list" + wishlistcount);
            if (wishlistcount != null) {
                if (Integer.parseInt(wishlistcount) > 1) {
                    System.out.println("Header wish list IF" + wishlistcount);
                    this.wishlist_availablehome.setText(wishlistcount + " " + context.getResources().getString(R.string.availablespaces));
                } else {
                    System.out.println("Header wish list Else" + wishlistcount);
                    this.wishlist_availablehome.setText(wishlistcount + " " + context.getResources().getString(R.string.availablespace));
                }
            }
            wishlist_invitefriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent x = new Intent(context, ShareActivity.class);
                    context.startActivity(x);
                }
            });

            this.txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("showDialog");
                    showDialog();
                }
            });

        }
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView explore_room_image;
        ImageView explore_wishlists;
        TextView explore_amount, explore_roomdetails, explore_reviewrate, explore_instantbook,tvspacetype;
        RatingBar explore_room_rate;

        public MovieHolder(View itemView) {
            super(itemView);
            explore_room_image = (ImageView) itemView.findViewById(R.id.explore_room_image);
            explore_wishlists = (ImageView) itemView.findViewById(R.id.explore_wishlists);
            explore_amount = (TextView) itemView.findViewById(R.id.explore_amount);
            explore_instantbook = (TextView) itemView.findViewById(R.id.explore_instantbook);
            explore_roomdetails = (TextView) itemView.findViewById(R.id.explore_roomdetails);
            explore_reviewrate = (TextView) itemView.findViewById(R.id.explore_reviewrate);
            explore_room_rate = (RatingBar) itemView.findViewById(R.id.explore_room_rate);
            tvspacetype = (TextView) itemView.findViewById(R.id.tvspacetype);
        }

        void bindData(final Makent_model movieModel, final int position) {

            Makent_model model = movieModel;
            Glide.with(context.getApplicationContext())
                    .load(movieModel.getExplore_room_image())

                    .into(new DrawableImageViewTarget(explore_room_image) {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                        }
                    });
            //String currencysymbol= Html.fromHtml(movieModel.getCurrencysymbol()).toString();
            String currencysymbol = movieModel.getCurrencysymbol();
            String instantbook = movieModel.getInstantBook();
            explore_amount.setText(currencysymbol + " " + movieModel.getRoomprice() + " ");

            if (instantbook.equals("No")) {
                explore_instantbook.setVisibility(View.GONE);
            } else {
                explore_instantbook.setVisibility(View.VISIBLE);
            }
            explore_roomdetails.setText(movieModel.getRoomname());
            /*explore_amount.measure(0, 0);
            int width = explore_amount.getMeasuredWidth();
            System.out.println("Width" + width + " Room Name " + movieModel.getRoomname());
            if (!movieModel.getRoomname().equals("")) {
                SpannableString s = new SpannableString(movieModel.getRoomname());
                System.out.println("s " + s);
                s.setSpan(new android.text.style.LeadingMarginSpan.Standard(width + 40, 0), 0, 1, 0);
                explore_roomdetails.setText(s);
            }*/

            if (Float.valueOf(movieModel.getRoomrating()) == 0) {
                explore_reviewrate.setVisibility(View.GONE);
                explore_room_rate.setVisibility(View.GONE);
            } else {
                explore_reviewrate.setVisibility(View.VISIBLE);
                explore_room_rate.setVisibility(View.VISIBLE);
            }
            if(movieModel.getRoomreview().equals("1")){
                explore_reviewrate.setText(movieModel.getRoomreview() + " " + context.getResources().getString(R.string.review_one));
            }else{
                explore_reviewrate.setText(movieModel.getRoomreview() + " " + context.getResources().getString(R.string.review));
            }

            tvspacetype.setText(movieModel.getSpaceTypeName()+" • "+movieModel.getSpaceMaxGuestCount()+" "+context.getResources().getString(R.string.people));

            explore_room_rate.setRating(Float.valueOf(movieModel.getRoomrating()));

            explore_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Glide.clear(explore_room_image);
                    //Glide.get(context).clearMemory();
                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, movieModel.getRoomid());
                    localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, movieModel.getRoomprice());
                    Intent x = new Intent(context, SpaceDetailActivity.class);
                    context.startActivity(x);

                }
            });

            Typeface font1 = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.fonts_makent1));
            Drawable icon1 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like), font1)
                    .sizeDp(20)
                    .colorRes(R.color.title_text_color);
            Drawable icon2 = new FontIconDrawable(context, context.getResources().getString(R.string.f1like1), font1)
                    .sizeDp(20)
                    .colorRes(R.color.red_text);


            if (movieModel.getRoomiswishlist().equals("Yes")) {
                explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_red_fill));
            } else {
                explore_wishlists.setImageDrawable(context.getResources().getDrawable(R.drawable.n2_heart_light_outline));
            }
            explore_wishlists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("getRoomid"+movieModel.getRoomid());
                    String wishlistcount = localSharedPreferences.getSharedPreferences(Constants.WishListCount);
                    System.out.println("Delete wish list" + wishlistcount);
                    int wc = Integer.parseInt(wishlistcount) - 1;
                    System.out.println("Update wish list" + wc);
                    localSharedPreferences.saveSharedPreferences(Constants.WishListCount, String.valueOf(wc));

                    LayoutInflater inflater = LayoutInflater.from(context);

                    HeaderHolder hh = new HeaderHolder(inflater.inflate(R.layout.wishlist_detailsheader, null, false));

                    deletelistid = movieModel.getRoomid();
                    deletelistname = movieModel.getRoomname();
                    deleteDialog();

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

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void deleteDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        // set the custom dialog components - text, ivPhoto and button
        TextView logout_msg = (TextView) dialog.findViewById(R.id.logout_msg);
        logout_msg.setText(context.getResources().getString(R.string.delete_msg) + " '" + deletelistname + "'?");
        Button dialogButton = (Button) dialog.findViewById(R.id.logout_cancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogButton1 = (Button) dialog.findViewById(R.id.logout_ok);
        dialogButton1.setText(context.getResources().getString(R.string.delete));
        // if button is clicked, close the custom dialog
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteWishList();
            }
        });

        dialog.show();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))

            return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_EDIT_WISHLIST:
                if (jsonResp.isSuccess()) {
                    editWishList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }
                }
                break;
            case REQ_DELETE_WISHLIST:
                if (jsonResp.isSuccess()) {
                    if (mydialog.isShowing())
                        mydialog.dismiss();
                    Intent intent = new Intent(context, SavedActivity.class);
                    activity.overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (mydialog.isShowing()) {
                        mydialog.dismiss();
                    }

                    //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (mydialog.isShowing()) {
            mydialog.dismiss();
        }
        //commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, wishlist_name, getResources(), this);
    }
    public void editWishList() {
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        listid = localSharedPreferences.getSharedPreferences(Constants.WishListId);
        listname = newlistname;
        try {
            newlistname = URLEncoder.encode(newlistname, "UTF-8");
            newlistname = newlistname.replace("+"," ");
            System.out.println("replaced newlistname"+newlistname);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ediwishlistapi");
        /**
         * For Edit Name
         */
        apiService.editWishList(edtWishlist()).enqueue(new RequestCallback(REQ_EDIT_WISHLIST, this));

    }

    private LinkedHashMap<String ,String > edtWishlist(){
        LinkedHashMap<String,String > edtParams= new LinkedHashMap<>();
        edtParams.put("list_id",listid);
        edtParams.put("list_name",listname);
        edtParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return edtParams;
    }

    public void editWishList(JsonResponse jsonResponse){

        try {
            newlistname = URLEncoder.encode(newlistname, "UTF-8");
            newlistname = newlistname.replace("+"," ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        newlistname = newlistname.replace("+"," ");
        localSharedPreferences.saveSharedPreferences(Constants.WishListName, listname);

        Intent intent = new Intent(context, SavedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();

    }
    public void deleteWishList() {
        if (!mydialog.isShowing())
            mydialog.show();
        apiService.deleteWishList(deleteWishlist()).enqueue(new RequestCallback(REQ_DELETE_WISHLIST, this));

    }

    private LinkedHashMap<String ,String > deleteWishlist(){
        LinkedHashMap<String,String > deleteParams= new LinkedHashMap<>();
        deleteParams.put("space_id",deletelistid);
        deleteParams.put("token",localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        return deleteParams;
    }


    public void showDialog() {
         EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(20, 0, 20, 0);
        input.setLayoutParams(lp);
        input.getBackground().setColorFilter(context.getResources().getColor(R.color.guestButton),
                PorterDuff.Mode.SRC_ATOP);
        String name = localSharedPreferences.getSharedPreferences(Constants.WishListName);
        input.setText(name);
        alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(context.getString(R.string.wish_list));
        alertDialog.setCancelable(true);
       // alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ok_val), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               newlistname = input.getText().toString();

               System.out.println("newListName"+newlistname);

//                try {
//                    newlistname = URLEncoder.encode(newlistname, "UTF-8");
//                    newlistname = newlistname.replace("+"," ");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                editWishList();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel_val), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.guestButton));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.guestButton));
            }
        });

        alertDialog.setView(input);
        alertDialog.show();
    }

}