package com.haut.music.mapper;

import com.haut.music.domain.User;

import java.util.List;

public interface UserMapper {

    Integer verifyPassword(String username, String password);

    User findByUsername(String username);
    User findUserInformationByUid(Long uid);


    void generateUser(Long uid,String username,String password);

    List<Long> generateRamUser();
}
