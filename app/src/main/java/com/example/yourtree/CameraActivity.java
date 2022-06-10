package com.example.yourtree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCamera, down, image_memo;
    ImageView imageView;
    String imageFilePath;
    Bitmap setbitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // 디자인 정의
        btnCamera = (Button) findViewById(R.id.btnPhoto);
        imageView = (ImageView) findViewById(R.id.ivCapture);
        down = (Button) findViewById(R.id.btnSave);
        btnCamera.setOnClickListener(this);
        image_memo = (Button) findViewById(R.id.image_memo);
    }

    Uri photoUri;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 카메라촬영 클릭 이벤트
            case R.id.btnPhoto:
                // 카메라 기능을 Intent
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 사진파일 변수 선언 및 경로세팅
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }

                // 사진을 저장하고 이미지뷰에 출력
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                    startActivityForResult(i, 0);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영을 하면 이미지뷰에 사진 삽입
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // 이미지파일을 bitmap 변수에 초기화
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            }
            catch(IOException e){
                e.printStackTrace();
            }

            // 이미지를 회전각도를 구한다
            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            // 이미지를 출력
            setbitmap = rotate(bitmap, exifDegree);
            imageView.setImageBitmap(setbitmap);

        }
    }

    // ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        imageFilePath = image.getAbsolutePath();

        return image;
    }

    // 사진의 돌아간 각도를 계산하는 메서드 선언
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    // 이미지를 회전시키는 메서드 선언
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 저장하기
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

    // 화면 캡쳐하기
    public File ScreenShot(View view) {
        down.setVisibility(View.INVISIBLE);

        view.setDrawingCacheEnabled(true);

        /*
        Bitmap screenBitmap = view.getDrawingCache(); //비트맵으로 변환
        //cropBitmap(screenBitmap);
        int cw = 1080; // crop width
        int ch = 1500; // crop height
        screenBitmap = Bitmap.createBitmap(screenBitmap, 0, 200, cw, ch);
         */
        String filename = "screenshot" + System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            setbitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵 > PNG파일
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    // 권한 요청
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(CameraActivity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    // 메모하기 위해 사진 전송
    public void Draw_memo(View view){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        setbitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String bitmap = Base64.encodeToString(bytes, Base64.DEFAULT);
        Intent intent1 = new Intent(CameraActivity.this, draw_memo.class);
        intent1.putExtra("bitmap", bitmap);
        startActivity(intent1);
        finish();
    }
}