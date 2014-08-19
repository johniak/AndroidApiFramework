package org.advanced_software.androidapiframework.oauth2;

import android.content.Context;

import org.advanced_software.androidapiframework.core.BaseApiManager;


/**
 * ApiManager with Oauth2 api authentication
 * Created by johniak on 8/16/14.
 */
public abstract class OAuth2ApiManager extends BaseApiManager {
    public OAuth2ApiManager(Context context) {
        super(context);
    }

}
