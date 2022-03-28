package com.haut.music.mapper;

import com.haut.music.domain.User;

import java.util.List;

public interface UserMapper {

    User findByUsername(String username);

    User findUserInformationByUid(String uid);

    void generateUser(Long uid,String username,String password);

    List<Long> generateRamUser();

    Integer songSubscribe(String uid, String song_id);

    Integer isSongSubscribe(String uid, String song_id);

    Integer songDeleteSubscribe(String uid, String song_id);

    List<String> getSongSub(String uid, Integer pageNum, Integer pageSize);

}
