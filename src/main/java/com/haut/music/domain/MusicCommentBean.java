package com.haut.music.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// 评论中间类
public class MusicCommentBean {
    private String id;
    //评论用户昵称
    private String userNickName;
    //评论用户头像
    private String userImgUrl;
    //评论内容
    private MusicComment musicComment;
}
