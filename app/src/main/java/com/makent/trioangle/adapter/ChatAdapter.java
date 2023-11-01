package com.makent.trioangle.adapter;

/**
 * @package com.makent.trioangle
 * @subpackage adapter
 * @category ChatAdapter
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.makent.trioangle.R;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.model.inbox.ChatData;

import java.util.ArrayList;

/**
 * Created by Belal on 5/29/2016.
 */
//Class extending RecyclerviewAdapter
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    //user id
    private int userId;
    private String username;
    private Context context;

    //Tag for tracking self message
    private int SELF = 786;
    public int OTHER = 0;
    public int STATUS = 1;


    //ArrayList of messages object containing all the messages in the thread
    private ArrayList<ChatData> messages;

    private LocalSharedPreferences localSharedPreferences;

    //Constructor
    public ChatAdapter(Context context, ArrayList<ChatData> messages, String username) {
        this.username = username;
        this.messages = messages;
        this.context = context;
        localSharedPreferences = new LocalSharedPreferences(context);
    }

    //IN this method we are tracking the self message
    @Override
    public int getItemViewType(int position) {
        //getting message object of current position
        ChatData message = messages.get(position);


        //If its owner  id is  equals to the logged in user id
        if (message.getType() == 1) {
            //Returning self
            System.out.println("Message status Inside" + message.getType());
            return STATUS;
        } else {
            if (message.getType() == 0) {
                //Returning self
                return SELF;
            }
            //else returning position
            return OTHER;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating view
        View itemView;
        //if view type is self
        System.out.println("View Holder " + viewType);
        if (viewType == STATUS) {
            //Inflating the layout self
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_status, parent, false);
        } else if (viewType == SELF) {
            //Inflating the layout self
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_own, parent, false);
        } else {
            //else inflating the layout others
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_other, parent, false);
        }
        //returing the view
        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //Adding messages to the views
        final ViewHolder vh = holder;
        if (holder.getItemViewType() == STATUS) {
            ChatData message = messages.get(position);
            System.out.println("messagechat0" + message.getSenderDetails().getInquiryTitle());
            holder.textViewMessage.setText(message.getSenderDetails().getInquiryTitle());
            holder.textViewTime.setText(message.getSenderDetails().getDate());


            // holder.textViewTime.setText(message.getName() + ", " + message.getSentAt());
            String profile = message.getSenderThumbImage();
            Glide.with(context.getApplicationContext()).asBitmap().load(profile).into(new BitmapImageViewTarget(vh.profile_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    vh.profile_image.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            boolean translateLang = localSharedPreferences.getSharedPreferencesBool(Constants.TranslateLanguage);
            ChatData message = messages.get(position);
            String profile = "";
            String messageText;

            holder.textViewTime.setText(message.getConversationTime());


            if (message.getType() == 0) {

                messageText = message.getSenderMessages();
                //holder.textViewMessage.setText(message.getSenderMessages());
                //translateTextToEnglish(message.getSenderMessages(),holder.textViewTime);
                profile = message.getSenderThumbImage();
            } else {
                messageText = message.getReceiverMessages();

                //holder.textViewMessage.setText(message.getReceiverMessages());
                profile = message.getReceiverThumbImage();
                //translateTextToEnglish(message.getReceiverMessages(),holder.textViewTime);
            }


            if(translateLang){
                translateTextToEnglish(messageText,holder.textViewMessage);
            }else{
                holder.textViewMessage.setText(messageText);
            }




            Glide.with(context.getApplicationContext()).asBitmap().load(profile).into(new BitmapImageViewTarget(vh.profile_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    vh.profile_image.setImageDrawable(circularBitmapDrawable);
                }
            });


        }
    }

    public void translateText(FirebaseTranslator langTranslator, TextView textView, String text)
    {
        //translate source text to english
        langTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                System.out.println("translated Text : " + translatedText);
                                textView.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();

                            }
                        });

    }

    public void downloadTranslatorAndTranslate(String langCode, TextView textView, String text) {
        //get source language id from bcp code

        System.out.println("Language Translation : "+FirebaseTranslateLanguage.languageForLanguageCode(langCode));

        int sourceLanguage;//= FirebaseTranslateLanguage.languageForLanguageCode(langCode);
        try{
             sourceLanguage = FirebaseTranslateLanguage
                    .languageForLanguageCode(langCode);
        }catch(Exception e){
            textView.setText(text);
            return;
        }


        //create translator for source and target languages
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage)
                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
                        .build();
        final FirebaseTranslator langTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //download language models if needed
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()

                .build();
        langTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.d("translator", "downloaded lang  model");
                                //after making sure language models are available
                                //perform translation
                                translateText(langTranslator, textView, text);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void translateTextToEnglish(String text, TextView textView) {
        //First identify the language of the entered text
        FirebaseLanguageIdentification languageIdentifier =
                FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {


                                    //download translator for the identified language
                                    // and translate the entered text into english
                                    Log.d("translator", "lang " + languageCode);
                                    Log.d("translator", "lang " + text);
                                    if (!languageCode.equals("und")&&!languageCode.equals("en")) {

                                        System.out.println("translator : lang : " + languageCode);
                                        downloadTranslatorAndTranslate(languageCode, textView, text);
                                    } else {
                                        textView.setText(text);
                                    }

                                }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,
                                        "Problem in identifying language of the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    //Initializing views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;
        public TextView textViewTime;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }
}
