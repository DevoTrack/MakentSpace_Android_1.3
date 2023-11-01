package com.makent.trioangle.backgroundtask;
/**
 * @package com.trioangle.gofereats
 * @subpackage backgroundtask
 * @category ImageCompressAsyncTask
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.makent.trioangle.controller.AppController;
import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.interfaces.ImageListener;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*****************************************************************
 Compress ivPhoto while upload in background
 ****************************************************************/
public class ImageCompressAsyncTask extends AsyncTask<Void, Void, Void> {
    LocalSharedPreferences localSharedPreferences;
    private String filePath = "";
    private String roomId="";
    private String compressPath = "";
    private WeakReference<AppCompatActivity> compressImgWeakRef;
    private RequestBody requestBody;
    private ImageListener imageListener;

    public ImageCompressAsyncTask(FragmentActivity activity, String filePath, ImageListener imageListener, String roomId) {
        AppController.getAppComponent().inject(this);
        this.compressImgWeakRef = new WeakReference(activity);
        this.filePath = filePath;
        this.imageListener = imageListener;
        this.roomId=roomId;
        localSharedPreferences = new LocalSharedPreferences(activity);
    }

    /**
     * Call when before call the WS.
     */
    @Override
    protected void onPreExecute() {
        if (this.compressImgWeakRef == null) {
            this.cancel(true);
        }
    }

    /**
     * action to be performed in background
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    publishProgress();
                    System.out.println("filePathfilePath"+filePath);
                    file = new Compressor.Builder(this.compressImgWeakRef.get()).setMaxWidth(1080).setMaxHeight(1920).setQuality(100).setCompressFormat(Bitmap.CompressFormat.JPEG).build().compressToFile(file);
                    compressPath = file.getPath();
                    requestBody = uploadImgParam(compressPath);
                }
            }
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * called after the WS return the response.
     */
    @Override
    protected void onPostExecute(Void value) {
        if (compressImgWeakRef != null && compressImgWeakRef.get() != null && requestBody != null) {
            imageListener.onImageCompress(compressPath, requestBody);
        } else {
            imageListener.onImageCompress(compressPath, null);
        }
    }

    public RequestBody uploadImgParam(String imagePath) throws IOException {
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        File file = null;
        try {
            file = new File(imagePath);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            multipartBody.addFormDataPart("image", "IMG_" + timeStamp + ".jpg", RequestBody.create(MediaType.parse("ivPhoto/png"), file));
            if(!roomId.equals(""))
                multipartBody.addFormDataPart("space_id", roomId);

            multipartBody.addFormDataPart("token", localSharedPreferences.getSharedPreferences(Constants.AccessToken));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody formBody = multipartBody.build();
        return formBody;
    }
}