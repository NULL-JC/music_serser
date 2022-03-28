package com.haut.music.service.impl;

import com.github.pagehelper.PageInfo;
import com.haut.music.domain.Song;
import com.haut.music.mapper.RecommendMapper;
import com.haut.music.service.MusicService;
import com.haut.music.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RecommendServiceImpl implements RecommendService {

    private static String musicSource="nc";
    @Autowired
    private MusicService musicService;
    @Autowired
    private RecommendMapper recMapper;


    @Override
    public PageInfo<Song> recommendSongsByUid(String uid, Integer pageNum, Integer pageSize) {

        List<String> songIdList = recMapper.getRecSongIdByUid(uid,pageNum,pageSize);
        final CountDownLatch latch = new CountDownLatch(songIdList.size());
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Song> songs = new ArrayList<>();
        for (String songId : songIdList) {
            Runnable run = new Runnable() {
                public void run() {
                    Song song = musicService.getSongWithoutLyrBySourceAndId(musicSource, songId);
                    if (song != null) {
                        songs.add(song);
                    }
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
        return new PageInfo<>(songs);
    }
}
