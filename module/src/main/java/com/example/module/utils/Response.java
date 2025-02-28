package com.example.module.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response<T> {
    private final ResponseStatus status;
    private final T result;

    public Response(int status) {
        this.status = new ResponseStatus().setCode(status).setMsg(ResponseCode.getMsg(status));
        this.result = null;
    }

    public Response(int status, T result) {
        this.status = new ResponseStatus().setCode(status).setMsg(ResponseCode.getMsg(status));
        this.result = result;
    }
}
