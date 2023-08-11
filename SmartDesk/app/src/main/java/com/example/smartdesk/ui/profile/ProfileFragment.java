package com.example.smartdesk.ui.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.LoginActivity;
import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.FragmentProfileBinding;
import com.example.smartdesk.ui.dialog.CheckDialog;
import com.example.smartdesk.ui.dialog.ConfirmDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {

    final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;

    Retrofit retrofit = RetrofitClient.getClient();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    Dialog deskDialog;
    Dialog logoutDialog;
    Dialog ChangePasswordDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView change_desk = root.findViewById(R.id.change_desk);
        TextView desk_height = root.findViewById(R.id.desk_height);
        ImageView info = root.findViewById(R.id.info);
        TextView change_pw = root.findViewById(R.id.change_pw);
        Switch isAutoReserve = root.findViewById(R.id.option_switch);
        TextView logoutBtn = root.findViewById(R.id.logout);

        // 비밀번호 변경 클릭 시, 페이지 이동
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogShow();
            }
        });

        // 책상 높이 버튼 클릭 시, 팝업 띄우기
        change_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeskDiaglogShow();
            }
        });
        desk_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeskDiaglogShow();
            }
        });

        // 좌석 자동 예약 info 클릭 시, 팝업 띄우기
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog infoDialog =
                        new ConfirmDialog(getContext(), R.drawable.ic_error_48px, "좌석 자동 예약", "초기 앱 실행 시 \n최근 좌석 자동 예약 기능");
                infoDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
                    @Override
                    public void okBtnClicked(String btnName) {

                    }
                });

                infoDialog.show();
            }
        });

        Log.d(TAG, "isAutoReserve: " + isAutoReserve.isChecked());
        // 좌석 자동 예약 토글 셋팅
        isAutoReserve.setChecked(Employee.getInstance().getAutoBook());
        isAutoReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Employee.getInstance().setAutoBook(isAutoReserve.isChecked());
                retrofitAPI.reqChangeAutoToggle(Employee.getInstance().getEmpId().toString(), Employee.getInstance()).enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        Log.d(TAG, "Auto Reserve Toggle is changed: " + isAutoReserve.isChecked());
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        Log.d(TAG, "Auto Reserve Toggle is NOT changed");
                    }
                });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialogShow();
            }
        });

        return root;
    }

    private void logoutDialogShow() {
        logoutDialog = new Dialog(this.getContext());
        logoutDialog.setContentView(R.layout.check_dialog);
        // 뒷 배경 투명하게 만들어주기
        logoutDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        logoutDialog.show();
        
        // 로그아웃 확인 다이얼로그 설정
        ImageView warningImageView = logoutDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = logoutDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = logoutDialog.findViewById(R.id.check_dialog_content);
        Button yesbtn = logoutDialog.findViewById(R.id.check_yesbtn);
        Button no_btn = logoutDialog.findViewById(R.id.check_nobtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("로그아웃");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("로그아웃하시겠습니까?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
                
                // 로그아웃 시 사원ID 로컬 데이엍 삭제 (SharedPreferences)
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
                SharedPreferences.Editor logoutEditor = sharedPreferences.edit();
                logoutEditor.clear();
                logoutEditor.apply();

                // 로그아웃 요청시 LoginActivity로 이동
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });
    }

    // 선호 책상 높이 변경 여부 확인 팝업 발생
    private void changeDeskDiaglogShow() {
        CheckDialog chDeskDialog =
                new CheckDialog(this.getContext(), R.drawable.ic_error_48px, "책상 높이 변경", "현재 높이를 즐겨찾기 책상 높이로 \n변경하시겠습니까?");
        deskDialog = new Dialog(this.getContext());
        deskDialog.setContentView(R.layout.check_dialog);
        // 뒷 배경 투명하게 만들어주기
        deskDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        deskDialog.show();

        chDeskDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                Log.d(TAG, "changeDeskDiaglogShow(): Clicked OK");
                reqChangeDeskHeight();
            }

            @Override
            public void noBtnClicked(String btnName) {
                Log.d(TAG, "changeDeskDiaglogShow(): Clicked NO");
            }
        });

        chDeskDialog.show();
    }

    private void reqChangeDeskHeight() {
        retrofitAPI.reqChangeDeskHeight(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee data = response.body();
                Employee.getInstance().setPersonalDeskHeight(data.getPersonalDeskHeight());

                Log.d(TAG, "Personal Desk Height is changed: " + Employee.getInstance().getPersonalDeskHeight());
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "Personal Desk Height is NOT changed");
            }
        });

    }

    private void ChangePasswordDialogShow() {
        ChangePasswordDialog = new Dialog(this.getContext(), R.style.Theme_Login);
        ChangePasswordDialog.setContentView(R.layout.change_pw_dialog);
        ChangePasswordDialog.show();


        //다이얼로그의 구성요소들이 동작할 코드작성
        TextInputLayout inputPresentPassword = ChangePasswordDialog.findViewById(R.id.pw_present);
        TextInputLayout inputNewPassword = ChangePasswordDialog.findViewById(R.id.pw_new);
        TextInputLayout inputConfirmPassword = ChangePasswordDialog.findViewById(R.id.pw_confirm);
        TextInputEditText editPresentPassword = ChangePasswordDialog.findViewById(R.id.edit_pw_present);
        TextInputEditText editNewPassword = ChangePasswordDialog.findViewById(R.id.edit_pw_new);
        TextInputEditText editCofirmPassword = ChangePasswordDialog.findViewById(R.id.edit_pw_confirm);
        Button changePasswordBtn = ChangePasswordDialog.findViewById(R.id.btn_change_pw);
        ImageView closePasswordView = ChangePasswordDialog.findViewById(R.id.close_change_pw);

        // local에서 비밀번호 동일한지 체크
        editPresentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String presentPassword = Employee.getInstance().get
//                if (!editPresentPassword.equals(presentPassword)) {
//                    inputPresentPassword.setError("기존 비밀번호와 동일하지 않습니다.");
//                }
//                else {
//                    inputPresentPassword.setError(null);
//                }
            }
        });
        editNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String presentPw = editPresentPassword.getText().toString();
                String newPw = editNewPassword.getText().toString();

                if (newPw.equals(presentPw)) {
                    inputNewPassword.setError("기존 비밀번호와 같습니다.");
                }
                else {
                    inputNewPassword.setError(null);
                }
            }
        });
        editCofirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newPw = editNewPassword.getText().toString();
                String confirmPw = editCofirmPassword.getText().toString();
                if (!confirmPw.equals(newPw)) {
                    inputConfirmPassword.setError("비밀번호가 일치하지 않습니다.");
                }
                else {
                    inputConfirmPassword.setError(null);
                }

            }
        });


        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String presentPw = editPresentPassword.getText().toString();
                String newPw = editNewPassword.getText().toString();
                String confirmPw = editCofirmPassword.getText().toString();

                // 입력이 되어 있지 않으면 Error
                if (TextUtils.isEmpty(presentPw)) {
                    inputPresentPassword.setError("현재 비밀번호를 입력해주세요.");
                }
                if (TextUtils.isEmpty(newPw)) {
                    inputNewPassword.setError("새 비밀번호를 입력해주세요.");
                }
                if (TextUtils.isEmpty(confirmPw)) {
                    inputConfirmPassword.setError("비밀번호를 확인해주세요.");
                }
                else {
                    Employee.getInstance().getPassword();

                }
            }
        });
        closePasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialog.dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



