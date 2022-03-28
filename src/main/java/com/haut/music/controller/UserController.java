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
import com.haut.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public RespEntity login(@RequestParam("username") String username, @RequestParam("password") String password){


        String token = userService.userLoginByUsernameAndPass(username, password);
        if(token==null||token.isEmpty()){
            return success(false,new ExcepData(RespCode.ACCOUNT),"账户或密码不正确");
        }
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        user.setPassword("");
        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user",user);
        return success(data);
    }

    @GetMapping("/getUserInformation/{token}")
    public RespEntity getUserInformation(@PathVariable("token") String token){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);
        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        return success(user);
    }

    @PostMapping("/song_sub")
    public RespEntity subscribe(@RequestParam("token") String token,
                                @RequestParam("song_id") String songId,
                                @RequestParam("source") String source,
                                @RequestParam("sub") Boolean sub){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);

        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }

        userService.songSubscribe(user.getUid(), source + "_" + songId, sub);
        return success(null);
    }

    @GetMapping("/song_sub_check")
    public RespEntity isSubscribe(@RequestParam("token") String token,
                                  @RequestParam("song_id") String songId,
                                  @RequestParam("source") String source){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);

        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        Boolean favor = userService.songIsSubscribe(user.getUid(), source + "_" + songId);
        HashMap<String, Object> data = new HashMap<>();
        data.put("favor", favor);
        return success(data);
    }

    @GetMapping("/song_sublist")
    public RespEntity getSongSubList(@RequestParam("token") String token,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        //查看是否登录
        User user = redisService.get(UserKeyPrefix.TOKEN, token, User.class);

        if(user==null){
            return success(false,new ExcepData(RespCode.SIGN),"请先登录");
        }
        PageInfo<Song> data = userService.getSongSubList(user.getUid(), pageNum, pageSize);
        return success(data);
    }

}
