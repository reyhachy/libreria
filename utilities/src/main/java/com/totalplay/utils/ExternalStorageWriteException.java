package com.totalplay.utils;

/**
 * Created by reype on 29/01/2018.
 * @deprecated
 */
public class ExternalStorageWriteException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public ExternalStorageWriteException() {
        super();
    }

    public ExternalStorageWriteException(String message) {
        super(message);
        this.message = message;
    }

    public ExternalStorageWriteException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}