package com.haut.music.base;


import com.haut.music.exception.MusicException;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class BaseController {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  泛型
     * @return 返回包装类对象
     */
    protected static <T> RespEntity<T> success(T data) {
        return new RespEntity<T>(data);
    }

    protected static <T> RespEntity<T> success(T data,String message) {
        return new RespEntity<T>(data,message);
    }


    protected static <T> RespEntity<T> success(boolean success, String message) {
        return new RespEntity<T>(success,message);
    }


    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     泛型
     * @return 返回包装类对象
     */
    protected static <T> RespEntity<T> fail(int code, String message) {
        return new RespEntity<T>(code, message);
    }

    /**
     * 普通错误
     *
     * @param message
     * @param <T>
     * @return
     */
    protected static <T> RespEntity<T> fail(String message) {
        return fail(RespCode.PARAM_ERROR, message);
    }

    /**
     * 异常处理
     *
     * @param e
     * @param <T>
     * @return
     */
    @ExceptionHandler
    private <T> RespEntity<T> exceptionHandle(Exception e) {
        if (e instanceof MusicException) {
            e.printStackTrace();
            return fail(((MusicException) e).getCode(), e.getMessage());
        }
        e.printStackTrace();
        return fail(RespCode.EXCEPTION, e.getMessage());
    }


}
