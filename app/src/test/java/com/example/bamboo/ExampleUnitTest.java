package com.example.bamboo;

import com.example.bamboo.opengl.utils.RandomTagPoint;
import com.example.bamboo.util.TimeUtil;

import org.junit.Test;

import java.sql.Array;
import java.sql.Time;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void timeUtilTest() {
        float[] arr = new float[10];
        RandomTagPoint.randomRadius(arr, 10);
        System.out.println(Arrays.toString(arr));
    }
}