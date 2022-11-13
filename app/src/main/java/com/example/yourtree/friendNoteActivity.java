package com.example.yourtree;

import static com.android.volley.VolleyLog.TAG;
import static okhttp3.internal.Util.UTF_8;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class friendNoteActivity extends AppCompatActivity {

    private ListView noteListView;
    private NoteListAdapter NoteListAdapter;
    private List<Note> noteList;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendnote);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        noteListView = (ListView) findViewById(R.id.noteListView);
        noteList = new ArrayList<Note>(); // 초기화
        // 노트 목록 데이터베이스 접근해 사용 실행
        new BackgroundTask().execute();
        NoteListAdapter = new NoteListAdapter(getApplicationContext(), noteList); // 어뎁터에 넣기
        noteListView.setAdapter(NoteListAdapter); // 어덥터에 들어있는 내용이 각각 뷰의 형태로 보여짐

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteActicity.class);
                intent.putExtra("notetitle", noteList.get(position).getNoteTitle());
                intent.putExtra("notewriter", noteList.get(position).getNoteWriter());
                intent.putExtra("notedate", noteList.get(position).getNoteDate());
                intent.putExtra("notecontent", noteList.get(position).getNoteContent());
                intent.putExtra("studytime", noteList.get(position).getStudytime());
                startActivity(intent);
            }
        });
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
                String selectData = "userID=" + userID;
                URL url = new URL(target);
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                } // 연결 상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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
                Integer noteNum;
                String noteTitle, noteContent, noteName, noteDate, studytime;
                // 해당 내용 가져오기
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noteNum = object.getInt("noteNum");
                    noteTitle = object.getString("noteTitle");
                    noteContent = object.getString("noteContent");
                    noteName = object.getString("noteName");
                    noteDate = object.getString("noteDate");
                    studytime = object.getString("studytime");

                    // 하나의 노트에 대한 객체 생성
                    Note note = new Note(noteNum, noteTitle, noteContent, noteName, noteDate, studytime);
                    noteList.add(note); // 모든 노트가 noteList에 추가
                    NoteListAdapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}