package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class draw_memo extends AppCompatActivity {
    private MyPaintView myView;
    ImageView imagebackview;
    LinearLayout paintLayout;
    Bitmap mBitmap;
    ImageButton down;
    int count = 0;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_memo);
        //imagebackview = (ImageView) findViewById(R.id.imagebackview);

        // permission 부분(접근 권한)
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        down = findViewById(R.id.down);
        paintLayout = findViewById(R.id.paintLayout);

        String geturi = getIntent().getStringExtra("uri");
        if (geturi != null && !geturi.isEmpty()) {
            Uri imageUri = Uri.parse(geturi);

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
            Drawable drawable = new BitmapDrawable(bitmap);

            if (drawable != null) {
                paintLayout.setBackground(drawable);
            }
        }

        setTitle("간단 그림판");
        myView = new MyPaintView(this);
        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);
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

        ((Button)findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.mBitmap.eraseColor(Color.TRANSPARENT);
                myView.invalidate();
            }
        });
    }

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
            mCanvas.setBitmap(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            if(imagebackview!=null) {
                imagebackview.setImageBitmap(mBitmap);
            }
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
    public void ScreenshotButton(View view) {

        View rootView = getWindow().getDecorView();  //전체화면 부분

        File screenShot = ScreenShot(rootView);
        if (screenShot != null) {
            //갤러리에 추가합니다
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
        }
        down.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), "갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    //화면 캡쳐하기
    public File ScreenShot(View view) {
        down.setVisibility(View.INVISIBLE);

        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache(); //비트맵으로 변환
        //cropBitmap(screenBitmap);
        int cw = 1080; // crop width
        int ch = 1500; // crop height
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
}