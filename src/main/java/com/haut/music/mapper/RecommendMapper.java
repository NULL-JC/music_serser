package com.haut.music.mapper;

import com.haut.music.domain.Song;

import java.util.List;

public interface RecommendMapper {

    List<Song> getRecSongByUid(String uid,Integer pageNum, Integer pageSize);

    List<String> getRecSongIdByUid(String uid,Integer pageNum, Integer pageSize);
}
