package com.example.roomdata;
import androidx.room.*;

import org.w3c.dom.Node;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    long insertNote(Note note);

    @Insert
    void insertNoteList(List<Note> noteList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_NOTE)
    List<Note> fetchAllNotes();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_NOTE + " WHERE note_id = :noteId")
    Note fetchNoteListById(int noteId);

    @Update
    int updateNote(Note note);

    @Delete
    int deleteNote(Note note);
}

