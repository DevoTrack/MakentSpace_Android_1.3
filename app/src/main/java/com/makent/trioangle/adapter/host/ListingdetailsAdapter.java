package com.makent.trioangle.adapter.host;

/**
 * @package com.makent.trioangle
 * @subpackage adapter/host
 * @category ListingdetailsAdapter
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.LYS_Home;
import com.makent.trioangle.model.host.HostListedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

@SuppressLint("ViewHolder")
public class ListingdetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
    public final int TYPE_HEAD = 2;

    static int count = 0;

    public @Inject
    Gson gson;


    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;

    private Activity activity;
    private LayoutInflater inflater;
    private List<HostListedModel> modelItems;
    static LocalSharedPreferences localSharedPreferences;

    public ListingdetailsAdapter(Context context, List<HostListedModel> Items) {
        this.context = context;
        this.modelItems = Items;
        localSharedPreferences = new LocalSharedPreferences(context);
        ButterKnife.bind((Activity) context);
        AppController.getAppComponent().inject(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.tab_listing_item, parent, false));
        } else if (viewType == TYPE_HEAD) {
            return new HeaderHolder(inflater.inflate(R.layout.amenities_header_item, parent, false));
        }
        {
            return new LoadHolder(inflater.inflate(R.layout.row_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_Explore) {
            ((MovieHolder) holder).bindData(modelItems.get(position),position);
        }

        if (getItemViewType(position) == TYPE_HEAD) {
            ((HeaderHolder) holder).bindData(modelItems.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        //if (!modelItems.get(position).type.isEmpty()) {
        System.out.println("Model items check " + modelItems.get(position).getType());

        if (modelItems.get(position).getType().equals("load")) {
            return TYPE_LOAD;
        } else if (modelItems.get(position).getType().equals("listed") || modelItems.get(position).getType().equals("unlisted")) {
            return TYPE_HEAD;
        } else return TYPE_Explore;
        //} else return TYPE_Explore;
    }

	/*@Override
    public int getItemViewType(int position) {
		if(modelItems.get(position).getlisting_room_image().equals("load")){
			return TYPE_LOAD;
		}else{
			return TYPE_Explore;
		}
	}*/

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    /* VIEW HOLDERS */

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView listing_room_image;
        TextView listing_roomdetails, listing_status;
        private String listdata;
        public JSONObject joLastmin;
        public JSONArray jaLasmin, jaEarlyBird, jaLengthstay, jaLengthstayoptions;
        private JSONObject joEarlyBird, joLengthstay, joLengthstayoptions;

        public MovieHolder(View itemView) {
            super(itemView);
            listing_room_image = (ImageView) itemView.findViewById(R.id.listing_room_image);
            listing_roomdetails = (TextView) itemView.findViewById(R.id.listing_roomdetails);
            listing_status = (TextView) itemView.findViewById(R.id.listing_status);
        }

        void bindData(final HostListedModel movieModel,int position) {


            System.out.println("cheking in " + movieModel.getRoomTitle());
            if (movieModel.getRoomTitle() != null && !movieModel.getRoomTitle().equals("")) {
                listing_roomdetails.setText(movieModel.getRoomTitle());
            } else {
                listing_roomdetails.setText(movieModel.getRoomName() + " " + context.getResources().getString(R.string.in) + " " + movieModel.getRoomLocation());
            }

            if (Integer.parseInt(movieModel.getRemainingSteps()) == 0) {

                listing_status.setBackgroundColor(context.getResources().getColor(R.color.g_start));
            } else {

                listing_status.setBackgroundColor(context.getResources().getColor(R.color.pink));
            }

            listing_status.setText(movieModel.getRoomStatus());
            System.out.println("Adapter Room ID" + movieModel.getRoomId());
            //System.out.println("Adapter Room Image"+movieModel.getlisting_room_image());

			/*if(!movieModel.getlisting_room_image().equals("")) {
				Glide.with(context.getApplicationContext())
						.load(movieModel.getlisting_room_image())

						.into(new GlideDrawableImageViewTarget(listing_room_image) {
							@Override
							public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
								super.onResourceReady(drawable, anim);
								//	explore_progress.setVisibility(View.GONE);
							}
						});
			}else
			{
				listing_room_image.setBackgroundResource(R.drawable.default_photo_bg);
			}*/
            System.out.println("movieModel.getRoomThumbImages()"+movieModel.getRoomThumbImages());
            if (movieModel.getRoomThumbImages().size() > 0) {
                System.out.println("getRoomThumbImages"+movieModel.getRoomThumbImages().get(0));
                Glide.with(context.getApplicationContext())
                        .load(movieModel.getRoomThumbImages().get(0))

                        .into(new DrawableImageViewTarget(listing_room_image) {
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                            }
                        });
            }else{
                Glide.with(context.getApplicationContext())
                        .load("")

                        .into(new DrawableImageViewTarget(listing_room_image) {
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                            }
                        });
            }

            listing_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, movieModel.getRoomId());
                    localSharedPreferences.saveSharedPreferences(Constants.SelectedRoomPrice, null);
                     //localSharedPreferences.saveSharedPreferences(Constants.ListData, movieModel.);

                    System.out.println("Response check "+movieModel.getAdditionalRulesMsg());

                    String hostListedModels=gson.toJson(movieModel);
                    localSharedPreferences.saveSharedPreferences(Constants.ListData, hostListedModels);

                    HostListedModel hostListedModel=gson.fromJson(localSharedPreferences.getSharedPreferences(Constants.ListData), HostListedModel.class);


                    listdata = localSharedPreferences.getSharedPreferences(Constants.ListData);
                    //String listData = movieModel.getRoomDetails();
                    try {
                        JSONObject dataJSONObject = new JSONObject(listdata);
                        if (dataJSONObject.getString("room_id").equals(movieModel.getRoomId())) {
                            JSONArray arrJson = dataJSONObject.getJSONArray("availability_rules");
                            JSONArray arrJson1 = dataJSONObject.getJSONArray("availability_rules_options");
                            localSharedPreferences.saveSharedPreferences(Constants.ReserveSettings, arrJson.toString());
                            localSharedPreferences.saveSharedPreferences(Constants.MinimumStay, dataJSONObject.getString("minimum_stay"));
                            localSharedPreferences.saveSharedPreferences(Constants.MaximumStay, dataJSONObject.getString("maximum_stay"));
                            localSharedPreferences.saveSharedPreferences(Constants.AvailableRulesOption, arrJson1.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        joLastmin = new JSONObject(listdata);
                        jaLasmin = joLastmin.getJSONArray("last_min_rules");
                        localSharedPreferences.saveSharedPreferences(Constants.LastMinCount, jaLasmin.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        joEarlyBird = new JSONObject(listdata);
                        jaEarlyBird = joEarlyBird.getJSONArray("early_bird_rules");
                        localSharedPreferences.saveSharedPreferences(Constants.EarlyBirdDiscount, jaEarlyBird.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        joLengthstay = new JSONObject(listdata);
                        jaLengthstay = joLengthstay.getJSONArray("length_of_stay_rules");
                        localSharedPreferences.saveSharedPreferences(Constants.LengthOfStay, jaLengthstay.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Length of stay 1 " + jaLengthstay.toString());
                    try {
                        joLengthstayoptions = new JSONObject(listdata);
                        jaLengthstayoptions = joLengthstayoptions.getJSONArray("length_of_stay_options");
                        localSharedPreferences.saveSharedPreferences(Constants.LengthOfStayOptions, jaLengthstayoptions.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Length of stay 2 " + jaLengthstayoptions.toString());


                    Intent x = new Intent(context, LYS_Home.class);
                    x.putExtra("from", "oldlist");
                    context.startActivity(x);

                    if (Integer.parseInt(movieModel.getRemainingSteps()) == 0) {
                        //Glide.clear(listing_room_image);
                        //Glide.get(context).clearMemory();
                        //	localSharedPreferences.saveSharedPreferences(Constants.mSpaceId, movieModel.getSpaceID());
                        //	Intent x = new Intent(context, SpaceDetailActivity.class);
                        //	context.startActivity(x);
                    } else {
                        //Toast.makeText(context,"Room details not completed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tv_header;

        public HeaderHolder(View itemView) {
            super(itemView);

            tv_header = (TextView) itemView.findViewById(R.id.tv_header);

        }

        void bindData(final HostListedModel movieModel) {
            if (movieModel.getType().equals("unlisted")) {
                tv_header.setText(context.getResources().getString(R.string.unlisted));
            } else {
                tv_header.setText(context.getResources().getString(R.string.listed));
            }
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemloading);
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(imageView);
            Glide.with(context).load(R.drawable.dot_loading).into(imageViewTarget);
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


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void clear() {
        modelItems.clear();
        notifyDataSetChanged();
    }

}