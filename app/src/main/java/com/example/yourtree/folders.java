package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class folders extends AppCompatActivity {

    private ImageButton folder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        folder1 = findViewById(R.id.folder1);
        folder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(folders.this, notelist.class); // 인텐트 생성 (현재 액티비티, 이동하고 싶은 액티비티)
                    startActivity(intent); // 액티비티 이동
            } // 폴더 버튼 눌렀을 때
        });
    }
}