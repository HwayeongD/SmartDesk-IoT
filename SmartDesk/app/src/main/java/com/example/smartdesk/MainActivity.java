package com.example.smartdesk;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.ActivityMainBinding;
import com.example.smartdesk.ui.calendar.CalendarFragment;
import com.example.smartdesk.ui.home.HomeFragment;
import com.example.smartdesk.ui.profile.ProfileFragment;
import com.example.smartdesk.ui.seat.SeatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.container_fragment);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        String reqEmpId = Employee.getInstance().getEmpId().toString();
        retrofitAPI.getEmpData(reqEmpId).enqueue(new Callback<Employee>() {
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
                Employee.getInstance().setPersonalAutoReserve(data.getPersonalAutoReserve());

                Log.d(TAG, Employee.getInstance().printEmpData());

                reserveSeat();
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Log.d(TAG, "getEmpData 통신 실패");
            }
        });

    }

    public void reserveSeat() {
        Log.d(TAG, "reserveSeat()");
        Log.d(TAG, "getWorkAttTime() : " + Employee.getInstance().getWorkAttTime());
        // 출근시간이 있어야 좌석 정보를 판단 가능
        if(Employee.getInstance().getWorkAttTime() != null && !Employee.getInstance().getWorkAttTime().isEmpty()) {
            // 좌석정보 없어야 예약 여부를 판단 가능
            if(Employee.getInstance().getSeatId() == null || Employee.getInstance().getSeatId().isEmpty()) {
                Log.d(TAG, "Personal Auto Reserve: " + Employee.getInstance().getPersonalAutoReserve());
                // 자동 좌석 예약 기능 ON
                if(Employee.getInstance().getPersonalAutoReserve()) {
                    Retrofit retrofit = RetrofitClient.getClient();
                    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

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
                                navController.navigate(R.id.navigation_seat);
                            }
                        }

                        @Override
                        public void onFailure(Call<Employee> call, Throwable t) {

                        }
                    });
                }
                // 자동 좌석 예약 기능 OFF
                else {
                    navController.navigate(R.id.navigation_seat);



                }
            }
        }
    }
}