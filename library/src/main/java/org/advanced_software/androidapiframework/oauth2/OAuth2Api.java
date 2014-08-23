package org.advanced_software.androidapiframework.oauth2;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.advanced_software.androidapiframework.core.Api;
import org.advanced_software.androidapiframework.core.ApiError;
import org.advanced_software.androidapiframework.core.ApiRequest;
import org.advanced_software.androidapiframework.core.OnApiResult;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by johniak on 8/16/14.
 * <p/>
 * Api provides methods and credentials needed to authorization and authentication.
 */
public class OAuth2Api extends Api {

    protected OAuth2ApiCredentials apiCredentials;
    protected OAuth2ApiManager apiManager;
    protected OAuth2LoginCredentials loginCredentials;

    public OAuth2Api(OAuth2ApiManager apiManager) {
        super(apiManager);
        this.apiManager = apiManager;
    }

    public boolean isLogged() {
        return apiCredentials != null;
    }

    /**
     * Check if access token is expired (60 sec safety margin)
     * @return
     */
    public boolean isAccessTokenExpired() {
        long offsetTime=60;
        long actualTime = Calendar.getInstance().getTimeInMillis() / 1000;
        long expirationTime = apiCredentials.startTime + apiCredentials.expireIn+offsetTime;
        if (expirationTime >= actualTime)
            return true;
        return false;
    }

    /**
     * Provide authorization credentials from OAuth API.
     *
     * @param username
     * @param password
     * @param onApiResult
     */
    public void accessToken(String username, String password, final OnApiResult onApiResult) {
        loginCredentials.username = username;
        loginCredentials.password = password;
        loginCredentials.grantType = "password";
        loginCredentials.clientId=apiManager.getClientId();
        loginCredentials.clientSecret=apiManager.getClientSecret();

        OnApiResult<OAuth2ApiCredentials> onApiResultInner = new OnApiResult<OAuth2ApiCredentials>() {
            @Override
            public void onSuccess(OAuth2ApiCredentials result) {
                apiCredentials = result;
                apiCredentials.startTime = Calendar.getInstance().getTimeInMillis() / 1000;
                onApiResult.onSuccess(result);
            }

            @Override
            public void onError(ApiError result) {
                apiCredentials = null;
                onApiResult.onError(result);
            }
        };
        getParsedData(apiManager.getAccessTokenUrl(), ApiRequest.POST, loginCredentials, onApiResultInner, OAuth2ApiCredentials.class, false);
    }

    /**
     * Refresh api credentials, after access token expired
     *
     * @throws IOException
     */
    public void refreshToken() throws IOException {
        OAuth2RefreshCredentials credentials = new OAuth2RefreshCredentials();
        credentials.clientId = apiManager.getClientId();
        credentials.clientSecret = apiManager.getClientSecret();
        credentials.refreshToken = apiCredentials.refreshToken;
        ApiRequest request = new ApiRequest(apiManager.getRefreshTokenUrl());
        request.setMethod(ApiRequest.POST);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        request.setRequestBody(gson.toJson(credentials));
        request.request();
        OAuth2ApiCredentials newApiCredentials = null;
        if (request.getStatus() / 100 == 2) {
            newApiCredentials = gson.fromJson(request.getResponseBody(), OAuth2ApiCredentials.class);
            newApiCredentials.startTime = Calendar.getInstance().getTimeInMillis() / 1000;
        }
        apiCredentials = newApiCredentials;
    }
}
