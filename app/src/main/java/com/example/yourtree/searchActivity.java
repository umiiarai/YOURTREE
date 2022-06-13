package com.example.yourtree;

import static com.android.volley.VolleyLog.TAG;

import static okhttp3.internal.Util.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    private EditText searchID;
    private ListView searchListView;
    private searchAdapter searchAdapter;
    private List<search> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchID = (EditText) findViewById(R.id.et_searchid);
        searchListView = (ListView) findViewById(R.id.searchListView);
        searchList = new ArrayList<search>();
        searchAdapter = new searchAdapter(getApplicationContext(), searchList, searchActivity.this);
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
                target = "https://thddbap.cafe24.com/SearchList.php"; // 해당 웹서버 URL에 접속
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String selectData = "searchID=" + searchID.getText().toString();
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
        // doInBackgroundString에서 return한 값을 받음
        public void onPostExecute(String result) {
            try {
                searchList.clear(); // 깨끗한 화면
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                String userID, userPassword, userName, userBirth;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count); // 현재 배열의 원소값 가져오기
                    userID = object.optString("userID", "text on no value");
                    userPassword = object.optString("userPassword", "text on no value");
                    userName = object.optString("userName", "text on no value");
                    userBirth = object.optString("userBirth", "text on no value");

                    search search = new search(userID, userPassword, userName, userBirth);
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