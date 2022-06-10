package com.example.yourtree;

import android.app.Activity;
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

        final ImageButton btn_book = (ImageButton) findViewById(R.id.btn_book);
        final ImageButton btn_friends = (ImageButton) findViewById(R.id.btn_friends);
        final ImageButton btn_folder = (ImageButton) findViewById(R.id.btn_folder);
        final ImageButton btn_graph = (ImageButton) findViewById(R.id.btn_graph);
        final ImageButton btn_tree = (ImageButton) findViewById(R.id.btn_tree);
        final LinearLayout mainpage = (LinearLayout) findViewById(R.id.mainpage);

        // 전공책 버튼 눌렀을 때
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainpage.setVisibility(View.GONE); // 메인 화면 안보여짐
                // 버튼 변화
                btn_book.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark)); // 버튼을 어둡게 만들어줌
                btn_friends.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary)); // 버튼을 발게
                btn_folder.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_graph.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_tree.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

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
                btn_book.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_friends.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                btn_folder.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_graph.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_tree.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

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
                // 버튼 변화
                btn_book.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_friends.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_folder.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                btn_graph.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_tree.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

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
                // 버튼 변화
                btn_book.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_friends.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_folder.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_graph.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                btn_tree.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));

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
                // 버튼 변화
                btn_book.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_friends.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_folder.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_graph.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                btn_tree.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

                // 화면 전환
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new studytreeFragment()); // 프레그먼트 부분을 대체
                fragmentTransaction.commit();
            }
        });

        // 노트 목록 데이터베이스 접근해 사용 실행
        new BackgroundTask().execute();
    }

    // 서버와 노트 연결
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        @Override
        protected void onPreExecute() {
            target = "https://thddbap.cafe24.com/NoteList.php";// 해당 웹서버 URL에 접속
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream(); //. 넘어오는 결과값 그대로 저장
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // 해당 inputstream에 내용을 버퍼에 담아서 읽어낼 수 있게 만듦
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                // 받아온 값 한 줄씩 익으면서 temp에 저장하기
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close(); // 끝나고 닫아주기
                inputStream.close();
                httpURLConnection.disconnect(); // 연결 끊어주기
                return stringBuilder.toString().trim(); // 다 들어간 문자열들 반환
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        // 헤당 결과를 해결
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);// 응답 부분 처리
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String noteContent, noteName, noteDate;
                // 해당 내용 가져오기
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noteContent = object.getString("noteContent");
                    noteName = object.getString("noteName");
                    noteDate = object.getString("noteDate");

                    // 하나의 노트에 대한 객체 생성
                    Note note = new Note(noteContent, noteName, noteDate);
                    noteList.add(note); // 모든 노트가 noteList에 추가
                    NoteListAdapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
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