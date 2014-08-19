package org.advanced_software.androidapiframework.core;

/**
 * Created by johniak on 10/10/13.
 */
public class ApiError {

    public static final int UNDEFINED_CODE = -100;
    public static final int NO_INTERNET_CONNECTION_CODE = -101;
    public static final int JSONOBJECT_PARSE_CODE = -103;
    public static final int NULL_RESPONSE = -106;
    public static final int INVALID_LOGIN_CREDENTIALS = -107;
    public static final int WRONG_FILTER_FORMAT = -108;
    public static final int UNKNOWN_ERROR = -109;


    public static final String NO_INTERNET_CONNECTION_MSG = "No internet connection";
    public static final String NULL_RESPONSE_MSG = "Null server response";
    public static final String WRONG_FILTER_FORMAT_MSG = "Filter in wrong format";
    public static final String UNKNOWN_ERROR_MSG = "Unknown error";

    protected int statusCode;
    protected int errorCode;
    protected String message;


    public ApiError(int statusCode, int errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Message contains empty String.
     *
     * @param statusCode
     * @param errorCode
     */
    public ApiError(int statusCode, int errorCode) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = "";
    }

    /**
     * Status code is filled with error code.
     *
     * @param errorCode
     * @param message
     */
    public ApiError(int errorCode, String message) {
        this.statusCode = errorCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Message contains empty String.
     * Status code is filled with error code.
     *
     * @param errorCode
     */
    public ApiError(int errorCode) {
        this.statusCode = errorCode;
        this.errorCode = errorCode;
        this.message = "";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiError unknowError() {
        return new ApiError(UNKNOWN_ERROR, UNKNOWN_ERROR_MSG);
    }

    @Override
    public String toString() {
        return statusCode + ": " + message;
    }
}
