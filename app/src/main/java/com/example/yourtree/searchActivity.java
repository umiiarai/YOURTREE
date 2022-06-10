package com.example.yourtree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    private String SearchID = "";

    private ListView searchListView;
    private searchAdapter searchAdapter;
    private List<search> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText searchID = (EditText) findViewById(R.id.et_searchid);
        SearchID = searchID.getText().toString();
        //

        searchListView = (ListView) findViewById(R.id.searchListView);
        searchList = new ArrayList<search>();
        searchAdapter = new searchAdapter(getApplicationContext(), searchList, this);
        searchListView.setAdapter(searchAdapter);

        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        @Override
        protected void onPreExecute() {
            try {
                target = "https://thddbap.cafe24.com/SearchList.php?searchID=" + URLEncoder.encode(SearchID, "UTF-8");// 해당 웹서버 URL에 접속
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(searchActivity.this);
                dialog = builder.setMessage(result).setPositiveButton("확인", null).create();
                dialog.show();

                searchList.clear(); // 깨끗한 화면
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String SfriendID;
                String SfriendName;
                String SfriendSTime;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    SfriendID = object.getString("userID");
                    SfriendName = object.getString("userName");
                    SfriendSTime = object.getString("userBirth");

                    search search = new search(SfriendID, SfriendName, SfriendSTime);
                    searchList.add(search);
                    count++;
                }

                if(count == 0) {
                    AlertDialog dialogs;
                    AlertDialog.Builder builders = new AlertDialog.Builder(searchActivity.this);
                    dialogs = builders.setMessage("조회된 아이디가 존재하지 않습니다.").setPositiveButton("확인", null).create();
                    dialogs.show();
                }
                searchAdapter.notifyDataSetChanged();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}