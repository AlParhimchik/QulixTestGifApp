package com.example.sashok.qulixgifapp.model.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sashok on 4.11.17.
 */

public class Response implements Serializable {

    @SerializedName("meta")
    public ErrorType mErrorType;

    public class ErrorType{
        @SerializedName("status")
        public int status;
        @SerializedName("msg")
        public String errorMessage;
    }
}
