package com.haut.music.service;

import com.github.pagehelper.PageInfo;
import com.haut.music.domain.Song;
import com.haut.music.domain.User;

import java.util.List;

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

    /**
     * 收藏歌曲
     * @param uid
     * @param song_id
     * @return
     */
    Boolean songSubscribe(String uid,String song_id,Boolean sub);

    /**
     * 收藏歌曲
     * @param uid
     * @param song_id
     * @return
     */
    Boolean songIsSubscribe(String uid,String song_id);


    /**
     * 获取收藏音乐
     * @param uid
     * @return
     */
    PageInfo<Song> getSongSubList(String uid, Integer pageNum, Integer pageSize);
}
