package com.haut.music;

import com.haut.music.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MusicApplication.class})
public class GenerateUserTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getLearn(){
        List<Long> uids = userMapper.generateRamUser();
        int i=1;
        for (Long uid : uids) {
            userMapper.generateUser(uid, "test"+i,"e10adc3949ba59abbe56e057f20f883e");
            i++;
        }
    }
}
