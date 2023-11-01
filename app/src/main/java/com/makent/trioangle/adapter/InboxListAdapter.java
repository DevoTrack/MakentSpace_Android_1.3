package com.makent.trioangle.adapter;

/**
 * @package com.makent.trioangle
 * @subpackage adapter
 * @category InboxListAdapter
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makent.trioangle.R;
import com.makent.trioangle.chat.ChatActivity;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.ReservationDetails;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.inbox.InboxDataModel;
import com.makent.trioangle.profile.ProfilePageActivity;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class InboxListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_Explore = 1;
    public final int TYPE_LOAD = 2;
    static String userid;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    Header header;
    public static LocalSharedPreferences localSharedPreferences;
    public static int isHost;
    static Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    protected static final String TAG = null;

    private ArrayList<InboxDataModel> modelItems;


    public InboxListAdapter(Header header, Context context, ArrayList<InboxDataModel> Items) {
        this.header = header;
        this.context = context;
        this.modelItems = Items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        localSharedPreferences = new LocalSharedPreferences(context);
        isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
        System.out.println("View Type" + viewType);
        if (viewType == TYPE_HEADER) {
            if (isHost == 0) {
                return new HeaderHolder(inflater.inflate(R.layout.inboxheader, parent, false));
            } else {
                return new HeaderHolder(inflater.inflate(R.layout.reservationheader, parent, false));
            }
        }
        if (viewType == TYPE_Explore) {
            return new MovieHolder(inflater.inflate(R.layout.inboxlist, parent, false));
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
        } else if (holder instanceof MovieHolder) {
            System.out.println("BedTypeHolder position" + position);
            InboxDataModel currentItem = getItem(position - 1);
            ((MovieHolder) holder).bindData(currentItem);
        }
    }

    private InboxDataModel getItem(int position) {
        return modelItems.get(position);
    }

    public int getItemViewType(int position) {
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

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.review_titlename);
        }
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        RelativeLayout inboxlistdetails;
        ImageView inbox_host_image, inbox_unready_msg;
        TextView inbox_tripstatus, inboxhost_name, inbox_amount, inbox_lastmsg, inboxaddress, inbox_receivedate;

        public MovieHolder(View itemView) {
            super(itemView);
            inbox_tripstatus = (TextView) itemView.findViewById(R.id.inbox_tripstatus);
            inboxhost_name = (TextView) itemView.findViewById(R.id.inboxhost_name);
            inbox_amount = (TextView) itemView.findViewById(R.id.inbox_amount);
            inbox_lastmsg = (TextView) itemView.findViewById(R.id.inbox_lastmsg);
            inboxaddress = (TextView) itemView.findViewById(R.id.inboxaddress);
            inbox_receivedate = (TextView) itemView.findViewById(R.id.inbox_receivedate);

            inbox_host_image = (ImageView) itemView.findViewById(R.id.inbox_host_image);
            inbox_unready_msg = (ImageView) itemView.findViewById(R.id.unreadMessageNotify);

            inboxlistdetails = (RelativeLayout) itemView.findViewById(R.id.inboxlistdetails);

        }

        void bindData(final InboxDataModel movieModel) {
            userid = String.valueOf(localSharedPreferences.getSharedPreferencesInt(Constants.UserId));

            inboxlistdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String buttontype = null;
                    System.out.println(" Trip Status  " + movieModel.getMessageStatus() + " Booking Status " + movieModel.getBookingStatus() + " special offer status " + movieModel.getSpecialOfferStatus() + " Special Offer Id " + movieModel.getSpecialOfferId());
                    isHost = localSharedPreferences.getSharedPreferencesInt(Constants.isHost);
                    if (!userid.equals(movieModel.getRequestUserId())) {
                        if (movieModel.getMessageStatus().equals("Pre-Approved") || movieModel.getMessageStatus().equals("Pre-Accepted") || movieModel.getMessageStatus().equals("Inquiry") || movieModel.getSpecialOfferStatus().equals("Yes")) {
                            if (movieModel.getBookingStatus().equals("Available")) {
                                buttontype = "Book Now";
                            } else {
                                buttontype = null;
                            }
                        }
                    } else {

                        if (movieModel.getMessageStatus().equals("Inquiry")) {
                            buttontype = "Inquiry";
                        } else if (movieModel.getMessageStatus().equals("Pending")) {
                            isHost = 1;
                        } else {

                        }
                    }

                    if (isHost == 0) {
                        if (movieModel.getMessageStatus().equalsIgnoreCase(Constants.RESUBMIT)) {

                        } else {
                            Intent x = new Intent(context, ChatActivity.class);
                            x.putExtra("reservationid", movieModel.getReservationId());
                            x.putExtra("hostid", movieModel.getOtherUserId());
                            x.putExtra("host_username", movieModel.getOtherUserName());
                            x.putExtra("trip_status", movieModel.getMessageStatus());
                            x.putExtra("button_type", buttontype);
                            x.putExtra("roomid", movieModel.getSpaceId());
                            x.putExtra("special_offer", movieModel.getSpecialOfferStatus());
                            x.putExtra("special_offer_id", movieModel.getSpecialOfferId());
                            x.putExtra("deleted_user", movieModel.isDeletedUser());
                            x.putExtra("last_message", movieModel.getLastMessage());
                            context.startActivity(x);
                        }
                    } else {
                        Intent x = new Intent(context, ReservationDetails.class);
                        x.putExtra("isHost", isHost);
                        x.putExtra("reservationid", movieModel.getReservationId());
                        x.putExtra("hostid", movieModel.getHostUserId());
                        x.putExtra("host_username", movieModel.getOtherUserName());
                        x.putExtra("trip_status", movieModel.getMessageStatus());
                        x.putExtra("roomid", movieModel.getSpaceId());
                        context.startActivity(x);
                    }
                }
            });

            inboxhost_name.setText(movieModel.getOtherUserName());

            if (movieModel.getIsMessageRead().equals("0")) {
                inbox_unready_msg.setVisibility(View.VISIBLE);
            } else {
                inbox_unready_msg.setVisibility(View.GONE);
            }
            if (movieModel.getLastMessage().equals("")) {
                inbox_lastmsg.setText(movieModel.getLastMessage());
                inbox_lastmsg.setVisibility(View.GONE);
            } else {
                inbox_lastmsg.setText(movieModel.getLastMessage());
                inbox_lastmsg.setVisibility(View.VISIBLE);
            }
            String currencycymbol = localSharedPreferences.getSharedPreferences(Constants.CurrencySymbol);

            inbox_amount.setText(currencycymbol + "" + movieModel.getTotalCost());
            inbox_amount.setVisibility(View.VISIBLE);
            inbox_tripstatus.setVisibility(View.VISIBLE);

            String tripStatus = movieModel.getMessageStatus();
            inbox_tripstatus.setText(tripStatus);
            InboxListAdapter.updateStatus(tripStatus, this.inbox_tripstatus);
            if (tripStatus.equals("Cancelled") || tripStatus.equals("Declined") || tripStatus.equals("Expired")) {
                inbox_tripstatus.setTextColor(context.getResources().getColor(R.color.text_shadow));
            } else if (tripStatus.equals("Accepted")) {
                inbox_tripstatus.setTextColor(context.getResources().getColor(R.color.background));
            } else if (tripStatus.equals("Pre-Approved") || tripStatus.equals("Pre-Accepted") || tripStatus.equals("Inquiry")) {
                inbox_tripstatus.setTextColor(context.getResources().getColor(R.color.yellow));
            } else if (tripStatus.equals("Pending")) {
                inbox_tripstatus.setTextColor(context.getResources().getColor(R.color.pink));
            }

            inbox_tripstatus.setVisibility(View.VISIBLE);
            inbox_amount.setVisibility(View.VISIBLE);

            if (tripStatus.equals("Resubmit")) {
                inboxaddress.setVisibility(View.GONE);
                inbox_amount.setVisibility(View.GONE);
                inboxhost_name.setText("Admin");
                inbox_tripstatus.setVisibility(View.INVISIBLE);
            }

            /*if (tripStatus.equals("Expired")) {
                inbox_receivedate.setVisibility(View.GONE);
            }else{
                inbox_receivedate.setVisibility(View.VISIBLE);
            }*/
            inbox_receivedate.setText(movieModel.getReservationDate());

            inboxaddress.setText(movieModel.getSpaceLocation());
            String host_user_image = movieModel.getOtherThumbImage();
            String reservationId = movieModel.getReservationId();
            Glide.with(context.getApplicationContext()).asBitmap().load(host_user_image).into(new BitmapImageViewTarget(inbox_host_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    inbox_host_image.setImageDrawable(circularBitmapDrawable);
                }
            });

            inbox_host_image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View vw) {
                    String host_user_image = movieModel.getOtherThumbImage();
                    Intent x = new Intent(context, ProfilePageActivity.class);
                    System.out.println("Other user id" + movieModel.getHostUserId());
                    x.putExtra("otheruserid", movieModel.getOtherUserId());
                    context.startActivity(x);
                }
            });

            String isRead = movieModel.getIsMessageRead();
            if (isRead.equals("")) {
            } else {
            }
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

    public void setLoadMoreListener(InboxListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void clear() {
        modelItems.clear();
        notifyDataSetChanged();
    }

    public static void updateStatus(String status, TextView tvTextView) {

        if (status != null && status.equals("Cancelled")) {
            tvTextView.setText(context.getResources().getString(R.string.cancelled));
        } else if (status != null && status.equals("Declined")) {
            tvTextView.setText(context.getResources().getString(R.string.declined));
        } else if (status != null && status.equals("Expired")) {
            tvTextView.setText(context.getResources().getString(R.string.expire));
        } else if (status != null && status.equals("Accepted")) {
            tvTextView.setText(context.getResources().getString(R.string.accepted));
        } else if (status != null && status.equals("Pre-Approved")) {
            tvTextView.setText(context.getResources().getString(R.string.preApproved));
        } else if (status != null && status.equals("Pre-Accepted")) {
            tvTextView.setText(context.getResources().getString(R.string.preAccepted));
        } else if (status != null && status.equals("Inquiry")) {
            tvTextView.setText(context.getResources().getString(R.string.inquiry));
        } else if (status != null && status.equals("Pending")) {
            tvTextView.setText(context.getResources().getString(R.string.pending));
        }

    }
}