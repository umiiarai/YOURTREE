package com.example.yourtree;

import static okhttp3.internal.Util.UTF_8;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mypage extends AppCompatActivity {

    private static final String URL_UPLOAD = "https://thddbap.cafe24.com/uploadtest.php";

    String userID = MainActivity.userID;
    Uri uri; //uri 이미지
    ImageView IMGs;
    ContentResolver cr;
    private final String TAG = this.getClass().getSimpleName();
    private Bitmap bitmap;
    private String jsonString;
    ArrayList<Me> MeArrayList;

    private TextView tvuserid;
    private TextView tvuserpw;
    private TextView tvusername;
    private TextView tvuserbirth;


    //프로필 사진 요청코드
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 이미지 제외하고 사용자 정보 매칭하기
        tvusername = (TextView) findViewById(R.id.username);
        tvuserid = (TextView) findViewById(R.id.userid);
        tvuserpw = (TextView) findViewById(R.id.userpw);
        tvuserbirth = (TextView) findViewById(R.id.userbirth);

        new BackgroundTask().execute();

        // 이미지 제외하고 사용자 정보 매칭하기 여기까지

        IMGs = findViewById(R.id.IMG);

        // 사진 등록
        final Button btn_upload= (Button) findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                launcher.launch(intent);
            }

        });

        // 사진 보기
        final Button btn_download= (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download(); // 사진 불러오기
            }
        });
    }

    // 사진 갤러리에서 불러오는 launcher 코드
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Log.e(TAG, "result : " + result);
                        Intent intent = result.getData();
                        Log.e(TAG, "intent : " + intent);
                        uri = intent.getData();
                        Log.e(TAG, "uri : " + uri);
//                        imageview.setImageURI(uri);
                        //Glide.with(mypage.this).load(uri).into(IMGs);

                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            IMGs.setImageBitmap(bitmap);
                        } catch (IOException e) {e.printStackTrace();};
                        uploadPicture(userID, getStringImage(bitmap));

                    } else if (result.getResultCode() == RESULT_CANCELED) {} // 취소시 호출할 행동 쓰기
                }
            });


    private void uploadPicture(final String id, final String photo) {
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
                        Toast.makeText(mypage.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(mypage.this, "Try Again! error : " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(mypage.this, "Error : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("userID", id);
                params.put("IMG", photo);

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

    private void Download() { // 사진 불러오기
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        String IMG = jsonObject.optString("IMG", "없어");
                        Toast.makeText(mypage.this, IMG, Toast.LENGTH_SHORT).show();
                        Glide.with(mypage.this).load(IMG).into(IMGs);
                        //json데이터로 받은 image를 이미지뷰에 넣거나,
                        //글라이드 라이브러리를 통해 사용하면 된다.
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Download download = new Download(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(mypage.this);
        queue.add(download);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        @Override
        protected void onPreExecute() {
            target = "https://thddbap.cafe24.com/me.php";
        }
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(Void... voids) {    // execute의 매개변수를 받아와서 사용
            try {
                String selectData = "userID=" + userID;
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                // 어플에서 데이터 전송
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectData.getBytes(UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                //. 넘어오는 결과값 그대로 저장
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                } // 연결 상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();        // 받아온 JSON 의 공백을 제거
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String fromdoInBackgroundString) { // doInBackgroundString에서 return한 값을 받음
            super.onPostExecute(fromdoInBackgroundString);

            if(fromdoInBackgroundString == null)
                tvuserid.setText("error");
            else {
                jsonString = fromdoInBackgroundString;
                MeArrayList = doParse();
                if(MeArrayList.size()!=0) {
                    Log.d(TAG, MeArrayList.get(0).getUserID());
                    tvusername.setText(MeArrayList.get(0).getUserName());
                    tvuserid.setText(MeArrayList.get(0).getUserID());
                    tvuserpw.setText(MeArrayList.get(0).getUserPassword());
                    tvuserbirth.setText(MeArrayList.get(0).getUserBirth());
                }
            }
        }

        private ArrayList<Me> doParse() {
            ArrayList<Me> tmpMeArray = new ArrayList<Me>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for(int i=0;i<jsonArray.length();i++) {
                    Me tmpMe = new Me();
                    JSONObject item = jsonArray.getJSONObject(i);
                    tmpMe.setUserID(item.getString("userID"));
                    tmpMe.setUserPassword(item.getString("userPassword"));
                    tmpMe.setUserName(item.getString("userName"));
                    tmpMe.setUserBirth(item.getString("userBirth"));

                    tmpMeArray.add(tmpMe);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tmpMeArray;
        } // JSON을 가공하여 ArrayList에 넣음
    }

}