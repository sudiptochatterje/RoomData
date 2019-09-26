package com.example.roomdata;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_NOTE)
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int note_id;

    public String name;

    public String description;

    @Ignore
    public String priority;

}
