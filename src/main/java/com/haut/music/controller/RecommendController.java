package com.haut.music.controller;

import com.github.pagehelper.PageInfo;
import com.haut.music.base.BaseController;
import com.haut.music.base.RespEntity;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.Song;
import com.haut.music.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
public class RecommendController extends BaseController {

    @Autowired
    private RecommendService recService;
    @Autowired
    private RedisService redisService;

    @GetMapping("/songs")
    public RespEntity<PageInfo<Song>> recommendSongsByUid(@RequestParam("token") String token,
                                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        //查看是否登录
        String username = redisService.get(UserKeyPrefix.TOKEN, token, String.class);
        if(username==null||username.isEmpty()){
            return success(false,"请先登录");
        }
        return success(recService.recommendSongsByUid(username,pageNum,pageSize));

    }
}
