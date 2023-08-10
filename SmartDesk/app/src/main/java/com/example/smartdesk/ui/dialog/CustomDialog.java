package com.example.smartdesk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartdesk.R;

public class CustomDialog extends Dialog {

    private static CustomDialog customDialog;

    private CustomDialog(@NonNull Context context) {
        super(context);
    }

    public static CustomDialog getInstance(Context context) {
        customDialog = new CustomDialog(context);

        // 다이어로그 테두리 둥글게 만들어주기 위한 필수 코드!!!
        // MAYBE... 기본으로 제공해주는 배경을 제거해주는 코드인 거 같다
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return customDialog;
    }

    //책상 높이 변경 체크 팝업
//    public void showCheckDialog() {
        //참조할 다이얼로그 화면을 연결한다.
//        customDialog.setContentView(R.layout.check_dialog);
//
//        //다이얼로그의 구성요소들이 동작할 코드작성
//        ImageView warningImageView = customDialog.findViewById(R.id.check_dialog_image);
//        TextView titleTextView = customDialog.findViewById(R.id.check_dialog_title);
//        TextView contentTextView = customDialog.findViewById(R.id.check_dialog_content);
//        Button yesbtn = customDialog.findViewById(R.id.check_yesbtn);
//
//        warningImageView.setImageResource(R.drawable.ic_error_48px);
//        titleTextView.setText("책상 높이 변경");
//        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
//        contentTextView.setText("현재 높이를 즐겨찾기 책상 높이로 \n변경하시겠습니까?");
//        yesbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //reqChangeDeskHeight();
//            }
//        });
//        Button no_btn = customDialog.findViewById(R.id.check_nobtn);
//        no_btn.setOnClickListener(clickCancel);
//        customDialog.show();
//    }

    // 좌석 자동 예약 팝업 커스텀
    public void setAutoSeatDialog() {
        ImageView warningImageView = customDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.confirm_dialog_content);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("예약 안내");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("최근 좌석으로 예약하시겠습니까?\n(3초 후 자동 예약됩니다)");
    }

    // 좌석 예약 불가 팝업 커스텀
    public void showNotAllowedDialog() {

        customDialog.setContentView(R.layout.confirm_dialog);


        ImageView warningImageView = customDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.confirm_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.confirm_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_do_not_disturb_on_total_silence_48px);
        titleTextView.setText("예약 불가");
        titleTextView.setTextColor(Color.parseColor("FF0000"));
        contentTextView.setText("이미 예약된 좌석입니다");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }

    //좌석 변경 체크 팝업
    public void showChangeDeskDialog() {
        //참조할 다이얼로그 화면을 연결한다.
        customDialog.setContentView(R.layout.check_dialog);

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView warningImageView = customDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.check_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.check_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("예약 변경");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("이미 예약된 좌석이 있습니다\n좌석을 변경하시겠습니까?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reqChangeDesk();
            }
        });
        Button no_btn = customDialog.findViewById(R.id.check_nobtn);
        no_btn.setOnClickListener(clickCancel);
        customDialog.show();
    }

    // 예약 변경 확인 팝업
    public void showChangeConfirmDialog() {

        customDialog.setContentView(R.layout.confirm_dialog);


        ImageView warningImageView = customDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.confirm_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.confirm_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_check_circle_48px);
        titleTextView.setText("예약 확인");
        titleTextView.setTextColor(Color.parseColor("2FC600"));
        contentTextView.setText("예약이 변경되었습니다.");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }


    // 예약 확인 팝업
    public void showAllowConfirmDialog() {

        customDialog.setContentView(R.layout.confirm_dialog);


        ImageView warningImageView = customDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.confirm_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.confirm_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_check_circle_48px);
        titleTextView.setText("예약 확인");
        titleTextView.setTextColor(Color.parseColor("2FC600"));
        contentTextView.setText("예약이 확정되었습니다.");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }


    // 팝업창 닫을 때(확인 or 취소), 클릭리스너
    View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customDialog.dismiss();
        }
    };


}
