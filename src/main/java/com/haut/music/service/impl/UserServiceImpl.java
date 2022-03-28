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
        Integer count = userMapper.isSongSubscribe(uid, song_id);
        if(sub&&count!=null|| !sub&&count==null)
            return true;
        if(sub){
            return userMapper.songSubscribe(uid, song_id)==1;
        }
        return userMapper.songDeleteSubscribe(uid, song_id)==1;
    }

    @Override
    public Boolean songIsSubscribe(String uid, String song_id) {
        Integer count = userMapper.isSongSubscribe(uid, song_id);
        return count != null;
    }

    @Override
    public PageInfo<Song> getSongSubList(String uid, Integer pageNum, Integer pageSize) {
        List<String> songIdList = userMapper.getSongSub(uid,pageNum,pageSize);
        List<Song> res = new ArrayList<>();
        for (String songId : songIdList) {
            String[] str = songId.split("_");
            Song song = musicService.getSongWithoutLyrBySourceAndId(str[0], str[1]);
            res.add(song);
        }
        return new PageInfo<>(res);
    }

}
