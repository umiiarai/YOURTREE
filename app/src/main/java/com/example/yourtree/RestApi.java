package com.example.yourtree;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {  // 수정

    @GET("tests")
    Call<List<Post>> getPosts();

    public class Post {
        private int id;

        private String test;

        public int getId() {
            return id;
        }

        public String getTest() {
            return test;
        }
    }
}