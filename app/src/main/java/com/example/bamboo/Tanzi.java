package com.example.bamboo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.OnAddFriendInterface;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.HuanXinHelper;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 添加好友
 * @time 2018/12/31 18:08
 * @change
 * @chang time
 * @class describe
 */
public class Tanzi extends AppCompatActivity {
    public static int[][] matrixReshape(int[][] nums, int r, int c) {
        if (nums.length * nums[0].length < r * c) {
            return nums;
        }
        int srcPoint = 0;
        int destRow, destColumn;
        destRow = destColumn = 0;
        int destSize = nums[0].length > c ? c : nums[0].length;
        int[][] newMatrix = new int[r][c];
        for (int i = 0; i < nums.length; i++) {
            if (destRow >= r) {
                break;
            }
            System.arraycopy(nums[i], srcPoint, newMatrix[destRow][destColumn], 0, destSize);
            if (c < nums[i].length) {
                destRow++;
                srcPoint += c - 1;
                destColumn = 0;
                i--;
            } else {
                srcPoint = 0;
            }

        }
        return newMatrix;
    }
}
