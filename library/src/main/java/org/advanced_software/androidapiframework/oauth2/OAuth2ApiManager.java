package org.advanced_software.androidapiframework.oauth2;

import android.content.Context;

import org.advanced_software.androidapiframework.core.ApiRequest;
import org.advanced_software.androidapiframework.core.BaseApiManager;
import org.advanced_software.androidapiframework.debug.Logger;
import org.apache.http.message.BasicNameValuePair;


/**
 * ApiManager with Oauth2 api authentication
 * Created by johniak on 8/16/14.
 */
public abstract class OAuth2ApiManager extends BaseApiManager {

    private OAuth2Api oAuth2Api = new OAuth2Api(this);

    public OAuth2ApiManager(Context context) {
        super(context);
    }

    public OAuth2Api getOAuth2Api() {
        return oAuth2Api;
    }

    protected boolean addAuthCredentials(ApiRequest request){
        try {
            if(oAuth2Api.isAccessTokenExpired())
                oAuth2Api.refreshToken();
        } catch (Exception e){
            Logger.e(e);
            e.printStackTrace();
            return false;
        }
        if(!oAuth2Api.isLogged())
            return false;
        request.addHeader(new BasicNameValuePair("Authorization","Bearer "+oAuth2Api.apiCredentials.accessToken));
        return true;
    }

    public abstract String getAccessTokenUrl();
    public abstract String getRefreshTokenUrl();
    public abstract String getClientId();
    public abstract String getClientSecret();
}
