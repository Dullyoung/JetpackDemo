package com.dullyoung.jetpackdemo.database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dullyoung.jetpackdemo.database.bean.UserInfo;

import java.util.List;

/**
 * @author Dullyoung
 * Created by　Dullyoung on 2021/4/6
 **/
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<UserInfo> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<UserInfo> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    UserInfo findByName(String first, String last);

    @Insert
    void insertAll(UserInfo... users);

    @Delete
    void delete(UserInfo user);
}
