package com.makent.trioangle.helper;

import android.content.Context;

import com.makent.trioangle.R;

import java.io.File;

public class CreateStoragePath {
    /**
     * Create A Folder For Both Uploads and Downloads
     * @param context Activity context
     * @param FolderName name of Your Folder
     * @param SubFolderName Sub Folder of your Folder
     *
     * Note: When you are Going to Create a sub Folder Start using "/" (slash) before the name of Sub Folder
     */

    public static File getCreateFolderPath(Context context, String FolderName, String SubFolderName){
        // To be safe, you should check that the SDCard is mounted
        // using getCacheDir().getAbsolutePath() before doing this.
        File mediaStorageDir = new File(context.getCacheDir().getAbsolutePath()
                + "/"
                + context.getResources().getString(R.string.app_name)
                + "/"
                +FolderName
                +SubFolderName);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "");
        return mediaFile;
    }
}
