package com.haut.music.controller;

import com.github.pagehelper.PageInfo;
import com.haut.music.base.BaseController;
import com.haut.music.base.RespEntity;
import com.haut.music.domain.Song;
import com.haut.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/song")
public class SongController extends BaseController {
    @Autowired
    private MusicService musicService;

    @GetMapping("/search")
    public RespEntity<PageInfo<Song>> search(
            @RequestParam String keywords,
            @RequestParam String source,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return success(musicService.searchSong(keywords, source, pageNum, pageSize));
    }
}
