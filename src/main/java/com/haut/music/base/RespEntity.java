package com.haut.music.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回值
 *
 * @author dezhishen
 */
@Getter
@Setter
public class RespEntity<T> {
    private Integer code;
    private boolean success;
    private String message;
    private T data;

    public RespEntity() {
    }


    public RespEntity(T data) {
        this.success=true;
        this.code = RespCode.SUCCESS;
        this.data = data;
    }
    public RespEntity(boolean success, T data) {
        this.code = RespCode.SUCCESS;
        this.success = success;
        this.data = data;
    }

    public RespEntity(boolean success, T data, String message) {
        this.code = RespCode.SUCCESS;
        this.success = success;
        this.message = message;
        this.data = data;
    }


    public RespEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
