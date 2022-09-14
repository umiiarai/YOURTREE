package com.example.yourtree;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //note adapter
    private ListView noteListView;
    private NoteListAdapter NoteListAdapter;
    private List<Note> noteList;

    public static String userID;
    public static ImageButton profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 화면 세로 고정

        userID = getIntent().getStringExtra("userID");




        // note adapter 추가
        noteListView = (ListView) findViewById(R.id.noteListView);
        noteList = new ArrayList<Note>(); // 초기화
        NoteListAdapter = new NoteListAdapter(getApplicationContext(), noteList); // 어뎁터에 넣기
        noteListView.setAdapter(NoteListAdapter); // 어덥터에 들어있는 내용이 각각 뷰의 형태로 보여짐

        profile_img = (ImageButton) findViewById(R.id.profile_img);
        final ImageButton btn_book = (ImageButton) findViewById(R.id.btn_book);
        final ImageButton btn_friends = (ImageButton) findViewById(R.id.btn_friends);
        final ImageButton btn_folder = (ImageButton) findViewById(R.id.btn_folder);
        final ImageButton btn_graph = (ImageButton) findViewById(R.id.btn_graph);
        final ImageButton btn_tree = (ImageButton) findViewById(R.id.btn_tree);
        final LinearLayout mainpage = (LinearLayout) findViewById(R.id.mainpage);

        // 프로필 버튼 눌렀을 때
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), mypage.class);
                startActivity(intent);
            }
        });

        // 전공책 버튼 눌렀을 때
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐


                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new bookFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

        // 친구 버튼 눌렀을 때
        btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐
                // 버튼 변화


                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new userFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

        // 폴더 버튼 눌렀을 때
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐


                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new notelistFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

        // 그래프 버튼 눌렀을 때
        btn_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐


                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new graphFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

        // 공부나무 버튼 눌렀을 때
        btn_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐


                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new studytreeFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

    }
    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로'버튼을 한 번 더 눌러 종료함", Toast.LENGTH_SHORT);
        lastTimeBackPressed = System.currentTimeMillis();
    }
}