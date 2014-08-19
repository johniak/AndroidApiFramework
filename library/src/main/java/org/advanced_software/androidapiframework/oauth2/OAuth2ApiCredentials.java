package org.advanced_software.androidapiframework.oauth2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johniak on 8/16/14.
 */
public class OAuth2ApiCredentials {
    @Expose
    @SerializedName("access_token")
    public String accessToken;
    @Expose
    @SerializedName("refresh_token")
    public String refreshToken;
    @Expose
    public String scope;
    @Expose
    @SerializedName("expire_in")
    public long expireIn;

    public long startTime;
}
