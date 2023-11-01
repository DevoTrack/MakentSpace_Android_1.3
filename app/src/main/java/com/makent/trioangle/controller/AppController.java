package com.makent.trioangle.controller;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  controller
 * @category    AppController
 * @author      Trioangle Product Team
 * @version     1.1
 */

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.util.Log;


import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.makent.trioangle.R;
import com.makent.trioangle.dependencies.component.AppComponent;
import com.makent.trioangle.dependencies.component.DaggerAppComponent;
import com.makent.trioangle.dependencies.module.ApplicationModule;
import com.makent.trioangle.dependencies.module.NetworkModule;

public class AppController extends Application {

	private static AppComponent appComponent;


	public static AppComponent getAppComponent() {
		Log.v("non","null"+appComponent);
		return appComponent;
	}

	public static final String TAG = AppController.class.getSimpleName();

	private static AppController mInstance;


	@Override
	public void onCreate() {
		super.onCreate();
		FirebaseApp.initializeApp(this);
		//mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		MultiDex.install(this);
		mInstance = this;

		// Dagger%COMPONENT_NAME%
		appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
				.networkModule(new NetworkModule(getResources().getString(R.string.baseurl))).build();
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}