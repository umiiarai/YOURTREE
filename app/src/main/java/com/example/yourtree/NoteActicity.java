package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteActicity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_acticity);

        TextView notetitle = (TextView)findViewById(R.id.notetitle);
        TextView notecontent = (TextView)findViewById(R.id.notecontent);
        TextView notewriter = (TextView) findViewById(R.id.notewriter);
        TextView notedate = (TextView) findViewById(R.id.notedate);
        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        notetitle.setText(intent.getStringExtra("notetitle"));
        notecontent.setText(intent.getStringExtra("notecontent"));
        notewriter.setText(intent.getStringExtra("notewriter"));
        notedate.setText(intent.getStringExtra("notedate"));

        //noteimage.setImageResource(intent.getIntExtra("img", 0));

    }
}