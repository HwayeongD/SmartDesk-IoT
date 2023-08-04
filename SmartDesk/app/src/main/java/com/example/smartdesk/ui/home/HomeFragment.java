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
import android.widget.Button;
import android.widget.ImageView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    Retrofit retrofit = RetrofitClient.getClient();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    Dialog timerDialog; // 좌석 배정 타이머 팝업

    public boolean isAllowed = false;
    private String nSeatNum;

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
                empLeave();
            }
        });

        retrofitAPI.getEmpData(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee data = response.body();
                Log.d(TAG, data.toString());

                Employee.getInstance().setNickname(data.getNickname());
                Employee.getInstance().setWorkAttTime(data.getWorkAttTime());
                Employee.getInstance().setCalTime(data.getCalTime());
                Employee.getInstance().setCalDetail(data.getCalDetail());
                Employee.getInstance().setSeatId(data.getSeatId());
                Employee.getInstance().setPersonalDeskHeight(data.getPersonalDeskHeight());
                Employee.getInstance().setAutoBook(data.getAutoBook());

                nSeatNum = data.getSeatId();
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
                    // 좌석페이지로 바로 이동
                    goToSeatFragment();
                }
            }
        }
        Log.d(TAG, "reserveSeat() End");
    }

    private void reqAutoReserve() {
        retrofitAPI.reqAutoReserveSeat(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Log.d(TAG, "reserved success : " + response.body().getReserveSuccess());
                Log.d(TAG, "reserved seatId : " + response.body().getSeatId());
                if(response.body().getReserveSuccess()) {
                    Employee.getInstance().setSeatId(response.body().getSeatId());
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
        timerDialog = new Dialog(this.getContext());
        timerDialog.setContentView(R.layout.confirm_dialog);
        timerDialog.show();

        ImageView warningImageView = timerDialog.findViewById(R.id.confirm_dialog_image);
        TextView titleTextView = timerDialog.findViewById(R.id.confirm_dialog_title);
        TextView contentTextView = timerDialog.findViewById(R.id.confirm_dialog_content);

        warningImageView.setImageResource(R.drawable.ic_error_48px);
        titleTextView.setText("예약 안내");
        titleTextView.setTextColor(Color.parseColor("#FF7F00"));
        contentTextView.setText("최근 좌석으로 예약하시겠습니까?\n(3초 후 자동 예약됩니다)");

        Button noBtn = timerDialog.findViewById(R.id.confirm_yesbtn);
        noBtn.setText("아니오");
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerDialog.dismiss();
                goToSeatFragment();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(timerDialog.isShowing()) {
                    timerDialog.dismiss();
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