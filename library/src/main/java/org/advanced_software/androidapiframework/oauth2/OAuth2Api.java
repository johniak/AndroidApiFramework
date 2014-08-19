package org.advanced_software.androidapiframework.oauth2;


import org.advanced_software.androidapiframework.core.Api;
import org.advanced_software.androidapiframework.core.ApiError;
import org.advanced_software.androidapiframework.core.ApiRequest;
import org.advanced_software.androidapiframework.core.OnApiResult;

/**
 * Created by johniak on 8/16/14.
 *
 * Api provides methods and credentials needed to authorization and authentication.
 *
 */
public class OAuth2Api extends Api {

    protected String accessTokenUrl;
    protected String refreshTokenUrl;
    protected OAuth2ApiCredentials apiCredentials;
    protected OAuth2ApiManager apiManager;
    protected OAuth2LoginCredentials loginCredentials;

    public OAuth2Api(OAuth2ApiManager apiManager, String accessTokenUrl, String refreshTokenUrl) {
        super(apiManager);
        this.apiManager = apiManager;
        this.accessTokenUrl = accessTokenUrl;
        this.refreshTokenUrl = refreshTokenUrl;
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
        OnApiResult<OAuth2ApiCredentials> onApiResultInner = new OnApiResult<OAuth2ApiCredentials>() {
            @Override
            public void onSuccess(OAuth2ApiCredentials result) {
                apiCredentials = result;
                onApiResult.onSuccess(result);
            }

            @Override
            public void onError(ApiError result) {
                onApiResult.onError(result);
            }
        };
        getParsedData(accessTokenUrl, ApiRequest.POST, loginCredentials, onApiResultInner, OAuth2ApiCredentials.class, false);
    }


    public void refreshToken() {
        //ToDo
        OAuth2RefreshCredentials credentials = new OAuth2RefreshCredentials();
        credentials.clientId = loginCredentials.clientId;
        credentials.clientSecret = loginCredentials.clientSecret;
        credentials.refreshToken = apiCredentials.refreshToken;
    }
}
