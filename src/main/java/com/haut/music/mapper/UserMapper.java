package com.haut.music.mapper;

import java.util.List;

public interface UserMapper {

    String findPassByUsername(String username);

    Long findUidByUsername(String username);

    void generateUser(Long uid,String username,String password);

    List<Long> generateRamUser();
}
