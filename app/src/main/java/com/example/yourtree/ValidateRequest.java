package com.example.yourtree;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
// 회원가입 가능 여부 확인 요청
public class ValidateRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public ValidateRequest(String UserID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // URL에P POST 방식으로
        // 파라미터값 매칭
        parameters = new HashMap<>();
        parameters.put("UserID", UserID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
