package com.andreysankov.itmoprog2sem.common.dto;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;

    public Response(boolean succes, String message) {
        this.success = succes;
        this.message = message;
    }

    public boolean isSuccess() { return this.success; }
    public String getMessage() { return this.message; }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + this.success +
                ", message='" + this.message + '\'' +
                '}';
    }
}
