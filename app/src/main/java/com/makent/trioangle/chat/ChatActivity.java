package com.makent.trioangle.chat;

/**
 * @package com.makent.trioangle
 * @subpackage chat
 * @category ChatActivity
 * @author Trioangle Product Team
 * @version 1.1
 */


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.ChatAdapter;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.PreAcceptActivity;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.Chat;
import com.makent.trioangle.model.Header;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.inbox.ChatData;
import com.makent.trioangle.model.inbox.ChatResult;
import com.makent.trioangle.spacebooking.views.RequestSpaceActivity;
import com.makent.trioangle.travelling.CancelReservationActivity;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.ConnectionDetector;
import com.makent.trioangle.util.RequestCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.makent.trioangle.helper.Constants.Bookingtype;
import static com.makent.trioangle.helper.Constants.RESERVATIONID;
import static com.makent.trioangle.helper.Constants.SESSIONID;
import static com.makent.trioangle.helper.Constants.SPECIALOFFERID;
import static com.makent.trioangle.util.Enums.REQ_CHAT_LIST;
import static com.makent.trioangle.util.Enums.REQ_SEND_CHAT;

/* ************************************************************
Chating between guest and host
*************************************************************** */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener, ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    //Recyclerview objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    protected boolean isInternetAvailable;
    //ArrayList of messages to store the thread messages
    private ArrayList<Chat> messages;
    private ArrayList<Chat> chatmessages;
    ArrayList<ChatData> chatData = new ArrayList<>();
    Chat messagObject;

    ChatResult chatResult;
    //Button to send new message on the thread
    private Button buttonSend;

    //EditText to send new message on the thread
    private EditText editTextMessage;
    TextView tvTranslate;

    RelativeLayout chatback, chatacceptbuttons;
    ImageView chatmenu,chat_backimg;
    LinearLayout chatmessage_details, acceptbutton,lltSendmessage;
    String spaceid, userid, hostid, reservationid, hostusername, tripstatus;
    LocalSharedPreferences localSharedPreferences;

    TextView conversation_status, chat_hostname, chat_booknow, chat_pre_approve, chat_decline;
    String message_type = "5", sendmessage;

    String specialofferstatus, specialofferid, username, senderstatus, receiverstatus, senderimage, sendername, sendermsgstatus, sendermsgdate, sendermessage, receiverimage, receivername, receivermsgstatus, receivermsgdate, receivermessage, buttontype;

    ImageView chat_dot_loader, send_dot_loader;
    private String messageStatus;
    private String lastMessage;

    private boolean isDeletedUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        commonMethods = new CommonMethods();
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);

        isInternetAvailable = getNetworkState().isConnectingToInternet();
        Intent x = getIntent();
        reservationid = x.getStringExtra("reservationid");
        hostid = x.getStringExtra("hostid");
        hostusername = x.getStringExtra("host_username");
        messageStatus = x.getStringExtra("trip_status");
        tripstatus = x.getStringExtra("trip_status");
        spaceid = x.getStringExtra("roomid");
        buttontype = x.getStringExtra("button_type");
        specialofferstatus = x.getStringExtra("special_offer");
        specialofferid = x.getStringExtra("special_offer_id");
        isDeletedUser = x.getBooleanExtra("deleted_user",false);
        lastMessage = x.getStringExtra("last_message");

      //  Log.e("Reservation","Reservation"+x.getStringExtra("reservationStatus"));

        System.out.println("Special_offer_status " + specialofferstatus + " Special_offer_id " + specialofferid);


        if(messageStatus==null)
            messageStatus="";

        chat_booknow = (TextView) findViewById(R.id.chat_booknow);
        tvTranslate = (TextView) findViewById(R.id.tv_translate);
        lltSendmessage= (LinearLayout) findViewById(R.id.sendmessage);
        chat_pre_approve = (TextView) findViewById(R.id.chat_pre_approve);
        chat_decline = (TextView) findViewById(R.id.chat_decline);
        acceptbutton = (LinearLayout) findViewById(R.id.acceptbutton);
        chatacceptbuttons = (RelativeLayout) findViewById(R.id.chatacceptbuttons);
        conversation_status = (TextView) findViewById(R.id.conversation_status);
        chat_backimg =(ImageView)findViewById(R.id.chat_backimg);
        commonMethods.rotateArrow(chat_backimg,this);


        if (buttontype == null) {
            chatacceptbuttons.setVisibility(View.GONE);
            acceptbutton.setVisibility(View.GONE);
            chat_booknow.setVisibility(View.GONE);
        } else if (buttontype.equals("Book Now")) {
            chatacceptbuttons.setVisibility(View.VISIBLE);
            acceptbutton.setVisibility(View.GONE);
            chat_booknow.setVisibility(View.VISIBLE);
            conversation_status.setPadding(0, 0, 200, 0);
        } else if (buttontype.equals("Inquiry")) {
            chatacceptbuttons.setVisibility(View.VISIBLE);
            acceptbutton.setVisibility(View.VISIBLE);
            chat_booknow.setVisibility(View.GONE);
        } else {
            chatacceptbuttons.setVisibility(View.GONE);
        }

        localSharedPreferences = new LocalSharedPreferences(this);
        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        //spaceid=localSharedPreferences.getSharedPreferences(Constants.mSpaceId);

        translateTextChange();

        chat_dot_loader = (ImageView) findViewById(R.id.chat_dot_loader);
        chat_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget1 = new DrawableImageViewTarget(chat_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget1);

        send_dot_loader = (ImageView) findViewById(R.id.send_dot_loader);
        send_dot_loader.setVisibility(View.GONE);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(send_dot_loader);
        Glide.with(getApplicationContext()).load(R.drawable.dot_loading).into(imageViewTarget);


        chat_hostname = (TextView) findViewById(R.id.chat_hostname);
        conversation_status.setText(tripstatus);

        updateStatus(tripstatus,conversation_status);

        chat_hostname.setText(hostusername);


        if(messageStatus.equals("Resubmit")) {
            lltSendmessage.setVisibility(View.GONE);
            tvTranslate.setVisibility(View.GONE);
        }else {
            lltSendmessage.setVisibility(View.VISIBLE);
            tvTranslate.setVisibility(View.VISIBLE);
        }

            //Initializing recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing message arraylist
        messages = new ArrayList<>();
        chatmessages = new ArrayList<>();

        //Calling function to fetch the existing messages on the thread
        // fetchMessages();

        //initializing button and edittext
        buttonSend = (Button) findViewById(R.id.buttonSend);
        chatback = (RelativeLayout) findViewById(R.id.chatback);
        chatmessage_details = (LinearLayout) findViewById(R.id.chatmessage_details);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        chatmenu = (ImageView) findViewById(R.id.chatmenu);

        //Adding listener to button
        buttonSend.setOnClickListener(this);
        chatback.setOnClickListener(this);
        chatmessage_details.setOnClickListener(this);
        chatmenu.setOnClickListener(this);

        chat_booknow.setOnClickListener(this);
        chat_pre_approve.setOnClickListener(this);
        chat_decline.setOnClickListener(this);
        chat_backimg.setOnClickListener(this);
        tvTranslate.setOnClickListener(this);


        chatmessage_details.setVisibility(View.VISIBLE);

        if (isInternetAvailable) {
            //new LoadChatData().execute();
            loadChatData();
        } else {
            commonMethods.snackBar(getResources().getString(R.string.interneterror),"",false,2,editTextMessage,editTextMessage,getResources(),this);
        }


    }

    private void translateTextChange() {

        boolean translateLangBool = localSharedPreferences.getSharedPreferencesBool(Constants.TranslateLanguage);

        System.out.println("Translated : "+translateLangBool);
        if(translateLangBool){
            tvTranslate.setText(getResources().getString(R.string.show_original_txt));
        }else{

            tvTranslate.setText(getResources().getString(R.string.translate_english));
        }

    }

    public  void updateStatus(String status, TextView tvTextView) {

        if (status != null && status.equals("Cancelled")) {
            tvTextView.setText(getResources().getString(R.string.cancelled));
        } else if (status != null && status.equals("Declined")) {
            tvTextView.setText(getResources().getString(R.string.declined));
        } else if (status != null && status.equals("Expired")) {
            tvTextView.setText(getResources().getString(R.string.expire));
        } else if (status != null && status.equals("Accepted")) {
            tvTextView.setText(getResources().getString(R.string.accepted));
        } else if (status != null && status.equals("Pre-Approved")) {
            tvTextView.setText(getResources().getString(R.string.preApproved));
        } else if (status != null && status.equals("Pre-Accepted")) {
            tvTextView.setText(getResources().getString(R.string.preAccepted));
        } else if (status != null && status.equals("Inquiry")) {
            tvTextView.setText(getResources().getString(R.string.inquiry));
        } else if (status != null && status.equals("Pending")) {
            tvTextView.setText(getResources().getString(R.string.pending));
        }else if (status != null && status.equals("Resubmit")) {
            tvTextView.setText(getResources().getString(R.string.resubmit));
        }

    }









    //method to scroll the recyclerview to bottom
    private void scrollToBottom() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 1)
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
        }
    }

    //This method will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return format.format(new Date());
    }

    //Sending message onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSend: {
                sendMessage();
            }
            break;
            case R.id.chat_backimg: {
                onBackPressed();
            }
            break;
            case R.id.chatmenu: {
                popupMenu();
            }
            case R.id.tv_translate: {
                boolean translateLangBool = localSharedPreferences.getSharedPreferencesBool(Constants.TranslateLanguage);
                if(translateLangBool)
                {
                    tvTranslate.setText(getResources().getString(R.string.translate_english));
                    localSharedPreferences.saveSharedPreferences(Constants.TranslateLanguage,false);
                }
                else
                {
                    tvTranslate.setText(getResources().getString(R.string.show_original_txt));
                    localSharedPreferences.saveSharedPreferences(Constants.TranslateLanguage,true);
                }
                adapter.notifyDataSetChanged();
            }
            break;
            case R.id.chatmessage_details: {
                // Intent tripSubDetails = new Intent(getApplicationContext(), TripsSubDetails.class);
                // startActivity(tripSubDetails);
            }
            break;
            case R.id.chat_booknow: {// Call book now page in web view
                /*String userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
                String url;
                if (specialofferstatus.equals("No")) {
                    url = "pay_now?reservation_id=" + reservationid + "&token=" + userid;

                } else {
                    url = "pay_now?special_offer_id=" + specialofferid + "&token=" + userid;

                }
                String baseurl = getResources().getString(R.string.baseurl);
                String weburl = baseurl + url;
                weburl = weburl.replaceAll(" ", "%20");
                System.out.println("Payment Page Url Chat Activity " + weburl);
                Intent x = new Intent(getApplicationContext(), PaymentWebView.class);
                x.putExtra("weburl", weburl);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);*/

                Intent x = new Intent(getApplicationContext(), RequestSpaceActivity.class);
                x.putExtra(SESSIONID,"");
                x.putExtra(RESERVATIONID,reservationid);
                x.putExtra(SPECIALOFFERID,specialofferid);
                x.putExtra(Bookingtype,"Yes");

                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);

            }
            break;
            case R.id.chat_pre_approve: {// Call pre accept page
                Intent x = new Intent(getApplicationContext(), PreAcceptActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("tripstatus", tripstatus);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;
            case R.id.chat_decline: {// Call cancellation page
                Intent x = new Intent(getApplicationContext(), CancelReservationActivity.class);
                x.putExtra("reservationid", reservationid);
                x.putExtra("tripstatus", tripstatus);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(x, bndlanimation);
            }
            break;


        }

    }

    public void popupMenu() {
        //openOptionsMenu();
        PopupMenu popup = new PopupMenu(this, chatmenu, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.chat_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //    Toast.makeText(getApplicationContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    //This method will fetch all the messages of the thread
    private void fetchMessages() {

        int[] userid = {101, 102};
        String[] names = {"Siva", "Sonai"};
        String[] status = {"Preapproval", "expired"};
        String[] messagess = {"Hai", "hai", "I'm Sonai", "I'm Siva", "How Are You Sonai", "Fine Siva", "Bye", "bye"};
        int type = 0;
        int userId = 102;
        String name = null;
        for (int i = 0; i < messagess.length + 2; i++) {
            System.out.println("Message" + status.length);

            String statuss = null;
            String message = null;
            String sentAt = null;

            if (i < status.length) {
                statuss = status[i];
                type = 1;
                messagObject = new Chat(null, null, null, null, type);
            } else {
                statuss = null;
                if (i % 2 == 0) {
                    userId = 102;
                    name = "Sonai";
                } else {
                    userId = 101;
                    name = "Siva";
                }
                message = messagess[i - 2];

                sentAt = getTimeStamp();
                type = 0;
                messagObject = new Chat(message, sentAt, name, null, type);
            }

            messages.add(messagObject);
        }
        userId = 101;

        adapter = new ChatAdapter(ChatActivity.this, chatData, name);
        recyclerView.setAdapter(adapter);
        scrollToBottom();
    }

    //Processing message to add on the thread
    private void processMessage(String name, String message, String id, String status) {
        Chat m = new Chat(message, getTimeStamp(), name, null, 0);
        messages.add(m);
        scrollToBottom();
    }

    //This method will send the new message to the thread
    private void sendMessage() {
        sendmessage = editTextMessage.getText().toString().trim();
        sendmessage = sendmessage.replaceAll("^\\s+|\\s+$", "");
        if (sendmessage.equalsIgnoreCase(""))
            return;
        isInternetAvailable = getNetworkState().isConnectingToInternet();
        if (isInternetAvailable) {
            //new sendMessage().execute(); // Send Message
            sendChat();
        } else {
            snackBar();
        }


        /*String sentAt = getTimeStamp();
        Chat m = new Chat(sendmessage, sentAt, sendername,senderimage,0);
        chatmessages.add(m);
        adapter.notifyDataSetChanged();

        scrollToBottom();

        editTextMessage.setText("");*/
    }

    public Header getHeader() {
        Header header = new Header();
        header.setHeader("I'm header");
        return header;
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_CHAT_LIST:
                if (jsonResp.isSuccess()) {
                    onSuccessChat(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,editTextMessage,editTextMessage,getResources(),this);
                }
                break;
            case REQ_SEND_CHAT:
                if (jsonResp.isSuccess()) {
                    onSuccessSend(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(),"",false,2,editTextMessage,editTextMessage,getResources(),this);
                }
                break;
            default:
                break;

    }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        chat_dot_loader.setVisibility(View.GONE);
    }

    public void loadChatData() {
        chat_dot_loader.setVisibility(View.VISIBLE);
        apiService.conversationList(localSharedPreferences.getSharedPreferences(Constants.AccessToken), hostid, reservationid).enqueue(new RequestCallback(REQ_CHAT_LIST,this));
    }

    public void onSuccessChat(JsonResponse jsonResp) {
        chatResult = gson.fromJson(jsonResp.getStrResponse(), ChatResult.class);
        ArrayList<ChatData> chtData = chatResult.getData();
        for (int i = 0; i < chtData.size(); i++) {
            if (chtData.get(i).getSenderMessageStatus()!=null&&!"".equals(chtData.get(i).getSenderMessageStatus()) && (chtData.get(i).getSenderMessageStatus().equals("0")) || chtData.get(i).getSenderMessageStatus().equals("1")) {
                if ("".equals(chtData.get(i).getSenderDetails().getDateTime()) && !"".equals(chtData.get(i).getSenderDetails().getInquiryTitle()))
                    chtData.get(i).setType(1);
                else
                    chtData.get(i).setType(0);
            } else {
                chtData.get(i).setType(2);
            }
            chatData.add(chtData.get(i));
        }


        if(isDeletedUser){
            acceptbutton.setVisibility(View.GONE);
            lltSendmessage.setVisibility(View.GONE);
        }else{
            lltSendmessage.setVisibility(View.VISIBLE);
        }
        sendername = chatResult.getSenderUserName();
        receivername = chatResult.getReceiverUserName();
        username = chatResult.getSenderUserName();
        senderimage = chatResult.getSenderThumbImage();
        receiverimage = chatResult.getReceiverThumbImage();
        adapter = new ChatAdapter(ChatActivity.this, chatData, username);
        recyclerView.setAdapter(adapter);
        scrollToBottom();
        chat_dot_loader.setVisibility(View.GONE);

    }


    public void sendChat(){
        send_dot_loader.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        apiService.sendMessage(localSharedPreferences.getSharedPreferences(Constants.AccessToken), spaceid, hostid, reservationid,message_type,sendmessage).enqueue(new RequestCallback(REQ_SEND_CHAT,this));
    }

    public void onSuccessSend(JsonResponse jsonResponse){
        System.out.println("Message Send");
        send_dot_loader.setVisibility(View.GONE);
        buttonSend.setVisibility(View.VISIBLE);
//commonMethods.getJsonValue(jsonResponse.getStrResponse(), "message", String.class)
        String cumessage = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "message", String.class);
        String sendtime =(String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "message_time", String.class);
        String sentAt = getTimeStamp();
        ChatData chtdata = new ChatData();
        chtdata.setType(0);
        chtdata.setSenderMessages(cumessage);
        chtdata.setConversationTime(sendtime);
        chtdata.setSenderThumbImage(senderimage);
        chatData.add(chtdata);
      /*  Chat m = new Chat(cumessage, sendtime, sendername, senderimage, 0);
        chatmessages.add(m);*/
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        scrollToBottom();

        editTextMessage.setText("");
    }

    // Show network error and exception
    public void snackBar() {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(buttonSend, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        RelativeLayout snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snackbar_background.setBackgroundColor(getResources().getColor(R.color.background));

        Button button = (Button) snackView.findViewById(R.id.snackbar_action);
        button.setVisibility(View.GONE);
        button.setText(getResources().getString(R.string.showpassword));
        button.setTextColor(getResources().getColor(R.color.background));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView textViewTop = (TextView) snackView.findViewById(R.id.snackbar_text);
        if (isInternetAvailable) {
        } else {
            textViewTop.setText(getResources().getString(R.string.Interneterror));
        }

        // textViewTop.setTextSize(getResources().getDimension(R.dimen.midb));
        textViewTop.setTextColor(getResources().getColor(R.color.title_text_color));

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
        snackbar.show();

    }


    public ConnectionDetector getNetworkState() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        return connectionDetector;
    }


}
