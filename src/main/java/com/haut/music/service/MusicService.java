package com.haut.music.service;


import com.github.pagehelper.PageInfo;
import com.haut.music.domain.MusicUser;
import com.haut.music.domain.PlayList;
import com.haut.music.domain.Song;

/**
 * 音乐服务
 *
 */
public interface MusicService {
    /**
     * 搜索音乐
     *
     * @param condition
     * @param pageNum
     * @param source    来源
     * @param pageSize
     * @return
     */
    PageInfo<Song> searchSong(String condition, String source, Integer pageNum, Integer pageSize);

    /**
     * 搜索音乐服务中的用户
     *
     * @param q
     * @param source
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<MusicUser> searchMusicUser(String q, String source, Integer pageNum, Integer pageSize);


    /**
     * @param q
     * @param source
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<PlayList> searchPlayList(String q, String source, Integer pageNum, Integer pageSize);

    /**
     * 获取音乐
     *
     * @param source 来源
     * @param id     id
     * @return song
     */
    Song getSongBySourceAndId(String source, String id);

    /**
     * 获取音乐(无歌词)
     *
     * @param source 来源
     * @param id     id
     * @return song
     */
    Song getSongWithoutLyrBySourceAndId(String source, String id);

    /**
     * 获取 音乐的播放地址
     *
     * @param source 来源
     * @param id     id
     * @return url地址
     */
    String getSongUrlBySourceAndId(String source, String id);

    /**
     * 获取歌单的歌曲列表
     *
     * @param source
     * @param sourcePlayListId
     * @return
     */
    PlayList getSongsBySourceAndPlayListId(String source, String sourcePlayListId);

    Boolean updateCover();

    /**
     * 判断用户是否已收藏歌曲
     * @param user
     * @param song_id
     * @return
     */
    Boolean isSubscribe(String user,String song_id);


}
