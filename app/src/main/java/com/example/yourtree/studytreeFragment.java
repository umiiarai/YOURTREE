package com.example.yourtree;

import static com.android.volley.VolleyLog.TAG;
import static okhttp3.internal.Util.UTF_8;

import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link studytreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class studytreeFragment extends Fragment {

    private String userID;
    int totalstudytime = 0;
    NoteListAdapter NoteListAdapter;
    List<Note> noteList;
    int[] studytimelist;

    ImageView treeView;
    TextView treename;
    TextView treetalk;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public studytreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studytreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static studytreeFragment newInstance(String param1, String param2) {
        studytreeFragment fragment = new studytreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studytree, container, false);

        userID = MainActivity.userID;
        treeView = (ImageView) v.findViewById(R.id.main_tree);
        treename = (TextView) v.findViewById(R.id.treename);
        treetalk = (TextView) v.findViewById(R.id.treetalk);
        noteList = new ArrayList<Note>(); // 초기화

        new BackgroundTask().execute();
        Log.d(TAG, "최종시간 : " + totalstudytime);

        //StudyTreeSet(totalstudytime); //187200000
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void StudyTreeSet (int totalstudytime) {
        int time = (int) (totalstudytime / 1000);
        int hour = time / (60 * 60);

        if (hour >= 40) {
            treeView.setImageResource(R.drawable.tree4);
            treename.setText("어린왕자의 바오밥나무");
            treetalk.setText("세상에 당신이 모르는 내용은 없어요!");
        } else if (hour < 40 && hour >= 30) {
            treeView.setImageResource(R.drawable.tree3);
            treename.setText("뉴턴의 사과나무");
            treetalk.setText("당신은 노력하는 천재입니다!");
        }
        else if (hour < 30 && hour >= 20) {
            treeView.setImageResource(R.drawable.tree2);
            treename.setText("잭의 콩나무");
            treetalk.setText("공부를 즐기는 사람이 되십시오!");
        }
        else if (hour < 20 && hour >= 10) {
            treeView.setImageResource(R.drawable.tree1);
            treename.setText("옆집 김씨 아져씨 감나무");
            treetalk.setText("ㅇ,,이제 조금 알 거 같은데?");
        }
        else if (hour < 10) {
            treeView.setImageResource(R.drawable.peach);
            treename.setText("당신의 소중한 씨앗");
            treetalk.setText("영차 영차 화이팅!");
        }
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
                Log.d("DB","연결 :" + target);

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
                    Log.d("DB","추가 :" + noteList);
                    count++;
                }
                if(noteList.size()!=0) {
                    studytimelist = new int[noteList.size()];

                    for(int i = 0; i < noteList.size() ; i++) {
                        studytimelist[i] = Integer.parseInt(noteList.get(i).getStudytime());
                    }
                    for (int j = 0 ; j < studytimelist.length ; j++) {
                        totalstudytime = totalstudytime + studytimelist[j];
                        Log.d(TAG, "DB단: " + totalstudytime);
                    }
                    StudyTreeSet(totalstudytime);

                    //Log.d(TAG, "리스트 ㄹ제발 : " + noteList.get(0).getStudytime());
                    //totalstudytime = Integer.parseInt(noteList.get(0).getStudytime());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}