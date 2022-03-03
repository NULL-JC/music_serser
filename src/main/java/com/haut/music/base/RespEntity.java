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
    private String message;
    private T data;

    public RespEntity() {
    }

    public RespEntity(T data) {
        this.code = RespCode.SUCCESS;
        this.data = data;
    }
    public RespEntity(T data,String message) {
        this.code = RespCode.SUCCESS;
        this.data = data;
        this.message = message;
    }

    public RespEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
