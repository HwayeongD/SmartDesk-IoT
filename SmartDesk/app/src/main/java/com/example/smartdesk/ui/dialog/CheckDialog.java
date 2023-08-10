package com.example.smartdesk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartdesk.R;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    public interface CustomDialogInterface {
        void okBtnClicked(String btnName);
        void noBtnClicked(String btnName);
    }

    private static ConfirmDialog customDialog;

    // confirm_dialog 확인용 버튼 1개
    ImageView conDialogImg;
    TextView conDialogTitle;
    TextView conDialogContent;
    Button conOkBtn;
    
    // check_dialog 질의용 버튼 2개
    ImageView checkDialogImg;
    TextView checkDialogTitle;
    TextView checkDialogContent;
    Button checkOkBtn;
    Button checkNoBtn;

    private String[] titleColors;

    public CustomDialogInterface customDialogInterface;

    private ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, int dImgResource, String dTitle, String dContent, String okBtn) {
        super(context);

        conDialogImg.setImageResource(dImgResource);
        conDialogTitle.setText(dTitle);
        conDialogTitle.setTextColor(Color.parseColor(getTitleColor(dImgResource)));
        conDialogContent.setText(dContent);
    }

    public ConfirmDialog(Context context, int dImgResource, String dTitle, String dContent, String okBtn, String noBtn) {
        super(context);

        checkDialogImg.setImageResource(dImgResource);
        checkDialogTitle.setText(dTitle);
        checkDialogTitle.setTextColor(Color.parseColor(getTitleColor(dImgResource)));
        checkDialogContent.setText(dContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // confirm_dialog 확인용 버튼 1개
        conDialogImg = findViewById(R.id.confirm_dialog_image);
        conDialogTitle = findViewById(R.id.confirm_dialog_title);
        conDialogContent = findViewById(R.id.confirm_dialog_content);
        conOkBtn = findViewById(R.id.confirm_yesbtn);

        // check_dialog 질의용 버튼 2개
        checkDialogImg = findViewById(R.id.check_dialog_image);
        checkDialogTitle = findViewById(R.id.check_dialog_title);
        checkDialogContent = findViewById(R.id.check_dialog_content);
        checkOkBtn = findViewById(R.id.check_yesbtn);
        checkNoBtn = findViewById(R.id.check_nobtn);

        titleColors = new String[3];
        titleColors[0] = "#2FC600"; titleColors[1] = "#FF7F00"; titleColors[2] = "#FF0000";
    }

    public static ConfirmDialog getInstance(Context context) {
        customDialog = new ConfirmDialog(context);

        // 다이어로그 테두리 둥글게 만들어주기 위한 필수 코드!!!
        // MAYBE... 기본으로 제공해주는 배경을 제거해주는 코드인 거 같다
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return customDialog;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.check_yesbtn) {
            customDialogInterface.okBtnClicked("확인");
            dismiss();
        }
        else if(view.getId() == R.id.check_nobtn) {
            customDialogInterface.noBtnClicked("취소");
            dismiss();
        }
    }

    public void setDialogListener(CustomDialogInterface customDialogInterface) {
        this.customDialogInterface = customDialogInterface;
    }

    //책상 높이 변경 체크 팝업
    public void showCheckDialog() {
        //참조할 다이얼로그 화면을 연결한다.
        //참조할 다이얼로그 화면을 연결한다.
        customDialog.setContentView(R.layout.check_dialog);

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView warningImageView = customDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.check_dialog_content);
        Button yesbtn = customDialog.findViewById(R.id.check_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("책상 높이 변경");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("현재 높이를 즐겨찾기 책상 높이로 \n변경하시겠습니까?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reqChangeDeskHeight();
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
        contentTextView.setText("초기 앱 실행 시 \n최근 좌석 자동 예약 기능");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }

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
        titleTextView.setTextColor(Color.parseColor("#FF0000"));
        contentTextView.setText("이미 예약된 좌석입니다");

        yesbtn.setOnClickListener(clickCancel);
        customDialog.show();
    }

    //좌석 취소 체크 팝업
    public void showCancelDeskDialog() {
        //참조할 다이얼로그 화면을 연결한다.
        customDialog.setContentView(R.layout.check_dialog);

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView warningImageView = customDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = customDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = customDialog.findViewById(R.id.check_dialog_content);
        Button yesBtn = customDialog.findViewById(R.id.check_yesbtn);
        Button no_btn = customDialog.findViewById(R.id.check_nobtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("예약 취소");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("이미 예약한 본인 좌석입니다\n좌석을 취소하시겠습니까?");
        yesBtn.setOnClickListener(cancelClickListener);
        no_btn.setOnClickListener(clickCancel);
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
        Button yesBtn = customDialog.findViewById(R.id.check_yesbtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("예약 변경");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("이미 예약된 좌석이 있습니다\n좌석을 변경하시겠습니까?");
        yesBtn.setOnClickListener(changeClickListener);
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

    private String getTitleColor(int dImageId) {

    }


    // 팝업창 닫을 때(확인 or 취소), 클릭리스너
    View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customDialog.dismiss();
        }
    };


}
