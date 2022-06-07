package com.example.yourtree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class join extends AppCompatActivity {

    private AlertDialog dialog; // 알림창
    private boolean validate = false; // 회원가입 가능 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final EditText et_id = (EditText) findViewById(R.id.et_id);
        final EditText et_password = (EditText) findViewById(R.id.et_password);
        final EditText et_name = (EditText) findViewById(R.id.et_name);
        final EditText et_birth = (EditText) findViewById(R.id.et_birth);

        // id 중복확인
        final Button btn_validate = (Button) findViewById(R.id.btn_validate);
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = et_id.getText().toString();
                // validate 체크가 되어있으면 바로 함수 종료
                if(validate) {
                    return;
                }
                // validate 체크가 되어있지 않고 아아디가 적형있지 않을 경우 알림창
                if (userID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                    dialog = builder.setMessage("아이디는 빈칸일 수 없습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //정상적인 아이디 작성일 경우 중복 체크
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // // 해당 웹사이트에 접속한 이후에 특정한 응답을 다시 받을 수 있도록
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // 해당 과정이 정상적을 이루어졌는지 여부
                            boolean success = jsonResponse.getBoolean("success");
                            // 사용가능한 아이디인 경우
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                                dialog = builder.setMessage("사용할 수 있는 아아디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                et_id.setEnabled(false); // 아이디 변경 더 이상 못함
                                validate = true; // 체크 완료
                                et_id.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                btn_validate.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            // 사용할 수 없는 아이디인 경우
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                                dialog = builder.setMessage("사용할 수 없는 아아디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        }
                        // 오류가 발생했을 경우
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 실직적으로 접속할 수 있도록 객체 생성, 중복확인을 했을 경우 중복확인 요청을 보냄
                ValidateRequest validateRequest = new ValidateRequest(userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(join.this);
                queue.add(validateRequest);
            }
        });

        // 회원가입 버튼
        Button btn_join = (Button) findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ET에 입력한 내용 그대로 가져오기
                String userID = et_id.getText().toString();
                String userPassword = et_password.getText().toString();
                String userName = et_name.getText().toString();
                String userBirth = et_birth.getText().toString();

                // 중복 체크가 되어있지 않다면
                if(!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                // 빈칸이 체크하는 부분
                if (userID.equals("") || userPassword.equals("") || userName.equals("") || userBirth.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                    dialog = builder.setMessage("빈칸 없이 입력 해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                // 아무런 문제가 없을 경우, 연결하는 부분
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 응답을 다시 받을 수 있도록
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); // 서버 통신을 알려줌
                            // 회원등록에 성공한 경우
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                                dialog = builder.setMessage("회원등록에 성공하였습니다..").setPositiveButton("확인", null).create();
                                dialog.show();
                                finish(); // 회원가입 창을 닫음
                            }
                            // 회원등록에 실패한 경우
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                                dialog = builder.setMessage("회원등록에 실패했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        }
                        // 오류가 발생했을 경우
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 회원가입 버튼을 눌렀을 때 요청을 보냄
                JoinRequest registerRequest = new JoinRequest(userID, userPassword, userName, userBirth, responseListener);
                RequestQueue queue = Volley.newRequestQueue(join.this);
                queue.add(registerRequest);
            }
        });
    }

    // 회원등록 이후 회원등록 창이 꺼지면 실행되는 부분
    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}