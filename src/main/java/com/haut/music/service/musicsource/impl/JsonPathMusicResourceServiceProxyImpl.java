package com.haut.music.service.musicsource.impl;


import com.github.pagehelper.PageInfo;
import com.haut.music.cache.RedisService;
import com.haut.music.cache.vo.SongKeyPrefix;
import com.haut.music.constant.CacheKey;
import com.haut.music.domain.*;
import com.haut.music.exception.MusicException;
import com.haut.music.service.musicsource.IMusicResourceServiceProxy;
import com.haut.music.service.musicsource.conf.*;
import com.haut.music.storage.MusicSourceStorage;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 音乐服务对外入口
 *
 */
@Slf4j
@Service
@Primary
public class JsonPathMusicResourceServiceProxyImpl implements IMusicResourceServiceProxy {

    @Resource(name = "MusicSourceTemplate")
    private RestTemplate restTemplate;

    @Bean(name = "MusicSourceTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(
                (request, body, execution) -> {
                    request.getHeaders().set("Accept", "*/*");
                    request.getHeaders().set("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
                    request.getHeaders().set("Connection", "keep-alive");
                    request.getHeaders().set("Referer", "never");
                    return execution.execute(request, body);
                }
        ));
        return restTemplate;
    }

    @Autowired
    private MusicServerConfig musicServerConfig;
    @Autowired
    private RedisService redisService;

    /**
     * 根据id获取音乐信息
     *
     * @param source
     * @param id
     * @return
     */
    @Override
    public Song getSongById(String source, String id) {
        Song result = getSongWithoutLyrById(source, id);
        result.setLyric(getLyric(source, id));
        return result;
    }

    @Override
    public Song getSongWithoutLyrById(String source, String id) {
        Song result = redisService.get(SongKeyPrefix.SONG, source + "_" + id, Song.class);
        if (result == null) {
            Map<String, Object> uriVariables = new HashMap<>(2);
            uriVariables.put("id", id);
            MusicServerProperty property = musicServerConfig.getProperty(source);
            MusicApiGetSongConfig config = property.getApi().getSong();
            String resp = request(
                    property.getBaseUri() + "/" + config.getUri(),
                    uriVariables,
                    config.getMethod()
            );
            Object songJson = read(resp, config.getRoot());
            result = new Song();
            result.setId(read(songJson, config.getId()));
            result.setName(read(songJson, config.getName()));
            result.setCover(read(songJson, config.getCover()));

            JsonPathArtistsConfig artistsConfig = config.getArtists();
            List<Object> artistsJsonArray = read(songJson, artistsConfig.getRoot());
            if (artistsJsonArray != null && !artistsJsonArray.isEmpty()) {
                List<Artist> artists = new ArrayList<>();
                for (Object artistsJson : artistsJsonArray) {
                    Artist artist = new Artist();
                    artist.setId(read(artistsJson, artistsConfig.getId()).toString());
                    artist.setName(read(artistsJson, artistsConfig.getName()));
                    artists.add(artist);
                }
                result.setArtists(artists);
            }
            JsonPathAlumConfig albumConfig = config.getAlbum();
            Object albumJson = read(songJson,albumConfig.getRoot());
            Album album = new Album();
            album.setId(read(albumJson,albumConfig.getId()).toString());
            album.setName(read(albumJson,albumConfig.getName()));
            album.setPicUrl(read(albumJson,albumConfig.getPicUrl()));


            if(source.equals("qq")){
                String imgUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000"+ album.getId() +".jpg?max_age=2592000";
                album.setPicUrl(imgUrl);
                result.setCover(imgUrl);
            }

            result.setAlbum(album);


            if (config.isProcessProperties()
                    && config.getPropertiesAlias() != null
                    && !config.getPropertiesAlias().isEmpty()
            ) {
                Map<String, String> properties = new HashMap<>();
                config.getPropertiesAlias().forEach((k, exp) -> {
                    String value = read(songJson, exp);
                    if (!StringUtils.isEmpty(value)) {
                        properties.put(k, value);
                    }
                });
                if (!properties.isEmpty()) {
                    result.setProperties(properties);
                }
            }
            result.setSource(source);
            redisService.set(SongKeyPrefix.SONG,source + "_" + id,result);
        }
        return result;
    }


    @Override
    public String getSongUrlById(String source, String id) {

        String url = redisService.get(SongKeyPrefix.SONGURL, source + "_" + id, String.class);
        if (StringUtils.isEmpty(url)) {
            Map<String, Object> uriVariables = new HashMap<>(2);
            uriVariables.put("id", id);
            MusicServerProperty property = musicServerConfig.getProperty(source);
            MusicApiGetSongUrlConfig config = property.getApi().getSongUrl();
            if (config.isUseProperties()) {
                Song song = getSongWithoutLyrById(source, id);
                if (song.getProperties() != null && !song.getProperties().isEmpty()) {
                    uriVariables.putAll(song.getProperties());
                }
            }
            String resp = request(
                    property.getBaseUri() + "/" + config.getUri(),
                    uriVariables,
                    config.getMethod()
            );
            Object root = read(resp, config.getRoot());
            url = read(root, config.getUrl());
            redisService.set(SongKeyPrefix.SONGURL,source + "_" + id,url);
        }
        return url;
    }

    @Override
    public PageInfo<Song> searchSong(String q, String source, Integer pageNum, Integer pageSize) {
        Map<String, Object> uriVariables = new HashMap<>(8);
        uriVariables.put("q", q);
        uriVariables.put("pageNum", pageNum);
        uriVariables.put("pageSize", pageSize);
        uriVariables.put("offset", pageSize * (pageNum - 1));
        uriVariables.put("limit", pageSize);
        MusicServerProperty property = musicServerConfig.getProperty(source);
        MusicApiSearchSongConfig config = property.getApi().getSearchSong();
        String resp = request(
                property.getBaseUri() + "/" + config.getUri(),
                uriVariables,
                config.getMethod()
        );
        PageInfo<Song> result = new PageInfo<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        Object resultJson = read(resp, config.getRoot());
        Object total = read(resultJson, config.getTotal());
        if (total == null) {
            result.setTotal(0);
        } else {
            result.setTotal(Long.parseLong(total.toString()));
        }
        MusicApiSearchSongConfig.SongConfig listConfig = config.getList();
        List<Object> songJsonArray = read(resultJson, listConfig.getRoot());
        MusicApiSearchSongConfig.ArtistConfig artistConfig = listConfig.getArtists();
        if (songJsonArray != null && !songJsonArray.isEmpty()) {
            List<Song> list = new ArrayList<>();
            for (Object songJson : songJsonArray) {
                Song song = new Song();
                song.setId(read(songJson, listConfig.getId()).toString());
                song.setName(read(songJson, listConfig.getName()));
                List<Object> artistsJsonArray = read(songJson, artistConfig.getRoot());
                if (artistsJsonArray != null && !artistsJsonArray.isEmpty()) {
                    List<Artist> artists = new ArrayList<>();
                    for (Object artistsJson : artistsJsonArray) {
                        Artist artist = new Artist();
                        artist.setId(read(artistsJson, artistConfig.getId()).toString());
                        artist.setName(read(artistsJson, artistConfig.getName()));
                        artist.setPicUrl(read(artistsJson, artistConfig.getPicUrl()));
                        artists.add(artist);
                    }
                    song.setArtists(artists);
                }
                song.setSource(source);
                list.add(song);
            }
            result.setList(list);
        }
        return result;
    }

    @Override
    public PageInfo<MusicUser> searchMusicUser(String q, String source, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public PageInfo<PlayList> searchPlayList(String q, String source, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public String getLyric(String source, String id) {
        Map<String, Object> uriVariables = new HashMap<>(2);
        uriVariables.put("id", id);
        MusicServerProperty property = musicServerConfig.getProperty(source);
        MusicApiGetSongLyrConfig config = property.getApi().getSongLyric();
        if (config.isUseProperties()) {
            Song song = getSongWithoutLyrById(source, id);
            if (song.getProperties() != null && !song.getProperties().isEmpty()) {
                uriVariables.putAll(song.getProperties());
            }
        }
        String resp = request(
                property.getBaseUri() + "/" + config.getUri(),
                uriVariables,
                config.getMethod()
        );
        Object root = read(resp, config.getRoot());
        return read(root, config.getLyric());
    }

    @Override
    public PlayList getSongsBySourceAndPlayListId(String source, String playListId) {
        Map<String, Object> uriVariables = new HashMap<>(2);
        uriVariables.put("playListId", playListId);
        MusicServerProperty property = musicServerConfig.getProperty(source);
        MusicApiGetPlayListConfig config = property.getApi().getPlayList();
        if (config.isUseProperties()) {
            Song song = getSongWithoutLyrById(source, playListId);
            if (song.getProperties() != null && !song.getProperties().isEmpty()) {
                uriVariables.putAll(song.getProperties());
            }
        }
        String resp = request(
                property.getBaseUri() + "/" + config.getUri(),
                uriVariables,
                config.getMethod()
        );
        PlayList result = new PlayList();
        Object root = read(resp, config.getRoot());
        result.setName(read(root, config.getName()));
        List<Song> songs = new ArrayList<>();
        if (!StringUtils.isEmpty(config.getSongIds())) {
            List<String> songIds = read(root, config.getSongIds());
            if (songIds != null && !songIds.isEmpty()) {
                for (Object id : songIds) {
                    Song s = new Song();
                    s.setId(id);
                    s.setSource(source);
                    songs.add(s);
                }
            }
        } else {
            List<Object> songsJson = read(root, config.getSongs().getRoot());
            if (songsJson != null && !songsJson.isEmpty()) {
                for (Object o : songsJson) {
                    Song s = new Song();
                    s.setId(read(o, config.getSongs().getId()));
                    s.setName(read(o, config.getSongs().getName()));
                    s.setSource(source);
                    songs.add(s);
                }
            }
        }
        result.setSongs(songs);
        return result;
    }

    @Autowired
    private MusicSourceStorage musicSourceStorage;

    @PostConstruct
    public void initSources() {
        if (musicServerConfig.getServices() == null || musicServerConfig.getServices().isEmpty()) {
            throw new MusicException("没有可用的音乐服务");
        }
        musicServerConfig.getServices().forEach((id, value) -> {
            MusicSource source = new MusicSource();
            source.setEnabled(value.isEnabled());
            source.setLabel(value.getLabel());
            source.setUri(value.getBaseUri());
            source.setId(id);
            musicSourceStorage.save(source);
        });
    }

    private String request(String url, Map<String, ?> uriVariables, HttpMethod httpMethod) {
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        if (HttpMethod.GET.equals(httpMethod)) {
            return restTemplate.getForObject(url, String.class, uriVariables);
        }
        if (HttpMethod.PATCH.equals(httpMethod)) {
            return restTemplate.patchForObject(url, uriVariables, String.class, uriVariables);
        }
        if (HttpMethod.POST.equals(httpMethod)) {
            return restTemplate.postForObject(url, uriVariables, String.class, uriVariables);
        }
        if (HttpMethod.PUT.equals(httpMethod)) {
            restTemplate.put(url, uriVariables, uriVariables);
            return null;
        }
        if (HttpMethod.DELETE.equals(httpMethod)) {
            restTemplate.delete(url, uriVariables);
            return null;
        }
        throw new MusicException("不支持的类型[%s]", httpMethod.name());
    }

    private <T> T read(Object source, String exp) {
        if (source == null) {
            return null;
        }
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        if (StringUtils.isEmpty(exp)) {
            return null;
        }
        try {
            if (source instanceof String) {
                return JsonPath.read((String) source, exp);
            }
            return JsonPath.read(source, exp);
        } catch (PathNotFoundException pathNotFoundException) {
            log.warn("{} is not found", exp);
            return null;
        }
    }
}
