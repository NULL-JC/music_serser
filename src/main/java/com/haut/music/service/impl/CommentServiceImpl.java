package com.haut.music.service.impl;

import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.BO.MusicCommentBO;
import com.haut.music.domain.MusicCommentBean;
import com.haut.music.domain.User;
import com.haut.music.mapper.CommentMapper;
import com.haut.music.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public Boolean addMusicComment(String uid, String songId,String content) {
        int count = commentMapper.addMusicComment(uid,songId,content);
        return count!=0;
    }


    @Override
    public List<MusicCommentBO> getMusicComment(String token, String songId, Integer pageNum, Integer pageSize){
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        List<MusicCommentBean> comments= commentMapper.getMusicComment(songId, pageNum, pageSize);
        List<MusicCommentBO> res = new ArrayList<>();
        for (MusicCommentBean comment : comments) {
            MusicCommentBO item = new MusicCommentBO();
            item.setOriginalComment(comment);
            if(user!=null&&user.getUid().equals(comment.getMusicComment().getUid())){
                item.setOwner(true);
            }
            if(user!=null){
                item.setLike(commentMapper.commentIsLike(user.getUid(),comment.getId(),0)==1);
            }
            res.add(item);
        }
        return res;
    }

    @Override
    public Boolean commentLike(String uid, String typeId, Integer type, Boolean like) {
        int count = commentMapper.commentIsLike(uid,typeId,type);
        if(like&&count!=0 || !like&&count==0){
            return true;
        }

        count=like? commentMapper.addCommentLike(uid,typeId,type):commentMapper.deleteCommentLike(uid,typeId,type);

        switch (type){
            case 0:
                if(like){
                    commentMapper.addMusicCommentLike(typeId);
                }
                else{
                    commentMapper.deleteMusicCommentLike(typeId);
                }
                break;
            case 1:
                break;
            default:

        }
        return count==1;
    }
}
