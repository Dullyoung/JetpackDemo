package com.dullyoung.jetpackdemo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dullyoung.jetpackdemo.database.Dao.UserDao;
import com.dullyoung.jetpackdemo.database.bean.UserInfo;

/**
 * @author Dullyoung
 * Created by　Dullyoung on 2021/4/6
 **/
@Database(entities = {UserInfo.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    private static AppDB mAppDB;

    public abstract UserDao userDao();

    public static AppDB getInstance(Context context) {
        if (mAppDB == null) {
            synchronized (AppDB.class) {
                if (mAppDB == null) {
                    mAppDB = Room.databaseBuilder(context,
                            AppDB.class, "user").build();
                }
            }
        }
        return mAppDB;
    }
}
