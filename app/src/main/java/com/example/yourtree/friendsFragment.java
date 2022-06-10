package com.example.yourtree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link friendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class friendsFragment extends Fragment {

    //friend adapter
    private ListView friendListView;
    private FriendListAdapter FriendListAdapter;
    private List<Friend> friendList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public friendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment friendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static friendsFragment newInstance(String param1, String param2) {
        friendsFragment fragment = new friendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // friend adapter 추가
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        friendListView = (ListView) v.findViewById(R.id.friendListView);
        friendList = new ArrayList<Friend>(); // 초기화
        // 친구 목록 데이터베이스 접근해 사용 실행
        new BackgroundTask().execute();
        FriendListAdapter = new FriendListAdapter(getActivity().getApplicationContext(), friendList); // 어뎁터에 넣기
        friendListView.setAdapter(FriendListAdapter); // 어덥터에 들어있는 내용이 각각 뷰의 형태로 보여짐

        final ImageButton add_friend = (ImageButton) v.findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), searchActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    // 서버와 노트 연결
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        @Override
        protected void onPreExecute() {
            target = "https://thddbap.cafe24.com/FriendList2.php";// 해당 웹서버 URL에 접속
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
                String userId, userPassword, userName, userBirth;
                // 해당 내용 가져오기
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    // 이미지받아오는 부분
                    // friendImage = object
                    userId = object.getString("userId");
                    userPassword = object.getString("userPassword");
                    userName = object.getString("userName");
                    userBirth = object.getString("userBirth");

                    // 하나의 노트에 대한 객체 생성
                    Friend friend = new Friend(userId, userPassword, userName, userBirth);
                    friendList.add(friend); // 모든 노트가 friend에 추가
                    FriendListAdapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}