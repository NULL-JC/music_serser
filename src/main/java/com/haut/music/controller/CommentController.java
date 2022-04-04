package com.haut.music.controller;

import com.github.pagehelper.PageInfo;
import com.haut.music.base.BaseController;
import com.haut.music.base.ExcepData;
import com.haut.music.base.RespCode;
import com.haut.music.base.RespEntity;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.BO.MusicCommentBO;
import com.haut.music.domain.Song;
import com.haut.music.domain.User;
import com.haut.music.service.CommentService;
import com.haut.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/music")
    public RespEntity musicComment(@RequestParam("token") String token,
                                          @RequestParam("songId") String songId,
                                          @RequestParam(value = "source",defaultValue = "nc") String source,
                                          @RequestParam("content") String content){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        Boolean res = commentService.addMusicComment(user.getUid(), source+"_"+songId, content);
        return success(null);
    }


    @GetMapping("/getMusicComment")
    public RespEntity getMusicComment(@RequestParam(value = "token", required = false) String token,
                                      @RequestParam(value = "source",defaultValue = "nc") String source,
                                      @RequestParam("songId") String songId,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize){

        List<MusicCommentBO> res = commentService.getMusicComment(token, source + "_" + songId, pageNum, pageSize);
        return success(new PageInfo<>(res));
    }

    @PostMapping("/like")
    public RespEntity musicLike(@RequestParam("token") String token,
                                @RequestParam("typeId") String typeId,
                                @RequestParam(value = "type",defaultValue = "0") Integer type,
                               @RequestParam(value = "like") Boolean like){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        Boolean res = commentService.commentLike(user.getUid(), typeId, type, like);
        return success(null);
    }}
