package com.haut.music.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private String uid;
    private String username;
    private String password;
    private String nickname;
    private Date birthday;
    private Integer gender;
    //个性签名
    private String imgUrl;
    private String sign;
}
