package com.haut.music.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户-播放列表
 *
 */
@Getter
@Setter
public class UserPlayList implements Serializable {
    private String userId;
    private String playListId;
}
