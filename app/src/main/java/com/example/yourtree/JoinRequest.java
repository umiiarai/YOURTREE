package com.example.yourtree;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 서버에 회원가입 요청
public class JoinRequest extends StringRequest {
    final static private String URL = "https://thddbap.cafe24.com/UserJoin.php"; // 접속할 서버 주소
    private Map<String, String> parameters;

    public JoinRequest(String UserID, String UserPW, String UserName, String UserBirth, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // 헤당 요청을 URL에 POST(숨겨서) 방식으로
        // 파라미터값 매칭
        parameters = new HashMap<>();
        parameters.put("userID", UserID);
        parameters.put("userPassword", UserPW);
        parameters.put("userName", UserName);
        parameters.put("userBirth", UserBirth);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
