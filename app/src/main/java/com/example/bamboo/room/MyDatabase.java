package com.example.bamboo.room;

import android.content.Context;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.room.entity.UserEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.room
 * @class describe
 * @time 2019/1/23 16:39
 * @change
 * @chang time
 * @class describe
 */
@Database(entities = {UserEntity.class}, version = 1, exportSchema = true)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase ourInstance;

    public static MyDatabase getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "bamboo.db").build();
        }
        return ourInstance;
    }

    public static void destroy() {
        ourInstance = null;
    }

    /**
     * 获取用户实体类的接口
     *
     * @return 用户实体
     */
    public abstract UserEntity getUserEntityDao();
}
