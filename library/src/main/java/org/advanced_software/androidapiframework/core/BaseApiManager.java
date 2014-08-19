package org.advanced_software.androidapiframework.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by johniak on 18.03.14.
 * Base class of ApiManager.
 * Derived class should contains instances of all APIs class.
 * If you want to have only one instance don't use singleton! Put it in your custom Application class.
 * <p/>
 * Use own manager for each auth method (e.g. OAuth2, OAuth, Cookies,...)
 *
 * @see android.app.Application
 */
public abstract class BaseApiManager {
    Context context;


    public BaseApiManager(Context context) {
        this.context = context;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * You can add here any authorization credentials needed in request.
     *
     * @param request api request before executing
     */
    protected abstract void addAuthCredentials(ApiRequest request);


}



