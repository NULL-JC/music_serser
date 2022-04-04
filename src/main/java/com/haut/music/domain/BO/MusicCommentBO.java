package com.haut.music.domain.BO;

import com.haut.music.domain.MusicCommentBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicCommentBO {
    //原评论
    private MusicCommentBean originalComment;
    //评论拥有者
    private boolean owner;
    //点赞
    private boolean like;

}
