package com.example.yourtree;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class community extends AppCompatActivity {  // 수정
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        textView = findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://acee-220-66-87-245.jp.ngrok.io") //본인의 디장고 서버 url을 적는다.
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        RestApi RestApi = retrofit.create(RestApi.class);

        Call<List<RestApi.Post>> call = RestApi.getPosts();

        call.enqueue(new Callback<List<RestApi.Post>>() {
            @Override
            public void onResponse(Call<List<RestApi.Post>> call, Response<List<RestApi.Post>> response) {

                if (!response.isSuccessful())
                {
                    textView.setText("Code:" + response.code());
                    return;
                }

                List<RestApi.Post> posts = response.body();

                for ( RestApi.Post post : posts) {
                    String content ="";
                    content += "ID : " + post.getId() + "\n";
                    content += "test : " + post.getTest() + "\n\n";

                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<RestApi.Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

}