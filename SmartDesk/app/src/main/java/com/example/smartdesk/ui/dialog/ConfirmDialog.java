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
    }

    // confirm_dialog 확인용 버튼 1개
    ImageView conDialogImg;
    TextView conDialogTitle, conDialogContent;
    Button conOkBtn;

    private int img;
    private String title;
    private String content;

    private String[] titleColors = {"#2FC600", "#FF7F00", "#FF0000"};

    public CustomDialogInterface customDialogInterface;

    private ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, int dImgResource, String dTitle, String dContent) {
        super(context);

        img = dImgResource;
        title = dTitle;
        content = dContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleColors = new String[3];
        titleColors[0] = "#2FC600"; titleColors[1] = "#FF7F00"; titleColors[2] = "#FF0000";

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.confirm_dialog);

        // confirm_dialog 확인용 버튼 1개
        conDialogImg = findViewById(R.id.confirm_dialog_image);
        conDialogTitle = findViewById(R.id.confirm_dialog_title);
        conDialogContent = findViewById(R.id.confirm_dialog_content);
        conOkBtn = findViewById(R.id.confirm_yesbtn);

        conDialogImg.setImageResource(img);
        conDialogTitle.setText(title);
        conDialogTitle.setTextColor(Color.parseColor(getTitleColor(img)));
        conDialogContent.setText(content);

        conOkBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.confirm_yesbtn) {
            if(title.equals("예약 안내"))
                customDialogInterface.okBtnClicked("아니오");
            else
                customDialogInterface.okBtnClicked("확인");
            dismiss();
        }
    }

    public void setDialogListener(CustomDialogInterface customDialogInterface) {
        this.customDialogInterface = customDialogInterface;
    }

    private String getTitleColor(int dImageId) {
        if(dImageId == R.drawable.ic_check_circle_48px) return titleColors[0];
        else if(dImageId == R.drawable.ic_error_48px) return titleColors[1];
        else if(dImageId == R.drawable.ic_do_not_disturb_on_total_silence_48px) return titleColors[2];

        return "#000000";
    }

}
