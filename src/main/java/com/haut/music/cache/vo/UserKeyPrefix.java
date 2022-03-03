package com.haut.music.cache.vo;

import java.io.Serializable;

public class UserKeyPrefix extends BaseKeyPrefix implements Serializable {
    public static final int TOKEN_EXPIRE = 30*60;   //缓存有效时间30min

    public UserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 用户cookie
     */
    public static UserKeyPrefix TOKEN = new UserKeyPrefix(TOKEN_EXPIRE, "token");
    /**
     * 用于存储用户对象到redis的key前缀
     */
    public static UserKeyPrefix USER_USERNAME = new UserKeyPrefix(0, "id");

}
