package com.haut.music.controller;

import com.github.pagehelper.PageInfo;
import com.haut.music.base.BaseController;
import com.haut.music.base.RespEntity;
import com.haut.music.domain.Song;
import com.haut.music.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommend")
public class RecommendController extends BaseController {

    @Autowired
    private RecommendService recService;

    @GetMapping("/songs")
    public RespEntity<PageInfo<Song>> recommendSongsByUid(@RequestParam Long uid,
                                                          @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return success(recService.recommendSongsByUid(uid,pageNum,pageSize));

    }
}
