package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchView extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        editText = findViewById(R.id.editText);

        button = findViewById(R.id.key_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length()==0){
                    Log.e("test","키워드를 입력해주세요.");
                    Toast.makeText(SearchView.this,
                            "키워드를 입력해주세요.",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    String keyword = editText.getText().toString();
                    Log.e("test",keyword);
                    Toast.makeText(SearchView.this, keyword, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}