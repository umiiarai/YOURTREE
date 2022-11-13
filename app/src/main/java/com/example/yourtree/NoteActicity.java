package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoteActicity extends AppCompatActivity {

    String IMGurlz;
    Bitmap bitmap;
    String noteTitle;
    String noteWriter;
    String noteContent;
    String noteDate;
    String studytime;
    long setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_acticity);

        TextView notetitle = (TextView)findViewById(R.id.notetitle);
        TextView notecontent = (TextView)findViewById(R.id.notecontent);
        TextView notewriter = (TextView) findViewById(R.id.notewriter);
        TextView notedate = (TextView) findViewById(R.id.notedate);
        TextView studyTime = (TextView) findViewById(R.id.studytime);
        ImageView notecontent_img = (ImageView) findViewById(R.id.noteIMG);
        //xml에 studytime 란 추가하기

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        noteTitle = intent.getStringExtra("notetitle");
        noteWriter = intent.getStringExtra("notewriter");
        noteContent = intent.getStringExtra("notecontent");
        noteDate = intent.getStringExtra("notedate");
        studytime = intent.getStringExtra("studytime");
        setTime = Long.parseLong(studytime);// 숫자로 바꾸기

        int times = (int) (setTime / 1000);
        int hour = times / (60 * 60);
        int min = times % (60 * 60) / 60;
        int sec = times % 60;

        notetitle.setText(noteTitle);
        notewriter.setText(noteWriter);
        notedate.setText(noteDate);
        notecontent.setText(noteContent);
        studyTime.setText(hour + ":" + min + ":" + sec);

        //noteimage.setImageResource(intent.getIntExtra("img", 0));

        Thread uThread = new Thread() {
            @Override
            public void run(){
                String TAG = "ThreadTest";
                try{
                    //서버에 올려둔 이미지 URL
                    URL url = new URL(noteContent);
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
            notecontent_img.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}