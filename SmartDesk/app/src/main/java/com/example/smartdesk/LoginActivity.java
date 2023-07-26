package com.example.smartdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

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

                // 입력창이 비어있으면 Error 띄워주기
                if (TextUtils.isEmpty(inputId)) {
                    textInputLayout1.setError("아이디를 입력해주세요.");
                } else if (!TextUtils.isEmpty(inputId)) {
                    textInputLayout1.setError(null); // Clear the error message if not empty
                }
                if (TextUtils.isEmpty(inputPwd)) {
                    textInputLayout2.setError("비밀번호를 입력해주세요.");
                } else if (!TextUtils.isEmpty(inputPwd)) {
                    textInputLayout2.setError(null); // Clear the error message if not empty
                }
                if (!TextUtils.isEmpty(inputId) && !TextUtils.isEmpty(inputPwd)) {
                    // Both fields are not empty, proceed with login
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        });
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

