package com.haut.music.controller;

import com.haut.music.base.BaseController;
import com.haut.music.base.RespEntity;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.User;
import com.haut.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public RespEntity login(@RequestParam("username") String username, @RequestParam("password") String password){

        Map<String, Object> map = new HashMap<>();
        String res = userService.userLoginByUsernameAndPass(username, password);
        if(res==null||res.isEmpty()){
            return success(false,"账户或密码不正确");
        }
        User user = userService.getUserInformation(username);
        user.setPassword("");
        map.put("token", res);
        map.put("user",user);
        return success(map);
    }

    @GetMapping("/getUserInformation/{token}")
    public RespEntity getUserInformation(@PathVariable("token") String token){
        //查看是否登录
        String username = redisService.get(UserKeyPrefix.TOKEN, token, String.class);
        if(username==null||username.isEmpty()){
            return success(false,"请先登录");
        }
        return success(userService.getUserInformation(username));
    }
}
