package org.advanced_software.androidapiframework.core;

/**
 * Used to provide result of called asynchronously API request.
 * @param <T>
 */
public interface OnApiResult<T> {

	public void onSuccess(T result);
    public void onError(ApiError result);
}
