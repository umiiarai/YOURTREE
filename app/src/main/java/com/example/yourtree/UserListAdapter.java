package com.example.yourtree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// 친구 목록
public class UserListAdapter extends BaseAdapter {

    private static final String TAG = "확인";
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

    public User getItems(int i) {return userList.get(i);}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.user, null);
        TextView tv_uid = (TextView) v.findViewById(R.id.tv_uid);
        TextView tv_uname = (TextView) v.findViewById(R.id.tv_uname);
        TextView tv_ubirth = (TextView) v.findViewById(R.id.tv_ubirth);

        tv_uid.setText(userList.get(i).getUserID());
        tv_uname.setText(userList.get(i).getUserName());
        tv_ubirth.setText(userList.get(i).getUserBirth());

        // ImageView imageView=itemView.findViewById(R.id.iv_note_content);
        // int imgResource=(noteList.get(i).getImg();
        // imageView.setImageResource(imgResource);
        /*
        v.setTag(i);
        Log.d(TAG, "클릭1");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // 111번 라인에서 저장한 tag(position)을 꺼내온다.
                int position = (Integer) v.getTag();

                // 리스트에서 position에 맞는 데이터를 받아온다.
                User user = getItems(position);

                Bundle extras = new Bundle();
                extras.putString("userID", user.getUserID());

                // 인텐트를 생성한다.
                // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 ItemClickExampleNextActivity 를 지정한다.
                Intent intent = new Intent(v.getContext(), friendNoteActivity.class);
                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // 액티비티를 생성한다.
                v.getContext().startActivity(intent);
            }
        });
        Log.d(TAG, "클릭2");*/
        // 태그 붙여주기
        v.setTag(userList.get(i).getUserID());
        return v;
    }
}
