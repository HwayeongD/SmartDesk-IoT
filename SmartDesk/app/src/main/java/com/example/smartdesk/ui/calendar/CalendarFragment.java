    package com.example.smartdesk.ui.calendar;

    import static android.content.ContentValues.TAG;

    import android.app.Dialog;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.CalendarView;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ListView;
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
    import java.util.ArrayList;
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
        ArrayAdapter<String> adapter;
        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        private String selectedDate; // 현재 선택된 날짜를 저장하는 변수
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


            int currentYear  = calendar.get(Calendar.YEAR); // Month starts from 0
            int currentMonth  = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
            int currentDay  = calendar.get(Calendar.DATE); // Month starts from 0
            String empId = Employee.getInstance().getEmpId().toString();

            Log.d("CalendarFragment", "Today: " + today);
            Log.d("CalendarFragment", "Current Year: " + currentYear );
            Log.d("CalendarFragment", "Current Month: " + currentMonth );
            Log.d("CalendarFragment", "Employee ID: " + empId);


            // 처음 Fragment에 들어왔을 때 모든 schedule 데이터 받아오기
            retrofitAPI.getScheduleByDate(empId, currentYear, currentMonth, currentDay).enqueue(new Callback<List<Schedule>>() {
                @Override
                public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, "yes");

                        List<Schedule> data = response.body();
                        if (data != null) {
                            List<String> titleList = new ArrayList<>();
                            for (Schedule schedule : data) {
                                titleList.add(schedule.getHead());
                                Log.d(TAG, schedule.toString());
                            }
                            // 어댑터 초기화
                            adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, titleList);
                            // ListView에 어댑터 설정
                            ListView listView = root.findViewById(R.id.calendarListview);
                            listView.setAdapter(adapter);


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // 해당 아이템을 클릭했을 때의 처리
                                    Schedule selectedSchedule = data.get(position); // 클릭된 아이템의 Schedule 객체 가져오기

                                    // 다이얼로그를 띄우고 선택된 스케줄 정보를 전달하여 보여줌
                                    updateSchedule(selectedSchedule);
                                }
                            });


                        }
                        else {
                            Log.d(TAG, "No data received");
                        }
                    }
                    else { Log.d(TAG, "Request failed"); }
                }

                @Override
                public void onFailure(Call<List<Schedule>> call, Throwable t) {
                    Log.d(TAG, "Network error: " + t.getMessage());
                }
            });


            // 날짜를 선택 했을 때, 해당하는 날짜의 일정 불러오기
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                    // 날짜가 선택되었을 때 실행되는 부분
                    selectedDate = year + "-" + (month + 1) + "-" + day;
                    Log.d("CalendarFragment", "Selected Date: " + selectedDate);
                    Log.d("CalendarFragment", "Selected Date: " + year);
                    Log.d("CalendarFragment", "Selected Date: " + day);

                    // 선택된 날짜에 해당하는 일정을 가져와서 리스트에 표시
                    retrofitAPI.getScheduleByDate(empId, year, month + 1, day).enqueue(new Callback<List<Schedule>>() {
                        public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                            if (response.isSuccessful()) {

                                Log.d(TAG, "yes");
                                List<Schedule> data = response.body();
                                if (data != null && !data.isEmpty()) {
//                                    Log.d(TAG, data.toString());
                                    List<String> titleList = new ArrayList<>();
                                    for (Schedule schedule : data) {
                                        titleList.add(schedule.getHead());
                                        Log.d(TAG, schedule.toString());
                                    }
                                    adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, titleList);
                                    ListView listView = root.findViewById(R.id.calendarListview);
                                    listView.setAdapter(adapter);

                                } else {
                                    List<String> titleList = new ArrayList<>();
                                    titleList.add("일정 없음");
                                    adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, titleList);
                                    // ListView에 어댑터 설정
                                    ListView listView = root.findViewById(R.id.calendarListview);
                                    listView.setAdapter(adapter);
                                    Log.d(TAG, "No data received");
                                }

                            } else {
                                Log.d(TAG, "Request failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Schedule>> call, Throwable t) {
                            Log.d(TAG, "Network error: " + t.getMessage());
                        }
                    });
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



        // 새로운 일정 추가
        private void addSchedule() {
            scheduleDialog = new Dialog(requireContext(), R.style.Theme_Login);
            scheduleDialog.setContentView(R.layout.schedule_dialog_add);
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
                    newSchedule.setHead(title);
                    newSchedule.setDetail(memo);

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

        private void updateSchedule(Schedule schedule) {
            Dialog updateDialog = new Dialog(requireContext(), R.style.Theme_Login);
            updateDialog.setContentView(R.layout.schedule_dialog_update);

            EditText schedule_title_update = updateDialog.findViewById(R.id.schedule_title_update);
            EditText schedule_memo_update = updateDialog.findViewById(R.id.schedule_memo_update);
            TextView delete_schedule = updateDialog.findViewById(R.id.delete_schedule);
            TextView confirm_update_schedule = updateDialog.findViewById(R.id.confirm_update_schedule);

            // 기존 스케줄 정보를 다이얼로그에 표시
            schedule_title_update.setText(schedule.getHead());
            schedule_memo_update.setText(schedule.getDetail());

            confirm_update_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newTitle = schedule_title_update.getText().toString();
                    String newMemo = schedule_memo_update.getText().toString();

                    // 변경된 정보를 서버로 전송하는 로직 추가

                    updateDialog.dismiss();
                }
            });

            delete_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDialog.dismiss();
                }
            });

            updateDialog.show();
        }

    }
