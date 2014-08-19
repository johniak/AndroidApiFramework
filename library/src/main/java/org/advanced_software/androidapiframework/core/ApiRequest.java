package org.advanced_software.androidapiframework.core;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by johniak on 8/15/14.
 */
public class ApiRequest {

    private int status;
    private String responseBody;
    private String requestBody;
    private String method;
    private ArrayList<NameValuePair> urlArguments = new ArrayList<NameValuePair>();
    private ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();

    public final static String GET = "GET";
    public final static String PUT = "PUT";
    public final static String POST = "POST";
    public final static String DELETE = "DELETE";
    public final static String HEAD = "HEAD";
    public final static String OPTIONS = "OPTIONS";
    public final static String PATCH = "PATCH";

    private int connectionTimeout = 10000;

    private String url;

    public ApiRequest(String url) {
        this.url = url;
    }

    public void request() throws IOException {
        String query = URLEncodedUtils.format(this.urlArguments, "utf-8");
        URL url = null;
        String urlText = this.url;
        if (urlText.contains("?")) {
            urlText += "&" + query;
        } else {
            urlText += "?" + query;
        }
        url = new URL(this.url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(this.method);
        for (NameValuePair header : this.headers) {
            httpURLConnection.setRequestProperty(header.getName(), header.getValue());
        }
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setConnectTimeout(connectionTimeout);
        if (requestBody != null)
            httpURLConnection.getOutputStream().write(this.requestBody.getBytes(Charset.forName("UTF-8")));
        this.status = httpURLConnection.getResponseCode();
        this.requestBody = inputStreamToString(httpURLConnection.getInputStream());
    }

    private String inputStreamToString(InputStream stream) throws IOException {
        InputStreamReader is = new InputStreamReader(stream);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while (read != null) {
            sb.append(read);
            read = br.readLine();

        }
        return sb.toString();
    }

    public void addHeader(NameValuePair header) {
        headers.add(header);
    }

    public void addUrlArgument(NameValuePair arg) {
        urlArguments.add(arg);
    }

    public void addHeaders(Collection<NameValuePair> header) {
        headers.addAll(header);
    }

    public void addUrlArguments(Collection<NameValuePair> arg) {
        urlArguments.addAll(arg);
    }

    public int getStatus() {
        return status;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
