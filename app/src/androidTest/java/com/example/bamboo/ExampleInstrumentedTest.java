package com.example.bamboo;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        int[][] arr = new int[][]{{1}, {2}, {3}, {4}};
        System.out.println(Arrays.toString(Tanzi.matrixReshape(arr, 4, 1)));
    }
}
