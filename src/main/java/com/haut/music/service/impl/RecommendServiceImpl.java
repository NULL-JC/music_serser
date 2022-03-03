package com.haut.music.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haut.music.domain.Artist;
import com.haut.music.domain.Song;
import com.haut.music.mapper.RecommendMapper;
import com.haut.music.mapper.UserMapper;
import com.haut.music.service.RecommendService;
import com.haut.music.service.musicsource.IMusicResourceServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RecommendServiceImpl implements RecommendService {

    private static String musicSource="NeteaseCloud";
    @Autowired
    private IMusicResourceServiceProxy sourceProxy;
    @Autowired
    private RecommendMapper recMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public PageInfo<Song> recommendSongsByUid(String username, Integer pageNum, Integer pageSize) {
        long uid = userMapper.findUidByUsername(username);
        List<Song> songs = recMapper.getRecSongByUid(uid,pageNum,pageSize);
        final CountDownLatch latch = new CountDownLatch(songs.size());
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Song song : songs) {
            Runnable run = new Runnable() {
                public void run() {
                    Song sourceSong = sourceProxy.getSongWithoutLyrById(musicSource, song.getId());

                    if (sourceSong == null) {
                        return;
                    }
                    song.setSource(musicSource);
                    song.setCover(sourceSong.getCover());
                    song.setArtists(sourceSong.getArtists());
                    song.setAlbum(sourceSong.getAlbum());
                    latch.countDown();
                }
            };
            pool.execute(run);

        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new PageInfo<Song>(songs);
    }
}
