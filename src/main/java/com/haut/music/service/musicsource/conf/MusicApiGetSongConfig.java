package com.haut.music.service.musicsource.conf;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MusicApiGetSongConfig extends BaseMusicApiConfig {
    private String id;
    private String name;
    private String cover;
    private JsonPathArtistsConfig artists;
    private JsonPathAlumConfig album;
    private boolean processProperties;
    private Map<String, String> propertiesAlias;
}
