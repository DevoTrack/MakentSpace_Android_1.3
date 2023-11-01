package com.makent.trioangle.host;

/**
 * @package com.makent.trioangle
 * @subpackage host/tabs
 * @category HostLYS_Setp1_AddPhotoActivity
 * @author Trioangle Product Team
 * @version 1.1
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makent.trioangle.BuildConfig;
import com.makent.trioangle.R;
import com.makent.trioangle.adapter.host.RoomImageViewAdapter;
import com.makent.trioangle.backgroundtask.ImageCompressAsyncTask;
import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.Dialog_loading;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.interfaces.ApiService;
import com.makent.trioangle.interfaces.ImageListener;
import com.makent.trioangle.interfaces.ServiceListener;
import com.makent.trioangle.model.JsonResponse;
import com.makent.trioangle.model.host.RoomImageItem;
import com.makent.trioangle.model.host.addphotos.RemoveImageResult;
import com.makent.trioangle.util.CommonMethods;
import com.makent.trioangle.util.RequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import okhttp3.RequestBody;

import static com.makent.trioangle.util.Enums.REQ_DELETE_ROOM_IMAGE;
import static com.makent.trioangle.util.Enums.REQ_UPLOAD_ROOM_IMAGE;

/************************************************************************
 This is List Your Space Setp1 Page Contain Add Photos
 **************************************************************************  */

public class LYS_Setp1_AddPhoto extends AppCompatActivity implements ServiceListener, ImageListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public @Inject
    RunTimePermission runTimePermission;

    ImageView addphoto_back, addphoto_camera, addphoto_galary, delete_photo;

    LocalSharedPreferences localSharedPreferences;
    Dialog_loading mydialog;

    String userid, roomid;

    private static final int RESULT_LOAD_IMAGE = 3;
    private static final int CAMERA_REQUEST = 1;
    String imagepath, uploadedroomimage, uploadedroomimageid;

    GridView gridGallery;
    Handler handler;
    ArrayList<RoomImageItem> items;
    RoomImageViewAdapter adapter_grid;
    String listroomimage, listroomimageid;
    Bitmap bm;
    String[] roomimage, roomimageid;
    ArrayList<String> selected;
    RelativeLayout addphoto_title_show, addphoto_title_hide;
    TextView addphoto;
    int deleteimg = 0;
    boolean update = false;
    private Uri mImageCaptureUri;
    private File imageFile = null;
    private Uri imageUri;
    private String imagePath = "";
    int imageType = 0;// 0 for camera 1 for gallery
    RemoveImageResult removeImageResult;
    boolean removing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lys_setp1_addphoto);
        ButterKnife.bind(this);
        AppController.getAppComponent().inject(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        localSharedPreferences = new LocalSharedPreferences(this);

        commonMethods =new CommonMethods();

        userid = localSharedPreferences.getSharedPreferences(Constants.AccessToken);
        roomid = localSharedPreferences.getSharedPreferences(Constants.mSpaceId);
        listroomimageid = localSharedPreferences.getSharedPreferences(Constants.ListRoomImageId);
        listroomimage = localSharedPreferences.getSharedPreferences(Constants.ListRoomImage);
        Log.e("Take Poto","Take Poto Activity");

        items = new ArrayList<RoomImageItem>();

        addphoto_title_show = (RelativeLayout) findViewById(R.id.addphoto_title_show);
        addphoto_title_hide = (RelativeLayout) findViewById(R.id.addphoto_title_hide);
        addphoto = (TextView) findViewById(R.id.addphoto);
        addphoto_title_show.setVisibility(View.GONE);
        addphoto_title_hide.setVisibility(View.VISIBLE);

        addphoto_back = (ImageView) findViewById(R.id.addphoto_back);
        commonMethods.rotateArrow(addphoto_back,this);
        addphoto_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                onBackPressed();
            }
        });

        delete_photo = (ImageView) findViewById(R.id.delete_photo);
        delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                deleteRoomImages();// this is used to delet the room ivPhoto function
            }
        });

        addphoto_camera = (ImageView) findViewById(R.id.addphoto_camera);
        addphoto_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                imageType = 0;
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                checkAllPermission(permissions);
            }
        });

        addphoto_galary = (ImageView) findViewById(R.id.addphoto_galary);
        addphoto_galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                imageType = 1;
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                checkAllPermission(permissions);
            }
        });
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter_grid = new RoomImageViewAdapter(this, items);

        gridGallery.setAdapter(adapter_grid);

        update = false;

        loadImage(); // room ivPhoto load on this function

        gridGallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                adapter_grid.showDelete = true;
                adapter_grid.notifyDataSetChanged();
                delete_photo.setVisibility(View.VISIBLE);

                return false;
            }
        });

    }

    public void loadImage() {
        listroomimageid = localSharedPreferences.getSharedPreferences(Constants.ListRoomImageId);
        listroomimage = localSharedPreferences.getSharedPreferences(Constants.ListRoomImage);
        System.out.println("listroomimageid" + listroomimageid);
        System.out.println("listroomimage" + listroomimage);
        if (listroomimageid != null && listroomimage != null) {

            JSONArray image = null;
            JSONArray id = null;
            items.clear();

            try {
                image = new JSONArray(listroomimage);
                id = new JSONArray(listroomimageid);

                roomimage = new String[image.length()];
                roomimageid = new String[id.length()];

                for (int j = 0; j < image.length(); j++) {

                    if (j == 0) {
                        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages, image.getString(j));
                    }
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount, String.valueOf(image.length()));
                    roomimage[j] = image.getString(j);
                    roomimageid[j] = id.getString(j);
                    System.out.println("roomimageidjj]" + roomimageid[j]);
                    items.add(new RoomImageItem(roomimage[j], roomimageid[j], roomid));

                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        addphoto_title_show.setVisibility(View.VISIBLE);
                        addphoto_title_hide.setVisibility(View.GONE);
                        adapter_grid.notifyDataSetChanged();
                    }
                });
                if (roomimageid.length == 0) {
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages, null);
                    localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount, String.valueOf(0));
                    addphoto_title_show.setVisibility(View.GONE);
                    addphoto_title_hide.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImages, null);
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageCount, String.valueOf(0));
            addphoto_title_show.setVisibility(View.GONE);
            addphoto_title_hide.setVisibility(View.VISIBLE);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                String roomimagecount = localSharedPreferences.getSharedPreferences(Constants.ListRoomImageCount);
                if (Integer.parseInt(roomimagecount) == 0) {
                    addphoto.setText(getResources().getString(R.string.add_photo));
                } else if (Integer.parseInt(roomimagecount) == 1) {
                    addphoto.setText(roomimagecount + " " + getResources().getString(R.string.photo));
                } else if (Integer.parseInt(roomimagecount) > 1) {
                    addphoto.setText(roomimagecount + " " + getResources().getString(R.string.photos));
                }
            }
        });
    }

    public void deleteRoomImages() {

        selected = adapter_grid.getSelectedString();
        if (selected.size() > 0) {

            // for (int j = 0; j < selected.size(); j++) {
            String delt = selected.toString().replace(", ", ",").replaceAll("[\\[.\\]]", "");
            apiService.removeUploadedImage(localSharedPreferences.getSharedPreferences(Constants.AccessToken), roomid, delt).enqueue(new RequestCallback(REQ_DELETE_ROOM_IMAGE, this));
            //  }
        }
        adapter_grid.showDelete = false;
        adapter_grid.notifyDataSetChanged();
        delete_photo.setVisibility(View.GONE);
    }

    public void onSuccessRemove(JsonResponse jsonResp) {
        removeImageResult = gson.fromJson(jsonResp.getStrResponse(), RemoveImageResult.class);
        removing = false;
        deleteimg = deleteimg + 1;
        //if(deleteimg==selected.size()) {
        String arrJson = gson.toJson(removeImageResult.getRoomThumbImages());
        String roomimageid = gson.toJson(removeImageResult.getRoomImageId());
        update = true;
        if (arrJson.length() > 0) {
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage, arrJson.toString());
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId, roomimageid.toString());
        } else {
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage, null);
            localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId, null);
        }
        adapter_grid.clearSelectedString();
        loadImage();
        //  }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            /*Bitmap bp = (Bitmap) data.getExtras().get("data");

            bm = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            imagepath = ImageWrite(bm);*/
            imageUri = Uri.fromFile(imageFile);
            imagepath = imageUri.getPath();
//            commonMethods.showProgressDialog(this, customDialog);
            new ImageCompressAsyncTask(LYS_Setp1_AddPhoto.this, imagepath, this, roomid).execute();
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {

            mImageCaptureUri = data.getData();
            try {
                Bitmap bitmap;
                /*InputStream inputStream = getContentResolver().openInputStream(mImageCaptureUri);
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                if (sizeCheck > 5) {
                int sizeCheck = decodeSampledBitmapFromUri(inputStream, 0, 200, 200);

                System.out.println("Size check "+sizeCheck);
                    final BitmapFactory.Options option = new BitmapFactory.Options();
                    option.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, option);

                } else {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                }*/

                bitmap = decodeBitmap(mImageCaptureUri, this);
                imagepath = ImageWrite(bitmap);
                new ImageCompressAsyncTask(LYS_Setp1_AddPhoto.this, imagepath, this, roomid).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Bitmap decodeBitmap(Uri selectedImage, Context context)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 500;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(selectedImage), null, o2);
    }

    public static int decodeSampledBitmapFromUri(InputStream inputStream, int resId,
                                                 int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        System.out.println("options.inSampleSize Check " + options.inSampleSize);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return options.inSampleSize;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of ivPhoto
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        System.out.println("height check " + height + " width check " + width);

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public String ImageWrite(Bitmap bitmap) {


        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        File file = new File(extStorageDirectory, "slectimage.png");
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

        String imageInSD = "/sdcard/slectimage.png";

        return imageInSD;

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        /*if(myDialog.isShowing())
            myDialog.dismiss();*/
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data))
                return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_DELETE_ROOM_IMAGE:
                if (jsonResp.isSuccess()) {
                    onSuccessRemove(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.snackBar(jsonResp.getStatusMsg(), "", false, 2, delete_photo, getResources(), this);
                }
                break;
            case REQ_UPLOAD_ROOM_IMAGE:
                commonMethods.hideProgressDialog();
                if (jsonResp.isSuccess()) {
                    onRoomImageUpload(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.hideProgressDialog();
                    Snackbar snackbar = Snackbar
                            .make(addphoto_back, getString(R.string.upload_failed), Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                    snackbar.show();
                }
                break;
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }


    public void onRoomImageUpload(JsonResponse jsonResponse) {
        try {
            JSONObject user_jsonobj = new JSONObject(jsonResponse.getStrResponse());
            for (int i = 0; i < user_jsonobj.length(); i++) {

                String statuscode = user_jsonobj.getString("status_code");
                if (statuscode.matches("1")) {
                    uploadedroomimage = user_jsonobj.getString("image_urls");
                    uploadedroomimageid = user_jsonobj.getString("room_image_id");
                    update = true;
                    if (i == user_jsonobj.length() - 1) {
                        JSONArray arrJson = user_jsonobj.getJSONArray("room_thumb_images");
                        JSONArray roomimageid = user_jsonobj.getJSONArray("room_thumb_image_id");

                        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImage, arrJson.toString());
                        localSharedPreferences.saveSharedPreferences(Constants.ListRoomImageId, roomimageid.toString());
                        loadImage();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(addphoto_back, getString(R.string.upload_failed), Snackbar.LENGTH_LONG);

                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.background));
                    snackbar.show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Image To Compress and Update Using Post Method
     */
    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadRoomImage(requestBody, localSharedPreferences.getSharedPreferences(Constants.AccessToken)).enqueue(new RequestCallback(REQ_UPLOAD_ROOM_IMAGE, this));
        }
    }

    private void cameraGallery() {
        Log.e("Camera Activity","Camera Activity");
        if (imageType == 0) {
            //ImagePicker.cameraIntent(LYS_Setp1_AddPhoto.this);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            imageFile = commonMethods.cameraFilePath();
            imageUri = FileProvider.getUriForFile(LYS_Setp1_AddPhoto.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);
            try {
                List<ResolveInfo> resolvedIntentActivities = LYS_Setp1_AddPhoto.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                cameraIntent.putExtra("return-data", true);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, 1);
            commonMethods.refreshGallery(LYS_Setp1_AddPhoto.this, imageFile);
        } else {
            Intent intent = new Intent();
            intent.setType("ivPhoto/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), RESULT_LOAD_IMAGE);
        }
    }

    /**
     * Check Permissions
     *
     * @param permission
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            cameraGallery();
        }
    }

    /**
     * Permission granted or denied
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            cameraGallery();
        }
    }

    /**
     * If Deny open and Enable the permission
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog("Alert", message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Opens the APP Permission Settings Page
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

}
