package com.makent.trioangle.dependencies.module;
/**
 * @package com.trioangle.com.makent.trioangle
 * @subpackage dependencies.module
 * @category AppContainerModule
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.helper.CustomDialog;
import com.makent.trioangle.helper.RunTimePermission;
import com.makent.trioangle.model.JsonResponse;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/*****************************************************************
 App Container Module
 ****************************************************************/
@Module(includes = com.makent.trioangle.dependencies.module.ApplicationModule.class)
public class AppContainerModule {
    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }


    @Provides
    @Singleton
    public Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public ArrayList<String> providesStringArrayList() {
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    public JsonResponse providesJsonResponse() {
        return new JsonResponse();
    }

    @Provides
    @Singleton
    RunTimePermission providesRunTimePermission() {
        return new RunTimePermission();
    }

    @Provides
    @Singleton
    CustomDialog providesCustomDialog() {
        return new CustomDialog();
    }


}
