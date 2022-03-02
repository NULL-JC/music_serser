package com.haut.music.mapper;

import com.haut.music.domain.Song;

import java.util.List;

public interface RecommendMapper {

    List<Song> getRecSongByUid(Long  uid,Integer pageNum, Integer pageSize);
}
