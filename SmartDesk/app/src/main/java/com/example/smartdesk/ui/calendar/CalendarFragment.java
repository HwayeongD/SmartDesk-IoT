    package com.example.smartdesk.ui.calendar;

    import static android.content.ContentValues.TAG;

    import android.app.Dialog;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CalendarView;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Switch;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;

    import com.example.smartdesk.R;
    import com.example.smartdesk.data.Model.Employee;
    import com.example.smartdesk.data.Model.Schedule;
    import com.example.smartdesk.data.RetrofitAPI;
    import com.example.smartdesk.data.RetrofitClient;
    import com.example.smartdesk.databinding.FragmentCalendarBinding;

    import java.text.SimpleDateFormat;
    import java.util.Calendar;
    import java.util.List;
    import java.util.Locale;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import retrofit2.Retrofit;

    public class CalendarFragment extends Fragment {

        Dialog scheduleDialog;
        CalendarView calendarView;

        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        private FragmentCalendarBinding binding;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            CalendarViewModel calendarViewModel =
                    new ViewModelProvider(this).get(CalendarViewModel.class);

            binding = FragmentCalendarBinding.inflate(inflater, container, false);
            View root = binding.getRoot();


            // 날짜 설정
            calendarView = root.findViewById(R.id.calendarView);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = dateFormat.format(calendar.getTime());

            int currentMonth = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
            String empId = Employee.getInstance().getEmpId().toString();

            Log.d("CalendarFragment", "Today: " + today);
            Log.d("CalendarFragment", "Current Month: " + currentMonth);
            Log.d("CalendarFragment", "Employee ID: " + empId);

            // 처음 Fragment에 들어왔을 때 모든 schedule 데이터 받아오기
            retrofitAPI.getSchedule(empId, currentMonth).enqueue(new Callback<List<Schedule>>() {
                @Override
                public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, "yes");

                        List<Schedule> data = response.body();
//                        String abc = response.body().toString();
//                        Log.d(TAG, abc);
                        for (Schedule schedule : data) {
                            Log.d(TAG, "Id: " + schedule.getSchId() + ", Head: " + schedule.getSchHead() + ", Start: " + schedule.getSchStart()
                                    + ", End: " + schedule.getSchEnd() + ", Status: " + schedule.getStatus() + ", Detail: " + schedule.getSchDetail());
                        }
                    }
                    else {
                        Log.d(TAG, "No");
                    }
                }

                @Override
                public void onFailure(Call<List<Schedule>> call, Throwable t) {
                    Log.d(TAG, "네트워크 오류");
                }
            });



            // 날짜를 선택 했을 때, 해당하는 날짜의 일정 불러오기
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    // 날짜가 선택되었을 때 실행되는 부분
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    // loadDataForDate(selectedDate);
                    Log.d("CalendarFragment", "Selected Date: " + selectedDate);
                }
            });

            // 새로운 일정 추가하기
            ImageView plus_schedule = root.findViewById(R.id.add_schedule);

            plus_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addSchedule();
                }
            });
            return root;
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }



        private void addSchedule() {
            scheduleDialog = new Dialog(requireContext(), R.style.Theme_Login);
            scheduleDialog.setContentView(R.layout.add_schedule_dialog);
            scheduleDialog.show();

            EditText schedule_title = scheduleDialog.findViewById(R.id.schedule_title);
            Switch status_switch = scheduleDialog.findViewById(R.id.status_switch);
            TextView start_schedule_day = scheduleDialog.findViewById(R.id.start_schedule_day);
            EditText start_schedule_time = scheduleDialog.findViewById(R.id.start_schedule_time);
            TextView finish_schedule_day = scheduleDialog.findViewById(R.id.start_schedule_day);
            EditText finish_schedule_time = scheduleDialog.findViewById(R.id.start_schedule_time);
            EditText schedule_memo = scheduleDialog.findViewById(R.id.schedule_memo);
            TextView cancel_add_schedule = scheduleDialog.findViewById(R.id.cancel_add_schedule);
            TextView confirm_add_schedule = scheduleDialog.findViewById(R.id.confirm_add_schedule);


            confirm_add_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = schedule_title.getText().toString();
                    String memo = schedule_memo.getText().toString();
                    String empId = Employee.getInstance().getEmpId().toString();


                    Schedule newSchedule = new Schedule();
                    newSchedule.setSchHead(title);
                    newSchedule.setSchDetail(memo);

                    // Send the POST request to the server
                    retrofitAPI.reqCreateSchedule(empId, newSchedule).enqueue(new Callback<Schedule>() {
                        @Override
                        public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                            if (response.isSuccessful()) {
                                Log.d("CalendarFragment", "title:" + title);
                                Log.d("CalendarFragment", "title:" + empId);
                                Log.d("CalendarFragment", "Schedule added successfully");
                                scheduleDialog.dismiss();
                            } else {
                                Log.e("CalendarFragment", "Failed to add schedule");
                            }
                        }

                        @Override
                        public void onFailure(Call<Schedule> call, Throwable t) {
                            Log.e("CalendarFragment", "Network error: " + t.getMessage());
                        }
                    });
                }
            });

            cancel_add_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleDialog.dismiss();
                }
            });
        }

    }
