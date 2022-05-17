package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class folders extends AppCompatActivity {

    private ImageButton bookpage;
    private ImageButton friendspage;
    private ImageButton graphpage;
    private ImageButton treepage;

    private ImageButton folder2;
    private ImageButton timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        //하단바 버튼
        bookpage = findViewById(R.id.bookpage);
        bookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(folders.this, book.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        friendspage = findViewById(R.id.friendpage);
        friendspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(folders.this, friends.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        graphpage = findViewById(R.id.graphpage);
        graphpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(folders.this, graph.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });

        treepage = findViewById(R.id.treepage);
        bookpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(folders.this, studytree.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });


        // 폴더 이미지 클릭
        folder2 = findViewById(R.id.folder2);
        folder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(folders.this, notelist.class); // 인텐트 생성 (현재 액티비티, 이동하고 싶은 액티비티)
                    startActivity(intent); // 액티비티 이동
            } // 폴더 버튼 눌렀을 때
        });

        timetable = findViewById(R.id.timefolder);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(folders.this, notelist.class); // 나중에 시간표 불러오는 화면 생성 후 변경
                startActivity(intent); // 액티비티 이동
            }
        });


    }
}