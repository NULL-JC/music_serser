package com.haut.music.mapper;

import com.haut.music.domain.MusicCommentBean;

import java.util.List;

public interface CommentMapper {

    Integer addMusicComment(String uid, String songId, String content);

    List<MusicCommentBean> getMusicComment(String songId, Integer pageNum, Integer pageSize);

    Integer commentIsLike(String uid, String typeId, Integer type);

    Integer addCommentLike(String uid, String typeId, Integer type);

    Integer deleteCommentLike(String uid, String typeId, Integer type);

    void addMusicCommentLike(String songCommentId);

    void deleteMusicCommentLike(String songCommentId);
}
