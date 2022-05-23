package com.example.yourtree;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
// 회원가입 요청
public class RegisterRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public RegisterRequest(String UserID, String UserPW, String UserName, String Userage, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // URL에P POST 방식으로
        // 파라미터값 매칭
        parameters = new HashMap<>();
        parameters.put("UserID", UserID);
        parameters.put("UserPW", UserPW);
        parameters.put("UserName", UserName);
        parameters.put("Userage", Userage);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}
