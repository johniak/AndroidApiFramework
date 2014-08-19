package org.advanced_software.androidapiframework.core;


import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.advanced_software.androidapiframework.debug.Logger;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by johniak on 18.03.14.
 * Base class of api, for example AuthApi, or SocialApi.
 * Contains methods useful to create API calls.
 */
public abstract class Api {
    protected BaseApiManager apiManager;

    /**
     * Gets the api manager, u can use different calls from it,
     * or save important for other APIs
     *
     * @param apiManager
     */
    public Api(BaseApiManager apiManager) {
        this.apiManager = apiManager;
    }

    /**
     * Asynchronous method to make API calls (method GET).
     *
     * @param url
     * @param urlArgs     GET parameters
     * @param onApiResult OnSuccess called when API response have status code is in range <200,300), OnError
     *                    when status is different, or something goes wrong.
     * @param classType   Type of result which is send as argument in OnSuccess method. JSON is parsed to this object.
     * @param needAuth    If it should send authorization credentials.
     */
    protected void getParsedDataGet(final String url, final List<NameValuePair> urlArgs, final OnApiResult onApiResult, final Class<?> classType, boolean needAuth) {
        ApiRequest request = new ApiRequest(url);
        request.addUrlArguments(urlArgs);
        request.setMethod(ApiRequest.GET);
        ApiRequestAsyncTask asyncTask = new ApiRequestAsyncTask(classType, onApiResult, needAuth);
        asyncTask.execute(request);
    }

    /**
     * Asynchronous method to make API calls (various request methods).
     *
     * @param url
     * @param method      Request method (e.g. POST, PUT, GET...)
     * @param requestBody Request body is send as JSON serialized from this object
     * @param onApiResult OnSuccess called when API response have status code is in range <200,300), OnError
     *                    when status is different, or something goes wrong.
     * @param classType   Type of result which is send as argument in OnSuccess method. JSON is parsed to this object
     * @param needAuth    If it should send authorization credentials.
     */
    protected void getParsedData(final String url, String method, Object requestBody, final OnApiResult onApiResult, final Class<?> classType, boolean needAuth) {
        ApiRequest request = new ApiRequest(url);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        request.setRequestBody(gson.toJson(requestBody));
        request.setMethod(method);
        ApiRequestAsyncTask asyncTask = new ApiRequestAsyncTask(classType, onApiResult, needAuth);
        asyncTask.execute(request);
    }

    /**
     * Asynchronous method to make API calls (various request methods).
     *
     * @param url
     * @param method      Request method (e.g. POST, PUT, GET...)
     * @param requestBody Request body is send as JSON serialized from this hash map.
     * @param onApiResult OnSuccess called when API response have status code is in range <200,300), OnError
     *                    when status is different, or something goes wrong.
     * @param classType   Type of result which is send as argument in OnSuccess method. JSON is parsed to this object.
     * @param needAuth    If it should send authorization credentials.
     */
    protected void getParsedData(final String url, String method, HashMap requestBody, final OnApiResult onApiResult, final Class<?> classType, boolean needAuth) {
        ApiRequest request = new ApiRequest(url);
        Gson gson = new Gson();
        request.setRequestBody(gson.toJson(requestBody));
        request.setMethod(method);
        ApiRequestAsyncTask asyncTask = new ApiRequestAsyncTask(classType, onApiResult, needAuth);
        asyncTask.execute(request);
    }

    /**
     * Method get from request (earlier executed) response body, and parse it in to object of specified type
     *
     * @param request   executed earlier ApiRequest
     * @param classType Type of returned object
     * @param <T>       Type of returned object
     * @return object of type classType
     * @see org.advanced_software.androidapiframework.core.ApiRequest
     */
    protected <T> T tryParseGson(ApiRequest request, final Class<T> classType) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(request.getResponseBody(), classType);
    }

    /**
     * Method get from request (earlier executed) response body, and parse it in to object of specified type.
     * Doesn't throw any Exception, returns ApiError instead.
     *
     * @param request      executed earlier ApiRequest
     * @param classType    Type of returned object
     * @param errorMessage Message of error set in ApiError when parsing was unsuccessful
     * @return object of type classType if it was successful or ApiError when it fails.
     * @see org.advanced_software.androidapiframework.core.ApiRequest
     * @see org.advanced_software.androidapiframework.core.ApiError
     */
    protected Object tryParseGson(ApiRequest request, final Class<?> classType, String errorMessage) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            return gson.fromJson(request.getResponseBody(), classType);
        } catch (Exception ex) {
            return new ApiError(request.getStatus(), ApiError.JSONOBJECT_PARSE_CODE, errorMessage + " " + request.getResponseBody());
        }
    }

    /**
     * Async task used to make API calls and parse it into Java objects.
     */
    private class ApiRequestAsyncTask extends AsyncTask<ApiRequest, Integer, Object> {
        Class<?> classType;
        OnApiResult onApiResult;
        boolean needAuth = true;

        public ApiRequestAsyncTask(final Class<?> classType, OnApiResult onApiResult, boolean needAuth) {
            this.classType = classType;
            this.onApiResult = onApiResult;
            this.needAuth = needAuth;
        }

        @Override
        protected Object doInBackground(ApiRequest... params) {
            ApiRequest request = params[0];
            return job(request);
        }

        protected Object job(ApiRequest request) {
            if (needAuth)
                apiManager.addAuthCredentials(request);
            try {
                request.request();
            } catch (IOException e) {
                return e;
            }
            if (request.getStatus() / 100 != 2) {
                return tryParseGson(request, ApiError.class, "Cannot parse response to ApiError.");
            }
            if (classType != null) {
                return tryParseGson(request, classType, "Cannot parse " + classType.getName() + ".");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o instanceof ApiError) {
                onApiResult.onError((ApiError) o);
                return;
            }
            if (o instanceof IOException) {
                onApiResult.onError(new ApiError(ApiError.UNKNOWN_ERROR, o.toString()));
                Logger.e(o);
                return;
            }
            onApiResult.onSuccess(o);
        }
    }
}
