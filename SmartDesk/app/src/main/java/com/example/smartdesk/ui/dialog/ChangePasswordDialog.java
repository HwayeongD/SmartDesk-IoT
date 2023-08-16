package com.example.smartdesk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.smartdesk.R;

public class ChangePasswordDialog extends Dialog {

    private static ChangePasswordDialog changePasswordDialog;

    private ChangePasswordDialog(@NonNull Context context) {
        super(context,  R.style.Theme_Login);
    }

    public static ChangePasswordDialog getInstance(Context context) {
        changePasswordDialog = new ChangePasswordDialog(context);

        return changePasswordDialog;
    }

    //체크 팝업 커스텀
    public void showChangePasswordDialog() {
        //참조할 다이얼로그 화면을 연결한다.
        changePasswordDialog.setContentView(R.layout.change_pw_dialog);

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView closeImageView = changePasswordDialog.findViewById(R.id.close_change_pw);

        closeImageView.setOnClickListener(clickCancel);
        changePasswordDialog.show();
    }


    //취소버튼을 눌렀을때 일반적인 클릭리스너
    View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changePasswordDialog.dismiss();
        }
    };


}
