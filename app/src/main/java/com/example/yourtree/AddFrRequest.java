package com.example.yourtree;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 서버에 회원가입 요청
public class AddFrRequest extends StringRequest {
    final static private String URL = "https://thddbap.cafe24.com/FriendAdd.php"; // 접속할 서버 주소
    private Map<String, String> parameters;

    public AddFrRequest(String userID, String friendID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // 헤당 요청을 URL에 POST(숨겨서) 방식으로
        // 파라미터값 매칭
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("friendID", friendID);
        // 이름과 공부시간 추가?
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
