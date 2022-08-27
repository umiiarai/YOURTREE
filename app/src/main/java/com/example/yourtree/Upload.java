package com.example.yourtree;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Upload extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="https://thddbap.cafe24.com/upload.php";
    private Map<String,String> map;

    public Upload(String userID, Uri IMG, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("userID", userID);
        map.put("IMG", IMG +"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
