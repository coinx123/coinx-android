package com.coin.libbase.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/24
 * @description
 */
public class ErrorBody {

    @SerializedName("message")
    public String message;
    @SerializedName("code")
    public int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ErrorBody{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
