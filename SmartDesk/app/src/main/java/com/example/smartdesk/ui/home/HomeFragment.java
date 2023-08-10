package com.example.smartdesk.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartdesk.MainActivity;
import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.FragmentHomeBinding;
import com.example.smartdesk.ui.dialog.ConfirmDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    Retrofit retrofit = RetrofitClient.getClient();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    public boolean isAllowed = false;

    private TextView empNickname;
    private TextView empScheduleTime;
    private TextView empScheduleContent;
    private TextView empSeatId;
    private TextView empDeskHeight;
//    Dialog  timerDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView nickname = binding.homeBtnProfileName;
        final TextView mySeat = binding.mySeat;
        final TextView myDeskHeight = binding.myDeskHeight;

        homeViewModel.getNickname().observe(getViewLifecycleOwner(), nickname::setText);
        homeViewModel.getMySeat().observe(getViewLifecycleOwner(), mySeat::setText);
        homeViewModel.getMyDeskHeight().observe(getViewLifecycleOwner(), myDeskHeight::setText);

        empNickname = (TextView) root.findViewById(R.id.home_btn_profile_name);
        empScheduleTime = (TextView) root.findViewById(R.id.today_schedule_time);
        empScheduleContent = (TextView) root.findViewById(R.id.today_schedule_content);
        empSeatId = (TextView) root.findViewById(R.id.my_seat);
        empDeskHeight = (TextView) root.findViewById(R.id.my_desk_height);

        // 좌석 정보 버튼 - 좌석페이지로 이동
        root.findViewById(R.id.home_btn_seat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSeatFragment();
            }
        });
        
        // 책상 높이 정보 버튼 - 선호 높이로 책상 조절 요청
        root.findViewById(R.id.home_btn_desk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqMoveDesk();
            }
        });

        // 퇴근 버튼 - click 리스터
        root.findViewById(R.id.home_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 퇴근 확인 팝업 발생 후 처리
                empLeave();
            }
        });

        retrofitAPI.getEmpData(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee data = response.body();
                Log.d(TAG, data.toString());

                Employee.getInstance().setNickname(data.getNickname());
                if(data.getNickname() != null && !data.getNickname().equals("")) {
                    empNickname.setText("Welome, " + data.getNickname());
                }

                Employee.getInstance().setWorkAttTime(data.getWorkAttTime());

                Employee.getInstance().setSchStart(data.getSchStart());
                if(data.getSchStart() != null && !data.getSchStart().equals("")) {
                    empScheduleTime.setText(data.getSchStart().split(" ")[1].substring(0, 5));
                }

                Employee.getInstance().setSchHead(data.getSchHead());
                if(data.getSchHead() != null && !data.getSchHead().equals("")) {
                    empScheduleContent.setText(data.getSchHead());
                }

                Employee.getInstance().setSeatId(data.getSeatId());
                if(data.getSeatId() != null && !data.getSeatId().equals("")) {
                    empSeatId.setText(data.getSeatId());
                }

                Employee.getInstance().setPersonalDeskHeight(data.getPersonalDeskHeight());
                if(data.getPersonalDeskHeight() != null && !data.getPersonalDeskHeight().equals("")) {
                    empDeskHeight.setText(data.getPersonalDeskHeight() + "cm");
                }

                Employee.getInstance().setAutoBook(data.getAutoBook());

                Log.d(TAG, Employee.getInstance().printEmpData());
                reserveSeat();
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "getEmpData 통신 실패");
            }
        });

        return root;
    }

    // 선호하는 책상 높이로 조절
    private void reqMoveDesk() {
        retrofitAPI.reqMoveDeskHeight(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Log.d(TAG, "My desk is moved");
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "My desk is NOT moved");
            }
        });
    }

    private void empLeave() {
        retrofitAPI.reqLeave(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee data = response.body();
                Log.d(TAG, "Leave at " + data.getWorkEndTime());
                Employee.getInstance().setWorkEndTime(data.getWorkEndTime());

                TextView exitTime = binding.getRoot().findViewById(R.id.exit_time);
                exitTime.setText(data.getWorkEndTime());
                exitTime.setVisibility(View.VISIBLE);

                ConstraintLayout exitLayout = binding.getRoot().findViewById(R.id.home_btn_exit);
                exitLayout.setBackgroundColor(Color.parseColor("#BFBFBF"));
                exitLayout.setClickable(false);
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

            }
        });
    }

    public void reserveSeat() {
        Log.d(TAG, "reserveSeat() Start");
        // 출근시간이 있어야 좌석 정보를 판단 가능
        if(Employee.getInstance().getWorkAttTime() != null && !Employee.getInstance().getWorkAttTime().isEmpty()) {
            // 좌석정보 없어야 예약 여부를 판단 가능
            if(Employee.getInstance().getSeatId() == null || Employee.getInstance().getSeatId().isEmpty()) {
                Log.d(TAG, "Personal Auto Reserve: " + Employee.getInstance().getAutoBook());
                // 자동 좌석 예약 기능 ON
                if(Employee.getInstance().getAutoBook()) {
                    if(((MainActivity) getActivity()).isFirst) {
                        showTimerDialog();
                    }
                }
                // 자동 좌석 예약 기능 OFF
                else {
                    if(((MainActivity) getActivity()).isFirst) {
                        // 좌석페이지로 바로 이동
                        goToSeatFragment();
                    }
                }
            }
        }
        Log.d(TAG, "reserveSeat() End");
    }

    private void reqAutoReserve() {
        retrofitAPI.reqAutoReserveSeat(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee data = response.body();
                Log.d(TAG, "reserved success : " + data.getReserveSuccess().toString());
                Log.d(TAG, "reserved seatId : " + data.getSeatId());
                if(data.getReserveSuccess()) {
                    Employee.getInstance().setSeatId(data.getSeatId());
                    empSeatId.setText(data.getSeatId());
                    Log.d(TAG, "reserved seatId : " + Employee.getInstance().getSeatId());
                }
                else {
                    goToSeatFragment();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

            }
        });
    }

    private void showTimerDialog() {
        ConfirmDialog autoReserveDialog = new ConfirmDialog(this.getContext(), R.drawable.ic_error_48px, "예약 안내", "최근 좌석으로 예약하시겠습니까?\n(3초 후 자동 예약됩니다)");
//        timerDialog = new Dialog(this.getContext());
//        timerDialog.setContentView(R.layout.confirm_dialog);
//        // 뒷 배경 투명하게 만들어주기
//        timerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        timerDialog.show();

        autoReserveDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                goToSeatFragment();
            }
        });

        autoReserveDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(autoReserveDialog.isShowing()) {
                    autoReserveDialog.dismiss();
                    reqAutoReserve();
                    isAllowed = true;
                }
            }
        }, 3000);
    }

    private void goToSeatFragment() {
        ((MainActivity) getActivity()).isFirst = false;
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_home_to_seat);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}