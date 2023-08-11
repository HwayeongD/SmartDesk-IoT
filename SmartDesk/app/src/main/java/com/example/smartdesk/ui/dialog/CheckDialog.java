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

public class CheckDialog extends Dialog implements View.OnClickListener {
    public interface CustomDialogInterface {
        void okBtnClicked(String btnName);
        void noBtnClicked(String btnName);
    }

    private Context context;

    // check_dialog 질의용 버튼 2개
    ImageView checkDialogImg;
    TextView checkDialogTitle;
    TextView checkDialogContent;
    Button checkOkBtn;
    Button checkNoBtn;

    private int img;
    private String title;
    private String content;

    private String[] titleColors = {"#2FC600", "#FF7F00", "#FF0000"};

    private CustomDialogInterface customDialogInterface;

    public CheckDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CheckDialog(Context context, int dImgResource, String dTitle, String dContent) {
        super(context);

        img = dImgResource;
        title = dTitle;
        content = dContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.check_dialog);

        // check_dialog 질의용 버튼 2개
        checkDialogImg = findViewById(R.id.check_dialog_image);
        checkDialogTitle = findViewById(R.id.check_dialog_title);
        checkDialogContent = findViewById(R.id.check_dialog_content);
        checkOkBtn = findViewById(R.id.check_yesbtn);
        checkNoBtn = findViewById(R.id.check_nobtn);

        checkDialogImg.setImageResource(img);
        checkDialogTitle.setText(title);
        checkDialogTitle.setTextColor(Color.parseColor(getTitleColor(img)));
        checkDialogContent.setText(content);

        checkOkBtn.setOnClickListener(this);
        checkNoBtn.setOnClickListener(this);

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

    private String getTitleColor(int dImageId) {
        if(dImageId == R.drawable.ic_check_circle_48px) return titleColors[0];
        else if(dImageId == R.drawable.ic_error_48px) return titleColors[1];
        else if(dImageId == R.drawable.ic_do_not_disturb_on_total_silence_48px) return titleColors[2];

        return "#000000";
    }

}
