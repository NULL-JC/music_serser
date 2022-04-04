package com.haut.music.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MusicComment {

    private String uid;

    private String songId;

    private String content;

    private Long giveLike;

    private String createTime;

    private Integer state;

}
