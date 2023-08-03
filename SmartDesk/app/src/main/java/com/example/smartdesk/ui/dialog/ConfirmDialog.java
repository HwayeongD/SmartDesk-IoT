package com.example.smartdesk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartdesk.R;

public class ConfirmDialog extends Dialog {

    private static ConfirmDialog customDialog;

    private ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public static ConfirmDialog getInstance(Context context) {
        customDialog = new ConfirmDialog(context);

        return customDialog;
    }

    //체크 팝업 커스텀
    public void showCheckDialog() {
        //참조할 다이얼로그 화면을 연결한다.
        customDialog.setContentView(R.layout.check_dialog);

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView warningImageView = customDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.check_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.check_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("책상 높이 확인");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("즐겨찾기 책상 높이로 \n변경하시겠습니까?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.check_dialog_content);
                textView.setText("확인 버튼을 눌렀습니다. :)");
            }
        });
        Button no_btn = customDialog.findViewById(R.id.check_nobtn);
        no_btn.setOnClickListener(clickCancel);
        customDialog.show();
    }
    // 확인 팝업 커스텀
    public void showConfirmDialog() {

        customDialog.setContentView(R.layout.confirm_dialog);


        ImageView warningImageView = customDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.confirm_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.confirm_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("좌석 자동 예약");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("출근하여 앱 실행 시 \n최근 좌석으로 자동 예약");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }

    //취소버튼을 눌렀을때 일반적인 클릭리스너
    View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customDialog.dismiss();
        }
    };

}
