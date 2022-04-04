package com.haut.music.service;

import com.haut.music.domain.BO.MusicCommentBO;

import java.util.List;

public interface CommentService {

    /**
     * 提交music评论
     * @param uid
     * @param songId
     * @param content
     * @return
     */
    Boolean addMusicComment(String uid, String songId, String content);

    /**
     * 获取音乐评论
     * @param token
     * @param songId
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<MusicCommentBO> getMusicComment(String token, String songId, Integer pageNum, Integer pageSize);

    /**
     * 评论点赞
     * @param uid
     * @param typeId
     * @param like
     * @return
     */
    Boolean commentLike(String uid, String typeId,Integer type, Boolean like);
}
