package com.haut.music;

import com.haut.music.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Test
    public void getData() throws ParseException {
        String string = "2022-03-31 02:28:05";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string );
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        // 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
        int month = ca.get(Calendar.MONTH) + 1;
        // 获取日
        int day = ca.get(Calendar.DAY_OF_MONTH);
        // 获取时
        int hour = ca.get(Calendar.HOUR);
        // int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示
        // 获取分
        int minute = ca.get(Calendar.MINUTE);
        // 获取秒
        int second = ca.get(Calendar.SECOND);
        System.out.println();
    }


}
