package com.example.smartdesk.ui.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    Dialog scheduleDialog;
    CalendarView calendarView;
    private FragmentCalendarBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        calendarView = root.findViewById(R.id.calendarView);
//        calendarView.setBackground(R.style.Theme_Login);




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
        scheduleDialog = new Dialog(this.getContext());
        scheduleDialog.setContentView(R.layout.add_schedule_dialog);
        // 기존 dialog 배경 투명하게 만들어줌
        scheduleDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // dialog 화면을 fullscreen으로 띄워줌
        scheduleDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        scheduleDialog.show();

        TextView schedule_title = scheduleDialog.findViewById(R.id.schedule_title);
        TextView schedule_memo = scheduleDialog.findViewById(R.id.schedule_memo);
        TextView cancle_add_schedule = scheduleDialog.findViewById(R.id.cancle_add_schedule);
        TextView confirm_add_schedule = scheduleDialog.findViewById(R.id.confirm_add_schedule);


        schedule_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(requireContext());
                editText.setText(schedule_title.getText());

            }
        });

        cancle_add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { scheduleDialog.dismiss();}
        });

    }
}
