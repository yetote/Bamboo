package com.example.bamboo;

import com.example.bamboo.util.TimeUtil;

import org.junit.Test;

import java.sql.Time;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void timeUtilTest() {
        System.out.println(TimeUtil.agoTime(1541920173000L));
    }
}