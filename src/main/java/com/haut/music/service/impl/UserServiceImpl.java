package com.haut.music.service.impl;

import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.User;
import com.haut.music.mapper.UserMapper;
import com.haut.music.service.UserService;
import com.haut.music.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public String userLoginByUsernameAndPass(String username, String password) {
        //判断用户是否存在（首先从缓存中取，再从数据库取)
        User user = this.getUserByUsername(username);
        if(user==null)
            return "-1";
        //判断username对应密码是否一致
        String pass = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(pass)){
            return "-2";
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        // 每次访问都会生成一个新的session存储于redis和反馈给客户端，一个session对应存储一个user对象
        redisService.set(UserKeyPrefix.TOKEN, token, user.getUsername());
        return token;
    }

    @Override
    public User getUserInformation(String username) {
        return getUserByUsername(username);
    }


    /**
     * 根据username查询用户信息
     * @param username
     * @return
     */
    private User getUserByUsername(String username) {
        //1.从redis中获取用户数据缓存
        User user = redisService.get(UserKeyPrefix.USER_USERNAME, "" + username, User.class);
        if(user!=null)
            return user;
        //2.如果缓存中没有用户数据 ，则从数据库中查询数据并将数据写入缓存
        User u = userMapper.findByUsername(username);
        if(user==null)
            return null;
        user = userMapper.findUserInformationByUid(u.getUid());
        user.setUsername(username);
        user.setPassword(u.getPassword());

        if(user!=null)
            redisService.set(UserKeyPrefix.USER_USERNAME, "" + username, user);
        return user;
    }
}
