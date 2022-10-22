package com.example.yourtree;

import static com.android.volley.VolleyLog.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class draw_memo extends AppCompatActivity {

    private static final String URL_UPLOAD = "https://thddbap.cafe24.com/noteupload.php";
    private MyPaintView myView;
    Chronometer chrono;
    ImageView imagebackview;
    TextView textView;
    EditText editText;
    LinearLayout paintLayout;
    Bitmap mbitmap;
    Button stop;
    Button start;
    public Intent intent;
    public String noteTitle; // 노트제목
    Uri screenShotUri;
    Bitmap screenBitmap;
    String noteContent;// 이미지 uri
    String noteWriter = MainActivity.userID; // 작성자 아이디
    String studytime; // 공부 시간
    long now;
    Date date;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    String noteDate;// 노트 저장 날짜
    private AlertDialog dialog; // 알림창

    int count = 0;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_memo);

        imagebackview = (ImageView) findViewById(R.id.imagebackview);
        textView = (TextView) findViewById(R.id.textview);
        editText = (EditText) findViewById(R.id.noteTitle_et);
        chrono = findViewById(R.id.chrono);
        stop = (Button) findViewById(R.id.stop);
        start = (Button) findViewById(R.id.start);
        paintLayout = findViewById(R.id.paintLayout);

        //타이머 start
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.start();
            }
        });

        //타이머 stop + 노트 저장하기 + DB로 전송하기
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chrono.stop();
                long current = SystemClock.elapsedRealtime() - chrono.getBase();
                int time = (int) (current / 1000);
                int hour = time / (60 * 60);
                int min = time % (60 * 60) / 60;
                int sec = time % 60;
                printLog(hour + "시간 " + min + "분 " + sec +  "초");
                ScreenshotButton(view);

                // 저장 데이터 가져오기
                studytime = String.valueOf(time); // 공부 시간
                now = System.currentTimeMillis();
                date = new Date(now);
                noteDate = mFormat.format(date); // 공부날짜
                noteContent = screenShotUri.toString();
                noteTitle = editText.getText().toString();
                Log.d("HI", "result2 : " + noteTitle + "//" + noteWriter + "//" + noteDate + "//" + studytime);

                if (noteTitle != null && !noteTitle.equals("")) {
                    // DB에 노트사진을 제외한 정보 저장하기
                    // 아무런 문제가 없을 경우, 연결하는 부분
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 응답을 다시 받을 수 있도록
                            Log.d("HI", "result : db연결 1");
                            try {
                                Log.d("HI", "result : db연결 2");
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success"); // 서버 통신을 알려줌
                                if (success) {
                                    Log.d("HI", "result : db연결 2");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(draw_memo.this);
                                    dialog = builder.setMessage("노트 저장 완료").setPositiveButton("확인", null).create();
                                    dialog.show();
                                    finish(); //창을 닫음
                                }
                                // 노트저장에 경우
                                else {
                                    Log.d("HI", "result : db연결 3");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(draw_memo.this);
                                    dialog = builder.setMessage("노트 저장 실패").setNegativeButton("확인", null).create();
                                    dialog.show();
                                }
                            }
                            // 오류가 발생했을 경우
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    TimeSave timesave = new TimeSave(noteTitle, noteWriter, noteDate, studytime, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(draw_memo.this);
                    queue.add(timesave);
                }
                else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(draw_memo.this);
                    ad.setIcon(R.mipmap.ic_launcher);
                    ad.setTitle("알림");
                    ad.setMessage("노트 제목을 입력하세요");

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }

                // 사진 저장하기
                updateMemoContent (noteWriter, noteTitle, getStringImage(screenBitmap));
            }
        });


        // permission 부분(접근 권한)
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // imagedown에서 사진 전달 받기
        String geturi = getIntent().getStringExtra("uri");
        if (geturi != null && !geturi.isEmpty()) {
            Uri imageUri = Uri.parse(geturi);
            imagebackview.setImageURI(imageUri);
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            Drawable drawable = new BitmapDrawable(bitmap);

            if (drawable != null) {
                paintLayout.setBackground(drawable);
            }
             */
        }

        // CameraActivity에서 사진 전달 받기
        String getbitmap = getIntent().getStringExtra("bitmap");
        if (getbitmap != null && !getbitmap.isEmpty()){
            byte[] encodeByte = Base64.decode(getbitmap, Base64.DEFAULT);
            Bitmap setbitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imagebackview.setImageBitmap(setbitmap);
        }

        setTitle("노트 필기");
        myView = new MyPaintView(this);
        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);

        //선 색깔
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.btnRed:
                                myView.mPaint.setColor(Color.RED);
                                break;
                            case R.id.btnGreen:
                                myView.mPaint.setColor(Color.GREEN);
                                break;
                            case R.id.btnBlue:
                                myView.mPaint.setColor(Color.BLUE);
                                break;
                        }
                    }
                });

        //선 두께
        Button btnTh = findViewById(R.id.btnTh);
        btnTh.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count%2==1){
                    btnTh.setText("Thin");
                    myView.mPaint.setStrokeWidth(10);
                    count++;
                } else {
                    btnTh.setText("Thick");
                    myView.mPaint.setStrokeWidth(20);
                    count++;
                }
            }
        }));

        //지우기
        ((Button)findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.mBitmap.eraseColor(Color.TRANSPARENT);
                myView.invalidate();
            }
        });
    }

    private void updateMemoContent(String noteWriterz, String noteTitlez, String noteContentz) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.e(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1"))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(draw_memo.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(draw_memo.this, "Try Again! error : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(draw_memo.this, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("noteWriter", noteWriterz);
                params.put("noteTitle", noteTitlez);
                params.put("notrContent", noteContentz);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodedImage;
    }

    //선 두께, 색깔 등 지정, 그리기 기능 실행
    private static class MyPaintView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        ImageView imagebackview;
        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(1000, 1500, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            //mCanvas.setBitmap(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
            /*
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            if(imagebackview!=null) {
                imagebackview.setImageBitmap(mBitmap);
            }
             */
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int)event.getX();
            int y = (int)event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint); //mBitmap 에 기록
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }
    }

    // 저장하기
    public void ScreenshotButton(View view) {

        View rootView = getWindow().getDecorView();  //전체화면 부분

        File screenShot = ScreenShot(rootView);
        screenShotUri = Uri.fromFile(screenShot);
        if (screenShot != null) {
            //갤러리에 추가합니다
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, screenShotUri));
        }
        //down.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    //화면 캡쳐하기
    public File ScreenShot(View view) {
        //down.setVisibility(View.INVISIBLE);

        view.setDrawingCacheEnabled(true);

        screenBitmap = view.getDrawingCache(); //비트맵으로 변환
        //cropBitmap(screenBitmap);
        int cw = 1080; // crop width
        int ch = 1390; // crop height
        screenBitmap = Bitmap.createBitmap(screenBitmap, 0, 200, cw, ch);

        String filename = "screenshot" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵 > PNG파일
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    //권한 요청
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(draw_memo activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void printLog(String msg){
        textView.append(msg);
        textView.append("\n");
    }
}