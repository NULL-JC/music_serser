package com.haut.music.controller;

import com.github.pagehelper.PageInfo;
import com.haut.music.base.BaseController;
import com.haut.music.base.ExcepData;
import com.haut.music.base.RespCode;
import com.haut.music.base.RespEntity;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.Song;
import com.haut.music.domain.User;
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
    public RespEntity recommendSongsByUid(@RequestParam("token") String token,
                                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        return success(recService.recommendSongsByUid(user.getUid(),pageNum,pageSize));

    }
}
