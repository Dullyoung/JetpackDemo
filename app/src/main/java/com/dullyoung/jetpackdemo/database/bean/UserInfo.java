package com.dullyoung.jetpackdemo.database.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Dullyoung
 * Created by　Dullyoung on 2021/4/6
 **/
@Entity(tableName = "user")
public class UserInfo {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "age")
    public String age;

    @ColumnInfo(name = "address")
    public String address;

}
