package com.haut.music.service;

import com.haut.music.domain.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 根据帐号和密码查找用户
     * @param username,password
     * 帐号和密码被实例化的User
     * @return
     * 若找到返回true
     */
    String userLoginByUsernameAndPass(String username,String password);
    User getUserInformation(String username);
}
