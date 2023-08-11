package com.example.smartdesk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private TextInputEditText editId;
    private TextInputEditText editPwd;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        editId = findViewById(R.id.edit_id);
        editPwd = findViewById(R.id.edit_pwd);
        btn_login = findViewById(R.id.btn_login);

        // 로컬 데이터 확인 및 자동 로그인 처리 (SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        Long empId = sharedPreferences.getLong("empId", 0);
        if(empId != 0) {
            Employee.getInstance().setEmpId(empId);
            goToMainActivity();
        }

        // 입력창에 값을 입력하면 ERROR 제거하기
        // addTextChangedListener : 문자열 변경 감지 이벤트 리스너
        editId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout1.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputLayout2.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //btn_login Button의 Click이벤트
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력값 가져오기
                String inputId = editId.getText().toString();
                String inputPwd = editPwd.getText().toString();

                // TODO: 각 if문들은 에러 띄우고 아무것도 없다. 따라서, 각 분기문 내부에 return 처리해주기
                // TODO: 그래야 아래에서 정상인 상황에 굳이 모든 케이스에 대한 if 문으로 묶어줄 필요가 없다.
                // 입력창이 비어있으면 Error 띄워주기
                if (TextUtils.isEmpty(inputId)) {
                    textInputLayout1.setError("아이디를 입력해주세요.");
                }

                if (TextUtils.isEmpty(inputPwd)) {
                    textInputLayout2.setError("비밀번호를 입력해주세요.");
                }

                // 모두 입력했으면 main 페이지로 이동하기
                else {
                    // TODO: Retrofit 통신 테스트용 - 수정 예정
                    Employee.getInstance().setEmpId(Long.parseLong(inputId));
                    Employee.getInstance().setPassword(inputPwd);

                    Retrofit retrofit = RetrofitClient.getClient();
                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                    retrofitAPI.getLoginAccess(Employee.getInstance()).enqueue(new Callback<Employee>() {
                        @Override
                        public void onResponse(Call<Employee> call, Response<Employee> response) {

                            try {
                                if(response.isSuccessful()) {
                                    Employee data = response.body();
                                    Log.d(TAG, "성공");
                                    Log.d(TAG, data.getNickname());

                                    // 자동로그인을 위한 사원ID 로컬 저장 (SharedPreferences)
                                    SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLoginEditor = sharedPreferences.edit();
                                    autoLoginEditor.putBoolean("auto", true);
                                    autoLoginEditor.putLong("empId", Employee.getInstance().getEmpId());
                                    autoLoginEditor.apply();
                                    // 서버에서의 응답이 정상인 경우, 로그인이 성공한 경우 로그인 창 끝나고 다음 페이지로 넘어가기
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    finish();


                                    goToMainActivity();
                                }
                                else {
                                    Employee data = response.body();
                                    Log.d(TAG, "실패인가,,");
                                    Log.d(TAG, data.getResultCode());

                                }
                            } catch (Exception e) {
                                Log.e(TAG, "왜 실패인가,,: " + e.getMessage());
                                textInputLayout1.setError("유효한 아이디를 입력해주세요.");
                                textInputLayout2.setError("유효한 비밀번호를 입력해주세요.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {
                            Log.d(TAG, "실패");

                            t.printStackTrace();

                            Toast.makeText(LoginActivity.this,"네트워크 오류가 발생했습니다. 인터넷 연결을 확인하고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void goToMainActivity() {
        // 서버에서의 응답이 정상인 경우, 로그인이 성공한 경우 로그인 창 끝나고 다음 페이지로 넘어가기
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override       // Edittext 외부 터치시 키보드 내리기
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager)  getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}

