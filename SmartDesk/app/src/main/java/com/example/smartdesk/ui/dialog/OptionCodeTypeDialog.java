package com.example.smartdesk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartdesk.R;
import com.example.smartdesk.ui.dialog.CustomDialogClickListener; // 위에서 생성한 interface 의 path

public class OptionCodeTypeDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView tvTitle, tvNegative, tvPositive, tvContent;

    public OptionCodeTypeDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);

        tvTitle = findViewById(R.id.dialogTitle);
        tvContent = findViewById(R.id.dialogContent);
        tvPositive = findViewById(R.id.yesbtn);
        tvNegative = findViewById(R.id.nobtn);

        tvPositive.setOnClickListener(v -> {
            // 저장버튼 클릭
            this.customDialogClickListener.yesClick();
            dismiss();
        });
        tvNegative.setOnClickListener(v -> {
            // 취소버튼 클릭
            this.customDialogClickListener.noClick();
            dismiss();
        });
    }
}