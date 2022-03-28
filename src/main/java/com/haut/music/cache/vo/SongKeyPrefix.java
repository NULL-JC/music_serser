package com.haut.music.cache.vo;

import java.io.Serializable;

public class SongKeyPrefix extends BaseKeyPrefix implements Serializable {
    private static final int SONG_EXPIRE = 60*60*24*7;   //缓存有效时间7days

    private SongKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 音乐
     */
    public static SongKeyPrefix SONG = new SongKeyPrefix(SONG_EXPIRE, "id");

    public static SongKeyPrefix SONGURL = new SongKeyPrefix(SONG_EXPIRE, "id");

}
