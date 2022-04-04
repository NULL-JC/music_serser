package com.haut.music.mapper;

import com.haut.music.domain.User;

import java.util.List;

public interface UserMapper {

    User findByUsername(String username);

    User findUserInformationByUid(String uid);

    void generateUser(Long uid,String username,String password);

    List<Long> generateRamUser();

    Integer songSubscribe(String uid, String songId);

    Integer songIsSubscribe(String uid, String songId);

    Integer songDeleteSubscribe(String uid, String songId);

    List<String> getSongSub(String uid, Integer pageNum, Integer pageSize);

    Integer playlistIsSubscribe(String uid, String playlistId);

    Integer playlistSubscribe(String uid, String playlistId);

    Integer playlistDeleteSubscribe(String uid, String playlistId);

    List<String> getPlaylistSub(String uid, Integer pageNum, Integer pageSize);
}
