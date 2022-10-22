package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class noteChoice extends AppCompatActivity {

    private ImageButton btn_photo;
    private ImageButton btn_camera;
    private ImageButton btn_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_choice);

        // 이미지 선택 버튼을 눌렀을 때
        btn_photo = findViewById(R.id.btn_Photo);
        btn_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoIntent  = new Intent(noteChoice.this, imagedown.class);
                startActivity(photoIntent);
            }
        });

        // 카메라 버튼을 눌렀을 때
        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent cameraIntent  = new Intent(noteChoice.this, CameraActivity.class);
                startActivity(cameraIntent);
            }
        });

        // PDF 버튼을 눌렀을 때
        btn_pdf = findViewById(R.id.btn_pdf);
        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pdfIntent  = new Intent(noteChoice.this, PDFViewer.class);
                startActivity(pdfIntent);
            }
        });
    }
}