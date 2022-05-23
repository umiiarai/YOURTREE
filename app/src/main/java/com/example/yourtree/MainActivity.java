package com.example.yourtree;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView noteListView;
    private NoteListAdapter Adapter;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListView = (ListView) findViewById(R.id.noteListView);
        notesList = new ArrayList<Note>();
        notesList.add(new Note(""))
    }
}