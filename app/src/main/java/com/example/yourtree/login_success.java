package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login_success extends AppCompatActivity {

    private TextView tv_id;
    private Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        tv_id  = findViewById(R.id.tv_id); // tv_id 끼리 연결

        Intent intent = getIntent(); // intent 받아주기
        String ID = intent.getStringExtra("id"); // 보낸 곳의 매개변수 명과 동일 해야함.
        tv_id.setText(ID);

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_success.this, friends.class); // 인텐트 생성 (현재 액티비티, 이동하고 싶은 액티비티)
                startActivity(intent); // 액티비티 이동
            } // btn_go 버튼을 눌렀을 때
        });
    }
}