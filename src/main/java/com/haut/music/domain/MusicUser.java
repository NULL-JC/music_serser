package com.haut.music.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class MusicUser implements Serializable {
    private String id;
    private String name;
    private String headImageUrl;
}
