package com.mynote.app;
// Rahmat - 10120150 - IF4

import android.database.Cursor;

import com.mynote.app.model.Note;

public interface NoteInterface {
    public Cursor read();
    public boolean create(Note note);
    public boolean update(Note note);
    public boolean delete(String id);

}
