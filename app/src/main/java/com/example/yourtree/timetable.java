package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class timetable extends AppCompatActivity {

    private ImageButton bookpage;
    private ImageButton friendspage;
    private ImageButton graphpage;
    private ImageButton treepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        //하단바 버튼
        bookpage = findViewById(R.id.bookpage);
        bookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(timetable.this, book.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        friendspage = findViewById(R.id.friendpage);
        friendspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(timetable.this, friends.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        graphpage = findViewById(R.id.graphpage);
        graphpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(timetable.this, graph.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        treepage = findViewById(R.id.treepage);
        bookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(timetable.this, studytree.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });
    }
}