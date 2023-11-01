package com.makent.trioangle.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makent.trioangle.R;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CreateStoragePath;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.signin.LoginActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by trioangle on 30/7/18.
 */

public class CommonMethods {

    public static CustomDialog progressDialog;
    public CommonMethods() {
        AppController.getAppComponent().inject(this);
    }

    public Object getJsonValue(String jsonString, String key, Object object) {
        Object objct = object;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) objct = jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return objct;
    }

    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration,EditText edt,TextView txt, Resources getRes, Activity ctx) {
        // Create the Snackbar
        Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;
        // Snack bar visible duration
        if (duration == 1) snackbar = Snackbar.make(edt, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2)
            snackbar = Snackbar.make(edt, "", Snackbar.LENGTH_LONG);
        else snackbar = Snackbar.make(edt, "", Snackbar.LENGTH_SHORT);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        

        // Inflate our custom view
        View snackView = ctx.getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snack_button = (TextView) snackView.findViewById(R.id.snackbar_action);
        snack_message = (TextView) snackView.findViewById(R.id.snackbar_text);

        snackbar_background.setBackgroundColor(getRes.getColor(R.color.title_text_color)); // Background Color

        if (buttonvisible) // set Right side button visible or gone
            snack_button.setVisibility(View.VISIBLE);
        else snack_button.setVisibility(View.GONE);

        snack_button.setTextColor(getRes.getColor(R.color.background)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text
        snack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onclikedmsg"+message);
                if(message.equals(getRes.getString(R.string.invalidelogin))){
                    edt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edt.setSelection(edt.length());
                    txt.setText(R.string.hide);
                }else if (message.equals(getRes.getString(R.string.emailalreadyexits))){
                    System.out.println("oncliked");
                    Intent login=new Intent(ctx,LoginActivity.class);
                    login.putExtra("email",edt.getText().toString());
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ctx, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                    ctx.startActivity(login,bndlanimation);
                }else if (message.equals(getRes.getString(R.string.login_title))){
                    System.out.println("oncliked");
                    Intent login=new Intent(ctx,LoginActivity.class);
                    login.putExtra("email",edt.getText().toString());
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ctx, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                    ctx.startActivity(login,bndlanimation);
                }
                else if(message.equals(getRes.getString(R.string.login_title))){

                    System.out.println("oncliked");
                    Intent login=new Intent(ctx,LoginActivity.class);
                    login.putExtra("email",edt.getText().toString());
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ctx, R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                    ctx.startActivity(login,bndlanimation);

                }

            }
        });

        snack_message.setTextColor(getRes.getColor(R.color.background)); // set left side main message text color
        snack_message.setText(message);  // set left side main message text

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getRes.getColor(R.color.title_text_color));
        snackbar.show();
        System.out.println("Snack bar ended");

    }





    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration,View view, Resources getRes, Activity ctx) {
        // Create the Snackbar
        Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;
        // Snack bar visible duration
        if (duration == 1) snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2)
            snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        else snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = ctx.getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar_background);
        snack_button = (TextView) snackView.findViewById(R.id.snackbar_action);
        snack_message = (TextView) snackView.findViewById(R.id.snackbar_text);

        snackbar_background.setBackgroundColor(getRes.getColor(R.color.background)); // Background Color

        if (buttonvisible) // set Right side button visible or gone
            snack_button.setVisibility(View.VISIBLE);
        else snack_button.setVisibility(View.GONE);

        snack_button.setTextColor(getRes.getColor(R.color.background)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text
        snack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        snack_message.setTextColor(getRes.getColor(R.color.title_text_color)); // set left side main message text color
        snack_message.setText(message);  // set left side main message text

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getRes.getColor(R.color.background));
        snackbar.show();
        System.out.println("Snack bar ended");

    }

    public void showProgressDialog(AppCompatActivity mActivity, CustomDialog customDialog) {
        if (mActivity == null || customDialog == null || (customDialog.getDialog() != null && customDialog.getDialog().isShowing()))
            return;
        progressDialog = new CustomDialog(true);
        progressDialog.show(mActivity.getSupportFragmentManager(), "");
    }

    public void hideProgressDialog() {
        if (progressDialog == null || progressDialog.getDialog() == null || !progressDialog.getDialog().isShowing())
            return;
        progressDialog.dismissAllowingStateLoss();
        progressDialog = null;
    }

    //Show Dialog with message
    public void showMessage(Context context, AlertDialog dialog, String msg) {
        if (context != null && dialog != null && !((Activity) context).isFinishing()) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    //Create and Get Dialog
    public AlertDialog getAlertDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        return dialog;
    }

    public boolean isNotEmpty(Object s) {
        if (s == null) {
            return false;
        }
        if ((s instanceof String) && (((String) s).trim().length() > 0)) {
            return true;
        }
        if (s instanceof ArrayList) {
            return !((ArrayList<?>) s).isEmpty();
        }
        if (s instanceof Map) {
            return !((Map<?, ?>) s).isEmpty();
        }
        if (s instanceof List) {
            return !((List<?>) s).isEmpty();
        }

        if (s instanceof Object[]) {
            return !(((Object[]) s).length == 0);
        }
        return false;
    }

    public boolean isTaped() {
        if (SystemClock.elapsedRealtime() - Constants.mLastClickTime < 1000) return true;
        Constants.mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    public File cameraFilePath() {
        return new File(getDefaultCameraPath(), "Makent_" + System.currentTimeMillis() + ".png");
    }

    public String getDefaultCameraPath() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (path.exists()) {
            File test1 = new File(path, "Camera/");
            if (test1.exists()) {
                path = test1;
            } else {
                File test2 = new File(path, "100MEDIA/");
                if (test2.exists()) {
                    path = test2;
                } else {
                    File test3 = new File(path, "100ANDRO/");
                    if (test3.exists()) {
                        path = test3;
                    } else {
                        test1.mkdirs();
                        path = test1;
                    }
                }
            }
        } else {
            path = new File(path, "Camera/");
            path.mkdirs();
        }
        return path.getPath();
    }

    public void refreshGallery(Context context, File file) {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file); //out is your file you saved/deleted/moved/copied
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////// CAMERA ///////////////////////////////////
    public File getDefaultFileName(Context context) {
        File imageFile;
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) { // External storage path
            imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        } else {  // Internal storage path
            imageFile = new File(context.getFilesDir() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        }
        return imageFile;
    }
    public File getDefaultFileName(Context context,String type) {
        return new File(CreateStoragePath.getCreateFolderPath(context,"","") + File.separator +type+Constants.FILE_NAME + System.currentTimeMillis() + ".png");
    }



    public void clearFileCache(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetWorkAvailable(Context context, AlertDialog dialog, String msg) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo == null || (netInfo != null && !netInfo.isConnected())) {
                showMessage(context, dialog, msg);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isOnline(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public void rotateArrow(ImageView ivArrow, Context context) {
        if(context.getResources().getString(R.string.layout_direction).equals("1")){
            ivArrow.setRotation(180);
            System.out.println("### ---- > inside layout direction"+context.getResources().getString(R.string.layout_direction));
        }else{
            System.out.println("### ---- > inside layout direction"+context.getResources().getString(R.string.layout_direction));
            ivArrow.setRotation(0);
        }
    }


    public void NotrotateArrow(ImageView ivArrow, Context context) {
            ivArrow.setRotation(0);

    }


    public void setTvAlign(View vTextAlign, Context context) {
        if(context.getResources().getString(R.string.layout_direction).equals("1")){
            vTextAlign.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }else{
            vTextAlign.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }
    public void rotateFAB(FloatingActionButton floatingActionButton,Context context)
    {
        if(context.getResources().getString(R.string.layout_direction).equals("1")){
            floatingActionButton.setRotation(180);
        }else{
           floatingActionButton.setRotation(0);
        }
    }

    public String  change12To24Hr(String time){
        SimpleDateFormat h_mm_a   = new SimpleDateFormat("h:mm a");
        SimpleDateFormat hh_mm_ss = new SimpleDateFormat("HH:mm:ss");
        Date d1 =null;
        try {
            d1 = h_mm_a.parse(time);
            System.out.println (hh_mm_ss.format(d1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hh_mm_ss.format(d1);
    }

    public String  change24To12Hr(String time){
        SimpleDateFormat hh_mm_ss   = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat h_mm_a = new SimpleDateFormat("h:mm a");
        Date d1 =null;
        try {
            d1 = hh_mm_ss.parse(time);
            System.out.println (h_mm_a.format(d1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return h_mm_a.format(d1);
    }

    public String DateFirstUserFormat(String date){

        SimpleDateFormat input   = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 =null;
        try {
            d1 = input.parse(date);
            System.out.println (output.format(d1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.format(d1);
    }

    public String YearFirstAPIFormat(String date){
        SimpleDateFormat input   = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 =null;
        try {
            d1 = input.parse(date);
            System.out.println (output.format(d1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.format(d1);
    }

    public boolean checkStartTimeEnd(String strStartTime,String strEndTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        if (strStartTime.equals(strEndTime)) return false;
        try {
            Date inTime = sdf.parse(strStartTime);
            Date outTime = sdf.parse(strEndTime);
            return isTimeAfter(inTime, outTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            return date1.before(date2);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isTimeAfter(Date startTime, Date endTime) {
        return !endTime.before(startTime);
    }

    public String getDay(String date){
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("E");
        return format2.format(dt1);
    }


    public Spannable changeColorForStar(String text){
        Spannable wordtoSpan = new SpannableString(text);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), text.length()-1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        System.out.println("Sb "+wordtoSpan.toString());
        return wordtoSpan;
    }


    //Create and Get Dialog
    public AlertDialog getCloseAlertDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        return dialog;
    }

    public String ImageWrite(Bitmap bitmap, Context context, String type) {
        OutputStream outStream = null;
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String fileName=type+"_"+timeStamp+".png";
        File file = new File(CreateStoragePath.getCreateFolderPath(context,"Uploads","")+ File.separator,fileName);
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the storage directory if it does not exist
        if (! file.exists()){
            if (! file.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String imageInSD = file.toString();
        return imageInSD;

    }


}
