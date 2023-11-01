package com.makent.trioangle.dependencies.interceptors;
/**
 * @package com.trioangle.gofereats
 * @subpackage dependencies.interceptors
 * @category AuthTokenInterceptor
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Context;

import com.makent.trioangle.controller.LocalSharedPreferences;
import com.makent.trioangle.helper.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*****************************************************************
 Auth Token Interceptor
 ****************************************************************/
public class AuthTokenInterceptor implements Interceptor {
    private Request.Builder requestBuilder;
   // private SessionManager sessionManager;
    LocalSharedPreferences localSharedPreferences;
    Context context;
    public AuthTokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request original = chain.request();
            localSharedPreferences = new LocalSharedPreferences(context);
            if (localSharedPreferences.getSharedPreferences(Constants.AccessToken) != null) {
                // Request customization: add request headers
                requestBuilder = original.newBuilder().header("Authorization", "bearer"+localSharedPreferences.getSharedPreferences(Constants.AccessToken)).method(original.method(), original.body());
            } else {
                // Request customization: add request headers
                requestBuilder = original.newBuilder().method(original.method(), original.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
