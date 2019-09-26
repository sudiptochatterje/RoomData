package com.example.roomdata;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "app.db";
    public static final String TABLE_NAME_NOTE = "note";

    public abstract DaoAccess daoAccess();

}
