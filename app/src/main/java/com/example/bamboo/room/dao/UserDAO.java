package com.example.bamboo.room.dao;

import com.example.bamboo.room.entity.UserEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.room.dao
 * @class describe
 * @time 2019/1/23 16:00
 * @change
 * @chang time
 * @class describe
 */
@Dao
public interface UserDAO {
    /**
     * 像本地输入库插入用户信息
     *
     * @param userEntity 用户信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

    /**
     * 查询本地数据库中user信息
     *
     * @param uId 查询的用户id
     * @return 查询出来的用户信息
     */
    @Query("select * from user where uId=:uId")
    Observable<UserEntity[]> selectUserIm(int uId);
}
