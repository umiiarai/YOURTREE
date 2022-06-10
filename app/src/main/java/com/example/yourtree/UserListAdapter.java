package com.example.yourtree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// 노트 목록
public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.user, null);
        TextView tv_uid = (TextView) v.findViewById(R.id.tv_uid);
        TextView tv_upw = (TextView) v.findViewById(R.id.tv_upw);
        TextView tv_uname = (TextView) v.findViewById(R.id.tv_uname);
        TextView tv_ubirth = (TextView) v.findViewById(R.id.tv_ubirth);

        tv_uid.setText(userList.get(i).getUserID());
        tv_upw.setText(userList.get(i).getUserPassword());
        tv_uname.setText(userList.get(i).getUserName());
        tv_ubirth.setText(userList.get(i).getUserBirth());

        // ImageView imageView=itemView.findViewById(R.id.iv_note_content);
        // int imgResource=(noteList.get(i).getImg();
        // imageView.setImageResource(imgResource);

        // 태그 붙여주기
        v.setTag(userList.get(i).getUserID());
        return v;
    }


}
