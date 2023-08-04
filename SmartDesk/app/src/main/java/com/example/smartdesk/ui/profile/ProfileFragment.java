package com.example.smartdesk.ui.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.FragmentProfileBinding;
import com.example.smartdesk.ui.dialog.ChangePasswordDialog;
import com.example.smartdesk.ui.dialog.CustomDialog;

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

        // 비밀번호 변경 클릭 시, 페이지 이동
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog.getInstance(requireActivity()).showChangePasswordDialog();
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
                CustomDialog.getInstance(requireActivity()).showConfirmDialog();
            }
        });

        Log.d(TAG, "isAutoReserve: " + isAutoReserve.isChecked());
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

        return root;
    }

    private void changeDeskDiaglogShow() {
        deskDialog = new Dialog(this.getContext());
        deskDialog.setContentView(R.layout.check_dialog);
        deskDialog.show();

        //다이얼로그의 구성요소들이 동작할 코드작성
        ImageView warningImageView = deskDialog.findViewById(R.id.check_dialog_image);
        TextView titleTextView = deskDialog.findViewById(R.id.check_dialog_title);
        TextView contentTextView = deskDialog.findViewById(R.id.check_dialog_content);
        Button yesbtn = deskDialog.findViewById(R.id.check_yesbtn);
        Button no_btn = deskDialog.findViewById(R.id.check_nobtn);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("책상 높이 변경");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("현재 높이를 즐겨찾기 책상 높이로 \n변경하시겠습니까?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqChangeDeskHeight();
                deskDialog.dismiss();
            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deskDialog.dismiss();
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



