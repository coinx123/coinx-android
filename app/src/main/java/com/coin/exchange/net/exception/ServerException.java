package com.coin.exchange.net.exception;

/**
 * Created by xiaoq on 17-3-16.
 * in LibModule
 */

public class ServerException extends RuntimeException{
    private int code;
    private String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
