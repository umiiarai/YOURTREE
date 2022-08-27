package com.example.yourtree;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Download extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="https://thddbap.cafe24.com/download.php";
    private Map<String,String> map;

    public Download(String userID, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
