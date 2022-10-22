package com.example.yourtree;

import static okhttp3.internal.Util.UTF_8;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    public static String IMGurl; //=https://thddbap.cafe24.com/profile/umi.jpeg
    Bitmap bitmap;

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

        new BackgroundTask().execute();

        Thread uThread = new Thread() {
            @Override
            public void run(){
                try{
                    //서버에 올려둔 이미지 URL
                    URL url = new URL(IMGurl);
                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    /* URLConnection 생성자가 protected로 선언되어 있으므로
                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/
                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행
        try{
            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join();
            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            profile_img.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        @Override
        protected void onPreExecute() {
            target = "https://thddbap.cafe24.com/download.php";// 해당 웹서버 URL에 접속
        }
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String selectData = "userID=" + MainActivity.userID;
                URL url = new URL(target);
                Log.d(TAG, "target, userID : " + target + "," + selectData);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                // 어플에서 데이터 전송
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectData.getBytes(UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                //. 넘어오는 결과값 그대로 저장
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                } // 연결 상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();        // 받아온 JSON 의 공백을 제거
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
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
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    MainActivity.IMGurl = object.getString("IMG");
                    count++;
                }
                Log.d(TAG, "IMGurl = " + MainActivity.IMGurl);
            }catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}