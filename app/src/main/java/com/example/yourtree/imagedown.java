package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class imagedown extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    Button image_memo, image_ocr;


    // 사진 선택
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedown);

        imageview = (ImageView)findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }


    // 사진을 draw_memo로 전송
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

            image_memo = findViewById(R.id.image_memo);
            image_ocr = findViewById(R.id.image_ocr);
            image_memo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(imagedown.this, draw_memo.class);
                    intent1.putExtra("uri", selectedImageUri.toString());
                    startActivity(intent1);
                    finish();
                }
            });

            image_ocr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(imagedown.this, CameraText.class);
                    intent1.putExtra("uri", selectedImageUri.toString());
                    startActivity(intent1);
                    finish();
                }
            });
        }
    }



}