package com.haut.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.haut.music.domain.MusicUser;
import com.haut.music.domain.PlayList;
import com.haut.music.domain.Song;
import com.haut.music.domain.User;
import com.haut.music.mapper.SongMapper;
import com.haut.music.mapper.UserMapper;
import com.haut.music.service.MusicService;
import com.haut.music.service.musicsource.IMusicResourceServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class  MusicServiceImpl implements MusicService {
    @Autowired
    private IMusicResourceServiceProxy sourceProxy;

    @Override
    public PageInfo<Song> searchSong(String q, String source, Integer pageNum, Integer pageSize) {
        return sourceProxy.searchSong(q, source, pageNum, pageSize);
    }

    @Override
    public PageInfo<MusicUser> searchMusicUser(String q, String source, Integer pageNum, Integer pageSize) {
        return sourceProxy.searchMusicUser(q, source, pageNum, pageSize);
    }

    @Override
    public PageInfo<PlayList> searchPlayList(String q, String source, Integer pageNum, Integer pageSize) {
        return sourceProxy.searchPlayList(q, source, pageNum, pageSize);
    }

    @Override
    public Song getSongBySourceAndId(String source, String id) {
        return sourceProxy.getSongById(source, id);
    }

    @Override
    public Song getSongWithoutLyrBySourceAndId(String source, String id) {
        return sourceProxy.getSongWithoutLyrById(source,id);
    }

    @Override
    public String getSongUrlBySourceAndId(String source, String id) {
        return sourceProxy.getSongUrlById(source, id);
    }

    @Override
    public PlayList getSongsBySourceAndPlayListId(String source, String sourcePlayListId) {
        return sourceProxy.getSongsBySourceAndPlayListId(source, sourcePlayListId);
    }

    @Override
    public Boolean updateCover() {
        return true;
    }

    @Override
    public Boolean isSubscribe(String user, String song_id) {

        return null;
    }


}
