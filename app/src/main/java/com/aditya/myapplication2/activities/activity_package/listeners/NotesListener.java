package com.aditya.myapplication2.activities.activity_package.listeners;

import com.aditya.myapplication2.activities.activity_package.entities.Note;

//this is an interface class which is responsible for reading data from the database when note in the main activity is clicked
//this class is called inside the noteAdapter class
//hadneling note read or update functionality
public interface NotesListener
{
    void onNoteClicked(Note note, int position);

    void onNoteLongClicked(Note note, int position);
}