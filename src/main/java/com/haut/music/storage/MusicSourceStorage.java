package com.haut.music.storage;

import com.haut.music.constant.CacheKey;
import com.haut.music.domain.MusicSource;
import org.springframework.stereotype.Service;

@Service
public class MusicSourceStorage extends BaseCacheStorage<MusicSource> {
    @Override
    protected String getCacheKey() {
        return CacheKey.MUSIC_SOURCE;
    }
}
