package org.advanced_software.androidapiframework.oauth2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by johniak on 8/16/14.
 */
public class OAuth2LoginCredentials {
    @Expose
    @SerializedName("client_id")
    public String clientId;
    @Expose
    @SerializedName("client_secret")
    public String clientSecret;
    @Expose
    @SerializedName("grant_type")
    public String grantType;
    @Expose
    String username;
    @Expose
    String password;
}
