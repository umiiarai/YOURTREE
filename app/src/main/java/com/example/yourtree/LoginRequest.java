package com.example.yourtree;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//서버에 로그인 요청.java
public class LoginRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public LoginRequest(String UserID, String UserPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // URL에P POST 방식으로
        // 파라미터값 매칭
        parameters = new HashMap<>();
        parameters.put("UserID", UserID);
        parameters.put("UserPW", UserPW);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

