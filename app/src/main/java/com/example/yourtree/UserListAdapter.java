package com.example.yourtree;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// 친구 목록
public class UserListAdapter extends BaseAdapter {

    private static final String TAG = "확인";
    private Context context;
    private List<User> userList;
    Bitmap bitmap;
    String uimageurl;

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
        ImageView uimage = (ImageView) v.findViewById(R.id.uimage);

        tv_uid.setText(userList.get(i).getUserID());
        tv_uname.setText(userList.get(i).getUserName());
        tv_ubirth.setText(userList.get(i).getUserBirth());
        uimageurl = userList.get(i).getUserIMG();

        Thread uThread = new Thread() {
            @Override
            public void run(){
                String TAG = "ThreadTest";
                try{
                    //서버에 올려둔 이미지 URL
                    URL url = new URL(uimageurl);
                    Log.d(TAG, "IMGurl onthread : " + url);
                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    /* URLConnection 생성자가 protected로 선언되어 있으므로
                     개발자가 직접 HttpURLConnection 객체 생성 불가 */
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    /* openConnection()메서드가 리턴하는 urlConnection 객체는
                    HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/
                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };

        uThread.start(); // 작업 Thread 실행
        try{
            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join();
            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            uimage.setImageBitmap(bitmap);
            //picture.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        // 태그 붙여주기
        v.setTag(userList.get(i).getUserID());
        return v;
    }
}
