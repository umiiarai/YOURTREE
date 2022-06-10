package com.example.yourtree;

import static com.example.yourtree.MainActivity.userID;

import android.content.Context;
import android.text.AlteredCharSequence;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class FriendListAdapter extends BaseAdapter {
    private Context context;
    private List<Friend> friendList;

    public FriendListAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.friend, null);
        TextView tv_fid = (TextView) v.findViewById(R.id.tv_fid);
        TextView tv_fpw = (TextView) v.findViewById(R.id.tv_fpw);
        TextView tv_fname = (TextView) v.findViewById(R.id.tv_fname);
        TextView tv_fSTime = (TextView) v.findViewById(R.id.tv_fSTime);

        // 프로필 매칭 시켜야 됨
        tv_fid.setText(friendList.get(i).getUserID());
        tv_fpw.setText(friendList.get(i).getUserPassword());
        tv_fname.setText(friendList.get(i).getUserName());
        tv_fSTime.setText(friendList.get(i).getUserBirth());

        // 태그 붙여주기
        v.setTag(friendList.get(i).getUserName());
        return v;
    }
}
