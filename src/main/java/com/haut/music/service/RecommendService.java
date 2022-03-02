package com.haut.music.service;

import com.github.pagehelper.PageInfo;
import com.haut.music.domain.Song;

/**
 * 音乐推荐服务
 */
public interface RecommendService {

    /**
     * 推荐歌曲
     * @param uid
     * @return
     */
    PageInfo<Song> recommendSongsByUid(Long uid,Integer pageNum, Integer pageSize);
}
