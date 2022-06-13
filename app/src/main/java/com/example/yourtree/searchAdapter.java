package com.example.yourtree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 노트 목록
public class searchAdapter extends BaseAdapter {

    private Context context;
    private List<search> searchList;

    // 친구 중복 테스트
    //private String userID = MainActivity.userID;
    //private Friend friend = new Friend();
    //private List<Integer> friendIDList;

    public searchAdapter(Context context, List<search> searchList) {
        this.context = context;
        this.searchList = searchList;

        //friend = new Friend();
        //friendIDList = new ArrayList<Integer>();

        //new BackgroundTask().execute();
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

        // ImageView imageView=itemView.findViewById(R.id.iv_note_content);
        // int imgResource=(noteList.get(i).getImg();
        // imageView.setImageResource(imgResource);

        // 태그 붙여주기
        v.setTag(searchList.get(i).getUserName());
        return v;
    }

    //public boolean alredadyIn(List<Integer> friendIDList, int item) {
    //    for(int i = 0; i < friendIDList.size(); i++ ) {
    //        if(friendIDList)
     //   }
    //}
}
