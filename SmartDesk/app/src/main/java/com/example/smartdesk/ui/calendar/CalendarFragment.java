package com.example.smartdesk.ui.calendar;

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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.Model.Schedule;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.FragmentCalendarBinding;
import com.example.smartdesk.ui.dialog.CheckDialog;
import com.example.smartdesk.ui.dialog.ConfirmDialog;

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

    private final String TAG = "CalendarFragment";
    Dialog scheduleDialog;
    CalendarView calendarView;
    ArrayAdapter<String> adapter;

    private ListView listView;
    private List<String> titleList;

    private Calendar selected;

    private TimePicker timePickerStart;
    private TimePicker timePickerFinish;

    private String[] scheduleField = {"", "제목", "시작시간", "종료시간", "자리비움", "내용"};

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

        titleList = new ArrayList<>();
        listView = root.findViewById(R.id.calendarListview);
        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, titleList);
        listView.setAdapter(adapter);

        // 날짜 설정
        calendarView = root.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());

        selected = Calendar.getInstance();

        int currentYear  = calendar.get(Calendar.YEAR); // Month starts from 0
        int currentMonth  = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
        int currentDay  = calendar.get(Calendar.DATE); // Month starts from 0
        String empId = Employee.getInstance().getEmpId().toString();

        Log.d("CalendarFragment", "Today: " + today);

        // 처음 Fragment 들어 왔을 때 모든 schedule 데이터 가져와서 표시해주기
        retrofitAPI.getSchedule(empId, currentYear, currentMonth).enqueue(new Callback<List<Schedule>>() {
          @Override
          public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
              if (response.isSuccessful()) {
                  List<Schedule> data = response.body();
                  Log.d(TAG, "getSchedule SUCCESS" + data);
//                  markEventDates(data);
              }
              else {
                  Log.d(TAG, "Request failed");
              }
          }

          @Override
          public void onFailure(Call<List<Schedule>> call, Throwable t) {
              Log.d(TAG, "Network error: " + t.getMessage());
          }
        });


        // 처음 Fragment에 들어 왔을 때 오늘 schedule 데이터 받아오기
        reqScheduleDate(currentYear, currentMonth, currentDay);

        // 날짜를 선택 했을 때, 해당하는 날짜의 일정 불러오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                selected.set(year, month, day);
                Log.d(TAG, selected.toString());

                // 날짜가 선택되었을 때 실행되는 부분
                selectedDate = year + "-" + (month + 1) + "-" + day;
                Log.d("CalendarFragment", "Selected Date: " + selectedDate);

                // 선택된 날짜에 해당하는 일정을 가져와서 리스트에 표시
                reqScheduleDate(year, month + 1, day);
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

    public void reqScheduleDate() {
        reqScheduleDate(selected.get(Calendar.YEAR), selected.get(Calendar.MONTH) + 1, selected.get(Calendar.DATE));
        Log.d(TAG, "Selected DAY: " + selected.get(Calendar.YEAR) + "-" + (selected.get(Calendar.MONTH) + 1) + "-" + selected.get(Calendar.DATE));
    }

    // 현재 지정한 달의 일정을 갱신
    public void reqScheduleDate(int year, int month, int day)   {
        Log.d(TAG, "reqScheduleData(): " + year + "-" + month + "-" + day);
        retrofitAPI.getScheduleByDate(Employee.getInstance().getEmpId().toString(), year, month, day).enqueue(new Callback<List<Schedule>>() {
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if (response.isSuccessful()) {
                    List<Schedule> data = response.body();
                    if(data.get(0).getResultCode() == null || data.get(0).getResultCode().equals("")) {
                        if (!data.isEmpty() && data.size() > 0) {
                            titleList.clear();

                            for (Schedule schedule : data) {
                                titleList.add(schedule.getHead());
                                Log.d(TAG, schedule.toString());
                            }

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // 해당 아이템을 클릭했을 때의 처리
                                    Schedule selectedSchedule = data.get(position); // 클릭된 아이템의 Schedule 객체 가져오기
                                    // 다이얼로그를 띄우고 선택된 스케줄 정보를 전달하여 보여줌
                                       updateSchedule(selectedSchedule);
                                }
                            });

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "No data received");
                        }
                    }
                    else if(data.get(0).getResultCode().equals("S201")) {
                        titleList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d(TAG, "Request failed");
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.d(TAG, "Network error: " + t.getMessage());
            }
        });
    }

    public String getEachDay() {
        String dayOfWeek[] = {"", "일", "월", "화", "수", "목", "금", "토"};

        return dayOfWeek[selected.get(Calendar.DAY_OF_WEEK)];
    }

    // 새로운 일정 추가
    private void addSchedule() {
        scheduleDialog = new Dialog(requireContext(), R.style.Theme_Login);
        scheduleDialog.setContentView(R.layout.schedule_dialog_add);
        scheduleDialog.show();

        EditText scheduleTitle = scheduleDialog.findViewById(R.id.schedule_title);
        TextView startScheduleTime = scheduleDialog.findViewById(R.id.start_schedule_time);
        TextView finishScheduleTime = scheduleDialog.findViewById(R.id.finish_schedule_time);
        Switch status = scheduleDialog.findViewById(R.id.status_switch);
        EditText scheduleMemo = scheduleDialog.findViewById(R.id.schedule_memo);

        TextView cancelAddSchedule = scheduleDialog.findViewById(R.id.cancel_add_schedule);
        TextView confirmAddSchedule = scheduleDialog.findViewById(R.id.confirm_add_schedule);

        timePickerStart = scheduleDialog.findViewById(R.id.timepicker_start);
        timePickerFinish = scheduleDialog.findViewById(R.id.timepicker_finish);

        TextView startScheduleDay = scheduleDialog.findViewById(R.id.start_schedule_day);
        TextView finishScheduleDay = scheduleDialog.findViewById(R.id.finish_schedule_day);
        startScheduleDay.setText((selected.get(Calendar.MONTH) + 1) + "월 " + selected.get(Calendar.DATE) + "일 (" + getEachDay() + ")");
        finishScheduleDay.setText((selected.get(Calendar.MONTH) + 1) + "월 " + selected.get(Calendar.DATE) + "일 (" + getEachDay() + ")");

        startScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timePickerFinish.getVisibility() == View.VISIBLE) {
                    timePickerFinish.setVisibility(View.GONE);
                    timePickerStart.setVisibility(View.VISIBLE);

                    startScheduleTime.setBackgroundResource(R.drawable.time_box_gray);
                    finishScheduleTime.setBackgroundResource(R.drawable.time_box);
                }
                else if(timePickerStart.getVisibility() == View.VISIBLE) {
                    timePickerStart.setVisibility(View.GONE);
                    startScheduleTime.setBackgroundResource(R.drawable.time_box);
                }
                else if (timePickerStart.getVisibility() == View.GONE){
                    timePickerStart.setVisibility(View.VISIBLE);
                    startScheduleTime.setBackgroundResource(R.drawable.time_box_gray);
                    finishScheduleTime.setBackgroundResource(R.drawable.time_box);
                }
            }
        });

        finishScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timePickerStart.getVisibility() == View.VISIBLE) {
                    timePickerStart.setVisibility(View.GONE);
                    timePickerFinish.setVisibility(View.VISIBLE);

                    startScheduleTime.setBackgroundResource(R.drawable.time_box);
                    finishScheduleTime.setBackgroundResource(R.drawable.time_box_gray);
                }
                else if(timePickerFinish.getVisibility() == View.VISIBLE) {
                    timePickerFinish.setVisibility(View.GONE);
                    finishScheduleTime.setBackgroundResource(R.drawable.time_box);
                }
                else if (timePickerFinish.getVisibility() == View.GONE){
                    timePickerFinish.setVisibility(View.VISIBLE);
                    startScheduleTime.setBackgroundResource(R.drawable.time_box);
                    finishScheduleTime.setBackgroundResource(R.drawable.time_box_gray);
                }
            }
        });

        timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                String textHour = hour < 10 ? "0" + hour : "" + hour;
                String textMinute = minute < 10 ? "0" + minute : "" + minute;
                startScheduleTime.setText(textHour + ":" + textMinute);
                Log.d(TAG, "timePicker: " + textHour + ":" + textMinute);
            }
        });

        timePickerFinish.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                String textHour = hour < 10 ? "0" + hour : "" + hour;
                String textMinute = minute < 10 ? "0" + minute : "" + minute;
                finishScheduleTime.setText(textHour + ":" + textMinute);
                Log.d(TAG, "timePicker: " + textHour + ":" + textMinute);
            }
        });

        confirmAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Schedule newSchedule = new Schedule();

                newSchedule.setHead(scheduleTitle.getText().toString());
                if(newSchedule.getHead() == null || newSchedule.getHead().equals("")) {
                    showAddTitle(1);
                    return;
                }

                if(startScheduleTime.getText().toString() == null || startScheduleTime.getText().toString().equals("")) {
                    showAddTitle(2);
                    return;
                } else {
                    //Log.d(TAG, "Before: " + selected.toString());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String[] hourAndMinute = startScheduleTime.getText().toString().split(":");
                    selected.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
                    selected.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
                    selected.set(Calendar.SECOND, Integer.parseInt("00"));
                    String startDateTime = format.format(selected.getTime());
                    newSchedule.setStart(startDateTime);
                }

                if(finishScheduleTime.getText().toString() == null || finishScheduleTime.getText().toString().equals("")) {
                    showAddTitle(3);
                    return;
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String[] hourAndMinute = finishScheduleTime.getText().toString().split(":");
                    selected.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourAndMinute[0]));
                    selected.set(Calendar.MINUTE, Integer.parseInt(hourAndMinute[1]));
                    String finishDateTime = format.format(selected.getTime());
                    newSchedule.setEnd(finishDateTime);
                }

                int isOffline = status.isChecked() == true ? 2 : 1; // TOGGLE ON:자리비움(2), OFF:자리있음(1)
                newSchedule.setStatus(isOffline);

                newSchedule.setDetail(scheduleMemo.getText().toString());
                if(newSchedule.getDetail() == null || newSchedule.getDetail().equals("")) {
                    showAddTitle(5);
                    return;
                }

                Log.d(TAG, newSchedule.toString());

                retrofitAPI.reqCreateSchedule(Employee.getInstance().getEmpId().toString(), newSchedule).enqueue(new Callback<Schedule>() {
                    @Override
                    public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                        if (response.isSuccessful()) {
                            Schedule data = response.body();
                            if(data.getResultCode().equals("S101")) {
                                Log.d(TAG, newSchedule.getHead() + " 일정 추가 완료");
                                reqScheduleDate();
                            } else if(data.getResultCode().equals("S201")) {
                                Log.d(TAG, "일정 제목이 없습니다");
                            } else if(data.getResultCode().equals("S202")) {
                                Log.d(TAG, "일정의 정보가 부족합니다");
                            } else if(data.getResultCode().equals("S203")) {
                                Log.d(TAG, "일정의 시간이 부족합니다" + data.getResultCode());
                                Toast.makeText(requireContext(), "시작 시간은 종료 시간 이전이어야 합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            scheduleDialog.dismiss();
                        } else {
                            Log.e(TAG, "Failed to add schedule");
                        }
                    }

                    @Override
                    public void onFailure(Call<Schedule> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                    }
                });
            }
        });

        cancelAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleDialog.dismiss();
            }
        });
    }


    // 일정 수정 및 삭제
    private void updateSchedule(Schedule schedule) {

        Dialog updateDialog = new Dialog(requireContext(), R.style.Theme_Login);
        updateDialog.setContentView(R.layout.schedule_dialog_update);

        EditText schedule_title_update = updateDialog.findViewById(R.id.schedule_title_update);
        EditText schedule_memo_update = updateDialog.findViewById(R.id.schedule_memo_update);
        TextView start_schedule_time_update = updateDialog.findViewById(R.id.start_schedule_time_update);
        TextView finish_schedule_time_update = updateDialog.findViewById(R.id.finish_schedule_time_update);
        Switch status_update = updateDialog.findViewById(R.id.status_switch_update);

        TextView delete_schedule = updateDialog.findViewById(R.id.delete_schedule);
        TextView confirm_update_schedule = updateDialog.findViewById(R.id.confirm_update_schedule);
        // 기존 스케줄 정보를 다이얼로그에 표시
        // 1. 제목과 메모 표시
        schedule_title_update.setText(schedule.getHead());
        schedule_memo_update.setText(schedule.getDetail());

        // 2. 날짜 표시
        TextView startScheduleDayUpdate = updateDialog.findViewById(R.id.start_schedule_day_update);
        TextView finishScheduleDayUpdate = updateDialog.findViewById(R.id.finish_schedule_day_update);
        startScheduleDayUpdate.setText((selected.get(Calendar.MONTH) + 1) + "월 " + selected.get(Calendar.DATE) + "일 (" + getEachDay() + ")");
        finishScheduleDayUpdate.setText((selected.get(Calendar.MONTH) + 1) + "월 " + selected.get(Calendar.DATE) + "일 (" + getEachDay() + ")");

        // 3. 시간 표시
        String[] startTimeParts = schedule.getStart().split(" ")[1].split(":");
        String[] endTimeParts = schedule.getEnd().split(" ")[1].split(":");
        start_schedule_time_update.setText(startTimeParts[0] + ":" + startTimeParts[1]);
        finish_schedule_time_update.setText(endTimeParts[0] + ":" + endTimeParts[1]);

        // 4. 자리 비움 여부 표시
        status_update.setChecked(schedule.getStatus() == 2); // 자리 비움이면 true


        // 수정 시
        confirm_update_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = schedule_title_update.getText().toString();
                String newMemo = schedule_memo_update.getText().toString();
                String newStart = start_schedule_time_update.getText().toString();
                String newEnd = finish_schedule_time_update.getText().toString();
                boolean newStatus = status_update.isChecked();
                Log.d(TAG, "*******************" + newStart);
                Schedule updatedSchedule = new Schedule();
                updatedSchedule.setSchId(schedule.getSchId());
                updatedSchedule.setHead(newTitle);
                updatedSchedule.setDetail(newMemo);
                updatedSchedule.setStatus(newStatus ? 2 : 1);


                // Format start time
                // 수정 dialog에 띄워주기 위해서 시간의 형태를 00:00으로 표기
                // 하지만 Schedule 객체에는 2023-08-14 17:50:00.0 형태이므로 바꿔줘야 한다.
                // 입력이 1400 또는 14:00 두 형태로 입력될 수 있어서 예외처리 해준다.
                // 이를 활용해 시간이 덜 입력되면 터지는 것을 예방할 수도 있다.
                int startHour = 0;
                int startMinute = 0;
                int endHour = 0;
                int endMinute = 0;

                if (newStart.length() == 5) {
                    startHour = Integer.parseInt(newStart.substring(0, 2));
                    startMinute = Integer.parseInt(newStart.substring(3, 5));
                } else if (newStart.length() == 4){
                    startHour = Integer.parseInt(newStart.substring(0, 2));
                    startMinute = Integer.parseInt(newStart.substring(2, 4));
                } else {
                    Toast.makeText(requireContext(), "올바른 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newEnd.length() == 5) {
                    endHour = Integer.parseInt(newEnd.substring(0, 2));
                    endMinute = Integer.parseInt(newEnd.substring(3, 5));
                } else if (newEnd.length() == 4){
                    endHour = Integer.parseInt(newEnd.substring(0, 2));
                    endMinute = Integer.parseInt(newEnd.substring(2, 4));
                } else {
                    Toast.makeText(requireContext(), "올바른 시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                selected.set(Calendar.HOUR_OF_DAY, startHour);
                selected.set(Calendar.MINUTE, startMinute);
                selected.set(Calendar.SECOND, 0);
                String formattedStartDateTime = format.format(selected.getTime());

                selected.set(Calendar.HOUR_OF_DAY, endHour);
                selected.set(Calendar.MINUTE, endMinute);
                String formattedEndDateTime = format.format(selected.getTime());

                updatedSchedule.setStart(formattedStartDateTime);
                updatedSchedule.setEnd(formattedEndDateTime);

                Log.d(TAG, "UpdatedSchehule" + updatedSchedule);

                retrofitAPI.reqUpdateSchedule( Employee.getInstance().getEmpId().toString(), updatedSchedule.getSchId().toString(), updatedSchedule).enqueue(new Callback<Schedule>() {
                    @Override
                    public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "YES");
                            reqScheduleDate();
                        } else {
                            Log.e(TAG, "Failed to update schedule");
                        }
                        updateDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Schedule> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                    }
                });
            }
        });

        // 삭제 시
        delete_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckDialog deleteScheduleDialog = new CheckDialog(getContext(), R.drawable.ic_error_48px, "일정 삭제", "일정을 삭제하시겠습니까?");
                deleteScheduleDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
                    @Override
                    public void okBtnClicked(String btnName) {
                        reqDeleteSchedule(schedule);
                        reqScheduleDate();
                    }

                    @Override
                    public void noBtnClicked(String btnName) {

                    }
                });
                deleteScheduleDialog.show();

                updateDialog.dismiss();
            }
        });

        updateDialog.show();
    }

    public void reqDeleteSchedule(Schedule schedule) {
        retrofitAPI.reqDeleteSchedule(Employee.getInstance().getEmpId().toString(), schedule.getSchId().toString()).enqueue(new Callback<Schedule>() {
        @Override
        public void onResponse(Call<Schedule> call, Response<Schedule> response) {
            String resultCode = response.body().getResultCode();
            if(resultCode.equals("S101")) {
                ConfirmDialog deleteConfirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_check_circle_48px, "삭제 완료", "일정 삭제가 완료되었습니다");
                deleteConfirmDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
                    @Override
                    public void btnClicked(String btnName) {
                        reqScheduleDate();
                    }
                });

                deleteConfirmDialog.show();
            } else if(resultCode.equals("S201")) {
                Log.d(TAG, "Schedule ID is NOT Found");
            } else if(resultCode.equals("S202")) {
                Log.d(TAG, "Delete Schedule is ERROR");
            }
        }

        @Override
        public void onFailure(Call<Schedule> call, Throwable t) {

        }
    });
    }

    // 1: 제목 2: 시작시간 3: 종료시간 4: 자리비움 여부 5:내용
    public void showAddTitle(int flag) {
        ConfirmDialog addTitleDialog = new ConfirmDialog(this.getContext(), R.drawable.ic_error_48px, "일정 " + scheduleField[flag], "일정의 " + scheduleField[flag] + "이 없습니다\n추가해주세요");
        addTitleDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void btnClicked(String btnName) {

            }
        });
        addTitleDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void btnClicked(String btnName) {

            }
        });
        addTitleDialog.show();
    }
}
