package com.haut.music.service.impl;

import com.haut.music.mapper.UserMapper;
import com.haut.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean userLoginByUsernameAndPass(String username, String password) {
        String pass = DigestUtils.md5DigestAsHex(password.getBytes());
        int cnt = userMapper.verifyPassword(username, pass);
        if(cnt>0){
            return true;
        }
        return false;
    }
}
