package com.haut.music.service.impl;

import com.github.pagehelper.PageInfo;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.UserKeyPrefix;
import com.haut.music.domain.Song;
import com.haut.music.domain.User;
import com.haut.music.mapper.SongMapper;
import com.haut.music.mapper.UserMapper;
import com.haut.music.service.MusicService;
import com.haut.music.service.UserService;
import com.haut.music.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private UserMapper userMapper;


    @Override
    public String userLoginByUsernameAndPass(String username, String password) {
        //判断用户是否存在（首先从缓存中取，再从数据库取)
        User user = this.getUserByUsername(username);
        if(user==null)
            return "";
        //判断username对应密码是否一致
        String pass = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(pass)){
            return "";
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        // 每次访问都会生成一个新的session存储于redis和反馈给客户端，一个session对应存储一个user对象
        redisService.set(UserKeyPrefix.TOKEN, token, user);
        return token;
    }


    /**
     * 根据username查询用户信息
     * @param username
     * @return
     */
    private User getUserByUsername(String username) {
        User u = userMapper.findByUsername(username);
        if(u==null)
            return null;
        User user = userMapper.findUserInformationByUid(u.getUid());
        user.setUsername(username);
        user.setPassword(u.getPassword());
        return user;
    }

    @Override
    public Boolean songSubscribe(String uid, String song_id,Boolean sub) {
        int count = userMapper.songIsSubscribe(uid, song_id);
        if(sub&&count!=0|| !sub&&count==0)
            return true;
        if(sub){
            return userMapper.songSubscribe(uid, song_id)==1;
        }
        return userMapper.songDeleteSubscribe(uid, song_id)==1;
    }

    @Override
    public Boolean songIsSubscribe(String uid, String song_id) {
        int count = userMapper.songIsSubscribe(uid, song_id);
        return count != 0;
    }

    @Override
    public List<Song> getSongSubList(String uid, Integer pageNum, Integer pageSize) {
        List<String> songIdList = userMapper.getSongSub(uid,pageNum,pageSize);
        List<Song> res = new ArrayList<>();
        for (String songId : songIdList) {
            String[] str = songId.split("_");
            Song song = musicService.getSongWithoutLyrBySourceAndId(str[0], str[1]);
            res.add(song);
        }
        return res;
    }

    @Override
    public Boolean playlistSubscribe(String uid, String playlistId, Boolean sub) {
        int count = userMapper.playlistIsSubscribe(uid, playlistId);
        if(sub&&count!=0|| !sub&&count==0)
            return true;
        if(sub){
            return userMapper.playlistSubscribe(uid, playlistId)==1;
        }
        return userMapper.playlistDeleteSubscribe(uid, playlistId)==1;
    }

    @Override
    public Boolean playlistIsSubscribe(String uid, String playlistId) {
        int count = userMapper.playlistIsSubscribe(uid, playlistId);
        return count != 0;
    }

    @Override
    public List<String[]> getPlaylistSubList(String uid, Integer pageNum, Integer pageSize) {
        List<String> songIdList = userMapper.getPlaylistSub(uid,pageNum,pageSize);
        List<String[]> res = new ArrayList<>();
        for (String songId : songIdList) {
            String[] item = songId.split("_");
            res.add(item);
        }

        return res;
    }

}
