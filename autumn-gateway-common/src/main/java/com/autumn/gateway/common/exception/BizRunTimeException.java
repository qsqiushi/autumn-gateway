package com.autumn.gateway.common.exception;

import lombok.Getter;

/**
 * @author qiushi
 */
@Getter
public class BizRunTimeException extends RuntimeException {
    /**
     * 异常对应的返回码
     */
    private final Integer code;
    /**
     * 异常对应的描述信息
     */
    private final String msg;

    public BizRunTimeException(String message) {
        super(message);
        this.code = 400;
        msg = message;
    }

    public BizRunTimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizRunTimeException(Exception e) {
        super(e);
        this.code = 500;
        msg = e.getMessage();
    }
}
