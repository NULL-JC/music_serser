package com.haut.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.haut.music.domain.MusicUser;
import com.haut.music.domain.PlayList;
import com.haut.music.domain.Song;
import com.haut.music.mapper.SongMapper;
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
    @Autowired
    private SongMapper songMapper;

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
    public String getSongUrlBySourceAndId(String source, String id) {
        return sourceProxy.getSongUrlById(source, id);
    }

    @Override
    public PlayList getSongsBySourceAndPlayListId(String source, String sourcePlayListId) {
        return sourceProxy.getSongsBySourceAndPlayListId(source, sourcePlayListId);
    }

    @Override
    public Boolean updateCover() {
        List<Song> songs = songMapper.selectList(new QueryWrapper<>());
        for (Song song : songs) {
            Song sourceSong = getSongBySourceAndId(song.getSource(), song.getId());
            if (sourceSong == null || StringUtils.isEmpty(sourceSong.getCover())) {
                continue;
            }
            Song t = new Song();
            t.setId(song.getId());
            t.setSource(song.getSource());
            Song entity = new Song();
            entity.setCover(sourceSong.getCover());
            songMapper.update(entity, new UpdateWrapper<>(t));
        }
        return true;
    }
}
