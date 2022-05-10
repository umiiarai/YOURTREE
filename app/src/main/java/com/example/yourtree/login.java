package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private Button btn_login;
    private EditText et_login_id;
    private String login_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        et_login_id = findViewById(R.id.et_login_id);

        btn_login = findViewById(R.id.btn_login); // 같은 이름 버튼을 찾아옴
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_id = et_login_id.getText().toString(); // 입력한 내용을 받아옴. 받아올 때 string 형태가 아님으로 string형태로 바꿔줌
                Intent intent = new Intent(login.this, login_success.class); // 인텐트 생성 (현재 액티비티, 이동하고 싶은 액티비티)
                intent.putExtra("id", login_id); //데이터 담아주기 (매개변수, 실질적인 값)
                startActivity(intent); // 액티비티 이동
            } // btn_login 버튼을 눌렀을 때
        });
    } // oncreate은 해당 액티비티가 처음 실행될 때 안에 구문 한 번 실행
}