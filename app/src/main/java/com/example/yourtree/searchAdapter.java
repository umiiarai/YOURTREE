package com.example.yourtree;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import android.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 검색 리스트 목록
public class searchAdapter extends BaseAdapter {

    private Context context;
    private List<search> searchList;
    private Activity activity;

    public searchAdapter(Context context, List<search> searchList, Activity activity) {
        this.context = context;
        this.searchList = searchList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int i) {
        return searchList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.search, null);
        TextView tv_fid = (TextView) v.findViewById(R.id.tv_fid);
        TextView tv_fname = (TextView) v.findViewById(R.id.tv_fname);
        TextView tv_fSTime = (TextView) v.findViewById(R.id.tv_fSTime);

        tv_fid.setText(searchList.get(i).getUserID());
        tv_fname.setText(searchList.get(i).getUserName());
        tv_fSTime.setText(searchList.get(i).getUserBirth());

        // 태그 붙여주기
        v.setTag(searchList.get(i).getUserName());

        Button btn_fadd = (Button) v.findViewById(R.id.btn_fadd);
        btn_fadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                String userID = MainActivity.userID;
                String friendID = tv_fid.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // // 해당 웹사이트에 접속한 이후에 특정한 응답을 다시 받을 수 있도록
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // 해당 과정이 정상적을 이루어졌는지 여부
                            boolean success = jsonResponse.getBoolean("success");
                            // 친구 추가 성공한 경우
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                AlertDialog dialog = builder.setMessage("친구가 추가 되었습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            }
                            // 친구 추가 실패한 경우
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                AlertDialog dialog = builder.setMessage("친구 추가에 실패했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        }
                        // 오류가 발생했을 경우
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 실직적으로 접속할 수 있도록 객체 생성, 중복확인을 했을 경우 중복확인 요청을 보냄
                AddFrRequest addFrRequest = new AddFrRequest(userID, friendID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(activity);
                queue.add(addFrRequest);
            }
        });
        return v;
    }
}
